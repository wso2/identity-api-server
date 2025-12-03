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

package org.wso2.carbon.identity.api.server.idp.debug.v1.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.identity.api.server.idp.debug.common.DebugFrameworkServiceHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Service layer for IdP debug operations.
 * This service provides a clean interface for the API layer and delegates to debug framework components.
 * It follows the OAuth 2.0 flow architecture using debug framework components:
 */
public class DebugService {

    private static final Log log = LogFactory.getLog(DebugService.class);

    /**
     * Constructor initializes debug framework service via service holder pattern.
     */
    public DebugService() {
        // Debug framework services will be loaded on-demand during OAuth URL generation
    }

    /**
     * Handles generic debug request for any resource type with properties.
     * Delegates to the appropriate debug handler based on resource type.
     *
     * @param resourceId Resource ID to debug.
     * @param resourceType Type of resource (IDP, APPLICATION, CONNECTOR, etc.).
     * @param properties Generic properties map for the debug request (optional).
     * @return Debug result containing session information and status.
     */
    public Map<String, Object> handleGenericDebugRequest(String resourceId, String resourceType,
                                                         java.util.Map<String, String> properties) {
        try {
            // Check if debug framework is available.
            if (!DebugFrameworkServiceHolder.isDebugFrameworkAvailable()) {
                throw new RuntimeException("Debug framework services are not available");
            }
            
            // Get the DebugRequestCoordinator for centralized request handling.
            Object requestCoordinator = DebugFrameworkServiceHolder.getDebugRequestCoordinator();
            if (requestCoordinator == null) {
                throw new RuntimeException("DebugRequestCoordinator not available");
            }
            
            // Build generic debug request context.
            Map<String, Object> debugRequestContext = new HashMap<>();
            debugRequestContext.put("resourceId", resourceId);
            debugRequestContext.put("resourceType", resourceType);
            debugRequestContext.put("properties", properties != null ? properties : new HashMap<String, String>());
            debugRequestContext.put("requestType", "GENERIC_DEBUG_REQUEST");
            
            // Delegate to DebugRequestCoordinator to handle the debug request.
            // The coordinator will detect the resource type and route to appropriate handler.
            Object debugResult = DebugFrameworkServiceHolder.invokeDebugRequestCoordinatorMethod(
                requestCoordinator,
                "handleResourceDebugRequest",
                new Class<?>[]{Map.class},
                debugRequestContext
            );
            
            if (debugResult == null) {
                throw new RuntimeException("Debug request returned null result");
            }
            
            // Extract the result details using reflection.
            Map<String, Object> resultMap = extractDebugResultData(debugResult);
            
            // Add additional metadata.
            resultMap.put("timestamp", System.currentTimeMillis());
            resultMap.put("resourceId", resourceId);
            resultMap.put("resourceType", resourceType);
            resultMap.put("status", resultMap.getOrDefault("status", "SUCCESS"));
            
            return resultMap;
            
        } catch (RuntimeException e) {
            String sanitizedMessage = e.getMessage() != null ? e.getMessage().replaceAll("[\r\n]", "") : "";
            log.error("Runtime error in generic debug request for resource: " + sanitizedMessage, e);
            throw e;
        } catch (Exception e) {
            String sanitizedMessage = e.getMessage() != null ? e.getMessage().replaceAll("[\r\n]", "") : "";
            log.error("Unexpected error in generic debug request: " + sanitizedMessage, e);
            throw new RuntimeException("Failed to process generic debug request: " + sanitizedMessage, e);
        }
    }

