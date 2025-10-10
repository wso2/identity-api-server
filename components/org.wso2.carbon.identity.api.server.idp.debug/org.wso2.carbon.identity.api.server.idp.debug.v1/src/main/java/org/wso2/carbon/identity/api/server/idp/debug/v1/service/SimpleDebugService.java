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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.idp.debug.common.DebugFrameworkServiceHolder;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;

/**
 * Service layer for IdP debug operations.
 * This service provides a clean interface for the API layer and delegates to debug framework components.
 * It follows the architecture diagram by using the DebugFlowService which orchestrates:
 * 1. ContextProvider - Creates context with IdP id and other relevant data
 * 2. Executer - Invokes authenticator and sends to FederatedIdP
 * 3. RequestCoordinator - Handles callback from /commonauth with debug identifier  
 * 4. Processor - Processes results and sends to client
 */
public class SimpleDebugService {

    private static final Log log = LogFactory.getLog(SimpleDebugService.class);
    private static final String DEFAULT_TENANT_DOMAIN = "carbon.super";

    /**
     * Constructor initializes debug framework service via service holder pattern.
     */
    public SimpleDebugService() {
        // Debug framework services will be loaded on-demand during OAuth URL generation
    }

    /**
     * Generates OAuth 2.0 authorization URL for IdP debug testing.
     * This method follows the new OAuth 2.0 flow architecture.
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
        // Input validation.
        validateDebugRequest(idpId);
        
        try {
            // Get the IdP object.
            IdentityProvider idp = getIdentityProvider(idpId);
            if (idp == null) {
                throw new RuntimeException("Identity Provider not found for ID: " + idpId);
            }

            if (!idp.isEnable()) {
                throw new RuntimeException("Identity Provider is disabled: " + idp.getIdentityProviderName());
            }

            // Determine authenticator to use.
            String targetAuthenticator = determineAuthenticator(idp, authenticatorName);
            if (targetAuthenticator == null) {
                throw new RuntimeException("No suitable authenticator found for IdP: " + idp.getIdentityProviderName());
            }

            // Generate session data key for debug flow.
            String sessionDataKey = "debug-" + UUID.randomUUID().toString();

            // Create authentication context and use Executer to build OAuth 2.0 URL.
            AuthenticationContext context = new AuthenticationContext();
            context.setContextIdentifier(sessionDataKey);
            context.setProperty("DEBUG_SESSION", "true");
            context.setProperty("DEBUG_AUTHENTICATOR_NAME", targetAuthenticator);
            context.setProperty("IDP_CONFIG", idp);
            
            // Add custom parameters if provided.
            if (redirectUri != null) {
                context.setProperty("CUSTOM_REDIRECT_URI", redirectUri);
            }
            if (scope != null) {
                context.setProperty("CUSTOM_SCOPE", scope);
            }
            if (additionalParams != null) {
                context.setProperty("ADDITIONAL_OAUTH_PARAMS", additionalParams);
            }

            // Use Executer via service holder to build OAuth 2.0 authorization URL.
            Object executer = DebugFrameworkServiceHolder.createExecuter();
            if (executer == null) {
                throw new RuntimeException("Failed to create Executer instance from debug framework");
            }
            
            // Execute using reflection
            Object executeResult = DebugFrameworkServiceHolder.invokeDebugServiceMethod("execute", 
                new Class<?>[]{IdentityProvider.class, AuthenticationContext.class}, idp, context);
            
            boolean success = false;
            if (executeResult instanceof Boolean) {
                success = (Boolean) executeResult;
            } else {
                // Try invoking on the executer object directly
                try {
                    java.lang.reflect.Method executeMethod = executer.getClass()
                        .getMethod("execute", IdentityProvider.class, AuthenticationContext.class);
                    Object result = executeMethod.invoke(executer, idp, context);
                    success = (Boolean) result;
                } catch (Exception e) {
                    log.error("Error invoking execute method on Executer", e);
                    throw new RuntimeException("Failed to execute debug flow", e);
                }
            }
            
            if (!success) {
                throw new RuntimeException("Failed to generate OAuth 2.0 authorization URL");
            }
            
            // Get the generated authorization URL from context.
            String authorizationUrl = (String) context.getProperty("DEBUG_EXTERNAL_REDIRECT_URL");
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
            result.put("idpName", idp.getIdentityProviderName());
            result.put("authenticatorName", targetAuthenticator);
            result.put("timestamp", System.currentTimeMillis());
            
            return result;
            
        } catch (RuntimeException e) {
            log.error("Runtime error generating OAuth 2.0 URL", e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error generating OAuth 2.0 URL", e);
            throw new RuntimeException("Failed to generate OAuth 2.0 authorization URL: " + e.getMessage(), e);
        }
    }

    // Removed validateCredentialsDirectly - using debug framework instead

    

    // Removed all service logic methods - now using debug framework only




    /**
     * Gets identity provider by ID using IdP management service.
     *
     * @param idpId Identity Provider ID (resource ID).
     * @return IdentityProvider object if found, null otherwise.
     */
    private IdentityProvider getIdentityProvider(String idpId) {
        try {
            // Start tenant flow for the default tenant.
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(DEFAULT_TENANT_DOMAIN, true);

            IdentityProviderManager idpManager = IdentityProviderManager.getInstance();
            
            try {
                // First try to get by resource ID.
                return idpManager.getIdPByResourceId(idpId, DEFAULT_TENANT_DOMAIN, true);
            } catch (IdentityProviderManagementException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Failed to get IdP by resource ID, trying by name: " + e.getMessage());
                }
                // If not found by resource ID, try by name.
                try {
                    return idpManager.getIdPByName(idpId, DEFAULT_TENANT_DOMAIN, true);
                } catch (IdentityProviderManagementException ex) {
                    log.error("Failed to get IdP by name: " + ex.getMessage(), ex);
                    return null;
                }
            }
        } catch (Exception e) {
            log.error("Error retrieving Identity Provider: " + e.getMessage(), e);
            return null;
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
    }

