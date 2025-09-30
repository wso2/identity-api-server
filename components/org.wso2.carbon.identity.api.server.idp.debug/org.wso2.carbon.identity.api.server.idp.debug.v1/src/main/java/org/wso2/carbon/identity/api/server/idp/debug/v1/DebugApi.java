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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Debug API for IdP authentication flows.
 */
@Path("/debug")
@Api(description = "IdP Debug API")
public interface DebugApi {
    /**
     * Handles the debug authentication flow request.
     *
     * @return Response containing debug flow results.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Debug IdP Authentication Flow",
                  notes = "Initiates debug authentication flow for the specified Identity Provider")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Debug authentication initiated successfully"),
        @ApiResponse(code = 400, message = "Invalid debug request parameters"),
        @ApiResponse(code = 404, message = "Target Identity Provider not found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    Response debug();
}
