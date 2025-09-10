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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundSCIMProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.OutboundProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.UpdateFunction;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.InboundProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.JustInTimeProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.OutboundProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.ProvisioningConnectorConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.util.List;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.setIfNotNull;

/**
 * Updates the provisioning configurations defined by the API model in the Service Provider model.
 */
public class UpdateProvisioningConfiguration implements UpdateFunction<ServiceProvider, ProvisioningConfiguration> {

    private static final Log log = LogFactory.getLog(UpdateProvisioningConfiguration.class);

    @Override
    public void apply(ServiceProvider application, ProvisioningConfiguration provisioningConfigApiModel) {

        if (log.isDebugEnabled()) {
            log.debug("Updating provisioning configuration for application: " + 
                (application != null ? application.getApplicationName() : "null"));
        }
        if (provisioningConfigApiModel != null) {
            InboundSCIMProvisioningConfiguration inboundProvisioningModel =
                    provisioningConfigApiModel.getInboundProvisioning();

            if (inboundProvisioningModel != null) {
                InboundProvisioningConfig inboundProvisioningConfig = getInboundProvisioningConfig(application);
                setIfNotNull(inboundProvisioningModel.getProxyMode(), inboundProvisioningConfig::setDumbMode);
                setIfNotNull(inboundProvisioningModel.getProvisioningUserstoreDomain(),
                        inboundProvisioningConfig::setProvisioningUserStore);
                application.setInboundProvisioningConfig(inboundProvisioningConfig);
            }

            List<OutboundProvisioningConfiguration> outboundProvisioningIdps =
                    provisioningConfigApiModel.getOutboundProvisioningIdps();

            if (outboundProvisioningIdps != null) {

                OutboundProvisioningConfig outboundProvisioningConfig = getOutboundProvisionConfig(application);
                IdentityProvider[] identityProviders = getProvisioningIdps(outboundProvisioningIdps);
                outboundProvisioningConfig.setProvisioningIdentityProviders(identityProviders);

                application.setOutboundProvisioningConfig(outboundProvisioningConfig);
            }
            if (log.isDebugEnabled()) {
                log.debug("Successfully updated provisioning configuration for application: " + 
                    (application != null ? application.getApplicationName() : "null"));
            }
        }
    }

    private IdentityProvider[] getProvisioningIdps(List<OutboundProvisioningConfiguration> provisioningIdps) {

        return provisioningIdps.stream()
                .map(this::getProvisioningIdentityProvider)
                .toArray(IdentityProvider[]::new);
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
            application.setInboundProvisioningConfig(new InboundProvisioningConfig());
        }
        return application.getInboundProvisioningConfig();
    }

    private OutboundProvisioningConfig getOutboundProvisionConfig(ServiceProvider application) {

        if (application.getOutboundProvisioningConfig() == null) {
            application.setOutboundProvisioningConfig(new OutboundProvisioningConfig());
        }
        return application.getOutboundProvisioningConfig();
    }
}
