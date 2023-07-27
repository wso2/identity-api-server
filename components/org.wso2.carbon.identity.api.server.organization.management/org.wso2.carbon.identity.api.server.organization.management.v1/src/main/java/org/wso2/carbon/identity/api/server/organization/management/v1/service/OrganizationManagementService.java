/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 LLC. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein in any form is strictly forbidden, unless permitted by WSO2 expressly.
 * You may not alter or remove any copyright or other notice from copies of this content.
 */

package org.wso2.carbon.identity.api.server.organization.management.v1.service;

import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.management.common.OrganizationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.organization.management.v1.exceptions.OrganizationManagementEndpointException;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.ApplicationSharePOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.Attribute;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.BasicOrganizationResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.GetOrganizationResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.Link;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationNameCheckPOSTResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPUTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPatchRequestItem;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.ParentOrganization;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.SharedApplicationResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.SharedApplicationsResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.SharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.util.OrganizationManagementEndpointUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;
import org.wso2.carbon.identity.organization.management.application.model.SharedApplication;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;
import org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementClientException;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementServerException;
import org.wso2.carbon.identity.organization.management.service.model.BasicOrganization;
import org.wso2.carbon.identity.organization.management.service.model.Organization;
import org.wso2.carbon.identity.organization.management.service.model.OrganizationAttribute;
import org.wso2.carbon.identity.organization.management.service.model.ParentOrganizationDO;
import org.wso2.carbon.identity.organization.management.service.model.PatchOperation;
import org.wso2.carbon.identity.organization.management.service.model.TenantTypeOrganization;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.organization.management.v1.constants.OrganizationManagementEndpointConstants.ASC_SORT_ORDER;
import static org.wso2.carbon.identity.api.server.organization.management.v1.constants.OrganizationManagementEndpointConstants.DESC_SORT_ORDER;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ERROR_BUILDING_PAGINATED_RESPONSE_URL;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_INVALID_PAGINATION_PARAMETER_NEGATIVE_LIMIT;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_INVALID_SHARE_APPLICATION_EMPTY_REQUEST_BODY;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_INVALID_SHARE_APPLICATION_REQUEST_BODY;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.SUPER;
import static org.wso2.carbon.identity.organization.management.service.util.Utils.buildURIForBody;
import static org.wso2.carbon.identity.organization.management.service.util.Utils.generateUniqueID;
import static org.wso2.carbon.identity.organization.management.service.util.Utils.handleClientException;

/**
 * Perform organization management related operations.
 */
public class OrganizationManagementService {

    private static final Log LOG = LogFactory.getLog(OrganizationManagementService.class);

    /**
     * Retrieve organization IDs.
     *
     * @param filter    The filter string.
     * @param limit     The maximum number of records to be returned.
     * @param after     The pointer to next page.
     * @param before    The pointer to previous page.
     * @param recursive Determines whether recursive search is required.
     * @return The list of organization IDs.
     */
    public Response getOrganizations(String filter, Integer limit, String after, String before, Boolean recursive) {

        return getOrganizationList(false, filter, limit, after, before, recursive);
    }

    /**
     * Retrieve organization IDs for authorized user.
     *
     * @param filter    The filter string.
     * @param limit     The maximum number of records to be returned.
     * @param after     The pointer to next page.
     * @param before    The pointer to previous page.
     * @param recursive Determines whether recursive search is required.
     * @return The list of organization IDs.
     */
    public Response getAuthorizedOrganizations(String filter, Integer limit, String after, String before,
                                               Boolean recursive) {

        return getOrganizationList(true, filter, limit, after, before, recursive);
    }

