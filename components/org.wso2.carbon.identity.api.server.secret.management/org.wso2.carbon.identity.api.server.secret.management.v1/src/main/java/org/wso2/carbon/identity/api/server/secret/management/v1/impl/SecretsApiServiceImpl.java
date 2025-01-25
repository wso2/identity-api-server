/*
 * Copyright (c) 2021-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.secret.management.v1.impl;

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.secret.management.v1.SecretsApiService;
import org.wso2.carbon.identity.api.server.secret.management.v1.core.SecretManagementService;
import org.wso2.carbon.identity.api.server.secret.management.v1.factories.SecretManagementServiceFactory;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretAddRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretPatchRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretResponse;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretUpdateRequest;

import java.net.URI;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants.SECRET_CONTEXT_PATH;
import static org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants.V1_API_PATH_COMPONENT;

/**
 * Implementation of Secret Management REST API.
 */
public class SecretsApiServiceImpl implements SecretsApiService {

    private final SecretManagementService secretManagementService;

    public SecretsApiServiceImpl() {

        try {
            this.secretManagementService = SecretManagementServiceFactory.getSecretManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating SecretManagementService.", e);
        }
    }

    @Override
    public Response createSecret(String secretType, SecretAddRequest secretAddRequest) {

        SecretResponse secretResponse = secretManagementService.addSecret(secretType, secretAddRequest);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + SECRET_CONTEXT_PATH + "/"
                + secretResponse.getSecretName());
        return Response.created(location).entity(secretResponse).build();

    }

    @Override
    public Response deleteSecret(String secretType, String name) {

        secretManagementService.deleteSecret(secretType, name);
        return Response.noContent().build();
    }

    @Override
    public Response getSecret(String secretType, String name) {

        return Response.ok().entity(secretManagementService.getSecret(secretType, name)).build();
    }

    @Override
    public Response getSecretsList(String secretType) {

        return Response.ok().entity(secretManagementService.getSecretsList(secretType)).build();
    }

    @Override
    public Response patchSecret(String secretType, String name, SecretPatchRequest secretPatchRequest) {

        return Response.ok().entity(secretManagementService.patchSecret(secretType, name, secretPatchRequest)).build();
    }

    @Override
    public Response updateSecret(String secretType, String name, SecretUpdateRequest secretUpdateRequest) {

        return Response.ok()
                .entity(secretManagementService.updateSecret(secretType, name, secretUpdateRequest)).build();
    }
}
