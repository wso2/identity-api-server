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
 * Converts the backend model IdentityProvider into the corresponding API model object.
 */
public class ProvisioningIdpToApiModel implements Function<IdentityProvider, OutboundProvisioningConfiguration> {

    @Override
    public OutboundProvisioningConfiguration apply(IdentityProvider identityProvider) {

        ProvisioningConnectorConfig provisioningConfig = identityProvider.getDefaultProvisioningConnectorConfig();
        JustInTimeProvisioningConfig justInTimeProvisioningConfig = identityProvider.getJustInTimeProvisioningConfig();

        return new OutboundProvisioningConfiguration()
                .idp(identityProvider.getIdentityProviderName())
                .blocking(provisioningConfig.isBlocking())
                .connector(provisioningConfig.getName())
                .rules(provisioningConfig.isRulesEnabled())
                .jit(justInTimeProvisioningConfig != null && justInTimeProvisioningConfig.isProvisioningEnabled());
    }
}