    private Response getOrganizationList(boolean authorizedSubOrgsOnly, String filter, Integer limit, String after,
                                         String before, Boolean recursive) {

        try {
            limit = validateLimit(limit);
            String sortOrder = StringUtils.isNotBlank(before) ? ASC_SORT_ORDER : DESC_SORT_ORDER;
            List<BasicOrganization> organizations = authorizedSubOrgsOnly ?
                    getOrganizationManager().getUserAuthorizedOrganizations(
                            limit + 1, after, before, sortOrder, filter, Boolean.TRUE.equals(recursive))
                    : getOrganizationManager().getOrganizations(
                    limit + 1, after, before, sortOrder, filter, Boolean.TRUE.equals(recursive));
            return Response.ok().entity(getOrganizationsResponse(limit, after, before, filter, organizations,
                    Boolean.TRUE.equals(recursive))).build();
        } catch (OrganizationManagementClientException e) {
            return OrganizationManagementEndpointUtil.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return OrganizationManagementEndpointUtil.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Check if organization exist for given name.
     *
     * @param organizationName Organization name.
     * @return Organization name check response.
     */
    public Response checkOrganizationName(String organizationName) {

        boolean nameExist = getOrganizationManager().isOrganizationExistByNameInGivenHierarchy(organizationName);
        OrganizationNameCheckPOSTResponse response = new OrganizationNameCheckPOSTResponse().available(false);
        if (!nameExist) {
            response.setAvailable(true);
        }
        return Response.ok().entity(response).build();
    }

    /**
     * Delete an organization.
     *
     * @param organizationId Unique identifier for the requested organization to be deleted.
     * @return Organization deletion response.
     */
    public Response deleteOrganization(String organizationId) {

        try {
            getOrganizationManager().deleteOrganization(organizationId);
            return Response.noContent().build();
        } catch (OrganizationManagementClientException e) {
            return OrganizationManagementEndpointUtil.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return OrganizationManagementEndpointUtil.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Fetch an organization.
     *
     * @param organizationId Unique identifier for the requested organization to be fetched.
     * @return Requested organization details.
     */
    public Response getOrganization(String organizationId, Boolean includePermissions) {

        try {
            Organization organization = getOrganizationManager().getOrganization(organizationId,
                    false, Boolean.TRUE.equals(includePermissions));
            return Response.ok().entity(getOrganizationResponseWithPermission(organization)).build();
        } catch (OrganizationManagementClientException e) {
            return OrganizationManagementEndpointUtil.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return OrganizationManagementEndpointUtil.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Patch an organization.
     *
     * @param organizationId               Unique identifier for the requested organization to be patched.
     * @param organizationPatchRequestItem The list of organization details to be patched.
     * @return The patched organization.
     */
    public Response patchOrganization(String organizationId, List<OrganizationPatchRequestItem>
            organizationPatchRequestItem) {

        try {
            Organization organization = getOrganizationManager().patchOrganization(organizationId,
                    organizationPatchRequestItem.stream().map(op ->
                            new PatchOperation(op.getOperation() == null ? null : op.getOperation().toString(),
                                    op.getPath(), op.getValue())).collect(Collectors.toList()));
            return Response.ok().entity(getOrganizationResponse(organization)).build();
        } catch (OrganizationManagementClientException e) {
            return OrganizationManagementEndpointUtil.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return OrganizationManagementEndpointUtil.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Update an organization.
     *
     * @param organizationId         Unique identifier for the requested organization to be updated.
     * @param organizationPUTRequest The organization update request.
     * @return The updated organization.
     */
    public Response updateOrganization(String organizationId, OrganizationPUTRequest organizationPUTRequest) {

        try {
            Organization updatedOrganization = getUpdatedOrganization(organizationId, organizationPUTRequest);
            return Response.ok().entity(getOrganizationResponse(updatedOrganization)).build();
        } catch (OrganizationManagementClientException e) {
            return OrganizationManagementEndpointUtil.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return OrganizationManagementEndpointUtil.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Add an organization.
     *
     * @param organizationPOSTRequest Add organization request.
     * @return The newly created organization.
     */
    public Response addOrganization(OrganizationPOSTRequest organizationPOSTRequest) {

        try {
            Organization organization = getOrganizationManager().addOrganization(getOrganizationFromPostRequest
                    (organizationPOSTRequest));
            String organizationId = organization.getId();
            return Response.created(OrganizationManagementEndpointUtil.getResourceLocation(organizationId)).entity
                    (getOrganizationResponse(organization)).build();
        } catch (OrganizationManagementClientException e) {
            return OrganizationManagementEndpointUtil.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return OrganizationManagementEndpointUtil.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Share an application to child organizations.
     *
     * @param organizationId Unique identifier of the organization owning the application.
     * @param applicationId  Application identifier.
     * @param requestBody    Request body of the share request.
     * @return The status of the operation.
     */
    public Response shareOrganizationApplication(String organizationId, String applicationId,
                                                 ApplicationSharePOSTRequest requestBody) {

        try {
            validateApplicationSharePostRequestBody(requestBody);
            boolean shareWithAllChildren = (requestBody.getShareWithAllChildren() != null)
                    ? requestBody.getShareWithAllChildren() : false;
            getOrgApplicationManager().shareOrganizationApplication(organizationId, applicationId,
                    shareWithAllChildren, requestBody.getSharedOrganizations());
            return Response.ok().build();
        } catch (OrganizationManagementClientException e) {
            return OrganizationManagementEndpointUtil.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return OrganizationManagementEndpointUtil.handleServerErrorResponse(e, LOG);
        }
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

    /**
     * Stop application sharing to an organization by removing the fragment application from the given organization.
     *
     * @param organizationId       ID of the organization owning the primary application.
     * @param applicationId        primary application ID.
     * @param sharedOrganizationId ID of the organization owning the fragment application.
     * @return the stop application sharing response.
     */
    public Response deleteSharedApplication(String organizationId, String applicationId, String sharedOrganizationId) {

        try {
            getOrgApplicationManager().deleteSharedApplication(organizationId, applicationId, sharedOrganizationId);
            return Response.noContent().build();
        } catch (OrganizationManagementClientException e) {
            return OrganizationManagementEndpointUtil.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return OrganizationManagementEndpointUtil.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Stop application sharing to all organizations by removing the fragment applications from the given organization.
     *
     * @param organizationId ID of the organization owning the main application.
     * @param applicationId  Main application ID.
     * @return The application unsharing response.
     */
    public Response deleteAllSharedApplications(String organizationId, String applicationId) {

        try {
            getOrgApplicationManager().deleteSharedApplication(organizationId, applicationId, null);
            return Response.noContent().build();
        } catch (OrganizationManagementClientException e) {
            return OrganizationManagementEndpointUtil.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return OrganizationManagementEndpointUtil.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Returns the list of organization with whom the primary application is shared.
     *
     * @param organizationId ID of the organization owning the primary application.
     * @param applicationId  ID of the primary application.
     * @return list of organization having the fragment applications.
     */
    public Response getApplicationSharedOrganizations(String organizationId, String applicationId) {

        try {
            List<BasicOrganization> basicOrganizations =
                    getOrgApplicationManager().getApplicationSharedOrganizations(organizationId, applicationId);
            return Response.ok(createSharedOrgResponse(basicOrganizations)).build();
        } catch (OrganizationManagementClientException e) {
            return OrganizationManagementEndpointUtil.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return OrganizationManagementEndpointUtil.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Returns the shared applications list of a given primary application, along with their organizations.
     *
     * @param organizationId ID of the organization owning the primary application.
     * @param applicationId  ID of the primary application.
     * @return shared applications list of a given primary application.
     */
    public Response getSharedApplications(String organizationId, String applicationId) {

        try {
            List<SharedApplication> sharedApplications =
                    getOrgApplicationManager().getSharedApplications(organizationId, applicationId);
            return Response.ok(createSharedApplicationsResponse(sharedApplications)).build();
        } catch (OrganizationManagementClientException e) {
            return OrganizationManagementEndpointUtil.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return OrganizationManagementEndpointUtil.handleServerErrorResponse(e, LOG);
        }
    }

    private Organization getOrganizationFromPostRequest(OrganizationPOSTRequest organizationPOSTRequest) {

        String organizationId = generateUniqueID();
        OrganizationPOSTRequest.TypeEnum type = organizationPOSTRequest.getType();
        Organization organization;
        if (OrganizationPOSTRequest.TypeEnum.TENANT.equals(type)) {
            // Set the organization id as the default domain name of the underlying tenant.
            organization = new TenantTypeOrganization(organizationId);
        } else {
            organization = new Organization();
        }
        organization.setId(organizationId);
        organization.setName(organizationPOSTRequest.getName());
        organization.setDescription(organizationPOSTRequest.getDescription());
        organization.setStatus(OrganizationManagementConstants.OrganizationStatus.ACTIVE.toString());
        organization.setType(type != null ? type.toString() : null);
        String parentId = organizationPOSTRequest.getParentId();
        if (StringUtils.isNotBlank(parentId)) {
            organization.getParent().setId(parentId);
        } else {
            organization.getParent().setId(SUPER);
        }
        List<Attribute> organizationAttributes = organizationPOSTRequest.getAttributes();
        if (CollectionUtils.isNotEmpty(organizationAttributes)) {
            organization.setAttributes(organizationAttributes.stream().map(attribute ->
                    new OrganizationAttribute(attribute.getKey(), attribute.getValue())).collect(Collectors.toList()));
        }
        return organization;
    }

    private OrganizationResponse getOrganizationResponse(Organization organization) {

        OrganizationResponse organizationResponse = new OrganizationResponse();
        organizationResponse.setId(organization.getId());
        organizationResponse.setName(organization.getName());
        organizationResponse.setDescription(organization.getDescription());

        OrganizationResponse.StatusEnum status;
        try {
            status = OrganizationResponse.StatusEnum.valueOf(organization.getStatus());
        } catch (IllegalArgumentException e) {
            status = OrganizationResponse.StatusEnum.DISABLED;
        }
        organizationResponse.setStatus(status);

        organizationResponse.setCreated(organization.getCreated().toString());
        organizationResponse.setLastModified(organization.getLastModified().toString());

        String type = organization.getType();
        if (StringUtils.equals(type, OrganizationResponse.TypeEnum.TENANT.toString())) {
            organizationResponse.setType(OrganizationResponse.TypeEnum.TENANT);
        } else {
            organizationResponse.setType(OrganizationResponse.TypeEnum.STRUCTURAL);
        }

        ParentOrganizationDO parentOrganizationDO = organization.getParent();
        if (parentOrganizationDO != null) {
            organizationResponse.setParent(getParentOrganization(parentOrganizationDO));
        }

        List<Attribute> attributeList = getOrganizationAttributes(organization);
        if (!attributeList.isEmpty()) {
            organizationResponse.setAttributes(attributeList);
        }
        return organizationResponse;
    }

    private GetOrganizationResponse getOrganizationResponseWithPermission(Organization organization) {

        GetOrganizationResponse organizationResponse = new GetOrganizationResponse();
        organizationResponse.setId(organization.getId());
        organizationResponse.setName(organization.getName());
        organizationResponse.setDescription(organization.getDescription());
        organizationResponse.setCreated(organization.getCreated().toString());
        organizationResponse.setLastModified(organization.getLastModified().toString());
        organizationResponse.setPermissions(organization.getPermissions());

        GetOrganizationResponse.StatusEnum status;
        try {
            status = GetOrganizationResponse.StatusEnum.valueOf(organization.getStatus());
        } catch (IllegalArgumentException e) {
            status = GetOrganizationResponse.StatusEnum.DISABLED;
        }
        organizationResponse.setStatus(status);

        String type = organization.getType();
        if (StringUtils.equals(type, GetOrganizationResponse.TypeEnum.TENANT.toString())) {
            organizationResponse.setType(GetOrganizationResponse.TypeEnum.TENANT);
        } else {
            organizationResponse.setType(GetOrganizationResponse.TypeEnum.STRUCTURAL);
        }

        ParentOrganizationDO parentOrganizationDO = organization.getParent();
        if (parentOrganizationDO != null) {
            organizationResponse.setParent(getParentOrganization(parentOrganizationDO));
        }

        List<Attribute> attributeList = getOrganizationAttributes(organization);
        if (!attributeList.isEmpty()) {
            organizationResponse.setAttributes(attributeList);
        }
        return organizationResponse;
    }

    private List<Attribute> getOrganizationAttributes(Organization organization) {

        List<Attribute> attributeList = new ArrayList<>();
        for (OrganizationAttribute attributeModel : organization.getAttributes()) {
            Attribute attribute = new Attribute();
            attribute.setKey(attributeModel.getKey());
            attribute.setValue(attributeModel.getValue());
            attributeList.add(attribute);
        }
        return attributeList;
    }

    private ParentOrganization getParentOrganization(ParentOrganizationDO parentOrganizationDO) {

        ParentOrganization parentOrganization = new ParentOrganization();
        parentOrganization.setId(parentOrganizationDO.getId());
        parentOrganization.setRef(parentOrganizationDO.getRef());
        return parentOrganization;
    }

    private int validateLimit(Integer limit) throws OrganizationManagementClientException {

        if (limit == null) {
            int defaultItemsPerPage = IdentityUtil.getDefaultItemsPerPage();
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Given limit is null. Therefore the default limit is set to %s.",
                        defaultItemsPerPage));
            }
            return defaultItemsPerPage;
        }

        if (limit < 0) {
            throw handleClientException(ERROR_CODE_INVALID_PAGINATION_PARAMETER_NEGATIVE_LIMIT);
        }

        int maximumItemsPerPage = IdentityUtil.getMaximumItemPerPage();
        if (limit > maximumItemsPerPage) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Given limit exceeds the maximum limit. Therefore the limit is set to %s.",
                        maximumItemsPerPage));
            }
            return maximumItemsPerPage;
        }

        return limit;
    }

    private OrganizationsResponse getOrganizationsResponse(Integer limit, String after, String before, String filter,
                                                           List<BasicOrganization> organizations, boolean recursive)
            throws OrganizationManagementServerException {

        OrganizationsResponse organizationsResponse = new OrganizationsResponse();

        if (limit != 0 && CollectionUtils.isNotEmpty(organizations)) {
            boolean hasMoreItems = organizations.size() > limit;
            boolean needsReverse = StringUtils.isNotBlank(before);
            boolean isFirstPage = (StringUtils.isBlank(before) && StringUtils.isBlank(after)) ||
                    (StringUtils.isNotBlank(before) && !hasMoreItems);
            boolean isLastPage = !hasMoreItems && (StringUtils.isNotBlank(after) || StringUtils.isBlank(before));

            String url = "?limit=" + limit + "&recursive=" + recursive;
            if (StringUtils.isNotBlank(filter)) {
                try {
                    url += "&filter=" + URLEncoder.encode(filter, StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    LOG.error("Server encountered an error while building pagination URL for the response.", e);
                    Error error = OrganizationManagementEndpointUtil.getError(
                            ERROR_CODE_ERROR_BUILDING_PAGINATED_RESPONSE_URL.getCode(),
                            ERROR_CODE_ERROR_BUILDING_PAGINATED_RESPONSE_URL.getMessage(),
                            ERROR_CODE_ERROR_BUILDING_PAGINATED_RESPONSE_URL.getDescription());
                    throw new OrganizationManagementEndpointException(Response.Status.INTERNAL_SERVER_ERROR, error);
                }
            }

            if (hasMoreItems) {
                organizations.remove(organizations.size() - 1);
            }
            if (needsReverse) {
                Collections.reverse(organizations);
            }
            if (!isFirstPage) {
                String encodedString = Base64.getEncoder().encodeToString(organizations.get(0).getCreated()
                        .getBytes(StandardCharsets.UTF_8));
                Link link = new Link();
                link.setHref(URI.create(
                        OrganizationManagementEndpointUtil.buildURIForPagination(url) + "&before=" + encodedString));
                link.setRel("previous");
                organizationsResponse.addLinksItem(link);
            }
            if (!isLastPage) {
                String encodedString = Base64.getEncoder().encodeToString(organizations.get(organizations.size() - 1)
                        .getCreated().getBytes(StandardCharsets.UTF_8));
                Link link = new Link();
                link.setHref(URI.create(
                        OrganizationManagementEndpointUtil.buildURIForPagination(url) + "&after=" + encodedString));
                link.setRel("next");
                organizationsResponse.addLinksItem(link);
            }

            List<BasicOrganizationResponse> organizationDTOs = new ArrayList<>();
            for (BasicOrganization organization : organizations) {
                BasicOrganizationResponse organizationDTO = new BasicOrganizationResponse();
                organizationDTO.setId(organization.getId());
                organizationDTO.setName(organization.getName());
                organizationDTO.setStatus(BasicOrganizationResponse.StatusEnum.valueOf(organization.getStatus()));
                organizationDTO.setRef(buildURIForBody(organization.getId()));
                organizationDTOs.add(organizationDTO);
            }
            organizationsResponse.setOrganizations(organizationDTOs);
        }
        return organizationsResponse;
    }

    private Organization getUpdatedOrganization(String organizationId, OrganizationPUTRequest organizationPUTRequest)
            throws OrganizationManagementException {

        Organization oldOrganization = getOrganizationManager().getOrganization(organizationId, false, false);
        String currentOrganizationName = oldOrganization.getName();
        Organization organization = createOrganizationClone(oldOrganization);

        organization.setName(organizationPUTRequest.getName());
        organization.setDescription(organizationPUTRequest.getDescription());

        OrganizationPUTRequest.StatusEnum statusEnum = organizationPUTRequest.getStatus();
        if (statusEnum != null) {
            String organizationStatus = statusEnum.toString();
            if (StringUtils.isNotBlank(organizationStatus)) {
                organization.setStatus(organizationStatus);
            }
        } else {
            organization.setStatus(null);
        }

        List<Attribute> organizationAttributes = organizationPUTRequest.getAttributes();
        if (CollectionUtils.isNotEmpty(organizationAttributes)) {
            organization.setAttributes(organizationAttributes.stream().map(attribute ->
                    new OrganizationAttribute(attribute.getKey(), attribute.getValue())).collect(Collectors.toList()));
        } else {
            organization.setAttributes(null);
        }
        return getOrganizationManager().updateOrganization(organizationId, currentOrganizationName, organization);
    }

    private Organization createOrganizationClone(Organization organization) {

        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(organization), Organization.class);
    }

    private SharedOrganizationsResponse createSharedOrgResponse(List<BasicOrganization> organizations)
            throws OrganizationManagementServerException {

        SharedOrganizationsResponse response = new SharedOrganizationsResponse();
        for (BasicOrganization org : organizations) {
            BasicOrganizationResponse basicOrganizationResponse =
                    new BasicOrganizationResponse().id(org.getId()).name(org.getName())
                            .ref(buildURIForBody(org.getId()));
            response.addOrganizationsItem(basicOrganizationResponse);
        }
        return response;
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

    private OrganizationManager getOrganizationManager() {

        return OrganizationManagementServiceHolder.getInstance().getOrganizationManager();
    }

    private OrgApplicationManager getOrgApplicationManager() {

        return OrganizationManagementServiceHolder.getInstance().getOrgApplicationManager();
    }
}
