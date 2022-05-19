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
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.context.RegistryType;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.SingleSignOnProfile;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
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
import org.wso2.carbon.identity.sso.saml.util.SAMLSSOUtil;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import java.util.stream.Collectors;

import static org.wso2.carbon.identity.sso.saml.Error.INVALID_REQUEST;
import static org.wso2.carbon.identity.sso.saml.Error.URL_NOT_FOUND;

/**
 * Helper functions for SAML inbound management.
 */
public class SAMLInboundFunctions {

    private static final String ATTRIBUTE_CONSUMING_SERVICE_INDEX = "attrConsumServiceIndex";
    private static final Log logger = LogFactory.getLog(SAMLInboundFunctions.class);

    private static final String ISSUER = "issuer";
    private static final String ISSUER_QUALIFIER = "issuerQualifier";
    private static final String ASSERTION_CONSUMER_URLS = "assertionConsumerUrls";
    private static final String DEFAULT_ASSERTION_CONSUMER_URL = "defaultAssertionConsumerUrl";
    private static final String SIGNING_ALGORITHM_URI = "signingAlgorithmURI";
    private static final String DIGEST_ALGORITHM_URI = "digestAlgorithmURI";
    private static final String ASSERTION_ENCRYPTION_ALGORITHM_URI = "assertionEncryptionAlgorithmURI";
    private static final String KEY_ENCRYPTION_ALGORITHM_URI = "keyEncryptionAlgorithmURI";
    private static final String CERT_ALIAS = "certAlias";
    private static final String DO_SIGN_RESPONSE = "doSignResponse";
    private static final String DO_SINGLE_LOGOUT = "doSingleLogout";
    private static final String DO_FRONT_CHANNEL_LOGOUT = "doFrontChannelLogout";
    private static final String FRONT_CHANNEL_LOGOUT_BINDING = "frontChannelLogoutBinding";
    private static final String IS_ASSERTION_QUERY_REQUEST_PROFILE_ENABLED = "isAssertionQueryRequestProfileEnabled";
    private static final String SUPPORTED_ASSERTION_QUERY_REQUEST_TYPES = "supportedAssertionQueryRequestTypes";
    private static final String ENABLE_SAML2_ARTIFACT_BINDING = "enableSAML2ArtifactBinding";
    private static final String DO_VALIDATE_SIGNATURE_IN_ARTIFACT_RESOLVE = "doValidateSignatureInArtifactResolve";
    private static final String LOGIN_PAGE_URL = "loginPageURL";
    private static final String SLO_RESPONSE_URL = "sloResponseURL";
    private static final String SLO_REQUEST_URL = "sloRequestURL";
    private static final String REQUESTED_CLAIMS = "requestedClaims";
    private static final String REQUESTED_AUDIENCES = "requestedAudiences";
    private static final String REQUESTED_RECIPIENTS = "requestedRecipients";
    private static final String ENABLE_ATTRIBUTES_BY_DEFAULT = "enableAttributesByDefault";
    private static final String NAME_ID_CLAIM_URI = "nameIdClaimUri";
    private static final String NAME_ID_FORMAT = "nameIDFormat";
    private static final String IDP_INIT_SSO_ENABLED = "idPInitSSOEnabled";
    private static final String IDP_INIT_SLO_ENABLED = "idPInitSLOEnabled";
    private static final String IDP_INIT_SLO_RETURN_TO_URLS = "idpInitSLOReturnToURLs";
    private static final String DO_ENABLE_ENCRYPTED_ASSERTION = "doEnableEncryptedAssertion";
    private static final String DO_VALIDATE_SIGNATURE_IN_REQUESTS = "doValidateSignatureInRequests";
    private static final String IDP_ENTITY_ID_ALIAS = "idpEntityIDAlias";
    private static final String IS_UPDATE = "isUpdate";
    private static final String METADATA = "metadata";

    private SAMLInboundFunctions() {

    }

