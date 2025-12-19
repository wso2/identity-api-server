package org.wso2.carbon.identity.rest.api.server.workflow.v1.util;

import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.*;
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

        FlowType temporaryWFType = FlowType.WORKFLOW_RULES; // (later I will do this with workflowOperation passing)
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
                        for (org.wso2.carbon.identity.rule.management.api.model.Expression expr : andCombinedRule.getExpressions()) {
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

//    private static FlowType getFlowType(Operation operation) throws WorkflowClientException {
//        // Map Workflow Operations to Rule Flow Types
//        switch (operation) {
//            case PRE_UPDATE_ROLE:
//                return FlowType.PRE_UPDATE_ROLE;
//            default:
//                throw new WorkflowClientException("Operation " + operation + " does not support rules.");
//        }
//    }

}