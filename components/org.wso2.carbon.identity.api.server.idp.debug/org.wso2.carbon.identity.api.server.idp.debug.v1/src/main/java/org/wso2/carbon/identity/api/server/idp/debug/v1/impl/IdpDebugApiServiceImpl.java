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
import org.wso2.carbon.identity.api.server.idp.debug.v1.core.DFDPService;
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
            // Use injected request/response for DefaultRequestCoordinator
            if (request != null && response != null) {
                request.setAttribute("action", "dfdp-debug");
                DefaultRequestCoordinator coordinator = new DefaultRequestCoordinator();
                coordinator.handle(request, response);
                // The response is already written by the coordinator
                return Response.ok().build();
            }
            // Fallback: original logic if request/response not available
            // Create a comprehensive debug response with IdP testing capabilities
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("status", "success");
            responseMap.put("message", "DFDP debug endpoint is working");
            responseMap.put("timestamp", System.currentTimeMillis());
            responseMap.put("version", "1.0.0");
            responseMap.put("service", "IdP Debug Flow Data Provider");
            
            // Add available testing endpoints
            Map<String, String> availableEndpoints = new HashMap<>();
            availableEndpoints.put("getIdps", 
                "GET /api/server/v1/debug/idps - Get available identity providers");
            availableEndpoints.put("getAuthenticators", 
                "GET /api/server/v1/debug/idps/{idpName}/authenticators - Get IdP authenticators");
            availableEndpoints.put("testAuthentication", 
                "POST /api/server/v1/debug/idps/{idpName}/test - Test IdP authentication");
            availableEndpoints.put("testSAML", 
                "GET /api/server/v1/debug/test/saml?idp={idpName} - Test SAML federated authenticator");
            availableEndpoints.put("testOIDC", 
                "GET /api/server/v1/debug/test/oidc?idp={idpName} - Test OIDC federated authenticator");
            availableEndpoints.put("testGoogle", 
                "GET /api/server/v1/debug/test/google - Test Google OIDC authenticator");
            availableEndpoints.put("testFacebook", 
                "GET /api/server/v1/debug/test/facebook - Test Facebook authenticator");
            availableEndpoints.put("testCustom", 
                "GET /api/server/v1/debug/test/custom?authenticator={name}&idp={idpName} - Test custom authenticator");
            responseMap.put("availableEndpoints", availableEndpoints);
            
            // Add system information
            Map<String, Object> systemInfo = new HashMap<>();
            systemInfo.put("javaVersion", System.getProperty("java.version"));
            systemInfo.put("osName", System.getProperty("os.name"));
            systemInfo.put("userTimezone", System.getProperty("user.timezone"));
            responseMap.put("systemInfo", systemInfo);
            
            return Response.ok(responseMap).build();
            
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

    /**
     * Test SAML federated authenticator with a specific IdP.
     */
    @GET
    @Path("/test/saml")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response testSAMLAuthenticator(@QueryParam("idp") String idpName) {
        try {
            if (idpName == null || idpName.trim().isEmpty()) {
                idpName = "DefaultSAMLIdP";
            }
            
            LOG.info("Testing SAML authenticator with IdP: " + idpName);
            
            Object testResult = dfdpService.testIdpAuthentication(idpName, "SAML2Authenticator", "json");
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "SAML authenticator test completed");
            response.put("authenticatorType", "SAML2Authenticator");
            response.put("idpName", idpName);
            response.put("testResult", testResult);
            response.put("timestamp", System.currentTimeMillis());
            response.put("usageInfo", "This endpoint tests SAML2 federated authentication flow and claim mappings");
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            LOG.error("Error testing SAML authenticator", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error testing SAML authenticator: " + e.getMessage());
            errorResponse.put("authenticatorType", "SAML2Authenticator");
            errorResponse.put("idpName", idpName);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(errorResponse)
                          .build();
        }
    }

    /**
     * Test OIDC federated authenticator with a specific IdP.
     */
    @GET
    @Path("/test/oidc")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response testOIDCAuthenticator(@QueryParam("idp") String idpName) {
        try {
            if (idpName == null || idpName.trim().isEmpty()) {
                idpName = "DefaultOIDCIdP";
            }
            
            LOG.info("Testing OIDC authenticator with IdP: " + idpName);
            
            Object testResult = dfdpService.testIdpAuthentication(idpName, "OIDCAuthenticator", "json");
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "OIDC authenticator test completed");
            response.put("authenticatorType", "OIDCAuthenticator");
            response.put("idpName", idpName);
            response.put("testResult", testResult);
            response.put("timestamp", System.currentTimeMillis());
            response.put("usageInfo", 
                "This endpoint tests OpenID Connect federated authentication flow and claim mappings");
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            LOG.error("Error testing OIDC authenticator", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error testing OIDC authenticator: " + e.getMessage());
            errorResponse.put("authenticatorType", "OIDCAuthenticator");
            errorResponse.put("idpName", idpName);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(errorResponse)
                          .build();
        }
    }

    /**
     * Test Google OIDC federated authenticator.
     */
    @GET
    @Path("/test/google")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response testGoogleAuthenticator() {
        try {
            LOG.info("Testing Google OIDC authenticator");
            
            Object testResult = dfdpService.testIdpAuthentication("Google", "GoogleOIDCAuthenticator", "json");
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Google OIDC authenticator test completed");
            response.put("authenticatorType", "GoogleOIDCAuthenticator");
            response.put("idpName", "Google");
            response.put("testResult", testResult);
            response.put("timestamp", System.currentTimeMillis());
            response.put("usageInfo", "This endpoint tests Google OAuth 2.0 / OIDC authentication and claim mappings");
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            LOG.error("Error testing Google authenticator", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error testing Google authenticator: " + e.getMessage());
            errorResponse.put("authenticatorType", "GoogleOIDCAuthenticator");
            errorResponse.put("idpName", "Google");
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(errorResponse)
                          .build();
        }
    }

    /**
     * Test Facebook federated authenticator.
     */
    @GET
    @Path("/test/facebook")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response testFacebookAuthenticator() {
        try {
            LOG.info("Testing Facebook authenticator");
            
            Object testResult = dfdpService.testIdpAuthentication("Facebook", "FacebookAuthenticator", "json");
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Facebook authenticator test completed");
            response.put("authenticatorType", "FacebookAuthenticator");
            response.put("idpName", "Facebook");
            response.put("testResult", testResult);
            response.put("timestamp", System.currentTimeMillis());
            response.put("usageInfo", "This endpoint tests Facebook Graph API authentication and claim mappings");
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            LOG.error("Error testing Facebook authenticator", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error testing Facebook authenticator: " + e.getMessage());
            errorResponse.put("authenticatorType", "FacebookAuthenticator");
            errorResponse.put("idpName", "Facebook");
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(errorResponse)
                          .build();
        }
    }

    /**
     * Test custom federated authenticator.
     */
    @GET
    @Path("/test/custom")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response testCustomAuthenticator(@QueryParam("authenticator") String authenticatorName, 
                                          @QueryParam("idp") String idpName) {
        try {
            if (authenticatorName == null || authenticatorName.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "Authenticator name is required. Use ?authenticator=<name>");
                errorResponse.put("timestamp", System.currentTimeMillis());
                errorResponse.put("example", 
                    "/api/server/v1/debug/test/custom?authenticator=MyCustomAuthenticator&idp=MyIdP");
                
                return Response.status(Response.Status.BAD_REQUEST)
                              .entity(errorResponse)
                              .build();
            }
            
            if (idpName == null || idpName.trim().isEmpty()) {
                idpName = "DefaultIdP";
            }
            
            LOG.info("Testing custom authenticator: " + authenticatorName + " with IdP: " + idpName);
            
            Object testResult = dfdpService.testIdpAuthentication(idpName, authenticatorName, "json");
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Custom authenticator test completed");
            response.put("authenticatorType", authenticatorName);
            response.put("idpName", idpName);
            response.put("testResult", testResult);
            response.put("timestamp", System.currentTimeMillis());
            response.put("usageInfo", "This endpoint tests custom federated authenticators and their claim mappings");
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            LOG.error("Error testing custom authenticator: " + authenticatorName, e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error testing custom authenticator: " + e.getMessage());
            errorResponse.put("authenticatorType", authenticatorName);
            errorResponse.put("idpName", idpName);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(errorResponse)
                          .build();
        }
    }
}
