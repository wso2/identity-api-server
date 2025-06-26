/*
 * Copyright (c) 2023-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.api.resource.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceManager;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceMgtException;
import org.wso2.carbon.identity.api.resource.mgt.model.APIResourceSearchResult;
import org.wso2.carbon.identity.api.server.api.resource.common.APIResourceManagementServiceHolder;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceListItem;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceListResponse;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourcePatchModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceResponse;
import org.wso2.carbon.identity.api.server.api.resource.v1.PaginationLink;
import org.wso2.carbon.identity.api.server.api.resource.v1.Property;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopeCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopeGetModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopePatchModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants.ErrorMessage;
import org.wso2.carbon.identity.api.server.api.resource.v1.util.APIResourceMgtEndpointUtil;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.application.common.model.APIResource;
import org.wso2.carbon.identity.application.common.model.APIResourceProperty;
import org.wso2.carbon.identity.application.common.model.Scope;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants.ALLOWED_API_RESOURCE_TYPES;
import static org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants.ASC_SORT_ORDER;
import static org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants.DEFAULT_LIMIT;
import static org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants.DESC_SORT_ORDER;
import static org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants.MCP_SERVER_RESOURCE_TYPE;

import static org.wso2.carbon.identity.api.server.api.resource.v1.util.AuthorizationDetailsTypeMgtUtil.toAuthorizationDetailsGetModels;
import static org.wso2.carbon.identity.api.server.api.resource.v1.util.AuthorizationDetailsTypeMgtUtil.toAuthorizationDetailsTypes;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;

/**
 * Server API Resource Management Service.
 */
public class ServerAPIResourceManagementService {

    private final APIResourceManager apiResourceManager;
    private static final Log LOG = LogFactory.getLog(ServerAPIResourceManagementService.class);

    public ServerAPIResourceManagementService(APIResourceManager apiResourceManager) {

        this.apiResourceManager = apiResourceManager;
    }

