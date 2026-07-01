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

import static org.wso2.carbon.identity.notification.push.device.handler.constant.PushDeviceHandlerConstants.ATTR_ENABLE_DEVICE_MANAGEMENT;
import static org.wso2.carbon.identity.notification.push.device.handler.constant.PushDeviceHandlerConstants.ATTR_MAX_DEVICE_LIMIT;
import static org.wso2.carbon.identity.notification.push.device.handler.constant.PushDeviceHandlerConstants.RESOURCE_NAME;
import static org.wso2.carbon.identity.notification.push.device.handler.constant.PushDeviceHandlerConstants.RESOURCE_TYPE;

/**
 * Allowlist for the POST /configs/preferences endpoint.
 * Each constant declares one config store resource exposed via the endpoint and the attribute names
 * permitted for it. An empty attribute list means all attributes of the resource are permitted.
 * To expose a new config store resource via the endpoint, add a new constant.
 */
public enum ConfigPreferencesAllowlist {

    // Push authentication device management configuration.
    PUSH_DEVICE_MGT_CONFIG(RESOURCE_TYPE, RESOURCE_NAME,
            ATTR_ENABLE_DEVICE_MANAGEMENT, ATTR_MAX_DEVICE_LIMIT);

    private final String resourceType;
    private final String resourceName;
    private final Set<String> allowedAttributes;

    ConfigPreferencesAllowlist(String resourceType, String resourceName, String... allowedAttributes) {

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

        for (ConfigPreferencesAllowlist entry : values()) {
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

        return findEntry(resourceType, resourceName) != null;
    }

    /**
     * Check whether the given attribute is allowed for the given resource type and resource name.
     *
     * @param resourceType  Config store resource type.
     * @param resourceName  Config store resource name.
     * @param attributeName Attribute name.
     * @return True if the attribute is in the allowlist, or if the resource permits all attributes.
     */
    public static boolean isAttributeAllowed(String resourceType, String resourceName, String attributeName) {

        ConfigPreferencesAllowlist entry = findEntry(resourceType, resourceName);
        if (entry == null) {
            return false;
        }
        // An empty set means all attributes of the resource are permitted.
        return entry.allowedAttributes.isEmpty() || entry.allowedAttributes.contains(attributeName);
    }

    private static ConfigPreferencesAllowlist findEntry(String resourceType, String resourceName) {

        for (ConfigPreferencesAllowlist entry : values()) {
            if (entry.resourceType.equals(resourceType) && entry.resourceName.equals(resourceName)) {
                return entry;
            }
        }
        return null;
    }
}