    public static InboundAuthenticationRequestConfig putSAMLInbound(ServiceProvider application,
                                                                    SAML2Configuration saml2Configuration) {

        try {
            validateSingleSignOnProfileBindings(saml2Configuration);
        } catch (IdentityException e) {
            throw handleException(e);
        }

        try {
            InboundAuthenticationRequestConfig inboundConfig =  createSAMLInbound(saml2Configuration);
            Property[] properties = Arrays.stream(inboundConfig.getProperties()).filter(property ->
                    (!property.getName().equals(IS_UPDATE))).toArray(Property[]::new);
            List<Property> propertyList = new ArrayList<>(Arrays.asList(properties));
            addKeyValuePair(IS_UPDATE, "true", propertyList);
            inboundConfig.setProperties(propertyList.toArray(new Property[0]));
            return inboundConfig;
        } catch (APIError error) {
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

        List<Property> propertyList = new ArrayList<>();

        SAMLSSOServiceProviderDTO serviceProviderDTO;
        if (saml2Configuration.getMetadataFile() != null) {
            serviceProviderDTO = createSAMLSpWithMetadataFile(saml2Configuration.getMetadataFile(),
                    propertyList);
        } else if (saml2Configuration.getMetadataURL() != null) {
            serviceProviderDTO = createSAMLSpWithMetadataUrl(saml2Configuration.getMetadataURL(), propertyList);
        } else if (saml2Configuration.getManualConfiguration() != null) {
            serviceProviderDTO = createSAMLSpWithManualConfiguration(saml2Configuration.getManualConfiguration());
        } else {
            throw Utils.buildBadRequestError("Invalid SAML2 Configuration. One of metadataFile, metaDataUrl or " +
                    "serviceProvider manual configuration needs to be present.");
        }

        InboundAuthenticationRequestConfig samlInbound = new InboundAuthenticationRequestConfig();
        samlInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.SAML2);
        samlInbound.setInboundAuthKey(serviceProviderDTO.getIssuer());

        addSAMLInboundProperties(propertyList, serviceProviderDTO);

        Property[] properties = propertyList.toArray(new Property[0]);
        samlInbound.setProperties(properties);
        return samlInbound;
    }

    public static SAML2ServiceProvider getSAML2ServiceProvider(InboundAuthenticationRequestConfig inboundAuth) {

        if (inboundAuth.getProperties() == null) {
            return null;
        }
        SAMLSSOServiceProviderDTO serviceProvider = getServiceProviderDTO(inboundAuth.getProperties());
        return new SAMLSSOServiceProviderToAPIModel().apply(serviceProvider);
    }

    private static SAMLSSOServiceProviderDTO getServiceProviderDTO(Property[] properties) {
        HashMap<String, List<String>> map = new HashMap<>(Arrays.stream(properties).collect(Collectors.groupingBy(
                Property::getName, Collectors.mapping(Property::getValue, Collectors.toList()))));

        SAMLSSOServiceProviderDTO serviceProviderDTO = new SAMLSSOServiceProviderDTO();
        if (map.containsKey(ISSUER)) {
            serviceProviderDTO.setIssuer(map.get(ISSUER).get(0));
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("SAML SP not found for issuer: " + ISSUER + " in tenantDomain: " + getTenantDomain());
            }
            return null;
        }
        serviceProviderDTO.setIssuerQualifier(getSingleValue(map, ISSUER_QUALIFIER));
        serviceProviderDTO.setAssertionConsumerUrls(getMultiValues(map, ASSERTION_CONSUMER_URLS));

        serviceProviderDTO.setDefaultAssertionConsumerUrl(getSingleValue(map, DEFAULT_ASSERTION_CONSUMER_URL));
        serviceProviderDTO.setSigningAlgorithmURI(getSingleValue(map, SIGNING_ALGORITHM_URI));
        serviceProviderDTO.setDigestAlgorithmURI(getSingleValue(map, DIGEST_ALGORITHM_URI));
        serviceProviderDTO.setAssertionEncryptionAlgorithmURI(getSingleValue(map, ASSERTION_ENCRYPTION_ALGORITHM_URI));
        serviceProviderDTO.setKeyEncryptionAlgorithmURI(getSingleValue(map, KEY_ENCRYPTION_ALGORITHM_URI));
        serviceProviderDTO.setCertAlias(getSingleValue(map, CERT_ALIAS));
        serviceProviderDTO.setAttributeConsumingServiceIndex(getSingleValue(map, ATTRIBUTE_CONSUMING_SERVICE_INDEX));

