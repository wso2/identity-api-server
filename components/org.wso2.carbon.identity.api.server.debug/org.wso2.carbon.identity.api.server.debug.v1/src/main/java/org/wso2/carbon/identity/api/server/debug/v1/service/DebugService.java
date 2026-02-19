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

package org.wso2.carbon.identity.api.server.debug.v1.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.debug.common.Constants;
import org.wso2.carbon.identity.api.server.debug.common.DebugFrameworkServiceHolder;
import org.wso2.carbon.identity.debug.framework.core.DebugRequestCoordinator;
import org.wso2.carbon.identity.debug.framework.model.DebugRequest;
import org.wso2.carbon.identity.debug.framework.model.DebugResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Service layer for debug operations.
 * 
 * This service provides a clean interface for the API layer and delegates
 * to debug framework components. It handles:
 * Generic resource debug requests
 * OAuth 2.0 authorization URL generation for debugging
 *
 * The service uses the {@link DebugRequestCoordinator} for centralized
 * request routing and protocol-specific handling.
 */

public class DebugService {

    private static final Log LOG = LogFactory.getLog(DebugService.class);

    // Request type constants
    private static final String REQUEST_TYPE_GENERIC = "GENERIC_DEBUG_REQUEST";
    private static final String REQUEST_TYPE_INITIAL = "INITIAL_DEBUG_REQUEST";

    // Context key constants
    private static final String KEY_CONNECTION_ID = "connectionId";
    private static final String KEY_RESOURCE_TYPE = "resourceType";
    private static final String KEY_PROPERTIES = "properties";
    private static final String KEY_REQUEST_TYPE = "requestType";
    private static final String KEY_IDP_ID = "idpId";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_STATUS = "status";

    /**
     * Constructor initializes debug framework service via service holder pattern.
     */
    public DebugService() {

        // Debug framework services are loaded on-demand
    }

