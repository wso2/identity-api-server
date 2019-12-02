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
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundFunctions;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.IdentityOAuthClientException;
import org.wso2.carbon.identity.oauth.common.OAuthConstants;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildBadRequestError;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildServerError;

/**
 * Helper functions for OAuth inbound management.
 */
public class OAuthInboundFunctions {

    private OAuthInboundFunctions() {

    }

    public static InboundAuthenticationRequestConfig putOAuthInbound(ServiceProvider application,
                                                                     OpenIDConnectConfiguration oidcConfigModel) {

        // First we identify whether this is a insert or update.
        String currentClientId = InboundFunctions.getInboundAuthKey(application, StandardInboundProtocols.OAUTH2);
        try {
            if (currentClientId != null) {
                // This is an update.
                OAuthConsumerAppDTO oauthApp = ApplicationManagementServiceHolder.getOAuthAdminService()
                        .getOAuthApplicationData(currentClientId);
                // TODO: reject if client not equals previous
                oidcConfigModel.setClientId(oauthApp.getOauthConsumerKey());
                oidcConfigModel.setClientSecret(oauthApp.getOauthConsumerSecret());

                OAuthConsumerAppDTO appToUpdate = new ApiModelToOAuthConsumerApp().apply(oidcConfigModel);
                // Delete the current app.
                ApplicationManagementServiceHolder.getOAuthAdminService().updateConsumerApplication(appToUpdate);

                String updatedClientId = appToUpdate.getOauthConsumerKey();
                return createInboundAuthRequestConfig(updatedClientId);
            } else {
                return createOAuthInbound(oidcConfigModel);
            }

        } catch (IdentityOAuthAdminException e) {
            throw handleOAuthException(e);
        }
    }

    private static APIError handleOAuthException(IdentityOAuthAdminException e) {

        String message = "Error while Creating/Updating OAuth2/OpenIDConnect configuration. " + e.getMessage();
        if (e instanceof IdentityOAuthClientException) {
            return buildBadRequestError(message);
        }
        return buildServerError(message, e);
    }

    public static InboundAuthenticationRequestConfig createOAuthInbound(OpenIDConnectConfiguration oidcModel) {

        // Build a consumer apps object.
        OAuthConsumerAppDTO consumerApp = new ApiModelToOAuthConsumerApp().apply(oidcModel);
        try {
            OAuthConsumerAppDTO createdOAuthApp = ApplicationManagementServiceHolder.getOAuthAdminService()
                    .registerAndRetrieveOAuthApplicationData(consumerApp);

            return createInboundAuthRequestConfig(createdOAuthApp.getOauthConsumerKey());
        } catch (IdentityOAuthAdminException e) {
            throw handleOAuthException(e);
        }
    }

    private static InboundAuthenticationRequestConfig createInboundAuthRequestConfig(String clientId) {

        InboundAuthenticationRequestConfig oidcInbound = new InboundAuthenticationRequestConfig();
        oidcInbound.setInboundAuthType(StandardInboundProtocols.OAUTH2);
        oidcInbound.setInboundAuthKey(clientId);
        return oidcInbound;
    }

    public static OpenIDConnectConfiguration getOAuthConfiguration(InboundAuthenticationRequestConfig inboundAuth) {

        String clientId = inboundAuth.getInboundAuthKey();
        try {
            OAuthConsumerAppDTO oauthApp =
                    ApplicationManagementServiceHolder.getOAuthAdminService().getOAuthApplicationData(clientId);
            return new OAuthConsumerAppToApiModel().apply(oauthApp);

        } catch (IdentityOAuthAdminException e) {
            throw buildServerError("Error while retrieving oauth application for clientId: " + clientId, e);
        }
    }

    public static void deleteOAuthInbound(InboundAuthenticationRequestConfig inbound) {

        try {
            String consumerKey = inbound.getInboundAuthKey();
            ApplicationManagementServiceHolder.getOAuthAdminService().removeOAuthApplicationData(consumerKey);
        } catch (IdentityOAuthAdminException e) {
            throw buildServerError("Error while trying to rollback OAuth2/OpenIDConnect " +
                    "configuration." + e.getMessage(), e);
        }
    }

    public static OpenIDConnectConfiguration regenerateClientSecret(String clientId) {

        try {
            OAuthConsumerAppDTO oAuthConsumerAppDTO = ApplicationManagementServiceHolder.getOAuthAdminService()
                    .updateAndRetrieveOauthSecretKey(clientId);
            return new OAuthConsumerAppToApiModel().apply(oAuthConsumerAppDTO);
        } catch (IdentityOAuthAdminException e) {
            throw buildServerError("Error while regenerating client secret of oauth application.", e);
        }
    }

    public static void revokeOAuthClient(String clientId) {

        try {
            ApplicationManagementServiceHolder.getOAuthAdminService()
                    .updateConsumerAppState(clientId, OAuthConstants.OauthAppStates.APP_STATE_REVOKED);
        } catch (IdentityOAuthAdminException e) {
            throw buildServerError("Error while revoking oauth application.", e);
        }
    }
}
