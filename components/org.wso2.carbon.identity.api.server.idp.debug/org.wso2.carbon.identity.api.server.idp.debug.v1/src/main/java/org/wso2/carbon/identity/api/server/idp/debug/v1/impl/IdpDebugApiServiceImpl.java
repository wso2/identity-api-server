/*
 * Copyright (c) 2019-2025, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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
import org.wso2.carbon.identity.api.server.idp.debug.v1.IdpDebugApi;
import org.wso2.carbon.identity.debug.framework.DebugFlowService;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of IdP Debug Flow Data Provider API.
 */
@Path("/debug")
@PermitAll
public class IdpDebugApiServiceImpl implements IdpDebugApi {

    private static final Log LOG = LogFactory.getLog(IdpDebugApiServiceImpl.class);

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    public IdpDebugApiServiceImpl() {
        //this.dfdpService = new DFDPService();
    }

    /**
     * Handles the debug authentication flow request.
     *
     * @return Response containing debug flow results.
     */
    @Override
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response debug() {
        try {
            // Parse query parameters from the request.
            String idpResourceId = request.getParameter("idpResourceId");
            String authenticatorName = request.getParameter("authenticatorName");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String sessionDataKey = request.getParameter("sessionDataKey");
            if (sessionDataKey == null || sessionDataKey.isEmpty()) {
                sessionDataKey = java.util.UUID.randomUUID().toString();
            }

            // Validate required parameters.
            if (idpResourceId == null || authenticatorName == null || username == null || password == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("status", "error");
                error.put("message", "Missing required parameters: idpResourceId, authenticatorName, username, password");
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

            // Fetch IdP object using idp-mgt by resourceId.
            IdentityProviderManager idpManager = IdentityProviderManager.getInstance();
            IdentityProvider idpObj;
            try {
                idpObj = idpManager.getIdPByResourceId(idpResourceId, "carbon.super", true);
            } catch (IdentityProviderManagementException e) {
                Map<String, Object> error = new HashMap<>();
                error.put("status", "error");
                error.put("message", "Invalid IdP resourceId: " + e.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
            if (idpObj == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("status", "error");
                error.put("message", "Identity Provider not found for resourceId: " + idpResourceId);
                return Response.status(Response.Status.NOT_FOUND).entity(error).build();
            }

            // Delegate debug flow logic to DebugFlowService in debug-framework.
            DebugFlowService debugFlowService = new DebugFlowService();
            Map<String, Object> debugResults = debugFlowService.executeDebugFlow(
                idpObj, authenticatorName, username, password, sessionDataKey, request, response);

            // Build DebugResponse (populate as much as possible from debugResults).
            org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse debugResponse = new org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse();
            debugResponse.setSessionId((String) debugResults.getOrDefault("debugSessionId", ""));
            debugResponse.setStatus((String) debugResults.getOrDefault("status", "IN_PROGRESS"));
            debugResponse.setTargetIdp(idpObj.getIdentityProviderName());
            debugResponse.setAuthenticatorUsed(authenticatorName);
            debugResponse.setMetadata((Map<String, Object>) debugResults.get("metadata"));
            // TODO: Populate authenticationResult, claimsAnalysis, flowEvents, errors from logger/analyzer/reporter if available.

            return Response.ok(debugResponse).build();
        } catch (Exception e) {
            LOG.error("Error in debug endpoint", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Internal server error: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(errorResponse)
                          .build();
        }
    }

}
