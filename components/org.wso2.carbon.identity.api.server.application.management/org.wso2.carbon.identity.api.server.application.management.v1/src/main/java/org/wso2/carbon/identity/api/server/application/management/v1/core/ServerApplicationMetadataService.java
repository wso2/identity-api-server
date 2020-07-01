/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.application.management.v1.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.AdaptiveAuthTemplates;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthProtocolMetadata;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolProperty;
import org.wso2.carbon.identity.api.server.application.management.v1.GrantType;
import org.wso2.carbon.identity.api.server.application.management.v1.GrantTypeMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.MetadataProperty;
import org.wso2.carbon.identity.api.server.application.management.v1.OIDCMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.SAMLMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.mgt.AbstractInboundAuthenticatorConfig;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;
import org.wso2.carbon.identity.oauth.dto.OAuthIDTokenAlgorithmDTO;
import org.wso2.carbon.identity.oauth.dto.TokenBindingMetaDataDTO;
import org.wso2.carbon.identity.sso.saml.SAMLSSOConfigServiceImpl;
import org.wso2.carbon.security.SecurityConfigException;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.DEFAULT_CERTIFICATE_ALIAS;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.DEFAULT_NAME_ID_FORMAT;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage.ERROR_RETRIEVING_SAML_METADATA;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage.ERROR_WS_TRUST_METADATA_SERVICE_NOT_FOUND;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.getOAuthGrantTypeNames;

/**
 * Calls internal osgi services to get required application metadata.
 */
public class ServerApplicationMetadataService {

    private static final Log LOG = LogFactory.getLog(ServerApplicationMetadataService.class);

    /**
     * Return a list of all available inbound protocols. If the customOnly parameter set to True, will return only the
     * custom protocols.
     *
     * @param customOnly Set to True to get only custom protocols. Default value: False.
     * @return The list of inbound protocols.
     */
    public List<AuthProtocolMetadata> getInboundProtocols(Boolean customOnly) {

        List<AuthProtocolMetadata> authProtocolMetadataList = new ArrayList<>();

        // Add custom inbound protocols
        Map<String, AbstractInboundAuthenticatorConfig> allCustomAuthenticators =
                ApplicationManagementServiceHolder.getInstance().getApplicationManagementService()
                        .getAllInboundAuthenticatorConfig();

        for (Map.Entry<String, AbstractInboundAuthenticatorConfig> entry : allCustomAuthenticators
                .entrySet()) {
            AuthProtocolMetadata protocol = new AuthProtocolMetadata()
                    .name(entry.getValue().getName())
                    .displayName(entry.getValue().getFriendlyName());
            authProtocolMetadataList.add(protocol);
        }

        if (customOnly == null || !customOnly) {
            // Add default inbound protocols. WS-Federation (Passive) is not added because it doesn't have metadata,
            authProtocolMetadataList.add(new AuthProtocolMetadata().name("saml")
                    .displayName("SAML2 Web SSO Configuration"));
            authProtocolMetadataList.add(new AuthProtocolMetadata().name("oidc")
                    .displayName("OAuth/OpenID Connect Configuration"));
            authProtocolMetadataList.add(new AuthProtocolMetadata().name("ws-trust")
                    .displayName("WS-Trust Security Token Service Configuration"));
        }

        return authProtocolMetadataList;
    }

