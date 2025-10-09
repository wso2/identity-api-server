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

import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse.AuthenticationResult;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse.ClaimsAnalysis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Collections;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

// Import IdentityProvider and related classes from Carbon Identity framework
import org.wso2.carbon.identity.application.common.model.IdentityProvider;


// Import API error classes
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;

/**
 * Service layer for IdP debug operations.
 * Handles business logic for debug authentication flows following the architecture:
 * API Layer -> Service Layer -> Framework Components
 * 
 * This service encapsulates all business logic and provides a clean interface for the API layer.
 */
public class DebugService {

    private static final Log LOG = LogFactory.getLog(DebugService.class);

    /**
     * Constructor.
     */
    public DebugService() {
        // Service is stateless.
    }

    /**
     * Execute debug authentication flow for the specified IdP.
     * Follows the architecture: Service Layer -> Framework Components.
     *
     * @param idpId Identity Provider ID (resource ID or name).
     * @param username Username for authentication test.
     * @param password Password for authentication test.
     * @return DebugResponse containing authentication results and claims.
     * @throws APIError if validation fails or execution encounters errors.
     */
    public DebugResponse executeDebugFlow(String idpId, String username, String password) throws APIError {
        
        LOG.warn("Legacy executeDebugFlow with credentials called - converting to OAuth 2.0 flow");
        
        try {
            // Convert to OAuth 2.0 flow using SimpleDebugService.
            SimpleDebugService simpleDebugService = new SimpleDebugService();
            Map<String, Object> oauth2Result = simpleDebugService.generateOAuth2AuthorizationUrl(idpId, null, null, null, null);
            
            // Convert to legacy response format.
            DebugResponse response = new DebugResponse();
            response.setSessionId((String) oauth2Result.get("sessionId"));
            response.setStatus((String) oauth2Result.get("status"));
            response.setTargetIdp(idpId);
            response.setAuthenticatorUsed((String) oauth2Result.get("authenticatorName"));
            
            // Add OAuth URL to metadata.
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("authorizationUrl", oauth2Result.get("authorizationUrl"));
            metadata.put("message", "Legacy API converted to OAuth 2.0 flow. Use authorizationUrl for authentication.");
            metadata.put("timestamp", oauth2Result.get("timestamp"));
            response.setMetadata(metadata);
            
            return response;
            
        } catch (APIError e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Error converting legacy debug flow for IdP: " + 
                (idpId != null ? idpId.replaceAll("[\r\n]", "_") : "null"), e);
            throw createAPIError("INTERNAL_ERROR", "Failed to execute legacy debug flow: " + e.getMessage());
        }
    }



    /**
     * Validates debug request parameters.
     *
     * @param idpId Identity Provider ID.
     * @param username Username.
     * @param password Password.
     * @throws APIError if validation fails.
     */
    private void validateDebugRequest(String idpId, String username, String password) throws APIError {
        if (StringUtils.isBlank(idpId)) {
            throw createAPIError("INVALID_REQUEST", "Identity Provider ID is required");
        }
        
        if (StringUtils.isBlank(username)) {
            throw createAPIError("INVALID_REQUEST", "Username is required for debug authentication");
        }
        
        if (StringUtils.isBlank(password)) {
            throw createAPIError("INVALID_REQUEST", "Password is required for debug authentication");
        }
        
        // Security: Basic input validation to prevent injection.
        if (username.length() > 255 || password.length() > 255) {
            throw createAPIError("INVALID_REQUEST", "Username and password must be less than 255 characters");
        }
        
        // Security: Check for potentially dangerous characters.
        if (containsSqlInjectionPattern(username) || containsSqlInjectionPattern(password)) {
            throw createAPIError("INVALID_REQUEST", "Invalid characters detected in credentials");
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
     * Creates APIError with proper error response.
     *
     * @param errorCode Error code.
     * @param message Error message.
     * @return APIError.
     */
    private APIError createAPIError(String errorCode, String message) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(errorCode);
        errorDTO.setMessage(message);
        errorDTO.setDescription(message);
        return new APIError(javax.ws.rs.core.Response.Status.BAD_REQUEST, errorDTO);
    }










    /**
     * Builds debug response from debug result.
     *
     * @param sessionId Debug session ID.
     * @param identityProvider Identity Provider.
     * @param debugResult Debug result map.
     * @return DebugResponse.
     */
    @SuppressWarnings("unchecked")
    private DebugResponse buildDebugResponse(String sessionId, IdentityProvider identityProvider, 
                                           Map<String, Object> debugResult) {
        DebugResponse response = new DebugResponse();
        response.setSessionId(sessionId);
        response.setTargetIdp(identityProvider.getIdentityProviderName());
        
        Boolean authSuccess = (Boolean) debugResult.get("authenticationSuccess");
        response.setStatus(Boolean.TRUE.equals(authSuccess) ? "SUCCESS" : "FAILURE");

        // Build authentication result.
        AuthenticationResult authResult = new AuthenticationResult();
        authResult.setSuccess(Boolean.TRUE.equals(authSuccess));
        authResult.setUserExists(Boolean.TRUE.equals(authSuccess));
        response.setAuthenticationResult(authResult);

        // Build claims analysis.
        Map<String, String> claims = (Map<String, String>) debugResult.get("claims");
        if (claims != null) {
            ClaimsAnalysis claimsAnalysis = new ClaimsAnalysis();
            claimsAnalysis.setOriginalRemoteClaims(claims);
            claimsAnalysis.setMappedLocalClaims(claims);
            claimsAnalysis.setMappingErrors(Collections.emptyList());
            response.setClaimsAnalysis(claimsAnalysis);
        }

        // Add metadata.
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("idpResourceId", identityProvider.getResourceId());
        metadata.put("timestamp", debugResult.get("timestamp"));
        response.setMetadata(metadata);

        return response;
    }

    /**
     * Generates unique debug session ID.
     *
     * @return Debug session ID.
     */
    private String generateDebugSessionId() {
        return "debug-" + UUID.randomUUID().toString();
    }




}
