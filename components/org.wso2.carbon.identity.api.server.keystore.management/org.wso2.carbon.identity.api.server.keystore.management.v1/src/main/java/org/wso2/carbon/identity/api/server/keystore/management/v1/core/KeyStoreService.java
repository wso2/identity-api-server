/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.keystore.management.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants;
import org.wso2.carbon.identity.api.server.keystore.management.v1.model.CertificateResponse;
import org.wso2.carbon.security.keystore.KeyStoreManagementException;
import org.wso2.carbon.security.keystore.KeyStoreManagementServerException;
import org.wso2.carbon.security.keystore.KeyStoreManagementService;

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
import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants.CERTIFICATE_FILE_EXTENSION;
import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants.CERTIFICATE_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants.CERTIFICATE_TEMPORARY_DIRECTORY_PATH;
import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants.CLIENT_CERTIFICATE_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants.ErrorMessage.ERROR_CODE_ENCODE_CERTIFICATE;
import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants.ErrorMessage.ERROR_CODE_FILE_WRITE;
import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants.ErrorMessage.ERROR_CODE_INVALID_ALIAS;
import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants.KEYSTORES_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants.PATH_SEPERATOR;

/**
 * Keystore service APIs are processed in this class.
 */
public class KeyStoreService {

    private final KeyStoreManagementService keyStoreManagementService;
    private static final Log LOG = LogFactory.getLog(KeyStoreService.class);

    public KeyStoreService(KeyStoreManagementService keyStoreManagementService) {

        this.keyStoreManagementService = keyStoreManagementService;
    }

