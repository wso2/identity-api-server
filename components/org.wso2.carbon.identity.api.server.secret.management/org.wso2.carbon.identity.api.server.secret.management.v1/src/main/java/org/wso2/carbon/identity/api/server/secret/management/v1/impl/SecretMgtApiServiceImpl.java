/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.secret.management.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.secret.management.v1.SecretMgtApiService;
import org.wso2.carbon.identity.api.server.secret.management.v1.core.SecretManagementService;
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
public class SecretMgtApiServiceImpl implements SecretMgtApiService {

    @Autowired
    private SecretManagementService secretManagementService;

    /**
     * This method will be invoked when creating a secret.
     *
     * @param secretType Type of the secret.
     * @param secretAddRequest Secret add request.
     * @return {@link Response} .
     */
    @Override
    public Response createSecret(String secretType, SecretAddRequest secretAddRequest) {

        SecretResponse secretResponse = secretManagementService.addSecret(secretType, secretAddRequest);
        return Response.created(
                getResourceLocation(secretResponse.getSecretName())
        ).entity(secretResponse).build();
    }

    /**
     * This method will be invoked when deleting a secret.
     *
     * @param secretType Type of the secret.
     * @param secretId Id of the secret.
     * @return {@link Response} .
     */
    @Override
    public Response deleteSecret(String secretType, String secretId) {

        secretManagementService.deleteSecret(secretId);
        return Response.noContent().build();
    }

    /**
     * This method will be invoked when retrieving a secret.
     *
     * @param secretType Type of the secret.
     * @param secretId Id of the secret.
     * @return {@link Response} .
     */
    @Override
    public Response getSecret(String secretType, String secretId) {

        return Response.ok().entity(secretManagementService.getSecret(secretId)).build();
    }

    /**
     * This method will be invoked when retrieving a list of secrets.
     *
     * @param secretType Type of the secrets.
     * @param limit Maximum number of records to return.
     * @param offset Number of records to skip for pagination.
     * @return {@link Response} .
     */
    @Override
    public Response getSecretsList(String secretType, Integer limit, Integer offset) {

        return Response.ok().entity(secretManagementService.getSecretsList(secretType)).build();
    }

    /**
     * This method will be invoked when patching a secret.
     *
     * @param secretType Type of the secret.
     * @param secretId Id of the secret.
     * @param secretPatchRequest Patch request.
     * @return {@link Response} .
     */
    @Override
    public Response patchSecret(String secretType, String secretId, SecretPatchRequest secretPatchRequest) {

        return Response.ok().entity(
                secretManagementService.patchSecret(secretType, secretId, secretPatchRequest)
        ).build();
    }

    /**
     * This method will be invoked when updating a secret.
     *
     * @param secretType Type of the secret.
     * @param secretId Id of the secret.
     * @param secretUpdateRequest Secret update request.
     * @return {@link Response} .
     */
    @Override
    public Response updateSecret(String secretType, String secretId, SecretUpdateRequest secretUpdateRequest) {

        return Response.ok().entity(
                secretManagementService.updateSecret(secretType, secretId, secretUpdateRequest)
        ).build();
    }

    /**
     * Retrieve the location for header.
     * @param resourceId Resource id.
     * @return URI
     */
    private URI getResourceLocation(String resourceId) {

        return ContextLoader.buildURIForHeader(
                V1_API_PATH_COMPONENT + SECRET_CONTEXT_PATH + "/" + resourceId);
    }
}
