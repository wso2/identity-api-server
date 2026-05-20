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

package org.wso2.carbon.identity.api.server.organization.capability.governance.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.organization.capability.governance.common.GovernancePolicyServiceHolder;
import org.wso2.carbon.identity.api.server.organization.capability.governance.common.constants.GovernancePolicyApiConstants.ErrorMessage;
import org.wso2.carbon.identity.api.server.organization.capability.governance.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.capability.governance.v1.model.GovernancePolicyEvaluateRequest;
import org.wso2.carbon.identity.api.server.organization.capability.governance.v1.model.GovernancePolicyEvaluateResponse;
import org.wso2.carbon.identity.organization.management.capability.governance.GovernancePolicyEvaluator;
import org.wso2.carbon.identity.organization.management.capability.governance.exception.GovernancePolicyMgtClientException;
import org.wso2.carbon.identity.organization.management.capability.governance.exception.GovernancePolicyMgtException;
import org.wso2.carbon.identity.organization.management.capability.governance.model.GovernancePolicyEvaluationResult;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;

import java.util.UUID;

import javax.ws.rs.core.Response;

/**
 * Core service for the governance policy evaluation API.
 */
public class OrgGovernanceApiServiceCore {

    private static final Log LOG = LogFactory.getLog(OrgGovernanceApiServiceCore.class);

    public Response evaluateGovernancePolicy(GovernancePolicyEvaluateRequest request) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        String orgId;
        try {
            orgId = GovernancePolicyServiceHolder.getOrganizationManager().resolveOrganizationId(tenantDomain);
        } catch (OrganizationManagementException e) {
            LOG.debug("Failed to resolve organization ID for tenant: " + tenantDomain, e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(
                            ErrorMessage.ERROR_RESOLVING_ORGANIZATION_ID.getCode(),
                            ErrorMessage.ERROR_RESOLVING_ORGANIZATION_ID.getMessage(),
                            ErrorMessage.ERROR_RESOLVING_ORGANIZATION_ID.getDescription()))
                    .build();
        }

        GovernancePolicyEvaluator evaluator = GovernancePolicyServiceHolder.getGovernancePolicyEvaluator();
        String capability = request.getCapability();
        String resourceType = request.getResourceType();

        try {
            GovernancePolicyEvaluationResult evaluationResult = evaluator.evaluate(orgId, capability, resourceType);

            GovernancePolicyEvaluateResponse result = new GovernancePolicyEvaluateResponse()
                    .resourceType(resourceType)
                    .capability(capability)
                    .allowed(evaluationResult.isAllowed());
            return Response.ok(result).build();

        } catch (GovernancePolicyMgtClientException e) {
            LOG.debug("Client error evaluating governance policy.", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(e.getErrorCode(), e.getMessage(), e.getDescription()))
                    .build();
        } catch (GovernancePolicyMgtException e) {
            LOG.error("Error evaluating governance policy for organization: " + orgId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(buildErrorResponse(
                            ErrorMessage.ERROR_EVALUATING_GOVERNANCE_POLICY.getCode(),
                            ErrorMessage.ERROR_EVALUATING_GOVERNANCE_POLICY.getMessage(),
                            ErrorMessage.ERROR_EVALUATING_GOVERNANCE_POLICY.getDescription()))
                    .build();
        }
    }

    private Error buildErrorResponse(String code, String message, String description) {

        return new Error()
                .code(code)
                .message(message)
                .description(description)
                .traceId(UUID.randomUUID().toString());
    }
}
