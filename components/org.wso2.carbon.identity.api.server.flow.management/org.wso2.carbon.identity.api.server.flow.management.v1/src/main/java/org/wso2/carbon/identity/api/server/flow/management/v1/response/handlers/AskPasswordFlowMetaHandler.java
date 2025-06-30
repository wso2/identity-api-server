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
import org.wso2.carbon.identity.multi.attribute.login.constants.MultiAttributeLoginConstants;
import org.wso2.carbon.identity.recovery.IdentityRecoveryConstants;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.EMAIL_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FlowType.ASK_PASSWORD;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.PASSWORD_IDENTIFIER;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.PASSWORD_PROVISIONING_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.SELF_REGISTRATION_ATTRIBUTE_PROFILE;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.SMS_OTP_EXECUTOR;

/**
 * Handler for managing the ask password flow meta information.
 * This class extends the AbstractMetaResponseHandler to provide specific
 * implementations for ask password flow.
 */
public class AskPasswordFlowMetaHandler extends AbstractMetaResponseHandler {

    @Override
    public String getFlowType() {

        return String.valueOf(ASK_PASSWORD);
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
        connectorConfigs.setSelfRegistrationEnabled(utils.isFlowConfigEnabled(tenantDomain,
                            IdentityRecoveryConstants.ConnectorConfig.ENABLE_SELF_SIGNUP));
        connectorConfigs.setMultiAttributeLoginEnabled(utils.isFlowConfigEnabled(tenantDomain,
                        MultiAttributeLoginConstants.MULTI_ATTRIBUTE_LOGIN_PROPERTY));

        return connectorConfigs;
    }

    @Override
    public List<String> getRequiredInputFields() {

        ArrayList<String> requiredInputFields = new ArrayList<>();
        requiredInputFields.add(PASSWORD_IDENTIFIER);
        return requiredInputFields;
    }

    public List<String> getSupportedExecutors() {

        ArrayList<String> supportedExecutors = new ArrayList<>();
        supportedExecutors.add(PASSWORD_PROVISIONING_EXECUTOR);
        supportedExecutors.add(EMAIL_OTP_EXECUTOR);
        supportedExecutors.add(SMS_OTP_EXECUTOR);
        return supportedExecutors;
    }

}
