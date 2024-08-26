package org.wso2.carbon.identity.api.server.api.resource.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceMgtException;
import org.wso2.carbon.identity.api.server.api.resource.common.APIResourceManagementServiceHolder;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesGetModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesPatchModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.InlineResponse200;
import org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.api.resource.v1.util.APIResourceMgtEndpointUtil;
import org.wso2.carbon.identity.api.server.api.resource.v1.util.AuthorizationDetailsTypeMgtUtil;
import org.wso2.carbon.identity.application.common.model.AuthorizationDetailsType;

import java.util.List;

import javax.ws.rs.core.Response;

public class AuthorizationDetailsTypeManagementService {

    private static final Log LOG = LogFactory.getLog(AuthorizationDetailsTypeManagementService.class);

    public void addAuthorizationDetailsTypes(String apiResourceId,
                                             List<AuthorizationDetailsTypesPatchModel> typesPatchModels) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding authorization details types to resource with id: " + apiResourceId);
        }
        try {
            APIResourceManagementServiceHolder.getAuthorizationDetailsTypeManager()
                    .addAuthorizationDetailsTypes(
                            apiResourceId,
                            AuthorizationDetailsTypeMgtUtil
                                    .toAuthorizationDetailsTypesList(),
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
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_AUTHORIZATION_DETAILS_TYPE_NOT_FOUND, apiResourceId);
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
            List<AuthorizationDetailsType> AuthorizationDetailsTypes = APIResourceManagementServiceHolder
                    .getAuthorizationDetailsTypeManager()
                    .getAuthorizationDetailsTypesByApiId(
                            apiResourceId,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()
                    );

            if (CollectionUtils.isEmpty(AuthorizationDetailsTypes)) {
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_AUTHORIZATION_DETAILS_TYPE_NOT_FOUND, apiResourceId);
            }
            return AuthorizationDetailsTypeMgtUtil.toAuthorizationDetailsGetModelsList(AuthorizationDetailsTypes);
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public InlineResponse200 isAuthorizationDetailsTypeExists(String apiResourceId, String authorizationDetailsType) {
        try {
            return new InlineResponse200()
                    .exists(APIResourceManagementServiceHolder.getAuthorizationDetailsTypeManager()
                            .isAuthorizationDetailsTypeExists(apiResourceId, authorizationDetailsType));
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public void updateAuthorizationDetailsTypes(String apiResourceId, String authorizationDetailsType,
                                                AuthorizationDetailsTypesPatchModel typesPatchModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Updating authorization details type against resource id: %s and type: %s ",
                    apiResourceId, authorizationDetailsType));
        }
        try {
            APIResourceManagementServiceHolder.getAuthorizationDetailsTypeManager()
                    .updateAuthorizationDetailsType(AuthorizationDetailsTypeMgtUtil
                                    .toAuthorizationDetailsType(authorizationDetailsType, typesPatchModel),
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()
                    );
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }
}
