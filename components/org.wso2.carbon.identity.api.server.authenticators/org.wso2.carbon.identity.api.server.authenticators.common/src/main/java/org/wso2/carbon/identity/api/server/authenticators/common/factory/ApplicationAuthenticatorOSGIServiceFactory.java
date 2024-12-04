/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.authenticators.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.common.ApplicationAuthenticatorService;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the ApplicationAuthenticatorService type of object inside the container.
 */
public class ApplicationAuthenticatorOSGIServiceFactory extends AbstractFactoryBean<ApplicationAuthenticatorService> {

    private ApplicationAuthenticatorService applicationAuthenticatorService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected ApplicationAuthenticatorService createInstance() throws Exception {

        if (this.applicationAuthenticatorService == null) {
            ApplicationAuthenticatorService taskOperationService = (ApplicationAuthenticatorService)
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getOSGiService(
                            ApplicationAuthenticatorService.class, null);
            if (taskOperationService != null) {
                this.applicationAuthenticatorService = taskOperationService;
            } else {
                throw new Exception("Unable to retrieve ApplicationAuthenticatorService service.");
            }
        }
        return this.applicationAuthenticatorService;
    }
}
