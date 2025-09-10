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

package org.wso2.carbon.identity.api.server.api.resource.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourcePatchModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceResponse;
import org.wso2.carbon.identity.api.server.api.resource.v1.ApiResourcesApiService;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopeCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopePatchModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.api.resource.v1.core.AuthorizationDetailsTypeManagementService;
import org.wso2.carbon.identity.api.server.api.resource.v1.core.ServerAPIResourceManagementService;
import org.wso2.carbon.identity.api.server.api.resource.v1.factories.AuthorizationDetailsTypeManagementServiceFactory;
import org.wso2.carbon.identity.api.server.api.resource.v1.factories.ServerAPIResourceManagementServiceFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.application.common.model.AuthorizationDetailsType;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;

/**
 * Implementation of the Api resources REST Api.
 */
public class ApiResourcesApiServiceImpl implements ApiResourcesApiService {

    private static final Log LOG = LogFactory.getLog(ApiResourcesApiServiceImpl.class);
    private final ServerAPIResourceManagementService serverAPIResourceManagementService;
    private final AuthorizationDetailsTypeManagementService typeMgtService;

    public ApiResourcesApiServiceImpl() {

        try {
            this.serverAPIResourceManagementService = ServerAPIResourceManagementServiceFactory
                    .getServerAPIResourceManagementService();
            this.typeMgtService = AuthorizationDetailsTypeManagementServiceFactory
                    .getAuthorizationDetailsTypeManagementService();
            LOG.info("API resource service implementation initialized successfully.");
        } catch (IllegalStateException e) {
            LOG.error("Failed to initialize API resource management service.", e);
            throw new RuntimeException("Error occurred while initiating API resource management service.", e);
        }
    }

