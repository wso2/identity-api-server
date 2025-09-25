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

package org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers;

import org.wso2.carbon.identity.api.server.flow.management.v1.FlowMetaResponse;
import org.wso2.carbon.identity.flow.mgt.Constants;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.APPLE_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.FACEBOOK_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.FIDO2_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.GITHUB_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.GOOGLE_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.OFFICE365_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.OPENID_CONNECT_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.SELF_REGISTRATION_ATTRIBUTE_PROFILE;

/**
 * Handler for managing the registration flow meta information.
 * This class extends the AbstractMetaResponseHandler to provide specific
 * implementations for registration flow.
 */
public class RegistrationFlowMetaHandler extends AbstractMetaResponseHandler {

    @Override
    public String getFlowType() {

        return String.valueOf(Constants.FlowTypes.REGISTRATION);
    }

    @Override
    public String getAttributeProfile() {

        return SELF_REGISTRATION_ATTRIBUTE_PROFILE;
    }

    @Override
    public List<String> getRequiredInputFields() {

        return new ArrayList<>();
    }

    @Override
    public FlowMetaResponse createResponse() {

        return super.createResponse();
    }

    @Override
    public List<String> getSupportedExecutors() {

        List<String> supportedExecutors = super.getSupportedExecutors();
        supportedExecutors.add(OPENID_CONNECT_EXECUTOR);
        supportedExecutors.add(GOOGLE_EXECUTOR);
        supportedExecutors.add(FACEBOOK_EXECUTOR);
        supportedExecutors.add(OFFICE365_EXECUTOR);
        supportedExecutors.add(APPLE_EXECUTOR);
        supportedExecutors.add(GITHUB_EXECUTOR);
        supportedExecutors.add(FIDO2_EXECUTOR);
        return supportedExecutors;
    }
}
