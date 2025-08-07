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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.wso2.carbon.identity.api.server.flow.management.v1.factories.FlowApiServiceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/flow")
@Api(description = "The flow API")

public class FlowApi {

    private final FlowApiService delegate;

    public FlowApi() {

        this.delegate = FlowApiServiceFactory.getFlowApi();
    }

    @Valid
    @POST
    @Path("/generate")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Generate a flow", notes = "", response = FlowGenerateResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Generate Flow",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flow generation request successfully submitted", response = FlowGenerateResponse.class),
            @ApiResponse(code = 400, message = "Invalid flow type specified", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class)
    })
    public Response generateFlow(@ApiParam(value = "", required = true) @Valid FlowGenerateRequest flowGenerateRequest) {

        return delegate.generateFlow(flowGenerateRequest);
    }

    @Valid
    @GET


    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve the complete flow", notes = "", response = FlowResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Flow Composer",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the flow", response = FlowResponse.class),
            @ApiResponse(code = 400, message = "Invalid flow type specified", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Flow of specified type not found", response = Error.class)
    })
    public Response getFlow(@Valid @NotNull(message = "Property  cannot be null.") @ApiParam(value = "Type of the flow to retrieve", required = true, allowableValues = "SELF_REGISTRATION, PASSWORD_RECOVERY, ASK_PASSWORD") @QueryParam("flowType") String flowType) {

        return delegate.getFlow(flowType);
    }

    @Valid
    @GET
    @Path("/config")

    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve metadata related to a flow type", notes = "", response = FlowConfig.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Flow Configuration",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved flow configurations", response = FlowConfig.class),
            @ApiResponse(code = 400, message = "Invalid flow type specified", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class)
    })
    public Response getFlowConfigForFlow(@Valid @NotNull(message = "Property  cannot be null.") @ApiParam(value = "Type of the flow to get configurations for", required = true, allowableValues = "SELF_REGISTRATION, PASSWORD_RECOVERY, ASK_PASSWORD") @QueryParam("flowType") String flowType) {

        return delegate.getFlowConfigForFlow(flowType);
    }

    @Valid
    @GET
    @Path("/configs")

    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve flow management configuration", notes = "", response = FlowConfig.class, responseContainer = "List", authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Flow Configuration",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved flow configurations", response = FlowConfig.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class)
    })
    public Response getFlowConfigs() {

        return delegate.getFlowConfigs();
    }

    @Valid
    @GET
    @Path("/result")

    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve flow generation result", notes = "", response = FlowGenerateResult.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Generate Flow",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the generation result", response = FlowGenerateResult.class),
            @ApiResponse(code = 400, message = "Invalid operation specified", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 500, message = "Encountered a server error", response = Error.class)
    })
    public Response getFlowGenerationResult(@Valid @NotNull(message = "Property  cannot be null.") @ApiParam(value = "Operation id to get the generation result", required = true) @QueryParam("operationId") String operationId) {

        return delegate.getFlowGenerationResult(operationId);
    }

    @Valid
    @GET
    @Path("/status")

    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve flow generation status", notes = "", response = FlowGenerateStatus.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Generate Flow",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the generation status.", response = FlowGenerateStatus.class),
            @ApiResponse(code = 400, message = "Invalid operation specified", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 500, message = "Encountered a server error", response = Error.class)
    })
    public Response getFlowGenerationStatus(@Valid @NotNull(message = "Property  cannot be null.") @ApiParam(value = "Operation id to get the generation status", required = true) @QueryParam("operationId") String operationId) {

        return delegate.getFlowGenerationStatus(operationId);
    }

    @Valid
    @GET
    @Path("/meta")

    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve metadata related to a flow type", notes = "", response = FlowMetaResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Flow Composer",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved flow metadata", response = FlowMetaResponse.class),
            @ApiResponse(code = 400, message = "Invalid flow type specified", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Metadata for specified flow type not found", response = Error.class)
    })
    public Response getFlowMeta(@Valid @NotNull(message = "Property  cannot be null.") @ApiParam(value = "Type of the flow to get metadata for", required = true, allowableValues = "SELF_REGISTRATION, PASSWORD_RECOVERY, ASK_PASSWORD") @QueryParam("flowType") String flowType) {

        return delegate.getFlowMeta(flowType);
    }

    @Valid
    @PUT

    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Create or update the complete flow", notes = "", response = Void.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Flow Composer",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flow successfully updated", response = Void.class),
            @ApiResponse(code = 201, message = "Flow successfully created", response = Void.class),
            @ApiResponse(code = 400, message = "Invalid request body or unsupported flow type", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Flow type not found", response = Error.class),
            @ApiResponse(code = 500, message = "Encountered a server error", response = Error.class)
    })
    public Response updateFlow(@ApiParam(value = "", required = true) @Valid FlowRequest flowRequest) {

        return delegate.updateFlow(flowRequest);
    }

    @Valid
    @PATCH
    @Path("/config")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Update the flow configuration", notes = "", response = FlowConfig.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Flow Configuration"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flow successfully updated", response = FlowConfig.class),
            @ApiResponse(code = 400, message = "Invalid request body or unsupported flow type", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Flow type not found", response = Error.class),
            @ApiResponse(code = 500, message = "Encountered a server error", response = Error.class)
    })
    public Response updateFlowConfig(@ApiParam(value = "", required = true) @Valid FlowConfigPatchModel flowConfigPatchModel) {

        return delegate.updateFlowConfig(flowConfigPatchModel);
    }

}
