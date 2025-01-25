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

package org.wso2.carbon.identity.api.server.organization.management.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.organization.discovery.service.OrganizationDiscoveryManager;
import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;

/**
 * Service holder class for organization management related services.
 */
public class OrganizationManagementServiceHolder {

    private OrganizationManagementServiceHolder() {}

    private static class OrgApplicationManagerHolder {

        static final OrgApplicationManager SERVICE = (OrgApplicationManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(OrgApplicationManager.class, null);
    }

    private static class OrganizationManagerHolder {

        static final OrganizationManager SERVICE = (OrganizationManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(OrganizationManager.class, null);
    }

    private static class OrganizationDiscoveryManagerHolder {

        static final OrganizationDiscoveryManager SERVICE = (OrganizationDiscoveryManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(OrganizationDiscoveryManager.class, null);
    }

    /**
     * Get OrgApplicationManager OSGi service.
     *
     * @return OrgApplicationManager.
     */
    public static OrgApplicationManager getOrgApplicationManager() {

        return OrgApplicationManagerHolder.SERVICE;
    }

    /**
     * Get OrganizationManager OSGi service.
     *
     * @return OrganizationManager.
     */
    public static OrganizationManager getOrganizationManager() {

        return OrganizationManagerHolder.SERVICE;
    }

    /**
     * Get OrganizationDiscoveryManager OSGi service.
     *
     * @return OrganizationDiscoveryManager.
     */
    public static OrganizationDiscoveryManager getOrganizationDiscoveryManager() {

        return OrganizationDiscoveryManagerHolder.SERVICE;
    }
}
