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

import org.wso2.carbon.identity.api.server.keystore.management.v2.model.PrivateKeysResponse;
import org.wso2.carbon.security.keystore.service.KeyData;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.wso2.carbon.identity.api.server.common.Constants.V2_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForHeader;
import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants.KEYSTORE_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants.KEYS_PATH_COMPONENT;

/**
 * Converts list of CertData to PrivateKeys response.
 */
public class PrivateKeysDataToExternal implements Function<List<KeyData>, List<PrivateKeysResponse>> {

    @Override
    public List<PrivateKeysResponse> apply(List<KeyData> keysData) {

        List<PrivateKeysResponse> keysresponse = new ArrayList<>();
        for (KeyData data : keysData) {
            PrivateKeysResponse response = new PrivateKeysResponse();
            response.setAlias(data.getAlias());
            response.setIssurDN(data.getIssuerDN());
            response.setSubjectDN(data.getSubjectDN());
            response.setNotAfter(data.getNotAfter());
            response.setPrivatekey(getKeyReferenceUrl(data.getAlias()));
            keysresponse.add(response);
        }
        return keysresponse;
    }

    private URI getKeyReferenceUrl(String alias) {

        String certificateEndPoint =
                String.format(V2_API_PATH_COMPONENT + KEYSTORE_API_PATH_COMPONENT + KEYS_PATH_COMPONENT, alias);
        return buildURIForHeader(certificateEndPoint);
    }

}
