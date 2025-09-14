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
import org.wso2.carbon.identity.dfdp.core.DFDPService;
import org.wso2.carbon.identity.application.authentication.framework.handler.request.impl.DefaultRequestCoordinator;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkUtils;
import org.wso2.carbon.identity.application.authentication.framework.cache.AuthenticationContextCache;
import org.wso2.carbon.identity.application.authentication.framework.cache.AuthenticationContextCacheKey;
import org.wso2.carbon.identity.application.authentication.framework.cache.AuthenticationContextCacheEntry;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    //private final DFDPService dfdpService;

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    public IdpDebugApiServiceImpl() {
        //this.dfdpService = new DFDPService();
    }

    @Override
    @GET
    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response debug() {
        try {
            // Parse query parameters from the request.
            String idpName = request.getParameter("idpName");
            String authenticatorName = request.getParameter("authenticatorName");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String sessionDataKey = request.getParameter("sessionDataKey");
            if (sessionDataKey == null || sessionDataKey.isEmpty()) {
                sessionDataKey = java.util.UUID.randomUUID().toString();
            }

            // Validate required parameters.
            if (idpName == null || authenticatorName == null || username == null || password == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("status", "error");
                error.put("message", "Missing required parameters: idpName, authenticatorName, username, password");
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

            // Create and configure AuthenticationContext.
            AuthenticationContext authContext = new AuthenticationContext();
            authContext.setRequestType("DFDP_DEBUG");
            authContext.setCallerSessionKey(java.util.UUID.randomUUID().toString());
            authContext.setProperty("idpName", idpName);
            authContext.setProperty("authenticatorName", authenticatorName);
            authContext.setProperty("username", username);
            authContext.setProperty("password", password);
            authContext.setTenantDomain("carbon.super");
            authContext.setRelyingParty("DFDP_DEBUG_SP");
            authContext.setProperty("IS_DEBUG_FLOW", Boolean.TRUE);
            request.setAttribute("sessionDataKey", sessionDataKey);
            request.setAttribute("AuthenticationContext", authContext);
            request.setAttribute("DFDP_API_FLOW", Boolean.TRUE);
            String debugSessionId = java.util.UUID.randomUUID().toString();
            request.setAttribute("DFDP_DEBUG_SESSION_ID", debugSessionId);

            // Ensure SequenceConfig is set to avoid NPE in framework.
            if (authContext.getSequenceConfig() == null) {
                org.wso2.carbon.identity.application.authentication.framework.config.model.SequenceConfig sequenceConfig =
                        new org.wso2.carbon.identity.application.authentication.framework.config.model.SequenceConfig();
                sequenceConfig.setName("DFDP_DEBUG_SEQUENCE");
                sequenceConfig.setApplicationId("DFDP_DEBUG_SP");
                org.wso2.carbon.identity.application.authentication.framework.config.model.StepConfig stepConfig =
                        new org.wso2.carbon.identity.application.authentication.framework.config.model.StepConfig();
                stepConfig.setOrder(1);
                stepConfig.setSubjectIdentifierStep(true);
                stepConfig.setSubjectAttributeStep(true);
                org.wso2.carbon.identity.application.authentication.framework.config.model.AuthenticatorConfig authenticatorConfig =
                        new org.wso2.carbon.identity.application.authentication.framework.config.model.AuthenticatorConfig();
                authenticatorConfig.setName(authenticatorName);
                authenticatorConfig.setEnabled(true);
                java.util.List<String> idpNames = new java.util.ArrayList<>();
                idpNames.add(idpName);
                authenticatorConfig.setIdPNames(idpNames);
                java.util.Map<String, org.wso2.carbon.identity.application.common.model.IdentityProvider> idps = new java.util.HashMap<>();
                org.wso2.carbon.identity.application.common.model.IdentityProvider idpObj = new org.wso2.carbon.identity.application.common.model.IdentityProvider();
                idpObj.setIdentityProviderName(idpName);
                org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig fedAuthConfig =
                        new org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig();
                fedAuthConfig.setName(authenticatorName);
                fedAuthConfig.setEnabled(true);
                idpObj.setFederatedAuthenticatorConfigs(new org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig[]{fedAuthConfig});
                idpObj.setDefaultAuthenticatorConfig(fedAuthConfig);
                idps.put(idpName, idpObj);
                authenticatorConfig.setIdPs(idps);
                stepConfig.setAuthenticatorList(java.util.Collections.singletonList(authenticatorConfig));
                java.util.Map<Integer, org.wso2.carbon.identity.application.authentication.framework.config.model.StepConfig> stepMap =
                        new java.util.HashMap<>();
                stepMap.put(1, stepConfig);
                sequenceConfig.setStepMap(stepMap);
                org.wso2.carbon.identity.application.common.model.ServiceProvider sp =
                        new org.wso2.carbon.identity.application.common.model.ServiceProvider();
                sp.setApplicationName("DFDP_DEBUG_SP");
                sp.setApplicationResourceId("DFDP_DEBUG_SP_RESOURCE_ID");
                org.wso2.carbon.identity.application.authentication.framework.config.model.ApplicationConfig appConfig =
                        new org.wso2.carbon.identity.application.authentication.framework.config.model.ApplicationConfig(sp, "carbon.super");
                sequenceConfig.setApplicationConfig(appConfig);
                authContext.setSequenceConfig(sequenceConfig);
                authContext.setProperty("ServiceProviderResourceId", "DFDP_DEBUG_SP_RESOURCE_ID");
            }

            // Set the context identifier to match the sessionDataKey for framework compatibility.
            authContext.setContextIdentifier(sessionDataKey);

            // Add AuthenticationContext to cache so the framework can find it.
            final String finalSessionDataKey = sessionDataKey;
            AuthenticationContextCacheKey cacheKey = new AuthenticationContextCacheKey(finalSessionDataKey);
            AuthenticationContextCacheEntry cacheEntry = new AuthenticationContextCacheEntry(authContext);
            AuthenticationContextCache.getInstance().addToCache(cacheKey, cacheEntry);
            request.setAttribute("sessionDataKey", finalSessionDataKey);

            // Register DFDP event listener for this debug session
            DFDPService dfdpService = new DFDPService();
            // dfdpService.registerLogger(debugSessionId, ...); // If such a method exists

            // Wrap the request to inject sessionDataKey as a parameter for the framework.
            javax.servlet.http.HttpServletRequest wrappedRequest = new javax.servlet.http.HttpServletRequestWrapper(request) {
                @Override
                public String getParameter(String name) {
                    if ("sessionDataKey".equals(name)) {
                        return finalSessionDataKey;
                    }
                    return super.getParameter(name);
                }
            };

            // Invoke the authentication flow using DefaultRequestCoordinator.
            DefaultRequestCoordinator coordinator = new DefaultRequestCoordinator();
            coordinator.handle(wrappedRequest, response);

            // After the flow, collect events, claims, and run analysis/reporting.
            // This is a placeholder for the actual event/claim collection and analysis logic.
            Map<String, Object> debugResults = dfdpService.getAllRealIncomingClaims(idpName, authenticatorName, finalSessionDataKey);
            // Optionally, run analysis and reporting here if available in DFDPService.

            // Build DebugResponse (populate as much as possible from debugResults and captured events).
            org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse debugResponse = new org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse();
            debugResponse.setSessionId(debugSessionId);
            debugResponse.setStatus((String) debugResults.getOrDefault("status", "IN_PROGRESS"));
            debugResponse.setTargetIdp(idpName);
            debugResponse.setAuthenticatorUsed(authenticatorName);
            // Populate authenticationResult, claimsAnalysis, flowEvents, errors, metadata as available.
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

    @GET
    @Path("/idps")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableIdps() {
        try {
            LOG.info("Getting available identity providers");
            // Use local DFDPService instance for testing.
            Object idpsResult = new DFDPService().getAvailableIdps();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Available identity providers retrieved");
            response.put("result", idpsResult);
            response.put("timestamp", System.currentTimeMillis());
            return Response.ok(response).build();
        } catch (Exception e) {
            LOG.error("Error getting available IdPs", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error getting available IdPs: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(errorResponse)
                          .build();
        }
    }
}
