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

package org.wso2.carbon.identity.api.server.organization.management.v1.factories;

import org.wso2.carbon.identity.api.server.organization.management.common.OrganizationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.organization.management.v1.service.OrganizationManagementService;
import org.wso2.carbon.identity.organization.discovery.service.OrganizationDiscoveryManager;
import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;

/**
 * Factory class for OrganizationManagementService.
 */
public class OrganizationManagementServiceFactory {

    private OrganizationManagementServiceFactory() {

    }

    private static class OrganizationServiceHolder {

        private static final OrganizationManagementService SERVICE = createServiceInstance();
    }

    private static OrganizationManagementService createServiceInstance() {

        OrgApplicationManager orgApplicationManager = getOrgApplicationManagerService();
        OrganizationManager organizationManager = getOrganizationManagerService();
        OrganizationDiscoveryManager organizationDiscoveryManager = getOrganizationDiscoveryManagerService();

        return new OrganizationManagementService(orgApplicationManager, organizationManager,
                organizationDiscoveryManager);
    }

    /**
     * Get OrganizationManagementService.
     *
     * @return OrganizationManagementService.
     */
    public static OrganizationManagementService getOrganizationManagementService() {

        return OrganizationServiceHolder.SERVICE;
    }

    private static OrgApplicationManager getOrgApplicationManagerService() {

        OrgApplicationManager service = OrganizationManagementServiceHolder.getOrgApplicationManager();
        if (service == null) {
            throw new IllegalStateException("OrgApplicationManager is not available from OSGi context.");
        }

        return service;
    }

    private static OrganizationManager getOrganizationManagerService() {

        OrganizationManager service = OrganizationManagementServiceHolder.getOrganizationManager();
        if (service == null) {
            throw new IllegalStateException("OrganizationManager is not available from OSGi context.");
        }

        return service;
    }

    private static OrganizationDiscoveryManager getOrganizationDiscoveryManagerService() {

        OrganizationDiscoveryManager service = OrganizationManagementServiceHolder.getOrganizationDiscoveryManager();
        if (service == null) {
            throw new IllegalStateException("OrganizationDiscoveryManager is not available from OSGi context.");
        }

        return service;
    }
}
