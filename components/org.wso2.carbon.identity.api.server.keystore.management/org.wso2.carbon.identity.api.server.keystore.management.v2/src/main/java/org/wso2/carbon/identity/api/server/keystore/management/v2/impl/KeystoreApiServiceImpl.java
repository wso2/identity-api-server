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

package org.wso2.carbon.identity.api.server.keystore.management.v2.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.keystore.management.v2.KeystoreApiService;
import org.wso2.carbon.identity.api.server.keystore.management.v2.core.KeyStoreMgtService;
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.AddPrivateKeyRequest;

import javax.ws.rs.core.Response;

/**
 * Keystore api implemetation.
 */
public class KeystoreApiServiceImpl implements KeystoreApiService {

    @Autowired
    private KeyStoreMgtService keyStoreMgtService;

    @Override
    public Response deletePrivateKey(String alias) {

        return keyStoreMgtService.deletePrivateKey(alias);
    }

    @Override
    public Response getPrivateKey(String alias) {

        return Response.ok().entity(keyStoreMgtService.getPrivateKey(alias)).build();
    }

    @Override
    public Response getPrivateKeyAliases(String filter) {

        return Response.ok().entity(keyStoreMgtService.getAllPrivateKeys()).build();
    }

    @Override
    public Response uploadPrivateKey(AddPrivateKeyRequest addPrivateKeyRequest) {

        return keyStoreMgtService.uploadPrivateKey(addPrivateKeyRequest.getAlias(),
                addPrivateKeyRequest.getCertificate());
    }
}