    /**
     * Pull SAML metadata from the SAMLSSOConfigServiceImpl and return.
     *
     * @return Populated SAMLMetaData object.
     */
    public SAMLMetaData getSAMLMetadata() {

        SAMLMetaData samlMetaData = new SAMLMetaData();
        SAMLSSOConfigServiceImpl samlSSOConfigService = ApplicationManagementServiceHolder.getInstance()
                .getSamlssoConfigService();

        samlMetaData.setDefaultNameIdFormat(DEFAULT_NAME_ID_FORMAT);

        try {
            samlMetaData.setCertificateAlias(new MetadataProperty()
                    .defaultValue(DEFAULT_CERTIFICATE_ALIAS)
                    .options(Arrays.asList(samlSSOConfigService.getCertAliasOfPrimaryKeyStore())));
        } catch (IdentityException e) {
            throw handleException(e);
        }

        samlMetaData.setResponseSigningAlgorithm(new MetadataProperty()
                .defaultValue(samlSSOConfigService.getSigningAlgorithmUriByConfig())
                .options(Arrays.asList(samlSSOConfigService.getSigningAlgorithmUris())));

        samlMetaData.setResponseDigestAlgorithm(new MetadataProperty()
                .defaultValue(samlSSOConfigService.getDigestAlgorithmURIByConfig())
                .options(Arrays.asList(samlSSOConfigService.getDigestAlgorithmURIs())));

        samlMetaData.setAssertionEncryptionAlgorithm(new MetadataProperty()
                .defaultValue(samlSSOConfigService.getAssertionEncryptionAlgorithmURIByConfig())
                .options(Arrays.asList(samlSSOConfigService.getAssertionEncryptionAlgorithmURIs())));

        samlMetaData.setKeyEncryptionAlgorithm(new MetadataProperty()
                .defaultValue(samlSSOConfigService.getKeyEncryptionAlgorithmURIByConfig())
                .options(Arrays.asList(samlSSOConfigService.getKeyEncryptionAlgorithmURIs())));

        return samlMetaData;
    }

    /**
     * Pull OAuth/OIDC Metadata from OAuthAdminServiceImpl and return.
     *
     * @return Populated OIDCMetadata object.
     */
    public OIDCMetaData getOIDCMetadata() {

        OIDCMetaData oidcMetaData = new OIDCMetaData();
        OAuthAdminServiceImpl oAuthAdminService = ApplicationManagementServiceHolder.getInstance()
                .getOAuthAdminService();

        List<String> supportedGrantTypes = new LinkedList<>(Arrays.asList(oAuthAdminService.getAllowedGrantTypes()));
        List<GrantType> supportedGrantTypeNames = new ArrayList<>();
        // Iterate through the standard grant type names and add matching elements.
        for (String supportedGrantTypeName : supportedGrantTypes) {
            GrantType grantType = new GrantType();
            if (getOAuthGrantTypeNames().keySet().contains(supportedGrantTypeName)) {
                grantType.setName(supportedGrantTypeName);
                grantType.setDisplayName(getOAuthGrantTypeNames().get(supportedGrantTypeName));
            } else {
                grantType.setName(supportedGrantTypeName);
                grantType.setDisplayName(supportedGrantTypeName);
            }
            supportedGrantTypeNames.add(grantType);
        }
        // Set extracted grant types.
        oidcMetaData.setAllowedGrantTypes(
                new GrantTypeMetaData()
                        .options(supportedGrantTypeNames));

        oidcMetaData.setDefaultUserAccessTokenExpiryTime(
                String.valueOf(oAuthAdminService.getTokenExpiryTimes().getUserAccessTokenExpiryTime()));
        oidcMetaData.defaultApplicationAccessTokenExpiryTime(
                String.valueOf(oAuthAdminService.getTokenExpiryTimes().getApplicationAccessTokenExpiryTime()));
        oidcMetaData.defaultRefreshTokenExpiryTime(
                String.valueOf(oAuthAdminService.getTokenExpiryTimes().getRefreshTokenExpiryTime()));
        oidcMetaData.defaultIdTokenExpiryTime(
                String.valueOf(oAuthAdminService.getTokenExpiryTimes().getIdTokenExpiryTime()));

        OAuthIDTokenAlgorithmDTO idTokenAlgorithmDTO = oAuthAdminService.getSupportedIDTokenAlgorithms();
        oidcMetaData.setIdTokenEncryptionAlgorithm(
                new MetadataProperty()
                        .defaultValue(idTokenAlgorithmDTO.getDefaultIdTokenEncryptionAlgorithm())
                        .options(idTokenAlgorithmDTO.getSupportedIdTokenEncryptionAlgorithms()));
        oidcMetaData.idTokenEncryptionMethod(
                new MetadataProperty()
                        .defaultValue(idTokenAlgorithmDTO.getDefaultIdTokenEncryptionMethod())
                        .options(idTokenAlgorithmDTO.getSupportedIdTokenEncryptionMethods()));

        oidcMetaData.setScopeValidators(
                new MetadataProperty()
                        .defaultValue(null)
                        .options(Arrays.asList(oAuthAdminService.getAllowedScopeValidators())));

        oidcMetaData.accessTokenType(
                new MetadataProperty()
                        .defaultValue(oAuthAdminService.getDefaultTokenType())
                        .options(oAuthAdminService.getSupportedTokenTypes()));
        List<TokenBindingMetaDataDTO> supportedTokenBindings = oAuthAdminService.getSupportedTokenBindingsMetaData();
        List<String> supportedTokenBindingTypes = new ArrayList<>();
        supportedTokenBindingTypes.add("None");
        for (TokenBindingMetaDataDTO tokenBindingDTO : supportedTokenBindings) {
            supportedTokenBindingTypes.add(tokenBindingDTO.getTokenBindingType());
        }
        oidcMetaData.setAccessTokenBindingType(
                new MetadataProperty()
                        .defaultValue("None")
                        .options(supportedTokenBindingTypes));
        return oidcMetaData;
    }

