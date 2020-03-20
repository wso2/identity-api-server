/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.configs.v1;

import org.springframework.beans.factory.annotation.Autowired;

import org.wso2.carbon.identity.api.server.configs.v1.model.Authenticator;
import org.wso2.carbon.identity.api.server.configs.v1.model.AuthenticatorListItem;
import org.wso2.carbon.identity.api.server.configs.v1.model.Error;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

@Path("/configs")
@Api(description = "The configs API")

public class ConfigsApi  {

    @Autowired
    private ConfigsApiService delegate;

    @Valid
    @GET
    @Path("/authenticators/{authenticator-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get authenticator by ID", notes = "By passing in the appropriate authenticator ID, you can retrieve authenticator details ", response = Authenticator.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Local Authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Authenticator.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getAuthenticator(@ApiParam(value = "ID of an authenticator",required=true) @PathParam("authenticator-id") String authenticatorId) {

        return delegate.getAuthenticator(authenticatorId );
    }

    @Valid
    @GET
    @Path("/authenticators")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List local authenticators in the server", notes = "List available local authenticators of the server ", response = AuthenticatorListItem.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Local Authenticators" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = AuthenticatorListItem.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response listAuthenticators() {

        return delegate.listAuthenticators();
    }

}
