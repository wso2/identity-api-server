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

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.AccessTokenConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ClientAuthenticationConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.HybridFlowConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.IdTokenConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.IdTokenEncryptionConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.OAuth2PKCEConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.OIDCLogoutConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PushAuthorizationRequestConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.RefreshTokenConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.RequestObjectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.RequestObjectEncryptionConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.SubjectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.SubjectTokenConfiguration;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Converts the backend model OAuthConsumerAppDTO into the corresponding API model object.
 */
public class OAuthConsumerAppToApiModel implements Function<OAuthConsumerAppDTO, OpenIDConnectConfiguration> {

    @Override
    public OpenIDConnectConfiguration apply(OAuthConsumerAppDTO oauthAppDTO) {

        return new OpenIDConnectConfiguration()
                .clientId(oauthAppDTO.getOauthConsumerKey())
                .clientSecret(oauthAppDTO.getOauthConsumerSecret())
                .state(OpenIDConnectConfiguration.StateEnum.valueOf(oauthAppDTO.getState()))
                .grantTypes(buildGrantTypeList(oauthAppDTO))
                .publicClient(oauthAppDTO.isBypassClientCredentials())
                .callbackURLs(getCallbackUrls(oauthAppDTO))
                .allowedOrigins(getAllowedOrigins(oauthAppDTO))
                .pkce(buildPKCEConfiguration(oauthAppDTO))
                .hybridFlow(buildHybridFlowConfiguration(oauthAppDTO))
                .accessToken(buildTokenConfiguration(oauthAppDTO))
                .refreshToken(buildRefreshTokenConfiguration(oauthAppDTO))
                .idToken(buildIdTokenConfiguration(oauthAppDTO))
                .logout(buildLogoutConfiguration(oauthAppDTO))
                .scopeValidators(getScopeValidators(oauthAppDTO))
                .validateRequestObjectSignature(oauthAppDTO.isRequestObjectSignatureValidationEnabled())
                .clientAuthentication(buildClientAuthenticationConfiguration(oauthAppDTO))
                .requestObject(buildRequestObjectConfiguration(oauthAppDTO))
                .pushAuthorizationRequest(buildPARAuthenticationConfiguration(oauthAppDTO))
                .subject(buildSubjectConfiguration(oauthAppDTO))
                .isFAPIApplication(oauthAppDTO.isFapiConformanceEnabled())
                .subjectToken(buildSubjectTokenConfiguration(oauthAppDTO));
    }

    private List<String> getScopeValidators(OAuthConsumerAppDTO oauthAppDTO) {

        return oauthAppDTO.getScopeValidators() != null ?
                Arrays.asList(oauthAppDTO.getScopeValidators()) : Collections.emptyList();
    }

    private OIDCLogoutConfiguration buildLogoutConfiguration(OAuthConsumerAppDTO oAuthConsumerAppDTO) {

        return new OIDCLogoutConfiguration()
                .backChannelLogoutUrl(oAuthConsumerAppDTO.getBackChannelLogoutUrl())
                .frontChannelLogoutUrl(oAuthConsumerAppDTO.getFrontchannelLogoutUrl());
    }

    private OAuth2PKCEConfiguration buildPKCEConfiguration(OAuthConsumerAppDTO oAuthConsumerAppDTO) {

        return new OAuth2PKCEConfiguration()
                .mandatory(oAuthConsumerAppDTO.getPkceMandatory())
                .supportPlainTransformAlgorithm(oAuthConsumerAppDTO.getPkceSupportPlain());
    }

    private HybridFlowConfiguration buildHybridFlowConfiguration(OAuthConsumerAppDTO oAuthConsumerAppDTO) {

        return new HybridFlowConfiguration()
                .enable(oAuthConsumerAppDTO.isHybridFlowEnabled())
                .responseType(oAuthConsumerAppDTO.getHybridFlowResponseType());
    }

    private AccessTokenConfiguration buildTokenConfiguration(OAuthConsumerAppDTO oAuthConsumerAppDTO) {

        return new AccessTokenConfiguration()
                .type(oAuthConsumerAppDTO.getTokenType())
                .userAccessTokenExpiryInSeconds(oAuthConsumerAppDTO.getUserAccessTokenExpiryTime())
                .applicationAccessTokenExpiryInSeconds(oAuthConsumerAppDTO.getApplicationAccessTokenExpiryTime())
                .bindingType(oAuthConsumerAppDTO.getTokenBindingType())
                .revokeTokensWhenIDPSessionTerminated(oAuthConsumerAppDTO
                        .isTokenRevocationWithIDPSessionTerminationEnabled())
                .validateTokenBinding(oAuthConsumerAppDTO.isTokenBindingValidationEnabled())
                .accessTokenAttributes(getAccessTokenAttributes(oAuthConsumerAppDTO));
    }

    private List<String> getAccessTokenAttributes(OAuthConsumerAppDTO oauthAppDTO) {

        return oauthAppDTO.getAccessTokenClaims() != null ?
                Arrays.asList(oauthAppDTO.getAccessTokenClaims()) : Collections.emptyList();
    }

    private RefreshTokenConfiguration buildRefreshTokenConfiguration(OAuthConsumerAppDTO oAuthConsumerAppDTO) {

        return new RefreshTokenConfiguration()
                .expiryInSeconds(oAuthConsumerAppDTO.getRefreshTokenExpiryTime())
                .renewRefreshToken(Boolean.parseBoolean(oAuthConsumerAppDTO.getRenewRefreshTokenEnabled()));
    }

