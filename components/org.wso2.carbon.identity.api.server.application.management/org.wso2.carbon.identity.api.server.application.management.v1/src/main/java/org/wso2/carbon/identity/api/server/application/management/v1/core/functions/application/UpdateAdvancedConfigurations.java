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

import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.AdditionalSpProperty;
import org.wso2.carbon.identity.api.server.application.management.v1.AdvancedApplicationConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.Certificate;
import org.wso2.carbon.identity.api.server.application.management.v1.TrustedAppConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.DiscoverableGroupModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.UpdateFunction;
import org.wso2.carbon.identity.application.common.model.ClientAttestationMetaData;
import org.wso2.carbon.identity.application.common.model.LocalAndOutboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.DiscoverableGroup;
import org.wso2.carbon.identity.application.common.model.SpTrustedAppMetadata;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage.ADDITIONAL_SP_PROP_NOT_SUPPORTED;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildBadRequestError;
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
            handleAdditionalSpProperties(advancedConfigurations.getAdditionalSpProperties());
            setIfNotNull(advancedConfigurations.getSaas(), serviceProvider::setSaasApp);
            setIfNotNull(advancedConfigurations.getDiscoverableByEndUsers(), serviceProvider::setDiscoverable);

            LocalAndOutboundAuthenticationConfig config = getLocalAndOutboundConfig(serviceProvider);
            setIfNotNull(advancedConfigurations.getSkipLoginConsent(), config::setSkipConsent);
            setIfNotNull(advancedConfigurations.getSkipLogoutConsent(), config::setSkipLogoutConsent);
            setIfNotNull(advancedConfigurations.getReturnAuthenticatedIdpList(),
                    config::setAlwaysSendBackAuthenticatedListOfIdPs);
            setIfNotNull(advancedConfigurations.getEnableAuthorization(), config::setEnableAuthorization);
            setIfNotNull(advancedConfigurations.getUseExternalConsentPage(), config::setUseExternalConsentPage);
            setIfNotNull(advancedConfigurations.getEnableAPIBasedAuthentication(),
                    serviceProvider::setAPIBasedAuthenticationEnabled);
            if (advancedConfigurations.getAttestationMetaData() != null) {
                ClientAttestationMetaData clientAttestationMetaData = new ClientAttestationMetaData();
                setIfNotNull(advancedConfigurations.getAttestationMetaData().getEnableClientAttestation(),
                        clientAttestationMetaData::setAttestationEnabled);
                setIfNotNull(advancedConfigurations.getAttestationMetaData().getAndroidPackageName(),
                        clientAttestationMetaData::setAndroidPackageName);
                setIfNotNull(advancedConfigurations.getAttestationMetaData().getAppleAppId(),
                        clientAttestationMetaData::setAppleAppId);
                if (advancedConfigurations.getAttestationMetaData()
                        .getAndroidAttestationServiceCredentials() != null) {
                    String androidAttestationServiceCredentials
                            = parseAndroidAttestationServiceCredentials(advancedConfigurations
                            .getAttestationMetaData()
                            .getAndroidAttestationServiceCredentials());
                    setIfNotNull(androidAttestationServiceCredentials,
                            clientAttestationMetaData::setAndroidAttestationServiceCredentials);
                }
                serviceProvider.setClientAttestationMetaData(clientAttestationMetaData);
            }
            handleTrustedAppConfigurations(advancedConfigurations.getTrustedAppConfiguration(), serviceProvider);
            updateCertificate(advancedConfigurations.getCertificate(), serviceProvider);
            updateDiscoverableGroups(serviceProvider, advancedConfigurations.getDiscoverableGroups());
        }
    }

    private void updateDiscoverableGroups(ServiceProvider serviceProvider, List<DiscoverableGroupModel> discoverableGroups) {

        List<DiscoverableGroup> discoverableGroupList = new ArrayList<>();

        if(!discoverableGroups.isEmpty()) {
            for (DiscoverableGroupModel discoverableGroupModelItem : discoverableGroups) {
                DiscoverableGroup discoverableGroup = new DiscoverableGroup(discoverableGroupModelItem.getUserStore(),discoverableGroupModelItem.getGroups());
                discoverableGroupList.add(discoverableGroup);
            }
            serviceProvider.setDiscoverableGroups(discoverableGroupList);
        }
    }

    /**
     * Parses the Android attestation service credentials and converts them into a JSON string.
     *
     * @param androidAttestationServiceCredentials The Android attestation service credentials to be parsed.
     * @return A JSON string representation of the parsed credentials.
     */
    private String parseAndroidAttestationServiceCredentials(Object androidAttestationServiceCredentials) {

        Gson gson = new Gson();
        return gson.toJson(androidAttestationServiceCredentials);
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

    private void handleAdditionalSpProperties(List<AdditionalSpProperty> spAdditionalProperties) {

        // `additionalSpProperties` not yet supported.
        if (!CollectionUtils.isEmpty(spAdditionalProperties)) {
            throw buildBadRequestError(ADDITIONAL_SP_PROP_NOT_SUPPORTED.getCode(),
                    ADDITIONAL_SP_PROP_NOT_SUPPORTED.getDescription());
        }
    }

    /**
     * Handles the trusted app configurations from the API model and sets them to the Service Provider object.
     *
     * @param trustedAppConfiguration The trusted app configuration of the API model.
     * @param serviceProvider         The service provider to update.
     */
    private void handleTrustedAppConfigurations(TrustedAppConfiguration trustedAppConfiguration,
                                                ServiceProvider serviceProvider) {

        if (trustedAppConfiguration != null) {
            SpTrustedAppMetadata trustedAppMetadata = new SpTrustedAppMetadata();
            List<String> thumbprints = trustedAppConfiguration.getAndroidThumbprints();

            setIfNotNull(trustedAppConfiguration.getAndroidPackageName(), trustedAppMetadata::setAndroidPackageName);
            setIfNotNull(thumbprints != null ? thumbprints.toArray(new String[0]) : null,
                    trustedAppMetadata::setAndroidThumbprints);
            setIfNotNull(trustedAppConfiguration.getAppleAppId(), trustedAppMetadata::setAppleAppId);
            setIfNotNull(trustedAppConfiguration.getIsFIDOTrustedApp(), trustedAppMetadata::setIsFidoTrusted);
            setIfNotNull(trustedAppConfiguration.getIsConsentGranted(), trustedAppMetadata::setIsConsentGranted);
            serviceProvider.setTrustedAppMetadata(trustedAppMetadata);
        }
    }
}
