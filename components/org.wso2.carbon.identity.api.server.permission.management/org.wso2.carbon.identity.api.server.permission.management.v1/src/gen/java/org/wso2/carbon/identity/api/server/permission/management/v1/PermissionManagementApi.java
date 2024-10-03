/*
* Copyright (c) 2019-2024, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.permission.management.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.wso2.carbon.identity.api.server.permission.management.v1.impl.PermissionManagementApiServiceImpl;
import org.wso2.carbon.identity.api.server.permission.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.permission.management.v1.model.Permission;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/permission-management")
@Api(description = "The permission-management API")

public class PermissionManagementApi  {

    private final PermissionManagementApiService delegate;

    public PermissionManagementApi() {
        this.delegate = new PermissionManagementApiServiceImpl();
    }

    @Valid
    @GET
    @Path("/permissions")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List permissions in the permission tree ", notes = "This API provides the array list of permissions in the UI permission tree. ", response = Permission.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "permissions" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Permission.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response permissionManagementPermissionsGet() {

        return delegate.permissionManagementPermissionsGet();
    }

}
