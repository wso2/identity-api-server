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

package org.wso2.carbon.identity.api.server.registration.execution.v1.impl;

import org.wso2.carbon.identity.api.server.registration.execution.v1.RegistrationApiService;
import org.wso2.carbon.identity.api.server.registration.execution.v1.RegistrationExecutionRequest;
import org.wso2.carbon.identity.api.server.registration.execution.v1.core.UserRegistrationFlowServiceCore;
import org.wso2.carbon.identity.api.server.registration.execution.v1.factories.UserRegistrationFlowServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of the Registration API.
 */
public class RegistrationApiServiceImpl implements RegistrationApiService {

    private final UserRegistrationFlowServiceCore userRegistrationFlowServiceCore;

    public RegistrationApiServiceImpl() {

        this.userRegistrationFlowServiceCore = UserRegistrationFlowServiceFactory.getRegistrationFlowMgtService();
    }

    @Override
    public Response registrationExecutePost(RegistrationExecutionRequest registrationExecutionRequest) {

        return Response.ok()
                .entity(userRegistrationFlowServiceCore.processUserRegistrationExecution(registrationExecutionRequest))
                .build();
    }
}
