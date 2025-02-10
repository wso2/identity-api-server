/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.tenant.management.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.wso2.carbon.identity.api.server.tenant.management.v1.factories.ChannelVerifiedTenantsApiServiceFactory;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.ChannelVerifiedTenantModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.Error;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/channel-verified-tenants")
@Api(description = "The channel-verified-tenants API")

public class ChannelVerifiedTenantsApi  {

    private final ChannelVerifiedTenantsApiService delegate;

    public ChannelVerifiedTenantsApi() {

        this.delegate = ChannelVerifiedTenantsApiServiceFactory.getChannelVerifiedTenantsApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add tenant.", notes = "This API provides the capability to create new channel verified tenants.  <b>Permission required:</b> * /permission/protected/manage/monitor/tenants/list  <b>scope required:</b> * internal_list_tenants  <b> cURL</b> curl -k -v -X POST -H \"Authorization: Basic YWRtaW5Ad3NvMi5jb206YWRtaW4=\" -H \"Content-Type: application/json\" -d '{ \"domain\":\"lanka.com\", \"purpose\":\"Personal\", \"code\":\"2d962a0b-9280-4394-8652-0137a25d9bac\", \"owners\":[ { \"username\":\"lanka@wso2.com\", \"email\":\"lanka@wso2.com\",\"password\":\"Pass#word2\", \"firstname\":\"Lanka\", \"lastname\":\"Jaya\", \"provisioningMethod\":\"verified-lite-user\", \"additionalClaims\":[ { \"claim\":\"http://wso2.org/claims/telephone\", \"value\":\"+94 562 8723\" } ] } ] }' \"https://localhost:9443/api/server/v1/channel-verified-tenants\" ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Tenants" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Item Created", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Element Already Exists", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response addChannelVerifiedTenant(@ApiParam(value = "This represents the tenant to be created." ,required=true) @Valid ChannelVerifiedTenantModel channelVerifiedTenantModel) {

        return delegate.addChannelVerifiedTenant(channelVerifiedTenantModel );
    }

}
