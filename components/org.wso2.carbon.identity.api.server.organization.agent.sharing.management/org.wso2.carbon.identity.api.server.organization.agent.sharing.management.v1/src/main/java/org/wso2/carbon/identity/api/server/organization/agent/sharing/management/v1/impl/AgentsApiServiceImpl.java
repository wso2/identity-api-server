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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.impl;

import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.AgentsApiService;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.core.AgentsApiServiceCore;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.factories.AgentsApiServiceCoreFactory;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharingPatchRequest;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareSelectedRequestBody;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.ERROR_INITIATING_AGENTS_API_SERVICE;

/**
 * Implementation of the agent sharing management APIs.
 */
public class AgentsApiServiceImpl implements AgentsApiService {

    private final AgentsApiServiceCore agentsApiServiceCore;

    public AgentsApiServiceImpl() {

        try {
            this.agentsApiServiceCore = AgentsApiServiceCoreFactory.getAgentsApiServiceCore();
        } catch (IllegalStateException e) {
            throw new RuntimeException(ERROR_INITIATING_AGENTS_API_SERVICE.getMessage(), e);
        }
    }

    @Override
    public Response getAgentSharedOrganizations(String agentId, String before, String after, String filter,
                                                Integer limit, Boolean recursive, String attributes) {

        return agentsApiServiceCore.getAgentSharedOrganizations(agentId, before, after, filter, limit, recursive,
                attributes);
    }

    @Override
    public Response patchAgentSharing(AgentSharingPatchRequest agentSharingPatchRequest) {

        return agentsApiServiceCore.patchAgentSharing(agentSharingPatchRequest);
    }

    @Override
    public Response shareAgentsWithAll(AgentShareAllRequestBody agentShareAllRequestBody) {

        return agentsApiServiceCore.shareAgentsWithAllOrgs(agentShareAllRequestBody);
    }

    @Override
    public Response shareAgentsWithSelected(AgentShareSelectedRequestBody agentShareSelectedRequestBody) {

        return agentsApiServiceCore.shareAgentsWithSelectedOrgs(agentShareSelectedRequestBody);
    }

    @Override
    public Response unshareAgentsFromAll(AgentUnshareAllRequestBody agentUnshareAllRequestBody) {

        return agentsApiServiceCore.unshareAgentsFromAllOrgs(agentUnshareAllRequestBody);
    }

    @Override
    public Response unshareAgentsFromSelected(AgentUnshareSelectedRequestBody agentUnshareSelectedRequestBody) {

        return agentsApiServiceCore.unshareAgentsFromSelectedOrgs(agentUnshareSelectedRequestBody);
    }
}