    private IdTokenConfiguration buildIdTokenConfiguration(OAuthConsumerAppDTO oAuthConsumerAppDTO) {

        return new IdTokenConfiguration()
                .expiryInSeconds(oAuthConsumerAppDTO.getIdTokenExpiryTime())
                .audience(getAudiences(oAuthConsumerAppDTO))
                .idTokenSignedResponseAlg(oAuthConsumerAppDTO.getIdTokenSignatureAlgorithm())
                .encryption(buildIdTokenEncryptionConfiguration(oAuthConsumerAppDTO));
    }

    private List<String> getAudiences(OAuthConsumerAppDTO oAuthConsumerAppDTO) {

        if (oAuthConsumerAppDTO.getAudiences() == null) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(oAuthConsumerAppDTO.getAudiences());
        }
    }

    private IdTokenEncryptionConfiguration buildIdTokenEncryptionConfiguration(OAuthConsumerAppDTO appDTO) {

        return new IdTokenEncryptionConfiguration()
                .enabled(appDTO.isIdTokenEncryptionEnabled())
                .algorithm(StringUtils.equals(appDTO.getIdTokenEncryptionAlgorithm(), "null") ||
                        StringUtils.isBlank(appDTO.getIdTokenEncryptionAlgorithm()) ? "" :
                        appDTO.getIdTokenEncryptionAlgorithm())
                .method(StringUtils.equals(appDTO.getIdTokenEncryptionMethod(), "null") ||
                        StringUtils.isBlank(appDTO.getIdTokenEncryptionMethod()) ? "" :
                        appDTO.getIdTokenEncryptionMethod());
    }

    private List<String> buildGrantTypeList(OAuthConsumerAppDTO oauthApp) {

        if (StringUtils.isNotBlank(oauthApp.getGrantTypes())) {
            return Arrays.asList(oauthApp.getGrantTypes().split("\\s+"));
        } else {
            return Collections.emptyList();
        }
    }

    private List<String> getAllowedOrigins(OAuthConsumerAppDTO oauthApp) {

        return Collections.emptyList();
    }

    private List<String> getCallbackUrls(OAuthConsumerAppDTO oauthApp) {

        List<String> callbackUris = new ArrayList<>();
        if (StringUtils.isNotBlank(oauthApp.getCallbackUrl())) {
            callbackUris.add(oauthApp.getCallbackUrl());
        }
        return callbackUris;
    }

    private ClientAuthenticationConfiguration buildClientAuthenticationConfiguration(OAuthConsumerAppDTO appDTO) {

        return new ClientAuthenticationConfiguration()
                .tokenEndpointAuthMethod(appDTO.getTokenEndpointAuthMethod())
                .tokenEndpointAuthSigningAlg(appDTO.getTokenEndpointAuthSignatureAlgorithm())
                .tokenEndpointAllowReusePvtKeyJwt(appDTO.isTokenEndpointAllowReusePvtKeyJwt())
                .tlsClientAuthSubjectDn(appDTO.getTlsClientAuthSubjectDN());
    }

    private RequestObjectConfiguration buildRequestObjectConfiguration(OAuthConsumerAppDTO oAuthConsumerAppDTO) {

        return new RequestObjectConfiguration()
                .requestObjectSigningAlg(oAuthConsumerAppDTO.getRequestObjectSignatureAlgorithm())
                .encryption(buildRequestObjectEncryptionConfiguration(oAuthConsumerAppDTO));
    }

    private RequestObjectEncryptionConfiguration buildRequestObjectEncryptionConfiguration(OAuthConsumerAppDTO appDTO) {

        String algorithm = appDTO.getRequestObjectEncryptionAlgorithm();
        String method = appDTO.getRequestObjectEncryptionMethod();

        if (StringUtils.equals(algorithm, "null") || StringUtils.isBlank(algorithm)) {
            algorithm = "";
        }
        if (StringUtils.equals(method, "null") || StringUtils.isBlank(method)) {
            method = "";
        }
        return new RequestObjectEncryptionConfiguration()
                .algorithm(algorithm)
                .method(method);
    }

    private PushAuthorizationRequestConfiguration buildPARAuthenticationConfiguration(OAuthConsumerAppDTO appDTO) {

        return new PushAuthorizationRequestConfiguration()
                .requirePushAuthorizationRequest(appDTO.getRequirePushedAuthorizationRequests());
    }

    private SubjectConfiguration buildSubjectConfiguration(OAuthConsumerAppDTO oAuthConsumerAppDTO) {

        return new SubjectConfiguration()
                .subjectType(oAuthConsumerAppDTO.getSubjectType())
                .sectorIdentifierUri(oAuthConsumerAppDTO.getSectorIdentifierURI());
    }

    private SubjectTokenConfiguration buildSubjectTokenConfiguration(OAuthConsumerAppDTO oAuthConsumerAppDTO) {

        return new SubjectTokenConfiguration()
                .enable(oAuthConsumerAppDTO.isSubjectTokenEnabled())
                .applicationSubjectTokenExpiryInSeconds(oAuthConsumerAppDTO.getSubjectTokenExpiryTime());
    }
}
