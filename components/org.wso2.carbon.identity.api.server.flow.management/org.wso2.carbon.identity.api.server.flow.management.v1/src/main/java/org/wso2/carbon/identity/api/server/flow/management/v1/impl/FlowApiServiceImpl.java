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

import org.wso2.carbon.identity.api.server.flow.management.v1.BaseFlowMetaResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowApiService;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfig;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfigPatchModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowRequest;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.core.ServerFlowMgtService;
import org.wso2.carbon.identity.api.server.flow.management.v1.factories.ServerFlowMgtServiceFactory;

import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Implementation of the Flow API.
 */
public class FlowApiServiceImpl implements FlowApiService {

    ServerFlowMgtService flowMgtService;

    public FlowApiServiceImpl() {

        try {
            this.flowMgtService = ServerFlowMgtServiceFactory.getFlowMgtService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating flow management service.", e);
        }
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
    public Response getFlowMeta(String flowType) {

        BaseFlowMetaResponse flowMeta = flowMgtService.getFlowMeta(flowType);
        return Response.ok().entity(flowMeta).build();
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


}
