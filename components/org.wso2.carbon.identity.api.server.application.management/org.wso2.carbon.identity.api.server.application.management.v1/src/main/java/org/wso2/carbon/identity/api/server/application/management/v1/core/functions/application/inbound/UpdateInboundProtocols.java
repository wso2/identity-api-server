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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound;

import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.UpdateFunction;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.custom.CustomInboundUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.oauth2.OAuthInboundUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.saml.SAMLInboundUtils;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundUtils.rollbackInbounds;

/**
 * Updates the inbound authentication protocols defined by the API model in the Service Provider model.
 */
public class UpdateInboundProtocols implements UpdateFunction<ServiceProvider, InboundProtocols> {

    @Override
    public void update(ServiceProvider application, InboundProtocols inboundProtocols) {

        List<InboundAuthenticationRequestConfig> inbounds = new ArrayList<>();

        try {
            if (inboundProtocols.getOidc() != null) {
                inbounds.add(getOIDCInbound(inboundProtocols.getOidc()));
            }

            if (inboundProtocols.getSaml() != null) {
                inbounds.add(getSAMLInbound(inboundProtocols.getSaml()));
            }

            if (inboundProtocols.getWsTrust() != null) {
                inbounds.add(getWsTrustInbound(inboundProtocols.getWsTrust()));
            }
        } catch (APIError error) {
            // TODO: a log here.
            rollbackInbounds(inbounds);
            throw error;
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

        application.setInboundAuthenticationConfig(inboundAuthConfig);
    }

    private InboundAuthenticationRequestConfig buildCustomInbound(CustomInboundProtocolConfiguration inboundModel) {

        return CustomInboundUtils.createCustomInbound(inboundModel);
    }

    private InboundAuthenticationRequestConfig getOIDCInbound(OpenIDConnectConfiguration oidcModel) {

        // Build a consumer apps object.
        OAuthConsumerAppDTO registeredOAuthApp = OAuthInboundUtils.createOAuthInbound(oidcModel);

        // Try to register it.
        // Set the consumer key and secret in inbound.
        InboundAuthenticationRequestConfig oidcInbound = new InboundAuthenticationRequestConfig();
        oidcInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.OAUTH2);
        oidcInbound.setInboundAuthKey(registeredOAuthApp.getOauthConsumerKey());
        return oidcInbound;
    }

    private InboundAuthenticationRequestConfig getSAMLInbound(SAML2Configuration saml2Configuration) {

        String issuer = SAMLInboundUtils.createSAMLInbound(saml2Configuration);

        InboundAuthenticationRequestConfig samlInbound = new InboundAuthenticationRequestConfig();
        samlInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.SAML2);
        samlInbound.setInboundAuthKey(issuer);
        return samlInbound;
    }

    private InboundAuthenticationRequestConfig getPassiveStsInbound(PassiveStsConfiguration passiveSts) {

        return PassiveSTSInboundUtils.createPassiveSTSInboundConfig(passiveSts);
    }

    private InboundAuthenticationRequestConfig getWsTrustInbound(WSTrustConfiguration wsTrust) {

        return WSTrustInboundUtils.createWsTrustInbound(wsTrust);
    }
}
