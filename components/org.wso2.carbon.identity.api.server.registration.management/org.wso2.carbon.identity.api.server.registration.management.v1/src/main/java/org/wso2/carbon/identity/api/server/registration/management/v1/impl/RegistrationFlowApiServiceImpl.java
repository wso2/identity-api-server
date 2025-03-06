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

package org.wso2.carbon.identity.api.server.registration.management.v1.impl;

import org.wso2.carbon.identity.api.server.registration.management.v1.RegistrationFlowApiService;
import org.wso2.carbon.identity.api.server.registration.management.v1.RegistrationFlowRequest;
import org.wso2.carbon.identity.api.server.registration.management.v1.RegistrationFlowResponse;
import org.wso2.carbon.identity.api.server.registration.management.v1.core.ServerRegistrationFlowMgtService;
import org.wso2.carbon.identity.api.server.registration.management.v1.factories.ServerRegistrationFlowMgtServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of the Registration Flow API.
 */
public class RegistrationFlowApiServiceImpl implements RegistrationFlowApiService {

    ServerRegistrationFlowMgtService registrationFlowMgtService;

    public RegistrationFlowApiServiceImpl() {

        try {
            this.registrationFlowMgtService = ServerRegistrationFlowMgtServiceFactory.getRegistrationFlowMgtService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating registration flow management service.", e);
        }
    }

    @Override
    public Response getRegistrationFlow() {

        RegistrationFlowResponse registrationFlow = registrationFlowMgtService.getRegistrationFlow();
        return Response.ok().entity(registrationFlow).build();
    }

    @Override
    public Response updateRegistrationFlow(RegistrationFlowRequest registrationFlowRequest) {

        registrationFlowMgtService.updateRegistrationFlow(registrationFlowRequest);
        return Response.ok().build();
    }
}
