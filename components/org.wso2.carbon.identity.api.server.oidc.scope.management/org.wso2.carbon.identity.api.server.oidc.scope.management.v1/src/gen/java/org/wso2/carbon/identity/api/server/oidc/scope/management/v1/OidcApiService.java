/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.oidc.scope.management.v1;

import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.*;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.ErrorResponse;
import java.io.File;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.Scope;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.model.ScopeUpdateRequest;
import javax.ws.rs.core.Response;


public interface OidcApiService {

      public Response addScope(Scope scope);

      public Response deleteScope(String id);

      public Response exportScopeToFile(String id, String accept);

      public Response getScope(String id);

      public Response getScopes();

      public Response importScopeFromFile(InputStream fileInputStream, Attachment fileDetail);

      public Response updateScope(String id, ScopeUpdateRequest scopeUpdateRequest);

      public Response updateScopeFromFile(String id, InputStream fileInputStream, Attachment fileDetail);
}
