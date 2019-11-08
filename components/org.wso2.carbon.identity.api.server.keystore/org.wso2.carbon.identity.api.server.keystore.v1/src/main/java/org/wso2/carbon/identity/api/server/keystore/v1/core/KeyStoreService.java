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

package org.wso2.carbon.identity.api.server.keystore.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.keystore.common.KeyStoreConstants;
import org.wso2.carbon.identity.api.server.keystore.v1.model.CertificatesResponse;
import org.wso2.carbon.security.keystore.KeyStoreManagementException;
import org.wso2.carbon.security.keystore.KeyStoreManagementServerException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForHeader;
import static org.wso2.carbon.identity.api.server.keystore.common.KeyStoreConstants.CERTIFICATE_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.keystore.common.KeyStoreConstants.CLIENT_CERTIFICATE_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.keystore.common.KeyStoreConstants.ErrorMessage.ERROR_CODE_ENCODE_CERTIFICATE;
import static org.wso2.carbon.identity.api.server.keystore.common.KeyStoreConstants.ErrorMessage.ERROR_CODE_FILE_WRITE;
import static org.wso2.carbon.identity.api.server.keystore.common.KeyStoreConstants.ErrorMessage.ERROR_CODE_INVALID_ALIAS;
import static org.wso2.carbon.identity.api.server.keystore.common.KeyStoreConstants.KEYSTORES_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.keystore.common.KeyStoreManagamentDataHolder.getKeyStoreManager;

/**
 * Keystore service APIs are processed in this class.
 */
public class KeyStoreService {

    private static final Log LOG = LogFactory.getLog(KeyStoreService.class);

