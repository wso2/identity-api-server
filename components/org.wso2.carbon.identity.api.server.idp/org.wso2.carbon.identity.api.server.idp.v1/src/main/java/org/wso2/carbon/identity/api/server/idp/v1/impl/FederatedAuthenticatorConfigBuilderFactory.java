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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.idp.common.Constants;
import org.wso2.carbon.identity.api.server.idp.common.IdentityProviderServiceHolder;
import org.wso2.carbon.identity.api.server.idp.v1.model.AuthenticationType;
import org.wso2.carbon.identity.api.server.idp.v1.model.Endpoint;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticator;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorPUTRequest;
import org.wso2.carbon.identity.application.common.ApplicationAuthenticatorService;
import org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.UserDefinedAuthenticatorEndpointConfig;
import org.wso2.carbon.identity.application.common.model.UserDefinedFederatedAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants;
import org.wso2.carbon.identity.base.AuthenticatorPropertyConstants.DefinedByType;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementClientException;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementServerException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLEncode;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.GOOGLE_PRIVATE_KEY;
import static org.wso2.carbon.identity.api.server.idp.common.Constants.IDP_PATH_COMPONENT;

/**
 * The factory class for building federated authenticator configuration related models.
 */
public class FederatedAuthenticatorConfigBuilderFactory {

    /**
     * Builds a FederatedAuthenticatorConfig instance based on the definedBy type for the
     * given FederatedAuthenticatorPUTRequest.
     *
     * @param authenticator FederatedAuthenticatorPUTRequest instance.
     * @param definedByType DefinedByType of the authenticator.
     * @return FederatedAuthenticatorConfig instance.
     * @throws IdentityProviderManagementClientException If an error occurs while building
     *                                                   the FederatedAuthenticatorConfig.
     */
    public static FederatedAuthenticatorConfig build(FederatedAuthenticatorPUTRequest authenticator,
                                                     String authenticatorName, DefinedByType definedByType)
            throws IdentityProviderManagementClientException {

        FederatedAuthenticatorConfigDTO fedAuthConfigDTO = new FederatedAuthenticatorConfigDTO()
                .definedByType(definedByType)
                .authenticatorName(authenticatorName)
                .endpoint(authenticator.getEndpoint())
                .properties(authenticator.getProperties())
                .isEnabled(authenticator.getIsEnabled());

        FederatedAuthenticatorConfig federatedAuthenticatorConfig =
                getFederatedAuthenticatorConfigUpdateModel(fedAuthConfigDTO);
        federatedAuthenticatorConfig.setName(fedAuthConfigDTO.authenticatorName);
        federatedAuthenticatorConfig.setDisplayName(fedAuthConfigDTO.displayName);
        federatedAuthenticatorConfig.setEnabled(fedAuthConfigDTO.isEnabled);

        return federatedAuthenticatorConfig;
    }

    /**
     * Builds a FederatedAuthenticatorConfig instance based on the definedBy type for the given FederatedAuthenticator.
     *
     * @param authenticator FederatedAuthenticator instance.
     * @param definedByType DefinedByType of the authenticator.
     * @return FederatedAuthenticator instance.
     * @throws IdentityProviderManagementClientException    If an error occurs while building the
     *                                                      FederatedAuthenticatorConfig.
     */
    public static FederatedAuthenticatorConfig build(FederatedAuthenticator authenticator,
                                                     String authenticatorName, DefinedByType definedByType)
            throws IdentityProviderManagementClientException {

        FederatedAuthenticatorConfigDTO fedAuthConfigDTO = new FederatedAuthenticatorConfigDTO()
                .definedByType(definedByType)
                .authenticatorName(authenticatorName)
                .endpoint(authenticator.getEndpoint())
                .properties(authenticator.getProperties())
                .isEnabled(authenticator.getIsEnabled());

        FederatedAuthenticatorConfig federatedAuthenticatorConfig =
                getFederatedAuthenticatorConfigCreateModel(fedAuthConfigDTO);
        federatedAuthenticatorConfig.setName(fedAuthConfigDTO.authenticatorName);
        federatedAuthenticatorConfig.setDisplayName(fedAuthConfigDTO.displayName);
        federatedAuthenticatorConfig.setEnabled(fedAuthConfigDTO.isEnabled);

        return federatedAuthenticatorConfig;
    }

