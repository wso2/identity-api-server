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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.oauth2;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.AccessTokenConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.IdTokenConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.OAuth2PKCEConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.OIDCLogoutConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.RefreshTokenConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.oauth.common.OAuthConstants;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Converts OpenIDConnectConfiguration api model to OAuthConsumerAppDTO.
 */
public class ApiModelToOAuthConsumerApp implements Function<OpenIDConnectConfiguration, OAuthConsumerAppDTO> {

    @Override
    public OAuthConsumerAppDTO apply(OpenIDConnectConfiguration oidcModel) {

        OAuthConsumerAppDTO consumerAppDTO = new OAuthConsumerAppDTO();

        consumerAppDTO.setApplicationName(UUID.randomUUID().toString());
        consumerAppDTO.setOauthConsumerKey(oidcModel.getClientId());
        consumerAppDTO.setOauthConsumerSecret(oidcModel.getClientSecret());

        consumerAppDTO.setCallbackUrl(getCallbackUrl(oidcModel.getCallbackURLs()));

        consumerAppDTO.setOAuthVersion(OAuthConstants.OAuthVersions.VERSION_2);
        consumerAppDTO.setUsername(ContextLoader.getUsernameFromContext());

        consumerAppDTO.setGrantTypes(getGrantTypes(oidcModel));
        consumerAppDTO.setScopeValidators(getScopeValidators(oidcModel));

        consumerAppDTO.setBypassClientCredentials(oidcModel.getPublicClient());
        consumerAppDTO.setRequestObjectSignatureValidationEnabled(oidcModel.getValidateRequestObjectSignature());

        updateAllowedOrigins(consumerAppDTO, oidcModel.getAllowedOrigins());
        updatePkceConfigurations(consumerAppDTO, oidcModel.getPkce());
        updateAccessTokenConfiguration(consumerAppDTO, oidcModel.getAccessToken());
        updateRefreshTokenConfiguration(consumerAppDTO, oidcModel.getRefreshToken());
        updateIdTokenConfiguration(consumerAppDTO, oidcModel.getIdToken());
        updateOidcLogoutConfiguration(consumerAppDTO, oidcModel.getLogout());

        return consumerAppDTO;
    }

    private String getGrantTypes(OpenIDConnectConfiguration oidcModel) {

        if (CollectionUtils.isEmpty(oidcModel.getGrantTypes())) {
            return null;
        } else {
            return StringUtils.join(oidcModel.getGrantTypes(), " ");
        }
    }

    private void updateOidcLogoutConfiguration(OAuthConsumerAppDTO consumerAppDTO, OIDCLogoutConfiguration logout) {

        if (logout != null) {
            consumerAppDTO.setBackChannelLogoutUrl(logout.getBackChannelLogoutUrl());
            consumerAppDTO.setFrontchannelLogoutUrl(logout.getFrontChannelLogoutUrl());
        }
    }

    private void updateIdTokenConfiguration(OAuthConsumerAppDTO consumerAppDTO, IdTokenConfiguration idToken) {

        if (idToken != null) {
            consumerAppDTO.setIdTokenExpiryTime(idToken.getExpiryInSeconds());
            consumerAppDTO.setAudiences(Optional.ofNullable(idToken.getAudience())
                    .map(audiences -> audiences.toArray(new String[0]))
                    .orElse(new String[0])
            );
        }
    }

    private void updateRefreshTokenConfiguration(OAuthConsumerAppDTO consumerAppDTO,
                                                 RefreshTokenConfiguration refreshToken) {

        if (refreshToken != null) {
            consumerAppDTO.setRefreshTokenExpiryTime(refreshToken.getExpiryInSeconds());
            String renewRefreshToken = refreshToken.getRenewRefreshToken() != null ?
                    String.valueOf(refreshToken.getRenewRefreshToken()) : null;
            consumerAppDTO.setRenewRefreshTokenEnabled(renewRefreshToken);
        }
    }

    private void updateAllowedOrigins(OAuthConsumerAppDTO consumerAppDTO, List<String> allowedOrigins) {

        if (CollectionUtils.isNotEmpty(allowedOrigins)) {
            throw Utils.buildNotImplementedErrorResponse("Allowed origins are not supported yet.");
        }
    }

    private void updateAccessTokenConfiguration(OAuthConsumerAppDTO consumerAppDTO,
                                                AccessTokenConfiguration accessToken) {

        if (accessToken != null) {
            consumerAppDTO.setTokenType(accessToken.getType());
            consumerAppDTO.setUserAccessTokenExpiryTime(accessToken.getUserAccessTokenExpiryInSeconds());
            consumerAppDTO.setApplicationAccessTokenExpiryTime(accessToken.getApplicationAccessTokenExpiryInSeconds());
        }
    }

    private void updatePkceConfigurations(OAuthConsumerAppDTO consumerAppDTO, OAuth2PKCEConfiguration pkce) {

        if (pkce != null) {
            consumerAppDTO.setPkceMandatory(pkce.getMandatory());
            consumerAppDTO.setPkceSupportPlain(pkce.getSupportPlainTransformAlgorithm());
        }
    }

    private String[] getScopeValidators(OpenIDConnectConfiguration oidcModel) {

        return Optional.ofNullable(oidcModel.getScopeValidators())
                .map(validators -> validators.toArray(new String[0]))
                .orElse(new String[0]);
    }

    private String getCallbackUrl(List<String> callbackURLs) {

        if (CollectionUtils.isNotEmpty(callbackURLs)) {
            // We can't support multiple callback URLs at the moment. So we need to send a server error.
            if (callbackURLs.size() > 1) {
                throw Utils.buildNotImplementedErrorResponse("Multiple callbacks for OAuth2 are not supported yet. " +
                        "Please use regex to define multiple callbacks.");
            } else if (callbackURLs.size() == 1) {
                return callbackURLs.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
