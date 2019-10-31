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
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PropertyModel;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.identity.sso.saml.util.SAMLSSOUtil;
import org.wso2.carbon.security.SecurityConfigException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Converts InboundProtocols API model object to InboundAuthenticationConfig object.
 */
public class ApiModelToInboundAuthenticatorConfig implements Function<InboundProtocols, InboundAuthenticationConfig> {

    @Override
    public InboundAuthenticationConfig apply(InboundProtocols inboundProtocols) {

        List<InboundAuthenticationRequestConfig> inbounds = new ArrayList<>();

        if (inboundProtocols.getOidc() != null) {
            inbounds.add(getOIDCInbound(inboundProtocols.getOidc()));
        }

        if (inboundProtocols.getSaml() != null) {
            inbounds.add(getSAMLInbound(inboundProtocols.getSaml()));
        }

        if (inboundProtocols.getWsTrust() != null) {
            inbounds.add(getWsTrustInbound(inboundProtocols.getWsTrust()));
        }

        if (inboundProtocols.getPassiveSts() != null) {
            inbounds.add(getPassiveStsInbound(inboundProtocols.getPassiveSts()));
        }

        if (inboundProtocols.getCustom() != null) {
            inboundProtocols.getCustom().forEach(inboundConfigModel -> {
                // Add validate at swagger to make sure inbound key and name are not null.
                InboundAuthenticationRequestConfig inboundRequestConfig = buildCustomInbound(inboundConfigModel);
                inbounds.add(inboundRequestConfig);
            });
        }

        InboundAuthenticationConfig inboundAuthConfig = new InboundAuthenticationConfig();
        inboundAuthConfig.setInboundAuthenticationRequestConfigs(
                inbounds.toArray(new InboundAuthenticationRequestConfig[0])
        );
        return inboundAuthConfig;
    }

    private InboundAuthenticationRequestConfig buildCustomInbound(CustomInboundProtocolConfiguration inboundModel) {

        InboundAuthenticationRequestConfig inboundRequestConfig = new InboundAuthenticationRequestConfig();
        inboundRequestConfig.setInboundAuthType(inboundModel.getName());
        inboundRequestConfig.setInboundAuthKey(inboundModel.getInboundKey());
        inboundRequestConfig.setProperties(getProperties(inboundModel));
        return inboundRequestConfig;
    }

    private InboundAuthenticationRequestConfig getOIDCInbound(OpenIDConnectConfiguration oidcModel) {

        // Build a consumer apps object.
        // Try to register it.
        // Set the consumer key and secret in inbound.
        return new InboundAuthenticationRequestConfig();
    }

    private InboundAuthenticationRequestConfig getSAMLInbound(SAML2Configuration saml) {

        InboundAuthenticationRequestConfig samlInbound = new InboundAuthenticationRequestConfig();
        SAML2ServiceProvider saml2SpModel = saml.getServiceProvider();
        if (saml2SpModel != null) {
            SAMLSSOServiceProviderDTO samlSp = new ApiModelToSAMLSSOServiceProvider().apply(saml2SpModel);
            try {
                boolean success =
                        ApplicationManagementServiceHolder.getSamlssoConfigService().addRPServiceProvider(samlSp);
                if (success) {
                    samlInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.SAML2);
                    samlInbound.setInboundAuthKey(getIssuerWithQualifier(samlSp));
                } else {
                    // TODO: rollback other inbounds..
                    throw Utils.buildServerErrorResponse(null, "Error adding SAML config with issuer: " +
                            saml2SpModel.getIssuer());
                }
            } catch (IdentityException e) {
                // TODO: throw an error and rollback other inbounds.
                throw Utils.buildServerErrorResponse(null, "Error adding SAML config with issuer: " +
                        saml2SpModel.getIssuer());

            }
        }
        return samlInbound;
    }

    private String getIssuerWithQualifier(SAMLSSOServiceProviderDTO samlSp) {

        if (StringUtils.isNotBlank(samlSp.getIssuerQualifier())) {
            return SAMLSSOUtil.getIssuerWithQualifier(samlSp.getIssuer(), samlSp.getIssuerQualifier());
        } else {
            return samlSp.getIssuer();
        }
    }

    private InboundAuthenticationRequestConfig getPassiveStsInbound(PassiveStsConfiguration passiveSts) {

        InboundAuthenticationRequestConfig passiveStsInbound = new InboundAuthenticationRequestConfig();
        passiveStsInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.PASSIVE_STS);
        passiveStsInbound.setInboundAuthKey(passiveSts.getRealm());

        Property passiveStsReplyUrl = new Property();
        passiveStsReplyUrl.setName(IdentityApplicationConstants.PassiveSTS.PASSIVE_STS_REPLY_URL);
        passiveStsReplyUrl.setValue(passiveSts.getReplyTo());

        passiveStsInbound.setProperties(new Property[]{passiveStsReplyUrl});
        return passiveStsInbound;
    }

    private InboundAuthenticationRequestConfig getWsTrustInbound(WSTrustConfiguration wsTrust) {

        try {
            ApplicationManagementServiceHolder.getStsAdminService()
                    .addTrustedService(wsTrust.getAudience(), wsTrust.getCertificateAlias());
        } catch (SecurityConfigException e) {
            // Error while adding WS Trust, we can't continue
            throw Utils.buildServerErrorResponse(e, "Error while adding WS-Trust configuration.");
        }

        InboundAuthenticationRequestConfig wsTrustInbound = new InboundAuthenticationRequestConfig();
        wsTrustInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.WS_TRUST);
        wsTrustInbound.setInboundAuthKey(wsTrust.getAudience());
        return wsTrustInbound;
    }

    private Property[] getProperties(CustomInboundProtocolConfiguration inboundConfigModel) {

        return Optional.of(inboundConfigModel.getProperties())
                .map(modelProperties -> modelProperties.stream().map(this::buildProperty).toArray(Property[]::new))
                .orElse(new Property[0]);
    }

    private Property buildProperty(PropertyModel modelProperty) {

        Property property = new Property();
        property.setName(modelProperty.getKey());
        property.setValue(modelProperty.getValue());
        return property;
    }

}