    /**
     * Pull WS Trust metadata from STSAdminServiceInterface and return.
     *
     * @return Populated WSTrustMetadata object.
     */
    public WSTrustMetaData getWSTrustMetadata() {

        WSTrustMetaData wsTrustMetaData = new WSTrustMetaData();
        try {
            // Check if WS-Trust is deployed.
            if (ApplicationManagementServiceHolder.getInstance().getStsAdminService() != null) {
                wsTrustMetaData.setCertificateAlias(new MetadataProperty()
                        .defaultValue(null)
                        .options(Arrays.asList(ApplicationManagementServiceHolder.getInstance().getStsAdminService()
                                .getCertAliasOfPrimaryKeyStore())));
            } else {
                throw new SecurityConfigException(ERROR_WS_TRUST_METADATA_SERVICE_NOT_FOUND.getDescription());
            }
        } catch (SecurityConfigException e) {
            if (e.getMessage().equals(ERROR_WS_TRUST_METADATA_SERVICE_NOT_FOUND.getDescription())) {
                // Throw 404 error since the WS-Trust connector is not available.
                throw handleNotFoundError(e);
            } else {
                throw handleException(e);
            }
        }
        return wsTrustMetaData;
    }

    /**
     * Pull property metadata of the custom inbound protocol that matches to the protocol name.
     *
     * @param inboundProtocolName URL encoded protocol name.
     * @return Populated CustomInboundProtocolMetaData object.
     */
    public CustomInboundProtocolMetaData getCustomProtocolMetadata(String inboundProtocolName) {

        String protocolName = URLDecoder.decode(inboundProtocolName);
        Map<String, AbstractInboundAuthenticatorConfig> allCustomAuthenticators =
                ApplicationManagementServiceHolder.getInstance().getApplicationManagementService()
                        .getAllInboundAuthenticatorConfig();

        // Loop through all custom inbound protocols and match the name.
        for (Map.Entry<String, AbstractInboundAuthenticatorConfig> entry : allCustomAuthenticators
                .entrySet()) {
            if (entry.getValue().getName().equals(protocolName)) {
                return new CustomInboundProtocolMetaData()
                        .displayName(entry.getValue().getFriendlyName())
                        .configName(entry.getValue().getConfigName())
                        .properties(getCustomInboundProtocolProperties(entry.getValue().getConfigurationProperties()));
            }
        }

        // Throw 404 error if the protocol not found
        throw handleInvalidInboundProtocol(inboundProtocolName);
    }

