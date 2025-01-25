/*
 * Copyright (c) 2021-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.secret.management.v1;

import org.wso2.carbon.identity.api.server.secret.management.v1.factories.SecretTypeApiServiceFactory;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretResponse;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretTypeAddRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretTypeResponse;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretTypeUpdateRequest;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;

@Path("/secret-type")
@Api(description = "The secret-type API")

public class SecretTypeApi {

    private final SecretTypeApiService delegate;

    public SecretTypeApi() {

        this.delegate = SecretTypeApiServiceFactory.getSecretTypeApi();
    }

    @Valid
    @POST

    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Create secret type ", notes = "This API is used to create a new secret type. ", response = SecretResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Secret Type",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful Response", response = SecretResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 409, message = "Conflict", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response createSecretType(@ApiParam(value = "") @Valid SecretTypeAddRequest secretTypeAddRequest) {

        return delegate.createSecretType(secretTypeAddRequest);
    }

    @Valid
    @DELETE
    @Path("/{name}")

    @Produces({"application/json"})
    @ApiOperation(value = "Delete an secret type by name", notes = "This API provides the capability to delete a secret by name. ", response = Void.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Secret Type",})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content", response = Void.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteSecretType(@ApiParam(value = "name of the secret", required = true) @PathParam("name") String name) {

        return delegate.deleteSecretType(name);
    }

    @Valid
    @GET
    @Path("/{name}")

    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve secret type by name", notes = "This API provides the capability to retrieve a secret type. ", response = SecretTypeResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Secret Type",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = SecretTypeResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getSecretType(@ApiParam(value = "name of the secret Type", required = true) @PathParam("name") String name) {

        return delegate.getSecretType(name);
    }

    @Valid
    @PUT
    @Path("/{name}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Update a secret type", notes = "This API provides the capability to update a secret type by name. ", response = SecretTypeResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Secret Type"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = SecretTypeResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateSecretType(@ApiParam(value = "name of the secret type", required = true) @PathParam("name") String name, @ApiParam(value = "", required = true) @Valid SecretTypeUpdateRequest secretTypeUpdateRequest) {

        return delegate.updateSecretType(name, secretTypeUpdateRequest);
    }

}
