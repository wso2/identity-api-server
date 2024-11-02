/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.core;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.UserSharingMgtConstants;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.RoleWithAudience;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserShareRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserShareRequestBodyOrganizations;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserShareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserSharedRolesResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserUnshareRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserUnshareWithAllRequestBody;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.UserSharingPolicyHandlerServiceImpl;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.RoleWithAudienceDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.UserShareGeneralDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.UserShareSelectiveDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.UserShareSelectiveOrgDetailsDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.UserUnshareGeneralDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.UserUnshareSelectiveDO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * Core service class for handling user sharing management APIs.
 */
public class UsersApiServiceCore {

    /**
     * Handles sharing a user across specific organizations.
     *
     * @param userShareRequestBody Contains details for user sharing.
     */
    public void shareUser(UserShareRequestBody userShareRequestBody) {

        UserSharingPolicyHandlerServiceImpl userSharingPolicyHandlerService = new UserSharingPolicyHandlerServiceImpl();

        // Populate UserShareSelectiveDO object from the request body.
        UserShareSelectiveDO userShareSelectiveDO = new UserShareSelectiveDO();

        // Set user criteria.
        Map<String, List<String>> userCriteria = new HashMap<>();
        userCriteria.put("userIds", userShareRequestBody.getUserCriteria().getUserIds());
        userShareSelectiveDO.setUserCriteria(userCriteria);

        // Set organizations.
        List<UserShareSelectiveOrgDetailsDO> organizationsList = new ArrayList<>();
        for (UserShareRequestBodyOrganizations org : userShareRequestBody.getOrganizations()) {
            UserShareSelectiveOrgDetailsDO userShareSelectiveOrgDetailsDO = new UserShareSelectiveOrgDetailsDO();
            userShareSelectiveOrgDetailsDO.setOrganizationId(org.getOrgId());
            userShareSelectiveOrgDetailsDO.setPolicy(org.getPolicy().value());

            List<RoleWithAudienceDO> roleWithAudiences = new ArrayList<>();

            for(RoleWithAudience role : org.getRoles()) {
                RoleWithAudienceDO roleWithAudienceDO = new RoleWithAudienceDO();
                roleWithAudienceDO.setRoleName(role.getDisplayName());
                roleWithAudienceDO.setAudienceName(role.getAudience().getDisplay());
                roleWithAudienceDO.setAudienceType(role.getAudience().getType());
                roleWithAudiences.add(roleWithAudienceDO);
            }

            userShareSelectiveOrgDetailsDO.setRoles(roleWithAudiences);

            organizationsList.add(userShareSelectiveOrgDetailsDO);
        }
        userShareSelectiveDO.setOrganizations(organizationsList);

        try {
            userSharingPolicyHandlerService.propagateUserSelectiveShare(userShareSelectiveDO);
        } catch (Exception e) {
            // TODO: Handle exceptions in selective share API
        }
    }

    /**
     * Handles sharing a user across all organizations.
     *
     * @param userShareWithAllRequestBody Contains details for sharing users with all organizations.
     */
    public void shareUserWithAll(UserShareWithAllRequestBody userShareWithAllRequestBody) {

        UserSharingPolicyHandlerServiceImpl userSharingPolicyHandlerService = new UserSharingPolicyHandlerServiceImpl();

        // Populate UserShareGeneralDO object from the request body.
        UserShareGeneralDO userShareGeneralDO = new UserShareGeneralDO();

        // Set user criteria.
        Map<String, List<String>> userCriteria = new HashMap<>();
        userCriteria.put("userIds", userShareWithAllRequestBody.getUserCriteria().getUserIds());
        userShareGeneralDO.setUserCriteria(userCriteria);

        // Set policy.
        userShareGeneralDO.setPolicy(userShareWithAllRequestBody.getPolicy().value());

        // Set roles.
        List<RoleWithAudienceDO> rolesList = new ArrayList<>();
        if (userShareWithAllRequestBody.getRoles() != null) {
            for (RoleWithAudience role : userShareWithAllRequestBody.getRoles()) {
                RoleWithAudienceDO roleDetails = new RoleWithAudienceDO();
                roleDetails.setRoleName(role.getDisplayName());
                roleDetails.setAudienceName(role.getAudience().getDisplay());
                roleDetails.setAudienceType(role.getAudience().getType());
                rolesList.add(roleDetails);
            }
        }
        userShareGeneralDO.setRoles(rolesList);

        try {
            userSharingPolicyHandlerService.propagateUserGeneralShare(userShareGeneralDO);
        } catch (Exception e) {
            // TODO: Handle exceptions in shareUserWithAll API
        }
    }