    @Override
    public Response addAPIResource(APIResourceCreationModel apIResourceCreationModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to add new API resource with identifier: " +
                    (apIResourceCreationModel != null ? apIResourceCreationModel.getIdentifier() : "null"));
        }
        if (apIResourceCreationModel == null) {
            LOG.error("API resource creation model cannot be null");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        APIResourceResponse apiResourceResponse =
                serverAPIResourceManagementService.addAPIResourceWithResourceId(apIResourceCreationModel);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT +
                APIResourceMgtEndpointConstants.API_RESOURCE_PATH_COMPONENT + "/" + apiResourceResponse.getId());
        LOG.info("Successfully created API resource with ID: " + apiResourceResponse.getId());
        return Response.created(location).entity(apiResourceResponse).build();
    }

    @Override
    public Response addAuthorizationDetailsTypes(
            String apiResourceId, List<AuthorizationDetailsTypesCreationModel> authorizationDetailsTypesCreationModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to add authorization details types for API resource ID: " + apiResourceId);
        }
        List<AuthorizationDetailsType> authorizationDetailsTypes =
                typeMgtService.addAuthorizationDetailsTypes(apiResourceId, authorizationDetailsTypesCreationModel);
        LOG.info("Successfully added authorization details types for API resource ID: " + apiResourceId);
        return Response.ok().entity(authorizationDetailsTypes).build();
    }

    @Override
    public Response apiResourcesApiResourceIdDelete(String apiResourceId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to delete API resource with ID: " + apiResourceId);
        }
        serverAPIResourceManagementService.deleteAPIResource(apiResourceId);
        LOG.info("Successfully deleted API resource with ID: " + apiResourceId);
        return Response.noContent().build();
    }

    @Override
    public Response apiResourcesApiResourceIdGet(String apiResourceId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to get API resource with ID: " + apiResourceId);
        }
        return Response.ok().entity(
                serverAPIResourceManagementService.getAPIResourceResponseById(apiResourceId)).build();
    }

    @Override
    public Response apiResourcesApiResourceIdPatch(String apiResourceId, APIResourcePatchModel apIResourcePatchModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to patch API resource with ID: " + apiResourceId);
        }
        serverAPIResourceManagementService.patchAPIResourceById(apiResourceId, apIResourcePatchModel);
        LOG.info("Successfully patched API resource with ID: " + apiResourceId);
        return Response.noContent().build();
    }

    @Override
    public Response apiResourcesApiResourceIdScopesGet(String apiResourceId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to get scopes for API resource with ID: " + apiResourceId);
        }
        return Response.ok().entity(serverAPIResourceManagementService.getScopesByAPIId(apiResourceId)).build();
    }

    @Override
    public Response apiResourcesApiResourceIdScopesPut(String apiResourceId,
                                                       List<ScopeCreationModel> scopeCreationModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to update scopes for API resource with ID: " + apiResourceId);
        }
        serverAPIResourceManagementService.putScopesByAPIId(apiResourceId, scopeCreationModel);
        LOG.info("Successfully updated scopes for API resource with ID: " + apiResourceId);
        return Response.noContent().build();
    }

    @Override
    public Response apiResourcesApiResourceIdScopesScopeNameDelete(String apiResourceId, String scopeName) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to delete scope '" + scopeName + "' for API resource ID: " + apiResourceId);
        }
        serverAPIResourceManagementService.deleteScopeByScopeName(apiResourceId, scopeName);
        LOG.info("Successfully deleted scope '" + scopeName + "' for API resource ID: " + apiResourceId);
        return Response.noContent().build();
    }

    @Override
    public Response apiResourcesApiResourceIdScopesScopeNamePatch(String apiResourceId, String scopeName,
                                                                  ScopePatchModel scopePatchModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to patch scope '" + scopeName + "' for API resource ID: " + apiResourceId);
        }
        serverAPIResourceManagementService.patchScopeMetadataByScopeName(apiResourceId, scopeName, scopePatchModel);
        LOG.info("Successfully patched scope '" + scopeName + "' for API resource ID: " + apiResourceId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteAuthorizationDetailsType(String apiResourceId, String authorizationDetailsTypeId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to delete authorization details type with ID: " + authorizationDetailsTypeId
                    + " for API resource ID: " + apiResourceId);
        }
        typeMgtService.deleteAuthorizationDetailsTypeById(apiResourceId, authorizationDetailsTypeId);
        LOG.info("Successfully deleted authorization details type with ID: " + authorizationDetailsTypeId
                + " for API resource ID: " + apiResourceId);
        return Response.noContent().build();
    }

    @Override
    public Response getAPIResources(String before, String after, String filter, Integer limit, String attributes) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to get API resources with filter: " + filter + ", limit: " + limit);
        }
        return Response.ok().entity(serverAPIResourceManagementService.getAPIResources(before, after, filter, limit,
                attributes)).build();
    }

    @Override
    public Response getAuthorizationDetailsType(String apiResourceId, String authorizationDetailsTypeId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to get authorization details type with ID: " + authorizationDetailsTypeId
                    + " for API resource ID: " + apiResourceId);
        }
        return Response.ok().entity(typeMgtService
                .getAuthorizationDetailsTypeById(apiResourceId, authorizationDetailsTypeId)).build();
    }

    @Override
    public Response getAuthorizationDetailsTypes(String apiResourceId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to get authorization details types for API resource ID: " + apiResourceId);
        }
        return Response.ok().entity(typeMgtService.getAuthorizationDetailsTypes(apiResourceId)).build();
    }

    @Override
    public Response updateAuthorizationDetailsType(String apiResourceId, String authorizationDetailsTypeId,
                                                   AuthorizationDetailsTypesCreationModel creationModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request received to update authorization details type with ID: " + authorizationDetailsTypeId
                    + " for API resource ID: " + apiResourceId);
        }
        typeMgtService.updateAuthorizationDetailsTypes(apiResourceId, authorizationDetailsTypeId, creationModel);
        LOG.info("Successfully updated authorization details type with ID: " + authorizationDetailsTypeId
                + " for API resource ID: " + apiResourceId);
        return Response.noContent().build();
    }
}
