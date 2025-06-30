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

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowMetaResponseConnectorConfigs;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.EMAIL_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FlowType.REGISTRATION;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.PASSWORD_IDENTIFIER;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.PASSWORD_ONBOARD_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.PASSWORD_PROVISIONING_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.SELF_REGISTRATION_ATTRIBUTE_PROFILE;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.SMS_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.USERNAME_IDENTIFIER;
import static org.wso2.carbon.identity.recovery.IdentityRecoveryConstants.ConnectorConfig.ENABLE_SELF_SIGNUP;
import static org.wso2.carbon.identity.recovery.IdentityRecoveryConstants.ConnectorConfig.PASSWORD_RECOVERY_EMAIL_LINK_ENABLE;

/**
 * Handler for managing the registration flow meta information.
 * This class extends the AbstractMetaResponseHandler to provide specific
 * implementations for registration flow.
 */
public class RegistrationFlowMetaHandler extends AbstractMetaResponseHandler {

    @Override
    public String getFlowType() {

        return String.valueOf(REGISTRATION);
    }

    @Override
    public String getAttributeProfile() {

        return SELF_REGISTRATION_ATTRIBUTE_PROFILE;
    }

    @Override
    public FlowMetaResponseConnectorConfigs getConnectorConfigs() {

        Utils utils = new Utils();
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        FlowMetaResponseConnectorConfigs connectorConfigs = new FlowMetaResponseConnectorConfigs();
        connectorConfigs.setSelfRegistrationEnabled(
                    utils.isFlowConfigEnabled(tenantDomain, ENABLE_SELF_SIGNUP));
        connectorConfigs.setMultiAttributeLoginEnabled(
                utils.isFlowConfigEnabled(tenantDomain, PASSWORD_RECOVERY_EMAIL_LINK_ENABLE));

        return connectorConfigs;
    }

    @Override
    public List<String> getRequiredInputFields() {

        ArrayList<String> requiredInputFields = new ArrayList<>();
        requiredInputFields.add(USERNAME_IDENTIFIER);
        requiredInputFields.add(PASSWORD_IDENTIFIER);
        return requiredInputFields;
    }

    public List<String> getSupportedExecutors() {

        ArrayList<String> supportedExecutors = new ArrayList<>();
        supportedExecutors.add(PASSWORD_PROVISIONING_EXECUTOR);
        supportedExecutors.add(PASSWORD_ONBOARD_EXECUTOR);
        supportedExecutors.add(EMAIL_OTP_EXECUTOR);
        supportedExecutors.add(SMS_OTP_EXECUTOR);
        return supportedExecutors;
    }

}
