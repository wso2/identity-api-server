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

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PropertyModel;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.oauth2.OAuthConsumerAppToApiModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.saml.SAMLSSOServiceProviderToAPIModel;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.security.SecurityConfigException;
import org.wso2.carbon.security.sts.service.util.TrustedServiceData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildServerErrorResponse;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.OAUTH2;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.PASSIVE_STS;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.SAML2;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.WS_TRUST;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.PassiveSTS.PASSIVE_STS_REPLY_URL;

/**
 * Utility functions related to application inbound protocols.
 */
public class InboundUtils {

    public static OpenIDConnectConfiguration getOAuthInbound(ServiceProvider application) {

        return getInboundConfiguration(application, OAUTH2, InboundUtils::buildOIDCModel);
    }

    public static SAML2ServiceProvider getSAMLInbound(ServiceProvider application) {

        return getInboundConfiguration(application, SAML2, InboundUtils::buildSaml2Configuration);
    }

    public static PassiveStsConfiguration getPassiveSTSInbound(ServiceProvider application) {

        return getInboundConfiguration(application, PASSIVE_STS, InboundUtils::buildPassiveSTSModel);
    }

    public static WSTrustConfiguration getWSTrustInbound(ServiceProvider application) {

        return getInboundConfiguration(application, WS_TRUST, InboundUtils::buildWsTrustConfiguration);
    }

    public static CustomInboundProtocolConfiguration getCustomInbound(ServiceProvider application, String inboundType) {

        return getInboundConfiguration(application, inboundType, InboundUtils::buildCustomInbound);
    }

    private static <T> T getInboundConfiguration(ServiceProvider application,
                                                 String inboundType,
                                                 Function<InboundAuthenticationRequestConfig, T> function) {

        InboundAuthenticationConfig inboundAuthConfig = application.getInboundAuthenticationConfig();
        if (inboundAuthConfig != null) {
            InboundAuthenticationRequestConfig[] inbounds = inboundAuthConfig.getInboundAuthenticationRequestConfigs();
            if (inbounds != null) {
                return Arrays.stream(inbounds)
                        .filter(inbound -> inboundType.equals(inbound.getInboundAuthType()))
                        .findAny()
                        .map(function)
                        .orElse(null);
            }
        }

        return null;
    }

    private static OpenIDConnectConfiguration buildOIDCModel(InboundAuthenticationRequestConfig inboundAuth) {

        String clientId = inboundAuth.getInboundAuthKey();
        try {
            OAuthConsumerAppDTO oauthApp =
                    ApplicationManagementServiceHolder.getOAuthAdminService().getOAuthApplicationData(clientId);
            return new OAuthConsumerAppToApiModel().apply(oauthApp);

        } catch (IdentityOAuthAdminException e) {

            throw buildServerErrorResponse(e, "Error while retrieving oauth application for clientId: " + clientId);
        }
    }