    /**
     * Add API resource.
     *
     * @param apIResourceCreationModel API resource creation model.
     * @return Response.
     */
    public APIResourceResponse addAPIResourceWithResourceId(APIResourceCreationModel apIResourceCreationModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding API resource with resource id: " + apIResourceCreationModel.getIdentifier());
        }
        try {
            APIResource apiResource = createAPIResource(apIResourceCreationModel);
            APIResource createdAPIResource = apiResourceManager.addAPIResource(apiResource,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (createdAPIResource == null) {
                LOG.error(ErrorMessage.ERROR_CODE_ADD_API_RESOURCE.getDescription());
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.INTERNAL_SERVER_ERROR,
                        ErrorMessage.ERROR_CODE_ADD_API_RESOURCE);
            }
            return buildAPIResourceResponse(createdAPIResource);
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Get API Resources.
     *
     * @param before before parameter for cursor based pagination.
     * @param after  after parameter for cursor based pagination.
     * @param filter filter parameter.
     * @return Response with API Resources list.
     */
    public APIResourceListResponse getAPIResources(String before, String after, String filter, Integer limit,
                                                   String requiredAttributes) {

        APIResourceListResponse apiResourceListResponse = new APIResourceListResponse();

        try {
            // Set default values if the parameters are not set.
            limit = validatedLimit(limit);

            // Validate before and after parameters.
            if (StringUtils.isNotBlank(before) && StringUtils.isNotBlank(after)) {
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_BOTH_BEFORE_AFTER_PROVIDED);
            }

            // Set the pagination sort order.
            String paginationSortOrder = StringUtils.isNotBlank(before) ? DESC_SORT_ORDER : ASC_SORT_ORDER;

            // Validate the required attributes.
            List<String> requestedAttributeList = new ArrayList<>();
            if (StringUtils.isNotEmpty(requiredAttributes)) {
                requestedAttributeList = new ArrayList<>(Arrays.asList(requiredAttributes.split(",")));
                validateRequiredAttributes(requestedAttributeList);
            }

            APIResourceSearchResult apiResourceSearchResult;
            if (CollectionUtils.isNotEmpty(requestedAttributeList)) {
                apiResourceSearchResult = apiResourceManager.getAPIResourcesWithRequiredAttributes(before,
                        after, limit + 1, filter, paginationSortOrder,
                                CarbonContext.getThreadLocalCarbonContext().getTenantDomain(), requestedAttributeList);
            } else {
                apiResourceSearchResult = apiResourceManager.getAPIResources(before,
                        after, limit + 1, filter, paginationSortOrder,
                                CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            }
            List<APIResource> apiResources = apiResourceSearchResult.getAPIResources();

            if (CollectionUtils.isNotEmpty(apiResources)) {
                boolean hasMoreItems = apiResources.size() > limit;
                boolean needsReverse = StringUtils.isNotBlank(before);
                boolean isFirstPage = (StringUtils.isBlank(before) && StringUtils.isBlank(after)) ||
                        (StringUtils.isNotBlank(before) && !hasMoreItems);
                boolean isLastPage = !hasMoreItems && (StringUtils.isNotBlank(after) || StringUtils.isBlank(before));

                String url = "?limit=" + limit;

                if (StringUtils.isNotBlank(filter)) {
                    try {
                        url += "&filter=" + URLEncoder.encode(filter, StandardCharsets.UTF_8.name());
                    } catch (UnsupportedEncodingException e) {
                        LOG.error("Server encountered an error while building pagination URL for the response.", e);
                    }
                }

                if (hasMoreItems) {
                    apiResources.remove(apiResources.size() - 1);
                }
                if (needsReverse) {
                    Collections.reverse(apiResources);
                }
                if (!isFirstPage) {
                    String encodedString = Base64.getEncoder().encodeToString(apiResources.get(0).getCursorKey()
                            .toString().getBytes(StandardCharsets.UTF_8));
                    apiResourceListResponse.addLinksItem(buildPaginationLink(url + "&before=" + encodedString,
                            "previous"));
                }
                if (!isLastPage) {
                    String encodedString = Base64.getEncoder().encodeToString(apiResources.get(apiResources.size() - 1)
                            .getCursorKey().toString().getBytes(StandardCharsets.UTF_8));
                    apiResourceListResponse.addLinksItem(buildPaginationLink(url + "&after=" + encodedString, "next"));
                }
            }
            if (apiResources == null || apiResources.isEmpty()) {
                apiResourceListResponse.setTotalResults(0);
                apiResourceListResponse.setApiResources(new ArrayList<>());
                return apiResourceListResponse;
            }
            apiResourceListResponse.setTotalResults(apiResourceSearchResult.getTotalCount());
            apiResourceListResponse.setApiResources(apiResourceSearchResult.getAPIResources().stream()
                    .map(this::buildAPIResourceListItem).collect(Collectors.toList()));
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
        return apiResourceListResponse;
    }

    /**
     * Get API Resource Response by ID.
     *
     * @param apiResourceId API Resource ID.
     * @return API Resource.
     */
    public APIResourceResponse getAPIResourceResponseById(String apiResourceId) {

        return buildAPIResourceResponse(getAPIResourceById(apiResourceId));
    }

    private APIResource getAPIResourceById(String apiResourceID) {

        try {
            APIResource apiResource = apiResourceManager.getAPIResourceById(apiResourceID,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (apiResource == null) {
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_API_RESOURCE_NOT_FOUND, apiResourceID);
            }
            return apiResource;
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Patch API Resource by ID.
     *
     * @param apiResourceID         API Resource ID.
     * @param apiResourcePatchModel API Resource Patch Model.
     */
    public void patchAPIResourceById(String apiResourceID, APIResourcePatchModel apiResourcePatchModel) {

        try {
            APIResource currentAPIResource = getAPIResourceById(apiResourceID);
            handleSystemAPI(currentAPIResource);
            if (apiResourcePatchModel.getRemovedScopes() != null) {
                LOG.debug("Removed scopes field is not supported in patch operation.");
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_IMPLEMENTED,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_REMOVED_SCOPES_PATCH_NOT_SUPPORTED);
            }

            String displayName = apiResourcePatchModel.getName() == null ? currentAPIResource.getName() :
                    apiResourcePatchModel.getName();
            String description = apiResourcePatchModel.getDescription() == null ? currentAPIResource.getDescription() :
                    apiResourcePatchModel.getDescription();
            List<Scope> addedScopes = createScopes(apiResourcePatchModel.getAddedScopes());
            // Creating an empty list of removed scope names since operation is not supported.
            List<String> removedScopeNames = new ArrayList<>();

            APIResource.APIResourceBuilder apiResourceBuilder = new APIResource.APIResourceBuilder()
                    .name(displayName)
                    .id(apiResourceID)
                    .type(currentAPIResource.getType())
                    .description(description);
            APIResource apiResource = apiResourceBuilder.build();
            apiResourceManager.updateAPIResource(apiResource, addedScopes,
                    removedScopeNames, CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            // Replacing Authorization Details Types
            APIResourceManagementServiceHolder.getAuthorizationDetailsTypeManager()
                    .updateAuthorizationDetailsTypes(apiResourceID,
                            apiResourcePatchModel.getRemovedAuthorizationDetailsTypes(),
                            toAuthorizationDetailsTypes(apiResourcePatchModel.getAddedAuthorizationDetailsTypes()),
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()
                    );
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Delete API Resource by ID.
     *
     * @param apiResourceID API Resource ID.
     */
    public void deleteAPIResource(String apiResourceID) {

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Deleting API Resource with ID: " + apiResourceID);
            }
            String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            APIResource apiResource = apiResourceManager.getAPIResourceById(apiResourceID, tenantDomain);
            if (apiResource == null) {
                return;
            }
            handleSystemAPI(apiResource);
            apiResourceManager.deleteAPIResourceById(apiResourceID, tenantDomain);
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Get scopes by API ID.
     *
     * @param apiResourceId API resource id.
     * @return List of scopes.
     */
    public List<Scope> getScopesByAPIId(String apiResourceId) {

        try {
            APIResource apiResource = apiResourceManager.getAPIResourceById(apiResourceId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (apiResource == null) {
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_API_RESOURCE_NOT_FOUND, apiResourceId);
            }
            return apiResource.getScopes();
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Put scopes by API ID. This replaces existing scopes.
     *
     * @param apiResourceId       API resource id.
     * @param scopeCreationModels Scope creation models.
     */
    public void putScopesByAPIId(String apiResourceId, List<ScopeCreationModel> scopeCreationModels) {

        try {
            APIResource apiResource = apiResourceManager.getAPIResourceById(apiResourceId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (apiResource == null) {
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_API_RESOURCE_NOT_FOUND, apiResourceId);
            }
            handleSystemAPI(apiResource);
            List<Scope> scopes = createScopes(scopeCreationModels);
            apiResourceManager.putScopes(apiResourceId, apiResource.getScopes(),
                    scopes, CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Delete scopes by the scope ID.
     *
     * @param apiResourceId API Resource ID.
     * @param scopeName     Scope Name.
     */
    public void deleteScopeByScopeName(String apiResourceId, String scopeName) {

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Deleting scope with ID: " + scopeName + " of API Resource ID: " + apiResourceId);
            }
            String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            APIResource apiResource = apiResourceManager.getAPIResourceById(apiResourceId, tenantDomain);
            if (apiResource == null) {
                return;
            }
            handleSystemAPI(apiResource);
            apiResourceManager.deleteAPIScopeByScopeName(apiResourceId,
                    scopeName, tenantDomain);
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Patch scope metadata by the scope name.
     *
     * @param apiResourceId     API Resource ID.
     * @param scopeName         Scope Name.
     * @param scopePatchModel   Parameters to be updated.
     */
    public void patchScopeMetadataByScopeName(String apiResourceId, String scopeName, ScopePatchModel scopePatchModel) {

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Updating scope with name: " + scopeName + " of API Resource ID: " + apiResourceId);
            }
            String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();

            APIResource apiResource = apiResourceManager.getAPIResourceById(apiResourceId, tenantDomain);
            if (apiResource == null) {
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_API_RESOURCE_NOT_FOUND, apiResourceId);
            }

            Scope scopeWithMetadata = apiResourceManager.getScopeByName(scopeName, tenantDomain);
            if (scopeWithMetadata == null) {
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_SCOPE_NAME);
            }
            String displayName = StringUtils.isBlank(scopePatchModel.getDisplayName()) ?
                    scopeWithMetadata.getDisplayName() : scopePatchModel.getDisplayName();
            String description = scopePatchModel.getDescription() == null ? scopeWithMetadata.getDescription() :
                    scopePatchModel.getDescription();

            handleSystemAPI(apiResource);
            Scope.ScopeBuilder scopeBuilder = new Scope.ScopeBuilder()
                    .id(scopeWithMetadata.getId())
                    .name(scopeName)
                    .displayName(displayName)
                    .description(description)
                    .apiID(scopeWithMetadata.getApiID())
                    .orgID(scopeWithMetadata.getOrgID());
            Scope scopeForUpdate = scopeBuilder.build();
            apiResourceManager.updateScopeMetadata(scopeForUpdate, apiResource, tenantDomain);
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Search Scopes registered in the tenant.
     *
     * @param filter Filter query.
     * @return List of scopes.
     */
    public List<Scope> getScopesByTenant(String filter) {

        try {
            return apiResourceManager.getScopesByTenantDomain(CarbonContext.getThreadLocalCarbonContext()
                    .getTenantDomain(), filter);
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Build APIResourceResponse from APIResource.
     *
     * @param apiResource APIResource object.
     * @return APIResourceResponse object.
     */
    private APIResourceResponse buildAPIResourceResponse(APIResource apiResource) {

        return new APIResourceResponse()
                .id(apiResource.getId())
                .name(apiResource.getName())
                .identifier(apiResource.getIdentifier())
                .description(apiResource.getDescription())
                .type(apiResource.getType())
                .scopes(apiResource.getScopes().stream().map(this::buildScopeGetResponse)
                        .collect(Collectors.toList()))
                .requiresAuthorization(apiResource.isAuthorizationRequired())
                .authorizationDetailsTypes(toAuthorizationDetailsGetModels(apiResource.getAuthorizationDetailsTypes()))
                .properties(apiResource.getProperties().stream().map(this::buildAPIResourceProperty)
                        .collect(Collectors.toList()));
    }

    /**
     * Build Property from APIResourceProperty.
     *
     * @param apiResourceProperty APIResourceProperty object.
     * @return Property object.
     */
    private Property buildAPIResourceProperty(APIResourceProperty apiResourceProperty) {

        return new Property()
                .name(apiResourceProperty.getName())
                .value(apiResourceProperty.getValue());
    }

    /**
     * Build ScopeGetModel from Scope.
     *
     * @param scope Scope object.
     * @return ScopeGetModel object.
     */
    private ScopeGetModel buildScopeGetResponse(Scope scope) {

        return new ScopeGetModel()
                .id(scope.getId())
                .name(scope.getName())
                .displayName(scope.getDisplayName())
                .description(scope.getDescription());
    }

    /**
     * Create API resource from the API resource creation model.
     *
     * @param apIResourceCreationModel API resource creation model.
     * @return API resource.
     */
    private APIResource createAPIResource(APIResourceCreationModel apIResourceCreationModel)
            throws APIResourceMgtException {

        APIResourceMgtEndpointUtil.validateAPIResource(apIResourceCreationModel);
        APIResource.APIResourceBuilder apiResourceBuilder = new APIResource.APIResourceBuilder()
                .name(apIResourceCreationModel.getName())
                .identifier(apIResourceCreationModel.getIdentifier())
                .description(apIResourceCreationModel.getDescription())
                .scopes(createScopes(apIResourceCreationModel.getScopes()))
                .requiresAuthorization(apIResourceCreationModel.getRequiresAuthorization() != null ?
                        apIResourceCreationModel.getRequiresAuthorization() : true)
                .authorizationDetailsTypes(
                        toAuthorizationDetailsTypes(apIResourceCreationModel.getAuthorizationDetailsTypes()));

        if (apIResourceCreationModel.getResourceType() != null &&
                ALLOWED_API_RESOURCE_TYPES.contains(apIResourceCreationModel.getResourceType())
        ) {
            apiResourceBuilder.type(apIResourceCreationModel.getResourceType());
        } else {
            apiResourceBuilder.type(APIResourceMgtEndpointConstants.BUSINESS_API_RESOURCE_TYPE);
        }

        return apiResourceBuilder.build();
    }

    /**
     * Create scopes from the scope creation models.
     *
     * @param scopeCreationModels Scope creation models.
     * @return List of scopes.
     */
    private List<Scope> createScopes(List<ScopeCreationModel> scopeCreationModels)
            throws APIResourceMgtException {

        APIResourceMgtEndpointUtil.validateScopes(scopeCreationModels);
        List<Scope> scopes = new ArrayList<>();
        if (scopeCreationModels == null) {
            return scopes;
        }
        for (ScopeCreationModel scopeCreationModel : scopeCreationModels) {
            Scope.ScopeBuilder scopeBuilder = new Scope.ScopeBuilder()
                    .name(scopeCreationModel.getName())
                    .displayName(scopeCreationModel.getDisplayName() != null ? scopeCreationModel.getDisplayName() :
                            scopeCreationModel.getName())
                    .description(scopeCreationModel.getDescription());
            scopes.add(scopeBuilder.build());
        }
        return scopes;
    }

    /**
     * Build API Resource List Item from API Resource.
     *
     * @param apiResource API Resource.
     * @return API Resource List Item.
     */
    private APIResourceListItem buildAPIResourceListItem(APIResource apiResource) {

        List<Property> properties = apiResource.getProperties() != null ?
                apiResource.getProperties().stream().map(this::buildAPIResourceProperty).collect(Collectors.toList()) :
                null;

        return new APIResourceListItem()
                .id(apiResource.getId())
                .name(apiResource.getName())
                .identifier(apiResource.getIdentifier())
                .type(apiResource.getType())
                .requiresAuthorization(apiResource.isAuthorizationRequired())
                .properties(properties)
                .self(ContextLoader.buildURIForBody(V1_API_PATH_COMPONENT +
                                APIResourceMgtEndpointConstants.API_RESOURCE_PATH_COMPONENT + "/" + apiResource.getId())
                        .toString());
    }

    /**
     * Build Pagination Link.
     *
     * @param url URL
     * @param rel Rel
     * @return Pagination Link
     */
    private PaginationLink buildPaginationLink(String url, String rel) {

        return new PaginationLink()
                .href(ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT +
                        APIResourceMgtEndpointConstants.API_RESOURCE_PATH_COMPONENT + url).toString())
                .rel(rel);
    }

    /**
     * Validate limit parameter.
     *
     * @param limit Limit parameter.
     * @return Validated limit.
     * @throws APIError if the limit is invalid.
     */
    private static Integer validatedLimit(Integer limit) throws APIError {

        limit = limit == null ? DEFAULT_LIMIT : limit;
        if (limit <= 0) {
            throw APIResourceMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                    ErrorMessage.ERROR_CODE_INVALID_LIMIT);
        }
        return limit;
    }

    /**
     * Validate required attributes.
     *
     * @param requiredAttributeList Requested attribute list.
     * @throws APIError if the requested attributes are invalid.
     */
    private void validateRequiredAttributes(List<String> requiredAttributeList) throws APIError {

        for (String attribute : requiredAttributeList) {
            if (!(APIResourceMgtEndpointConstants.SUPPORTED_REQUIRED_ATTRIBUTES.contains(attribute))) {
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                        ErrorMessage.ERROR_CODE_INVALID_REQ_ATTRIBUTES);
            }
        }
    }

    /**
     * Validate whether the given API resource is a system API resource and throw an error if it is.
     *
     * @param apiResource API resource to be handled.
     */
    private void handleSystemAPI(APIResource apiResource) {

        if (apiResource.getType() != null &&
                !apiResource.getType().startsWith(APIResourceMgtEndpointConstants.BUSINESS_API_RESOURCE_TYPE)
                && !apiResource.getType().startsWith(MCP_SERVER_RESOURCE_TYPE)) {
            throw APIResourceMgtEndpointUtil.handleException(Response.Status.FORBIDDEN,
                    ErrorMessage.ERROR_CODE_SYSTEM_API_RESOURCE_NOT_MODIFIABLE);
        }
    }
}
