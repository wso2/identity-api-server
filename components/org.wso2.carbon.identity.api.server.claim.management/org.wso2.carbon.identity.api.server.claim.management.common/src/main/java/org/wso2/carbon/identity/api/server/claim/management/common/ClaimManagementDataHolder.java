/*
 * Copyright (c) (2019-2023), WSO2 LLC. (http://www.wso2.org).
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

package org.wso2.carbon.identity.api.server.claim.management.common;

import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;

/**
 * Service holder class for identity governance.
 */
public class ClaimManagementDataHolder {

    private static ClaimMetadataManagementService claimMetadataManagementService;
    private static OrganizationManager organizationManager;

    public static ClaimMetadataManagementService getClaimMetadataManagementService() {

        return claimMetadataManagementService;
    }

    public static void setClaimMetadataManagementService(
            ClaimMetadataManagementService claimMetadataManagementService) {

        ClaimManagementDataHolder.claimMetadataManagementService = claimMetadataManagementService;
    }

    /**
     * Get organizationManager OSGi service.
     *
     * @return organization Manager.
     */
    public static OrganizationManager getOrganizationManager() {

        return organizationManager;
    }

    /**
     * Set organizationManager OSGi service.
     *
     * @param organizationManager Organization Manager.
     */
    public static void setOrganizationManager(OrganizationManager organizationManager) {

        ClaimManagementDataHolder.organizationManager = organizationManager;
    }
}
