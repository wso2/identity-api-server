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
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.flow.execution.v1.FlowExecutionRequest;
import org.wso2.carbon.identity.api.server.flow.execution.v1.FlowExecutionResponse;
import org.wso2.carbon.identity.api.server.flow.execution.v1.utils.Utils;
import org.wso2.carbon.identity.flow.execution.engine.FlowExecutionService;
import org.wso2.carbon.identity.flow.execution.engine.exception.FlowEngineClientException;
import org.wso2.carbon.identity.flow.execution.engine.exception.FlowEngineException;
import org.wso2.carbon.identity.flow.execution.engine.model.FlowExecutionStep;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.wso2.carbon.identity.api.server.flow.execution.v1.constants.FlowExecutionEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_VARIABLES;
import static org.wso2.carbon.identity.api.server.flow.execution.v1.constants.FlowExecutionEndpointConstants.VARIABLES;

/**
 * Service class for flow execution.
 */
public class FlowExecutionServiceCore {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final FlowExecutionService flowExecutionService;

    public FlowExecutionServiceCore(FlowExecutionService flowExecutionService) {

        this.flowExecutionService = flowExecutionService;
    }

    /**
     * Process flow execution.
     *
     * @param flowExecutionRequest FlowExecutionRequest.
     * @return FlowExecutionResponse.
     */
    public FlowExecutionResponse processFlowExecution(FlowExecutionRequest flowExecutionRequest) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        try {
            if (StringUtils.isBlank(flowExecutionRequest.getFlowId())) {
                Utils.validateFlowInitiation(flowExecutionRequest);
            }

            Map<String, Object> inputs = Optional.ofNullable(flowExecutionRequest.getInputs())
                    .map(requestInputs -> OBJECT_MAPPER.convertValue(requestInputs, new ObjectMapTypeReference()))
                    .orElseGet(HashMap::new);
            Map<String, Object> variableInputs = takeVariableInputs(inputs);
            Map<String, String> inputMap = OBJECT_MAPPER.convertValue(inputs, new MapTypeReference());
            FlowExecutionStep flowExecutionStep = flowExecutionService.executeFlow(tenantDomain,
                    flowExecutionRequest.getApplicationId(), flowExecutionRequest.getFlowId(),
                    flowExecutionRequest.getActionId(), flowExecutionRequest.getFlowType(), inputMap,
                    variableInputs);
            FlowExecutionResponse flowExecutionResponse = new FlowExecutionResponse();

            if (flowExecutionStep == null) {
                return flowExecutionResponse;
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
     * Takes the variables object out of the inputs. Its values keep their JSON types, and the engine reads each for
     * the type of its variable.
     *
     * @param inputs Inputs of the request.
     * @return Values for the variables of the flow, by name.
     */
    private static Map<String, Object> takeVariableInputs(Map<String, Object> inputs) {

        Object variables = inputs.remove(VARIABLES);
        if (variables == null) {
            return Collections.emptyMap();
        }
        if (!(variables instanceof Map)) {
            throw Utils.handleFlowException(new FlowEngineClientException(
                    ERROR_CODE_INVALID_VARIABLES.getCode(),
                    ERROR_CODE_INVALID_VARIABLES.getMessage(),
                    ERROR_CODE_INVALID_VARIABLES.getDescription()));
        }
        return OBJECT_MAPPER.convertValue(variables, new ObjectMapTypeReference());
    }

    /**
     * Static inner class for TypeReference.
     */
    private static class MapTypeReference extends TypeReference<Map<String, String>> {

    }

    /**
     * Type reference for a JSON object whose values keep their JSON types.
     */
    private static class ObjectMapTypeReference extends TypeReference<Map<String, Object>> {

    }
}
