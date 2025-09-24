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

import org.wso2.carbon.identity.api.server.credential.management.v1.CredentialApiService;
import org.wso2.carbon.identity.api.server.credential.management.v1.core.ServerCredentialManagementService;
import org.wso2.carbon.identity.api.server.credential.management.v1.factories.ServerCredentialManagementServiceFactory;
import org.wso2.carbon.identity.api.server.credential.management.v1.utils.CredentialMgtEndpointUtils;

import javax.ws.rs.core.Response;

public class CredentialApiServiceImpl implements CredentialApiService {

    private final ServerCredentialManagementService adminServerCredentialManagementService;

    public CredentialApiServiceImpl() {

        try {
            this.adminServerCredentialManagementService = ServerCredentialManagementServiceFactory
                    .getServerCredentialManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating server credential management service.", e);
        }
    }

    @Override
    public Response getCredentialsByUserId(String userId) {

        return Response.ok()
                .entity(CredentialMgtEndpointUtils
                .toCredentialResponse(adminServerCredentialManagementService.getCredentialsForUser(userId)))
                .build();
    }

    @Override
    public Response deleteCredentialById(String userId, String type, String credentialId) {

        adminServerCredentialManagementService.deleteCredentialForUser(userId, type, credentialId);

        return Response.noContent().build();
    }
}
