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

import org.wso2.carbon.identity.api.server.flow.management.v1.FlowMetaResponseConnectionMeta;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowMetaResponseConnectorConfigs;
import org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.application.common.model.LocalAuthenticatorConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.PASSWORD_IDENTIFIER;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.USERNAME_IDENTIFIER;

/**
 * Abstract class for handling meta responses for different flows.
 * This class provides methods to retrieve flow type, attribute profile,
 * supported executors, connector configurations, and connection meta information.
 */
public abstract class AbstractMetaResponseHandler {

    /**
     * Get the flow type.
     *
     * @return Flow type.
     */
    public abstract String getFlowType();

    /**
     * Get the attribute profile.
     *
     * @return Attribute profile.
     */
    public abstract String getAttributeProfile();

    /**
     * Get the list of supported executors for the flow.
     *
     * @return List of supported executors.
     */
    public abstract List<String> getSupportedExecutors();

    /**
     * Get the connector configurations for the flow.
     *
     * @return Connector configurations.
     */
    public abstract FlowMetaResponseConnectorConfigs getConnectorConfigs();

    /**
     * Get the required input fields for the flow.
     *
     * @param flowType Type of the flow.
     * @return List of required input fields.
     */
    public List<String> getRequiredInputFields(String flowType) {

        ArrayList<String> requiredInputFields = new ArrayList<>();
        if (FlowEndpointConstants.FlowType.REGISTRATION.toString().equals(flowType)) {
            requiredInputFields.add(USERNAME_IDENTIFIER);
            requiredInputFields.add(PASSWORD_IDENTIFIER);
        }
        if (FlowEndpointConstants.FlowType.PASSWORD_RECOVERY.toString().equals(flowType)) {
            requiredInputFields.add(USERNAME_IDENTIFIER);
            return requiredInputFields;
        }
        if (FlowEndpointConstants.FlowType.ASK_PASSWORD.toString().equals(flowType)) {
            requiredInputFields.add(PASSWORD_IDENTIFIER);
            return requiredInputFields;
        }
        return new ArrayList<>();
    };

    /**
     * Get the connection meta information for the flow.
     *
     * @return Connection meta information.
     */
    public FlowMetaResponseConnectionMeta getConnectionMeta() {

        Utils utils = new Utils();
        LocalAuthenticatorConfig[] localAuthenticators = utils.getConnections();

        List<Map<String, Object>> supportedConnections = Arrays.stream(localAuthenticators)
                .map(config -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", config.getName());
                    map.put("displayName", config.getDisplayName());
                    map.put("enabled", config.isEnabled());
                    map.put("properties", config.getProperties()); // Already public array of Property[]
                    map.put("tags", config.getTags());
                    map.put("definedByType", config.getDefinedByType().toString());
                    return map;
                })
                .collect(Collectors.toList());

        FlowMetaResponseConnectionMeta connectionMeta = new FlowMetaResponseConnectionMeta();
        connectionMeta.setSupportedConnections(supportedConnections);
        return connectionMeta;
    }
}
