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
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowListResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowRequest;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.WorkflowsApiService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.factories.WorkflowsApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/workflows")
@Api(description = "The workflows API")

public class WorkflowsApi  {

    private final WorkflowsApiService delegate;

    public WorkflowsApi() {

        this.delegate = WorkflowsApiServiceFactory.getWorkflowsApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add a new workflow.", notes = "Create a new workflow using the specified workflow template and execution engine.  <b>Scope required:</b>     * internal_workflow_create ", response = WorkflowResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Workflow Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = WorkflowResponse.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Item Already Exists", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response addWorkflow(@ApiParam(value = "This represents the workflow to be created." ,required=true) @Valid WorkflowRequest workflowRequest) {

        return delegate.addWorkflow(workflowRequest );
    }

    @Valid
    @DELETE
    @Path("/{workflow-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete the workflow by workflow-id.", notes = "Delete a specific workflow identified by the workflow-id    <b>Scope required:</b>   * internal_workflow_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Workflow Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response deleteWorkflowById(@ApiParam(value = "Workflow ID",required=true) @PathParam("workflow-id") String workflowId) {

        return delegate.deleteWorkflowById(workflowId );
    }

    @Valid
    @GET
    @Path("/{workflow-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the workflow by workflow id.", notes = "Retrieve information about a specific workflow identified by the workflow id.  <b>Scope required:</b>     * internal_workflow_view ", response = WorkflowResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Workflow Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = WorkflowResponse.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getWorkflowById(@ApiParam(value = "Workflow ID",required=true) @PathParam("workflow-id") String workflowId) {

        return delegate.getWorkflowById(workflowId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve all the available workflows.", notes = "Retrieve all the available workflows in the system.  <b>Scope required:</b>     * internal_workflow_view ", response = WorkflowListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Workflow Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = WorkflowListResponse.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getWorkflows(    @Valid@ApiParam(value = "Maximum number of records to return")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Records, filtered by their name")  @QueryParam("filter") String filter) {

        return delegate.getWorkflows(limit,  offset,  filter );
    }

    @Valid
    @PUT
    @Path("/{workflow-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update an existing workflow.", notes = "Update a workflow identified by workflow-id.  <b>Scope required:</b>             * internal_workflow_update ", response = WorkflowResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Workflow Management" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = WorkflowResponse.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response updateWorkflow(@ApiParam(value = "Workflow ID",required=true) @PathParam("workflow-id") String workflowId, @ApiParam(value = "Contains the details of the updated workflow." ) @Valid WorkflowRequest workflowRequest) {

        return delegate.updateWorkflow(workflowId,  workflowRequest );
    }

}
