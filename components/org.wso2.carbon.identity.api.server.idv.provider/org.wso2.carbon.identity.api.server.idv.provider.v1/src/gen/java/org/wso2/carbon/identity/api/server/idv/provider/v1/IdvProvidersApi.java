/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.idv.provider.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.idv.provider.v1.factories.IdvProvidersApiServiceFactory;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.Error;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.IdVProviderListResponse;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.IdVProviderRequest;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.IdVProviderResponse;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/idv-providers")
@Api(description = "The idv-providers API")

public class IdvProvidersApi  {

    private final IdvProvidersApiService delegate;

    public IdvProvidersApi() {

        this.delegate = IdvProvidersApiServiceFactory.getIdvProvidersApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json", "application/xml",  })
    @ApiOperation(value = "Add a new identity verification provider. ", notes = "This API provides the capability to add an identity verification provider. <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idvp/add <br> <b>Scope required:</b> <br>     * internal_idvp_add ", response = IdVProviderResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Identity Verification Providers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = IdVProviderResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response addIdVProvider(@ApiParam(value = "This represents the identity provider to be created." ,required=true) @Valid IdVProviderRequest idVProviderRequest) {

        return delegate.addIdVProvider(idVProviderRequest );
    }

    @Valid
    @DELETE
    @Path("/{idv-provider-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete an identity verification provider by using the identity provider's ID. ", notes = "This API provides the capability to delete an identity verification provider by giving its ID. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idvp/delete <br> <b>Scope required:</b> <br>     * internal_idvp_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Identity Verification Providers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteIdVProvider(@ApiParam(value = "ID of the identity verification provider",required=true) @PathParam("idv-provider-id") String idvProviderId) {

        return delegate.deleteIdVProvider(idvProviderId );
    }

    @Valid
    @GET
    @Path("/{idv-provider-id}")
    
    @Produces({ "application/json", "application/xml",  })
    @ApiOperation(value = "Retrieve identity verification provider by identity verification provider's ID ", notes = "This API provides the capability to retrieve the identity verification provider details by using its ID.  <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idvp/view <br> <b>Scope required:</b> <br>     * internal_idvp_view ", response = IdVProviderResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Identity Verification Providers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = IdVProviderResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getIdVProvider(@ApiParam(value = "ID of the identity verification provider.",required=true) @PathParam("idv-provider-id") String idvProviderId) {

        return delegate.getIdVProvider(idvProviderId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List identity verification providers. ", notes = "This API provides the capability to retrieve the list of identity verification providers.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idvp/view <br> <b>Scope required:</b> <br>     * internal_idvp_view ", response = IdVProviderListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Identity Verification Providers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = IdVProviderListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response getIdVProviders(    @Valid@ApiParam(value = "Maximum number of records to return. ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination. ")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations and also complex queries with 'and' operations. E.g. /idv-providers?filter=name+sw+onfido+and+isEnabled+eq+true ")  @QueryParam("filter") String filter) {

        return delegate.getIdVProviders(limit,  offset,  filter );
    }

    @Valid
    @PUT
    @Path("/{idv-provider-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update an identity verification provider. ", notes = "This API provides the capability to update an identity verification provider <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idvp/update <br> <b>Scope required:</b> <br>     * internal_idvp_update ", response = IdVProviderResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Identity Verification Providers" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = IdVProviderResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateIdVProviders(@ApiParam(value = "ID of the identity verification provider.",required=true) @PathParam("idv-provider-id") String idvProviderId, @ApiParam(value = "" ,required=true) @Valid IdVProviderRequest idVProviderRequest) {

        return delegate.updateIdVProviders(idvProviderId,  idVProviderRequest );
    }

}
