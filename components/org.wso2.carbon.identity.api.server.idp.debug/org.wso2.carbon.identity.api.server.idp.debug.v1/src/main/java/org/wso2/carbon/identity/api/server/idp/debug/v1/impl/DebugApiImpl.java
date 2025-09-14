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
import org.wso2.carbon.identity.api.server.idp.debug.v1.core.DFDPDebugService;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugRequest;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse;

import java.util.UUID;
import javax.validation.Valid;
import javax.ws.rs.core.Response;

/**
 * Implementation of DFDP Debug API for testing Identity Provider authentication flows.
 * This implementation provides debugging capabilities with event listener-based claim processing.
 */
public class DebugApiImpl implements DebugApi {

    private static final Log LOG = LogFactory.getLog(DebugApiImpl.class);
    private final DFDPDebugService debugService;

    public DebugApiImpl() {
        this.debugService = new DFDPDebugService();
    }

    /**
     * Initiate a debug authentication flow with a specific Identity Provider.
     * Uses event listeners for claim processing instead of direct logging.
     * 
     * @param debugRequest Debug request containing target IdP and test parameters
     * @return Debug response with authentication flow analysis
     */
    @Override
    public Response debugAuthenticate(@Valid DebugRequest debugRequest) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Initiating DFDP debug authentication for IdP: " + debugRequest.getTargetIdp() +
                         " with authenticator: " + debugRequest.getTargetAuthenticator());
            }

            // Generate unique session ID for this debug session
            String sessionId = "dfdp-session-" + UUID.randomUUID().toString();

            // Validate debug request parameters
            if (debugRequest.getTargetIdp() == null || debugRequest.getTargetIdp().trim().isEmpty()) {
                DebugResponse errorResponse = createErrorResponse(sessionId, "INVALID_REQUEST", 
                    "Target Identity Provider is required");
                return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
            }

            // Process debug authentication using event listeners
            DebugResponse debugResponse = debugService.processDebugAuthentication(sessionId, debugRequest);

            if (LOG.isDebugEnabled()) {
                LOG.debug("DFDP debug authentication completed with status: " + debugResponse.getStatus());
            }

            return Response.ok(debugResponse).build();

        } catch (Exception e) {
            LOG.error("Error occurred during DFDP debug authentication", e);
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
    @Override
    public Response testAuthenticator(String idpId, String authenticatorName, String testClaims) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Testing authenticator: " + authenticatorName + " for IdP: " + idpId);
            }

            // Generate session ID for authenticator test
            String sessionId = "dfdp-auth-test-" + UUID.randomUUID().toString();

            // Validate parameters
            if (idpId == null || idpId.trim().isEmpty() || 
                authenticatorName == null || authenticatorName.trim().isEmpty()) {
                DebugResponse errorResponse = createErrorResponse(sessionId, "INVALID_PARAMETERS", 
                    "Both IdP ID and authenticator name are required");
                return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
            }

            // Test authenticator configuration and claim mapping
            DebugResponse debugResponse = debugService.testAuthenticatorConfiguration(
                sessionId, idpId, authenticatorName, testClaims);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Authenticator test completed with status: " + debugResponse.getStatus());
            }

            return Response.ok(debugResponse).build();

        } catch (Exception e) {
            LOG.error("Error occurred during authenticator testing", e);
            DebugResponse errorResponse = createErrorResponse(null, "INTERNAL_ERROR", 
                "Internal server error during authenticator test: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

    /**
     * Get debug session status and results.
     * 
     * @param sessionId Debug session ID
     * @return Debug response with session status and collected data
     */
    @Override
    public Response getDebugSession(String sessionId) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Retrieving debug session status for session: " + sessionId);
            }

            // Validate session ID
            if (sessionId == null || sessionId.trim().isEmpty()) {
                DebugResponse errorResponse = createErrorResponse(null, "INVALID_SESSION", 
                    "Session ID is required");
                return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
            }

            // Retrieve session data and event listener results
            DebugResponse debugResponse = debugService.getDebugSessionData(sessionId);

            if (debugResponse == null) {
                DebugResponse notFoundResponse = createErrorResponse(sessionId, "SESSION_NOT_FOUND", 
                    "Debug session not found");
                return Response.status(Response.Status.NOT_FOUND).entity(notFoundResponse).build();
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Debug session data retrieved successfully for session: " + sessionId);
            }

            return Response.ok(debugResponse).build();

        } catch (Exception e) {
            LOG.error("Error occurred while retrieving debug session data", e);
            DebugResponse errorResponse = createErrorResponse(sessionId, "INTERNAL_ERROR", 
                "Internal server error while retrieving session data: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
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
