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

import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementService;
import org.wso2.carbon.identity.api.server.credential.management.common.dto.CredentialDeletionRequestDTO;
import org.wso2.carbon.identity.api.server.credential.management.v1.UsersApiService;
import org.wso2.carbon.identity.api.server.credential.management.v1.factories.CredentialManagementServiceFactory;
import org.wso2.carbon.identity.api.server.credential.management.v1.utils.CredentialMgtEndpointUtils;

import javax.ws.rs.core.Response;

/**
 * Implementation of User Credential API Service.
 */
public class UsersCredentialApiServiceImpl implements UsersApiService {

    private final CredentialManagementService credentialManagementService =
            CredentialManagementServiceFactory.getCredentialManagementService();

    @Override
    public Response deleteUserCredentialById(String userId, String type, String credentialId) {

        CredentialDeletionRequestDTO userCredentialDeletionRequest = new CredentialDeletionRequestDTO.Builder()
                .entityId(userId)
                .type(type)
                .credentialId(credentialId)
                .build();
        credentialManagementService.deleteCredential(userCredentialDeletionRequest);
        return Response.noContent().build();
    }

    @Override
    public Response getUserCredentialsById(String userId) {

        return Response.ok()
                .entity(CredentialMgtEndpointUtils
                        .toCredentialResponse(credentialManagementService.getCredentials(userId)))
                .build();
    }
}
