/*
* Copyright (c) 2021, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.authenticators.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.authenticators.v1.AuthenticatorsApiService;
import org.wso2.carbon.identity.api.server.authenticators.v1.core.ServerAuthenticatorManagementService;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.Authenticator;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.UserDefinedLocalAuthenticatorCreation;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.UserDefinedLocalAuthenticatorUpdate;
import org.wso2.carbon.identity.api.server.common.ContextLoader;

import java.net.URI;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;

/**
 * Implementation of the Server Authenticators Rest API.
 */
public class AuthenticatorsApiServiceImpl implements AuthenticatorsApiService {

    @Autowired
    private ServerAuthenticatorManagementService authenticatorManagementService;

    @Override
    public Response authenticatorsGet(String filter, Integer limit, Integer offset) {

        return Response.ok().entity(authenticatorManagementService.getAuthenticators(filter, limit, offset)).build();
    }

    @Override
    public Response authenticatorsMetaTagsGet() {

        return Response.ok().entity(authenticatorManagementService.getTags()).build();
    }

    @Override
    public Response addUserDefinedLocalAuthenticator(
            UserDefinedLocalAuthenticatorCreation userDefinedLocalAuthenticatorCreation) {

        Authenticator response = authenticatorManagementService
                .addUserDefinedLocalAuthenticator(userDefinedLocalAuthenticatorCreation);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT +
                "/authenticator/custom/" + response.getId());
        return Response.created(location).entity(response).build();
    }

    @Override
    public Response deleteUserDefinedLocalAuthenticator(String authenticatorId) {

        authenticatorManagementService.deleteUserDefinedLocalAuthenticator(authenticatorId);
        return Response.noContent().build();
    }

    @Override
    public Response getConnectedAppsOfLocalAuthenticator(String authenticatorId, Integer limit, Integer offset) {

        return Response.ok().entity(authenticatorManagementService
                .getConnectedAppsOfLocalAuthenticator(authenticatorId, limit, offset)).build();
    }

    @Override
    public Response updateUserDefinedLocalAuthenticator(
            String authenticatorId, UserDefinedLocalAuthenticatorUpdate userDefinedLocalAuthenticatorUpdate) {

        return Response.ok().entity(authenticatorManagementService
                .updateUserDefinedLocalAuthenticator(authenticatorId, userDefinedLocalAuthenticatorUpdate)).build();
    }
}
