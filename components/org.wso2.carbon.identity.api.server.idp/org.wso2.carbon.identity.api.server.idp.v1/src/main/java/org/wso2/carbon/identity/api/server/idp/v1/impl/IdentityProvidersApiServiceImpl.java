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

package org.wso2.carbon.identity.api.server.idp.v1.impl;

import org.wso2.carbon.identity.api.server.idp.v1.*;
import org.wso2.carbon.identity.api.server.idp.v1.model.*;
import java.util.List;
import javax.ws.rs.core.Response;

public class IdentityProvidersApiServiceImpl implements IdentityProvidersApiService {

    @Override
    public Response addIDP(IdentityProviderPOSTRequest identityProviderPOSTRequest) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response addIDPTemplate(IdentityProviderTemplate identityProviderTemplate) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response deleteIDP(String identityProviderId, Boolean force) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response deleteIDPTemplate(String templateId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getClaimConfig(String identityProviderId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getConnectedApps(String identityProviderId, Integer limit, Integer offset) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getFederatedAuthenticator(String identityProviderId, String federatedAuthenticatorId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getFederatedAuthenticators(String identityProviderId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getIDP(String identityProviderId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getIDPTemplate(String templateId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getIDPTemplates(Integer limit, Integer offset, String filter) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getIDPs(Integer limit, Integer offset, String filter, String requiredAttributes) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getJITConfig(String identityProviderId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getMetaFederatedAuthenticator(String federatedAuthenticatorId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getMetaFederatedAuthenticators() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getMetaOutboundConnector(String outboundProvisioningConnectorId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getMetaOutboundConnectors() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getOutboundConnector(String identityProviderId, String outboundProvisioningConnectorId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getOutboundConnectors(String identityProviderId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getProvisioningConfig(String identityProviderId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getRoleConfig(String identityProviderId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response patchIDP(String identityProviderId, List<Patch> patch) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateClaimConfig(String identityProviderId, Claims claims) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateFederatedAuthenticator(String identityProviderId, String federatedAuthenticatorId, FederatedAuthenticatorPUTRequest federatedAuthenticatorPUTRequest) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateFederatedAuthenticators(String identityProviderId, FederatedAuthenticatorRequest federatedAuthenticatorRequest) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateIDPTemplate(String templateId, IdentityProviderTemplate identityProviderTemplate) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateJITConfig(String identityProviderId, JustInTimeProvisioning justInTimeProvisioning) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateOutboundConnector(String identityProviderId, String outboundProvisioningConnectorId, OutboundConnectorPUTRequest outboundConnectorPUTRequest) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateOutboundConnectors(String identityProviderId, OutboundProvisioningRequest outboundProvisioningRequest) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateRoleConfig(String identityProviderId, Roles roles) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }
}
