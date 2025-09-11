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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.flow.management.common.FlowMgtServiceHolder;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowGenerateRequest;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowGenerateResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowGenerateResult;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowGenerateResultData;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowGenerateStatus;
import org.wso2.carbon.identity.api.server.flow.management.v1.Step;
import org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtFrameworkException;
import org.wso2.carbon.identity.flow.mgt.model.FlowGenerationRequestDTO;
import org.wso2.carbon.identity.flow.mgt.model.FlowGenerationResponseDTO;
import org.wso2.carbon.identity.flow.mgt.model.FlowGenerationResultDTO;
import org.wso2.carbon.identity.flow.mgt.model.FlowGenerationStatusDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * FlowAIServiceCore provides methods to generate authentication sequences using AI based on user query.
 */
public class FlowAIServiceCore {

    private static final Log log = LogFactory.getLog(FlowAIServiceCore.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Generate authentication sequence using login flow AI. Here we generate the authentication sequence based on the
     * available user claims metadata and authenticators.
     *
     * @param flowGenerateRequest FlowGenerateRequest containing the flow type.
     * @return LoginFlowGenerateResponse.
     */
    public FlowGenerateResponse generateFlow(FlowGenerateRequest flowGenerateRequest) {

        try {
            FlowGenerationRequestDTO requestDTO = new FlowGenerationRequestDTO.Builder()
                    .flowType(flowGenerateRequest.getFlowType())
                    .userQuery(flowGenerateRequest.getUserQuery())
                    .build();
            if (log.isDebugEnabled()) {
                log.debug("Generating flow for flow type: " + requestDTO.getFlowType() +
                        " with user query: " + requestDTO.getUserQuery());
            }
            FlowGenerationResponseDTO responseDTO = FlowMgtServiceHolder.getFlowAIService().generateFlow(requestDTO);
            return new FlowGenerateResponse().operationId(responseDTO.getOperationId());
        } catch (FlowMgtFrameworkException e) {
            throw Utils.handleFlowMgtException(e);
        }
    }

    /**
     * Get the status of the flow generation operation.
     *
     * @param operationId The operation ID of the flow generation request.
     * @return FlowGenerateStatus containing the status of the flow generation operation.
     */
    public FlowGenerateStatus getFlowGenerationStatus(String operationId) {

        if (log.isDebugEnabled()) {
            log.debug("Getting flow generation status for operation ID: " + operationId);
        }
        try {
            FlowGenerationStatusDTO responseDTO = FlowMgtServiceHolder.getFlowAIService()
                    .getFlowGenerationStatus(operationId);
            return new FlowGenerateStatus()
                    .optimizingQuery(responseDTO.isOptimizingQuery())
                    .fetchingSamples(responseDTO.isFetchingSamples())
                    .generatingFlow(responseDTO.isGeneratingFlow())
                    .completed(responseDTO.isCompleted());
        } catch (FlowMgtFrameworkException e) {
            log.error("Error getting flow generation status for operation ID: " + operationId, e);
            throw Utils.handleFlowMgtException(e);
        }
    }

    /**
     * Get the generated flow result for the given operation ID.
     *
     * @param operationId The operation ID of the flow generation request.
     * @return FlowResponse containing the generated flow result.
     */
    public FlowGenerateResult getFlowGenerationResult(String operationId) {

        try {
            FlowGenerationResultDTO resultDTO = FlowMgtServiceHolder.getFlowAIService()
                    .getFlowGenerationResult(operationId);
            if (log.isDebugEnabled()) {
                log.debug("Retrieving flow generation result for operation ID: " + operationId);
            }
            FlowGenerateResult flowGenerateResult = new FlowGenerateResult();
            if (resultDTO != null) {
                flowGenerateResult.setStatus(resultDTO.getStatus());
                FlowGenerateResultData flowGenerateResultData = new FlowGenerateResultData();
                if (log.isDebugEnabled()) {
                    log.debug("Flow generation result status: " + flowGenerateResult.getStatus() +
                            "for operation ID: " + operationId);
                }
                if (FlowEndpointConstants.FlowGeneration.STATUS_FAILED.equals(flowGenerateResult.getStatus())) {
                    flowGenerateResultData.setError((String) resultDTO.getData()
                            .get(FlowEndpointConstants.FlowGeneration.ERROR));
                } else {
                    flowGenerateResultData.setSteps(convertToSteps(resultDTO.getData()
                            .get(FlowEndpointConstants.FlowGeneration.STEPS)));
                }
                flowGenerateResult.setData(flowGenerateResultData);
            }
            return flowGenerateResult;
        } catch (FlowMgtFrameworkException e) {
            log.error("Error retrieving flow generation result for operation ID: " + operationId, e);
            throw Utils.handleFlowMgtException(e);
        }
    }

    private List<Step> convertToSteps(Object steps) {

        List<Step> stepList = new ArrayList<>();
        if (steps instanceof List) {
            for (Object step : (List<?>) steps) {
                Step convertedStep = objectMapper.convertValue(step, Step.class);
                stepList.add(convertedStep);
            }
        }
        return stepList;
    }
}

