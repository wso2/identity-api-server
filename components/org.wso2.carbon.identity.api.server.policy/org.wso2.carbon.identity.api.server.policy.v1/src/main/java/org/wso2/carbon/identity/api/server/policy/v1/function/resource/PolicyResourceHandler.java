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

package org.wso2.carbon.identity.api.server.policy.v1.function.resource;

import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResourceRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResourceResponse;
import org.wso2.carbon.identity.policy.management.api.exception.PolicyManagementException;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResource;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResource.ResourceType;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResourceMetadata;

/**
 * Maps the resources of one resource type between the Policy API and policy management. Each resource type has
 * its own subclass, so the generic policy builders never deal with resource type specific formats.
 */
public abstract class PolicyResourceHandler {

    /**
     * Returns the resource type this handler maps.
     *
     * @return Resource type.
     */
    public abstract ResourceType getResourceType();

    /**
     * Build a policy resource from a resource of a policy request.
     *
     * @param resourceRequest Resource of the policy request.
     * @param policyType      Type of the policy the resource belongs to.
     * @param tenantDomain    Tenant domain.
     * @return Policy resource.
     * @throws PolicyManagementException If the resource cannot be built for the policy type.
     */
    public abstract PolicyResource buildResource(PolicyResourceRequest resourceRequest, String policyType,
                                                 String tenantDomain) throws PolicyManagementException;

    /**
     * Build the response of a policy resource.
     *
     * @param resource Policy resource.
     * @return Policy resource response.
     */
    public PolicyResourceResponse buildResourceResponse(PolicyResource resource) {

        PolicyResourceResponse resourceResponse = new PolicyResourceResponse();
        resourceResponse.setTarget(resource.getTarget());
        resourceResponse.setResourceType(PolicyResourceResponse.ResourceTypeEnum.fromValue(getResourceType().name()));
        addResourceDetails(resource, resourceResponse);
        return resourceResponse;
    }

    /**
     * Build the response of the metadata of the resource type.
     *
     * @param metadata Resource metadata.
     * @return Metadata response.
     */
    public abstract Object buildMetadataResponse(PolicyResourceMetadata metadata);

    /**
     * Add the resource type specific details of a policy resource to its response.
     *
     * @param resource         Policy resource.
     * @param resourceResponse Policy resource response.
     */
    protected abstract void addResourceDetails(PolicyResource resource, PolicyResourceResponse resourceResponse);
}
