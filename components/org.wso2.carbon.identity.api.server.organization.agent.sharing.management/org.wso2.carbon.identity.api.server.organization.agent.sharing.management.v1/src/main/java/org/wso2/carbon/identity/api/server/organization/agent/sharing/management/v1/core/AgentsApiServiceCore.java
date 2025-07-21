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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.core;

import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareRequestBodyOrganizations;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharedOrganizationsResponseLinks;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharedOrganizationsResponseSharedOrganizations;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharedRolesResponse;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.RoleWithAudience;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.RoleWithAudienceAudience;
import org.wso2.carbon.identity.core.util.IdentityUtil;
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

import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.INVALID_GENERAL_AGENT_SHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.INVALID_GENERAL_AGENT_UNSHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.INVALID_SELECTIVE_AGENT_SHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.INVALID_SELECTIVE_AGENT_UNSHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.INVALID_UUID_FORMAT;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.RESPONSE_DETAIL_AGENT_SHARE;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.RESPONSE_DETAIL_AGENT_UNSHARE;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.RESPONSE_STATUS_PROCESSING;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.IS_AGENT_SHARING;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.USER_IDS;

/**
 * Core service class for handling agent sharing management APIs.
 */
public class AgentsApiServiceCore {

    private final UserSharingPolicyHandlerService userSharingPolicyHandlerService;

    public AgentsApiServiceCore(UserSharingPolicyHandlerService userSharingPolicyHandlerService) {

        this.userSharingPolicyHandlerService = userSharingPolicyHandlerService;
    }

