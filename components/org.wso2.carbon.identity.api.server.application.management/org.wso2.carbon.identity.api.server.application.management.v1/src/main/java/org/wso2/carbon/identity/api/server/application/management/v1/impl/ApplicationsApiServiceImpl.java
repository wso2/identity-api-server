/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.application.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationOwner;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationPatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationShareAllRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationSharePOSTRequest;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationSharingPatchRequest;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplateModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationUnshareAllRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationUnshareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationsApiService;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthorizedAPICreationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthorizedAPIPatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocolListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowGenerateRequest;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowGenerateResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowResultResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowStatusResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ResidentApplication;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.ServerApplicationManagementService;
import org.wso2.carbon.identity.api.server.application.management.v1.core.ServerApplicationMetadataService;
import org.wso2.carbon.identity.api.server.application.management.v1.core.ServerApplicationSharingService;
import org.wso2.carbon.identity.api.server.application.management.v1.core.TransferResource;
import org.wso2.carbon.identity.api.server.application.management.v1.factories.LoginFlowAIServiceFactory;
import org.wso2.carbon.identity.api.server.application.management.v1.factories
        .ServerApplicationManagementServiceFactory;
import org.wso2.carbon.identity.api.server.application.management.v1.factories.ServerApplicationMetadataServiceFactory;
import org.wso2.carbon.identity.api.server.application.management.v1.factories.ServerApplicationSharingServiceFactory;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Implementation of ApplicationsApiService.
 */
public class ApplicationsApiServiceImpl implements ApplicationsApiService {

    private static final Log log = LogFactory.getLog(ApplicationsApiServiceImpl.class);

    private final ServerApplicationManagementService applicationManagementService;
    private final ServerApplicationMetadataService applicationMetadataService;
    private final ServerApplicationSharingService applicationSharingService;

    public ApplicationsApiServiceImpl() {

        if (log.isDebugEnabled()) {
            log.debug("Initializing ApplicationsApiServiceImpl.");
        }
        try {
            this.applicationManagementService = ServerApplicationManagementServiceFactory
                    .getServerApplicationManagementService();
            this.applicationMetadataService = ServerApplicationMetadataServiceFactory
                    .getServerApplicationMetadataService();
            this.applicationSharingService = ServerApplicationSharingServiceFactory
                    .getServerApplicationSharingService();
            if (log.isDebugEnabled()) {
                log.debug("Successfully initialized ApplicationsApiServiceImpl with required services.");
            }
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating application management services.", e);
            throw new RuntimeException("Error occurred while initiating application management services.", e);
        }
    }

    @Deprecated
    @Override
    public Response getAllApplications(Integer limit, Integer offset, String filter, String sortOrder, String sortBy,
                                       String requiredAttributes) {

        log.warn("Using deprecated getAllApplications method without excludeSystemPortals parameter.");
        return getAllApplications(limit, offset, filter, sortOrder, sortBy, requiredAttributes, false);
    }

