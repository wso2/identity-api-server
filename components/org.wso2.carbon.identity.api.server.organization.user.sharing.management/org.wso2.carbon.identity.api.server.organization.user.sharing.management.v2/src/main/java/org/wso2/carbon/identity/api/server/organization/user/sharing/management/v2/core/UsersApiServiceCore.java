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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.Error;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.Link;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.RoleAssignment;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.RoleShareConfig;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.RoleShareConfigAudience;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.SharingMode;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserCriteria;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserOrgShareConfig;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserShareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserSharedOrganization;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserSharingPatchOperation;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserSharingPatchRequest;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserUnshareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserUnshareSelectedRequestBody;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.UserSharingPolicyHandlerServiceV2;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.constant.RoleAssignmentMode;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.constant.UserSharePatchOperation;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.exception.UserSharingMgtClientException;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.exception.UserSharingMgtException;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.GeneralUserShareV2DO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.GeneralUserUnshareDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.GetUserSharedOrgsDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.PatchOperationDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.ResponseOrgDetailsV2DO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.ResponseSharedOrgsV2DO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.RoleAssignmentDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.RoleWithAudienceDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.SelectiveUserShareOrgDetailsV2DO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.SelectiveUserShareV2DO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.SelectiveUserUnshareDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.SharingModeDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.dos.UserSharePatchDO;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.usercriteria.UserCriteriaType;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.models.usercriteria.UserIdList;
import org.wso2.carbon.identity.organization.resource.sharing.policy.management.constant.PolicyEnum;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.ERROR_EMPTY_USER_SHARE_PATCH_PATH;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.ERROR_MISSING_USER_CRITERIA;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.ERROR_MISSING_USER_IDS;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.ERROR_UNSUPPORTED_USER_SHARE_PATCH_PATH;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.ERROR_UNSUPPORTED_USER_SHARE_POLICY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_GENERAL_USER_SHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_GENERAL_USER_UNSHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_SELECTIVE_USER_SHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_SELECTIVE_USER_UNSHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.INVALID_USER_SHARE_PATCH_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.RESPONSE_DETAIL_USER_SHARE;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.RESPONSE_DETAIL_USER_SHARE_PATCH;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.RESPONSE_DETAIL_USER_UNSHARE;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.RESPONSE_STATUS_PROCESSING;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.USER_IDS;

/**
 * Core service class for handling user sharing management APIs V2.
 */
public class UsersApiServiceCore {

    private static final Log LOG = LogFactory.getLog(UsersApiServiceCore.class);
    private final UserSharingPolicyHandlerServiceV2 userSharingPolicyHandlerServiceV2;

    /**
     * Creates the Users API v2 core service with the required backend service dependency.
     *
     * @param userSharingPolicyHandlerServiceV2 Backend service used to perform user sharing operations.
     */
    public UsersApiServiceCore(UserSharingPolicyHandlerServiceV2 userSharingPolicyHandlerServiceV2) {

        this.userSharingPolicyHandlerServiceV2 = userSharingPolicyHandlerServiceV2;
    }

    // Public Methods corresponding to the endpoints.

