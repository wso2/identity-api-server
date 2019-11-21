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
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.custom.CustomInboundUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.oauth2.OAuthInboundUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.saml.SAMLInboundUtils;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.OAUTH2;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.PASSIVE_STS;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.SAML2;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.WS_TRUST;

/**
 * Utility functions related to application inbound protocols.
 */
public class InboundUtils {

    private InboundUtils() {

    }

    public static OpenIDConnectConfiguration getOAuthInbound(ServiceProvider application) {

        return getInboundConfiguration(application, OAUTH2, OAuthInboundUtils::getOAuthConfiguration);
    }

    public static SAML2ServiceProvider getSAMLInbound(ServiceProvider application) {

        return getInboundConfiguration(application, SAML2, SAMLInboundUtils::getSAML2ServiceProvider);
    }

    public static PassiveStsConfiguration getPassiveSTSInbound(ServiceProvider application) {

        return getInboundConfiguration(application, PASSIVE_STS, PassiveSTSInboundUtils::getPassiveSTSConfiguration);
    }

    public static WSTrustConfiguration getWSTrustInbound(ServiceProvider application) {

        return getInboundConfiguration(application, WS_TRUST, WSTrustInboundUtils::getWSTrustConfiguration);
    }

    public static CustomInboundProtocolConfiguration getCustomInbound(ServiceProvider application, String inboundType) {

        return getInboundConfiguration(application, inboundType, CustomInboundUtils::getCustomInbound);
    }

    private static <T> T getInboundConfiguration(ServiceProvider application,
                                                 String inboundType,
                                                 Function<InboundAuthenticationRequestConfig, T> getInboundDetails) {

        InboundAuthenticationConfig inboundAuthConfig = application.getInboundAuthenticationConfig();
        if (inboundAuthConfig != null) {
            InboundAuthenticationRequestConfig[] inbounds = inboundAuthConfig.getInboundAuthenticationRequestConfigs();
            if (inbounds != null) {
                return Arrays.stream(inbounds)
                        .filter(inbound -> inboundType.equals(inbound.getInboundAuthType()))
                        .findAny()
                        .map(getInboundDetails)
                        .orElse(null);
            }
        }

        return null;
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
            rollbackInbound(inbound);
        }
    }

    public static void rollbackInbound(InboundAuthenticationRequestConfig inbound) {

        switch (inbound.getInboundAuthType()) {
            case FrameworkConstants.StandardInboundProtocols.SAML2:
                SAMLInboundUtils.deleteSAMLServiceProvider(inbound);
                break;
            case FrameworkConstants.StandardInboundProtocols.OAUTH2:
                OAuthInboundUtils.deleteOAuthInbound(inbound);
                break;
            case FrameworkConstants.StandardInboundProtocols.WS_TRUST:
                WSTrustInboundUtils.deleteWSTrustConfiguration(inbound);
                break;
            default:
                // No rollbacks required for other inbounds.
                break;
        }
    }

    public static void updateOrInsertInbound(ServiceProvider application,
                                             InboundAuthenticationRequestConfig newInbound) {

        InboundAuthenticationConfig inboundAuthConfig = application.getInboundAuthenticationConfig();
        if (inboundAuthConfig != null) {

            InboundAuthenticationRequestConfig[] inbounds = inboundAuthConfig.getInboundAuthenticationRequestConfigs();
            if (inbounds != null) {
                Map<String, InboundAuthenticationRequestConfig> inboundAuthConfigs =
                        Arrays.stream(inbounds).collect(
                                Collectors.toMap(InboundAuthenticationRequestConfig::getInboundAuthType,
                                        Function.identity()));

                inboundAuthConfigs.put(newInbound.getInboundAuthType(), newInbound);
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
