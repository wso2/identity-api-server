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

package org.wso2.carbon.identity.api.server.api.resource.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.api.resource.v1.Error;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopeGetModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopesApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/scopes")
@Api(description = "The scopes API")

public class ScopesApi  {

    @Autowired
    private ScopesApiService delegate;

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get all scopes in the tenant", notes = "Get all scopes in the tenant <b>Permission required:</b> <br>   * /permission/admin/manage/identity/apiresourcemgt/view <br> <b>Scope required:</b> <br>   * internal_api_resource_view ", response = ScopeGetModel.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "API Resource Scopes" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ScopeGetModel.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response scopesGet(    @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations. ")  @QueryParam("filter") String filter) {

        return delegate.scopesGet(filter );
    }

}
