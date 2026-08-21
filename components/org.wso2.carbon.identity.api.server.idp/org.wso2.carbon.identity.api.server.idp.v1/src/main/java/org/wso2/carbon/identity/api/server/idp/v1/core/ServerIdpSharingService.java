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

package org.wso2.carbon.identity.api.server.idp.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.idp.common.Constants;
import org.wso2.carbon.identity.api.server.idp.v1.model.BasicOrganizationResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderShareAllRequestBody;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderUnshareAllRequestBody;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderUnshareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.idp.v1.model.Link;
import org.wso2.carbon.identity.api.server.idp.v1.model.OrgShareConfig;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.SharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.SharingMode;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.api.dto.ConnectionSharingModeDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.api.dto.GeneralConnectionShareDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.api.dto.GeneralConnectionUnshareDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.api.dto.GetConnectionSharedOrgsDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.api.dto.ResponseConnectionOrgDetailsDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.api.dto.ResponseSharedConnectionOrgsDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.api.dto.SelectiveConnectionShareDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.api.dto.SelectiveConnectionShareOrgConfigDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.api.dto.SelectiveConnectionUnshareDTO;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.api.exception.ConnectionSharingMgtClientException;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.api.exception.ConnectionSharingMgtException;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.api.service.ConnectionSharingPolicyHandlerService;
import org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants;
import org.wso2.carbon.identity.organization.resource.sharing.policy.management.constant.PolicyEnum;
import org.wso2.carbon.identity.organization.resource.sharing.policy.management.constant.ResourceType;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForBody;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.ASYNC_OPERATION_RESPONSE_STATUS;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.EXCLUDED_ATTRIBUTES_PARAM;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.FILTER_PARAM;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.IDP_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.IDP_SHARE_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.IDP_SHARE_TRIGGER_SUCCESS;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.IDP_UNSHARE_TRIGGER_SUCCESS;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.INCLUDED_ATTRIBUTES_PARAM;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.LIMIT_PARAM;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.PAGE_LINK_REL_NEXT;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.PAGE_LINK_REL_PREVIOUS;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.PATH_SEPERATOR;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.RECURSIVE_PARAM;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ORGANIZATION_PATH;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.PAGINATION_AFTER;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.PAGINATION_BEFORE;
import static org.wso2.carbon.identity.organization.management.service.util.Utils.getOrganizationId;

/**
 * Calls internal osgi services to perform identity provider sharing related operations.
 */
public class ServerIdpSharingService {

    private static final Log log = LogFactory.getLog(ServerIdpSharingService.class);

    private final ConnectionSharingPolicyHandlerService connectionSharingPolicyHandlerService;

    public ServerIdpSharingService(ConnectionSharingPolicyHandlerService connectionSharingPolicyHandlerService) {

        this.connectionSharingPolicyHandlerService = connectionSharingPolicyHandlerService;
    }

