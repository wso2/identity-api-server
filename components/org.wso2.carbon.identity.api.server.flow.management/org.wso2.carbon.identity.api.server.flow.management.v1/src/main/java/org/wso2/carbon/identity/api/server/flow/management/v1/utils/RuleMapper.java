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

import org.wso2.carbon.identity.api.server.flow.management.v1.AtomicCondition;
import org.wso2.carbon.identity.api.server.flow.management.v1.Condition;
import org.wso2.carbon.identity.api.server.flow.management.v1.ConditionValue;
import org.wso2.carbon.identity.api.server.flow.management.v1.FieldReference;
import org.wso2.carbon.identity.api.server.flow.management.v1.Rule;
import org.wso2.carbon.identity.api.server.flow.management.v1.RuleExpression;
import org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtClientException;
import org.wso2.carbon.identity.flow.mgt.model.RuleDTO;
import org.wso2.carbon.identity.rule.management.api.model.ANDCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.Expression;
import org.wso2.carbon.identity.rule.management.api.model.ORCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts rules and their expressions between the API model and the flow model.
 * <p>
 * On the far side an expression is rule management's own model, which is what evaluation takes and what is
 * stored: conditions become AND combined rules, atomic conditions become expressions, a qualifier becomes the
 * expression's key, and a value read from a field becomes a FIELD value. A rule with no expression is the
 * default and stays that way in both directions.
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
            if (ruleExpression.getConditions() != null) {
                for (Condition condition : ruleExpression.getConditions()) {
                    ANDCombinedRule.Builder andBuilder = new ANDCombinedRule.Builder();
                    if (condition.getAtomicConditions() != null) {
                        for (AtomicCondition atomicCondition : condition.getAtomicConditions()) {
                            andBuilder.addExpression(toRuleMgtExpression(atomicCondition));
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
        RuleExpression apiExpression = new RuleExpression().operator(RuleExpression.OperatorEnum.OR);
        for (ANDCombinedRule andCombinedRule : orCombinedRule.getRules()) {
            Condition condition = new Condition().operator(Condition.OperatorEnum.AND);
            for (Expression expression : andCombinedRule.getExpressions()) {
                condition.addAtomicConditionsItem(toAtomicCondition(expression));
            }
            apiExpression.addConditionsItem(condition);
        }
        return apiExpression;
    }

    /**
     * An atomic condition as rule management holds it. The qualifier is the expression's key; a literal is handed
     * over raw, exactly as the action management API does, leaving its type to the field definition, except a list,
     * which has no raw form and names its own type.
     */
    private static Expression toRuleMgtExpression(AtomicCondition atomicCondition) {

        Expression.Builder builder = new Expression.Builder()
                .field(atomicCondition.getField() == null ? null : atomicCondition.getField().getName())
                .key(atomicCondition.getField() == null ? null : atomicCondition.getField().getQualifier())
                .operator(atomicCondition.getOperator());

        ConditionValue value = atomicCondition.getValue();
        if (value.getType() == ConditionValue.TypeEnum.FIELD) {
            builder.value(Value.fieldOf(new org.wso2.carbon.identity.rule.management.api.model.FieldReference(
                    value.getField().getName(), value.getField().getQualifier())));
        } else if (value.getLiteral() instanceof List) {
            List<String> entries = new ArrayList<>();
            for (Object entry : (List<?>) value.getLiteral()) {
                entries.add(String.valueOf(entry));
            }
            builder.value(Value.listOf(entries));
        } else {
            builder.value(String.valueOf(value.getLiteral()));
        }
        return builder.build();
    }

    private static AtomicCondition toAtomicCondition(Expression expression) {

        return new AtomicCondition()
                .field(new FieldReference().name(expression.getField()).qualifier(expression.getKey()))
                .operator(expression.getOperator())
                .value(toConditionValue(expression.getValue()));
    }

    /**
     * A rule management value as the API shows it: a field reference for FIELD, an array for LIST, and the single
     * value as a string for everything else. The resolved type is not published, since it is derived from the field
     * definition and a client rebuilding the condition does not supply it.
     */
    private static ConditionValue toConditionValue(Value value) {

        if (value == null) {
            return null;
        }
        if (value.getType() == Value.Type.FIELD && value.getFieldReference() != null) {
            return new ConditionValue().type(ConditionValue.TypeEnum.FIELD)
                    .field(new FieldReference().name(value.getFieldReference().getName())
                            .qualifier(value.getFieldReference().getKey()));
        }
        ConditionValue literal = new ConditionValue().type(ConditionValue.TypeEnum.LITERAL);
        return value.getType() == Value.Type.LIST
                ? literal.literal(value.getFieldValues())
                : literal.literal(value.getFieldValue());
    }
}
