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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.idp.debug.common.DebugFrameworkServiceHolder;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse.AuthenticationResult;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse.ClaimsAnalysis;
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
        try {
            log.info("STARTUP: Initializing SimpleDebugService with DebugFrameworkServiceHolder");
            
            // Check if debug framework services are available
            if (!DebugFrameworkServiceHolder.isDebugFrameworkAvailable()) {
                log.warn("STARTUP WARNING: Debug framework services not immediately available via OSGi lookup");
                log.info("Will attempt direct instantiation and service lookup during runtime");
            } else {
                log.info("STARTUP: Debug framework services detected via OSGi lookup");
            }
            
            log.info("STARTUP: Successfully initialized SimpleDebugService");
        } catch (Exception e) {
            log.error("STARTUP ERROR: Failed to initialize SimpleDebugService - " + 
                    e.getClass().getName() + ": " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize debug framework service: " + 
                    e.getMessage(), e);
        }
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
        log.info("Generating OAuth 2.0 authorization URL for IdP ID: " + 
            (idpId != null ? idpId.replaceAll("[\r\n]", "_") : "null"));
        
        // Input validation.
        validateDebugRequest(idpId);
        
        try {
            // Step 1: Get the IdP object.
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

            log.info("Using IdP: " + idp.getIdentityProviderName() + 
                     " with authenticator: " + targetAuthenticator);

            // Step 2: Create authentication context and use Executer to build OAuth 2.0 URL.
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
                    log.error("Error invoking execute method on Executer: " + e.getMessage(), e);
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

            log.info("Successfully generated OAuth 2.0 authorization URL for session: " + 
                (sessionDataKey != null ? sessionDataKey.replaceAll("[\r\n]", "_") : "null"));

            // Step 3: Build response with URL and session information.
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
            log.error("Runtime error generating OAuth 2.0 URL: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error generating OAuth 2.0 URL: " + e.getMessage(), e);
            throw new RuntimeException("Failed to generate OAuth 2.0 authorization URL: " + e.getMessage(), e);
        }
    }

    // Removed validateCredentialsDirectly - using debug framework instead

    

    // Removed all service logic methods - now using debug framework only

    /**
     * Legacy method for backward compatibility - now generates OAuth 2.0 URL.
     * @deprecated Use generateOAuth2AuthorizationUrl instead.
     */
    @Deprecated
    public DebugResponse executeDebugFlow(String idpId, String username, String password, 
                                        String authenticatorName, HttpServletRequest request, 
                                        HttpServletResponse response) throws RuntimeException {
        
        log.warn("Legacy executeDebugFlow called - redirecting to OAuth 2.0 flow");
        
        // Generate OAuth 2.0 URL instead of using credentials.
        Map<String, Object> result = generateOAuth2AuthorizationUrl(idpId, authenticatorName, null, null, null);
        
        // Convert to legacy DebugResponse format.
        DebugResponse legacyResponse = new DebugResponse();
        legacyResponse.setSessionId((String) result.get("sessionId"));
        legacyResponse.setStatus((String) result.get("status"));
        legacyResponse.setTargetIdp(idpId);
        legacyResponse.setAuthenticatorUsed((String) result.get("authenticatorName"));
        
        // Add metadata with OAuth URL.
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("authorizationUrl", result.get("authorizationUrl"));
        metadata.put("message", "Legacy API call converted to OAuth 2.0 flow. Use the authorizationUrl for authentication.");
        Object metadataObj = result.get("metadata");
        if (metadataObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> resultMetadata = (Map<String, Object>) metadataObj;
            metadata.putAll(resultMetadata);
        }
        legacyResponse.setMetadata(metadata);
        
        return legacyResponse;
    }
    
    /**
     * Legacy validation method - redirects to OAuth 2.0 flow validation.
     */
    @SuppressWarnings("unused")
    private void validateDebugRequestLegacy(String idpId, String username, String password) {
        // Input validation.
        validateDebugRequest(idpId);
        // Note: Username and password are no longer validated as OAuth 2.0 flow doesn't require them upfront.
    }


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
            log.warn("Requested authenticator not found in IdP: " + 
                (authenticatorName != null ? authenticatorName.replaceAll("[\r\n]", "_") : "null"));
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
     * Builds debug response from debug flow results.
     *
     * @param debugResults Debug results from DebugFlowService.
     * @param idp Identity Provider used.
     * @param authenticatorName Authenticator used.
     * @return DebugResponse.
     */
    // Removed old buildDebugResponse method - now using direct credential validation
    @SuppressWarnings({"unused", "unchecked"})
    private DebugResponse buildDebugResponseOld(Map<String, Object> debugResults, IdentityProvider idp, String authenticatorName) {
        DebugResponse response = new DebugResponse();
        
        // Set basic response information.
        String sessionId = (String) debugResults.get("debugSessionId");
        response.setSessionId(sessionId != null ? sessionId : "debug-" + UUID.randomUUID().toString());
        response.setTargetIdp(idp.getResourceId());
        
        // Determine overall status.
        String status = (String) debugResults.get("status");
        response.setStatus(status != null ? status : "UNKNOWN");

        // Build authentication result.
        AuthenticationResult authResult = new AuthenticationResult();
        boolean isSuccessful = "SUCCESS".equals(status);
        authResult.setSuccess(isSuccessful);
        authResult.setUserExists(isSuccessful);
        
        if (isSuccessful) {
            authResult.setUserDetails("Authentication successful with " + idp.getIdentityProviderName());
        } else {
            String error = (String) debugResults.get("error");
            authResult.setUserDetails(error != null ? error : "Authentication failed");
        }
        response.setAuthenticationResult(authResult);

        // Build claims analysis from processed result.
        Object processedResult = debugResults.get("processedResult");
        if (processedResult instanceof Map) {
            Map<String, Object> processedData = (Map<String, Object>) processedResult;
            Map<String, Object> claimsAnalysisData = (Map<String, Object>) processedData.get("claimsAnalysis");
            
            if (claimsAnalysisData != null) {
                ClaimsAnalysis claimsAnalysis = new ClaimsAnalysis();
                
                Map<String, String> originalClaims = (Map<String, String>) claimsAnalysisData.get("originalRemoteClaims");
                Map<String, String> mappedClaims = (Map<String, String>) claimsAnalysisData.get("mappedLocalClaims");
                List<String> mappingErrors = (List<String>) claimsAnalysisData.get("mappingErrors");
                
                claimsAnalysis.setOriginalRemoteClaims(originalClaims != null ? originalClaims : Collections.emptyMap());
                claimsAnalysis.setMappedLocalClaims(mappedClaims != null ? mappedClaims : Collections.emptyMap());
                claimsAnalysis.setMappingErrors(mappingErrors != null ? mappingErrors : Collections.emptyList());
                
                response.setClaimsAnalysis(claimsAnalysis);
            }
        }

        // Build flow events from processed result.
        List<DebugResponse.FlowEvent> flowEvents = new ArrayList<>();
        if (processedResult instanceof Map) {
            Map<String, Object> processedData = (Map<String, Object>) processedResult;
            List<Map<String, Object>> flowEventsData = (List<Map<String, Object>>) processedData.get("flowEvents");
            
            if (flowEventsData != null) {
                for (Map<String, Object> eventData : flowEventsData) {
                    DebugResponse.FlowEvent flowEvent = new DebugResponse.FlowEvent();
                    
                    Object timestamp = eventData.get("timestamp");
                    if (timestamp instanceof Long) {
                        flowEvent.setTimestamp((Long) timestamp);
                    }
                    
                    flowEvent.setEventType((String) eventData.get("eventType"));
                    flowEvent.setStep((String) eventData.get("step"));
                    
                    Object success = eventData.get("success");
                    if (success instanceof Boolean) {
                        flowEvent.setSuccess((Boolean) success);
                    }
                    
                    flowEvent.setAuthenticator((String) eventData.get("authenticator"));
                    flowEvent.setData(eventData.get("data"));
                    
                    flowEvents.add(flowEvent);
                }
            }
        }
        response.setFlowEvents(flowEvents);

        // Build errors from processed result.
        List<DebugResponse.DebugError> errors = new ArrayList<>();
        if (processedResult instanceof Map) {
            Map<String, Object> processedData = (Map<String, Object>) processedResult;
            List<Map<String, Object>> errorsData = (List<Map<String, Object>>) processedData.get("errors");
            
            if (errorsData != null) {
                for (Map<String, Object> errorData : errorsData) {
                    DebugResponse.DebugError debugError = new DebugResponse.DebugError();
                    debugError.setCode((String) errorData.get("code"));
                    debugError.setMessage((String) errorData.get("message"));
                    debugError.setStep((String) errorData.get("step"));
                    errors.add(debugError);
                }
            }
        }
        
        // Add any general errors from debug results.
        String generalError = (String) debugResults.get("error");
        if (generalError != null) {
            DebugResponse.DebugError debugError = new DebugResponse.DebugError();
            debugError.setCode("GENERAL_ERROR");
            debugError.setMessage(generalError);
            debugError.setStep("Overall");
            errors.add(debugError);
        }
        response.setErrors(errors);

        // Set authenticator used.
        response.setAuthenticatorUsed(authenticatorName);

        // Add metadata.
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("idpId", idp.getResourceId());
        metadata.put("idpName", idp.getIdentityProviderName());
        metadata.put("authenticator", authenticatorName);
        metadata.put("timestamp", debugResults.get("timestamp"));
        metadata.put("sessionDataKey", debugResults.get("sessionDataKey"));
        metadata.put("executionStarted", debugResults.get("executionStarted"));
        metadata.put("callbackProcessed", debugResults.get("callbackProcessed"));
        metadata.put("architecture", "Following the debug flow architecture: API -> ContextProvider -> Executer -> FederatedIdP -> /commonauth -> RequestCoordinator -> Processor");
        response.setMetadata(metadata);

        return response;
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
