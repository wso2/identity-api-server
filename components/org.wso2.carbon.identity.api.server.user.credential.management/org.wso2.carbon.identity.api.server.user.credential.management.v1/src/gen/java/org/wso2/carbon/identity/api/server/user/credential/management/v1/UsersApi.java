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

package org.wso2.carbon.identity.api.server.user.credential.management.v1;

import org.wso2.carbon.identity.api.server.user.credential.management.v1.factories.UsersApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

@Path("/users")
@Api(description = "The users API")

public class UsersApi  {

    private final UsersApiService delegate;

    public UsersApi() {

        this.delegate = UsersApiServiceFactory.getUsersApi();
    }

    @Valid
    @DELETE
    @Path("/{user-id}/credentials/{type}/{credential-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a user-enrolled credential.", notes = "Deletes a specific enrolled credential for a user. Requires administrative privileges with appropriate scope and organizational permissions.", response = Void.class, tags={ "Delete User UserCredential", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "UserCredential Deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response deleteCredentialById(@ApiParam(value = "The unique identifier of the user.",required=true) @PathParam("user-id") String userId, @ApiParam(value = "The type of the credential.",required=true, allowableValues="passkey, push-auth") @PathParam("type") String type, @ApiParam(value = "The unique identifier of the device to be deleted.",required=true) @PathParam("credential-id") String credentialId) {

        return delegate.deleteCredentialById(userId,  type,  credentialId );
    }

    @Valid
    @GET
    @Path("/{user-id}/credentials")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get all credentials for a user", notes = "Retrieves a list of all user-enrolled credentials. Requires administrative privileges with appropriate scope and organizational permissions. This API currently supports passkey and push authentication credentials.", response = UserCredential.class, responseContainer = "List", tags={ "Get User Credentials" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = UserCredential.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Error.class),
        @ApiResponse(code = 404, message = "User Not Found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getCredentialsByUserId(@ApiParam(value = "The unique identifier of the user.",required=true) @PathParam("user-id") String userId) {

        return delegate.getCredentialsByUserId(userId );
    }

}
