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

package org.wso2.carbon.identity.api.server.consent.management.v2;

import org.wso2.carbon.identity.api.server.consent.management.v2.factories.ConsentsApiServiceFactory;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.AuthorizationCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.AuthorizationDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentListResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentResponseDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentValidateResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ErrorDTO;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/consents")
@Api(description = "The consents API")

public class ConsentsApi  {

    private final ConsentsApiService delegate;

    public ConsentsApi() {

        this.delegate = ConsentsApiServiceFactory.getConsentsApi();
    }

    @Valid
    @POST
    @Path("/{consentId}/authorize")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Authorize a consent", notes = "Authorizes a PENDING consent. The authenticated user must be in the consent's authorization list. When all required users have approved, the consent transitions to ACTIVE state. If any user rejects, the consent transitions to REJECTED state. Returns 403 if the calling user is not in the authorization list. ", response = AuthorizationDTO.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Consents", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Consent authorized successfully", response = AuthorizationDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 409, message = "Conflict", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response consentsAuthorize( @Size(min=1,max=255)@ApiParam(value = "ID of the consent.",required=true) @PathParam("consentId") String consentId, @ApiParam(value = "" ,required=true) @Valid AuthorizationCreateRequest authorizationCreateRequest) {

        return delegate.consentsAuthorize(consentId,  authorizationCreateRequest );
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a consent record", notes = "Record user consent for specified purposes and elements. ", response = ConsentResponseDTO.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Consents", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Consent recorded successfully", response = ConsentResponseDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 409, message = "Conflict", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response consentsCreate(@ApiParam(value = "" ,required=true) @Valid ConsentCreateRequest consentCreateRequest) {

        return delegate.consentsCreate(consentCreateRequest );
    }

    @Valid
    @GET
    @Path("/{consentId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a consent record", notes = "Retrieve a specific consent record by consent ID. ", response = ConsentDTO.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Consents", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Consent record details", response = ConsentDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response consentsGet( @Size(min=1,max=255)@ApiParam(value = "ID of the consent.",required=true) @PathParam("consentId") String consentId) {

        return delegate.consentsGet(consentId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List consent records", notes = "Retrieve consent records with optional filtering. ", response = ConsentListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Consents", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "List of consents", response = ConsentListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response consentsList(    @Valid @Size(max=255)@ApiParam(value = "Filter by subject username.")  @QueryParam("subjectId") String subjectId,     @Valid @Size(max=255)@ApiParam(value = "Filter by service ID.")  @QueryParam("serviceId") String serviceId,     @Valid@ApiParam(value = "Filter consents by state.", allowableValues="PENDING, ACTIVE, REJECTED, REVOKED, EXPIRED")  @QueryParam("state") String state,     @Valid@ApiParam(value = "Filter consents by purpose ID.")  @QueryParam("purposeId") String purposeId,     @Valid@ApiParam(value = "Filter consents by specific purpose version ID.")  @QueryParam("purposeVersionId") String purposeVersionId,     @Valid @Min(1)@ApiParam(value = "Maximum number of records to return.", defaultValue="10") @DefaultValue("10")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Cursor for forward pagination. Pass the base64-encoded UUID of the last item received from the previous page to retrieve the next page of results. ")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Cursor for backward pagination. Pass the base64-encoded UUID of the first item received from the current page to retrieve the previous page of results. ")  @QueryParam("before") String before) {

        return delegate.consentsList(subjectId,  serviceId,  state,  purposeId,  purposeVersionId,  limit,  after,  before );
    }

    @Valid
    @POST
    @Path("/{consentId}/revoke")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Revoke a consent", notes = "Revokes the consent. Idempotent — if the consent is already revoked, returns 204 with no error. For consents with an authorization list, only users in that list may revoke (returns 403 otherwise). ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Consents", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Consent revoked successfully", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response consentsRevoke( @Size(min=1,max=255)@ApiParam(value = "ID of the consent.",required=true) @PathParam("consentId") String consentId) {

        return delegate.consentsRevoke(consentId );
    }

    @Valid
    @GET
    @Path("/{consentId}/validate")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the current state of a consent object", notes = "", response = ConsentValidateResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Consents" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Validation result", response = ConsentValidateResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response consentsValidate( @Size(min=1,max=255)@ApiParam(value = "ID of the consent.",required=true) @PathParam("consentId") String consentId) {

        return delegate.consentsValidate(consentId );
    }

}
