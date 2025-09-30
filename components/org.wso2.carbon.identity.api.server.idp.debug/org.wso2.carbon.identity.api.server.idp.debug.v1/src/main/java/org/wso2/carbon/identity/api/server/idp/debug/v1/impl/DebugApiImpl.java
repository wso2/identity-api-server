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

package org.wso2.carbon.identity.api.server.idp.debug.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.idp.debug.v1.DebugApi;
import org.wso2.carbon.identity.debug.framework.DebugFlowService;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugRequest;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse.AuthenticationResult;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse.ClaimsAnalysis;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;
import org.wso2.carbon.identity.debug.framework.ContextProvider;
import org.wso2.carbon.identity.debug.framework.Executer;
import org.wso2.carbon.identity.debug.framework.RequestCoordinator;
import org.wso2.carbon.identity.debug.framework.Processor;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;

import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.Response;

/**
 * Implementation of DFDP Debug API for testing Identity Provider authentication flows.
 * This implementation provides debugging capabilities with event listener-based claim processing.
 */
public class DebugApiImpl implements DebugApi {

    private static final Log LOG = LogFactory.getLog(DebugApiImpl.class);
    private final ContextProvider contextProvider = new ContextProvider();
    private final Executer executer = new Executer();
    private final RequestCoordinator coordinator = new RequestCoordinator();
    private final Processor processor = new Processor();
    private final DebugFlowService debugService;

    public DebugApiImpl() {
        this.debugService = new DebugFlowService();
    }

