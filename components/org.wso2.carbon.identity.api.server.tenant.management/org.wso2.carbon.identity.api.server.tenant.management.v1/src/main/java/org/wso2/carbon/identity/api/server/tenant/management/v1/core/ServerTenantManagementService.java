/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.tenant.management.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.tenant.management.common.TenantManagementConstants;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.AdditionalClaims;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.ChannelVerifiedTenantModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.LifeCycleStatus;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.Link;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.OwnerInfoResponse;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.OwnerPutModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.OwnerResponse;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantListItem;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantPutModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantResponseModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantsListResponse;
import org.wso2.carbon.identity.recovery.IdentityRecoveryException;
import org.wso2.carbon.identity.recovery.model.UserRecoveryData;
import org.wso2.carbon.identity.recovery.store.JDBCRecoveryDataStore;
import org.wso2.carbon.identity.recovery.store.UserRecoveryDataStore;
import org.wso2.carbon.stratos.common.constants.TenantConstants;
import org.wso2.carbon.stratos.common.exception.TenantManagementClientException;
import org.wso2.carbon.stratos.common.exception.TenantManagementServerException;
import org.wso2.carbon.stratos.common.exception.TenantMgtException;
import org.wso2.carbon.stratos.common.util.ClaimsMgtUtil;
import org.wso2.carbon.tenant.mgt.services.TenantMgtService;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.common.User;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.tenant.Tenant;
import org.wso2.carbon.user.core.tenant.TenantSearchResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_RESOURCE_LIMIT_REACHED;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.tenant.management.common.TenantManagementConstants.ErrorMessage.ERROR_CODE_PARTIALLY_CREATED_OR_UPDATED;
import static org.wso2.carbon.identity.api.server.tenant.management.common.TenantManagementConstants.ErrorMessage.ERROR_CODE_TENANT_LIMIT_REACHED;
import static org.wso2.carbon.identity.api.server.tenant.management.common.TenantManagementConstants.TENANT_MANAGEMENT_PATH_COMPONENT;
import static org.wso2.carbon.stratos.common.constants.TenantConstants.ErrorMessage.ERROR_CODE_INVALID_EMAIL;
import static org.wso2.carbon.stratos.common.constants.TenantConstants.ErrorMessage.ERROR_CODE_MISSING_REQUIRED_PARAMETER;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

/**
 * Call internal osgi services to perform server tenant management related operations.
 */
public class ServerTenantManagementService {

    private final TenantMgtService tenantMgtService;
    private final RealmService realmService;

    private static final Log log = LogFactory.getLog(ServerTenantManagementService.class);
    private static final String VERIFIED_LITE_USER = "verified-lite-user";
    private static final String INLINE_PASSWORD = "inline-password";
    private static final String CODE = "code";
    private static final String PURPOSE = "purpose";

    public ServerTenantManagementService(TenantMgtService tenantMgtService, RealmService realmService) {

        this.tenantMgtService = tenantMgtService;
        this.realmService = realmService;
    }

    /**
     * Add a tenant.
     *
     * @param tenantModel tenantModel.
     * @return TenantResponseModel.
     */
    public String addTenant(TenantModel tenantModel) {

        String resourceId;
        try {
            Tenant tenant = createTenantInfoBean(tenantModel);
            resourceId = tenantMgtService.addTenant(tenant);
        } catch (TenantMgtException e) {
            throw handleTenantManagementException(e, TenantManagementConstants.ErrorMessage
                    .ERROR_CODE_ERROR_ADDING_TENANT, null);
        }
        return resourceId;
    }

