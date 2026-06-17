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
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentUpdateRequest;
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
    @ApiOperation(value = "Authorize a consent", notes = "Authorizes a PENDING consent. The authenticated user must be in the consent's authorization list, returns 403 otherwise. When all required users have approved, the consent transitions to ACTIVE state. If any user rejects, the consent transitions to REJECTED state. ", response = AuthorizationDTO.class, authorizations = {
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
    @ApiOperation(value = "Create a consent record", notes = "Record consent for specified purposes and elements.  Without a scope, the authenticated user creates and gives consent simultaneously. The consent is immediately recorded as `ACTIVE`. The `subjectId` field is ignored — the calling user is always the consent subject. Providing `authorizations` returns 400.  <b>Scope(Permission) required:</b> `internal_consent_mgt_consent_create` — Creates a consent record on behalf of a subject user without giving consent. The consent is created in `PENDING` state and requires the subject user to authorize via `POST /consents/{consentId}/authorize`. The `subjectId` field is required. The `authorizations` field may be provided to pre-declare which users must authorize the pending consent. ", response = ConsentResponseDTO.class, authorizations = {
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
    @ApiOperation(value = "Get a consent record", notes = "Retrieve a specific consent record by consent ID.  Without a scope, only the consent subject may retrieve the record. Returns 403 if the authenticated user is not the consent subject.  <b>Scope(Permission) required:</b> `internal_consent_mgt_consent_view` — Any consent record can be retrieved regardless of who the subject is. ", response = ConsentDTO.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Consents", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Consent record details", response = ConsentDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response consentsGet( @Size(min=1,max=255)@ApiParam(value = "ID of the consent.",required=true) @PathParam("consentId") String consentId) {

        return delegate.consentsGet(consentId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List consent records", notes = "Retrieve consent records with optional filtering.  Without a scope, returns only the authenticated user's own consents. The `subjectId` query parameter is ignored.  <b>Scope(Permission) required:</b> `internal_consent_mgt_consent_view` — Returns consents for any user. The `subjectId` parameter is applied as a filter. ", response = ConsentListResponse.class, authorizations = {
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
    public Response consentsList(    @Valid @Size(max=255)@ApiParam(value = "Filter by subject username.")  @QueryParam("subjectId") String subjectId,     @Valid @Size(max=255)@ApiParam(value = "Filter by service ID.")  @QueryParam("serviceId") String serviceId,     @Valid@ApiParam(value = "Filter consents by state.", allowableValues="PENDING, ACTIVE, REJECTED, REVOKED, EXPIRED")  @QueryParam("state") String state,     @Valid@ApiParam(value = "Filter consents by purpose ID.")  @QueryParam("purposeId") String purposeId,     @Valid@ApiParam(value = "Filter consents by specific purpose version ID.")  @QueryParam("purposeVersionId") String purposeVersionId,     @Valid@ApiParam(value = "Filter consents by custom properties using dot notation `properties.<key>`. Supports 'sw' (starts with), 'co' (contains), 'ew' (ends with), and 'eq' (equals) operations. Combine multiple conditions with 'and', 'or' logical operators. Examples: - filter=properties.dataCategory eq personal - filter=properties.region eq EU - filter=properties.region eq EU and properties.dataCategory eq personal ")  @QueryParam("filter") String filter,     @Valid @Min(1)@ApiParam(value = "Maximum number of records to return.", defaultValue="10") @DefaultValue("10")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Cursor for forward pagination. Pass the base64-encoded UUID of the last item received from the previous page to retrieve the next page of results. ")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Cursor for backward pagination. Pass the base64-encoded UUID of the first item received from the current page to retrieve the previous page of results. ")  @QueryParam("before") String before) {

        return delegate.consentsList(subjectId,  serviceId,  state,  purposeId,  purposeVersionId,  filter,  limit,  after,  before );
    }

    @Valid
    @POST
    @Path("/{consentId}/revoke")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Revoke a consent", notes = "Revokes the consent. The authenticated user must be the consent subject, returns 403 otherwise. Idempotent — if the consent is already revoked, returns 204 with no error. ", response = Void.class, authorizations = {
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
    @PATCH
    @Path("/{consentId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update a consent record", notes = "Update an existing consent record's expiry time, properties, and authorizations. All fields in the request body are optional; omit a field to leave that aspect of the consent unchanged. `expiryTime` sets or clears the expiry, `properties` replaces the full property map, and `authorizations` upserts the listed users — adding them as authorizers if absent or overriding their state if present.  <b>Scope(Permission) required:</b> `internal_consent_mgt_consent_update` — Updates any consent record regardless of who the subject is. ", response = ConsentDTO.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Consents", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Consent updated successfully", response = ConsentDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response consentsUpdate( @Size(min=1,max=255)@ApiParam(value = "ID of the consent.",required=true) @PathParam("consentId") String consentId, @ApiParam(value = "" ,required=true) @Valid ConsentUpdateRequest consentUpdateRequest) {

        return delegate.consentsUpdate(consentId,  consentUpdateRequest );
    }

    @Valid
    @GET
    @Path("/{consentId}/validate")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the current state of a consent object", notes = "Returns the current state of a consent record, performing a lazy expiry check. The authenticated user must be the consent subject, returns 403 otherwise. ", response = ConsentValidateResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Consents" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Validation result", response = ConsentValidateResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response consentsValidate( @Size(min=1,max=255)@ApiParam(value = "ID of the consent.",required=true) @PathParam("consentId") String consentId) {

        return delegate.consentsValidate(consentId );
    }

}
