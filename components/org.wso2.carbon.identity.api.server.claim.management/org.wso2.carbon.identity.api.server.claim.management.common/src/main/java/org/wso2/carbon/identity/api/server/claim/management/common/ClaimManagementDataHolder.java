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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;

/**
 * Service holder class for identity governance.
 */
public class ClaimManagementDataHolder {

    private static final Log log = LogFactory.getLog(ClaimManagementDataHolder.class);

    private static class OrganizationManagerHolder {

        static final OrganizationManager SERVICE = (OrganizationManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(OrganizationManager.class, null);
    }

    private static class ClaimMetadataManagementServiceHolder {

        static final ClaimMetadataManagementService SERVICE = (ClaimMetadataManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(ClaimMetadataManagementService.class, null);
    }

    /**
     * Get ClaimMetadataManagementService OSGi service.
     *
     * @return ClaimMetadataManagementService.
     */
    public static ClaimMetadataManagementService getClaimMetadataManagementService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving ClaimMetadataManagementService OSGi service.");
        }
        ClaimMetadataManagementService service = ClaimMetadataManagementServiceHolder.SERVICE;
        if (service == null) {
            log.warn("ClaimMetadataManagementService is not available.");
        }
        return service;
    }

    /**
     * Get organizationManager OSGi service.
     *
     * @return organization Manager.
     */
    public static OrganizationManager getOrganizationManager() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving OrganizationManager OSGi service.");
        }
        OrganizationManager service = OrganizationManagerHolder.SERVICE;
        if (service == null) {
            log.warn("OrganizationManager service is not available.");
        }
        return service;
    }
}
