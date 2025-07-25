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

package org.wso2.carbon.identity.api.server.flow.management.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.flow.mgt.FlowMgtService;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;
import org.wso2.carbon.idp.mgt.IdpManager;

/**
 * This class is used to hold the FlowMgtService instance.
 */
public class FlowMgtServiceHolder {

    private static final Log log = LogFactory.getLog(FlowMgtServiceHolder.class);

    private FlowMgtServiceHolder() {
    }

    private static class FlowMgtServiceHolderInstance {

        static final FlowMgtService SERVICE = (FlowMgtService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(FlowMgtService.class, null);
    }

    private static class IdentityGovernanceServiceHolder {

        private static final IdentityGovernanceService SERVICE =
                (IdentityGovernanceService) PrivilegedCarbonContext
                        .getThreadLocalCarbonContext().getOSGiService(IdentityGovernanceService.class, null);
    }

    private static class IdpManagerHolder {

        private static final IdpManager SERVICE =
                (IdpManager) PrivilegedCarbonContext
                        .getThreadLocalCarbonContext().getOSGiService(IdpManager.class, null);
    }

    /**
     * Get FlowMgtService OSGi service.
     *
     * @return FlowMgtService
     */
    public static FlowMgtService getMgtService() {

        FlowMgtService service = FlowMgtServiceHolderInstance.SERVICE;
        if (service == null) {
            log.warn("FlowMgtService is not available. Flow management functionality may not work properly.");
        } else if (log.isDebugEnabled()) {
            log.debug("FlowMgtService retrieved successfully.");
        }
        return service;
    }

    /**
     * Get IdentityGovernanceService osgi service.
     *
     * @return IdentityGovernanceService
     */
    public static IdentityGovernanceService getIdentityGovernanceService() {

        IdentityGovernanceService service = IdentityGovernanceServiceHolder.SERVICE;
        if (service == null) {
            log.warn("IdentityGovernanceService is not available. Identity governance functionality may not work " +
                    "properly.");
        } else if (log.isDebugEnabled()) {
            log.debug("IdentityGovernanceService retrieved successfully.");
        }
        return service;
    }

    /**
     * Get IdpManager osgi service.
     *
     * @return IdpManager
     */
    public static IdpManager getIdpManager() {

        IdpManager service = IdpManagerHolder.SERVICE;
        if (service == null) {
            log.warn("IdpManager is not available. Identity provider management functionality may not work " +
                    "properly.");
        } else if (log.isDebugEnabled()) {
            log.debug("IdpManager retrieved successfully.");
        }
        return service;
    }
}
