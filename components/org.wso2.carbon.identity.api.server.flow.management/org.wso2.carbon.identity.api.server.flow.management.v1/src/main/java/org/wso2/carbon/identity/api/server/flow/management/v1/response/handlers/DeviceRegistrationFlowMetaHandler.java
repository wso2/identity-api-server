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

package org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers;

import org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants;
import org.wso2.carbon.identity.flow.mgt.Constants;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.DEVICE_REGISTRATION_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.EMAIL_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.SMS_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.USER_RESOLVE_EXECUTOR;

/**
 * Handler for managing the device registration flow meta information.
 */
public class DeviceRegistrationFlowMetaHandler extends AbstractMetaResponseHandler {

    @Override
    public String getFlowType() {

        return Constants.FlowTypes.DEVICE_REGISTRATION.getType();
    }

    @Override
    public String getAttributeProfile() {

        return FlowEndpointConstants.END_USER_ATTRIBUTE_PROFILE;
    }

    @Override
    public List<String> getRequiredInputFields() {

        List<String> fields = new ArrayList<>();
        fields.add(FlowEndpointConstants.USERNAME_IDENTIFIER);
        return fields;
    }

    @Override
    public List<String> getSupportedExecutors() {

        List<String> executors = new ArrayList<>();
        executors.add(DEVICE_REGISTRATION_EXECUTOR);
        executors.add(EMAIL_OTP_EXECUTOR);
        executors.add(SMS_OTP_EXECUTOR);
        executors.add(USER_RESOLVE_EXECUTOR);
        return executors;
    }
}
