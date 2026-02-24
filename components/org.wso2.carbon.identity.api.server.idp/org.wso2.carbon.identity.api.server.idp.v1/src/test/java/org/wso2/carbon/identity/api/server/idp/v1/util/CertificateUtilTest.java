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
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.idp.v1.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.api.server.idp.v1.model.Certificate;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementClientException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

/**
 * Unit tests for {@link CertificateUtil}.
 */
public class CertificateUtilTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void testConvertCertificateJsonStringWithNullCertificateReturnsEmptyArray()
            throws IdentityProviderManagementClientException {

        Assert.assertEquals(CertificateUtil.convertCertificateJsonString(null), "[]");
    }

    @Test
    public void testConvertCertificateJsonStringWithNullCertificatesReturnsEmptyArray()
            throws IdentityProviderManagementClientException {

        Certificate certificate = new Certificate();
        certificate.setCertificates(null);

        Assert.assertEquals(CertificateUtil.convertCertificateJsonString(certificate), "[]");
    }

    @Test
    public void testConvertCertificateJsonStringWithEmptyCertificatesReturnsEmptyArray()
            throws IdentityProviderManagementClientException {

        Certificate certificate = new Certificate();
        certificate.setCertificates(Collections.emptyList());

        Assert.assertEquals(CertificateUtil.convertCertificateJsonString(certificate), "[]");
    }

    @Test(expectedExceptions = IdentityProviderManagementClientException.class)
    public void testConvertCertificateJsonStringWithNonBase64CertificateThrows()
            throws Exception {

        String certValue = "plain-certificate-value";
        Certificate certificate = new Certificate().certificates(Collections.singletonList(certValue));

        // CertificateUtil requires PEM content; non-PEM input should cause a client exception
        CertificateUtil.convertCertificateJsonString(certificate);
    }

    @Test(expectedExceptions = IdentityProviderManagementClientException.class)
    public void testConvertCertificateJsonStringWithBase64NonPemThrows()
            throws Exception {

        byte[] decodedBytes = "decoded-certificate-text".getBytes(StandardCharsets.UTF_8);
        String certValue = Base64.getEncoder().encodeToString(decodedBytes);
        Certificate certificate = new Certificate().certificates(Collections.singletonList(certValue));

        // Decoded content is not PEM; CertificateUtil should fail to extract DER and throw
        CertificateUtil.convertCertificateJsonString(certificate);
    }

    @Test
    public void testConvertCertificateJsonStringWithBase64EncodedPemUsesDerBytes()
            throws Exception {

        byte[] derBytes = "sample-der-bytes".getBytes(StandardCharsets.UTF_8);
        String innerBase64 = Base64.getEncoder().encodeToString(derBytes);
        String pem = "-----BEGIN CERTIFICATE-----\n" + innerBase64 + "\n-----END CERTIFICATE-----";
        String certValue = Base64.getEncoder().encodeToString(pem.getBytes(StandardCharsets.UTF_8));
        Certificate certificate = new Certificate().certificates(Collections.singletonList(certValue));

        JsonNode result = toJsonArray(CertificateUtil.convertCertificateJsonString(certificate));
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).get("certValue").asText(), certValue);
        Assert.assertEquals(result.get(0).get("thumbPrint").asText(), sha256Hex(derBytes));
    }

    @Test(expectedExceptions = IdentityProviderManagementClientException.class)
    public void testConvertCertificateJsonStringSkipsNullEntriesThrowsWhenNoPemPresent()
            throws Exception {

        String certValue = "plain-cert";
        Certificate certificate = new Certificate().certificates(Arrays.asList(null, certValue, null));

        // Only non-PEM entries -> should result in exception
        CertificateUtil.convertCertificateJsonString(certificate);
    }

    private static JsonNode toJsonArray(String json) throws Exception {

        return OBJECT_MAPPER.readTree(json);
    }

    private static String sha256Hex(byte[] value) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = digest.digest(value);
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            builder.append(String.format("%02x", b & 0xff));
        }
        return builder.toString();
    }
}
