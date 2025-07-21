/*
 * Copyright (c) 2024-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationShareAllRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationSharePOSTRequest;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationSharingPatchOperation;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationSharingPatchRequest;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationUnshareAllRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationUnshareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.BasicOrganizationResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.Link;
import org.wso2.carbon.identity.api.server.application.management.v1.OrgShareConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.RoleShareConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.RoleShareConfigAudience;
import org.wso2.carbon.identity.api.server.application.management.v1.RoleSharing;
import org.wso2.carbon.identity.api.server.application.management.v1.SharedApplicationResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.SharedApplicationsResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.SharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.SharingMode;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;
import org.wso2.carbon.identity.organization.management.application.model.RoleWithAudienceDO;
import org.wso2.carbon.identity.organization.management.application.model.SharedApplication;
import org.wso2.carbon.identity.organization.management.application.model.SharedApplicationOrganizationNode;
import org.wso2.carbon.identity.organization.management.application.model.SharedApplicationOrganizationNodePage;
import org.wso2.carbon.identity.organization.management.application.model.SharingModeDO;
import org.wso2.carbon.identity.organization.management.application.model.operation.ApplicationShareRolePolicy;
import org.wso2.carbon.identity.organization.management.application.model.operation.ApplicationShareUpdateOperation;
import org.wso2.carbon.identity.organization.management.application.model.operation.GeneralApplicationShareOperation;
import org.wso2.carbon.identity.organization.management.application.model.operation.SelectiveShareApplicationOperation;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementClientException;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementServerException;
import org.wso2.carbon.identity.organization.resource.sharing.policy.management.constant.PolicyEnum;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.APPLICATION_MANAGEMENT_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.APPLICATION_SHARE_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.APPLICATION_SHARE_TRIGGER_SUCCESS;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.APPLICATION_SHARE_UPDATE_TRIGGER_SUCCESS;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.APPLICATION_UNSHARE_TRIGGER_SUCCESS;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.ASYNC_OPERATION_RESPONSE_STATUS;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.EXCLUDED_ATTRIBUTES_PARAM;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.FILTER_PARAM;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.INCLUDED_ATTRIBUTES_PARAM;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.LIMIT_PARAM;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.NEXT;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.PREVIOUS;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.RECURSIVE_PARAM;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.ROLE_AUDIENCE_DISPLAY_KEY;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.ROLE_AUDIENCE_KEY;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.ROLE_AUDIENCE_TYPE_KEY;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationManagementEndpointConstants.ROLE_DISPLAY_NAME_KEY;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForBody;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ERROR_BUILDING_PAGINATED_RESPONSE_URL;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_INVALID_SHARE_APPLICATION_EMPTY_REQUEST_BODY;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_INVALID_SHARE_APPLICATION_NO_OPERATIONS;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_INVALID_SHARE_APPLICATION_REQUEST_BODY;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_INVALID_SHARE_APP_REQUEST_NO_ORGANIZATIONS;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ROLE_AUDIENCE_DISPLAY_MISSING;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ROLE_AUDIENCE_MISSING;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ROLE_AUDIENCE_TYPE_MISSING;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ROLE_DISPLAY_NAME_MISSING;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ROLE_SHARING_OBJECT_NOT_A_MAP;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ROLE_SHARING_ROLES_MISSING;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ROLE_SHARING_ROLES_NOT_ALLOWED;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_POLICY;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_ROLE_SHARING_MODE;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ORGANIZATION_PATH;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.PAGINATION_AFTER;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.PAGINATION_BEFORE;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.organization.management.service.util.Utils.getOrganizationId;
import static org.wso2.carbon.identity.organization.management.service.util.Utils.handleClientException;

/**
 * Calls internal osgi services to perform server application management related operations.
 */
public class ServerApplicationSharingService {

    private final OrgApplicationManager orgApplicationManager;

    public ServerApplicationSharingService(OrgApplicationManager orgApplicationManager) {

        this.orgApplicationManager = orgApplicationManager;
    }