    /**
     * Builds a FederatedAuthenticatorConfig instance based on the definedBy type for the given
     * FederatedAuthenticatorConfig.
     *
     * @param config     FederatedAuthenticatorConfig instance.
     * @return FederatedAuthenticator instance.
     * @throws IdentityProviderManagementServerException    If an error occurs while building the
     *                                                      FederatedAuthenticator.
     */
    public static FederatedAuthenticator build(FederatedAuthenticatorConfig config)
            throws IdentityProviderManagementServerException {

        FederatedAuthenticator federatedAuthenticator = new FederatedAuthenticator();

        federatedAuthenticator.setName(config.getName());
        federatedAuthenticator.setIsEnabled(config.isEnabled());
        String[] tags = resolveAuthenticatorTags(config);
        if (ArrayUtils.isNotEmpty(tags)) {
            federatedAuthenticator.setTags(Arrays.asList(tags));
        }

        if (DefinedByType.SYSTEM == config.getDefinedByType()) {
            federatedAuthenticator.setDefinedBy(FederatedAuthenticator.DefinedByEnum.SYSTEM);
            List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> properties =
                    Arrays.stream(config.getProperties()).map(propertyToExternal).collect(Collectors.toList());
            federatedAuthenticator.setProperties(properties);
        } else {
            federatedAuthenticator.setDefinedBy(FederatedAuthenticator.DefinedByEnum.USER);
            resolveEndpointConfiguration(federatedAuthenticator, config);
        }

        return federatedAuthenticator;
    }

