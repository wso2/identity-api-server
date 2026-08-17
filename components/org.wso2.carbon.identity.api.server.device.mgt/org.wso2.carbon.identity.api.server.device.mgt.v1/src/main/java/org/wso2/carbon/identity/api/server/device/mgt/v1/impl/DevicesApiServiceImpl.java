/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.device.mgt.v1.impl;

import org.wso2.carbon.identity.api.server.device.mgt.v1.DevicesApiService;
import org.wso2.carbon.identity.api.server.device.mgt.v1.core.DeviceManagementApiService;
import org.wso2.carbon.identity.api.server.device.mgt.v1.factories.DeviceServiceFactory;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DevicePatchRequest;

import javax.ws.rs.core.Response;

/**
 * JAX-RS delegate implementation — bridges generated DevicesApiService to the core service.
 */
public class DevicesApiServiceImpl implements DevicesApiService {

    private final DeviceManagementApiService service;

    public DevicesApiServiceImpl() {

        this.service = DeviceServiceFactory.getDeviceManagementApiService();
    }

    @Override
    public Response listDevices(Integer limit, Integer offset, String userId) {

        return Response.ok().entity(service.listDevices(limit, offset, userId)).build();
    }

    @Override
    public Response getDevice(String deviceId) {

        return Response.ok().entity(service.getDevice(deviceId)).build();
    }

    @Override
    public Response updateDeviceName(String deviceId, DevicePatchRequest patchRequest) {

        return Response.ok().entity(service.updateDeviceName(deviceId, patchRequest)).build();
    }

    @Override
    public Response deleteDevice(String deviceId) {

        service.deleteDevice(deviceId);
        return Response.noContent().build();
    }
}
