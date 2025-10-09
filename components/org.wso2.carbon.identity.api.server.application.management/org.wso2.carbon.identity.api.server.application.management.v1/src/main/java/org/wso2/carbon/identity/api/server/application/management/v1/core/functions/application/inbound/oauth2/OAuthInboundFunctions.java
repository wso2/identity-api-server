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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.mgt.inbound.InboundFunctions;
import org.wso2.carbon.identity.application.mgt.inbound.dto.InboundProtocolConfigurationDTO;
import org.wso2.carbon.identity.authorization.common.AuthorizationUtil;
import org.wso2.carbon.identity.authorization.common.exception.ForbiddenException;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.cors.mgt.core.exception.CORSManagementServiceClientException;
import org.wso2.carbon.identity.cors.mgt.core.exception.CORSManagementServiceException;
import org.wso2.carbon.identity.cors.mgt.core.model.CORSOrigin;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.IdentityOAuthClientException;
import org.wso2.carbon.identity.oauth.common.OAuthConstants;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.VIEW_APPLICATION_CLIENT_SECRET_OPERATION;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildBadRequestError;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildServerError;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols.OAUTH2;

/**
 * Helper functions for OAuth inbound management.
 */
public class OAuthInboundFunctions {

    private static final Log log = LogFactory.getLog(OAuthInboundFunctions.class);

    private OAuthInboundFunctions() {

    }
    
    /**
     * This method used to create or update OAuth inbound authentication request config by calling the AuthAdminService.
     * Using this method is not recommended since framework will handle the update of OAuth inbound authentication
     * configurations.
     *
     * @param application     Service provider
     * @param oidcConfigModel OpenIDConnectConfiguration.
     * @return InboundAuthenticationRequestConfig.
     */
    @Deprecated
    public static InboundAuthenticationRequestConfig putOAuthInbound(ServiceProvider application,
                                                                     OpenIDConnectConfiguration oidcConfigModel) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        List<String> existingCORSOrigins = null;