    /**
     * Determines which authenticator to use for the debug flow.
     *
     * @param idp Identity Provider.
     * @param authenticatorName Requested authenticator name (optional).
     * @return Authenticator name to use.
     */
    private String determineAuthenticator(IdentityProvider idp, String authenticatorName) {
        if (authenticatorName != null && !authenticatorName.trim().isEmpty()) {
            // Check if the specified authenticator exists in IdP configuration.
            if (idp.getFederatedAuthenticatorConfigs() != null) {
                for (org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig config : 
                     idp.getFederatedAuthenticatorConfigs()) {
                    if (authenticatorName.equals(config.getName())) {
                        return authenticatorName;
                    }
                }
            }
        }

        // Use default authenticator from IdP configuration.
        if (idp.getDefaultAuthenticatorConfig() != null) {
            return idp.getDefaultAuthenticatorConfig().getName();
        }

        // Use first available authenticator.
        if (idp.getFederatedAuthenticatorConfigs() != null && idp.getFederatedAuthenticatorConfigs().length > 0) {
            return idp.getFederatedAuthenticatorConfigs()[0].getName();
        }

        return null;
    }

    /**
     * Validates debug request parameters for OAuth 2.0 flow.
     *
     * @param idpId Identity Provider ID.
     * @throws RuntimeException if validation fails.
     */
    private void validateDebugRequest(String idpId) throws RuntimeException {
        if (isBlank(idpId)) {
            throw new RuntimeException("Identity Provider ID is required");
        }
        
        // Security: Basic input validation to prevent injection.
        if (idpId.length() > 255) {
            throw new RuntimeException("Identity Provider ID must be less than 255 characters");
        }
        
        // Security: Check for potentially dangerous characters.
        if (containsSqlInjectionPattern(idpId)) {
            throw new RuntimeException("Invalid characters detected in IdP ID");
        }
    }

    /**
     * Basic SQL injection pattern detection.
     * 
     * Vulnerability: Potential XSS - Input validation should be more comprehensive.
     * Risk: Basic pattern matching may not catch all injection attempts.
     * Suggestion: Use a proper input sanitization library or framework validation.
     *
     * @param input Input string to validate.
     * @return true if suspicious patterns detected, false otherwise.
     */
    private boolean containsSqlInjectionPattern(String input) {
        if (input == null) {
            return false;
        }
        
        String lowerInput = input.toLowerCase(java.util.Locale.ROOT);
        String[] sqlPatterns = {"'", "\"", ";", "--", "/*", "*/", "union", "select", "insert", "update", "delete"};
        
        for (String pattern : sqlPatterns) {
            if (lowerInput.contains(pattern)) {
                return true;
            }
        }
        return false;
    }



    /**
     * Checks if a string is blank (null, empty, or whitespace only).
     *
     * @param str String to check.
     * @return true if blank, false otherwise.
     */
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
