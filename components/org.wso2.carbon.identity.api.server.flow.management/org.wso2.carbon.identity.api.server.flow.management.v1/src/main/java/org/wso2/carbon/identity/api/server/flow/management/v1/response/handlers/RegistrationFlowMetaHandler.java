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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.flow.management.v1.BaseConnectorConfigs;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowMetaResponseConnectionMeta;
import org.wso2.carbon.identity.api.server.flow.management.v1.RegistrationFlowMetaResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.SelfRegistrationConnectorConfigs;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.flow.mgt.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.SELF_REGISTRATION_ATTRIBUTE_PROFILE;
import static org.wso2.carbon.identity.recovery.IdentityRecoveryConstants.ConnectorConfig.ENABLE_SELF_SIGNUP;

/**
 * Handler for managing the registration flow meta information.
 * This class extends the AbstractMetaResponseHandler to provide specific
 * implementations for registration flow.
 */
public class RegistrationFlowMetaHandler extends AbstractMetaResponseHandler {

    private static final Log LOG = LogFactory.getLog(RegistrationFlowMetaHandler.class);

    @Override
    public String getFlowType() {

        return String.valueOf(Constants.FlowTypes.REGISTRATION);
    }

    @Override
    public String getAttributeProfile() {

        return SELF_REGISTRATION_ATTRIBUTE_PROFILE;
    }

    @Override
    public SelfRegistrationConnectorConfigs getConnectorConfigs() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving connector configs for registration flow.");
        }
        Utils utils = new Utils();
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        SelfRegistrationConnectorConfigs connectorConfigs = new SelfRegistrationConnectorConfigs();
        BaseConnectorConfigs baseConfigs = super.getConnectorConfigs();
        connectorConfigs.setMultiAttributeLoginEnabled(baseConfigs.getMultiAttributeLoginEnabled());
        boolean selfRegEnabled = utils.isFlowConfigEnabled(tenantDomain, ENABLE_SELF_SIGNUP);
        connectorConfigs.setSelfRegistrationEnabled(selfRegEnabled);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieved connector configs for registration flow in tenant: " + tenantDomain +
                    " with selfRegistration: " + selfRegEnabled);
        }
        return connectorConfigs;
    }

    @Override
    public List<String> getRequiredInputFields() {

        return new ArrayList<>(getLoginInputFields());
    }

    @Override
    public RegistrationFlowMetaResponse createResponse() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating registration flow meta response.");
        }
        RegistrationFlowMetaResponse response = new RegistrationFlowMetaResponse();
        response.setFlowType(getFlowType());
        response.setAttributeProfile(getAttributeProfile());
        response.setSupportedExecutors(getSupportedExecutors());
        response.setConnectorConfigs(getConnectorConfigs());
        response.setConnectionMeta(getConnectionMeta());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully created registration flow meta response.");
        }
        return response;
    }

    /**
     * Get the connection meta information for the flow.
     *
     * @return Connection meta information.
     */
    private FlowMetaResponseConnectionMeta getConnectionMeta() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving connection meta information for registration flow.");
        }
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
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieved " + supportedConnections.size() + " connections for registration flow.");
        }
        return connections;
    }

    @Override
    public List<String> getSupportedExecutors() {

        return new ArrayList<>(super.getSupportedExecutors());
    }

}
