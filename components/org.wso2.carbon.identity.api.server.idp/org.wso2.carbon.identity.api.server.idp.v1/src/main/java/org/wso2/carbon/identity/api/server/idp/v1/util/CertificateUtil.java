/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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
import org.wso2.carbon.identity.api.server.idp.v1.model.Certificate;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementClientException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for Certificate related operations.
 */
public final class CertificateUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Pattern PEM_INNER_BASE64 =
            Pattern.compile("-----BEGIN CERTIFICATE-----(.*?)-----END CERTIFICATE-----", Pattern.DOTALL);

    /**
     * Convert incoming `Certificate` model (containing certificates list) to a JSON array string
     * of objects with certValue and thumbPrint (sha-256 hex).
     * <p>
     * Example return:
     * [ {"certValue":"...","thumbPrint":"..."}, {"certValue":"...","thumbPrint":"..."} ]
     *
     * @param certificate incoming Certificate model object
     * @return JSON array string
     * @throws IdentityProviderManagementClientException on digest errors
     */
    public static String convertCertificateJsonString(Certificate certificate)
            throws IdentityProviderManagementClientException {

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

            byte[] outerDecoded;
            try {
                outerDecoded = Base64.getDecoder().decode(certValue);
            } catch (IllegalArgumentException iae) {
                outerDecoded = certValue.getBytes(StandardCharsets.UTF_8);
            }

            String pemText = new String(outerDecoded, StandardCharsets.UTF_8);
            byte[] derBytes = getDerBytesFromPem(pemText);
            if (derBytes.length == 0) {
                derBytes = outerDecoded;
            }
            String thumbPrint;
            try {
                thumbPrint = sha256Hex(derBytes);
            } catch (NoSuchAlgorithmException e) {
                throw new IdentityProviderManagementClientException("Error while generating certificate thumbprint.",
                        e);
            }
            resultArray.addObject()
                    .put("certValue", certValue)
                    .put("thumbPrint", thumbPrint);
        }

        return resultArray.toString();
    }

    private static byte[] getDerBytesFromPem(String pemText) {

        Matcher m = PEM_INNER_BASE64.matcher(pemText);
        if (m.find()) {
            String innerBase64 = m.group(1);
            innerBase64 = innerBase64.replaceAll("\\s+", "");
            try {
                return Base64.getDecoder().decode(innerBase64);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return new byte[0];
    }

    private static String sha256Hex(byte[] data) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(data);
        StringBuilder sb = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
