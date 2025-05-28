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

package org.wso2.carbon.identity.api.server.flow.execution.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.flow.execution.v1.FlowExecutionRequest;
import org.wso2.carbon.identity.api.server.flow.execution.v1.FlowExecutionResponse;
import org.wso2.carbon.identity.api.server.flow.execution.v1.RegistrationApiService;
import org.wso2.carbon.identity.api.server.flow.execution.v1.factories.RegistrationApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/registration")
@Api(description = "The registration API")

public class RegistrationApi  {

    private final RegistrationApiService delegate;

    public RegistrationApi() {

        this.delegate = RegistrationApiServiceFactory.getRegistrationApi();
    }

    @Valid
    @POST
    @Path("/execute")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Execute a flow step", notes = "", response = FlowExecutionResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Execute a flow step" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully executed the flow step", response = FlowExecutionResponse.class)
    })
    public Response registrationExecutePost(@ApiParam(value = "" ,required=true) @Valid FlowExecutionRequest flowExecutionRequest) {

        return delegate.registrationExecutePost(flowExecutionRequest );
    }

}
