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

import org.wso2.carbon.identity.api.server.policy.v1.function.resource.PolicyResourceHandlerRegistry;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResourceRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyUpdateRequest;
import org.wso2.carbon.identity.policy.management.api.exception.PolicyManagementException;
import org.wso2.carbon.identity.policy.management.api.model.Policy;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static Policy buildPolicy(PolicyRequest policyRequest, String tenantDomain)
            throws PolicyManagementException {

        List<PolicyResource> resources =
                buildPolicyResources(policyRequest.getResources(), policyRequest.getType(), tenantDomain);
        return new Policy.Builder()
                .name(policyRequest.getName())
                .type(policyRequest.getType())
                .resources(resources)
                .build();
    }

    public static Policy buildUpdatingPolicy(PolicyUpdateRequest policyUpdateRequest, String policyId,
                                              String policyType, String tenantDomain)
            throws PolicyManagementException {

        List<PolicyResource> resources =
                buildPolicyResources(policyUpdateRequest.getResources(), policyType, tenantDomain);
        // Policy name and type are immutable; the backend retains the stored values.
        return new Policy.Builder()
                .id(policyId)
                .resources(resources)
                .build();
    }

    private static List<PolicyResource> buildPolicyResources(List<PolicyResourceRequest> resourceRequests,
                                                               String policyType, String tenantDomain)
            throws PolicyManagementException {

        if (resourceRequests == null || resourceRequests.isEmpty()) {
            return Collections.emptyList();
        }
        List<PolicyResource> resources = new ArrayList<>();
        for (PolicyResourceRequest resourceRequest : resourceRequests) {
            String resourceType = resourceRequest.getResourceType() != null
                    ? resourceRequest.getResourceType().value() : null;
            resources.add(PolicyResourceHandlerRegistry.getInstance().getHandler(resourceType)
                    .buildResource(resourceRequest, policyType, tenantDomain));
        }
        return resources;
    }
}
