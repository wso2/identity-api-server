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

import org.apache.commons.collections.CollectionUtils;
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

import java.util.List;
import java.util.function.Function;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildBadRequestError;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.setIfNotNull;

/**
 * Converts api model
 */
public class ApiModelToSAMLSSOServiceProvider implements Function<SAML2ServiceProvider, SAMLSSOServiceProviderDTO> {

    @Override
    public SAMLSSOServiceProviderDTO apply(SAML2ServiceProvider samlModel) {

        SAMLSSOServiceProviderDTO dto = new SAMLSSOServiceProviderDTO();
        dto.setIssuer(samlModel.getIssuer());
        dto.setIssuerQualifier(samlModel.getServiceProviderQualifier());
        dto.setAssertionConsumerUrls(getAssertionConsumerUrls(samlModel));
        dto.setDefaultAssertionConsumerUrl(getDefaultAssertionConsumerUrl(samlModel));
        dto.setIdpEntityIDAlias(samlModel.getIdpEntityIdAlias());

        updateSingleSignOnProfile(dto, samlModel.getSingleSignOnProfile());
        updateAttributeProfile(dto, samlModel.getAttributeProfile());
        updateSingleLogoutProfile(dto, samlModel.getSingleLogoutProfile());
        updateRequestSignatureValidationConfig(dto, samlModel.getRequestValidation());
        updateResponseSigningConfig(dto, samlModel.getResponseSigning());

        setIfNotNull(samlModel.getEnableAssertionQueryProfile(), dto::setAssertionQueryRequestProfileEnabled);
        dto.setAssertionQueryRequestProfileEnabled(samlModel.getEnableAssertionQueryProfile());

        return dto;
    }

    private void updateResponseSigningConfig(SAMLSSOServiceProviderDTO dto,
                                             SAMLResponseSigning responseSigning) {

        // Regardless of the response signing configuration we always sign the assertions.
        dto.setDoSignAssertions(true);
        if (responseSigning != null) {
            setIfNotNull(responseSigning.getEnabled(), dto::setDoSignResponse);
            dto.setSigningAlgorithmURI(responseSigning.getSigningAlgorithm());
        }
    }

    private void updateRequestSignatureValidationConfig(SAMLSSOServiceProviderDTO dto,
                                                        SAMLRequestValidation requestValidation) {

        if (requestValidation != null) {
            setIfNotNull(requestValidation.getEnableSignatureValidation(), dto::setDoValidateSignatureInRequests);
            dto.setCertAlias(requestValidation.getSignatureValidationCertAlias());
        }
    }

    private void updateSingleLogoutProfile(SAMLSSOServiceProviderDTO dto, SingleLogoutProfile sloProfile) {

        if (sloProfile != null) {
            setIfNotNull(sloProfile.getEnabled(), dto::setDoSingleLogout);
            dto.setSloRequestURL(sloProfile.getLogoutRequestUrl());
            dto.setSloResponseURL(sloProfile.getLogoutResponseUrl());

            updateLogoutMechanism(dto, sloProfile);

            IdpInitiatedSingleLogout idpInitiatedSingleLogout = sloProfile.getIdpInitiatedSingleLogout();
            if (idpInitiatedSingleLogout != null) {
                setIfNotNull(idpInitiatedSingleLogout.getEnabled(), dto::setIdPInitSLOEnabled);
                dto.setIdpInitSLOReturnToURLs(toArray(idpInitiatedSingleLogout.getReturnToUrls()));
            }
        }
    }

