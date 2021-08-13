/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.server.secret.management.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.secret.management.v1.SecretTypeApiService;
import org.wso2.carbon.identity.api.server.secret.management.v1.core.SecretTypeManagementService;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretTypeAddRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretTypeResponse;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretTypeUpdateRequest;

import java.net.URI;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants.SECRET_CONTEXT_PATH;
import static org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants.V1_API_PATH_COMPONENT;

/**
 * Implementation of Secret Type Management REST API.
 */
public class SecretTypeApiServiceImpl implements SecretTypeApiService {

    @Autowired
    private SecretTypeManagementService secretTypeManagementService;

    @Override
    public Response createSecretType(SecretTypeAddRequest secretTypeAddRequest) {

        SecretTypeResponse secretType = secretTypeManagementService.addSecretType(secretTypeAddRequest);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + SECRET_CONTEXT_PATH + "/"
                + secretType.getName());
        return Response.created(location).entity(secretType).build();

    }

    @Override
    public Response deleteSecretType(String name) {

        secretTypeManagementService.deleteSecretType(name);
        return Response.noContent().build();
    }

    @Override
    public Response getSecretType(String name) {

        return Response.ok().entity(secretTypeManagementService.getSecretType(name)).build();
    }

    @Override
    public Response updateSecretType(String name, SecretTypeUpdateRequest secretTypeUpdateRequest) {

        return Response.ok()
                .entity(secretTypeManagementService.updateTypeSecret(name, secretTypeUpdateRequest)).build();
    }
}
