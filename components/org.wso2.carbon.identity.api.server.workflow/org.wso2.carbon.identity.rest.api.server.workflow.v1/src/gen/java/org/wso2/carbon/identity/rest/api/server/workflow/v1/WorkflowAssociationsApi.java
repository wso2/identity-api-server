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
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowAssociationListResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowAssociationPatchRequest;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowAssociationRequest;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowAssociationResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.WorkflowAssociationsApiService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.factories.WorkflowAssociationsApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/workflow-associations")
@Api(description = "The workflow-associations API")

public class WorkflowAssociationsApi  {

    private final WorkflowAssociationsApiService delegate;

    public WorkflowAssociationsApi() {

        this.delegate = WorkflowAssociationsApiServiceFactory.getWorkflowAssociationsApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a new workflow association.", notes = "Create a new workflow association by assigning a user operation to an existing workflow.  <b>Scope required:</b>             * internal_workflow_association_create ", response = WorkflowAssociationResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Workflow Associations Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Item Created", response = WorkflowAssociationResponse.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Item Already Exists", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response addWorkflowAssociation(@ApiParam(value = "Contains the details of the newly created workflow association." ,required=true) @Valid WorkflowAssociationRequest workflowAssociationRequest) {

        return delegate.addWorkflowAssociation(workflowAssociationRequest );
    }

    @Valid
    @DELETE
    @Path("/{association-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete the workflow association by association-id.", notes = "Delete a specific workflow association identified by the association ID.  <b>Scope required:</b>                     * internal_workflow_association_view ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Workflow Associations Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response deleteWorkflowAssociationById(@ApiParam(value = "Workflow Association ID",required=true) @PathParam("association-id") String associationId) {

        return delegate.deleteWorkflowAssociationById(associationId );
    }

    @Valid
    @GET
    @Path("/{association-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the workflow association by association-id.", notes = "Get a specific workflow association identified by the association id.  <b>Scope required:</b>                     * internal_workflow_association_view ", response = WorkflowAssociationResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Workflow Associations Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Array of workflow associations matching the search criteria", response = WorkflowAssociationResponse.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getWorkflowAssociationById(@ApiParam(value = "Workflow Association ID",required=true) @PathParam("association-id") String associationId) {

        return delegate.getWorkflowAssociationById(associationId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve all the available workflow associations.", notes = "Retrieve all the available workflow associations in the system.  <b>Scope required:</b>             * internal_workflow_association_view ", response = WorkflowAssociationListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Workflow Associations Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = WorkflowAssociationListResponse.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getWorkflowAssociations(    @Valid@ApiParam(value = "Maximum number of records to return")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Records, filtered by their name")  @QueryParam("filter") String filter) {

        return delegate.getWorkflowAssociations(limit,  offset,  filter );
    }

    @Valid
    @PATCH
    @Path("/{association-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Partially update association by association-id. ", notes = "Partially update an association by association-id.<br>    <b>Scope required:</b> <br>       * internal_workflow_association_update ", response = WorkflowAssociationResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Workflow Associations Management" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully Updated", response = WorkflowAssociationResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "#/components/responses/Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "#/components/responses/Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "#/components/responses/Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateAssociation(@ApiParam(value = "Workflow Association ID",required=true) @PathParam("association-id") String associationId, @ApiParam(value = "Represents the association details to be updated." ) @Valid WorkflowAssociationPatchRequest workflowAssociationPatchRequest) {

        return delegate.updateAssociation(associationId,  workflowAssociationPatchRequest );
    }

}