    private void updateLogoutMechanism(SAMLSSOServiceProviderDTO dto, SingleLogoutProfile sloProfile) {

        SingleLogoutProfile.LogoutMethodEnum logoutMethod = sloProfile.getLogoutMethod();

        if (isFrontChannelLogoutEnabled(logoutMethod)) {
            dto.setDoFrontChannelLogout(true);

            if (logoutMethod == SingleLogoutProfile.LogoutMethodEnum.FRONTCHANNEL_HTTP_POST) {
                dto.setFrontChannelLogoutBinding(SAMLSSOProviderConstants.HTTP_POST_BINDING);
            }

            if (logoutMethod == SingleLogoutProfile.LogoutMethodEnum.FRONTCHANNEL_HTTP_REDIRECT) {
                dto.setFrontChannelLogoutBinding(SAMLSSOProviderConstants.HTTP_REDIRECT_BINDING);
            }
        }
    }

    private void updateAttributeProfile(SAMLSSOServiceProviderDTO dto, SAMLAttributeProfile attributeProfile) {

        if (attributeProfile != null) {
            setIfNotNull(attributeProfile.getEnabled(), dto::setEnableAttributeProfile);
            setIfNotNull(attributeProfile.getAlwaysIncludeAttributesInResponse(), dto::setEnableAttributesByDefault);
        }
    }

    private void updateSingleSignOnProfile(SAMLSSOServiceProviderDTO dto, SingleSignOnProfile ssoConfig) {

        if (ssoConfig != null) {

            List<SingleSignOnProfile.BindingsEnum> bindings = ssoConfig.getBindings();
            // HTTP_POST and HTTP_REDIRECT bindings are not considered at the backend. They are always available by
            // default, therefore we only need to process the artifact binding.
            if (CollectionUtils.isNotEmpty(bindings) && bindings.contains(SingleSignOnProfile.BindingsEnum.ARTIFACT)) {
                dto.setEnableSAML2ArtifactBinding(true);
            }

            dto.setDoValidateSignatureInArtifactResolve(ssoConfig.getEnableSignatureValidationForArtifactBinding());
            dto.setIdPInitSSOEnabled(ssoConfig.getEnableIdpInitiatedSingleSignOn());

            SAMLAssertionConfiguration assertionConfig = ssoConfig.getAssertion();
            if (assertionConfig != null) {
                dto.setNameIDFormat(assertionConfig.getNameIdFormat());
                dto.setRequestedAudiences(toArray(assertionConfig.getAudiences()));
                dto.setRequestedRecipients(toArray(assertionConfig.getRecipients()));
                dto.setDigestAlgorithmURI(assertionConfig.getDigestAlgorithm());

                AssertionEncryptionConfiguration encryption = assertionConfig.getEncryption();
                if (encryption != null) {
                    setIfNotNull(encryption.getEnabled(), dto::setDoEnableEncryptedAssertion);
                    dto.setAssertionEncryptionAlgorithmURI(encryption.getAssertionEncryptionAlgorithm());
                    dto.setKeyEncryptionAlgorithmURI(encryption.getKeyEncryptionAlgorithm());
                }
            }
        }
    }

    private String[] getAssertionConsumerUrls(SAML2ServiceProvider sp) {

        if (isEmpty(sp.getAssertionConsumerUrls())) {
            throw buildBadRequestError("At least one assertion consumer URL is required for a SAML Application.");
        }
        return toArray(sp.getAssertionConsumerUrls());
    }

    private boolean isFrontChannelLogoutEnabled(SingleLogoutProfile.LogoutMethodEnum logoutMethod) {

        return logoutMethod != null && logoutMethod != SingleLogoutProfile.LogoutMethodEnum.BACKCHANNEL;
    }

    private String getDefaultAssertionConsumerUrl(SAML2ServiceProvider spModel) {

        if (spModel.getAssertionConsumerUrls().contains(spModel.getDefaultAssertionConsumerUrl())) {
            return spModel.getDefaultAssertionConsumerUrl();
        } else {
            return spModel.getAssertionConsumerUrls().get(0);
        }
    }

    private String[] toArray(List<String> list) {

        if (isEmpty(list)) {
            return new String[0];
        } else {
            return list.toArray(new String[0]);
        }
    }
}
