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
import org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.WorkflowEnginesApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

@Path("/workflow-engines")
@Api(description = "The workflow-engines API")

public class WorkflowEnginesApi  {

    @Autowired
    private WorkflowEnginesApiService delegate;

    @Valid
    @GET
    
    
    
    @ApiOperation(value = "Removed from API", notes = "Retrieve metadata information of all the workflow engines in the system. (Removed from API) ", response = Void.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 410, message = "Gone", response = Void.class)
    })
    public Response searchWorkFlowEngines() {

        return delegate.searchWorkFlowEngines();
    }
}
