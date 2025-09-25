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

package org.wso2.carbon.identity.api.server.credential.management.common.impl;

import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.server.credential.management.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementService;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialHandler;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the CredentialManagementService interface.
 */
public class CredentialManagementServiceImpl implements CredentialManagementService {

    private final Map<CredentialTypes, CredentialHandler> handlerMap;

    public CredentialManagementServiceImpl() {

        handlerMap = new EnumMap<>(CredentialTypes.class);
        initializeHandlers();
    }

    private void initializeHandlers() {

        handlerMap.put(CredentialTypes.PASSKEY, new PasskeyCredentialHandler());
        handlerMap.put(CredentialTypes.PUSH_AUTH, new PushCredentialHandler());
    }

    @Override
    public List<CredentialDTO> getCredentialsForUser(String userId) throws CredentialMgtException {

        List<CredentialDTO> allCredentials = new ArrayList<>();

        for (CredentialHandler handler : handlerMap.values()) {
            allCredentials.addAll(handler.getCredentialsForUser(userId));
        }

        return allCredentials;
    }

    @Override
    public void deleteCredentialForUser(String userId, String type, String credentialId)
            throws CredentialMgtException {

        CredentialTypes credentialType = CredentialTypes.valueOf(type.replace("-", "_").toUpperCase());

        CredentialHandler handler = handlerMap.get(credentialType);
        handler.deleteCredentialForUser(userId, credentialId);
    }
}
