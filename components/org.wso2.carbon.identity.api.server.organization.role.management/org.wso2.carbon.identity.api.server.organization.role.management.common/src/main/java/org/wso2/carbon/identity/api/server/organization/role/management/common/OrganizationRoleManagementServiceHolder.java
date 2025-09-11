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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.organization.management.role.management.service.RoleManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationUserResidentResolverService;

/**
 * Service holder class for role management related services.
 */
public class OrganizationRoleManagementServiceHolder {

    private static final Log LOG = LogFactory.getLog(OrganizationRoleManagementServiceHolder.class);

    private OrganizationRoleManagementServiceHolder() {}

    private static class RoleManagerServiceHolder {

        static final RoleManager SERVICE = (RoleManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(RoleManager.class, null);
    }

    private static class OrganizationUserResidentResolverServiceHolder {

        static final OrganizationUserResidentResolverService SERVICE =
                (OrganizationUserResidentResolverService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(OrganizationUserResidentResolverService.class, null);
    }

    /**
     * Get RoleManager OSGi service.
     *
     * @return RoleManager.
     */
    public static RoleManager getRoleManager() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving RoleManager OSGi service.");
        }
        RoleManager roleManager = RoleManagerServiceHolder.SERVICE;
        if (roleManager == null) {
            LOG.warn("RoleManager OSGi service is not available.");
        }
        return roleManager;
    }

    /**
     * Method to get the organization user resident resolver OSGi service.
     *
     * @return OrganizationUserResidentResolverService.
     */
    public static OrganizationUserResidentResolverService getOrganizationUserResidentResolverService() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving OrganizationUserResidentResolverService OSGi service.");
        }
        OrganizationUserResidentResolverService service = OrganizationUserResidentResolverServiceHolder.SERVICE;
        if (service == null) {
            LOG.warn("OrganizationUserResidentResolverService OSGi service is not available.");
        }
        return service;
    }
}
