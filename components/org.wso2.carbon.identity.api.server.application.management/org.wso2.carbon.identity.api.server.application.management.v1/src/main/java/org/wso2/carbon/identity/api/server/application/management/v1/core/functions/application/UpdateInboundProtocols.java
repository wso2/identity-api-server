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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.oauth2.ApiModelToOAuthConsumerApp;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.saml.ApiModelToSAMLSSOServiceProvider;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;
import org.wso2.carbon.identity.sso.saml.SAMLSSOConfigServiceImpl;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.identity.sso.saml.exception.IdentitySAML2SSOException;
import org.wso2.carbon.identity.sso.saml.util.SAMLSSOUtil;
import org.wso2.carbon.security.SecurityConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.rollbackInbounds;

/**
 * Updates the inbound authentication protocols defined by the API model in the Service Provider model.
 */
public class UpdateInboundProtocols implements UpdateFunction<ServiceProvider, InboundProtocols> {

    private static final int CONNECTION_TIMEOUT_IN_SECONDS = 5;
    private static final int READ_TIMEOUT_IN_SECONDS = 10;

    @Override
    public void update(ServiceProvider application, InboundProtocols inboundProtocols) {

        List<InboundAuthenticationRequestConfig> inbounds = new ArrayList<>();

        try {
            if (inboundProtocols.getOidc() != null) {
                inbounds.add(getOIDCInbound(inboundProtocols.getOidc()));
            }

            if (inboundProtocols.getSaml() != null) {
                inbounds.add(getSAMLInbound(inboundProtocols.getSaml()));
            }

            if (inboundProtocols.getWsTrust() != null) {
                inbounds.add(getWsTrustInbound(inboundProtocols.getWsTrust()));
            }
        } catch (APIError error) {
            // TODO: a log here.
            rollbackInbounds(inbounds);
            throw error;
        }

        if (inboundProtocols.getPassiveSts() != null) {
            inbounds.add(getPassiveStsInbound(inboundProtocols.getPassiveSts()));
        }

        if (inboundProtocols.getCustom() != null) {
            inboundProtocols.getCustom().forEach(inboundConfigModel -> {
                // Add validate at swagger to make sure inbound key and name are not null.
                InboundAuthenticationRequestConfig inboundRequestConfig = buildCustomInbound(inboundConfigModel);
                inbounds.add(inboundRequestConfig);
            });
        }

        InboundAuthenticationConfig inboundAuthConfig = new InboundAuthenticationConfig();
        inboundAuthConfig.setInboundAuthenticationRequestConfigs(
                inbounds.toArray(new InboundAuthenticationRequestConfig[0])
        );

        application.setInboundAuthenticationConfig(inboundAuthConfig);
    }

    private InboundAuthenticationRequestConfig buildCustomInbound(CustomInboundProtocolConfiguration inboundModel) {

        return new ApiModelToCustomInbound().apply(inboundModel);
    }

