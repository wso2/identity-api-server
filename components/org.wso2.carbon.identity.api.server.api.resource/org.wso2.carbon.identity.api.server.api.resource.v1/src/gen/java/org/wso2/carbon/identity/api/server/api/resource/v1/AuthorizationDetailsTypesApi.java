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

package org.wso2.carbon.identity.api.server.api.resource.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesGetModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.Error;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;
import org.wso2.carbon.identity.api.server.api.resource.v1.factories.AuthorizationDetailsTypesApiServiceFactory;

import javax.validation.constraints.*;

@Path("/authorization-details-types")
@Api(description = "The authorization-details-types API")

public class AuthorizationDetailsTypesApi  {

    private final AuthorizationDetailsTypesApiService delegate;

    public AuthorizationDetailsTypesApi() {

        this.delegate = AuthorizationDetailsTypesApiServiceFactory.getAuthorizationDetailsTypesApi();
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Gets all authorization details types in the tenant", notes = "Get all authorization details types in the tenant <b>Permission required:</b> <br>   * /permission/admin/manage/identity/apiresourcemgt/view <br> <b>Scope required:</b> <br>   * internal_api_resource_view ", response = AuthorizationDetailsTypesGetModel.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resource Authorization Details Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuthorizationDetailsTypesGetModel.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response authorizationDetailsTypesGet(    @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations. ")  @QueryParam("filter") String filter) {

        return delegate.authorizationDetailsTypesGet(filter );
    }

    @Valid
    @HEAD
    
    
    
    @ApiOperation(value = "Checks an authorization details type existence by type in the tenant", notes = "This API is used to check a registered authorization details type's existence using a given type in the tenant.  <b>Permission required:</b>     * /permission/admin/manage/identity/apiresourcemgt/view    <b>Scope required:</b>     * internal_api_resource_view ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resource Authorization Details Types" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Provided authorization details type exists", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized request", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found", response = Void.class),
        @ApiResponse(code = 500, message = "Encountered a server error", response = Void.class)
    })
    public Response isAuthorizationDetailsTypeExists(    @Valid @NotNull(message = "Property  cannot be null.") @ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations. ",required=true)  @QueryParam("filter") String filter) {

        return delegate.isAuthorizationDetailsTypeExists(filter );
    }

}
