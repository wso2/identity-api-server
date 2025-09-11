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
import org.wso2.carbon.identity.api.server.secret.management.v1.SecretTypeApiService;
import org.wso2.carbon.identity.api.server.secret.management.v1.core.SecretTypeManagementService;
import org.wso2.carbon.identity.api.server.secret.management.v1.factories.SecretTypeManagementServiceFactory;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretTypeAddRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretTypeResponse;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretTypeUpdateRequest;

import java.net.URI;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants.
        SECRET_TYPE_CONTEXT_PATH;
import static org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants.
        V1_API_PATH_COMPONENT;

/**
 * Implementation of Secret Type Management REST API.
 */
public class SecretTypeApiServiceImpl implements SecretTypeApiService {

    private static final Log log = LogFactory.getLog(SecretTypeApiServiceImpl.class);
    private final SecretTypeManagementService secretTypeManagementService;

    public SecretTypeApiServiceImpl() {

        try {
            this.secretTypeManagementService = SecretTypeManagementServiceFactory.getSecretTypeManagementService();
            if (log.isDebugEnabled()) {
                log.debug("SecretTypeManagementService initialized successfully.");
            }
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating SecretTypeManagementService.", e);
            throw new RuntimeException("Error occurred while initiating SecretTypeManagementService.", e);
        }
    }

    @Override
    public Response createSecretType(SecretTypeAddRequest secretTypeAddRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Creating secret type with name: " + 
                    (secretTypeAddRequest != null ? secretTypeAddRequest.getName() : "null"));
        }
        if (secretTypeAddRequest == null) {
            throw new IllegalArgumentException("SecretTypeAddRequest cannot be null");
        }
        SecretTypeResponse secretType = secretTypeManagementService.addSecretType(secretTypeAddRequest);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + SECRET_TYPE_CONTEXT_PATH + "/"
                + secretType.getName());
        if (log.isDebugEnabled()) {
            log.debug("Secret type created successfully with name: " + secretType.getName());
        }
        return Response.created(location).entity(secretType).build();
    }

    @Override
    public Response deleteSecretType(String name) {

        if (log.isDebugEnabled()) {
            log.debug("Deleting secret type with name: " + name);
        }
        secretTypeManagementService.deleteSecretType(name);
        if (log.isDebugEnabled()) {
            log.debug("Secret type deleted successfully with name: " + name);
        }
        return Response.noContent().build();
    }

    @Override
    public Response getSecretType(String name) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving secret type with name: " + name);
        }
        return Response.ok().entity(secretTypeManagementService.getSecretType(name)).build();
    }

    @Override
    public Response updateSecretType(String name, SecretTypeUpdateRequest secretTypeUpdateRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Updating secret type with name: " + name);
        }
        return Response.ok().entity(secretTypeManagementService
                .updateTypeSecret(name, secretTypeUpdateRequest)).build();
    }
}
