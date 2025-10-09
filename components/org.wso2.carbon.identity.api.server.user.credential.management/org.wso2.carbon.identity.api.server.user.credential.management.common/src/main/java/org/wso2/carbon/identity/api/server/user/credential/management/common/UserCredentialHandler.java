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

package org.wso2.carbon.identity.api.server.user.credential.management.common;

import org.wso2.carbon.identity.api.server.user.credential.management.common.dto.UserCredentialDTO;
import org.wso2.carbon.identity.api.server.user.credential.management.common.dto.UserCredentialDeletionRequestDTO;
import org.wso2.carbon.identity.api.server.user.credential.management.common.exception.CredentialMgtException;
import java.util.List;

/**
 * Credential Management Service interface.
 */
public interface UserCredentialHandler {
    /**
     * Retrieves credentials for a given user.
     */
    List<UserCredentialDTO> getCredentialsForUser(String userId) throws CredentialMgtException;

    /**
     * Deletes a credential for a user.
     */
    void deleteCredentialForUser(UserCredentialDeletionRequestDTO userCredentialDeletionRequest)
            throws CredentialMgtException;
}