    /**
     * Loop through all protocol properties and create a list of CustomInboundProtocolProperty objects.
     *
     * @param properties Custom inbound protocol properties.
     * @return Populated property list.
     */
    private List<CustomInboundProtocolProperty> getCustomInboundProtocolProperties(Property[] properties) {

        List<CustomInboundProtocolProperty> protocolProperties = new ArrayList<>();
        for (Property property : properties) {
            CustomInboundProtocolProperty protocolProperty = new CustomInboundProtocolProperty();

            if (StringUtils.isNotBlank(property.getName())) {
                protocolProperty.setName(property.getName());
            }
            if (StringUtils.isNotBlank(property.getDisplayName())) {
                protocolProperty.setDisplayName(property.getDisplayName());
            }
            if (StringUtils.isNotBlank(property.getType())) {
                protocolProperty.setType(CustomInboundProtocolProperty.TypeEnum.valueOf(
                        property.getType().toUpperCase(Locale.ENGLISH)));
            } else {
                protocolProperty.setType(CustomInboundProtocolProperty.TypeEnum.STRING);
            }
            protocolProperty.setRequired(property.isRequired());
            if (property.getOptions() != null) {
                protocolProperty.setAvailableValues(Arrays.asList(property.getOptions()));
            }
            if (StringUtils.isNotBlank(property.getDefaultValue())) {
                protocolProperty.setDefaultValue(property.getDefaultValue());
            }
            if (StringUtils.isNotBlank(property.getRegex())) {
                protocolProperty.setValidationRegex(property.getRegex());
            }
            protocolProperty.setDisplayOrder(property.getDisplayOrder());
            protocolProperty.setIsConfidential(property.isConfidential());

            protocolProperties.add(protocolProperty);
        }
        return protocolProperties;
    }

    public AdaptiveAuthTemplates getAdaptiveAuthTemplates() {

        AdaptiveAuthTemplates adaptiveAuthTemplates = new AdaptiveAuthTemplates();
        adaptiveAuthTemplates.setTemplatesJSON(ApplicationManagementServiceHolder.getInstance()
                .getApplicationManagementService().getAuthenticationTemplatesJSON());
        return adaptiveAuthTemplates;
    }

    /**
     * If the passed exception has an error message, set it to the description of the API error response.
     *
     * @param e Exception caught.
     * @return APIError with exception error message if present.
     */
    private APIError handleException(Exception e) {

        ErrorMessage errorEnum = ERROR_RETRIEVING_SAML_METADATA;
        String description = errorEnum.getDescription();
        if (StringUtils.isNotBlank(e.getMessage())) {
            description = e.getMessage();
        }

        return Utils.buildServerError(errorEnum.getCode(), errorEnum.getMessage(), description, e);
    }

    /**
     * Extract the required arguments and build a not found error.
     *
     * @param e Exception caught.
     * @return APIError with exception code, message and description.
     */
    private APIError handleNotFoundError(Exception e) {

        ErrorMessage errorEnum = ERROR_WS_TRUST_METADATA_SERVICE_NOT_FOUND;
        String errorCode = errorEnum.getCode();
        String errorMessage = errorEnum.getMessage();
        String errorDescription = e.getMessage();

        return Utils.buildNotFoundError(errorCode, errorMessage, errorDescription);
    }

    private APIError handleInvalidInboundProtocol(String inboundName) {

        String errorCode = ErrorMessage.INVALID_INBOUND_PROTOCOL.getCode();
        String errorMessage = ErrorMessage.INVALID_INBOUND_PROTOCOL.getMessage();
        String errorDescription = String.format(ErrorMessage.INVALID_INBOUND_PROTOCOL.getDescription(), inboundName);

        return Utils.buildClientError(errorCode, errorMessage, errorDescription);
    }
}
