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

import org.wso2.carbon.identity.api.server.user.credential.management.common.dto.UserCredentialDeletionRequestDTO;
import org.wso2.carbon.identity.api.server.user.credential.management.v1.UsersApiService;
import org.wso2.carbon.identity.api.server.user.credential.management.v1.factories.UserCredentialManagementServiceFactory;
import org.wso2.carbon.identity.api.server.user.credential.management.v1.utils.UserCredentialMgtEndpointUtils;

import javax.ws.rs.core.Response;

/**
 * Implementation of the UserCredential API Service.
 */
public class UserCredentialApiServiceImpl implements UsersApiService {

    private final UserCredentialManagementServiceImpl userCredentialManagementService;

    public UserCredentialApiServiceImpl() {

        this.userCredentialManagementService = UserCredentialManagementServiceFactory
                .getUserCredentialManagementService();
    }

    @Override
    public Response getCredentialsByUserId(String userId) {

        return Response.ok()
                .entity(UserCredentialMgtEndpointUtils
                .toCredentialResponse(userCredentialManagementService.getCredentialsForUser(userId)))
                .build();
    }

    @Override
    public Response deleteCredentialById(String userId, String type, String credentialId) {

        UserCredentialDeletionRequestDTO userCredentialDeletionRequest = new UserCredentialDeletionRequestDTO.Builder()
                .userId(userId)
                .type(type)
                .credentialId(credentialId)
                .build();
        userCredentialManagementService.deleteCredentialForUser(userCredentialDeletionRequest);
        return Response.noContent().build();
    }
}
