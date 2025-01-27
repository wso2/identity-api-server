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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.core;

import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.RoleWithAudience;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.RoleWithAudienceAudience;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserShareRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserShareRequestBodyOrganizations;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserShareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserSharedOrganizationsResponseLinks;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserSharedOrganizationsResponseSharedOrganizations;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserSharedRolesResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserUnshareRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserUnshareWithAllRequestBody;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.UserSharingPolicyHandlerService;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.exception.UserSharingMgtClientException;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.exception.UserSharingMgtException;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.GeneralUserShareDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.GeneralUserUnshareDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.ResponseLinkDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.ResponseOrgDetailsDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.ResponseSharedOrgsDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.ResponseSharedRolesDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.RoleWithAudienceDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.SelectiveUserShareDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.SelectiveUserShareOrgDetailsDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.SelectiveUserUnshareDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.usercriteria.UserCriteriaType;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.usercriteria.UserIdList;
import org.wso2.carbon.identity.organization.resource.sharing.policy.management.constant.PolicyEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_GENERAL_USER_SHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_GENERAL_USER_UNSHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_SELECTIVE_USER_SHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_SELECTIVE_USER_UNSHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_UUID_FORMAT;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.USER_IDS;

/**
 * Core service class for handling user sharing management APIs.
 */
public class UsersApiServiceCore {

    private final UserSharingPolicyHandlerService userSharingPolicyHandlerService;

    public UsersApiServiceCore(UserSharingPolicyHandlerService userSharingPolicyHandlerService) {

        this.userSharingPolicyHandlerService = userSharingPolicyHandlerService;
    }

    /**
     * Handles sharing a user across specific organizations.
     *
     * @param userShareRequestBody Contains details for user sharing.
     */
    public void shareUser(UserShareRequestBody userShareRequestBody) throws UserSharingMgtException {

        if (userShareRequestBody == null) {
            throw new UserSharingMgtClientException(INVALID_SELECTIVE_USER_SHARE_REQUEST_BODY.getCode(),
                    INVALID_SELECTIVE_USER_SHARE_REQUEST_BODY.getMessage(),
                    INVALID_SELECTIVE_USER_SHARE_REQUEST_BODY.getDescription());
        }

        // Populate selectiveUserShareDO object from the request body.
        SelectiveUserShareDO selectiveUserShareDO = new SelectiveUserShareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria = new HashMap<>();
        UserCriteriaType userIds = new UserIdList(userShareRequestBody.getUserCriteria().getUserIds());
        userCriteria.put(USER_IDS, userIds);
        selectiveUserShareDO.setUserCriteria(userCriteria);

        // Set organizations.
        List<SelectiveUserShareOrgDetailsDO> organizationsList = new ArrayList<>();
        for (UserShareRequestBodyOrganizations org : userShareRequestBody.getOrganizations()) {
            SelectiveUserShareOrgDetailsDO selectiveUserShareOrgDetailsDO = new SelectiveUserShareOrgDetailsDO();
            selectiveUserShareOrgDetailsDO.setOrganizationId(org.getOrgId());
            selectiveUserShareOrgDetailsDO.setPolicy(PolicyEnum.getPolicyByValue(org.getPolicy().value()));

            List<RoleWithAudienceDO> roleWithAudiences = new ArrayList<>();

            for (RoleWithAudience role : org.getRoles()) {
                RoleWithAudienceDO roleWithAudienceDO = new RoleWithAudienceDO();
                roleWithAudienceDO.setRoleName(role.getDisplayName());
                roleWithAudienceDO.setAudienceName(role.getAudience().getDisplay());
                roleWithAudienceDO.setAudienceType(role.getAudience().getType());
                roleWithAudiences.add(roleWithAudienceDO);
            }

            selectiveUserShareOrgDetailsDO.setRoles(roleWithAudiences);

            organizationsList.add(selectiveUserShareOrgDetailsDO);
        }
        selectiveUserShareDO.setOrganizations(organizationsList);

        userSharingPolicyHandlerService.populateSelectiveUserShare(selectiveUserShareDO);
    }

