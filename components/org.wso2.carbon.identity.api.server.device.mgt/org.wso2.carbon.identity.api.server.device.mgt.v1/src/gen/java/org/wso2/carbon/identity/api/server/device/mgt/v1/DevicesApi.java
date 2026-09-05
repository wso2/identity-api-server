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

package org.wso2.carbon.identity.api.server.device.mgt.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DeviceListResponse;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DevicePatchRequest;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DeviceResponse;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.Error;
import org.wso2.carbon.identity.api.server.device.mgt.v1.DevicesApiService;
import org.wso2.carbon.identity.api.server.device.mgt.v1.factories.DevicesApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/devices")
@Api(description = "The devices API")

public class DevicesApi  {

    private final DevicesApiService delegate;

    public DevicesApi() {

        this.delegate = DevicesApiServiceFactory.getDevicesApi();
    }

    @Valid
    @DELETE
    @Path("/{device-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a registered device.", notes = "This API provides the capability to delete a registered device by its ID.   <b>Scope (Permission) required:</b> ``internal_device_mgt_delete``  ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Device Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteDevice(@ApiParam(value = "UUID of the registered device",required=true) @PathParam("device-id") String deviceId) {

        return delegate.deleteDevice(deviceId );
    }

    @Valid
    @GET
    @Path("/{device-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a registered device by ID.", notes = "This API provides the capability to retrieve a registered device by its ID.   <b>Scope (Permission) required:</b> ``internal_device_mgt_view``  ", response = DeviceResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Device Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = DeviceResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getDevice(@ApiParam(value = "UUID of the registered device",required=true) @PathParam("device-id") String deviceId) {

        return delegate.getDevice(deviceId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List all registered devices in the tenant.", notes = "This API provides the capability to list a paginated set of registered devices for the tenant.   <b>Scope (Permission) required:</b> ``internal_device_mgt_view``  ", response = DeviceListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Device Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = DeviceListResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response listDevices(    @Valid @Min(1)@ApiParam(value = "Maximum number of records to return.", defaultValue="30") @DefaultValue("30")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination.", defaultValue="0") @DefaultValue("0")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Filter devices by the ID of the user who registered them. Returns devices of any status. Pagination applies as usual.")  @QueryParam("userId") String userId) {

        return delegate.listDevices(limit,  offset,  userId );
    }

    @Valid
    @PATCH
    @Path("/{device-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Rename a registered device.", notes = "This API provides the capability to update the display name of a registered device.   <b>Scope (Permission) required:</b> ``internal_device_mgt_update``  ", response = DeviceResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Device Management" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = DeviceResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateDeviceName(@ApiParam(value = "UUID of the registered device",required=true) @PathParam("device-id") String deviceId, @ApiParam(value = "" ,required=true) @Valid DevicePatchRequest devicePatchRequest) {

        return delegate.updateDeviceName(deviceId,  devicePatchRequest );
    }

}
