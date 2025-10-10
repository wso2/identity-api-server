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

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.idp.debug.v1.IdpDebugApi;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse;

/**
 * Legacy IdP Debug API implementation.
 * 
 * @deprecated This endpoint is deprecated. Use POST /debug/connection/{idpId} instead.
 */
@Deprecated
public class IdpDebugApiServiceImpl implements IdpDebugApi {

    private static final Log LOG = LogFactory.getLog(IdpDebugApiServiceImpl.class);

    /**
     * Handles legacy debug requests.
     * 
     * @deprecated Use POST /debug/connection/{idpId} instead.
     * @return Response indicating deprecation and recommended endpoint.
     */
    @Override
    @Deprecated
    public Response debug() {
        try {
            DebugResponse debugResponse = new DebugResponse();
            debugResponse.setSessionId(java.util.UUID.randomUUID().toString());
            debugResponse.setStatus("INFO");
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("message", "This endpoint is deprecated. Use POST /debug/connection/{idpId} instead.");
            metadata.put("recommendedEndpoint", "/api/server/v1/debug/connection/{idpId}");
            metadata.put("timestamp", System.currentTimeMillis());
            debugResponse.setMetadata(metadata);

            return Response.ok(debugResponse).build();
            
        } catch (Exception e) {
            LOG.error("Error in legacy debug endpoint", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Internal server error: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(errorResponse)
                          .build();
        }
    }
}
