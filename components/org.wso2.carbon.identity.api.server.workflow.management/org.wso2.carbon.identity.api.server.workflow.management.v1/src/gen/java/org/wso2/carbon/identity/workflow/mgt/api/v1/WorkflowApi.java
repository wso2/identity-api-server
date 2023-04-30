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

package org.wso2.carbon.identity.workflow.mgt.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.workflow.mgt.api.v1.model.Error;
import org.wso2.carbon.identity.workflow.mgt.api.v1.model.WorkflowApprovalPatchRequest;
import org.wso2.carbon.identity.workflow.mgt.api.v1.WorkflowApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/workflow")
@Api(description = "The workflow API")

public class WorkflowApi  {

    @Autowired
    private WorkflowApiService delegate;

    @Valid
    @PATCH
    @Path("/{workflow-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the workflow request status. ", notes = "This API provides the capability to update the workflow request status. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Void.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response workflowWorkflowIdPatch(@ApiParam(value = "ID of the workflow request.",required=true) @PathParam("workflow-id") String workflowId, @ApiParam(value = "" ) @Valid WorkflowApprovalPatchRequest workflowApprovalPatchRequest) {

        return delegate.workflowWorkflowIdPatch(workflowId,  workflowApprovalPatchRequest );
    }

}
