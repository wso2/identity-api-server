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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.factories.UsersApiServiceFactory;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.Error;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserShareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserSharingPatchRequest;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserUnshareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserUnshareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.UsersApiService;

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
    @GET
    @Path("/{userId}/share")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List organizations where the user has shared access", notes = "Retrieve the list of organizations where the specified user has shared access, including per-organization effective role assignments.  This follows the same pattern as **`GET /applications/{applicationId}/share`**.  **Scope required:** `internal_user_shared_access_view`", response = UserSharedOrganizationsResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Accessible Organizations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response with the user's shared organizations.", response = UserSharedOrganizationsResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getUserSharedOrganizations(@ApiParam(value = "The ID of the user.",required=true) @PathParam("userId") String userId,     @Valid@ApiParam(value = "Base64 encoded cursor for forward pagination.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Base64 encoded cursor for backward pagination.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "Maximum number of records to return.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports operations like `sw`, `co`, `ew`, and `eq` depending on implementation.")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Whether to include shared organizations recursively in the hierarchy.")  @QueryParam("recursive") Boolean recursive) {

        return delegate.getUserSharedOrganizations(userId,  after,  before,  limit,  filter,  recursive );
    }

    @Valid
    @PATCH
    @Path("/share")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Perform incremental role assignment operations for already shared users", notes = "Perform **incremental updates** to the role assignments of already shared users.  This endpoint supports **SCIM-like PATCH semantics**: - `op: \"add\"`    → assign additional roles. - `op: \"remove\"` → remove specific roles.  > **Note:**   > Only **role assignments** are managed here. Sharing/unsharing organizations > is handled via `/users/share`, `/users/share-with-all`, `/users/unshare`, > and `/users/unshare-with-all`.  **Scope required:** `internal_user_share`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Role assignment patch operation processed successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response patchUserSharing(@ApiParam(value = "" ,required=true) @Valid UserSharingPatchRequest userSharingPatchRequest) {

        return delegate.patchUserSharing(userSharingPatchRequest );
    }

    @Valid
    @POST
    @Path("/share-with-all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share users with all organizations", notes = "Share one or more users with **all organizations**, or all immediate child organizations, according to the specified policy.  A common `roleAssignment` can be provided to assign roles in all matching organizations.  This endpoint is treated as a **processing function** and responds with `202 Accepted`.  **Scope required:** `internal_user_share`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Share-all process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response shareUsersWithAll(@ApiParam(value = "" ,required=true) @Valid UserShareAllRequestBody userShareAllRequestBody) {

        return delegate.shareUsersWithAll(userShareAllRequestBody );
    }

    @Valid
    @POST
    @Path("/share")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share users with specific organizations", notes = "Share one or more users with a selected set of organizations, optionally assigning roles to each shared user in each target organization.  This endpoint is treated as a **processing function**: it triggers a sharing process and responds with `202 Accepted`.  **Scope required:** `internal_user_share`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "User sharing process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response shareUsersWithSelected(@ApiParam(value = "" ,required=true) @Valid UserShareSelectedRequestBody userShareSelectedRequestBody) {

        return delegate.shareUsersWithSelected(userShareSelectedRequestBody );
    }

    @Valid
    @POST
    @Path("/unshare-with-all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Unshare users from all organizations", notes = "Completely remove all shared access for one or more users from **all organizations**.  This endpoint is treated as a **processing function** and responds with `202 Accepted`.  **Scope required:** `internal_user_unshare`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Unshare-all process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response unshareUsersFromAll(@ApiParam(value = "" ,required=true) @Valid UserUnshareAllRequestBody userUnshareAllRequestBody) {

        return delegate.unshareUsersFromAll(userUnshareAllRequestBody );
    }

    @Valid
    @POST
    @Path("/unshare")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Unshare users from specific organizations", notes = "Unshare one or more users from a selected set of organizations.  > **Note:**   > This only removes the user from the specified organizations. If the user > was shared with a parent org plus its children via a broader policy, > you must explicitly include all relevant organization IDs when unsharing.  This endpoint is treated as a **processing function** and responds with `202 Accepted`.  **Scope required:** `internal_user_unshare`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User Sharing" })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Unsharing process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response unshareUsersFromSelected(@ApiParam(value = "" ,required=true) @Valid UserUnshareSelectedRequestBody userUnshareSelectedRequestBody) {

        return delegate.unshareUsersFromSelected(userUnshareSelectedRequestBody );
    }

}
