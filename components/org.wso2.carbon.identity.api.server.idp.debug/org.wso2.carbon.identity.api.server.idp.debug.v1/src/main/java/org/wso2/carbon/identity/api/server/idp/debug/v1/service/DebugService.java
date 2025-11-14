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
     * Generates OAuth 2.0 authorization URL for debug flow using headless browser simulation.
     * This method delegates to the debug framework's OAuth2ContextResolver and OAuth2Executor.
     *
     * @param idpId Identity Provider resource ID.
     * @param authenticatorName Optional authenticator name.
     * @param redirectUri Optional custom redirect URI.
     * @param scope Optional OAuth 2.0 scope.
     * @param additionalParams Optional additional OAuth 2.0 parameters.
     * @return OAuth 2.0 authorization URL and session information.
     */
    public Map<String, Object> generateOAuth2AuthorizationUrl(String idpId, String authenticatorName, 
                                                              String redirectUri, String scope, 
                                                              Map<String, String> additionalParams) {
        try {
            // Check if debug framework is available.
            if (!DebugFrameworkServiceHolder.isDebugFrameworkAvailable()) {
                throw new RuntimeException("Debug framework services are not available");
            }
            
            // Get the OAuth2ContextResolver.
            Object contextResolver = DebugFrameworkServiceHolder.getDebugContextResolver();
            if (contextResolver == null) {
                throw new RuntimeException("DebugContextResolver not available");
            }
            
            // Build authentication context map with IdP and OAuth2 configuration.
            Map<String, Object> authenticationContext = new HashMap<>();
            authenticationContext.put("idpName", idpId);
            authenticationContext.put("authenticatorName", authenticatorName != null ? authenticatorName : "oauth2");
            // TODO: Extract actual IdP configuration (endpoints, client credentials, etc.)
            // For now, these would come from IdP configuration store.
            
            // Invoke resolveContext method on OAuth2ContextResolver.
            Object oauth2Context = DebugFrameworkServiceHolder.invokeDebugContextResolverMethod(
                contextResolver,
                "resolveContext",
                new Class<?>[]{Map.class},
                authenticationContext
            );
            
            if (oauth2Context == null) {
                throw new RuntimeException("Failed to resolve OAuth2 context");
            }
            
            // Get the OAuth2Executor.
            Object executor = DebugFrameworkServiceHolder.getDebugExecutor();
            if (executor == null) {
                throw new RuntimeException("DebugExecutor not available");
            }
            
            // Invoke execute method on OAuth2Executor (standard DebugExecutor interface).
            Object authUrlResult = DebugFrameworkServiceHolder.invokeDebugExecutorMethod(
                executor,
                "execute",
                new Class<?>[]{Map.class},
                oauth2Context
            );
            
            if (authUrlResult == null) {
                throw new RuntimeException("Authorization URL generation returned null result");
            }
            
            // Extract the result details using reflection.
            Map<String, Object> resultMap = extractDebugResultData(authUrlResult);
            
            // Add additional metadata.
            resultMap.put("timestamp", System.currentTimeMillis());
            resultMap.put("idpName", idpId);
            resultMap.put("authenticatorName", authenticatorName != null ? authenticatorName : "oauth2");
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

