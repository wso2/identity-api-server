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
    private final DFDPService dfdpService;

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    public IdpDebugApiServiceImpl() {
        this.dfdpService = new DFDPService();
    }

    @Override
    @GET
    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response debug() {
        try {
            // Parse query parameters from the request.
            String action = request.getParameter("action");
            String idpName = request.getParameter("idpName");
            String authenticatorName = request.getParameter("authenticatorName");
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // Set parameters as request attributes for the authentication framework.
            if (action != null) {
                request.setAttribute("action", action);
            }
            if (idpName != null) {
                request.setAttribute("idpName", idpName);
            }
            if (authenticatorName != null) {
                request.setAttribute("authenticatorName", authenticatorName);
            }
            if (username != null) {
                request.setAttribute("username", username);
            }
            if (password != null) {
                request.setAttribute("password", password);
            }

            // Mark this as an API-based DFDP flow for the framework.
            request.setAttribute("DFDP_API_FLOW", Boolean.TRUE);

            // Invoke the real authentication flow using DefaultRequestCoordinator.
            DefaultRequestCoordinator coordinator = new DefaultRequestCoordinator();
            coordinator.handle(request, response);
            // The response is already written by the coordinator (redirect, JSON, etc.)
            return Response.ok().build();
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
            
            Object idpsResult = dfdpService.getAvailableIdps();
            
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
