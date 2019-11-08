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

import org.wso2.carbon.identity.api.server.application.management.v1.AdvancedApplicationConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.Certificate;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.application.common.model.LocalAndOutboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.util.function.BiConsumer;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.setIfNotNull;

/**
 * Updates the advanced application configurations defined by the API model in the Service Provider model.
 */
public class PatchAdvancedConfigurations implements BiConsumer<ServiceProvider, AdvancedApplicationConfiguration> {

    @Override
    public void accept(ServiceProvider serviceProvider,
                       AdvancedApplicationConfiguration advancedConfigurations) {

        if (advancedConfigurations != null) {
            Utils.setIfNotNull(advancedConfigurations.getSaas(), serviceProvider::setSaasApp);

            LocalAndOutboundAuthenticationConfig config = serviceProvider.getLocalAndOutBoundAuthenticationConfig();
            Utils.setIfNotNull(advancedConfigurations.getSkipConsent(), config::setSkipConsent);
            Utils.setIfNotNull(advancedConfigurations.getReturnAuthenticatedIdpList(),
                    config::setAlwaysSendBackAuthenticatedListOfIdPs);
            Utils.setIfNotNull(advancedConfigurations.getEnableAuthorization(), config::setEnableAuthorization);

            updateCertificate(advancedConfigurations.getCertificate(), serviceProvider);
        }
    }

    private void updateCertificate(Certificate certificate, ServiceProvider serviceProvider) {

        if (certificate != null) {
            if (certificate.getType() == Certificate.TypeEnum.PEM) {
                setIfNotNull(certificate.getValue(), serviceProvider::setCertificateContent);
            } else if (certificate.getType() == Certificate.TypeEnum.JWKS) {
                setIfNotNull(certificate.getValue(), serviceProvider::setJwksUri);
            }
        }
    }
}
