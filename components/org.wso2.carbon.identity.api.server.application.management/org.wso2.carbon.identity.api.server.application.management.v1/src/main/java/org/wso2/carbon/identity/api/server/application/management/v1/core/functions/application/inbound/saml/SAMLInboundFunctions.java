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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensaml.saml.saml1.core.NameIdentifier;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
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
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.sso.saml.SAMLSSOConstants;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.identity.sso.saml.exception.IdentitySAML2ClientException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.sso.saml.Error.URL_NOT_FOUND;

/**
 * Helper functions for SAML inbound management.
 */
public class SAMLInboundFunctions {

    private static final Log logger = LogFactory.getLog(SAMLInboundFunctions.class);

    private SAMLInboundFunctions() {

    }

    public static InboundAuthenticationRequestConfig putSAMLInbound(ServiceProvider application,
                                                                    SAML2Configuration saml2Configuration) {

        try {
            validateSingleSignOnProfileBindings(saml2Configuration);
        } catch (IdentityException e) {
            throw handleException(e);
        }

        InboundAuthenticationRequestConfig inboundConfig = createSAMLInbound(saml2Configuration);
        Property[] properties = Arrays.stream(inboundConfig.getProperties()).filter(property ->
                (!property.getName().equals(SAMLSSOConstants.Metadata.IS_UPDATE))).toArray(Property[]::new);
        List<Property> propertyList = new ArrayList<>(Arrays.asList(properties));
        addKeyValuePair(SAMLSSOConstants.Metadata.IS_UPDATE, "true", propertyList);
        inboundConfig.setProperties(propertyList.toArray(new Property[0]));
        return inboundConfig;
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

    public static InboundAuthenticationRequestConfig createSAMLInbound(SAML2Configuration saml2Configuration) {

        InboundAuthenticationRequestConfig samlInbound = new InboundAuthenticationRequestConfig();
        List<Property> propertyList;
        if (samlInbound.getProperties() == null || samlInbound.getProperties().length == 0) {
            propertyList = new ArrayList<>();
        } else {
            propertyList = Arrays.asList(samlInbound.getProperties());
        }
        if (saml2Configuration.getMetadataFile() != null) {
            addKeyValuePair(SAMLSSOConstants.Metadata.METADATA_FILE, saml2Configuration.getMetadataFile(),
                    propertyList);
        } else if (saml2Configuration.getMetadataURL() != null) {
            addKeyValuePair(SAMLSSOConstants.Metadata.METADATA_URL, saml2Configuration.getMetadataURL(), propertyList);
        } else if (saml2Configuration.getManualConfiguration() != null) {
            SAMLSSOServiceProviderDTO serviceProviderDTO =
                    createSAMLSpWithManualConfiguration(saml2Configuration.getManualConfiguration());
            addSAMLInboundProperties(propertyList, serviceProviderDTO);
        } else {
            throw Utils.buildBadRequestError("Invalid SAML2 Configuration. One of metadataFile, metaDataUrl or " +
                    "serviceProvider manual configuration needs to be present.");
        }
        samlInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.SAML2);
        addKeyValuePair(SAMLSSOConstants.Metadata.IS_UPDATE, "false", propertyList);

        Property[] properties = propertyList.toArray(new Property[0]);
        samlInbound.setProperties(properties);
        return samlInbound;
    }

    public static SAML2ServiceProvider getSAML2ServiceProvider(InboundAuthenticationRequestConfig inboundAuth) {

        if (inboundAuth == null || inboundAuth.getProperties() == null) {
            return null;
        }
        SAMLSSOServiceProviderDTO serviceProvider = getServiceProviderDTO(inboundAuth.getProperties());
        return new SAMLSSOServiceProviderToAPIModel().apply(serviceProvider);
    }

