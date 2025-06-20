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
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowEngine;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.WorkflowEnginesApiService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.factories.WorkflowEnginesApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/workflow-engines")
@Api(description = "The workflow-engines API")

public class WorkflowEnginesApi  {

    private final WorkflowEnginesApiService delegate;

    public WorkflowEnginesApi() {

        this.delegate = WorkflowEnginesApiServiceFactory.getWorkflowEnginesApi();
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve all the available workflow engines.", notes = "Retrieve metadata information of all the workflow engines in the system. ", response = WorkflowEngine.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "search results matching criteria", response = WorkflowEngine.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response searchWorkFlowEngines() {

        return delegate.searchWorkFlowEngines();
    }

}
