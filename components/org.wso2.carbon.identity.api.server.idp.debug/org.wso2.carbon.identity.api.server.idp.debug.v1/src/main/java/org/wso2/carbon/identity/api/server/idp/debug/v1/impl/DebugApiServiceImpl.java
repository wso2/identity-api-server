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

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.idp.debug.v1.DebugApiService;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.idp.debug.v1.service.SimpleDebugService;

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
     * Debug IdP connection with OAuth 2.0 flow.
     * Generates authorization URL for user authentication.
     *
     * @param idpId Identity Provider ID from path parameter.
     * @param debugConnectionRequest Debug connection request containing OAuth 2.0 parameters.
     * @return Response containing OAuth 2.0 authorization URL and session information.
     */
    @Override
    public Response debugConnection(String idpId, DebugConnectionRequest debugConnectionRequest) {
        try {
            // Input validation at API layer.
            if (idpId == null || idpId.trim().isEmpty()) {
                return createErrorResponse("INVALID_REQUEST", "Identity Provider ID is required", 
                    Response.Status.BAD_REQUEST);
            }

            // Extract OAuth 2.0 parameters from request (all optional).
            String authenticatorName = null;
            String redirectUri = null;
            String scope = null;
            Map<String, String> additionalParams = null;
            
            if (debugConnectionRequest != null) {
                authenticatorName = debugConnectionRequest.getAuthenticatorName();
                redirectUri = debugConnectionRequest.getRedirectUri();
                scope = debugConnectionRequest.getScope();
                additionalParams = debugConnectionRequest.getAdditionalParams();
            }

            // Generate OAuth 2.0 authorization URL using the service layer.
            Map<String, Object> oauth2Result = debugService.generateOAuth2AuthorizationUrl(
                idpId, authenticatorName, redirectUri, scope, additionalParams
            );

            // Create OAuth 2.0 response.
            DebugConnectionResponse response = createOAuth2Response(oauth2Result, idpId);
            return Response.ok(response).build();

        } catch (APIError e) {
            LOG.error("API error in OAuth 2.0 debug connection for IdP: " + idpId, e);
            return createErrorResponse(e.getCode(), e.getMessage(), mapToHttpStatus(e.getCode()));
        } catch (Exception e) {
            LOG.error("Unexpected error in OAuth 2.0 debug connection for IdP: " + idpId, e);
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
     * Creates OAuth 2.0 response from service layer result.
     *
     * @param oauth2Result OAuth 2.0 generation result from service layer.
     * @param idpId Identity Provider ID.
     * @return DebugConnectionResponse with OAuth 2.0 authorization URL.
     */
    private DebugConnectionResponse createOAuth2Response(Map<String, Object> oauth2Result, String idpId) {
        DebugConnectionResponse response = new DebugConnectionResponse();
        
        // Set basic response data.
        response.setSessionId((String) oauth2Result.get("sessionId"));
        response.setAuthorizationUrl((String) oauth2Result.get("authorizationUrl"));
        response.setStatus((String) oauth2Result.get("status"));
        response.setMessage((String) oauth2Result.get("message"));

        // Create comprehensive metadata.
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("idpId", idpId);
        metadata.put("idpName", oauth2Result.get("idpName"));
        metadata.put("authenticatorName", oauth2Result.get("authenticatorName"));
        metadata.put("timestamp", oauth2Result.get("timestamp"));
        metadata.put("flow", "OAuth 2.0 Authorization Code with PKCE");
        metadata.put("nextStep", "Redirect user to authorizationUrl for authentication");
        metadata.put("callbackEndpoint", "/commonauth");
        
        response.setMetadata(metadata);
        return response;
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
        
        switch (errorCode.toUpperCase(java.util.Locale.ROOT)) {
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
