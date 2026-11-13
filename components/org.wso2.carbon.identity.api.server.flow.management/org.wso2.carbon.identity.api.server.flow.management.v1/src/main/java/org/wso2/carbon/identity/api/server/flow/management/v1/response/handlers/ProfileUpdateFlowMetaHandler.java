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
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.PASSWORD_PROVISIONING_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.USER_RESOLVE_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.USER_VERIFICATION_EXECUTOR;
import static org.wso2.carbon.identity.flow.mgt.Constants.FlowTypes.PROFILE_UPDATE;

/**
 * Handler for managing the profile update flow meta information.
 * This class extends the AbstractMetaResponseHandler to provide specific
 * implementations for profile update flow.
 */
public class ProfileUpdateFlowMetaHandler extends AbstractMetaResponseHandler {

    @Override
    public String getFlowType() {

        return String.valueOf(PROFILE_UPDATE);
    }

    @Override
    public String getAttributeProfile() {

        return END_USER_ATTRIBUTE_PROFILE;
    }

    @Override
    public List<String> getRequiredInputFields() {

        return new ArrayList<>();
    }

    @Override
    public List<String> getSupportedExecutors() {

        List<String> supportedExecutors = new ArrayList<>(super.getSupportedExecutors());
        supportedExecutors.add(USER_RESOLVE_EXECUTOR);
        return supportedExecutors;
    }

    /**
     * The user verification or the user resolve executor must be present in a valid profile update flow.
     */
    @Override
    public List<Set<String>> getRequiredExecutorGroups() {

        return Collections.singletonList(
                new HashSet<>(Arrays.asList(USER_VERIFICATION_EXECUTOR, USER_RESOLVE_EXECUTOR)));
    }

    /**
     * At least one step-up factor must also be present when the user is resolved in the flow.
     */
    @Override
    public List<Set<String>> getRequiredExecutorGroups(Set<String> executors) {

        List<Set<String>> requiredExecutorGroups = new ArrayList<>(getRequiredExecutorGroups());
        if (executors.contains(USER_RESOLVE_EXECUTOR)) {
            Set<String> stepUpFactors = new HashSet<>(FlowEndpointConstants.LegacyExecutors.RECOVERY_FACTORS);
            stepUpFactors.addAll(Utils.filterAuthenticationExecutors(getSupportedExtensionExecutors()));
            requiredExecutorGroups.add(stepUpFactors);
        }
        return requiredExecutorGroups;
    }

    @Override
    protected List<String> getLegacyExecutorBaseline() {

        List<String> legacyExecutors = super.getLegacyExecutorBaseline();
        legacyExecutors.remove(PASSWORD_PROVISIONING_EXECUTOR);
        return legacyExecutors;
    }
}
