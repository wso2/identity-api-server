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
        } catch (IllegalStateException e) {
            LOG.error("Failed to initialize API resource management services", e);
            throw new RuntimeException("Error occurred while initiating API resource management service.", e);
        }
    }

    @Override
    public Response addAPIResource(APIResourceCreationModel apIResourceCreationModel) {

        if (LOG.isDebugEnabled()) {
            String identifier = apIResourceCreationModel != null ? apIResourceCreationModel.getIdentifier() : "null";
            LOG.debug("Creating API resource with identifier: " + identifier);
        }
        APIResourceResponse apiResourceResponse =
                serverAPIResourceManagementService.addAPIResourceWithResourceId(apIResourceCreationModel);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT +
                APIResourceMgtEndpointConstants.API_RESOURCE_PATH_COMPONENT + "/" + apiResourceResponse.getId());
        return Response.created(location).entity(apiResourceResponse).build();
    }

    @Override
    public Response addAuthorizationDetailsTypes(
            String apiResourceId, List<AuthorizationDetailsTypesCreationModel> authorizationDetailsTypesCreationModel) {

        List<AuthorizationDetailsType> authorizationDetailsTypes =
                typeMgtService.addAuthorizationDetailsTypes(apiResourceId, authorizationDetailsTypesCreationModel);
        return Response.ok().entity(authorizationDetailsTypes).build();
    }

    @Override
    public Response apiResourcesApiResourceIdDelete(String apiResourceId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting API resource with ID: " + apiResourceId);
        }
        serverAPIResourceManagementService.deleteAPIResource(apiResourceId);
        return Response.noContent().build();
    }

    @Override
    public Response apiResourcesApiResourceIdGet(String apiResourceId) {

        return Response.ok().entity(
                serverAPIResourceManagementService.getAPIResourceResponseById(apiResourceId)).build();
    }

    @Override
    public Response apiResourcesApiResourceIdPatch(String apiResourceId, APIResourcePatchModel apIResourcePatchModel) {

        serverAPIResourceManagementService.patchAPIResourceById(apiResourceId, apIResourcePatchModel);
        return Response.noContent().build();
    }

    @Override
    public Response apiResourcesApiResourceIdScopesGet(String apiResourceId) {

        return Response.ok().entity(serverAPIResourceManagementService.getScopesByAPIId(apiResourceId)).build();
    }

    @Override
    public Response apiResourcesApiResourceIdScopesPut(String apiResourceId,
                                                       List<ScopeCreationModel> scopeCreationModel) {

        serverAPIResourceManagementService.putScopesByAPIId(apiResourceId, scopeCreationModel);
        return Response.noContent().build();
    }

    @Override
    public Response apiResourcesApiResourceIdScopesScopeNameDelete(String apiResourceId, String scopeName) {

        serverAPIResourceManagementService.deleteScopeByScopeName(apiResourceId, scopeName);
        return Response.noContent().build();
    }

    @Override
    public Response apiResourcesApiResourceIdScopesScopeNamePatch(String apiResourceId, String scopeName,
                                                                  ScopePatchModel scopePatchModel) {

        serverAPIResourceManagementService.patchScopeMetadataByScopeName(apiResourceId, scopeName, scopePatchModel);
        return Response.noContent().build();
    }

    @Override
    public Response deleteAuthorizationDetailsType(String apiResourceId, String authorizationDetailsTypeId) {

        typeMgtService.deleteAuthorizationDetailsTypeById(apiResourceId, authorizationDetailsTypeId);
        return Response.noContent().build();
    }

    @Override
    public Response getAPIResources(String before, String after, String filter, Integer limit, String attributes) {

        return Response.ok().entity(serverAPIResourceManagementService.getAPIResources(before, after, filter, limit,
                attributes)).build();
    }

    @Override
    public Response getAuthorizationDetailsType(String apiResourceId, String authorizationDetailsTypeId) {

        return Response.ok().entity(typeMgtService
                .getAuthorizationDetailsTypeById(apiResourceId, authorizationDetailsTypeId)).build();
    }

    @Override
    public Response getAuthorizationDetailsTypes(String apiResourceId) {

        return Response.ok().entity(typeMgtService.getAuthorizationDetailsTypes(apiResourceId)).build();
    }

    @Override
    public Response updateAuthorizationDetailsType(String apiResourceId, String authorizationDetailsTypeId,
                                                   AuthorizationDetailsTypesCreationModel creationModel) {

        typeMgtService.updateAuthorizationDetailsTypes(apiResourceId, authorizationDetailsTypeId, creationModel);
        return Response.noContent().build();
    }
}
