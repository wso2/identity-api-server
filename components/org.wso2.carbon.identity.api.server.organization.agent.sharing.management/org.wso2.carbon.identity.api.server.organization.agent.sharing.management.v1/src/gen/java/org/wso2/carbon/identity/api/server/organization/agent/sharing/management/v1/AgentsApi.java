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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharingPatchRequest;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.AgentsApiService;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.factories.AgentsApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/agents")
@Api(description = "The agents API")

public class AgentsApi  {

    private final AgentsApiService delegate;

    public AgentsApi() {

        this.delegate = AgentsApiServiceFactory.getAgentsApi();
    }

    @Valid
    @GET
    @Path("/{agentId}/share")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List organizations where the agent has shared access", notes = "Retrieve the list of organizations where the specified agent has shared access, including per-organization effective role assignments.  This follows the same pattern as **`GET /users/{userId}/share`**.  **Scope required:** `internal_user_shared_access_view`", response = AgentSharedOrganizationsResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Agent Accessible Organizations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response with the agent's shared organizations.", response = AgentSharedOrganizationsResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getAgentSharedOrganizations(@ApiParam(value = "The ID of the agent.",required=true) @PathParam("agentId") String agentId,     @Valid@ApiParam(value = "Base64 encoded cursor value for backward pagination.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "Base64 encoded cursor value for forward pagination.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports `sw`, `co`, `ew`, and `eq` operations.")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Maximum number of records to return. If you do not specify this parameter, this will return all shared organizations.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Determines whether a recursive search should happen. If set to true, includes shared organizations in all levels of the hierarchy; if set to false, includes only shared organizations in the next level of the hierarchy.")  @QueryParam("recursive") Boolean recursive,     @Valid@ApiParam(value = "Specifies the required parameters in the response. Supported values: `roles`, `sharingMode`.")  @QueryParam("attributes") String attributes) {

        return delegate.getAgentSharedOrganizations(agentId,  before,  after,  filter,  limit,  recursive,  attributes );
    }

    @Valid
    @PATCH
    @Path("/share")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Perform incremental role assignment operations for already shared agents", notes = "Perform **incremental updates** to the shared attributes (e.g., role assignments) of already shared agents.  This endpoint supports **SCIM-like PATCH semantics**: - `op: \"add\"`    → assign additional roles. - `op: \"remove\"` → remove specific roles.  > **Note:** > Only **role assignment** updates are managed here as of now. Sharing/unsharing organizations > is handled via `/agents/share`, `/agents/share-with-all`, `/agents/unshare`, > and `/agents/unshare-with-all`.  **Scope required:** `internal_user_shared_access_update`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Agent Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Role assignment patch operation processed successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response patchAgentSharing(@ApiParam(value = "" ,required=true) @Valid AgentSharingPatchRequest agentSharingPatchRequest) {

        return delegate.patchAgentSharing(agentSharingPatchRequest );
    }

    @Valid
    @POST
    @Path("/share-with-all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share agents with all organizations", notes = "Share one or more agents with **all organizations** according to the specified policy.  A common `roleAssignment` can be provided to assign roles in all matching organizations.  This endpoint is treated as a **processing function** and responds with `202 Accepted`.  **Scope required:** `internal_user_share`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Agent Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Share-all process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response shareAgentsWithAll(@ApiParam(value = "" ,required=true) @Valid AgentShareAllRequestBody agentShareAllRequestBody) {

        return delegate.shareAgentsWithAll(agentShareAllRequestBody );
    }

    @Valid
    @POST
    @Path("/share")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share agents with specific organizations", notes = "Share one or more agents with a selected set of organizations, optionally assigning roles to each shared agent in each target organization.  This endpoint is treated as a **processing function**: it triggers a sharing process and responds with `202 Accepted`.  **Scope required:** `internal_user_share`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Agent Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Agent sharing process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response shareAgentsWithSelected(@ApiParam(value = "" ,required=true) @Valid AgentShareSelectedRequestBody agentShareSelectedRequestBody) {

        return delegate.shareAgentsWithSelected(agentShareSelectedRequestBody );
    }

    @Valid
    @POST
    @Path("/unshare-with-all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Unshare agents from all organizations", notes = "Completely remove all shared access for one or more agents from **all organizations**.  This endpoint is treated as a **processing function** and responds with `202 Accepted`.  **Scope required:** `internal_user_unshare`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Agent Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Unshare-all process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response unshareAgentsFromAll(@ApiParam(value = "" ,required=true) @Valid AgentUnshareAllRequestBody agentUnshareAllRequestBody) {

        return delegate.unshareAgentsFromAll(agentUnshareAllRequestBody );
    }

    @Valid
    @POST
    @Path("/unshare")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Unshare agents from specific organizations", notes = "Unshare one or more agents from a selected set of organizations.  > **Note:** > This only removes the agent from the specified organizations. If the agent > was shared with a parent org plus its children via a broader policy, > you must explicitly include all relevant organization IDs when unsharing.  This endpoint is treated as a **processing function** and responds with `202 Accepted`.  **Scope required:** `internal_user_unshare`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Agent Sharing" })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Unsharing process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response unshareAgentsFromSelected(@ApiParam(value = "" ,required=true) @Valid AgentUnshareSelectedRequestBody agentUnshareSelectedRequestBody) {

        return delegate.unshareAgentsFromSelected(agentUnshareSelectedRequestBody );
    }

}