    /**
     * Shares users with the organizations specified in the request, applying per-organization policy and role
     * assignment configuration.
     *
     * @param userShareSelectedRequestBody Request payload containing user selection criteria and target organizations.
     * @return JAX-RS response with 202 (Accepted) on success, or an error response on validation/processing failures.
     */
    public Response shareUsersWithSelectedOrgs(UserShareSelectedRequestBody userShareSelectedRequestBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Selective user sharing API v2 invoked form tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (userShareSelectedRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_SELECTIVE_USER_SHARE_REQUEST_BODY))).build();
        }

        try {
            SelectiveUserShareV2DO selectiveUserShareV2DO =
                    populateSelectiveUserShareV2DO(userShareSelectedRequestBody);
            userSharingPolicyHandlerServiceV2.populateSelectiveUserShareV2(selectiveUserShareV2DO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_USER_SHARE)).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            LOG.error("Error occurred while sharing user with specific organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Shares users with organizations according to the provided global policy and role assignment configuration.
     *
     * @param userShareAllRequestBody Request payload containing user selection criteria and global sharing
     *                                configuration.
     * @return JAX-RS response with 202 (Accepted) on success, or an error response on validation/processing failures.
     */
    public Response shareUsersWithAllOrgs(UserShareAllRequestBody userShareAllRequestBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("General user sharing API v2 invoked form tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (userShareAllRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_GENERAL_USER_SHARE_REQUEST_BODY))).build();
        }

        try {
            GeneralUserShareV2DO generalUserShareV2DO = populateGeneralUserShareV2DO(userShareAllRequestBody);
            userSharingPolicyHandlerServiceV2.populateGeneralUserShareV2(generalUserShareV2DO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_USER_SHARE)).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            LOG.error("Error occurred while sharing user with all organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Revokes user sharing from the organizations specified in the request.
     *
     * @param userUnshareSelectedRequestBody Request payload containing user selection criteria and organization IDs to
     *                                       unshare.
     * @return JAX-RS response with 202 (Accepted) on success, or an error response on validation/processing failures.
     */
    public Response unshareUsersFromSelectedOrgs(UserUnshareSelectedRequestBody userUnshareSelectedRequestBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Selective user un-sharing API v2 invoked form tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (userUnshareSelectedRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_SELECTIVE_USER_UNSHARE_REQUEST_BODY))).build();
        }

        try {
            SelectiveUserUnshareDO selectiveUserUnshareDO =
                    populateSelectiveUserUnshareDO(userUnshareSelectedRequestBody);
            userSharingPolicyHandlerServiceV2.populateSelectiveUserUnshareV2(selectiveUserUnshareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_USER_UNSHARE))
                    .build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            LOG.error("Error occurred while unsharing user from specific organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Revokes all user sharing associations for the users identified by the given criteria.
     *
     * @param userUnshareAllRequestBody Request payload containing user selection criteria.
     * @return JAX-RS response with 202 (Accepted) on success, or an error response on validation/processing failures.
     */
    public Response unshareUsersFromAllOrgs(UserUnshareAllRequestBody userUnshareAllRequestBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("General user un-sharing API v2 invoked form tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (userUnshareAllRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_GENERAL_USER_UNSHARE_REQUEST_BODY))).build();
        }

        try {
            GeneralUserUnshareDO generalUserUnshareDO = populateGeneralUserUnshareDO(userUnshareAllRequestBody);
            userSharingPolicyHandlerServiceV2.populateGeneralUserUnshareV2(generalUserUnshareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_USER_UNSHARE))
                    .build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            LOG.error("Error occurred while unsharing user from all organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Applies patch operations to update attributes of already-shared users (for example, assigned roles within shared
     * organizations), based on the provided patch request.
     *
     * @param userSharingPatchRequest Request payload containing user selection criteria and patch operations.
     * @return JAX-RS response with 202 (Accepted) on success, or an error response on validation/processing failures.
     */
    public Response patchUserSharing(UserSharingPatchRequest userSharingPatchRequest) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Patch user sharing API v2 invoked form tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (userSharingPatchRequest == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_USER_SHARE_PATCH_REQUEST_BODY))).build();
        }

        try {
            UserSharePatchDO userSharePatchDO = populateUserSharePatch(userSharingPatchRequest);
            userSharingPolicyHandlerServiceV2.updateRoleAssignmentV2(userSharePatchDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_USER_SHARE_PATCH))
                    .build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            LOG.error("Error occurred while patching shared user attributes.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Retrieves the list of organizations shared with a specific user, supporting cursor-based pagination and optional
     * response shaping based on the provided query parameters.
     *
     * @param userId     ID of the user whose shared organizations are to be retrieved.
     * @param before     Cursor for pagination to get results before the specified cursor.
     * @param after      Cursor for pagination to get results after the specified cursor.
     * @param filter     Filter string to narrow down the results based on organization attributes.
     * @param limit      Maximum number of results to return.
     * @param recursive  Whether to include organizations shared with parent organizations recursively.
     * @param attributes Comma-separated list of additional attributes to include in the response.
     * @return JAX-RS response containing the list of shared organizations or an error response on failure.
     */
    public Response getUserSharedOrganizations(String userId, String before, String after, String filter, Integer limit,
                                               Boolean recursive, String attributes) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get user shared organizations API v2 invoked form tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain() + " of user: " + userId);
        }
        if (userId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(ERROR_MISSING_USER_IDS))).build();
        }

        try {
            GetUserSharedOrgsDO getUserSharedOrgsDO =
                    new GetUserSharedOrgsDO(userId, null, before, after, filter, limit, recursive,
                            splitAttributes(attributes));
            ResponseSharedOrgsV2DO result =
                    userSharingPolicyHandlerServiceV2.getUserSharedOrganizationsV2(getUserSharedOrgsDO);

            UserSharedOrganizationsResponse response =
                    mapUserSharedOrganizationsResponse(result, userId, filter, limit, recursive, attributes);
            return Response.ok().entity(response).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            LOG.error("Error occurred while retrieving organizations shared with user: " + userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    // Helper methods to build the relevant DTOs to send to the service layer.

    /**
     * Populates a SelectiveUserShareDO object from the provided UserShareRequestBody.
     *
     * @param userShareSelectedRequestBody Contains details for user sharing.
     * @return A populated SelectiveUserShareDO object.
     */
    private SelectiveUserShareV2DO populateSelectiveUserShareV2DO(
            UserShareSelectedRequestBody userShareSelectedRequestBody) throws UserSharingMgtClientException {

        SelectiveUserShareV2DO selectiveUserShareV2DO = new SelectiveUserShareV2DO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria =
                buildUserCriteriaFromRequest(userShareSelectedRequestBody.getUserCriteria());
        selectiveUserShareV2DO.setUserCriteria(userCriteria);

        // Set organizations details - policy and roles for each organization.
        List<SelectiveUserShareOrgDetailsV2DO> organizationsList = new ArrayList<>();
        for (UserOrgShareConfig orgDetail : userShareSelectedRequestBody.getOrganizations()) {
            if (orgDetail != null) {
                SelectiveUserShareOrgDetailsV2DO selectiveUserShareOrgDetailsV2DO =
                        new SelectiveUserShareOrgDetailsV2DO();
                selectiveUserShareOrgDetailsV2DO.setOrganizationId(orgDetail.getOrgId());
                selectiveUserShareOrgDetailsV2DO.setPolicy(resolvePolicy(orgDetail.getPolicy()));
                selectiveUserShareOrgDetailsV2DO.setRoleAssignments(
                        buildRoleAssignmentFromRequest(orgDetail.getRoleAssignment()));
                organizationsList.add(selectiveUserShareOrgDetailsV2DO);
            }
        }
        selectiveUserShareV2DO.setOrganizations(organizationsList);

        return selectiveUserShareV2DO;
    }

    /**
     * Populates a GeneralUserShareDO object from the provided UserShareWithAllRequestBody.
     *
     * @param userShareAllRequestBody Contains details for sharing users with all organizations.
     * @return A populated GeneralUserShareDO object.
     */
    private GeneralUserShareV2DO populateGeneralUserShareV2DO(UserShareAllRequestBody userShareAllRequestBody)
            throws UserSharingMgtClientException {

        GeneralUserShareV2DO generalUserShareV2DO = new GeneralUserShareV2DO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria =
                buildUserCriteriaFromRequest(userShareAllRequestBody.getUserCriteria());
        generalUserShareV2DO.setUserCriteria(userCriteria);

        // Set policy and roles.
        generalUserShareV2DO.setPolicy(resolvePolicy(userShareAllRequestBody.getPolicy()));
        generalUserShareV2DO.setRoleAssignments(
                buildRoleAssignmentFromRequest(userShareAllRequestBody.getRoleAssignment()));

        return generalUserShareV2DO;
    }

    /**
     * Populates a SelectiveUserUnshareDO object from the provided UserUnshareRequestBody.
     *
     * @param userUnshareSelectedRequestBody Contains details for user unsharing.
     * @return A populated SelectiveUserUnshareDO object.
     */
    private SelectiveUserUnshareDO populateSelectiveUserUnshareDO(
            UserUnshareSelectedRequestBody userUnshareSelectedRequestBody) throws UserSharingMgtClientException {

        SelectiveUserUnshareDO selectiveUserUnshareDO = new SelectiveUserUnshareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria =
                buildUserCriteriaFromRequest(userUnshareSelectedRequestBody.getUserCriteria());
        selectiveUserUnshareDO.setUserCriteria(userCriteria);

        // Set organizations.
        selectiveUserUnshareDO.setOrganizations(userUnshareSelectedRequestBody.getOrgIds());

        return selectiveUserUnshareDO;
    }

    /**
     * Populates a GeneralUserUnshareDO object from the provided UserUnshareWithAllRequestBody.
     *
     * @param userUnshareAllRequestBody Contains details for removing shared access.
     * @return A populated GeneralUserUnshareDO object.
     */
    private GeneralUserUnshareDO populateGeneralUserUnshareDO(UserUnshareAllRequestBody userUnshareAllRequestBody)
            throws UserSharingMgtClientException {

        GeneralUserUnshareDO generalUserUnshareDO = new GeneralUserUnshareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria =
                buildUserCriteriaFromRequest(userUnshareAllRequestBody.getUserCriteria());
        generalUserUnshareDO.setUserCriteria(userCriteria);

        return generalUserUnshareDO;
    }

    /**
     * Populates a PatchUserShareDO object from the provided UserSharingPatchRequest.
     *
     * @param userSharingPatchRequest Contains details for patching user sharing.
     * @return A populated PatchUserShareDO object.
     */
    private UserSharePatchDO populateUserSharePatch(UserSharingPatchRequest userSharingPatchRequest)
            throws UserSharingMgtClientException {

        UserSharePatchDO userSharePatchDO = new UserSharePatchDO();

        Map<String, UserCriteriaType> userCriteria =
                buildUserCriteriaFromRequest(userSharingPatchRequest.getUserCriteria());
        userSharePatchDO.setUserCriteria(userCriteria);
        userSharePatchDO.setPatchOperations(buildPatchOperationsFromRequest(userSharingPatchRequest.getOperations()));

        return userSharePatchDO;
    }

    // Helper methods to map relevant responses from service layer to API layer.

    /**
     * Map the backend response DO for shared organizations into the API response model, including pagination
     * links when cursors are available.
     *
     * @param responseSharedOrgsV2DO Backend response containing shared organization details and paging cursors.
     * @return API response model representing shared organizations and navigation links.
     */
    private UserSharedOrganizationsResponse mapUserSharedOrganizationsResponse(
            ResponseSharedOrgsV2DO responseSharedOrgsV2DO, String userId, String filter, Integer limit,
            Boolean recursive, String attributes) {

        UserSharedOrganizationsResponse response = new UserSharedOrganizationsResponse();

        // 1) Map global sharingMode (if present)
        if (responseSharedOrgsV2DO.getSharingMode() != null) {
            response.setSharingMode(toApiSharingMode(responseSharedOrgsV2DO.getSharingMode()));
        }

        // 2) Map org list
        List<UserSharedOrganization> orgs = new ArrayList<>();
        if (responseSharedOrgsV2DO.getSharedOrgs() != null) {
            for (ResponseOrgDetailsV2DO orgDO : responseSharedOrgsV2DO.getSharedOrgs()) {
                orgs.add(toApiUserSharedOrganization(orgDO));
            }
        }
        response.setOrganizations(orgs);

        // 3) Build pagination links (keep empty list when none)
        List<Link> links =
                buildPaginationLinks(responseSharedOrgsV2DO, userId, filter, limit, recursive, attributes);
        response.setLinks(links);

        return response;
    }

    /**
     * Retrieves the current request's UriInfo.
     *
     * @return The UriInfo of the current request, or null if not available.
     */
    private UriInfo getCurrentRequestUriInfo() {

        Message message = JAXRSUtils.getCurrentMessage();
        if (message == null) {
            return null;
        }
        return new UriInfoImpl(message);
    }

    // Helper methods for mapping individual components.

    /**
     * Builds the internal user criteria map from the API-level {@link UserCriteria}.
     * <p>
     * Each supported criterion provided in the request is converted into its
     * corresponding {@link UserCriteriaType} implementation and added to a single
     * criteria map keyed by the criterion name.
     * <p>
     * At least one user criterion must be present in the request; otherwise, an
     * {@link IllegalArgumentException} is thrown.
     *
     * @param userCriteria User selection criteria provided in the API request.
     * @return A map of user criteria keyed by criterion type.
     * @throws IllegalArgumentException If no supported user criteria are provided.
     */
    private Map<String, UserCriteriaType> buildUserCriteriaFromRequest(UserCriteria userCriteria)
            throws UserSharingMgtClientException {

        Map<String, UserCriteriaType> userCriteriaMap = new HashMap<>();

        // Populate user IDs criterion (if provided).
        if (userCriteria.getUserIds() != null && !userCriteria.getUserIds().isEmpty()) {
            userCriteriaMap.put(USER_IDS, new UserIdList(userCriteria.getUserIds()));
        }

        if (userCriteriaMap.isEmpty()) {
            throw makeRequestError(ERROR_MISSING_USER_CRITERIA);
        }

        return userCriteriaMap;
    }

    /**
     * Builds a {@link RoleAssignmentDO} from the role assignment information
     * provided in the API request.
     * <p>
     * This method converts the API-level role assignment configuration into the
     * internal role assignment representation, including the assignment mode and
     * associated roles with audiences.
     *
     * @param roleAssignment Role assignment configuration received in the request.
     * @return A {@link RoleAssignmentDO} constructed from the request data.
     */
    private RoleAssignmentDO buildRoleAssignmentFromRequest(RoleAssignment roleAssignment) {

        RoleAssignmentDO roleAssignmentDO = new RoleAssignmentDO();
        if (roleAssignment != null) {
            RoleAssignmentMode mode = roleAssignment.getMode() != null
                    ? RoleAssignmentMode.fromString(roleAssignment.getMode().toString())
                    : RoleAssignmentMode.NONE;
            roleAssignmentDO.setMode(mode);
            roleAssignmentDO.setRoles(buildRoleWithAudienceDO(roleAssignment.getRoles()));
        } else {
            roleAssignmentDO.setMode(RoleAssignmentMode.NONE);
            roleAssignmentDO.setRoles(new ArrayList<>());
        }
        return roleAssignmentDO;
    }

    /**
     * Populates a list of RoleWithAudienceDO objects from the provided list of RoleWithAudience.
     *
     * @param roles The list of RoleShareConfig objects to be converted.
     * @return A list of RoleWithAudienceDO objects.
     */
    private List<RoleWithAudienceDO> buildRoleWithAudienceDO(List<RoleShareConfig> roles) {

        List<RoleWithAudienceDO> rolesList = new ArrayList<>();
        if (roles != null) {
            for (RoleShareConfig role : roles) {
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
     * Converts API-level patch operations into internal patch operation DOs, including converting operation type and
     * mapping values based on the operation path.
     *
     * @param operations List of patch operations from the request.
     * @return List of internal {@link PatchOperationDO} instances.
     */
    private List<PatchOperationDO> buildPatchOperationsFromRequest(List<UserSharingPatchOperation> operations)
            throws UserSharingMgtClientException {

        List<PatchOperationDO> patchOperations = new ArrayList<>();

        if (operations != null) {
            for (UserSharingPatchOperation operation : operations) {
                PatchOperationDO patchOperationDO = new PatchOperationDO();
                patchOperationDO.setOperation(UserSharePatchOperation.fromValue(operation.getOp()));
                patchOperationDO.setPath(operation.getPath());
                patchOperationDO.setValues(
                        buildPatchOperationValuesFromRequest(operation.getPath(), operation.getValue()));
                patchOperations.add(patchOperationDO);
            }
        }
        return patchOperations;
    }

    /**
     * Builds the internal "values" object for a patch operation based on its path.
     *
     * @param path   Patch operation path.
     * @param values API role values supplied for the operation.
     * @return Internal values object appropriate for the given path.
     * @throws IllegalArgumentException If the path is empty or not supported.
     */
    private Object buildPatchOperationValuesFromRequest(String path, List<RoleShareConfig> values)
            throws UserSharingMgtClientException {

        if (path == null || path.isEmpty()) {
            throw makeRequestError(ERROR_EMPTY_USER_SHARE_PATCH_PATH);
        }

        // For now, support only role patching.
        if (isRolesPath(path)) {
            return buildRoleWithAudienceDO(values);
        }

        throw makeRequestError(ERROR_UNSUPPORTED_USER_SHARE_PATCH_PATH);
    }

    /**
     * Resolves the string representation of a policy to its corresponding PolicyEnum.
     *
     * @param policy The string representation of the policy.
     * @return The corresponding PolicyEnum.
     * @throws UserSharingMgtClientException If the provided policy is unsupported.
     */
    private PolicyEnum resolvePolicy(String policy) throws UserSharingMgtClientException {

        try {
            return PolicyEnum.getPolicyByValue(policy);
        } catch (IllegalArgumentException e) {
            throw makeRequestError(ERROR_UNSUPPORTED_USER_SHARE_POLICY);
        }
    }

    /**
     * Checks whether the given patch path targets role updates.
     *
     * @param path Patch operation path.
     * @return {@code true} if the path targets roles; {@code false} otherwise.
     */
    private boolean isRolesPath(String path) {

        return path.trim().endsWith("].roles") || path.trim().endsWith(".roles");
    }

    /**
     * Splits a comma-separated attributes string into a distinct, trimmed list.
     *
     * @param attributes Comma-separated attributes string (may be null/empty).
     * @return Distinct list of attribute tokens; empty list if input is null/empty.
     */
    private List<String> splitAttributes(String attributes) {

        if (attributes == null || attributes.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        return java.util.Arrays.stream(attributes.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Maps an internal organization details DO into the API-level shared organization representation.
     *
     * @param orgDO Backend organization details.
     * @return API {@link UserSharedOrganization} model.
     */
    private UserSharedOrganization toApiUserSharedOrganization(ResponseOrgDetailsV2DO orgDO) {

        UserSharedOrganization org = new UserSharedOrganization();

        org.setOrgId(orgDO.getOrganizationId());
        org.setOrgName(orgDO.getOrganizationName());
        org.setSharedUserId(orgDO.getSharedUserId());

        if (orgDO.getSharedType() != null) {
            org.setSharedType(orgDO.getSharedType().toString());
        }

        // Per-org sharing mode (if present in DO)
        if (orgDO.getSharingModeDO() != null) {
            org.setSharingMode(toApiSharingMode(orgDO.getSharingModeDO()));
        }

        // Effective roles for that org
        if (orgDO.getRoleWithAudienceDOList() != null) {
            List<RoleShareConfig> roles = new ArrayList<>();
            for (RoleWithAudienceDO roleDO : orgDO.getRoleWithAudienceDOList()) {
                roles.add(toApiRole(roleDO));
            }
            org.setRoles(roles);
        } else {
            org.setRoles(new ArrayList<>());
        }

        return org;
    }

    /**
     * Maps the internal sharing mode DO into the API-level {@link SharingMode} model, including the effective policy
     * and role assignment configuration when available.
     *
     * @param modeDO Backend sharing mode details.
     * @return API sharing mode model.
     */
    private SharingMode toApiSharingMode(SharingModeDO modeDO) {

        SharingMode apiMode = new SharingMode();

        if (modeDO.getPolicy() != null) {
            apiMode.setPolicy(modeDO.getPolicy().getValue());
        }

        RoleAssignmentDO raDO = modeDO.getRoleAssignment();
        if (raDO != null) {
            RoleAssignment ra = new RoleAssignment();
            if (raDO.getMode() != null) {
                ra.setMode(RoleAssignment.ModeEnum.fromValue(raDO.getMode().toString()));
            }

            if (raDO.getRoles() != null) {
                List<RoleShareConfig> roles = new ArrayList<>();
                for (RoleWithAudienceDO roleDO : raDO.getRoles()) {
                    roles.add(toApiRole(roleDO));
                }
                ra.setRoles(roles);
            } else {
                ra.setRoles(new ArrayList<>());
            }

            apiMode.setRoleAssignment(ra);
        }

        return apiMode;
    }

    /**
     * Maps an internal role-with-audience DO into the API role representation.
     *
     * @param roleDO Backend role details including audience metadata.
     * @return API {@link RoleShareConfig} model.
     */
    private RoleShareConfig toApiRole(RoleWithAudienceDO roleDO) {

        RoleShareConfig role = new RoleShareConfig();
        role.setDisplayName(roleDO.getRoleName());

        RoleShareConfigAudience audience = new RoleShareConfigAudience();
        audience.setDisplay(roleDO.getAudienceName());
        audience.setType(roleDO.getAudienceType());

        role.setAudience(audience);

        return role;
    }

    private List<Link> buildPaginationLinks(ResponseSharedOrgsV2DO result, String userId, String filter, Integer limit,
                                            Boolean recursive, String attributes) {

        UriInfo uriInfo = getCurrentRequestUriInfo();

        List<Link> links = new ArrayList<>();

        int nextCursor = result.getNextPageCursor();
        if (nextCursor > 0) {
            links.add(new Link()
                    .rel("next")
                    .href(buildSharedOrgsPageLink(uriInfo, userId, filter, limit, recursive, attributes,
                            /*after=*/ encodeCursor(nextCursor),
                            /*before=*/ null)));
        }

        int prevCursor = result.getPreviousPageCursor();
        if (prevCursor > 0) {
            links.add(new Link()
                    .rel("previous")
                    .href(buildSharedOrgsPageLink(uriInfo, userId, filter, limit, recursive, attributes,
                            /*after=*/ null,
                            /*before=*/ encodeCursor(prevCursor))));
        }

        return links;
    }

    /**
     * Builds an absolute link to /users/{userId}/share preserving all query params
     * and setting exactly one of after/before.
     */
    private String buildSharedOrgsPageLink(UriInfo uriInfo, String userId, String filter, Integer limit,
                                           Boolean recursive, String attributes, String after, String before) {

        if (uriInfo == null) {
            return buildRelativeSharedOrgsPageLink(userId, filter, limit, recursive, attributes, after, before);
        }

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("users")
                .path(userId)
                .path("share");

        if (limit != null) {
            builder.queryParam("limit", limit);
        }
        if (filter != null && !filter.isEmpty()) {
            builder.queryParam("filter", filter);
        }
        if (recursive != null) {
            builder.queryParam("recursive", recursive);
        }
        if (attributes != null && !attributes.isEmpty()) {
            builder.queryParam("attributes", attributes);
        }

        // exactly one cursor param
        if (after != null && !after.isEmpty()) {
            builder.queryParam("after", after);
        } else if (before != null && !before.isEmpty()) {
            builder.queryParam("before", before);
        }

        return builder.build().toString();
    }

    private String buildRelativeSharedOrgsPageLink(String userId, String filter, Integer limit, Boolean recursive,
                                                   String attributes, String after, String before) {

        String base = "/o/api/server/v2/users/" + urlEncode(userId) + "/share";

        List<String> qp = new ArrayList<>();
        if (limit != null) {
            qp.add("limit=" + limit);
        }
        if (filter != null && !filter.isEmpty()) {
            qp.add("filter=" + urlEncode(filter));
        }
        if (recursive != null) {
            qp.add("recursive=" + recursive);
        }
        if (attributes != null && !attributes.isEmpty()) {
            qp.add("attributes=" + urlEncode(attributes));
        }
        if (after != null && !after.isEmpty()) {
            qp.add("after=" + urlEncode(after));
        } else if (before != null && !before.isEmpty()) {
            qp.add("before=" + urlEncode(before));
        }

        return qp.isEmpty() ? base : base + "?" + String.join("&", qp);
    }

    /**
     * Encodes the given paging cursor into the wire format expected by the API.
     *
     * @param cursor Numeric cursor value returned by the backend.
     * @return Encoded cursor string.
     */
    private String encodeCursor(int cursor) {

        return java.util.Base64.getEncoder()
                .encodeToString(String.valueOf(cursor).getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    /**
     * URL-encodes the given value using UTF-8 encoding.
     *
     * @param value The string value to encode.
     * @return The URL-encoded string.
     */
    private String urlEncode(String value) {

        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            // UTF-8 is guaranteed to be supported
            throw new IllegalStateException("UTF-8 encoding not supported", e);
        }
    }

    // Helper methods for building responses and errors.

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
                .traceId(UUID.randomUUID().toString());
    }
}
