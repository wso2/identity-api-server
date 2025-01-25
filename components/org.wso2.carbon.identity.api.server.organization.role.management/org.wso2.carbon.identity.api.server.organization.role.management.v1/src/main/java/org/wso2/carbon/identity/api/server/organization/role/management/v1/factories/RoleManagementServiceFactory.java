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

package org.wso2.carbon.identity.api.server.organization.role.management.v1.factories;

import org.wso2.carbon.identity.api.server.organization.role.management.common.OrganizationRoleManagementServiceHolder;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.service.RoleManagementService;
import org.wso2.carbon.identity.organization.management.role.management.service.RoleManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationUserResidentResolverService;

/**
 * Factory class for RoleManagementService.
 */
public class RoleManagementServiceFactory {

    private static final RoleManagementService SERVICE;

    static {
        RoleManager roleManager = OrganizationRoleManagementServiceHolder.getRoleManager();
        OrganizationUserResidentResolverService organizationUserResidentResolverService =
                OrganizationRoleManagementServiceHolder.getOrganizationUserResidentResolverService();

        if (roleManager == null) {
            throw new IllegalStateException("RoleManager service is not available from OSGi context.");
        }

        if (organizationUserResidentResolverService == null) {
            throw new IllegalStateException("OrganizationUserResidentResolverService is not available " +
                    "from OSGi context.");
        }

        SERVICE = new RoleManagementService(roleManager, organizationUserResidentResolverService);
    }

    /**
     * Get RoleManagementService.
     *
     * @return RoleManagementService.
     */
    public static RoleManagementService getRoleManagementService() {
    
        return SERVICE;
    }
}
