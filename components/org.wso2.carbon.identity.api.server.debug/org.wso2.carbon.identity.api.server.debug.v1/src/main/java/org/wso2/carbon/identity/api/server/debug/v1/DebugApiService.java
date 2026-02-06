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

package org.wso2.carbon.identity.api.server.debug.v1;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.debug.v1.service.DebugService;

import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Response;

/**
 * Implementation of Debug API service for testing Identity Provider
 * authentication flows.
 * 
 * This service handles debug requests for identity providers, supporting both
 * generic resource debugging and IdP-specific OAuth 2.0 flow debugging.
 * 
 * The service delegates actual debug operations to {@link DebugService} which
 * coordinates with the debug framework.
 */
public class DebugApiService {

    private static final Log LOG = LogFactory.getLog(DebugApiService.class);

    // Constants for status values
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_FAILURE = "FAILURE";

    // Constants for result map keys
    private static final String KEY_SESSION_ID = "sessionId";
    private static final String KEY_STATE = "state";
    private static final String KEY_AUTHORIZATION_URL = "authorizationUrl";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_TIMESTAMP = "timestamp";

    private final DebugService debugService;

    /**
     * Constructor initializes the service layer.
     */
    public DebugApiService() {
        this.debugService = new DebugService();
    }

    /**
     * Starts a debug session for any resource type with custom properties.
     * This is the primary entry point for debug requests.
     *
     * @param debugRequest Debug request containing resourceId, resourceType, and
     *                     properties.
     * @return Response containing debug result and session information.
     */
    public Response startDebugSession(DebugConnectionRequest debugRequest) {

        try {
            // Input validation
            Response validationError = validateDebugRequest(debugRequest);
            if (validationError != null) {
                return validationError;
            }

            String resourceId = debugRequest.getResourceId();
            String resourceType = debugRequest.getResourceType();

            // Delegate to DebugService for resource-type-specific handling
            Map<String, Object> debugResult = debugService.handleGenericDebugRequest(
                    resourceId, resourceType, debugRequest.getProperties());

            // Create and return response
            DebugConnectionResponse response = createDebugResponse(debugResult);
            return Response.ok(response).build();

        } catch (Exception e) {
            return handleException("generic debug request", getResourceId(debugRequest), e);
        }
    }

    /**
     * Debug IdP connection with OAuth 2.0 flow.
     * Generates authorization URL for user authentication.
     *
     * @param idpId                  Identity Provider ID from path parameter.
     * @param debugConnectionRequest Debug connection request (optional).
     * @return Response containing OAuth 2.0 authorization URL and session
     *         information.
     */
    public Response debugConnection(String idpId, DebugConnectionRequest debugConnectionRequest) {

        try {
            // Input validation
            if (idpId == null || idpId.trim().isEmpty()) {
                return createErrorResponse("INVALID_REQUEST", "Identity Provider ID is required",
                        Response.Status.BAD_REQUEST);
            }

            // Extract properties from request if available
            Map<String, String> properties = null;
            if (debugConnectionRequest != null) {
                properties = debugConnectionRequest.getProperties();
            }

            // Generate OAuth 2.0 authorization URL using the service layer
            Map<String, Object> oauth2Result = debugService.generateOAuth2AuthorizationUrl(idpId, properties);

            // Create and return response
            DebugConnectionResponse response = createDebugResponse(oauth2Result);
            return Response.ok(response).build();

        } catch (Exception e) {
            return handleException("OAuth 2.0 debug connection", idpId, e);
        }
    }

    /**
     * Validates the debug request for required fields.
     *
     * @param debugRequest The request to validate.
     * @return Error response if validation fails, null if valid.
     */
    private Response validateDebugRequest(DebugConnectionRequest debugRequest) {

        if (debugRequest == null) {
            return createErrorResponse("INVALID_REQUEST", "Debug request is required",
                    Response.Status.BAD_REQUEST);
        }

        if (debugRequest.getResourceId() == null || debugRequest.getResourceId().trim().isEmpty()) {
            return createErrorResponse("INVALID_REQUEST", "Resource ID is required",
                    Response.Status.BAD_REQUEST);
        }

        if (debugRequest.getResourceType() == null || debugRequest.getResourceType().trim().isEmpty()) {
            return createErrorResponse("INVALID_REQUEST", "Resource type is required",
                    Response.Status.BAD_REQUEST);
        }

        return null;
    }

