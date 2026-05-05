/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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
import org.wso2.carbon.identity.api.resource.mgt.APIResourceManager;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceMgtException;
import org.wso2.carbon.identity.api.resource.mgt.AuthorizationDetailsTypeManager;
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
    private final AuthorizationDetailsTypeManager authorizationDetailsTypeManager;
    private final APIResourceManager apiResourceManager;

    public AuthorizationDetailsTypeManagementService(APIResourceManager apiResourceManager,
                                                     AuthorizationDetailsTypeManager authorizationDetailsTypeManager) {

        this.apiResourceManager = apiResourceManager;
        this.authorizationDetailsTypeManager = authorizationDetailsTypeManager;
        LOG.info("Authorization details type management service initialized successfully.");
    }

    /**
     * Adds a list of authorization details types to a specified API resource.
     *
     * @param apiResourceId  The ID of the API resource.
     * @param creationModels List of models containing the authorization details types to be added.
     * @return A list of added {@link AuthorizationDetailsType} instances.
     */
    public List<AuthorizationDetailsType> addAuthorizationDetailsTypes(
            final String apiResourceId, final List<AuthorizationDetailsTypesCreationModel> creationModels) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding authorization details types to resource with id: " + apiResourceId);
        }

        try {
            this.assertApiResourceIdExistence(apiResourceId);

            List<AuthorizationDetailsType> result = this.authorizationDetailsTypeManager.addAuthorizationDetailsTypes(
                    apiResourceId,
                    AuthorizationDetailsTypeMgtUtil.toAuthorizationDetailsTypes(creationModels),
                    getThreadLocalCarbonContext().getTenantDomain()
            );
            LOG.info("Successfully added " + (result != null ? result.size() : 0) +
                    " authorization details types for API resource ID: " + apiResourceId);
            return result;
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Deletes a specific authorization details type by its ID for a given API resource ID.
     *
     * @param apiResourceId              The ID of the API resource.
     * @param authorizationDetailsTypeId The ID of the authorization details type to be deleted.
     */
    public void deleteAuthorizationDetailsTypeById(String apiResourceId, String authorizationDetailsTypeId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Deleting authorization details type against resource ID: %s and type ID: %s ",
                    apiResourceId, authorizationDetailsTypeId));
        }
        try {
            this.authorizationDetailsTypeManager.deleteAuthorizationDetailsTypeByApiIdAndTypeId(
                    apiResourceId,
                    authorizationDetailsTypeId,
                    getThreadLocalCarbonContext().getTenantDomain()
            );
            LOG.info("Successfully deleted authorization details type with ID: " + authorizationDetailsTypeId +
                    " for API resource ID: " + apiResourceId);
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Retrieves a specific authorization details type by its ID for a given API resource ID.
     *
     * @param apiResourceId              The ID of the API resource.
     * @param authorizationDetailsTypeId The ID of the authorization details type to retrieve.
     * @return An {@link AuthorizationDetailsTypesGetModel} containing the authorization details type.
     */
    public AuthorizationDetailsTypesGetModel getAuthorizationDetailsTypeById(String apiResourceId,
                                                                             String authorizationDetailsTypeId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving authorization details type against resource ID: %s and type ID: %s ",
                    apiResourceId, authorizationDetailsTypeId));
        }
        try {
            AuthorizationDetailsType authorizationDetailsType = this.authorizationDetailsTypeManager
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

    /**
     * Retrieves a list of authorization details types by a given API resource ID.
     *
     * @param apiResourceId The ID of the API resource.
     * @return A list of {@link AuthorizationDetailsTypesGetModel} containing the authorization details types.
     */
    public List<AuthorizationDetailsTypesGetModel> getAuthorizationDetailsTypes(String apiResourceId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving authorization details type against resource id: " + apiResourceId);
        }
        try {
            final APIResource apiResource = this.apiResourceManager
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

    /**
     * Updates the authorization details type for a specified API resource.
     *
     * @param apiResourceId              The ID of the API resource.
     * @param authorizationDetailsTypeId The ID of the authorization details type to update.
     * @param creationModel              The authorization details types to be updated.
     */
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

            this.authorizationDetailsTypeManager.updateAuthorizationDetailsType(
                    apiResourceId,
                    toAuthorizationDetailsType(authorizationDetailsTypeId, creationModel),
                    getThreadLocalCarbonContext().getTenantDomain()
            );
            LOG.info("Successfully updated authorization details type with ID: " + authorizationDetailsTypeId +
                    " for API resource ID: " + apiResourceId);
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Checks if the specified authorization details type exists for a given type ID and API resource ID.
     *
     * @param apiResourceId              The ID of the API resource.
     * @param authorizationDetailsTypeId The ID of the authorization details type to check.
     * @return {@code true} if the authorization details type exists, {@code false} otherwise.
     */
    public boolean isAuthorizationDetailsTypeIdExists(String apiResourceId, String authorizationDetailsTypeId) {

        try {
            return this.authorizationDetailsTypeManager.getAuthorizationDetailsTypeByApiIdAndTypeId(
                    apiResourceId,
                    authorizationDetailsTypeId,
                    getThreadLocalCarbonContext().getTenantDomain()
            ) != null;
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Checks if an authorization details type exists based on a filter.
     *
     * @param filter The filter string to match authorization details types.
     * @return {@code true} if at least one authorization details type matches the filter, {@code false} otherwise.
     */
    public boolean isAuthorizationDetailsTypeExists(String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Checking authorization details type exists against filter: " + filter);
        }
        try {
            return this.authorizationDetailsTypeManager
                    .isAuthorizationDetailsTypeExists(
                            filter, getThreadLocalCarbonContext().getTenantDomain()
                    );
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    /**
     * Retrieves all authorization details types that match a specific filter.
     *
     * @param filter The filter string to retrieve matching authorization details types.
     * @return A list of {@link AuthorizationDetailsTypesGetModel} objects that match the filter.
     */
    public List<AuthorizationDetailsTypesGetModel> getAllAuthorizationDetailsTypes(final String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving all authorization details type against filter: " + filter);
        }
        try {
            final List<AuthorizationDetailsType> authorizationDetailsTypes = this.authorizationDetailsTypeManager
                    .getAuthorizationDetailsTypes(filter, getThreadLocalCarbonContext().getTenantDomain());

            return toAuthorizationDetailsGetModels(authorizationDetailsTypes);
        } catch (APIResourceMgtException e) {
            throw APIResourceMgtEndpointUtil.handleAPIResourceMgtException(e);
        }
    }

    private void assertApiResourceIdExistence(final String apiResourceId) throws APIResourceMgtException {

        final APIResource apiResource = this.apiResourceManager
                .getAPIResourceById(apiResourceId, getThreadLocalCarbonContext().getTenantDomain());

        if (apiResource == null) {
            throw APIResourceMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                    APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_API_RESOURCE_NOT_FOUND, apiResourceId);
        }
    }
}
