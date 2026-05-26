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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import org.wso2.carbon.identity.api.server.flow.management.v1.*;
import org.wso2.carbon.identity.api.server.flow.management.v1.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.flow.management.v1.Error;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfig;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfigPatchModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionBasicResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionContextTreeResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionUpdateModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowGenerateRequest;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowGenerateResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowGenerateResult;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowGenerateStatus;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowMetaResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowRequest;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowResponse;
import javax.ws.rs.core.Response;


public interface FlowApiService {

      public Response createFlowExtension(FlowExtensionModel flowExtensionModel);

      public Response deleteFlow(String flowType);

      public Response deleteFlowExtension(String extensionId);

      public Response generateFlow(FlowGenerateRequest flowGenerateRequest);

      public Response getFlow(String flowType);

      public Response getFlowConfigForFlow(String flowType);

      public Response getFlowConfigs();

      public Response getFlowExtensionById(String extensionId);

      public Response getFlowExtensionContextTree(String flowType);

      public Response getFlowExtensions();

      public Response getFlowGenerationResult(String operationId);

      public Response getFlowGenerationStatus(String operationId);

      public Response getFlowMeta(String flowType);

      public Response updateFlow(FlowRequest flowRequest);

      public Response updateFlowConfig(FlowConfigPatchModel flowConfigPatchModel);

      public Response updateFlowExtension(String extensionId, FlowExtensionUpdateModel flowExtensionUpdateModel);
}
