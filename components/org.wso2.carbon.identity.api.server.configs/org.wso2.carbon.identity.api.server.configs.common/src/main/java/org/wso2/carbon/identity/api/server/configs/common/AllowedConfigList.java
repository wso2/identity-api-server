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

package org.wso2.carbon.identity.api.server.configs.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.wso2.carbon.identity.notification.push.device.handler.constant.PushDeviceHandlerConstants.ATTR_ENABLE_MULTIPLE_DEVICE_ENROLLMENT;
import static org.wso2.carbon.identity.notification.push.device.handler.constant.PushDeviceHandlerConstants.ATTR_MAX_DEVICE_LIMIT;
import static org.wso2.carbon.identity.notification.push.device.handler.constant.PushDeviceHandlerConstants.PUSH_DEVICE_MGT_RESOURCE_NAME;
import static org.wso2.carbon.identity.notification.push.device.handler.constant.PushDeviceHandlerConstants.PUSH_DEVICE_MGT_RESOURCE_TYPE;

/**
 * Allowed ConfigList for the config preferences endpoint.
 */
public enum AllowedConfigList {

    // Push authentication device management configuration.
    PUSH_DEVICE_MGT_CONFIG(PUSH_DEVICE_MGT_RESOURCE_TYPE, PUSH_DEVICE_MGT_RESOURCE_NAME,
            ATTR_ENABLE_MULTIPLE_DEVICE_ENROLLMENT, ATTR_MAX_DEVICE_LIMIT);

    private final String resourceType;
    private final String resourceName;
    private final Set<String> allowedAttributes;

    AllowedConfigList(String resourceType, String resourceName, String... allowedAttributes) {

        this.resourceType = resourceType;
        this.resourceName = resourceName;
        this.allowedAttributes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(allowedAttributes)));
    }

    /**
     * Check whether the given resource type is allowed via the config preferences endpoint.
     *
     * @param resourceType Config store resource type.
     * @return True if the resource type is in the allowlist.
     */
    public static boolean isResourceTypeAllowed(String resourceType) {

        for (AllowedConfigList entry : values()) {
            if (entry.resourceType.equals(resourceType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given resource name is allowed for the given resource type.
     *
     * @param resourceType Config store resource type.
     * @param resourceName Config store resource name.
     * @return True if the resource name is in the allowlist for the resource type.
     */
    public static boolean isResourceNameAllowed(String resourceType, String resourceName) {

        return getAllowedConfigs(resourceType, resourceName) != null;
    }

    /**
     * Check whether the given attribute is allowed for the given resource type and resource name.
     *
     * @param resourceType  Config store resource type.
     * @param resourceName  Config store resource name.
     * @param attributeName Attribute name.
     * @return True if the attribute is in the allowlist.
     */
    public static boolean isAttributeAllowed(String resourceType, String resourceName, String attributeName) {

        AllowedConfigList entry = getAllowedConfigs(resourceType, resourceName);
        if (entry == null) {
            return false;
        }
        return entry.allowedAttributes.contains(attributeName);
    }

    private static AllowedConfigList getAllowedConfigs(String resourceType, String resourceName) {

        for (AllowedConfigList entry : values()) {
            if (entry.resourceType.equals(resourceType) && entry.resourceName.equals(resourceName)) {
                return entry;
            }
        }
        return null;
    }
}
