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
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowMetaResponseConnectionMeta;
import org.wso2.carbon.identity.api.server.flow.management.v1.RegistrationFlowMetaResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.SelfRegistrationConnectorConfigs;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ABSTRACT_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.APPLE_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.EMAIL_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FACEBOOK_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FIDO2_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FlowType.REGISTRATION;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.GOOGLE_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.MAGIC_LINK_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.OFFICE365_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.OPENID_CONNECT_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.PASSWORD_ONBOARD_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.PASSWORD_PROVISIONING_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.SELF_REGISTRATION_ATTRIBUTE_PROFILE;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.SMS_OTP_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.USER_RESOLVE_EXECUTOR;
import static org.wso2.carbon.identity.recovery.IdentityRecoveryConstants.ConnectorConfig.ENABLE_SELF_SIGNUP;

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
    public SelfRegistrationConnectorConfigs getConnectorConfigs() {

        Utils utils = new Utils();
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        SelfRegistrationConnectorConfigs connectorConfigs = new SelfRegistrationConnectorConfigs();
        BaseConnectorConfigs baseConfigs = super.getConnectorConfigs();
        connectorConfigs.setMultiAttributeLoginEnabled(baseConfigs.getMultiAttributeLoginEnabled());
        connectorConfigs.setSelfRegistrationEnabled(
                    utils.isFlowConfigEnabled(tenantDomain, ENABLE_SELF_SIGNUP));
        return connectorConfigs;
    }

    @Override
    public List<String> getRequiredInputFields() {

        return new ArrayList<>(getLoginInputFields());
    }

    @Override
    public RegistrationFlowMetaResponse createResponse() {

        RegistrationFlowMetaResponse response = new RegistrationFlowMetaResponse();
        response.setFlowType(getFlowType());
        response.setAttributeProfile(getAttributeProfile());
        response.setSupportedExecutors(getSupportedExecutors());
        response.setConnectorConfigs(getConnectorConfigs());
        response.setConnectionMeta(getConnectionMeta());
        return response;
    }

    /**
     * Get the connection meta information for the flow.
     *
     * @return Connection meta information.
     */
    private FlowMetaResponseConnectionMeta getConnectionMeta() {

        Utils utils = new Utils();
        List<IdentityProvider> identityProviders = utils.getConnections();

        List<Map<String, Object>> supportedConnections = identityProviders.stream()
                .map(config -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", config.getIdentityProviderName());
                    map.put("enabled", config.isEnable());
                    map.put("image", config.getImageUrl());
                    return map;
                })
                .collect(Collectors.toList());

        FlowMetaResponseConnectionMeta connections = new FlowMetaResponseConnectionMeta();
        connections.setSupportedConnections(supportedConnections);
        return connections;
    }

    @Override
    public List<String> getSupportedExecutors() {

        ArrayList<String> supportedExecutors = new ArrayList<>();
        supportedExecutors.add(OPENID_CONNECT_EXECUTOR);
        supportedExecutors.add(GOOGLE_EXECUTOR);
        supportedExecutors.add(FACEBOOK_EXECUTOR);
        supportedExecutors.add(OFFICE365_EXECUTOR);
        supportedExecutors.add(APPLE_EXECUTOR);
        supportedExecutors.add(FIDO2_EXECUTOR);
        supportedExecutors.add(USER_RESOLVE_EXECUTOR);
        supportedExecutors.add(PASSWORD_PROVISIONING_EXECUTOR);
        supportedExecutors.add(PASSWORD_ONBOARD_EXECUTOR);
        supportedExecutors.add(ABSTRACT_OTP_EXECUTOR);
        supportedExecutors.add(EMAIL_OTP_EXECUTOR);
        supportedExecutors.add(SMS_OTP_EXECUTOR);
        supportedExecutors.add(MAGIC_LINK_EXECUTOR);
        return supportedExecutors;
    }

}
