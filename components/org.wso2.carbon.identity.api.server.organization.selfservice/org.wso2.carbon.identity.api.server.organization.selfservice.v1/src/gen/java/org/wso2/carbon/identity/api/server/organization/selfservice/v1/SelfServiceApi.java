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

package org.wso2.carbon.identity.api.server.organization.selfservice.v1;

import org.springframework.beans.factory.annotation.Autowired;

import org.wso2.carbon.identity.api.server.organization.selfservice.v1.model.PropertyPatchReq;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.model.PropertyRes;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/self-service")
@Api(description = "The self-service API")

public class SelfServiceApi  {

    @Autowired
    private SelfServiceApiService delegate;

    @Valid
    @GET
    @Path("/preferences")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get is supported only for auto login & enable/disable self service.", notes = "This API provides the capability to retrieve properties related to self service", response = PropertyRes.class, responseContainer = "List", authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "BearerAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags={ "SelfService", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Requested self service properties.", response = PropertyRes.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
            @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
            @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
            @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationPreferenceGet() {

        return delegate.organizationPreferenceGet();
    }

    @Valid
    @PATCH
    @Path("/preferences")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch is supported only self service governance configs.", notes = "This API provides the capability to update properties related to self service for an organization", response = Void.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "BearerAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags={ "SelfService" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK.", response = Void.class),
            @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
            @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
            @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
            @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationPreferencePatch(@ApiParam(value = "" ,required=true) @Valid PropertyPatchReq propertyPatchReq) {

        return delegate.organizationPreferencePatch(propertyPatchReq );
    }

}
