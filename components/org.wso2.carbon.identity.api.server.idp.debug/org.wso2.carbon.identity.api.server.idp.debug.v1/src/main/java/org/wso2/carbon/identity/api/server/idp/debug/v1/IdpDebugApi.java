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

package org.wso2.carbon.identity.api.server.idp.debug.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.wso2.carbon.identity.api.server.idp.debug.common.Constants;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DFDPTestRequest;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DFDPTestResponse;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * IdP Debug Flow Data Provider API.
 */
@Path(Constants.V1.DFDP_API_PATH_COMPONENT)
@Api(value = "IdP Debug Flow Data Provider", 
     description = "REST API for testing external identity provider claim mappings and authentication flows")
public interface IdpDebugApi {

/**
 * IdP Debug Flow Data Provider API.
 */
@Path(Constants.V1.DFDP_API_PATH_COMPONENT)
@Api(value = "IdP Debug Flow Data Provider", 
     description = "REST API for testing external identity provider claim mappings and authentication flows")
public interface IdpDebugApi {

    /**
     * Debug endpoint for identity provider functionality.
     * Supports multiple operations based on query parameters:
     * - Test authentication (POST with testRequest body)
     * - List available IdPs (GET with action=list-idps)
     * - Get IdP authenticators (GET with action=list-authenticators&idpName=...)
     * - Health check (GET with action=health)
     *
     * @param action Action to perform (list-idps, list-authenticators, health)
     * @param idpName Identity provider name (required for authentication test and list-authenticators)
     * @param authenticatorName Authenticator name (optional for authentication test)
     * @param format Response format (json, html, text, summary)
     * @param testRequest Test request details (for POST requests)
     * @return Response based on the action requested
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Test external identity provider authentication",
            notes = "This operation tests authentication with an external identity provider and " +
                    "returns claim mappings and authentication details.",
            response = DFDPTestResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = DFDPTestResponse.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Server Error")
    })
    Response debugIdpAuthentication(
            @ApiParam(value = "Identity provider name", required = true)
            @QueryParam("idpName") String idpName,
            
            @ApiParam(value = "Authenticator name (optional)")
            @QueryParam("authenticatorName") String authenticatorName,
            
            @ApiParam(value = "Response format (json, html, text, summary)", allowableValues = "json,html,text,summary")
            @QueryParam("format") String format,
            
            @ApiParam(value = "Test request details")
            @Valid DFDPTestRequest testRequest
    );

    /**
     * Debug endpoint for identity provider functionality (GET operations).
     * Supports multiple operations based on query parameters:
     * - List available IdPs (action=list-idps)
     * - Get IdP authenticators (action=list-authenticators&idpName=...)
     * - Health check (action=health or no action)
     *
     * @param action Action to perform (list-idps, list-authenticators, health)
     * @param idpName Identity provider name (required for list-authenticators)
     * @return Response based on the action requested
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Debug identity provider operations",
            notes = "Supports various debug operations: list IdPs, get authenticators, health check.",
            response = Object.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Server Error")
    })
    Response debugIdpOperations(
            @ApiParam(value = "Action to perform", allowableValues = "list-idps,list-authenticators,health")
            @QueryParam("action") String action,
            
            @ApiParam(value = "Identity provider name (required for list-authenticators)")
            @QueryParam("idpName") String idpName
    );
}
