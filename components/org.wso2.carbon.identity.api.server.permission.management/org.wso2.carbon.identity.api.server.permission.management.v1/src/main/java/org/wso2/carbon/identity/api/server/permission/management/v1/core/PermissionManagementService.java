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

package org.wso2.carbon.identity.api.server.permission.management.v1.core;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.permission.management.common.Constant;
import org.wso2.carbon.identity.api.server.permission.management.v1.model.Permission;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.user.mgt.RolePermissionException;
import org.wso2.carbon.user.mgt.RolePermissionManagementService;

import javax.ws.rs.core.Response;

/**
 * The Permission Management Service class.
 */
public class PermissionManagementService {

    private final RolePermissionManagementService rolePermissionManagementService;
    private static final Log LOG = LogFactory.getLog(PermissionManagementService.class);

    public PermissionManagementService(RolePermissionManagementService rolePermissionManagementService) {

        this.rolePermissionManagementService = rolePermissionManagementService;
    }

    /**
     * Get all permissions array.
     *
     * @return PermissionObject[] of permissions.
     */
    public Permission[] getAllPermissions() {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Retrieving all permissions for tenant: " + tenantDomain);
            }
            Permission[] permissions = getPermissionObjects(rolePermissionManagementService.getAllPermissions(
                    IdentityTenantUtil.getTenantId(tenantDomain)));
            LOG.info("Successfully retrieved " + permissions.length + " permissions for tenant: " + tenantDomain);
            return permissions;
        } catch (RolePermissionException e) {
            String currentTenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            LOG.warn("Error occurred while retrieving permissions for tenant: " + currentTenantDomain);
            throw handleException(e);
        }
    }

    /**
     * Convert Permission Object to PermissionObject type.
     *
     * @param permissions from backend service.
     * @return PermissionObject[] of permissions.
     */
    private Permission[] getPermissionObjects(org.wso2.carbon.user.mgt.common.model.Permission[] permissions) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Converting " + permissions.length + " permission objects");
        }
        Permission[] outputPermissions = new Permission[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            Permission permission = new Permission();
            permission.setDisplayName(permissions[i].getDisplayName());
            permission.setResourcePath(permissions[i].getResourcePath());
            outputPermissions[i] = permission;
        }
        return outputPermissions;
    }

    private APIError handleException(Exception e, String... data) {

        ErrorResponse errorResponse = getErrorBuilder(data)
                .build(LOG, e, buildErrorDescription(data));

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, errorResponse);
    }

    private ErrorResponse.Builder getErrorBuilder(String... data) {

        return new ErrorResponse.Builder()
                .withCode(Constant.ErrorMessage.ERROR_CODE_ERROR_GETTING_PERMISSIONS.getCode())
                .withMessage(Constant.ErrorMessage.ERROR_CODE_ERROR_GETTING_PERMISSIONS.getMessage())
                .withDescription(buildErrorDescription(data));
    }

    private String buildErrorDescription(String... data) {

        String errorDescription;
        if (!ArrayUtils.isEmpty(data)) {
            errorDescription = String.format(Constant.ErrorMessage
                    .ERROR_CODE_ERROR_GETTING_PERMISSIONS.getDescription(), (Object) data);
        } else {
            errorDescription = Constant.ErrorMessage.ERROR_CODE_ERROR_GETTING_PERMISSIONS.getDescription();
        }
        return errorDescription;
    }
}
