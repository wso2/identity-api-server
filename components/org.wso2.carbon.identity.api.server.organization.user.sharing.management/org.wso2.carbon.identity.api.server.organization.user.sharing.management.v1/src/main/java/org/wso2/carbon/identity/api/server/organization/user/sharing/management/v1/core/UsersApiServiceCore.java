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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.Error;
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
import java.util.UUID;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_GENERAL_USER_SHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_GENERAL_USER_UNSHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_SELECTIVE_USER_SHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_SELECTIVE_USER_UNSHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_UUID_FORMAT;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.RESPONSE_DETAIL_USER_SHARE;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.RESPONSE_DETAIL_USER_UNSHARE;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.RESPONSE_STATUS_PROCESSING;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.USER_IDS;

/**
 * Core service class for handling user sharing management APIs.
 */
public class UsersApiServiceCore {

    private static final Logger log = LoggerFactory.getLogger(UsersApiServiceCore.class);
    private final UserSharingPolicyHandlerService userSharingPolicyHandlerService;

    public UsersApiServiceCore(UserSharingPolicyHandlerService userSharingPolicyHandlerService) {

        this.userSharingPolicyHandlerService = userSharingPolicyHandlerService;
    }

    /**
     * Handles sharing a user across specific organizations.
     *
     * @param userShareRequestBody Contains details for user sharing.
     */
    public Response shareUser(UserShareRequestBody userShareRequestBody) {

        if (userShareRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_SELECTIVE_USER_SHARE_REQUEST_BODY))).build();
        }

        // Populate selectiveUserShareDO object from the request body.
        SelectiveUserShareDO selectiveUserShareDO = populateSelectiveUserShareDO(userShareRequestBody);

        try {
            userSharingPolicyHandlerService.populateSelectiveUserShare(selectiveUserShareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_USER_SHARE)).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            log.error("Error occurred while sharing user with specific organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Handles sharing a user across all organizations.
     *
     * @param userShareWithAllRequestBody Contains details for sharing users with all organizations.
     */
    public Response shareUserWithAll(UserShareWithAllRequestBody userShareWithAllRequestBody) {

        if (userShareWithAllRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_GENERAL_USER_SHARE_REQUEST_BODY))).build();
        }

        // Populate GeneralUserShareDO object from the request body.
        GeneralUserShareDO generalUserShareDO = populateGeneralUserShareDO(userShareWithAllRequestBody);

        try {
            userSharingPolicyHandlerService.populateGeneralUserShare(generalUserShareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_USER_SHARE)).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            log.error("Error occurred while sharing user with all organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Handles unsharing a user from specific organizations.
     *
     * @param userUnshareRequestBody Contains details for user unsharing.
     */
    public Response unshareUser(UserUnshareRequestBody userUnshareRequestBody) {

        if (userUnshareRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_SELECTIVE_USER_UNSHARE_REQUEST_BODY))).build();
        }

        // Populate SelectiveUserUnshareDO object from the request body.
        SelectiveUserUnshareDO selectiveUserUnshareDO = populateSelectiveUserUnshareDO(userUnshareRequestBody);

        try {
            userSharingPolicyHandlerService.populateSelectiveUserUnshare(selectiveUserUnshareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_USER_UNSHARE))
                    .build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            log.error("Error occurred while unsharing user from specific organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Handles removing a user's shared access from all organizations.
     *
     * @param userUnshareWithAllRequestBody Contains details for removing shared access.
     */
    public Response unshareUserWithAll(UserUnshareWithAllRequestBody userUnshareWithAllRequestBody) {

        if (userUnshareWithAllRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_GENERAL_USER_UNSHARE_REQUEST_BODY))).build();
        }

        // Populate GeneralUserUnshareDO object from the request body.
        GeneralUserUnshareDO generalUserUnshareDO = populateGeneralUserUnshareDO(userUnshareWithAllRequestBody);

        try {
            userSharingPolicyHandlerService.populateGeneralUserUnshare(generalUserUnshareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_USER_UNSHARE))
                    .build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            log.error("Error occurred while unsharing user from all organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
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
    public Response getSharedOrganizations(String userId, String after, String before,
                                           Integer limit, String filter, Boolean recursive) {

        if (userId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_UUID_FORMAT))).build();
        }

        try {
            ResponseSharedOrgsDO result =
                    userSharingPolicyHandlerService.getSharedOrganizationsOfUser(userId, after, before, limit, filter,
                            recursive);

            UserSharedOrganizationsResponse response = populateUserSharedOrganizationsResponse(result);
            return Response.ok().entity(response).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            log.error("Error occurred while retrieving organizations shared with user: " + userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
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
    public Response getSharedRoles(String userId, String orgId, String after, String before,
                                   Integer limit, String filter, Boolean recursive) {

        if (userId == null || orgId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_UUID_FORMAT))).build();
        }

        try {
            ResponseSharedRolesDO result =
                    userSharingPolicyHandlerService.getRolesSharedWithUserInOrganization(userId, orgId, after, before,
                            limit, filter, recursive);

            UserSharedRolesResponse response = populateUserSharedRolesResponse(result);
            return Response.ok().entity(response).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            log.error("Error occurred while retrieving roles shared with user: " + userId + " in org: " + orgId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Populates a SelectiveUserShareDO object from the provided UserShareRequestBody.
     *
     * @param userShareRequestBody Contains details for user sharing.
     * @return A populated SelectiveUserShareDO object.
     */
    private SelectiveUserShareDO populateSelectiveUserShareDO(UserShareRequestBody userShareRequestBody) {

        SelectiveUserShareDO selectiveUserShareDO = new SelectiveUserShareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria =
                populateUserCriteria(userShareRequestBody.getUserCriteria().getUserIds());
        selectiveUserShareDO.setUserCriteria(userCriteria);

        // Set organizations.
        List<SelectiveUserShareOrgDetailsDO> organizationsList = new ArrayList<>();
        for (UserShareRequestBodyOrganizations org : userShareRequestBody.getOrganizations()) {
            SelectiveUserShareOrgDetailsDO selectiveUserShareOrgDetailsDO = new SelectiveUserShareOrgDetailsDO();
            selectiveUserShareOrgDetailsDO.setOrganizationId(org.getOrgId());
            selectiveUserShareOrgDetailsDO.setPolicy(PolicyEnum.getPolicyByValue(org.getPolicy().value()));
            selectiveUserShareOrgDetailsDO.setRoles(populateRoleWithAudienceDO(org.getRoles()));
            organizationsList.add(selectiveUserShareOrgDetailsDO);
        }
        selectiveUserShareDO.setOrganizations(organizationsList);

        return selectiveUserShareDO;
    }

    private Map<String, UserCriteriaType> populateUserCriteria(List<String> userIdList) {

        Map<String, UserCriteriaType> userCriteria = new HashMap<>();
        UserCriteriaType userIds = new UserIdList(userIdList);
        userCriteria.put(USER_IDS, userIds);
        return userCriteria;
    }

    /**
     * Populates a GeneralUserShareDO object from the provided UserShareWithAllRequestBody.
     *
     * @param userShareWithAllRequestBody Contains details for sharing users with all organizations.
     * @return A populated GeneralUserShareDO object.
     */
    private GeneralUserShareDO populateGeneralUserShareDO(UserShareWithAllRequestBody userShareWithAllRequestBody) {

        GeneralUserShareDO generalUserShareDO = new GeneralUserShareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria =
                populateUserCriteria(userShareWithAllRequestBody.getUserCriteria().getUserIds());
        generalUserShareDO.setUserCriteria(userCriteria);

        // Set policy.
        generalUserShareDO.setPolicy(PolicyEnum.getPolicyByValue(userShareWithAllRequestBody.getPolicy().value()));

        // Set roles.
        List<RoleWithAudienceDO> rolesList = populateRoleWithAudienceDO(userShareWithAllRequestBody.getRoles());
        generalUserShareDO.setRoles(rolesList);
        return generalUserShareDO;
    }

    /**
     * Populates a SelectiveUserUnshareDO object from the provided UserUnshareRequestBody.
     *
     * @param userUnshareRequestBody Contains details for user unsharing.
     * @return A populated SelectiveUserUnshareDO object.
     */
    private SelectiveUserUnshareDO populateSelectiveUserUnshareDO(UserUnshareRequestBody userUnshareRequestBody) {

        SelectiveUserUnshareDO selectiveUserUnshareDO = new SelectiveUserUnshareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria =
                populateUserCriteria(userUnshareRequestBody.getUserCriteria().getUserIds());
        selectiveUserUnshareDO.setUserCriteria(userCriteria);

        // Set organizations.
        selectiveUserUnshareDO.setOrganizations(userUnshareRequestBody.getOrganizations());

        return selectiveUserUnshareDO;
    }

    /**
     * Populates a GeneralUserUnshareDO object from the provided UserUnshareWithAllRequestBody.
     *
     * @param userUnshareWithAllRequestBody Contains details for removing shared access.
     * @return A populated GeneralUserUnshareDO object.
     */
    private GeneralUserUnshareDO populateGeneralUserUnshareDO(
            UserUnshareWithAllRequestBody userUnshareWithAllRequestBody) {

        GeneralUserUnshareDO generalUserUnshareDO = new GeneralUserUnshareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria =
                populateUserCriteria(userUnshareWithAllRequestBody.getUserCriteria().getUserIds());
        generalUserUnshareDO.setUserCriteria(userCriteria);

        return generalUserUnshareDO;
    }

    /**
     * Populates a UserSharedOrganizationsResponse object from the provided ResponseSharedOrgsDO.
     *
     * @param result The ResponseSharedOrgsDO containing the shared organization's data.
     * @return A populated UserSharedOrganizationsResponse object.
     */
    private UserSharedOrganizationsResponse populateUserSharedOrganizationsResponse(ResponseSharedOrgsDO result) {

        List<UserSharedOrganizationsResponseLinks> responseLinks =
                populateUserSharedOrganizationsResponseLinks(result.getResponseLinks());

        List<UserSharedOrganizationsResponseSharedOrganizations> responseOrgs = new ArrayList<>();
        List<ResponseOrgDetailsDO> resultOrgDetails = result.getSharedOrgs();
        for (ResponseOrgDetailsDO resultOrgDetail : resultOrgDetails) {
            UserSharedOrganizationsResponseSharedOrganizations org =
                    new UserSharedOrganizationsResponseSharedOrganizations().orgId(
                                    resultOrgDetail.getOrganizationId())
                            .orgName(resultOrgDetail.getOrganizationName())
                            .sharedUserId(resultOrgDetail.getSharedUserId())
                            .sharedType(resultOrgDetail.getSharedType().toString())
                            .rolesRef(resultOrgDetail.getRolesRef());
            responseOrgs.add(org);
        }

        return new UserSharedOrganizationsResponse().links(responseLinks).sharedOrganizations(responseOrgs);
    }

    /**
     * Populates a UserSharedRolesResponse object from the provided ResponseSharedRolesDO.
     *
     * @param result The ResponseSharedRolesDO containing the shared role's data.
     * @return A populated UserSharedRolesResponse object.
     */
    private UserSharedRolesResponse populateUserSharedRolesResponse(ResponseSharedRolesDO result) {

        List<UserSharedOrganizationsResponseLinks> responseLinks =
                populateUserSharedOrganizationsResponseLinks(result.getResponseLinks());

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
     * Populates a list of RoleWithAudienceDO objects from the provided list of RoleWithAudience.
     *
     * @param roles The list of RoleWithAudience objects to be converted.
     * @return A list of RoleWithAudienceDO objects.
     */
    private List<RoleWithAudienceDO> populateRoleWithAudienceDO(List<RoleWithAudience> roles) {

        List<RoleWithAudienceDO> rolesList = new ArrayList<>();
        if (roles != null) {
            for (RoleWithAudience role : roles) {
                RoleWithAudienceDO roleDetails = new RoleWithAudienceDO();
                roleDetails.setRoleName(role.getDisplayName());
                roleDetails.setAudienceName(role.getAudience().getDisplay());
                roleDetails.setAudienceType(role.getAudience().getType());
                rolesList.add(roleDetails);
            }
        }
        return rolesList;
    }

    /**
     * Populates a list of UserSharedOrganizationsResponseLinks objects from the provided list of ResponseLinkDO.
     *
     * @param resultLinks The list of ResponseLinkDO objects to be converted.
     * @return A list of UserSharedOrganizationsResponseLinks objects.
     */
    private List<UserSharedOrganizationsResponseLinks> populateUserSharedOrganizationsResponseLinks(
            List<ResponseLinkDO> resultLinks) {

        List<UserSharedOrganizationsResponseLinks> responseLinks = new ArrayList<>();
        if (resultLinks != null) {
            for (ResponseLinkDO resultLink : resultLinks) {
                UserSharedOrganizationsResponseLinks links = new UserSharedOrganizationsResponseLinks();
                links.href(resultLink.getHref());
                links.rel(resultLink.getRel());
                responseLinks.add(links);
            }
        }
        return responseLinks;
    }

    /**
     * Constructs a success response object for a completed process.
     *
     * @param details Additional details or description about the process.
     * @return A {@link ProcessSuccessResponse} object containing the status and details of the process.
     */
    private ProcessSuccessResponse getProcessSuccessResponse(String details) {

        ProcessSuccessResponse processSuccessResponse = new ProcessSuccessResponse();
        processSuccessResponse.status(RESPONSE_STATUS_PROCESSING);
        processSuccessResponse.setDetails(details);
        return processSuccessResponse;
    }

    /**
     * Creates a UserSharingMgtClientException based on the provided error message.
     *
     * @param error The error message containing the code, message, and description.
     * @return A UserSharingMgtClientException with the specified error details.
     */
    private UserSharingMgtClientException makeRequestError(UserSharingMgtConstants.ErrorMessage error) {

        return new UserSharingMgtClientException(error.getCode(), error.getMessage(), error.getDescription());
    }

    /**
     * Builds a structured error response.
     *
     * @param e The exception containing error details.
     * @return An Error object containing the error code, message, description, and a trace ID.
     */
    private Error buildErrorResponse(UserSharingMgtException e) {

        return new Error().code(e.getErrorCode()).message(e.getMessage()).description(e.getDescription())
                .traceId(UUID.randomUUID());
    }
}
