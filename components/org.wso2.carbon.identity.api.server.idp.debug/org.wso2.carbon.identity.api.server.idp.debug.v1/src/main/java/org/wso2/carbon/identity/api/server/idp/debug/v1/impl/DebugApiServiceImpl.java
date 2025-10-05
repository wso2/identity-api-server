/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.idp.debug.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.idp.debug.v1.DebugApiService;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.idp.debug.v1.service.SimpleDebugService;
import org.wso2.carbon.identity.api.server.common.error.APIError;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of DebugApiService for testing Identity Provider authentication flows.
 */
public class DebugApiServiceImpl implements DebugApiService {

    private static final Log LOG = LogFactory.getLog(DebugApiServiceImpl.class);
    private final SimpleDebugService debugService;

    /**
     * Constructor initializes the service layer.
     */
    public DebugApiServiceImpl() {
        this.debugService = new SimpleDebugService();
    }

    /**
     * Debug IdP connection with authentication credentials.
     *
     * @param idpId Identity Provider ID from path parameter.
     * @param debugConnectionRequest Debug connection request containing credentials.
     * @return Response containing debug authentication results and claims.
     */
    @Override
    public Response debugConnection(String idpId, DebugConnectionRequest debugConnectionRequest) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Processing debug connection request for IdP: " + idpId);
        }

        try {
            // Input validation at API layer.
            if (debugConnectionRequest == null) {
                return createErrorResponse("INVALID_REQUEST", "Request body is required", 
                    Response.Status.BAD_REQUEST);
            }

            if (idpId == null || idpId.trim().isEmpty()) {
                return createErrorResponse("INVALID_REQUEST", "Identity Provider ID is required", 
                    Response.Status.BAD_REQUEST);
            }

            // Execute debug flow.
            String sessionId = debugService.executeDebugFlow(
                idpId, 
                debugConnectionRequest.getUsername(), 
                debugConnectionRequest.getPassword()
            );

            // Create response.
            DebugConnectionResponse response = new DebugConnectionResponse();
            response.setSessionId(sessionId);
            response.setStatus("SUCCESS");
            response.setMessage("Debug connection completed successfully");
            
            // Add metadata.
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("idpId", idpId);
            metadata.put("username", debugConnectionRequest.getUsername());
            metadata.put("timestamp", System.currentTimeMillis());
            response.setMetadata(metadata);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Debug connection completed for IdP: " + idpId + ", sessionId: " + sessionId);
            }

            return Response.ok(response).build();

        } catch (APIError e) {
            LOG.error("API error in debug connection for IdP: " + idpId, e);
            return createErrorResponse(e.getCode(), e.getMessage(), mapToHttpStatus(e.getCode()));
        } catch (Exception e) {
            LOG.error("Unexpected error in debug connection for IdP: " + idpId, e);
            return createErrorResponse("INTERNAL_ERROR", "An unexpected error occurred", 
                Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates an HTTP error response.
     *
     * @param errorCode Error code.
     * @param errorMessage Error message.
     * @param status HTTP status.
     * @return HTTP Response with error details.
     */
    private Response createErrorResponse(String errorCode, String errorMessage, Response.Status status) {
        DebugConnectionResponse errorResponse = new DebugConnectionResponse();
        errorResponse.setSessionId(java.util.UUID.randomUUID().toString());
        errorResponse.setStatus("FAILURE");
        errorResponse.setMessage(errorMessage);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("code", errorCode);
        errorDetails.put("message", errorMessage);
        errorResponse.setMetadata(errorDetails);

        return Response.status(status).entity(errorResponse).build();
    }

    /**
     * Maps API error codes to HTTP status codes.
     *
     * @param errorCode API error code.
     * @return HTTP status code.
     */
    private Response.Status mapToHttpStatus(String errorCode) {
        if (errorCode == null) {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
        
        switch (errorCode.toUpperCase()) {
            case "IDP_NOT_FOUND":
                return Response.Status.NOT_FOUND;
            case "INVALID_REQUEST":
                return Response.Status.BAD_REQUEST;
            case "AUTHENTICATION_FAILED":
                return Response.Status.UNAUTHORIZED;
            default:
                return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
}
