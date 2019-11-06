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

import org.springframework.beans.factory.annotation.Autowired;
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

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response deleteApplication(String applicationId) {

        applicationManagementService.deleteApplication(applicationId);
        return Response.noContent().build();
    }

    @Override
    public Response updateApplication(String applicationId, ApplicationModel applicationModel) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
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
    public Response getAdvancedConfigurations(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getAuthenticationSequence(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getClaimConfiguration(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getCustomInboundConfiguration(String applicationId, String inboundProtocolId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getInboundAuthenticationConfigurations(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getInboundOAuthConfiguration(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getInboundSAMLConfiguration(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getPassiveStsConfiguration(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getProvisioningConfiguration(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getResidentApplication() {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getWSTrustConfiguration(String applicationId) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
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

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