    /**
     * Get tenant list.
     *
     * @param limit     Items per page.
     * @param offset    Offset.
     * @param filter    Filter string. E.g. filter="domainName" sw "wso2.com"
     * @param sortBy    Attribute to sort the tenants by. E.g. domainName
     * @param sortOrder Order in which tenants should be sorted. Can be either ASC or DESC.
     * @return TenantsListResponse.
     */
    public TenantsListResponse listTenants(Integer limit, Integer offset, String sortOrder, String sortBy,
                                           String filter) {

        try {
            TenantSearchResult tenantSearchResult = tenantMgtService.listTenants(limit, offset, sortOrder, sortBy,
                    filter);
            return createTenantListResponse(tenantSearchResult);
        } catch (TenantMgtException e) {
            throw handleTenantManagementException(e, TenantManagementConstants.ErrorMessage
                    .ERROR_CODE_ERROR_LISTING_TENANTS, null);
        }
    }

    /**
     * Get a tenant identified by tenant unique id.
     *
     * @param tenantUniqueID tenant unique identifier.
     * @return TenantResponseModel.
     */
    public TenantResponseModel getTenant(String tenantUniqueID) {

        try {
            Tenant tenant = tenantMgtService.getTenant(tenantUniqueID);
            return createTenantResponse(tenant);
        } catch (TenantMgtException e) {
            throw handleTenantManagementException(e, TenantManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_RETRIEVING_TENANT, tenantUniqueID);
        }
    }

    /**
     * Get a tenant identified by tenant unique id.
     *
     * @param domain tenant domain.
     * @return TenantResponseModel.
     */
    public TenantResponseModel getTenantByDomain(String domain) {

        try {
            Tenant tenant = tenantMgtService.getTenantByDomain(domain);
            return createTenantResponse(tenant);
        } catch (TenantMgtException e) {
            throw handleTenantManagementException(e, TenantManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_RETRIEVING_TENANT, domain);
        }
    }

    /**
     * Get a tenant identified by tenant domain.
     *
     * @param tenantDomain tenant unique identifier.
     * @return taken or not.
     */
    public boolean isDomainAvailable(String tenantDomain) {

        try {
            return tenantMgtService.isDomainAvailable(tenantDomain);
        } catch (TenantMgtException e) {
            throw handleTenantManagementException(e, TenantManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_RETRIEVING_TENANT, tenantDomain);
        }
    }

    /**
     * Get owners of a tenant which is identified by tenant unique id.
     *
     * @param tenantUniqueID tenant unique identifier.
     * @return List<OwnerResponse>.
     */
    public List<OwnerResponse> getOwners(String tenantUniqueID) {

        try {
            User user = tenantMgtService.getOwner(tenantUniqueID);
            return createOwnerResponse(user);
        } catch (TenantMgtException e) {
            throw handleTenantManagementException(e, TenantManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_RETRIEVING_TENANT, tenantUniqueID);
        }
    }

    public OwnerInfoResponse getOwner(String tenantUniqueID, String ownerID, String additionalClaims) {

        try {
            Tenant tenant = tenantMgtService.getTenant(tenantUniqueID);
            validateTenantOwnerId(tenant, ownerID);

            String[] claimsList = StringUtils.split(additionalClaims, ",");
            return createOwnerInfoResponse(tenant, claimsList);
        } catch (TenantMgtException e) {
            throw handleTenantManagementException(e, TenantManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_RETRIEVING_OWNER, tenantUniqueID);
        }
    }

    public void updateOwner(String tenantUniqueID, String ownerID, OwnerPutModel ownerPutModel) {

        try {
            Tenant tenant = tenantMgtService.getTenant(tenantUniqueID);
            validateTenantOwnerId(tenant, ownerID);

            createTenantInfoBean(tenant, ownerPutModel);

            tenantMgtService.updateOwner(tenant);
        } catch (TenantMgtException e) {
            throw handleTenantManagementException(e, TenantManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_UPDATING_OWNER, tenantUniqueID);
        }
    }