    /**
     * Test IdP and authenticator configuration.
     * @param debugRequest Debug request containing target IdP and authenticator
     * @return Debug response with configuration validation result
     */
    public Response testConfiguration(@Valid DebugRequest debugRequest) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Testing configuration for IdP: " + debugRequest.getTargetIdp() +
                         " and authenticator: " + debugRequest.getTargetAuthenticator());
            }
            IdentityProvider idp = IdentityProviderManager.getInstance().getIdPByName(debugRequest.getTargetIdp(), "carbon.super");
            boolean authenticatorExists = idp.getFederatedAuthenticatorConfigs() != null &&
                java.util.Arrays.stream(idp.getFederatedAuthenticatorConfigs())
                    .anyMatch(cfg -> cfg.getName().equals(debugRequest.getTargetAuthenticator()));
            DebugResponse response = new DebugResponse();
            response.setSessionId(java.util.UUID.randomUUID().toString());
            response.setTargetIdp(debugRequest.getTargetIdp());
            response.setAuthenticatorUsed(debugRequest.getTargetAuthenticator());
            response.setStatus(authenticatorExists ? "SUCCESS" : "FAILURE");
            AuthenticationResult authResult = new AuthenticationResult();
            authResult.setSuccess(authenticatorExists);
            authResult.setUserExists(false);
            response.setAuthenticationResult(authResult);
            return Response.ok(response).build();
        } catch (IdentityProviderManagementException e) {
            DebugResponse errorResponse = createErrorResponse(null, "IDP_NOT_FOUND", "Identity Provider not found: " + debugRequest.getTargetIdp());
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
        }
    }

    /**
     * Initiate a debug authentication flow with a specific Identity Provider.
     * Uses event listeners for claim processing instead of direct logging.
     * 
     * @param debugRequest Debug request containing target IdP and test parameters
     * @return Debug response with authentication flow analysis
     */
    public Response debugAuthenticate(@Valid DebugRequest debugRequest) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Initiating debug authentication for IdP: " + debugRequest.getTargetIdp() +
                         " with authenticator: " + debugRequest.getTargetAuthenticator());
            }
            String sessionId = "debug-session-" + UUID.randomUUID().toString();
            if (debugRequest.getTargetIdp() == null || debugRequest.getTargetIdp().trim().isEmpty()) {
                DebugResponse errorResponse = createErrorResponse(sessionId, "INVALID_REQUEST", 
                    "Target Identity Provider is required");
                return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
            }
            // Find IdP object
            IdentityProvider idp;
            try {
                idp = IdentityProviderManager.getInstance().getIdPByName(debugRequest.getTargetIdp(), "carbon.super");
            } catch (IdentityProviderManagementException e) {
                DebugResponse errorResponse = createErrorResponse(sessionId, "IDP_NOT_FOUND", "Identity Provider not found: " + debugRequest.getTargetIdp());
                return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
            }
            // Call debug framework service
            Map<String, Object> result = debugService.executeDebugFlow(
                idp,
                debugRequest.getTargetAuthenticator(),
                debugRequest.getTestUser(),
                null, // password (not provided in DebugRequest)
                sessionId,
                null, // HttpServletRequest (not available in API layer)
                null  // HttpServletResponse (not available in API layer)
            );
            DebugResponse debugResponse = new DebugResponse();
            debugResponse.setSessionId((String) result.get("debugSessionId"));
            debugResponse.setStatus((String) result.get("status"));
            debugResponse.setTargetIdp(debugRequest.getTargetIdp());
            debugResponse.setAuthenticatorUsed(debugRequest.getTargetAuthenticator());
            debugResponse.setAuthenticationResult(null); // TODO: Map processedResult if available
            debugResponse.setClaimsAnalysis(null); // TODO: Map processedResult if available
            // ...other fields as needed...
            if (LOG.isDebugEnabled()) {
                LOG.debug("Debug authentication completed with status: " + debugResponse.getStatus());
            }
            return Response.ok(debugResponse).build();
        } catch (Exception e) {
            LOG.error("Error occurred during debug authentication", e);
            DebugResponse errorResponse = createErrorResponse(null, "INTERNAL_ERROR", 
                "Internal server error during debug authentication: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

    /**
     * Test specific authenticator directly without full authentication flow.
     * 
     * @param idpId Target Identity Provider ID
     * @param authenticatorName Name of the authenticator to test
     * @param testClaims Optional test claims to verify
     * @return Debug response with authenticator test results
     */
    public Response testAuthenticator(String idpId, String authenticatorName, String testClaims) {
        // Feature not implemented in DebugFlowService. Return error response.
        DebugResponse errorResponse = createErrorResponse(null, "NOT_IMPLEMENTED", "testAuthenticator is not implemented in DebugFlowService.");
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity(errorResponse).build();
    }

    /**
     * Get debug session status and results.
     * 
     * @param sessionId Debug session ID
     * @return Debug response with session status and collected data
     */
    public Response getDebugSession(String sessionId) {
        // Feature not implemented in DebugFlowService. Return error response.
        DebugResponse errorResponse = createErrorResponse(sessionId, "NOT_IMPLEMENTED", "getDebugSession is not implemented in DebugFlowService.");
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity(errorResponse).build();
    }

    /**
     * Test authentication flow and return claims.
     * @param debugRequest Debug request containing test user and claims
     * @return Debug response with authentication and claims analysis
     */
    public Response testAuthentication(@Valid DebugRequest debugRequest) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Testing authentication for IdP: " + debugRequest.getTargetIdp() +
                         " and authenticator: " + debugRequest.getTargetAuthenticator());
            }
            IdentityProvider idp = IdentityProviderManager.getInstance().getIdPByName(debugRequest.getTargetIdp(), "carbon.super");
            String sessionId = java.util.UUID.randomUUID().toString();
            AuthenticationContext authContext = contextProvider.provideContext(null);
            authContext.setProperty("idpName", idp.getIdentityProviderName());
            authContext.setProperty("authenticatorName", debugRequest.getTargetAuthenticator());
            authContext.setProperty("username", debugRequest.getTestUser());
            authContext.setContextIdentifier(sessionId);
            boolean authResult = executer.execute(idp, authContext);
            coordinator.coordinate(authContext, null, null);
            Object processedResult = processor.process(authContext);
            DebugResponse response = new DebugResponse();
            response.setSessionId(sessionId);
            response.setTargetIdp(debugRequest.getTargetIdp());
            response.setAuthenticatorUsed(debugRequest.getTargetAuthenticator());
            response.setStatus(authResult ? "SUCCESS" : "FAILURE");
            AuthenticationResult result = new AuthenticationResult();
            result.setSuccess(authResult);
            result.setUserExists(authResult);
            result.setUserDetails(debugRequest.getTestUser());
            response.setAuthenticationResult(result);
            ClaimsAnalysis claims = new ClaimsAnalysis();
            claims.setOriginalRemoteClaims(debugRequest.getTestClaims());
            claims.setMappedLocalClaims(debugRequest.getTestClaims());
            claims.setMappingErrors(authResult ? null : java.util.Collections.singletonList("Mapping failed."));
            response.setClaimsAnalysis(claims);
            return Response.ok(response).build();
        } catch (IdentityProviderManagementException e) {
            DebugResponse errorResponse = createErrorResponse(null, "IDP_NOT_FOUND", "Identity Provider not found: " + debugRequest.getTargetIdp());
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
        }
    }

    /**
     * View claims processed during the flow.
     * @param sessionId Debug session ID
     * @return Debug response with claims analysis
     */
    public Response viewClaims(String sessionId) {
        // For demonstration, return a static response. In a real implementation, fetch from session store.
        DebugResponse response = new DebugResponse();
        response.setSessionId(sessionId);
        response.setStatus("SUCCESS");
        ClaimsAnalysis claims = new ClaimsAnalysis();
        claims.setOriginalRemoteClaims(java.util.Collections.singletonMap("email", "testuser@gmail.com"));
        claims.setMappedLocalClaims(java.util.Collections.singletonMap("local_email", "testuser@gmail.com"));
        claims.setMappingErrors(null);
        response.setClaimsAnalysis(claims);
        return Response.ok(response).build();
    }

    /**
     * Create an error response with the specified details.
     * 
     * @param sessionId Session ID (can be null)
     * @param errorCode Error code
     * @param errorMessage Error message
     * @return DebugResponse with error information
     */
    private DebugResponse createErrorResponse(String sessionId, String errorCode, String errorMessage) {
        DebugResponse errorResponse = new DebugResponse();
        errorResponse.setSessionId(sessionId);
        errorResponse.setStatus("FAILURE");

        DebugResponse.DebugError error = new DebugResponse.DebugError();
        error.setCode(errorCode);
        error.setMessage(errorMessage);

        errorResponse.setErrors(java.util.Arrays.asList(error));
        return errorResponse;
    }
}
