/*
* Copyright (c) 2019-2024, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.permission.management.v1.impl;

import org.wso2.carbon.identity.api.server.permission.management.v1.PermissionManagementApiService;
import org.wso2.carbon.identity.api.server.permission.management.v1.core.PermissionManagementService;
import org.wso2.carbon.identity.api.server.permission.management.v1.factories.PermissionManagementServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Permission Management API Service Implementation class.
 */
public class PermissionManagementApiServiceImpl implements PermissionManagementApiService {

    private final PermissionManagementService permissionManagementService;

    public PermissionManagementApiServiceImpl() {
        try {
            this.permissionManagementService = PermissionManagementServiceFactory.getPermissionManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating PermissionManagementService.", e);
        }
    }

    @Override
    public Response permissionManagementPermissionsGet() {

        return Response.ok().entity(permissionManagementService.getAllPermissions()).build();
    }
}
