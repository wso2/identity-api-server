/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.identity.api.server.claim.management.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Claim Management util class.
 */
public class Util {

    private static final Log log = LogFactory.getLog(Util.class);

    /**
     * Get ClaimMetadataManagementService osgi service.
     *
     * @return ClaimMetadataManagementService
     */
    @Deprecated
    public static ClaimMetadataManagementService getClaimMetadataManagementService() {
        if (log.isDebugEnabled()) {
            log.debug("Retrieving deprecated ClaimMetadataManagementService from OSGi context.");
        }
        ClaimMetadataManagementService service = (ClaimMetadataManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(ClaimMetadataManagementService.class, null);
        if (service == null && log.isDebugEnabled()) {
            log.debug("ClaimMetadataManagementService is not available in the OSGi context.");
        }
        return service;
    }

    /**
     * Get ClaimMetadataManagementService osgi service.
     *
     * @return ClaimMetadataManagementService
     */
    @Deprecated
    public static RealmService getRealmService() {
        if (log.isDebugEnabled()) {
            log.debug("Retrieving deprecated RealmService from OSGi context.");
        }
        RealmService service = (RealmService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(RealmService.class, null);
        if (service == null && log.isDebugEnabled()) {
            log.debug("RealmService is not available in the OSGi context.");
        }
        return service;
    }
}
