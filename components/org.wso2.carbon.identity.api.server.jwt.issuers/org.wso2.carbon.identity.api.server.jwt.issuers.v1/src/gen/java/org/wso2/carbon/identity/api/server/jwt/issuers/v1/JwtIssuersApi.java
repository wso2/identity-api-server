/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.jwt.issuers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.jwt.issuers.v1.model.Error;
import org.wso2.carbon.identity.api.server.jwt.issuers.v1.model.IdentityProviderListResponse;
import org.wso2.carbon.identity.api.server.jwt.issuers.v1.model.IdentityProviderResponse;
import org.wso2.carbon.identity.api.server.jwt.issuers.v1.model.JWTIssuerPOSTRequest;
import org.wso2.carbon.identity.api.server.jwt.issuers.v1.JwtIssuersApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/jwt-issuers")
@Api(description = "The jwt-issuers API")

public class JwtIssuersApi  {

    @Autowired
    private JwtIssuersApiService delegate;

    @Valid
    @POST
    
    @Consumes({ "application/json", "application/xml" })
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Add a new jwt issuer ", notes = "This API provides the capability to create a jwt issuer. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/create <br> <b>Scope required:</b> <br>     * internal_idp_create ", response = IdentityProviderResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "JWT Issuers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = IdentityProviderResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response addJWTIssuer(@ApiParam(value = "This represents the JWT issuer to be created." ,required=true) @Valid JWTIssuerPOSTRequest jwTIssuerPOSTRequest) {

        return delegate.addJWTIssuer(jwTIssuerPOSTRequest );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List JWT issuers ", notes = "This API provides the capability to retrieve the list of jwt issuers.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = IdentityProviderListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "JWT Issuers" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = IdentityProviderListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response getJwtIssuers(    @Valid@ApiParam(value = "Maximum number of records to return. ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination. ")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations and also complex queries with 'and' operations. E.g. /identity-providers?filter=name+sw+\"google\"+and+isEnabled+eq+\"true\" ")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Attribute by which the retrieved records should be sorted. Currently sorting through _<b>domainName<b>_ only supported.")  @QueryParam("sortBy") String sortBy,     @Valid@ApiParam(value = "Define the order in which the retrieved tenants should be sorted.", allowableValues="asc, desc")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Specifies the required parameters in the response. ")  @QueryParam("requiredAttributes") String requiredAttributes) {

        return delegate.getJwtIssuers(limit,  offset,  filter,  sortBy,  sortOrder,  requiredAttributes );
    }

}
