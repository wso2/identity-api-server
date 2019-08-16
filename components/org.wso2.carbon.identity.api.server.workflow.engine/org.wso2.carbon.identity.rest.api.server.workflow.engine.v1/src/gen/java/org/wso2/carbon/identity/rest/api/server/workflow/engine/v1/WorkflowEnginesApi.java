/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.server.workflow.engine.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.dto.*;
import org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.WorkflowEnginesApiService;
import org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.factories.WorkflowEnginesApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.dto.WorkFlowEngineDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/workflow-engines")


@io.swagger.annotations.Api(value = "/workflow-engines", description = "the workflow-engines API")
public class WorkflowEnginesApi  {

   @Autowired
   private WorkflowEnginesApiService delegate;

    @GET
    
    
    
    @io.swagger.annotations.ApiOperation(value = "Retrieve all the available workflow engines.", notes = "Retrieve metadata information of all the workflow engines in the system.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/humantask/viewtasks\n", response = WorkFlowEngineDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response searchWorkFlowEngines()
    {
    return delegate.searchWorkFlowEngines();
    }
}

