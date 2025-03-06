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

package org.wso2.carbon.identity.api.server.registration.management.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.wso2.carbon.identity.api.server.registration.management.v1.factories.RegistrationFlowApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/registration-flow")
@Api(description = "The registration-flow API")

public class RegistrationFlowApi  {

    private RegistrationFlowApiService delegate;

    public RegistrationFlowApi() {

        this.delegate = RegistrationFlowApiServiceFactory.getRegistrationFlowApi();
    }

    @Valid
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the complete registration flow", notes = "", response = RegistrationFlowResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Registration Flow", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully retrieved the registration flow", response = RegistrationFlowResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class)
    })
    public Response getRegistrationFlow() {

        return delegate.getRegistrationFlow();
    }

    @Valid
    @PUT
    @Path("/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the complete registration flow", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Registration Flow" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Registration flow successfully updated", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid request body", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 500, message = "Encountered a server error", response = Error.class)
    })
    public Response updateRegistrationFlow(@ApiParam(value = "" ,required=true) @Valid RegistrationFlowRequest registrationFlowRequest) {

        return delegate.updateRegistrationFlow(registrationFlowRequest );
    }

}
