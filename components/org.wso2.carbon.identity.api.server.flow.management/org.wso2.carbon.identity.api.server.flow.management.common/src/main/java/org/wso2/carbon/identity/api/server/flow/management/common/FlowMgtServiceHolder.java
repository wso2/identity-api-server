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
import org.wso2.carbon.identity.flow.mgt.FlowAIService;
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

    private static class FlowAIServiceHolder {

        private static final FlowAIService SERVICE = (FlowAIService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(FlowAIService.class, null);
    }

    /**
     * Get FlowMgtService OSGi service.
     *
     * @return FlowMgtService
     */
    public static FlowMgtService getMgtService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving FlowMgtService OSGi service.");
        }
        FlowMgtService flowMgtService = FlowMgtServiceHolderInstance.SERVICE;
        if (flowMgtService == null) {
            log.warn("FlowMgtService OSGi service is not available.");
        }
        return flowMgtService;
    }

    /**
     * Get IdentityGovernanceService osgi service.
     *
     * @return IdentityGovernanceService
     */
    public static IdentityGovernanceService getIdentityGovernanceService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving IdentityGovernanceService OSGi service.");
        }
        IdentityGovernanceService identityGovernanceService = IdentityGovernanceServiceHolder.SERVICE;
        if (identityGovernanceService == null) {
            log.warn("IdentityGovernanceService OSGi service is not available.");
        }
        return identityGovernanceService;
    }

    /**
     * Get IdpManager osgi service.
     *
     * @return IdpManager
     */
    public static IdpManager getIdpManager() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving IdpManager OSGi service.");
        }
        IdpManager idpManager = IdpManagerHolder.SERVICE;
        if (idpManager == null) {
            log.warn("IdpManager OSGi service is not available.");
        }
        return idpManager;
    }

    /**
     * Get FlowAIService OSGi service.
     *
     * @return FlowAIService
     */
    public static FlowAIService getFlowAIService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving FlowAIService OSGi service.");
        }
        FlowAIService flowAIService = FlowAIServiceHolder.SERVICE;
        if (flowAIService == null) {
            log.warn("FlowAIService OSGi service is not available.");
        }
        return flowAIService;
    }
}
