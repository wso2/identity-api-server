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

package org.wso2.carbon.identity.api.server.flow.execution.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.flow.execution.engine.FlowExecutionService;
import org.wso2.carbon.identity.flow.mgt.FlowMgtService;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;

/**
 * Holder class for FlowExecutionService.
 */
public class FlowExecutionServiceHolder {

    private static final Log log = LogFactory.getLog(FlowExecutionServiceHolder.class);

    private FlowExecutionServiceHolder() {

    }

    private static class FlowExecutionServiceHolderInstance {

        private static final FlowExecutionService INSTANCE = (FlowExecutionService)
                PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(FlowExecutionService.class, null);
    }

    private static class FlowMgtServiceHolderInstance {

        private static final FlowMgtService INSTANCE = (FlowMgtService)
                PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(FlowMgtService.class, null);
    }

    private static class IdentityGovernanceServiceHolder {

        private static final IdentityGovernanceService SERVICE =
                (IdentityGovernanceService) PrivilegedCarbonContext
                        .getThreadLocalCarbonContext().getOSGiService(IdentityGovernanceService.class, null);
    }

    /**
     * Get FlowExecutionService instance.
     *
     * @return FlowExecutionService instance
     */
    public static FlowExecutionService getInstance() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving FlowExecutionService instance.");
        }
        FlowExecutionService service = FlowExecutionServiceHolderInstance.INSTANCE;
        if (service == null && log.isWarnEnabled()) {
            log.warn("FlowExecutionService instance is not available.");
        }
        return service;
    }

    /**
     * Get FlowMgtService instance.
     *
     * @return FlowMgtService instance
     */
    public static FlowMgtService getFlowMgtService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving FlowMgtService instance.");
        }
        FlowMgtService service = FlowMgtServiceHolderInstance.INSTANCE;
        if (service == null && log.isWarnEnabled()) {
            log.warn("FlowMgtService instance is not available.");
        }
        return service;
    }

    /**
     * Get IdentityGovernanceService osgi service.
     *
     * @return IdentityGovernanceService
     */
    public static IdentityGovernanceService getIdentityGovernanceService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving IdentityGovernanceService instance.");
        }
        IdentityGovernanceService service = IdentityGovernanceServiceHolder.SERVICE;
        if (service == null && log.isWarnEnabled()) {
            log.warn("IdentityGovernanceService instance is not available.");
        }
        return service;
    }
}
