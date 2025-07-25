/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopesApiService;
import org.wso2.carbon.identity.api.server.api.resource.v1.core.ServerAPIResourceManagementService;
import org.wso2.carbon.identity.api.server.api.resource.v1.factories.ServerAPIResourceManagementServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of scopes API.
 */
public class ScopesApiServiceImpl implements ScopesApiService {

    private static final Log LOG = LogFactory.getLog(ScopesApiServiceImpl.class);
    private final ServerAPIResourceManagementService serverAPIResourceManagementService;

    public ScopesApiServiceImpl() {

        try {
            this.serverAPIResourceManagementService = ServerAPIResourceManagementServiceFactory
                    .getServerAPIResourceManagementService();
        } catch (IllegalStateException e) {
            LOG.error("Failed to initialize API resource management service", e);
            throw new RuntimeException("Error occurred while initiating API resource management service.", e);
        }
    }

    @Override
    public Response scopesGet(String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving scopes with filter: " + (filter != null ? filter : "none"));
        }
        return Response.ok().entity(serverAPIResourceManagementService.getScopesByTenant(filter)).build();
    }
}
