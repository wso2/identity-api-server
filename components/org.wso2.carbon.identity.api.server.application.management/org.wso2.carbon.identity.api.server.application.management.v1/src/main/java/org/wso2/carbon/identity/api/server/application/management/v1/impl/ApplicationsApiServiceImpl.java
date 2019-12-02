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

package org.wso2.carbon.identity.api.server.application.management.v1.impl;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.v1.AdvancedApplicationConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationsApiService;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationSequence;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.ServerApplicationManagementService;
import org.wso2.carbon.identity.api.server.application.management.v1.core.ServerApplicationMetadataService;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;

import java.io.InputStream;
import java.net.URI;
import java.net.URI;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.APPLICATION_MANAGEMENT_PATH_COMPONENT;

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

        return Response.ok()
                .entity(applicationManagementService
                        .getAllApplications(limit, offset, filter, sortOrder, sortBy, requiredAttributes))
                .build();
    }

    @Override
    public Response getApplication(String applicationId) {

        return Response.ok().entity(applicationManagementService.getApplication(applicationId)).build();
    }

    @Override
    public Response createApplication(ApplicationModel applicationModel, String template) {

        ApplicationModel createdApp = applicationManagementService.createApplication(applicationModel);
        return Response.created(getResourceLocation(createdApp.getId())).entity(createdApp).build();
    }

    @Override
    public Response deleteApplication(String applicationId) {

        applicationManagementService.deleteApplication(applicationId);
        return Response.noContent().build();
    }

    @Override
    public Response updateApplication(String applicationId, ApplicationModel applicationModel) {

        applicationManagementService.updateApplication(applicationId, applicationModel);
        return Response.noContent().build();
    }

    @Override
    public Response deleteAdvancedConfigurations(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response deleteAuthenticationSequence(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response deleteClaimConfiguration(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response deleteCustomInboundConfiguration(String applicationId, String inboundProtocolId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response deleteInboundAuthenticationConfigurations(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response deleteInboundOAuthConfiguration(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response deleteInboundSAMLConfiguration(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response deletePassiveStsConfiguration(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response deleteProvisioningConfiguration(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response deleteWSTrustConfiguration(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response exportApplication(String applicationId, Boolean exportSecrets) {

        return Response.ok().entity(
                applicationManagementService.exportApplication(applicationId, exportSecrets)).build();
    }

    @Override
    public Response importApplication(InputStream fileInputStream, Attachment fileDetail) {

        ApplicationModel applicationModel = applicationManagementService.importApplication(fileInputStream, fileDetail);
        URI location = ContextLoader.buildURIForHeader(
                Constants.V1_API_PATH_COMPONENT + APPLICATION_MANAGEMENT_PATH_COMPONENT + "/" +
                        applicationModel.getId());
        return Response.created(location).entity(applicationModel).build();
    }

    @Override
    public Response getAdvancedConfigurations(String applicationId) {

        ApplicationModel application = applicationManagementService.getApplication(applicationId);
        if (application.getAdvancedConfigurations() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(application.getAdvancedConfigurations()).build();
        }
    }

    @Override
    public Response getAuthenticationSequence(String applicationId) {

        ApplicationModel application = applicationManagementService.getApplication(applicationId);
        if (application.getAuthenticationSequence() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(application.getAuthenticationSequence()).build();
        }
    }

    @Override
    public Response getClaimConfiguration(String applicationId) {

        ApplicationModel application = applicationManagementService.getApplication(applicationId);
        if (application.getClaimConfiguration() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(application.getClaimConfiguration()).build();
        }
    }

    @Override
    public Response getCustomInboundConfiguration(String applicationId, String inboundProtocolId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getInboundAuthenticationConfigurations(String applicationId) {

        ApplicationModel application = applicationManagementService.getApplication(applicationId);
        if (application.getInboundProtocolConfiguration() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(application.getInboundProtocolConfiguration()).build();
        }
    }

    @Override
    public Response getInboundOAuthConfiguration(String applicationId) {

        ApplicationModel application = applicationManagementService.getApplication(applicationId);
        if (application.getInboundProtocolConfiguration() == null ||
                application.getInboundProtocolConfiguration().getOidc() != null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(application.getInboundProtocolConfiguration().getOidc()).build();
        }
    }

    @Override
    public Response getInboundSAMLConfiguration(String applicationId) {

        ApplicationModel application = applicationManagementService.getApplication(applicationId);
        if (application.getInboundProtocolConfiguration() == null ||
                application.getInboundProtocolConfiguration().getSaml() != null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(application.getInboundProtocolConfiguration().getSaml()).build();
        }
    }

    @Override
    public Response getPassiveStsConfiguration(String applicationId) {

        ApplicationModel application = applicationManagementService.getApplication(applicationId);
        if (application.getInboundProtocolConfiguration() == null ||
                application.getInboundProtocolConfiguration().getPassiveSts() != null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(application.getInboundProtocolConfiguration().getPassiveSts()).build();
        }
    }

    @Override
    public Response getProvisioningConfiguration(String applicationId) {

        ApplicationModel application = applicationManagementService.getApplication(applicationId);
        if (application.getProvisioningConfigurations() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(application.getProvisioningConfigurations()).build();
        }
    }

    @Override
    public Response getResidentApplication() {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getWSTrustConfiguration(String applicationId) {

        ApplicationModel application = applicationManagementService.getApplication(applicationId);
        if (application.getInboundProtocolConfiguration() == null ||
                application.getInboundProtocolConfiguration().getWsTrust() != null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(application.getInboundProtocolConfiguration().getWsTrust()).build();
        }
    }

    @Override
    public Response regenerateOAuthApplicationSecret(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response revokeOAuthApplication(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response updateAdvancedConfigurations(String applicationId,
                                                 AdvancedApplicationConfiguration advancedApplicationConfiguration) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response updateAuthenticationSequence(String applicationId, AuthenticationSequence authenticationSequence) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response updateClaimConfiguration(String applicationId, ClaimConfiguration claimConfiguration) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response updateCustomInboundConfiguration(String applicationId,
                                                     String inboundProtocolId,
                                                     CustomInboundProtocolConfiguration customInboundProtocolConfig) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response updateInboundAuthenticationConfigurations(String applicationId, InboundProtocols inboundProtocols) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response updateInboundOAuthConfiguration(String applicationId,
                                                    OpenIDConnectConfiguration openIDConnectConfiguration) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response updateInboundSAMLConfiguration(String applicationId, SAML2Configuration saML2Configuration) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response updatePassiveStsConfiguration(String applicationId,
                                                  PassiveStsConfiguration passiveStsConfiguration) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response updateProvisioningConfiguration(String applicationId,
                                                    ProvisioningConfiguration provisioningConfiguration) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response updateResidentApplication(ProvisioningConfiguration provisioningConfiguration) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response updateWSTrustConfiguration(String applicationId, WSTrustConfiguration wsTrustConfiguration) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    private URI getResourceLocation(String resourceId) {

        return ContextLoader.buildURIForHeader(Constants.V1_API_PATH_COMPONENT +
                ApplicationManagementConstants.APPLICATION_MANAGEMENT_PATH_COMPONENT + "/" + resourceId);
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
}