    private InboundAuthenticationRequestConfig getOIDCInbound(OpenIDConnectConfiguration oidcModel) {

        // Build a consumer apps object.
        OAuthConsumerAppDTO consumerApp = new ApiModelToOAuthConsumerApp().apply(oidcModel);
        OAuthConsumerAppDTO registeredOAuthApp;
        try {
            registeredOAuthApp = ApplicationManagementServiceHolder.getOAuthAdminService()
                    .registerAndRetrieveOAuthApplicationData(consumerApp);
        } catch (IdentityOAuthAdminException e) {
            throw Utils.buildServerErrorResponse(e,
                    "Error creating OAuth2/OpenIDConnect configuration. " + e.getMessage());
        }
        // Try to register it.
        // Set the consumer key and secret in inbound.
        InboundAuthenticationRequestConfig oidcInbound = new InboundAuthenticationRequestConfig();
        oidcInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.OAUTH2);
        oidcInbound.setInboundAuthKey(registeredOAuthApp.getOauthConsumerKey());
        return oidcInbound;
    }

    private InboundAuthenticationRequestConfig getSAMLInbound(SAML2Configuration saml) {

        SAML2ServiceProvider samlManualConfiguration = saml.getManualConfiguration();

        String issuer;
        if (saml.getMetadataFile() != null) {
            issuer = createSAMLSpWithMetadataFile(saml.getMetadataFile());
        } else if (saml.getMetadataURL() != null) {
            issuer = createSAMLSpWithMetadataUrl(saml.getMetadataURL());
        } else if (samlManualConfiguration != null) {
            issuer = createSAMLSpWithManualConfiguration(samlManualConfiguration);
        } else {
            // TODO error code.
            throw Utils.buildClientError("Invalid SAML2 Configuration. One of metadataFile, metaDataUrl or " +
                    "serviceProvider manual configuration needs to be present.");
        }

        InboundAuthenticationRequestConfig samlInbound = new InboundAuthenticationRequestConfig();
        samlInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.SAML2);
        samlInbound.setInboundAuthKey(issuer);
        return samlInbound;
    }

    private String createSAMLSpWithManualConfiguration(SAML2ServiceProvider saml2SpModel) {

        SAMLSSOServiceProviderDTO samlSp = new ApiModelToSAMLSSOServiceProvider().apply(saml2SpModel);
        String issuerWithQualifier = getIssuerWithQualifier(samlSp);
        try {
            boolean success = getSamlssoConfigService().addRPServiceProvider(samlSp);
            if (success) {
                return issuerWithQualifier;
            } else {
                String msg = "Error adding SAML configuration. SAML service provider with issuer: %s and " +
                        "qualifier: %s already exists.";
                throw Utils.buildServerErrorResponse(null,
                        String.format(msg, samlSp.getIssuer(), samlSp.getIssuerQualifier()));
            }
        } catch (IdentityException e) {
            throw Utils.buildServerErrorResponse(e, "Error adding SAML config with issuer: " +
                    saml2SpModel.getIssuer() + ". " + e.getMessage());
        }
    }

    private String createSAMLSpWithMetadataFile(String encodedMetaFileContent) {

        try {
            byte[] metaData = Base64.getDecoder().decode(encodedMetaFileContent.getBytes(StandardCharsets.UTF_8));
            String base64DecodedMetadata = new String(metaData, StandardCharsets.UTF_8);

            return createSAMLSpWithMetadataContent(base64DecodedMetadata);
        } catch (IdentitySAML2SSOException e) {
            throw Utils.buildServerErrorResponse(e, "Error adding SAML configuration using metadata file. " +
                    e.getMessage());
        }
    }

    private String createSAMLSpWithMetadataContent(String metadataContent) throws IdentitySAML2SSOException {

        SAMLSSOServiceProviderDTO serviceProviderDTO =
                getSamlssoConfigService().uploadRPServiceProvider(metadataContent);
        return serviceProviderDTO.getIssuer();
    }

    private String createSAMLSpWithMetadataUrl(String metadataUrl) {

        try {
            String metadataFileContent = getMetaContentFromUrl(metadataUrl);
            SAMLSSOServiceProviderDTO serviceProviderDTO =
                    getSamlssoConfigService().uploadRPServiceProvider(metadataFileContent);
            return serviceProviderDTO.getIssuer();
        } catch (IdentitySAML2SSOException e) {
            throw Utils.buildServerErrorResponse(e, "Error adding SAML configuration using metadata file. " +
                    e.getMessage());
        }
    }

    private String getMetaContentFromUrl(String metadataUrl) {
        // DO a HTTP call and get the metadata file.
        InputStream in = null;
        try {
            URL url = new URL(metadataUrl);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(CONNECTION_TIMEOUT_IN_SECONDS * 1000);
            con.setReadTimeout(READ_TIMEOUT_IN_SECONDS * 1000);
            in = con.getInputStream();
            return IOUtils.toString(in);
        } catch (IOException e) {
            throw Utils.buildServerErrorResponse(e, "Error while fetching SAML metadata from URL: " + metadataUrl);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private SAMLSSOConfigServiceImpl getSamlssoConfigService() {

        return ApplicationManagementServiceHolder.getSamlssoConfigService();
    }

    private String getIssuerWithQualifier(SAMLSSOServiceProviderDTO samlSp) {

        if (StringUtils.isNotBlank(samlSp.getIssuerQualifier())) {
            return SAMLSSOUtil.getIssuerWithQualifier(samlSp.getIssuer(), samlSp.getIssuerQualifier());
        } else {
            return samlSp.getIssuer();
        }
    }

    private InboundAuthenticationRequestConfig getPassiveStsInbound(PassiveStsConfiguration passiveSts) {

        InboundAuthenticationRequestConfig passiveStsInbound = new InboundAuthenticationRequestConfig();
        passiveStsInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.PASSIVE_STS);
        passiveStsInbound.setInboundAuthKey(passiveSts.getRealm());

        Property passiveStsReplyUrl = new Property();
        passiveStsReplyUrl.setName(IdentityApplicationConstants.PassiveSTS.PASSIVE_STS_REPLY_URL);
        passiveStsReplyUrl.setValue(passiveSts.getReplyTo());

        passiveStsInbound.setProperties(new Property[]{passiveStsReplyUrl});
        return passiveStsInbound;
    }

    private InboundAuthenticationRequestConfig getWsTrustInbound(WSTrustConfiguration wsTrust) {

        try {
            ApplicationManagementServiceHolder.getStsAdminService()
                    .addTrustedService(wsTrust.getAudience(), wsTrust.getCertificateAlias());
        } catch (SecurityConfigException e) {
            // Error while adding WS Trust, we can't continue
            throw Utils.buildServerErrorResponse(e, "Error while adding WS-Trust configuration. " + e.getMessage());
        }

        InboundAuthenticationRequestConfig wsTrustInbound = new InboundAuthenticationRequestConfig();
        wsTrustInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.WS_TRUST);
        wsTrustInbound.setInboundAuthKey(wsTrust.getAudience());
        return wsTrustInbound;
    }
}