    /**
     * Delete the metadata of the tenant which is identified by tenant unique id.
     *
     * @param tenantUniqueID tenant unique identifier.
     */
    public void deleteTenantMetadata(String tenantUniqueID) {

        try {
            tenantMgtService.deleteTenantMetaData(tenantUniqueID);
        } catch (TenantMgtException e) {
            throw handleTenantManagementException(e, TenantManagementConstants.ErrorMessage.
                    ERROR_CODE_DELETE_TENANT_METADATA, tenantUniqueID);
        }
    }

    /**
     * Update tenant life-cycle status.
     *
     * @param tenantUniqueID tenant unique identifier.
     * @param tenantPutModel tenant life-cycle status.
     * @return tenant unique id.
     */
    public String updateTenantStatus(String tenantUniqueID, TenantPutModel tenantPutModel) {

        boolean activated = tenantPutModel.getActivated();
        try {
            if (activated) {
                tenantMgtService.activateTenant(tenantUniqueID);
            } else {
                tenantMgtService.deactivateTenant(tenantUniqueID);
            }
        } catch (TenantMgtException e) {
            throw handleTenantManagementException(e, TenantManagementConstants.ErrorMessage.
                    ERROR_CODE_UPDATE_LIFECYCLE_STATUS, String.valueOf(activated));
        }
        return tenantUniqueID;
    }

    private List<OwnerResponse> createOwnerResponse(User user) {

        List<OwnerResponse> ownerResponseList = new ArrayList<>();
        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setId(user.getUserID());
        ownerResponse.setUsername(user.getUsername());
        ownerResponseList.add(ownerResponse);
        return ownerResponseList;
    }

    private void createTenantInfoBean(Tenant tenant, OwnerPutModel ownerPutModel) {

        if (StringUtils.isNotBlank(ownerPutModel.getFirstname())) {
            tenant.setAdminFirstName(ownerPutModel.getFirstname());
        }
        if (StringUtils.isNotBlank(ownerPutModel.getLastname())) {
            tenant.setAdminLastName(ownerPutModel.getLastname());
        }
        if (StringUtils.isNotBlank(ownerPutModel.getEmail())) {
            tenant.setEmail(ownerPutModel.getEmail());
        }
        tenant.setAdminPassword(ownerPutModel.getPassword());
        List<AdditionalClaims> additionalClaimsList = ownerPutModel.getAdditionalClaims();
        if (CollectionUtils.isNotEmpty(additionalClaimsList)) {
            tenant.setClaimsMap(createClaimsMapping(additionalClaimsList));
        } else {
            // Avoid updating the claims map if the request does not contain any additional claims.
            tenant.setClaimsMap(new HashMap<>());
        }
    }

    private void validateTenantOwnerId(Tenant tenant, String ownerID) {

        if (tenant.getAdminUserId() == null || !tenant.getAdminUserId().equals(ownerID)) {
            throw handleException(Response.Status.BAD_REQUEST, TenantManagementConstants.ErrorMessage.
                    ERROR_CODE_OWNER_NOT_FOUND, tenant.getTenantUniqueID());
        }
    }

    private OwnerInfoResponse createOwnerInfoResponse(Tenant tenant, String[] claimsList) throws TenantMgtException {

        OwnerInfoResponse ownerInfoResponse = new OwnerInfoResponse();
        ownerInfoResponse.setId(tenant.getAdminUserId());
        ownerInfoResponse.setUsername(tenant.getAdminName());
        ownerInfoResponse.setEmail(tenant.getEmail());

        try {
            ownerInfoResponse.setFirstname(ClaimsMgtUtil.getFirstNamefromUserStoreManager(
                    realmService, tenant.getId()));
            ownerInfoResponse.setLastname(ClaimsMgtUtil.getLastNamefromUserStoreManager(
                    realmService, tenant.getId()));
        } catch (UserStoreException e) {
            if (e.getMessage().startsWith(TenantManagementConstants.NON_EXISTING_USER_CODE)) {
                throw handleException(Response.Status.NOT_FOUND, TenantManagementConstants.ErrorMessage.
                        ERROR_CODE_OWNER_NOT_FOUND, tenant.getTenantUniqueID());
            }
            throw new TenantMgtException(e.getMessage());
        }

        if (claimsList != null) {
            for (String claim : claimsList) {
                try {
                    String claimValue = ClaimsMgtUtil.getClaimfromUserStoreManager(realmService, tenant.getId(), claim);
                    if (StringUtils.isNotBlank(claimValue)) {
                        ownerInfoResponse.addAdditionalClaimsItem(
                                new AdditionalClaims().claim(claim).value(claimValue));
                    }
                } catch (org.wso2.carbon.user.core.UserStoreException e) {
                    if (log.isDebugEnabled()) {
                        log.debug("Error while retrieving claim: " + claim + " for tenant: " + tenant.getId(), e);
                    }
                }
            }
        }
        return ownerInfoResponse;
    }

