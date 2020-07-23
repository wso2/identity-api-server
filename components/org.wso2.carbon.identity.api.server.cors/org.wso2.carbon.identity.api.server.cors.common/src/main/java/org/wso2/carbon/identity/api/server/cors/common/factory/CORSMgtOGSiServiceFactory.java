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

package org.wso2.carbon.identity.api.server.cors.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.cors.mgt.core.CORSManagementService;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the CORSManager type of object inside the container.
 */
public class CORSMgtOGSiServiceFactory extends AbstractFactoryBean<CORSManagementService> {

    private CORSManagementService corsManagementService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected CORSManagementService createInstance() throws Exception {

        if (this.corsManagementService == null) {
            CORSManagementService taskOperationService = (CORSManagementService) PrivilegedCarbonContext.
                    getThreadLocalCarbonContext().getOSGiService(CORSManagementService.class, null);
            if (taskOperationService != null) {
                this.corsManagementService = taskOperationService;
            } else {
                throw new Exception("Unable to retrieve corsManagementService service.");
            }
        }
        return this.corsManagementService;
    }
}