        // First we identify whether this is a insert or update.
        try {
            Optional<String> optionalInboundAuthKey = InboundFunctions.getInboundAuthKey(application, OAUTH2);

            // Retrieve the existing CORS origins for the application.
            existingCORSOrigins = ApplicationManagementServiceHolder.getCorsManagementService()
                    .getApplicationCORSOrigins(application.getApplicationResourceId(), tenantDomain)
                    .stream().map(CORSOrigin::getOrigin).collect(Collectors.toList());

            // Update the CORS origins.
            List<String> corsOrigins = oidcConfigModel.getAllowedOrigins();
            ApplicationManagementServiceHolder.getCorsManagementService()
                    .setCORSOrigins(application.getApplicationResourceId(), corsOrigins, tenantDomain);

            if (optionalInboundAuthKey.isPresent()) {
                // Update an existing application.
                OAuthConsumerAppDTO oauthApp = ApplicationManagementServiceHolder.getOAuthAdminService
                        ().getOAuthApplicationData(optionalInboundAuthKey.get());

                if (!StringUtils.equals(oauthApp.getOauthConsumerKey(), oidcConfigModel.getClientId())) {
                    throw buildBadRequestError("Invalid ClientID provided for update.");
                }

                if (!StringUtils.equals(oauthApp.getOauthConsumerSecret(), oidcConfigModel.getClientSecret())) {
                    throw buildBadRequestError("Invalid ClientSecret provided for update.");
                }

                OAuthConsumerAppDTO appToUpdate = new ApiModelToOAuthConsumerApp().apply(application
                        .getApplicationName(), oidcConfigModel);
                ApplicationManagementServiceHolder.getOAuthAdminService().updateConsumerApplication(appToUpdate);

                String updatedClientId = appToUpdate.getOauthConsumerKey();
                return createInboundAuthRequestConfig(updatedClientId);
            } else {
                // Create a new application.
                return createOAuthInbound(application.getApplicationName(), oidcConfigModel);
            }

        } catch (IdentityOAuthAdminException e) {
            /*
            If an IdentityOAuthAdminException exception is thrown after the CORS update, then the application
            update has failed. Therefore rollback the update on CORS origins.
             */
            try {
                ApplicationManagementServiceHolder.getCorsManagementService().setCORSOrigins
                        (application.getApplicationResourceId(), existingCORSOrigins, tenantDomain);
            } catch (CORSManagementServiceException corsManagementServiceException) {
                throw handleException(e);
            }
            throw handleException(e);
        } catch (CORSManagementServiceException e) {
            throw handleException(e);
        }
    }
    
    /**
     * Get the InboundProtocolConfigurationDTO from the OpenIDConnectConfiguration.
     *
     * @param application     Service provider.
     * @param oidcConfigModel OpenIDConnectConfiguration.
     * @return InboundProtocolConfigurationDTO.
     */
    public static InboundProtocolConfigurationDTO getInboundProtocolConfig(
            ServiceProvider application, OpenIDConnectConfiguration oidcConfigModel) {
        
        return new ApiModelToOAuthConsumerApp().apply(application.getApplicationName(), oidcConfigModel);
    }
    
    private static APIError handleException(Exception e) {

        String message = "Error while updating OpenIDConnect configuration. " + e.getMessage();
        if (e instanceof IdentityOAuthClientException) {
            String errorCode = ((IdentityOAuthClientException) e).getErrorCode();
            return buildBadRequestError(errorCode, message);
        } else if (e instanceof CORSManagementServiceClientException) {
            return buildBadRequestError(message);
        }
        return buildServerError(message, e);
    }

    /**
     * @deprecated This method will be removed in the upcoming major release.
     * Because, service provider name should be passed to create oauth app name.
     * Use {@link #createOAuthInbound(String, OpenIDConnectConfiguration)} instead.
     */
    @Deprecated
    public static InboundAuthenticationRequestConfig createOAuthInbound(OpenIDConnectConfiguration oidcModel) {

        return createOAuthInbound(UUID.randomUUID().toString(), oidcModel);
    }
    
    /**
     * Create OAuth inbound authentication request config by calling the AuthAdminService. It's not recommended to use
     * this method since framework will handle the creation of OAuth inbound authentication configurations.
     *
     * @param appName   Name of the application.
     * @param oidcModel OpenIDConnectConfiguration.
     * @return InboundAuthenticationRequestConfig.
     */
    @Deprecated
    public static InboundAuthenticationRequestConfig createOAuthInbound(String appName, OpenIDConnectConfiguration
            oidcModel) {

        // Build a consumer apps object.
        OAuthConsumerAppDTO consumerApp = new ApiModelToOAuthConsumerApp().apply(appName, oidcModel);
        try {
            OAuthConsumerAppDTO createdOAuthApp = ApplicationManagementServiceHolder.getOAuthAdminService()
                    .registerAndRetrieveOAuthApplicationData(consumerApp);

            return createInboundAuthRequestConfig(createdOAuthApp.getOauthConsumerKey());
        } catch (IdentityOAuthAdminException e) {
            throw handleException(e);
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

            OpenIDConnectConfiguration openIDConnectConfiguration = new OAuthConsumerAppToApiModel().apply(oauthApp);

            removeClientSecretIfUnauthorized(openIDConnectConfiguration);

            // Set CORS origins as allowed domains.
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            String applicationResourceId = ApplicationManagementServiceHolder.getApplicationManagementService()
                    .getServiceProviderByClientId(clientId, OAUTH2, tenantDomain).getApplicationResourceId();
            List<CORSOrigin> corsOriginList = ApplicationManagementServiceHolder.getCorsManagementService()
                    .getApplicationCORSOrigins(applicationResourceId, tenantDomain);
            openIDConnectConfiguration.setAllowedOrigins(corsOriginList.stream().map(CORSOrigin::getOrigin)
                    .collect(Collectors.toList()));

            return openIDConnectConfiguration;

        } catch (IdentityOAuthAdminException | IdentityApplicationManagementException
                | CORSManagementServiceException e) {
            throw buildServerError("Error while retrieving oauth application for clientId: " + clientId, e);
        }
    }

    private static void removeClientSecretIfUnauthorized(OpenIDConnectConfiguration openIDConnectConfiguration) {

        if (Boolean.parseBoolean(IdentityUtil.getProperty(
                ApplicationManagementConstants.SKIP_ENFORCE_AUTHORIZED_API_UPDATE_PERMISSION))) {
            return;
        }

        try {
            AuthorizationUtil.validateOperationScopes(VIEW_APPLICATION_CLIENT_SECRET_OPERATION);
        } catch (ForbiddenException e) {
            // Removing the client secret if operation scope is not present.
            openIDConnectConfiguration.setClientSecret(null);
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
            OAuthConsumerAppDTO oAuthConsumerAppDTO = ApplicationManagementServiceHolder
                    .getOAuthAdminService().updateAndRetrieveOauthSecretKey(clientId);
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

    /**
     * Set the CORS origins given in the OIDC configurations to the given application.
     *
     * @param applicationId   application id
     * @param oidcConfigModel oidc configurations of the application.
     * @throws CORSManagementServiceException if CORS origin update fails.
     */
    public static void updateCorsOrigins(String applicationId, OpenIDConnectConfiguration oidcConfigModel)
            throws CORSManagementServiceException {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        // Update the CORS origins.
        List<String> corsOrigins = oidcConfigModel.getAllowedOrigins();
        if (!CollectionUtils.isEmpty(corsOrigins)) {
            ApplicationManagementServiceHolder.getCorsManagementService()
                    .setCORSOrigins(applicationId, corsOrigins, tenantDomain);
        }
    }
}
