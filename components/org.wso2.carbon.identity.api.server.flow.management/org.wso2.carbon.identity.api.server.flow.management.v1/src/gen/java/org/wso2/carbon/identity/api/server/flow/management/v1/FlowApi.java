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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.flow.management.v1.Error;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowRequest;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowApiService;
import org.wso2.carbon.identity.api.server.flow.management.v1.factories.FlowApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/flow")
@Api(description = "The flow API")

public class FlowApi  {

    private final FlowApiService delegate;

    public FlowApi() {

        this.delegate = FlowApiServiceFactory.getFlowApi();
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the complete flow", notes = "", response = FlowResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Flow Composer", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully retrieved the flow", response = FlowResponse.class),
        @ApiResponse(code = 400, message = "Invalid flow type specified", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Flow of specified type not found", response = Error.class)
    })
    public Response getFlow(    @Valid @NotNull(message = "Property  cannot be null.") @ApiParam(value = "Type of the flow to retrieve",required=true, allowableValues="SELF_REGISTRATION, PASSWORD_RECOVERY, ASK_PASSWORD")  @QueryParam("flowType") String flowType) {

        return delegate.getFlow(flowType );
    }

    @Valid
    @PUT
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create or update the complete flow", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Flow Composer" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Flow successfully updated", response = Void.class),
        @ApiResponse(code = 201, message = "Flow successfully created", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid request body or unsupported flow type", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Flow type not found", response = Error.class),
        @ApiResponse(code = 500, message = "Encountered a server error", response = Error.class)
    })
    public Response updateFlow(@ApiParam(value = "" ,required=true) @Valid FlowRequest flowRequest) {

        return delegate.updateFlow(flowRequest );
    }

}
