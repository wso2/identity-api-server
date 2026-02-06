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

package org.wso2.carbon.identity.api.server.debug.v1.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.debug.common.DebugFrameworkServiceHolder;
import org.wso2.carbon.identity.debug.framework.core.DebugRequestCoordinator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Service layer for debug operations.
 * 
 * <p>
 * This service provides a clean interface for the API layer and delegates
 * to debug framework components. It handles:
 * </p>
 * <ul>
 * <li>Generic resource debug requests</li>
 * <li>OAuth 2.0 authorization URL generation for debugging</li>
 * </ul>
 * 
 * <p>
 * The service uses the {@link DebugRequestCoordinator} for centralized
 * request routing and protocol-specific handling.
 * </p>
 */
public class DebugService {

    private static final Log LOG = LogFactory.getLog(DebugService.class);

    // Request type constants
    private static final String REQUEST_TYPE_GENERIC = "GENERIC_DEBUG_REQUEST";
    private static final String REQUEST_TYPE_INITIAL = "INITIAL_DEBUG_REQUEST";

    // Context key constants
    private static final String KEY_RESOURCE_ID = "resourceId";
    private static final String KEY_RESOURCE_TYPE = "resourceType";
    private static final String KEY_PROPERTIES = "properties";
    private static final String KEY_REQUEST_TYPE = "requestType";
    private static final String KEY_IDP_ID = "idpId";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_STATUS = "status";

    private static final String STATUS_SUCCESS = "SUCCESS";

    /**
     * Constructor initializes debug framework service via service holder pattern.
     */
    public DebugService() {
        // Debug framework services are loaded on-demand
    }

    /**
     * Handles generic debug request for any resource type with properties.
     *
     * @param resourceId   Resource ID to debug.
     * @param resourceType Type of resource (IDP, APPLICATION, CONNECTOR, etc.).
     * @param properties   Generic properties map for the debug request (optional).
     * @return Debug result containing session information and status.
     * @throws RuntimeException if debug request fails.
     */
    public Map<String, Object> handleGenericDebugRequest(String resourceId, String resourceType,
            Map<String, String> properties) {

        // Build request context
        Map<String, Object> debugRequestContext = new HashMap<>();
        debugRequestContext.put(KEY_RESOURCE_ID, resourceId);
        debugRequestContext.put(KEY_RESOURCE_TYPE, resourceType);
        debugRequestContext.put(KEY_PROPERTIES, properties != null ? properties : new HashMap<String, String>());
        debugRequestContext.put(KEY_REQUEST_TYPE, REQUEST_TYPE_GENERIC);

        // Execute and get result
        Map<String, Object> resultMap = executeDebugRequest("handleResourceDebugRequest", debugRequestContext);

        // Add metadata
        resultMap.put(KEY_TIMESTAMP, System.currentTimeMillis());
        resultMap.put(KEY_RESOURCE_ID, resourceId);
        resultMap.put(KEY_RESOURCE_TYPE, resourceType);
        resultMap.putIfAbsent(KEY_STATUS, STATUS_SUCCESS);

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

        // Execute and get result
        Map<String, Object> resultMap = executeDebugRequest("handleInitialDebugRequest", debugRequestContext);

        // Add metadata
        resultMap.put(KEY_TIMESTAMP, System.currentTimeMillis());
        resultMap.put(KEY_IDP_ID, idpId);
        resultMap.put(KEY_STATUS, STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * Executes a debug request via the DebugRequestCoordinator.
     *
     * @param methodName The coordinator method to invoke.
     * @param context    The request context.
     * @return The extracted result map.
     * @throws RuntimeException if execution fails.
     */
    private Map<String, Object> executeDebugRequest(String methodName, Map<String, Object> context) {

        try {
            // Ensure debug framework is available
            DebugRequestCoordinator coordinator = getCoordinatorOrThrow();

            // Invoke the coordinator method
            Object result = DebugFrameworkServiceHolder.invokeDebugRequestCoordinatorMethod(
                    coordinator, methodName, new Class<?>[] { Map.class }, context);

            if (result == null) {
                throw new RuntimeException("Debug request returned null result");
            }

            return extractDebugResultData(result);

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
    private DebugRequestCoordinator getCoordinatorOrThrow() {

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
     * Extracts debug result data from the result object.
     * Handles both Map results and DebugResult objects.
     *
     * @param result The result object.
     * @return Map containing extracted result data.
     */
    private Map<String, Object> extractDebugResultData(Object result) {

        Map<String, Object> extractedData = new HashMap<>();

        try {
            if (result instanceof Map) {
                // Direct Map result
                Map<?, ?> resultMap = (Map<?, ?>) result;
                resultMap.forEach((key, value) -> {
                    if (key != null && value != null) {
                        extractedData.put(key.toString(), value);
                    }
                });
            } else {
                // DebugResult object - extract via reflection
                extractFromDebugResult(result, extractedData);
            }
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error extracting debug result data: " + e.getMessage());
            }
        }

        return extractedData;
    }

    /**
     * Extracts data from a DebugResult object using reflection.
     *
     * @param debugResult The DebugResult object.
     * @param target      The target map to populate.
     */
    private void extractFromDebugResult(Object debugResult, Map<String, Object> target) {

        // Extract resultData
        Object resultData = invokeMethod(debugResult, "getResultData");
        if (resultData instanceof Map) {
            ((Map<?, ?>) resultData).forEach((key, value) -> {
                if (key != null && value != null) {
                    target.put(key.toString(), value);
                }
            });
        }

        // Extract metadata (don't overwrite existing data)
        Object metadata = invokeMethod(debugResult, "getMetadata");
        if (metadata instanceof Map) {
            ((Map<?, ?>) metadata).forEach((key, value) -> {
                if (key != null && value != null) {
                    target.putIfAbsent(key.toString(), value);
                }
            });
        }

        // Extract status if not present
        if (!target.containsKey(KEY_STATUS)) {
            Object status = invokeMethod(debugResult, "getStatus");
            if (status != null) {
                target.put(KEY_STATUS, status.toString());
            }
        }

        // Extract timestamp if not present
        if (!target.containsKey(KEY_TIMESTAMP)) {
            Object timestamp = invokeMethod(debugResult, "getTimestamp");
            if (timestamp != null) {
                target.put(KEY_TIMESTAMP, timestamp);
            }
        }
    }

    /**
     * Invokes a no-arg method on an object using reflection.
     *
     * @param object     The target object.
     * @param methodName The method name.
     * @return The method result, or null if invocation fails.
     */
    private Object invokeMethod(Object object, String methodName) {

        try {
            Method method = object.getClass().getMethod(methodName);
            return method.invoke(object);
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Method " + methodName + " not found on " + object.getClass().getName());
            }
            return null;
        }
    }

    /**
     * Logs an error with sanitized message.
     *
     * @param context Error context description.
     * @param e       The exception.
     */
    private void logError(String context, Exception e) {

        LOG.error(context + ": " + sanitize(e.getMessage()), e);
    }

    /**
     * Sanitizes a string for logging (removes newlines).
     *
     * @param value The value to sanitize.
     * @return Sanitized string.
     */
    private String sanitize(String value) {

        return value != null ? value.replaceAll("[\\r\\n]", "") : "";
    }
}
