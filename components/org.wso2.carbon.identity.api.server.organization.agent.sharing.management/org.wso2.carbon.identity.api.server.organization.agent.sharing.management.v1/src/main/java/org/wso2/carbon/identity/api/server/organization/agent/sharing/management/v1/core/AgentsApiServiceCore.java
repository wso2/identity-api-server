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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentCriteria;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentOrgShareConfig;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharedOrganization;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharingPatchOperation;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharingPatchRequest;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.Link;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.RoleAssignment;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.RoleShareConfig;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.RoleShareConfigAudience;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.SharingMode;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.AgentSharingPolicyHandlerService;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.constant.AgentSharePatchOperation;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.constant.RoleAssignmentMode;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.exception.AgentSharingMgtClientException;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.exception.AgentSharingMgtException;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.GeneralAgentShareDO;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.GeneralAgentUnshareDO;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.GetAgentSharedOrgsDO;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.PatchOperationDO;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.ResponseAgentSharedOrgsDO;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.ResponseOrgDetailsAgentDO;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.RoleAssignmentDO;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.RoleWithAudienceDO;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.SelectiveAgentShareDO;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.SelectiveAgentShareOrgDetailsDO;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.SelectiveAgentUnshareDO;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.AgentSharePatchDO;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.dos.SharingModeDO;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.agentcriteria.AgentCriteriaType;
import org.wso2.carbon.identity.organization.management.organization.agent.sharing.models.agentcriteria.AgentIdList;
import org.wso2.carbon.identity.organization.resource.sharing.policy.management.constant.PolicyEnum;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.AGENT_IDS;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.ERROR_EMPTY_AGENT_SHARE_PATCH_PATH;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.ERROR_INVALID_CURSOR;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.ERROR_INVALID_LIMIT;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.ERROR_MISSING_AGENT_CRITERIA;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.ERROR_MISSING_AGENT_IDS;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.ERROR_UNSUPPORTED_AGENT_SHARE_PATCH_PATH;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.ERROR_UNSUPPORTED_AGENT_SHARE_POLICY;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.INVALID_AGENT_SHARE_PATCH_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.INVALID_GENERAL_AGENT_SHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.INVALID_GENERAL_AGENT_UNSHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.INVALID_SELECTIVE_AGENT_SHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.INVALID_SELECTIVE_AGENT_UNSHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.ErrorMessage.INVALID_UUID_FORMAT;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.RESPONSE_DETAIL_AGENT_SHARE;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.RESPONSE_DETAIL_AGENT_SHARE_PATCH;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.RESPONSE_DETAIL_AGENT_UNSHARE;
import static org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants.AgentSharingMgtConstants.RESPONSE_STATUS_PROCESSING;
import static org.wso2.carbon.identity.organization.management.service.util.Utils.getOrganizationId;

/**
 * Core service class for handling agent sharing management APIs V1.
 */
public class AgentsApiServiceCore {

    private static final Log LOG = LogFactory.getLog(AgentsApiServiceCore.class);
    private final AgentSharingPolicyHandlerService agentSharingPolicyHandlerService;

    /**
     * Creates the Agents API core service with the required backend service dependency.
     *
     * @param agentSharingPolicyHandlerService Backend service used to perform agent sharing operations.
     */
    public AgentsApiServiceCore(AgentSharingPolicyHandlerService agentSharingPolicyHandlerService) {

        this.agentSharingPolicyHandlerService = agentSharingPolicyHandlerService;
    }

    // -------------------------------------------------------------------------
    // Public methods corresponding to API endpoints
    // -------------------------------------------------------------------------

