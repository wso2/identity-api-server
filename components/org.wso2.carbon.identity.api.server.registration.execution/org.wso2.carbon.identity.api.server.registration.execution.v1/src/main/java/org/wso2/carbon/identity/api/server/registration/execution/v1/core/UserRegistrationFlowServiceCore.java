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

package org.wso2.carbon.identity.api.server.registration.execution.v1.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.registration.execution.v1.RegistrationSubmissionRequest;
import org.wso2.carbon.identity.api.server.registration.execution.v1.RegistrationSubmissionResponse;
import org.wso2.carbon.identity.api.server.registration.execution.v1.utils.Utils;
import org.wso2.carbon.identity.user.registration.engine.UserRegistrationFlowService;
import org.wso2.carbon.identity.user.registration.engine.exception.RegistrationEngineException;
import org.wso2.carbon.identity.user.registration.engine.model.RegistrationStep;

import java.util.Map;

/**
 * Service class for registration management.
 */
public class UserRegistrationFlowServiceCore {

    private final UserRegistrationFlowService userRegistrationMgtService;

    public UserRegistrationFlowServiceCore(UserRegistrationFlowService userRegistrationMgtService) {

        this.userRegistrationMgtService = userRegistrationMgtService;
    }

    /**
     * Initiate user registration.
     *
     * @return RegistrationSubmissionResponse.
     */
    public RegistrationSubmissionResponse initiateUserRegistration() {

        try {
            RegistrationStep registrationStep = userRegistrationMgtService.initiateDefaultRegistrationFlow(
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            RegistrationSubmissionResponse registrationSubmissionResponse = new RegistrationSubmissionResponse();
            if (registrationStep == null) {
                return registrationSubmissionResponse;
            }
            return registrationSubmissionResponse
                    .flowId(registrationStep.getFlowId())
                    .flowStatus(registrationStep.getFlowStatus())
                    .type(RegistrationSubmissionResponse.TypeEnum.valueOf(registrationStep.getStepType()))
                    .data(Utils.convertToData(registrationStep.getData(), registrationStep.getStepType()));
        } catch (RegistrationEngineException e) {
            throw Utils.handleRegistrationException(e);
        }
    }

    /**
     * Process user registration.
     *
     * @param registrationSubmissionRequest RegistrationSubmissionRequest.
     * @return RegistrationSubmissionResponse.
     */
    public RegistrationSubmissionResponse processUserRegistration(RegistrationSubmissionRequest
                                                                          registrationSubmissionRequest) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> inpputMap = objectMapper.convertValue(registrationSubmissionRequest.getInputs(),
                    new MapTypeReference());
            RegistrationStep registrationStep = userRegistrationMgtService
                    .continueFlow(registrationSubmissionRequest.getFlowId(),
                            registrationSubmissionRequest.getActionId(), inpputMap);
            RegistrationSubmissionResponse registrationSubmissionResponse = new RegistrationSubmissionResponse();
            if (registrationStep == null) {
                return registrationSubmissionResponse;
            }
            return registrationSubmissionResponse.flowId(registrationStep.getFlowId())
                    .flowStatus(registrationStep.getFlowStatus())
                    .type(RegistrationSubmissionResponse.TypeEnum.valueOf(registrationStep.getStepType()))
                    .data(Utils.convertToData(registrationStep.getData(), registrationStep.getStepType()));
        } catch (RegistrationEngineException e) {
            throw Utils.handleRegistrationException(e);
        }
    }

    /**
     * Static inner class for TypeReference.
     */
    private static class MapTypeReference extends TypeReference<Map<String, String>> {

    }
}
