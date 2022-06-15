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

import org.apache.commons.collections.CollectionUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.AdditionalSpProperty;
import org.wso2.carbon.identity.api.server.application.management.v1.AdvancedApplicationConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.Certificate;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.UpdateFunction;
import org.wso2.carbon.identity.application.common.model.LocalAndOutboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.ServiceProviderProperty;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.setIfNotNull;

/**
 * Updates the advanced application configurations defined by the API model in the Service Provider model.
 */
public class UpdateAdvancedConfigurations implements UpdateFunction<ServiceProvider, AdvancedApplicationConfiguration> {

    public static final String TYPE_JWKS = "JWKS";
    public static final String TYPE_PEM = "PEM";

    @Override
    public void apply(ServiceProvider serviceProvider,
                      AdvancedApplicationConfiguration advancedConfigurations) {

        if (advancedConfigurations != null) {
            setIfNotNull(advancedConfigurations.getSaas(), serviceProvider::setSaasApp);
            setIfNotNull(advancedConfigurations.getDiscoverableByEndUsers(), serviceProvider::setDiscoverable);

            LocalAndOutboundAuthenticationConfig config = getLocalAndOutboundConfig(serviceProvider);
            setIfNotNull(advancedConfigurations.getSkipLoginConsent(), config::setSkipConsent);
            setIfNotNull(advancedConfigurations.getSkipLogoutConsent(), config::setSkipLogoutConsent);
            setIfNotNull(advancedConfigurations.getReturnAuthenticatedIdpList(),
                    config::setAlwaysSendBackAuthenticatedListOfIdPs);
            setIfNotNull(advancedConfigurations.getEnableAuthorization(), config::setEnableAuthorization);

            updateCertificate(advancedConfigurations.getCertificate(), serviceProvider);
            addAdditionalSpProperties(advancedConfigurations.getAdditionalSpProperties(), serviceProvider);
        }
    }

    private LocalAndOutboundAuthenticationConfig getLocalAndOutboundConfig(ServiceProvider application) {

        if (application.getLocalAndOutBoundAuthenticationConfig() == null) {
            application.setLocalAndOutBoundAuthenticationConfig(new LocalAndOutboundAuthenticationConfig());
        }

        return application.getLocalAndOutBoundAuthenticationConfig();
    }

    private void updateCertificate(Certificate certificate, ServiceProvider serviceProvider) {

        if (certificate != null) {
            if (TYPE_PEM.equals(certificate.getType())) {
                setIfNotNull(certificate.getValue(), serviceProvider::setCertificateContent);
                serviceProvider.setJwksUri(null);
            } else if (TYPE_JWKS.equals(certificate.getType())) {
                setIfNotNull(certificate.getValue(), serviceProvider::setJwksUri);
                serviceProvider.setCertificateContent(null);
            }
        }
    }


    private void addAdditionalSpProperties(List<AdditionalSpProperty> spAdditionalProperties,
                                           ServiceProvider serviceProvider) {

        List<ServiceProviderProperty> serviceProviderProperties = new ArrayList<>();
        if (!CollectionUtils.isEmpty(spAdditionalProperties)) {
            for (AdditionalSpProperty spProp: spAdditionalProperties) {
                ServiceProviderProperty serviceProviderProperty = new ServiceProviderProperty();
                serviceProviderProperty.setName(spProp.getName());
                serviceProviderProperty.setValue(spProp.getValue());
                serviceProviderProperty.setDisplayName(spProp.getDisplayName());
                serviceProviderProperties.add(serviceProviderProperty);
            }
            ServiceProviderProperty[] updatedAdditionalProps =
                    new ServiceProviderProperty[serviceProviderProperties.size()];
            updatedAdditionalProps = serviceProviderProperties.toArray(updatedAdditionalProps);
            serviceProvider.setSpProperties(serviceProviderProperties.toArray(updatedAdditionalProps));
        }
    }
}
