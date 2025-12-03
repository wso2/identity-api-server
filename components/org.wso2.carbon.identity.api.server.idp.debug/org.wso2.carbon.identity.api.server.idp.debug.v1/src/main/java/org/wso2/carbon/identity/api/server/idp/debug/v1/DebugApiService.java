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

package org.wso2.carbon.identity.api.server.idp.debug.v1;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.idp.debug.v1.service.DebugService;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

/**
 * Implementation of Debug API service for testing Identity Provider authentication flows.
 */
public class DebugApiService {

    private static final Log LOG = LogFactory.getLog(DebugApiService.class);
    private final DebugService debugService;

    /**
     * Constructor initializes the service layer.
     */
    public DebugApiService() {
        this.debugService = new DebugService();
    }

    /**
     * Starts a debug session for any resource type with custom properties.
     * Initiates debugging for the specified resource and generates appropriate response.
     *
     * @param debugRequest Debug request containing resourceId, resourceType, and properties.
     * @return Response containing debug result and session information.
     */
    public Response startDebugSession(DebugConnectionRequest debugRequest) {
        try {
            // Input validation at API layer.
            if (debugRequest == null) {
                return createErrorResponse("INVALID_REQUEST", "Debug request is required", 
                    Response.Status.BAD_REQUEST);
            }

            String resourceId = debugRequest.getResourceId();
            String resourceType = debugRequest.getResourceType();

            if (resourceId == null || resourceId.trim().isEmpty()) {
                return createErrorResponse("INVALID_REQUEST", "Resource ID is required", 
                    Response.Status.BAD_REQUEST);
            }

            if (resourceType == null || resourceType.trim().isEmpty()) {
                return createErrorResponse("INVALID_REQUEST", "Resource type is required", 
                    Response.Status.BAD_REQUEST);
            }

            // Delegate to DebugService for resource-type-specific handling.
            Map<String, Object> debugResult = debugService.handleGenericDebugRequest(
                resourceId, resourceType, debugRequest.getProperties()
            );

            // Create response from debug result.
            DebugConnectionResponse response = createDebugResponse(debugResult, resourceId, resourceType);
            return Response.ok(response).build();

        } catch (Exception e) {
            String sanitizedResourceId = debugRequest != null && debugRequest.getResourceId() != null ? 
                debugRequest.getResourceId().replaceAll("[\r\n]", "") : "null";
            LOG.error("Unexpected error in generic debug request for resource: " + sanitizedResourceId, e);
            return createErrorResponse("INTERNAL_ERROR", 
                "Failed to process debug request: " + e.getMessage(), 
                Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Debug IdP connection with OAuth 2.0 flow.
     * Generates authorization URL for user authentication.
     *
     * @param idpId Identity Provider ID from path parameter.
     * @param debugConnectionRequest Debug connection request.
     * @return Response containing OAuth 2.0 authorization URL and session information.
     */
    public Response debugConnection(String idpId, DebugConnectionRequest debugConnectionRequest) {
        try {
            // Input validation at API layer.
            if (idpId == null || idpId.trim().isEmpty()) {
                return createErrorResponse("INVALID_REQUEST", "Identity Provider ID is required", 
                    Response.Status.BAD_REQUEST);
            }

            // Extract properties from request if available (all optional).
            Map<String, String> properties = null;
            
            if (debugConnectionRequest != null) {
                properties = debugConnectionRequest.getProperties();
            }

            // Generate OAuth 2.0 authorization URL using the service layer.
            Map<String, Object> oauth2Result = debugService.generateOAuth2AuthorizationUrl(
                idpId, properties
            );

            // Create OAuth 2.0 response.
            DebugConnectionResponse response = createOAuth2Response(oauth2Result, idpId);
            return Response.ok(response).build();

        } catch (Exception e) {
            String sanitizedIdpId = idpId != null ? idpId.replaceAll("[\r\n]", "") : "null";
            LOG.error("Unexpected error in OAuth 2.0 debug connection for IdP: " + sanitizedIdpId, e);
            return createErrorResponse("INTERNAL_ERROR", 
                "Failed to generate OAuth 2.0 authorization URL: " + e.getMessage(), 
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
        errorResponse.setDebugId(java.util.UUID.randomUUID().toString());
        errorResponse.setStatus("FAILURE");
        errorResponse.setMessage(errorMessage);

        return Response.status(status).entity(errorResponse).build();
    }

    /**
     * Creates OAuth 2.0 response from service layer result.
     *
     * @param oauth2Result OAuth 2.0 generation result from service layer.
     * @param idpId Identity Provider ID.
     * @return DebugConnectionResponse with OAuth 2.0 authorization URL.
     */
    private DebugConnectionResponse createOAuth2Response(Map<String, Object> oauth2Result, String idpId) {
        DebugConnectionResponse response = new DebugConnectionResponse();
        
        // Extract or generate debug ID (state parameter).
        String debugId = (String) oauth2Result.get("sessionId");
        if (debugId == null) {
            // Fall back to state if sessionId not present (state is what OAuth2Executor returns)
            debugId = (String) oauth2Result.get("state");
        }
        if (debugId == null) {
            // Generate a debug ID if neither present
            debugId = "debug-" + java.util.UUID.randomUUID().toString().replace("-", "");
        }
        response.setDebugId(debugId);
        
        // Set authorization URL
        String authUrl = (String) oauth2Result.get("authorizationUrl");
        response.setAuthorizationUrl(authUrl);
        
        // Set status and message
        response.setStatus((String) oauth2Result.get("status"));
        response.setMessage((String) oauth2Result.get("message"));
        
        // Set timestamp
        Long timestamp = null;
        Object timestampObj = oauth2Result.get("timestamp");
        if (timestampObj instanceof Long) {
            timestamp = (Long) timestampObj;
        } else if (timestampObj instanceof Number) {
            timestamp = ((Number) timestampObj).longValue();
        } else {
            timestamp = System.currentTimeMillis();
        }
        response.setTimestamp(timestamp);
        
        return response;
    }

    /**
     * Creates debug response from generic debug result.
     *
     * @param debugResult Debug result from service layer.
     * @param resourceId Resource ID being debugged.
     * @param resourceType Resource type being debugged.
     * @return DebugConnectionResponse with debug information.
     */
    private DebugConnectionResponse createDebugResponse(Map<String, Object> debugResult, String resourceId, String resourceType) {
        DebugConnectionResponse response = new DebugConnectionResponse();
        
        // Extract or generate debug ID.
        String debugId = (String) debugResult.get("sessionId");
        if (debugId == null) {
            debugId = (String) debugResult.get("state");
        }
        if (debugId == null) {
            debugId = "debug-" + java.util.UUID.randomUUID().toString().replace("-", "");
        }
        response.setDebugId(debugId);
        
        // Set authorization URL if available.
        String authUrl = (String) debugResult.get("authorizationUrl");
        if (authUrl != null) {
            response.setAuthorizationUrl(authUrl);
        }
        
        // Set status and message.
        response.setStatus((String) debugResult.getOrDefault("status", "SUCCESS"));
        response.setMessage((String) debugResult.getOrDefault("message", "Debug request processed successfully"));
        
        // Set timestamp at top level.
        Long timestamp = null;
        Object timestampObj = debugResult.get("timestamp");
        if (timestampObj instanceof Long) {
            timestamp = (Long) timestampObj;
        } else if (timestampObj instanceof Number) {
            timestamp = ((Number) timestampObj).longValue();
        } else {
            timestamp = System.currentTimeMillis();
        }
        response.setTimestamp(timestamp);
        
        return response;
    }
}