    /**
     * Handles sharing an agent across specific organizations.
     *
     * @param agentShareRequestBody Contains details for agent sharing.
     */
    public Response shareAgent(AgentShareRequestBody agentShareRequestBody) {

        if (agentShareRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_SELECTIVE_AGENT_SHARE_REQUEST_BODY))).build();
        }

        IdentityUtil.threadLocalProperties.get().put(IS_AGENT_SHARING, true);

        // Populate selectiveAgentShareDO object from the request body.
        SelectiveUserShareDO selectiveAgentShareDO = populateSelectiveAgentShareDO(agentShareRequestBody);

        try {
            userSharingPolicyHandlerService.populateSelectiveUserShare(selectiveAgentShareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_AGENT_SHARE)).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        } finally {
            IdentityUtil.threadLocalProperties.get().put(IS_AGENT_SHARING, false);
        }
    }

    /**
     * Handles sharing an agent across all organizations.
     *
     * @param agentShareWithAllRequestBody Contains details for sharing agents with all organizations.
     */
    public Response shareAgentWithAll(AgentShareWithAllRequestBody agentShareWithAllRequestBody) {

        if (agentShareWithAllRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_GENERAL_AGENT_SHARE_REQUEST_BODY))).build();
        }

        IdentityUtil.threadLocalProperties.get().put("isAgentSharing", true);

        // Populate GeneralUserShareDO object from the request body.
        GeneralUserShareDO generalAgentShareDO = populateGeneralAgentShareDO(agentShareWithAllRequestBody);

        try {
            userSharingPolicyHandlerService.populateGeneralUserShare(generalAgentShareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_AGENT_SHARE)).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        } finally {
            IdentityUtil.threadLocalProperties.get().put("isAgentSharing", true);
        }
    }

    /**
     * Handles unsharing an agent from specific organizations.
     *
     * @param agentUnshareRequestBody Contains details for agent unsharing.
     */
    public Response unshareAgent(AgentUnshareRequestBody agentUnshareRequestBody) {

        if (agentUnshareRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_SELECTIVE_AGENT_UNSHARE_REQUEST_BODY))).build();
        }

        // Populate SelectiveUserUnshareDO object from the request body.
        SelectiveUserUnshareDO selectiveUserUnshareDO = populateSelectiveAgentUnshareDO(agentUnshareRequestBody);

        try {
            userSharingPolicyHandlerService.populateSelectiveUserUnshare(selectiveUserUnshareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_AGENT_UNSHARE))
                    .build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Handles removing an agent's shared access from all organizations.
     *
     * @param agentUnshareWithAllRequestBody Contains details for removing shared access.
     */
    public Response unshareAgentWithAll(AgentUnshareWithAllRequestBody agentUnshareWithAllRequestBody) {

        if (agentUnshareWithAllRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_GENERAL_AGENT_UNSHARE_REQUEST_BODY))).build();
        }

        // Populate GeneralAgentUnshareDO object from the request body.
        GeneralUserUnshareDO generalAgentUnshareDO = populateGeneralUserUnshareDO(agentUnshareWithAllRequestBody);

        try {
            userSharingPolicyHandlerService.populateGeneralUserUnshare(generalAgentUnshareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_AGENT_UNSHARE))
                    .build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Retrieves the organizations that an agent has access to.
     *
     * @param agentId    The ID of the agent.
     * @param after     The cursor for the next page.
     * @param before    The cursor for the previous page.
     * @param limit     The maximum number of results per page.
     * @param filter    The filter criteria.
     * @param recursive Whether to include child organizations.
     * @return AgentSharedOrganizationsResponse containing accessible organizations.
     */
    public Response getSharedOrganizations(String agentId, String after, String before,
                                           Integer limit, String filter, Boolean recursive) {

        if (agentId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_UUID_FORMAT))).build();
        }

        try {
            ResponseSharedOrgsDO result =
                    userSharingPolicyHandlerService.getSharedOrganizationsOfUser(agentId, after, before, limit, filter,
                            recursive);

            AgentSharedOrganizationsResponse response = populateAgentSharedOrganizationsResponse(result);
            return Response.ok().entity(response).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Retrieves the roles assigned to an agent within a specified organization.
     *
     * @param agentId    The ID of the agent.
     * @param orgId     The ID of the organization.
     * @param after     The cursor for the next page.
     * @param before    The cursor for the previous page.
     * @param limit     The maximum number of results per page.
     * @param filter    The filter criteria.
     * @param recursive Whether to include child roles.
     * @return AgentSharedRolesResponse containing shared roles.
     */
    public Response getSharedRoles(String agentId, String orgId, String after, String before,
                                   Integer limit, String filter, Boolean recursive) {

        if (agentId == null || orgId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_UUID_FORMAT))).build();
        }

        try {
            ResponseSharedRolesDO result =
                    userSharingPolicyHandlerService.getRolesSharedWithUserInOrganization(agentId, orgId, after, before,
                            limit, filter, recursive);

            AgentSharedRolesResponse response = populateAgentSharedRolesResponse(result);
            return Response.ok().entity(response).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Populates a SelectiveAgentShareDO object from the provided AgentShareRequestBody.
     *
     * @param agentShareRequestBody Contains details for agent sharing.
     * @return A populated SelectiveAgentShareDO object.
     */
    private SelectiveUserShareDO populateSelectiveAgentShareDO(AgentShareRequestBody agentShareRequestBody) {

        SelectiveUserShareDO selectiveAgentShareDO = new SelectiveUserShareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> agentCriteria =
                populateAgentCriteria(agentShareRequestBody.getAgentCriteria().getAgentIds());
        selectiveAgentShareDO.setUserCriteria(agentCriteria);

        // Set organizations.
        List<SelectiveUserShareOrgDetailsDO> organizationsList = new ArrayList<>();
        for (AgentShareRequestBodyOrganizations org : agentShareRequestBody.getOrganizations()) {
            SelectiveUserShareOrgDetailsDO selectiveAgentShareOrgDetailsDO = new SelectiveUserShareOrgDetailsDO();
            selectiveAgentShareOrgDetailsDO.setOrganizationId(org.getOrgId());
            selectiveAgentShareOrgDetailsDO.setPolicy(PolicyEnum.getPolicyByValue(org.getPolicy().value()));
            selectiveAgentShareOrgDetailsDO.setRoles(populateRoleWithAudienceDO(org.getRoles()));
            organizationsList.add(selectiveAgentShareOrgDetailsDO);
        }
        selectiveAgentShareDO.setOrganizations(organizationsList);

        return selectiveAgentShareDO;
    }

    private Map<String, UserCriteriaType> populateAgentCriteria(List<String> agentIdList) {

        Map<String, UserCriteriaType> userCriteria = new HashMap<>();
        UserCriteriaType userIds = new UserIdList(agentIdList);
        userCriteria.put(USER_IDS, userIds);
        return userCriteria;
    }

    /**
     * Populates a GeneralAgentShareDO object from the provided AgentShareWithAllRequestBody.
     *
     * @param userShareWithAllRequestBody Contains details for sharing agents with all organizations.
     * @return A populated GeneralAgentShareDO object.
     */
    private GeneralUserShareDO populateGeneralAgentShareDO(AgentShareWithAllRequestBody userShareWithAllRequestBody) {

        GeneralUserShareDO generalAgentShareDO = new GeneralUserShareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria =
                populateAgentCriteria(userShareWithAllRequestBody.getAgentCriteria().getAgentIds());
        generalAgentShareDO.setUserCriteria(userCriteria);

        // Set policy.
        generalAgentShareDO.setPolicy(PolicyEnum.getPolicyByValue(userShareWithAllRequestBody.getPolicy().value()));

        // Set roles.
        List<RoleWithAudienceDO> rolesList = populateRoleWithAudienceDO(userShareWithAllRequestBody.getRoles());
        generalAgentShareDO.setRoles(rolesList);
        return generalAgentShareDO;
    }

    /**
     * Populates a SelectiveUserUnshareDO object from the provided AgentUnshareRequestBody.
     *
     * @param agentUnshareRequestBody Contains details for agent unsharing.
     * @return A populated SelectiveAgentUnshareDO object.
     */
    private SelectiveUserUnshareDO populateSelectiveAgentUnshareDO(AgentUnshareRequestBody agentUnshareRequestBody) {

        SelectiveUserUnshareDO selectiveUserUnshareDO = new SelectiveUserUnshareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria =
                populateAgentCriteria(agentUnshareRequestBody.getAgentCriteria().getAgentIds());
        selectiveUserUnshareDO.setUserCriteria(userCriteria);

        // Set organizations.
        selectiveUserUnshareDO.setOrganizations(agentUnshareRequestBody.getOrganizations());

        return selectiveUserUnshareDO;
    }

    /**
     * Populates a GeneralAgentUnshareDO object from the provided AgentUnshareWithAllRequestBody.
     *
     * @param agentUnshareWithAllRequestBody Contains details for removing shared access.
     * @return A populated GeneralAgentUnshareDO object.
     */
    private GeneralUserUnshareDO populateGeneralUserUnshareDO(AgentUnshareWithAllRequestBody
                                                                      agentUnshareWithAllRequestBody) {

        GeneralUserUnshareDO generalUserUnshareDO = new GeneralUserUnshareDO();

        // Set user criteria.
        Map<String, UserCriteriaType> userCriteria =
                populateAgentCriteria(agentUnshareWithAllRequestBody.getAgentCriteria().getAgentIds());
        generalUserUnshareDO.setUserCriteria(userCriteria);

        return generalUserUnshareDO;
    }

    /**
     * Populates a AgentSharedOrganizationsResponse object from the provided ResponseSharedOrgsDO.
     *
     * @param result The ResponseSharedOrgsDO containing the shared organization's data.
     * @return A populated AgentSharedOrganizationsResponse object.
     */
    private AgentSharedOrganizationsResponse populateAgentSharedOrganizationsResponse(ResponseSharedOrgsDO result) {

        List<AgentSharedOrganizationsResponseLinks> responseLinks =
                populateAgentSharedOrganizationsResponseLinks(result.getResponseLinks());

        List<AgentSharedOrganizationsResponseSharedOrganizations> responseOrgs = new ArrayList<>();
        List<ResponseOrgDetailsDO> resultOrgDetails = result.getSharedOrgs();
        for (ResponseOrgDetailsDO resultOrgDetail : resultOrgDetails) {
            AgentSharedOrganizationsResponseSharedOrganizations org =
                    new AgentSharedOrganizationsResponseSharedOrganizations().orgId(
                                    resultOrgDetail.getOrganizationId())
                            .orgName(resultOrgDetail.getOrganizationName())
                            .sharedAgentId(resultOrgDetail.getSharedUserId())
                            .sharedType(resultOrgDetail.getSharedType().toString())
                            .rolesRef(resultOrgDetail.getRolesRef());
            responseOrgs.add(org);
        }

        return new AgentSharedOrganizationsResponse().links(responseLinks).sharedOrganizations(responseOrgs);
    }

    /**
     * Populates a AgentSharedRolesResponse object from the provided ResponseSharedRolesDO.
     *
     * @param result The ResponseSharedRolesDO containing the shared role's data.
     * @return A populated AgentSharedRolesResponse object.
     */
    private AgentSharedRolesResponse populateAgentSharedRolesResponse(ResponseSharedRolesDO result) {

        List<AgentSharedOrganizationsResponseLinks> responseLinks =
                populateAgentSharedOrganizationsResponseLinks(result.getResponseLinks());

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

        return new AgentSharedRolesResponse().links(responseLinks).roles(responseRoles);
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
     * Populates a list of AgentSharedOrganizationsResponseLinks objects from the provided list of ResponseLinkDO.
     *
     * @param resultLinks The list of ResponseLinkDO objects to be converted.
     * @return A list of AgentSharedOrganizationsResponseLinks objects.
     */
    private List<AgentSharedOrganizationsResponseLinks> populateAgentSharedOrganizationsResponseLinks(
            List<ResponseLinkDO> resultLinks) {

        List<AgentSharedOrganizationsResponseLinks> responseLinks = new ArrayList<>();
        for (ResponseLinkDO resultLink : resultLinks) {
            AgentSharedOrganizationsResponseLinks links = new AgentSharedOrganizationsResponseLinks();
            links.href(resultLink.getHref());
            links.rel(resultLink.getRel());
            responseLinks.add(links);
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
     * Creates a AgentSharingMgtClientException based on the provided error message.
     *
     * @param error The error message containing the code, message, and description.
     * @return A AgentSharingMgtClientException with the specified error details.
     */
    private UserSharingMgtClientException makeRequestError(AgentSharingMgtConstants.ErrorMessage error) {

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
