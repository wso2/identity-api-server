/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.idp.v1.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.idp.v1.model.Certificate;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementClientException;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;

/**
 * Utility class for Certificate related operations.
 */
public final class CertificateUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Log LOG = LogFactory.getLog(CertificateUtil.class);
    private static final String CERT_VALUE_KEY = "certValue";
    private static final String THUMB_PRINT_KEY = "thumbPrint";
    private static final String CERT_TYPE = "X.509";

    /**
     * Convert incoming `Certificate` model (containing certificates list) to a JSON array string
     * of objects with certValue and thumbPrint (sha-256 hex).
     * <p>
     * Example return:
     * [ {"certValue":"...","thumbPrint":"..."}, {"certValue":"...","thumbPrint":"..."} ]
     *
     * @param certificate incoming Certificate model object.
     * @return JSON array string
     */
    public static String convertCertificateJsonString(Certificate certificate)
            throws IdentityProviderManagementClientException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Converting certificate to JSON string format");
        }

        if (certificate == null) {
            return "[]";
        }

        List<String> certificates = certificate.getCertificates();
        if (certificates == null || certificates.isEmpty()) {
            return "[]";
        }

        ArrayNode resultArray = MAPPER.createArrayNode();

        for (String certValue : certificates) {
            if (certValue == null) {
                continue;
            }
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance(CERT_TYPE);
                java.security.cert.Certificate cert = certificateFactory.generateCertificate(
                        new ByteArrayInputStream(certValue.getBytes(StandardCharsets.UTF_8)));
                String thumbPrint = OAuth2Util.getThumbPrint(cert);

                resultArray.addObject()
                        .put(CERT_VALUE_KEY, certValue)
                        .put(THUMB_PRINT_KEY, thumbPrint);
            } catch (CertificateException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Exception occurred while generating certificate from the provided " +
                            "certificate value", e);
                }
                throw new IdentityProviderManagementClientException(
                        "Error while generating certificate.", e);
            } catch (IdentityOAuth2Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Exception occurred while calculating thumbprint for the provided " +
                            "certificate", e);
                }
                throw new IdentityProviderManagementClientException(
                        "Error while calculating thumbprint for the provided certificate.", e);
            }
        }

        return resultArray.toString();
    }
}
