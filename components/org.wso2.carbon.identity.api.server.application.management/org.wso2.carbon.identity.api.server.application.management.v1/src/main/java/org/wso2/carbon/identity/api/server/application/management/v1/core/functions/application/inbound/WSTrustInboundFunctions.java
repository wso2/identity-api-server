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
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.security.SecurityConfigException;
import org.wso2.carbon.security.sts.service.util.TrustedServiceData;

import java.util.Arrays;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildBadRequestError;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildNotFoundError;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildServerError;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.WS_TRUST;

/**
 * Helper functions for WSTrust inbound management.
 */
public class WSTrustInboundFunctions {

    private static final String ERROR_CODE = "60504";
    private static final String ERROR_MESSAGE = "WS-Trust protocol is not supported.";
    private static final String ERROR_DESCRIPTION = "STS admin service is unavailable at the moment.";

    private WSTrustInboundFunctions() {

    }

    public static InboundAuthenticationRequestConfig putWSTrustConfiguration(ServiceProvider application,
                                                                             WSTrustConfiguration wsTrustModel) {

        String inboundAuthKey = InboundFunctions.getInboundAuthKey(application, WS_TRUST);
        try {
            if (inboundAuthKey != null) {
                if (wsTrustAudienceChanged(wsTrustModel, inboundAuthKey)) {
                    // We do not allow the inbound unique key to be changed during an update.
                    throw buildBadRequestError("Invalid audience value provided for update.");
                }
                // Check if WS-Trust is deployed.
                if (ApplicationManagementServiceHolder.getStsAdminService() != null) {
                    ApplicationManagementServiceHolder.getStsAdminService()
                            .removeTrustedService(inboundAuthKey);
                } else {
                    // Throw 404 error since the WS-Trust connector is not available.
                    throw buildNotFoundError(ERROR_CODE, ERROR_MESSAGE, ERROR_DESCRIPTION);
                }
            }

            return createWsTrustInbound(wsTrustModel);
        } catch (SecurityConfigException e) {
            String applicationId = application.getApplicationResourceId();
            throw buildServerError("Error while creating/updating WSTrust inbound of application: " + applicationId, e);
        }
    }

    private static boolean wsTrustAudienceChanged(WSTrustConfiguration wsTrustModel, String inboundAuthKey) {

        return !StringUtils.equals(inboundAuthKey, wsTrustModel.getAudience());
    }

    public static InboundAuthenticationRequestConfig createWsTrustInbound(WSTrustConfiguration wsTrustConfiguration) {

        try {
            // Check if WS-Trust is deployed.
            if (ApplicationManagementServiceHolder.getStsAdminService() != null) {
                ApplicationManagementServiceHolder.getStsAdminService()
                        .addTrustedService(wsTrustConfiguration.getAudience(),
                                wsTrustConfiguration.getCertificateAlias());

                InboundAuthenticationRequestConfig wsTrustInbound = new InboundAuthenticationRequestConfig();
                wsTrustInbound.setInboundAuthType(WS_TRUST);
                wsTrustInbound.setInboundAuthKey(wsTrustConfiguration.getAudience());
                return wsTrustInbound;
            } else {
                // Throw 401 error since the WS-Trust connector is not available.
                throw buildBadRequestError(ERROR_DESCRIPTION);
            }

        } catch (SecurityConfigException e) {
            // Error while adding WS Trust, we can't continue.
            throw buildServerError("Error while adding WSTrust configuration. " + e.getMessage(), e);
        }
    }

    public static WSTrustConfiguration getWSTrustConfiguration(InboundAuthenticationRequestConfig inboundAuth) {

        String audience = inboundAuth.getInboundAuthKey();
        try {

            TrustedServiceData[] trustedServices;

            // Check if WS-Trust is deployed.
            if (ApplicationManagementServiceHolder.getStsAdminService() != null) {
                trustedServices =
                        ApplicationManagementServiceHolder.getStsAdminService().getTrustedServices();
            } else {
                // Throw 404 error since the WS-Trust connector is not available.
                throw buildNotFoundError(ERROR_CODE, ERROR_MESSAGE, ERROR_DESCRIPTION);
            }

            return Arrays.stream(trustedServices)
                    .filter(trustedServiceData -> StringUtils.equals(trustedServiceData.getServiceAddress(), audience))
                    .findAny()
                    .map(trustedServiceData -> new WSTrustConfiguration()
                            .audience(trustedServiceData.getServiceAddress())
                            .certificateAlias(trustedServiceData.getCertAlias()))
                    .orElse(null);

        } catch (SecurityConfigException e) {
            throw buildServerError("Error while retrieving WSTrust configuration for audience: " + audience, e);
        }
    }

    public static void deleteWSTrustConfiguration(InboundAuthenticationRequestConfig inbound) {

        try {
            String trustedServiceAudience = inbound.getInboundAuthKey();

            // Check if WS-Trust is deployed.
            if (ApplicationManagementServiceHolder.getStsAdminService() != null) {
                ApplicationManagementServiceHolder.getStsAdminService()
                        .removeTrustedService(trustedServiceAudience);
            } else {
                // Throw 404 error since the WS-Trust connector is not available.
                throw buildNotFoundError(ERROR_CODE, ERROR_MESSAGE, ERROR_DESCRIPTION);
            }

        } catch (SecurityConfigException e) {
            throw buildServerError("Error while trying to rollback WSTrust configuration. " + e.getMessage(), e);
        }
    }
}
