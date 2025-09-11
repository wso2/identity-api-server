/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.permission.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.permission.management.common.RolePermissionManagementServiceDataHolder;
import org.wso2.carbon.identity.api.server.permission.management.v1.core.PermissionManagementService;
import org.wso2.carbon.user.mgt.RolePermissionManagementService;

/**
 * Permission Management Service Factory.
 */
public class PermissionManagementServiceFactory {

    private static final Log LOG = LogFactory.getLog(PermissionManagementServiceFactory.class);
    private static final PermissionManagementService SERVICE;

    static {
        RolePermissionManagementService rolePermissionManagementService = RolePermissionManagementServiceDataHolder
                .getRolePermissionManagementService();

        if (rolePermissionManagementService == null) {
            LOG.error("RolePermissionManagementService is not available from OSGi context");
            throw new IllegalStateException("RolePermissionManagementService is not available from OSGi context.");
        }

        LOG.info("PermissionManagementService initialized successfully");
        SERVICE = new PermissionManagementService(rolePermissionManagementService);
    }

    /**
     * Get PermissionManagementService.
     *
     * @return PermissionManagementService
     */
    public static PermissionManagementService getPermissionManagementService() {

        return SERVICE;
    }
}
