/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.tenant.management.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.tenant.management.v1.factories.TenantsApiServiceFactory;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.OwnerInfoResponse;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.OwnerPutModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.OwnerResponse;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantPutModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantResponseModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantsListResponse;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/tenants")
@Api(description = "The tenants API")

public class TenantsApi  {

    private final TenantsApiService delegate;

    public TenantsApi() {

        this.delegate = TenantsApiServiceFactory.getTenantsApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add tenant.", notes = "This API provides the capability to create new tenants.  <b>Permission required:</b> * /permission/protected/manage/monitor/tenants/list  <b>scope required:</b> * internal_list_tenants ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Tenants", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Item Created", response = Void.class),
        @ApiResponse(code = 206, message = "Partial Content", response = Error.class),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Element Already Exists", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response addTenant(@ApiParam(value = "This represents the tenant to be created." ,required=true) @Valid TenantModel tenantModel) {

        return delegate.addTenant(tenantModel );
    }

    @Valid
    @DELETE
    @Path("/{tenant-id}/metadata")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete tenant's metadata by ID ", notes = "This API provides the capability to delete the tenant meta data(tenant specific data like tenant domain, tenant owner details). <br>   <b>Permission required:</b> <br>       * /permission/protected/manage/modify/tenants <br>   <b>Scope required:</b> <br>       * internal_modify_tenants ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Tenants", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteTenantMetadata(@ApiParam(value = "tenant id",required=true) @PathParam("tenant-id") String tenantId) {

        return delegate.deleteTenantMetadata(tenantId );
    }

    @Valid
    @GET
    @Path("/{tenant-id}/owners/{owner-id}")

    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve tenant owner.", notes = "Retrieve the tenant owner identified by the provided tenant id and owner id.  <b>Permission required:</b> * /permission/protected/manage/monitor/tenants/list  <b>scope required:</b> * internal_list_tenants ", response = OwnerInfoResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Tenants", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = OwnerInfoResponse.class),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getOwner(@ApiParam(value = "tenant id",required=true) @PathParam("tenant-id") String tenantId, @ApiParam(value = "owner id",required=true) @PathParam("owner-id") String ownerId,     @Valid@ApiParam(value = "Define set of additional user claims (as comma separated) to be returned.")  @QueryParam("additionalClaims") String additionalClaims) {

        return delegate.getOwner(tenantId,  ownerId,  additionalClaims );
    }

    @Valid
    @GET
    @Path("/{tenant-id}/owners")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve tenant's owners.", notes = "Retrieve owners of the tenant which are identified by the provided tenant id.  <b>Permission required:</b> * /permission/protected/manage/monitor/tenants/list  <b>scope required:</b> * internal_list_tenants ", response = OwnerResponse.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Tenants", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OwnerResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getOwners(@ApiParam(value = "tenant id",required=true) @PathParam("tenant-id") String tenantId) {

        return delegate.getOwners(tenantId );
    }

    @Valid
    @GET
    @Path("/{tenant-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve tenant by tenant ID.", notes = "Retrieve tenant using the tenant ID.  <b>Permission required:</b> * /permission/protected/manage/monitor/tenants/list  <b>scope required:</b> * internal_list_tenants ", response = TenantResponseModel.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Tenants", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = TenantResponseModel.class),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getTenant(@ApiParam(value = "tenant id",required=true) @PathParam("tenant-id") String tenantId) {

        return delegate.getTenant(tenantId );
    }

    @Valid
    @GET
    @Path("/domain/{tenant-domain}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get tenant by domain.", notes = "Get the tenant using domain.  <b>Permission required:</b> * /permission/protected/manage/monitor/tenants/list  <b>scope required:</b> * internal_list_tenants ", response = TenantResponseModel.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Tenants", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = TenantResponseModel.class),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getTenantByDomain(@ApiParam(value = "tenant domain",required=true) @PathParam("tenant-domain") String tenantDomain) {

        return delegate.getTenantByDomain(tenantDomain );
    }

    @Valid
    @HEAD
    @Path("/domain/{tenant-domain}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Check domain Existence.", notes = "Check the tenant existence using domain.  <b>Permission required:</b> * /permission/protected/manage/monitor/tenants/list  <b>scope required:</b> * internal_list_tenants ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Tenants", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Requested Resource Exists", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response isDomainExist(@ApiParam(value = "tenant domain",required=true) @PathParam("tenant-domain") String tenantDomain) {

        return delegate.isDomainExist(tenantDomain );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve all tenants.", notes = "Retrieve all tenants in the system.  <b>Permission required:</b> * /permission/protected/manage/monitor/tenants/list  <b>scope required:</b> * internal_list_tenants ", response = TenantsListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Tenants", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = TenantsListResponse.class),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response retrieveTenants(    @Valid @Min(0)@ApiParam(value = "Maximum number of records to return.")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination.")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Define the order in which the retrieved tenants should be sorted.", allowableValues="asc, desc")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Attribute by which the retrieved records should be sorted. Currently sorting through <b>domainName</b> only supported.")  @QueryParam("sortBy") String sortBy,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations. Currently, filtering is supported only by the <b>domainName</b>. E.g. /tenants?filter=domainName+sw+wso2. ")  @QueryParam("filter") String filter) {

        return delegate.retrieveTenants(limit,  offset,  sortOrder,  sortBy,  filter );
    }

    @Valid
    @PUT
    @Path("/{tenant-id}/owners/{owner-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update tenant owner.", notes = "This API provides the capability to update the tenant owner.  <b>Permission required:</b> * /permission/protected/manage/modify/tenants  <b>scope required:</b> * internal_modify_tenants ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Tenants", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 206, message = "Partial Content", response = Error.class),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response updateOwner(@ApiParam(value = "tenant id",required=true) @PathParam("tenant-id") String tenantId, @ApiParam(value = "owner id",required=true) @PathParam("owner-id") String ownerId, @ApiParam(value = "This represents the tenant owner to be updated." ,required=true) @Valid OwnerPutModel ownerPutModel) {

        return delegate.updateOwner(tenantId,  ownerId,  ownerPutModel );
    }

    @Valid
    @PUT
    @Path("/{tenant-id}/lifecycle-status")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Activate or deactivate tenant.", notes = "This API activates/deactivates the specified tenant.  <b>Permission required:</b> * /permission/protected/manage/modify/tenants  <b>scope required:</b> * internal_modify_tenants ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Tenants" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully updated", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Element Already Exists", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response updateTenantStatus(@ApiParam(value = "tenant id",required=true) @PathParam("tenant-id") String tenantId, @ApiParam(value = "This represents the tenant to be updated." ,required=true) @Valid TenantPutModel tenantPutModel) {

        return delegate.updateTenantStatus(tenantId,  tenantPutModel );
    }

}