        if (map.containsKey(ATTRIBUTE_CONSUMING_SERVICE_INDEX)
                && StringUtils.isNotBlank(map.get(ATTRIBUTE_CONSUMING_SERVICE_INDEX).get(0))) {
            serviceProviderDTO.setEnableAttributeProfile(true);
        }

        serviceProviderDTO.setDoSignResponse(Boolean.parseBoolean(getSingleValue(map, DO_SIGN_RESPONSE)));
                /*
                According to the spec, "The <Assertion> element(s) in the <Response> MUST be signed". Therefore we
                should not reply on any property to decide this behaviour. Hence the property is set to sign by default.
                */
        serviceProviderDTO.setDoSignAssertions(true);
        serviceProviderDTO.setDoSingleLogout(Boolean.parseBoolean(getSingleValue(map, DO_SINGLE_LOGOUT)));
        serviceProviderDTO.setDoFrontChannelLogout(Boolean.parseBoolean(getSingleValue(map, DO_FRONT_CHANNEL_LOGOUT)));
        serviceProviderDTO.setFrontChannelLogoutBinding(getSingleValue(map, FRONT_CHANNEL_LOGOUT_BINDING));
        serviceProviderDTO.setAssertionQueryRequestProfileEnabled(Boolean.parseBoolean(
                getSingleValue(map, IS_ASSERTION_QUERY_REQUEST_PROFILE_ENABLED)));
        serviceProviderDTO.setSupportedAssertionQueryRequestTypes(
                getSingleValue(map, SUPPORTED_ASSERTION_QUERY_REQUEST_TYPES));
        serviceProviderDTO.setEnableSAML2ArtifactBinding(Boolean.parseBoolean(
                getSingleValue(map, ENABLE_SAML2_ARTIFACT_BINDING)));
        serviceProviderDTO.setDoValidateSignatureInArtifactResolve(
                Boolean.parseBoolean(getSingleValue(map, DO_VALIDATE_SIGNATURE_IN_ARTIFACT_RESOLVE)));

        if (!map.containsKey(LOGIN_PAGE_URL) || map.get(LOGIN_PAGE_URL).get(0) == null
                || "null".equals(map.get(LOGIN_PAGE_URL).get(0))) {
            serviceProviderDTO.setLoginPageURL("");
        } else {
            serviceProviderDTO.setLoginPageURL(getSingleValue(map, LOGIN_PAGE_URL));
        }

        serviceProviderDTO.setSloResponseURL(getSingleValue(map, SLO_RESPONSE_URL));
        serviceProviderDTO.setSloRequestURL(getSingleValue(map, SLO_REQUEST_URL));
        serviceProviderDTO.setRequestedClaims(getMultiValues(map, REQUESTED_CLAIMS));
        serviceProviderDTO.setRequestedAudiences(getMultiValues(map, REQUESTED_AUDIENCES));
        serviceProviderDTO.setRequestedRecipients(getMultiValues(map, REQUESTED_RECIPIENTS));
        serviceProviderDTO.setEnableAttributesByDefault(Boolean.parseBoolean(
                getSingleValue(map, ENABLE_ATTRIBUTES_BY_DEFAULT)));
        serviceProviderDTO.setNameIdClaimUri(getSingleValue(map, NAME_ID_CLAIM_URI));
        serviceProviderDTO.setNameIDFormat(getSingleValue(map, NAME_ID_FORMAT));

        if (serviceProviderDTO.getNameIDFormat() == null) {
            serviceProviderDTO.setNameIDFormat(NameIdentifier.UNSPECIFIED);
        }
        serviceProviderDTO.setNameIDFormat(serviceProviderDTO.getNameIDFormat().replace(":", "/"));

