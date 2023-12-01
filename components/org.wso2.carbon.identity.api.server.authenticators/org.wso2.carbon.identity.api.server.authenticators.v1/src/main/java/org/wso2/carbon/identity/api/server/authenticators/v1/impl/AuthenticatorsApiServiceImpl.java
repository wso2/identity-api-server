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

import javax.ws.rs.core.Response;

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
    public Response getConnectedAppsOfLocalAuthenticator(String authenticatorId, Integer limit, Integer offset) {

        return Response.ok().entity(authenticatorManagementService
                .getConnectedAppsOfLocalAuthenticator(authenticatorId, limit, offset)).build();
    }
}
