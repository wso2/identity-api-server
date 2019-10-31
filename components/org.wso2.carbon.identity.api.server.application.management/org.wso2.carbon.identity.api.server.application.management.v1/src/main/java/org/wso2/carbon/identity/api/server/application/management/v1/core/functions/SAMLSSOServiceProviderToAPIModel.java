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

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.sso.saml.common.SAMLSSOProviderConstants;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Converts the backend model SAMLSSOServiceProviderDTO into the corresponding API model object.
 */
public class SAMLSSOServiceProviderToAPIModel implements Function<SAMLSSOServiceProviderDTO, SAML2Configuration> {

    private static final String QUALIFIER_ID = ":urn:sp:qualifier:";

    @Override
    public SAML2Configuration apply(SAMLSSOServiceProviderDTO dto) {

        return new SAML2Configuration()
                .issuer(getIssuerWithoutQualifier(dto))
                .serviceProviderQualifier(dto.getIssuerQualifier())
                .defaultAssertionConsumerUrl(dto.getDefaultAssertionConsumerUrl())
                .assertionConsumerUrls(Arrays.asList(dto.getAssertionConsumerUrls()))

                .nameIdFormat(dto.getNameIDFormat())
                .requestValidationCertificateAlias(dto.getCertAlias())

                .responseSigningAlgorithm(dto.getSigningAlgorithmURI())
                .responseDigestAlgorithm(dto.getDigestAlgorithmURI())
                .assertionEncryptionAlgroithm(dto.getAssertionEncryptionAlgorithmURI())
                .keyEncryptionAlgorithm(dto.getKeyEncryptionAlgorithmURI())

                .enableResponseSigning(dto.isDoSignResponse())
                .enableRequestSignatureValidation(dto.isDoValidateSignatureInRequests())
                .enableAssertionEncryption(dto.isDoEnableEncryptedAssertion())

                .enableRequestSignatureValidation(dto.isDoSingleLogout())
                .singleLogoutResponseUrl(dto.getSloResponseURL())
                .singleLogoutRequestUrl(dto.getSloRequestURL())
                .singleLogoutMethod(getSingleLogoutMethod(dto))

                .enableAttributeProfile(dto.isEnableAttributeProfile())
                .includedAttributeInResponseAlways(dto.isEnableAttributesByDefault())

                .audiences(Arrays.asList(dto.getRequestedAudiences()))
                .recipients(Arrays.asList(dto.getRequestedRecipients()))

                .enableIdpInitiatedSingleSignOn(dto.isIdPInitSSOEnabled())
                .enableIdpInitiatedSingleLogOut(dto.isIdPInitSLOEnabled())
                .idpInitiatedLogoutReturnUrls(Arrays.asList(dto.getIdpInitSLOReturnToURLs()))

                .enableAssertionQueryProfile(dto.isAssertionQueryRequestProfileEnabled())
                .enableSAML2ArtifactBinding(dto.isEnableSAML2ArtifactBinding())
                .enableSignatureValidationInArtifactBinding(dto.isDoValidateSignatureInArtifactResolve())

                .idPEntityidAlias(dto.getIdpEntityIDAlias());
    }

    private String getIssuerWithoutQualifier(SAMLSSOServiceProviderDTO dto) {

        return StringUtils.substringBefore(dto.getIssuer(), QUALIFIER_ID);
    }

    private SAML2Configuration.SingleLogoutMethodEnum getSingleLogoutMethod(SAMLSSOServiceProviderDTO dto) {

        if (dto.isDoFrontChannelLogout()) {
            if (StringUtils.equals(dto.getFrontChannelLogoutBinding(), SAMLSSOProviderConstants.HTTP_POST_BINDING)) {
                return SAML2Configuration.SingleLogoutMethodEnum.FRONTCHANNEL_HTTP_POST;
            } else {
                return SAML2Configuration.SingleLogoutMethodEnum.FRONTCHANNEL_HTTP_REDIRECT;
            }

        } else {
            return SAML2Configuration.SingleLogoutMethodEnum.BACKCHANNEL;
        }
    }
}