        serviceProviderDTO.setIdPInitSSOEnabled(Boolean.parseBoolean(getSingleValue(map, IDP_INIT_SSO_ENABLED)));
        serviceProviderDTO.setIdPInitSLOEnabled(Boolean.parseBoolean(getSingleValue(map, IDP_INIT_SLO_ENABLED)));
        serviceProviderDTO.setIdpInitSLOReturnToURLs(getMultiValues(map, IDP_INIT_SLO_RETURN_TO_URLS));
        serviceProviderDTO.setDoEnableEncryptedAssertion(Boolean.parseBoolean(
                getSingleValue(map, DO_ENABLE_ENCRYPTED_ASSERTION)));
        serviceProviderDTO.setDoValidateSignatureInRequests(Boolean.parseBoolean(
                getSingleValue(map, DO_VALIDATE_SIGNATURE_IN_REQUESTS)));
        serviceProviderDTO.setIdpEntityIDAlias(getSingleValue(map, IDP_ENTITY_ID_ALIAS));
        return serviceProviderDTO;
    }

    private static String[] getMultiValues(HashMap<String, List<String>> map, String key) {
        if (key != null && map.containsKey(key) && map.get(key) != null) {
            return map.get(key).toArray(new String[0]);
        }
        return new String[0];
    }

    private static String getSingleValue(HashMap<String, List<String>> map, String key) {
        if (key != null && map.containsKey(key) && map.get(key) != null) {
            return map.get(key).get(0);
        }
        return null;
    }

    public static void deleteSAMLServiceProvider(InboundAuthenticationRequestConfig inbound) {

        try {
            String issuer = inbound.getInboundAuthKey();
            ApplicationManagementServiceHolder.getSamlssoConfigService().removeServiceProvider(issuer);
        } catch (IdentityException e) {
            throw buildServerError("Error while trying to rollback SAML2 configuration. " + e.getMessage(), e);
        }
    }

    private static SAMLSSOServiceProviderDTO createSAMLSpWithManualConfiguration(SAML2ServiceProvider saml2SpModel) {

        SAMLSSOServiceProviderDTO serviceProviderDTO = new ApiModelToSAMLSSOServiceProvider().apply(saml2SpModel);
        //TODO : update DTO like in SAMLSSOConfigAdmin
        return serviceProviderDTO;
    }

    private static SAMLSSOServiceProviderDTO createSAMLSpWithMetadataFile(String encodedMetaFileContent,
                                                                         List<Property> propertyList) {
        try {
            byte[] metaData = Base64.getDecoder().decode(encodedMetaFileContent.getBytes(StandardCharsets.UTF_8));
            String base64DecodedMetadata = new String(metaData, StandardCharsets.UTF_8);

            if (logger.isDebugEnabled()) {
                logger.debug("Creating SAML Service Provider with metadata: " + base64DecodedMetadata);
            }
            return getServiceProviderDTOFromMetadata(base64DecodedMetadata, propertyList);
        } catch (IdentityException e) {
            throw handleException(e);
        }
    }

    private static SAMLSSOServiceProviderDTO getServiceProviderDTOFromMetadata(String metadata,
                                                                               List<Property> propertyList)
            throws IdentityException {
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
            addKeyValuePair(METADATA, metadata, propertyList);
        }
        return createSAMLSSOServiceProviderDTO(samlssoServiceProviderDO);
    }

    private static SAMLSSOServiceProviderDTO createSAMLSpWithMetadataUrl(String metadataUrl,
                                                                        List<Property> propertyList) {

        try {
            SAMLSSOServiceProviderDTO serviceProviderDTO =
                    createSAMLServiceProviderDOWithMetadataUrl(metadataUrl, propertyList);
            return serviceProviderDTO;
        } catch (IdentityException e) {
            throw handleException(e);
        }
    }

    private static SAMLSSOServiceProviderDTO createSAMLServiceProviderDOWithMetadataUrl(String metadataUrl,
                                                                                       List<Property> propertyList)
            throws IdentitySAML2SSOException {

        InputStream in = null;
        try {
            URL url = new URL(metadataUrl);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(getConnectionTimeoutInMillis());
            con.setReadTimeout(getReadTimeoutInMillis());
            in = new BoundedInputStream(con.getInputStream(), getMaxSizeInBytes());

            String metadata = IOUtils.toString(in);
            return getServiceProviderDTOFromMetadata(metadata, propertyList);
        } catch (IOException e) {
            String tenantDomain = getTenantDomain();
            throw handleIOException(URL_NOT_FOUND, "Non-existing metadata URL for SAML service provider creation in " +
                    "tenantDomain: " + tenantDomain, e);
        } catch (IdentityException e) {
            throw handleException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private static SAMLSSOServiceProviderDTO createSAMLSSOServiceProviderDTO(SAMLSSOServiceProviderDO serviceProviderDO)
            throws IdentityException {
        SAMLSSOServiceProviderDTO serviceProviderDTO = new SAMLSSOServiceProviderDTO();

        serviceProviderDTO.setIssuer(serviceProviderDO.getIssuer());

        serviceProviderDTO.setIssuerQualifier(serviceProviderDO.getIssuerQualifier());

        serviceProviderDTO.setAssertionConsumerUrls(serviceProviderDO.getAssertionConsumerUrls());
        serviceProviderDTO.setDefaultAssertionConsumerUrl(serviceProviderDO.getDefaultAssertionConsumerUrl());
        serviceProviderDTO.setCertAlias(serviceProviderDO.getCertAlias());

        try {

            if (serviceProviderDO.getX509Certificate() != null) {
                serviceProviderDTO.setCertificateContent(IdentityUtil.convertCertificateToPEM(
                        serviceProviderDO.getX509Certificate()));
            }
        } catch (CertificateException e) {
            throw new IdentityException("An error occurred while converting the application certificate to " +
                    "PEM content.", e);
        }

        serviceProviderDTO.setDoSingleLogout(serviceProviderDO.isDoSingleLogout());
        serviceProviderDTO.setDoFrontChannelLogout(serviceProviderDO.isDoFrontChannelLogout());
        serviceProviderDTO.setFrontChannelLogoutBinding(serviceProviderDO.getFrontChannelLogoutBinding());
        serviceProviderDTO.setLoginPageURL(serviceProviderDO.getLoginPageURL());
        serviceProviderDTO.setSloRequestURL(serviceProviderDO.getSloRequestURL());
        serviceProviderDTO.setSloResponseURL(serviceProviderDO.getSloResponseURL());
        serviceProviderDTO.setDoSignResponse(serviceProviderDO.isDoSignResponse());
        /*
        According to the spec, "The <Assertion> element(s) in the <Response> MUST be signed". Therefore we should not
        reply on any property to decide this behaviour. Hence the property is set to sign by default.
        */
        serviceProviderDTO.setDoSignAssertions(true);
        serviceProviderDTO.setNameIdClaimUri(serviceProviderDO.getNameIdClaimUri());
        serviceProviderDTO.setSigningAlgorithmURI(serviceProviderDO.getSigningAlgorithmUri());
        serviceProviderDTO.setDigestAlgorithmURI(serviceProviderDO.getDigestAlgorithmUri());
        serviceProviderDTO.setAssertionEncryptionAlgorithmURI(serviceProviderDO.getAssertionEncryptionAlgorithmUri());
        serviceProviderDTO.setKeyEncryptionAlgorithmURI(serviceProviderDO.getKeyEncryptionAlgorithmUri());
        serviceProviderDTO.setAssertionQueryRequestProfileEnabled(serviceProviderDO
                .isAssertionQueryRequestProfileEnabled());
        serviceProviderDTO.setSupportedAssertionQueryRequestTypes(serviceProviderDO
                .getSupportedAssertionQueryRequestTypes());
        serviceProviderDTO.setEnableAttributesByDefault(serviceProviderDO.isEnableAttributesByDefault());
        serviceProviderDTO.setEnableSAML2ArtifactBinding(serviceProviderDO.isEnableSAML2ArtifactBinding());
        serviceProviderDTO.setDoValidateSignatureInArtifactResolve(serviceProviderDO
                .isDoValidateSignatureInArtifactResolve());

        if (serviceProviderDO.getNameIDFormat() == null) {
            serviceProviderDO.setNameIDFormat(NameIdentifier.UNSPECIFIED);
        } else {
            serviceProviderDO.setNameIDFormat(serviceProviderDO.getNameIDFormat().replace("/", ":"));
        }

        serviceProviderDTO.setNameIDFormat(serviceProviderDO.getNameIDFormat());

        if (StringUtils.isNotBlank(serviceProviderDO.getAttributeConsumingServiceIndex())) {
            serviceProviderDTO.setAttributeConsumingServiceIndex(serviceProviderDO.getAttributeConsumingServiceIndex());
            serviceProviderDTO.setEnableAttributeProfile(true);
        }

        if (serviceProviderDO.getRequestedAudiences() != null && serviceProviderDO.getRequestedAudiences().length !=
                0) {
            serviceProviderDTO.setRequestedAudiences(serviceProviderDO.getRequestedAudiences());
        }
        if (serviceProviderDO.getRequestedRecipients() != null && serviceProviderDO.getRequestedRecipients().length
                != 0) {
            serviceProviderDTO.setRequestedRecipients(serviceProviderDO.getRequestedRecipients());
        }
        serviceProviderDTO.setIdPInitSSOEnabled(serviceProviderDO.isIdPInitSSOEnabled());
        serviceProviderDTO.setDoEnableEncryptedAssertion(serviceProviderDO.isDoEnableEncryptedAssertion());
        serviceProviderDTO.setDoValidateSignatureInRequests(serviceProviderDO.isDoValidateSignatureInRequests());
        serviceProviderDTO.setIdpEntityIDAlias(serviceProviderDO.getIdpEntityIDAlias());
        return serviceProviderDTO;
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
                logger.error("Provided HTTP connection config value in " + xPath + " should be an integer type. " +
                        "Value : " + config);
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

    private static IdentitySAML2ClientException buildClientException(Error error, String message, Exception e) {

        return new IdentitySAML2ClientException(error.getErrorCode(), message, e);
    }

    private static void addSAMLInboundProperties(List<Property> propertyList,
                                                 SAMLSSOServiceProviderDTO serviceProviderDTO) {
        if (StringUtils.isNotBlank(serviceProviderDTO.getIssuerQualifier())) {
            serviceProviderDTO.setIssuer(SAMLSSOUtil.getIssuerWithQualifier(serviceProviderDTO.getIssuer(),
                    serviceProviderDTO.getIssuerQualifier()));
        }
        addKeyValuePair(ISSUER, serviceProviderDTO.getIssuer(), propertyList);
        addKeyValuePair(ISSUER_QUALIFIER, serviceProviderDTO.getIssuerQualifier(), propertyList);
        for (String url : serviceProviderDTO.getAssertionConsumerUrls()) {
            addKeyValuePair(ASSERTION_CONSUMER_URLS, url, propertyList);
        }
        addKeyValuePair(DEFAULT_ASSERTION_CONSUMER_URL,
                serviceProviderDTO.getDefaultAssertionConsumerUrl(), propertyList);
        addKeyValuePair(SIGNING_ALGORITHM_URI, serviceProviderDTO.getSigningAlgorithmURI(), propertyList);
        addKeyValuePair(DIGEST_ALGORITHM_URI, serviceProviderDTO.getDigestAlgorithmURI(), propertyList);
        addKeyValuePair(ASSERTION_ENCRYPTION_ALGORITHM_URI,
                serviceProviderDTO.getAssertionEncryptionAlgorithmURI(), propertyList);
        addKeyValuePair(KEY_ENCRYPTION_ALGORITHM_URI,
                serviceProviderDTO.getKeyEncryptionAlgorithmURI(), propertyList);
        addKeyValuePair(CERT_ALIAS, serviceProviderDTO.getCertAlias(), propertyList);
        addKeyValuePair(ATTRIBUTE_CONSUMING_SERVICE_INDEX, serviceProviderDTO.getAttributeConsumingServiceIndex(),
                propertyList);
        addKeyValuePair(DO_SIGN_RESPONSE, serviceProviderDTO.isDoSignResponse() ? "true" : "false", propertyList);
        addKeyValuePair(DO_SINGLE_LOGOUT, serviceProviderDTO.isDoSingleLogout() ? "true" : "false", propertyList);
        addKeyValuePair(DO_FRONT_CHANNEL_LOGOUT,
                serviceProviderDTO.isDoFrontChannelLogout() ? "true" : "false", propertyList);
        addKeyValuePair(FRONT_CHANNEL_LOGOUT_BINDING,
                serviceProviderDTO.getFrontChannelLogoutBinding(), propertyList);
        addKeyValuePair(IS_ASSERTION_QUERY_REQUEST_PROFILE_ENABLED,
                serviceProviderDTO.isAssertionQueryRequestProfileEnabled() ? "true" : "false", propertyList);
        addKeyValuePair(SUPPORTED_ASSERTION_QUERY_REQUEST_TYPES,
                serviceProviderDTO.getSupportedAssertionQueryRequestTypes(), propertyList);
        addKeyValuePair(ENABLE_SAML2_ARTIFACT_BINDING,
                serviceProviderDTO.isEnableSAML2ArtifactBinding() ? "true" : "false", propertyList);
        addKeyValuePair(DO_VALIDATE_SIGNATURE_IN_ARTIFACT_RESOLVE,
                serviceProviderDTO.isDoValidateSignatureInArtifactResolve() ? "true" : "false", propertyList);
        addKeyValuePair(LOGIN_PAGE_URL, serviceProviderDTO.getLoginPageURL(), propertyList);
        addKeyValuePair(SLO_RESPONSE_URL, serviceProviderDTO.getSloResponseURL(), propertyList);
        addKeyValuePair(SLO_REQUEST_URL, serviceProviderDTO.getSloRequestURL(), propertyList);
        for (String claim : serviceProviderDTO.getRequestedClaims()) {
            addKeyValuePair(REQUESTED_CLAIMS, claim, propertyList);
        }
        for (String audience : serviceProviderDTO.getRequestedAudiences()) {
            addKeyValuePair(REQUESTED_AUDIENCES, audience, propertyList);
        }
        for (String recipient : serviceProviderDTO.getRequestedRecipients()) {
            addKeyValuePair(REQUESTED_RECIPIENTS, recipient, propertyList);
        }
        addKeyValuePair(ENABLE_ATTRIBUTES_BY_DEFAULT,
                serviceProviderDTO.isEnableAttributesByDefault() ? "true" : "false", propertyList);
        addKeyValuePair(NAME_ID_CLAIM_URI, serviceProviderDTO.getNameIdClaimUri(), propertyList);
        addKeyValuePair(NAME_ID_FORMAT, serviceProviderDTO.getNameIDFormat(), propertyList);
        addKeyValuePair(IDP_INIT_SSO_ENABLED,
                serviceProviderDTO.isIdPInitSSOEnabled() ? "true" : "false", propertyList);
        addKeyValuePair(IDP_INIT_SLO_ENABLED,
                serviceProviderDTO.isIdPInitSLOEnabled() ? "true" : "false", propertyList);
        for (String url : serviceProviderDTO.getIdpInitSLOReturnToURLs()) {
            addKeyValuePair(IDP_INIT_SLO_RETURN_TO_URLS, url, propertyList);
        }
        addKeyValuePair(DO_ENABLE_ENCRYPTED_ASSERTION,
                serviceProviderDTO.isDoEnableEncryptedAssertion() ? "true" : "false", propertyList);
        addKeyValuePair(DO_VALIDATE_SIGNATURE_IN_REQUESTS,
                serviceProviderDTO.isDoValidateSignatureInRequests() ? "true" : "false", propertyList);
        addKeyValuePair(IDP_ENTITY_ID_ALIAS, serviceProviderDTO.getIdpEntityIDAlias(), propertyList);
        addKeyValuePair(IS_UPDATE, "false", propertyList);
    }

    private static void addKeyValuePair(String key, String value, List<Property> propertyList) {
        if (value == null) {
            return;
        }
        Property property = new Property();
        property.setName(key);
        property.setValue(value);
        propertyList.add(property);
    }

}
