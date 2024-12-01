/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.authenticators.v1.impl;

import org.wso2.carbon.identity.api.server.authenticators.common.Constants;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.Authenticator;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.Endpoint;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.UserDefinedLocalAuthenticatorCreation;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.UserDefinedLocalAuthenticatorUpdate;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.application.common.exception.AuthenticatorMgtClientException;
import org.wso2.carbon.identity.application.common.model.LocalAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.UserDefinedAuthenticatorEndpointConfig;
import org.wso2.carbon.identity.application.common.model.UserDefinedLocalAuthenticatorConfig;
import org.wso2.carbon.identity.base.AuthenticatorPropertyConstants;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.authenticators.common.Constants.AUTHENTICATOR_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.authenticators.common.Constants.ErrorMessage.ERROR_CODE_ENDPOINT_CONFIG;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;

/**
 * The factory class for building user defined local authenticator configuration related models.
 */
public class LocalAuthenticatorConfigBuilderFactory {

    /**
     * Builds the authenticator model from the UserDefinedLocalAuthenticatorConfig.
     *
     * @param config The user defined local authenticator configuration.
     * @return The authenticator model.
     */
    public static Authenticator build(UserDefinedLocalAuthenticatorConfig config) {

        Authenticator authenticator = new Authenticator();
        authenticator.setName(config.getName());
        authenticator.setDisplayName(config.getDisplayName());
        authenticator.setIsEnabled(config.isEnabled());
        authenticator.setDefinedBy(Authenticator.DefinedByEnum.USER);
        authenticator.setType(Authenticator.TypeEnum.LOCAL);
        authenticator.setSelf(ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT +
                AUTHENTICATOR_PATH_COMPONENT + "/%s", config.getName())).toString());

        return authenticator;
    }

    /**
     * Builds the UserDefinedLocalAuthenticatorConfig from the user defined local authenticator creation request.
     *
     * @param config The user defined local authenticator creation request.
     * @return The user defined local authenticator configuration.
     * @throws AuthenticatorMgtClientException If an error occurs while building the configuration.
     */
    public static UserDefinedLocalAuthenticatorConfig build(UserDefinedLocalAuthenticatorCreation config)
            throws AuthenticatorMgtClientException {

        UserDefinedLocalAuthenticatorConfig authConfig = new UserDefinedLocalAuthenticatorConfig(
                AuthenticatorPropertyConstants.AuthenticationType.valueOf(
                        config.getAuthenticationType().toString()));
        authConfig.setName(config.getName());
        authConfig.setDisplayName(config.getDisplayName());
        authConfig.setEnabled(config.getIsEnabled());
        authConfig.setEndpointConfig(buildEndpointConfig(config.getEndpoint()));

        return authConfig;
    }

    /**
     * Builds the UserDefinedLocalAuthenticatorConfig from the user defined local authenticator update request.
     *
     * @param config The user defined local authenticator update request.
     * @param existingConfig The existing user defined local authenticator configuration.
     * @return The user defined local authenticator configuration.
     * @throws AuthenticatorMgtClientException If an error occurs while building the configuration.
     */
    public static UserDefinedLocalAuthenticatorConfig build(UserDefinedLocalAuthenticatorUpdate config,
                LocalAuthenticatorConfig existingConfig) throws AuthenticatorMgtClientException {

        UserDefinedLocalAuthenticatorConfig authConfig = new UserDefinedLocalAuthenticatorConfig(
                resolveAuthenticationType(existingConfig));
        authConfig.setName(existingConfig.getName());
        authConfig.setDisplayName(config.getDisplayName());
        authConfig.setEnabled(config.getIsEnabled());
        authConfig.setEndpointConfig(buildEndpointConfig(config.getEndpoint()));

        return authConfig;
    }

    private static UserDefinedAuthenticatorEndpointConfig buildEndpointConfig(Endpoint endpointConfig)
            throws AuthenticatorMgtClientException {

        try {
            UserDefinedAuthenticatorEndpointConfig.UserDefinedAuthenticatorEndpointConfigBuilder endpointConfigBuilder =
                    new UserDefinedAuthenticatorEndpointConfig.UserDefinedAuthenticatorEndpointConfigBuilder();
            endpointConfigBuilder.uri(endpointConfig.getUri());
            endpointConfigBuilder.authenticationType(endpointConfig.getAuthentication().getType().toString());
            endpointConfigBuilder.authenticationProperties(endpointConfig.getAuthentication().getProperties()
                    .entrySet().stream().collect(Collectors.toMap(
                            Map.Entry::getKey, entry -> entry.getValue().toString())));
            return endpointConfigBuilder.build();
        } catch (NoSuchElementException e) {
            Constants.ErrorMessage error = ERROR_CODE_ENDPOINT_CONFIG;
            throw new AuthenticatorMgtClientException(error.getCode(), error.getMessage(), error.getMessage());
        }
    }

    private static AuthenticatorPropertyConstants.AuthenticationType resolveAuthenticationType(
            LocalAuthenticatorConfig config) {

        if (Arrays.asList(config.getTags()).contains("2FA")) {
            return AuthenticatorPropertyConstants.AuthenticationType.VERIFICATION;
        } else {
            return AuthenticatorPropertyConstants.AuthenticationType.IDENTIFICATION;
        }
    }
}