/*
 * Copyright (c) 2019, WSO2 LLC. (http://www.wso2.com).
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.sso.saml.SAMLSSOConfigServiceImpl;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.identity.sso.saml.exception.IdentitySAML2ClientException;
import org.wso2.carbon.identity.sso.saml.exception.IdentitySAML2SSOException;
import org.wso2.carbon.identity.sso.saml.util.SAMLSSOUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.wso2.carbon.identity.sso.saml.Error.URL_NOT_FOUND;

/**
 * Helper functions for SAML inbound management.
 */
public class SAMLInboundFunctions {

    private static final String ATTRIBUTE_CONSUMING_SERVICE_INDEX = "attrConsumServiceIndex";
    private static final Log logger = LogFactory.getLog(SAMLInboundFunctions.class);

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
                oldSAMLSp = getSamlSsoConfigService().getServiceProvider(currentIssuer);
            }
        } catch (IdentityException e) {
            throw handleException(e);
        }

        try {
            if (currentIssuer != null) {
                // This is an update.
                return updateSAMLInbound(application, saml2Configuration);
            }
            return createSAMLInbound(application, saml2Configuration);
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

    @Deprecated
    /* @Deprecated. Please use {@link #createSAMLInbound(ServiceProvider, SAML2Configuration)} instead. */
    public static InboundAuthenticationRequestConfig createSAMLInbound(SAML2Configuration saml2Configuration) {

        SAMLSSOServiceProviderDTO samlssoServiceProviderDTO = getSamlSsoServiceProviderDTO(saml2Configuration, false);

        return returnSAMLInbound(samlssoServiceProviderDTO);
    }

    public static InboundAuthenticationRequestConfig createSAMLInbound(
            ServiceProvider serviceProvider, SAML2Configuration saml2Configuration) {

        SAMLSSOServiceProviderDTO samlssoServiceProviderDTO = getSamlSsoServiceProviderDTO(saml2Configuration, false);

        // Set certificate if available.
        if (samlssoServiceProviderDTO.getCertificateContent() != null) {
            serviceProvider.setCertificateContent(base64Encode(samlssoServiceProviderDTO.getCertificateContent()));
        }

        return returnSAMLInbound(samlssoServiceProviderDTO);
    }

    public static InboundAuthenticationRequestConfig updateSAMLInbound(
            ServiceProvider serviceProvider, SAML2Configuration saml2Configuration) {

        SAMLSSOServiceProviderDTO samlssoServiceProviderDTO = getSamlSsoServiceProviderDTO(saml2Configuration, true);

        // Set certificate if available.
        if (samlssoServiceProviderDTO.getCertificateContent() != null) {
            serviceProvider.setCertificateContent(base64Encode(samlssoServiceProviderDTO.getCertificateContent()));
        }

        return returnSAMLInbound(samlssoServiceProviderDTO);
    }

    private static InboundAuthenticationRequestConfig returnSAMLInbound(
            SAMLSSOServiceProviderDTO samlssoServiceProviderDTO) {

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

    private static SAMLSSOServiceProviderDTO createSAMLSpWithManualConfiguration(SAML2ServiceProvider saml2SpModel,
                                                                                 boolean isUpdate) {

        SAMLSSOServiceProviderDTO serviceProviderDTO = new ApiModelToSAMLSSOServiceProvider().apply(saml2SpModel);
        try {
            if (isUpdate) {
                return getSamlSsoConfigService().updateServiceProvider(serviceProviderDTO);
            }
            return getSamlSsoConfigService().createServiceProvider(serviceProviderDTO);
        } catch (IdentityException e) {
            throw handleException(e);
        }
    }

    private static SAMLSSOServiceProviderDTO createSAMLSpWithMetadataFile(String encodedMetaFileContent,
                                                                         boolean isUpdate) {

        try {
            byte[] metaData = Base64.getDecoder().decode(encodedMetaFileContent.getBytes(StandardCharsets.UTF_8));
            String base64DecodedMetadata = new String(metaData, StandardCharsets.UTF_8);

            if (isUpdate) {
                return getSamlSsoConfigService().updateRPServiceProvider(base64DecodedMetadata);
            }
            return getSamlSsoConfigService().uploadRPServiceProvider(base64DecodedMetadata);
        } catch (IdentitySAML2SSOException e) {
            throw handleException(e);
        }
    }

    private static SAMLSSOServiceProviderDTO createSAMLSpWithMetadataUrl(String metadataUrl, boolean isUpdate) {

        try {
            if (isUpdate) {
                return getSamlSsoConfigService().updateServiceProviderWithMetadataURL(metadataUrl);
            }
            return getSamlSsoConfigService().createServiceProviderWithMetadataURL(metadataUrl);
        } catch (IdentitySAML2SSOException e) {
            throw handleException(e);
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

    private static String base64Encode(String content) {

        return new String(Base64.getEncoder().encode(content.getBytes(StandardCharsets.UTF_8)),
                (StandardCharsets.UTF_8));
    }

    private static SAMLSSOServiceProviderDTO getSamlSsoServiceProviderDTO(SAML2Configuration saml2Configuration,
                                                                          boolean isUpdate) throws APIError {

        SAML2ServiceProvider samlManualConfiguration = saml2Configuration.getManualConfiguration();

        if (saml2Configuration.getMetadataFile() != null) {
            return createSAMLSpWithMetadataFile(saml2Configuration.getMetadataFile(), isUpdate);
        } else if (saml2Configuration.getMetadataURL() != null) {
            return createSAMLSpWithMetadataUrl(saml2Configuration.getMetadataURL(), isUpdate);
        } else if (samlManualConfiguration != null) {
            return createSAMLSpWithManualConfiguration(samlManualConfiguration, isUpdate);
        } else {
            throw Utils.buildBadRequestError("Invalid SAML2 Configuration. One of metadataFile, metaDataUrl or " +
                    "serviceProvider manual configuration needs to be present.");
        }
    }

}
