/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.oidc.scope.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;

import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.ErrorResponse;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.Scope;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.ScopeUpdateRequest;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.factories.OidcApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/oidc")
@Api(description = "The oidc API")

public class OidcApi  {

    private final OidcApiService delegate;

    public OidcApi() {

        this.delegate = OidcApiServiceFactory.getOidcApi();
    }

    @Valid
    @POST
    @Path("/scopes")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add a new OIDC Scope", notes = "", response = Void.class, tags={ "OIDC Scope Endpoint", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 409, message = "Scope is already Exists.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response addScope(@ApiParam(value = "" ) @Valid Scope scope) {

        return delegate.addScope(scope );
    }

    @Valid
    @DELETE
    @Path("/scopes/{id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete an OIDC Scope", notes = "", response = Void.class, tags={ "OIDC Scope Endpoint", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No Content.", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response deleteScope(@ApiParam(value = "scope name as the id",required=true) @PathParam("id") String id) {

        return delegate.deleteScope(id );
    }

    @Valid
    @GET
    @Path("/scopes/{id}/export")
    
    @Produces({ "application/json", "application/xml", "application/yaml" })
    @ApiOperation(value = "Export an OIDC Scope in XML, YAML, or JSON format", notes = "", response = String.class, tags={ "OIDC Scope Endpoint", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = String.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response exportScopeToFile(@ApiParam(value = "scope name as the id",required=true) @PathParam("id") String id,     @Valid @ApiParam(value = "Content type of the file. " , allowableValues="application/json, application/xml, application/yaml, application/x-yaml, text/yaml, text/xml, text/json", defaultValue="application/yaml")@HeaderParam("Accept") String accept) {

        return delegate.exportScopeToFile(id,  accept );
    }

    @Valid
    @GET
    @Path("/scopes/{id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a scope", notes = "This REST API can be used to get the scope.", response = Scope.class, tags={ "OIDC Scope Endpoint", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = Scope.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response getScope(@ApiParam(value = "scope name as the id",required=true) @PathParam("id") String id) {

        return delegate.getScope(id );
    }

    @Valid
    @GET
    @Path("/scopes")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the list of scopes", notes = "This REST API can be used to get the avaiable scopes details.", response = Scope.class, responseContainer = "List", tags={ "OIDC Scope Endpoint", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = Scope.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response getScopes() {

        return delegate.getScopes();
    }

    @Valid
    @POST
    @Path("/scopes/import")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Import an OIDC Scope from XML, YAML, or JSON file", notes = "", response = Void.class, tags={ "OIDC Scope Endpoint", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successfully imported", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 409, message = "Scope already Exists.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response importScopeFromFile(@Multipart(value = "file") InputStream fileInputStream,@Multipart(value = "file" ) Attachment fileDetail) {

        return delegate.importScopeFromFile(fileInputStream, fileDetail );
    }

    @Valid
    @PUT
    @Path("/scopes/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update an OIDC Scope", notes = "", response = Void.class, tags={ "OIDC Scope Endpoint" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response updateScope(@ApiParam(value = "scope name as the id",required=true) @PathParam("id") String id, @ApiParam(value = "" ) @Valid ScopeUpdateRequest scopeUpdateRequest) {

        return delegate.updateScope(id,  scopeUpdateRequest );
    }

    @Valid
    @PUT
    @Path("/scopes/{id}/import")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update an OIDC Scope from XML, YAML, or JSON file", notes = "", response = Void.class, tags={ "OIDC Scope Endpoint" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully Updated.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response updateScopeFromFile(@ApiParam(value = "scope name as the ID",required=true) @PathParam("id") String id, @Multipart(value = "file") InputStream fileInputStream,@Multipart(value = "file" ) Attachment fileDetail) {

        return delegate.updateScopeFromFile(id,  fileInputStream, fileDetail );
    }

}
