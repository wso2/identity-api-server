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

package org.wso2.carbon.identity.api.server.flow.execution.v1.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.flow.execution.v1.FlowExecutionRequest;
import org.wso2.carbon.identity.api.server.flow.execution.v1.FlowExecutionResponse;
import org.wso2.carbon.identity.api.server.flow.execution.v1.utils.Utils;
import org.wso2.carbon.identity.flow.execution.engine.FlowExecutionService;
import org.wso2.carbon.identity.flow.execution.engine.exception.FlowEngineException;
import org.wso2.carbon.identity.flow.execution.engine.model.FlowExecutionStep;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for flow execution.
 */
public class FlowExecutionServiceCore {

    private static final Log LOG = LogFactory.getLog(FlowExecutionServiceCore.class);
    private final FlowExecutionService flowExecutionService;

    public FlowExecutionServiceCore(FlowExecutionService flowExecutionService) {

        this.flowExecutionService = flowExecutionService;
        if (LOG.isDebugEnabled()) {
            LOG.debug("FlowExecutionServiceCore initialized with FlowExecutionService.");
        }
    }

    /**
     * Process flow execution.
     *
     * @param flowExecutionRequest FlowExecutionRequest.
     * @return FlowExecutionResponse.
     */
    public FlowExecutionResponse processFlowExecution(FlowExecutionRequest flowExecutionRequest) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Processing flow execution for tenant: " + tenantDomain + 
                    ", flowType: " + (flowExecutionRequest != null ? flowExecutionRequest.getFlowType() : "null"));
        }

        if (flowExecutionRequest == null) {
            LOG.warn("FlowExecutionRequest is null. Cannot process flow execution.");
            throw new IllegalArgumentException("FlowExecutionRequest cannot be null");
        }

        try {
            if (StringUtils.isBlank(flowExecutionRequest.getFlowId())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Flow ID is blank, validating flow initiation.");
                }
                Utils.validateFlowInitiation(flowExecutionRequest);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> inputMap = Optional.ofNullable(flowExecutionRequest.getInputs())
                    .map(inputs -> objectMapper.convertValue(inputs, new MapTypeReference()))
                    .orElse(Collections.emptyMap());
            FlowExecutionStep flowExecutionStep = flowExecutionService.executeFlow(tenantDomain,
                    flowExecutionRequest.getApplicationId(), flowExecutionRequest.getFlowId(),
                    flowExecutionRequest.getActionId(), flowExecutionRequest.getFlowType(), inputMap);
            FlowExecutionResponse flowExecutionResponse = new FlowExecutionResponse();

            if (flowExecutionStep == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Flow execution step is null, returning empty response.");
                }
                return flowExecutionResponse;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Flow execution completed successfully for flowId: " + flowExecutionStep.getFlowId() +
                        ", flowStatus: " + flowExecutionStep.getFlowStatus());
            }
            return flowExecutionResponse
                    .flowId(flowExecutionStep.getFlowId())
                    .flowType(flowExecutionStep.getFlowType())
                    .flowStatus(flowExecutionStep.getFlowStatus())
                    .type(FlowExecutionResponse.TypeEnum.valueOf(flowExecutionStep.getStepType()))
                    .data(Utils.convertToData(flowExecutionStep.getData(), flowExecutionStep.getStepType()));
        } catch (FlowEngineException e) {
            throw Utils.handleFlowException(e, tenantDomain);
        }
    }

    /**
     * Static inner class for TypeReference.
     */
    private static class MapTypeReference extends TypeReference<Map<String, String>> {

    }
}