    private static SAMLSSOServiceProviderDTO getServiceProviderDTO(Property[] properties) {
        HashMap<String, List<String>> map = new HashMap<>(Arrays.stream(properties).collect(Collectors.groupingBy(
                Property::getName, Collectors.mapping(Property::getValue, Collectors.toList()))));

        SAMLSSOServiceProviderDTO serviceProviderDTO = new SAMLSSOServiceProviderDTO();
        serviceProviderDTO.setIssuer(getSingleValue(map, SAMLSSOConstants.Metadata.ISSUER));

        serviceProviderDTO.setAssertionConsumerUrls(getMultiValues(map,
                SAMLSSOConstants.Metadata.ASSERTION_CONSUMER_URLS));
        serviceProviderDTO.setDefaultAssertionConsumerUrl(getSingleValue(map,
                SAMLSSOConstants.Metadata.DEFAULT_ASSERTION_CONSUMER_URL));
        serviceProviderDTO.setCertAlias(getSingleValue(map, SAMLSSOConstants.Metadata.ISSUER_CERT_ALIAS));

        if (StringUtils.isNotEmpty(getSingleValue(map, SAMLSSOConstants.Metadata.SIGNING_ALGORITHM))) {
            serviceProviderDTO.setSigningAlgorithmURI(getSingleValue(map, SAMLSSOConstants.Metadata.SIGNING_ALGORITHM));
        }

        if (getSingleValue(map, SAMLSSOConstants.Metadata.ASSERTION_QUERY_REQUEST_PROFILE_ENABLED) != null) {
            serviceProviderDTO.setAssertionQueryRequestProfileEnabled(Boolean.parseBoolean(getSingleValue(map,
                    SAMLSSOConstants.Metadata.ASSERTION_QUERY_REQUEST_PROFILE_ENABLED).trim()));
        }

        if (getSingleValue(map, SAMLSSOConstants.Metadata.SUPPORTED_ASSERTION_QUERY_REQUEST_TYPES) != null) {
            serviceProviderDTO.setSupportedAssertionQueryRequestTypes(getSingleValue(map,
                    SAMLSSOConstants.Metadata.SUPPORTED_ASSERTION_QUERY_REQUEST_TYPES).trim());
        }

        if (getSingleValue(map, SAMLSSOConstants.Metadata.ENABLE_SAML2_ARTIFACT_BINDING) != null) {
            serviceProviderDTO.setEnableSAML2ArtifactBinding(Boolean.parseBoolean(getSingleValue(map,
                    SAMLSSOConstants.Metadata.ENABLE_SAML2_ARTIFACT_BINDING).trim()));
        }

        if (StringUtils.isNotEmpty(getSingleValue(map, SAMLSSOConstants.Metadata.DIGEST_ALGORITHM))) {
            serviceProviderDTO.setDigestAlgorithmURI(getSingleValue(map, SAMLSSOConstants.Metadata.DIGEST_ALGORITHM));
        }

        if (StringUtils.isNotEmpty(getSingleValue(map, SAMLSSOConstants.Metadata.ASSERTION_ENCRYPTION_ALGORITHM))) {
            serviceProviderDTO.setAssertionEncryptionAlgorithmURI(getSingleValue(map,
                    SAMLSSOConstants.Metadata.ASSERTION_ENCRYPTION_ALGORITHM));
        }

        if (StringUtils.isNotEmpty(getSingleValue(map, SAMLSSOConstants.Metadata.KEY_ENCRYPTION_ALGORITHM))) {
            serviceProviderDTO.setKeyEncryptionAlgorithmURI(getSingleValue(map,
                    SAMLSSOConstants.Metadata.KEY_ENCRYPTION_ALGORITHM));
        }

        if (getSingleValue(map, SAMLSSOConstants.Metadata.DO_SINGLE_LOGOUT) != null) {
            serviceProviderDTO.setDoSingleLogout(Boolean.parseBoolean(getSingleValue(map,
                    SAMLSSOConstants.Metadata.DO_SINGLE_LOGOUT).trim()));
        }

        if (getSingleValue(map, SAMLSSOConstants.Metadata.NAME_ID_FORMAT) != null) {
            serviceProviderDTO.setNameIDFormat(getSingleValue(map,
                    SAMLSSOConstants.Metadata.NAME_ID_FORMAT));
        }

        if (getSingleValue(map, SAMLSSOConstants.Metadata.ENABLE_NAME_ID_CLAIM_URI) != null) {
            if (Boolean.parseBoolean(getSingleValue(map, SAMLSSOConstants.Metadata.ENABLE_NAME_ID_CLAIM_URI).trim())) {
                serviceProviderDTO.setNameIdClaimUri(getSingleValue(map, SAMLSSOConstants.Metadata.NAME_ID_CLAIM_URI));
            }
        }

        serviceProviderDTO.setLoginPageURL(getSingleValue(map, SAMLSSOConstants.Metadata.LOGIN_PAGE_URL));

        if (getSingleValue(map, SAMLSSOConstants.Metadata.DO_SIGN_RESPONSE) != null) {
            serviceProviderDTO.setDoSignResponse(Boolean.parseBoolean(getSingleValue(map,
                    SAMLSSOConstants.Metadata.DO_SIGN_RESPONSE).trim()));
        }

        if (serviceProviderDTO.isDoSingleLogout()) {
            serviceProviderDTO.setSloResponseURL(getSingleValue(map, SAMLSSOConstants.Metadata.SLO_RESPONSE_URL));
            serviceProviderDTO.setSloRequestURL(getSingleValue(map, SAMLSSOConstants.Metadata.SLO_REQUEST_URL));
            // Check front channel logout enable.
            if (getSingleValue(map, SAMLSSOConstants.Metadata.DO_FRONT_CHANNEL_LOGOUT) != null) {
                serviceProviderDTO.setDoFrontChannelLogout(Boolean.parseBoolean(getSingleValue(map,
                        SAMLSSOConstants.Metadata.DO_FRONT_CHANNEL_LOGOUT).trim()));
                if (serviceProviderDTO.isDoFrontChannelLogout()) {
                    if (getSingleValue(map, SAMLSSOConstants.Metadata.FRONT_CHANNEL_LOGOUT_BINDING) != null) {
                        serviceProviderDTO.setFrontChannelLogoutBinding(getSingleValue(map,
                                SAMLSSOConstants.Metadata.FRONT_CHANNEL_LOGOUT_BINDING));
                    } else {
                        // Default is redirect-binding.
                        serviceProviderDTO.setFrontChannelLogoutBinding(SAMLSSOConstants.Metadata
                                .DEFAULT_FRONT_CHANNEL_LOGOUT_BINDING);
                    }

                }
            }
        }

        if (getSingleValue(map, SAMLSSOConstants.Metadata.DO_SIGN_ASSERTIONS) != null) {
            serviceProviderDTO.setDoSignAssertions(Boolean.parseBoolean(getSingleValue(map,
                    SAMLSSOConstants.Metadata.DO_SIGN_ASSERTIONS).trim()));
        }

        if (getSingleValue(map, SAMLSSOConstants.Metadata.ENABLE_ECP) != null) {
            serviceProviderDTO.setSamlECP(Boolean.parseBoolean(getSingleValue(map,
                    SAMLSSOConstants.Metadata.ENABLE_ECP).trim()));
        }

        if (getSingleValue(map, SAMLSSOConstants.Metadata.ATTRIBUTE_CONSUMING_SERVICE_INDEX) != null) {
            serviceProviderDTO.setAttributeConsumingServiceIndex(getSingleValue(map,
                    SAMLSSOConstants.Metadata.ATTRIBUTE_CONSUMING_SERVICE_INDEX));
        } else {
            // Specific DB's (like oracle) returns empty strings as null.
            serviceProviderDTO.setAttributeConsumingServiceIndex("");
        }

        if (getSingleValue(map, SAMLSSOConstants.Metadata.REQUESTED_CLAIMS) != null) {
            serviceProviderDTO.setRequestedClaims(getMultiValues(map, SAMLSSOConstants.Metadata.REQUESTED_CLAIMS));
        }

        if (getSingleValue(map, SAMLSSOConstants.Metadata.REQUESTED_AUDIENCES) != null) {
            serviceProviderDTO.setRequestedAudiences(getMultiValues(map,
                    SAMLSSOConstants.Metadata.REQUESTED_AUDIENCES));
        }

        if (getSingleValue(map, SAMLSSOConstants.Metadata.REQUESTED_RECIPIENTS) != null) {
            serviceProviderDTO.setRequestedRecipients(getMultiValues(map,
                    SAMLSSOConstants.Metadata.REQUESTED_RECIPIENTS));
        }

        if (getSingleValue(map, SAMLSSOConstants.Metadata.ENABLE_ATTRIBUTES_BY_DEFAULT) != null) {
            String enableAttrByDefault = getSingleValue(map,
                    SAMLSSOConstants.Metadata.ENABLE_ATTRIBUTES_BY_DEFAULT);
            serviceProviderDTO.setEnableAttributesByDefault(Boolean.parseBoolean(enableAttrByDefault));
        }
        if (getSingleValue(map, SAMLSSOConstants.Metadata.IDP_INIT_SSO_ENABLED) != null) {
            serviceProviderDTO.setIdPInitSSOEnabled(Boolean.parseBoolean(getSingleValue(map,
                    SAMLSSOConstants.Metadata.IDP_INIT_SSO_ENABLED).trim()));
        }
        if (getSingleValue(map, SAMLSSOConstants.Metadata.IDP_INIT_SLO_ENABLED) != null) {
            serviceProviderDTO.setIdPInitSLOEnabled(Boolean.parseBoolean(getSingleValue(map,
                    SAMLSSOConstants.Metadata.IDP_INIT_SLO_ENABLED).trim()));
            if (serviceProviderDTO.isIdPInitSLOEnabled() && getSingleValue(map,
                    SAMLSSOConstants.Metadata.IDP_INIT_SLO_RETURN_URLS) != null) {
                serviceProviderDTO.setIdpInitSLOReturnToURLs(getMultiValues(map,
                        SAMLSSOConstants.Metadata.IDP_INIT_SLO_RETURN_URLS));
            }
        }
        if (getSingleValue(map, SAMLSSOConstants.Metadata.ENABLE_ENCRYPTED_ASSERTION) != null) {
            serviceProviderDTO.setDoEnableEncryptedAssertion(Boolean.parseBoolean(getSingleValue(map,
                    SAMLSSOConstants.Metadata.ENABLE_ENCRYPTED_ASSERTION).trim()));
        }
        if (getSingleValue(map, SAMLSSOConstants.Metadata.VALIDATE_SIGNATURE_IN_REQUESTS) != null) {
            serviceProviderDTO.setDoValidateSignatureInRequests(Boolean.parseBoolean(getSingleValue(map,
                    SAMLSSOConstants.Metadata.VALIDATE_SIGNATURE_IN_REQUESTS).trim()));
        }
        if (getSingleValue(map, SAMLSSOConstants.Metadata.VALIDATE_SIGNATURE_IN_ARTIFACT_RESOLVE) != null) {
            serviceProviderDTO.setDoValidateSignatureInArtifactResolve(Boolean.parseBoolean(getSingleValue(map,
                    SAMLSSOConstants.Metadata.VALIDATE_SIGNATURE_IN_ARTIFACT_RESOLVE).trim()));
        }
        if (getSingleValue(map, SAMLSSOConstants.Metadata.ISSUER_QUALIFIER) != null) {
            serviceProviderDTO.setIssuerQualifier(getSingleValue(map,
                    SAMLSSOConstants.Metadata.ISSUER_QUALIFIER));
        }
        if (getSingleValue(map, SAMLSSOConstants.Metadata.IDP_ENTITY_ID_ALIAS) != null) {
            serviceProviderDTO.setIdpEntityIDAlias(getSingleValue(map, SAMLSSOConstants.Metadata
                    .IDP_ENTITY_ID_ALIAS));
        }
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
        //no need for remove SAML SP inbound configuration separately from the application.Because now SAML metadata
        // will get stored only with the application
    }

