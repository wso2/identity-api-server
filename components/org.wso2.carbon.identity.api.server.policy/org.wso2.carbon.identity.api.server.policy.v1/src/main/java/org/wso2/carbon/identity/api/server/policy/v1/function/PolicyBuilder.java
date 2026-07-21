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

import org.wso2.carbon.identity.api.server.policy.common.Constants;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResourceRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyUpdateRequest;
import org.wso2.carbon.identity.api.server.policy.v1.util.PolicyManagementAPIErrorBuilder;
import org.wso2.carbon.identity.policy.management.api.model.Policy;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResource;
import org.wso2.carbon.identity.policy.management.api.model.RulePolicyResource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

/**
 * Builds a Policy (domain model) from a PolicyRequest (API model).
 *
 * Used in:
 *   addPolicy    → no ID yet, pass null
 *   updatePolicy → ID comes from the URL path param
 */
public class PolicyBuilder {

    private PolicyBuilder() {

    }

    public static Policy buildPolicy(PolicyRequest policyRequest, String policyId, String tenantDomain) {

        List<PolicyResource> resources = buildPolicyResources(policyRequest.getResources(), tenantDomain);
        return new Policy(policyId, policyRequest.getName(), null, resources);
    }

    public static Policy buildUpdatingPolicy(PolicyUpdateRequest policyUpdateRequest, String policyId,
                                              String tenantDomain) {

        List<PolicyResource> resources = buildPolicyResources(policyUpdateRequest.getResources(), tenantDomain);
        // Policy name is immutable; the backend retains the stored name.
        return new Policy(policyId, null, null, resources);
    }

    private static List<PolicyResource> buildPolicyResources(List<PolicyResourceRequest> resourceRequests,
                                                               String tenantDomain) {

        if (resourceRequests == null || resourceRequests.isEmpty()) {
            return Collections.emptyList();
        }
        return resourceRequests.stream()
                .map(resourceRequest -> toRulePolicyResource(resourceRequest, tenantDomain))
                .collect(Collectors.toList());
    }

    private static PolicyResource toRulePolicyResource(PolicyResourceRequest resourceRequest, String tenantDomain) {

        validateResourceType(resourceRequest.getResourceType());
        return new RulePolicyResource(null, resourceRequest.getTarget(), null,
                PolicyRuleBuilder.buildRule(resourceRequest.getRule(), tenantDomain));
    }

    private static void validateResourceType(PolicyResourceRequest.ResourceTypeEnum resourceType) {

        // resourceType is optional in the API and defaults to RULE; RULE is the only supported type.
        if (resourceType != null && resourceType != PolicyResourceRequest.ResourceTypeEnum.RULE) {
            throw PolicyManagementAPIErrorBuilder.handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_RESOURCE_TYPE, resourceType.toString());
        }
    }
}
