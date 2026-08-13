/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * JAX-RS resource for Presentation Definition management.
 */
@Path("/openid4vp/presentation-definitions")
@Api(value = "/openid4vp/presentation-definitions", description = "Presentation Definition Management API")
public class PresentationDefinitionsApi {

    private final PresentationDefinitionsApiService delegate =
            PresentationDefinitionsApiServiceFactory.getPresentationDefinitionsApi();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List Presentation Definitions", response = PresentationDefinitionList.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PresentationDefinitionList.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response listPresentationDefinitions(
            @ApiParam(value = "Base64 encoded cursor value for backward pagination.")
            @QueryParam("before") String before,
            @ApiParam(value = "Base64 encoded cursor value for forward pagination.")
            @QueryParam("after") String after,
            @ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations.")
            @QueryParam("filter") String filter,
            @ApiParam(value = "Maximum number of records to return.")
            @QueryParam("limit") Integer limit) {

        return delegate.listPresentationDefinitions(before, after, filter, limit);
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

    @PATCH
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

    @GET
    @Path("/{definition-id}/connected-connections")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get connections using a Presentation Definition",
            response = ConnectedConnectionsResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ConnectedConnectionsResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getConnectedConnections(
            @ApiParam(value = "Unique identifier of the presentation definition.", required = true)
            @PathParam("definition-id") String definitionId) {

        return delegate.getConnectedConnections(definitionId);
    }

    @PATCH
    @Path("/{definition-id}/trusted-cas")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update Trusted CAs for a Credential",
            response = PresentationDefinitionResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PresentationDefinitionResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response patchTrustedCas(
            @ApiParam(value = "Unique identifier of the presentation definition.", required = true)
            @PathParam("definition-id") String definitionId,
            @ApiParam(value = "The credential query ID to target.", required = true)
            @QueryParam("credential-id") String credentialId,
            @ApiParam(value = "Patch operations to apply to trusted CAs.", required = true)
                    List<CertificatePatch> patchRequest) {

        return delegate.patchTrustedCas(definitionId, credentialId, patchRequest);
    }
}
