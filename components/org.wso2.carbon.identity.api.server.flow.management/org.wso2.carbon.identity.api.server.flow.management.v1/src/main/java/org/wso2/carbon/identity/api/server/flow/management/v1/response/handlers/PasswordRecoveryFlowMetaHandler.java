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
import org.wso2.carbon.identity.api.server.flow.management.v1.BaseConnectorConfigs;
import org.wso2.carbon.identity.api.server.flow.management.v1.PasswordRecoveryConnectorConfigs;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.EMAIL_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.END_USER_ATTRIBUTE_PROFILE;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FlowType.PASSWORD_RECOVERY;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.MAGIC_LINK_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.PASSWORD_PROVISIONING_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.SMS_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.USER_RESOLVE_EXECUTOR;
import static org.wso2.carbon.identity.recovery.IdentityRecoveryConstants.ConnectorConfig.PASSWORD_RECOVERY_EMAIL_LINK_ENABLE;
import static org.wso2.carbon.identity.recovery.IdentityRecoveryConstants.ConnectorConfig.PASSWORD_RECOVERY_SEND_OTP_IN_EMAIL;
import static org.wso2.carbon.identity.recovery.IdentityRecoveryConstants.ConnectorConfig.PASSWORD_RECOVERY_SMS_OTP_ENABLE;

/**
 * Handler for managing the password recovery flow meta information.
 * This class extends the AbstractMetaResponseHandler to provide specific
 * implementations for password recovery flow.
 */
public class PasswordRecoveryFlowMetaHandler extends AbstractMetaResponseHandler {

    @Override
    public String getFlowType() {

        return String.valueOf(PASSWORD_RECOVERY);
    }

    @Override
    public String getAttributeProfile() {

        return END_USER_ATTRIBUTE_PROFILE;
    }

    @Override
    public PasswordRecoveryConnectorConfigs getConnectorConfigs() {

        Utils utils = new Utils();
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        PasswordRecoveryConnectorConfigs connectorConfigs = new PasswordRecoveryConnectorConfigs();
        BaseConnectorConfigs baseConfigs = super.getConnectorConfigs();
        connectorConfigs.setMultiAttributeLoginEnabled(baseConfigs.getMultiAttributeLoginEnabled());
        connectorConfigs.setPasswordRecoveryEnabled(
                utils.isFlowConfigEnabled(tenantDomain,
                        PASSWORD_RECOVERY_EMAIL_LINK_ENABLE) ||
                utils.isFlowConfigEnabled(tenantDomain,
                        PASSWORD_RECOVERY_SMS_OTP_ENABLE) ||
                utils.isFlowConfigEnabled(tenantDomain,
                        PASSWORD_RECOVERY_SEND_OTP_IN_EMAIL));
        return connectorConfigs;
    }

    @Override
    public List<String> getRequiredInputFields() {

        return new ArrayList<>(getLoginInputFields());
    }

    @Override
    public List<String> getSupportedExecutors() {

        ArrayList<String> supportedExecutors = new ArrayList<>();
        supportedExecutors.add(USER_RESOLVE_EXECUTOR);
        supportedExecutors.add(PASSWORD_PROVISIONING_EXECUTOR);
        supportedExecutors.add(EMAIL_OTP_EXECUTOR);
        supportedExecutors.add(MAGIC_LINK_EXECUTOR);
        supportedExecutors.add(SMS_OTP_EXECUTOR);
        return supportedExecutors;
    }
}
