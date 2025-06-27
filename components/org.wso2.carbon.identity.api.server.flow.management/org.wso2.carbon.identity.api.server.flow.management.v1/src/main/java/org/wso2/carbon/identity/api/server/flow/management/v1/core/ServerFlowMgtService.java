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

package org.wso2.carbon.identity.api.server.flow.management.v1.core;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowMetaResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowRequest;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.AbstractMetaResponseHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.PasswordRecoveryFlowMetaHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.RegistrationFlowMetaHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.flow.mgt.FlowMgtService;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtFrameworkException;
import org.wso2.carbon.identity.flow.mgt.model.FlowDTO;

import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FlowType.validateFlowType;

/**
 * Service class for flow management.
 */
public class ServerFlowMgtService {

    private final FlowMgtService flowMgtService;

    public ServerFlowMgtService(FlowMgtService flowMgtService) {

        this.flowMgtService = flowMgtService;
    }

    /**
     * Retrieve the flow.
     *
     * @return FlowResponse.
     */
    public FlowResponse getFlow(String flowType) {

        FlowDTO flowDTO;
        try {
            validateFlowType(flowType);
            flowDTO = flowMgtService
                    .getFlow(flowType, PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId());
            FlowResponse flowResponse = new FlowResponse();
            if (flowDTO == null) {
                return flowResponse;
            }
            flowResponse.steps(flowDTO.getSteps().stream().map(Utils::convertToStep)
                    .collect(Collectors.toList()));
            return flowResponse;
        } catch (FlowMgtFrameworkException e) {
            throw Utils.handleFlowMgtException(e);
        }
    }

    /**
     * Retrieve flow metadata based on the flow type.
     *
     * @param flowType Type of the flow.
     * @return FlowMetaResponse containing metadata.
     */
    public FlowMetaResponse getFlowMeta(String flowType) {

        validateFlowType(flowType);
        AbstractMetaResponseHandler metaResponseHandler = resolveHandler(flowType);

        FlowMetaResponse flowMetaResponse = new FlowMetaResponse();
        flowMetaResponse.setFlowType(metaResponseHandler.getFlowType());
        flowMetaResponse.setAttributeProfile(metaResponseHandler.getAttributeProfile());
        flowMetaResponse.setSupportedExecutors(metaResponseHandler.getSupportedExecutors());
        flowMetaResponse.setConnectionMeta(metaResponseHandler.getConnectionMeta());
        flowMetaResponse.setConnectorConfigs(metaResponseHandler.getConnectorConfigs());

        return flowMetaResponse;
    }

    /**
     * Update the flow.
     *
     * @param flowRequest FlowRequest.
     */
    public void updateFlow(FlowRequest flowRequest) {

        try {
            validateFlowType(flowRequest.getFlowType());
            FlowDTO flowDTO = new FlowDTO();
            flowDTO.setSteps(flowRequest.getSteps().stream().map(Utils::convertToStepDTO)
                    .collect(Collectors.toList()));
            flowDTO.setFlowType(flowRequest.getFlowType());
            flowMgtService.updateFlow(flowDTO,
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId());
        } catch (FlowMgtFrameworkException e) {
            throw Utils.handleFlowMgtException(e);
        }
    }

    private AbstractMetaResponseHandler resolveHandler(String flowType) {

        switch (FlowEndpointConstants.FlowType.valueOf(flowType)) {
            case REGISTRATION:
                return new RegistrationFlowMetaHandler();
            case PASSWORD_RECOVERY:
                return new PasswordRecoveryFlowMetaHandler();
            default:
                // Should not happen due to validateFlowType().
                throw new IllegalStateException("Unhandled flow type: " + flowType);
        }
    }
}
