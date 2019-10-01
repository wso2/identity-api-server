/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.permission.management.common;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.user.mgt.RolePermissionManagementService;
import org.wso2.carbon.user.mgt.RolePermissionManagementServiceImpl;

/**
 * RolePermissionManagementService Factory class.
 */
public class RolePermissionManagementServiceFactory extends
        AbstractFactoryBean<RolePermissionManagementServiceImpl> {

    private RolePermissionManagementServiceImpl rolePermissionManagementService;

    @Override
    public Class<RolePermissionManagementServiceImpl> getObjectType() {

        return RolePermissionManagementServiceImpl.class;
    }

    @Override
    protected RolePermissionManagementServiceImpl createInstance() throws Exception {

        if (this.rolePermissionManagementService != null) {
            return this.rolePermissionManagementService;
        } else {
            RolePermissionManagementServiceImpl rolePermissionManagementService = (RolePermissionManagementServiceImpl)
                    PrivilegedCarbonContext.getThreadLocalCarbonContext()
                            .getOSGiService(RolePermissionManagementService.class, null);
            if (rolePermissionManagementService != null) {
                this.rolePermissionManagementService = rolePermissionManagementService;
            } else {
                throw new Exception("Unable to get the RolePermissionManagementServiceImpl");
            }
            return rolePermissionManagementService;
        }
    }
}
