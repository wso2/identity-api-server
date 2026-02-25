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

package org.wso2.carbon.identity.api.server.vp.template.management.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.factories.PresentationDefinitionsApiServiceFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * JAX-RS resource for Presentation Definition management.
 */
@Path("/vp/template")
@Api(value = "/vp/template", description = "Presentation Definition Management API")
public class PresentationDefinitionsApi {

    private final PresentationDefinitionsApiService delegate =
            PresentationDefinitionsApiServiceFactory.getPresentationDefinitionsApi();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List Presentation Definitions", response = PresentationDefinitionList.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PresentationDefinitionList.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response listPresentationDefinitions() {

        return delegate.listPresentationDefinitions();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a Presentation Definition",
            response = PresentationDefinitionResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = PresentationDefinitionResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 409, message = "Conflict", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response createPresentationDefinition(
            @ApiParam(value = "Presentation definition to create", required = true)
                    PresentationDefinitionCreationModel presentationDefinitionCreationModel) {

        return delegate.createPresentationDefinition(presentationDefinitionCreationModel);
    }

    @GET
    @Path("/{definition-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get a Presentation Definition",
            response = PresentationDefinitionResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PresentationDefinitionResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getPresentationDefinition(
            @ApiParam(value = "Unique identifier of the presentation definition.", required = true)
            @PathParam("definition-id") String definitionId) {

        return delegate.getPresentationDefinition(definitionId);
    }

    @PUT
    @Path("/{definition-id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update a Presentation Definition",
            response = PresentationDefinitionResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PresentationDefinitionResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response updatePresentationDefinition(
            @ApiParam(value = "Unique identifier of the presentation definition.", required = true)
            @PathParam("definition-id") String definitionId,
            @ApiParam(value = "Updated presentation definition", required = true)
                    PresentationDefinitionUpdateModel presentationDefinitionUpdateModel) {

        return delegate.updatePresentationDefinition(definitionId, presentationDefinitionUpdateModel);
    }

    @DELETE
    @Path("/{definition-id}")
    @ApiOperation(value = "Delete a Presentation Definition")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response deletePresentationDefinition(
            @ApiParam(value = "Unique identifier of the presentation definition.", required = true)
            @PathParam("definition-id") String definitionId) {

        return delegate.deletePresentationDefinition(definitionId);
    }
}
