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

import com.fasterxml.jackson.databind.ObjectMapper;

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
            Constants.ResourceType.FRAUD_DETECTION));

    // Validation constants.
    private static final int MAX_PROPERTIES_SIZE = 50;
    private static final int MAX_PROPERTY_KEY_LENGTH = 100;
    private static final int MAX_PROPERTY_VALUE_LENGTH = 2048;
    private static final int MAX_RESOURCE_TYPE_LENGTH = 50;
    private static final String SESSION_ID_PATTERN = "^[a-zA-Z0-9_-]+$";

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
     * @param debugRequest Debug request containing resourceType and properties.
     * @return Response containing debug result and session information.
     */
    @Override
    public Response startDebugSession(DebugConnectionRequest debugRequest) {

        try {
            // Input validation.
            Response validationError = validateDebugRequest(debugRequest);
            if (validationError != null) {
                return validationError;
            }

            String resourceType = debugRequest.getResourceType();

            // Map connectionId from properties to connectionId for framework compatibility.
            String connectionId = extractConnectionIdFromProperties(debugRequest.getProperties());

            // Delegate to DebugService for resource-type-specific handling.
            Map<String, Object> debugResult = debugService.handleGenericDebugRequest(
                    connectionId, resourceType, debugRequest.getProperties());

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
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String[] STEP_STATUS_KEYS = {
            "step_connection_status",
            "step_authentication_status",
            "step_claim_mapping_status"
    };

    @Override
    public Response getDebugResult(String sessionId) {

        // Validate session ID.
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return createErrorResponse("INVALID_REQUEST", "Session ID is required.",
                    Response.Status.BAD_REQUEST);
        }

        // Validate session ID format (alphanumeric, hyphens, and underscores only).
        if (!sessionId.matches(SESSION_ID_PATTERN)) {
            return createErrorResponse("INVALID_REQUEST",
                    "Invalid session ID format. Only alphanumeric characters, hyphens, and underscores are allowed.",
                    Response.Status.BAD_REQUEST);
        }

        // Validate session ID length.
        if (sessionId.length() > MAX_PROPERTY_VALUE_LENGTH) {
            return createErrorResponse("INVALID_REQUEST",
                    "Session ID exceeds maximum length.",
                    Response.Status.BAD_REQUEST);
        }

        String resultJson = debugService.getDebugResult(sessionId);

        if (resultJson == null) {
            return createErrorResponse("DEBUG_RESULT_NOT_FOUND", "Debug result not found", Response.Status.NOT_FOUND);
        }

        try {
            Map<String, Object> protocolResult = OBJECT_MAPPER.readValue(resultJson, Map.class);

            // Restructure protocol-specific result to generic API format.
            Map<String, Object> apiResponse = restructureForGenericApi(protocolResult);

            String enrichedJson = OBJECT_MAPPER.writeValueAsString(apiResponse);
            return Response.ok(enrichedJson).build();

        } catch (java.io.IOException e) {
            LOG.error("Failed to process debug result.", e);
            return createErrorResponse("PROCESSING_ERROR", "Failed to process debug result",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Restructures protocol-specific debug result to generic API contract.
     * Ensures only sessionId and success are at top level, everything else goes to
     * metadata.
     * This makes the API response generic and independent of protocol
     * implementation.
     *
     * @param protocolResult Debug result from protocol processor (may have mixed
     *                       structure).
     * @return Restructured result following generic API contract.
     */
    private Map<String, Object> restructureForGenericApi(Map<String, Object> protocolResult) {

        Map<String, Object> apiResponse = new java.util.HashMap<>();

        // First-class properties (generic, always present).
        String sessionId = (String) protocolResult.getOrDefault("sessionId", "");
        boolean success = (boolean) protocolResult.getOrDefault("success", false);

        apiResponse.put("sessionId", sessionId);
        apiResponse.put("success", success);

        // All other fields go to metadata.
        Map<String, Object> metadata = new java.util.HashMap<>();

        for (java.util.Map.Entry<String, Object> entry : protocolResult.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Skip first-class properties and null values.
            if (!key.equals("sessionId") && !key.equals("success") && value != null) {
                metadata.put(key, value);
            }
        }

        apiResponse.put("metadata", metadata);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Restructured debug result for generic API response with metadata");
        }

        return apiResponse;
    }

    /**
     * Validates the debug request for required fields.
     * Resource type is always required. Resource ID is validated based on the
     * resource type.
     *
     * @param debugRequest The request to validate.
     * @return Error response if validation fails, null if valid.
     */
    protected Response validateDebugRequest(DebugConnectionRequest debugRequest) {

        if (debugRequest == null) {
            return createErrorResponse("INVALID_REQUEST", "Debug request is required.",
                    Response.Status.BAD_REQUEST);
        }

        String resourceType = debugRequest.getResourceType();
        if (resourceType == null || resourceType.trim().isEmpty()) {
            return createErrorResponse("INVALID_REQUEST", "Resource type is required.",
                    Response.Status.BAD_REQUEST);
        }

        // Validate resource type length.
        if (resourceType.length() > MAX_RESOURCE_TYPE_LENGTH) {
            return createErrorResponse("INVALID_REQUEST",
                    "Resource type exceeds maximum length of " + MAX_RESOURCE_TYPE_LENGTH + " characters.",
                    Response.Status.BAD_REQUEST);
        }

        // Validate resource type against allowed values.
        if (!ALLOWED_RESOURCE_TYPES.contains(resourceType.toUpperCase(java.util.Locale.ENGLISH))) {
            return createErrorResponse("INVALID_REQUEST",
                    "Invalid resource type. Allowed values: " + ALLOWED_RESOURCE_TYPES,
                    Response.Status.BAD_REQUEST);
        }

        // Validate properties map.
        Response propertiesValidation = validateProperties(debugRequest.getProperties());
        if (propertiesValidation != null) {
            return propertiesValidation;
        }

        // Resource-type-specific validation.
        return validateResourceTypeSpecificFields(debugRequest, resourceType);
    }

    /**
     * Validates the properties map for size and content.
     *
     * @param properties The properties map to validate.
     * @return Error response if validation fails, null if valid.
     */
    private Response validateProperties(java.util.Map<String, String> properties) {

        if (properties == null) {
            return null; // Properties are optional.
        }

        // Validate properties map size.
        if (properties.size() > MAX_PROPERTIES_SIZE) {
            return createErrorResponse("INVALID_REQUEST",
                    "Properties map exceeds maximum size of " + MAX_PROPERTIES_SIZE + " entries.",
                    Response.Status.BAD_REQUEST);
        }

        // Validate each property key and value.
        for (java.util.Map.Entry<String, String> entry : properties.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // Validate key.
            if (key == null || key.trim().isEmpty()) {
                return createErrorResponse("INVALID_REQUEST",
                        "Property key cannot be null or empty.",
                        Response.Status.BAD_REQUEST);
            }

            if (key.length() > MAX_PROPERTY_KEY_LENGTH) {
                return createErrorResponse("INVALID_REQUEST",
                        "Property key '" + key + "' exceeds maximum length of "
                                + MAX_PROPERTY_KEY_LENGTH + " characters.",
                        Response.Status.BAD_REQUEST);
            }

            // Validate value.
            if (value != null && value.length() > MAX_PROPERTY_VALUE_LENGTH) {
                return createErrorResponse("INVALID_REQUEST",
                        "Property value for key '" + key + "' exceeds maximum length of " + MAX_PROPERTY_VALUE_LENGTH
                                + " characters.",
                        Response.Status.BAD_REQUEST);
            }
        }

        return null;
    }

    /**
     * Validates fields that are required based on the resource type.
     * For example, IDP resource type requires a connectionId in the properties.
     *
     * @param debugRequest The request to validate.
     * @param resourceType The resource type (expected to be uppercase).
     * @return Error response if validation fails, null if valid.
     */
    private Response validateResourceTypeSpecificFields(DebugConnectionRequest debugRequest, String resourceType) {

        // Check for IDP resource type.
        if (Constants.ResourceType.IDP.equals(resourceType)) {
            java.util.Map<String, String> properties = debugRequest.getProperties();
            String connectionId = properties != null ? properties.get("connectionId") : null;
            if (connectionId == null || connectionId.trim().isEmpty()) {
                return createErrorResponse("INVALID_REQUEST",
                        "Connection ID is required in properties for IDP resource type.",
                        Response.Status.BAD_REQUEST);
            }
        }

        return null;
    }

    /**
     * Extracts connectionId from properties map.
     * Maps connectionId → connectionId for framework compatibility.
     *
     * @param properties The properties map.
     * @return The connectionId, or null if not found.
     */
    private String extractConnectionIdFromProperties(java.util.Map<String, String> properties) {

        if (properties == null) {
            return null;
        }

        // Check for connectionId first (API layer naming).
        String connectionId = properties.get("connectionId");
        if (connectionId != null) {
            // Map connectionId to connectionId for framework layer.
            return connectionId;
        }

        // Fallback to connectionId for backward compatibility.
        return properties.get("connectionId");
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

        // Extract or generate debug ID (state parameter).
        String debugId = extractDebugId(debugResult);
        response.setDebugId(debugId);

        // Set generic status (always SUCCESS for successfully generated debug
        // sessions).
        response.setStatus(Constants.Status.SUCCESS);

        // Set generic message.
        response.setMessage("Debug session executed successfully");

        // Set timestamp.
        response.setTimestamp(extractTimestamp(debugResult));

        // Create metadata map for resource-specific fields.
        java.util.Map<String, Object> metadata = new java.util.HashMap<>();

        // For IDP resources, only add authorization URL to metadata.
        Object authUrl = debugResult.get(KEY_AUTHORIZATION_URL);
        if (authUrl != null) {
            metadata.put("authorizationUrl", authUrl.toString());
        }

        // Only set metadata if it has content.
        if (!metadata.isEmpty()) {
            response.setMetadata(metadata);
        }

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
