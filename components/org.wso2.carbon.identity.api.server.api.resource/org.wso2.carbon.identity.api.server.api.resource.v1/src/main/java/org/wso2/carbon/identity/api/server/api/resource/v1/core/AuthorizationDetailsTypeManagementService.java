package org.wso2.carbon.identity.api.server.api.resource.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceMgtException;
import org.wso2.carbon.identity.api.server.api.resource.common.APIResourceManagementServiceHolder;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesGetModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesPatchModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.api.resource.v1.util.APIResourceMgtEndpointUtil;
import org.wso2.carbon.identity.api.server.api.resource.v1.util.AuthorizationDetailsTypeMgtUtil;
import org.wso2.carbon.identity.application.common.model.APIResource;
import org.wso2.carbon.identity.application.common.model.AuthorizationDetailsType;

import java.util.List;

import javax.ws.rs.core.Response;

public class AuthorizationDetailsTypeManagementService {

    private static final Log LOG = LogFactory.getLog(AuthorizationDetailsTypeManagementService.class);

    public void addAuthorizationDetailsTypes(String apiResourceId,
                                             List<AuthorizationDetailsTypesCreationModel> creationModels) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding authorization details types to resource with id: " + apiResourceId);
        }
        try {
            APIResource apiResource = APIResourceManagementServiceHolder.getApiResourceManager()
                    .getAPIResourceById(apiResourceId, CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (apiResource == null) {
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_API_RESOURCE_NOT_FOUND, apiResourceId);
            }

            APIResourceManagementServiceHolder.getAuthorizationDetailsTypeManager().addAuthorizationDetailsTypes(
                    apiResourceId,
                    AuthorizationDetailsTypeMgtUtil.toAuthorizationDetailsTypesList(creationModels),
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain()
            );
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public void deleteAuthorizationDetailsType(String apiResourceId, String authorizationDetailsType) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Deleting authorization details type against resource id: %s and type: %s ",
                    apiResourceId, authorizationDetailsType));
        }
        try {
            APIResourceManagementServiceHolder.getAuthorizationDetailsTypeManager()
                    .deleteAuthorizationDetailsTypeByApiIdAndType(
                            apiResourceId,
                            authorizationDetailsType,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()
                    );
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public AuthorizationDetailsTypesGetModel getAuthorizationDetailsType(String apiResourceId, String type) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving authorization details type against resource id: %s and type: %s ",
                    apiResourceId, type));
        }
        try {
            AuthorizationDetailsType authorizationDetailsType = APIResourceManagementServiceHolder
                    .getAuthorizationDetailsTypeManager()
                    .getAuthorizationDetailsTypeByApiIdAndType(
                            apiResourceId,
                            type,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()
                    );
            if (authorizationDetailsType == null) {
                throw APIResourceMgtEndpointUtil.handleException(
                        Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_AUTHORIZATION_DETAILS_TYPE_NOT_FOUND,
                        apiResourceId
                );
            }
            return AuthorizationDetailsTypeMgtUtil.toAuthorizationDetailsGetModel(authorizationDetailsType);
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public List<AuthorizationDetailsTypesGetModel> getAuthorizationDetailsTypes(String apiResourceId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving authorization details type against resource id: %s", apiResourceId));
        }
        try {
            APIResource apiResource = APIResourceManagementServiceHolder.getApiResourceManager()
                    .getAPIResourceById(apiResourceId, CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (apiResource == null) {
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_API_RESOURCE_NOT_FOUND, apiResourceId);
            }

            return AuthorizationDetailsTypeMgtUtil.
                    toAuthorizationDetailsGetModelsList(apiResource.getAuthorizationDetailsTypes());
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public boolean isAuthorizationDetailsTypeExists(String apiResourceId, String authorizationDetailsType) {

        try {
            return APIResourceManagementServiceHolder.getAuthorizationDetailsTypeManager()
                    .isAuthorizationDetailsTypeExists(
                            apiResourceId,
                            authorizationDetailsType,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()
                    );
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public void updateAuthorizationDetailsTypes(String apiResourceId, String authorizationDetailsType,
                                                AuthorizationDetailsTypesPatchModel patchModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Updating authorization details type against resource id: %s and type: %s ",
                    apiResourceId, authorizationDetailsType));
        }
        try {
            APIResource apiResource = APIResourceManagementServiceHolder.getApiResourceManager()
                    .getAPIResourceById(apiResourceId, CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (apiResource == null) {
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_API_RESOURCE_NOT_FOUND, apiResourceId);
            }

            if (!this.isAuthorizationDetailsTypeExists(apiResourceId, authorizationDetailsType)) {
                throw APIResourceMgtEndpointUtil.handleException(
                        Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_AUTHORIZATION_DETAILS_TYPE_NOT_FOUND,
                        apiResourceId
                );
            }

            APIResourceManagementServiceHolder.getAuthorizationDetailsTypeManager().updateAuthorizationDetailsType(
                    apiResourceId,
                    AuthorizationDetailsTypeMgtUtil.toAuthorizationDetailsType(authorizationDetailsType, patchModel),
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain()
            );
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public List<AuthorizationDetailsTypesGetModel> getAllAuthorizationDetailsTypes(String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving all authorization details type against filter: %s", filter));
        }
        try {
            List<AuthorizationDetailsType> AuthorizationDetailsTypes = APIResourceManagementServiceHolder
                    .getAuthorizationDetailsTypeManager()
                    .getAuthorizationDetailsTypes(
                            filter, CarbonContext.getThreadLocalCarbonContext().getTenantDomain()
                    );

            return AuthorizationDetailsTypeMgtUtil.toAuthorizationDetailsGetModelsList(AuthorizationDetailsTypes);
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }
}
