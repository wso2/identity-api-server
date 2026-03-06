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

package org.wso2.carbon.identity.api.server.api.resource.v1;

import org.wso2.carbon.identity.api.server.api.resource.v1.*;
import org.wso2.carbon.identity.api.server.api.resource.v1.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceListResponse;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourcePatchModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceResponse;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.AuthorizationDetailsTypesGetModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.Error;
import java.util.List;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopeCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopeGetModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopePatchModel;
import javax.ws.rs.core.Response;


public interface ApiResourcesApiService {

      public Response addAPIResource(APIResourceCreationModel apIResourceCreationModel);

      public Response addAuthorizationDetailsTypes(String apiResourceId, List<AuthorizationDetailsTypesCreationModel> authorizationDetailsTypesCreationModel);

      public Response apiResourcesApiResourceIdDelete(String apiResourceId);

      public Response apiResourcesApiResourceIdGet(String apiResourceId);

      public Response apiResourcesApiResourceIdPatch(String apiResourceId, APIResourcePatchModel apIResourcePatchModel);

      public Response apiResourcesApiResourceIdScopesGet(String apiResourceId);

      public Response apiResourcesApiResourceIdScopesIdScopeIdDelete(String apiResourceId, String scopeId);

      public Response apiResourcesApiResourceIdScopesIdScopeIdPatch(String apiResourceId, String scopeId, ScopePatchModel scopePatchModel);

      public Response apiResourcesApiResourceIdScopesPut(String apiResourceId, List<ScopeCreationModel> scopeCreationModel);

      public Response apiResourcesApiResourceIdScopesScopeNameDelete(String apiResourceId, String scopeName);

      public Response apiResourcesApiResourceIdScopesScopeNamePatch(String apiResourceId, String scopeName, ScopePatchModel scopePatchModel);

      public Response deleteAuthorizationDetailsType(String apiResourceId, String authorizationDetailsTypeId);

      public Response getAPIResources(String before, String after, String filter, Integer limit, String attributes);

      public Response getAuthorizationDetailsType(String apiResourceId, String authorizationDetailsTypeId);

      public Response getAuthorizationDetailsTypes(String apiResourceId);

      public Response updateAuthorizationDetailsType(String apiResourceId, String authorizationDetailsTypeId, AuthorizationDetailsTypesCreationModel authorizationDetailsTypesCreationModel);
}
