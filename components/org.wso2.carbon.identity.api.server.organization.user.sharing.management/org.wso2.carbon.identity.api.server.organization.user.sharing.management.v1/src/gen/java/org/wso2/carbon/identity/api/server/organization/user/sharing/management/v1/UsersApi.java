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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.factories.UsersApiServiceFactory;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserShareRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserShareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserSharedRolesResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserUnshareRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserUnshareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.UsersApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/users")
@Api(description = "The users API")

public class UsersApi  {

    private final UsersApiService delegate;

    public UsersApi() {

        this.delegate = UsersApiServiceFactory.getUsersApi();
    }

    @Valid
    @POST
    @Path("/share")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share a user across specific organizations", notes = "This API shares one or more users across specified organizations, assigning roles based on the provided policy. The policy defines the sharing scope for each organization, including whether access extends to child organizations.  <b>Permission required:</b> `internal_user_shared_access_add` ", response = ProcessSuccessResponse.class, tags={ "User Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Sharing process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response processUserSharing(@ApiParam(value = "" ,required=true) @Valid UserShareRequestBody userShareRequestBody) {

        return delegate.processUserSharing(userShareRequestBody );
    }

    @Valid
    @POST
    @Path("/share-with-all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share a user with all organizations", notes = "This API shares users across all organizations, applying the provided roles to each organization. The policy determines the scope of sharing, including whether it applies to all current organizations or future organizations as well.  <b>Permission required:</b> `internal_user_shared_access_add` ", response = ProcessSuccessResponse.class, tags={ "User Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Sharing process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response processUserSharingAll(@ApiParam(value = "" ,required=true) @Valid UserShareWithAllRequestBody userShareWithAllRequestBody) {

        return delegate.processUserSharingAll(userShareWithAllRequestBody );
    }

    @Valid
    @POST
    @Path("/unshare")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Unshare a user from specific organizations", notes = "This API removes shared access for one or more users from specified organizations. The payload includes the list of user IDs and the organizations from which the users should be unshared.  <b>Permission required:</b> `internal_user_shared_access_delete` ", response = ProcessSuccessResponse.class, tags={ "User Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Unsharing process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response processUserUnsharing(@ApiParam(value = "" ,required=true) @Valid UserUnshareRequestBody userUnshareRequestBody) {

        return delegate.processUserUnsharing(userUnshareRequestBody );
    }

    @Valid
    @POST
    @Path("/unshare-with-all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Remove a user's shared access", notes = "This API removes all shared access for one or more users, unsharing them from all organizations.  <b>Permission required:</b> `internal_user_shared_access_delete` ", response = ProcessSuccessResponse.class, tags={ "User Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Share removal process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response removeUserSharing(@ApiParam(value = "" ,required=true) @Valid UserUnshareWithAllRequestBody userUnshareWithAllRequestBody) {

        return delegate.removeUserSharing(userUnshareWithAllRequestBody );
    }

    @Valid
    @GET
    @Path("/{userId}/shared-organizations")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get organizations a user has access to", notes = "This API retrieves the list of organizations where the specified user has shared access, with support for pagination and filtering.  <b>Permission required:</b> `internal_user_shared_access_view` ", response = UserSharedOrganizationsResponse.class, tags={ "User Accessible Organizations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response with Accessible Organizations", response = UserSharedOrganizationsResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response usersUserIdSharedOrganizationsGet(@ApiParam(value = "The ID of the user whose accessible organizations are being retrieved.",required=true) @PathParam("userId") String userId,     @Valid@ApiParam(value = "The cursor pointing to the item after which the next page of results should be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "The cursor pointing to the item before which the previous page of results should be returned.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "The maximum number of results to return per page.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "A filter to apply to the results, such as by organization name or other criteria.")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Whether to retrieve organizations recursively, including child organizations.", defaultValue="false") @DefaultValue("false")  @QueryParam("recursive") Boolean recursive) {

        return delegate.usersUserIdSharedOrganizationsGet(userId,  after,  before,  limit,  filter,  recursive );
    }

    @Valid
    @GET
    @Path("/{userId}/shared-roles")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get roles assigned to a user in an organization", notes = "This API fetches the roles assigned to the specified user within a particular organization, with support for pagination, filtering, and recursion.  <b>Permission required:</b> `internal_user_shared_access_view` ", response = UserSharedRolesResponse.class, tags={ "User Accessible Roles" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response with Accessible Roles", response = UserSharedRolesResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response usersUserIdSharedRolesGet(@ApiParam(value = "The ID of the user for whom roles are being retrieved.",required=true) @PathParam("userId") String userId,     @Valid @NotNull(message = "Property  cannot be null.") @ApiParam(value = "The organization ID for which roles are being fetched.",required=true)  @QueryParam("orgId") String orgId,     @Valid@ApiParam(value = "The cursor pointing to the item after which the next page of results should be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "The cursor pointing to the item before which the previous page of results should be returned.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "The maximum number of results to return per page.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Filter to apply when retrieving the roles.")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Set to true to retrieve roles recursively.")  @QueryParam("recursive") Boolean recursive) {

        return delegate.usersUserIdSharedRolesGet(userId,  orgId,  after,  before,  limit,  filter,  recursive );
    }

}
