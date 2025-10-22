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

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.wso2.carbon.identity.api.server.idp.debug.v1.factories.DebugApiServiceFactory;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.Error;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * Debug API for OAuth 2.0 authentication flow debugging.
 */
@Path("/debug")
@Api(description = "The debug API")
public class DebugApi {

    private final DebugApiService delegate;

    public DebugApi() {
        this.delegate = DebugApiServiceFactory.getDebugApi();
    }

        /**
         * Retrieves the debug result for a given session ID (state).
         * @param sessionId The session ID (state) to fetch the debug result for.
         * @return JSON debug result or 404 if not found.
         */
        @GET
        @Path("/result/{session-id}")
        @Produces({ "application/json" })
        @ApiOperation(value = "Get debug result by session ID", notes = "Fetches the debug result for the given session ID (state).", response = String.class)
        @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Debug result found", response = String.class),
            @ApiResponse(code = 404, message = "Debug result not found", response = Error.class)
        })
        public Response getDebugResult(@ApiParam(value = "Session ID (state)", required = true) @PathParam("session-id") String sessionId) {
            String result = org.wso2.carbon.identity.debug.framework.DebugResultCache.get(sessionId);
            if (result != null) {
                return Response.ok(result).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Debug result not found").build();
            }
        }
    @Valid
    @POST
    @Path("/connection/{idp-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Debug identity provider connection", 
                  notes = "This API provides the capability to debug identity provider connections.", 
                  response = DebugConnectionResponse.class, 
                  authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {})
    }, tags = { "Identity Provider Debug" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = DebugConnectionResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response debugConnection(@ApiParam(value = "ID of the identity provider", required = true) 
                                  @PathParam("idp-id") String idpId,
                                  @ApiParam(value = "Debug connection request", required = true) 
                                  @Valid DebugConnectionRequest debugConnectionRequest) {
        return delegate.debugConnection(idpId, debugConnectionRequest);
    }
}
