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
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.flow.mgt.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.END_USER_ATTRIBUTE_PROFILE;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Executors.USER_RESOLVE_EXECUTOR;

/**
 * Handler for managing the password recovery flow meta information.
 * This class extends the AbstractMetaResponseHandler to provide specific
 * implementations for password recovery flow.
 */
public class PasswordRecoveryFlowMetaHandler extends AbstractMetaResponseHandler {

    private static final String PASSWORD_RECOVERY_EMAIL_OTP_ENABLED = "passwordRecoveryEmailOtpEnabled";
    private static final String EMAIL_OTP_ENABLED_PROPERTY = "Recovery.Notification.Password.OTP.SendOTPInEmail";
    private static final String PASSWORD_RECOVERY_SMS_OTP_ENABLED = "passwordRecoverySmsOtpEnabled";
    private static final String SMS_OTP_ENABLED_PROPERTY = "Recovery.Notification.Password.smsOtp.Enable";
    private static final String PASSWORD_RECOVERY_MAGIC_LINK_ENABLED = "passwordRecoveryMagicLinkEnabled";
    private static final String EMAIL_LINK_ENABLED_PROPERTY = "Recovery.Notification.Password.emailLink.Enable";

    @Override
    public String getFlowType() {

        return String.valueOf(Constants.FlowTypes.PASSWORD_RECOVERY);
    }

    @Override
    public String getAttributeProfile() {

        return END_USER_ATTRIBUTE_PROFILE;
    }

    @Override
    public Map<String, Boolean> getConnectorConfigs() {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        Map<String, Boolean> connectorConfigs = super.getConnectorConfigs();
        connectorConfigs.put(PASSWORD_RECOVERY_EMAIL_OTP_ENABLED,
                Utils.getGovernanceConfig(tenantDomain, EMAIL_OTP_ENABLED_PROPERTY));
        connectorConfigs.put(PASSWORD_RECOVERY_SMS_OTP_ENABLED,
                Utils.getGovernanceConfig(tenantDomain, SMS_OTP_ENABLED_PROPERTY));
        connectorConfigs.put(PASSWORD_RECOVERY_MAGIC_LINK_ENABLED,
                Utils.getGovernanceConfig(tenantDomain, EMAIL_LINK_ENABLED_PROPERTY));
        return connectorConfigs;
    }

    @Override
    public List<String> getRequiredInputFields() {

        return new ArrayList<>(getLoginInputFields());
    }

    @Override
    public List<String> getSupportedExecutors() {

        List<String> supportedExecutors = new ArrayList<>(super.getSupportedExecutors());
        supportedExecutors.add(USER_RESOLVE_EXECUTOR);
        return supportedExecutors;
    }
}