    private TenantResponseModel createTenantResponse(Tenant tenant) {

        TenantResponseModel tenantResponseModel = new TenantResponseModel();
        tenantResponseModel.setCreatedDate(getISOFormatDate(tenant.getCreatedDate()));
        tenantResponseModel.setDomain(tenant.getDomain());
        tenantResponseModel.setId(tenant.getTenantUniqueID());
        tenantResponseModel.setLifecycleStatus(getLifeCycleStatus(tenant.isActive()));
        tenantResponseModel.setOwners(getOwnerResponses(tenant));
        return tenantResponseModel;
    }

    private Tenant createTenantInfoBean(TenantModel tenantModel) throws TenantManagementClientException {

        Tenant tenant = new Tenant();
        tenant.setActive(true);
        tenant.setDomain(tenantModel.getDomain());
        if (tenantModel.getOwners() != null) {
            tenant.setAdminName(tenantModel.getOwners().get(0).getUsername());
            tenant.setAdminFirstName(tenantModel.getOwners().get(0).getFirstname());
            tenant.setAdminLastName(tenantModel.getOwners().get(0).getLastname());
            tenant.setEmail(tenantModel.getOwners().get(0).getEmail());
            String provisioningMethod = tenantModel.getOwners().get(0).getProvisioningMethod();
            if (INLINE_PASSWORD.equalsIgnoreCase(provisioningMethod)) {
                String password = tenantModel.getOwners().get(0).getPassword();
                if (StringUtils.isBlank(password)) {
                    throw new TenantManagementClientException(TenantConstants.ErrorMessage.
                            ERROR_CODE_MISSING_REQUIRED_PARAMETER.getCode(), String.format(TenantConstants.ErrorMessage.
                            ERROR_CODE_MISSING_REQUIRED_PARAMETER.getMessage(), "password"));
                }
                tenant.setAdminPassword(password);
            }
            tenant.setProvisioningMethod(provisioningMethod);

            List<AdditionalClaims> additionalClaimsList = tenantModel.getOwners().get(0).getAdditionalClaims();
            if (CollectionUtils.isNotEmpty(additionalClaimsList)) {
                tenant.setClaimsMap(createClaimsMapping(additionalClaimsList));
            }
        } else {
            throw new TenantManagementClientException(TenantConstants.ErrorMessage.ERROR_CODE_OWNER_REQUIRED);
        }

        return tenant;
    }

    private Map<String, String> createClaimsMapping(List<AdditionalClaims> additionalClaimsList) {

        Map<String, String> claimsMapping = new HashMap<>();
        for (AdditionalClaims additionalClaims : additionalClaimsList) {
            claimsMapping.put(additionalClaims.getClaim(), additionalClaims.getValue());
        }
        return claimsMapping;
    }

