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
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * @return List of required input fields.
     */
    public abstract List<String> getRequiredInputFields();

    /**
     * Get the connection meta information for the flow.
     *
     * @return Connection meta information.
     */
    public FlowMetaResponseConnectionMeta getConnectionMeta() {

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
}
