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

package org.wso2.carbon.identity.api.server.registration.management.v1.core;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.registration.management.v1.RegistrationFlowRequest;
import org.wso2.carbon.identity.api.server.registration.management.v1.RegistrationFlowResponse;
import org.wso2.carbon.identity.api.server.registration.management.v1.utils.Utils;
import org.wso2.carbon.identity.user.registration.mgt.RegistrationFlowMgtService;
import org.wso2.carbon.identity.user.registration.mgt.exception.RegistrationFrameworkException;
import org.wso2.carbon.identity.user.registration.mgt.model.RegistrationFlowDTO;

import java.util.stream.Collectors;

/**
 * Service class for registration flow management.
 */
public class ServerRegistrationFlowMgtService {

    private final RegistrationFlowMgtService registrationFlowMgtService;

    public ServerRegistrationFlowMgtService(RegistrationFlowMgtService registrationFlowMgtService) {

        this.registrationFlowMgtService = registrationFlowMgtService;
    }

    /**
     * Retrieve the registration flow.
     *
     * @return RegistrationFlowResponse.
     */
    public RegistrationFlowResponse getRegistrationFlow() {

        RegistrationFlowDTO registrationFlowDTO;
        try {
            registrationFlowDTO = registrationFlowMgtService
                    .getRegistrationFlow(PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId());
            RegistrationFlowResponse registrationFlowResponse = new RegistrationFlowResponse();
            if (registrationFlowDTO == null) {
                return registrationFlowResponse;
            }
            registrationFlowResponse.steps(registrationFlowDTO.getSteps().stream().map(Utils::convertToStep)
                    .collect(Collectors.toList()));
            return registrationFlowResponse;
        } catch (RegistrationFrameworkException e) {
            throw Utils.handleRegistrationException(e);
        }
    }

    /**
     * Update the registration flow.
     *
     * @param registrationFlowRequest RegistrationFlowRequest.
     */
    public void updateRegistrationFlow(RegistrationFlowRequest registrationFlowRequest) {

        try {
            RegistrationFlowDTO registrationFlowDTO = new RegistrationFlowDTO();
            registrationFlowDTO.setSteps(registrationFlowRequest.getSteps().stream().map(Utils::convertToStepDTO)
                    .collect(Collectors.toList()));
            registrationFlowMgtService.updateDefaultRegistrationFlow(registrationFlowDTO,
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId());
        } catch (RegistrationFrameworkException e) {
            throw Utils.handleRegistrationException(e);
        }
    }
}