    private static SAMLSSOServiceProviderDTO createSAMLSpWithManualConfiguration(SAML2ServiceProvider saml2SpModel) {

        SAMLSSOServiceProviderDTO serviceProviderDTO = new ApiModelToSAMLSSOServiceProvider().apply(saml2SpModel);
        if (serviceProviderDTO.getNameIDFormat() == null) {
            serviceProviderDTO.setNameIDFormat(NameIdentifier.UNSPECIFIED);
        } else {
            serviceProviderDTO.setNameIDFormat(serviceProviderDTO.getNameIDFormat().replace("/", ":"));
        }
        if (serviceProviderDTO.isEnableAttributeProfile()) {
            String attributeConsumingIndex = serviceProviderDTO.getAttributeConsumingServiceIndex();
            if (StringUtils.isEmpty(attributeConsumingIndex)) {
                try {
                    serviceProviderDTO.setAttributeConsumingServiceIndex(
                            Integer.toString(IdentityUtil.getRandomInteger()));
                } catch (IdentityException e) {
                    handleException(e);
                }
            }
        } else {
            serviceProviderDTO.setAttributeConsumingServiceIndex("");
            if (serviceProviderDTO.isEnableAttributesByDefault()) {
                logger.warn("Enable Attribute Profile must be selected to activate it by default. " +
                        "EnableAttributesByDefault will be disabled.");
            }
            serviceProviderDTO.setEnableAttributesByDefault(false);
        }

        return serviceProviderDTO;
    }

