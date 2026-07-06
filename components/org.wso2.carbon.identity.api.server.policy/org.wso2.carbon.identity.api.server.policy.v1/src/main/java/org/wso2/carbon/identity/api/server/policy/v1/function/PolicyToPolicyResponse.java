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

import org.wso2.carbon.identity.api.server.policy.v1.model.ANDRuleResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.ExpressionResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.ExpressionValue;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResourceResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleResponse;
import org.wso2.carbon.identity.policy.management.api.model.Policy;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResource;
import org.wso2.carbon.identity.rule.management.api.model.ANDCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.Expression;
import org.wso2.carbon.identity.rule.management.api.model.ORCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.Rule;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper: Policy (domain model) → PolicyResponse (API model).
 *
 * Used after every successful operation to build the JSON response
 * that gets sent back to the client.
 */
public class PolicyToPolicyResponse implements Function<Policy, PolicyResponse> {

    private final Map<String, String> fieldDisplayNames;

    public PolicyToPolicyResponse() {

        this.fieldDisplayNames = Collections.emptyMap();
    }

    public PolicyToPolicyResponse(Map<String, String> fieldDisplayNames) {

        this.fieldDisplayNames = fieldDisplayNames != null ? fieldDisplayNames : Collections.emptyMap();
    }

    @Override
    public PolicyResponse apply(Policy policy) {

        PolicyResponse response = new PolicyResponse();
        response.setId(policy.getId());
        response.setName(policy.getName());

        if (policy.getResources() != null && !policy.getResources().isEmpty()) {
            List<PolicyResourceResponse> resourceResponses = policy.getResources().stream()
                    .map(this::toPolicyResourceResponse)
                    .collect(Collectors.toList());
            response.setResources(resourceResponses);
        }

        return response;
    }

    private PolicyResourceResponse toPolicyResourceResponse(PolicyResource policyResource) {

        PolicyResourceResponse resourceResponse = new PolicyResourceResponse();
        resourceResponse.setId(policyResource.getId());
        resourceResponse.setResourceId(policyResource.getResourceId());
        resourceResponse.setTarget(policyResource.getTarget());
        if (policyResource.getResourceType() != null) {
            resourceResponse.setResourceType(
                    PolicyResourceResponse.ResourceTypeEnum.fromValue(policyResource.getResourceType().name()));
        }
        if (policyResource.getRule() != null) {
            resourceResponse.setRule(toRuleResponse(policyResource.getRule()));
        }
        return resourceResponse;
    }

    private RuleResponse toRuleResponse(Rule rule) {

        ORCombinedRule orRule = (ORCombinedRule) rule;
        List<ANDRuleResponse> andGroups = orRule.getRules().stream()
                .map(this::toANDRuleResponse)
                .collect(Collectors.toList());
        RuleResponse ruleResponse = new RuleResponse();
        ruleResponse.setRules(andGroups);
        return ruleResponse;
    }

    private ANDRuleResponse toANDRuleResponse(ANDCombinedRule andRule) {

        List<ExpressionResponse> expressions = andRule.getExpressions().stream()
                .map(this::toExpressionResponse)
                .collect(Collectors.toList());
        ANDRuleResponse andRuleResponse = new ANDRuleResponse();
        andRuleResponse.setExpressions(expressions);
        return andRuleResponse;
    }

    private ExpressionResponse toExpressionResponse(Expression expression) {

        ExpressionValue expressionValue = new ExpressionValue();
        if (expression.getValue() != null) {
            expressionValue.setType(
                    ExpressionValue.TypeEnum.fromValue(expression.getValue().getType().name()));
            expressionValue.setValue(expression.getValue().getFieldValue());
        }
        ExpressionResponse expressionResponse = new ExpressionResponse();
        expressionResponse.setField(expression.getField());
        expressionResponse.setDisplayName(
                fieldDisplayNames.getOrDefault(expression.getField(), expression.getField()));
        expressionResponse.setOperator(expression.getOperator());
        expressionResponse.setValue(expressionValue);
        return expressionResponse;
    }
}