    private static SAML2ServiceProvider buildSaml2Configuration(InboundAuthenticationRequestConfig inboundAuth) {

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

    private static PassiveStsConfiguration buildPassiveSTSModel(InboundAuthenticationRequestConfig inboundAuth) {

        // TODO : null check on property array
        String replyTo = Arrays.stream(inboundAuth.getProperties())
                .filter(property -> StringUtils.equals(property.getName(), PASSIVE_STS_REPLY_URL))
                .findAny()
                .map(Property::getValue).orElse(null);

        return new PassiveStsConfiguration()
                .realm(inboundAuth.getInboundAuthKey())
                .replyTo(replyTo);
    }

    private static WSTrustConfiguration buildWsTrustConfiguration(InboundAuthenticationRequestConfig inboundAuth) {

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

    private static CustomInboundProtocolConfiguration buildCustomInbound(InboundAuthenticationRequestConfig inbound) {

        return new CustomInboundProtocolConfiguration()
                .name(inbound.getInboundAuthType())
                .inboundKey(inbound.getInboundAuthKey())
                .properties(
                        Optional.ofNullable(inbound.getProperties())
                                .map(inboundProperties -> Arrays.stream(inboundProperties)
                                        .map(InboundUtils::buildPropertyModel).collect(Collectors.toList())
                                ).orElse(Collections.emptyList()));

    }

    private static PropertyModel buildPropertyModel(Property inboundProperty) {

        return new PropertyModel()
                .key(inboundProperty.getName())
                .value(inboundProperty.getValue())
                .friendlyName(inboundProperty.getDisplayName());
    }

    public static String getInboundAuthKey(ServiceProvider application,
                                           String inboundType) {

        InboundAuthenticationConfig inboundAuthConfig = application.getInboundAuthenticationConfig();
        if (inboundAuthConfig != null) {
            InboundAuthenticationRequestConfig[] inbounds = inboundAuthConfig.getInboundAuthenticationRequestConfigs();
            if (inbounds != null) {
                return Arrays.stream(inbounds)
                        .filter(inbound -> inboundType.equals(inbound.getInboundAuthType()))
                        .findAny()
                        .map(InboundAuthenticationRequestConfig::getInboundAuthKey)
                        .orElse(null);
            }
        }

        return null;
    }

    public static void rollbackInbounds(List<InboundAuthenticationRequestConfig> currentlyAddedInbounds) {

        for (InboundAuthenticationRequestConfig inbound : currentlyAddedInbounds) {
            switch (inbound.getInboundAuthType()) {
                case FrameworkConstants.StandardInboundProtocols.SAML2:
                    rollbackSAMLServiceProvider(inbound);
                    break;
                case FrameworkConstants.StandardInboundProtocols.OAUTH2:
                    rollbackOAuth2ConsumerApp(inbound);
                    break;
                case FrameworkConstants.StandardInboundProtocols.WS_TRUST:
                    rollbackWsTrustService(inbound);
                    break;
                default:
                    // No rollbacks required for other inbounds.
                    break;
            }
        }
    }

    private static void rollbackWsTrustService(InboundAuthenticationRequestConfig inbound) {

        try {
            String trustedServiceAudience = inbound.getInboundAuthKey();
            ApplicationManagementServiceHolder.getStsAdminService().removeTrustedService(trustedServiceAudience);
        } catch (SecurityConfigException e) {
            throw Utils.buildServerErrorResponse(e, "Error while trying to rollback wsTrust configuration. "
                    + e.getMessage());
        }
    }

    private static void rollbackOAuth2ConsumerApp(InboundAuthenticationRequestConfig inbound) {

        try {
            String consumerKey = inbound.getInboundAuthKey();
            ApplicationManagementServiceHolder.getOAuthAdminService().removeOAuthApplicationData(consumerKey);
        } catch (IdentityOAuthAdminException e) {
            throw Utils.buildServerErrorResponse(e, "Error while trying to rollback OAuth2/OpenIDConnect " +
                    "configuration." + e.getMessage());
        }
    }

    private static void rollbackSAMLServiceProvider(InboundAuthenticationRequestConfig inbound) {

        try {
            String issuer = inbound.getInboundAuthKey();
            ApplicationManagementServiceHolder.getSamlssoConfigService().removeServiceProvider(issuer);
        } catch (IdentityException e) {
            throw Utils.buildServerErrorResponse(e, "Error while trying to rollback SAML2 configuration."
                    + e.getMessage());
        }
    }

    public static void updateOrInsertInbound(ServiceProvider application,
                                             String inboundType,
                                             InboundAuthenticationRequestConfig newInbound) {

        InboundAuthenticationConfig inboundAuthConfig = application.getInboundAuthenticationConfig();
        if (inboundAuthConfig != null) {

            InboundAuthenticationRequestConfig[] inbounds = inboundAuthConfig.getInboundAuthenticationRequestConfigs();
            if (inbounds != null) {
                Map<String, InboundAuthenticationRequestConfig> inboundAuthConfigs =
                        Arrays.stream(inbounds).collect(
                                Collectors.toMap(InboundAuthenticationRequestConfig::getInboundAuthType,
                                        Function.identity()));

                inboundAuthConfigs.put(inboundType, newInbound);
                inboundAuthConfig.setInboundAuthenticationRequestConfigs(
                        inboundAuthConfigs.values().toArray(new InboundAuthenticationRequestConfig[0]));
            } else {
                addNewInboundToSp(application, newInbound);
            }
        } else {
            // Create new inbound auth config.
            addNewInboundToSp(application, newInbound);
        }
    }

    private static void addNewInboundToSp(ServiceProvider application, InboundAuthenticationRequestConfig newInbound) {

        InboundAuthenticationConfig inboundAuth = new InboundAuthenticationConfig();
        inboundAuth.setInboundAuthenticationRequestConfigs(new InboundAuthenticationRequestConfig[]{newInbound});

        application.setInboundAuthenticationConfig(inboundAuth);
    }
}
