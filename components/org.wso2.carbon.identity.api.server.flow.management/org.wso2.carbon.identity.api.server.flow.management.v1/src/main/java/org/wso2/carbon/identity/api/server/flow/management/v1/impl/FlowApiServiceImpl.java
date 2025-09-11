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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowApiService;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfig;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfigPatchModel;
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

import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Implementation of the Flow API.
 */
public class FlowApiServiceImpl implements FlowApiService {

    private static final Log log = LogFactory.getLog(FlowApiServiceImpl.class);
    ServerFlowMgtService flowMgtService;
    FlowAIServiceCore flowAIServiceCore;

    public FlowApiServiceImpl() {

        try {
            log.info("Initializing Flow API service implementation.");
            this.flowMgtService = ServerFlowMgtServiceFactory.getFlowMgtService();
            this.flowAIServiceCore = FlowAIServiceFactory.getFlowAIService();
            log.info("Flow API service implementation initialized successfully.");
        } catch (IllegalStateException e) {
            log.error("Failed to initialize flow management service.", e);
            throw new RuntimeException("Error occurred while initiating flow management service.", e);
        }
    }

    @Override
    public Response generateFlow(FlowGenerateRequest flowGenerateRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Generating flow for type: " + 
                    (flowGenerateRequest != null ? flowGenerateRequest.getFlowType() : null));
        }

        if (flowGenerateRequest == null) {
            log.warn("FlowGenerateRequest is null. Cannot generate flow.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        FlowGenerateResponse flowResponse = flowAIServiceCore.generateFlow(flowGenerateRequest);
        return Response.ok().entity(flowResponse).build();
    }

    @Override
    public Response getFlow(String flowType) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving flow for type: " + flowType);
        }
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
    public Response updateFlow(FlowRequest flowRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Updating flow for type: " + 
                    (flowRequest != null ? flowRequest.getFlowType() : null));
        }

        if (flowRequest == null) {
            log.warn("FlowRequest is null. Cannot update flow.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        flowMgtService.updateFlow(flowRequest);
        log.info("Flow updated successfully for type: " + flowRequest.getFlowType());
        return Response.ok().build();
    }

    @Override
    public Response updateFlowConfig(FlowConfigPatchModel flowConfigPatchModel) {

        if (log.isDebugEnabled()) {
            log.debug("Updating flow config for type: " + 
                    (flowConfigPatchModel != null ? flowConfigPatchModel.getFlowType() : null));
        }

        if (flowConfigPatchModel == null) {
            log.warn("FlowConfigPatchModel is null. Cannot update flow config.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        FlowConfig flowConfig = flowMgtService.updateFlowConfig(flowConfigPatchModel);
        log.info("Flow config updated successfully for type: " + flowConfigPatchModel.getFlowType());
        return Response.ok().entity(flowConfig).build();
    }
}
