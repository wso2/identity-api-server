/*
 * Copyright (c) 2023-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.common.UserInvitationMgtConstants;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.AcceptanceRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.Audience;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.GroupAssignmentResponse;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.IntrospectSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationResponse;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationSuccessResponseResult;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationsListResponse;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.Property;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.RoleAssignmentResponse;
import org.wso2.carbon.identity.organization.user.invitation.management.InvitationCoreService;
import org.wso2.carbon.identity.organization.user.invitation.management.exception.UserInvitationMgtException;
import org.wso2.carbon.identity.organization.user.invitation.management.models.GroupAssignments;
import org.wso2.carbon.identity.organization.user.invitation.management.models.Invitation;
import org.wso2.carbon.identity.organization.user.invitation.management.models.InvitationDO;
import org.wso2.carbon.identity.organization.user.invitation.management.models.InvitationResult;
import org.wso2.carbon.identity.organization.user.invitation.management.models.RoleAssignments;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.organization.user.invitation.management.constant.UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_INVALID_CONFIRMATION_CODE;
import static org.wso2.carbon.identity.organization.user.invitation.management.constant.UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_INVALID_FILTER;
import static org.wso2.carbon.identity.organization.user.invitation.management.constant.UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_INVALID_GROUP;
import static org.wso2.carbon.identity.organization.user.invitation.management.constant.UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_INVALID_INVITATION_ID;
import static org.wso2.carbon.identity.organization.user.invitation.management.constant.UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_INVALID_ROLE;
import static org.wso2.carbon.identity.organization.user.invitation.management.constant.UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_INVALID_USER;
import static org.wso2.carbon.identity.organization.user.invitation.management.constant.UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_MULTIPLE_INVITATIONS_FOR_USER;
import static org.wso2.carbon.identity.organization.user.invitation.management.constant.UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE;
import static org.wso2.carbon.identity.organization.user.invitation.management.constant.UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE_VALUE;
import static org.wso2.carbon.identity.organization.user.invitation.management.constant.UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_USER_ALREADY_EXISTS;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

/**
 * Service class for the User Invitation Management APIs.
 */
public class GuestApiServiceCore {

    private static final Log log = LogFactory.getLog(GuestApiServiceCore.class);
    private final InvitationCoreService invitationCoreService;

    public GuestApiServiceCore(InvitationCoreService invitationCoreService) {

        this.invitationCoreService = invitationCoreService;
        if (log.isDebugEnabled()) {
            log.debug("GuestApiServiceCore initialized with InvitationCoreService.");
        }
    }

    private static List<RoleAssignmentResponse> buildRoleAssignmentResponse(Invitation invitationRecord) {

        List<RoleAssignmentResponse> roleAssignmentResponseList = new ArrayList<>();
        for (RoleAssignments roleAssignment : invitationRecord.getRoleAssignments()) {
            RoleAssignmentResponse roleAssignmentResponse = new RoleAssignmentResponse();
            roleAssignmentResponse.setDisplayName(roleAssignment.getRoleName());
            roleAssignmentResponse.setId(roleAssignment.getRoleId());
            if (roleAssignment.getAudience() != null) {
                Audience audience = new Audience();
                List<Audience> audienceList = new ArrayList<>();
                audience.setDisplay(roleAssignment.getAudience().getApplicationName());
                audience.setType(roleAssignment.getAudience().getApplicationType());
                audience.setValue(roleAssignment.getAudience().getApplicationId());
                audienceList.add(audience);
                roleAssignmentResponse.setAudience(audienceList);
            }
            roleAssignmentResponseList.add(roleAssignmentResponse);
        }
        return roleAssignmentResponseList;
    }

    private static List<GroupAssignmentResponse> buildGroupAssignmentResponse(Invitation invitationRecord) {

        List<GroupAssignmentResponse> groupAssignmentResponseList = new ArrayList<>();
        for (GroupAssignments groupAssignment : invitationRecord.getGroupAssignments()) {
            GroupAssignmentResponse groupAssignmentResponse = new GroupAssignmentResponse();
            groupAssignmentResponse.setDisplayName(groupAssignment.getDisplayName());
            groupAssignmentResponse.setId(groupAssignment.getGroupId());
            groupAssignmentResponseList.add(groupAssignmentResponse);
        }
        return groupAssignmentResponseList;
    }

