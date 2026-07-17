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

package org.wso2.carbon.identity.api.server.device.mgt.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.device.mgt.api.service.DeviceManagementService;

/**
 * Service holder for device management API — retrieves OSGi services from the Carbon context.
 */
public class DeviceMgtServiceHolder {

    private DeviceMgtServiceHolder() {
    }

    /**
     * Returns the DeviceManagementService OSGi service.
     *
     * @return DeviceManagementService instance.
     */
    public static DeviceManagementService getDeviceManagementService() {

        return (DeviceManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(DeviceManagementService.class, null);
    }
}
