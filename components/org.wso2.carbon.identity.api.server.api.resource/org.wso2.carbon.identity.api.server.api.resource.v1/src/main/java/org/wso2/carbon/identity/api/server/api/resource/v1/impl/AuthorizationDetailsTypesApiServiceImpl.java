/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.api.resource.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesApiService;
import org.wso2.carbon.identity.api.server.api.resource.v1.core.AuthorizationDetailsTypeManagementService;
import org.wso2.carbon.identity.api.server.api.resource.v1.factories.AuthorizationDetailsTypeManagementServiceFactory;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Implementation of the Authorization details types REST Api.
 */
public class AuthorizationDetailsTypesApiServiceImpl implements AuthorizationDetailsTypesApiService {

    private static final Log LOG = LogFactory.getLog(AuthorizationDetailsTypesApiServiceImpl.class);
    private final AuthorizationDetailsTypeManagementService typeMgtService;

    public AuthorizationDetailsTypesApiServiceImpl() {

        try {
            this.typeMgtService = AuthorizationDetailsTypeManagementServiceFactory
                    .getAuthorizationDetailsTypeManagementService();
        } catch (IllegalStateException e) {
            LOG.error("Failed to initialize AuthorizationDetailsTypeManagementService", e);
            throw new RuntimeException("Error occurred while initiating AuthorizationDetailsTypeManagementService.", e);
        }
    }

    @Override
    public Response authorizationDetailsTypesGet(String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving authorization details types with filter: " + (filter != null ? filter : "none"));
        }
        return Response.ok().entity(typeMgtService.getAllAuthorizationDetailsTypes(filter)).build();
    }

    @Override
    public Response isAuthorizationDetailsTypeExists(final String filter) {

        return typeMgtService.isAuthorizationDetailsTypeExists(filter)
                ? Response.ok().build()
                : Response.status(NOT_FOUND).build();
    }
}