    private TenantsListResponse createTenantListResponse(TenantSearchResult tenantSearchResult) {

        List<Tenant> tenants = tenantSearchResult.getTenantList();
        TenantsListResponse listResponse = new TenantsListResponse();
        if (CollectionUtils.isNotEmpty(tenants)) {
            List<TenantListItem> tenantListItems = getTenantListItems(tenants);
            listResponse.setTenants(tenantListItems);
            listResponse.setCount(tenantListItems.size());
        } else {
            listResponse.setCount(0);
        }

        listResponse.setTotalResults(tenantSearchResult.getTotalTenantCount());
        listResponse.setStartIndex(tenantSearchResult.getOffSet() + 1);
        listResponse.setLinks(createLinks(V1_API_PATH_COMPONENT + TENANT_MANAGEMENT_PATH_COMPONENT,
                tenantSearchResult.getLimit(), tenantSearchResult.getOffSet(), tenantSearchResult.getTotalTenantCount(),
                tenantSearchResult.getFilter()));
        return listResponse;
    }

    private List<TenantListItem> getTenantListItems(List<Tenant> tenants) {

        List<TenantListItem> tenantListItems = new ArrayList<>();
        for (Tenant tenant : tenants) {
            TenantListItem listItem = new TenantListItem();
            listItem.setLifecycleStatus(getLifeCycleStatus(tenant.isActive()));
            listItem.setCreatedDate(getISOFormatDate(tenant.getCreatedDate()));
            listItem.setDomain(tenant.getDomain());
            listItem.setId(tenant.getTenantUniqueID());
            listItem.setOwners(getOwnerResponses(tenant));

            tenantListItems.add(listItem);
        }
        return tenantListItems;
    }

    private LifeCycleStatus getLifeCycleStatus(boolean isActive) {

        LifeCycleStatus lifeCycleStatus = new LifeCycleStatus();
        lifeCycleStatus.setActivated(isActive);
        return lifeCycleStatus;
    }

    private List<OwnerResponse> getOwnerResponses(Tenant tenant) {

        List<OwnerResponse> ownerResponseList = new ArrayList<>();
        OwnerResponse ownerResponse = new OwnerResponse();
        ownerResponse.setUsername(tenant.getAdminName());
        ownerResponse.setId(tenant.getAdminUserId());
        ownerResponseList.add(ownerResponse);
        return ownerResponseList;
    }

    private List<Link> createLinks(String url, int limit, int offset, int total, String filter) {

        List<Link> links = new ArrayList<>();

        // Next Link
        if (limit > 0 && offset >= 0 && (offset + limit) < total) {
            links.add(buildPageLink(new StringBuilder(url), TenantManagementConstants.PAGE_LINK_REL_NEXT, (offset +
                    limit), limit, filter));
        }

        // Previous Link
        // Previous link matters only if offset and limit are greater than 0.
        if (offset > 0 && limit > 0) {
            if ((offset - limit) >= 0) { // A previous page of size 'limit' exists
                links.add(buildPageLink(new StringBuilder(url), TenantManagementConstants.PAGE_LINK_REL_PREVIOUS,
                        calculateOffsetForPreviousLink(offset, limit, total), limit, filter));
            } else { // A previous page exists but it's size is less than the specified limit
                links.add(buildPageLink(new StringBuilder(url), TenantManagementConstants.PAGE_LINK_REL_PREVIOUS,
                        0, offset, filter));
            }
        }

        return links;
    }

    private Link buildPageLink(StringBuilder url, String rel, int offset, int limit, String filter) {

        if (StringUtils.isNotBlank(filter)) {
            try {
                url.append(String.format(TenantManagementConstants.PAGINATION_WITH_FILTER_LINK_FORMAT, offset, limit,
                        URLEncoder.encode(filter, StandardCharsets.UTF_8.name())));
            } catch (UnsupportedEncodingException e) {
                throw handleException(Response.Status.INTERNAL_SERVER_ERROR, TenantManagementConstants.
                        ErrorMessage.ERROR_CODE_BUILDING_LINKS, "Unable to url-encode filter: " + filter);
            }
        } else {
            url.append(String.format(TenantManagementConstants.PAGINATION_LINK_FORMAT, offset, limit));
        }
        return new Link().rel(rel).href(ContextLoader.buildURIForBody((url.toString())).toString());
    }

