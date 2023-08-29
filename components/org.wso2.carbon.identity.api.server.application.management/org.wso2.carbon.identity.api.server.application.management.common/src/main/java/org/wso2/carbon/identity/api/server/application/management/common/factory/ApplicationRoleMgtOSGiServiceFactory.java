/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.role.mgt.ApplicationRoleManager;
/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the Application role Manager type of object inside the container.
 */
public class ApplicationRoleMgtOSGiServiceFactory extends AbstractFactoryBean<ApplicationRoleManager> {

    private static ApplicationRoleManager applicationRoleMgtService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected ApplicationRoleManager createInstance() throws Exception {

        if (applicationRoleMgtService == null) {
            ApplicationRoleManager taskOperationService = (ApplicationRoleManager) PrivilegedCarbonContext.
                    getThreadLocalCarbonContext().getOSGiService(ApplicationRoleManager.class, null);

            if (taskOperationService == null) {
                throw new Exception("Unable to retrieve application role management service.");
            }
            applicationRoleMgtService = taskOperationService;
        }
        return applicationRoleMgtService;
    }
}
