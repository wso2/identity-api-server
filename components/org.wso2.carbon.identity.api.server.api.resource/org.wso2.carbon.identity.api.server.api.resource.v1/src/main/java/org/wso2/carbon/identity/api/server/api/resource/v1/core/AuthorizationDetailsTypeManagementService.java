/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceMgtException;
import org.wso2.carbon.identity.api.server.api.resource.common.APIResourceManagementServiceHolder;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesGetModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.api.resource.v1.util.APIResourceMgtEndpointUtil;
import org.wso2.carbon.identity.api.server.api.resource.v1.util.AuthorizationDetailsTypeMgtUtil;
import org.wso2.carbon.identity.application.common.model.APIResource;
import org.wso2.carbon.identity.application.common.model.AuthorizationDetailsType;

import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.context.CarbonContext.getThreadLocalCarbonContext;
import static org.wso2.carbon.identity.api.server.api.resource.v1.util.AuthorizationDetailsTypeMgtUtil.toAuthorizationDetailsGetModels;
import static org.wso2.carbon.identity.api.server.api.resource.v1.util.AuthorizationDetailsTypeMgtUtil.toAuthorizationDetailsType;

/**
 * Authorization Details Type Management Service.
 */
public class AuthorizationDetailsTypeManagementService {

    private static final Log LOG = LogFactory.getLog(AuthorizationDetailsTypeManagementService.class);

    public List<AuthorizationDetailsType> addAuthorizationDetailsTypes(
            final String apiResourceId, final List<AuthorizationDetailsTypesCreationModel> creationModels) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding authorization details types to resource with id: " + apiResourceId);
        }

        try {
            this.assertApiResourceIdExistence(apiResourceId);

            return APIResourceManagementServiceHolder.getAuthorizationDetailsTypeManager().addAuthorizationDetailsTypes(
                    apiResourceId,
                    AuthorizationDetailsTypeMgtUtil.toAuthorizationDetailsTypesList(creationModels),
                    getThreadLocalCarbonContext().getTenantDomain()
            );
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public void deleteAuthorizationDetailsTypeById(String apiResourceId, String authorizationDetailsTypeId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Deleting authorization details type against resource ID: %s and type ID: %s ",
                    apiResourceId, authorizationDetailsTypeId));
        }
        try {
            APIResourceManagementServiceHolder.getAuthorizationDetailsTypeManager()
                    .deleteAuthorizationDetailsTypeByApiIdAndTypeId(
                            apiResourceId,
                            authorizationDetailsTypeId,
                            getThreadLocalCarbonContext().getTenantDomain()
                    );
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public AuthorizationDetailsTypesGetModel getAuthorizationDetailsTypeById(String apiResourceId,
                                                                             String authorizationDetailsTypeId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving authorization details type against resource ID: %s and type ID: %s ",
                    apiResourceId, authorizationDetailsTypeId));
        }
        try {
            AuthorizationDetailsType authorizationDetailsType = APIResourceManagementServiceHolder
                    .getAuthorizationDetailsTypeManager()
                    .getAuthorizationDetailsTypeByApiIdAndTypeId(
                            apiResourceId,
                            authorizationDetailsTypeId,
                            getThreadLocalCarbonContext().getTenantDomain()
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
            LOG.debug("Retrieving authorization details type against resource id: " + apiResourceId);
        }
        try {
            final APIResource apiResource = APIResourceManagementServiceHolder.getApiResourceManager()
                    .getAPIResourceById(apiResourceId, getThreadLocalCarbonContext().getTenantDomain());

            if (apiResource == null) {
                throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_API_RESOURCE_NOT_FOUND, apiResourceId);
            }

            return toAuthorizationDetailsGetModels(apiResource.getAuthorizationDetailsTypes());
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public void updateAuthorizationDetailsTypes(String apiResourceId, String authorizationDetailsTypeId,
                                                AuthorizationDetailsTypesCreationModel creationModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Updating authorization details type against resource ID: %s and type ID: %s ",
                    apiResourceId, authorizationDetailsTypeId));
        }
        try {
            this.assertApiResourceIdExistence(apiResourceId);

            if (!this.isAuthorizationDetailsTypeIdExists(apiResourceId, authorizationDetailsTypeId)) {
                throw APIResourceMgtEndpointUtil.handleException(
                        Response.Status.NOT_FOUND,
                        APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_AUTHORIZATION_DETAILS_TYPE_NOT_FOUND,
                        apiResourceId
                );
            }

            APIResourceManagementServiceHolder.getAuthorizationDetailsTypeManager().updateAuthorizationDetailsType(
                    apiResourceId,
                    toAuthorizationDetailsType(authorizationDetailsTypeId, creationModel),
                    getThreadLocalCarbonContext().getTenantDomain()
            );
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public boolean isAuthorizationDetailsTypeIdExists(String apiResourceId, String authorizationDetailsTypeId) {

        try {
            return APIResourceManagementServiceHolder.getAuthorizationDetailsTypeManager()
                    .getAuthorizationDetailsTypeByApiIdAndTypeId(
                            apiResourceId,
                            authorizationDetailsTypeId,
                            getThreadLocalCarbonContext().getTenantDomain()
                    ) != null;
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public boolean isAuthorizationDetailsTypeExists(String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Checking authorization details type exists against filter: " + filter);
        }
        try {
            return APIResourceManagementServiceHolder
                    .getAuthorizationDetailsTypeManager()
                    .isAuthorizationDetailsTypeExists(
                            filter, getThreadLocalCarbonContext().getTenantDomain()
                    );
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    public List<AuthorizationDetailsTypesGetModel> getAllAuthorizationDetailsTypes(final String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving all authorization details type against filter: " + filter);
        }
        try {
            final List<AuthorizationDetailsType> authorizationDetailsTypes = APIResourceManagementServiceHolder
                    .getAuthorizationDetailsTypeManager()
                    .getAuthorizationDetailsTypes(filter, getThreadLocalCarbonContext().getTenantDomain());

            return toAuthorizationDetailsGetModels(authorizationDetailsTypes);
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    private void assertApiResourceIdExistence(final String apiResourceId) throws APIResourceMgtException {

        final APIResource apiResource = APIResourceManagementServiceHolder.getApiResourceManager()
                .getAPIResourceById(apiResourceId, getThreadLocalCarbonContext().getTenantDomain());

        if (apiResource == null) {
            throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                    APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_API_RESOURCE_NOT_FOUND, apiResourceId);
        }
    }
}
