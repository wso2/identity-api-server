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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.factories.AgentsApiServiceFactory;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharedRolesResponse;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.AgentsApiService;

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
    @Path("/{agentId}/shared-organizations")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get organizations a agent has access to", notes = "This API retrieves the list of organizations where the specified agent has shared access, with support for pagination and filtering.  <b>Scope(Permission) required:</b> `internal_agent_shared_access_view` ", response = AgentSharedOrganizationsResponse.class, tags={ "Agent Accessible Organizations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response with Accessible Organizations", response = AgentSharedOrganizationsResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response agentsAgentIdSharedOrganizationsGet(@ApiParam(value = "The ID of the agent whose accessible organizations are being retrieved.",required=true) @PathParam("agentId") String agentId,     @Valid@ApiParam(value = "The cursor pointing to the item after which the next page of results should be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "The cursor pointing to the item before which the previous page of results should be returned.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "The maximum number of results to return per page.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "A filter to apply to the results, such as by organization name or other criteria.")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Whether to retrieve organizations recursively, including child organizations.", defaultValue="false") @DefaultValue("false")  @QueryParam("recursive") Boolean recursive) {

        return delegate.agentsAgentIdSharedOrganizationsGet(agentId,  after,  before,  limit,  filter,  recursive );
    }

    @Valid
    @GET
    @Path("/{agentId}/shared-roles")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get roles assigned to a agent in an organization", notes = "This API fetches the roles assigned to the specified agent within a particular organization, with support for pagination, filtering, and recursion.  <b>Scope(Permission) required:</b> `internal_agent_shared_access_view` ", response = AgentSharedRolesResponse.class, tags={ "Agent Accessible Roles", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response with Accessible Roles", response = AgentSharedRolesResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response agentsAgentIdSharedRolesGet(@ApiParam(value = "The ID of the agent for whom roles are being retrieved.",required=true) @PathParam("agentId") String agentId,     @Valid @NotNull(message = "Property  cannot be null.") @ApiParam(value = "The organization ID for which roles are being fetched.",required=true)  @QueryParam("orgId") String orgId,     @Valid@ApiParam(value = "The cursor pointing to the item after which the next page of results should be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "The cursor pointing to the item before which the previous page of results should be returned.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "The maximum number of results to return per page.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Filter to apply when retrieving the roles.")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Set to true to retrieve roles recursively.")  @QueryParam("recursive") Boolean recursive) {

        return delegate.agentsAgentIdSharedRolesGet(agentId,  orgId,  after,  before,  limit,  filter,  recursive );
    }

    @Valid
    @POST
    @Path("/share")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share a agent across specific organizations", notes = "This API shares one or more agents across specified organizations, assigning roles based on the provided policy. The policy defines the sharing scope for each organization, including whether access extends to child organizations.  <b>Scope(Permission) required:</b> `internal_org_user_share` ", response = ProcessSuccessResponse.class, tags={ "Agent Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Sharing process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response processAgentSharing(@ApiParam(value = "" ,required=true) @Valid AgentShareRequestBody agentShareRequestBody) {

        return delegate.processAgentSharing(agentShareRequestBody );
    }

    @Valid
    @POST
    @Path("/share-with-all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share a agent with all organizations", notes = "This API shares agents across all organizations, applying the provided roles to each organization. The policy determines the scope of sharing, including whether it applies to all current organizations or future organizations as well.  <b>Scope(Permission) required:</b> `internal_agent_share` ", response = ProcessSuccessResponse.class, tags={ "Agent Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Sharing process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response processAgentSharingAll(@ApiParam(value = "" ,required=true) @Valid AgentShareWithAllRequestBody agentShareWithAllRequestBody) {

        return delegate.processAgentSharingAll(agentShareWithAllRequestBody );
    }

    @Valid
    @POST
    @Path("/unshare")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Unshare a agent from specific organizations", notes = "This API removes shared access for one or more agents from specified organizations. The payload includes the list of agent IDs and the organizations from which the agents should be unshared.  <b>Scope(Permission) required:</b> `internal_agent_unshare` ", response = ProcessSuccessResponse.class, tags={ "Agent Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Unsharing process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response processAgentUnsharing(@ApiParam(value = "" ,required=true) @Valid AgentUnshareRequestBody agentUnshareRequestBody) {

        return delegate.processAgentUnsharing(agentUnshareRequestBody );
    }

    @Valid
    @POST
    @Path("/unshare-with-all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Remove a agent's shared access", notes = "This API removes all shared access for one or more agents, unsharing them from all organizations.  <b>Scope(Permission) required:</b> `internal_agent_unshare` ", response = ProcessSuccessResponse.class, tags={ "Agent Sharing" })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Share removal process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response removeAgentSharing(@ApiParam(value = "" ,required=true) @Valid AgentUnshareWithAllRequestBody agentUnshareWithAllRequestBody) {

        return delegate.removeAgentSharing(agentUnshareWithAllRequestBody );
    }

}
