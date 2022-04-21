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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.saml;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BoundedInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensaml.saml.saml1.core.NameIdentifier;
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.context.RegistryType;
import org.wso2.carbon.core.util.KeyStoreManager;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.SingleSignOnProfile;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundFunctions;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.model.SAMLSSOServiceProviderDO;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.sp.metadata.saml2.exception.InvalidMetadataException;
import org.wso2.carbon.identity.sp.metadata.saml2.util.Parser;
import org.wso2.carbon.identity.sso.saml.Error;
import org.wso2.carbon.identity.sso.saml.SAMLSSOConfigServiceImpl;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.identity.sso.saml.exception.IdentitySAML2ClientException;
import org.wso2.carbon.identity.sso.saml.exception.IdentitySAML2SSOException;
import org.wso2.carbon.identity.sso.saml.internal.IdentitySAMLSSOServiceComponent;
import org.wso2.carbon.identity.sso.saml.util.SAMLSSOUtil;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.session.UserRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.*;

import static org.wso2.carbon.identity.sso.saml.Error.INVALID_REQUEST;
import static org.wso2.carbon.identity.sso.saml.Error.URL_NOT_FOUND;

/**
 * Helper functions for SAML inbound management.
 */
public class SAMLInboundFunctions {

    private static final String ATTRIBUTE_CONSUMING_SERVICE_INDEX = "attrConsumServiceIndex";
    private static final Log logger = LogFactory.getLog(SAMLInboundFunctions.class);

    public static final String ISSUER = "issuer";
    public static final String ISSUER_QUALIFIER = "issuerQualifier";
    public static final String ASSERTION_CONSUMER_URLS = "assertionConsumerUrls";
    public static final String DEFAULT_ASSERTION_CONSUMER_URL = "defaultAssertionConsumerUrl";
    public static final String SIGNING_ALGORITHM_URI = "signingAlgorithmURI";
    public static final String DIGEST_ALGORITHM_URI = "digestAlgorithmURI";
    public static final String ASSERTION_ENCRYPTION_ALGORITHM_URI = "assertionEncryptionAlgorithmURI";
    public static final String KEY_ENCRYPTION_ALGORITHM_URI = "keyEncryptionAlgorithmURI";
    public static final String CERT_ALIAS = "certAlias";
//    TODO: check "attrConsumServiceIndex" (Line 77)
//    public static final String ATTRIBUTE_CONSUMING_SERVICE_INDEX = "attributeConsumingServiceIndex";
    public static final String DO_SIGN_RESPONSE = "doSignResponse";
    public static final String DO_SINGLE_LOGOUT = "doSingleLogout";
    public static final String DO_FRONT_CHANNEL_LOGOUT = "doFrontChannelLogout";
    public static final String FRONT_CHANNEL_LOGOUT_BINDING = "frontChannelLogoutBinding";
    public static final String IS_ASSERTION_QUERY_REQUEST_PROFILE_ENABLED = "isAssertionQueryRequestProfileEnabled";
    public static final String SUPPORTED_ASSERTION_QUERY_REQUEST_TYPES = "supportedAssertionQueryRequestTypes";
    public static final String ENABLE_SAML2_ARTIFACT_BINDING = "enableSAML2ArtifactBinding";
    public static final String DO_VALIDATE_SIGNATURE_IN_ARTIFACT_RESOLVE = "doValidateSignatureInArtifactResolve";
    public static final String LOGIN_PAGE_URL = "loginPageURL";
    public static final String SLO_RESPONSE_URL = "sloResponseURL";
    public static final String SLO_REQUEST_URL = "sloRequestURL";
    public static final String REQUESTED_CLAIMS = "requestedClaims";
    public static final String REQUESTED_AUDIENCES = "requestedAudiences";
    public static final String REQUESTED_RECIPIENTS = "requestedRecipients";
    public static final String ENABLE_ATTRIBUTES_BY_DEFAULT = "enableAttributesByDefault";
    public static final String NAME_ID_CLAIM_URI = "nameIdClaimUri";
    public static final String NAME_ID_FORMAT = "nameIDFormat";
    public static final String IDP_INIT_SSO_ENABLED = "idPInitSSOEnabled";
    public static final String IDP_INIT_SLO_ENABLED = "idPInitSLOEnabled";
    public static final String IDP_INIT_SLO_RETURN_TO_URLS = "idpInitSLOReturnToURLs";
    public static final String DO_ENABLE_ENCRYPTED_ASSERTION = "doEnableEncryptedAssertion";
    public static final String DO_VALIDATE_SIGNATURE_IN_REQUESTS = "doValidateSignatureInRequests";
    public static final String IDP_ENTITY_ID_ALIAS = "idpEntityIDAlias";

