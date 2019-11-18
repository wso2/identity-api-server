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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.oauth2;

import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundUtils;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildServerErrorResponse;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundUtils.updateOrInsertInbound;

/**
 * Helper functions for OAuth inbound management.
 */
public class OAuthInboundUtils {

    public static void putOAuthInbound(ServiceProvider application,
                                       OpenIDConnectConfiguration oidcConfigModel) {

        // First we identify whether this is a insert or update.
        String currentClientId = InboundUtils.getInboundAuthKey(application, StandardInboundProtocols.OAUTH2);
        String updatedClientId;
        try {
            if (currentClientId != null) {
                if (oidcConfigModel.getClientId() == null || oidcConfigModel.getClientSecret() == null) {
                    // Get the current clientId and secret since they are not specified in the request.
                    OAuthConsumerAppDTO oauthApp = ApplicationManagementServiceHolder.getOAuthAdminService()
                            .getOAuthApplicationData(currentClientId);

                    oidcConfigModel.setClientId(oauthApp.getOauthConsumerKey());
                    oidcConfigModel.setClientSecret(oauthApp.getOauthConsumerSecret());
                }

                OAuthConsumerAppDTO appToUpdate = new ApiModelToOAuthConsumerApp().apply(oidcConfigModel);
                updatedClientId = appToUpdate.getOauthConsumerKey();
                // Delete the current app.
                ApplicationManagementServiceHolder.getOAuthAdminService().updateConsumerApplication(appToUpdate);
            } else {

                OAuthConsumerAppDTO addedConsumerApp = createOAuthInbound(oidcConfigModel);
                updatedClientId = addedConsumerApp.getOauthConsumerKey();
            }

            // Update the inbound details.
            InboundAuthenticationRequestConfig oidcInbound = new InboundAuthenticationRequestConfig();
            oidcInbound.setInboundAuthType(StandardInboundProtocols.OAUTH2);
            oidcInbound.setInboundAuthKey(updatedClientId);
            updateOrInsertInbound(application, StandardInboundProtocols.OAUTH2, oidcInbound);

        } catch (IdentityOAuthAdminException e) {
            String applicationId = application.getApplicationResourceId();
            throw buildServerErrorResponse(e,
                    "Error while creating/updating OAuth inbound of application: " + applicationId);
        }
    }

    public static OAuthConsumerAppDTO createOAuthInbound(OpenIDConnectConfiguration oidcModel) {

        // Build a consumer apps object.
        OAuthConsumerAppDTO consumerApp = new ApiModelToOAuthConsumerApp().apply(oidcModel);
        try {
            return ApplicationManagementServiceHolder.getOAuthAdminService()
                    .registerAndRetrieveOAuthApplicationData(consumerApp);
        } catch (IdentityOAuthAdminException e) {
            throw buildServerErrorResponse(e,
                    "Error creating OAuth2/OpenIDConnect configuration. " + e.getMessage());
        }
    }

    public static OpenIDConnectConfiguration getOAuthConfiguration(InboundAuthenticationRequestConfig inboundAuth) {

        String clientId = inboundAuth.getInboundAuthKey();
        try {
            OAuthConsumerAppDTO oauthApp =
                    ApplicationManagementServiceHolder.getOAuthAdminService().getOAuthApplicationData(clientId);
            return new OAuthConsumerAppToApiModel().apply(oauthApp);

        } catch (IdentityOAuthAdminException e) {

            throw buildServerErrorResponse(e, "Error while retrieving oauth application for clientId: " + clientId);
        }
    }

    public static void deleteOAuthInbound(InboundAuthenticationRequestConfig inbound) {

        try {
            String consumerKey = inbound.getInboundAuthKey();
            ApplicationManagementServiceHolder.getOAuthAdminService().removeOAuthApplicationData(consumerKey);
        } catch (IdentityOAuthAdminException e) {
            throw Utils.buildServerErrorResponse(e, "Error while trying to rollback OAuth2/OpenIDConnect " +
                    "configuration." + e.getMessage());
        }
    }

    public static OpenIDConnectConfiguration regenerateClientSecret(String clientId) {

        try {
            OAuthConsumerAppDTO oAuthConsumerAppDTO = ApplicationManagementServiceHolder.getOAuthAdminService()
                    .updateAndRetrieveOauthSecretKey(clientId);
            return new OAuthConsumerAppToApiModel().apply(oAuthConsumerAppDTO);
        } catch (IdentityOAuthAdminException e) {
            throw Utils.buildServerErrorResponse(e, "Error while regenerating client secret of oauth application.");
        }
    }

    // TODO: Updating a state of an OAuth application needs to be implemented.
}
