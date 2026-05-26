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

package org.wso2.carbon.identity.api.server.flow.management.v1.impl;

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowApiService;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfig;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfigPatchModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionBasicResponse;
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
import org.wso2.carbon.identity.api.server.flow.management.v1.core.FlowAIServiceCore;
import org.wso2.carbon.identity.api.server.flow.management.v1.core.ServerFlowMgtService;
import org.wso2.carbon.identity.api.server.flow.management.v1.factories.FlowAIServiceFactory;
import org.wso2.carbon.identity.api.server.flow.management.v1.factories.ServerFlowMgtServiceFactory;
import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FLOW_EXTENSION_PATH_COMPONENT;

/**
 * Implementation of the Flow API.
 */
public class FlowApiServiceImpl implements FlowApiService {

    ServerFlowMgtService flowMgtService;
    FlowAIServiceCore flowAIServiceCore;

    public FlowApiServiceImpl() {

        try {
            this.flowMgtService = ServerFlowMgtServiceFactory.getFlowMgtService();
            this.flowAIServiceCore = FlowAIServiceFactory.getFlowAIService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating flow management service.", e);
        }
    }


    @Override
    public Response createFlowExtension(FlowExtensionModel flowExtensionModel) {

        FlowExtensionResponse created = flowMgtService.createFlowExtension(flowExtensionModel);
        URI location = ContextLoader.buildURIForHeader(
                V1_API_PATH_COMPONENT + FLOW_EXTENSION_PATH_COMPONENT + created.getId());
        return Response.created(location).entity(created).build();
    }

    @Override
    public Response deleteFlow(String flowType) {

        flowMgtService.deleteFlow(flowType);
        return Response.noContent().build();
    }

    @Override
    public Response deleteFlowExtension(String extensionId) {

        flowMgtService.deleteFlowExtension(extensionId);
        return Response.noContent().build();
    }

    @Override
    public Response generateFlow(FlowGenerateRequest flowGenerateRequest) {

        FlowGenerateResponse flowResponse = flowAIServiceCore.generateFlow(flowGenerateRequest);
        return Response.ok().entity(flowResponse).build();
    }

    @Override
    public Response getFlow(String flowType) {

        FlowResponse flow = flowMgtService.getFlow(flowType);
        return Response.ok().entity(flow).build();
    }

    @Override
    public Response getFlowConfigForFlow(String flowType) {

        FlowConfig flowConfig = flowMgtService.getFlowConfigForFlow(flowType);
        return Response.ok().entity(flowConfig).build();
    }

    @Override
    public Response getFlowConfigs() {

        List<FlowConfig> flowConfigs = flowMgtService.getFlowConfigs();
        return Response.ok().entity(flowConfigs).build();
    }

    @Override
    public Response getFlowGenerationResult(String operationId) {

        FlowGenerateResult flowGenerateResult = flowAIServiceCore.getFlowGenerationResult(operationId);
        return Response.ok().entity(flowGenerateResult).build();
    }

    @Override
    public Response getFlowGenerationStatus(String operationId) {

        FlowGenerateStatus status = flowAIServiceCore.getFlowGenerationStatus(operationId);
        return Response.ok().entity(status).build();
    }

    @Override
    public Response getFlowMeta(String flowType) {

        FlowMetaResponse flowMeta = flowMgtService.getFlowMeta(flowType);
        return Response.ok().entity(flowMeta).build();
    }

    @Override
    public Response getFlowExtensionById(String extensionId) {

        FlowExtensionResponse extension = flowMgtService.getFlowExtensionById(extensionId);
        return Response.ok().entity(extension).build();
    }

    @Override
    public Response getFlowExtensionContextTree(String flowType) {

        return Response.ok().entity(
                flowMgtService.getFlowExtensionContextTree(flowType)).build();
    }

    @Override
    public Response getFlowExtensions() {

        List<FlowExtensionBasicResponse> extensions = flowMgtService.getFlowExtensions();
        return Response.ok().entity(extensions).build();
    }

    @Override
    public Response updateFlow(FlowRequest flowRequest) {

        flowMgtService.updateFlow(flowRequest);
        return Response.ok().build();
    }

    @Override
    public Response updateFlowConfig(FlowConfigPatchModel flowConfigPatchModel) {

        FlowConfig flowConfig = flowMgtService.updateFlowConfig(flowConfigPatchModel);
        return Response.ok().entity(flowConfig).build();
    }

    @Override
    public Response updateFlowExtension(String extensionId,
                                          FlowExtensionUpdateModel flowExtensionUpdateModel) {

        FlowExtensionResponse updated =
                flowMgtService.updateFlowExtension(extensionId, flowExtensionUpdateModel);
        return Response.ok().entity(updated).build();
    }
}
