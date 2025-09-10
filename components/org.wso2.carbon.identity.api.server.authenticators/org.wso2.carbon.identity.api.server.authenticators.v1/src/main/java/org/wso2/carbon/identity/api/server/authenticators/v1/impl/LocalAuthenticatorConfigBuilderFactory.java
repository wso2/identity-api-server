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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.AuthenticationType;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.Authenticator;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.Endpoint;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.UserDefinedLocalAuthenticatorCreation;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.UserDefinedLocalAuthenticatorUpdate;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.application.common.exception.AuthenticatorMgtClientException;
import org.wso2.carbon.identity.application.common.model.LocalAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.UserDefinedAuthenticatorEndpointConfig;
import org.wso2.carbon.identity.application.common.model.UserDefinedLocalAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.util.AuthenticatorMgtExceptionBuilder.AuthenticatorMgtError;
import org.wso2.carbon.identity.base.AuthenticatorPropertyConstants;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.authenticators.common.Constants.CONFIGS_AUTHENTICATOR_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLEncode;

/**
 * The factory class for building user defined local authenticator configuration related models.
 */
public class LocalAuthenticatorConfigBuilderFactory {

    private static final Log log = LogFactory.getLog(LocalAuthenticatorConfigBuilderFactory.class);
    private static final String TAG_2FA = "2FA";

    /**
     * Builds the authenticator model from the UserDefinedLocalAuthenticatorConfig.
     *
     * @param config The user defined local authenticator configuration.
     * @return The authenticator model.
     */
    public static Authenticator build(UserDefinedLocalAuthenticatorConfig config) {

        if (config == null) {
            if (log.isDebugEnabled()) {
                log.debug("UserDefinedLocalAuthenticatorConfig is null, returning null");
            }
            return null;
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Building authenticator model from UserDefinedLocalAuthenticatorConfig: " + config.getName());
        }
        
        Authenticator authenticator = new Authenticator();
        String authenticatorId = base64URLEncode(config.getName());
        authenticator.setName(config.getName());
        authenticator.setId(authenticatorId);
        authenticator.setDisplayName(config.getDisplayName());
        authenticator.setImage(config.getImageUrl());
        authenticator.description(config.getDescription());
        authenticator.setIsEnabled(config.isEnabled());
        authenticator.setDefinedBy(Authenticator.DefinedByEnum.USER);
        authenticator.setType(Authenticator.TypeEnum.LOCAL);
        authenticator.setTags(Arrays.asList(config.getTags()));
        authenticator.setSelf(ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT +
                CONFIGS_AUTHENTICATOR_PATH_COMPONENT, authenticatorId)).toString());

        if (log.isDebugEnabled()) {
            log.debug("Successfully built authenticator model for: " + config.getName());
        }
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

        if (config == null) {
            throw new IllegalArgumentException("Authentication configuration cannot be null");
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Building UserDefinedLocalAuthenticatorConfig from creation request: " + 
                    config.getName());
        }

        validateUserDefinedLocalAuthenticatorConfig(config);
        String authenticationType = AuthenticatorPropertyConstants.AuthenticationType.IDENTIFICATION.toString();
        if (config.getAuthenticationType() != null) {
            authenticationType = config.getAuthenticationType().toString();
        }
        UserDefinedLocalAuthenticatorConfig authConfig = new UserDefinedLocalAuthenticatorConfig(
                AuthenticatorPropertyConstants.AuthenticationType.valueOf(authenticationType));
        authConfig.setName(config.getName());
        authConfig.setDisplayName(config.getDisplayName());
        authConfig.setImageUrl(config.getImage());
        authConfig.setDescription(config.getDescription());
        authConfig.setEnabled(config.getIsEnabled());
        authConfig.setEndpointConfig(buildEndpointConfig(config.getEndpoint()));

        log.info("Created user defined local authenticator config: " + config.getName());
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

        if (existingConfig == null) {
            throw new IllegalArgumentException("Existing authenticator configuration cannot be null");
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Building UserDefinedLocalAuthenticatorConfig from update request for: " + 
                    existingConfig.getName());
        }

        UserDefinedLocalAuthenticatorConfig authConfig = new UserDefinedLocalAuthenticatorConfig(
                resolveAuthenticationType(existingConfig));
        authConfig.setName(existingConfig.getName());
        authConfig.setDisplayName(config.getDisplayName());
        authConfig.setImageUrl(config.getImage());
        authConfig.setDescription(config.getDescription());
        authConfig.setEnabled(config.getIsEnabled());
        authConfig.setEndpointConfig(buildEndpointConfig(config.getEndpoint()));

        log.info("Updated user defined local authenticator config: " + existingConfig.getName());
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
            endpointConfigBuilder.allowedHeaders(endpointConfig.getAllowedHeaders());
            endpointConfigBuilder.allowedParameters(endpointConfig.getAllowedParameters());
            return endpointConfigBuilder.build();
        } catch (NoSuchElementException | IllegalArgumentException e) {
            log.warn("Failed to build endpoint configuration due to invalid config: " + e.getMessage());
            AuthenticatorMgtError error = AuthenticatorMgtError.ERROR_CODE_INVALID_ENDPOINT_CONFIG;
            throw new AuthenticatorMgtClientException(error.getCode(), error.getMessage(), e.getMessage());
        }
    }

    private static AuthenticatorPropertyConstants.AuthenticationType resolveAuthenticationType(
            LocalAuthenticatorConfig config) {

        if (Arrays.asList(config.getTags()).contains(TAG_2FA)) {
            return AuthenticatorPropertyConstants.AuthenticationType.VERIFICATION;
        } else {
            return AuthenticatorPropertyConstants.AuthenticationType.IDENTIFICATION;
        }
    }

    private static void validateUserDefinedLocalAuthenticatorConfig(UserDefinedLocalAuthenticatorCreation config)
            throws AuthenticatorMgtClientException {

        if (config == null) {
            throw new IllegalArgumentException("Authentication configuration cannot be null");
        }
        
        if (config.getEndpoint() == null || config.getEndpoint().getAuthentication() == null) {
            throw new IllegalArgumentException("Endpoint authentication configuration is required");
        }
        
        if (config.getEndpoint().getAuthentication().getType() == AuthenticationType.TypeEnum.NONE) {
            return;
        }

        if (config.getEndpoint().getAuthentication().getProperties() == null ||
                config.getEndpoint().getAuthentication().getProperties().isEmpty()) {
            log.warn("Endpoint authentication properties missing for authenticator: " + config.getName());
            AuthenticatorMgtError error = AuthenticatorMgtError.ERROR_CODE_INVALID_ENDPOINT_CONFIG;
            throw new AuthenticatorMgtClientException(error.getCode(), error.getMessage(),
                    "Endpoint authentication properties must be provided for user defined local authenticator: "
                            + config.getName());
        }
    }
}
