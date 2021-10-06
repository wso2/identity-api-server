/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.secret.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretAddRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretPatchRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretResponse;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretUpdateRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretsListObject;
import org.wso2.carbon.identity.api.server.secret.management.v1.SecretMgtApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/secret-mgt")
@Api(description = "The secret-mgt API")

public class SecretMgtApi  {

    @Autowired
    private SecretMgtApiService delegate;

    @Valid
    @POST
    @Path("/types/{secret-type}/secrets")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a secret", notes = "This API provides the capability to create a secret ", response = SecretResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful Response", response = SecretResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response createSecret(@ApiParam(value = "name of the secret type",required=true) @PathParam("secret-type") String secretType, @ApiParam(value = "" ) @Valid SecretAddRequest secretAddRequest) {

        return delegate.createSecret(secretType,  secretAddRequest );
    }

    @Valid
    @DELETE
    @Path("/types/{secret-type}/secrets/{secret-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a secret by id", notes = "This API provides the capability to delete a secret by id. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No Content", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Secret Type Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteSecret(@ApiParam(value = "Name of the secret type",required=true) @PathParam("secret-type") String secretType, @ApiParam(value = "Id of the secret",required=true) @PathParam("secret-id") String secretId) {

        return delegate.deleteSecret(secretType,  secretId );
    }

    @Valid
    @GET
    @Path("/types/{secret-type}/secrets/{secret-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve a secret by id", notes = "This API provides the capability to retrieve a secret ", response = SecretResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = SecretResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getSecret(@ApiParam(value = "Name of the secret type",required=true) @PathParam("secret-type") String secretType, @ApiParam(value = "Id of the secret",required=true) @PathParam("secret-id") String secretId) {

        return delegate.getSecret(secretType,  secretId );
    }

    @Valid
    @GET
    @Path("/types/{secret-type}/secrets")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a list of configured secrets", notes = "This API provides the capability to retrieve the list of configured secrets. ", response = SecretsListObject.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = SecretsListObject.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getSecretsList(@ApiParam(value = "name of the secret type",required=true) @PathParam("secret-type") String secretType,     @Valid@ApiParam(value = "Maximum number of records to return. ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination. ")  @QueryParam("offset") Integer offset) {

        return delegate.getSecretsList(secretType,  limit,  offset );
    }

    @Valid
    @PATCH
    @Path("/types/{secret-type}/secrets/{secret-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch a secret by id", notes = "This API provides the capability to update a secret using patch request by using its id. ", response = SecretResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = SecretResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchSecret(@ApiParam(value = "Name of the secret type",required=true) @PathParam("secret-type") String secretType, @ApiParam(value = "Id of the secret",required=true) @PathParam("secret-id") String secretId, @ApiParam(value = "" ,required=true) @Valid SecretPatchRequest secretPatchRequest) {

        return delegate.patchSecret(secretType,  secretId,  secretPatchRequest );
    }

    @Valid
    @PUT
    @Path("/types/{secret-type}/secrets/{secret-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update a secret by id", notes = "This API provides the capability to update a secret name. ", response = SecretResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = SecretResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateSecret(@ApiParam(value = "Name of the secret type",required=true) @PathParam("secret-type") String secretType, @ApiParam(value = "Id of the secret",required=true) @PathParam("secret-id") String secretId, @ApiParam(value = "" ,required=true) @Valid SecretUpdateRequest secretUpdateRequest) {

        return delegate.updateSecret(secretType,  secretId,  secretUpdateRequest );
    }

}