    private static void addSAMLInboundProperties(List<Property> propertyList,
                                                 SAMLSSOServiceProviderDTO serviceProviderDTO) {
        addKeyValuePair(SAMLSSOConstants.Metadata.ISSUER, serviceProviderDTO.getIssuer(), propertyList);
        for (String url : serviceProviderDTO.getAssertionConsumerUrls()) {
            addKeyValuePair(SAMLSSOConstants.Metadata.ASSERTION_CONSUMER_URLS, url, propertyList);
        }
        addKeyValuePair(SAMLSSOConstants.Metadata.DEFAULT_ASSERTION_CONSUMER_URL,
                serviceProviderDTO.getDefaultAssertionConsumerUrl(), propertyList);
        addKeyValuePair(SAMLSSOConstants.Metadata.ISSUER_CERT_ALIAS, serviceProviderDTO.getCertAlias(), propertyList);
        addKeyValuePair(SAMLSSOConstants.Metadata.LOGIN_PAGE_URL, serviceProviderDTO.getLoginPageURL(), propertyList);
        addKeyValuePair(SAMLSSOConstants.Metadata.NAME_ID_FORMAT, serviceProviderDTO.getNameIDFormat(), propertyList);
        addKeyValuePair(SAMLSSOConstants.Metadata.SIGNING_ALGORITHM, serviceProviderDTO.getSigningAlgorithmURI(),
                propertyList);
        addKeyValuePair(SAMLSSOConstants.Metadata.DIGEST_ALGORITHM, serviceProviderDTO.getDigestAlgorithmURI(),
                propertyList);
        addKeyValuePair(SAMLSSOConstants.Metadata.ASSERTION_ENCRYPTION_ALGORITHM, serviceProviderDTO
                .getAssertionEncryptionAlgorithmURI(), propertyList);
        addKeyValuePair(SAMLSSOConstants.Metadata.KEY_ENCRYPTION_ALGORITHM, serviceProviderDTO
                .getKeyEncryptionAlgorithmURI(), propertyList);
        if (serviceProviderDTO.getNameIdClaimUri() != null
                && serviceProviderDTO.getNameIdClaimUri().trim().length() > 0) {
            addKeyValuePair(SAMLSSOConstants.Metadata.ENABLE_NAME_ID_CLAIM_URI, "true", propertyList);
            addKeyValuePair(SAMLSSOConstants.Metadata.NAME_ID_CLAIM_URI, serviceProviderDTO.getNameIdClaimUri(),
                    propertyList);
        } else {
            addKeyValuePair(SAMLSSOConstants.Metadata.ENABLE_NAME_ID_CLAIM_URI, "false", propertyList);
        }

        String doSingleLogout = String.valueOf(serviceProviderDTO.isDoSingleLogout());
        addKeyValuePair(SAMLSSOConstants.Metadata.DO_SINGLE_LOGOUT, doSingleLogout, propertyList);
        if (serviceProviderDTO.isDoSingleLogout()) {
            if (StringUtils.isNotBlank(serviceProviderDTO.getSloResponseURL())) {
                addKeyValuePair(SAMLSSOConstants.Metadata.SLO_RESPONSE_URL, serviceProviderDTO.getSloResponseURL(),
                        propertyList);
            }
            if (StringUtils.isNotBlank(serviceProviderDTO.getSloRequestURL())) {
                addKeyValuePair(SAMLSSOConstants.Metadata.SLO_REQUEST_URL, serviceProviderDTO.getSloRequestURL(),
                        propertyList);
            }
            // Create doFrontChannelLogout property in the registry.
            String doFrontChannelLogout = String.valueOf(serviceProviderDTO.isDoFrontChannelLogout());
            addKeyValuePair(SAMLSSOConstants.Metadata.DO_FRONT_CHANNEL_LOGOUT, doFrontChannelLogout,
                    propertyList);
            if (serviceProviderDTO.isDoFrontChannelLogout()) {
                // Create frontChannelLogoutMethod property in the registry.
                addKeyValuePair(SAMLSSOConstants.Metadata.FRONT_CHANNEL_LOGOUT_BINDING,
                        serviceProviderDTO.getFrontChannelLogoutBinding(), propertyList);
            }
        }

        String doSignResponse = String.valueOf(serviceProviderDTO.isDoSignResponse());
        addKeyValuePair(SAMLSSOConstants.Metadata.DO_SIGN_RESPONSE, doSignResponse, propertyList);
        String isAssertionQueryRequestProfileEnabled = String.valueOf(serviceProviderDTO
                .isAssertionQueryRequestProfileEnabled());
        addKeyValuePair(SAMLSSOConstants.Metadata.ASSERTION_QUERY_REQUEST_PROFILE_ENABLED,
                isAssertionQueryRequestProfileEnabled, propertyList);
        String supportedAssertionQueryRequestTypes = serviceProviderDTO.getSupportedAssertionQueryRequestTypes();
        addKeyValuePair(SAMLSSOConstants.Metadata.SUPPORTED_ASSERTION_QUERY_REQUEST_TYPES,
                supportedAssertionQueryRequestTypes, propertyList);
        String isEnableSAML2ArtifactBinding = String.valueOf(serviceProviderDTO.isEnableSAML2ArtifactBinding());
        addKeyValuePair(SAMLSSOConstants.Metadata.ENABLE_SAML2_ARTIFACT_BINDING, isEnableSAML2ArtifactBinding,
                propertyList);

        String doSignAssertions = String.valueOf(serviceProviderDTO.isDoSignAssertions());
        addKeyValuePair(SAMLSSOConstants.Metadata.DO_SIGN_ASSERTIONS, doSignAssertions, propertyList);

        String isSamlECP = String.valueOf(serviceProviderDTO.isSamlECP());
        addKeyValuePair(SAMLSSOConstants.Metadata.ENABLE_ECP, isSamlECP, propertyList);
        if (CollectionUtils.isNotEmpty(Arrays.asList(serviceProviderDTO.getRequestedClaims()))) {
            for (String requestedClaim : serviceProviderDTO.getRequestedClaims()) {
                addKeyValuePair(SAMLSSOConstants.Metadata.REQUESTED_CLAIMS, requestedClaim, propertyList);
            }
        }

        addKeyValuePair(SAMLSSOConstants.Metadata.ATTRIBUTE_CONSUMING_SERVICE_INDEX,
                serviceProviderDTO.getAttributeConsumingServiceIndex(), propertyList);

        if (CollectionUtils.isNotEmpty(Arrays.asList(serviceProviderDTO.getRequestedAudiences()))) {
            for (String requestedAudience : serviceProviderDTO.getRequestedAudiences()) {
                addKeyValuePair(SAMLSSOConstants.Metadata.REQUESTED_AUDIENCES, requestedAudience, propertyList);
            }
        }
        if (CollectionUtils.isNotEmpty(Arrays.asList(serviceProviderDTO.getRequestedRecipients()))) {
            for (String requestedRecipient : serviceProviderDTO.getRequestedRecipients()) {
                addKeyValuePair(SAMLSSOConstants.Metadata.REQUESTED_RECIPIENTS, requestedRecipient, propertyList);
            }
        }

        String enableAttributesByDefault = String.valueOf(serviceProviderDTO.isEnableAttributesByDefault());
        addKeyValuePair(SAMLSSOConstants.Metadata.ENABLE_ATTRIBUTES_BY_DEFAULT, enableAttributesByDefault,
                propertyList);

        String idPInitSSOEnabled = String.valueOf(serviceProviderDTO.isIdPInitSSOEnabled());
        addKeyValuePair(SAMLSSOConstants.Metadata.IDP_INIT_SSO_ENABLED, idPInitSSOEnabled, propertyList);

        String idPInitSLOEnabled = String.valueOf(serviceProviderDTO.isIdPInitSLOEnabled());
        addKeyValuePair(SAMLSSOConstants.Metadata.IDP_INIT_SLO_ENABLED, idPInitSLOEnabled, propertyList);

        if (serviceProviderDTO.isIdPInitSLOEnabled() && serviceProviderDTO.getIdpInitSLOReturnToURLs().length > 0) {
            for (String sloReturnUrl : serviceProviderDTO.getIdpInitSLOReturnToURLs()) {
                addKeyValuePair(SAMLSSOConstants.Metadata.IDP_INIT_SLO_RETURN_URLS, sloReturnUrl, propertyList);
            }
        }
        String enableEncryptedAssertion = String.valueOf(serviceProviderDTO.isDoEnableEncryptedAssertion());
        addKeyValuePair(SAMLSSOConstants.Metadata.ENABLE_ENCRYPTED_ASSERTION, enableEncryptedAssertion, propertyList);

        String validateSignatureInRequests = String.valueOf(serviceProviderDTO.isDoValidateSignatureInRequests());
        addKeyValuePair(SAMLSSOConstants.Metadata.VALIDATE_SIGNATURE_IN_REQUESTS, validateSignatureInRequests,
                propertyList);

        String validateSignatureInArtifactResolve =
                String.valueOf(serviceProviderDTO.isDoValidateSignatureInArtifactResolve());
        addKeyValuePair(SAMLSSOConstants.Metadata.VALIDATE_SIGNATURE_IN_ARTIFACT_RESOLVE,
                validateSignatureInArtifactResolve, propertyList);
        if (StringUtils.isNotBlank(serviceProviderDTO.getIssuerQualifier())) {
            addKeyValuePair(SAMLSSOConstants.Metadata.ISSUER_QUALIFIER, serviceProviderDTO.getIssuerQualifier(),
                    propertyList);
        }
        if (StringUtils.isNotBlank(serviceProviderDTO.getIdpEntityIDAlias())) {
            addKeyValuePair(SAMLSSOConstants.Metadata.IDP_ENTITY_ID_ALIAS, serviceProviderDTO.getIdpEntityIDAlias(),
                    propertyList);
        }
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


}