    /**
     * Creates the invitation to the shared user.
     *
     * @param invitationRequestBody Contains the details of the invitation.
     * @return The details of the created invitation.
     */
    public List<InvitationSuccessResponse> createInvitation(InvitationRequestBody invitationRequestBody) {

        if (invitationRequestBody == null) {
            log.error("InvitationRequestBody cannot be null.");
            throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                    .ERROR_CODE_CREATE_INVITATION, "InvitationRequestBody cannot be null");
        }
        if (log.isDebugEnabled()) {
            log.debug("Creating invitations for users: " +
                    (invitationRequestBody.getUsernames() != null ?
                            invitationRequestBody.getUsernames().size() : 0));
        }
        InvitationDO invitation = new InvitationDO();
        invitation.setUsernamesList(invitationRequestBody.getUsernames());
        invitation.setUserDomain(invitationRequestBody.getUserDomain());
        if (invitationRequestBody.getRoles() != null) {
            List<RoleAssignments> roleAssignments = new ArrayList<>();
            for (String roleId : invitationRequestBody.getRoles()) {
                RoleAssignments roleAssignment = new RoleAssignments();
                roleAssignment.setRole(roleId);
                roleAssignments.add(roleAssignment);
            }
            invitation.setRoleAssignments(roleAssignments.toArray(new RoleAssignments[0]));
        }
        if (invitationRequestBody.getGroups() != null) {
            List<GroupAssignments> groupAssignments = new ArrayList<>();
            for (String groupId : invitationRequestBody.getGroups()) {
                GroupAssignments groupAssignment = new GroupAssignments();
                groupAssignment.setGroupId(groupId);
                groupAssignments.add(groupAssignment);
            }
            invitation.setGroupAssignments(groupAssignments.toArray(new GroupAssignments[0]));
        }
        List<Property> properties = invitationRequestBody.getProperties();
        if (properties != null) {
            properties.forEach((prop) -> invitation.getInvitationProperties().put(prop.getKey(), prop.getValue()));
        }
        List<InvitationResult> invitationResponse;
        try {
            invitationResponse = invitationCoreService.createInvitations(invitation);
            log.info("Invitations created successfully for " + invitation.getUsernamesList().size() + " users.");
        } catch (UserInvitationMgtException e) {
            if (ERROR_CODE_MULTIPLE_INVITATIONS_FOR_USER.getCode().equals(e.getErrorCode())) {
                log.warn("Multiple invitations found for users: " + invitation.getUsernamesList().toString());
                throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                        .ERROR_CODE_MULTIPLE_INVITATIONS_FOR_USER, invitation.getUsernamesList().toString());
            } else if (ERROR_CODE_INVALID_ROLE.getCode().equals(e.getErrorCode())) {
                log.warn("Invalid role specified in invitation request.");
                throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                        .ERROR_CODE_INVALID_ROLE, StringUtils.EMPTY);
            } else if (ERROR_CODE_INVALID_GROUP.getCode().equals(e.getErrorCode())) {
                log.warn("Invalid group specified in invitation request.");
                throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_INVALID_GROUP,
                        StringUtils.EMPTY);
            }
            log.error("Error creating invitations for users: " + invitation.getUsernamesList().toString(), e);
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_CREATE_INVITATION,
                    invitation.getUsernamesList().toString());
        }
        return createInvitationSuccessResponse(invitationResponse);
    }

    /**
     * Gets the invitations for the authenticated user's organization.
     *
     * @param filter Contains the filter to be applied to the invitation list. ex : status eq 'PENDING'
     * @return The list of invitations initiated by the authenticated user's organization.
     */
    public InvitationsListResponse getInvitations(String filter, Integer limit, Integer offset, String sortOrder,
                                                  String sortBy) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving invitations with filter: " + filter);
        }
        if (!isUnsupportedParamAvailable(limit, offset, sortOrder, sortBy)) {
            try {
                List<Invitation> invitations = invitationCoreService.getInvitations(filter);
                log.info("Retrieved " + (invitations != null ? invitations.size() : 0) + " invitations.");
                if (invitations == null) {
                    log.warn("Received null invitations list from core service.");
                    invitations = new ArrayList<>();
                }
                return buildInvitationsListResponse(invitations);
            } catch (UserInvitationMgtException e) {
                if (ERROR_CODE_INVALID_FILTER.getCode().equals(e.getErrorCode())) {
                    log.warn("Invalid filter provided: " + filter);
                    throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                            .ERROR_CODE_INVALID_FILTER, filter);
                } else if (ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE_VALUE.getCode().equals(e.getErrorCode())) {
                    log.warn("Unsupported filter attribute value in filter: " + filter);
                    throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                            .ERROR_CODE_INVALID_FILTER, filter);
                } else if (ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE.getCode().equals(e.getErrorCode())) {
                    log.warn("Unsupported filter attribute in filter: " + filter);
                    throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                            .ERROR_CODE_INVALID_FILTER, filter);
                }
                log.error("Error retrieving invitations.", e);
                throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                        UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_GET_INVITATIONS, StringUtils.EMPTY);
            }
        }
        return null;
    }

    /**
     * Gets the validity of the invitation by using the confirmation code.
     *
     * @param confirmationCode Contains the confirmation code of the invitation.
     * @return Very limited details of the invitation including the status.
     */
    public IntrospectSuccessResponse introspectInvitation(String confirmationCode) {

        if (log.isDebugEnabled()) {
            log.debug("Introspecting invitation with confirmation code.");
        }
        try {
            Invitation invitation = invitationCoreService.introspectInvitation(confirmationCode);
            log.info("Invitation introspection completed for user: " +
                    (invitation != null ? invitation.getUsername() : "unknown"));
            if (invitation == null) {
                log.error("Received null invitation from core service for confirmation code.");
                throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                        .ERROR_CODE_INVALID_CONFIRMATION_CODE, confirmationCode);
            }
            return buildValidateResponse(invitation);
        } catch (UserInvitationMgtException e) {
            if (ERROR_CODE_INVALID_CONFIRMATION_CODE.getCode().equals(e.getErrorCode())) {
                log.warn("Invalid confirmation code provided for introspection.");
                throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                        .ERROR_CODE_INVALID_CONFIRMATION_CODE, confirmationCode);
            }
            log.error("Error introspecting invitation.", e);
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_VALIDATE_INVITATION, confirmationCode);
        }
    }

    /**
     * Deletes an invitation by using the invitation id. This will remove the invitation from the core.
     *
     * @param invitationId Contains the invitation id of the invitation to be deleted.
     * @return True if the invitation is deleted successfully.
     */
    public boolean deleteInvitation(String invitationId) {

        if (log.isDebugEnabled()) {
            log.debug("Deleting invitation with ID: " + invitationId);
        }
        try {
            boolean deleted = invitationCoreService.deleteInvitation(invitationId);
            if (deleted) {
                log.info("Invitation deleted successfully for ID: " + invitationId);
            } else {
                log.warn("Invitation deletion failed for ID: " + invitationId);
            }
            return deleted;
        } catch (UserInvitationMgtException e) {
            if (ERROR_CODE_INVALID_INVITATION_ID.getCode().equals(e.getErrorCode())) {
                log.warn("Invalid invitation ID provided for deletion: " + invitationId);
                throw handleException(BAD_REQUEST,
                        UserInvitationMgtConstants.ErrorMessage.ERROR_CODE_INVALID_INVITATION, invitationId);
            }
            log.error("Error deleting invitation with ID: " + invitationId, e);
            throw handleException(INTERNAL_SERVER_ERROR, UserInvitationMgtConstants.ErrorMessage
                    .ERROR_CODE_DELETE_INVITATION, invitationId);
        }
    }

    /**
     * Accepts the invitation by sending the confirmation code. This will invalidate the confirmation code and
     * proceed with the sharing and the role assignments if there are any.
     * NOTE : NOT IMPLEMENTED
     *
     * @param acceptanceRequestBody Contains the confirmation code of the invitation.
     */
    public void acceptInvitation(AcceptanceRequestBody acceptanceRequestBody) {

        if (log.isDebugEnabled()) {
            log.debug("Processing invitation acceptance.");
        }
        try {
            invitationCoreService.acceptInvitation(acceptanceRequestBody.getConfirmationCode());
            log.info("Invitation accepted successfully.");
        } catch (UserInvitationMgtException e) {
            if (ERROR_CODE_INVALID_CONFIRMATION_CODE.getCode().equals(e.getErrorCode())) {
                log.warn("Invalid confirmation code provided for acceptance.");
                throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                        .ERROR_CODE_INVALID_CONFIRMATION_CODE_FOR_ACCEPTANCE, acceptanceRequestBody
                        .getConfirmationCode());
            } else if (ERROR_CODE_INVALID_USER.getCode().equals(e.getErrorCode())) {
                log.warn("Invalid user provided for invitation acceptance.");
                throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                        .ERROR_CODE_INVALID_USER, StringUtils.EMPTY);
            } else if (ERROR_CODE_USER_ALREADY_EXISTS.getCode().equals(e.getErrorCode())) {
                log.warn("User already exists in the organization.");
                throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                        .ERROR_CODE_EXISTING_USER, StringUtils.EMPTY);
            }
            log.error("Error accepting invitation.", e);
            throw handleException(INTERNAL_SERVER_ERROR, UserInvitationMgtConstants.ErrorMessage
                            .ERROR_CODE_ACCEPT_INVITATION, acceptanceRequestBody.getConfirmationCode());
        }
    }

    private APIError handleException(Response.Status status, UserInvitationMgtConstants.ErrorMessage error,
                                     String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    private ErrorResponse.Builder getErrorBuilder(UserInvitationMgtConstants.ErrorMessage errorMsg, String data) {

        return new ErrorResponse.Builder()
                .withCode(errorMsg.getCode())
                .withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    private String includeData(UserInvitationMgtConstants.ErrorMessage error, String data) {

        if (StringUtils.isNotBlank(data)) {
            return String.format(error.getDescription(), data);
        }
        return error.getDescription();
    }

    private List<InvitationSuccessResponse> createInvitationSuccessResponse(List<InvitationResult> invitationList) {

        List<InvitationSuccessResponse> invitationSuccessResponseList = new ArrayList<>();
        for (InvitationResult invitation : invitationList) {
            InvitationSuccessResponse invitationSuccessResponse = new InvitationSuccessResponse();
            InvitationSuccessResponseResult invitationSuccessResponseResult = new InvitationSuccessResponseResult();
            if (UserInvitationMgtConstants.ERROR_FAIL_STATUS.equals(invitation.getStatus())) {
                invitationSuccessResponseResult.setErrorCode(invitation.getErrorMsg().getCode());
                invitationSuccessResponseResult.setErrorMessage(invitation.getErrorMsg().getMessage());
                invitationSuccessResponseResult.setErrorDescription(invitation.getErrorMsg()
                        .getDescription());
            }
            invitationSuccessResponseResult.setStatus(invitation.getStatus());
            invitationSuccessResponse.setUsername(invitation.getUsername());
            invitationSuccessResponse.setResult(invitationSuccessResponseResult);
            if (StringUtils.isNotBlank(invitation.getConfirmationCode())) {
                invitationSuccessResponse.setConfirmationCode(invitation.getConfirmationCode());
            }
            invitationSuccessResponseList.add(invitationSuccessResponse);
        }
        return invitationSuccessResponseList;
    }

    private InvitationsListResponse buildInvitationsListResponse(List<Invitation> invitationList) {

        InvitationsListResponse invitationsListResponse = new InvitationsListResponse();
        for (Invitation invitationRecord : invitationList) {
            InvitationResponse invitationResponse = new InvitationResponse();
            invitationResponse.setId(invitationRecord.getInvitationId());
            invitationResponse.setUsername(invitationRecord.getUsername());
            invitationResponse.setEmail(invitationRecord.getEmail());
            invitationResponse.setStatus(invitationRecord.getStatus());
            invitationResponse.setExpiredAt(invitationRecord.getExpiredAt().toString());
            if (invitationRecord.getRoleAssignments().length > 0) {
                List<RoleAssignmentResponse> roleAssignments = buildRoleAssignmentResponse(invitationRecord);
                invitationResponse.setRoles(roleAssignments);
            }
            if (invitationRecord.getGroupAssignments().length > 0) {
                List<GroupAssignmentResponse> groupAssignments = buildGroupAssignmentResponse(invitationRecord);
                invitationResponse.setGroups(groupAssignments);
            }
            invitationsListResponse.addInvitationsItem(invitationResponse);
        }
        return invitationsListResponse;
    }

    private IntrospectSuccessResponse buildValidateResponse(Invitation invitation) {

        IntrospectSuccessResponse introspectSuccessResponse = new IntrospectSuccessResponse();
        introspectSuccessResponse.setConfirmationCode(invitation.getConfirmationCode());
        introspectSuccessResponse.setUsername(invitation.getUsername());
        introspectSuccessResponse.setUserOrganization(invitation.getUserOrganizationId());
        introspectSuccessResponse.setInitiatedOrganization(invitation.getInvitedOrganizationId());
        introspectSuccessResponse.setStatus(invitation.getStatus());
        return introspectSuccessResponse;
    }

    private boolean isUnsupportedParamAvailable(Integer limit, Integer offset, String sortOrder, String sortBy) {

        if (limit != null) {
            throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                    .ERROR_CODE_UNSUPPORTED_LIMIT, String.valueOf(limit));
        }
        if (offset != null) {
            throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                    .ERROR_CODE_UNSUPPORTED_OFFSET, String.valueOf(offset));
        }
        if (StringUtils.isNotBlank(sortOrder)) {
            throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                    .ERROR_CODE_UNSUPPORTED_SORT_ORDER, sortOrder);
        }
        if (StringUtils.isNotBlank(sortBy)) {
            throw handleException(BAD_REQUEST, UserInvitationMgtConstants.ErrorMessage
                    .ERROR_CODE_UNSUPPORTED_SORT_BY, sortBy);
        }
        return false;
    }
}