    /**
     * Generates OAuth 2.0 authorization URL for debug flow using headless browser simulation.
     * This method delegates to the debug framework's DebugRequestCoordinator which handles
     * protocol-agnostic request routing and coordinates context resolution and execution.
     *
     * @param idpId Identity Provider resource ID.
     * @param properties Optional properties for OAuth 2.0 configuration.
     * @return OAuth 2.0 authorization URL and session information.
     */
    public Map<String, Object> generateOAuth2AuthorizationUrl(String idpId, 
                                                              Map<String, String> properties) {
        try {
            // Check if debug framework is available.
            if (!DebugFrameworkServiceHolder.isDebugFrameworkAvailable()) {
                throw new RuntimeException("Debug framework services are not available");
            }
            
            // Get the DebugRequestCoordinator for centralized request handling.
            Object requestCoordinator = DebugFrameworkServiceHolder.getDebugRequestCoordinator();
            if (requestCoordinator == null) {
                throw new RuntimeException("DebugRequestCoordinator not available");
            }
            
            // Build debug request context with IdP and OAuth2 configuration.
            Map<String, Object> debugRequestContext = new HashMap<>();
            debugRequestContext.put("idpId", idpId);
            debugRequestContext.put("properties", properties != null ? properties : new HashMap<String, String>());
            debugRequestContext.put("requestType", "INITIAL_DEBUG_REQUEST");
            
            // Delegate to DebugRequestCoordinator to handle protocol-specific routing.
            // The coordinator will detect the protocol, load appropriate context resolver,
            // execute the flow, and return the authorization URL.
            Object authUrlResult = DebugFrameworkServiceHolder.invokeDebugRequestCoordinatorMethod(
                requestCoordinator,
                "handleInitialDebugRequest",
                new Class<?>[]{Map.class},
                debugRequestContext
            );
            
            if (authUrlResult == null) {
                throw new RuntimeException("Authorization URL generation returned null result");
            }
            
            // Extract the result details using reflection.
            Map<String, Object> resultMap = extractDebugResultData(authUrlResult);
            
            // Add additional metadata.
            resultMap.put("timestamp", System.currentTimeMillis());
            resultMap.put("idpId", idpId);
            resultMap.put("status", "SUCCESS");
            
            return resultMap;
            
        } catch (RuntimeException e) {
            String sanitizedMessage = e.getMessage() != null ? e.getMessage().replaceAll("[\r\n]", "") : "";
            log.error("Runtime error generating OAuth 2.0 URL: " + sanitizedMessage, e);
            throw e;
        } catch (Exception e) {
            String sanitizedMessage = e.getMessage() != null ? e.getMessage().replaceAll("[\r\n]", "") : "";
            log.error("Unexpected error generating OAuth 2.0 URL: " + sanitizedMessage, e);
            throw new RuntimeException("Failed to generate OAuth 2.0 authorization URL: " + sanitizedMessage, e);
        }
    }
    
    /**
     * Extracts debug result data using reflection.
     * This method avoids compile-time dependency on DebugResult class.
     * DebugResult has getResultData() and getMetadata() methods that return Maps.
     *
     * @param authUrlResult The result from OAuth2Executor.execute().
     * @return Map containing extracted result data.
     */
    private Map<String, Object> extractDebugResultData(Object authUrlResult) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // If the result is already a Map, use it directly.
            if (authUrlResult instanceof Map) {
                Map<?, ?> resultMap = (Map<?, ?>) authUrlResult;
                for (Map.Entry<?, ?> entry : resultMap.entrySet()) {
                    if (entry.getKey() != null && entry.getValue() != null) {
                        result.put(entry.getKey().toString(), entry.getValue());
                    }
                }
            } else {
                // Try to extract from DebugResult using getResultData() and getMetadata()
                Object resultDataObj = invokeMethodViaReflection(authUrlResult, "getResultData", new Class<?>[]{});
                if (resultDataObj instanceof Map) {
                    Map<?, ?> resultDataMap = (Map<?, ?>) resultDataObj;
                    for (Map.Entry<?, ?> entry : resultDataMap.entrySet()) {
                        if (entry.getKey() != null && entry.getValue() != null) {
                            result.put(entry.getKey().toString(), entry.getValue());
                        }
                    }
                }
                
                Object metadataObj = invokeMethodViaReflection(authUrlResult, "getMetadata", new Class<?>[]{});
                if (metadataObj instanceof Map) {
                    Map<?, ?> metadataMap = (Map<?, ?>) metadataObj;
                    for (Map.Entry<?, ?> entry : metadataMap.entrySet()) {
                        if (entry.getKey() != null && entry.getValue() != null) {
                            // Don't overwrite existing result data with metadata
                            String key = entry.getKey().toString();
                            if (!result.containsKey(key)) {
                                result.put(key, entry.getValue());
                            }
                        }
                    }
                }
                
                // Extract basic DebugResult fields if not already present
                if (!result.containsKey("status")) {
                    Object status = invokeMethodViaReflection(authUrlResult, "getStatus", new Class<?>[]{});
                    if (status != null) {
                        result.put("status", status.toString());
                    }
                }
                
                if (!result.containsKey("timestamp")) {
                    Object timestamp = invokeMethodViaReflection(authUrlResult, "getTimestamp", new Class<?>[]{});
                    if (timestamp != null) {
                        result.put("timestamp", timestamp);
                    }
                }
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Error extracting debug result data: " + e.getMessage());
            }
        }
        
        return result;
    }
    
    /**
     * Invokes a method on an object using reflection.
     *
     * @param object The object on which to invoke the method.
     * @param methodName The name of the method to invoke.
     * @param parameterTypes The parameter types of the method.
     * @return The result of the method invocation, or null if the method is not found or returns null.
     */
    private Object invokeMethodViaReflection(Object object, String methodName, Class<?>[] parameterTypes) {
        try {
            java.lang.reflect.Method method = object.getClass().getMethod(methodName, parameterTypes);
            return method.invoke(object);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Method " + methodName + " not found on object " + object.getClass().getName());
            }
            return null;
        }
    }
}

