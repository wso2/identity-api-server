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

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.APPLE_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.CONFIRMATION_CODE_VALIDATION_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.FACEBOOK_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.FIDO2_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.GITHUB_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.GOOGLE_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.OFFICE365_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.OPENID_CONNECT_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.USER_RESOLVE_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.SELF_REGISTRATION_ATTRIBUTE_PROFILE;
import static org.wso2.carbon.identity.flow.mgt.Constants.FlowTypes.INVITED_USER_REGISTRATION;

/**
 * Handler for managing the ask password flow meta information.
 * This class extends the AbstractMetaResponseHandler to provide specific
 * implementations for ask password flow.
 */
public class AskPasswordFlowMetaHandler extends AbstractMetaResponseHandler {

    @Override
    public String getFlowType() {

        return String.valueOf(INVITED_USER_REGISTRATION);
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
    public List<String> getSupportedExecutors() {

        List<String> supportedExecutors = new ArrayList<>(super.getSupportedExecutors());
        supportedExecutors.add(USER_RESOLVE_EXECUTOR);
        supportedExecutors.add(CONFIRMATION_CODE_VALIDATION_EXECUTOR);
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
