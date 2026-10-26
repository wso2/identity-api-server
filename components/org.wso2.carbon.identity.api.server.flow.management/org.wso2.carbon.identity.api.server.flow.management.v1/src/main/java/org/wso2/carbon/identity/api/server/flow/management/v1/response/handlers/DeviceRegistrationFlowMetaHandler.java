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

import org.wso2.carbon.identity.flow.mgt.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.END_USER_ATTRIBUTE_PROFILE;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.DEVICE_REGISTRATION_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.EMAIL_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.SMS_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.USER_RESOLVE_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.USERNAME_IDENTIFIER;

/**
 * Handler for managing the device registration flow meta information.
 * The device registration executor itself is not listed here; it is contributed by the flow engine, which
 * advertises it for this flow type.
 */
public class DeviceRegistrationFlowMetaHandler extends AbstractMetaResponseHandler {

    @Override
    public String getFlowType() {

        return String.valueOf(Constants.FlowTypes.DEVICE_REGISTRATION);
    }

    @Override
    public String getAttributeProfile() {

        return END_USER_ATTRIBUTE_PROFILE;
    }

    @Override
    public List<String> getRequiredInputFields() {

        return new ArrayList<>(Collections.singletonList(USERNAME_IDENTIFIER));
    }

    /**
     * The built in executors that can be used alongside device registration to identify and verify the user.
     */
    @Override
    protected List<String> getLegacyExecutorBaseline() {

        return new ArrayList<>(Arrays.asList(USER_RESOLVE_EXECUTOR, EMAIL_OTP_EXECUTOR, SMS_OTP_EXECUTOR));
    }

    /**
     * A valid device registration flow must contain the device registration executor.
     */
    @Override
    public List<Set<String>> getRequiredExecutorGroups() {

        return Collections.singletonList(Collections.singleton(DEVICE_REGISTRATION_EXECUTOR));
    }
}
