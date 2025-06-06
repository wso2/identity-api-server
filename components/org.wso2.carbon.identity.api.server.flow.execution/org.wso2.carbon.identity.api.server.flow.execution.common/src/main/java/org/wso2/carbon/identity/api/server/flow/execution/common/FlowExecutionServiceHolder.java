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

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.flow.engine.FlowService;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;

/**
 * Holder class for UserRegistrationMgtService.
 */
public class FlowExecutionServiceHolder {

    private FlowExecutionServiceHolder() {
    }

    private static class FlowServiceHolderInstance {

        private static final FlowService INSTANCE = (FlowService)
                PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(FlowService.class, null);
    }

    private static class IdentityGovernanceServiceHolder {

        private static final IdentityGovernanceService SERVICE =
                (IdentityGovernanceService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(IdentityGovernanceService.class, null);
    }

    /**
     * Get FlowService instance.
     *
     * @return FlowService instance
     */
    public static FlowService getInstance() {

        return FlowServiceHolderInstance.INSTANCE;
    }

    /**
     * Get IdentityGovernanceService osgi service.
     *
     * @return IdentityGovernanceService
     */
    public static IdentityGovernanceService getIdentityGovernanceService() {

        return IdentityGovernanceServiceHolder.SERVICE;
    }
}
