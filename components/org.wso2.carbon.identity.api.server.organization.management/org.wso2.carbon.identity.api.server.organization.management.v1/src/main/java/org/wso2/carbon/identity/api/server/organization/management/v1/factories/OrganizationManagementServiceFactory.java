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

package org.wso2.carbon.identity.api.server.organization.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.management.common.OrganizationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.organization.management.v1.service.OrganizationManagementService;
import org.wso2.carbon.identity.organization.discovery.service.OrganizationDiscoveryManager;
import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;

/**
 * Factory class for OrganizationManagementService.
 */
public class OrganizationManagementServiceFactory {

    private static final Log LOG = LogFactory.getLog(OrganizationManagementServiceFactory.class);
    private static final OrganizationManagementService SERVICE;

    static {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Initializing OrganizationManagementService factory.");
        }
        OrgApplicationManager orgApplicationManager = OrganizationManagementServiceHolder.getOrgApplicationManager();
        OrganizationManager organizationManager = OrganizationManagementServiceHolder.getOrganizationManager();
        OrganizationDiscoveryManager organizationDiscoveryManager = OrganizationManagementServiceHolder
                .getOrganizationDiscoveryManager();

        if (orgApplicationManager == null) {
            LOG.error("OrgApplicationManager service is not available from OSGi context.");
            throw new IllegalStateException("OrgApplicationManager service is not available from OSGi context.");
        }

        if (organizationManager == null) {
            LOG.error("OrganizationManager service is not available from OSGi context.");
            throw new IllegalStateException("OrganizationManager service is not available from OSGi context.");
        }

        if (organizationDiscoveryManager == null) {
            LOG.error("OrganizationDiscoveryManager service is not available from OSGi context.");
            throw new IllegalStateException("OrganizationDiscoveryManager service is not available from OSGi context.");
        }

        SERVICE = new OrganizationManagementService(orgApplicationManager,
                organizationManager, organizationDiscoveryManager);
        if (LOG.isDebugEnabled()) {
            LOG.debug("OrganizationManagementService factory initialization completed successfully.");
        }
    }

    /**
     * Get OrganizationManagementService.
     *
     * @return OrganizationManagementService.
     */
    public static OrganizationManagementService getOrganizationManagementService() {

        return SERVICE;
    }
}
