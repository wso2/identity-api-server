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
import org.wso2.carbon.identity.api.server.policy.v1.model.ExpressionRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResourceRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleRequest;
import org.wso2.carbon.identity.api.server.policy.v1.util.PolicyManagementAPIErrorBuilder;
import org.wso2.carbon.identity.policy.management.api.model.Policy;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResource;
import org.wso2.carbon.identity.policy.management.api.model.RulePolicyResource;
import org.wso2.carbon.identity.rule.management.api.model.ANDCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.Expression;
import org.wso2.carbon.identity.rule.management.api.model.ORCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.Rule;
import org.wso2.carbon.identity.rule.management.api.model.Value;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

/**
 * Mapper: PolicyRequest (API model) → Policy (domain model).
 *
 * Used in:
 *   addPolicy    → no ID yet, pass null
 *   updatePolicy → ID comes from the URL path param
 */
public class PolicyRequestToPolicy implements Function<PolicyRequest, Policy> {

    private static final String OPERATOR_IN = "in";

    private final String policyId;

    public PolicyRequestToPolicy() {

        this.policyId = null;
    }

    public PolicyRequestToPolicy(String policyId) {

        this.policyId = policyId;
    }

    @Override
    public Policy apply(PolicyRequest policyRequest) {

        List<PolicyResource> resources = buildPolicyResources(policyRequest.getResources());
        return new Policy(policyId, policyRequest.getName(), null, resources);
    }

    private List<PolicyResource> buildPolicyResources(List<PolicyResourceRequest> resourceRequests) {

        if (resourceRequests == null || resourceRequests.isEmpty()) {
            return Collections.emptyList();
        }
        return resourceRequests.stream()
                .map(this::toRulePolicyResource)
                .collect(Collectors.toList());
    }

    private PolicyResource toRulePolicyResource(PolicyResourceRequest resourceRequest) {

        validateResourceType(resourceRequest.getResourceType());
        return new RulePolicyResource(null, resourceRequest.getTarget(), null, buildRule(resourceRequest.getRule()));
    }

    private void validateResourceType(PolicyResourceRequest.ResourceTypeEnum resourceType) {

        // resourceType is optional in the API and defaults to RULE; RULE is the only supported type.
        if (resourceType != null && resourceType != PolicyResourceRequest.ResourceTypeEnum.RULE) {
            throw PolicyManagementAPIErrorBuilder.handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_RESOURCE_TYPE, resourceType.toString());
        }
    }

    private Rule buildRule(RuleRequest ruleRequest) {

        // Each AND sub-rule maps to an ANDCombinedRule; all sub-rules are OR-combined.
        List<ANDCombinedRule> andRules = ruleRequest.getRules().stream()
                .map(andRuleRequest -> {
                    List<Expression> expressions = andRuleRequest.getExpressions().stream()
                            .map(this::buildExpression)
                            .collect(Collectors.toList());
                    return new ANDCombinedRule.Builder()
                            .setExpressions(expressions)
                            .build();
                })
                .collect(Collectors.toList());
        return new ORCombinedRule.Builder()
                .setRules(andRules)
                .build();
    }

    private Expression buildExpression(ExpressionRequest expressionRequest) {

        Expression.Builder builder = new Expression.Builder()
                .field(expressionRequest.getField())
                .operator(expressionRequest.getOperator());

        if (OPERATOR_IN.equals(expressionRequest.getOperator())) {
            builder.value(new Value(Value.Type.LIST, expressionRequest.getValue()));
        } else {
            builder.value(expressionRequest.getValue());
        }

        return builder.build();
    }
}
