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

package org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionCriteria;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionOrgShareConfig;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionShareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionSharedOrganization;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionSharingMode;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionUnshareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionUnshareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.Link;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ProcessSuccessResponse;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.ConnectionSharingPolicyHandlerService;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.dto.ConnectionSharingModeDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.dto.GeneralConnectionShareDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.dto.GeneralConnectionUnshareDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.dto.GetConnectionSharedOrgsDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.dto.ResponseConnectionOrgDetailsDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.dto.ResponseSharedConnectionOrgsDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.dto.SelectiveConnectionShareDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.dto.SelectiveConnectionShareOrgConfigDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.dto.SelectiveConnectionUnshareDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.exception.ConnectionSharingMgtClientException;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.exception.ConnectionSharingMgtException;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.models.connectioncriteria.ConnectionCriteriaType;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.models.connectioncriteria.ConnectionIdList;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.models.connectioncriteria.ConnectionNameList;
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

import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.CONNECTION_IDS;
import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.CONNECTION_NAMES;
import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.ErrorMessage.ERROR_INVALID_CURSOR;
import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.ErrorMessage.ERROR_INVALID_LIMIT;
import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.ErrorMessage.ERROR_MISSING_CONNECTION_CRITERIA;
import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.ErrorMessage.ERROR_UNSUPPORTED_CONNECTION_SHARE_POLICY;
import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.ErrorMessage.INVALID_GENERAL_CONNECTION_SHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.ErrorMessage.INVALID_GENERAL_CONNECTION_UNSHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.ErrorMessage.INVALID_SELECTIVE_CONNECTION_SHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.ErrorMessage.INVALID_SELECTIVE_CONNECTION_UNSHARE_REQUEST_BODY;
import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.ErrorMessage.INVALID_UUID_FORMAT;
import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.RESPONSE_DETAIL_CONNECTION_SHARE;
import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.RESPONSE_DETAIL_CONNECTION_UNSHARE;
import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.RESPONSE_STATUS_PROCESSING;
import static org.wso2.carbon.identity.organization.management.service.util.Utils.getOrganizationId;

/**
 * Core service class for handling connection sharing management APIs V1.
 */
public class ConnectionsApiServiceCore {

    private static final Log LOG = LogFactory.getLog(ConnectionsApiServiceCore.class);
    private final ConnectionSharingPolicyHandlerService connectionSharingPolicyHandlerService;

    /**
     * Creates the Connections API v1 core service with the required backend service dependency.
     *
     * @param connectionSharingPolicyHandlerService Backend service for connection sharing operations.
     */
    public ConnectionsApiServiceCore(ConnectionSharingPolicyHandlerService connectionSharingPolicyHandlerService) {

        this.connectionSharingPolicyHandlerService = connectionSharingPolicyHandlerService;
    }

    // Public methods corresponding to the endpoints.