    /**
     * Share an identity provider with selected organizations.
     *
     * @param requestBody Request body of the share request.
     * @return The status of the operation.
     */
    public Response shareIdentityProviderWithSelected(IdentityProviderShareSelectedRequestBody requestBody) {

        if (requestBody == null || StringUtils.isBlank(requestBody.getIdentityProviderId())) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_SHARE_REQUEST_BODY, null);
        }
        if (CollectionUtils.isEmpty(requestBody.getOrganizations())) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_SHARE_REQUEST_NO_ORGANIZATIONS, null);
        }
        List<SelectiveConnectionShareOrgConfigDTO> organizations = new ArrayList<>();
        for (OrgShareConfig orgShareConfig : requestBody.getOrganizations()) {
            SelectiveConnectionShareOrgConfigDTO orgConfig = new SelectiveConnectionShareOrgConfigDTO();
            orgConfig.setOrgId(orgShareConfig.getOrgId());
            if (orgShareConfig.getPolicy() != null) {
                orgConfig.setPolicy(getPolicyEnum(orgShareConfig.getPolicy()));
            }
            organizations.add(orgConfig);
        }
        SelectiveConnectionShareDTO selectiveConnectionShareDTO = new SelectiveConnectionShareDTO();
        selectiveConnectionShareDTO.setConnectionId(requestBody.getIdentityProviderId());
        selectiveConnectionShareDTO.setResourceType(ResourceType.CONNECTION_IDENTITY_PROVIDER);
        selectiveConnectionShareDTO.setOrganizations(organizations);
        try {
            connectionSharingPolicyHandlerService.populateSelectiveConnectionShare(selectiveConnectionShareDTO);
            return Response.accepted()
                    .entity(createProcessSuccessResponse(IDP_SHARE_TRIGGER_SUCCESS)).build();
        } catch (ConnectionSharingMgtException e) {
            throw handleConnectionSharingException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_SHARING_IDP);
        }
    }

    /**
     * Share an identity provider with all organizations.
     *
     * @param requestBody Request body of the share request.
     * @return The status of the operation.
     */
    public Response shareIdentityProviderWithAll(IdentityProviderShareAllRequestBody requestBody) {

        if (requestBody == null || StringUtils.isBlank(requestBody.getIdentityProviderId())
                || requestBody.getPolicy() == null) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_SHARE_REQUEST_BODY, null);
        }
        PolicyEnum policy = getPolicyEnum(requestBody.getPolicy());
        if (PolicyEnum.ALL_EXISTING_ORGS_ONLY != policy && PolicyEnum.ALL_EXISTING_AND_FUTURE_ORGS != policy) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_SHARE_POLICY, String.valueOf(policy));
        }
        GeneralConnectionShareDTO generalConnectionShareDTO = new GeneralConnectionShareDTO();
        generalConnectionShareDTO.setConnectionId(requestBody.getIdentityProviderId());
        generalConnectionShareDTO.setResourceType(ResourceType.CONNECTION_IDENTITY_PROVIDER);
        generalConnectionShareDTO.setPolicy(policy);
        try {
            connectionSharingPolicyHandlerService.populateGeneralConnectionShare(generalConnectionShareDTO);
            return Response.accepted()
                    .entity(createProcessSuccessResponse(IDP_SHARE_TRIGGER_SUCCESS)).build();
        } catch (ConnectionSharingMgtException e) {
            throw handleConnectionSharingException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_SHARING_IDP);
        }
    }

    /**
     * Unshare an identity provider from selected organizations.
     *
     * @param requestBody Request body of the unshare request.
     * @return The status of the operation.
     */
    public Response unshareIdentityProviderFromSelected(IdentityProviderUnshareSelectedRequestBody requestBody) {

        if (requestBody == null || StringUtils.isBlank(requestBody.getIdentityProviderId())) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_SHARE_REQUEST_BODY, null);
        }
        if (CollectionUtils.isEmpty(requestBody.getOrgIds())) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_SHARE_REQUEST_NO_ORGANIZATIONS, null);
        }
        SelectiveConnectionUnshareDTO selectiveConnectionUnshareDTO = new SelectiveConnectionUnshareDTO();
        selectiveConnectionUnshareDTO.setConnectionId(requestBody.getIdentityProviderId());
        selectiveConnectionUnshareDTO.setResourceType(ResourceType.CONNECTION_IDENTITY_PROVIDER);
        selectiveConnectionUnshareDTO.setOrgIds(requestBody.getOrgIds());
        try {
            connectionSharingPolicyHandlerService.populateSelectiveConnectionUnshare(selectiveConnectionUnshareDTO);
            return Response.accepted()
                    .entity(createProcessSuccessResponse(IDP_UNSHARE_TRIGGER_SUCCESS)).build();
        } catch (ConnectionSharingMgtException e) {
            throw handleConnectionSharingException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UNSHARING_IDP);
        }
    }

    /**
     * Unshare an identity provider from all organizations.
     *
     * @param requestBody Request body of the unshare request.
     * @return The status of the operation.
     */
    public Response unshareIdentityProviderFromAll(IdentityProviderUnshareAllRequestBody requestBody) {

        if (requestBody == null || StringUtils.isBlank(requestBody.getIdentityProviderId())) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_SHARE_REQUEST_BODY, null);
        }
        GeneralConnectionUnshareDTO generalConnectionUnshareDTO = new GeneralConnectionUnshareDTO();
        generalConnectionUnshareDTO.setConnectionId(requestBody.getIdentityProviderId());
        generalConnectionUnshareDTO.setResourceType(ResourceType.CONNECTION_IDENTITY_PROVIDER);
        try {
            connectionSharingPolicyHandlerService.populateGeneralConnectionUnshare(generalConnectionUnshareDTO);
            return Response.accepted()
                    .entity(createProcessSuccessResponse(IDP_UNSHARE_TRIGGER_SUCCESS)).build();
        } catch (ConnectionSharingMgtException e) {
            throw handleConnectionSharingException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UNSHARING_IDP);
        }
    }

    /**
     * Returns the organizations that the given identity provider is shared with.
     *
     * @param identityProviderId ID of the identity provider.
     * @param before             Base64 encoded cursor for backward pagination.
     * @param after              Base64 encoded cursor for forward pagination.
     * @param filter             Filter to apply on the organizations.
     * @param limit              Maximum number of organizations to return.
     * @param recursive          Whether to return shared organizations in all levels of the hierarchy.
     * @param excludedAttributes Attributes to exclude from the response.
     * @param attributes         Attributes to include in the response.
     * @return The organizations that the identity provider is shared with.
     */
    public Response getSharedOrganizations(String identityProviderId, String before, String after, String filter,
                                           Integer limit, Boolean recursive, String excludedAttributes,
                                           String attributes) {

        int limitValue = (limit == null) ? 0 : limit;
        // To return all shared organizations if the recursive flag was not provided.
        boolean recursiveFlag = (recursive == null) || recursive;
        GetConnectionSharedOrgsDTO getConnectionSharedOrgsDTO = new GetConnectionSharedOrgsDTO();
        getConnectionSharedOrgsDTO.setConnectionId(identityProviderId);
        getConnectionSharedOrgsDTO.setResourceType(ResourceType.CONNECTION_IDENTITY_PROVIDER);
        getConnectionSharedOrgsDTO.setInitiatingOrgId(getOrganizationId());
        getConnectionSharedOrgsDTO.setBefore(decodeCursor(before));
        getConnectionSharedOrgsDTO.setAfter(decodeCursor(after));
        getConnectionSharedOrgsDTO.setFilter(filter);
        getConnectionSharedOrgsDTO.setLimit(limitValue);
        getConnectionSharedOrgsDTO.setRecursive(recursiveFlag);
        getConnectionSharedOrgsDTO.setAttributes(resolveAttributeList(attributes));
        try {
            ResponseSharedConnectionOrgsDTO sharedOrgsResponse =
                    connectionSharingPolicyHandlerService.getConnectionSharedOrganizations(
                            getConnectionSharedOrgsDTO);
            String paginationQueryParams = buildPaginationQueryParams(filter, limitValue, recursive,
                    excludedAttributes, attributes);
            return Response.ok()
                    .entity(createSharedOrgsResponse(sharedOrgsResponse, paginationQueryParams, identityProviderId))
                    .build();
        } catch (ConnectionSharingMgtException e) {
            throw handleConnectionSharingException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_SHARED_ORGS);
        }
    }

    private SharedOrganizationsResponse createSharedOrgsResponse(ResponseSharedConnectionOrgsDTO sharedOrgsResponse,
                                                                 String paginationQueryParams,
                                                                 String identityProviderId) {

        SharedOrganizationsResponse response = new SharedOrganizationsResponse();
        if (sharedOrgsResponse == null) {
            return response;
        }
        if (sharedOrgsResponse.getSharedOrgs() != null) {
            for (ResponseConnectionOrgDetailsDTO orgDetails : sharedOrgsResponse.getSharedOrgs()) {
                BasicOrganizationResponse basicOrganizationResponse = new BasicOrganizationResponse()
                        .id(orgDetails.getOrgId())
                        .name(orgDetails.getOrgName())
                        .orgHandle(orgDetails.getOrgHandle())
                        .parentId(orgDetails.getParentOrgId())
                        .depthFromRoot(orgDetails.getDepthFromRoot())
                        .ref(buildOrganizationURL(orgDetails.getOrgId()).toString())
                        .hasChildren(orgDetails.getHasChildren())
                        .sharingMode(convertSharingModeToResponseType(orgDetails.getSharingMode()));
                if (StringUtils.isNotBlank(orgDetails.getStatus())) {
                    basicOrganizationResponse.setStatus(
                            BasicOrganizationResponse.StatusEnum.valueOf(orgDetails.getStatus()));
                }
                response.addOrganizationsItem(basicOrganizationResponse);
            }
        }
        response.setSharingMode(convertSharingModeToResponseType(sharedOrgsResponse.getSharingMode()));
        if (sharedOrgsResponse.getNextPageCursor() != 0) {
            Link nextLink = new Link();
            String base64EncodedCursor = Base64.getEncoder().encodeToString(
                    String.valueOf(sharedOrgsResponse.getNextPageCursor()).getBytes(StandardCharsets.UTF_8));
            nextLink.setHref(URI.create(buildURIForPagination(paginationQueryParams, identityProviderId) + "&"
                    + PAGINATION_AFTER + "=" + base64EncodedCursor).toString());
            nextLink.setRel(PAGE_LINK_REL_NEXT);
            response.addLinksItem(nextLink);
        }
        if (sharedOrgsResponse.getPreviousPageCursor() != 0) {
            Link previousLink = new Link();
            String base64EncodedCursor = Base64.getEncoder().encodeToString(
                    String.valueOf(sharedOrgsResponse.getPreviousPageCursor()).getBytes(StandardCharsets.UTF_8));
            previousLink.setHref(URI.create(buildURIForPagination(paginationQueryParams, identityProviderId) + "&"
                    + PAGINATION_BEFORE + "=" + base64EncodedCursor).toString());
            previousLink.setRel(PAGE_LINK_REL_PREVIOUS);
            response.addLinksItem(previousLink);
        }
        return response;
    }

    private SharingMode convertSharingModeToResponseType(ConnectionSharingModeDTO sharingModeDTO) {

        if (sharingModeDTO == null || sharingModeDTO.getPolicy() == null) {
            return null;
        }
        SharingMode sharingMode = new SharingMode();
        sharingMode.setPolicy(sharingModeDTO.getPolicy().name());
        return sharingMode;
    }

    private List<String> resolveAttributeList(String attributes) {

        if (StringUtils.isBlank(attributes)) {
            return Collections.emptyList();
        }
        List<String> attributeList = new ArrayList<>();
        for (String attribute : Arrays.asList(attributes.split(","))) {
            if (StringUtils.isNotBlank(attribute)) {
                attributeList.add(attribute.trim());
            }
        }
        return attributeList;
    }

    private int decodeCursor(String encodedCursor) {

        if (StringUtils.isBlank(encodedCursor)) {
            return 0;
        }
        try {
            return Integer.parseInt(
                    new String(Base64.getDecoder().decode(encodedCursor), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            throw handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_PAGINATION_CURSOR, null);
        }
    }

    private String buildPaginationQueryParams(String filter, int limit, Boolean recursive, String excludedAttributes,
                                              String attributes) {

        StringBuilder urlStringBuilder = new StringBuilder("?");
        if (limit != 0) {
            urlStringBuilder.append(LIMIT_PARAM).append("=").append(limit);
        }
        if (recursive != null) {
            urlStringBuilder.append("&").append(RECURSIVE_PARAM).append("=").append(recursive);
        }
        if (StringUtils.isNotBlank(filter)) {
            try {
                urlStringBuilder.append("&").append(FILTER_PARAM).append("=")
                        .append(URLEncoder.encode(filter, StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                        Constants.ErrorMessage.ERROR_CODE_BUILDING_LINKS, e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(excludedAttributes)) {
            urlStringBuilder.append("&").append(EXCLUDED_ATTRIBUTES_PARAM).append("=").append(excludedAttributes);
        }
        if (StringUtils.isNotBlank(attributes)) {
            urlStringBuilder.append("&").append(INCLUDED_ATTRIBUTES_PARAM).append("=").append(attributes);
        }
        return urlStringBuilder.toString();
    }

    private static String buildURIForPagination(String paginationQueryParams, String identityProviderId) {

        return buildURIForBody(V1_API_PATH_COMPONENT + IDP_PATH_COMPONENT + PATH_SEPERATOR + identityProviderId
                + IDP_SHARE_PATH_COMPONENT + paginationQueryParams).toString();
    }

    private static URI buildOrganizationURL(String organizationId) {

        return buildURIForBody(PATH_SEPERATOR + OrganizationManagementConstants.V1_API_PATH_COMPONENT
                + PATH_SEPERATOR + ORGANIZATION_PATH + PATH_SEPERATOR + organizationId);
    }

    private ProcessSuccessResponse createProcessSuccessResponse(String details) {

        return new ProcessSuccessResponse()
                .status(ASYNC_OPERATION_RESPONSE_STATUS)
                .details(details);
    }

    private PolicyEnum getPolicyEnum(String policy) {

        if (policy == null) {
            return null;
        }
        if (PolicyEnum.ALL_EXISTING_ORGS_ONLY.getValue().equals(policy)) {
            return PolicyEnum.ALL_EXISTING_ORGS_ONLY;
        } else if (PolicyEnum.ALL_EXISTING_AND_FUTURE_ORGS.getValue().equals(policy)) {
            return PolicyEnum.ALL_EXISTING_AND_FUTURE_ORGS;
        } else if (PolicyEnum.SELECTED_ORG_ONLY.getValue().equals(policy)) {
            return PolicyEnum.SELECTED_ORG_ONLY;
        } else if (PolicyEnum.SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN.getValue().equals(policy)) {
            return PolicyEnum.SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN;
        }
        throw handleException(Response.Status.BAD_REQUEST,
                Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_SHARE_POLICY, policy);
    }

    private APIError handleConnectionSharingException(ConnectionSharingMgtException e,
                                                      Constants.ErrorMessage errorEnum) {

        ErrorResponse errorResponse;
        Response.Status status;
        if (e instanceof ConnectionSharingMgtClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                        Constants.IDP_MANAGEMENT_PREFIX + errorCode;
                errorResponse = getErrorBuilder(errorCode, e.getMessage(), e.getDescription())
                        .build(log, e.getMessage());
            } else {
                errorResponse = getErrorBuilder(errorEnum, null).build(log, e.getMessage());
                errorResponse.setDescription(e.getMessage());
            }
            status = Response.Status.BAD_REQUEST;
        } else {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                        Constants.IDP_MANAGEMENT_PREFIX + errorCode;
                errorResponse = getErrorBuilder(errorCode, e.getMessage(), e.getDescription())
                        .build(log, e, e.getMessage());
            } else {
                errorResponse = getErrorBuilder(errorEnum, null).build(log, e, e.getMessage());
            }
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    private APIError handleException(Response.Status status, Constants.ErrorMessage error, String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg, String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    private ErrorResponse.Builder getErrorBuilder(String errorCode, String errorMsg, String description) {

        return new ErrorResponse.Builder().withCode(errorCode).withMessage(errorMsg)
                .withDescription(description);
    }

    private static String includeData(Constants.ErrorMessage error, String data) {

        if (StringUtils.isNotBlank(data)) {
            return String.format(error.getDescription(), data);
        }
        return String.format(error.getDescription(), "");
    }
}
