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

import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility functions related to application inbound protocols.
 */
public class InboundFunctions {

    private InboundFunctions() {

    }

    /**
     * Extract the inbound configuration of a particular type from an application and converts it to the API model.
     *
     * @param application
     * @param inboundType Inbound Type
     * @return
     */
    public static InboundAuthenticationRequestConfig getInboundAuthenticationRequestConfig(ServiceProvider application,
                                                                                           String inboundType) {

        InboundAuthenticationConfig inboundAuthConfig = application.getInboundAuthenticationConfig();
        if (inboundAuthConfig != null) {
            InboundAuthenticationRequestConfig[] inbounds = inboundAuthConfig.getInboundAuthenticationRequestConfigs();
            if (inbounds != null) {
                return Arrays.stream(inbounds)
                        .filter(inbound -> inboundType.equals(inbound.getInboundAuthType()))
                        .findAny()
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
        
        // No rollbacks required for other inbounds since those are handled by the framework.
        if (inbound.getInboundAuthType().equals(FrameworkConstants.StandardInboundProtocols.WS_TRUST)) {
            WSTrustInboundFunctions.deleteWSTrustConfiguration(inbound);
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
