/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.permission.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.permission.management.v1.PermissionManagementApiService;
import org.wso2.carbon.identity.api.server.permission.management.v1.core.PermissionManagementService;
import org.wso2.carbon.identity.api.server.permission.management.v1.factories.PermissionManagementServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Permission Management API Service Implementation class.
 */
public class PermissionManagementApiServiceImpl implements PermissionManagementApiService {

    private static final Log LOG = LogFactory.getLog(PermissionManagementApiServiceImpl.class);
    private final PermissionManagementService permissionManagementService;

    public PermissionManagementApiServiceImpl() {

        try {
            this.permissionManagementService = PermissionManagementServiceFactory.getPermissionManagementService();
            LOG.info("PermissionManagementApiServiceImpl initialized successfully");
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while initiating PermissionManagementService");
            throw new RuntimeException("Error occurred while initiating PermissionManagementService.", e);
        }
    }

    @Override
    public Response permissionManagementPermissionsGet() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Processing GET request for all permissions");
        }
        Response response = Response.ok().entity(permissionManagementService.getAllPermissions()).build();
        LOG.info("Successfully processed GET request for all permissions");
        return response;
    }
}
