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

package org.wso2.carbon.identity.api.server.idp.v1.impl;

import org.wso2.carbon.identity.api.server.idp.common.Constants;
import org.wso2.carbon.identity.api.server.idp.v1.model.Endpoint;
import org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.UserDefinedAuthenticatorEndpointConfig;
import org.wso2.carbon.identity.application.common.model.UserDefinedFederatedAuthenticatorConfig;
import org.wso2.carbon.identity.base.AuthenticatorPropertyConstants.DefinedByType;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementClientException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The factory class for creating instances of FederatedAuthenticatorConfig depending on the definedBy type.
 * Returns FederatedAuthenticatorConfig for SYSTEM types and UserDefinedFederatedAuthenticatorConfig for USER types.
 */
public class FederatedAuthenticatorConfigBuilderFactory {

    private static FederatedAuthenticatorConfig createFederatedAuthenticatorConfig(Builder builder)
            throws IdentityProviderManagementClientException {

        FederatedAuthenticatorConfig config;
        if (DefinedByType.SYSTEM == builder.definedByType) {
            config = createSystemDefinedFederatedAuthenticator(builder);
        } else {
            config = createUserDefinedFederatedAuthenticator(builder);
        }

        config.setName(builder.authenticatorName);
        config.setDisplayName(builder.displayName);
        config.setEnabled(builder.isEnabled);

        return config;
    }

    private static FederatedAuthenticatorConfig createSystemDefinedFederatedAuthenticator(
            Builder builder) throws IdentityProviderManagementClientException {

        validateSystemDefinedFederatedAuthenticatorModel(builder);
        FederatedAuthenticatorConfig authConfig = new FederatedAuthenticatorConfig();
        authConfig.setDefinedByType(DefinedByType.SYSTEM);
        authConfig.setProperties(builder.properties.toArray(new Property[0]));
        return authConfig;
    }

    private static void validateSystemDefinedFederatedAuthenticatorModel(Builder builder)
            throws IdentityProviderManagementClientException {

        // The System-defined authenticator configs must not have endpoint configurations; throw an error if they do.
        if (builder.endpoint != null) {
            Constants.ErrorMessage error = Constants.ErrorMessage.ERROR_CODE_ENDPOINT_PROVIDED_FOR_SYSTEM_AUTH;
            throw new IdentityProviderManagementClientException(error.getCode(), String.format(error.getDescription(),
                    builder.authenticatorName));
        }
    }

    private static UserDefinedFederatedAuthenticatorConfig createUserDefinedFederatedAuthenticator(Builder builder)
            throws IdentityProviderManagementClientException {

        validateUserDefinedFederatedAuthenticatorModel(builder);

        UserDefinedFederatedAuthenticatorConfig authConfig = new UserDefinedFederatedAuthenticatorConfig();
        UserDefinedAuthenticatorEndpointConfig.UserDefinedAuthenticatorEndpointConfigBuilder endpointConfigBuilder =
                new UserDefinedAuthenticatorEndpointConfig.UserDefinedAuthenticatorEndpointConfigBuilder();
        endpointConfigBuilder.uri(builder.endpoint.getUri());
        endpointConfigBuilder.authenticationType(builder.endpoint.getAuthentication().getType().toString());
        endpointConfigBuilder.authenticationProperties(builder.endpoint.getAuthentication().getProperties()
                .entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey, entry -> entry.getValue().toString())));
        authConfig.setEndpointConfig(endpointConfigBuilder.build());

        return authConfig;
    }

    private static void validateUserDefinedFederatedAuthenticatorModel(Builder builder)
            throws IdentityProviderManagementClientException {

        // The User-defined authenticator configs must not have properties configurations; throw an error if they do.
        if (builder.properties != null) {
            Constants.ErrorMessage error = Constants.ErrorMessage.ERROR_CODE_PROPERTIES_PROVIDED_FOR_USER_AUTH;
            throw new IdentityProviderManagementClientException(error.getCode(),
                    String.format(error.getDescription(), builder.authenticatorName));
        }

        // The User-defined authenticator configs must have endpoint configurations; throw an error if they don't.
        if (builder.endpoint == null) {
            Constants.ErrorMessage error = Constants.ErrorMessage.ERROR_CODE_NO_ENDPOINT_PROVIDED;
            throw new IdentityProviderManagementClientException(error.getCode(),
                    String.format(error.getDescription(), builder.authenticatorName));
        }
    }

    /**
     * Builder class to build FederatedAuthenticatorConfig.
     */
    public static class Builder {
        private DefinedByType definedByType;
        private String authenticatorName;
        private String displayName;
        private Endpoint endpoint;
        private List<Property> properties;
        private Boolean isEnabled;

        public Builder definedByType(DefinedByType definedByType) {

            this.definedByType = definedByType;
            return this;
        }

        public Builder authenticatorName(String authenticatorName) {

            this.authenticatorName = authenticatorName;
            return this;
        }

        public Builder displayName(String displayName) {

            this.displayName = displayName;
            return this;
        }

        public Builder endpoint(Endpoint endpoint) {

            this.endpoint = endpoint;
            return this;
        }

        public Builder properties(List<Property> properties) {

            this.properties = properties;
            return this;
        }

        public Builder enabled(Boolean enabled) {

            isEnabled = enabled;
            return this;
        }

        public FederatedAuthenticatorConfig build() throws IdentityProviderManagementClientException {

            return FederatedAuthenticatorConfigBuilderFactory.createFederatedAuthenticatorConfig(this);
        }
    }
}
