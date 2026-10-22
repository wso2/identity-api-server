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

package org.wso2.carbon.identity.api.server.flow.management.v1.utils;

import org.wso2.carbon.identity.api.server.flow.management.v1.AtomicCondition;
import org.wso2.carbon.identity.api.server.flow.management.v1.Branch;
import org.wso2.carbon.identity.api.server.flow.management.v1.Condition;
import org.wso2.carbon.identity.api.server.flow.management.v1.ConditionValue;
import org.wso2.carbon.identity.api.server.flow.management.v1.FieldReference;
import org.wso2.carbon.identity.api.server.flow.management.v1.Rule;
import org.wso2.carbon.identity.flow.mgt.model.BranchDTO;
import org.wso2.carbon.identity.rule.management.api.model.ANDCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.Expression;
import org.wso2.carbon.identity.rule.management.api.model.ORCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts the branches of a decision step, and their rules, between the API model and the flow management model.
 */
public class RuleMapper {

    private RuleMapper() {

    }

    /**
     * Convert the branches of a decision step from the API model to the flow management model.
     *
     * @param branches Branches in the API model.
     * @return Branches in the flow management model, or null when no branches are given.
     */
    public static List<BranchDTO> toBranchDTOs(List<Branch> branches) {

        if (branches == null) {
            return null;
        }
        List<BranchDTO> branchDTOs = new ArrayList<>();
        for (Branch branch : branches) {
            branchDTOs.add(new BranchDTO.Builder()
                    .id(branch.getId())
                    .name(branch.getName())
                    .nextId(branch.getNext())
                    .rule(toORCombinedRule(branch))
                    .build());
        }
        return branchDTOs;
    }

    /**
     * Convert the branches of a decision step from the flow management model to the API model, in the order they
     * are stored.
     *
     * @param branchDTOs Branches in the flow management model.
     * @return Branches in the API model, or null when no branches are given.
     */
    public static List<Branch> toBranches(List<BranchDTO> branchDTOs) {

        if (branchDTOs == null) {
            return null;
        }
        List<Branch> branches = new ArrayList<>();
        for (BranchDTO branchDTO : branchDTOs) {
            Branch branch = new Branch()
                    .id(branchDTO.getId())
                    .name(branchDTO.getName())
                    .next(branchDTO.getNextId())
                    .rule(toRule(branchDTO.getRule()));
            branches.add(branch);
        }
        return branches;
    }

    /**
     * Convert a branch's rule from the API model to the rule management model.
     *
     * @param branch Branch whose rule is converted.
     * @return The rule in the rule management model, or null for the default branch, which has no rule.
     */
    private static ORCombinedRule toORCombinedRule(Branch branch) {

        Rule rule = branch.getRule();
        if (rule == null) {
            return null;
        }
        try {
            ORCombinedRule.Builder orBuilder = new ORCombinedRule.Builder();
            if (rule.getConditions() != null) {
                for (Condition condition : rule.getConditions()) {
                    ANDCombinedRule.Builder andBuilder = new ANDCombinedRule.Builder();
                    if (condition.getConditions() != null) {
                        for (AtomicCondition atomicCondition : condition.getConditions()) {
                            andBuilder.addExpression(toRuleMgtExpression(atomicCondition));
                        }
                    }
                    orBuilder.addRule(andBuilder.build());
                }
            }
            return orBuilder.build();
        } catch (IllegalArgumentException e) {
            throw Utils.handleDecisionStepException("Invalid rule.",
                    "A condition in the branch %s is not valid: %s", branch.getName(), e.getMessage());
        }
    }

