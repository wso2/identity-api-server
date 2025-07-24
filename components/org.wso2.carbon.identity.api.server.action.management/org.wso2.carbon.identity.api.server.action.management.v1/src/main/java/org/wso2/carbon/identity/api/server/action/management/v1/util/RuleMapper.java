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

package org.wso2.carbon.identity.api.server.action.management.v1.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.action.management.api.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.api.model.Action;
import org.wso2.carbon.identity.action.management.api.model.ActionRule;
import org.wso2.carbon.identity.api.server.action.management.v1.ANDRule;
import org.wso2.carbon.identity.api.server.action.management.v1.ANDRuleResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ExpressionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ORRule;
import org.wso2.carbon.identity.api.server.action.management.v1.ORRuleResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants;
import org.wso2.carbon.identity.rule.management.api.exception.RuleManagementClientException;
import org.wso2.carbon.identity.rule.management.api.exception.RuleManagementException;
import org.wso2.carbon.identity.rule.management.api.model.ANDCombinedRule;
import org.wso2.carbon.identity.rule.management.api.model.Expression;
import org.wso2.carbon.identity.rule.management.api.model.FlowType;
import org.wso2.carbon.identity.rule.management.api.model.ORCombinedRule;
import org.wso2.carbon.identity.rule.management.api.util.RuleBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Mapper class for Rule.
 * This class maps the Rule object and its sub-objects into API model objects and vice-versa.
 */
public class RuleMapper {

    private static final Log LOG = LogFactory.getLog(RuleMapper.class);

    /**
     * Converts an ActionRule object from service model to an ORRuleResponse object from API model.
     *
     * @param actionRule ActionRule object.
     * @return ORRuleResponse object.
     * @throws ActionMgtException If an error occurs while converting the object.
     */
    public static ORRuleResponse toORRuleResponse(ActionRule actionRule) throws ActionMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Converting ActionRule to ORRuleResponse");
        }
        ORCombinedRule orCombinedRule = (ORCombinedRule) actionRule.getRule();
        List<ANDCombinedRule> andCombinedRuleList = orCombinedRule.getRules();

        List<ANDRuleResponse> andRuleResponseList = new ArrayList<>();
        for (ANDCombinedRule andCombinedRule : andCombinedRuleList) {
            List<Expression> expressionList = andCombinedRule.getExpressions();
            List<ExpressionResponse> expressionResponseList = new ArrayList<>();
            for (Expression expression : expressionList) {
                ExpressionResponse expressionResponse = new ExpressionResponse().field(expression.getField())
                        .operator(expression.getOperator())
                        .value(expression.getValue().getFieldValue());
                expressionResponseList.add(expressionResponse);
            }
            ANDRuleResponse andRuleResponse =
                    new ANDRuleResponse().condition(ANDRuleResponse.ConditionEnum.AND)
                            .expressions(expressionResponseList);
            andRuleResponseList.add(andRuleResponse);
        }

        ORRuleResponse response = new ORRuleResponse().condition(ORRuleResponse.ConditionEnum.OR)
                .rules(andRuleResponseList);
        if (LOG.isDebugEnabled()) {
            LOG.debug("ActionRule converted to ORRuleResponse successfully");
        }
        return response;
    }

    /**
     * Converts an ORRule object from API model to an ActionRule object from service model.
     *
     * @param ruleRequest  ORRule object.
     * @param actionType   Action type.
     * @param tenantDomain Tenant domain.
     * @return ActionRule object.
     * @throws ActionMgtException If an error occurs while converting the object.
     */
    public static ActionRule toActionRule(ORRule ruleRequest, Action.ActionTypes actionType, String tenantDomain)
            throws ActionMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Converting ORRule to ActionRule for type: " + (actionType != null ? actionType : "null") + 
                    " and tenant: " + tenantDomain);
        }
        List<ANDRule> andRuleList = ruleRequest.getRules();
        if (andRuleList == null || andRuleList.isEmpty()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No rules provided, creating ActionRule with null rule to indicate removal");
            }
            return ActionRule.create(null);
        }

        RuleBuilder ruleBuilder;
        try {
            ruleBuilder = RuleBuilder.create(getFlowType(actionType), tenantDomain);
        } catch (RuleManagementException e) {
            throw ActionMgtEndpointUtil.buildActionMgtServerException(
                    ActionMgtEndpointConstants.ErrorMessage.ERROR_WHILE_INITIALIZING_RULE_BUILDER, e);
        }

        addExpressionsToRuleBuilder(andRuleList, ruleBuilder);

        try {
            ActionRule actionRule = ActionRule.create(ruleBuilder.build());
            if (LOG.isDebugEnabled()) {
                LOG.debug("ORRule converted to ActionRule successfully for type: " + actionType);
            }
            return actionRule;
        } catch (RuleManagementClientException e) {
            LOG.warn("Failed to create ActionRule due to invalid rule for type: " + actionType + ". Error: " + 
                    e.getMessage(), e);
            throw ActionMgtEndpointUtil.buildActionMgtClientException(
                    ActionMgtEndpointConstants.ErrorMessage.ERROR_INVALID_RULE, e, e.getMessage());
        }
    }

    private static void addExpressionsToRuleBuilder(List<ANDRule> andRuleList, RuleBuilder ruleBuilder) {

        for (int i = 0; i < andRuleList.size(); i++) {
            if (i > 0) { // Add OR condition between AND conditions.
                ruleBuilder.addOrCondition();
            }

            List<org.wso2.carbon.identity.api.server.action.management.v1.Expression> expressionList =
                    andRuleList.get(i).getExpressions();
            for (org.wso2.carbon.identity.api.server.action.management.v1.Expression expression : expressionList) {
                ruleBuilder.addAndExpression(new Expression.Builder().field(expression.getField())
                        .operator(expression.getOperator()).value(expression.getValue()).build());
            }
        }
    }

    private static FlowType getFlowType(Action.ActionTypes actionType) {

        switch (actionType) {
            case PRE_ISSUE_ACCESS_TOKEN:
                return FlowType.PRE_ISSUE_ACCESS_TOKEN;
            case PRE_UPDATE_PASSWORD:
                return FlowType.PRE_UPDATE_PASSWORD;
            case PRE_UPDATE_PROFILE:
                return FlowType.PRE_UPDATE_PROFILE;
            default:
                throw ActionMgtEndpointUtil.handleException(Response.Status.NOT_IMPLEMENTED,
                        ActionMgtEndpointConstants.ErrorMessage.ERROR_NOT_IMPLEMENTED_ACTION_RULE_FLOW_TYPE);
        }
    }
}
