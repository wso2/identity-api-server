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

package org.wso2.carbon.identity.api.server.flow.management.v1.utils;

import org.wso2.carbon.identity.api.server.flow.management.v1.FlowInputs;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowRequestValueInput;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowVariableInput;
import org.wso2.carbon.identity.flow.mgt.model.FlowInputDTO;
import org.wso2.carbon.identity.flow.mgt.model.FlowInputDataType;
import org.wso2.carbon.identity.flow.mgt.model.FlowInputsDTO;
import org.wso2.carbon.identity.flow.mgt.model.FlowVariableInputDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts the headers, query parameters and variables a flow defines between the API model and the flow management
 * model. The data types share their names in both, and an initial value passes through as it is, to be checked
 * against its type when the flow is saved.
 */
public class FlowInputMapper {

    private FlowInputMapper() {

    }

    /**
     * Convert the inputs of a flow from the API model to the flow management model.
     *
     * @param inputs Inputs in the API model.
     * @return Inputs in the flow management model, or null when none are given.
     */
    public static FlowInputsDTO toFlowInputsDTO(FlowInputs inputs) {

        if (inputs == null) {
            return null;
        }
        FlowInputsDTO inputsDTO = new FlowInputsDTO();
        if (inputs.getHeaders() != null) {
            for (FlowRequestValueInput header : inputs.getHeaders()) {
                inputsDTO.getHeaders().add(toFlowInputDTO(header));
            }
        }
        if (inputs.getQueryParams() != null) {
            for (FlowRequestValueInput queryParam : inputs.getQueryParams()) {
                inputsDTO.getQueryParams().add(toFlowInputDTO(queryParam));
            }
        }
        if (inputs.getVariables() != null) {
            for (FlowVariableInput variable : inputs.getVariables()) {
                inputsDTO.getVariables().add(toFlowVariableInputDTO(variable));
            }
        }
        return inputsDTO;
    }

    /**
     * Convert the inputs of a flow from the flow management model to the API model, in the order they were
     * defined.
     *
     * @param inputsDTO Inputs in the flow management model.
     * @return Inputs in the API model, or null when the flow defines nothing.
     */
    public static FlowInputs toFlowInputs(FlowInputsDTO inputsDTO) {

        if (inputsDTO == null || inputsDTO.isEmpty()) {
            return null;
        }
        List<FlowRequestValueInput> headers = new ArrayList<>();
        for (FlowInputDTO header : inputsDTO.getHeaders()) {
            headers.add(toFlowRequestValueInput(header));
        }
        List<FlowRequestValueInput> queryParams = new ArrayList<>();
        for (FlowInputDTO queryParam : inputsDTO.getQueryParams()) {
            queryParams.add(toFlowRequestValueInput(queryParam));
        }
        List<FlowVariableInput> variables = new ArrayList<>();
        for (FlowVariableInputDTO variable : inputsDTO.getVariables()) {
            variables.add(toFlowVariableInput(variable));
        }
        return new FlowInputs()
                .headers(headers)
                .queryParams(queryParams)
                .variables(variables);
    }

    private static FlowInputDTO toFlowInputDTO(FlowRequestValueInput input) {

        if (input == null) {
            return null;
        }
        String dataType = input.getDataType() == null ? null : input.getDataType().value();
        return new FlowInputDTO(input.getName(), FlowInputDataType.fromName(dataType));
    }

    private static FlowVariableInputDTO toFlowVariableInputDTO(FlowVariableInput input) {

        if (input == null) {
            return null;
        }
        String dataType = input.getDataType() == null ? null : input.getDataType().value();
        FlowVariableInputDTO variableDTO =
                new FlowVariableInputDTO(input.getName(), FlowInputDataType.fromName(dataType));
        variableDTO.setValue(input.getValue());
        variableDTO.setClientWritable(Boolean.TRUE.equals(input.getClientWritable()));
        return variableDTO;
    }

    private static FlowRequestValueInput toFlowRequestValueInput(FlowInputDTO inputDTO) {

        return new FlowRequestValueInput()
                .name(inputDTO.getName())
                .dataType(FlowRequestValueInput.DataTypeEnum.fromValue(inputDTO.getDataType().name()));
    }

    private static FlowVariableInput toFlowVariableInput(FlowVariableInputDTO variableDTO) {

        return new FlowVariableInput()
                .name(variableDTO.getName())
                .dataType(FlowVariableInput.DataTypeEnum.fromValue(variableDTO.getDataType().name()))
                .value(variableDTO.getValue())
                .clientWritable(variableDTO.isClientWritable());
    }
}