    /**
     * Convert a rule from the rule management model to the API model.
     *
     * @param orCombinedRule Rule in the rule management model.
     * @return The rule in the API model, or null when there is no rule.
     */
    private static Rule toRule(ORCombinedRule orCombinedRule) {

        if (orCombinedRule == null) {
            return null;
        }
        Rule apiRule = new Rule().operator(Rule.OperatorEnum.OR);
        for (ANDCombinedRule andCombinedRule : orCombinedRule.getRules()) {
            Condition condition = new Condition().operator(Condition.OperatorEnum.AND);
            for (Expression expression : andCombinedRule.getExpressions()) {
                condition.addConditionsItem(toAtomicCondition(expression));
            }
            apiRule.addConditionsItem(condition);
        }
        return apiRule;
    }

    /**
     * Convert a condition from the API model to a rule management expression. A literal value is passed as text,
     * and its type is resolved from the field definition, as the action management API does. A list of values is
     * passed as a LIST value.
     *
     * @param atomicCondition Condition in the API model.
     * @return The condition as a rule management expression.
     */
    private static Expression toRuleMgtExpression(AtomicCondition atomicCondition) {

        Expression.Builder builder = new Expression.Builder()
                .field(atomicCondition.getField() == null ? null : atomicCondition.getField().getName())
                .fieldQualifier(atomicCondition.getField() == null ? null : atomicCondition.getField().getQualifier())
                .operator(atomicCondition.getOperator());

        ConditionValue value = atomicCondition.getValue();
        if (value == null) {
            return builder.build();
        }
        if (value.getType() == ConditionValue.TypeEnum.FIELD) {
            FieldReference field = value.getField();
            builder.value(new Value(new org.wso2.carbon.identity.rule.management.api.model.FieldReference(
                    field == null ? null : field.getName(), field == null ? null : field.getQualifier())));
        } else if (value.getLiteral() instanceof List) {
            List<String> entries = new ArrayList<>();
            for (Object entry : (List<?>) value.getLiteral()) {
                entries.add(toLiteralText(entry));
            }
            builder.value(new Value(entries));
        } else if (value.getLiteral() != null) {
            builder.value(toLiteralText(value.getLiteral()));
        }
        return builder.build();
    }

    /**
     * Convert a literal value to text. A number is written in plain form, so 18.0 and 1e1 become 18 and 10.
     *
     * @param literal Literal value from the request.
     * @return The literal as text.
     */
    private static String toLiteralText(Object literal) {

        if (literal instanceof Number) {
            try {
                return new BigDecimal(literal.toString()).stripTrailingZeros().toPlainString();
            } catch (NumberFormatException e) {
                return String.valueOf(literal);
            }
        }
        return String.valueOf(literal);
    }

    /**
     * Convert a rule management expression to a condition in the API model.
     *
     * @param expression Rule management expression.
     * @return The expression as a condition in the API model.
     */
    private static AtomicCondition toAtomicCondition(Expression expression) {

        return new AtomicCondition()
                .field(new FieldReference().name(expression.getField()).qualifier(expression.getFieldQualifier()))
                .operator(expression.getOperator())
                .value(toConditionValue(expression.getValue()));
    }

    /**
     * Convert a rule management value to the API model: a field reference for a FIELD value, a list for a LIST
     * value, and a single value as text otherwise. The resolved value type is not returned, because it is derived
     * from the field definition.
     *
     * @param value Rule management value.
     * @return The value in the API model, or null when there is no value.
     */
    private static ConditionValue toConditionValue(Value value) {

        if (value == null) {
            return null;
        }
        if (value.getType() == Value.Type.FIELD) {
            if (value.getFieldReference() == null) {
                return null;
            }
            FieldReference field = new FieldReference()
                    .name(value.getFieldReference().getName())
                    .qualifier(value.getFieldReference().getQualifier());
            return new ConditionValue().type(ConditionValue.TypeEnum.FIELD).field(field);
        }
        ConditionValue literal = new ConditionValue().type(ConditionValue.TypeEnum.LITERAL);
        return value.getType() == Value.Type.LIST
                ? literal.literal(value.getFieldValues())
                : literal.literal(value.getFieldValue());
    }
}
