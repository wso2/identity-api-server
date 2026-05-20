/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.consent.management.v2;

import org.wso2.carbon.identity.api.server.consent.management.v2.model.ErrorDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeListResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeVersionCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeVersionDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeVersionListResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.SetLatestVersionRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.factories.PurposesApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/purposes")
@Api(description = "The purposes API")

public class PurposesApi  {

    private final PurposesApiService delegate;

    public PurposesApi() {

        this.delegate = PurposesApiServiceFactory.getPurposesApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a new purpose", notes = "Create a new consent purpose (e.g., \"Privacy Policy\", \"Marketing\"). ", response = PurposeDTO.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Purposes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Purpose created successfully", response = PurposeDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 409, message = "Conflict", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response purposesCreate(@ApiParam(value = "" ,required=true) @Valid PurposeCreateRequest purposeCreateRequest) {

        return delegate.purposesCreate(purposeCreateRequest );
    }

    @Valid
    @DELETE
    @Path("/{purposeId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a purpose", notes = "Delete a purpose by ID. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Purposes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Purpose deleted successfully", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 409, message = "Conflict", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response purposesDelete(@ApiParam(value = "ID of the purpose.",required=true) @PathParam("purposeId") String purposeId) {

        return delegate.purposesDelete(purposeId );
    }

    @Valid
    @GET
    @Path("/{purposeId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a purpose", notes = "Retrieve a specific purpose by ID. ", response = PurposeDTO.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Purposes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Purpose details", response = PurposeDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response purposesGet(@ApiParam(value = "ID of the purpose.",required=true) @PathParam("purposeId") String purposeId) {

        return delegate.purposesGet(purposeId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List all purposes", notes = "Retrieve all purposes with optional filtering. ", response = PurposeListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Purposes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "List of purposes", response = PurposeListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response purposesList(    @Valid@ApiParam(value = "Filter purposes by supported attributes: name, type. Supports 'sw' (starts with), 'co' (contains), 'ew' (ends with), and 'eq' (equals) operations. Combine multiple conditions with 'and', 'or' logical operators. Examples: - filter=name eq Marketing - filter=type co Policy - filter=name sw Data and type eq Policy ")  @QueryParam("filter") String filter,     @Valid @Min(1)@ApiParam(value = "Maximum number of records to return.", defaultValue="10") @DefaultValue("10")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Cursor for forward pagination. Pass the base64-encoded UUID of the last item received from the previous page to retrieve the next page of results. ")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Cursor for backward pagination. Pass the base64-encoded UUID of the first item received from the current page to retrieve the previous page of results. ")  @QueryParam("before") String before) {

        return delegate.purposesList(filter,  limit,  after,  before );
    }

    @Valid
    @PUT
    @Path("/{purposeId}/versions/latest")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Set the latest version of a purpose", notes = "Designate a specific version as the \"latest\" version of a purpose. The latest version is returned in GET /purposes/{purposeId} and used when creating new consents. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Purposes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Latest version updated successfully", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response purposesSetLatestVersion(@ApiParam(value = "ID of the purpose.",required=true) @PathParam("purposeId") String purposeId, @ApiParam(value = "" ,required=true) @Valid SetLatestVersionRequest setLatestVersionRequest) {

        return delegate.purposesSetLatestVersion(purposeId,  setLatestVersionRequest );
    }

    @Valid
    @POST
    @Path("/{purposeId}/versions")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a new purpose version", notes = "Add a new version to an existing purpose. ", response = PurposeVersionDTO.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Purposes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Purpose version created successfully", response = PurposeVersionDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 409, message = "Conflict", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response purposesVersionsCreate(@ApiParam(value = "ID of the purpose.",required=true) @PathParam("purposeId") String purposeId, @ApiParam(value = "" ,required=true) @Valid PurposeVersionCreateRequest purposeVersionCreateRequest) {

        return delegate.purposesVersionsCreate(purposeId,  purposeVersionCreateRequest );
    }

    @Valid
    @DELETE
    @Path("/{purposeId}/versions/{versionId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a purpose version", notes = "Delete a specific version of a purpose. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Purposes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Purpose version deleted successfully", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 409, message = "Conflict", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response purposesVersionsDelete(@ApiParam(value = "ID of the purpose.",required=true) @PathParam("purposeId") String purposeId, @ApiParam(value = "ID of the purpose version.",required=true) @PathParam("versionId") String versionId) {

        return delegate.purposesVersionsDelete(purposeId,  versionId );
    }

    @Valid
    @GET
    @Path("/{purposeId}/versions/{versionId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a purpose version", notes = "Retrieve a specific version of a purpose. ", response = PurposeVersionDTO.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Purposes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Purpose version details", response = PurposeVersionDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response purposesVersionsGet(@ApiParam(value = "ID of the purpose.",required=true) @PathParam("purposeId") String purposeId, @ApiParam(value = "ID of the purpose version.",required=true) @PathParam("versionId") String versionId) {

        return delegate.purposesVersionsGet(purposeId,  versionId );
    }

    @Valid
    @GET
    @Path("/{purposeId}/versions")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List purpose versions", notes = "Retrieve all versions of a specific purpose with pagination support. ", response = PurposeVersionListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Purposes" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "List of purpose versions", response = PurposeVersionListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response purposesVersionsList(@ApiParam(value = "ID of the purpose.",required=true) @PathParam("purposeId") String purposeId,     @Valid @Min(1)@ApiParam(value = "Maximum number of records to return.", defaultValue="10") @DefaultValue("10")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Cursor for forward pagination. Pass the base64-encoded UUID of the last item received from the previous page to retrieve the next page of results. ")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Cursor for backward pagination. Pass the base64-encoded UUID of the first item received from the current page to retrieve the previous page of results. ")  @QueryParam("before") String before) {

        return delegate.purposesVersionsList(purposeId,  limit,  after,  before );
    }

}
