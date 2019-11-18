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
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.security.SecurityConfigException;
import org.wso2.carbon.security.sts.service.util.TrustedServiceData;

import java.util.Arrays;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildServerErrorResponse;

/**
 * Helper functions for WSTrust inbound management.
 */
public class WSTrustInboundUtils {

    public static void putWSTrustConfiguration(ServiceProvider application,
                                               WSTrustConfiguration wsTrustConfiguration) {

        String inboundAuthKey = InboundUtils.getInboundAuthKey(application, StandardInboundProtocols.WS_TRUST);
        try {
            if (inboundAuthKey != null) {
                ApplicationManagementServiceHolder.getStsAdminService().removeTrustedService(inboundAuthKey);
            }

            addWSTrustInbound(wsTrustConfiguration);
        } catch (SecurityConfigException e) {
            String applicationId = application.getApplicationResourceId();
            throw buildServerErrorResponse(e,
                    "Error while creating/updating WSTrust inbound of application: " + applicationId);
        }
    }

    public static void addWSTrustInbound(WSTrustConfiguration wsTrustConfiguration) {

        try {
            ApplicationManagementServiceHolder.getStsAdminService()
                    .addTrustedService(wsTrustConfiguration.getAudience(), wsTrustConfiguration.getCertificateAlias());
        } catch (SecurityConfigException e) {
            // Error while adding WS Trust, we can't continue
            throw Utils.buildServerErrorResponse(e, "Error while adding WS-Trust configuration. " + e.getMessage());
        }
    }

    public static WSTrustConfiguration getWSTrustConfiguration(InboundAuthenticationRequestConfig inboundAuth) {

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

    public static void deleteWSTrustConfiguration(InboundAuthenticationRequestConfig inbound) {

        try {
            String trustedServiceAudience = inbound.getInboundAuthKey();
            ApplicationManagementServiceHolder.getStsAdminService().removeTrustedService(trustedServiceAudience);
        } catch (SecurityConfigException e) {
            throw Utils.buildServerErrorResponse(e, "Error while trying to rollback wsTrust configuration. "
                    + e.getMessage());
        }
    }
}
