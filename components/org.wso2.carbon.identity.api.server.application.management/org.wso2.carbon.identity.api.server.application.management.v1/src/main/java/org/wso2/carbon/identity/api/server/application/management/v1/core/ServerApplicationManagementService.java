/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.api.server.application.management.v1.core;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationPatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationResponseModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthProtocolMetadata;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocolListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.Link;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ResidentApplication;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.ApiModelToServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.ApplicationBasicInfoToApiModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.ServiceProviderToApiModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.UpdateServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundAuthConfigToApiModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.PassiveSTSInboundFunctions;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.WSTrustInboundFunctions;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.custom.CustomInboundFunctions;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.oauth2.OAuthInboundFunctions;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.saml.SAMLInboundFunctions;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.provisioning.BuildProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.provisioning.UpdateProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementClientException;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.ApplicationBasicInfo;
import org.wso2.carbon.identity.application.common.model.ImportResponse;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.SpFileContent;
import org.wso2.carbon.identity.application.mgt.ApplicationConstants;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.core.util.IdentityUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage.INBOUND_NOT_CONFIGURED;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildNotImplementedError;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundFunctions.getInboundAuthKey;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundFunctions.getInboundAuthenticationRequestConfig;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundFunctions.rollbackInbound;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundFunctions.rollbackInbounds;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundFunctions.updateOrInsertInbound;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.OAUTH2;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.PASSIVE_STS;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.SAML2;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.WS_TRUST;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.Error.INVALID_REQUEST;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.Error.UNEXPECTED_SERVER_ERROR;

/**
 * Calls internal osgi services to perform server application management related operations.
 */
public class ServerApplicationManagementService {

    private static final Log log = LogFactory.getLog(ServerApplicationManagementService.class);

    private static final List<String> SEARCH_SUPPORTED_FIELDS = new ArrayList<>();
    private static final String APP_NAME = "name";

    // Filter related constants.
    private static final String FILTER_STARTS_WITH = "sw";
    private static final String FILTER_ENDS_WITH = "ew";
    private static final String FILTER_EQUALS = "eq";
    private static final String FILTER_CONTAINS = "co";
    private static final int DEFAULT_OFFSET = 0;

    static {
        SEARCH_SUPPORTED_FIELDS.add(APP_NAME);
    }

    @Autowired
    private ServerApplicationMetadataService applicationMetadataService;

    public ApplicationListResponse getAllApplications(Integer limit, Integer offset, String filter, String sortOrder,
                                                      String sortBy, String requiredAttributes) {

        handleNotImplementedCapabilities(sortOrder, sortBy, requiredAttributes);

        limit = validateAndGetLimit(limit);
        offset = validateAndGetOffset(offset);

        // Format the filter to a value that can be interpreted by the backend.
        String formattedFilter = buildFilter(filter);
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        String username = ContextLoader.getUsernameFromContext();
        try {
            int totalResults = getApplicationManagementService()
                    .getCountOfApplications(tenantDomain, username, formattedFilter);

            ApplicationBasicInfo[] filteredAppList = getApplicationManagementService()
                    .getApplicationBasicInfo(tenantDomain, username, formattedFilter, offset, limit);
            int resultsInCurrentPage = filteredAppList.length;

            return new ApplicationListResponse()
                    .totalResults(totalResults)
                    .startIndex(offset)
                    .count(resultsInCurrentPage)
                    .applications(getApplicationListItems(filteredAppList))
                    .links(buildLinks(limit, offset, filter, totalResults));

        } catch (IdentityApplicationManagementException e) {
            String msg = "Error listing applications of tenantDomain: " + tenantDomain;
            throw handleIdentityApplicationManagementException(e, msg);
        }
    }

    private int validateAndGetOffset(Integer offset) {

        if (offset != null && offset >= 0) {
            return offset;
        } else {
            return DEFAULT_OFFSET;
        }
    }

    private int validateAndGetLimit(Integer limit) {

        final int maximumItemPerPage = IdentityUtil.getMaximumItemPerPage();
        if (limit != null && limit > 0 && limit <= maximumItemPerPage) {
            return limit;
        } else {
            return IdentityUtil.getDefaultItemsPerPage();
        }
    }

