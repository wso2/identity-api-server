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

package org.wso2.carbon.identity.api.server.idp.debug.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugRequest;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugResponse;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * DFDP (Debug Flow Data Provider) API for testing Identity Provider authentication flows.
 * This API provides debugging capabilities for IdP authentication without using the commonauth endpoint.
 */
@Path("/debug")
@Api(description = "DFDP Debug API")
public interface DebugApi {

    /**
     * Initiate a debug authentication flow with a specific Identity Provider.
     * This endpoint bypasses the normal authentication flow and provides detailed debugging information.
     * 
     * @param debugRequest Debug request containing target IdP and test parameters
     * @return Debug response with authentication flow analysis
     */
    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
        @ApiOperation(value = "Debug IdP Authentication Flow",
                  notes = "Initiates debug authentication flow for the specified Identity Provider")
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Debug authentication initiated successfully", 
                     response = DebugResponse.class),
        @ApiResponse(code = 400, message = "Invalid debug request parameters"),
        @ApiResponse(code = 404, message = "Target Identity Provider not found"),
        @ApiResponse(code = 500, message = "Internal Server Error") 
    })
    Response debugAuthenticate(@ApiParam(value = "Debug authentication request", required = true) 
                              @Valid DebugRequest debugRequest);

    /**
     * Test specific authenticator directly without full authentication flow.
     * 
     * @param idpId Target Identity Provider ID
     * @param authenticatorName Name of the authenticator to test
     * @param testClaims Optional test claims to verify
     * @return Debug response with authenticator test results
     */
    @GET
    @Path("/test-authenticator")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Test Specific Authenticator", 
                  notes = "Test a specific authenticator configuration and claim mapping", 
                  response = DebugResponse.class,
                  tags = {"DFDP Debug"})
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Authenticator test completed", response = DebugResponse.class),
        @ApiResponse(code = 400, message = "Invalid test parameters"),
        @ApiResponse(code = 404, message = "Authenticator not found"),
        @ApiResponse(code = 500, message = "Internal Server Error") 
    })
    Response testAuthenticator(@ApiParam(value = "Identity Provider ID", required = true) 
                              @QueryParam("idpId") String idpId,
                              @ApiParam(value = "Authenticator name", required = true) 
                              @QueryParam("authenticator") String authenticatorName,
                              @ApiParam(value = "Test claims (JSON format)") 
                              @QueryParam("testClaims") String testClaims);

    /**
     * Get debug session status and results.
     * 
     * @param sessionId Debug session ID
     * @return Debug response with session status and collected data
     */
    @GET
    @Path("/session/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get Debug Session Status", 
                  notes = "Retrieve the status and results of a debug session", 
                  response = DebugResponse.class,
                  tags = {"DFDP Debug"})
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Debug session status retrieved", response = DebugResponse.class),
        @ApiResponse(code = 404, message = "Debug session not found"),
        @ApiResponse(code = 500, message = "Internal Server Error") 
    })
    Response getDebugSession(@ApiParam(value = "Debug session ID", required = true) 
                            @PathParam("sessionId") String sessionId);
}
