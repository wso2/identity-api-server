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

import org.wso2.carbon.identity.api.server.flow.management.v1.FlowHeader;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowRuntimeValue;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowVariables;
import org.wso2.carbon.identity.flow.mgt.model.FlowRuntimeValueDTO;
import org.wso2.carbon.identity.flow.mgt.model.FlowVariableDTO;
import org.wso2.carbon.identity.flow.mgt.model.FlowVariableDataType;
import org.wso2.carbon.identity.flow.mgt.model.FlowVariablesDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts the headers and runtime values a flow defines between the API model and the flow management model. The
 * data types share their names in both, and an initial value passes through as it is, to be checked against its type
 * when the flow is saved.
 */
public class FlowVariableMapper {

    private FlowVariableMapper() {

    }

    /**
     * Convert the variables of a flow from the API model to the flow management model.
     *
     * @param variables Variables in the API model.
     * @return Variables in the flow management model, or null when none are given.
     */
    public static FlowVariablesDTO toFlowVariablesDTO(FlowVariables variables) {

        if (variables == null) {
            return null;
        }
        FlowVariablesDTO variablesDTO = new FlowVariablesDTO();
        if (variables.getHeaders() != null) {
            for (FlowHeader header : variables.getHeaders()) {
                variablesDTO.getHeaders().add(toFlowVariableDTO(header));
            }
        }
        if (variables.getRuntimeValues() != null) {
            for (FlowRuntimeValue runtimeValue : variables.getRuntimeValues()) {
                variablesDTO.getRuntimeValues().add(toFlowRuntimeValueDTO(runtimeValue));
            }
        }
        return variablesDTO;
    }

    /**
     * Convert the variables of a flow from the flow management model to the API model, in the order they were
     * defined.
     *
     * @param variablesDTO Variables in the flow management model.
     * @return Variables in the API model, or null when the flow defines nothing.
     */
    public static FlowVariables toFlowVariables(FlowVariablesDTO variablesDTO) {

        if (variablesDTO == null || variablesDTO.isEmpty()) {
            return null;
        }
        List<FlowHeader> headers = new ArrayList<>();
        for (FlowVariableDTO header : variablesDTO.getHeaders()) {
            headers.add(toFlowHeader(header));
        }
        List<FlowRuntimeValue> runtimeValues = new ArrayList<>();
        for (FlowRuntimeValueDTO runtimeValue : variablesDTO.getRuntimeValues()) {
            runtimeValues.add(toFlowRuntimeValue(runtimeValue));
        }
        return new FlowVariables()
                .headers(headers)
                .runtimeValues(runtimeValues);
    }

    private static FlowVariableDTO toFlowVariableDTO(FlowHeader header) {

        if (header == null) {
            return null;
        }
        String dataType = header.getDataType() == null ? null : header.getDataType().value();
        return new FlowVariableDTO(header.getName(), FlowVariableDataType.fromName(dataType));
    }

    private static FlowRuntimeValueDTO toFlowRuntimeValueDTO(FlowRuntimeValue runtimeValue) {

        if (runtimeValue == null) {
            return null;
        }
        String dataType = runtimeValue.getDataType() == null ? null : runtimeValue.getDataType().value();
        FlowRuntimeValueDTO runtimeValueDTO =
                new FlowRuntimeValueDTO(runtimeValue.getName(), FlowVariableDataType.fromName(dataType));
        runtimeValueDTO.setValue(runtimeValue.getValue());
        runtimeValueDTO.setClientWritable(Boolean.TRUE.equals(runtimeValue.getClientWritable()));
        return runtimeValueDTO;
    }

    private static FlowHeader toFlowHeader(FlowVariableDTO headerDTO) {

        return new FlowHeader()
                .name(headerDTO.getName())
                .dataType(FlowHeader.DataTypeEnum.fromValue(headerDTO.getDataType().name()));
    }

    private static FlowRuntimeValue toFlowRuntimeValue(FlowRuntimeValueDTO runtimeValueDTO) {

        return new FlowRuntimeValue()
                .name(runtimeValueDTO.getName())
                .dataType(FlowRuntimeValue.DataTypeEnum.fromValue(runtimeValueDTO.getDataType().name()))
                .value(runtimeValueDTO.getValue())
                .clientWritable(runtimeValueDTO.isClientWritable());
    }
}
