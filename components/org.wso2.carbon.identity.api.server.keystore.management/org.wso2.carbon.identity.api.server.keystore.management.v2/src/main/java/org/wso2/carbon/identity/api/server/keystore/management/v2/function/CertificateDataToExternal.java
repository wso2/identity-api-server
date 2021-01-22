/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.keystore.management.v2.function;

import org.wso2.carbon.identity.api.server.keystore.management.v2.model.CertificateData;
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.Fingerprints;
import org.wso2.carbon.security.keystore.service.KeyData;

import java.util.Map;
import java.util.function.Function;

/**
 * Converts CertData to PrivateKeyData object.
 */
public class CertificateDataToExternal implements Function<KeyData, CertificateData> {


    @Override
    public CertificateData apply(KeyData keyData) {

        CertificateData response = new CertificateData();
        if (keyData != null) {
            response.setIssurDN(keyData.getIssuerDN());
            response.setSubjectDN(keyData.getSubjectDN());
            response.setSerialNumber(String.valueOf(String.valueOf(keyData.getSerialNumber())));
            response.setNotAfter(keyData.getNotAfter());
            response.setNotBefore(keyData.getNotBefore());
            response.setVersion(keyData.getVersion());
            Fingerprints fingerprints = new Fingerprints();
            for (Map.Entry<String, String> entry : keyData.getFingerprint().entrySet()) {
                switch (entry.getKey()) {
                    case ("MD5"):
                        fingerprints.setMD5(entry.getValue());
                        break;
                    case ("SHA1"):
                        fingerprints.setSHA1(entry.getValue());
                        break;
                    case ("SHA-256"):
                        fingerprints.setSHA256(entry.getValue());
                        break;
                    default:
                        break;
                }
            }
            response.setCertificateFingerprints(fingerprints);
            response.setSignatureAlgorithm(keyData.getSignatureAlgName());
            response.setPublicKey(keyData.getPublicKey());
        }
        return response;
    }
}
