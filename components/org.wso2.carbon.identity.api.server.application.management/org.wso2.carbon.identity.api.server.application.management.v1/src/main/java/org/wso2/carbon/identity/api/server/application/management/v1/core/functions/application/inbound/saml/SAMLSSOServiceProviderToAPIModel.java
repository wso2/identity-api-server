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

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.AssertionEncryptionConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.IdpInitiatedSingleLogout;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.SAMLAssertionConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAMLAttributeProfile;
import org.wso2.carbon.identity.api.server.application.management.v1.SAMLRequestValidation;
import org.wso2.carbon.identity.api.server.application.management.v1.SAMLResponseSigning;
import org.wso2.carbon.identity.api.server.application.management.v1.SingleLogoutProfile;
import org.wso2.carbon.identity.api.server.application.management.v1.SingleSignOnProfile;
import org.wso2.carbon.identity.sso.saml.common.SAMLSSOProviderConstants;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.identity.sso.saml.util.SAMLSSOUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Converts the backend model SAMLSSOServiceProviderDTO into the corresponding API model object.
 */
public class SAMLSSOServiceProviderToAPIModel implements Function<SAMLSSOServiceProviderDTO, SAML2ServiceProvider> {

    @Override
    public SAML2ServiceProvider apply(SAMLSSOServiceProviderDTO dto) {

        return new SAML2ServiceProvider()
                .issuer(getIssuerWithoutQualifier(dto))
                .serviceProviderQualifier(dto.getIssuerQualifier())
                .assertionConsumerUrls(Arrays.asList(dto.getAssertionConsumerUrls()))
                .defaultAssertionConsumerUrl(dto.getDefaultAssertionConsumerUrl())
                .idpEntityIdAlias(dto.getIdpEntityIDAlias())

                .singleSignOnProfile(buildSingleSignOnProfile(dto))
                .attributeProfile(buildAttributeProfile(dto))
                .singleLogoutProfile(buildSingleLogoutProfile(dto))
                .requestValidation(buildRequestValidationConfig(dto))
                .responseSigning(buildResponseSigning(dto))
                .enableAssertionQueryProfile(dto.isAssertionQueryRequestProfileEnabled());
    }

    private SAMLResponseSigning buildResponseSigning(SAMLSSOServiceProviderDTO dto) {

        return new SAMLResponseSigning().enabled(dto.isDoSignResponse()).signingAlgorithm(dto.getSigningAlgorithmURI());
    }

    private SAMLRequestValidation buildRequestValidationConfig(SAMLSSOServiceProviderDTO dto) {

        return new SAMLRequestValidation()
                .enableSignatureValidation(dto.isDoValidateSignatureInRequests())
                .signatureValidationCertAlias(dto.getCertAlias());
    }

    private SingleLogoutProfile buildSingleLogoutProfile(SAMLSSOServiceProviderDTO dto) {

        return new SingleLogoutProfile()
                .enabled(dto.isDoSingleLogout())
                .logoutRequestUrl(dto.getSloRequestURL())
                .logoutResponseUrl(dto.getSloResponseURL())
                .logoutMethod(getSingleLogoutMethod(dto))
                .idpInitiatedSingleLogout(buildIdpInitiatedLogoutConfig(dto));
    }

    private IdpInitiatedSingleLogout buildIdpInitiatedLogoutConfig(SAMLSSOServiceProviderDTO dto) {

        return new IdpInitiatedSingleLogout()
                .enabled(dto.isIdPInitSLOEnabled())
                .returnToUrls(Arrays.asList(dto.getIdpInitSLOReturnToURLs()));
    }

    private SingleSignOnProfile buildSingleSignOnProfile(SAMLSSOServiceProviderDTO dto) {

        return new SingleSignOnProfile()
                .bindings(getBindings(dto))
                .enableSignatureValidationForArtifactBinding(dto.isDoValidateSignatureInArtifactResolve())
                .attributeConsumingServiceIndex(dto.getAttributeConsumingServiceIndex())
                .enableIdpInitiatedSingleSignOn(dto.isIdPInitSSOEnabled())
                .assertion(buildAssertionConfiguration(dto));
    }

    private SAMLAssertionConfiguration buildAssertionConfiguration(SAMLSSOServiceProviderDTO dto) {

        return new SAMLAssertionConfiguration()
                .nameIdFormat(dto.getNameIDFormat())
                .audiences(Arrays.asList(dto.getRequestedAudiences()))
                .recipients(Arrays.asList(dto.getRequestedRecipients()))
                .digestAlgorithm(dto.getDigestAlgorithmURI())
                .encryption(buildAssertionEncryptionConfiguration(dto));
    }

    private AssertionEncryptionConfiguration buildAssertionEncryptionConfiguration(SAMLSSOServiceProviderDTO dto) {

        return new AssertionEncryptionConfiguration()
                .enabled(dto.isDoEnableEncryptedAssertion())
                .assertionEncryptionAlgorithm(dto.getAssertionEncryptionAlgorithmURI())
                .keyEncryptionAlgorithm(dto.getKeyEncryptionAlgorithmURI());
    }

    private List<SingleSignOnProfile.BindingsEnum> getBindings(SAMLSSOServiceProviderDTO dto) {

        List<SingleSignOnProfile.BindingsEnum> bindings = new ArrayList<>();
        // There is no way to enable/disable HTTP POST and HTTP REDIRECT bindings.
        bindings.add(SingleSignOnProfile.BindingsEnum.HTTP_POST);
        bindings.add(SingleSignOnProfile.BindingsEnum.HTTP_REDIRECT);

        if (dto.isEnableSAML2ArtifactBinding()) {
            bindings.add(SingleSignOnProfile.BindingsEnum.ARTIFACT);
        }
        return bindings;
    }

    private SAMLAttributeProfile buildAttributeProfile(SAMLSSOServiceProviderDTO dto) {

        return new SAMLAttributeProfile()
                .enabled(dto.isEnableAttributeProfile())
                .alwaysIncludeAttributesInResponse(dto.isEnableAttributesByDefault());
    }

    private String getIssuerWithoutQualifier(SAMLSSOServiceProviderDTO dto) {

        return SAMLSSOUtil.getIssuerWithoutQualifier(dto.getIssuer());
    }

    private SingleLogoutProfile.LogoutMethodEnum getSingleLogoutMethod(SAMLSSOServiceProviderDTO dto) {

        if (dto.isDoFrontChannelLogout()) {
            return getFrontChannelLogoutBinding(dto.getFrontChannelLogoutBinding());
        } else {
            return SingleLogoutProfile.LogoutMethodEnum.BACKCHANNEL;
        }
    }

    private SingleLogoutProfile.LogoutMethodEnum getFrontChannelLogoutBinding(String logoutBinding) {

        if (StringUtils.equals(logoutBinding, SAMLSSOProviderConstants.HTTP_POST_BINDING)) {
            return SingleLogoutProfile.LogoutMethodEnum.FRONTCHANNEL_HTTP_POST;
        } else {
            return SingleLogoutProfile.LogoutMethodEnum.FRONTCHANNEL_HTTP_REDIRECT;
        }
    }
}
