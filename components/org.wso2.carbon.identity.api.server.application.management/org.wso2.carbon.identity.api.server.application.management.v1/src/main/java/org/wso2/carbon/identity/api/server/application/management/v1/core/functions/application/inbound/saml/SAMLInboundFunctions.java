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
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.sso.saml.SAMLSSOConfigServiceImpl;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.identity.sso.saml.exception.IdentitySAML2ClientException;
import org.wso2.carbon.identity.sso.saml.exception.IdentitySAML2SSOException;
import org.wso2.carbon.identity.sso.saml.util.SAMLSSOUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * Helper functions for SAML inbound management.
 */
public class SAMLInboundFunctions {

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

        String issuer;
        if (saml2Configuration.getMetadataFile() != null) {
            issuer = createSAMLSpWithMetadataFile(saml2Configuration.getMetadataFile());
        } else if (saml2Configuration.getMetadataURL() != null) {
            issuer = createSAMLSpWithMetadataUrl(saml2Configuration.getMetadataURL());
        } else if (samlManualConfiguration != null) {
            issuer = createSAMLSpWithManualConfiguration(samlManualConfiguration);
        } else {
            throw Utils.buildBadRequestError("Invalid SAML2 Configuration. One of metadataFile, metaDataUrl or " +
                    "serviceProvider manual configuration needs to be present.");
        }

        InboundAuthenticationRequestConfig samlInbound = new InboundAuthenticationRequestConfig();
        samlInbound.setInboundAuthType(FrameworkConstants.StandardInboundProtocols.SAML2);
        samlInbound.setInboundAuthKey(issuer);
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

    private static String createSAMLSpWithManualConfiguration(SAML2ServiceProvider saml2SpModel) {

        SAMLSSOServiceProviderDTO serviceProviderDTO = new ApiModelToSAMLSSOServiceProvider().apply(saml2SpModel);
        try {
            SAMLSSOServiceProviderDTO addedSAMLSp = getSamlSsoConfigService().createServiceProvider(serviceProviderDTO);
            return addedSAMLSp.getIssuer();
        } catch (IdentityException e) {
            throw handleException(e);
        }
    }

    private static String createSAMLSpWithMetadataFile(String encodedMetaFileContent) {

        try {
            byte[] metaData = Base64.getDecoder().decode(encodedMetaFileContent.getBytes(StandardCharsets.UTF_8));
            String base64DecodedMetadata = new String(metaData, StandardCharsets.UTF_8);

            return createSAMLSpWithMetadataContent(base64DecodedMetadata);
        } catch (IdentitySAML2SSOException e) {
            throw handleException(e);
        }
    }

    private static String createSAMLSpWithMetadataContent(String metadataContent) throws IdentitySAML2SSOException {

        SAMLSSOServiceProviderDTO serviceProviderDTO =
                getSamlSsoConfigService().uploadRPServiceProvider(metadataContent);
        return serviceProviderDTO.getIssuer();
    }

    private static String createSAMLSpWithMetadataUrl(String metadataUrl) {

        try {
            SAMLSSOServiceProviderDTO serviceProviderDTO =
                    getSamlSsoConfigService().createServiceProviderWithMetadataURL(metadataUrl);
            return serviceProviderDTO.getIssuer();
        } catch (IdentitySAML2SSOException e) {
            throw handleException(e);
        }
    }

    private static APIError handleException(IdentityException e) {

        String msg = "Error while creating/updating SAML inbound of application.";
        if (e instanceof IdentitySAML2ClientException) {
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

    private static APIError buildServerError(String message, IdentityException e) {

        String errorCode = e.getErrorCode();
        String errorDescription = e.getMessage();

        return Utils.buildServerError(errorCode, message, errorDescription, e);
    }

    private static SAMLSSOConfigServiceImpl getSamlSsoConfigService() {

        return ApplicationManagementServiceHolder.getSamlssoConfigService();
    }

}
