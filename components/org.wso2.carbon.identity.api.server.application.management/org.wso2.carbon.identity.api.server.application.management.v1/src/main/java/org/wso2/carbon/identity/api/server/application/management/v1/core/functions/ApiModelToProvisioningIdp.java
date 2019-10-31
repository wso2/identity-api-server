/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions;

import org.wso2.carbon.identity.api.server.application.management.v1.OutboundProvisioningConfiguration;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.JustInTimeProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.ProvisioningConnectorConfig;

import java.util.function.Function;

/**
 * Converts OutboundProvisioningConfiguration to a provisioning IDP object.
 */
public class ApiModelToProvisioningIdp implements Function<OutboundProvisioningConfiguration, IdentityProvider> {

    @Override
    public IdentityProvider apply(OutboundProvisioningConfiguration config) {

        IdentityProvider identityProvider = new IdentityProvider();
        identityProvider.setIdentityProviderName(config.getIdp());

        JustInTimeProvisioningConfig jitProvisioningConfig = new JustInTimeProvisioningConfig();
        jitProvisioningConfig.setProvisioningEnabled(Utils.getBooleanValue(config.getJit()));

        identityProvider.setJustInTimeProvisioningConfig(jitProvisioningConfig);

        ProvisioningConnectorConfig provisioningConfig = new ProvisioningConnectorConfig();
        provisioningConfig.setName(config.getConnector());
        provisioningConfig.setBlocking(config.getBlocking());
        provisioningConfig.setRulesEnabled(config.getRules());

        identityProvider.setDefaultProvisioningConnectorConfig(provisioningConfig);

        return identityProvider;
    }
}
