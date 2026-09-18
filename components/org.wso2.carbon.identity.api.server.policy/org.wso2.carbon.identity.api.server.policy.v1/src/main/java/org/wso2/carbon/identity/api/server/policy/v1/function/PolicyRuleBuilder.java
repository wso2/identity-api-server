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
import org.wso2.carbon.identity.api.server.policy.v1.model.ANDRuleRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.ExpressionRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleRequest;
import org.wso2.carbon.identity.api.server.policy.v1.util.PolicyManagementAPIErrorBuilder;
import org.wso2.carbon.identity.rule.management.api.exception.RuleManagementClientException;
import org.wso2.carbon.identity.rule.management.api.exception.RuleManagementException;
import org.wso2.carbon.identity.rule.management.api.model.Expression;
import org.wso2.carbon.identity.rule.management.api.model.FlowType;
import org.wso2.carbon.identity.rule.management.api.model.Rule;
import org.wso2.carbon.identity.rule.management.api.model.Value;
import org.wso2.carbon.identity.rule.management.api.util.RuleBuilder;

import java.util.List;
import javax.ws.rs.core.Response;

/**
 * Builds a Rule (domain model) from a RuleRequest (API model).
 *
 * Rules are built through {@link RuleBuilder} so that each expression value is validated and
 * resolved against the device-policy field metadata. This is what assigns the correct value type
 * (e.g. SYMBOLIC for OS version fields such as LATEST_ANDROID); hand-constructing the Value here
 * would persist symbolic tokens as plain LIST/RAW values and fail at rule evaluation time.
 */
public class PolicyRuleBuilder {

    private static final String OPERATOR_IN = "in";

    private PolicyRuleBuilder() {

    }

    public static Rule buildRule(RuleRequest ruleRequest, String tenantDomain) {

        RuleBuilder ruleBuilder;
        try {
            ruleBuilder = RuleBuilder.create(FlowType.DEVICE_POLICY, tenantDomain);
        } catch (RuleManagementException e) {
            throw PolicyManagementAPIErrorBuilder.handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_INITIALIZING_RULE_BUILDER, e);
        }

        // Each AND sub-rule maps to an ANDCombinedRule; all sub-rules are OR-combined.
        List<ANDRuleRequest> andRules = ruleRequest.getRules();
        for (int i = 0; i < andRules.size(); i++) {
            if (i > 0) { // Add OR condition between AND conditions.
                ruleBuilder.addOrCondition();
            }
            for (ExpressionRequest expressionRequest : andRules.get(i).getExpressions()) {
                Expression.Builder expressionBuilder = new Expression.Builder()
                        .field(expressionRequest.getField())
                        .operator(expressionRequest.getOperator());
                // The 'in' operator carries a comma-separated multi-value; type it as LIST so each token is
                // validated (and resolved) individually. Single-value operators pass the raw value through.
                if (OPERATOR_IN.equals(expressionRequest.getOperator())) {
                    expressionBuilder.value(new Value(Value.Type.LIST, expressionRequest.getValue()));
                } else {
                    expressionBuilder.value(expressionRequest.getValue());
                }
                ruleBuilder.addAndExpression(expressionBuilder.build());
            }
        }

        try {
            return ruleBuilder.build();
        } catch (RuleManagementClientException e) {
            throw PolicyManagementAPIErrorBuilder.handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_RULE, e.getMessage());
        }
    }
}