    /**
     * Builds a list of FederatedAuthenticatorListItem instances based on the given array of
     * FederatedAuthenticatorConfig.
     *
     * @param fedAuthConfigs Array of FederatedAuthenticatorConfig instances.
     * @param idpResourceId  Identity provider resource ID.
     * @return List of FederatedAuthenticatorListItem instances.
     */
    public static List<FederatedAuthenticatorListItem> build(FederatedAuthenticatorConfig[] fedAuthConfigs,
                                                             String idpResourceId) {

        List<FederatedAuthenticatorListItem> authenticators = new ArrayList<>();
        for (FederatedAuthenticatorConfig config : fedAuthConfigs) {
            FederatedAuthenticatorListItem authenticatorListItem = new FederatedAuthenticatorListItem();
            authenticatorListItem.setAuthenticatorId(base64URLEncode(config.getName()));
            authenticatorListItem.setName(config.getName());
            authenticatorListItem.setIsEnabled(config.isEnabled());
            authenticatorListItem.definedBy(FederatedAuthenticatorListItem.DefinedByEnum.valueOf(
                    config.getDefinedByType().toString()));
            String[] tags = resolveAuthenticatorTags(config);
            if (ArrayUtils.isNotEmpty(tags)) {
                authenticatorListItem.setTags(Arrays.asList(tags));
            }
            authenticatorListItem.setSelf(ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT +
                     IDP_PATH_COMPONENT + "/%s/federated-authenticators/%s", idpResourceId,
                                    base64URLEncode(config.getName()))).toString());
            authenticators.add(authenticatorListItem);
        }

        return authenticators;
    }

    private static FederatedAuthenticatorConfig getFederatedAuthenticatorConfigCreateModel(
            FederatedAuthenticatorConfigDTO fedAuthConfigDTO) throws IdentityProviderManagementClientException {

        if (DefinedByType.SYSTEM == fedAuthConfigDTO.definedByType) {
            performSystemDefinedFederatedAuthenticatorValidationForCreateRequest(fedAuthConfigDTO);
            return createSystemDefinedFederatedAuthenticator(fedAuthConfigDTO);
        }

        performUserDefinedFederatedAuthenticatorValidationsForCreateRequest(fedAuthConfigDTO);
        return createUserDefinedFederatedAuthenticator(fedAuthConfigDTO);
    }

    private static FederatedAuthenticatorConfig getFederatedAuthenticatorConfigUpdateModel(
            FederatedAuthenticatorConfigDTO fedAuthConfigDTO) throws IdentityProviderManagementClientException {

        if (DefinedByType.SYSTEM == fedAuthConfigDTO.definedByType) {
            performSystemDefinedFederatedAuthenticatorValidationsForUpdateRequest(fedAuthConfigDTO);
            return createSystemDefinedFederatedAuthenticator(fedAuthConfigDTO);
        }

        performUserDefinedFederatedAuthenticatorCommonValidations(fedAuthConfigDTO);
        return createUserDefinedFederatedAuthenticator(fedAuthConfigDTO);
    }

    private static FederatedAuthenticatorConfig createSystemDefinedFederatedAuthenticator(
            FederatedAuthenticatorConfigDTO fedAuthConfigDTO) {

        FederatedAuthenticatorConfig authConfig = new FederatedAuthenticatorConfig();
        authConfig.setDefinedByType(DefinedByType.SYSTEM);
        authConfig.setProperties(fedAuthConfigDTO.properties.toArray(new Property[0]));
        return authConfig;
    }

    private static void performSystemDefinedFederatedAuthenticatorValidationForCreateRequest(
            FederatedAuthenticatorConfigDTO fedAuthConfigDTO) throws IdentityProviderManagementClientException {

        performSystemDefinedFederatedAuthenticatorCommonValidations(fedAuthConfigDTO);
        if (!areAllDistinct(fedAuthConfigDTO.properties)) {
            Constants.ErrorMessage error = Constants.ErrorMessage.ERROR_CODE_INVALID_INPUT;
            throw new IdentityProviderManagementClientException(error.getCode(), error.getMessage(),
                    String.format(error.getDescription(), " Duplicate properties are found in " +
                            "the request."));
        }
    }

    private static void performSystemDefinedFederatedAuthenticatorValidationsForUpdateRequest(
            FederatedAuthenticatorConfigDTO fedAuthConfigDTO) throws IdentityProviderManagementClientException {

        performSystemDefinedFederatedAuthenticatorCommonValidations(fedAuthConfigDTO);
        if (IdentityApplicationConstants.Authenticator.OIDC.FED_AUTH_NAME.equals(fedAuthConfigDTO.authenticatorName)) {
            validateDuplicateOpenIDConnectScopes(fedAuthConfigDTO.properties);
            validateDefaultOpenIDConnectScopes(fedAuthConfigDTO.properties);
        }
    }

    private static void performSystemDefinedFederatedAuthenticatorCommonValidations(
            FederatedAuthenticatorConfigDTO fedAuthConfigDTO) throws IdentityProviderManagementClientException {

        if (IdentityApplicationConstants.Authenticator.SAML2SSO.FED_AUTH_NAME
                .equals(fedAuthConfigDTO.authenticatorName)) {
            validateSamlMetadata(fedAuthConfigDTO.properties);
        }

        // The System-defined authenticator configs must not have endpoint configurations; throw an error if they do.
        if (fedAuthConfigDTO.endpoint != null) {
            Constants.ErrorMessage error = Constants.ErrorMessage.ERROR_CODE_ENDPOINT_PROVIDED_FOR_SYSTEM_AUTH;
            throw new IdentityProviderManagementClientException(error.getCode(), error.getMessage(),
                    String.format(error.getDescription(), fedAuthConfigDTO.authenticatorName));
        }
    }

    private static UserDefinedFederatedAuthenticatorConfig createUserDefinedFederatedAuthenticator(
            FederatedAuthenticatorConfigDTO federatedAuthenticatorConfigDTO)
            throws IdentityProviderManagementClientException {

        try {
            UserDefinedFederatedAuthenticatorConfig authConfig = new UserDefinedFederatedAuthenticatorConfig();
            UserDefinedAuthenticatorEndpointConfig.UserDefinedAuthenticatorEndpointConfigBuilder endpointConfigBuilder =
                    new UserDefinedAuthenticatorEndpointConfig.UserDefinedAuthenticatorEndpointConfigBuilder();
            endpointConfigBuilder.uri(federatedAuthenticatorConfigDTO.endpoint.getUri());
            endpointConfigBuilder.authenticationType(
                    federatedAuthenticatorConfigDTO.endpoint.getAuthentication().getType().toString());
            if (federatedAuthenticatorConfigDTO.endpoint.getAuthentication().getProperties() != null) {
                endpointConfigBuilder.authenticationProperties(
                        federatedAuthenticatorConfigDTO.endpoint.getAuthentication().getProperties()
                        .entrySet().stream().collect(Collectors.toMap(
                                Map.Entry::getKey, entry -> entry.getValue().toString())));
            }
            endpointConfigBuilder.allowedHeaders(federatedAuthenticatorConfigDTO.endpoint.getAllowedHeaders());
            endpointConfigBuilder.allowedParameters(federatedAuthenticatorConfigDTO.endpoint.getAllowedParameters());
            authConfig.setEndpointConfig(endpointConfigBuilder.build());

            return authConfig;
        } catch (NoSuchElementException | IllegalArgumentException e) {
            throw new IdentityProviderManagementClientException(Constants.ErrorMessage
                    .ERROR_CODE_INVALID_INPUT.getCode(), Constants.ErrorMessage.ERROR_CODE_INVALID_INPUT.getMessage(),
                    e.getMessage());
        }
    }

    private static void performUserDefinedFederatedAuthenticatorValidationsForCreateRequest(
            FederatedAuthenticatorConfigDTO fedAuthConfigDTO) throws IdentityProviderManagementClientException {

        performUserDefinedFederatedAuthenticatorCommonValidations(fedAuthConfigDTO);
        if (fedAuthConfigDTO.endpoint.getAuthentication().getType() == AuthenticationType.TypeEnum.NONE) {
            return;
        }

        if (fedAuthConfigDTO.endpoint.getAuthentication().getProperties() == null
                || fedAuthConfigDTO.endpoint.getAuthentication().getProperties().isEmpty()) {
            throw new IdentityProviderManagementClientException(
                    Constants.ErrorMessage.ERROR_CODE_INVALID_INPUT.getCode(),
                    Constants.ErrorMessage.ERROR_CODE_INVALID_INPUT.getMessage(),
                    "Endpoint authentication properties must be provided for user defined federated authenticator: "
                            + fedAuthConfigDTO.authenticatorName);
        }
    }

    private static void performUserDefinedFederatedAuthenticatorCommonValidations(
            FederatedAuthenticatorConfigDTO fedAuthConfigDTO) throws IdentityProviderManagementClientException {

        // The User-defined authenticator configs must not have properties configurations; throw an error if they do.
        if (fedAuthConfigDTO.properties != null) {
            Constants.ErrorMessage error = Constants.ErrorMessage.ERROR_CODE_PROPERTIES_PROVIDED_FOR_USER_AUTH;
            throw new IdentityProviderManagementClientException(error.getCode(), error.getMessage(),
                    String.format(error.getDescription(), fedAuthConfigDTO.authenticatorName));
        }

        // The User-defined authenticator configs must have endpoint configurations; throw an error if they don't.
        if (fedAuthConfigDTO.endpoint == null) {
            Constants.ErrorMessage error = Constants.ErrorMessage.ERROR_CODE_NO_ENDPOINT_PROVIDED;
            throw new IdentityProviderManagementClientException(error.getCode(), error.getMessage(),
                    String.format(error.getDescription(), fedAuthConfigDTO.authenticatorName));
        }
    }


    /**
     * If selectMode property is set as saml metadata file configuration mode, this function validates whether a
     * valid base-64 encoded SAML metadata file content is provided with the property key 'meta_data_saml'. If found,
     * it will decode the file content and update the value of 'meta_data_saml' property with decoded content.
     *
     * @param samlAuthenticatorProperties Authenticator properties of SAML authenticator.
     */
    private static void validateSamlMetadata(List<Property> samlAuthenticatorProperties)
            throws IdentityProviderManagementClientException {

        if (samlAuthenticatorProperties != null) {
            for (Property property : samlAuthenticatorProperties) {

                if (Constants.SELECT_MODE.equals(property.getName()) &&
                        Constants.SELECT_MODE_METADATA.equals(property.getValue())) {
                    // SAML metadata file configuration has been selected. Hence we need to validate whether valid SAML
                    // metadata (property with key = 'meta_data_saml') is sent.

                    boolean validMetadataFound = false;
                    String encodedData = null;
                    int positionOfMetadataKey = -1;

                    for (int i = 0; i < samlAuthenticatorProperties.size(); i++) {
                        if (Constants.META_DATA_SAML.equals(samlAuthenticatorProperties.get(i).getName()) &&
                                StringUtils.isNotBlank
                                        (samlAuthenticatorProperties.get(i).getValue())) {
                            validMetadataFound = true;
                            encodedData = samlAuthenticatorProperties.get(i).getValue();
                            positionOfMetadataKey = i;
                            break;
                        }
                    }
                    if (validMetadataFound) {
                        String metadata = new String(Base64.getDecoder().decode(encodedData), (StandardCharsets.UTF_8));
                        // Add decoded data to property list.
                        Property metadataProperty = samlAuthenticatorProperties.get(positionOfMetadataKey);
                        metadataProperty.setValue(metadata);
                        samlAuthenticatorProperties.set(positionOfMetadataKey, metadataProperty);
                    } else {
                        Constants.ErrorMessage error = Constants.ErrorMessage.ERROR_CODE_INVALID_SAML_METADATA;
                        throw new IdentityProviderManagementClientException(error.getCode(), error.getMessage(),
                                error.getDescription());
                    }
                }
            }
        }
    }

    /**
     * Verify if scopes have not been set in both Scopes field and Additional Query Parameters field.
     *
     * @param oidcAuthenticatorProperties Authenticator properties of OIDC authenticator.
     */
    private static void validateDuplicateOpenIDConnectScopes(List<Property> oidcAuthenticatorProperties)
            throws IdentityProviderManagementClientException {

        if (oidcAuthenticatorProperties != null) {
            boolean scopesFieldFilled = false;
            boolean queryParamsScopesFilled = false;
            for (Property oidcAuthenticatorProperty : oidcAuthenticatorProperties) {
                if (IdentityApplicationConstants.Authenticator.OIDC.SCOPES.equals(oidcAuthenticatorProperty.getName())
                        && StringUtils.isNotBlank(oidcAuthenticatorProperty.getValue())) {
                    scopesFieldFilled = true;
                }
                if (IdentityApplicationConstants.Authenticator.OIDC.QUERY_PARAMS.equals
                        (oidcAuthenticatorProperty.getName())
                        && oidcAuthenticatorProperty.getValue().contains("scope=")) {
                    queryParamsScopesFilled = true;
                }
            }
            if (scopesFieldFilled && queryParamsScopesFilled) {
                Constants.ErrorMessage error = Constants.ErrorMessage.ERROR_CODE_DUPLICATE_OIDC_SCOPES;
                throw new IdentityProviderManagementClientException(error.getCode(), error.getMessage(),
                        error.getDescription());
            }
        }
    }

    /**
     * Verify if scopes contain `openid`.
     *
     * @param oidcAuthenticatorProperties Authenticator properties of OIDC authenticator.
     */
    private static void validateDefaultOpenIDConnectScopes(List<Property> oidcAuthenticatorProperties)
            throws IdentityProviderManagementClientException {

        if (oidcAuthenticatorProperties != null) {
            for (Property oidcAuthenticatorProperty : oidcAuthenticatorProperties) {
                if (IdentityApplicationConstants.Authenticator.OIDC.SCOPES.equals(
                        oidcAuthenticatorProperty.getName())) {
                    String scopes = oidcAuthenticatorProperty.getValue();
                    if (StringUtils.isNotBlank(scopes) && !scopes.contains("openid")) {
                        Constants.ErrorMessage error = Constants.ErrorMessage.ERROR_CODE_INVALID_OIDC_SCOPES;
                        throw new IdentityProviderManagementClientException(error.getCode(), error.getMessage(),
                                error.getDescription());
                    }
                }
            }
        }
    }

    static boolean areAllDistinct(List<Property> properties) {
        return properties.stream()
                .map(Property::getName)
                .distinct().count() == properties.size();
    }

    private static Function<org.wso2.carbon.identity.api.server.idp.v1.model.Property, Property> propertyToInternal
            = apiProperty -> {

        Property property = new Property();
        property.setName(apiProperty.getKey());
        property.setValue(apiProperty.getValue());
        if (StringUtils.equals(GOOGLE_PRIVATE_KEY, apiProperty.getKey())) {
            property.setType(IdentityApplicationConstants.ConfigElements.PROPERTY_TYPE_BLOB);
        }
        return property;
    };

    private static Function<Property, org.wso2.carbon.identity.api.server.idp.v1.model.Property> propertyToExternal
            = property -> {

        org.wso2.carbon.identity.api.server.idp.v1.model.Property apiProperty = new org.wso2.carbon.identity.api
                .server.idp.v1.model.Property();
        apiProperty.setKey(property.getName());
        apiProperty.setValue(property.getValue());
        return apiProperty;
    };

    private static void resolveEndpointConfiguration(FederatedAuthenticator authenticator,
             FederatedAuthenticatorConfig config) throws IdentityProviderManagementServerException {

        try {
            UserDefinedFederatedAuthenticatorConfig userDefinedConfig =
                    (UserDefinedFederatedAuthenticatorConfig) config;
            UserDefinedAuthenticatorEndpointConfig endpointConfig = userDefinedConfig.getEndpointConfig();

            AuthenticationType authenticationType = new AuthenticationType();
            authenticationType.setType(AuthenticationType.TypeEnum.fromValue(endpointConfig.getEndpointConfig()
                    .getAuthentication().getType().toString()));
            authenticationType.setProperties(null);

            Endpoint endpoint = new Endpoint();
            endpoint.setUri(endpointConfig.getEndpointConfig().getUri());
            endpoint.setAuthentication(authenticationType);
            endpoint.setAllowedHeaders(endpointConfig.getEndpointConfig().getAllowedHeaders());
            endpoint.setAllowedParameters(endpointConfig.getEndpointConfig().getAllowedParameters());
            authenticator.setEndpoint(endpoint);
        } catch (ClassCastException e) {
            throw new IdentityProviderManagementServerException(String.format("Error occurred while resolving" +
                    " endpoint configuration of the authenticator %s.", authenticator.getName()), e);
        }
    }

    private static String[] resolveAuthenticatorTags(FederatedAuthenticatorConfig config) {

        /* If the authenticator is defined by the user, return the tags of the authenticator config. Otherwise, return
        the tags of the system registered federated authenticator template.
         */
        if (DefinedByType.USER == config.getDefinedByType()) {
            return config.getTags();
        }
        FederatedAuthenticatorConfig federatedAuthenticatorConfig =
                ApplicationAuthenticatorService.getInstance().getFederatedAuthenticatorByName(config.getName());
        return federatedAuthenticatorConfig != null ? federatedAuthenticatorConfig.getTags()
                : new String[0];
    }

    /**
     * Config class to build FederatedAuthenticatorConfig.
     */
    private static class FederatedAuthenticatorConfigDTO {
        private DefinedByType definedByType;
        private String authenticatorName;
        private String displayName;
        private Endpoint endpoint;
        private List<Property> properties;
        private Boolean isEnabled;

        FederatedAuthenticatorConfigDTO definedByType(DefinedByType definedByType) {

            this.definedByType = definedByType;
            return this;
        }

        FederatedAuthenticatorConfigDTO authenticatorName(String authenticatorName)
                throws IdentityProviderManagementClientException {

            this.authenticatorName = authenticatorName;
            this.displayName = getDisplayNameOfAuthenticator(authenticatorName);
            return this;
        }

        FederatedAuthenticatorConfigDTO endpoint(Endpoint endpoint) {

            this.endpoint = endpoint;
            return this;
        }

        FederatedAuthenticatorConfigDTO properties(
                List<org.wso2.carbon.identity.api.server.idp.v1.model.Property> properties) {

            this.properties = Optional.ofNullable(properties)
                    .map(props -> props.stream().map(propertyToInternal).collect(Collectors.toList()))
                    .orElse(null);
            return this;
        }

        FederatedAuthenticatorConfigDTO isEnabled(Boolean isEnabled) {

            this.isEnabled = isEnabled;
            return this;
        }

        private static String getDisplayNameOfAuthenticator(String authenticatorName)
                throws IdentityProviderManagementClientException {

            try {
                FederatedAuthenticatorConfig[] authenticatorConfigs =
                        IdentityProviderServiceHolder.getIdentityProviderManager()
                                .getAllFederatedAuthenticators();
                for (FederatedAuthenticatorConfig config : authenticatorConfigs) {

                    if (StringUtils.equals(config.getName(), authenticatorName)) {
                        return config.getDisplayName();
                    }
                }
            } catch (IdentityProviderManagementException e) {
                Constants.ErrorMessage error = Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_IDP;
                throw new IdentityProviderManagementClientException(error.getCode(), error.getMessage(),
                        error.getDescription());
            }
            return null;
        }
    }
}
