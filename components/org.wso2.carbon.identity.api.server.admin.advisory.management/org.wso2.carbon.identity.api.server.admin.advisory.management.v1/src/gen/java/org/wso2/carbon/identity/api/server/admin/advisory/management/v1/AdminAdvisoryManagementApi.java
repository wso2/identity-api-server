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

package org.wso2.carbon.identity.api.server.admin.advisory.management.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.wso2.carbon.identity.api.server.admin.advisory.management.v1.factories.AdminAdvisoryManagementApiServiceFactory;
import org.wso2.carbon.identity.api.server.admin.advisory.management.v1.model.AdminAdvisoryConfig;
import org.wso2.carbon.identity.api.server.admin.advisory.management.v1.model.Error;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import io.swagger.annotations.ApiParam;

/**
 * Admin Advisory Management API.
 **/
@Path("/admin-advisory-management")
@Api(description = "The admin-advisory-management API")

public class AdminAdvisoryManagementApi  {

    private final AdminAdvisoryManagementApiService delegate;

    public AdminAdvisoryManagementApi() {

        this.delegate = AdminAdvisoryManagementApiServiceFactory.getAdminAdvisoryManagementApi();
    }

    @Valid
    @GET
    @Path("/banner")
    
    @Produces({ "application/json", "*/*" })
    @ApiOperation(value = "Retrieve admin advisory banner related configurations.", notes = "Retrieve admin advisory banner related configurations.<br>  <b>Permission required:</b> <br>     * None <br>   <b>Scope required:</b> <br>     * None ", response = AdminAdvisoryConfig.class, tags={ "Management" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Admin advisory banner configuration.", response = AdminAdvisoryConfig.class),
            @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getAdminAdvisoryConfig() {

        return delegate.getAdminAdvisoryConfig();
    }

    @Valid
    @PATCH
    @Path("/banner")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "*/*" })
    @ApiOperation(value = "Update admin advisory banner related configurations.", notes = "Update admin advisory banner related configurations.<br>  <b>Permission required:</b> <br>     * None <br>   <b>Scope required:</b> <br>     * None     requestBody: ", response = AdminAdvisoryConfig.class, tags={ "Management" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Admin advisory banner configuration.", response = AdminAdvisoryConfig.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response updateAdminAdvisoryConfig(@ApiParam(value = "" ,required=true) @Valid AdminAdvisoryConfig adminAdvisoryConfig) {

        return delegate.updateAdminAdvisoryConfig(adminAdvisoryConfig );
    }

}