    public ApplicationResponseModel getApplication(String applicationId) {

        ServiceProvider application = getServiceProvider(applicationId);
        return new ServiceProviderToApiModel().apply(application);
    }

    /**
     * Export an application identified by the applicationId, as an XML string.
     *
     * @param applicationId ID of the application to be exported.
     * @param exportSecrets If True, all hashed or encrypted secrets will also be exported.
     * @return XML string of the application.
     */
    public String exportApplication(String applicationId, Boolean exportSecrets) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            return getApplicationManagementService().exportSPApplicationFromAppID(
                    applicationId, exportSecrets, tenantDomain);
        } catch (IdentityApplicationManagementException e) {
            String msg = "Error exporting application with id: " + applicationId;
            throw handleIdentityApplicationManagementException(e, msg);
        }
    }

    /**
     * Create a new application by importing an XML configuration file.
     *
     * @param fileInputStream File to be imported as an input stream.
     * @param fileDetail      File details.
     * @return Unique identifier of the created application.
     */
    public String importApplication(InputStream fileInputStream, Attachment fileDetail) {

        try {
            SpFileContent spFileContent = new SpFileContent();
            spFileContent.setContent(IOUtils.toString(fileInputStream, StandardCharsets.UTF_8.name()));
            spFileContent.setFileName(fileDetail.getDataHandler().getName());

            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            String username = ContextLoader.getUsernameFromContext();
            ImportResponse importResponse = getApplicationManagementService().importSPApplication(
                    spFileContent, tenantDomain, username, false);
            if (importResponse.getResponseCode() == ImportResponse.CREATED) {
                ServiceProvider application =
                        getApplicationManagementService().getApplicationExcludingFileBasedSPs(
                                importResponse.getApplicationName(), tenantDomain);
                return application.getApplicationResourceId();
            } else {
                throw handleErrorResponse(importResponse);
            }
        } catch (IOException e) {
            throw Utils.buildServerError("Error importing application from XML file.", e);
        } catch (IdentityApplicationManagementException e) {
            throw handleIdentityApplicationManagementException(e, "Error importing application from XML file.");
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    private APIError handleErrorResponse(ImportResponse importResponse) {

        String msg = "Error importing application from XML file.";
        String description = null;
        if (ArrayUtils.isNotEmpty(importResponse.getErrors())) {
            description = importResponse.getErrors()[0];
        }

        return Utils.buildClientError(INVALID_REQUEST.getCode(), msg, description);
    }

    public String createApplication(ApplicationModel applicationModel, String template) {

        if (StringUtils.isNotBlank(template)) {
            throw buildNotImplementedError("Application creation with templates not supported.");
        }

        String username = ContextLoader.getUsernameFromContext();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();

        ServiceProvider application = new ApiModelToServiceProvider().apply(applicationModel);
        try {
            return getApplicationManagementService().createApplication(application, tenantDomain, username);
        } catch (IdentityApplicationManagementException e) {
            if (log.isDebugEnabled()) {
                log.debug("Error while creating application. Rolling back possibly created inbound config data.");
            }
            rollbackInbounds(getConfiguredInbounds(application));

            String msg = "Error creating application.";
            throw handleIdentityApplicationManagementException(e, msg);
        }
    }

    public void patchApplication(String applicationId, ApplicationPatchModel applicationPatchModel) {

        ServiceProvider appToUpdate = cloneApplication(applicationId);
        if (applicationPatchModel != null) {
            new UpdateServiceProvider().apply(appToUpdate, applicationPatchModel);
        }

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            String username = ContextLoader.getUsernameFromContext();
            getApplicationManagementService()
                    .updateApplicationByResourceId(applicationId, appToUpdate, tenantDomain, username);
        } catch (IdentityApplicationManagementException e) {
            String msg = "Error patching application with id: " + applicationId;
            throw handleIdentityApplicationManagementException(e, msg);
        }
    }

    public void deleteApplication(String applicationId) {

        String username = ContextLoader.getUsernameFromContext();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            getApplicationManagementService().deleteApplicationByResourceId(applicationId, tenantDomain, username);
        } catch (IdentityApplicationManagementException e) {
            String msg = "Error deleting application with id: " + applicationId;
            throw handleIdentityApplicationManagementException(e, msg);
        }
    }

    public ResidentApplication getResidentApplication() {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        return getResidentApplication(tenantDomain);
    }

    public ResidentApplication updateResidentApplication(ProvisioningConfiguration provisioningConfig) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            ServiceProvider application = getResidentSp(tenantDomain);

            String residentSpResourceId = application.getApplicationResourceId();
            ServiceProvider applicationToUpdate = cloneApplication(residentSpResourceId);

            // Add provisioning configs to resident SP.
            if (provisioningConfig != null) {
                new UpdateProvisioningConfiguration().apply(applicationToUpdate, provisioningConfig);
            }

            updateServiceProvider(residentSpResourceId, applicationToUpdate);
            return getResidentApplication(tenantDomain);
        } catch (IdentityApplicationManagementException e) {
            String msg = "Error updating resident application of tenantDomain: " + tenantDomain;
            throw handleIdentityApplicationManagementException(e, msg);
        }
    }

    private ServiceProvider getResidentSp(String tenantDomain) throws IdentityApplicationManagementException {

        ServiceProvider application =
                getApplicationManagementService().getServiceProvider(ApplicationConstants.LOCAL_SP, tenantDomain);
        if (application == null) {
            throw Utils.buildServerError("Resident application cannot be found for tenantDomain: " + tenantDomain);
        }
        return application;
    }

    private ResidentApplication getResidentApplication(String tenantDomain) {

        try {
            ServiceProvider application = getResidentSp(tenantDomain);
            ProvisioningConfiguration provisioningConfig = new BuildProvisioningConfiguration().apply(application);
            return new ResidentApplication().provisioningConfigurations(provisioningConfig);
        } catch (IdentityApplicationManagementException e) {
            String msg = "Error retrieving resident application of tenantDomain: " + tenantDomain;
            throw handleIdentityApplicationManagementException(e, msg);
        }
    }

    public void deleteOAuthInbound(String applicationId) {

        deleteInbound(applicationId, OAUTH2);
    }

    public void deleteSAMLInbound(String applicationId) {

        deleteInbound(applicationId, StandardInboundProtocols.SAML2);
    }

    public void deletePassiveStsInbound(String applicationId) {

        deleteInbound(applicationId, StandardInboundProtocols.PASSIVE_STS);
    }

    public void deleteWSTrustInbound(String applicationId) {

        deleteInbound(applicationId, StandardInboundProtocols.WS_TRUST);
    }

    public void deleteCustomInbound(String applicationId, String customInboundType) {

        deleteInbound(applicationId, customInboundType);
    }

    public OpenIDConnectConfiguration getInboundOAuthConfiguration(String applicationId) {

        return getInbound(applicationId, OAUTH2, OAuthInboundFunctions::getOAuthConfiguration);
    }

    public SAML2ServiceProvider getInboundSAMLConfiguration(String applicationId) {

        return getInbound(applicationId, SAML2, SAMLInboundFunctions::getSAML2ServiceProvider);
    }

    public PassiveStsConfiguration getPassiveStsConfiguration(String applicationId) {

        return getInbound(applicationId, PASSIVE_STS, PassiveSTSInboundFunctions::getPassiveSTSConfiguration);
    }

    public WSTrustConfiguration getWSTrustConfiguration(String applicationId) {

        return getInbound(applicationId, WS_TRUST, WSTrustInboundFunctions::getWSTrustConfiguration);
    }

    public CustomInboundProtocolConfiguration getCustomInboundConfiguration(String applicationId, String inboundType) {

        if (isUnknownInboundType(inboundType)) {
            throw Utils.buildBadRequestError("Unknown inbound type: " + inboundType);
        }

        return getInbound(applicationId, inboundType, CustomInboundFunctions::getCustomInbound);
    }

    private boolean isUnknownInboundType(String inboundType) {

        List<AuthProtocolMetadata> inboundProtocols = applicationMetadataService.getInboundProtocols(true);
        return inboundProtocols.stream()
                .noneMatch(metadata -> StringUtils.equals(metadata.getName(), inboundType));
    }

    public List<InboundProtocolListItem> getInboundProtocols(String applicationId) {

        ServiceProvider serviceProvider = getServiceProvider(applicationId);
        return new InboundAuthConfigToApiModel().apply(serviceProvider);
    }

    public void putInboundOAuthConfiguration(String applicationId, OpenIDConnectConfiguration oidcConfigModel) {

        putInbound(applicationId, oidcConfigModel, OAuthInboundFunctions::putOAuthInbound);
    }

    public void putInboundSAMLConfiguration(String applicationId, SAML2Configuration saml2Configuration) {

        putInbound(applicationId, saml2Configuration, SAMLInboundFunctions::putSAMLInbound);
    }

    public void putInboundPassiveSTSConfiguration(String applicationId,
                                                  PassiveStsConfiguration passiveStsConfiguration) {

        putInbound(applicationId, passiveStsConfiguration, PassiveSTSInboundFunctions::putPassiveSTSInbound);
    }

    public void putInboundWSTrustConfiguration(String applicationId, WSTrustConfiguration wsTrustConfiguration) {

        putInbound(applicationId, wsTrustConfiguration, WSTrustInboundFunctions::putWSTrustConfiguration);
    }

    public void updateCustomInbound(String applicationId, String inboundType,
                                    CustomInboundProtocolConfiguration customInbound) {

        if (isUnknownInboundType(inboundType)) {
            throw Utils.buildBadRequestError("Unknown inbound type: " + inboundType);
        }

        customInbound.setName(inboundType);
        putInbound(applicationId, customInbound, CustomInboundFunctions::putCustomInbound);
    }

    private <T> T getInbound(String applicationId,
                             String inboundType,
                             Function<InboundAuthenticationRequestConfig, T> getInboundApiModelFunction) {

        InboundAuthenticationRequestConfig inboundAuthenticationRequestConfig =
                getInboundAuthRequestConfig(applicationId, inboundType);
        // Apply the function and convert the authentication request config to API model.
        return getInboundApiModelFunction.apply(inboundAuthenticationRequestConfig);
    }

    public OpenIDConnectConfiguration regenerateOAuthApplicationSecret(String applicationId) {

        InboundAuthenticationRequestConfig oauthInbound = getInboundAuthRequestConfig(applicationId, OAUTH2);
        String clientId = oauthInbound.getInboundAuthKey();
        return OAuthInboundFunctions.regenerateClientSecret(clientId);
    }

    public void revokeOAuthClient(String applicationId) {

        InboundAuthenticationRequestConfig oauthInbound = getInboundAuthRequestConfig(applicationId, OAUTH2);
        String clientId = oauthInbound.getInboundAuthKey();
        OAuthInboundFunctions.revokeOAuthClient(clientId);
    }

    private InboundAuthenticationRequestConfig getInboundAuthRequestConfig(String applicationId, String inboundType) {

        ServiceProvider application = getServiceProvider(applicationId);
        // Extract the inbound authentication request config for the given inbound type.
        InboundAuthenticationRequestConfig inboundAuthenticationRequestConfig =
                getInboundAuthenticationRequestConfig(application, inboundType);

        if (inboundAuthenticationRequestConfig == null) {
            // This means the inbound is not configured for the particular app.
            throw buildClientError(INBOUND_NOT_CONFIGURED, inboundType, applicationId);
        }
        return inboundAuthenticationRequestConfig;
    }

    private void deleteInbound(String applicationId, String inboundType) {

        ServiceProvider appToUpdate = cloneApplication(applicationId);
        InboundAuthenticationConfig inboundAuthConfig = appToUpdate.getInboundAuthenticationConfig();

        if (ArrayUtils.isNotEmpty(inboundAuthConfig.getInboundAuthenticationRequestConfigs())) {
            // Remove the deleted inbound type by filtering it out of the available inbounds and doing an update.
            InboundAuthenticationRequestConfig[] filteredInbounds =
                    Arrays.stream(inboundAuthConfig.getInboundAuthenticationRequestConfigs())
                            .filter(inbound -> !StringUtils.equals(inboundType, inbound.getInboundAuthType()))
                            .toArray(InboundAuthenticationRequestConfig[]::new);

            appToUpdate.getInboundAuthenticationConfig().setInboundAuthenticationRequestConfigs(filteredInbounds);
            updateServiceProvider(applicationId, appToUpdate);
        }
    }

    private ServiceProvider cloneApplication(String applicationId) {

        ServiceProvider originalSp = getServiceProvider(applicationId);
        return Utils.deepCopyApplication(originalSp);
    }

    private ServiceProvider getServiceProvider(String applicationId) {

        ServiceProvider application;
        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            application = getApplicationManagementService().getApplicationByResourceId(applicationId, tenantDomain);
            if (application == null) {
                throw buildClientError(ErrorMessage.APPLICATION_NOT_FOUND, applicationId, tenantDomain);
            }
        } catch (IdentityApplicationManagementException e) {
            String msg = "Error retrieving application with id: " + applicationId;
            throw handleIdentityApplicationManagementException(e, msg);
        }
        return application;
    }

    private List<InboundAuthenticationRequestConfig> getConfiguredInbounds(ServiceProvider app) {

        if (app.getInboundAuthenticationConfig() != null &&
                app.getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs() != null) {
            return Arrays.asList(app.getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs());
        }
        return Collections.emptyList();
    }

    private List<Link> buildLinks(int limit, int currentOffset, String filter, int totalResultsFromSearch) {

        // TODO: prev and next
        return new ArrayList<>();
    }

    private List<ApplicationListItem> getApplicationListItems(ApplicationBasicInfo[] allApplicationBasicInfo) {

        return Arrays.stream(allApplicationBasicInfo)
                .map(new ApplicationBasicInfoToApiModel())
                .collect(Collectors.toList());
    }

    private String buildFilter(String filter) {

        if (StringUtils.isNotBlank(filter)) {
            String[] filterArgs = filter.split("\\s+");
            if (filterArgs.length == 3) {
                String searchField = filterArgs[0];
                if (SEARCH_SUPPORTED_FIELDS.contains(searchField)) {
                    String searchOperation = filterArgs[1];
                    String searchValue = filterArgs[2];
                    return generateFilterStringForBackend(searchField, searchOperation, searchValue);
                } else {
                    throw buildClientError(ErrorMessage.UNSUPPORTED_FILTER_ATTRIBUTE, searchField);
                }

            } else {
                throw buildClientError(ErrorMessage.INVALID_FILTER_FORMAT);
            }
        } else {
            return null;
        }
    }

    private String generateFilterStringForBackend(String searchField, String searchOperation, String searchValue) {

        // We do not have support for searching any fields other than the name. Therefore we simply format the search
        // value based on the search operation.
        String formattedFilter;
        switch (searchOperation) {
            case FILTER_STARTS_WITH:
                formattedFilter = searchValue + "*";
                break;
            case FILTER_ENDS_WITH:
                formattedFilter = "*" + searchValue;
                break;
            case FILTER_EQUALS:
                formattedFilter = searchValue;
                break;
            case FILTER_CONTAINS:
                formattedFilter = "*" + searchValue + "*";
                break;
            default:
                throw buildClientError(ErrorMessage.INVALID_FILTER_OPERATION, searchOperation);
        }

        return formattedFilter;
    }

    /**
     * Create or replace the provided inbound configuration.
     *
     * @param <I>               Inbound API model
     * @param applicationId     Unique id of the app
     * @param inboundApiModel   Inbound API model to be created or replaced
     * @param getUpdatedInbound A function that takes the inbound API model and application as input and provides
     *                          updated inbound details.
     */
    private <I> void putInbound(String applicationId,
                                I inboundApiModel,
                                BiFunction<ServiceProvider, I, InboundAuthenticationRequestConfig> getUpdatedInbound) {

        // We need a cloned copy of the Service Provider so that we changes we do not make cache dirty.
        ServiceProvider appToUpdate = cloneApplication(applicationId);
        // Update the service provider with the inbound configuration.
        InboundAuthenticationRequestConfig updatedInbound = getUpdatedInbound.apply(appToUpdate, inboundApiModel);
        // Add the updated inbound details
        updateOrInsertInbound(appToUpdate, updatedInbound);

        try {
            // Do the service provider update.
            updateServiceProvider(applicationId, appToUpdate);
        } catch (APIError error) {
            if (log.isDebugEnabled()) {
                log.debug("Error while updating application: " + applicationId + ". Attempting to rollback possible " +
                        "inbound configurations created before the update.");
            }
            doRollback(applicationId, updatedInbound);
            throw error;
        }
    }

    private void doRollback(String applicationId, InboundAuthenticationRequestConfig updatedInbound) {

        ServiceProvider serviceProvider = getServiceProvider(applicationId);
        // Current inbound key. This will give us an idea whether updatedInbound was added or updated.
        String inboundAuthKey = getInboundAuthKey(serviceProvider, updatedInbound.getInboundAuthType());
        if (inboundAuthKey == null) {
            // This means the application did not have any associated inbound before. So the updated inbound
            // could have been created before the update. Attempt to rollback by creating any inbound configs created.
            if (log.isDebugEnabled()) {
                String inboundType = updatedInbound.getInboundAuthType();
                log.debug("Removing inbound data related to inbound type: " + inboundType + " of application: "
                        + applicationId + " as part of rollback.");
            }
            rollbackInbound(updatedInbound);
        }
    }

    private void updateServiceProvider(String applicationId, ServiceProvider updatedApplication) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            String username = ContextLoader.getUsernameFromContext();

            getApplicationManagementService().updateApplicationByResourceId(
                    applicationId, updatedApplication, tenantDomain, username);
        } catch (IdentityApplicationManagementException e) {
            String msg = "Error updating application with id: " + applicationId;
            throw handleIdentityApplicationManagementException(e, msg);
        }
    }

    private void handleNotImplementedCapabilities(String sortOrder, String sortBy, String requiredAttributes) {

        ErrorMessage errorEnum = null;
        if (sortBy != null || sortOrder != null) {
            errorEnum = ErrorMessage.SORTING_NOT_IMPLEMENTED;
        } else if (requiredAttributes != null) {
            errorEnum = ErrorMessage.ATTRIBUTE_FILTERING_NOT_IMPLEMENTED;
        }

        if (errorEnum != null) {
            throw Utils.buildServerError(errorEnum.getCode(), errorEnum.getMessage(), errorEnum.getDescription());
        }
    }

    private ApplicationManagementService getApplicationManagementService() {

        return ApplicationManagementServiceHolder.getApplicationManagementService();
    }

    private APIError handleIdentityApplicationManagementException(IdentityApplicationManagementException e,
                                                                  String msg) {

        if (e instanceof IdentityApplicationManagementClientException) {
            throw buildClientError(e, msg);
        }
        throw buildServerError(e, msg);
    }

    private APIError buildServerError(IdentityApplicationManagementException e, String message) {

        String errorCode = getErrorCode(e, UNEXPECTED_SERVER_ERROR.getCode());
        return Utils.buildServerError(errorCode, message, e.getMessage(), e);
    }

    private APIError buildClientError(IdentityApplicationManagementException e, String message) {

        String errorCode = getErrorCode(e, INVALID_REQUEST.getCode());
        return Utils.buildClientError(errorCode, message, e.getMessage());
    }

    private String getErrorCode(IdentityApplicationManagementException e, String defaultErrorCode) {

        return e.getErrorCode() != null ? e.getErrorCode() : defaultErrorCode;
    }

    private APIError buildClientError(ErrorMessage errorEnum, String... args) {

        String description = buildFormattedDescription(errorEnum.getDescription(), args);
        return Utils.buildClientError(errorEnum.getCode(), errorEnum.getMessage(), description);
    }

    private String buildFormattedDescription(String description, String... formatData) {

        if (formatData != null) {
            return String.format(description, formatData);
        } else {
            return description;
        }
    }
}
