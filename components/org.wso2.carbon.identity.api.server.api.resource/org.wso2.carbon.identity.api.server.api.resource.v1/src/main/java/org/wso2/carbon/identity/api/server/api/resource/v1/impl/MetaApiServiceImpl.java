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
import org.wso2.carbon.identity.api.server.api.resource.v1.MetaApiService;
import org.wso2.carbon.identity.api.server.api.resource.v1.core.ServerAPIResourceCollectionManagementService;
import org.wso2.carbon.identity.api.server.api.resource.v1.factories.ServerAPIResourceCollectionManagementServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of the API resource collections meta REST API.
 */
public class MetaApiServiceImpl implements MetaApiService {

    private static final Log LOG = LogFactory.getLog(MetaApiServiceImpl.class);
    private final ServerAPIResourceCollectionManagementService serverAPIResourceManagementService;

    public MetaApiServiceImpl() {

        try {
            this.serverAPIResourceManagementService = ServerAPIResourceCollectionManagementServiceFactory
                    .getServerAPIResourceCollectionManagementService();
        } catch (IllegalStateException e) {
            LOG.error("Failed to initialize API resource collection management service", e);
            throw new RuntimeException("Error occurred while initiating API resource collection management service.",
                    e);
        }
    }

    @Override
    public Response getAPIResourceCollectionByCollectionId(String collectionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving API resource collection with ID: " + collectionId);
        }
        return Response.ok().entity(
                serverAPIResourceManagementService.getAPIResourceCollectionByCollectionId(collectionId)).build();
    }

    @Override
    public Response getAPIResourceCollections(String filter, String attributes) {

        return Response.ok().entity(serverAPIResourceManagementService.getAPIResourceCollections(filter, attributes))
                .build();
    }
}
