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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.policy.v1.model.ANDRuleResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.ExpressionResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.ExpressionValue;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleResponse;
import org.wso2.carbon.identity.rule.management.api.model.ANDCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.Expression;
import org.wso2.carbon.identity.rule.management.api.model.ORCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.Rule;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds a RuleResponse (API model) from a Rule (domain model).
 */
public class RuleResponseBuilder {

    private static final Log LOG = LogFactory.getLog(RuleResponseBuilder.class);

    private RuleResponseBuilder() {

    }

    public static RuleResponse buildRuleResponse(Rule rule) {

        if (!(rule instanceof ORCombinedRule)) {
            LOG.error("Unexpected top-level rule type: "
                    + (rule != null ? rule.getClass().getName() : "null")
                    + ". Expected an ORCombinedRule while building the policy response.");
            throw new IllegalStateException(
                    "Unsupported rule structure encountered while building the policy response.");
        }
        ORCombinedRule orRule = (ORCombinedRule) rule;
        List<ANDRuleResponse> andGroups = orRule.getRules().stream()
                .map(RuleResponseBuilder::toANDRuleResponse)
                .collect(Collectors.toList());
        RuleResponse ruleResponse = new RuleResponse();
        ruleResponse.setRules(andGroups);
        return ruleResponse;
    }

    private static ANDRuleResponse toANDRuleResponse(ANDCombinedRule andRule) {

        List<ExpressionResponse> expressions = andRule.getExpressions().stream()
                .map(RuleResponseBuilder::toExpressionResponse)
                .collect(Collectors.toList());
        ANDRuleResponse andRuleResponse = new ANDRuleResponse();
        andRuleResponse.setExpressions(expressions);
        return andRuleResponse;
    }

    private static ExpressionResponse toExpressionResponse(Expression expression) {

        ExpressionValue expressionValue = new ExpressionValue();
        if (expression.getValue() != null) {
            expressionValue.setType(
                    ExpressionValue.TypeEnum.fromValue(expression.getValue().getType().name()));
            expressionValue.setValue(expression.getValue().getFieldValue());
        }
        ExpressionResponse expressionResponse = new ExpressionResponse();
        expressionResponse.setField(expression.getField());
        expressionResponse.setOperator(expression.getOperator());
        expressionResponse.setValue(expressionValue);
        return expressionResponse;
    }
}
