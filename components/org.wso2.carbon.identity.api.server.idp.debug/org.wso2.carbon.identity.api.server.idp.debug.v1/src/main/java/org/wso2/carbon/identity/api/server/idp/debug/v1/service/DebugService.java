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
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;

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
     * This method delegates to the debug framework's ContextProvider for proper separation of concerns.
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
            // Delegate context creation to debug framework's ContextProvider.
            // This removes all business logic from the API layer.
            Object context = DebugFrameworkServiceHolder.invokeContextProviderMethod(
                "createOAuth2DebugContext",
                new Class<?>[]{String.class, String.class, String.class, String.class, Map.class},
                idpId, authenticatorName, redirectUri, scope, additionalParams
            );
            
            if (context == null) {
                throw new RuntimeException("Failed to create debug context from ContextProvider");
            }
            
            // Extract IdP information from the context for response building.
            Object idpConfig = getPropertyFromContext(context, "IDP_CONFIG");
            String idpName = (String) getPropertyFromContext(context, "DEBUG_IDP_NAME");
            String targetAuthenticator = (String) getPropertyFromContext(context, "DEBUG_AUTHENTICATOR_NAME");
            String sessionDataKey = getContextIdentifier(context);
            
            if (sessionDataKey == null) {
                throw new RuntimeException("Session data key not found in context");
            }
            
                // Execute OAuth 2.0 URL generation using the debug framework.
                Object executer = DebugFrameworkServiceHolder.createExecuter();
                // Set executor instance in context for reporting
                if (context instanceof AuthenticationContext) {
                    ((AuthenticationContext) context).setProperty("DEBUG_EXECUTOR_INSTANCE", executer);
                }
                // Execute the debug flow using reflection to maintain loose coupling.
                boolean success = executeDebugFlow(executer, idpConfig, context);
                if (!success) {
                    throw new RuntimeException("Failed to generate OAuth 2.0 authorization URL");
                }
            
            // Get the generated authorization URL from context.
            String authorizationUrl = (String) getPropertyFromContext(context, "DEBUG_EXTERNAL_REDIRECT_URL");
            if (authorizationUrl == null) {
                throw new RuntimeException("OAuth 2.0 authorization URL not found in context");
            }

            // Build response with URL and session information.
            Map<String, Object> result = new HashMap<>();
            result.put("sessionId", sessionDataKey);
            result.put("authorizationUrl", authorizationUrl);
            result.put("status", "URL_GENERATED");
            result.put("message", "OAuth 2.0 authorization URL generated successfully. " +
                                   "Redirect user to this URL for authentication.");
            result.put("idpName", idpName != null ? idpName : "Unknown");
                result.put("authenticatorName", targetAuthenticator != null ? targetAuthenticator : "Unknown");
                // Add executor class name for clarity
                String executorClass = executer != null ? executer.getClass().getSimpleName() : "UnknownExecutor";
                result.put("executor", executorClass);
            result.put("timestamp", System.currentTimeMillis());
            
            return result;
            
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
     * Gets a property from AuthenticationContext using reflection.
     *
     * @param context AuthenticationContext object.
     * @param propertyName Property name to retrieve.
     * @return Property value or null if not found.
     */
    private Object getPropertyFromContext(Object context, String propertyName) {
        try {
            java.lang.reflect.Method getPropertyMethod = context.getClass().getMethod("getProperty", String.class);
            return getPropertyMethod.invoke(context, propertyName);
        } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    String sanitizedMessage = e.getMessage() != null ? e.getMessage().replaceAll("[\r\n]", "") : "";
                    log.debug("Error getting property '" + propertyName + "' from context: " + sanitizedMessage);
                }
            return null;
        }
    }

    /**
     * Gets the context identifier from AuthenticationContext using reflection.
     *
     * @param context AuthenticationContext object.
     * @return Context identifier or null if not found.
     */
    private String getContextIdentifier(Object context) {
        try {
            java.lang.reflect.Method getContextIdentifierMethod = context.getClass().getMethod("getContextIdentifier");
            Object result = getContextIdentifierMethod.invoke(context);
            return result != null ? result.toString() : null;
        } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    String sanitizedMessage = e.getMessage() != null ? e.getMessage().replaceAll("[\r\n]", "") : "";
                    log.debug("Error getting context identifier: " + sanitizedMessage);
                }
            return null;
        }
    }

    /**
     * Executes the debug flow using reflection to maintain loose coupling.
     *
     * @param executer Executer instance.
     * @param idpConfig Identity Provider configuration.
     * @param context Authentication context.
     * @return true if execution successful, false otherwise.
     */
    private boolean executeDebugFlow(Object executer, Object idpConfig, Object context) {
        try {
            Object executeResult = DebugFrameworkServiceHolder.invokeDebugServiceMethod("execute", 
                new Class<?>[]{IdentityProvider.class, AuthenticationContext.class}, 
                idpConfig, context);

            if (executeResult instanceof Boolean) {
                return (Boolean) executeResult;
            } else {
                // Try invoking on the executer object directly.
                java.lang.reflect.Method executeMethod = executer.getClass()
                    .getMethod("execute", IdentityProvider.class, AuthenticationContext.class);
                Object result = executeMethod.invoke(executer, idpConfig, context);
                return result instanceof Boolean ? (Boolean) result : false;
            }
        } catch (Exception e) {
            String sanitizedMessage = e.getMessage() != null ? e.getMessage().replaceAll("[\r\n]", "") : "";
            log.error("Error executing debug flow: " + sanitizedMessage, e);
            return false;
        }
    }
}
