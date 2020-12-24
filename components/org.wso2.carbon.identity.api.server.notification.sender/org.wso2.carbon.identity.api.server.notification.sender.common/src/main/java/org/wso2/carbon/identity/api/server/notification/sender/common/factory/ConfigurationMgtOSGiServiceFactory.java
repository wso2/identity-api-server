/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.notification.sender.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.configuration.mgt.core.ConfigurationManager;

/**
 * Factory Beans serve as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the ConfigurationManager type of object inside the container.
 */
public class ConfigurationMgtOSGiServiceFactory extends AbstractFactoryBean<ConfigurationManager> {

    private ConfigurationManager configurationManager;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected ConfigurationManager createInstance() throws Exception {

        if (this.configurationManager == null) {
            ConfigurationManager taskOperationService = (ConfigurationManager) PrivilegedCarbonContext.
                    getThreadLocalCarbonContext().getOSGiService(ConfigurationManager.class, null);

            if (taskOperationService != null) {
                this.configurationManager = taskOperationService;
            } else {
                throw new Exception("Unable to retrieve ConfigurationManager service.");
            }
        }
        return this.configurationManager;
    }
}