    /**
     * Handles unsharing a user from specific organizations.
     *
     * @param userUnshareRequestBody Contains details for user unsharing.
     */
    public void unshareUser(UserUnshareRequestBody userUnshareRequestBody) {

        UserSharingPolicyHandlerServiceImpl userSharingPolicyHandlerService = new UserSharingPolicyHandlerServiceImpl();

        // Populate UserUnshareSelectiveDO object from the request body.
        UserUnshareSelectiveDO userUnshareSelectiveDO = new UserUnshareSelectiveDO();

        // Set user criteria.
        Map<String, List<String>> userCriteria = new HashMap<>();
        userCriteria.put("userIds", userUnshareRequestBody.getUserCriteria().getUserIds());
        userUnshareSelectiveDO.setUserCriteria(userCriteria);

        // Set organizations.
        userUnshareSelectiveDO.setOrganizations(userUnshareRequestBody.getOrganizations());

        try {
            userSharingPolicyHandlerService.propagateUserSelectiveUnshare(userUnshareSelectiveDO);
        } catch (Exception e) {
            // TODO: Handle exceptions in unshareUser API
        }
    }

    /**
     * Handles removing a user's shared access from all organizations.
     *
     * @param userUnshareWithAllRequestBody Contains details for removing shared access.
     */
    public void unshareUserWithAll(UserUnshareWithAllRequestBody userUnshareWithAllRequestBody) {

        UserSharingPolicyHandlerServiceImpl userSharingPolicyHandlerService = new UserSharingPolicyHandlerServiceImpl();

        // Populate UserUnshareGeneralDO object from the request body.
        UserUnshareGeneralDO userUnshareGeneralDO = new UserUnshareGeneralDO();

        // Set user criteria.
        Map<String, List<String>> userCriteria = new HashMap<>();
        userCriteria.put("userIds", userUnshareWithAllRequestBody.getUserCriteria().getUserIds());
        userUnshareGeneralDO.setUserCriteria(userCriteria);

        try {
            userSharingPolicyHandlerService.propagateUserGeneralUnshare(userUnshareGeneralDO);
        } catch (Exception e) {
            // TODO: Handle exceptions in unshareUserWithAll API
        }
    }

    /**
     * Retrieves the organizations that a user has access to.
     *
     * @param userId    The ID of the user.
     * @param after     The cursor for the next page.
     * @param before    The cursor for the previous page.
     * @param limit     The maximum number of results per page.
     * @param filter    The filter criteria.
     * @param recursive Whether to include child organizations.
     * @return UserSharedOrganizationsResponse containing accessible organizations.
     */
    public UserSharedOrganizationsResponse getSharedOrganizations(String userId, String after, String before,
                                                                  Integer limit, String filter, Boolean recursive) {
        // Core logic to retrieve shared organizations
        UserSharedOrganizationsResponse response = new UserSharedOrganizationsResponse();
        // Populate the response with shared organizations
        return response;
    }

    /**
     * Retrieves the roles assigned to a user within a specified organization.
     *
     * @param userId    The ID of the user.
     * @param orgId     The ID of the organization.
     * @param after     The cursor for the next page.
     * @param before    The cursor for the previous page.
     * @param limit     The maximum number of results per page.
     * @param filter    The filter criteria.
     * @param recursive Whether to include child roles.
     * @return UserSharedRolesResponse containing shared roles.
     */
    public UserSharedRolesResponse getSharedRoles(String userId, String orgId, String after, String before,
                                                  Integer limit, String filter, Boolean recursive) {
        // Core logic to retrieve shared roles for the user in the specified organization
        UserSharedRolesResponse response = new UserSharedRolesResponse();
        // Populate the response with shared roles
        return response;
    }


    private boolean isUnsupportedParamAvailable(Integer limit, Integer offset, String sortOrder, String sortBy) {

        if (limit != null) {
            throw handleException(BAD_REQUEST, UserSharingMgtConstants.ErrorMessage
                    .ERROR_CODE_UNSUPPORTED_LIMIT, String.valueOf(limit));
        }
        if (offset != null) {
            throw handleException(BAD_REQUEST, UserSharingMgtConstants.ErrorMessage
                    .ERROR_CODE_UNSUPPORTED_OFFSET, String.valueOf(offset));
        }
        if (StringUtils.isNotBlank(sortOrder)) {
            throw handleException(BAD_REQUEST, UserSharingMgtConstants.ErrorMessage
                    .ERROR_CODE_UNSUPPORTED_SORT_ORDER, sortOrder);
        }
        if (StringUtils.isNotBlank(sortBy)) {
            throw handleException(BAD_REQUEST, UserSharingMgtConstants.ErrorMessage
                    .ERROR_CODE_UNSUPPORTED_SORT_BY, sortBy);
        }
        return false;
    }

    /**
     * Helper method to handle exceptions.
     *
     * @param status   HTTP status of the error.
     * @param error Error message to be returned.
     * @param data     Additional error data.
     * @return APIError object for error response.
     */
    private APIError handleException(Response.Status status, UserSharingMgtConstants.ErrorMessage error,
                                     String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    private ErrorResponse.Builder getErrorBuilder(UserSharingMgtConstants.ErrorMessage errorMsg, String data) {

        return new ErrorResponse.Builder()
                .withCode(errorMsg.getCode())
                .withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    private String includeData(UserSharingMgtConstants.ErrorMessage error, String data) {

        if (StringUtils.isNotBlank(data)) {
            return String.format(error.getDescription(), data);
        }
        return error.getDescription();
    }

}