/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.impl;


import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.AgentsApiService;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.core.AgentsApiServiceCore;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.factories.AgentsApiServiceCoreFactory;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareWithAllRequestBody;

import javax.ws.rs.core.Response;

/**
 * Implementation of the agent sharing management APIs.
 */
public class AgentsApiServiceImpl implements AgentsApiService {

    private final AgentsApiServiceCore agentsApiServiceCore;

    public AgentsApiServiceImpl() {

        try {
            this.agentsApiServiceCore = AgentsApiServiceCoreFactory.getAgentsApiServiceCore();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error initializing Agents API Service Core.", e);
        }
    }

    @Override
    public Response agentsAgentIdSharedOrganizationsGet(String agentId, String after, String before, Integer limit,
                                                        String filter, Boolean recursive) {

        return agentsApiServiceCore.getSharedOrganizations(agentId, after, before, limit, filter, recursive);
    }

    @Override
    public Response agentsAgentIdSharedRolesGet(String agentId, String orgId, String after, String before,
                                                Integer limit, String filter, Boolean recursive) {

        return agentsApiServiceCore.getSharedRoles(agentId, orgId, after, before, limit, filter, recursive);
    }

    @Override
    public Response processAgentSharing(AgentShareRequestBody agentShareRequestBody) {

        return agentsApiServiceCore.shareAgent(agentShareRequestBody);
    }

    @Override
    public Response processAgentSharingAll(AgentShareWithAllRequestBody agentShareWithAllRequestBody) {

        return agentsApiServiceCore.shareAgentWithAll(agentShareWithAllRequestBody);
    }

    @Override
    public Response processAgentUnsharing(AgentUnshareRequestBody agentUnshareRequestBody) {

        return agentsApiServiceCore.unshareAgent(agentUnshareRequestBody);
    }

    @Override
    public Response removeAgentSharing(AgentUnshareWithAllRequestBody agentUnshareWithAllRequestBody) {

        return agentsApiServiceCore.unshareAgentWithAll(agentUnshareWithAllRequestBody);
    }
}