    @Override
    public Response getAllApplications(Integer limit, Integer offset, String filter, String sortOrder, String sortBy,
                                       String requiredAttributes, Boolean excludeSystemPortals) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Retrieving applications with limit: %s, offset: %s, excludeSystemPortals: %s",
                    limit, offset, excludeSystemPortals));
        }
        ApplicationListResponse listResponse = applicationManagementService.getAllApplications(limit, offset, filter,
                sortOrder, sortBy, requiredAttributes, Boolean.TRUE.equals(excludeSystemPortals));
        if (log.isDebugEnabled()) {
            int count = listResponse != null && listResponse.getApplications() != null ? 
                    listResponse.getApplications().size() : 0;
            log.debug(String.format("Successfully retrieved %d applications.", count));
        }
        return Response.ok().entity(listResponse).build();
    }

    @Override
    public Response getApplication(String applicationId) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Retrieving application with ID: %s", applicationId));
        }
        return Response.ok().entity(applicationManagementService.getApplication(applicationId)).build();
    }

    @Override
    public Response getConfiguredAuthenticators(String applicationId) {

        return Response.ok().entity(applicationManagementService
                .getConfiguredAuthenticators(applicationId)).build();
    }

    @Override
    public Response getApplicationTemplate(String templateId) {

        return Response.ok().entity(applicationManagementService.getApplicationTemplateById(templateId)).build();
    }

    @Override
    public Response getAuthorizedAPIs(String applicationId) {

        return Response.ok().entity(applicationManagementService.getAuthorizedAPIs(applicationId)).build();
    }

    @Override
    public Response addAuthorizedAPI(String applicationId, AuthorizedAPICreationModel authorizedAPICreationModel) {

        applicationManagementService.addAuthorizedAPI(applicationId, authorizedAPICreationModel);
        return Response.ok().build();
    }

    @Override
    public Response changeApplicationOwner(String applicationId, ApplicationOwner applicationOwner) {

        String newOwner = applicationOwner != null ? applicationOwner.getId() : null;
        if (log.isInfoEnabled()) {
            log.info(String.format("Changing owner of application ID: %s to user: %s", applicationId, newOwner));
        }
        applicationManagementService.changeApplicationOwner(applicationId, applicationOwner);
        if (log.isInfoEnabled()) {
            log.info(String.format("Successfully changed owner of application ID: %s", applicationId));
        }
        return Response.ok().build();
    }

    @Override
    public Response createApplication(ApplicationModel applicationModel, String template) {

        String applicationName = applicationModel != null ? applicationModel.getName() : null;
        if (log.isInfoEnabled()) {
            log.info(String.format("Creating application: %s with template: %s", applicationName, template));
        }
        String resourceId = applicationManagementService.createApplication(applicationModel, template);
        if (log.isInfoEnabled()) {
            log.info(String.format("Successfully created application: %s with ID: %s", applicationName, resourceId));
        }
        return Response.created(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response createApplicationTemplate(ApplicationTemplateModel applicationTemplateModel) {

        String templateId = applicationManagementService.createApplicationTemplate(applicationTemplateModel);
        return Response.created(getTemplateResourceLocation(templateId)).build();
    }

    @Override
    public Response deleteApplication(String applicationId) {

        if (log.isInfoEnabled()) {
            log.info(String.format("Deleting application with ID: %s", applicationId));
        }
        applicationManagementService.deleteApplication(applicationId);
        if (log.isInfoEnabled()) {
            log.info(String.format("Successfully deleted application with ID: %s", applicationId));
        }
        return Response.noContent().build();
    }

    @Override
    public Response deleteApplicationTemplate(String templateId) {

        applicationManagementService.deleteApplicationTemplateById(templateId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteAuthorizedAPI(String applicationId, String authorizationId) {

        applicationManagementService.deleteAuthorizedAPI(applicationId, authorizationId);
        return Response.noContent().build();
    }

    @Override
    public Response patchApplication(String applicationId, ApplicationPatchModel applicationPatchModel) {

        if (log.isInfoEnabled()) {
            log.info(String.format("Patching application with ID: %s", applicationId));
        }
        applicationManagementService.patchApplication(applicationId, applicationPatchModel);
        if (log.isInfoEnabled()) {
            log.info(String.format("Successfully patched application with ID: %s", applicationId));
        }
        return Response.ok().build();
    }

    @Override
    public Response patchApplicationSharing(ApplicationSharingPatchRequest applicationSharingPatchRequest) {

        return applicationSharingService.updateSharedApplication(applicationSharingPatchRequest);
    }

    @Override
    public Response patchAuthorizedAPI(String applicationId, String authorizationId,
                                       AuthorizedAPIPatchModel authorizedAPIPatchModel) {

        applicationManagementService.updateAuthorizedAPI(applicationId, authorizationId, authorizedAPIPatchModel);
        return Response.ok().build();
    }

    @Override
    public Response getInboundOAuthConfiguration(String applicationId) {

        OpenIDConnectConfiguration oauthApp = applicationManagementService.getInboundOAuthConfiguration(applicationId);
        return Response.ok(oauthApp).build();
    }

    @Override
    public Response getInboundSAMLConfiguration(String applicationId) {

        SAML2ServiceProvider samlSp = applicationManagementService.getInboundSAMLConfiguration(applicationId);
        return Response.ok(samlSp).build();
    }

    @Override
    public Response getLoginFlowGenerationResult(String operationId) {

        LoginFlowResultResponse loginFlowAIGenerationResult = LoginFlowAIServiceFactory.getLoginFlowAIService()
                .getAuthenticationSequenceGenerationResult(operationId);
        return Response.ok(loginFlowAIGenerationResult).build();
    }

    @Override
    public Response getLoginFlowGenerationStatus(String operationId) {

        LoginFlowStatusResponse loginFlowAIStatus = LoginFlowAIServiceFactory.getLoginFlowAIService()
                .getAuthenticationSequenceGenerationStatus(operationId);
        return Response.ok(loginFlowAIStatus).build();
    }

    @Override
    public Response getPassiveStsConfiguration(String applicationId) {

        PassiveStsConfiguration passiveStsApp = applicationManagementService.getPassiveStsConfiguration(applicationId);
        return Response.ok(passiveStsApp).build();
    }

    @Override
    public Response getWSTrustConfiguration(String applicationId) {

        WSTrustConfiguration wsTrustConfiguration = applicationManagementService.getWSTrustConfiguration(applicationId);
        return Response.ok(wsTrustConfiguration).build();
    }

    @Override
    public Response getCustomInboundConfiguration(String applicationId, String inboundProtocolId) {

        CustomInboundProtocolConfiguration customInbound =
                applicationManagementService.getCustomInboundConfiguration(applicationId, inboundProtocolId);
        return Response.ok(customInbound).build();
    }

    @Override
    public Response deleteInboundOAuthConfiguration(String applicationId) {

        applicationManagementService.deleteOAuthInbound(applicationId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response deleteInboundSAMLConfiguration(String applicationId) {

        applicationManagementService.deleteSAMLInbound(applicationId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response deletePassiveStsConfiguration(String applicationId) {

        applicationManagementService.deletePassiveStsInbound(applicationId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response deleteWSTrustConfiguration(String applicationId) {

        applicationManagementService.deleteWSTrustInbound(applicationId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response deleteCustomInboundConfiguration(String applicationId, String inboundProtocolId) {

        applicationManagementService.deleteCustomInbound(applicationId, inboundProtocolId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response exportApplication(String applicationId, Boolean exportSecrets) {

        if (log.isInfoEnabled()) {
            log.info(String.format("Exporting application with ID: %s, exportSecrets: %s", applicationId, 
                    exportSecrets));
        }
        return Response.ok().entity(
                applicationManagementService.exportApplication(applicationId, exportSecrets)).build();
    }

    @Override
    public Response exportApplicationAsFile(String applicationId, Boolean exportSecrets, String fileType) {

        TransferResource transferResource = applicationManagementService.exportApplicationAsFile(
                applicationId,
                exportSecrets,
                fileType
        );

        return Response.ok()
                .type("application/octet-stream")
                .header("Content-Disposition", "attachment; filename=\""
                        + transferResource.getResourceName() + "\"")
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .entity(transferResource.getResource())
                .build();
    }

    @Override
    public Response generateLoginFlow(LoginFlowGenerateRequest loginFlowGenerateRequest) {

        LoginFlowGenerateResponse loginFlowGenerateResponse = LoginFlowAIServiceFactory.getLoginFlowAIService()
                .generateAuthenticationSequence(loginFlowGenerateRequest);
        return Response.accepted(loginFlowGenerateResponse).build();
    }

    @Override
    public Response importApplication(InputStream fileInputStream, Attachment fileDetail) {

        String fileName = fileDetail != null ? fileDetail.getContentDisposition().getFilename() : null;
        if (log.isInfoEnabled()) {
            log.info(String.format("Importing application from file: %s", fileName));
        }
        String resourceId = applicationManagementService.importApplication(fileInputStream, fileDetail);
        if (log.isInfoEnabled()) {
            log.info(String.format("Successfully imported application with ID: %s from file: %s", resourceId, 
                    fileName));
        }
        return Response.created(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response importApplicationForUpdate(InputStream fileInputStream, Attachment fileDetail) {

        String resourceId = applicationManagementService.importApplicationForUpdate(fileInputStream, fileDetail);
        return Response.ok().location(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response getInboundAuthenticationConfigurations(String applicationId) {

        List<InboundProtocolListItem> inbounds = applicationManagementService.getInboundProtocols(applicationId);
        return Response.ok(inbounds).build();
    }

    @Override
    public Response getResidentApplication() {

        ResidentApplication residentApplication = applicationManagementService.getResidentApplication();
        return Response.ok(residentApplication).build();
    }

    @Override
    public Response updateResidentApplication(ProvisioningConfiguration provisioningConfiguration) {

        ResidentApplication residentApplication =
                applicationManagementService.updateResidentApplication(provisioningConfiguration);
        return Response.ok(residentApplication).build();
    }

    @Override
    public Response regenerateOAuthClientSecret(String applicationId) {

        OpenIDConnectConfiguration openIDConnectConfiguration =
                applicationManagementService.regenerateOAuthApplicationSecret(applicationId);
        return Response.ok(openIDConnectConfiguration).build();
    }

    @Override
    public Response revokeOAuthClient(String applicationId) {

        applicationManagementService.revokeOAuthClient(applicationId);
        return Response.ok().build();
    }

    @Override
    public Response shareApplicationWithAll(ApplicationShareAllRequestBody applicationShareAllRequestBody) {

        return applicationSharingService.shareApplicationToAllOrganizations(applicationShareAllRequestBody);
    }

    @Override
    public Response shareApplicationWithSelected(
            ApplicationShareSelectedRequestBody applicationShareSelectedRequestBody) {

        return applicationSharingService.shareOrganizationApplication(applicationShareSelectedRequestBody);
    }

    @Override
    public Response shareOrgApplication(String applicationId, ApplicationSharePOSTRequest applicationSharePOSTRequest) {

        return applicationSharingService.shareOrganizationApplication(applicationId, applicationSharePOSTRequest);
    }

    @Override
    public Response shareOrgApplicationDelete(String applicationId, String sharedOrganizationId) {

        return applicationSharingService.deleteSharedApplication(applicationId, sharedOrganizationId);
    }

    @Override
    public Response shareOrgApplicationGet(String applicationId, String before, String after, String filter,
                                           Integer limit, Boolean recursive, String excludedAttributes,
                                           String attributes) {

        return applicationSharingService.getApplicationSharedOrganizations(applicationId, before, after, filter,
                limit, recursive, excludedAttributes, attributes);
    }

    @Override
    public Response sharedApplicationsAllDelete(String applicationId) {

        return applicationSharingService.deleteAllSharedApplications(applicationId);
    }

    @Override
    public Response sharedApplicationsGet(String applicationId) {

        return applicationSharingService.getSharedApplications(applicationId);
    }

    @Override
    public Response unshareApplicationFromAll(ApplicationUnshareAllRequestBody applicationUnshareAllRequestBody) {

        return applicationSharingService.unshareApplicationFromAllOrganizations(applicationUnshareAllRequestBody);
    }

    @Override
    public Response unshareApplicationFromSelected(
            ApplicationUnshareSelectedRequestBody applicationUnshareSelectedRequestBody) {

        return applicationSharingService.unshareApplicationFromSelectedOrganizations(
                applicationUnshareSelectedRequestBody);
    }

    @Override
    public Response updateApplicationTemplate(String templateId, ApplicationTemplateModel applicationTemplateModel) {

        applicationManagementService.updateApplicationTemplateById(templateId, applicationTemplateModel);
        return Response.ok().build();
    }

    @Override
    public Response updateCustomInboundConfiguration(String applicationId,
                                                     String inboundProtocolId,
                                                     CustomInboundProtocolConfiguration customInboundModel) {

        applicationManagementService.updateCustomInbound(applicationId, inboundProtocolId, customInboundModel);
        return Response.ok().build();
    }

    @Override
    public Response updateInboundOAuthConfiguration(String applicationId,
                                                    OpenIDConnectConfiguration openIDConnectConfiguration) {

        applicationManagementService.putInboundOAuthConfiguration(applicationId, openIDConnectConfiguration);
        return Response.ok().build();
    }

    @Override
    public Response updateInboundSAMLConfiguration(String applicationId, SAML2Configuration saml2Configuration) {

        applicationManagementService.putInboundSAMLConfiguration(applicationId, saml2Configuration);
        return Response.ok().build();
    }

    @Override
    public Response updatePassiveStsConfiguration(String applicationId,
                                                  PassiveStsConfiguration passiveStsConfiguration) {

        applicationManagementService.putInboundPassiveSTSConfiguration(applicationId, passiveStsConfiguration);
        return Response.ok().build();
    }

    @Override
    public Response updateWSTrustConfiguration(String applicationId, WSTrustConfiguration wsTrustConfiguration) {

        applicationManagementService.putInboundWSTrustConfiguration(applicationId, wsTrustConfiguration);
        return Response.ok().build();
    }

    @Override
    public Response getInboundProtocols(Boolean customOnly) {

        return Response.ok().entity(applicationMetadataService.getInboundProtocols(customOnly)).build();
    }

    @Override
    public Response getCustomProtocolMetadata(String inboundProtocolId) {

        return Response.ok().entity(applicationMetadataService.getCustomProtocolMetadata(inboundProtocolId)).build();
    }

    @Override
    public Response getOIDCMetadata() {

        return Response.ok().entity(applicationMetadataService.getOIDCMetadata()).build();
    }

    @Override
    public Response getSAMLMetadata() {

        return Response.ok().entity(applicationMetadataService.getSAMLMetadata()).build();
    }

    @Override
    public Response getWSTrustMetadata() {

        return Response.ok().entity(applicationMetadataService.getWSTrustMetadata()).build();
    }

    @Override
    public Response getAdaptiveAuthTemplates() {

        return Response.ok().entity(applicationMetadataService.getAdaptiveAuthTemplates()).build();
    }

    @Override
    public Response getAllApplicationTemplates(Integer limit, Integer offset, SearchContext searchContext) {

        return Response.ok().entity(applicationManagementService.listApplicationTemplates(limit, offset,
                searchContext)).build();
    }

    @Override
    public Response getGroups(String domain, String filter) {

        return Response.ok().entity(applicationManagementService.getGroups(domain, filter)).build();
    }

    private URI getResourceLocation(String resourceId) {

        return ContextLoader.buildURIForHeader(Constants.V1_API_PATH_COMPONENT +
                ApplicationManagementConstants.APPLICATION_MANAGEMENT_PATH_COMPONENT + "/" + resourceId);
    }

    private URI getTemplateResourceLocation(String resourceId) {

        return ContextLoader.buildURIForHeader(Constants.V1_API_PATH_COMPONENT +
                ApplicationManagementConstants.APPLICATION_MANAGEMENT_PATH_COMPONENT +
                ApplicationManagementConstants.APPLICATION_TEMPLATE_MANAGEMENT_PATH_COMPONENT + "/" + resourceId);
    }
}