    /**
     * Retrieves the list of certificates from the keystore.
     *
     * @param filter used to filter the result.
     * @return {@link List} of {@link CertificatesResponse}
     */
    public List<CertificatesResponse> listCertificateAliases(String filter) {

        List<String> aliasList;
        try {
            aliasList = getKeyStoreManager().getKeyStoreCertificateAliases(filter);
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Server encountered an error while retrieving the list of certificates " +
                    "from keystore.");
        }
        return generateCertificateResponseList(aliasList, false);
    }

    /**
     * Retrieves the certificate of the given alias from the keystore.
     *
     * @param alias      of the certificate.
     * @param encodeCert boolean value to decide whether the needs to be encoded or not.
     * @return a {@link File} with the certificate.
     */
    public File getCertificate(String alias, boolean encodeCert) {

        X509Certificate certificate;
        try {
            certificate = getKeyStoreManager().getKeyStoreCertificate(alias);
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Server encountered an error while retrieving the certificate with alias: "
                    + alias + " from keystore");
        }

        if (certificate == null) {
            throw handleException(ERROR_CODE_INVALID_ALIAS, "Couldn't find a certificate with alias: " + alias +
                    " from the keystore.", Response.Status.BAD_REQUEST);
        }
        return generateCertificateFile(alias, certificate, encodeCert);
    }

    /**
     * Imports the certificate to the keystore.
     *
     * @param alias       of the certificate.
     * @param certificate the certificate to be imported.
     */
    public URI uploadCertificate(String alias, String certificate) {

        try {
            getKeyStoreManager().addCertificate(alias, certificate);
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Server encountered an error while importing the certificate with alias: "
                    + alias + " to the keystore.");
        }
        String certificateEndPoint = String.format(V1_API_PATH_COMPONENT + KEYSTORES_API_PATH_COMPONENT +
                CERTIFICATE_PATH_COMPONENT, alias);
        return buildURIForHeader(certificateEndPoint);
    }

    /**
     * Deletes the certificate from the keystore.
     *
     * @param alias of the certificate.
     */
    public void deleteCertificate(String alias) {

        try {
            getKeyStoreManager().deleteCertificate(alias);
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Server encountered an error while removing the certificate with alias: "
                    + alias + " from the keystore.");
        }
    }

    /**
     * Retrieves the list of certificate aliases from the client truststore.
     *
     * @param filter used to filter the result.
     * @return {@link List} of {@link CertificatesResponse}
     */
    public List<CertificatesResponse> listClientCertificateAliases(String filter) {

        List<String> aliasList;
        try {
            aliasList = getKeyStoreManager().getClientCertificateAliases(filter);
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Server encountered an error while retrieving the list of certificates " +
                    "from client truststore.");
        }
        return generateCertificateResponseList(aliasList, true);
    }

    /**
     * Retrieves the certificate of the given alias from the client truststore.
     *
     * @param alias      of the certificate.
     * @param encodeCert boolean value to decide whether the needs to be encoded or not.
     * @return a {@link File} with the certificate.
     */
    public File getClientCertificate(String alias, boolean encodeCert) {

        X509Certificate certificate;
        try {
            certificate = getKeyStoreManager().getClientCertificate(alias);
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Server encountered an error while retrieving the certificate with alias: "
                    + alias + " from client truststore.");
        }

        if (certificate == null) {
            throw handleException(ERROR_CODE_INVALID_ALIAS, "Couldn't find a certificate with alias: " + alias +
                    " from the keystore.", Response.Status.BAD_REQUEST);
        }
        return generateCertificateFile(alias, certificate, encodeCert);
    }

    /**
     * Retrieves the public certificate from the keystore.
     *
     * @param encodeCert boolean value to decide whether the needs to be encoded or not.
     * @return a {@link File} with the certificate.
     */
    public File getPublicCertificate(boolean encodeCert) {

        Map<String, X509Certificate> certificateData;
        try {
            certificateData = getKeyStoreManager().getPublicCertificate();
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Server encountered an error while retrieving the public certificate from " +
                    "from keystore.");
        }

        String alias = null;
        X509Certificate certificate = null;
        Set<String> keyset = certificateData.keySet();
        for (String key : keyset) {
            alias = key;
            certificate = certificateData.get(alias);
        }
        return generateCertificateFile(alias, certificate, encodeCert);
    }

    private List<CertificatesResponse> generateCertificateResponseList(List<String> aliasList, boolean isClientCert) {

        List<CertificatesResponse> certificatesResponses = new ArrayList<>();
        String componentPath;
        if (!isClientCert) {
            componentPath = CERTIFICATE_PATH_COMPONENT;
        } else {
            componentPath = CLIENT_CERTIFICATE_PATH_COMPONENT;
        }

        for (String alias : aliasList) {
            CertificatesResponse certificatesResponse = new CertificatesResponse();
            certificatesResponse.setAlias(alias);
            String certificateEndPoint =
                    String.format(V1_API_PATH_COMPONENT + KEYSTORES_API_PATH_COMPONENT +
                            componentPath, alias);
            certificatesResponse.setCertificate(buildURIForHeader(certificateEndPoint).toString());
            certificatesResponses.add(certificatesResponse);
        }
        return certificatesResponses;
    }

    private File generateCertificateFile(String alias, X509Certificate certificate, boolean encodeCert) {

        String certificateContent;
        if (encodeCert) {
            try {
                certificateContent = Base64.getEncoder().encodeToString(certificate.getEncoded());
            } catch (CertificateEncodingException e) {
                throw handleException(ERROR_CODE_ENCODE_CERTIFICATE, e, Response.Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            certificateContent = certificate.toString();
        }

        String filename = alias + ".cer";
        File file = new File(filename);
        try {
            if (file.createNewFile()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("A file has been created with name: " + filename);
                }
            }
            try (Writer fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                fileWriter.write(certificateContent);
            }
        } catch (IOException e) {
            throw handleException(ERROR_CODE_FILE_WRITE, e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            file.deleteOnExit();
        }
        return file;
    }

    private APIError handleException(KeyStoreManagementException e, String description) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder().withCode(e.getErrorCode())
                .withMessage(e.getMessage()).withDescription(description);
        ErrorResponse errorResponse = builder.build(LOG, e, description);
        Response.Status status;
        if (e instanceof KeyStoreManagementServerException) {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            status = Response.Status.BAD_REQUEST;
        }
        return new APIError(status, errorResponse);
    }

    private APIError handleException(KeyStoreConstants.ErrorMessage errorMessage, Exception e, Response.Status status) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder().withCode(errorMessage.getCode())
                .withMessage(errorMessage.getMessage()).withDescription(e.getMessage());
        ErrorResponse errorResponse = builder.build(LOG, e, e.getMessage());
        return new APIError(status, errorResponse);
    }

    private APIError handleException(KeyStoreConstants.ErrorMessage errorMessage, String description,
                                     Response.Status status) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder().withCode(errorMessage.getCode())
                .withMessage(errorMessage.getMessage()).withDescription(description);
        ErrorResponse errorResponse = builder.build();
        return new APIError(status, errorResponse);
    }
}
