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

package org.wso2.carbon.identity.api.server.device.policy.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.device.policy.v1.model.DevicePolicyFieldDefinition;
import org.wso2.carbon.identity.api.server.device.policy.v1.model.Error;
import org.wso2.carbon.identity.api.server.device.policy.v1.DevicePoliciesApiService;
import org.wso2.carbon.identity.api.server.device.policy.v1.factories.DevicePoliciesApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/device-policies")
@Api(description = "The device-policies API")

public class DevicePoliciesApi  {

    private final DevicePoliciesApiService delegate;

    public DevicePoliciesApi() {

        this.delegate = DevicePoliciesApiServiceFactory.getDevicePoliciesApi();
    }

    @Valid
    @GET
    @Path("/metadata")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get device policy field metadata filtered by platform.", notes = "Returns the list of rule fields applicable for the devicePolicy flow, optionally filtered to a specific platform. Used by the UI to populate the rule builder with platform-relevant fields only. ", response = DevicePolicyFieldDefinition.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Device Policy Management" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Applicable fields for the given platform.", response = DevicePolicyFieldDefinition.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getDevicePolicyMetadata(    @Valid@ApiParam(value = "Filter fields to those applicable for this platform. If omitted all fields are returned.", allowableValues="android, ios, macos, windows")  @QueryParam("platform") String platform) {

        return delegate.getDevicePolicyMetadata(platform );
    }

}
