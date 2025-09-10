/*
* Copyright (c) 2021, WSO2 LLC. (http://www.wso2.com).
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.authenticators.v1.AuthenticatorsApiService;
import org.wso2.carbon.identity.api.server.authenticators.v1.core.ServerAuthenticatorManagementService;
import org.wso2.carbon.identity.api.server.authenticators.v1.factories.ServerAuthenticatorManagementServiceFactory;
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

    private static final Log log = LogFactory.getLog(AuthenticatorsApiServiceImpl.class);
    private final ServerAuthenticatorManagementService authenticatorManagementService;

    public AuthenticatorsApiServiceImpl() {

        try {
            log.info("Initializing AuthenticatorsApiService");
            authenticatorManagementService = ServerAuthenticatorManagementServiceFactory
                    .getServerAuthenticatorManagementService();
            log.info("Successfully initialized AuthenticatorsApiService");
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating the authenticator management services", e);
            throw new RuntimeException("Error occurred while initiating the authenticator management services.", e);
        }
    }

    @Override
    public Response authenticatorsGet(String filter, Integer limit, Integer offset) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving authenticators with filter: " + filter + ", limit: " + limit + 
                    ", offset: " + offset);
        }
        return Response.ok().entity(authenticatorManagementService.getAuthenticators(filter, limit, offset)).build();
    }

    @Override
    public Response authenticatorsMetaTagsGet() {

        return Response.ok().entity(authenticatorManagementService.getTags()).build();
    }

    @Override
    public Response addUserDefinedLocalAuthenticator(
            UserDefinedLocalAuthenticatorCreation userDefinedLocalAuthenticatorCreation) {

        log.info("Creating user defined local authenticator: " + 
                (userDefinedLocalAuthenticatorCreation != null ? 
                userDefinedLocalAuthenticatorCreation.getName() : "null"));
        
        Authenticator response = authenticatorManagementService
                .addUserDefinedLocalAuthenticator(userDefinedLocalAuthenticatorCreation);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT +
                "/authenticator/custom/" + response.getId());
        
        log.info("Successfully created user defined local authenticator with ID: " + response.getId());
        return Response.created(location).entity(response).build();
    }

    @Override
    public Response deleteUserDefinedLocalAuthenticator(String authenticatorId) {

        log.info("Deleting user defined local authenticator with ID: " + authenticatorId);
        authenticatorManagementService.deleteUserDefinedLocalAuthenticator(authenticatorId);
        log.info("Successfully deleted user defined local authenticator with ID: " + authenticatorId);
        return Response.noContent().build();
    }

    @Override
    public Response getConnectedAppsOfLocalAuthenticator(String authenticatorId, Integer limit, Integer offset) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving connected apps for authenticator ID: " + authenticatorId + 
                    ", limit: " + limit + ", offset: " + offset);
        }
        return Response.ok().entity(authenticatorManagementService
                .getConnectedAppsOfLocalAuthenticator(authenticatorId, limit, offset)).build();
    }

    @Override
    public Response updateUserDefinedLocalAuthenticator(
            String authenticatorId, UserDefinedLocalAuthenticatorUpdate userDefinedLocalAuthenticatorUpdate) {

        log.info("Updating user defined local authenticator with ID: " + authenticatorId);
        Authenticator response = authenticatorManagementService
                .updateUserDefinedLocalAuthenticator(authenticatorId, userDefinedLocalAuthenticatorUpdate);
        log.info("Successfully updated user defined local authenticator with ID: " + authenticatorId);
        return Response.ok().entity(response).build();
    }
}