    /**
     * Returns the shared applications list of a given primary application, along with their organizations.
     *
     * @param applicationId ID of the primary application.
     * @return shared applications list of a given primary application.
     */
    public Response getSharedApplications(String applicationId) {

        try {
            List<SharedApplication> sharedApplications = orgApplicationManager
                    .getSharedApplications(getOrganizationId(), applicationId);
            return Response.ok(createSharedApplicationsResponse(sharedApplications)).build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    /**
     * Stop application sharing to an organization by removing the fragment application from the given organization.
     *
     * @param applicationId        primary application ID.
     * @param sharedOrganizationId ID of the organization owning the fragment application.
     * @return the stop application sharing response.
     */
    public Response deleteSharedApplication(String applicationId, String sharedOrganizationId) {

        try {
            orgApplicationManager.deleteSharedApplication(getOrganizationId(), applicationId,
                    sharedOrganizationId);
            return Response.noContent().build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    /**
     * Returns the shared organizations of a given main application.
     *
     * @param applicationId        ID of the primary application.
     * @param before               Cursor for pagination before.
     * @param after                Cursor for pagination after.
     * @param filter               Filter to apply on the organizations.
     * @param limit                Limit for the number of organizations to return.
     * @param recursive            Whether to return all child organizations recursively.
     * @param excludedAttributes   Attributes to exclude from the response.
     * @param attributes           Attributes to include in the response.
     * @return Shared organizations of a given primary application.
     */
    public Response getApplicationSharedOrganizations(String applicationId, String before, String after, String filter,
                                                      Integer limit, Boolean recursive, String excludedAttributes,
                                                      String attributes) {

        try {
            if (limit == null) {
                limit = 0;
            }
            String organizationId = getOrganizationId();
            int afterCursor = after == null ? 0 : Integer.parseInt(new String(Base64.getDecoder().decode(after),
                    StandardCharsets.UTF_8));
            int beforeCursor = before == null ? 0 : Integer.parseInt(new String(Base64.getDecoder().decode(before),
                    StandardCharsets.UTF_8));
            // To return all organizations if recursive flag was not provided. This is to keep the backward
            // compatibility.
            boolean recursiveFlag = recursive == null || recursive;

            SharedApplicationOrganizationNodePage sharedApplicationOrganizationNodePage = orgApplicationManager
                    .getApplicationSharedOrganizations(organizationId, applicationId, filter, beforeCursor, afterCursor,
                            excludedAttributes, attributes, limit, recursiveFlag);

            String url = buildPaginationQueryParams(filter, limit, recursive, excludedAttributes, attributes);

            return Response.ok(createSharedOrgResponse(sharedApplicationOrganizationNodePage, url, applicationId))
                    .build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    /**
     * Share an application to child organizations.
     *
     * @param applicationId Application identifier.
     * @param requestBody   Request body of the share request.
     * @return The status of the operation.
     */
    public Response shareOrganizationApplication(String applicationId,
                                                 ApplicationSharePOSTRequest requestBody) {

        try {
            validateApplicationSharePostRequestBody(requestBody);
            boolean shareWithAllChildren = (requestBody.getShareWithAllChildren() != null)
                    ? requestBody.getShareWithAllChildren() : false;
            orgApplicationManager.shareOrganizationApplication(getOrganizationId(), applicationId,
                    shareWithAllChildren, requestBody.getSharedOrganizations());
            return Response.ok().build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    /**
     * Share an application with selected organizations.
     *
     * @param applicationShareSelectedRequestBody Request body of the share request.
     * @return The status of the operation.
     */
    public Response shareOrganizationApplication(ApplicationShareSelectedRequestBody
                                                         applicationShareSelectedRequestBody) {

        String applicationId = applicationShareSelectedRequestBody.getApplicationId();
        String ownerOrganizationId = getOrganizationId();
        List<OrgShareConfig> organizations = applicationShareSelectedRequestBody.getOrganizations();
        if (CollectionUtils.isEmpty(organizations)) {
            throw Utils.buildClientError(ERROR_CODE_INVALID_SHARE_APP_REQUEST_NO_ORGANIZATIONS.getCode(),
                    ERROR_CODE_INVALID_SHARE_APP_REQUEST_NO_ORGANIZATIONS.getMessage(),
                    ERROR_CODE_INVALID_SHARE_APP_REQUEST_NO_ORGANIZATIONS.getDescription());
        }
        List<SelectiveShareApplicationOperation> selectiveApplicationShareList = new ArrayList<>();
        for (OrgShareConfig orgShareConfig : organizations) {
            ApplicationShareRolePolicy roleSharingPolicy = getRoleSharingPolicy(orgShareConfig.getRoleSharing());
            PolicyEnum policyEnum = getPolicyEnum(orgShareConfig.getPolicy().value());
            String organizationId = orgShareConfig.getOrgId();
            SelectiveShareApplicationOperation selectiveApplicationShare = new SelectiveShareApplicationOperation(
                    organizationId, policyEnum,
                    roleSharingPolicy);
            selectiveApplicationShareList.add(selectiveApplicationShare);
        }
        try {
            orgApplicationManager.shareApplicationWithSelectedOrganizations(ownerOrganizationId, applicationId,
                    selectiveApplicationShareList);
            return Response.accepted()
                    .entity(createProcessSuccessResponse(ASYNC_OPERATION_RESPONSE_STATUS,
                            APPLICATION_SHARE_TRIGGER_SUCCESS)).build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    /**
     * Share an application to all organizations.
     *
     * @param requestBody Request body of the share request.
     * @return The status of the operation.
     */
    public Response shareApplicationToAllOrganizations(ApplicationShareAllRequestBody requestBody) {

        try {
            if (requestBody == null) {
                throw handleClientException(ERROR_CODE_INVALID_SHARE_APPLICATION_EMPTY_REQUEST_BODY);
            }
            if (requestBody.getPolicy() == null) {
                throw handleClientException(ERROR_CODE_INVALID_SHARE_APPLICATION_REQUEST_BODY);
            }
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        }
        String applicationId = requestBody.getApplicationId();
        String organizationId = getOrganizationId();
        PolicyEnum policyEnum = getPolicyEnum(requestBody.getPolicy().value());
        if (!(PolicyEnum.ALL_EXISTING_ORGS_ONLY.ordinal() == policyEnum.ordinal() ||
                PolicyEnum.ALL_EXISTING_AND_FUTURE_ORGS.ordinal() == policyEnum.ordinal())) {
            throw Utils.buildClientError(ERROR_CODE_UNSUPPORTED_POLICY.getCode(),
                    ERROR_CODE_UNSUPPORTED_POLICY.getMessage(),
                    String.format(ERROR_CODE_UNSUPPORTED_POLICY.getDescription(), policyEnum));
        }
        ApplicationShareRolePolicy roleSharePolicy = getRoleSharingPolicy(requestBody.getRoleSharing());
        GeneralApplicationShareOperation generalApplicationShare = new GeneralApplicationShareOperation(policyEnum,
                roleSharePolicy);
        try {
            orgApplicationManager.shareApplicationWithAllOrganizations(organizationId, applicationId,
                    generalApplicationShare);
            return Response.accepted()
                    .entity(createProcessSuccessResponse(ASYNC_OPERATION_RESPONSE_STATUS,
                            APPLICATION_SHARE_TRIGGER_SUCCESS)).build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    /**
     * Update the shared application with the given operations.
     *
     * @param applicationSharingPatchRequest Request body of the patch request.
     * @return The status of the operation.
     */
    public Response updateSharedApplication(
            ApplicationSharingPatchRequest applicationSharingPatchRequest) {

        String organizationId = getOrganizationId();
        String applicationId = applicationSharingPatchRequest.getApplicationId();
        List<ApplicationSharingPatchOperation> operations = applicationSharingPatchRequest.getOperations();
        if (CollectionUtils.isEmpty(operations)) {
            throw Utils.buildClientError(ERROR_CODE_INVALID_SHARE_APPLICATION_NO_OPERATIONS.getCode(),
                    ERROR_CODE_INVALID_SHARE_APPLICATION_NO_OPERATIONS.getMessage(),
                    ERROR_CODE_INVALID_SHARE_APPLICATION_NO_OPERATIONS.getDescription());
        }
        List<ApplicationShareUpdateOperation> updateOperationList = new ArrayList<>();
        for (ApplicationSharingPatchOperation operation : operations) {
            String path = operation.getPath();
            String op = operation.getOp();
            List<RoleWithAudienceDO> roleSharingDOFromUpdateObjectList = getRoleSharingDOFromUpdateObjectList(
                    operation.getValue());
            ApplicationShareUpdateOperation applicationShareUpdateOperation =
                    new ApplicationShareUpdateOperation(ApplicationShareUpdateOperation.Operation.fromValue(op),
                            path, roleSharingDOFromUpdateObjectList);
            updateOperationList.add(applicationShareUpdateOperation);
        }
        try {
            orgApplicationManager.updateSharedApplication(organizationId, applicationId, updateOperationList);
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
        return Response.accepted()
                .entity(createProcessSuccessResponse(ASYNC_OPERATION_RESPONSE_STATUS,
                        APPLICATION_SHARE_UPDATE_TRIGGER_SUCCESS)).build();
    }

    /**
     * Un share an application from selected organizations.
     *
     * @param applicationUnshareSelectedRequestBody Request body of the unshare request.
     * @return The status of the operation.
     */
    public Response unshareApplicationFromSelectedOrganizations(ApplicationUnshareSelectedRequestBody
                                                                        applicationUnshareSelectedRequestBody) {

        String applicationId = applicationUnshareSelectedRequestBody.getApplicationId();
        String organizationId = getOrganizationId();
        List<String> orgsToUnshare = applicationUnshareSelectedRequestBody.getOrgIds();
        try {
            orgApplicationManager.unshareApplicationFromSelectedOrganizations(organizationId, applicationId,
                    orgsToUnshare);
            return Response.accepted()
                    .entity(createProcessSuccessResponse(ASYNC_OPERATION_RESPONSE_STATUS,
                            APPLICATION_UNSHARE_TRIGGER_SUCCESS)).build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    /**
     * Un share an application from all organizations.
     *
     * @param applicationUnshareAllRequestBody Request body of the unshare request.
     * @return The status of the operation.
     */
    public Response unshareApplicationFromAllOrganizations(ApplicationUnshareAllRequestBody
                                                                   applicationUnshareAllRequestBody) {

        String applicationId = applicationUnshareAllRequestBody.getApplicationId();
        String organizationId = getOrganizationId();
        try {
            orgApplicationManager.unshareAllApplicationFromAllOrganizations(organizationId, applicationId);
            return Response.accepted()
                    .entity(createProcessSuccessResponse(ASYNC_OPERATION_RESPONSE_STATUS,
                            APPLICATION_UNSHARE_TRIGGER_SUCCESS)).build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    /**
     * Stop application sharing to all organizations by removing the fragment applications from the given organization.
     *
     * @param applicationId Main application ID.
     * @return The application unsharing response.
     */
    public Response deleteAllSharedApplications(String applicationId) {

        try {
            orgApplicationManager.deleteSharedApplication(getOrganizationId(), applicationId, null);
            return Response.noContent().build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    private ProcessSuccessResponse createProcessSuccessResponse(String status, String details) {

        return new ProcessSuccessResponse()
                .status(status)
                .details(details);
    }

    private SharedApplicationsResponse createSharedApplicationsResponse(List<SharedApplication> sharedApplications)
            throws OrganizationManagementServerException {

        SharedApplicationsResponse response = new SharedApplicationsResponse();
        for (SharedApplication sharedApp : sharedApplications) {
            SharedApplicationResponse sharedApplicationResponse =
                    new SharedApplicationResponse().applicationId(sharedApp.getSharedApplicationId())
                            .organizationId(sharedApp.getOrganizationId());
            response.addSharedApplicationsItem(sharedApplicationResponse);
        }
        return response;
    }

    private SharedOrganizationsResponse createSharedOrgResponse(
            SharedApplicationOrganizationNodePage sharedApplicationOrganizationNodePage, String url,
            String applicationId)
            throws OrganizationManagementServerException {

        SharedOrganizationsResponse response = new SharedOrganizationsResponse();
        List<SharedApplicationOrganizationNode> sharedApplicationOrganizationNodes =
                sharedApplicationOrganizationNodePage.getSharedApplicationOrganizationNodes();
        for (SharedApplicationOrganizationNode organizationNode : sharedApplicationOrganizationNodes) {
            BasicOrganizationResponse basicOrganizationResponse = new BasicOrganizationResponse()
                    .id(organizationNode.getOrganizationId())
                    .name(organizationNode.getOrganizationName())
                    .parentId(organizationNode.getParentOrganizationId())
                    .depthFromRoot(organizationNode.getDepthFromRoot())
                    .orgHandle(organizationNode.getOrganizationHandle())
                    .ref(buildOrganizationURL(organizationNode.getOrganizationId()).toString())
                    .hasChildren(organizationNode.hasChildren());
            List<RoleWithAudienceDO> roleWithAudienceDOList = organizationNode.getRoleWithAudienceDOList();
            basicOrganizationResponse.roles(convertRoleSharingConfigListToResponseType(roleWithAudienceDOList));
            basicOrganizationResponse.setSharingMode(
                    convertSharingModeToResponseType(organizationNode.getSharingModeDO()));
            response.addOrganizationsItem(basicOrganizationResponse);
        }
        response.setSharingMode(convertSharingModeToResponseType(
                sharedApplicationOrganizationNodePage.getSharingModeDO()));
        if (sharedApplicationOrganizationNodePage.getNextPageCursor() != 0) {
            Link nextLink = new Link();
            String base64EncodedCursor = Base64.getEncoder().encodeToString(
                    String.valueOf(sharedApplicationOrganizationNodePage.getNextPageCursor())
                            .getBytes(StandardCharsets.UTF_8));
            nextLink.setHref(URI.create(buildURIForPagination(url, applicationId) + "&" + PAGINATION_AFTER + "="
                    + base64EncodedCursor).toString());
            nextLink.setRel(NEXT);
            response.addLinksItem(nextLink);
        }
        if (sharedApplicationOrganizationNodePage.getPreviousPageCursor() != 0) {
            Link previousLink = new Link();
            String base64EncodedCursor = Base64.getEncoder().encodeToString(
                    String.valueOf(sharedApplicationOrganizationNodePage.getPreviousPageCursor())
                            .getBytes(StandardCharsets.UTF_8));
            previousLink.setHref(URI.create(buildURIForPagination(url, applicationId) + "&" + PAGINATION_BEFORE + "="
                    + base64EncodedCursor).toString());
            previousLink.setRel(PREVIOUS);
            response.addLinksItem(previousLink);
        }
        return response;
    }

    private String buildPaginationQueryParams(String filter, int limit, Boolean recursive, String excludedAttributes,
                                              String attributes) throws OrganizationManagementServerException {

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
                throw new OrganizationManagementServerException(
                        ERROR_CODE_ERROR_BUILDING_PAGINATED_RESPONSE_URL.getMessage(),
                        ERROR_CODE_ERROR_BUILDING_PAGINATED_RESPONSE_URL.getDescription(),
                        ERROR_CODE_ERROR_BUILDING_PAGINATED_RESPONSE_URL.getCode(), e);
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

    private void validateApplicationSharePostRequestBody(ApplicationSharePOSTRequest requestBody)
            throws OrganizationManagementClientException {

        if (requestBody == null) {
            throw handleClientException(ERROR_CODE_INVALID_SHARE_APPLICATION_EMPTY_REQUEST_BODY);
        }
        if (requestBody.getShareWithAllChildren() == null && requestBody.getSharedOrganizations() == null) {
            throw handleClientException(ERROR_CODE_INVALID_SHARE_APPLICATION_EMPTY_REQUEST_BODY);
        }
        if (requestBody.getShareWithAllChildren() != null
                && requestBody.getShareWithAllChildren()
                && CollectionUtils.isNotEmpty(requestBody.getSharedOrganizations())) {
            throw handleClientException(ERROR_CODE_INVALID_SHARE_APPLICATION_REQUEST_BODY);
        }
    }

    private static URI buildOrganizationURL(String organizationId) {

        return buildURIForBody(PATH_SEPARATOR + V1_API_PATH_COMPONENT + PATH_SEPARATOR + ORGANIZATION_PATH +
                PATH_SEPARATOR + organizationId);
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
        throw Utils.buildClientError(ERROR_CODE_UNSUPPORTED_POLICY.getCode(),
                ERROR_CODE_UNSUPPORTED_POLICY.getMessage(),
                String.format(ERROR_CODE_UNSUPPORTED_POLICY.getDescription(), policy));
    }

    private List<RoleWithAudienceDO> getRoleSharingConfigList(List<RoleShareConfig> roleShareConfigs) {

        List<RoleWithAudienceDO> roleWithAudienceDOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(roleShareConfigs)) {
            return roleWithAudienceDOList;
        }
        for (RoleShareConfig roleShareConfig : roleShareConfigs) {
            RoleWithAudienceDO.AudienceType audienceType = RoleWithAudienceDO.AudienceType.fromValue(
                    roleShareConfig.getAudience().getType());
            RoleWithAudienceDO roleWithAudienceDO = new RoleWithAudienceDO(roleShareConfig.getDisplayName(),
                    roleShareConfig.getAudience().getDisplay(), audienceType);
            roleWithAudienceDOList.add(roleWithAudienceDO);
        }
        return roleWithAudienceDOList;
    }

    private List<RoleShareConfig> convertRoleSharingConfigListToResponseType(
            List<RoleWithAudienceDO> roleWithAudienceDOList) {

        if (roleWithAudienceDOList == null) {
            return null;
        }
        List<RoleShareConfig> roleShareConfigs = new ArrayList<>();
        if (CollectionUtils.isEmpty(roleWithAudienceDOList)) {
            return roleShareConfigs;
        }
        for (RoleWithAudienceDO roleWithAudienceDO : roleWithAudienceDOList) {
            RoleShareConfig roleShareConfig = new RoleShareConfig();
            roleShareConfig.setDisplayName(roleWithAudienceDO.getRoleName());
            RoleShareConfigAudience audience = new RoleShareConfigAudience();
            audience.setDisplay(roleWithAudienceDO.getAudienceName());
            audience.setType(roleWithAudienceDO.getAudienceType().toString());
            roleShareConfig.setAudience(audience);
            roleShareConfigs.add(roleShareConfig);
        }
        return roleShareConfigs;
    }

    private SharingMode convertSharingModeToResponseType(SharingModeDO sharingModeDO) {

        if (sharingModeDO == null) {
            return null;
        }
        SharingMode sharingMode = new SharingMode();
        PolicyEnum policy = sharingModeDO.getPolicy();
        if (policy != null) {
            sharingMode.setPolicy(SharingMode.PolicyEnum.fromValue(policy.name()));
        }
        ApplicationShareRolePolicy rolePolicy = sharingModeDO.getApplicationShareRolePolicy();
        RoleSharing roleSharing = new RoleSharing();
        roleSharing.setMode(RoleSharing.ModeEnum.fromValue(rolePolicy.getMode().name()));
        roleSharing.setRoles(convertRoleSharingConfigListToResponseType(rolePolicy.getRoleWithAudienceDOList()));
        sharingMode.setRoleSharing(roleSharing);

        return sharingMode;
    }

    private List<RoleWithAudienceDO> getRoleSharingDOFromUpdateObjectList(List<Object> values) {

        List<RoleWithAudienceDO> roleWithAudienceDOList = new ArrayList<>();
        for (Object value : values) {
            roleWithAudienceDOList.add(getRoleSharingDOFromUpdateObjectValue(value));
        }
        return roleWithAudienceDOList;
    }

    private RoleWithAudienceDO getRoleSharingDOFromUpdateObjectValue(Object value) {

        if (!(value instanceof Map)) {
            throw Utils.buildClientError(
                    ERROR_CODE_ROLE_SHARING_OBJECT_NOT_A_MAP.getCode(),
                    ERROR_CODE_ROLE_SHARING_OBJECT_NOT_A_MAP.getMessage(),
                    ERROR_CODE_ROLE_SHARING_OBJECT_NOT_A_MAP.getDescription());
        }
        Map<?, ?> valueMap = (Map<?, ?>) value;
        Object audienceObj = valueMap.get(ROLE_AUDIENCE_KEY);
        Object displayNameObj = valueMap.get(ROLE_DISPLAY_NAME_KEY);

        if (!(displayNameObj instanceof String)) {
            throw Utils.buildClientError(
                    ERROR_CODE_ROLE_DISPLAY_NAME_MISSING.getCode(),
                    ERROR_CODE_ROLE_DISPLAY_NAME_MISSING.getMessage(),
                    ERROR_CODE_ROLE_DISPLAY_NAME_MISSING.getDescription());
        }
        if (!(audienceObj instanceof Map)) {
            throw Utils.buildClientError(
                    ERROR_CODE_ROLE_AUDIENCE_MISSING.getCode(),
                    ERROR_CODE_ROLE_AUDIENCE_MISSING.getMessage(),
                    ERROR_CODE_ROLE_AUDIENCE_MISSING.getDescription());
        }
        Map<?, ?> audienceMap = (Map<?, ?>) audienceObj;
        Object audienceTypeObj = audienceMap.get(ROLE_AUDIENCE_TYPE_KEY);
        Object audienceDisplayObj = audienceMap.get(ROLE_AUDIENCE_DISPLAY_KEY);

        if (!(audienceTypeObj instanceof String)) {
            throw Utils.buildClientError(
                    ERROR_CODE_ROLE_AUDIENCE_TYPE_MISSING.getCode(),
                    ERROR_CODE_ROLE_AUDIENCE_TYPE_MISSING.getMessage(),
                    ERROR_CODE_ROLE_AUDIENCE_TYPE_MISSING.getDescription());
        }
        if (!(audienceDisplayObj instanceof String)) {
            throw Utils.buildClientError(
                    ERROR_CODE_ROLE_AUDIENCE_DISPLAY_MISSING.getCode(),
                    ERROR_CODE_ROLE_AUDIENCE_DISPLAY_MISSING.getMessage(),
                    ERROR_CODE_ROLE_AUDIENCE_DISPLAY_MISSING.getDescription());
        }
        String displayName = (String) displayNameObj;
        String audienceDisplay = (String) audienceDisplayObj;
        RoleWithAudienceDO.AudienceType audienceType
                = RoleWithAudienceDO.AudienceType.fromValue((String) audienceTypeObj);
        return new RoleWithAudienceDO(displayName, audienceDisplay, audienceType);
    }

    private ApplicationShareRolePolicy.Mode getRoleSharingMode(RoleSharing.ModeEnum mode) {

        if (mode == null) {
            return null;
        }
        if (ApplicationShareRolePolicy.Mode.ALL.name().equals(mode.value())) {
            return ApplicationShareRolePolicy.Mode.ALL;
        } else if (RoleSharing.ModeEnum.SELECTED.name().equals(mode.value())) {
            return ApplicationShareRolePolicy.Mode.SELECTED;
        } else if (RoleSharing.ModeEnum.NONE.name().equals(mode.value())) {
            return ApplicationShareRolePolicy.Mode.NONE;
        } else {
            throw Utils.buildClientError(ERROR_CODE_UNSUPPORTED_ROLE_SHARING_MODE.getCode(),
                    ERROR_CODE_UNSUPPORTED_ROLE_SHARING_MODE.getMessage(),
                    String.format(ERROR_CODE_UNSUPPORTED_ROLE_SHARING_MODE.getDescription(), mode));
        }
    }

    private ApplicationShareRolePolicy getRoleSharingPolicy(RoleSharing roleSharing) {

        if (roleSharing == null) {
            return null;
        }
        ApplicationShareRolePolicy.Mode roleSharingMode = getRoleSharingMode(roleSharing.getMode());
        ApplicationShareRolePolicy.Builder roleSharingBuilder = new ApplicationShareRolePolicy.Builder()
                .mode(roleSharingMode);
        if (ApplicationShareRolePolicy.Mode.SELECTED.ordinal() == roleSharingMode.ordinal()) {
            if (CollectionUtils.isEmpty(roleSharing.getRoles())) {
                throw Utils.buildClientError(ERROR_CODE_ROLE_SHARING_ROLES_MISSING.getCode(),
                        ERROR_CODE_ROLE_SHARING_ROLES_MISSING.getMessage(),
                        ERROR_CODE_ROLE_SHARING_ROLES_MISSING.getDescription());
            }
            roleSharingBuilder.roleWithAudienceDOList(getRoleSharingConfigList(roleSharing.getRoles()));
        } else if (CollectionUtils.isNotEmpty(roleSharing.getRoles())) {
            throw Utils.buildClientError(ERROR_CODE_ROLE_SHARING_ROLES_NOT_ALLOWED.getCode(),
                    ERROR_CODE_ROLE_SHARING_ROLES_NOT_ALLOWED.getMessage(),
                    ERROR_CODE_ROLE_SHARING_ROLES_NOT_ALLOWED.getDescription());
        }
        return roleSharingBuilder.build();
    }

    private static String buildURIForPagination(String paginationURL, String applicationId) {

        return buildURIForBody(Constants.V1_API_PATH_COMPONENT + APPLICATION_MANAGEMENT_PATH_COMPONENT
                + PATH_SEPARATOR + applicationId + APPLICATION_SHARE_PATH_COMPONENT + paginationURL).toString();
    }
}
