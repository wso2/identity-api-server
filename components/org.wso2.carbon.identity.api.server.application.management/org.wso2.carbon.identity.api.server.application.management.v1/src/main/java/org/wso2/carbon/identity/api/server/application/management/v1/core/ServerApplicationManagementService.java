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
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationPatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationResponseModel;
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
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.PatchServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.ServiceProviderToApiModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.UpdateProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.CustomInboundUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundsToApiModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.OAuthInboundUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.PassiveSTSInboundUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.SAMLInboundUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.WSTrustInboundUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.provisioning.BuildProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.oauth2.OAuthConsumerAppToApiModel;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
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
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundUtils.rollbackInbounds;

/**
 * Calls internal osgi services to perform server application management related operations.
 */
public class ServerApplicationManagementService {

    private static final Log LOG = LogFactory.getLog(ServerApplicationManagementService.class);

    private static final List<String> SEARCH_SUPPORTED_FIELDS = new ArrayList<>();
    private static final String APP_NAME = "name";

    // Filter related constants.
    private static final String FILTER_STARTS_WITH = "sw";
    private static final String FILTER_ENDS_WITH = "ew";
    private static final String FILTER_EQUALS = "eq";
    private static final String FILTER_CONTAINS = "co";

    // TODO: should we read this from somewhere...
    private static final int DEFAULT_LIMIT = 30;
    private static final int DEFAULT_LIMIT_MAX = 50;

    static {
        SEARCH_SUPPORTED_FIELDS.add(APP_NAME);
    }

    public ApplicationListResponse getAllApplications(Integer limit, Integer offset, String filter, String sortOrder,
                                                      String sortBy, String requiredAttributes) {

        handleNotImplementedCapabilities(sortBy, sortOrder, requiredAttributes);

        // TODO: define a default pagination max limit for identity data..
        limit = (limit != null && limit > 0 && limit <= DEFAULT_LIMIT_MAX) ? limit : DEFAULT_LIMIT;
        offset = (offset != null && offset > 0) ? offset : 0;

        // Format the filter to a value that can be interpreted by the backend.
        String formattedFilter = buildFilter(filter);
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        String username = ContextLoader.getUsernameFromContext();
        try {
            int totalResultsFromFiltering = getApplicationManagementService()
                    .getCountOfApplications(tenantDomain, username, formattedFilter);

            ApplicationBasicInfo[] filteredAppList = getApplicationManagementService()
                    .getApplicationBasicInfo(tenantDomain, username, formattedFilter, offset, limit);
            int resultsInCurrentPage = filteredAppList.length;

            return new ApplicationListResponse()
                    .totalResults(totalResultsFromFiltering)
                    .startIndex(offset)
                    .count(resultsInCurrentPage)
                    .applications(getApplicationListItems(filteredAppList))
                    .links(buildLinks(limit, offset, filter, totalResultsFromFiltering));

        } catch (IdentityApplicationManagementException e) {
            String msg = "Error while listing application basic information in tenantDomain: " + tenantDomain;
            throw handleIdentityApplicationManagementException(e, msg);
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
        } catch (IdentityApplicationManagementClientException e) {
            throw buildClientError(e, ErrorMessage.ERROR_CODE_APPLICATION_NOT_FOUND);
        } catch (IdentityApplicationManagementException e) {
            throw buildServerError(e, "Error while retrieving application with id: " + applicationId);
        }
    }

