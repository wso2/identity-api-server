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

package org.wso2.carbon.identity.rest.api.server.workflow.v1.util;

import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.ANDRule;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.ANDRuleResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.Expression;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.ExpressionResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.ORRule;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.ORRuleResponse;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.Operation;
import org.wso2.carbon.identity.rule.management.api.exception.RuleManagementException;
import org.wso2.carbon.identity.rule.management.api.model.ANDCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.FlowType;
import org.wso2.carbon.identity.rule.management.api.model.ORCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.Rule;
import org.wso2.carbon.identity.rule.management.api.util.RuleBuilder;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowClientException;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for mapping between Workflow API rule models and the generic Rule Management service models.
 */
public class WorkflowRuleMapper {

    /**
     * Maps the API ORRule model to the generic Service Rule model.
     * @param workflowORRule The ORRule from the Workflow API.
     * @param tenantDomain The tenant domain.
     * @param workflowOperation The workflow operation.
     * @return The mapped Service Rule.
     */
    public static Rule mapApiRuleToServiceRule(ORRule workflowORRule, String tenantDomain, Operation workflowOperation)
            throws RuleManagementException, WorkflowClientException {

        if (workflowORRule == null || workflowORRule.getRules() == null) {
            return null;
        }

        FlowType temporaryWFType = FlowType.APPROVAL_WORKFLOW;
        List<ANDRule> andRuleList = workflowORRule.getRules();

        RuleBuilder ruleBuilder = RuleBuilder.create(temporaryWFType, tenantDomain);

        addExpressionsToRuleBuilder(andRuleList, ruleBuilder);

        return ruleBuilder.build();
    }

    /**
     * Maps the generic Service Rule model to the API ORRule model.
     * @param serviceRule The Service Rule from the Rule Management service.
     * @return The mapped API ORRule.
     */
    public static ORRuleResponse mapServiceRuleToApiRule(Rule serviceRule) {

        // Validation and Casting (Same as Action Service)
        if (serviceRule == null || !(serviceRule instanceof ORCombinedRule)) {
            return null;
        }
        ORCombinedRule orCombinedRule = (ORCombinedRule) serviceRule;

        // Prepare the Response Object
        ORRuleResponse orRuleResponse = new ORRuleResponse();
        orRuleResponse.setCondition(ORRuleResponse.ConditionEnum.OR);
        List<ANDRuleResponse> apiAndRules = new ArrayList<>();

        // Iterate through the AND Rules
        if (orCombinedRule.getRules() != null) {
            for (Rule rule : orCombinedRule.getRules()) {
                if (rule instanceof ANDCombinedRule) {
                    ANDCombinedRule andCombinedRule = (ANDCombinedRule) rule;

                    ANDRuleResponse andRuleResponse = new ANDRuleResponse();
                    andRuleResponse.setCondition(ANDRuleResponse.ConditionEnum.AND);
                    List<ExpressionResponse> apiExpressions = new ArrayList<>();

                    // Map Expressions
                    if (andCombinedRule.getExpressions() != null) {
                        for (org.wso2.carbon.identity.rule.management.api.model.Expression expr :
                                andCombinedRule.getExpressions()) {
                            ExpressionResponse exprResponse = new ExpressionResponse();
                            exprResponse.setField(expr.getField());
                            exprResponse.setOperator(expr.getOperator());

                            // Get the raw value string
                            if (expr.getValue() != null) {
                                exprResponse.setValue(expr.getValue().getFieldValue());
                            }

                            apiExpressions.add(exprResponse);
                        }
                    }
                    andRuleResponse.setExpressions(apiExpressions);
                    apiAndRules.add(andRuleResponse);
                }
            }
        }

        orRuleResponse.setRules(apiAndRules);
        return orRuleResponse;
    }


    private static void addExpressionsToRuleBuilder(List<ANDRule> andRuleList, RuleBuilder ruleBuilder) {

        for (int i = 0; i < andRuleList.size(); i++) {
            if (i > 0) {
                // Add OR condition between AND conditions.
                ruleBuilder.addOrCondition();
            }

            // Use the Expression class from YOUR Workflow API model package
            List<Expression> expressionList = andRuleList.get(i).getExpressions();

            if (expressionList != null) {
                for (Expression expression : expressionList) {
                    // Map to Backend Expression Builder
                    ruleBuilder.addAndExpression(
                            new org.wso2.carbon.identity.rule.management.api.model.Expression.Builder()
                                    .field(expression.getField())
                                    .operator(expression.getOperator())
                                    .value(expression.getValue())
                                    .build()
                    );
                }
            }
        }
    }
}
