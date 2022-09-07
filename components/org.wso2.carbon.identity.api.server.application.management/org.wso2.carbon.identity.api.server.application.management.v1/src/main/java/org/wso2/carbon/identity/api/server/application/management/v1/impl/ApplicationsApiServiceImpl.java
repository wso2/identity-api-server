/*
 * Copyright (c) 2019, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.application.management.v1.impl;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationOwner;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationPatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplateModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationsApiService;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocolListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ResidentApplication;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.ServerApplicationManagementService;
import org.wso2.carbon.identity.api.server.application.management.v1.core.ServerApplicationMetadataService;
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

    @Autowired
    private ServerApplicationManagementService applicationManagementService;

    @Autowired
    private ServerApplicationMetadataService applicationMetadataService;

    @Override
    public Response getAllApplications(Integer limit, Integer offset, String filter, String sortOrder, String sortBy,
                                       String requiredAttributes) {

        ApplicationListResponse listResponse = applicationManagementService
                .getAllApplications(limit, offset, filter, sortOrder, sortBy, requiredAttributes);
        return Response.ok().entity(listResponse).build();
    }

    @Override
    public Response getApplication(String applicationId) {

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
    public Response changeApplicationOwner(String applicationId, ApplicationOwner applicationOwner) {

        applicationManagementService.changeApplicationOwner(applicationId, applicationOwner);
        return Response.ok().build();
    }

    @Override
    public Response createApplication(ApplicationModel applicationModel, String template) {

        String resourceId = applicationManagementService.createApplication(applicationModel, template);
        return Response.created(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response createApplicationTemplate(ApplicationTemplateModel applicationTemplateModel) {

        String templateId = applicationManagementService.createApplicationTemplate(applicationTemplateModel);
        return Response.created(getTemplateResourceLocation(templateId)).build();
    }

    @Override
    public Response deleteApplication(String applicationId) {

        applicationManagementService.deleteApplication(applicationId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteApplicationTemplate(String templateId) {

        applicationManagementService.deleteApplicationTemplateById(templateId);
        return Response.noContent().build();
    }

    @Override
    public Response patchApplication(String applicationId, ApplicationPatchModel applicationPatchModel) {

        applicationManagementService.patchApplication(applicationId, applicationPatchModel);
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

        return Response.ok().entity(
                applicationManagementService.exportApplication(applicationId, exportSecrets)).build();
    }

    @Override
    public Response importApplication(InputStream fileInputStream, Attachment fileDetail) {

        String resourceId = applicationManagementService.importApplication(fileInputStream, fileDetail);
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
