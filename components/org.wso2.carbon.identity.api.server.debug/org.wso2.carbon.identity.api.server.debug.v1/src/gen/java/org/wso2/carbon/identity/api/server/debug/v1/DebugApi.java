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

package org.wso2.carbon.identity.api.server.debug.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugResult;
import org.wso2.carbon.identity.api.server.debug.v1.model.Error;
import org.wso2.carbon.identity.api.server.debug.v1.DebugApiService;
import org.wso2.carbon.identity.api.server.debug.v1.factories.DebugApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/debug")
@Api(description = "The debug API")

public class DebugApi  {

    private final DebugApiService delegate;

    public DebugApi() {

        this.delegate = DebugApiServiceFactory.getDebugApi();
    }

    @Valid
    @GET
    @Path("/{debugId}/result")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get Debug Result", notes = "Retrieves the debug results for a specific debug ID. Requires OAuth2 scope `internal_debug_mgt_view`.", response = DebugResult.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Debug", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Debug result retrieved successfully.", response = DebugResult.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getDebugResult(@ApiParam(value = "The debug session identifier.",required=true) @PathParam("debugId") String debugId) {

        return delegate.getDebugResult(debugId );
    }

    @Valid
    @POST
    @Path("/{resourceType}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Start Debug Session", notes = "Initiates a debug session for supported resource types with configurable properties. Requires OAuth2 scope `internal_debug_mgt_update`.", response = DebugConnectionResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Debug" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Debug session executed successfully.", response = DebugConnectionResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response startDebugSession( @Size(min=1,max=50)@ApiParam(value = "Type of resource to debug. Allowed values: idp, fraud_detection.",required=true) @PathParam("resourceType") String resourceType, @ApiParam(value = "Debug request with connection identifier and optional properties." ,required=true) @Valid DebugConnectionRequest debugConnectionRequest) {

        return delegate.startDebugSession(resourceType,  debugConnectionRequest );
    }

}