    /**
     * Create a new application by importing an XML configuration file.
     *
     * @param fileInputStream File to be imported as an input stream.
     * @param fileDetail      File details.
     * @return An application model of the created application.
     */
    public ApplicationResponseModel importApplication(InputStream fileInputStream, Attachment fileDetail) {

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
                return new ServiceProviderToApiModel().apply(application);
            } else {
                throw buildApiError(ErrorMessage.ERROR_IMPORTING_APPLICATION);
            }
        } catch (IOException e) {
            throw handleServerError(e, "Error while importing application from XML file.");
        } catch (IdentityApplicationManagementException e) {
            throw handleIdentityApplicationManagementException(e, "Error while importing application from XML file.");
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    public ApplicationResponseModel createApplication(ApplicationModel applicationModel, String template) {

        if (StringUtils.isNotBlank(template)) {
            throw buildApiError(Response.Status.NOT_IMPLEMENTED, "Application creation with templates not supported.");
        }

        String username = ContextLoader.getUsernameFromContext();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();

        ServiceProvider application = new ApiModelToServiceProvider().apply(applicationModel);
        try {
            ServiceProvider createdApp = getApplicationManagementService()
                    .createApplication(application, tenantDomain, username);
            return new ServiceProviderToApiModel().apply(createdApp);
        } catch (IdentityApplicationManagementException e) {
            rollbackInbounds(getInboundAuthenticationRequestConfigs(application));
            String msg = "Error while creating application with name '%s' in tenantDomain: %s.";
            msg = String.format(msg, applicationModel.getName(), tenantDomain);
            throw handleIdentityApplicationManagementException(e, msg);
        }
    }

    public ApplicationResponseModel patchApplication(String applicationId,
                                                     ApplicationPatchModel applicationPatchModel) {

        ServiceProvider appToUpdate = getClonedServiceProvider(applicationId);
        Utils.updateApplication(appToUpdate, applicationPatchModel, new PatchServiceProvider());

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        String username = ContextLoader.getUsernameFromContext();
        try {
            getApplicationManagementService()
                    .updateApplicationByResourceId(applicationId, appToUpdate, tenantDomain, username);

            ServiceProvider updatedApp =
                    getApplicationManagementService().getApplicationByResourceId(applicationId, tenantDomain);
            return new ServiceProviderToApiModel().apply(updatedApp);
        } catch (IdentityApplicationManagementException e) {
            throw handleIdentityApplicationManagementException(e, "Error while patching application: " + applicationId);
        }
    }

    public void deleteApplication(String applicationId) {

        String username = ContextLoader.getUsernameFromContext();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            getApplicationManagementService().deleteApplicationByResourceId(applicationId, tenantDomain, username);
        } catch (IdentityApplicationManagementException e) {
            String msg = "Error while deleting application with id: " + applicationId;
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
            ServiceProvider applicationToUpdate = getClonedServiceProvider(residentSpResourceId);

            // Add provisioning configs to resident SP.
            Utils.updateApplication(applicationToUpdate, provisioningConfig, new UpdateProvisioningConfiguration());
            updateServiceProvider(residentSpResourceId, applicationToUpdate);
            return getResidentApplication(tenantDomain);
        } catch (IdentityApplicationManagementException e) {
            String msg = "Error while retrieving resident application of tenantDomain: " + tenantDomain;
            throw handleIdentityApplicationManagementException(e, msg);
        }
    }

    private ServiceProvider getResidentSp(String tenantDomain) throws IdentityApplicationManagementException {

        ServiceProvider application =
                getApplicationManagementService().getServiceProvider(ApplicationConstants.LOCAL_SP, tenantDomain);
        if (application == null) {
            throw Utils.buildServerErrorResponse("Resident application cannot be found for tenantDomain: " +
                    tenantDomain);
        }
        return application;
    }

    private ResidentApplication getResidentApplication(String tenantDomain) {

        try {
            ServiceProvider application = getResidentSp(tenantDomain);
            ProvisioningConfiguration provisioningConfig = new BuildProvisioningConfiguration().apply(application);
            return new ResidentApplication().provisioningConfigurations(provisioningConfig);
        } catch (IdentityApplicationManagementException e) {
            String msg = "Error while retrieving resident application of tenantDomain: " + tenantDomain;
            throw handleIdentityApplicationManagementException(e, msg);
        }
    }

    public void deleteOAuthInbound(String applicationId) {

        deleteInbound(applicationId, StandardInboundProtocols.OAUTH2);
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

        ServiceProvider serviceProvider = getServiceProvider(applicationId);
        return InboundUtils.getOAuthInbound(serviceProvider);
    }

    public SAML2ServiceProvider getInboundSAMLConfiguration(String applicationId) {

        ServiceProvider serviceProvider = getServiceProvider(applicationId);
        return InboundUtils.getSAMLInbound(serviceProvider);
    }

    public PassiveStsConfiguration getPassiveStsConfiguration(String applicationId) {

        ServiceProvider serviceProvider = getServiceProvider(applicationId);
        return InboundUtils.getPassiveSTSInbound(serviceProvider);
    }

    public WSTrustConfiguration getWSTrustConfiguration(String applicationId) {

        ServiceProvider serviceProvider = getServiceProvider(applicationId);
        return InboundUtils.getWSTrustInbound(serviceProvider);
    }

    public CustomInboundProtocolConfiguration getCustomInboundConfiguration(String applicationId,
                                                                            String inboundType) {

        ServiceProvider serviceProvider = getServiceProvider(applicationId);
        return InboundUtils.getCustomInbound(serviceProvider, inboundType);
    }

    public OpenIDConnectConfiguration regenerateOAuthApplicationSecret(String applicationId) {

        OpenIDConnectConfiguration inboundOAuthConfiguration = getInboundOAuthConfiguration(applicationId);
        if (inboundOAuthConfiguration == null) {
            // TODO: improve error code.
            throw buildClientError(ErrorMessage.ERROR_INBOUND_PROTOCOL_NOT_FOUND);
        } else {
            String clientId = inboundOAuthConfiguration.getClientId();
            try {
                OAuthConsumerAppDTO oAuthConsumerAppDTO = ApplicationManagementServiceHolder.getOAuthAdminService()
                        .updateAndRetrieveOauthSecretKey(clientId);
                return new OAuthConsumerAppToApiModel().apply(oAuthConsumerAppDTO);
            } catch (IdentityOAuthAdminException e) {
                throw Utils.buildServerErrorResponse(e, "Error while regenerating client secret of oauth application.");
            }
        }
    }

    private void deleteInbound(String applicationId, String inboundType) {

        ServiceProvider serviceProvider = getClonedServiceProvider(applicationId);
        InboundAuthenticationConfig inboundAuthConfig = serviceProvider.getInboundAuthenticationConfig();

        if (ArrayUtils.isNotEmpty(inboundAuthConfig.getInboundAuthenticationRequestConfigs())) {
            // Remove the delete inbound configuration.
            InboundAuthenticationRequestConfig[] filteredInbounds =
                    Arrays.stream(inboundAuthConfig.getInboundAuthenticationRequestConfigs())
                            .filter(inbound -> !StringUtils.equals(inboundType, inbound.getInboundAuthType()))
                            .toArray(InboundAuthenticationRequestConfig[]::new);

            serviceProvider.getInboundAuthenticationConfig().setInboundAuthenticationRequestConfigs(filteredInbounds);

            try {
                String tenantDomain = ContextLoader.getTenantDomainFromContext();
                String username = ContextLoader.getUsernameFromContext();

                ApplicationManagementServiceHolder.getApplicationManagementService()
                        .updateApplicationByResourceId(applicationId, serviceProvider, tenantDomain, username);
            } catch (IdentityApplicationManagementException e) {
                String msg = "Error while deleting inbound type: " + inboundType + ".";
                throw handleIdentityApplicationManagementException(e, msg);
            }
        }
    }

    private ServiceProvider getClonedServiceProvider(String applicationId) {

        ServiceProvider originalSp = getServiceProvider(applicationId);
        return Utils.deepCopyApplication(originalSp);
    }

    private ServiceProvider getServiceProvider(String applicationId) {

        ServiceProvider application;
        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            application = getApplicationManagementService().getApplicationByResourceId(applicationId, tenantDomain);
            if (application == null) {
                throw buildApiError(ErrorMessage.ERROR_CODE_APPLICATION_NOT_FOUND, applicationId, tenantDomain);
            }
        } catch (IdentityApplicationManagementException e) {
            String msg = "Error while retrieving application with id: " + applicationId;
            throw handleIdentityApplicationManagementException(e, msg);
        }
        return application;
    }

    private List<InboundAuthenticationRequestConfig> getInboundAuthenticationRequestConfigs(ServiceProvider app) {

        if (app.getInboundAuthenticationConfig() != null) {
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
            String[] filterArgs = filter.split(" ");
            if (filterArgs.length == 3) {
                String searchField = filterArgs[0];
                if (SEARCH_SUPPORTED_FIELDS.contains(searchField)) {
                    String searchOperation = filterArgs[1];
                    String searchValue = filterArgs[2];
                    return generateFilterStringForBackend(searchField, searchOperation, searchValue);
                } else {
                    throw buildApiError(ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE, searchField);
                }

            } else {
                throw buildApiError(ErrorMessage.ERROR_CODE_INVALID_FILTER_FORMAT);
            }
        } else {
            return null;
        }
    }

    private String generateFilterStringForBackend(String searchField, String searchOperation, String searchValue) {

        // We do not have support for searching any fields other than the name. Therefore we simply format the search
        // value based on the search operation.
        // TODO: input validation for search value.
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
                throw buildApiError(ErrorMessage.ERROR_CODE_INVALID_FILTER_OPERATION, searchOperation);
        }

        return formattedFilter;
    }

    public List<InboundProtocolListItem> getInboundProtocols(String applicationId) {

        ServiceProvider serviceProvider = getServiceProvider(applicationId);
        return new InboundsToApiModel().apply(serviceProvider);
    }

    public OpenIDConnectConfiguration putInboundOAuthConfiguration(String applicationId,
                                                                   OpenIDConnectConfiguration oidcConfigModel) {

        return putInbound(applicationId, OAuthInboundUtils::putOAuthInbound, InboundUtils::getOAuthInbound,
                oidcConfigModel);
    }

    public SAML2ServiceProvider putInboundSAMLConfiguration(String applicationId,
                                                            SAML2Configuration saml2Configuration) {

        return putInbound(applicationId, SAMLInboundUtils::putSAMLInbound, InboundUtils::getSAMLInbound,
                saml2Configuration);
    }

    public PassiveStsConfiguration putInboundPassiveSTSConfiguration(String applicationId,
                                                                     PassiveStsConfiguration passiveStsConfiguration) {

        return putInbound(applicationId, PassiveSTSInboundUtils::putPassiveSTSInbound,
                InboundUtils::getPassiveSTSInbound, passiveStsConfiguration);
    }

    public WSTrustConfiguration putInboundWSTrustConfiguration(String applicationId,
                                                               WSTrustConfiguration wsTrustConfiguration) {

        return putInbound(applicationId, WSTrustInboundUtils::putWSTrustConfiguration,
                InboundUtils::getWSTrustInbound, wsTrustConfiguration);
    }

    public CustomInboundProtocolConfiguration updateCustomInbound(String applicationId,
                                                                  String inboundType,
                                                                  CustomInboundProtocolConfiguration customInbound) {

        return putInbound(applicationId, CustomInboundUtils::putCustomInbound,
                (application) -> InboundUtils.getCustomInbound(application, inboundType), customInbound);
    }

    /**
     * Create or replace the provided inbound configuration.
     *
     * @param applicationId
     * @param updateInboundFunction
     * @param getInboundDetailsFunction
     * @param inboundConfiguration
     * @param <T>                       Inbound creation/update API model
     * @param <R>                       Inbound API model
     * @return Updated inbound details in the form of it's API model.
     */
    private <T, R> T putInbound(String applicationId,
                                BiConsumer<ServiceProvider, R> updateInboundFunction,
                                Function<ServiceProvider, T> getInboundDetailsFunction,
                                R inboundConfiguration) {

        ServiceProvider appToUpdate = getClonedServiceProvider(applicationId);
        // Update the service provider with the inbound configuration.
        updateInboundFunction.accept(appToUpdate, inboundConfiguration);
        // Do the service provider update.
        updateServiceProvider(applicationId, appToUpdate);

        ServiceProvider updatedApplication = getServiceProvider(applicationId);
        // Return the inbound details from updated service provider.
        return getInboundDetailsFunction.apply(updatedApplication);
    }

    private void updateServiceProvider(String applicationId, ServiceProvider updatedApplication) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            String username = ContextLoader.getUsernameFromContext();

            getApplicationManagementService().updateApplicationByResourceId(
                    applicationId, updatedApplication, tenantDomain, username);
        } catch (IdentityApplicationManagementException e) {
            throw handleIdentityApplicationManagementException(e, "Error while updating application.");
        }
    }

    private APIError buildApiError(Response.Status statusCode, String message) {

        ErrorResponse errorResponse = new ErrorResponse.Builder().withMessage(message).build();
        return new APIError(statusCode, errorResponse);
    }

    private APIError buildApiError(ErrorMessage errorEnum) {

        ErrorResponse errorResponse = buildErrorResponse(errorEnum);
        return new APIError(errorEnum.getHttpStatusCode(), errorResponse);
    }

    private APIError buildApiError(ErrorMessage errorEnum,
                                   String... errorContextData) {

        ErrorResponse errorResponse = buildErrorResponse(errorEnum, errorContextData);
        return new APIError(errorEnum.getHttpStatusCode(), errorResponse);
    }

    private ErrorResponse buildErrorResponse(ErrorMessage errorEnum,
                                             String... errorContextData) {

        return new ErrorResponse.Builder()
                .withCode(errorEnum.getCode())
                .withDescription(buildFormattedDescription(errorEnum.getDescription(), errorContextData))
                .withMessage(errorEnum.getMessage())
                .build(LOG, errorEnum.getDescription());
    }

    private String buildFormattedDescription(String description, String... formatData) {

        if (formatData != null) {
            return String.format(description, formatData);
        } else {
            return description;
        }
    }

    private void handleNotImplementedCapabilities(String sortOrder, String sortBy, String requiredAttributes) {

        ErrorMessage errorEnum = null;

        if (sortBy != null || sortOrder != null) {
            errorEnum = ErrorMessage.ERROR_CODE_SORTING_NOT_IMPLEMENTED;
        } else if (requiredAttributes != null) {
            errorEnum = ErrorMessage.ERROR_CODE_ATTRIBUTE_FILTERING_NOT_IMPLEMENTED;
        }

        if (errorEnum != null) {
            throw buildApiError(errorEnum);
        }
    }

    private ApplicationManagementService getApplicationManagementService() {

        return ApplicationManagementServiceHolder.getApplicationManagementService();
    }

    private APIError buildServerError(IdentityApplicationManagementException e, String message) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder();

        ErrorResponse errorResponse = builder
                .withCode(e.getErrorCode())
                .withMessage(message)
                .withDescription(e.getMessage())
                .build(LOG, e, message);

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, errorResponse);
    }

    private APIError handleServerError(Exception e, String message) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder();

        ErrorResponse errorResponse = builder
                .withMessage(e.getMessage())
                .build(LOG, e, message);

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, errorResponse);
    }

    private APIError buildClientError(IdentityApplicationManagementException e, String message) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder();

        ErrorResponse errorResponse = builder
                .withCode(e.getErrorCode())
                .withMessage(message)
                .withDescription(e.getMessage())
                .build(LOG, e.getMessage());

        Response.Status status = Response.Status.BAD_REQUEST;
        return new APIError(status, errorResponse);
    }

    private APIError buildClientError(Exception e, ErrorMessage errorEnum) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorEnum.getCode())
                .withDescription(e.getMessage())
                .withMessage(errorEnum.getMessage())
                .build(LOG, errorEnum.getDescription());
        return new APIError(errorEnum.getHttpStatusCode(), errorResponse);
    }

    private APIError buildClientError(ErrorMessage errorEnum) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorEnum.getCode())
                .withMessage(errorEnum.getMessage())
                .build(LOG, errorEnum.getDescription());
        return new APIError(errorEnum.getHttpStatusCode(), errorResponse);
    }

    private APIError handleIdentityApplicationManagementException(IdentityApplicationManagementException e,
                                                                  String msg) {

        if (e instanceof IdentityApplicationManagementClientException) {
            throw buildClientError(e, msg);
        }
        throw buildServerError(e, msg);
    }
}