    /**
     * Shares agents with the organizations specified in the request.
     */
    public Response shareAgentsWithSelectedOrgs(AgentShareSelectedRequestBody agentShareSelectedRequestBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Selective agent sharing API v1 invoked from tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (agentShareSelectedRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_SELECTIVE_AGENT_SHARE_REQUEST_BODY))).build();
        }

        try {
            SelectiveAgentShareDO selectiveAgentShareDO = populateSelectiveAgentShareDO(agentShareSelectedRequestBody);
            agentSharingPolicyHandlerService.populateSelectiveAgentShare(selectiveAgentShareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_AGENT_SHARE)).build();
        } catch (AgentSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (AgentSharingMgtException e) {
            LOG.error("Error occurred while sharing agent with specific organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Shares agents with all organizations according to the provided global policy.
     */
    public Response shareAgentsWithAllOrgs(AgentShareAllRequestBody agentShareAllRequestBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("General agent sharing API v1 invoked from tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (agentShareAllRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_GENERAL_AGENT_SHARE_REQUEST_BODY))).build();
        }

        try {
            GeneralAgentShareDO generalAgentShareDO = populateGeneralAgentShareDO(agentShareAllRequestBody);
            agentSharingPolicyHandlerService.populateGeneralAgentShare(generalAgentShareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_AGENT_SHARE)).build();
        } catch (AgentSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (AgentSharingMgtException e) {
            LOG.error("Error occurred while sharing agent with all organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Revokes agent sharing from the organizations specified in the request.
     */
    public Response unshareAgentsFromSelectedOrgs(AgentUnshareSelectedRequestBody agentUnshareSelectedRequestBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Selective agent un-sharing API v1 invoked from tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (agentUnshareSelectedRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_SELECTIVE_AGENT_UNSHARE_REQUEST_BODY))).build();
        }

        try {
            SelectiveAgentUnshareDO selectiveAgentUnshareDO =
                    populateSelectiveAgentUnshareDO(agentUnshareSelectedRequestBody);
            agentSharingPolicyHandlerService.populateSelectiveAgentUnshare(selectiveAgentUnshareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_AGENT_UNSHARE)).build();
        } catch (AgentSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (AgentSharingMgtException e) {
            LOG.error("Error occurred while unsharing agent from specific organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Revokes all agent sharing associations for the identified agents.
     */
    public Response unshareAgentsFromAllOrgs(AgentUnshareAllRequestBody agentUnshareAllRequestBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("General agent un-sharing API v1 invoked from tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (agentUnshareAllRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_GENERAL_AGENT_UNSHARE_REQUEST_BODY))).build();
        }

        try {
            GeneralAgentUnshareDO generalAgentUnshareDO = populateGeneralAgentUnshareDO(agentUnshareAllRequestBody);
            agentSharingPolicyHandlerService.populateGeneralAgentUnshare(generalAgentUnshareDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_AGENT_UNSHARE)).build();
        } catch (AgentSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (AgentSharingMgtException e) {
            LOG.error("Error occurred while unsharing agent from all organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Applies PATCH operations to update attributes of already-shared agents.
     */
    public Response patchAgentSharing(AgentSharingPatchRequest agentSharingPatchRequest) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Patch agent sharing API v1 invoked from tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (agentSharingPatchRequest == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_AGENT_SHARE_PATCH_REQUEST_BODY))).build();
        }

        try {
            AgentSharePatchDO agentSharePatchDO = populateAgentSharePatch(agentSharingPatchRequest);
            agentSharingPolicyHandlerService.updateSharedAgentAttributes(agentSharePatchDO);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_AGENT_SHARE_PATCH)).build();
        } catch (AgentSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (AgentSharingMgtException e) {
            LOG.error("Error occurred while patching shared agent attributes.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Retrieves the list of organizations where the specified agent has shared access.
     */
    public Response getAgentSharedOrganizations(String agentId, String before, String after, String filter,
                                                Integer limit, Boolean recursive, String attributes) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get agent shared organizations API v1 invoked from tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain() +
                    " of agent: " + agentId);
        }
        if (agentId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(ERROR_MISSING_AGENT_IDS))).build();
        }

        try {
            UUID.fromString(agentId);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_UUID_FORMAT))).build();
        }

        if (limit != null && limit < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(ERROR_INVALID_LIMIT))).build();
        }

        final int resolvedLimit = (limit == null) ? 0 : limit;
        final boolean recursiveFlag = (recursive == null) || recursive;

        final int afterCursor;
        final int beforeCursor;
        try {
            afterCursor = decodeCursor(after);
            beforeCursor = decodeCursor(before);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(ERROR_INVALID_CURSOR))).build();
        }

        String organizationId = getOrganizationId();

        try {
            GetAgentSharedOrgsDO getAgentSharedOrgsDO =
                    new GetAgentSharedOrgsDO(agentId, organizationId, beforeCursor, afterCursor, filter, resolvedLimit,
                            recursiveFlag, splitAttributes(attributes));
            ResponseAgentSharedOrgsDO result =
                    agentSharingPolicyHandlerService.getAgentSharedOrganizations(getAgentSharedOrgsDO);

            AgentSharedOrganizationsResponse response =
                    mapAgentSharedOrganizationsResponse(result, agentId, filter, limit, recursive, attributes);
            return Response.ok().entity(response).build();
        } catch (AgentSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (AgentSharingMgtException e) {
            LOG.error("Error occurred while retrieving organizations shared with agent: " + agentId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers — build backend DOs from API request models
    // -------------------------------------------------------------------------

    private SelectiveAgentShareDO populateSelectiveAgentShareDO(
            AgentShareSelectedRequestBody agentShareSelectedRequestBody) throws AgentSharingMgtClientException {

        SelectiveAgentShareDO selectiveAgentShareDO = new SelectiveAgentShareDO();

        Map<String, AgentCriteriaType> agentCriteria =
                buildAgentCriteriaFromRequest(agentShareSelectedRequestBody.getAgentCriteria());
        selectiveAgentShareDO.setAgentCriteria(agentCriteria);

        List<SelectiveAgentShareOrgDetailsDO> organizationsList = new ArrayList<>();
        List<AgentOrgShareConfig> orgDetails = agentShareSelectedRequestBody.getOrganizations();
        if (orgDetails != null) {
            for (AgentOrgShareConfig orgDetail : orgDetails) {
                if (orgDetail != null) {
                    SelectiveAgentShareOrgDetailsDO orgDetailsDO = new SelectiveAgentShareOrgDetailsDO();
                    orgDetailsDO.setOrganizationId(orgDetail.getOrgId());
                    orgDetailsDO.setPolicy(resolvePolicy(orgDetail.getPolicy()));
                    orgDetailsDO.setRoleAssignments(buildRoleAssignmentFromRequest(orgDetail.getRoleAssignment()));
                    organizationsList.add(orgDetailsDO);
                }
            }
        }
        selectiveAgentShareDO.setOrganizations(organizationsList);

        return selectiveAgentShareDO;
    }

    private GeneralAgentShareDO populateGeneralAgentShareDO(AgentShareAllRequestBody agentShareAllRequestBody)
            throws AgentSharingMgtClientException {

        GeneralAgentShareDO generalAgentShareDO = new GeneralAgentShareDO();

        Map<String, AgentCriteriaType> agentCriteria =
                buildAgentCriteriaFromRequest(agentShareAllRequestBody.getAgentCriteria());
        generalAgentShareDO.setAgentCriteria(agentCriteria);
        generalAgentShareDO.setPolicy(resolvePolicy(agentShareAllRequestBody.getPolicy()));
        generalAgentShareDO.setRoleAssignments(
                buildRoleAssignmentFromRequest(agentShareAllRequestBody.getRoleAssignment()));

        return generalAgentShareDO;
    }

    private SelectiveAgentUnshareDO populateSelectiveAgentUnshareDO(
            AgentUnshareSelectedRequestBody agentUnshareSelectedRequestBody) throws AgentSharingMgtClientException {

        SelectiveAgentUnshareDO selectiveAgentUnshareDO = new SelectiveAgentUnshareDO();

        Map<String, AgentCriteriaType> agentCriteria =
                buildAgentCriteriaFromRequest(agentUnshareSelectedRequestBody.getAgentCriteria());
        selectiveAgentUnshareDO.setAgentCriteria(agentCriteria);
        selectiveAgentUnshareDO.setOrganizations(agentUnshareSelectedRequestBody.getOrgIds());

        return selectiveAgentUnshareDO;
    }

    private GeneralAgentUnshareDO populateGeneralAgentUnshareDO(AgentUnshareAllRequestBody agentUnshareAllRequestBody)
            throws AgentSharingMgtClientException {

        GeneralAgentUnshareDO generalAgentUnshareDO = new GeneralAgentUnshareDO();

        Map<String, AgentCriteriaType> agentCriteria =
                buildAgentCriteriaFromRequest(agentUnshareAllRequestBody.getAgentCriteria());
        generalAgentUnshareDO.setAgentCriteria(agentCriteria);

        return generalAgentUnshareDO;
    }

    private AgentSharePatchDO populateAgentSharePatch(AgentSharingPatchRequest agentSharingPatchRequest)
            throws AgentSharingMgtClientException {

        AgentSharePatchDO agentSharePatchDO = new AgentSharePatchDO();

        Map<String, AgentCriteriaType> agentCriteria =
                buildAgentCriteriaFromRequest(agentSharingPatchRequest.getAgentCriteria());
        agentSharePatchDO.setAgentCriteria(agentCriteria);
        agentSharePatchDO.setPatchOperations(
                buildPatchOperationsFromRequest(agentSharingPatchRequest.getOperations()));

        return agentSharePatchDO;
    }

    // -------------------------------------------------------------------------
    // Private helpers — map backend response DOs to API response models
    // -------------------------------------------------------------------------

    private AgentSharedOrganizationsResponse mapAgentSharedOrganizationsResponse(
            ResponseAgentSharedOrgsDO responseAgentSharedOrgsDO, String agentId, String filter, Integer limit,
            Boolean recursive, String attributes) {

        AgentSharedOrganizationsResponse response = new AgentSharedOrganizationsResponse();

        if (responseAgentSharedOrgsDO.getSharingMode() != null) {
            response.setSharingMode(toApiSharingMode(responseAgentSharedOrgsDO.getSharingMode()));
        }

        List<AgentSharedOrganization> orgs = new ArrayList<>();
        if (responseAgentSharedOrgsDO.getSharedOrgs() != null) {
            for (ResponseOrgDetailsAgentDO orgDO : responseAgentSharedOrgsDO.getSharedOrgs()) {
                orgs.add(toApiAgentSharedOrganization(orgDO));
            }
        }
        response.setOrganizations(orgs);

        List<Link> links =
                buildPaginationLinks(responseAgentSharedOrgsDO, agentId, filter, limit, recursive, attributes);
        response.setLinks(links);

        return response;
    }

    private AgentSharedOrganization toApiAgentSharedOrganization(ResponseOrgDetailsAgentDO orgDO) {

        AgentSharedOrganization org = new AgentSharedOrganization();
        org.setOrgId(orgDO.getOrganizationId());
        org.setOrgName(orgDO.getOrganizationName());
        org.setOrgHandle(orgDO.getOrganizationHandle());
        org.setOrgStatus(orgDO.getOrganizationStatus());
        org.setOrgRef(orgDO.getOrganizationReference());
        org.setHasChildren(orgDO.isHasChildren());
        org.setDepthFromRoot(orgDO.getDepthFromRoot());
        org.setParentOrgId(orgDO.getParentOrganizationId());
        org.setParentAgentId(orgDO.getAgentId());
        org.setSharedAgentId(orgDO.getSharedAgentId());

        if (orgDO.getSharedType() != null) {
            org.setSharedType(orgDO.getSharedType().toString());
        }
        if (orgDO.getSharingModeDO() != null) {
            org.setSharingMode(toApiSharingMode(orgDO.getSharingModeDO()));
        }
        if (orgDO.getRoleWithAudienceDOList() != null) {
            List<RoleShareConfig> roles = new ArrayList<>();
            for (RoleWithAudienceDO roleDO : orgDO.getRoleWithAudienceDOList()) {
                roles.add(toApiRole(roleDO));
            }
            org.setRoles(roles);
        }

        return org;
    }

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
            List<RoleShareConfig> roles = new ArrayList<>();
            if (raDO.getRoles() != null) {
                for (RoleWithAudienceDO roleDO : raDO.getRoles()) {
                    roles.add(toApiRole(roleDO));
                }
            }
            ra.setRoles(roles);
            apiMode.setRoleAssignment(ra);
        }
        return apiMode;
    }

    private RoleShareConfig toApiRole(RoleWithAudienceDO roleDO) {

        RoleShareConfig role = new RoleShareConfig();
        role.setDisplayName(roleDO.getRoleName());

        RoleShareConfigAudience audience = new RoleShareConfigAudience();
        audience.setDisplay(roleDO.getAudienceName());
        audience.setType(roleDO.getAudienceType());
        role.setAudience(audience);

        return role;
    }

    private List<Link> buildPaginationLinks(ResponseAgentSharedOrgsDO result, String agentId, String filter,
                                            Integer limit, Boolean recursive, String attributes) {

        UriInfo uriInfo = getCurrentRequestUriInfo();
        List<Link> links = new ArrayList<>();

        int nextCursor = result.getNextPageCursor();
        if (nextCursor > 0) {
            links.add(new Link()
                    .rel("next")
                    .href(buildSharedOrgsPageLink(uriInfo, agentId, filter, limit, recursive, attributes,
                            encodeCursor(nextCursor), null)));
        }
        int prevCursor = result.getPreviousPageCursor();
        if (prevCursor > 0) {
            links.add(new Link()
                    .rel("previous")
                    .href(buildSharedOrgsPageLink(uriInfo, agentId, filter, limit, recursive, attributes,
                            null, encodeCursor(prevCursor))));
        }
        return links;
    }

    private String buildSharedOrgsPageLink(UriInfo uriInfo, String agentId, String filter, Integer limit,
                                           Boolean recursive, String attributes, String after, String before) {

        if (uriInfo == null) {
            return buildRelativeSharedOrgsPageLink(agentId, filter, limit, recursive, attributes, after, before);
        }

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("agents").path(agentId).path("share");
        if (limit != null) builder.queryParam("limit", limit);
        if (filter != null && !filter.isEmpty()) builder.queryParam("filter", filter);
        if (recursive != null) builder.queryParam("recursive", recursive);
        if (attributes != null && !attributes.isEmpty()) builder.queryParam("attributes", attributes);
        if (after != null && !after.isEmpty()) builder.queryParam("after", after);
        else if (before != null && !before.isEmpty()) builder.queryParam("before", before);

        return builder.build().toString();
    }

    private String buildRelativeSharedOrgsPageLink(String agentId, String filter, Integer limit, Boolean recursive,
                                                   String attributes, String after, String before) {

        String base = "/o/api/server/v1/agents/" + urlEncode(agentId) + "/share";
        List<String> qp = new ArrayList<>();
        if (limit != null) qp.add("limit=" + limit);
        if (filter != null && !filter.isEmpty()) qp.add("filter=" + urlEncode(filter));
        if (recursive != null) qp.add("recursive=" + recursive);
        if (attributes != null && !attributes.isEmpty()) qp.add("attributes=" + urlEncode(attributes));
        if (after != null && !after.isEmpty()) qp.add("after=" + urlEncode(after));
        else if (before != null && !before.isEmpty()) qp.add("before=" + urlEncode(before));

        return qp.isEmpty() ? base : base + "?" + String.join("&", qp);
    }

    // -------------------------------------------------------------------------
    // Private helpers — criteria, role assignment, patch ops
    // -------------------------------------------------------------------------

    private Map<String, AgentCriteriaType> buildAgentCriteriaFromRequest(AgentCriteria agentCriteria)
            throws AgentSharingMgtClientException {

        if (agentCriteria == null) {
            throw makeRequestError(ERROR_MISSING_AGENT_CRITERIA);
        }
        Map<String, AgentCriteriaType> criteriaMap = new HashMap<>();
        if (agentCriteria.getAgentIds() != null && !agentCriteria.getAgentIds().isEmpty()) {
            criteriaMap.put(AGENT_IDS, new AgentIdList(agentCriteria.getAgentIds()));
        }
        if (criteriaMap.isEmpty()) {
            throw makeRequestError(ERROR_MISSING_AGENT_CRITERIA);
        }
        return criteriaMap;
    }

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

    private List<RoleWithAudienceDO> buildRoleWithAudienceDO(List<RoleShareConfig> roles) {

        List<RoleWithAudienceDO> rolesList = new ArrayList<>();
        if (roles != null) {
            for (RoleShareConfig role : roles) {
                if (role != null) {
                    RoleWithAudienceDO roleDO = new RoleWithAudienceDO();
                    roleDO.setRoleName(role.getDisplayName());
                    RoleShareConfigAudience audience = role.getAudience();
                    if (audience != null) {
                        roleDO.setAudienceName(audience.getDisplay());
                        roleDO.setAudienceType(audience.getType());
                        rolesList.add(roleDO);
                    }
                }
            }
        }
        return rolesList;
    }

    private List<PatchOperationDO> buildPatchOperationsFromRequest(List<AgentSharingPatchOperation> operations)
            throws AgentSharingMgtClientException {

        List<PatchOperationDO> patchOperations = new ArrayList<>();
        if (operations != null) {
            for (AgentSharingPatchOperation operation : operations) {
                if (operation != null) {
                    PatchOperationDO patchOperationDO = new PatchOperationDO();
                    patchOperationDO.setOperation(AgentSharePatchOperation.fromValue(operation.getOp()));
                    patchOperationDO.setPath(operation.getPath());
                    patchOperationDO.setValues(
                            buildPatchOperationValuesFromRequest(operation.getPath(), operation.getValue()));
                    patchOperations.add(patchOperationDO);
                }
            }
        }
        return patchOperations;
    }

    private Object buildPatchOperationValuesFromRequest(String path, List<RoleShareConfig> values)
            throws AgentSharingMgtClientException {

        if (path == null || path.isEmpty()) {
            throw makeRequestError(ERROR_EMPTY_AGENT_SHARE_PATCH_PATH);
        }
        if (isRolesPath(path)) {
            return buildRoleWithAudienceDO(values);
        }
        throw makeRequestError(ERROR_UNSUPPORTED_AGENT_SHARE_PATCH_PATH);
    }

    private PolicyEnum resolvePolicy(String policy) throws AgentSharingMgtClientException {

        try {
            return PolicyEnum.getPolicyByValue(policy);
        } catch (IllegalArgumentException e) {
            throw makeRequestError(ERROR_UNSUPPORTED_AGENT_SHARE_POLICY);
        }
    }

    private boolean isRolesPath(String path) {

        return path.trim().endsWith("].roles") || path.trim().endsWith(".roles");
    }

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

    private UriInfo getCurrentRequestUriInfo() {

        Message message = JAXRSUtils.getCurrentMessage();
        if (message == null) {
            return null;
        }
        return new UriInfoImpl(message);
    }

    private String encodeCursor(int cursor) {

        return Base64.getEncoder()
                .encodeToString(String.valueOf(cursor).getBytes(StandardCharsets.UTF_8));
    }

    private int decodeCursor(String cursor) {

        if (cursor == null) {
            return 0;
        }
        String decoded = new String(Base64.getDecoder().decode(cursor), StandardCharsets.UTF_8);
        return Integer.parseInt(decoded);
    }

    private String urlEncode(String value) {

        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not supported", e);
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers — build responses and errors
    // -------------------------------------------------------------------------

    private ProcessSuccessResponse getProcessSuccessResponse(String details) {

        ProcessSuccessResponse processSuccessResponse = new ProcessSuccessResponse();
        processSuccessResponse.status(RESPONSE_STATUS_PROCESSING);
        processSuccessResponse.setDetails(details);
        return processSuccessResponse;
    }

    private AgentSharingMgtClientException makeRequestError(AgentSharingMgtConstants.ErrorMessage error) {

        return new AgentSharingMgtClientException(error.getCode(), error.getMessage(), error.getDescription());
    }

    private Error buildErrorResponse(AgentSharingMgtException e) {

        return new Error().code(e.getErrorCode()).message(e.getMessage()).description(e.getDescription())
                .traceId(UUID.randomUUID().toString());
    }
}
