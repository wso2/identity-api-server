/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.vp.template.management.v1;

import org.wso2.carbon.identity.api.server.vp.template.management.v1.*;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.ConnectedIdpsResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.Error;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.IssuerConfigListResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionCreationModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionList;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionUpdateModel;
import javax.ws.rs.core.Response;


public interface PresentationDefinitionsApiService {

      public Response createPresentationDefinition(PresentationDefinitionCreationModel presentationDefinitionCreationModel);

      public Response deletePresentationDefinition(String definitionId);

      public Response getConnectedIdps(String definitionId);

      public Response getIssuerConfigs(String definitionId, String credentialIdentifier);

      public Response getPresentationDefinition(String definitionId);

      public Response listPresentationDefinitions(String before, String after, String filter, Integer limit);

      public Response replaceIssuerConfigs(String definitionId, String credentialIdentifier, IssuerConfigListResponse issuerConfigListResponse);

      public Response updatePresentationDefinition(String definitionId, PresentationDefinitionUpdateModel presentationDefinitionUpdateModel);
}