    /**
     * Handles sharing a user across all organizations.
     *
     * @param userShareWithAllRequestBody Contains details for sharing users with all organizations.
     */
    public void shareUserWithAll(UserShareWithAllRequestBody userShareWithAllRequestBody)
            throws UserSharingMgtException {

        if (userShareWithAllRequestBody == null) {
            throw new UserSharingMgtClientException(INVALID_GENERAL_USER_SHARE_REQUEST_BODY.getCode(),
                    INVALID_GENERAL_USER_SHARE_REQUEST_BODY.getMessage(),
                    INVALID_GENERAL_USER_SHARE_REQUEST_BODY.getDescription());
        }

        // Populate GeneralUserShareDO object from the request body.
        GeneralUserShareDO generalUserShareDO = new GeneralUserShareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria = new HashMap<>();
        UserCriteriaType userIds = new UserIdList(userShareWithAllRequestBody.getUserCriteria().getUserIds());
        userCriteria.put(USER_IDS, userIds);
        generalUserShareDO.setUserCriteria(userCriteria);

        // Set policy.
        generalUserShareDO.setPolicy(PolicyEnum.getPolicyByValue(userShareWithAllRequestBody.getPolicy().value()));

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
        generalUserShareDO.setRoles(rolesList);

        userSharingPolicyHandlerService.populateGeneralUserShare(generalUserShareDO);
    }

    /**
     * Handles unsharing a user from specific organizations.
     *
     * @param userUnshareRequestBody Contains details for user unsharing.
     */
    public void unshareUser(UserUnshareRequestBody userUnshareRequestBody) throws UserSharingMgtException {

        if (userUnshareRequestBody == null) {
            throw new UserSharingMgtClientException(INVALID_SELECTIVE_USER_UNSHARE_REQUEST_BODY.getCode(),
                    INVALID_SELECTIVE_USER_UNSHARE_REQUEST_BODY.getMessage(),
                    INVALID_SELECTIVE_USER_UNSHARE_REQUEST_BODY.getDescription());
        }

        // Populate SelectiveUserUnshareDO object from the request body.
        SelectiveUserUnshareDO selectiveUserUnshareDO = new SelectiveUserUnshareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria = new HashMap<>();
        UserCriteriaType userIds = new UserIdList(userUnshareRequestBody.getUserCriteria().getUserIds());
        userCriteria.put(USER_IDS, userIds);
        selectiveUserUnshareDO.setUserCriteria(userCriteria);

        // Set organizations.
        selectiveUserUnshareDO.setOrganizations(userUnshareRequestBody.getOrganizations());

        userSharingPolicyHandlerService.populateSelectiveUserUnshare(selectiveUserUnshareDO);
    }

