/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.debug.v1;

import org.wso2.carbon.identity.api.server.debug.v1.factories.DebugApiServiceFactory;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.debug.v1.model.Error;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Path("/debug")
@Api(description = "The debug API")
public class DebugApi {

    private final DebugApiService delegate;

    public DebugApi() {

        this.delegate = DebugApiServiceFactory.getDebugApi();
    }

    @POST
    @Path("")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Start debug session", notes = "Initiates a debug session for any resource type and properties.", response = DebugConnectionResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = { "Debug" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = DebugConnectionResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response startDebugSession(@Valid DebugConnectionRequest debugConnectionRequest) {

        return delegate.startDebugSession(debugConnectionRequest);
    }

    @GET
    @Path("/result/{session-id}")
    @Produces({ "application/json" })
    @ApiOperation(value = "Get debug result by session ID", notes = "Fetches the debug result for the given session ID (state).", response = String.class, tags = {
            "Debug" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Debug result found", response = String.class),
            @ApiResponse(code = 404, message = "Debug result not found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response getDebugResult(@PathParam("session-id") String sessionId) {

        return delegate.getDebugResult(sessionId);
    }
}