    private SAMLInboundFunctions() {

    }

    public static InboundAuthenticationRequestConfig putSAMLInbound(ServiceProvider application,
                                                                    SAML2Configuration saml2Configuration) {

        // First we identify whether this is a insert or update.
        String currentIssuer = InboundFunctions.getInboundAuthKey(application, StandardInboundProtocols.SAML2);
        SAMLSSOServiceProviderDTO oldSAMLSp = null;
        try {
            validateSingleSignOnProfileBindings(saml2Configuration);
            if (currentIssuer != null) {
                // Delete the current app.
                oldSAMLSp = getSamlSsoConfigService().getServiceProvider(currentIssuer);
                getSamlSsoConfigService().removeServiceProvider(currentIssuer);
            }
        } catch (IdentityException e) {
            throw handleException(e);
        }

        try {
            return createSAMLInbound(saml2Configuration);
        } catch (APIError error) {
            // Try to rollback by recreating the previous SAML SP.
            rollbackSAMLSpRemoval(oldSAMLSp);
            throw error;
        }
    }

    /**
     * Validate whether the request is trying to disable either HTTP_POST or HTTP_REDIRECT or both.
     *
     * @param saml2Configuration SAML2Configuration.
     * @throws IdentitySAML2ClientException If the request is trying to disable either HTTP_POST or HTTP_REDIRECT
     *                                      or both.
     */
    private static void validateSingleSignOnProfileBindings(SAML2Configuration saml2Configuration) throws
            IdentitySAML2ClientException {

        if (saml2Configuration.getManualConfiguration() == null) {
            return;
        }
        if (saml2Configuration.getManualConfiguration().getSingleSignOnProfile() == null) {
            return;
        }
        if (saml2Configuration.getManualConfiguration().getSingleSignOnProfile().getBindings() == null) {
            return;
        }
        List<SingleSignOnProfile.BindingsEnum> bindings =
                saml2Configuration.getManualConfiguration().getSingleSignOnProfile().getBindings();
        /*
        Both HTTP_POST and HTTP_REDIRECT have to be there by default. Since the backend support is not there, http
        bindings should not be allowed to change.
         */
        if (bindings.size() < 2 ||
                (bindings.size() == 2 && bindings.contains(SingleSignOnProfile.BindingsEnum.ARTIFACT))) {
            throw new IdentitySAML2ClientException(
                    ApplicationManagementConstants.ErrorMessage.DISABLE_REDIRECT_OR_POST_BINDINGS.getCode(),
                    ApplicationManagementConstants.ErrorMessage.DISABLE_REDIRECT_OR_POST_BINDINGS.getDescription());
        }
    }

    private static void rollbackSAMLSpRemoval(SAMLSSOServiceProviderDTO oldSAMLSp) {

        if (oldSAMLSp != null) {
            if (logger.isDebugEnabled()) {
                String issuer =
                        SAMLSSOUtil.getIssuerWithQualifier(oldSAMLSp.getIssuer(), oldSAMLSp.getIssuerQualifier());
                logger.debug("Error occurred while updating SAML SP with issuer: " + issuer +
                        ". Attempting to rollback by recreating the old SAML SP.");
            }
            try {
                getSamlSsoConfigService().addRPServiceProvider(oldSAMLSp);
            } catch (IdentityException e) {
                throw handleException(e);
            }
        }
    }

