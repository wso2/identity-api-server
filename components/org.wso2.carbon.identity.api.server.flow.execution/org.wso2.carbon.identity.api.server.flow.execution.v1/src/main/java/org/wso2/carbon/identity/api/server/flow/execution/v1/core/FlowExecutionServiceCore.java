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
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.flow.execution.v1.FlowExecutionRequest;
import org.wso2.carbon.identity.api.server.flow.execution.v1.FlowExecutionResponse;
import org.wso2.carbon.identity.api.server.flow.execution.v1.utils.Utils;
import org.wso2.carbon.identity.user.registration.engine.UserRegistrationFlowService;
import org.wso2.carbon.identity.user.registration.engine.exception.RegistrationEngineException;
import org.wso2.carbon.identity.user.registration.engine.model.RegistrationStep;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for flow execution.
 */
public class FlowExecutionServiceCore {

    private final UserRegistrationFlowService userRegistrationMgtService;

    public FlowExecutionServiceCore(UserRegistrationFlowService userRegistrationMgtService) {

        this.userRegistrationMgtService = userRegistrationMgtService;
    }

    /**
     * Process flow execution.
     *
     * @param flowExecutionRequest FlowExecutionRequest.
     * @return FlowExecutionResponse.
     */
    public FlowExecutionResponse processFlowExecution(FlowExecutionRequest
                                                                                  flowExecutionRequest) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        try {
            // Check whether the self registration and dynamic registration portal is enabled.
            Utils.isSelfRegistrationEnabled(tenantDomain);
            Utils.isDynamicRegistrationPortalEnabled(tenantDomain);
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, String> inputMap = Optional.ofNullable(flowExecutionRequest.getInputs())
                    .map(inputs -> objectMapper.convertValue(inputs, new MapTypeReference()))
                    .orElse(Collections.emptyMap());
            RegistrationStep registrationStep = userRegistrationMgtService.handleRegistration(tenantDomain,
                    flowExecutionRequest.getApplicationId(), flowExecutionRequest.getCallbackUrl(),
                    flowExecutionRequest.getFlowId(), flowExecutionRequest.getActionId(), inputMap);
            FlowExecutionResponse flowExecutionResponse = new FlowExecutionResponse();

            if (registrationStep == null) {
                return flowExecutionResponse;
            }

            return flowExecutionResponse
                    .flowId(registrationStep.getFlowId())
                    .flowStatus(registrationStep.getFlowStatus())
                    .type(FlowExecutionResponse.TypeEnum.valueOf(registrationStep.getStepType()))
                    .data(Utils.convertToData(registrationStep.getData(), registrationStep.getStepType()));
        } catch (RegistrationEngineException e) {
            throw Utils.handleFlowException(e, tenantDomain);
        }
    }

    /**
     * Static inner class for TypeReference.
     */
    private static class MapTypeReference extends TypeReference<Map<String, String>> {

    }
}
