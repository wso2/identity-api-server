/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

import org.wso2.carbon.identity.api.server.idp.debug.v1.factories.DebugApiServiceFactory;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.Error;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugConnectionResponse;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/debug")
@Api(description = "The debug API")
public class DebugApi {

    private final DebugApiService delegate;

    public DebugApi() {
        this.delegate = DebugApiServiceFactory.getDebugApi();
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
