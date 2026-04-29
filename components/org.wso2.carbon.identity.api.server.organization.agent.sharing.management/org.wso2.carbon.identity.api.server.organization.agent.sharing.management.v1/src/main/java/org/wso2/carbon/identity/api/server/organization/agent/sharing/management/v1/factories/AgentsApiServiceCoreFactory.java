/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.AgentSharingMgtServiceHolder;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.core.AgentsApiServiceCore;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.AgentSharingPolicyHandlerService;

/**
 * Factory class for creating the AgentsApiServiceCore singleton.
 */
public class AgentsApiServiceCoreFactory {

    private static final Log LOG = LogFactory.getLog(AgentsApiServiceCoreFactory.class);
    private static final AgentsApiServiceCore SERVICE;

    static {
        LOG.debug("Initializing AgentsApiServiceCoreFactory");
        AgentSharingPolicyHandlerService agentSharingPolicyHandlerService =
                AgentSharingMgtServiceHolder.getAgentSharingPolicyHandlerService();
        if (agentSharingPolicyHandlerService == null) {
            LOG.error(
                    "Failed to initialize AgentsApiServiceCoreFactory: AgentSharingPolicyHandlerService is not " +
                            "available from OSGi context");
            throw new IllegalStateException(
                    "AgentSharingPolicyHandlerService is not available from the OSGi context.");
        }
        SERVICE = new AgentsApiServiceCore(agentSharingPolicyHandlerService);
    }

    /**
     * Get AgentsApiServiceCore.
     *
     * @return AgentsApiServiceCore.
     */
    public static AgentsApiServiceCore getAgentsApiServiceCore() {

        return SERVICE;
    }
}
