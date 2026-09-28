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

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.vp.template.management.v1.ConnectedIdpsResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.Error;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.IssuerConfigListResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionCreationModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionList;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionUpdateModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionsApiService;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.factories.PresentationDefinitionsApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/presentation-definitions")
@Api(description = "The presentation-definitions API")

public class PresentationDefinitionsApi  {

    private final PresentationDefinitionsApiService delegate;

    public PresentationDefinitionsApi() {

        this.delegate = PresentationDefinitionsApiServiceFactory.getPresentationDefinitionsApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a presentation definition.", notes = "Creates a new presentation definition for the tenant.", response = PresentationDefinitionResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Presentation Definitions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created", response = PresentationDefinitionResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict — a presentation definition with the given name already exists.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response createPresentationDefinition(@ApiParam(value = "" ,required=true) @Valid PresentationDefinitionCreationModel presentationDefinitionCreationModel) {

        return delegate.createPresentationDefinition(presentationDefinitionCreationModel );
    }

    @Valid
    @DELETE
    @Path("/{definition-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a presentation definition.", notes = "Deletes a presentation definition. Fails with 409 Conflict if the definition is referenced by one or more connections. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Presentation Definitions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No Content — definition successfully deleted.", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict — the definition is in use by one or more connections.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response deletePresentationDefinition(@ApiParam(value = "UUID of the presentation definition.",required=true) @PathParam("definition-id") String definitionId) {

        return delegate.deletePresentationDefinition(definitionId );
    }

    @Valid
    @GET
    @Path("/{definition-id}/connected-idps")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get IDPs using a presentation definition.", notes = "Returns the list of identity provider connections that are configured to use this presentation definition. ", response = ConnectedIdpsResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Presentation Definitions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = ConnectedIdpsResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getConnectedIdps(@ApiParam(value = "UUID of the presentation definition.",required=true) @PathParam("definition-id") String definitionId) {

        return delegate.getConnectedIdps(definitionId );
    }

    @Valid
    @GET
    @Path("/{definition-id}/credentials/{credential-identifier}/issuer-configs")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get issuer configurations for a credential.", notes = "Returns all trusted issuer configurations for a specific credential within a presentation definition.", response = IssuerConfigListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Presentation Definitions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = IssuerConfigListResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getIssuerConfigs(@ApiParam(value = "UUID of the presentation definition.",required=true) @PathParam("definition-id") String definitionId, @ApiParam(value = "Identifier of the credential query within the presentation definition.",required=true) @PathParam("credential-identifier") String credentialIdentifier) {

        return delegate.getIssuerConfigs(definitionId,  credentialIdentifier );
    }

    @Valid
    @GET
    @Path("/{definition-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a presentation definition.", notes = "Returns a presentation definition by its unique identifier.", response = PresentationDefinitionResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Presentation Definitions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = PresentationDefinitionResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getPresentationDefinition(@ApiParam(value = "UUID of the presentation definition.",required=true) @PathParam("definition-id") String definitionId) {

        return delegate.getPresentationDefinition(definitionId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List presentation definitions.", notes = "Returns a cursor-paginated list of presentation definitions for the tenant. Supports SCIM-style filtering via the `filter` parameter. Only one of `before` or `after` may be provided at a time. ", response = PresentationDefinitionList.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Presentation Definitions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = PresentationDefinitionList.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response listPresentationDefinitions(    @Valid@ApiParam(value = "Base64-encoded cursor value for backward pagination.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "Base64-encoded cursor value for forward pagination.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "SCIM-style filter expression. Supports 'sw', 'co', 'ew', and 'eq' operations. Example: name sw \"my-def\" ")  @QueryParam("filter") String filter,     @Valid @Min(1) @Max(100)@ApiParam(value = "Maximum number of records to return per page. Defaults to 10, capped at 100.", defaultValue="10") @DefaultValue("10")  @QueryParam("limit") Integer limit) {

        return delegate.listPresentationDefinitions(before,  after,  filter,  limit );
    }

    @Valid
    @PUT
    @Path("/{definition-id}/credentials/{credential-identifier}/issuer-configs")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Replace issuer configurations for a credential.", notes = "Atomically replaces the entire set of trusted issuer configurations for a specific credential within a presentation definition. At least one configuration must be provided. ", response = IssuerConfigListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Presentation Definitions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response — the stored issuer configurations.", response = IssuerConfigListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response replaceIssuerConfigs(@ApiParam(value = "UUID of the presentation definition.",required=true) @PathParam("definition-id") String definitionId, @ApiParam(value = "Identifier of the credential query within the presentation definition.",required=true) @PathParam("credential-identifier") String credentialIdentifier, @ApiParam(value = "" ,required=true) @Valid IssuerConfigListResponse issuerConfigListResponse) {

        return delegate.replaceIssuerConfigs(definitionId,  credentialIdentifier,  issuerConfigListResponse );
    }

    @Valid
    @PATCH
    @Path("/{definition-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update a presentation definition.", notes = "Replaces the fields of an existing presentation definition. Only fields included in the request body are updated. ", response = PresentationDefinitionResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Presentation Definitions" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = PresentationDefinitionResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response updatePresentationDefinition(@ApiParam(value = "UUID of the presentation definition.",required=true) @PathParam("definition-id") String definitionId, @ApiParam(value = "" ,required=true) @Valid PresentationDefinitionUpdateModel presentationDefinitionUpdateModel) {

        return delegate.updatePresentationDefinition(definitionId,  presentationDefinitionUpdateModel );
    }

}
