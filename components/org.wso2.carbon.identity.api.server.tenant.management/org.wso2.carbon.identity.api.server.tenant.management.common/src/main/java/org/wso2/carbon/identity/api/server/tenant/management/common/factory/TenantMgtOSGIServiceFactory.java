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
package org.wso2.carbon.identity.api.server.tenant.management.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.tenant.mgt.services.TenantMgtService;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the TenantMgtService type of object inside the container.
 */
public class TenantMgtOSGIServiceFactory extends AbstractFactoryBean<TenantMgtService> {

    private TenantMgtService tenantMgtService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected TenantMgtService createInstance() throws Exception {

        TenantMgtService tenantMgtService = null;
        if (this.tenantMgtService == null) {
            tenantMgtService = (TenantMgtService)
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getOSGiService(TenantMgtService.class,
                            null);
            if (tenantMgtService != null) {
                this.tenantMgtService = tenantMgtService;
            } else {
                throw new Exception("Unable to get the TenantMgtService");
            }
        }
        return this.tenantMgtService;
    }
}
