/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.configs.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.oauth2.impersonation.services.ImpersonationConfigMgtService;

/**
 * Factory bean for creating instances of ImpersonationConfigMgtService.
 */
public class ImpersonationMgtOGSiServiceFactory extends AbstractFactoryBean<ImpersonationConfigMgtService> {

    private ImpersonationConfigMgtService impersonationConfigMgtService;

    /**
     * Specifies the type of object that this FactoryBean creates.
     *
     * @return The type of object created by this FactoryBean.
     */
    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    /**
     * Creates an instance of ImpersonationConfigMgtService.
     *
     * @return The created ImpersonationConfigMgtService instance.
     * @throws Exception If there is an error while creating the instance.
     */
    @Override
    protected ImpersonationConfigMgtService createInstance() throws Exception {
        // Check if the service instance has already been created.
        if (this.impersonationConfigMgtService == null) {
            // Attempt to retrieve the ImpersonationConfigMgtService instance from OSGi services.
            ImpersonationConfigMgtService taskOperationService =
                    (ImpersonationConfigMgtService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                            .getOSGiService(ImpersonationConfigMgtService.class, null);
            // If the service instance is retrieved successfully, store it in the instance variable.
            if (taskOperationService != null) {
                this.impersonationConfigMgtService = taskOperationService;
            } else {
                // If the service instance cannot be retrieved, throw an exception.
                throw new Exception("Unable to retrieve ImpersonationConfigMgtService service.");
            }
        }
        // Return the stored ImpersonationConfigMgtService instance.
        return this.impersonationConfigMgtService;
    }
}