    /**
     * Retrieves the list of certificates from the keystore.
     *
     * @param filter used to filter the result.
     * @return {@link List} of {@link CertificateResponse}
     */
    public List<CertificateResponse> listCertificateAliases(String filter) {

        List<String> aliasList;
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Listing certificate aliases for tenant: " + tenantDomain + " with filter: " + filter);
        }
        try {
            aliasList = keyStoreManagementService.getKeyStoreCertificateAliases(tenantDomain, filter);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Found " + (aliasList != null ? aliasList.size() : 0) + " certificate aliases.");
            }
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Unable to list certificates from keystore.");
        }
        return generateCertificateResponseList(aliasList != null ? aliasList : new ArrayList<>(), false);
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
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving certificate for tenant: " + tenantDomain + ", alias: " + alias + 
                    ", encodeCert: " + encodeCert);
        }
        try {
            certificate = keyStoreManagementService.getKeyStoreCertificate(tenantDomain, alias);
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Unable to retrieve the certificate with alias: " + alias + " from keystore");
        }

        if (certificate == null) {
            LOG.warn("Certificate not found for alias: " + alias);
            throw handleException(ERROR_CODE_INVALID_ALIAS, alias, "Couldn't find a certificate with alias: " + alias +
                    " from the keystore.", Response.Status.BAD_REQUEST);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Certificate retrieved successfully for alias: " + alias);
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

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Uploading certificate for tenant: " + tenantDomain + ", alias: " + alias);
        }
        try {
            keyStoreManagementService.addCertificate(tenantDomain, alias, certificate);
            LOG.info("Certificate uploaded successfully for tenant: " + tenantDomain + ", alias: " + alias);
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Unable to upload the certificate with alias: " + alias + " to the keystore.");
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

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting certificate for tenant: " + tenantDomain + ", alias: " + alias);
        }
        try {
            keyStoreManagementService.deleteCertificate(tenantDomain, alias);
            LOG.info("Certificate deleted successfully for tenant: " + tenantDomain + ", alias: " + alias);
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Unable to remove the certificate with alias: " + alias + " from the keystore.");
        }
    }

    /**
     * Retrieves the list of certificate aliases from the client truststore.
     *
     * @param filter used to filter the result.
     * @return {@link List} of {@link CertificateResponse}
     */
    public List<CertificateResponse> listClientCertificateAliases(String filter) {

        List<String> aliasList;
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Listing client certificate aliases for tenant: " + tenantDomain + " with filter: " + filter);
        }
        try {
            aliasList = keyStoreManagementService.getClientCertificateAliases(tenantDomain, filter);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Found " + (aliasList != null ? aliasList.size() : 0) + " client certificate aliases.");
            }
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Unable to retrieve the list of certificates from client truststore.");
        }
        return generateCertificateResponseList(aliasList != null ? aliasList : new ArrayList<>(), true);
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
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving client certificate for tenant: " + tenantDomain + ", alias: " + alias + 
                    ", encodeCert: " + encodeCert);
        }
        try {
            certificate = keyStoreManagementService.getClientCertificate(tenantDomain, alias);
        } catch (KeyStoreManagementException e) {
            throw handleException(e,
                    "Unable to retrieve the certificate with alias: " + alias + " from client truststore.");
        }

        if (certificate == null) {
            LOG.warn("Client certificate not found for alias: " + alias);
            throw handleException(ERROR_CODE_INVALID_ALIAS, alias, "Couldn't find a certificate with alias: " + alias +
                    " from the keystore.", Response.Status.BAD_REQUEST);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Client certificate retrieved successfully for alias: " + alias);
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
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving public certificate for tenant: " + tenantDomain + ", encodeCert: " + encodeCert);
        }
        try {
            certificateData = keyStoreManagementService.getPublicCertificate(tenantDomain);
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Unable to retrieve the public certificate from from keystore.");
        }

        String alias = null;
        X509Certificate certificate = null;
        Set<String> keyset = certificateData.keySet();
        for (String key : keyset) {
            alias = key;
            certificate = certificateData.get(alias);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Public certificate retrieved successfully with alias: " + alias);
        }
        return generateCertificateFile(alias, certificate, encodeCert);
    }

    private List<CertificateResponse> generateCertificateResponseList(List<String> aliasList, boolean isClientCert) {

        List<CertificateResponse> certificatesResponses = new ArrayList<>();
        String componentPath;
        if (!isClientCert) {
            componentPath = CERTIFICATE_PATH_COMPONENT;
        } else {
            componentPath = CLIENT_CERTIFICATE_PATH_COMPONENT;
        }

        for (String alias : aliasList) {
            CertificateResponse certificatesResponse = new CertificateResponse();
            certificatesResponse.setAlias(alias);
            String certificateEndPoint =
                    String.format(V1_API_PATH_COMPONENT + KEYSTORES_API_PATH_COMPONENT +
                            componentPath, alias);
            certificatesResponse.setCertificate(buildURIForHeader(certificateEndPoint));
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
                throw handleException(ERROR_CODE_ENCODE_CERTIFICATE, alias, e, Response.Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            certificateContent = certificate.toString();
        }

        String fileName = alias + CERTIFICATE_FILE_EXTENSION;
        File certsDirectory = new File(CERTIFICATE_TEMPORARY_DIRECTORY_PATH).getAbsoluteFile();
        if (certsDirectory.mkdirs()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(certsDirectory.toString() + " has been created.");
            }
        }
        File certificateFile = new File(CERTIFICATE_TEMPORARY_DIRECTORY_PATH + PATH_SEPERATOR + fileName)
                .getAbsoluteFile();
        try {
            if (certificateFile.createNewFile()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("A file has been created with name: " + fileName);
                }
            }
            try (Writer fileWriter = new OutputStreamWriter(new FileOutputStream(certificateFile),
                    StandardCharsets.UTF_8)) {
                fileWriter.write(certificateContent);
            }
        } catch (IOException e) {
            throw handleException(ERROR_CODE_FILE_WRITE, fileName, e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        return certificateFile;
    }

    private APIError handleException(KeyStoreManagementException e, String description) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder().withCode(e.getErrorCode())
                .withMessage(description).withDescription(e.getMessage());
        Response.Status status;
        ErrorResponse errorResponse;
        if (e instanceof KeyStoreManagementServerException) {
            errorResponse = builder.build(LOG, e, description);
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = builder.build(LOG, description);
            status = Response.Status.BAD_REQUEST;
        }
        return new APIError(status, errorResponse);
    }

    private APIError handleException(KeyStoreConstants.ErrorMessage errorMessage, String data, Exception e,
                                     Response.Status status) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder().withCode(errorMessage.getCode())
                .withMessage(generateErrorMessage(errorMessage.getMessage(), data)).withDescription(e.getMessage());
        ErrorResponse errorResponse = builder.build(LOG, e, e.getMessage());
        return new APIError(status, errorResponse);
    }

    private APIError handleException(KeyStoreConstants.ErrorMessage errorMessage, String data, String description,
                                     Response.Status status) {

        ErrorResponse.Builder builder = new ErrorResponse.Builder().withCode(errorMessage.getCode())
                .withMessage(generateErrorMessage(errorMessage.getMessage(), data)).withDescription(description);
        ErrorResponse errorResponse = builder.build();
        return new APIError(status, errorResponse);
    }

    private static String generateErrorMessage(String message, String data) {

        if (StringUtils.isNotBlank(data)) {
            message = String.format(message, data);
        }
        return message;
    }
}
