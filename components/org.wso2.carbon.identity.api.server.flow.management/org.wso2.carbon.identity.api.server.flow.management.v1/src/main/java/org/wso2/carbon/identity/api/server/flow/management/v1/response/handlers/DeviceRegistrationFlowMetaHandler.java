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
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.END_USER_ATTRIBUTE_PROFILE;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.DEVICE_REGISTRATION_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.PASSWORD_PROVISIONING_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.USER_PROVISIONING_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.USER_VERIFICATION_EXECUTOR;
import static org.wso2.carbon.identity.flow.mgt.Constants.FlowTypes.DEVICE_REGISTRATION;

/**
 * Handler for the device registration flow meta information, where a signed-in user registers a device.
 */
public class DeviceRegistrationFlowMetaHandler extends AbstractMetaResponseHandler {

    @Override
    public String getFlowType() {

        return String.valueOf(DEVICE_REGISTRATION);
    }

    @Override
    public String getAttributeProfile() {

        return END_USER_ATTRIBUTE_PROFILE;
    }

    @Override
    public List<String> getRequiredInputFields() {

        return new ArrayList<>();
    }

    /**
     * The user verification executor, a step-up factor and the device registration executor must be present in a
     * valid device registration flow.
     */
    @Override
    public List<Set<String>> getRequiredExecutorGroups() {

        Set<String> stepUpFactors = new HashSet<>(FlowEndpointConstants.LegacyExecutors.RECOVERY_FACTORS);
        stepUpFactors.addAll(Utils.filterAuthenticationExecutors(getSupportedExtensionExecutors()));
        return Arrays.asList(
                Collections.singleton(USER_VERIFICATION_EXECUTOR),
                stepUpFactors,
                Collections.singleton(DEVICE_REGISTRATION_EXECUTOR));
    }

    /**
     * The user already exists, so the user provisioning and password steps are not supported.
     */
    @Override
    protected List<String> getLegacyExecutorBaseline() {

        List<String> legacyExecutors = super.getLegacyExecutorBaseline();
        legacyExecutors.remove(PASSWORD_PROVISIONING_EXECUTOR);
        legacyExecutors.remove(USER_PROVISIONING_EXECUTOR);
        return legacyExecutors;
    }
}
