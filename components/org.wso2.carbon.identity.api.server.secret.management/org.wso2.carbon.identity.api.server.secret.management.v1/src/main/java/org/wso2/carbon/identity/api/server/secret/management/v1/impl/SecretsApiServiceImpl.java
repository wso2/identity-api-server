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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import static org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants.
        SECRET_CONTEXT_PATH;
import static org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants.
        V1_API_PATH_COMPONENT;

/**
 * Implementation of Secret Management REST API.
 */
public class SecretsApiServiceImpl implements SecretsApiService {

    private static final Log log = LogFactory.getLog(SecretsApiServiceImpl.class);
    private final SecretManagementService secretManagementService;

    public SecretsApiServiceImpl() {

        try {
            this.secretManagementService = SecretManagementServiceFactory.getSecretManagementService();
            if (log.isDebugEnabled()) {
                log.debug("SecretManagementService initialized successfully.");
            }
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating SecretManagementService.", e);
            throw new RuntimeException("Error occurred while initiating SecretManagementService.", e);
        }
    }

    @Override
    public Response createSecret(String secretType, SecretAddRequest secretAddRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Creating secret with type: " + secretType + " and name: " + 
                    (secretAddRequest != null ? secretAddRequest.getName() : "null"));
        }
        if (secretAddRequest == null) {
            throw new IllegalArgumentException("SecretAddRequest cannot be null");
        }
        SecretResponse secretResponse = secretManagementService.addSecret(secretType, secretAddRequest);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + SECRET_CONTEXT_PATH + "/"
                + secretResponse.getSecretName());
        if (log.isDebugEnabled()) {
            log.debug("Secret created successfully with name: " + secretResponse.getSecretName());
        }
        return Response.created(location).entity(secretResponse).build();

    }

    @Override
    public Response deleteSecret(String secretType, String name) {

        if (log.isDebugEnabled()) {
            log.debug("Deleting secret with type: " + secretType + " and name: " + name);
        }
        secretManagementService.deleteSecret(secretType, name);
        if (log.isDebugEnabled()) {
            log.debug("Secret deleted successfully with name: " + name);
        }
        return Response.noContent().build();
    }

    @Override
    public Response getSecret(String secretType, String name) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving secret with type: " + secretType + " and name: " + name);
        }
        return Response.ok().entity(secretManagementService.getSecret(secretType, name)).build();
    }

    @Override
    public Response getSecretsList(String secretType) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving secrets list for type: " + secretType);
        }
        return Response.ok().entity(secretManagementService.getSecretsList(secretType)).build();
    }

    @Override
    public Response patchSecret(String secretType, String name, SecretPatchRequest secretPatchRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Patching secret with type: " + secretType + " and name: " + name);
        }
        return Response.ok().entity(secretManagementService.patchSecret(secretType, name, secretPatchRequest)).build();
    }

    @Override
    public Response updateSecret(String secretType, String name, SecretUpdateRequest secretUpdateRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Updating secret with type: " + secretType + " and name: " + name);
        }
        return Response.ok()
                .entity(secretManagementService.updateSecret(secretType, name, secretUpdateRequest)).build();
    }
}
