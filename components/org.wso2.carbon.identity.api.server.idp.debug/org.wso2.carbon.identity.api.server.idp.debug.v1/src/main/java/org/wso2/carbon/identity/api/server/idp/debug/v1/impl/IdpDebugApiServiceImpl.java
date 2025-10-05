/*
 * Copyright (c) 2019-2025, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.idp.debug.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.idp.debug.v1.IdpDebugApi;
import org.wso2.carbon.identity.api.server.idp.debug.v1.service.SimpleDebugService;

import javax.ws.rs.core.Response;

/**
 * Implementation of IdP Debug Flow API.
 * This implementation follows the architecture diagram by delegating business logic to the DebugService.
 */
public class IdpDebugApiServiceImpl implements IdpDebugApi {

    private static final Log LOG = LogFactory.getLog(IdpDebugApiServiceImpl.class);
    private final SimpleDebugService debugService;

    /**
     * Constructor initializes the debug service.
     */
    public IdpDebugApiServiceImpl() {
        this.debugService = new SimpleDebugService();
    }

    /**
     * Handles the debug authentication flow request.
     * This endpoint provides backward compatibility for existing API consumers.
     *
     * @return Response containing debug flow results.
     */
    @Override
    public Response debug() {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Processing legacy debug request - redirecting to proper API usage");
            }
            
            // Create a basic response indicating this endpoint is deprecated
            org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse debugResponse = 
                new org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse();
            
            debugResponse.setSessionId(java.util.UUID.randomUUID().toString());
            debugResponse.setStatus("INFO");
            
            java.util.Map<String, Object> metadata = new java.util.HashMap<>();
            metadata.put("message", "This endpoint is deprecated. Use POST /debug/connection/{idpId} instead.");
            metadata.put("recommendedEndpoint", "/api/server/v1/debug/connection/{idpId}");
            metadata.put("timestamp", System.currentTimeMillis());
            debugResponse.setMetadata(metadata);

            return Response.ok(debugResponse).build();
            
        } catch (Exception e) {
            LOG.error("Error in legacy debug endpoint: " + e.getMessage(), e);
            
            java.util.Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Internal server error: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(errorResponse)
                          .build();
        }
    }
}
