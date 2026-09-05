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

package org.wso2.carbon.identity.api.server.device.policy.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.device.policy.api.service.DeviceFieldMetadataService;
import org.wso2.carbon.identity.rule.metadata.api.service.RuleMetadataService;

/**
 * Service holder for the device policy API — retrieves OSGi services from the Carbon context.
 */
public class DevicePolicyMetadataServiceHolder {

    private DevicePolicyMetadataServiceHolder() {}

    private static class RuleMetadataServiceHolder {

        static final RuleMetadataService SERVICE = (RuleMetadataService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(RuleMetadataService.class, null);
    }

    private static class DeviceFieldMetadataServiceHolder {

        static final DeviceFieldMetadataService SERVICE = (DeviceFieldMetadataService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(DeviceFieldMetadataService.class, null);
    }

    /**
     * Returns the RuleMetadataService OSGi service.
     */
    public static RuleMetadataService getRuleMetadataService() {

        return RuleMetadataServiceHolder.SERVICE;
    }

    /**
     * Returns the DeviceFieldMetadataService OSGi service.
     */
    public static DeviceFieldMetadataService getDeviceFieldMetadataService() {

        return DeviceFieldMetadataServiceHolder.SERVICE;
    }
}