    private int calculateOffsetForPreviousLink(int offset, int limit, int total) {

        int newOffset = (offset - limit);
        if (newOffset < total) {
            return newOffset;
        }

        return calculateOffsetForPreviousLink(newOffset, limit, total);
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param status HTTP Status.
     * @param error  Error Message information.
     * @return APIError.
     */
    private APIError handleException(Response.Status status, TenantManagementConstants.ErrorMessage error,
                                     String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Handle TenantMgtException, extract error code, error description and status code to be sent
     * in the response.
     *
     * @param e         TenantMgtException
     * @param errorEnum Error Message information.
     * @return APIError.
     */
    private APIError handleTenantManagementException(TenantMgtException e,
                                                     TenantManagementConstants.ErrorMessage errorEnum, String data) {

        ErrorResponse errorResponse;

        Response.Status status;

        if (e instanceof TenantManagementClientException) {
            if (ERROR_CODE_RESOURCE_LIMIT_REACHED.equals(e.getErrorCode())) {
                return handleResourceLimitReached();
            }
            if (ERROR_CODE_PARTIALLY_CREATED_OR_UPDATED.getCode().equals(e.getErrorCode())) {
                return handleResourcePartiallyCreated(e);
            }
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e.getMessage());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof TenantManagementServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    private APIError handleResourceLimitReached() {

        ErrorResponse errorResponse = getErrorBuilder(ERROR_CODE_TENANT_LIMIT_REACHED, null)
                .build(log, ERROR_CODE_TENANT_LIMIT_REACHED.getDescription());

        Response.Status status = Response.Status.FORBIDDEN;
        return new APIError(status, errorResponse);
    }

    private APIError handleResourcePartiallyCreated(TenantMgtException e) {

        String errorMessage = e.getCause().getMessage();
        ErrorResponse errorResponse = getErrorBuilder(ERROR_CODE_PARTIALLY_CREATED_OR_UPDATED, errorMessage)
                .build(log, errorMessage);
        return new APIError(Response.Status.PARTIAL_CONTENT, errorResponse);
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(TenantManagementConstants.ErrorMessage errorMsg, String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Constant.ErrorMessage.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private static String includeData(TenantManagementConstants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.getDescription(), data);
        } else {
            message = error.getDescription();
        }
        return message;
    }

    public String addTenant(ChannelVerifiedTenantModel channelVerifiedTenantModel) {
        String resourceId;
        try {
            validateInputAgainstCode(channelVerifiedTenantModel);
            Tenant tenant = createTenantInfoBean(channelVerifiedTenantModel);
            resourceId = tenantMgtService.addTenant(tenant);
        } catch (TenantMgtException e) {
            throw handleTenantManagementException(e, TenantManagementConstants.ErrorMessage
                    .ERROR_CODE_ERROR_ADDING_TENANT, null);
        }
        return resourceId;
    }

    /**
     * Validate details attached to the code sent in email verification with the sent in details.
     * @param tenant tenant
     * @throws TenantManagementClientException error in validating code
     */
    private void validateInputAgainstCode(ChannelVerifiedTenantModel tenant) throws TenantManagementClientException {

        String code = tenant.getCode();
        if (StringUtils.isBlank(code)) {
            throw new TenantManagementClientException(ERROR_CODE_MISSING_REQUIRED_PARAMETER.getCode(),
                    String.format(ERROR_CODE_MISSING_REQUIRED_PARAMETER.getMessage(), CODE));
        }

        UserRecoveryDataStore userRecoveryDataStore = JDBCRecoveryDataStore.getInstance();

        // If the code is validated, the load method will return data. Otherwise method will throw exceptions.
        try {
            UserRecoveryData recoveryData = userRecoveryDataStore.load(code);
            if (recoveryData != null && recoveryData.getUser() != null && tenant.getOwners() != null &&
                    tenant.getOwners().get(0) != null && tenant.getOwners().get(0).getEmail() != null &&
                    tenant.getOwners().get(0).getEmail().equalsIgnoreCase(recoveryData.getUser().getUserName())) {
                userRecoveryDataStore.invalidate(code);
                return;
            } else { // the confirmed email using the code and submitted emails are different.
                userRecoveryDataStore.invalidate(code);
                log.warn("The confirmed email using the code and submitted emails are different.");
                throw new TenantManagementClientException(ERROR_CODE_INVALID_EMAIL.getCode(),
                        String.format(ERROR_CODE_INVALID_EMAIL.getMessage(), CODE));
            }

        } catch (IdentityRecoveryException e) {
            throw handleException(Response.Status.UNAUTHORIZED, TenantManagementConstants.ErrorMessage
                    .ERROR_CODE_ERROR_VALIDATING_TENANT_CODE, null);
        }
    }

    private Tenant createTenantInfoBean(ChannelVerifiedTenantModel channelVerifiedTenantModel)
            throws TenantManagementClientException {

        Tenant tenant = new Tenant();
        Map<String, String> claimsMap = new HashMap<>();

        tenant.setActive(true);
        tenant.setDomain(StringUtils.lowerCase(channelVerifiedTenantModel.getDomain()));
        if (channelVerifiedTenantModel.getOwners() != null && channelVerifiedTenantModel.getOwners().size() > 0
                && channelVerifiedTenantModel.getOwners().get(0) != null) {
            tenant.setAdminName(channelVerifiedTenantModel.getOwners().get(0).getEmail());
            tenant.setAdminFirstName(channelVerifiedTenantModel.getOwners().get(0).getFirstname());
            tenant.setAdminLastName(channelVerifiedTenantModel.getOwners().get(0).getLastname());
            tenant.setEmail(channelVerifiedTenantModel.getOwners().get(0).getEmail());

            tenant.setProvisioningMethod(VERIFIED_LITE_USER);
            String password = channelVerifiedTenantModel.getOwners().get(0).getPassword();
            String code = channelVerifiedTenantModel.getCode();

            if (StringUtils.isBlank(code)) {
                throw new TenantManagementClientException(TenantConstants.ErrorMessage.
                        ERROR_CODE_MISSING_REQUIRED_PARAMETER.getCode(),
                        String.format(TenantConstants.ErrorMessage.
                                ERROR_CODE_MISSING_REQUIRED_PARAMETER.getMessage(), "code"));
            }

            if (channelVerifiedTenantModel.getPurpose() != null) {
                claimsMap.put(PURPOSE, channelVerifiedTenantModel.getPurpose().getName());
                if (!CollectionUtils.isEmpty(channelVerifiedTenantModel.getPurpose().getAttributes())) {
                    channelVerifiedTenantModel.getPurpose().getAttributes()
                            .forEach(attribute ->
                                    claimsMap.put(PURPOSE + "_" + attribute.getKey(), attribute.getValue()));
                }
            }

            tenant.setClaimsMap(claimsMap);
            tenant.setAdminPassword(password);

            List<AdditionalClaims> additionalClaimsList =
                    channelVerifiedTenantModel.getOwners().get(0).getAdditionalClaims();
            if (CollectionUtils.isNotEmpty(additionalClaimsList)) {
                tenant.setClaimsMap(createClaimsMapping(additionalClaimsList));
            }
        } else {
            throw new TenantManagementClientException(TenantConstants.ErrorMessage.ERROR_CODE_OWNER_REQUIRED);
        }

        return tenant;
    }
    /**
     * Convert {@link Date} instance to ISO-8601 format string.
     *
     * @param date Date instance to be converted.
     * @return ISO-8601 representation of the date.
     */
    private String getISOFormatDate(Date date) {

        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
                                                   .withZoneSameInstant(ZoneId.of("UTC"));
        return ISO_OFFSET_DATE_TIME.format(zonedDateTime);
    }
}
