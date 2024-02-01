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

package org.wso2.carbon.identity.api.server.organization.role.management.common;

import org.wso2.carbon.identity.organization.management.role.management.service.RoleManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationUserResidentResolverService;

/**
 * Service holder class for role management related services.
 */
public class OrganizationRoleManagementServiceHolder {

    private static OrganizationRoleManagementServiceHolder instance = new OrganizationRoleManagementServiceHolder();

    private RoleManager roleManager;

    private OrganizationUserResidentResolverService organizationUserResidentResolverService;

    private OrganizationRoleManagementServiceHolder() {

    }

    public static OrganizationRoleManagementServiceHolder getInstance() {

        return instance;
    }

    /**
     * Get RoleManager OSGi service.
     *
     * @return RoleManager.
     */
    public RoleManager getRoleManager() {

        return OrganizationRoleManagementServiceHolder.getInstance().roleManager;
    }

    /**
     * Set RoleManager OSGi service.
     *
     * @param roleManager RoleManager.
     */
    public void setRoleManager(RoleManager roleManager) {

        OrganizationRoleManagementServiceHolder.getInstance().roleManager = roleManager;
    }

    /**
     * Method to get the organization user resident resolver OSGi service.
     *
     * @return OrganizationUserResidentResolverService.
     */
    public OrganizationUserResidentResolverService getOrganizationUserResidentResolverService() {

        return OrganizationRoleManagementServiceHolder.getInstance().organizationUserResidentResolverService;
    }

    /**
     * Set OrganizationUserResidentResolverService OSGi service.
     *
     * @param organizationUserResidentResolverService OrganizationUserResidentResolverService.
     */
    public void setOrganizationUserResidentResolverService(
            OrganizationUserResidentResolverService organizationUserResidentResolverService) {

        OrganizationRoleManagementServiceHolder.getInstance().organizationUserResidentResolverService
                = organizationUserResidentResolverService;
    }
}
