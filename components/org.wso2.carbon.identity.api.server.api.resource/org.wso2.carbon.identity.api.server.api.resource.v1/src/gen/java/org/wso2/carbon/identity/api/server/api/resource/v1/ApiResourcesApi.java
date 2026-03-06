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

package org.wso2.carbon.identity.api.server.api.resource.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceListResponse;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourcePatchModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceResponse;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesGetModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.Error;
import java.util.List;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopeCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopeGetModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopePatchModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.ApiResourcesApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;
import org.wso2.carbon.identity.api.server.api.resource.v1.factories.ApiResourcesApiServiceFactory;

import javax.validation.constraints.*;

@Path("/api-resources")
@Api(description = "The api-resources API")

public class ApiResourcesApi  {

    private final ApiResourcesApiService delegate;

    public ApiResourcesApi() {

        this.delegate = ApiResourcesApiServiceFactory.getApiResourcesApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json", "application/xml",  })
    @ApiOperation(value = "Add a new API resource", notes = "Add a new API resource <b>Permission required:</b> <br>   * /permission/admin/manage/identity/apiresourcemgt/create <br> <b>Scope required:</b> <br>   * internal_api_resource_create ", response = APIResourceResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resources", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = APIResourceResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response addAPIResource(@ApiParam(value = "This represents the API resource to be created." ,required=true) @Valid APIResourceCreationModel apIResourceCreationModel) {

        return delegate.addAPIResource(apIResourceCreationModel );
    }

    @Valid
    @PUT
    @Path("/{apiResourceId}/authorization-details-types")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Adds new authorization details types to the API resource", notes = "This API is used to add a new authorization details type to API resource.  <b>Permission required:</b>     * /permission/admin/manage/identity/apiresourcemgt/update    <b>Scope required:</b>     * internal_api_resource_update ", response = AuthorizationDetailsTypesGetModel.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resource Authorization Details Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully added a list of authorization details types", response = AuthorizationDetailsTypesGetModel.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized request", response = Error.class),
        @ApiResponse(code = 404, message = "Requested resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Encountered a server error", response = Error.class)
    })
    public Response addAuthorizationDetailsTypes(@ApiParam(value = "ID of the API Resource.",required=true) @PathParam("apiResourceId") String apiResourceId, @ApiParam(value = "" ) @Valid List<AuthorizationDetailsTypesCreationModel> authorizationDetailsTypesCreationModel) {

        return delegate.addAuthorizationDetailsTypes(apiResourceId,  authorizationDetailsTypesCreationModel );
    }

    @Valid
    @DELETE
    @Path("/{apiResourceId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete API resource specified by the id", notes = "Delete API resource specified by the id <b>Permission required:</b> <br>   * /permission/admin/manage/identity/apiresourcemgt/delete <br> <b>Scope required:</b> <br>   * internal_api_resource_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resources", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No Content", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response apiResourcesApiResourceIdDelete(@ApiParam(value = "ID of the API Resource.",required=true) @PathParam("apiResourceId") String apiResourceId) {

        return delegate.apiResourcesApiResourceIdDelete(apiResourceId );
    }

    @Valid
    @GET
    @Path("/{apiResourceId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get API resource specified by the id", notes = "Get API resource specified by the id <b>Permission required:</b> <br>   * /permission/admin/manage/identity/apiresourcemgt/view <br> <b>Scope required:</b> <br>   * internal_api_resource_view ", response = APIResourceResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resources", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = APIResourceResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response apiResourcesApiResourceIdGet(@ApiParam(value = "ID of the API Resource.",required=true) @PathParam("apiResourceId") String apiResourceId) {

        return delegate.apiResourcesApiResourceIdGet(apiResourceId );
    }

    @Valid
    @PATCH
    @Path("/{apiResourceId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "application/xml",  })
    @ApiOperation(value = "Patch API resource specified by the id", notes = "Patch API resource specified by the id. Patch operation only supports \"name\", \"description\" updating and \"addedScopes\" fields at the moment. <b>Permission required:</b> <br>   * /permission/admin/manage/identity/apiresourcemgt/update <br> <b>Scope required:</b> <br>   * internal_api_resource_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resources", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Not Content", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response apiResourcesApiResourceIdPatch(@ApiParam(value = "ID of the API Resource.",required=true) @PathParam("apiResourceId") String apiResourceId, @ApiParam(value = "This represents the API resource to be patched." ,required=true) @Valid APIResourcePatchModel apIResourcePatchModel) {

        return delegate.apiResourcesApiResourceIdPatch(apiResourceId,  apIResourcePatchModel );
    }

    @Valid
    @GET
    @Path("/{apiResourceId}/scopes")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get API resource scopes", notes = "Get API resource scopes specified by the id <b>Permission required:</b> <br>   * /permission/admin/manage/identity/apiresourcemgt/view <br> <b>Scope required:</b> <br>   * internal_api_resource_view ", response = ScopeGetModel.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resource Scopes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ScopeGetModel.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response apiResourcesApiResourceIdScopesGet(@ApiParam(value = "ID of the API Resource.",required=true) @PathParam("apiResourceId") String apiResourceId) {

        return delegate.apiResourcesApiResourceIdScopesGet(apiResourceId );
    }

    @Valid
    @DELETE
    @Path("/{apiResourceId}/scopes/id/{scopeId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete API scope specified by the id", notes = "Delete API scope specified by the id <b>Permission required:</b> <br>   * /permission/admin/manage/identity/apiresourcemgt/delete <br> <b>Scope required:</b> <br>   * internal_api_resource_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resource Scopes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response apiResourcesApiResourceIdScopesIdScopeIdDelete(@ApiParam(value = "ID of the API Resource.",required=true) @PathParam("apiResourceId") String apiResourceId, @ApiParam(value = "Id of the Scope.",required=true) @PathParam("scopeId") String scopeId) {

        return delegate.apiResourcesApiResourceIdScopesIdScopeIdDelete(apiResourceId,  scopeId );
    }

    @Valid
    @PATCH
    @Path("/{apiResourceId}/scopes/id/{scopeId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch scope specified by the id", notes = "Patch scope specified by the id. Patch operation only supports \"displayName\" and \"description\" fields at the moment. <b>Permission required:</b> <br>   * /permission/admin/manage/identity/apiresourcemgt/update <br> <b>Scope required:</b> <br>   * internal_api_resource_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resource Scopes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Not Content", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response apiResourcesApiResourceIdScopesIdScopeIdPatch(@ApiParam(value = "ID of the API Resource.",required=true) @PathParam("apiResourceId") String apiResourceId, @ApiParam(value = "Id of the Scope.",required=true) @PathParam("scopeId") String scopeId, @ApiParam(value = "This represents the scopes to be patched." ,required=true) @Valid ScopePatchModel scopePatchModel) {

        return delegate.apiResourcesApiResourceIdScopesIdScopeIdPatch(apiResourceId,  scopeId,  scopePatchModel );
    }

    @Valid
    @PUT
    @Path("/{apiResourceId}/scopes")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "application/xml",  })
    @ApiOperation(value = "Add scopes to API resource", notes = "Put scopes API resource specified by the id <b>Permission required:</b> <br>   * /permission/admin/manage/identity/apiresourcemgt/update <br> <b>Scope required:</b> <br>   * internal_api_resource_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resource Scopes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No Content", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response apiResourcesApiResourceIdScopesPut(@ApiParam(value = "ID of the API Resource.",required=true) @PathParam("apiResourceId") String apiResourceId, @ApiParam(value = "This represents the API resource to be patched." ,required=true) @Valid List<ScopeCreationModel> scopeCreationModel) {

        return delegate.apiResourcesApiResourceIdScopesPut(apiResourceId,  scopeCreationModel );
    }

    @Valid
    @DELETE
    @Path("/{apiResourceId}/scopes/{scopeName}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete API scope specified by the name", notes = "Delete API scope specified by the name <b>Permission required:</b> <br>   * /permission/admin/manage/identity/apiresourcemgt/delete <br> <b>Scope required:</b> <br>   * internal_api_resource_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resource Scopes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response apiResourcesApiResourceIdScopesScopeNameDelete(@ApiParam(value = "ID of the API Resource.",required=true) @PathParam("apiResourceId") String apiResourceId, @ApiParam(value = "Name of the Scope.",required=true) @PathParam("scopeName") String scopeName) {

        return delegate.apiResourcesApiResourceIdScopesScopeNameDelete(apiResourceId,  scopeName );
    }

    @Valid
    @PATCH
    @Path("/{apiResourceId}/scopes/{scopeName}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch scope specified by the name", notes = "Patch scope specified by the name. Patch operation only supports \"displayName\" and \"description\" fields at the moment. <b>Permission required:</b> <br>   * /permission/admin/manage/identity/apiresourcemgt/update <br> <b>Scope required:</b> <br>   * internal_api_resource_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags = { "API Resource Scopes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Not Content", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response apiResourcesApiResourceIdScopesScopeNamePatch(@ApiParam(value = "ID of the API Resource.", required = true) @PathParam("apiResourceId") String apiResourceId, @ApiParam(value = "Name of the Scope.", required = true) @PathParam("scopeName") String scopeName, @ApiParam(value = "This represents the scopes to be patched." , required = true) @Valid
    ScopePatchModel scopePatchModel) {

        return delegate.apiResourcesApiResourceIdScopesScopeNamePatch(apiResourceId,  scopeName, scopePatchModel);
    }

    @Valid
    @DELETE
    @Path("/{apiResourceId}/authorization-details-types/{authorizationDetailsTypeId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Deletes a registered authorization details type by type ID", notes = "This API is used to delete a registered authorization details type by a given type ID.  <b>Permission required:</b>     * /permission/admin/manage/identity/apiresourcemgt/delete    <b>Scope required:</b>     * internal_api_resource_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resource Authorization Details Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully deleted an authorization details type by type ID", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized request", response = Error.class),
        @ApiResponse(code = 500, message = "Encountered a server error", response = Error.class)
    })
    public Response deleteAuthorizationDetailsType(@ApiParam(value = "ID of the API Resource.",required=true) @PathParam("apiResourceId") String apiResourceId, @ApiParam(value = "The ID of the authorization details type that is to be retrieved",required=true) @PathParam("authorizationDetailsTypeId") String authorizationDetailsTypeId) {

        return delegate.deleteAuthorizationDetailsType(apiResourceId,  authorizationDetailsTypeId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List all API resources in the server", notes = "List all API resources in the server <b>Permission required:</b> <br>   * /permission/admin/manage/identity/apiresourcemgt/view <br> <b>Scope required:</b> <br>   * internal_api_resource_view ", response = APIResourceListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resources" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = APIResourceListResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getAPIResources(    @Valid@ApiParam(value = "Base64 encoded cursor value for backward pagination. ")  @QueryParam("before") String before,     @Valid@ApiParam(value = "Base64 encoded cursor value for forward pagination. ")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations. ")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Maximum number of records to return. ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Specifies the required attributes in the response. Only 'properties' attribute is currently supported.")  @QueryParam("attributes") String attributes) {

        return delegate.getAPIResources(before,  after,  filter,  limit,  attributes );
    }

    @Valid
    @GET
    @Path("/{apiResourceId}/authorization-details-types/{authorizationDetailsTypeId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Returns a registered authorization details type by type ID", notes = "This API is used to retrieve details of a registered authorization details type by a given type ID.  <b>Permission required:</b>     * /permission/admin/manage/identity/apiresourcemgt/view    <b>Scope required:</b>     * internal_api_resource_view ", response = AuthorizationDetailsTypesGetModel.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resource Authorization Details Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully retrieved an authorization details type by type ID", response = AuthorizationDetailsTypesGetModel.class),
        @ApiResponse(code = 401, message = "Unauthorized request", response = Error.class),
        @ApiResponse(code = 404, message = "Requested resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Encountered a server error", response = Error.class)
    })
    public Response getAuthorizationDetailsType(@ApiParam(value = "ID of the API Resource.",required=true) @PathParam("apiResourceId") String apiResourceId, @ApiParam(value = "The ID of the authorization details type that is to be retrieved",required=true) @PathParam("authorizationDetailsTypeId") String authorizationDetailsTypeId) {

        return delegate.getAuthorizationDetailsType(apiResourceId,  authorizationDetailsTypeId );
    }

    @Valid
    @GET
    @Path("/{apiResourceId}/authorization-details-types")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Lists all registered authorization details types", notes = "This API is used to get all registered authorization details types.  <b>Permission required:</b>     * /permission/admin/manage/identity/apiresourcemgt/view    <b>Scope required:</b>     * internal_api_resource_view ", response = AuthorizationDetailsTypesGetModel.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resource Authorization Details Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully retrieved a list of authorization details types", response = AuthorizationDetailsTypesGetModel.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized request", response = Error.class),
        @ApiResponse(code = 404, message = "Requested resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Encountered a server error", response = Error.class)
    })
    public Response getAuthorizationDetailsTypes(@ApiParam(value = "ID of the API Resource.",required=true) @PathParam("apiResourceId") String apiResourceId) {

        return delegate.getAuthorizationDetailsTypes(apiResourceId );
    }

    @Valid
    @PATCH
    @Path("/{apiResourceId}/authorization-details-types/{authorizationDetailsTypeId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Updates a registered authorization details type by type ID", notes = "This API is used to update a registered authorization details type by a given type ID.  <b>Permission required:</b>     * /permission/admin/manage/identity/apiresourcemgt/update    <b>Scope required:</b>     * internal_api_resource_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resource Authorization Details Types" })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No Content", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized request", response = Error.class),
        @ApiResponse(code = 404, message = "Requested resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Encountered a server error", response = Error.class)
    })
    public Response updateAuthorizationDetailsType(@ApiParam(value = "ID of the API Resource.",required=true) @PathParam("apiResourceId") String apiResourceId, @ApiParam(value = "The ID of the authorization details type that is to be retrieved",required=true) @PathParam("authorizationDetailsTypeId") String authorizationDetailsTypeId, @ApiParam(value = "" ) @Valid AuthorizationDetailsTypesCreationModel authorizationDetailsTypesCreationModel) {

        return delegate.updateAuthorizationDetailsType(apiResourceId,  authorizationDetailsTypeId,  authorizationDetailsTypesCreationModel );
    }

}
