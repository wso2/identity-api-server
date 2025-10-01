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

package org.wso2.carbon.identity.api.server.credential.management.v1.core;

import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementService;
import org.wso2.carbon.identity.api.server.credential.management.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.server.credential.management.v1.utils.CredentialMgtEndpointUtils;

import java.util.List;

/**
 * Server service class for Credential Management Service.
 */
public class ServerCredentialManagementService {

    private final CredentialManagementService adminCredentialManagementService;

    public ServerCredentialManagementService(CredentialManagementService adminCredentialManagementService) {

        this.adminCredentialManagementService = adminCredentialManagementService;
    }

    /**
     * Get credentials for a given user.
     *
     * @param userId ID of the user.
     * @return List of credentials.
     */
    public List<CredentialDTO> getCredentialsForUser(String userId) {

        try {
            return adminCredentialManagementService.getCredentialsForUser(userId);
        } catch (CredentialMgtException e) {
            throw CredentialMgtEndpointUtils.handleCredentialMgtException(e);
        }
    }

    /**
     * Delete a credential for a user.
     *
     * @param userId       ID of the user.
     * @param type         Type of the credential.
     * @param credentialId ID of the credential.
     */
    public void deleteCredentialForUser(String userId, String type, String credentialId) {

        try {
            CredentialMgtEndpointUtils.validateCredentialId(credentialId);
            CredentialMgtEndpointUtils.validateCredentialType(type);
            adminCredentialManagementService.deleteCredentialForUser(userId, type, credentialId);
        } catch (CredentialMgtException e) {
            throw CredentialMgtEndpointUtils.handleCredentialMgtException(e);
        }
    }
}
