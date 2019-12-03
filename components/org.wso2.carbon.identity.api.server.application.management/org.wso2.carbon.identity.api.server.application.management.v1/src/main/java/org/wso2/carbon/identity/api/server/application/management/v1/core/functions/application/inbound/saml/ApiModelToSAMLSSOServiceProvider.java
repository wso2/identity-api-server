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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.saml;

import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;

import java.util.function.Function;

/**
 * Converts api model
 */
public class ApiModelToSAMLSSOServiceProvider implements Function<SAML2ServiceProvider, SAMLSSOServiceProviderDTO> {

    @Override
    public SAMLSSOServiceProviderDTO apply(SAML2ServiceProvider sp) {

        SAMLSSOServiceProviderDTO serviceProviderDTO = new SAMLSSOServiceProviderDTO();
        serviceProviderDTO.setIssuer(sp.getIssuer());
        serviceProviderDTO.setIssuerQualifier(sp.getServiceProviderQualifier());
        serviceProviderDTO.setAssertionConsumerUrls(getAssertionConsumerUrls(sp));
        serviceProviderDTO.setDefaultAssertionConsumerUrl(getDefaultAssertionConsumerUrl(sp));

        serviceProviderDTO.setDoValidateSignatureInRequests(sp.getEnableRequestSignatureValidation());
        serviceProviderDTO.setDoEnableEncryptedAssertion(sp.getEnableAssertionEncryption());
        serviceProviderDTO.setAssertionEncryptionAlgorithmURI(sp.getAssertionEncryptionAlgroithm());
        serviceProviderDTO.setKeyEncryptionAlgorithmURI(sp.getKeyEncryptionAlgorithm());

        serviceProviderDTO.setNameIDFormat(sp.getNameIdFormat());
        serviceProviderDTO.setIdPInitSSOEnabled(sp.getEnableIdpInitiatedSingleSignOn());

        serviceProviderDTO.setDoSignResponse(sp.getEnableResponseSigning());
        serviceProviderDTO.setCertAlias(sp.getRequestValidationCertificateAlias());

        serviceProviderDTO.setSigningAlgorithmURI(sp.getResponseSigningAlgorithm());
        serviceProviderDTO.setDigestAlgorithmURI(sp.getResponseDigestAlgorithm());

        serviceProviderDTO.setDoSingleLogout(sp.getEnableSingleLogout());
        serviceProviderDTO.setSloResponseURL(sp.getSingleLogoutRequestUrl());
        serviceProviderDTO.setSloRequestURL(sp.getSingleLogoutRequestUrl());

        serviceProviderDTO.setDoFrontChannelLogout(isFrontChannelLogoutEnabled(sp));

        // TODO fill the rest.

        return serviceProviderDTO;
    }

    private String[] getAssertionConsumerUrls(SAML2ServiceProvider sp) {

        return sp.getAssertionConsumerUrls().toArray(new String[0]);
    }

    private boolean isFrontChannelLogoutEnabled(SAML2ServiceProvider sp) {

        return sp.getSingleLogoutMethod() != null &&
                sp.getSingleLogoutMethod() != SAML2ServiceProvider.SingleLogoutMethodEnum.BACKCHANNEL;
    }

    private String getDefaultAssertionConsumerUrl(SAML2ServiceProvider spModel) {

        if (spModel.getAssertionConsumerUrls().contains(spModel.getDefaultAssertionConsumerUrl())) {
            return spModel.getDefaultAssertionConsumerUrl();
        } else {
            return spModel.getAssertionConsumerUrls().get(0);
        }
    }
}
