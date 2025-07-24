/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.identity.governance.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;

/**
 * Service holder class for identity governance.
 */
public class GovernanceDataHolder {

    private static final Log log = LogFactory.getLog(GovernanceDataHolder.class);

    private GovernanceDataHolder() {}

    private static class IdentityGovernanceServiceHolder {
        static final IdentityGovernanceService SERVICE = (IdentityGovernanceService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(IdentityGovernanceService.class, null);
    }

    /**
     * Get IdentityGovernanceService osgi service.
     *
     * @return IdentityGovernanceService
     */
    public static IdentityGovernanceService getIdentityGovernanceService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving IdentityGovernanceService from OSGi service registry.");
        }
        IdentityGovernanceService service = IdentityGovernanceServiceHolder.SERVICE;
        if (service == null) {
            log.warn("IdentityGovernanceService is not available in the OSGi service registry.");
        }
        return service;
    }
}
