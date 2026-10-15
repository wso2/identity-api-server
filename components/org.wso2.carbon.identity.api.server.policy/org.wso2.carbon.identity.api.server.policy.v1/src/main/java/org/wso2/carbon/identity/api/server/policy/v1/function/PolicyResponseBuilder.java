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

package org.wso2.carbon.identity.api.server.policy.v1.function;

import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResourceResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResponse;
import org.wso2.carbon.identity.policy.management.api.model.Policy;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResource;
import org.wso2.carbon.identity.policy.management.api.model.RulePolicyResource;
import org.wso2.carbon.identity.rule.management.api.model.Rule;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds a PolicyResponse (API model) from a Policy (domain model).
 *
 * Used after every successful operation to build the JSON response
 * that gets sent back to the client.
 */
public class PolicyResponseBuilder {

    private PolicyResponseBuilder() {

    }

    public static PolicyResponse buildPolicyResponse(Policy policy) {

        PolicyResponse response = new PolicyResponse();
        response.setId(policy.getId());
        response.setName(policy.getName());

        if (policy.getResources() != null && !policy.getResources().isEmpty()) {
            List<PolicyResourceResponse> resourceResponses = policy.getResources().stream()
                    .map(PolicyResponseBuilder::toPolicyResourceResponse)
                    .collect(Collectors.toList());
            response.setResources(resourceResponses);
        }

        return response;
    }

    private static PolicyResourceResponse toPolicyResourceResponse(PolicyResource policyResource) {

        PolicyResourceResponse resourceResponse = new PolicyResourceResponse();
        resourceResponse.setTarget(policyResource.getTarget());
        if (policyResource.getResourceType() != null) {
            resourceResponse.setResourceType(
                    PolicyResourceResponse.ResourceTypeEnum.fromValue(policyResource.getResourceType().name()));
        }
        if (policyResource instanceof RulePolicyResource) {
            Rule rule = ((RulePolicyResource) policyResource).getRule();
            if (rule != null) {
                resourceResponse.setRule(RuleResponseBuilder.buildRuleResponse(rule));
            }
        }
        return resourceResponse;
    }
}
