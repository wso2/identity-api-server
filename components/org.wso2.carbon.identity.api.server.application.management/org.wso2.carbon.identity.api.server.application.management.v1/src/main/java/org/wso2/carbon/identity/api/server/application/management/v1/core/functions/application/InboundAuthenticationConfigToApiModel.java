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

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PropertyModel;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.oauth2.OAuthConsumerAppToApiModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.saml.SAMLSSOServiceProviderToAPIModel;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.security.SecurityConfigException;
import org.wso2.carbon.security.sts.service.util.TrustedServiceData;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildServerErrorResponse;

/**
 * Converts backend InboundAuthenticationConfig to InboundProtocols api model object.
 */
public class InboundAuthenticationConfigToApiModel implements Function<InboundAuthenticationConfig, InboundProtocols> {

    @Override
    public InboundProtocols apply(InboundAuthenticationConfig inboundAuthConfigs) {

        InboundProtocols inboundProtocols = new InboundProtocols();

        if (inboundAuthConfigs != null) {
            InboundAuthenticationRequestConfig[] inbounds = inboundAuthConfigs.getInboundAuthenticationRequestConfigs();
            if (inbounds != null) {
                for (InboundAuthenticationRequestConfig inboundAuth : inbounds) {
                    switch (inboundAuth.getInboundAuthType()) {
                        case FrameworkConstants.StandardInboundProtocols.SAML2:
                            inboundProtocols.setSaml(buildSaml2Configuration(inboundAuth));
                            break;
                        case FrameworkConstants.StandardInboundProtocols.OAUTH2:
                            inboundProtocols.setOidc(buildOpenIdConnectConfiguration(inboundAuth));
                            break;
                        case FrameworkConstants.StandardInboundProtocols.PASSIVE_STS:
                            inboundProtocols.setPassiveSts(buildPassiveSTSConfiguration(inboundAuth));
                            break;
                        case FrameworkConstants.StandardInboundProtocols.WS_TRUST:
                            inboundProtocols.setWsTrust(buildWsTrustConfiguration(inboundAuth));
                            break;
                        case IdentityApplicationConstants.Authenticator.OpenID.NAME:
                            // We ignore openid now as we do not support it anymore.
                            break;
                        default:
                            inboundProtocols.addCustomItem(buildCustomInboundConfig(inboundAuth));
                            break;
                    }
                }
            }
        }

        return inboundProtocols;
    }

    private CustomInboundProtocolConfiguration buildCustomInboundConfig(InboundAuthenticationRequestConfig inbound) {

        return new CustomInboundProtocolConfiguration()
                .name(inbound.getInboundAuthType())
                .inboundKey(inbound.getInboundAuthKey())
                .properties(
                        Optional.ofNullable(inbound.getProperties())
                                .map(inboundProperties -> Arrays.stream(inboundProperties)
                                        .map(this::buildPropertyModel).collect(Collectors.toList())
                                ).orElse(Collections.emptyList()));

    }

    private PropertyModel buildPropertyModel(Property inboundProperty) {

        return new PropertyModel()
                .key(inboundProperty.getName())
                .value(inboundProperty.getValue())
                .friendlyName(inboundProperty.getDisplayName());
    }

    private WSTrustConfiguration buildWsTrustConfiguration(InboundAuthenticationRequestConfig inboundAuth) {

        String audience = inboundAuth.getInboundAuthKey();
        try {
            TrustedServiceData[] trustedServices =
                    ApplicationManagementServiceHolder.getStsAdminService().getTrustedServices();

            // TODO : check whether we need to throw an exception if we can't find a wstrust service
            return Arrays.stream(trustedServices)
                    .filter(trustedServiceData -> StringUtils.equals(trustedServiceData.getServiceAddress(), audience))
                    .findAny()
                    .map(trustedServiceData -> new WSTrustConfiguration()
                            .audience(trustedServiceData.getServiceAddress())
                            .certificateAlias(trustedServiceData.getCertAlias()))
                    .orElse(null);

        } catch (SecurityConfigException e) {
            throw buildServerErrorResponse(e, "Error while retrieving wsTrust configuration for audience: " + audience);
        }
    }

    private PassiveStsConfiguration buildPassiveSTSConfiguration(InboundAuthenticationRequestConfig inboundAuth) {

        return new PassiveStsConfiguration()
                .realm(inboundAuth.getInboundAuthKey())
                .replyTo(getPassiveSTSWReply(inboundAuth.getProperties()));
    }

    private String getPassiveSTSWReply(Property[] properties) {

        // TODO : null check on property array
        return Arrays.stream(properties)
                .filter(property -> StringUtils.equals(property.getName(),
                        IdentityApplicationConstants.PassiveSTS.PASSIVE_STS_REPLY_URL))
                .findAny()
                .map(Property::getValue).orElse(null);
    }

    private OpenIDConnectConfiguration buildOpenIdConnectConfiguration(InboundAuthenticationRequestConfig inboundAuth) {

        String clientId = inboundAuth.getInboundAuthKey();
        try {
            OAuthConsumerAppDTO oauthApp =
                    ApplicationManagementServiceHolder.getOAuthAdminService().getOAuthApplicationData(clientId);
            return new OAuthConsumerAppToApiModel().apply(oauthApp);

        } catch (IdentityOAuthAdminException e) {

            throw buildServerErrorResponse(e, "Error while retrieving oauth application for clientId: " + clientId);
        }
    }

    private SAML2Configuration buildSaml2Configuration(InboundAuthenticationRequestConfig inboundAuth) {

        String issuer = inboundAuth.getInboundAuthKey();
        try {
            SAMLSSOServiceProviderDTO serviceProvider =
                    ApplicationManagementServiceHolder.getSamlssoConfigService().getServiceProvider(issuer);

            if (serviceProvider != null) {
                return new SAMLSSOServiceProviderToAPIModel().apply(serviceProvider);
            } else {
                return null;
            }
        } catch (IdentityException e) {
            throw buildServerErrorResponse(e, "Error while retrieving service provider data for issuer: " + issuer);
        }
    }

}
