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

package org.wso2.carbon.identity.api.server.registration.v1.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.registration.v1.Action;
import org.wso2.carbon.identity.api.server.registration.v1.Position;
import org.wso2.carbon.identity.api.server.registration.v1.RegistrationFlowRequest;
import org.wso2.carbon.identity.api.server.registration.v1.RegistrationFlowResponse;
import org.wso2.carbon.identity.api.server.registration.v1.Size;
import org.wso2.carbon.identity.api.server.registration.v1.Step;
import org.wso2.carbon.identity.api.server.registration.v1.utils.Utils;
import org.wso2.carbon.identity.user.registration.mgt.RegistrationFlowMgtService;
import org.wso2.carbon.identity.user.registration.mgt.model.RegistrationFlowDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.StepDTO;

import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * Service class for registration flow management.
 */
public class ServerRegistrationFlowMgtService {

    private final RegistrationFlowMgtService registrationFlowMgtService;

    public ServerRegistrationFlowMgtService(RegistrationFlowMgtService registrationFlowMgtService) {

        this.registrationFlowMgtService = registrationFlowMgtService;
    }

    public RegistrationFlowResponse getRegistrationFlow() {

        RegistrationFlowDTO registrationFlowDTO = registrationFlowMgtService
                .getRegistrationFlow(PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId());
        RegistrationFlowResponse registrationFlowResponse = new RegistrationFlowResponse();
        registrationFlowResponse.steps(registrationFlowDTO.getSteps().stream().map(Utils::convertToStep)
                .collect(Collectors.toList()));
        return registrationFlowResponse;
    }

    public String updateRegistrationFlow(RegistrationFlowRequest registrationFlowRequest) {

        ObjectMapper mapper = new ObjectMapper();
        String registrationFlowJson = null;
        return null;
    }
}
