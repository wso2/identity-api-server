/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.idp.v1;

import java.util.List;

import org.wso2.carbon.identity.api.server.idp.v1.factories.TrustedTokenIssuersApiServiceFactory;
import org.wso2.carbon.identity.api.server.idp.v1.model.Error;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderListResponse;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.Patch;
import org.wso2.carbon.identity.api.server.idp.v1.model.TrustedTokenIssuerPOSTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.TrustedTokenIssuerResponse;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;
import org.wso2.carbon.identity.api.server.idp.v1.model.Error;

@Path("/trusted-token-issuers")
@Api(description = "The trusted-token-issuers API")

public class TrustedTokenIssuersApi  {

    private final TrustedTokenIssuersApiService delegate;

    public TrustedTokenIssuersApi() {

        this.delegate = TrustedTokenIssuersApiServiceFactory.getTrustedTokenIssuersApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Add a new trusted token issuer ", notes = "This API provides the capability to create a token issuer. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/create <br> <b>Scope required:</b> <br>     * internal_idp_create ", response = TrustedTokenIssuerResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Trusted Token Issuers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = TrustedTokenIssuerResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response addTrustedTokenIssuer(@ApiParam(value = "This represents the trusted Token issuer to be created." ,required=true) @Valid TrustedTokenIssuerPOSTRequest trustedTokenIssuerPOSTRequest) {

        return delegate.addTrustedTokenIssuer(trustedTokenIssuerPOSTRequest );
    }

    @Valid
    @DELETE
    @Path("/{trusted-token-issuer-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a trusted Token issuer by using the trusted token issuer's ID. ", notes = "This API provides the capability to delete a trusted Token issuer by giving its ID. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/delete <br> <b>Scope required:</b> <br>     * internal_idp_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Trusted Token Issuers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteTrustedTokenIssuer(@ApiParam(value = "ID of the trusted Token issuer",required=true) @PathParam("trusted-token-issuer-id") String trustedTokenIssuerId,     @Valid@ApiParam(value = "Enforces the forceful deletion of an identity provider, federated authenticator or an outbound provisioning connector even though it is referred by a service provider. ", defaultValue="false") @DefaultValue("false")  @QueryParam("force") Boolean force) {

        return delegate.deleteTrustedTokenIssuer(trustedTokenIssuerId,  force );
    }

    @Valid
    @GET
    @Path("/{trusted-token-issuer-id}")
    
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Retrieve identity provider by trusted token issuer's ID ", notes = "This API provides the capability to retrieve the trusted Token issuer details by using its ID.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = TrustedTokenIssuerResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Trusted Token Issuers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = TrustedTokenIssuerResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getTrustedTokenIssuer(@ApiParam(value = "ID of the trusted Token issuer.",required=true) @PathParam("trusted-token-issuer-id") String trustedTokenIssuerId) {

        return delegate.getTrustedTokenIssuer(trustedTokenIssuerId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List Trusted Token issuers ", notes = "This API provides the capability to retrieve the list of token issuers.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = IdentityProviderListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Trusted Token Issuers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = IdentityProviderListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getTrustedTokenIssuers(    @Valid@ApiParam(value = "Maximum number of records to return. ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination. ")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations and also complex queries with 'and' operations. E.g. /identity-providers?filter=name+sw+\"google\"+and+isEnabled+eq+\"true\" ")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Attribute by which the retrieved records should be sorted. Currently sorting through _<b>domainName<b>_ only supported.")  @QueryParam("sortBy") String sortBy,     @Valid@ApiParam(value = "Define the order in which the retrieved tenants should be sorted.", allowableValues="asc, desc")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Specifies the required parameters in the response. ")  @QueryParam("requiredAttributes") String requiredAttributes) {

        return delegate.getTrustedTokenIssuers(limit,  offset,  filter,  sortBy,  sortOrder,  requiredAttributes );
    }

    @Valid
    @PATCH
    @Path("/{trusted-token-issuer-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch a trusted Token issuer property by ID. Patch is supported only for key-value pairs ", notes = "This API provides the capability to update a trusted Token issuer property using patch request. Trusted Token issuer patch is supported only for key-value pairs. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/update <br> <b>Scope required:</b> <br>     * internal_idp_update ", response = TrustedTokenIssuerResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Trusted Token Issuers" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = TrustedTokenIssuerResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchTrustedTokenIssuer(@ApiParam(value = "ID of the trusted Token issuer.",required=true) @PathParam("trusted-token-issuer-id") String trustedTokenIssuerId, @ApiParam(value = "" ,required=true) @Valid List<Patch> patch) {

        return delegate.patchTrustedTokenIssuer(trustedTokenIssuerId,  patch );
    }

}
