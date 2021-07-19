/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.endpoint.configuration.management.common;

import org.wso2.carbon.identity.configuration.mgt.core.ConfigurationManager;

/**
 * Service holder class for endpoint configuration management.
 */
public class EndpointConfigurationManagementDataHolder {

    private static ConfigurationManager configurationConfigManager;

    /**
     * Get ConfigurationManager OSGi service.
     *
     * @return NotificationSenderConfig Manager.
     */
    public static ConfigurationManager getConfigurationConfigManager() {

        return configurationConfigManager;
    }

    /**
     * Set ConfigurationManager OSGi service.
     *
     * @param configurationConfigManager Configuration Manager.
     */
    public static void setConfigurationConfigManager(ConfigurationManager configurationConfigManager) {

        EndpointConfigurationManagementDataHolder.configurationConfigManager = configurationConfigManager;
    }

}
