/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.debug.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.debug.common.Constants;
import org.wso2.carbon.identity.api.server.debug.v1.DebugApiService;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.debug.v1.service.DebugService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.Response;

/**
 * Implementation of Debug API service for testing Identity Provider
 * authentication flows.
 * 
 * This service handles debug requests for different resource types including
 * Identity Providers,
 * applications, connectors, and authenticators. Provides methods for starting
 * debug
 * sessions and retrieving debug results.
 */
public class DebugApiServiceImpl extends DebugApiService {

    private static final Log LOG = LogFactory.getLog(DebugApiServiceImpl.class);

    // Allowed resource types for validation (using constants from common module)
    private static final Set<String> ALLOWED_RESOURCE_TYPES = new HashSet<>(Arrays.asList(
            Constants.ResourceType.IDP,
            Constants.ResourceType.APPLICATION,
            Constants.ResourceType.CONNECTOR,
            Constants.ResourceType.AUTHENTICATOR));

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
    public DebugApiServiceImpl() {

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
    @Override
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
            return handleException(e);
        }
    }

    /**
     * Retrieves a previous debug result by session ID.
     *
     * @param sessionId The debug session ID.
     * @return Response containing the debug result or an error.
     */
    private static final com.fasterxml.jackson.databind.ObjectMapper OBJECT_MAPPER 
        = new com.fasterxml.jackson.databind.ObjectMapper();
    private static final String[] STEP_STATUS_KEYS = {
            "step_connection_status",
            "step_authentication_status",
            "step_claim_mapping_status"
    };

    @Override
    public Response getDebugResult(String sessionId) {

        String resultJson = debugService.getDebugResult(sessionId);

        if (resultJson == null) {
            return createErrorResponse("DEBUG_RESULT_NOT_FOUND", "Debug result not found", Response.Status.NOT_FOUND);
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> resultMap = OBJECT_MAPPER.readValue(resultJson, Map.class);

            // Ensure metadata exists
            @SuppressWarnings("unchecked")
            Map<String, Object> metadata = (Map<String, Object>) resultMap.get("metadata");
            if (metadata == null) {
                metadata = new java.util.HashMap<>();
                resultMap.put("metadata", metadata);
            }

            // Copy step status fields to metadata
            for (String key : STEP_STATUS_KEYS) {
                if (resultMap.containsKey(key)) {
                    metadata.put(key, resultMap.get(key));
                }
            }

            String enrichedJson = OBJECT_MAPPER.writeValueAsString(resultMap);
            return Response.ok(enrichedJson).build();

        } catch (java.io.IOException e) {
            LOG.error("Failed to process debug result.", e);
            return createErrorResponse("PROCESSING_ERROR", "Failed to process debug result",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Validates the debug request for required fields.
     *
     * @param debugRequest The request to validate.
     * @return Error response if validation fails, null if valid.
     */
    protected Response validateDebugRequest(DebugConnectionRequest debugRequest) {

        if (debugRequest == null) {
            return createErrorResponse("INVALID_REQUEST", "Debug request is required",
                    Response.Status.BAD_REQUEST);
        }

        if (debugRequest.getResourceId() == null || debugRequest.getResourceId().trim().isEmpty()) {
            return createErrorResponse("INVALID_REQUEST", "Resource ID is required",
                    Response.Status.BAD_REQUEST);
        }

        String resourceType = debugRequest.getResourceType();
        if (resourceType == null || resourceType.trim().isEmpty()) {
            return createErrorResponse("INVALID_REQUEST", "Resource type is required",
                    Response.Status.BAD_REQUEST);
        }

        // Validate resource type against allowed values
        if (!ALLOWED_RESOURCE_TYPES.contains(resourceType.toUpperCase(java.util.Locale.ENGLISH))) {
            return createErrorResponse("INVALID_REQUEST",
                    "Invalid resource type. Allowed values: " + ALLOWED_RESOURCE_TYPES,
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
    protected Response createErrorResponse(String errorCode, String errorMessage, Response.Status status) {

        DebugConnectionResponse errorResponse = new DebugConnectionResponse();
        errorResponse.setDebugId(UUID.randomUUID().toString());
        errorResponse.setStatus(Constants.Status.FAILURE);
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
    protected DebugConnectionResponse createDebugResponse(Map<String, Object> debugResult) {

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
        response.setStatus(getStringOrDefault(debugResult, KEY_STATUS, Constants.Status.SUCCESS));
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
    protected String extractDebugId(Map<String, Object> debugResult) {

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
    protected Long extractTimestamp(Map<String, Object> debugResult) {

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
    protected String getStringOrDefault(Map<String, Object> map, String key, String defaultValue) {

        Object value = map.get(key);
        return value != null ? value.toString() : defaultValue;
    }

    /**
     * Handles exceptions by logging and creating an error response.
     *
     * @param e The exception that occurred.
     * @return Error response.
     */
    private Response handleException(Exception e) {

        LOG.error("Unexpected error processing debug request.", e);
        return createErrorResponse("INTERNAL_ERROR",
                "Failed to process debug request.",
                Response.Status.INTERNAL_SERVER_ERROR);
    }
}
