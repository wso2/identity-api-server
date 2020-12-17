/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.userstore.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.userstore.v1.model.AvailableUserStoreClassesRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.ConnectionEstablishedResponse;
import org.wso2.carbon.identity.api.server.userstore.v1.model.Error;
import java.util.List;
import org.wso2.carbon.identity.api.server.userstore.v1.model.MetaUserStoreType;
import org.wso2.carbon.identity.api.server.userstore.v1.model.PatchDocument;
import org.wso2.carbon.identity.api.server.userstore.v1.model.RDBMSConnectionReq;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreConfigurationsRes;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreListResponse;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreReq;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreResponse;
import org.wso2.carbon.identity.api.server.userstore.v1.UserstoresApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/userstores")
@Api(description = "The userstores API")

public class UserstoresApi  {

    @Autowired
    private UserstoresApiService delegate;

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add a secondary user store.", notes = "This API provides the capability to add a secondary user store.  **NOTE:**    To retrieve the available user store classes/types, use the **api/server/v1/userstores/meta/types** API.   <b>Permission required:</b>   - /permission/admin ", response = UserStoreResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = UserStoreResponse.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = Void.class),
        @ApiResponse(code = 409, message = "Element Already Exists.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response addUserStore(@ApiParam(value = "Secondary user store to add." ) @Valid UserStoreReq userStoreReq) {

        return delegate.addUserStore(userStoreReq );
    }

    @Valid
    @DELETE
    @Path("/{userstore-domain-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a secondary user store.", notes = "This API provides the capability to delete a secondary user store matching to the given user store domain id.   <b>Permission required:</b>  *_/permission/admin ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No Content.", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response deleteUserStore(@ApiParam(value = "The unique name of the user store domain",required=true) @PathParam("userstore-domain-id") String userstoreDomainId) {

        return delegate.deleteUserStore(userstoreDomainId );
    }

    @Valid
    @GET
    @Path("/meta/types")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the available user store classes/types.", notes = "This API provides the capability to retrieve the available user store types.   <b>Permission required:</b>  *_/permission/admin ", response = AvailableUserStoreClassesRes.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Meta", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response.", response = AvailableUserStoreClassesRes.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getAvailableUserStoreTypes() {

        return delegate.getAvailableUserStoreTypes();
    }

    @Valid
    @GET
    @Path("/primary")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the configurations of primary userstore.", notes = "This API provides the capability to retrieve the configurations of primary user store.    <b>Permission required:</b> *_/permission/admin ", response = UserStoreConfigurationsRes.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response.", response = UserStoreConfigurationsRes.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getPrimaryUserStore() {

        return delegate.getPrimaryUserStore();
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve or list the configured secondary user stores.", notes = "This API provides the capability to list the configured secondary userstores. <b>Permission required:</b> *_/permission/admin ", response = UserStoreListResponse.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response.", response = UserStoreListResponse.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response getSecondaryUserStores(    @Valid@ApiParam(value = "maximum number of records to return")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "number of records to skip for pagination")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Condition to filter the retrieval of records.")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Define the order of how the retrieved records should be sorted.")  @QueryParam("sort") String sort,     @Valid@ApiParam(value = "Define set of user store attributes (as comma separated) to be returned.")  @QueryParam("requiredAttributes") String requiredAttributes) {

        return delegate.getSecondaryUserStores(limit,  offset,  filter,  sort,  requiredAttributes );
    }

    @Valid
    @GET
    @Path("/{userstore-domain-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the configurations of secondary user store based on its domain id.", notes = "This API provides the capability to retrieve the configurations of secondary user store based on its domain id.    <b>Permission required:</b> *_/permission/admin ", response = UserStoreConfigurationsRes.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response.", response = UserStoreConfigurationsRes.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getUserStoreByDomainId(@ApiParam(value = "The unique name of the user store domain",required=true) @PathParam("userstore-domain-id") String userstoreDomainId) {

        return delegate.getUserStoreByDomainId(userstoreDomainId );
    }

    @Valid
    @GET
    @Path("/meta/types/{type-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the properties of secondary user store of a given user store type.", notes = "This API provides the capability to retrieve the properties of secondary user store of a given class name.   <b>Permission required:</b>  *_/permission/admin ", response = MetaUserStoreType.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Meta", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response.", response = MetaUserStoreType.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response getUserStoreManagerProperties(@ApiParam(value = "Id of the user store type",required=true) @PathParam("type-id") String typeId) {

        return delegate.getUserStoreManagerProperties(typeId );
    }

    @Valid
    @PATCH
    @Path("/{userstore-domain-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch the secondary user store by it's domain id.", notes = "This API provides the capability to update the secondary user store's property using patch request by using its domain id.   <b>Permission required:</b>  *_/permission/admin ", response = UserStoreResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response.", response = UserStoreResponse.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response patchUserStore(@ApiParam(value = "The unique name of the user store domain",required=true) @PathParam("userstore-domain-id") String userstoreDomainId, @ApiParam(value = "" ,required=true) @Valid List<PatchDocument> patchDocument) {

        return delegate.patchUserStore(userstoreDomainId,  patchDocument );
    }

    @Valid
    @POST
    @Path("/test-connection")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Test the connection to the datasource used by a JDBC user store manager.", notes = "This API provides the capability to test the connection to the datasource used by a JDBC user store manager.    <b>Permission required:</b>   *_/permission/admin ", response = ConnectionEstablishedResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Store", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response.", response = ConnectionEstablishedResponse.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response testRDBMSConnection(@ApiParam(value = "RDBMS connection properties used to connect to the datasource used by a JDBC user store manager." ) @Valid RDBMSConnectionReq rdBMSConnectionReq) {

        return delegate.testRDBMSConnection(rdBMSConnectionReq );
    }

    @Valid
    @PUT
    @Path("/{userstore-domain-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update a user store by its domain id.", notes = "This API provides the capability to edit a user store based on its domain id.   <b>Permission required:</b>   *_/permission/admin ", response = UserStoreResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Store" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response.", response = UserStoreResponse.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response updateUserStore(@ApiParam(value = "Current domain id of the user store",required=true) @PathParam("userstore-domain-id") String userstoreDomainId, @ApiParam(value = "The secondary user store values which are needed to be edited for a given domain id." ) @Valid UserStoreReq userStoreReq) {

        return delegate.updateUserStore(userstoreDomainId,  userStoreReq );
    }

}
