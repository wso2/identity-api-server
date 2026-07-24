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

package org.wso2.carbon.identity.api.server.device.mgt.v1.function;

import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DeviceResponse;
import org.wso2.carbon.identity.device.mgt.api.model.Device;

/**
 * Builds a DeviceResponse (API model) from a Device (domain model).
 *
 * Used after every successful operation to build the JSON response
 * that gets sent back to the client.
 */
public class DeviceResponseBuilder {

    private DeviceResponseBuilder() {

    }

    public static DeviceResponse buildDeviceResponse(Device device) {

        DeviceResponse response = new DeviceResponse();
        response.setId(device.getId());
        response.setUserId(device.getUserId());
        response.setDeviceName(device.getDeviceName());
        response.setDeviceModel(device.getDeviceModel());
        if (device.getStatus() != null) {
            response.setStatus(device.getStatus().name());
        }
        if (device.getRegisteredAt() != null) {
            response.setRegisteredAt(device.getRegisteredAt().toInstant().toString());
        }
        response.setMetadata(device.getMetadata());
        return response;
    }
}
