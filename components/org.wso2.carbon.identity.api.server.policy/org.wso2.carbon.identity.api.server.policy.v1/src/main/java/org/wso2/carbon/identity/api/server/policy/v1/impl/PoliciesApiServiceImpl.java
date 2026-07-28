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

package org.wso2.carbon.identity.api.server.policy.v1.impl;

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.policy.common.Constants;
import org.wso2.carbon.identity.api.server.policy.v1.PoliciesApiService;
import org.wso2.carbon.identity.api.server.policy.v1.core.PolicyManagementService;
import org.wso2.carbon.identity.api.server.policy.v1.factories.PolicyServiceFactory;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyUpdateRequest;

import java.net.URI;
import javax.ws.rs.core.Response;

/**
 * Implementation of the Policies API service.
 */
public class PoliciesApiServiceImpl implements PoliciesApiService {

    private final PolicyManagementService policyService;

    public PoliciesApiServiceImpl() {

        try {
            this.policyService = PolicyServiceFactory.getPolicyService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating PolicyService.", e);
        }
    }

    @Override
    public Response addPolicy(PolicyRequest policyRequest) {

        PolicyResponse policyResponse = policyService.addPolicy(policyRequest);
        URI location = ContextLoader.buildURIForHeader(
                Constants.V1_API_PATH_COMPONENT + Constants.POLICY_PATH_COMPONENT + "/" + policyResponse.getId());
        return Response.created(location).entity(policyResponse).build();
    }

    @Override
    public Response getPolicies(Integer limit, Integer offset, String filter) {

        return Response.ok().entity(policyService.getPoliciesBasicInfo(limit, offset, filter)).build();
    }

    @Override
    public Response deletePolicy(String policyId) {

        policyService.deletePolicy(policyId);
        return Response.noContent().build();
    }

    @Override
    public Response getPolicyById(String policyId) {

        PolicyResponse policyResponse = policyService.getPolicyById(policyId);
        return Response.ok().entity(policyResponse).build();
    }

    @Override
    public Response updatePolicy(String policyId, PolicyUpdateRequest policyUpdateRequest) {

        PolicyResponse policyResponse = policyService.updatePolicy(policyId, policyUpdateRequest);
        return Response.ok().entity(policyResponse).build();
    }
}