    /**
     * Handles removing a user's shared access from all organizations.
     *
     * @param userUnshareWithAllRequestBody Contains details for removing shared access.
     */
    public void unshareUserWithAll(UserUnshareWithAllRequestBody userUnshareWithAllRequestBody)
            throws UserSharingMgtException {

        if (userUnshareWithAllRequestBody == null) {
            throw new UserSharingMgtClientException(INVALID_GENERAL_USER_UNSHARE_REQUEST_BODY.getCode(),
                    INVALID_GENERAL_USER_UNSHARE_REQUEST_BODY.getMessage(),
                    INVALID_GENERAL_USER_UNSHARE_REQUEST_BODY.getDescription());
        }

        // Populate GeneralUserUnshareDO object from the request body.
        GeneralUserUnshareDO generalUserUnshareDO = new GeneralUserUnshareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria = new HashMap<>();
        UserCriteriaType userIds = new UserIdList(userUnshareWithAllRequestBody.getUserCriteria().getUserIds());
        userCriteria.put(USER_IDS, userIds);
        generalUserUnshareDO.setUserCriteria(userCriteria);

        userSharingPolicyHandlerService.populateGeneralUserUnshare(generalUserUnshareDO);
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
                                                                  Integer limit, String filter, Boolean recursive)
            throws UserSharingMgtException {

        if (userId == null) {
            throw new UserSharingMgtClientException(INVALID_UUID_FORMAT.getCode(),
                    INVALID_UUID_FORMAT.getMessage(),
                    INVALID_UUID_FORMAT.getDescription());
        }

        ResponseSharedOrgsDO result =
                userSharingPolicyHandlerService.getSharedOrganizationsOfUser(userId, after, before, limit, filter,
                        recursive);

        List<UserSharedOrganizationsResponseLinks> responseLinks = new ArrayList<>();
        List<ResponseLinkDO> resultLinks = result.getResponseLinks();
        for (ResponseLinkDO resultLink : resultLinks) {
            UserSharedOrganizationsResponseLinks links = new UserSharedOrganizationsResponseLinks();
            links.href(resultLink.getHref());
            links.rel(resultLink.getRel());
            responseLinks.add(links);
        }

        List<UserSharedOrganizationsResponseSharedOrganizations> reponseOrgs = new ArrayList<>();
        List<ResponseOrgDetailsDO> resultOrgDetails = result.getSharedOrgs();
        for (ResponseOrgDetailsDO resultOrgDetail : resultOrgDetails) {
            UserSharedOrganizationsResponseSharedOrganizations org =
                    new UserSharedOrganizationsResponseSharedOrganizations().orgId(resultOrgDetail.getOrganizationId())
                            .orgName(resultOrgDetail.getOrganizationName())
                            .sharedUserId(resultOrgDetail.getSharedUserId()).sharedType(resultOrgDetail.getSharedType())
                            .rolesRef(resultOrgDetail.getRolesRef());
            reponseOrgs.add(org);
        }

        return new UserSharedOrganizationsResponse().links(responseLinks).sharedOrganizations(reponseOrgs);
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
                                                  Integer limit, String filter, Boolean recursive)
            throws UserSharingMgtException {

        if (userId == null || orgId == null) {
            throw new UserSharingMgtClientException(INVALID_UUID_FORMAT.getCode(),
                    INVALID_UUID_FORMAT.getMessage(),
                    INVALID_UUID_FORMAT.getDescription());
        }

        ResponseSharedRolesDO result =
                userSharingPolicyHandlerService.getRolesSharedWithUserInOrganization(userId, orgId, after, before,
                        limit, filter, recursive);

        List<UserSharedOrganizationsResponseLinks> responseLinks = new ArrayList<>();
        List<ResponseLinkDO> resultLinks = result.getResponseLinks();
        for (ResponseLinkDO resultLink : resultLinks) {
            UserSharedOrganizationsResponseLinks links = new UserSharedOrganizationsResponseLinks();
            links.href(resultLink.getHref());
            links.rel(resultLink.getRel());
            responseLinks.add(links);
        }

        List<RoleWithAudience> responseRoles = new ArrayList<>();
        List<RoleWithAudienceDO> resultRoleDetails = result.getSharedRoles();

        for (RoleWithAudienceDO resultRoleDetail : resultRoleDetails) {
            RoleWithAudience roleWithAudience = new RoleWithAudience();
            roleWithAudience.setDisplayName(resultRoleDetail.getRoleName());
            roleWithAudience.setAudience(new RoleWithAudienceAudience()
                    .display(resultRoleDetail.getAudienceName())
                    .type(resultRoleDetail.getAudienceType()));
            responseRoles.add(roleWithAudience);
        }

        return new UserSharedRolesResponse().links(responseLinks).roles(responseRoles);
    }

    /**
     * Constructs a success response object for a completed process.
     *
     * @param status  The status of the process (e.g., "Processing").
     * @param details Additional details or description about the process.
     * @return A {@link ProcessSuccessResponse} object containing the status and details of the process.
     */
    public ProcessSuccessResponse getProcessSuccessResponse(String status, String details) {

        ProcessSuccessResponse processSuccessResponse = new ProcessSuccessResponse();
        processSuccessResponse.status(status);
        processSuccessResponse.setDetails(details);
        return processSuccessResponse;
    }
}
