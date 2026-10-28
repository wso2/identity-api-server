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
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResourceResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResponse;
import org.wso2.carbon.identity.policy.management.api.model.Policy;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResource;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResourceMetadata;

import java.util.ArrayList;
import java.util.List;

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
        response.setType(policy.getType());

        if (policy.getResources() != null && !policy.getResources().isEmpty()) {
            List<PolicyResourceResponse> resourceResponses = new ArrayList<>();
            for (PolicyResource resource : policy.getResources()) {
                resourceResponses.add(PolicyResourceHandlerRegistry.getInstance().getHandler(resource.getResourceType())
                        .buildResourceResponse(resource));
            }
            response.setResources(resourceResponses);
        }

        return response;
    }

    /**
     * Build the response of the metadata of a resource type, in the format of that resource type.
     *
     * @param metadata Resource metadata.
     * @return Metadata response.
     */
    public static Object buildResourceMetadataResponse(PolicyResourceMetadata metadata) {

        return PolicyResourceHandlerRegistry.getInstance().getHandler(metadata.getResourceType())
                .buildMetadataResponse(metadata);
    }
}
