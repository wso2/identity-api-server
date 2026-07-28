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

package org.wso2.carbon.identity.api.server.policy.v1.core;

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.Util;
import org.wso2.carbon.identity.api.server.policy.common.Constants;
import org.wso2.carbon.identity.api.server.policy.v1.function.PolicyBuilder;
import org.wso2.carbon.identity.api.server.policy.v1.function.PolicyResponseBuilder;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyListItem;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyListLink;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyListResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyUpdateRequest;
import org.wso2.carbon.identity.api.server.policy.v1.util.PolicyManagementAPIErrorBuilder;
import org.wso2.carbon.identity.policy.management.api.exception.PolicyManagementException;
import org.wso2.carbon.identity.policy.management.api.model.Policy;
import org.wso2.carbon.identity.policy.management.api.model.PolicyBasicInfo;
import org.wso2.carbon.identity.policy.management.api.service.PolicyManagementService;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

/**
 * Core service for the Policy API — handles policy CRUD operations.
 */
public class PolicyManagementService {

    private static final int DEFAULT_LIMIT = 30;
    private static final int DEFAULT_OFFSET = 0;

    private final PolicyManagementService policyManagementService;

    public PolicyManagementService(PolicyManagementService policyManagementService) {

        this.policyManagementService = policyManagementService;
    }

    /**
     * Create a new policy.
     *
     * @param policyRequest Policy creation request.
     * @return Created policy response.
     */
    public PolicyResponse addPolicy(PolicyRequest policyRequest) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            Policy policy = PolicyBuilder.buildPolicy(policyRequest, null, tenantDomain);
            Policy createdPolicy = policyManagementService.addPolicy(policy, tenantDomain);
            return PolicyResponseBuilder.buildPolicyResponse(createdPolicy);
        } catch (PolicyManagementException e) {
            throw PolicyManagementAPIErrorBuilder.handleException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_POLICY);
        }
    }

    /**
     * Get a policy by its ID.
     *
     * @param policyId ID of the policy to retrieve.
     * @return Policy response.
     */
    public PolicyResponse getPolicyById(String policyId) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            Policy policy = policyManagementService.getPolicyById(policyId, tenantDomain);
            if (policy == null) {
                throw PolicyManagementAPIErrorBuilder.handleException(Response.Status.NOT_FOUND,
                        Constants.ErrorMessage.ERROR_CODE_POLICY_NOT_FOUND, policyId);
            }
            return PolicyResponseBuilder.buildPolicyResponse(policy);
        } catch (PolicyManagementException e) {
            throw PolicyManagementAPIErrorBuilder.handleException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_POLICY);
        }
    }

    /**
     * Update an existing policy.
     *
     * @param policyId            ID of the policy to update.
     * @param policyUpdateRequest Policy update request.
     * @return Updated policy response.
     */
    public PolicyResponse updatePolicy(String policyId, PolicyUpdateRequest policyUpdateRequest) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            Policy policy = PolicyBuilder.buildUpdatingPolicy(policyUpdateRequest, policyId, tenantDomain);
            Policy updatedPolicy = policyManagementService.updatePolicy(policy, tenantDomain);
            return PolicyResponseBuilder.buildPolicyResponse(updatedPolicy);
        } catch (PolicyManagementException e) {
            throw PolicyManagementAPIErrorBuilder.handleException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_POLICY);
        }
    }

    /**
     * Delete a policy by its ID.
     *
     * @param policyId ID of the policy to delete.
     */
    public void deletePolicy(String policyId) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            policyManagementService.deletePolicy(policyId, tenantDomain);
        } catch (PolicyManagementException e) {
            throw PolicyManagementAPIErrorBuilder.handleException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_DELETING_POLICY);
        }
    }

    /**
     * Get a paginated list of policy summaries for the current tenant, optionally filtered by name.
     *
     * @param limit  Maximum number of records to return (defaults to 30 when null).
     * @param offset Number of records to skip (defaults to 0 when null).
     * @param filter Name substring filter; null or blank returns all policies.
     * @return Paginated policy list response.
     */
    public PolicyListResponse getPolicies(Integer limit, Integer offset, String filter) {

        int resolvedLimit = limit != null ? limit : DEFAULT_LIMIT;
        int resolvedOffset = offset != null ? offset : DEFAULT_OFFSET;
        validatePaginationParameters(resolvedLimit, resolvedOffset);

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            int totalResults = policyManagementService.getPolicyCount(tenantDomain, filter);
            List<PolicyBasicInfo> policies =
                    policyManagementService.getPolicies(tenantDomain, filter, resolvedOffset, resolvedLimit);

            List<PolicyListItem> items = policies.stream()
                    .map(this::toPolicyListItem)
                    .collect(Collectors.toList());

            List<PolicyListLink> links = Util.buildPaginationLinks(
                            resolvedLimit, resolvedOffset, totalResults, Constants.POLICY_PATH_COMPONENT, null, filter)
                    .entrySet().stream()
                    .map(link -> new PolicyListLink().rel(link.getKey()).href(link.getValue()))
                    .collect(Collectors.toList());

            return new PolicyListResponse()
                    .totalResults(totalResults)
                    .startIndex(resolvedOffset + 1)
                    .count(items.size())
                    .policies(items)
                    .links(links);
        } catch (PolicyManagementException e) {
            throw PolicyManagementAPIErrorBuilder.handleException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_POLICIES);
        }
    }

    private PolicyListItem toPolicyListItem(PolicyBasicInfo policy) {

        return new PolicyListItem()
                .id(policy.getId())
                .name(policy.getName())
                .self(ContextLoader.buildURIForBody(
                        Constants.V1_API_PATH_COMPONENT + Constants.POLICY_PATH_COMPONENT
                                + "/" + policy.getId()).toString());
    }

    private void validatePaginationParameters(int limit, int offset) {

        if (limit < 1 || offset < 0) {
            throw PolicyManagementAPIErrorBuilder.handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_PAGINATION);
        }
    }
}
