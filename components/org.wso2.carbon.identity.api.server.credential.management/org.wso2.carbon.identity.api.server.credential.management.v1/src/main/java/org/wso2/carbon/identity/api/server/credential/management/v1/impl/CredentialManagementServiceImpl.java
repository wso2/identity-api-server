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

package org.wso2.carbon.identity.api.server.credential.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialHandler;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementService;
import org.wso2.carbon.identity.api.server.credential.management.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.server.credential.management.common.dto.CredentialDeletionRequestDTO;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.server.credential.management.v1.utils.CredentialMgtEndpointUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Implementation of the CredentialManagementServiceImpl interface.
 */
public class CredentialManagementServiceImpl implements CredentialManagementService {

    private static final Log LOG = LogFactory.getLog(CredentialManagementServiceImpl.class);
    private final Map<CredentialTypes, CredentialHandler> handlerMap;

    public CredentialManagementServiceImpl(Map<CredentialTypes, CredentialHandler> handlerMap) {

        this.handlerMap = handlerMap;
    }

    @Override
    public List<CredentialDTO> getCredentials(String entityId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving credentials for: " + entityId);
        }

        List<CredentialDTO> allCredentials = new ArrayList<>();
        for (CredentialHandler handler : handlerMap.values()) {
            try {
                allCredentials.addAll(handler.getCredentials(entityId));
            } catch (CredentialMgtException e) {
                throw CredentialMgtEndpointUtils.handleCredentialMgtException(e);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.info("Successfully retrieved " + allCredentials.size() + " credentials.");
        }
        return allCredentials;
    }

    @Override
    public void deleteCredential(CredentialDeletionRequestDTO credentialDeletionRequest) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting credential type: "
                    + credentialDeletionRequest.getType() + " with ID: "
                    + credentialDeletionRequest.getCredentialId() + " for entity ID: "
                    + credentialDeletionRequest.getEntityId());
        }
        try {
            CredentialMgtEndpointUtils.validateCredentialId(credentialDeletionRequest.getCredentialId());
            CredentialMgtEndpointUtils.validateCredentialType(credentialDeletionRequest.getType());
            CredentialTypes credentialType = CredentialTypes.valueOf(credentialDeletionRequest.getType()
                    .replace("-", "_").toUpperCase(Locale.ROOT));
            CredentialHandler handler = handlerMap.get(credentialType);
            handler.deleteCredential(credentialDeletionRequest);
        } catch (CredentialMgtException e) {
            throw CredentialMgtEndpointUtils.handleCredentialMgtException(e);
        }
        if (LOG.isDebugEnabled()) {
            LOG.info("Successfully deleted credential type: "
                    + credentialDeletionRequest.getType() + " with ID: "
                    + credentialDeletionRequest.getCredentialId() + " for entity ID: "
                    + credentialDeletionRequest.getEntityId());
        }
    }
}
