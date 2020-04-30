/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.api.server.application.management.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.security.sts.service.PassiveSTSServiceInterface;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the PassiveSTSServiceImpl type of object inside the container.
 */
public class PassiveSTSOSGiServiceFactory extends AbstractFactoryBean<PassiveSTSServiceInterface> {

    private PassiveSTSServiceInterface passiveSTSService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected PassiveSTSServiceInterface createInstance() throws Exception {

        if (this.passiveSTSService == null) {
            /* Try catch statement is included due to a NullPointerException which occurs at the server
               startup when the Passive STS functionality is not available in the product. */
            try {
                PassiveSTSServiceInterface passiveSTSService =
                        (PassiveSTSServiceInterface) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                                .getOSGiService(PassiveSTSServiceInterface.class, null);

                if (passiveSTSService != null) {
                    this.passiveSTSService = passiveSTSService;
                } else {
                    throw new Exception("Unable to retrieve PassiveSTSService.");
                }
            } catch (NullPointerException e) {
                /* Catch block without implementation so that the Passive STS Service will be set to null
                   in-turn helps in validating the rest API requests. */
            }
        }
        return this.passiveSTSService;
    }

}
