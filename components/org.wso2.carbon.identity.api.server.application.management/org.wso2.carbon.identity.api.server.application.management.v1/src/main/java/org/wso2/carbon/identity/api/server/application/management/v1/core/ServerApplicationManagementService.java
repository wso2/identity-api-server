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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.search.ConditionType;
import org.apache.cxf.jaxrs.ext.search.PrimitiveStatement;
import org.apache.cxf.jaxrs.ext.search.SearchCondition;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationPatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationResponseModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplateModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplatesList;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplatesListItem;
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
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.template.ApplicationTemplateApiModelToTemplate;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.template.TemplateToApplicationTemplate;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.template.TemplateToApplicationTemplateListItem;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.Util;
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
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.configuration.mgt.core.model.ResourceSearchBean;
import org.wso2.carbon.identity.configuration.mgt.core.search.ComplexCondition;
import org.wso2.carbon.identity.configuration.mgt.core.search.Condition;
import org.wso2.carbon.identity.configuration.mgt.core.search.PrimitiveCondition;
import org.wso2.carbon.identity.core.model.ExpressionNode;
import org.wso2.carbon.identity.core.model.FilterTreeBuilder;
import org.wso2.carbon.identity.core.model.Node;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.cors.mgt.core.CORSManagementService;
import org.wso2.carbon.identity.cors.mgt.core.constant.ErrorMessages;
import org.wso2.carbon.identity.cors.mgt.core.exception.CORSManagementServiceClientException;
import org.wso2.carbon.identity.cors.mgt.core.exception.CORSManagementServiceException;
import org.wso2.carbon.identity.cors.mgt.core.model.CORSOrigin;
import org.wso2.carbon.identity.template.mgt.TemplateManager;
import org.wso2.carbon.identity.template.mgt.TemplateMgtConstants;
import org.wso2.carbon.identity.template.mgt.exception.TemplateManagementClientException;
import org.wso2.carbon.identity.template.mgt.exception.TemplateManagementException;
import org.wso2.carbon.identity.template.mgt.model.Template;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.APPLICATION_MANAGEMENT_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage.APPLICATION_CREATION_WITH_TEMPLATES_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage.ERROR_APPLICATION_LIMIT_REACHED;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage.INBOUND_NOT_CONFIGURED;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildBadRequestError;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildNotImplementedError;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundFunctions.getInboundAuthKey;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundFunctions.getInboundAuthenticationRequestConfig;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundFunctions.rollbackInbound;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundFunctions.rollbackInbounds;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundFunctions.updateOrInsertInbound;
import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_RESOURCE_LIMIT_REACHED;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.OAUTH2;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.PASSIVE_STS;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.SAML2;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.WS_TRUST;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.Error.INVALID_REQUEST;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.Error.UNEXPECTED_SERVER_ERROR;
import static org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType.PrimitiveOperator.EQUALS;
import static org.wso2.carbon.identity.cors.mgt.core.constant.ErrorMessages.ERROR_CODE_INVALID_APP_ID;

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

    // WS-Trust related constants.
    private static final String WS_TRUST_TEMPLATE_ID = "061a3de4-8c08-4878-84a6-24245f11bf0e";
    private static final String STS_TEMPLATE_NOT_FOUND_MESSAGE = "Request template with id: %s could " +
            "not be found since the WS-Trust connector has not been configured.";

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
                    .startIndex(offset + 1)
                    .count(resultsInCurrentPage)
                    .applications(getApplicationListItems(filteredAppList))
                    .links(Util.buildPaginationLinks(limit, offset, totalResults, APPLICATION_MANAGEMENT_PATH_COMPONENT)
                            .entrySet()
                            .stream()
                            .map(link -> new Link().rel(link.getKey()).href(link.getValue()))
                            .collect(Collectors.toList()));

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

        return doImportApplication(fileInputStream, fileDetail, false);
    }

    /**
     * Create a new application by importing an XML configuration file.
     *
     * @param fileInputStream File to be imported as an input stream.
     * @param fileDetail      File details.
     * @return Unique identifier of the created application.
     */
    public String importApplicationForUpdate(InputStream fileInputStream, Attachment fileDetail) {

        return doImportApplication(fileInputStream, fileDetail, true);
    }

    private String doImportApplication(InputStream fileInputStream, Attachment fileDetail, boolean isAppUpdate) {

        try {
            SpFileContent spFileContent = buildSpFileContent(fileInputStream, fileDetail);

            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            String username = ContextLoader.getUsernameFromContext();

            ImportResponse importResponse = getApplicationManagementService()
                    .importSPApplication(spFileContent, tenantDomain, username, isAppUpdate);

            if (importResponse.getResponseCode() == ImportResponse.FAILED) {
                throw handleErrorResponse(importResponse);
            } else {
                return importResponse.getApplicationResourceId();
            }
        } catch (IOException e) {
            throw Utils.buildServerError("Error importing application from XML file.", e);
        } catch (IdentityApplicationManagementException e) {
            throw handleIdentityApplicationManagementException(e, "Error importing application from XML file.");
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    private SpFileContent buildSpFileContent(InputStream fileInputStream, Attachment fileDetail) throws IOException {

        SpFileContent spFileContent = new SpFileContent();
        spFileContent.setContent(IOUtils.toString(fileInputStream, StandardCharsets.UTF_8.name()));
        spFileContent.setFileName(fileDetail.getDataHandler().getName());
        return spFileContent;
    }

    private APIError handleErrorResponse(ImportResponse importResponse) {

        String errorCode = importResponse.getErrorCode() != null ?
                importResponse.getErrorCode() : INVALID_REQUEST.getCode();
        String msg = "Error importing application from XML file.";
        String description = null;
        if (ArrayUtils.isNotEmpty(importResponse.getErrors())) {
            description = importResponse.getErrors()[0];
        }

        return Utils.buildClientError(errorCode, msg, description);
    }

    public String createApplication(ApplicationModel applicationModel, String template) {

        if (StringUtils.isNotBlank(template)) {
            String errorCode = APPLICATION_CREATION_WITH_TEMPLATES_NOT_IMPLEMENTED.getCode();
            throw buildNotImplementedError(errorCode, "Application creation with templates not supported.");
        }

        /*
         * CORS adding step should be moved to the service layer and the validation also should happen there.
         * But for now we are handling the validation here.
         */
        if (applicationModel.getInboundProtocolConfiguration() != null &&
                applicationModel.getInboundProtocolConfiguration().getOidc() != null) {
            validateCORSOrigins(applicationModel.getInboundProtocolConfiguration().getOidc().getAllowedOrigins());
        }

        String username = ContextLoader.getUsernameFromContext();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();

        String applicationId = null;
        ServiceProvider application = new ApiModelToServiceProvider().apply(applicationModel);
        try {
            applicationId = getApplicationManagementService().createApplication(application, tenantDomain, username);
            if (applicationModel.getInboundProtocolConfiguration() != null &&
                    applicationModel.getInboundProtocolConfiguration().getOidc() != null) {
                OAuthInboundFunctions.updateCorsOrigins(applicationId, applicationModel
                        .getInboundProtocolConfiguration().getOidc());
            }
            return applicationId;
        } catch (IdentityApplicationManagementException e) {
            if (log.isDebugEnabled()) {
                log.debug("Error while creating application. Rolling back possibly created inbound config data.", e);
            }
            rollbackInbounds(getConfiguredInbounds(application));
            throw handleIdentityApplicationManagementException(e, "Error creating application.");
        } catch (CORSManagementServiceException e) {
            if (log.isDebugEnabled()) {
                log.debug("Error while updating CORS origins. Rolling back created application data.", e);
            }
            if (applicationId != null) {
                deleteApplication(applicationId);
            }
            if (ErrorMessages.ERROR_CODE_DUPLICATE_ORIGINS.getCode().equals(e.getErrorCode())) {
                throw buildClientError(e,
                        "Error creating application. Found duplicate allowed origin entries.");
            }
            throw buildClientError(e, "Error creating application. Allow CORS origins update failed.");
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
        CORSManagementService corsManagementService = ApplicationManagementServiceHolder.getCorsManagementService();
        try {
            // Delete CORS origins for OIDC Apps.
            List<CORSOrigin> existingCORSOrigins =
                    corsManagementService.getApplicationCORSOrigins(applicationId, tenantDomain);
            if (!CollectionUtils.isEmpty(existingCORSOrigins)) {
                ApplicationManagementServiceHolder.getCorsManagementService()
                        .deleteCORSOrigins(applicationId,
                                existingCORSOrigins.stream().map(CORSOrigin::getId).collect(Collectors.toList()),
                                tenantDomain);
            }

            // Delete Application.
            getApplicationManagementService().deleteApplicationByResourceId(applicationId, tenantDomain, username);
        } catch (IdentityApplicationManagementException e) {
            String msg = "Error deleting application with id: " + applicationId;
            throw handleIdentityApplicationManagementException(e, msg);
        } catch (CORSManagementServiceClientException e) {
            /*
             For not existing application scenarios the following error code will be returned. To preserve the behaviour
             we need to return 204.
             */
            if (ERROR_CODE_INVALID_APP_ID.getCode().equals(e.getErrorCode())) {
                log.error("Invalid application id: " + applicationId, e);
                return;
            }
            String msg = "Error while trying to remove CORS origins associated with the application: " + applicationId;
            throw Utils.buildClientError(e.getErrorCode(), msg, e.getMessage());
        } catch (CORSManagementServiceException e) {
            String msg = "Error while trying to remove CORS origins associated with the application: " + applicationId;
            throw Utils.buildServerError(msg, e);
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
            throw buildBadRequestError("Unknown inbound type: " + inboundType);
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

        /*
         * CORS updating step should be moved to the service layer and the validation also should happen there.
         * But for now we are handling the validation here.
         */
        validateCORSOrigins(oidcConfigModel.getAllowedOrigins());
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
            throw buildBadRequestError("Unknown inbound type: " + inboundType);
        }

        customInbound.setName(inboundType);
        putInbound(applicationId, customInbound, CustomInboundFunctions::putCustomInbound);
    }

    /**
     * Create a new template object.
     *
     * @param applicationTemplateModel applicationTemplateModel from which the template is created.
     * @return unique id of the newly created template.
     */
    public String createApplicationTemplate(ApplicationTemplateModel applicationTemplateModel) {

        Template template = new ApplicationTemplateApiModelToTemplate().apply(applicationTemplateModel);

        try {
            return getTemplateManager().addTemplate(template);
        } catch (TemplateManagementException e) {
            throw handleTemplateManagementException(e, "Error while adding the new application template.");
        }
    }

    /**
     * List all the application templates of the tenant.
     *
     * @param limit  maximum number of items to be returned.
     * @param offset number of records to skip for pagination.
     * @return ApplicationTemplatesList containing the list of templates.
     */
    public ApplicationTemplatesList listApplicationTemplates(Integer limit, Integer offset, SearchContext
            searchContext) {

        validatePaginationSupport(limit, offset);
        try {
            List<Template> templateList = getTemplateManager().listTemplates(TemplateMgtConstants.TemplateType
                    .APPLICATION_TEMPLATE.toString(), null, null, getSearchCondition
                    (TemplateMgtConstants.TemplateType.APPLICATION_TEMPLATE.toString(), ContextLoader
                            .getTenantDomainFromContext(), searchContext));
            List<ApplicationTemplatesListItem> applicationTemplateList = templateList.stream().map(new
                    TemplateToApplicationTemplateListItem()).collect(Collectors.toList());

            ApplicationTemplatesList applicationTemplates = new ApplicationTemplatesList();
            applicationTemplates.setTemplates(applicationTemplateList);
            return applicationTemplates;
        } catch (TemplateManagementException e) {
            throw handleTemplateManagementException(e, "Error while listing application templates.");
        }
    }

    /**
     * Retrieve search condition from @{SearchContext}.
     *
     * @param templateType  Template type.
     * @param tenantDomain  Tenant domain.
     * @param searchContext Search context.
     * @return Condition.
     */
    private Condition getSearchCondition(String templateType, String tenantDomain, SearchContext searchContext) {

        if (searchContext != null) {
            SearchCondition<ResourceSearchBean> searchCondition = searchContext.getCondition(ResourceSearchBean.class);
            if (searchCondition != null) {
                Condition result = buildSearchCondition(searchCondition);
                List<Condition> list = new ArrayList<>();
                Condition typeCondition = new PrimitiveCondition(ApplicationManagementConstants.TemplateProperties
                        .TEMPLATE_TYPE_KEY, EQUALS, templateType);
                Condition tenantCondition = new PrimitiveCondition(ApplicationManagementConstants.TemplateProperties
                        .TENANT_DOMAIN_KEY, EQUALS, tenantDomain);
                if (result instanceof ComplexCondition) {
                    list = ((ComplexCondition) result).getConditions();
                    list.add(typeCondition);
                    list.add(tenantCondition);
                } else if (result instanceof PrimitiveCondition) {
                    list.add(result);
                    list.add(typeCondition);
                    list.add(tenantCondition);
                }
                return new ComplexCondition(getComplexOperatorFromOdata(ConditionType.AND),
                        list);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Search condition parsed from the search expression is invalid.");
                }
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Cannot find a valid search context.");
            }
        }
        return null;
    }

    private Condition buildSearchCondition(SearchCondition searchCondition) {

        if (!(searchCondition.getStatement() == null)) {
            PrimitiveStatement primitiveStatement = searchCondition.getStatement();

            if (ApplicationManagementConstants.TemplateProperties.SEARCH_KEYS.contains(primitiveStatement.getProperty
                    ())) {
                List<Condition> list = new ArrayList<>();
                Condition attrKeyCondition = new PrimitiveCondition(ApplicationManagementConstants.TemplateProperties
                        .ATTR_KEY, EQUALS, primitiveStatement.getProperty());
                Condition attrValueCondition = new PrimitiveCondition(ApplicationManagementConstants
                        .TemplateProperties.ATTR_VALUE, getPrimitiveOperatorFromOdata(primitiveStatement.getCondition
                        ()), primitiveStatement.getValue());
                list.add(attrKeyCondition);
                list.add(attrValueCondition);
                return new ComplexCondition(getComplexOperatorFromOdata(ConditionType.AND),
                        list);
            } else if (ApplicationManagementConstants.TemplateProperties.SEARCH_KEY_NAME.equals(primitiveStatement
                    .getProperty())) {
                return new PrimitiveCondition(ApplicationManagementConstants.TemplateProperties
                        .SEARCH_KEY_NAME_INTERNAL, getPrimitiveOperatorFromOdata(primitiveStatement
                        .getCondition()), primitiveStatement.getValue());
            } else {
                throw buildClientError(ApplicationManagementConstants.ErrorMessage
                        .ERROR_CODE_ERROR_INVALID_SEARCH_FILTER, null);
            }
        } else {
            List<Condition> conditions = new ArrayList<>();
            for (Object condition : searchCondition.getSearchConditions()) {
                Condition buildCondition = buildSearchCondition((SearchCondition) condition);
                conditions.add(buildCondition);
            }
            return new ComplexCondition(getComplexOperatorFromOdata(searchCondition.getConditionType()), conditions);
        }
    }

    private org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType.PrimitiveOperator
    getPrimitiveOperatorFromOdata(org.apache.cxf.jaxrs.ext.search.ConditionType odataConditionType) {

        org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType.PrimitiveOperator
                primitiveConditionType;
        switch (odataConditionType) {
            case EQUALS:
                primitiveConditionType = EQUALS;
                break;
            case GREATER_OR_EQUALS:
                primitiveConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant
                        .ConditionType.PrimitiveOperator.GREATER_OR_EQUALS;
                break;
            case LESS_OR_EQUALS:
                primitiveConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant
                        .ConditionType.PrimitiveOperator.LESS_OR_EQUALS;
                break;
            case GREATER_THAN:
                primitiveConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant
                        .ConditionType.PrimitiveOperator.GREATER_THAN;
                break;
            case NOT_EQUALS:
                primitiveConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant
                        .ConditionType.PrimitiveOperator.NOT_EQUALS;
                break;
            case LESS_THAN:
                primitiveConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant
                        .ConditionType.PrimitiveOperator.LESS_THAN;
                break;
            default:
                throw buildClientError(ErrorMessage.INVALID_FILTER_OPERATION, odataConditionType.name());
        }
        return primitiveConditionType;
    }

    private org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType.ComplexOperator
    getComplexOperatorFromOdata(org.apache.cxf.jaxrs.ext.search.ConditionType odataConditionType) {

        org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType.ComplexOperator
                complexConditionType;
        switch (odataConditionType) {
            case OR:
                complexConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType
                        .ComplexOperator.OR;
                break;
            case AND:
                complexConditionType = org.wso2.carbon.identity.configuration.mgt.core.search.constant.ConditionType
                        .ComplexOperator.AND;
                break;
            default:
                throw buildClientError(ErrorMessage.INVALID_FILTER_OPERATION, odataConditionType.name());
        }
        return complexConditionType;
    }

    /**
     * Retrieve the application template given the template id.
     *
     * @param templateId id of the template.
     * @return ApplicationTemplateModel
     */
    public ApplicationTemplateModel getApplicationTemplateById(String templateId) {

        try {
            return new TemplateToApplicationTemplate().apply(getTemplateManager().getTemplateById(templateId));
        } catch (TemplateManagementException e) {
            if (TemplateMgtConstants.ErrorMessages.ERROR_CODE_TEMPLATE_NOT_FOUND.getCode().equals(e.getErrorCode())) {
                throw handleTemplateNotFoundException(e);
            }
            String errorMessage = "Error while retrieving the application template with id " + templateId + ".";
            throw handleTemplateManagementException(e, errorMessage);
        }
    }

    /**
     * Delete the application template given the template id.
     *
     * @param templateId id of the template.
     */
    public void deleteApplicationTemplateById(String templateId) {

        try {
            getTemplateManager().deleteTemplateById(templateId);
        } catch (TemplateManagementException e) {
            if (TemplateMgtConstants.ErrorMessages.ERROR_CODE_TEMPLATE_NOT_FOUND.getCode().equals(e.getErrorCode())) {
                throw handleTemplateNotFoundException(e);
            }
            String errorMessage = "Error while deleting the application template with id " + templateId + ".";
            throw handleTemplateManagementException(e, errorMessage);
        }
    }

    /**
     * Update the application template given the template id and the new ApplicationTemplateModel.
     *
     * @param templateId id of the template.
     * @param model      template object to be replaced.
     */
    public void updateApplicationTemplateById(String templateId, ApplicationTemplateModel model) {

        try {
            getTemplateManager().updateTemplateById(templateId,
                    new ApplicationTemplateApiModelToTemplate().apply(model));
        } catch (TemplateManagementException e) {
            if (TemplateMgtConstants.ErrorMessages.ERROR_CODE_TEMPLATE_NOT_FOUND.getCode().equals(e.getErrorCode())) {
                throw handleTemplateNotFoundException(e);
            }
            String errorMessage = "Error while updating the application template with id " + templateId + ".";
            throw handleTemplateManagementException(e, errorMessage);
        }
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

        ServiceProvider appToUpdate;
        try {
            appToUpdate = cloneApplication(applicationId);
        } catch (APIError e) {
            if (ErrorMessage.APPLICATION_NOT_FOUND.getCode().equals(e.getCode())) {
                // Ignoring the delete operation and return 204 response code, since the resource does not exist.
                return;
            }
            throw e;
        }
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

        // Delete the associated CORS origins if the inboundType is oauth2.
        if (inboundType.equals(OAUTH2)) {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            CORSManagementService corsManagementService = ApplicationManagementServiceHolder.getCorsManagementService();
            try {
                List<CORSOrigin> existingCORSOrigins = corsManagementService
                        .getApplicationCORSOrigins(applicationId, tenantDomain);
                corsManagementService.deleteCORSOrigins(applicationId, existingCORSOrigins.stream()
                        .map(CORSOrigin::getId).collect(Collectors.toList()), tenantDomain);
            } catch (CORSManagementServiceException e) {
                log.error("Error while trying to remove CORS origins associated with the application.", e);
            }
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

    private List<ApplicationListItem> getApplicationListItems(ApplicationBasicInfo[] allApplicationBasicInfo) {

        return Arrays.stream(allApplicationBasicInfo)
                .map(new ApplicationBasicInfoToApiModel())
                .collect(Collectors.toList());
    }

    private String buildFilter(String filter) {
        if (StringUtils.isNotBlank(filter)) {
            try {

                FilterTreeBuilder filterTreeBuilder = new FilterTreeBuilder(filter);
                Node rootNode = filterTreeBuilder.buildTree();
                if (rootNode instanceof ExpressionNode) {
                    ExpressionNode expressionNode = (ExpressionNode) rootNode;
                    if (SEARCH_SUPPORTED_FIELDS.contains(expressionNode.getAttributeValue())) {
                        return generateFilterStringForBackend(expressionNode.getAttributeValue(), expressionNode
                                .getOperation(), expressionNode.getValue());
                    } else {
                        throw buildClientError(ErrorMessage.UNSUPPORTED_FILTER_ATTRIBUTE, expressionNode
                                .getAttributeValue());
                    }

                } else {
                    throw buildClientError(ErrorMessage.INVALID_FILTER_FORMAT);
                }
            } catch (IOException | IdentityException e) {
                throw buildClientError(ApplicationManagementConstants.ErrorMessage.INVALID_FILTER_FORMAT, null);
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
        // Current inbound key. This will give us an idea whether updatedInbound was newly added or not.
        String previousInboundKey = getInboundAuthKey(serviceProvider, updatedInbound.getInboundAuthType());
        String attemptedInboundKeyForUpdate = updatedInbound.getInboundAuthKey();
        if (!StringUtils.equals(previousInboundKey, attemptedInboundKeyForUpdate)) {
            // This means the application was updated with a newly created inbound. So the updated inbound details
            // could have been created before the update. Attempt to rollback by deleting any inbound configs created.
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

    private void validatePaginationSupport(Integer limit, Integer offset) {

        if (limit != null || offset != null) {
            ErrorMessage errorEnum = ErrorMessage.PAGINATED_LISTING_NOT_IMPLEMENTED;
            throw Utils.buildNotImplementedError(errorEnum.getCode(), errorEnum.getDescription());
        }
    }

    private ApplicationManagementService getApplicationManagementService() {

        return ApplicationManagementServiceHolder.getApplicationManagementService();
    }

    private TemplateManager getTemplateManager() {

        return ApplicationManagementServiceHolder.getTemplateManager();
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
        if (ERROR_CODE_RESOURCE_LIMIT_REACHED.equals(errorCode)) {
            return handleResourceLimitReached();
        }
        return Utils.buildClientError(errorCode, message, e.getMessage());
    }

    private APIError handleResourceLimitReached() {

        String code = ERROR_APPLICATION_LIMIT_REACHED.getCode();
        String message = ERROR_APPLICATION_LIMIT_REACHED.getMessage();
        String description = ERROR_APPLICATION_LIMIT_REACHED.getDescription();
        return Utils.buildForbiddenError(code, message, description);
    }

    private String getErrorCode(IdentityApplicationManagementException e, String defaultErrorCode) {

        return e.getErrorCode() != null ? e.getErrorCode() : defaultErrorCode;
    }

    private APIError handleTemplateManagementException(TemplateManagementException e, String msg) {

        if (e instanceof TemplateManagementClientException) {
            throw buildClientError(e, msg);
        }
        throw buildServerError(e, msg);
    }

    private APIError handleTemplateNotFoundException(TemplateManagementException e) {

        String errorMessage = "Template not found";
        return Utils.buildNotFoundError(e.getErrorCode(), errorMessage, e.getMessage());
    }

    private APIError buildServerError(TemplateManagementException e, String message) {

        String errorCode = getErrorCode(e, UNEXPECTED_SERVER_ERROR.getCode());
        return Utils.buildServerError(errorCode, message, e.getMessage(), e);
    }

    private APIError buildClientError(TemplateManagementException e, String message) {

        String errorCode = getErrorCode(e, INVALID_REQUEST.getCode());
        return Utils.buildClientError(errorCode, message, e.getMessage());
    }

    private APIError buildClientError(CORSManagementServiceException e, String message) {

        String errorCode = getErrorCode(e, INVALID_REQUEST.getCode());
        return Utils.buildClientError(errorCode, message, e.getMessage());
    }

    private String getErrorCode(TemplateManagementException e, String defaultErrorCode) {

        return e.getErrorCode() != null ? e.getErrorCode() : defaultErrorCode;
    }

    private String getErrorCode(CORSManagementServiceException e, String defaultErrorCode) {

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

    /**
     * Validate the CORS Origins. This should be moved to the service layer with the CORS adding step on creating
     * OIDC applications.
     *
     * @param corsOrigins List of the CORS Origins..
     */
    private static void validateCORSOrigins(List<String> corsOrigins) {

        boolean isValidOrigin = true;
        if (!CollectionUtils.isEmpty(corsOrigins)) {
            for (String origin : corsOrigins) {
                try {
                    URL url = new URL(origin);
                    if (StringUtils.isNotEmpty(url.toURI().getPath())) {
                        isValidOrigin = false;
                    }
                } catch (IllegalArgumentException | MalformedURLException | URISyntaxException e) {
                    isValidOrigin = false;
                }

                if (!isValidOrigin) {
                    throw buildBadRequestError("Error creating application. Invalid Allowed Origin found: " +
                            origin);
                }
            }
        }
    }
}