    /**
     * Shares connections with the organizations specified in the request.
     *
     * @param connectionShareSelectedRequestBody Request payload containing connection criteria and target
     *                                           organizations.
     * @return JAX-RS response.
     */
    public Response shareConnectionsWithSelectedOrgs(
            ConnectionShareSelectedRequestBody connectionShareSelectedRequestBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Selective connection sharing API v1 invoked from tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (connectionShareSelectedRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_SELECTIVE_CONNECTION_SHARE_REQUEST_BODY)))
                    .build();
        }

        try {
            SelectiveConnectionShareDTO dto = buildSelectiveConnectionShareDTO(connectionShareSelectedRequestBody);
            connectionSharingPolicyHandlerService.populateSelectiveConnectionShare(dto);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_CONNECTION_SHARE)).build();
        } catch (ConnectionSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (ConnectionSharingMgtException e) {
            LOG.error("Error occurred while sharing connections with specific organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Shares connections with all child organizations according to the provided policy.
     *
     * @param connectionShareWithAllRequestBody Request payload containing connection criteria and global policy.
     * @return JAX-RS response.
     */
    public Response shareConnectionsWithAllOrgs(ConnectionShareWithAllRequestBody connectionShareWithAllRequestBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("General connection sharing API v1 invoked from tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (connectionShareWithAllRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_GENERAL_CONNECTION_SHARE_REQUEST_BODY)))
                    .build();
        }

        try {
            GeneralConnectionShareDTO dto = buildGeneralConnectionShareDTO(connectionShareWithAllRequestBody);
            connectionSharingPolicyHandlerService.populateGeneralConnectionShare(dto);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_CONNECTION_SHARE)).build();
        } catch (ConnectionSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (ConnectionSharingMgtException e) {
            LOG.error("Error occurred while sharing connections with all organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Revokes connection sharing from the organizations specified in the request.
     *
     * @param connectionUnshareSelectedRequestBody Request payload containing connection criteria and organization IDs.
     * @return JAX-RS response.
     */
    public Response unshareConnectionsFromSelectedOrgs(
            ConnectionUnshareSelectedRequestBody connectionUnshareSelectedRequestBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Selective connection un-sharing API v1 invoked from tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (connectionUnshareSelectedRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_SELECTIVE_CONNECTION_UNSHARE_REQUEST_BODY)))
                    .build();
        }

        try {
            SelectiveConnectionUnshareDTO dto =
                    buildSelectiveConnectionUnshareDTO(connectionUnshareSelectedRequestBody);
            connectionSharingPolicyHandlerService.populateSelectiveConnectionUnshare(dto);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_CONNECTION_UNSHARE)).build();
        } catch (ConnectionSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (ConnectionSharingMgtException e) {
            LOG.error("Error occurred while unsharing connections from specific organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Revokes all connection sharing associations for the connections identified by the given criteria.
     *
     * @param connectionUnshareWithAllRequestBody Request payload containing connection criteria.
     * @return JAX-RS response.
     */
    public Response unshareConnectionsFromAllOrgs(
            ConnectionUnshareWithAllRequestBody connectionUnshareWithAllRequestBody) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("General connection un-sharing API v1 invoked from tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }
        if (connectionUnshareWithAllRequestBody == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(INVALID_GENERAL_CONNECTION_UNSHARE_REQUEST_BODY)))
                    .build();
        }

        try {
            GeneralConnectionUnshareDTO dto = buildGeneralConnectionUnshareDTO(connectionUnshareWithAllRequestBody);
            connectionSharingPolicyHandlerService.populateGeneralConnectionUnshare(dto);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(getProcessSuccessResponse(RESPONSE_DETAIL_CONNECTION_UNSHARE)).build();
        } catch (ConnectionSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (ConnectionSharingMgtException e) {
            LOG.error("Error occurred while unsharing connections from all organizations.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Retrieves the list of organizations a specific connection has been shared with.
     *
     * @param connectionId ID of the connection whose shared organizations are to be retrieved.
     * @param before       Cursor for pagination to get results before the specified cursor.
     * @param after        Cursor for pagination to get results after the specified cursor.
     * @param filter       Filter string to narrow down results.
     * @param limit        Maximum number of results to return.
     * @param recursive    Whether to include organizations shared recursively.
     * @param attributes   Comma-separated list of additional attributes to include in the response.
     * @return JAX-RS response containing the list of shared organizations.
     */
    public Response getConnectionSharedOrganizations(String connectionId, String before, String after, String filter,
                                                     Integer limit, Boolean recursive, String attributes) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get connection shared organizations API v1 invoked from tenantDomain: " +
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain() +
                    " for connection: " + connectionId);
        }

        try {
            UUID.fromString(connectionId);
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

        if (afterCursor < 0 || beforeCursor < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse(makeRequestError(ERROR_INVALID_CURSOR))).build();
        }

        String organizationId = getOrganizationId();

        try {
            GetConnectionSharedOrgsDTO dto = new GetConnectionSharedOrgsDTO();
            dto.setConnectionId(connectionId);
            dto.setInitiatingOrgId(organizationId);
            dto.setBefore(beforeCursor);
            dto.setAfter(afterCursor);
            dto.setFilter(filter);
            dto.setLimit(resolvedLimit);
            dto.setRecursive(recursiveFlag);
            dto.setAttributes(splitAttributes(attributes));

            ResponseSharedConnectionOrgsDTO result =
                    connectionSharingPolicyHandlerService.getConnectionSharedOrganizations(dto);

            ConnectionSharedOrganizationsResponse response =
                    mapConnectionSharedOrganizationsResponse(result, connectionId, filter, limit, recursive,
                            attributes);
            return Response.ok().entity(response).build();
        } catch (ConnectionSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (ConnectionSharingMgtException e) {
            LOG.error("Error occurred while retrieving organizations for connection: " + connectionId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    // Helper methods to build DTOs for the service layer.

    private SelectiveConnectionShareDTO buildSelectiveConnectionShareDTO(
            ConnectionShareSelectedRequestBody body) throws ConnectionSharingMgtClientException {

        SelectiveConnectionShareDTO dto = new SelectiveConnectionShareDTO();
        dto.setConnectionCriteria(buildConnectionCriteriaFromRequest(body.getConnectionCriteria()));

        List<SelectiveConnectionShareOrgConfigDTO> orgs = new ArrayList<>();
        if (body.getOrganizations() != null) {
            for (ConnectionOrgShareConfig orgConfig : body.getOrganizations()) {
                if (orgConfig != null) {
                    SelectiveConnectionShareOrgConfigDTO orgDto = new SelectiveConnectionShareOrgConfigDTO();
                    orgDto.setOrgId(orgConfig.getOrgId());
                    if (orgConfig.getPolicy() != null) {
                        orgDto.setPolicy(resolvePolicy(orgConfig.getPolicy().value()));
                    }
                    orgs.add(orgDto);
                }
            }
        }
        dto.setOrganizations(orgs);
        return dto;
    }

    private GeneralConnectionShareDTO buildGeneralConnectionShareDTO(
            ConnectionShareWithAllRequestBody body) throws ConnectionSharingMgtClientException {

        GeneralConnectionShareDTO dto = new GeneralConnectionShareDTO();
        dto.setConnectionCriteria(buildConnectionCriteriaFromRequest(body.getConnectionCriteria()));
        if (body.getPolicy() != null) {
            dto.setPolicy(resolvePolicy(body.getPolicy().value()));
        }
        return dto;
    }

    private SelectiveConnectionUnshareDTO buildSelectiveConnectionUnshareDTO(
            ConnectionUnshareSelectedRequestBody body) throws ConnectionSharingMgtClientException {

        SelectiveConnectionUnshareDTO dto = new SelectiveConnectionUnshareDTO();
        dto.setConnectionCriteria(buildConnectionCriteriaFromRequest(body.getConnectionCriteria()));
        dto.setOrgIds(body.getOrgIds());
        return dto;
    }

    private GeneralConnectionUnshareDTO buildGeneralConnectionUnshareDTO(
            ConnectionUnshareWithAllRequestBody body) throws ConnectionSharingMgtClientException {

        GeneralConnectionUnshareDTO dto = new GeneralConnectionUnshareDTO();
        dto.setConnectionCriteria(buildConnectionCriteriaFromRequest(body.getConnectionCriteria()));
        return dto;
    }

    private Map<String, ConnectionCriteriaType> buildConnectionCriteriaFromRequest(
            ConnectionCriteria connectionCriteria) throws ConnectionSharingMgtClientException {

        if (connectionCriteria == null) {
            throw makeRequestError(ERROR_MISSING_CONNECTION_CRITERIA);
        }

        Map<String, ConnectionCriteriaType> criteriaMap = new HashMap<>();

        if (connectionCriteria.getConnectionIds() != null && !connectionCriteria.getConnectionIds().isEmpty()) {
            criteriaMap.put(CONNECTION_IDS, new ConnectionIdList(connectionCriteria.getConnectionIds()));
        }
        if (connectionCriteria.getConnectionNames() != null && !connectionCriteria.getConnectionNames().isEmpty()) {
            criteriaMap.put(CONNECTION_NAMES, new ConnectionNameList(connectionCriteria.getConnectionNames()));
        }

        if (criteriaMap.isEmpty()) {
            throw makeRequestError(ERROR_MISSING_CONNECTION_CRITERIA);
        }

        return criteriaMap;
    }

    private PolicyEnum resolvePolicy(String policyValue) throws ConnectionSharingMgtClientException {

        try {
            return PolicyEnum.getPolicyByValue(policyValue);
        } catch (IllegalArgumentException e) {
            throw makeRequestError(ERROR_UNSUPPORTED_CONNECTION_SHARE_POLICY);
        }
    }

    // Helper methods to map service layer responses to API layer responses.

    private ConnectionSharedOrganizationsResponse mapConnectionSharedOrganizationsResponse(
            ResponseSharedConnectionOrgsDTO result, String connectionId, String filter, Integer limit,
            Boolean recursive, String attributes) {

        ConnectionSharedOrganizationsResponse response = new ConnectionSharedOrganizationsResponse();

        if (result.getSharingMode() != null) {
            response.setSharingMode(toApiConnectionSharingMode(result.getSharingMode()));
        }

        List<ConnectionSharedOrganization> orgs = new ArrayList<>();
        if (result.getSharedOrgs() != null) {
            for (ResponseConnectionOrgDetailsDTO orgDTO : result.getSharedOrgs()) {
                orgs.add(toApiConnectionSharedOrganization(orgDTO));
            }
        }
        response.setOrganizations(orgs);

        List<Link> links = buildPaginationLinks(result, connectionId, filter, limit, recursive, attributes);
        response.setLinks(links);

        return response;
    }

    private ConnectionSharedOrganization toApiConnectionSharedOrganization(ResponseConnectionOrgDetailsDTO orgDTO) {

        ConnectionSharedOrganization org = new ConnectionSharedOrganization();
        org.setOrgId(orgDTO.getOrgId());
        org.setOrgName(orgDTO.getOrgName());
        org.setOrgHandle(orgDTO.getOrgHandle());
        org.setOrgStatus(orgDTO.getStatus());
        org.setOrgRef(orgDTO.getOrgRef());
        org.setHasChildren(orgDTO.getHasChildren());
        org.setDepthFromRoot(orgDTO.getDepthFromRoot());
        org.setParentOrgId(orgDTO.getParentOrgId());
        org.setParentConnectionId(orgDTO.getParentConnectionId());
        org.setSharedConnectionId(orgDTO.getSharedConnectionId());
        if (orgDTO.getSharingMode() != null) {
            org.setSharingMode(toApiConnectionSharingMode(orgDTO.getSharingMode()));
        }
        return org;
    }

    private ConnectionSharingMode toApiConnectionSharingMode(ConnectionSharingModeDTO modeDTO) {

        ConnectionSharingMode apiMode = new ConnectionSharingMode();
        if (modeDTO.getPolicy() != null) {
            try {
                apiMode.setPolicy(
                        ConnectionSharingMode.PolicyEnum.fromValue(modeDTO.getPolicy().getValue()));
            } catch (IllegalArgumentException e) {
                LOG.warn("Unrecognised policy value in response: " + modeDTO.getPolicy().getValue());
            }
        }
        return apiMode;
    }

    private List<Link> buildPaginationLinks(ResponseSharedConnectionOrgsDTO result, String connectionId, String filter,
                                            Integer limit, Boolean recursive, String attributes) {

        UriInfo uriInfo = getCurrentRequestUriInfo();
        List<Link> links = new ArrayList<>();

        int nextCursor = result.getNextPageCursor();
        if (nextCursor > 0) {
            links.add(new Link()
                    .rel("next")
                    .href(buildSharedOrgsPageLink(uriInfo, connectionId, filter, limit, recursive, attributes,
                            encodeCursor(nextCursor), null)));
        }

        int prevCursor = result.getPreviousPageCursor();
        if (prevCursor > 0) {
            links.add(new Link()
                    .rel("previous")
                    .href(buildSharedOrgsPageLink(uriInfo, connectionId, filter, limit, recursive, attributes,
                            null, encodeCursor(prevCursor))));
        }

        return links;
    }

    private String buildSharedOrgsPageLink(UriInfo uriInfo, String connectionId, String filter, Integer limit,
                                           Boolean recursive, String attributes, String after, String before) {

        if (uriInfo == null) {
            return buildRelativeSharedOrgsPageLink(connectionId, filter, limit, recursive, attributes, after, before);
        }

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("connections")
                .path(connectionId)
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
        if (after != null && !after.isEmpty()) {
            builder.queryParam("after", after);
        } else if (before != null && !before.isEmpty()) {
            builder.queryParam("before", before);
        }

        return builder.build().toString();
    }

    private String buildRelativeSharedOrgsPageLink(String connectionId, String filter, Integer limit,
                                                   Boolean recursive, String attributes, String after, String before) {

        String base = "/o/api/server/v1/connections/" + urlEncode(connectionId) + "/share";
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

    private UriInfo getCurrentRequestUriInfo() {

        Message message = JAXRSUtils.getCurrentMessage();
        if (message == null) {
            return null;
        }
        return new UriInfoImpl(message);
    }

    // Utility helpers.

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

    private int decodeCursor(String cursor) {

        if (cursor == null) {
            return 0;
        }
        String decoded = new String(Base64.getDecoder().decode(cursor), StandardCharsets.UTF_8);
        return Integer.parseInt(decoded);
    }

    private String encodeCursor(int cursor) {

        return Base64.getEncoder()
                .encodeToString(String.valueOf(cursor).getBytes(StandardCharsets.UTF_8));
    }

    private String urlEncode(String value) {

        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not supported", e);
        }
    }

    // Response and error builders.

    private ProcessSuccessResponse getProcessSuccessResponse(String details) {

        ProcessSuccessResponse response = new ProcessSuccessResponse();
        response.status(RESPONSE_STATUS_PROCESSING);
        response.setDetails(details);
        return response;
    }

    private ConnectionSharingMgtClientException makeRequestError(
            ConnectionSharingMgtConstants.ErrorMessage error) {

        return new ConnectionSharingMgtClientException(error.getCode(), error.getMessage(), error.getDescription());
    }

    private Error buildErrorResponse(ConnectionSharingMgtException e) {

        return new Error().code(e.getErrorCode()).message(e.getMessage()).description(e.getDescription())
                .traceId(UUID.randomUUID().toString());
    }
}
