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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.saml.ApiModelToSAMLSSOServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.saml.SAMLSSOServiceProviderToAPIModel;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.identity.sso.saml.exception.IdentitySAML2SSOException;
import org.wso2.carbon.identity.sso.saml.util.SAMLSSOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder.getSamlssoConfigService;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildApiError;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildServerErrorResponse;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundUtils.updateOrInsertInbound;

/**
 * Helper functions for SAML inbound management.
 */
public class SAMLInboundUtils {

    private static final int CONNECTION_TIMEOUT_IN_SECONDS = 5;
    private static final int READ_TIMEOUT_IN_SECONDS = 10;

    public static SAML2ServiceProvider putSAMLInbound(ServiceProvider application,
                                                      SAML2Configuration saml2Configuration) {

        // First we identify whether this is a insert or update.
        String currentIssuer = InboundUtils.getInboundAuthKey(application, StandardInboundProtocols.SAML2);
        try {
            if (currentIssuer != null) {
                // Delete the current app.
                getSamlssoConfigService().removeServiceProvider(currentIssuer);
            }

            String createdIssuer = createSAMLInbound(saml2Configuration);
            // Update the inbound details.
            InboundAuthenticationRequestConfig samlInbound = new InboundAuthenticationRequestConfig();
            samlInbound.setInboundAuthType(StandardInboundProtocols.SAML2);
            samlInbound.setInboundAuthKey(createdIssuer);
            updateOrInsertInbound(application, StandardInboundProtocols.SAML2, samlInbound);

            SAMLSSOServiceProviderDTO serviceProvider = getSamlssoConfigService().getServiceProvider(createdIssuer);
            return new SAMLSSOServiceProviderToAPIModel().apply(serviceProvider);
        } catch (IdentityException e) {
            String applicationId = application.getApplicationResourceId();
            throw buildServerErrorResponse(e,
                    "Error while creating/updating SAML inbound of application: " + applicationId);
        }
    }

    public static String createSAMLInbound(SAML2Configuration saml2Configuration) {

        SAML2ServiceProvider samlManualConfiguration = saml2Configuration.getManualConfiguration();

        String issuer;
        if (saml2Configuration.getMetadataFile() != null) {
            issuer = createSAMLSpWithMetadataFile(saml2Configuration.getMetadataFile());
        } else if (saml2Configuration.getMetadataURL() != null) {
            issuer = createSAMLSpWithMetadataUrl(saml2Configuration.getMetadataURL());
        } else if (samlManualConfiguration != null) {
            issuer = createSAMLSpWithManualConfiguration(samlManualConfiguration);
        } else {
            // TODO error code.
            throw Utils.buildClientError("Invalid SAML2 Configuration. One of metadataFile, metaDataUrl or " +
                    "serviceProvider manual configuration needs to be present.");
        }

        return issuer;
    }

    private static String createSAMLSpWithManualConfiguration(SAML2ServiceProvider saml2SpModel) {

        SAMLSSOServiceProviderDTO samlSp = new ApiModelToSAMLSSOServiceProvider().apply(saml2SpModel);
        String issuerWithQualifier = getIssuerWithQualifier(samlSp);
        try {
            boolean success = getSamlssoConfigService().addRPServiceProvider(samlSp);
            if (success) {
                return issuerWithQualifier;
            } else {
                String msg = "Error adding SAML configuration. SAML service provider with issuer: %s and " +
                        "qualifier: %s already exists.";
                throw buildApiError(Response.Status.CONFLICT,
                        String.format(msg, samlSp.getIssuer(), samlSp.getIssuerQualifier()));
            }
        } catch (IdentityException e) {
            throw Utils.buildServerErrorResponse(e, "Error adding SAML config with issuer: " +
                    saml2SpModel.getIssuer() + ". " + e.getMessage());
        }
    }

    private static String createSAMLSpWithMetadataFile(String encodedMetaFileContent) {

        try {
            byte[] metaData = Base64.getDecoder().decode(encodedMetaFileContent.getBytes(StandardCharsets.UTF_8));
            String base64DecodedMetadata = new String(metaData, StandardCharsets.UTF_8);

            return createSAMLSpWithMetadataContent(base64DecodedMetadata);
        } catch (IdentitySAML2SSOException e) {
            throw Utils.buildServerErrorResponse(e, "Error adding SAML configuration using metadata file. " +
                    e.getMessage());
        }
    }

    private static String createSAMLSpWithMetadataContent(String metadataContent) throws IdentitySAML2SSOException {

        SAMLSSOServiceProviderDTO serviceProviderDTO =
                getSamlssoConfigService().uploadRPServiceProvider(metadataContent);
        return serviceProviderDTO.getIssuer();
    }

    private static String createSAMLSpWithMetadataUrl(String metadataUrl) {

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

    private static String getMetaContentFromUrl(String metadataUrl) {
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

    private static String getIssuerWithQualifier(SAMLSSOServiceProviderDTO samlSp) {

        if (StringUtils.isNotBlank(samlSp.getIssuerQualifier())) {
            return SAMLSSOUtil.getIssuerWithQualifier(samlSp.getIssuer(), samlSp.getIssuerQualifier());
        } else {
            return samlSp.getIssuer();
        }
    }
}
