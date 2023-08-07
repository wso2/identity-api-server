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

import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;

/**
 * Service holder class for organization management related services.
 */
public class OrganizationManagementServiceHolder {

    private static OrganizationManagementServiceHolder instance = new OrganizationManagementServiceHolder();

    private OrgApplicationManager orgApplicationManager;
    private OrganizationManager organizationManager;

    private OrganizationManagementServiceHolder() {

    }

    public static OrganizationManagementServiceHolder getInstance() {

        return instance;
    }

    /**
     * Get OrgApplicationManager OSGi service.
     *
     * @return OrgApplicationManager.
     */
    public OrgApplicationManager getOrgApplicationManager() {

        return OrganizationManagementServiceHolder.getInstance().orgApplicationManager;
    }

    /**
     * Set OrgApplicationManager OSGi service.
     *
     * @param orgApplicationManager OrgApplicationManager.
     */
    public void setOrgApplicationManager(OrgApplicationManager orgApplicationManager) {

        OrganizationManagementServiceHolder.getInstance().orgApplicationManager = orgApplicationManager;
    }

    /**
     * Get OrganizationManager OSGi service.
     *
     * @return OrganizationManager.
     */
    public OrganizationManager getOrganizationManager() {

        return OrganizationManagementServiceHolder.getInstance().organizationManager;
    }

    /**
     * Set OrganizationManager OSGi service.
     *
     * @param organizationManager OrganizationManager.
     */
    public void setOrganizationManager(OrganizationManager organizationManager) {

        OrganizationManagementServiceHolder.getInstance().organizationManager = organizationManager;
    }
}
