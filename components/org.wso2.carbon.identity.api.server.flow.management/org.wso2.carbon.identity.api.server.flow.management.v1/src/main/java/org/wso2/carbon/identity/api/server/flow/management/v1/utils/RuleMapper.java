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
import org.wso2.carbon.identity.api.server.flow.management.v1.Branch;
import org.wso2.carbon.identity.api.server.flow.management.v1.Expression;
import org.wso2.carbon.identity.api.server.flow.management.v1.Rule;
import org.wso2.carbon.identity.flow.mgt.model.BranchDTO;
import org.wso2.carbon.identity.rule.management.api.model.ANDCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.ORCombinedRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts branches and their conditions between the API model and the flow model.
 * <p>
 * The condition itself is rule management's own model on the far side, which is what evaluation
 * takes, so nothing here reshapes a rule -- it only crosses the boundary. A branch with no rule is
 * the default and stays that way in both directions.
 */
public class RuleMapper {

    private RuleMapper() {

    }

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
                    .rule(toRuleMgtRule(branch.getRule()))
                    .build());
        }
        return branchDTOs;
    }

    /**
     * Branches come back in evaluation order, with the default last however it was submitted, so a
     * response reads top to bottom the way it is evaluated.
     */
    public static List<Branch> toBranches(List<BranchDTO> branchDTOs) {

        if (branchDTOs == null) {
            return null;
        }
        List<Branch> guarded = new ArrayList<>();
        List<Branch> defaults = new ArrayList<>();
        for (BranchDTO branchDTO : branchDTOs) {
            Branch branch = new Branch()
                    .id(branchDTO.getId())
                    .name(branchDTO.getName())
                    .next(branchDTO.getNextId())
                    .rule(toApiRule(branchDTO.getRule()));
            if (branchDTO.isDefault()) {
                defaults.add(branch);
            } else {
                guarded.add(branch);
            }
        }
        guarded.addAll(defaults);
        return guarded;
    }

    private static ORCombinedRule toRuleMgtRule(Rule rule) {

        if (rule == null) {
            return null;
        }
        ORCombinedRule.Builder orBuilder = new ORCombinedRule.Builder();
        if (rule.getRules() != null) {
            for (ANDRule andRule : rule.getRules()) {
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
    }

    private static Rule toApiRule(ORCombinedRule rule) {

        if (rule == null) {
            return null;
        }
        Rule apiRule = new Rule().condition(Rule.ConditionEnum.OR);
        for (ANDCombinedRule andCombinedRule : rule.getRules()) {
            ANDRule apiAndRule = new ANDRule().condition(ANDRule.ConditionEnum.AND);
            for (org.wso2.carbon.identity.rule.management.api.model.Expression expression
                    : andCombinedRule.getExpressions()) {
                apiAndRule.addExpressionsItem(toApiExpression(expression));
            }
            apiRule.addRulesItem(apiAndRule);
        }
        return apiRule;
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
        if (expression.getValues() != null && !expression.getValues().isEmpty()) {
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
