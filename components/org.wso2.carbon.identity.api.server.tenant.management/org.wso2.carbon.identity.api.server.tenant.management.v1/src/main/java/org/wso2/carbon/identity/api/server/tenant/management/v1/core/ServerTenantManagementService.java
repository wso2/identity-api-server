package org.wso2.carbon.identity.api.server.tenant.management.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.tenant.management.common.TenantManagementConstants;
import org.wso2.carbon.identity.api.server.tenant.management.common.TenantManagementServiceHolder;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.AdditionalClaims;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.Link;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.OwnerResponse;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantListItem;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantPutModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantResponseModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantsListResponse;
import org.wso2.carbon.stratos.common.exception.TenantManagementClientException;
import org.wso2.carbon.stratos.common.exception.TenantManagementServerException;
import org.wso2.carbon.stratos.common.exception.TenantMgtException;
import org.wso2.carbon.tenant.mgt.services.TenantMgtService;
import org.wso2.carbon.user.core.common.User;
import org.wso2.carbon.user.core.tenant.Tenant;
import org.wso2.carbon.user.core.tenant.TenantSearchResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.tenant.management.common.TenantManagementConstants.TENANT_MANAGEMENT_PATH_COMPONENT;

/**
 * Call internal osgi services to perform server tenant management related operations.
 */
public class ServerTenantManagementService {

    private static final Log log = LogFactory.getLog(ServerTenantManagementService.class);

    /**
     * Add a tenant.
     *
     * @param tenantModel tenantModel.
     * @return TenantResponseModel.
     */
    public String addTenant(TenantModel tenantModel) {

        Tenant tenant;
        TenantMgtService tenantMgtService = TenantManagementServiceHolder.getTenantMgtService();

        tenant = createTenantInfoBean(tenantModel);

        String resourceId;
        try {
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

        handleNotImplementedCapabilities(filter);
        TenantMgtService tenantMgtService = TenantManagementServiceHolder.getTenantMgtService();

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
            Tenant tenant = TenantManagementServiceHolder.getTenantMgtService().getTenant(tenantUniqueID);
            if (tenant == null) {
                throw handleException(Response.Status.NOT_FOUND, TenantManagementConstants.ErrorMessage.
                        ERROR_CODE_TENANT_NOT_FOUND, tenantUniqueID);
            }
            return createTenantResponse(tenant);
        } catch (TenantMgtException e) {
            throw handleTenantManagementException(e, TenantManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_RETRIEVING_TENANT, tenantUniqueID);
        }
    }

    /**
     * Get owners of a tenant which is identified by tenant unique id.
     *
     * @param tenantUniqueID tenant unique identifier.
     * @return OwnerResponses.
     */
    public List<OwnerResponse> getOwners(String tenantUniqueID) {

        try {
            User user = TenantManagementServiceHolder.getTenantMgtService().getOwner(tenantUniqueID);
            if (user == null) {
                throw handleException(Response.Status.NOT_FOUND, TenantManagementConstants.ErrorMessage.
                        ERROR_CODE_OWNER_NOT_FOUND, tenantUniqueID);
            }
            return createOwnerResponse(user);
        } catch (TenantMgtException e) {
            throw handleTenantManagementException(e, TenantManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_RETRIEVING_TENANT, tenantUniqueID);
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
                TenantManagementServiceHolder.getTenantMgtService().activateTenant(tenantUniqueID);
            } else {
                TenantManagementServiceHolder.getTenantMgtService().deactivateTenant(tenantUniqueID);
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

    private TenantResponseModel createTenantResponse(Tenant tenant) {

        TenantResponseModel tenantResponseModel = new TenantResponseModel();
        tenantResponseModel.setCreatedDate(tenant.getCreatedDate().toString());
        tenantResponseModel.setDomain(tenant.getDomain());
        tenantResponseModel.setId(tenant.getTenantUniqueID());
        tenantResponseModel.setLifecycleStatus(tenant.isActive());
        tenantResponseModel.setOwners(getOwnerResponses(tenant));
        return tenantResponseModel;
    }

    private Tenant createTenantInfoBean(TenantModel tenantModel) {

        Tenant tenant = new Tenant();
        tenant.setActive(true);
        tenant.setDomain(tenantModel.getDomain());
        tenant.setAdminName(tenantModel.getOwners().get(0).getUsername());
        tenant.setAdminFirstName(tenantModel.getOwners().get(0).getFirstname());
        tenant.setAdminLastName(tenantModel.getOwners().get(0).getLastname());
        tenant.setEmail(tenantModel.getOwners().get(0).getEmail());
        List<AdditionalClaims> additionalClaimsList = tenantModel.getOwners().get(0).getAdditionalClaims();
        if (CollectionUtils.isNotEmpty(additionalClaimsList)) {
            tenant.setClaimsMap(createClaimsMapping(additionalClaimsList));
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
            listItem.setLifecycleStatus(tenant.isActive());
            listItem.setCreatedDate(tenant.getCreatedDate().toString());
            listItem.setDomain(tenant.getDomain());
            listItem.setId(tenant.getTenantUniqueID());
            listItem.setOwners(getOwnerResponses(tenant));

            tenantListItems.add(listItem);
        }
        return tenantListItems;
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
            errorResponse = getErrorBuilder(errorEnum, data).build(log, errorEnum.getDescription());
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

    /**
     * Return Not Implemented error response for tenant List filtering which are not yet supported by the server.
     *
     * @param filter Filter string.
     */
    private void handleNotImplementedCapabilities(String filter) {

        TenantManagementConstants.ErrorMessage errorEnum = null;

        if (filter != null) {
            errorEnum = TenantManagementConstants.ErrorMessage.ERROR_CODE_FILTER_NOT_IMPLEMENTED;
        }

        if (errorEnum != null) {
            ErrorResponse errorResponse = getErrorBuilder(errorEnum, null).build(log, errorEnum.getDescription());
            Response.Status status = Response.Status.NOT_IMPLEMENTED;
            throw new APIError(status, errorResponse);
        }
    }

}