    public static InboundAuthenticationRequestConfig createSAMLInbound(SAML2Configuration saml2Configuration) {

        SAML2ServiceProvider samlManualConfiguration = saml2Configuration.getManualConfiguration();

        SAMLSSOServiceProviderDTO samlssoServiceProviderDTO;
        if (saml2Configuration.getMetadataFile() != null) {
            samlssoServiceProviderDTO = createSAMLSpWithMetadataFile(saml2Configuration.getMetadataFile());
        } else if (saml2Configuration.getMetadataURL() != null) {
            samlssoServiceProviderDTO = createSAMLSpWithMetadataUrl(saml2Configuration.getMetadataURL());
        } else if (samlManualConfiguration != null) {
            samlssoServiceProviderDTO = createSAMLSpWithManualConfiguration(samlManualConfiguration);
        } else {
            throw Utils.buildBadRequestError("Invalid SAML2 Configuration. One of metadataFile, metaDataUrl or " +
                    "serviceProvider manual configuration needs to be present.");
        }

        InboundAuthenticationRequestConfig samlInbound = new InboundAuthenticationRequestConfig();
        samlInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.SAML2);
        samlInbound.setInboundAuthKey(samlssoServiceProviderDTO.getIssuer());
        if (samlssoServiceProviderDTO.isEnableAttributeProfile()) {
            Property[] properties = new Property[1];
            Property property = new Property();
            property.setName(ATTRIBUTE_CONSUMING_SERVICE_INDEX);
            if (StringUtils.isNotBlank(samlssoServiceProviderDTO.getAttributeConsumingServiceIndex())) {
                property.setValue(samlssoServiceProviderDTO.getAttributeConsumingServiceIndex());
            } else {
                try {
                    property.setValue(Integer.toString(IdentityUtil.getRandomInteger()));
                } catch (IdentityException e) {
                    handleException(e);
                }
            }
            properties[0] = property;
            samlInbound.setProperties(properties);
        }
        return samlInbound;
    }

    public static InboundAuthenticationRequestConfig createSAMLInbound_new(SAML2Configuration saml2Configuration) {

        SAML2ServiceProvider samlManualConfiguration = saml2Configuration.getManualConfiguration();

        SAMLSSOServiceProviderDO samlssoServiceProviderDO;
        if (saml2Configuration.getMetadataFile() != null) {
            samlssoServiceProviderDO = createSAMLSpWithMetadataFile_new(saml2Configuration.getMetadataFile());
        } else if (saml2Configuration.getMetadataURL() != null) {
            samlssoServiceProviderDO = createSAMLSpWithMetadataUrl_new(saml2Configuration.getMetadataURL());
        } else if (samlManualConfiguration != null) {
            samlssoServiceProviderDO = createSAMLSpWithManualConfiguration_new(samlManualConfiguration);
        } else {
            throw Utils.buildBadRequestError("Invalid SAML2 Configuration. One of metadataFile, metaDataUrl or " +
                    "serviceProvider manual configuration needs to be present.");
        }

        InboundAuthenticationRequestConfig samlInbound = new InboundAuthenticationRequestConfig();
        samlInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.SAML2);
        samlInbound.setInboundAuthKey(samlssoServiceProviderDO.getIssuer());
        List<Property> propertyList = new ArrayList<>();
        if (StringUtils.isNotBlank(samlssoServiceProviderDO.getAttributeConsumingServiceIndex())) {
            Property property = new Property();
            property.setName(ATTRIBUTE_CONSUMING_SERVICE_INDEX);
            if (StringUtils.isNotBlank(samlssoServiceProviderDO.getAttributeConsumingServiceIndex())) {
                property.setValue(samlssoServiceProviderDO.getAttributeConsumingServiceIndex());
            } else {
                try {
                    property.setValue(Integer.toString(IdentityUtil.getRandomInteger()));
                } catch (IdentityException e) {
                    handleException(e);
                }
            }
            propertyList.add(property);
        }

        addSAMLInboundProperties(propertyList,samlssoServiceProviderDO);

        Property[] properties = propertyList.toArray(new Property[0]);
        samlInbound.setProperties(properties);
        return samlInbound;
    }

    public static SAML2ServiceProvider getSAML2ServiceProvider(InboundAuthenticationRequestConfig inboundAuth) {

        String issuer = inboundAuth.getInboundAuthKey();
        try {
            SAMLSSOServiceProviderDTO serviceProvider = getSamlSsoConfigService().getServiceProvider(issuer);

            if (serviceProvider != null) {
                return new SAMLSSOServiceProviderToAPIModel().apply(serviceProvider);
            } else {
                return null;
            }
        } catch (IdentityException e) {
            throw buildServerError("Error while retrieving service provider data for issuer: " + issuer, e);
        }
    }

    public static void deleteSAMLServiceProvider(InboundAuthenticationRequestConfig inbound) {

        try {
            String issuer = inbound.getInboundAuthKey();
            ApplicationManagementServiceHolder.getSamlssoConfigService().removeServiceProvider(issuer);
        } catch (IdentityException e) {
            throw buildServerError("Error while trying to rollback SAML2 configuration. " + e.getMessage(), e);
        }
    }

    @Deprecated
    private static SAMLSSOServiceProviderDTO createSAMLSpWithManualConfiguration(SAML2ServiceProvider saml2SpModel) {

        SAMLSSOServiceProviderDTO serviceProviderDTO = new ApiModelToSAMLSSOServiceProvider().apply(saml2SpModel);
        try {
            SAMLSSOServiceProviderDTO addedSAMLSp = getSamlSsoConfigService().createServiceProvider(serviceProviderDTO);
            return addedSAMLSp;
        } catch (IdentityException e) {
            throw handleException(e);
        }
    }

    private static SAMLSSOServiceProviderDO createSAMLSpWithManualConfiguration_new(SAML2ServiceProvider saml2SpModel) {

        SAMLSSOServiceProviderDTO serviceProviderDTO = new ApiModelToSAMLSSOServiceProvider().apply(saml2SpModel);
        try {
            return createSAMLSSOServiceProviderDO(serviceProviderDTO);
        } catch (IdentityException e) {
            throw handleException(e);
        }
    }

    @Deprecated
    private static SAMLSSOServiceProviderDTO createSAMLSpWithMetadataFile(String encodedMetaFileContent) {

        try {
            byte[] metaData = Base64.getDecoder().decode(encodedMetaFileContent.getBytes(StandardCharsets.UTF_8));
            String base64DecodedMetadata = new String(metaData, StandardCharsets.UTF_8);

            return createSAMLSpWithMetadataContent(base64DecodedMetadata);
        } catch (IdentitySAML2SSOException e) {
            throw handleException(e);
        }
    }

    private static SAMLSSOServiceProviderDO createSAMLSpWithMetadataFile_new(String encodedMetaFileContent) {
        try {
            byte[] metaData = Base64.getDecoder().decode(encodedMetaFileContent.getBytes(StandardCharsets.UTF_8));
            String base64DecodedMetadata = new String(metaData, StandardCharsets.UTF_8);

            if (logger.isDebugEnabled()) {
                logger.debug("Creating SAML Service Provider with metadata: " + base64DecodedMetadata);
            }
            return getServiceProviderDOFromMetadata(base64DecodedMetadata);
        } catch (IdentityException e) {
            throw handleException(e);
        }
    }

    private static SAMLSSOServiceProviderDO getServiceProviderDOFromMetadata(String metadata) throws IdentityException {
        SAMLSSOServiceProviderDO samlssoServiceProviderDO = new SAMLSSOServiceProviderDO();
        Registry registry = getConfigSystemRegistry();
        try {
            Parser parser = new Parser(registry);
            //pass metadata to samlSSOServiceProvider object
            samlssoServiceProviderDO = parser.parse(metadata, samlssoServiceProviderDO);
        } catch (InvalidMetadataException e) {
            throw buildClientException(INVALID_REQUEST, "Error parsing SAML SP metadata.", e);
        }
        if (samlssoServiceProviderDO.getX509Certificate() != null) {
            try {
                //save certificate
                saveCertificateToKeyStore(samlssoServiceProviderDO, (UserRegistry) registry);
            } catch (Exception e) {
                throw new IdentityException("Error occurred while setting certificate and alias", e);
            }
        }
        return samlssoServiceProviderDO;
    }

    private static SAMLSSOServiceProviderDTO createSAMLSpWithMetadataContent(String metadataContent)
            throws IdentitySAML2SSOException {

        SAMLSSOServiceProviderDTO serviceProviderDTO =
                getSamlSsoConfigService().uploadRPServiceProvider(metadataContent);
        return serviceProviderDTO;
    }

    @Deprecated
    private static SAMLSSOServiceProviderDTO createSAMLSpWithMetadataUrl(String metadataUrl) {

        try {
            SAMLSSOServiceProviderDTO serviceProviderDTO =
                    getSamlSsoConfigService().createServiceProviderWithMetadataURL(metadataUrl);
            return serviceProviderDTO;
        } catch (IdentitySAML2SSOException e) {
            throw handleException(e);
        }
    }

    private static SAMLSSOServiceProviderDO createSAMLSpWithMetadataUrl_new(String metadataUrl) {

        try {
            SAMLSSOServiceProviderDO serviceProviderDO =
                    createSAMLSpWithMetadataUrl_new_new(metadataUrl);
            return serviceProviderDO;
        } catch (IdentitySAML2SSOException e) {
            throw handleException(e);
        }
    }

    private static SAMLSSOServiceProviderDO createSAMLSpWithMetadataUrl_new_new(String metadataUrl) throws IdentitySAML2SSOException {
        InputStream in = null;
        try {
            URL url = new URL(metadataUrl);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(getConnectionTimeoutInMillis());
            con.setReadTimeout(getReadTimeoutInMillis());
            in = new BoundedInputStream(con.getInputStream(), getMaxSizeInBytes());

            String metadata = IOUtils.toString(in);
            return getServiceProviderDOFromMetadata(metadata);
        } catch (IOException e) {
            String tenantDomain = getTenantDomain();
            throw handleIOException(URL_NOT_FOUND, "Non-existing metadata URL for SAML service provider creation in tenantDomain: "
                    + tenantDomain, e);
        } catch (IdentityException e) {
            throw handleException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private static APIError handleException(IdentityException e) {

        String msg = "Error while creating/updating SAML inbound of application.";
        if (e instanceof IdentitySAML2ClientException) {
            if (URL_NOT_FOUND.getErrorCode().equals(e.getErrorCode())) {
                return buildNotFoundError(msg, e);
            }
            return buildBadRequestError(msg, e);
        } else {
            return buildServerError(msg, e);
        }
    }

    private static APIError buildBadRequestError(String message, IdentityException ex) {

        String errorCode = ex.getErrorCode();
        String errorDescription = ex.getMessage();

        return Utils.buildClientError(errorCode, message, errorDescription);
    }

    private static APIError buildNotFoundError(String message, IdentityException ex) {

        String errorCode = ex.getErrorCode();
        String errorDescription = ex.getMessage();

        return Utils.buildNotFoundError(errorCode, message, errorDescription);
    }

    private static APIError buildServerError(String message, IdentityException e) {

        String errorCode = e.getErrorCode();
        String errorDescription = e.getMessage();

        return Utils.buildServerError(errorCode, message, errorDescription, e);
    }

    private static SAMLSSOConfigServiceImpl getSamlSsoConfigService() {

        return ApplicationManagementServiceHolder.getSamlssoConfigService();
    }

    //new

    private static int getConnectionTimeoutInMillis() {
        return getHttpConnectionConfigValue("SSOService.SAMLMetadataUrlConnectionTimeout", 5000);
    }

    private static int getReadTimeoutInMillis() {
        return getHttpConnectionConfigValue("SSOService.SAMLMetadataUrlReadTimeout", 5000);
    }

    private static int getMaxSizeInBytes() {
        return getHttpConnectionConfigValue("SSOService.SAMLMetadataUrlResponseMaxSize", 51200);
    }

    private static int getHttpConnectionConfigValue(String xPath, int defaultValue) {
        int configValue = defaultValue;
        String config = IdentityUtil.getProperty(xPath);
        if (StringUtils.isNotBlank(config)) {
            try {
                configValue = Integer.parseInt(config);
            } catch (NumberFormatException var6) {
                logger.error("Provided HTTP connection config value in " + xPath + " should be an integer type. Value : " + config);
            }
        }

        return configValue;
    }

    private static String getTenantDomain() {
        return CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
    }

    private static IdentitySAML2SSOException handleIOException(Error error, String message, IOException e) {
        return new IdentitySAML2ClientException(error.getErrorCode(), message, e);
    }

    private static Registry getConfigSystemRegistry() throws IdentityException {

        String tenantDomain = getTenantDomain();
        try {
            int tenantId = IdentityTenantUtil.getTenantId(tenantDomain);
            IdentityTenantUtil.getTenantRegistryLoader().loadTenantRegistry(tenantId);
            if (logger.isDebugEnabled()) {
                logger.debug("Loading tenant registry for tenant domain: " + tenantDomain);
            }
        } catch (RegistryException e) {
            throw new IdentityException("Error loading tenant registry for tenant domain " + tenantDomain, e);
        }

        return (Registry) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getRegistry(RegistryType.SYSTEM_CONFIGURATION);
    }

    /**
     * Save Certificate To Key Store
     *
     * @param serviceProviderDO Service provider data object
     * @throws Exception exception
     */
    private static void saveCertificateToKeyStore(SAMLSSOServiceProviderDO serviceProviderDO, UserRegistry registry) throws Exception {

        KeyStoreManager manager = KeyStoreManager.getInstance(registry.getTenantId(), IdentitySAMLSSOServiceComponent
                .getServerConfigurationService(), IdentityTenantUtil.getRegistryService());

        if (MultitenantConstants.SUPER_TENANT_ID == registry.getTenantId()) {

            KeyStore keyStore = manager.getPrimaryKeyStore();

            // Admin should manually add the service provider signing certificate to the keystore file.
            // If the certificate is available we will set the alias of that certificate.
            String alias = keyStore.getCertificateAlias(serviceProviderDO.getX509Certificate());
            if (!StringUtils.isBlank(alias)) {
                serviceProviderDO.setCertAlias(alias);
            } else {
                serviceProviderDO.setCertAlias(null);
            }
        } else {

            String keyStoreName = getKeyStoreName(registry.getTenantId());
            KeyStore keyStore = manager.getKeyStore(keyStoreName);

            // Add new certificate
            keyStore.setCertificateEntry(serviceProviderDO.getIssuer(), serviceProviderDO.getX509Certificate());
            manager.updateKeyStore(keyStoreName, keyStore);
        }
    }
    /**
     * This method returns the key store file name from the domain Name
     *
     * @return key store name
     */
    private static String getKeyStoreName(int tenantId) {

        String ksName = IdentityTenantUtil.getTenantDomain(tenantId).replace(".", "-");
        return (ksName + ".jks");
    }

    private static IdentitySAML2ClientException buildClientException(Error error, String message, Exception e) {

        return new IdentitySAML2ClientException(error.getErrorCode(), message, e);
    }

    private static SAMLSSOServiceProviderDO createSAMLSSOServiceProviderDO(SAMLSSOServiceProviderDTO serviceProviderDTO) throws IdentityException {
        SAMLSSOServiceProviderDO serviceProviderDO = new SAMLSSOServiceProviderDO();

        //TODO: validate in SAML validator
//        validateIssuer(serviceProviderDTO.getIssuer());
//        validateIssuerQualifier(serviceProviderDTO.getIssuerQualifier());

        serviceProviderDO.setIssuer(serviceProviderDTO.getIssuer());

        serviceProviderDO.setIssuerQualifier(serviceProviderDTO.getIssuerQualifier());

        serviceProviderDO.setAssertionConsumerUrls(serviceProviderDTO.getAssertionConsumerUrls());
        serviceProviderDO.setDefaultAssertionConsumerUrl(serviceProviderDTO.getDefaultAssertionConsumerUrl());
        serviceProviderDO.setCertAlias(serviceProviderDTO.getCertAlias());
        serviceProviderDO.setDoSingleLogout(serviceProviderDTO.isDoSingleLogout());
        serviceProviderDO.setDoFrontChannelLogout(serviceProviderDTO.isDoFrontChannelLogout());
        serviceProviderDO.setFrontChannelLogoutBinding(serviceProviderDTO.getFrontChannelLogoutBinding());
        serviceProviderDO.setSloResponseURL(serviceProviderDTO.getSloResponseURL());
        serviceProviderDO.setSloRequestURL(serviceProviderDTO.getSloRequestURL());
        serviceProviderDO.setLoginPageURL(serviceProviderDTO.getLoginPageURL());
        serviceProviderDO.setDoSignResponse(serviceProviderDTO.isDoSignResponse());
        /*
        According to the spec, "The <Assertion> element(s) in the <Response> MUST be signed". Therefore we should not
        reply on any property to decide this behaviour. Hence the property is set to sign by default.
        */
        serviceProviderDO.setDoSignAssertions(true);
        serviceProviderDO.setNameIdClaimUri(serviceProviderDTO.getNameIdClaimUri());
        serviceProviderDO.setSigningAlgorithmUri(serviceProviderDTO.getSigningAlgorithmURI());
        serviceProviderDO.setDigestAlgorithmUri(serviceProviderDTO.getDigestAlgorithmURI());
        serviceProviderDO.setAssertionEncryptionAlgorithmUri(serviceProviderDTO.getAssertionEncryptionAlgorithmURI());
        serviceProviderDO.setKeyEncryptionAlgorithmUri(serviceProviderDTO.getKeyEncryptionAlgorithmURI());
        serviceProviderDO.setAssertionQueryRequestProfileEnabled(serviceProviderDTO
                .isAssertionQueryRequestProfileEnabled());
        serviceProviderDO.setSupportedAssertionQueryRequestTypes(serviceProviderDTO.getSupportedAssertionQueryRequestTypes());
        serviceProviderDO.setEnableSAML2ArtifactBinding(serviceProviderDTO.isEnableSAML2ArtifactBinding());
        serviceProviderDO.setDoValidateSignatureInArtifactResolve(serviceProviderDTO
                .isDoValidateSignatureInArtifactResolve());
        if (serviceProviderDTO.getNameIDFormat() == null) {
            serviceProviderDTO.setNameIDFormat(NameIdentifier.UNSPECIFIED);
        } else {
            serviceProviderDTO.setNameIDFormat(serviceProviderDTO.getNameIDFormat().replace("/", ":"));
        }

        serviceProviderDO.setNameIDFormat(serviceProviderDTO.getNameIDFormat());

        if (serviceProviderDTO.isEnableAttributeProfile()) {
            String attributeConsumingIndex = serviceProviderDTO.getAttributeConsumingServiceIndex();
            if (StringUtils.isNotEmpty(attributeConsumingIndex)) {
                serviceProviderDO.setAttributeConsumingServiceIndex(attributeConsumingIndex);
            } else {
                serviceProviderDO.setAttributeConsumingServiceIndex(Integer.toString(IdentityUtil.getRandomInteger()));
            }
            serviceProviderDO.setEnableAttributesByDefault(serviceProviderDTO.isEnableAttributesByDefault());
        } else {
            serviceProviderDO.setAttributeConsumingServiceIndex("");
            if (serviceProviderDO.isEnableAttributesByDefault()) {
                logger.warn("Enable Attribute Profile must be selected to activate it by default. " +
                        "EnableAttributesByDefault will be disabled.");
            }
            serviceProviderDO.setEnableAttributesByDefault(false);
        }

        if (serviceProviderDTO.getRequestedAudiences() != null && serviceProviderDTO.getRequestedAudiences().length != 0) {
            serviceProviderDO.setRequestedAudiences(serviceProviderDTO.getRequestedAudiences());
        }
        if (serviceProviderDTO.getRequestedRecipients() != null && serviceProviderDTO.getRequestedRecipients().length != 0) {
            serviceProviderDO.setRequestedRecipients(serviceProviderDTO.getRequestedRecipients());
        }
        serviceProviderDO.setIdPInitSSOEnabled(serviceProviderDTO.isIdPInitSSOEnabled());
        serviceProviderDO.setIdPInitSLOEnabled(serviceProviderDTO.isIdPInitSLOEnabled());
        serviceProviderDO.setIdpInitSLOReturnToURLs(serviceProviderDTO.getIdpInitSLOReturnToURLs());
        serviceProviderDO.setDoEnableEncryptedAssertion(serviceProviderDTO.isDoEnableEncryptedAssertion());
        serviceProviderDO.setDoValidateSignatureInRequests(serviceProviderDTO.isDoValidateSignatureInRequests());
        serviceProviderDO.setIdpEntityIDAlias(serviceProviderDTO.getIdpEntityIDAlias());
        return serviceProviderDO;
    }

    private static void addSAMLInboundProperties(List<Property> propertyList, SAMLSSOServiceProviderDO serviceProviderDO) {
        propertyList.add(addKeyValuePair(ISSUER, serviceProviderDO.getIssuer()));
        propertyList.add(addKeyValuePair(ISSUER_QUALIFIER, serviceProviderDO.getIssuerQualifier()));
        for (String url : serviceProviderDO.getAssertionConsumerUrls()) {
            propertyList.add(addKeyValuePair(ASSERTION_CONSUMER_URLS, url));
        }
        propertyList.add(addKeyValuePair(DEFAULT_ASSERTION_CONSUMER_URL, serviceProviderDO.getDefaultAssertionConsumerUrl()));
        propertyList.add(addKeyValuePair(SIGNING_ALGORITHM_URI, serviceProviderDO.getSigningAlgorithmUri()));
        propertyList.add(addKeyValuePair(DIGEST_ALGORITHM_URI, serviceProviderDO.getDigestAlgorithmUri()));
        propertyList.add(addKeyValuePair(ASSERTION_ENCRYPTION_ALGORITHM_URI,
                serviceProviderDO.getAssertionEncryptionAlgorithmUri()));
        propertyList.add(addKeyValuePair(KEY_ENCRYPTION_ALGORITHM_URI, serviceProviderDO.getKeyEncryptionAlgorithmUri()));
        propertyList.add(addKeyValuePair(CERT_ALIAS, serviceProviderDO.getCertAlias()));
        //TODO: check ATTRIBUTE_CONSUMING_SERVICE_INDEX

        //        propertyList.add(addKeyValuePair(ATTRIBUTE_CONSUMING_SERVICE_INDEX,
        //        serviceProviderDO.getAttributeConsumingServiceIndex()));
        propertyList.add(addKeyValuePair(DO_SIGN_RESPONSE, serviceProviderDO.isDoSignResponse() ? "true" : "false"));
        propertyList.add(addKeyValuePair(DO_SINGLE_LOGOUT, serviceProviderDO.isDoSingleLogout() ? "true" : "false"));
        propertyList.add(addKeyValuePair(DO_FRONT_CHANNEL_LOGOUT,
                serviceProviderDO.isDoFrontChannelLogout() ? "true" : "false"));
        propertyList.add(addKeyValuePair(FRONT_CHANNEL_LOGOUT_BINDING, serviceProviderDO.getFrontChannelLogoutBinding()));
        propertyList.add(addKeyValuePair(IS_ASSERTION_QUERY_REQUEST_PROFILE_ENABLED,
                serviceProviderDO.isAssertionQueryRequestProfileEnabled() ? "true" : "false"));
        propertyList.add(addKeyValuePair(SUPPORTED_ASSERTION_QUERY_REQUEST_TYPES,
                serviceProviderDO.getSupportedAssertionQueryRequestTypes()));
        propertyList.add(addKeyValuePair(ENABLE_SAML2_ARTIFACT_BINDING,
                serviceProviderDO.isEnableSAML2ArtifactBinding() ? "true" : "false"));
        propertyList.add(addKeyValuePair(DO_VALIDATE_SIGNATURE_IN_ARTIFACT_RESOLVE,
                serviceProviderDO.isDoValidateSignatureInArtifactResolve() ? "true" : "false"));
        propertyList.add(addKeyValuePair(LOGIN_PAGE_URL, serviceProviderDO.getLoginPageURL()));
        propertyList.add(addKeyValuePair(SLO_RESPONSE_URL, serviceProviderDO.getSloResponseURL()));
        propertyList.add(addKeyValuePair(SLO_REQUEST_URL, serviceProviderDO.getSloRequestURL()));
        for (String claim : serviceProviderDO.getRequestedClaims()) {
            propertyList.add(addKeyValuePair(REQUESTED_CLAIMS, claim));
        }
        for (String audience : serviceProviderDO.getRequestedAudiences()) {
            propertyList.add(addKeyValuePair(REQUESTED_AUDIENCES, audience));
        }
        for (String recipient : serviceProviderDO.getRequestedRecipients()) {
            propertyList.add(addKeyValuePair(REQUESTED_RECIPIENTS, recipient));
        }
        propertyList.add(addKeyValuePair( ENABLE_ATTRIBUTES_BY_DEFAULT,
                serviceProviderDO.isEnableAttributesByDefault() ? "true" : "false"));
        propertyList.add(addKeyValuePair(NAME_ID_CLAIM_URI, serviceProviderDO.getNameIdClaimUri()));
        propertyList.add(addKeyValuePair(NAME_ID_FORMAT, serviceProviderDO.getNameIDFormat()));
        propertyList.add(addKeyValuePair(IDP_INIT_SSO_ENABLED, serviceProviderDO.isIdPInitSSOEnabled() ? "true" : "false"));
        propertyList.add(addKeyValuePair(IDP_INIT_SLO_ENABLED, serviceProviderDO.isIdPInitSLOEnabled() ? "true" : "false"));
        for (String url : serviceProviderDO.getIdpInitSLOReturnToURLs()) {
            propertyList.add(addKeyValuePair(IDP_INIT_SLO_RETURN_TO_URLS, url));
        }
        propertyList.add(addKeyValuePair(DO_ENABLE_ENCRYPTED_ASSERTION,
                serviceProviderDO.isDoEnableEncryptedAssertion() ? "true" : "false"));
        propertyList.add(addKeyValuePair(DO_VALIDATE_SIGNATURE_IN_REQUESTS,
                serviceProviderDO.isDoValidateSignatureInRequests() ? "true" : "false"));
        propertyList.add(addKeyValuePair(IDP_ENTITY_ID_ALIAS, serviceProviderDO.getIdpEntityIDAlias()));
    }

    private static Property addKeyValuePair(String key, String value) {
        Property property = new Property();
        property.setName(key);
        property.setValue(value);
        return null;
    }

}
