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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application;

import org.wso2.carbon.identity.api.server.application.management.v1.OutboundProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.InboundProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.JustInTimeProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.OutboundProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.ProvisioningConnectorConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.util.Optional;
import java.util.function.BiConsumer;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.setIfNotNull;

/**
 * Updates the provisioning configurations defined by the API model in the Service Provider model.
 */
public class PatchProvisioningConfiguration implements BiConsumer<ServiceProvider, ProvisioningConfiguration> {

    @Override
    public void accept(ServiceProvider application, ProvisioningConfiguration provisioningConfigApiModel) {

        if (provisioningConfigApiModel != null) {
            Optional.ofNullable(provisioningConfigApiModel.getInboundProvisioning())
                    .ifPresent(config -> {
                        InboundProvisioningConfig inboundProvisioningConfig = getInboundProvisioningConfig(application);
                        setIfNotNull(config.getProxyMode(), inboundProvisioningConfig::setDumbMode);
                        inboundProvisioningConfig.setProvisioningUserStore(config.getProvisioningUserstoreDomain());

                        application.setInboundProvisioningConfig(inboundProvisioningConfig);
                    });

            Optional.ofNullable(provisioningConfigApiModel.getOutboundProvisioningIdps())
                    .ifPresent(idps -> {
                        OutboundProvisioningConfig outboundProvisioningConfig = getOutboundProvisionConfig(application);
                        IdentityProvider[] identityProviders =
                                idps.stream()
                                        .map(this::getProvisioningIdentityProvider)
                                        .toArray(IdentityProvider[]::new);
                        outboundProvisioningConfig.setProvisioningIdentityProviders(identityProviders);

                        application.setOutboundProvisioningConfig(outboundProvisioningConfig);
                    });
        }
    }

    private IdentityProvider getProvisioningIdentityProvider(OutboundProvisioningConfiguration config) {

        IdentityProvider identityProvider = new IdentityProvider();
        identityProvider.setIdentityProviderName(config.getIdp());

        JustInTimeProvisioningConfig jitProvisioningConfig = new JustInTimeProvisioningConfig();
        setIfNotNull(config.getJit(), jitProvisioningConfig::setProvisioningEnabled);

        identityProvider.setJustInTimeProvisioningConfig(jitProvisioningConfig);

        ProvisioningConnectorConfig provisioningConfig = new ProvisioningConnectorConfig();
        provisioningConfig.setName(config.getConnector());
        provisioningConfig.setBlocking(config.getBlocking());
        provisioningConfig.setRulesEnabled(config.getRules());

        identityProvider.setDefaultProvisioningConnectorConfig(provisioningConfig);

        return identityProvider;
    }

    private InboundProvisioningConfig getInboundProvisioningConfig(ServiceProvider application) {

        if (application.getInboundProvisioningConfig() == null) {
            return new InboundProvisioningConfig();
        }
        return application.getInboundProvisioningConfig();
    }

    private OutboundProvisioningConfig getOutboundProvisionConfig(ServiceProvider application) {

        if (application.getOutboundProvisioningConfig() == null) {
            return new OutboundProvisioningConfig();
        }
        return application.getOutboundProvisioningConfig();
    }
}
