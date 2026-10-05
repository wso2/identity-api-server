/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.flow.management.v1.utils;

import org.wso2.carbon.identity.api.server.flow.management.v1.ANDRule;
import org.wso2.carbon.identity.api.server.flow.management.v1.Expression;
import org.wso2.carbon.identity.api.server.flow.management.v1.Rule;
import org.wso2.carbon.identity.api.server.flow.management.v1.RuleExpression;
import org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtClientException;
import org.wso2.carbon.identity.flow.mgt.model.RuleDTO;
import org.wso2.carbon.identity.rule.management.api.model.ANDCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.ORCombinedRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts rules and their expressions between the API model and the flow model.
 * <p>
 * The expression itself is rule management's own model on the far side, which is what evaluation
 * takes, so nothing here reshapes it -- it only crosses the boundary. A rule with no expression is
 * the default and stays that way in both directions.
 */
public class RuleMapper {

    private RuleMapper() {

    }

    public static List<RuleDTO> toRuleDTOs(List<Rule> rules) {

        if (rules == null) {
            return null;
        }
        List<RuleDTO> ruleDTOs = new ArrayList<>();
        for (Rule rule : rules) {
            ruleDTOs.add(new RuleDTO.Builder()
                    .id(rule.getId())
                    .name(rule.getName())
                    .nextId(rule.getNext())
                    .expression(toORCombinedRule(rule))
                    .build());
        }
        return ruleDTOs;
    }

    /**
     * Rules come back in evaluation order, with the default last however it was submitted, so a
     * response reads top to bottom the way it is evaluated.
     */
    public static List<Rule> toRules(List<RuleDTO> ruleDTOs) {

        if (ruleDTOs == null) {
            return null;
        }
        List<Rule> guarded = new ArrayList<>();
        List<Rule> defaults = new ArrayList<>();
        for (RuleDTO ruleDTO : ruleDTOs) {
            Rule rule = new Rule()
                    .id(ruleDTO.getId())
                    .name(ruleDTO.getName())
                    .next(ruleDTO.getNextId())
                    .expression(toRuleExpression(ruleDTO.getExpression()));
            if (ruleDTO.isDefault()) {
                defaults.add(rule);
            } else {
                guarded.add(rule);
            }
        }
        guarded.addAll(defaults);
        return guarded;
    }

    private static ORCombinedRule toORCombinedRule(Rule rule) {

        RuleExpression ruleExpression = rule.getExpression();
        if (ruleExpression == null) {
            return null;
        }
        try {
            ORCombinedRule.Builder orBuilder = new ORCombinedRule.Builder();
            if (ruleExpression.getRules() != null) {
                for (ANDRule andRule : ruleExpression.getRules()) {
                    ANDCombinedRule.Builder andBuilder = new ANDCombinedRule.Builder();
                    if (andRule.getExpressions() != null) {
                        for (Expression expression : andRule.getExpressions()) {
                            andBuilder.addExpression(toRuleMgtExpression(expression));
                        }
                    }
                    orBuilder.addRule(andBuilder.build());
                }
            }
            return orBuilder.build();
        } catch (IllegalArgumentException e) {
            // The rule model refuses to be built from what was sent, which is the caller's mistake.
            throw Utils.handleFlowMgtException(new FlowMgtClientException(
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNREADABLE_RULE_EXPRESSION.getCode(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNREADABLE_RULE_EXPRESSION.getMessage(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNREADABLE_RULE_EXPRESSION.getDescription()),
                    rule.getName(), e.getMessage());
        }
    }

    private static RuleExpression toRuleExpression(ORCombinedRule orCombinedRule) {

        if (orCombinedRule == null) {
            return null;
        }
        RuleExpression apiExpression = new RuleExpression().condition(RuleExpression.ConditionEnum.OR);
        for (ANDCombinedRule andCombinedRule : orCombinedRule.getRules()) {
            ANDRule apiAndRule = new ANDRule().condition(ANDRule.ConditionEnum.AND);
            for (org.wso2.carbon.identity.rule.management.api.model.Expression expression
                    : andCombinedRule.getExpressions()) {
                apiAndRule.addExpressionsItem(toApiExpression(expression));
            }
            apiExpression.addRulesItem(apiAndRule);
        }
        return apiExpression;
    }

    private static org.wso2.carbon.identity.rule.management.api.model.Expression toRuleMgtExpression(
            Expression expression) {

        org.wso2.carbon.identity.rule.management.api.model.Expression.Builder builder =
                new org.wso2.carbon.identity.rule.management.api.model.Expression.Builder()
                        .field(expression.getField())
                        .key(expression.getKey())
                        .operator(expression.getOperator());

        // A set of values has no raw form, so a list names its own type. A scalar is handed over raw,
        // exactly as the action management API does, leaving the rule builder to resolve its type
        // from the field definition.
        if (expression.getValues() != null) {
            builder.value(org.wso2.carbon.identity.rule.management.api.model.Value.listOf(expression.getValues()));
        } else {
            builder.value(expression.getValue());
        }

        return builder.build();
    }

    private static Expression toApiExpression(
            org.wso2.carbon.identity.rule.management.api.model.Expression expression) {

        Expression apiExpression = new Expression()
                .field(expression.getField())
                .key(expression.getKey())
                .operator(expression.getOperator());
        setApiValue(apiExpression, expression.getValue());
        return apiExpression;
    }

    /**
     * Writes a rule management value back onto an API expression, as a list for a list value and as a
     * plain string for everything else. The resolved type is not published, since it is derived from
     * the field definition and a client rebuilding the expression does not supply it.
     *
     * @param expression    API expression to write onto.
     * @param ruleMgtValue  Value from the rule management model.
     */
    private static void setApiValue(Expression expression,
                                    org.wso2.carbon.identity.rule.management.api.model.Value ruleMgtValue) {

        if (ruleMgtValue == null) {
            return;
        }
        if (ruleMgtValue.getType() == org.wso2.carbon.identity.rule.management.api.model.Value.Type.LIST) {
            expression.setValues(ruleMgtValue.getFieldValues());
        } else {
            expression.setValue(ruleMgtValue.getFieldValue());
        }
    }
}