    /**
     * Creates an HTTP error response.
     *
     * @param errorCode    Error code.
     * @param errorMessage Error message.
     * @param status       HTTP status.
     * @return HTTP Response with error details.
     */
    private Response createErrorResponse(String errorCode, String errorMessage, Response.Status status) {

        DebugConnectionResponse errorResponse = new DebugConnectionResponse();
        errorResponse.setDebugId(UUID.randomUUID().toString());
        errorResponse.setStatus(STATUS_FAILURE);
        errorResponse.setMessage(errorMessage);
        errorResponse.setTimestamp(System.currentTimeMillis());

        return Response.status(status).entity(errorResponse).build();
    }

    /**
     * Creates debug response from service layer result.
     * This method handles both OAuth2 and generic debug results.
     *
     * @param debugResult Debug result from service layer.
     * @return DebugConnectionResponse with debug information.
     */
    private DebugConnectionResponse createDebugResponse(Map<String, Object> debugResult) {

        DebugConnectionResponse response = new DebugConnectionResponse();

        // Extract or generate debug ID (state parameter)
        String debugId = extractDebugId(debugResult);
        response.setDebugId(debugId);

        // Set authorization URL if available
        Object authUrl = debugResult.get(KEY_AUTHORIZATION_URL);
        if (authUrl != null) {
            response.setAuthorizationUrl(authUrl.toString());
        }

        // Set status and message
        response.setStatus(getStringOrDefault(debugResult, KEY_STATUS, STATUS_SUCCESS));
        response.setMessage(getStringOrDefault(debugResult, KEY_MESSAGE, "Debug request processed successfully"));

        // Set timestamp
        response.setTimestamp(extractTimestamp(debugResult));

        return response;
    }

    /**
     * Extracts the debug ID from the result map.
     *
     * @param debugResult The result map.
     * @return The debug ID, generating a new one if not found.
     */
    private String extractDebugId(Map<String, Object> debugResult) {

        String debugId = (String) debugResult.get(KEY_SESSION_ID);
        if (debugId == null) {
            debugId = (String) debugResult.get(KEY_STATE);
        }
        if (debugId == null) {
            debugId = "debug-" + UUID.randomUUID().toString().replace("-", "");
        }
        return debugId;
    }

    /**
     * Extracts timestamp from the result map.
     *
     * @param debugResult The result map.
     * @return The timestamp, or current time if not found.
     */
    private Long extractTimestamp(Map<String, Object> debugResult) {

        Object timestampObj = debugResult.get(KEY_TIMESTAMP);
        if (timestampObj instanceof Long) {
            return (Long) timestampObj;
        } else if (timestampObj instanceof Number) {
            return ((Number) timestampObj).longValue();
        }
        return System.currentTimeMillis();
    }

    /**
     * Gets a string value from the map with a default fallback.
     *
     * @param map          The map to read from.
     * @param key          The key to look up.
     * @param defaultValue The default value if key is not found.
     * @return The string value or default.
     */
    private String getStringOrDefault(Map<String, Object> map, String key, String defaultValue) {

        Object value = map.get(key);
        return value != null ? value.toString() : defaultValue;
    }

    /**
     * Handles exceptions by logging and creating an error response.
     *
     * @param operation  Description of the operation that failed.
     * @param resourceId The resource ID for logging.
     * @param e          The exception that occurred.
     * @return Error response.
     */
    private Response handleException(String operation, String resourceId, Exception e) {

        String sanitizedResourceId = sanitizeForLogging(resourceId);
        LOG.error("Unexpected error in " + operation + " for resource: " + sanitizedResourceId, e);
        return createErrorResponse("INTERNAL_ERROR",
                "Failed to process debug request: " + e.getMessage(),
                Response.Status.INTERNAL_SERVER_ERROR);
    }

    /**
     * Sanitizes a string for logging (removes newlines to prevent log injection).
     *
     * @param value The value to sanitize.
     * @return Sanitized string.
     */
    private String sanitizeForLogging(String value) {

        return value != null ? value.replaceAll("[\\r\\n]", "") : "null";
    }

    /**
     * Safely gets the resource ID from a debug request for logging.
     *
     * @param debugRequest The debug request.
     * @return The resource ID or "null".
     */
    private String getResourceId(DebugConnectionRequest debugRequest) {

        return debugRequest != null ? debugRequest.getResourceId() : null;
    }
}
