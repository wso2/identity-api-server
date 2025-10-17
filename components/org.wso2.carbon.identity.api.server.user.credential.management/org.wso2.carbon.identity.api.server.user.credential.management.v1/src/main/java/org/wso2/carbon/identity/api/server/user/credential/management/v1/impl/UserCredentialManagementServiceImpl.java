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

package org.wso2.carbon.identity.api.server.user.credential.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialHandler;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialManagementService;
import org.wso2.carbon.identity.api.server.user.credential.management.common.dto.UserCredentialDTO;
import org.wso2.carbon.identity.api.server.user.credential.management.common.dto.UserCredentialDeletionRequestDTO;
import org.wso2.carbon.identity.api.server.user.credential.management.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.server.user.credential.management.v1.utils.UserCredentialMgtEndpointUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Implementation of the UserCredentialManagementServiceImpl interface.
 */
public class UserCredentialManagementServiceImpl implements UserCredentialManagementService {

    private static final Log LOG = LogFactory.getLog(UserCredentialManagementServiceImpl.class);
    private final Map<CredentialTypes, UserCredentialHandler> handlerMap;

    public UserCredentialManagementServiceImpl(Map<CredentialTypes, UserCredentialHandler> handlerMap) {

        this.handlerMap = handlerMap;
    }

    @Override
    public List<UserCredentialDTO> getCredentialsForUser(String userId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving credentials for user: " + userId);
        }

        List<UserCredentialDTO> allCredentials = new ArrayList<>();
        for (UserCredentialHandler handler : handlerMap.values()) {
            try {
                allCredentials.addAll(handler.getCredentialsForUser(userId));
            } catch (CredentialMgtException e) {
                throw UserCredentialMgtEndpointUtils.handleCredentialMgtException(e);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.info("Successfully retrieved " + allCredentials.size() + " credentials for user.");
        }
        return allCredentials;
    }

    @Override
    public void deleteCredentialForUser(UserCredentialDeletionRequestDTO userCredentialDeletionRequest) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting credential type: "
                    + userCredentialDeletionRequest.getType() + " with ID: "
                    + userCredentialDeletionRequest.getCredentialId() + " for user: "
                    + userCredentialDeletionRequest.getUserId());
        }
        try {
            UserCredentialMgtEndpointUtils.validateCredentialId(userCredentialDeletionRequest.getCredentialId());
            UserCredentialMgtEndpointUtils.validateCredentialType(userCredentialDeletionRequest.getType());
            CredentialTypes credentialType = CredentialTypes.valueOf(userCredentialDeletionRequest.getType()
                    .replace("-", "_").toUpperCase(Locale.ROOT));
            UserCredentialHandler handler = handlerMap.get(credentialType);
            handler.deleteCredentialForUser(userCredentialDeletionRequest);
        } catch (CredentialMgtException e) {
            throw UserCredentialMgtEndpointUtils.handleCredentialMgtException(e);
        }
        if (LOG.isDebugEnabled()) {
            LOG.info("Successfully deleted credential for user");
        }
    }
}
