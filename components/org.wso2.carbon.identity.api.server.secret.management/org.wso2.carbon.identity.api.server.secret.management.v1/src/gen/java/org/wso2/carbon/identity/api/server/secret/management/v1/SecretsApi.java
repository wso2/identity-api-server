/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.secret.management.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.secret.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.Secret;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretAdd;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretUpdateRequest;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;

@Path("/secrets")
@Api(description = "The secrets API")

public class SecretsApi {

    @Autowired
    private SecretsApiService delegate;

    @Valid
    @POST

    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Create a secret", notes = "This API provides the capability to create a secret ", response = Secret.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"secrets",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful Response", response = Secret.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 409, message = "Conflict", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response createSecret(@ApiParam(value = "") @Valid SecretAdd secretAdd) {

        return delegate.createSecret(secretAdd);
    }

    @Valid
    @DELETE
    @Path("/{name}")

    @Produces({"application/json"})
    @ApiOperation(value = "Delete an secret by name", notes = "This API provides the capability to delete a secret by name. ", response = Void.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"secrets",})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content", response = Void.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteSecret(@ApiParam(value = "name of the secret", required = true) @PathParam("name") String name) {

        return delegate.deleteSecret(name);
    }

    @Valid
    @GET
    @Path("/{name}")

    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve secret by name", notes = "This API provides the capability to retrieve a secret ", response = Secret.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"secrets",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = Secret.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getSecret(@ApiParam(value = "name of the secret", required = true) @PathParam("name") String name) {

        return delegate.getSecret(name);
    }

    @Valid
    @GET

    @Produces({"application/json"})
    @ApiOperation(value = "Get a list of configured secrets", notes = "This API provides the capability to retrieve the list of configured secrets. ", response = Secret.class, responseContainer = "List", authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"secrets",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = Secret.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getSecretsList() {

        return delegate.getSecretsList();
    }

    @Valid
    @PUT
    @Path("/{name}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Update a secret", notes = "This API provides the capability to update a secret name. ", response = Secret.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"secrets"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = Secret.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateSecret(@ApiParam(value = "name of the secret", required = true) @PathParam("name") String name, @ApiParam(value = "", required = true) @Valid SecretUpdateRequest secretUpdateRequest) {

        return delegate.updateSecret(name, secretUpdateRequest);
    }

}
