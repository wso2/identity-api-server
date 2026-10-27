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

package org.wso2.carbon.identity.api.server.policy.v1.function.resource.rule;

import org.wso2.carbon.identity.api.server.policy.common.Constants;
import org.wso2.carbon.identity.api.server.policy.common.PolicyServiceHolder;
import org.wso2.carbon.identity.api.server.policy.v1.function.resource.PolicyResourceHandler;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResourceRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResourceResponse;
import org.wso2.carbon.identity.api.server.policy.v1.util.PolicyManagementAPIErrorBuilder;
import org.wso2.carbon.identity.policy.management.api.exception.PolicyManagementException;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResource;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResource.ResourceType;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResourceMetadata;
import org.wso2.carbon.identity.policy.management.api.model.RulePolicyResource;
import org.wso2.carbon.identity.policy.management.api.model.RulePolicyResourceMetadata;
import org.wso2.carbon.identity.rule.management.api.model.Rule;

import javax.ws.rs.core.Response;

/**
 * Maps {@link ResourceType#RULE} resources. Rules are built against the rule flow that policy management gives in
 * the rule metadata of the policy type.
 */
public class RulePolicyResourceHandler extends PolicyResourceHandler {

    @Override
    public ResourceType getResourceType() {

        return ResourceType.RULE;
    }

    @Override
    public PolicyResource buildResource(PolicyResourceRequest resourceRequest, String policyType,
                                        String tenantDomain) throws PolicyManagementException {

        RulePolicyResourceMetadata metadata = toRuleMetadata(PolicyServiceHolder.getPolicyManagementService()
                .getResourceMetadata(policyType, ResourceType.RULE, resourceRequest.getTarget(), tenantDomain));
        return new RulePolicyResource.Builder()
                .target(resourceRequest.getTarget())
                .rule(RuleRequestBuilder.buildRule(resourceRequest.getRule(), metadata.getRuleFlowType(),
                        tenantDomain))
                .build();
    }

    @Override
    public Object buildMetadataResponse(PolicyResourceMetadata metadata) {

        return RuleMetadataResponseBuilder.buildRuleMetadataResponse(toRuleMetadata(metadata).getFieldDefinitions());
    }

    @Override
    protected void addResourceDetails(PolicyResource resource, PolicyResourceResponse resourceResponse) {

        if (resource instanceof RulePolicyResource) {
            Rule rule = ((RulePolicyResource) resource).getRule();
            if (rule != null) {
                resourceResponse.setRule(RuleResponseBuilder.buildRuleResponse(rule));
            }
        }
    }

    private RulePolicyResourceMetadata toRuleMetadata(PolicyResourceMetadata metadata) {

        if (!(metadata instanceof RulePolicyResourceMetadata)) {
            throw PolicyManagementAPIErrorBuilder.handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_METADATA);
        }
        return (RulePolicyResourceMetadata) metadata;
    }
}