    /**
     * Handles generic debug request for any resource type with properties.
     * The connectionId is optional at this level and may be null for resource types
     * that don't require it.
     *
     * @param connectionId   Connection ID to debug (can be null for some resource
     *                     types).
     * @param resourceType Type of resource (IDP, APPLICATION, CONNECTOR, etc.).
     * @param properties   Generic properties map for the debug request (optional).
     * @return Debug result containing session information and status.
     * @throws RuntimeException if debug request fails.
     */
    public Map<String, Object> handleGenericDebugRequest(String connectionId, String resourceType,
            Map<String, String> properties) {

        // Build typed request. connectionId can be null for resource types that don't
        // need it.
        DebugRequest debugRequest = new DebugRequest();
        debugRequest.setResourceType(resourceType);
        if (connectionId != null) {
            debugRequest.setConnectionId(connectionId);
        }
        if (properties != null) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                debugRequest.addContextProperty(entry.getKey(), entry.getValue());
            }
        }
        debugRequest.addContextProperty(KEY_REQUEST_TYPE, REQUEST_TYPE_GENERIC);

        // Execute and get result.
        DebugResponse response = executeDebugRequest("handleResourceDebugRequest", debugRequest);

        // Convert to Map and add metadata.
        Map<String, Object> resultMap = response.getData();
        resultMap.put(KEY_TIMESTAMP, System.currentTimeMillis());
        if (connectionId != null) {
            resultMap.put(KEY_CONNECTION_ID, connectionId);
        }
        resultMap.put(KEY_RESOURCE_TYPE, resourceType);
        resultMap.putIfAbsent(KEY_STATUS, Constants.Status.SUCCESS);

        return resultMap;
    }

    /**
     * Generates OAuth 2.0 authorization URL for debug flow.
     *
     * @param idpId      Identity Provider resource ID.
     * @param properties Optional properties for OAuth 2.0 configuration.
     * @return OAuth 2.0 authorization URL and session information.
     * @throws RuntimeException if URL generation fails.
     */
    public Map<String, Object> generateOAuth2AuthorizationUrl(String idpId, Map<String, String> properties) {

        // Build request context
        Map<String, Object> debugRequestContext = new HashMap<>();
        debugRequestContext.put(KEY_IDP_ID, idpId);
        debugRequestContext.put(KEY_PROPERTIES, properties != null ? properties : new HashMap<String, String>());
        debugRequestContext.put(KEY_REQUEST_TYPE, REQUEST_TYPE_INITIAL);

        // Convert to typed request.
        DebugRequest debugRequest = DebugRequest.fromMap(debugRequestContext);

        // Execute and get result.
        DebugResponse response = executeDebugRequest("handleInitialDebugRequest", debugRequest);

        // Get response data as map.
        Map<String, Object> resultMap = response.getData();

        // Add metadata.
        resultMap.put(KEY_TIMESTAMP, System.currentTimeMillis());
        resultMap.put(KEY_IDP_ID, idpId);
        resultMap.put(KEY_STATUS, Constants.Status.SUCCESS);

        return resultMap;
    }

    /**
     * Retrieves the debug result for the given session ID.
     * Uses direct access to the debug framework cache.
     *
     * @param sessionId The session ID to look up.
     * @return The debug result JSON string, or null if not found.
     */
    public String getDebugResult(String sessionId) {
        try {
            // Delegate to coordinator which handles listeners and cleanup.
            DebugRequestCoordinator coordinator = getCoordinatorOrThrow();
            return coordinator.getDebugResult(sessionId);

        } catch (RuntimeException e) {
            // Coordinator not available (framework not deployed/active).
            if (LOG.isDebugEnabled()) {
                LOG.debug("Debug framework not available for result retrieval.");
            }
        } catch (Exception e) {
            LOG.error("Error retrieving debug result via service.", e);
        }
        return null;
    }

    /**
     * Executes a debug request via the DebugRequestCoordinator using typed classes.
     *
     * @param methodName   The coordinator method to invoke.
     * @param debugRequest The typed debug request.
     * @return The debug response.
     * @throws RuntimeException if execution fails.
     */
    protected DebugResponse executeDebugRequest(String methodName, DebugRequest debugRequest) {

        try {
            // Ensure debug framework is available.
            DebugRequestCoordinator coordinator = getCoordinatorOrThrow();

            // Invoke the coordinator method directly.
            DebugResponse response;
            if ("handleResourceDebugRequest".equals(methodName)) {
                response = coordinator.handleResourceDebugRequest(debugRequest);
            } else if ("handleInitialDebugRequest".equals(methodName)) {
                response = coordinator.handleInitialDebugRequest(debugRequest);
            } else {
                throw new RuntimeException("Unknown coordinator method: " + methodName);
            }

            if (response == null) {
                throw new RuntimeException("Debug request returned null result");
            }

            return response;

        } catch (RuntimeException e) {
            logError("Runtime error in debug request", e);
            throw e;
        } catch (Exception e) {
            logError("Unexpected error in debug request", e);
            throw new RuntimeException("Failed to process debug request: " + sanitize(e.getMessage()), e);
        }
    }

    /**
     * Gets the DebugRequestCoordinator or throws if unavailable.
     *
     * @return The coordinator instance.
     * @throws RuntimeException if coordinator is not available.
     */
    protected DebugRequestCoordinator getCoordinatorOrThrow() {

        if (!DebugFrameworkServiceHolder.isDebugFrameworkAvailable()) {
            throw new RuntimeException("Debug framework services are not available");
        }

        DebugRequestCoordinator coordinator = DebugFrameworkServiceHolder.getDebugRequestCoordinator();
        if (coordinator == null) {
            throw new RuntimeException("DebugRequestCoordinator not available");
        }

        return coordinator;
    }

    /**
     * Extracts debug result data from the result map.
     *
     * @param result The result map from the coordinator.
     * @return Map containing extracted result data.
     */
    protected Map<String, Object> extractDebugResultData(Map<String, Object> result) {

        Map<String, Object> extractedData = new HashMap<>();

        try {
            result.forEach((key, value) -> {
                if (key != null && value != null) {
                    extractedData.put(key, value);
                }
            });
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error extracting debug result data.", e);
            }
        }

        return extractedData;
    }

    /**
     * Logs an error with sanitized message.
     *
     * @param context Error context description.
     * @param e       The exception.
     */
    protected void logError(String context, Exception e) {

        LOG.error("Error during debug request processing.", e);
    }

    /**
     * Sanitizes a string for logging (removes newlines).
     *
     * @param value The value to sanitize.
     * @return Sanitized string.
     */
    protected String sanitize(String value) {

        if (value == null) {
            return "";
        }
        return value.replaceAll("[\r\n]", "_");
    }
}
