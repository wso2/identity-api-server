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

package org.wso2.carbon.identity.rest.api.server.workflow.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.Error;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowInstanceListItem;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowInstanceListResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.WorkflowInstancesApiService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.factories.WorkflowInstancesApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/workflow-instances")
@Api(description = "The workflow-instances API")

public class  WorkflowInstancesApi  {

    private final WorkflowInstancesApiService delegate;

    public WorkflowInstancesApi() {

        this.delegate = WorkflowInstancesApiServiceFactory.getWorkflowInstancesApi();
    }

    @Valid
    @DELETE
    @Path("/{instance_id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete workflow instance by ID", notes = "Delete a workflow instance by providing the instance ID.  <b>Scope required:</b> internal_workflow_instance_manage ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully deleted the workflow instance", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class)
    })
    public Response deleteWorkflowInstance(@ApiParam(value = "",required=true) @PathParam("instance_id") String instanceId) {

        return delegate.deleteWorkflowInstance(instanceId );
    }

    @Valid
    @GET
    @Path("/{instance_id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get workflow instance by ID", notes = "Retrieve a specific workflow instance by providing its unique ID.  <b>Scope required:</b> internal_workflow_instance_view ", response = WorkflowInstanceListItem.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Workflow instance retrieved successfully", response = WorkflowInstanceListItem.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class)
    })
    public Response getWorkflowInstanceById(@ApiParam(value = "",required=true) @PathParam("instance_id") String instanceId) {

        return delegate.getWorkflowInstanceById(instanceId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get workflow instances for a tenant", notes = "Retrieve workflow instances filtered by various parameters.  Use a `filter` query param with supported operators like `eq`, `gt`, `gte`, `lt`, `lte`, `in`.  **Examples**: - `filter=createdAt>=2024-01-01T00:00:00Z` - `filter=status=APPROVED` - `filter=operationType in (CREATE,UPDATE)` - `filter=createdAt>=2024-01-01T00:00:00Z AND updatedAt<2025-01-01T00:00:00Z`  <b>Scope required:</b> internal_workflow_instance_view ", response = WorkflowInstanceListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Workflow instances retrieved successfully", response = WorkflowInstanceListResponse.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getWorkflowInstances(    @Valid@ApiParam(value = "", defaultValue="25") @DefaultValue("25")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "", defaultValue="0") @DefaultValue("0")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Filter conditions using logical expressions. Supported operators: =, !=, >, >=, <, <=, in. Combine multiple conditions with `AND`, `OR`. ")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "", allowableValues="createdAt, updatedAt, status, operationType, workflowName")  @QueryParam("sortBy") String sortBy,     @Valid@ApiParam(value = "", allowableValues="asc, desc")  @QueryParam("sortOrder") String sortOrder) {

        return delegate.getWorkflowInstances(limit,  offset,  filter,  sortBy,  sortOrder );
    }

}
