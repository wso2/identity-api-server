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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.provisioning;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundSCIMProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.OutboundProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.InboundProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.JustInTimeProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.OutboundProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.ProvisioningConnectorConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Builds a ProvisioningConfiguration model object based on a ServiceProvider's provisioning configurations.
 */
public class BuildProvisioningConfiguration implements Function<ServiceProvider, ProvisioningConfiguration> {

    @Override
    public ProvisioningConfiguration apply(ServiceProvider application) {

        ProvisioningConfiguration config = new ProvisioningConfiguration();

        if (application.getInboundProvisioningConfig() != null) {
            config.inboundProvisioning(buildInboundProvisioningConfig(application.getInboundProvisioningConfig()));
        }

        if (application.getOutboundProvisioningConfig() != null) {
            List<OutboundProvisioningConfiguration> provisioningIdps =
                    buildOutboundProvisioningConfig(application.getOutboundProvisioningConfig());
            config.outboundProvisioningIdps(provisioningIdps);
        }

        return config;
    }

    private List<OutboundProvisioningConfiguration> buildOutboundProvisioningConfig(
            OutboundProvisioningConfig outboundProvisioningConfig) {

        if (outboundProvisioningConfig.getProvisioningIdentityProviders() != null) {
            return Arrays.stream(outboundProvisioningConfig.getProvisioningIdentityProviders())
                    .map(this::getOutboundProvisioningConfiguration)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private OutboundProvisioningConfiguration getOutboundProvisioningConfiguration(IdentityProvider identityProvider) {

        ProvisioningConnectorConfig provisioningConfig =
                identityProvider.getDefaultProvisioningConnectorConfig();
        JustInTimeProvisioningConfig justInTimeProvisioningConfig =
                identityProvider.getJustInTimeProvisioningConfig();

        return new OutboundProvisioningConfiguration()
                .idp(identityProvider.getIdentityProviderName())
                .blocking(provisioningConfig.isBlocking())
                .connector(provisioningConfig.getName())
                .rules(provisioningConfig.isRulesEnabled())
                .jit(justInTimeProvisioningConfig != null && justInTimeProvisioningConfig.isProvisioningEnabled());
    }

    private InboundSCIMProvisioningConfiguration buildInboundProvisioningConfig(
            InboundProvisioningConfig inboundProvisioningConfig) {

        if (inboundProvisioningConfig.isDumbMode()) {
            return new InboundSCIMProvisioningConfiguration().proxyMode(true);
        } else if (StringUtils.isNotBlank(inboundProvisioningConfig.getProvisioningUserStore())) {
            return new InboundSCIMProvisioningConfiguration()
                    .provisioningUserstoreDomain(inboundProvisioningConfig.getProvisioningUserStore());
        }

        return null;
    }
}
