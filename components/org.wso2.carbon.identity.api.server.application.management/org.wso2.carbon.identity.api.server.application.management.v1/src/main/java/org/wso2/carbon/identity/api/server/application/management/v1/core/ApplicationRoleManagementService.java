/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1.core;

import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.Role;
import org.wso2.carbon.identity.api.server.application.management.v1.RoleCreationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.RolePatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.application.role.mgt.ApplicationRoleManager;
import org.wso2.carbon.identity.application.role.mgt.exceptions.ApplicationRoleManagementClientException;
import org.wso2.carbon.identity.application.role.mgt.exceptions.ApplicationRoleManagementException;
import org.wso2.carbon.identity.application.role.mgt.model.ApplicationRole;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.Error.INVALID_REQUEST;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.Error.UNEXPECTED_SERVER_ERROR;

/**
 * Application role management service.
 */
public class ApplicationRoleManagementService {

    /**
     * Add a new role to the application.
     *
     * @param applicationId Application ID.
     * @param role          Role.
     * @return Created role.
     */
    public Role addApplicationRole(String applicationId, RoleCreationModel role) {

        // Filter names of the permissions in role.getPermissions() to List[] permissions
        List<String> collect =
                role.getPermissions().stream().map(permission -> permission.getName()).collect(Collectors.toList());
        String roleId = UUID.randomUUID().toString();
        try {
            getApplicationRoleManager().addApplicationRole(
                    new ApplicationRole(roleId, role.getName(), collect.toArray(new String[0]),
                            applicationId));

            Role createdRole = new Role();
            createdRole.setId(roleId);
            createdRole.setName(role.getName());
            createdRole.setPermissions(role.getPermissions());
            return createdRole;
        } catch (ApplicationRoleManagementException e) {
            String msg = "Error while creating application role: " + role.getName() + " for application: " +
                    applicationId;
            throw handleApplicationRoleManagementException(e, msg);
        }
    }

    /**
     * Get a role by ID.
     *
     * @param applicationId Application ID.
     * @param roleId        Role ID.
     * @return Role.
     */
    public Role getApplicationRole(String applicationId, String roleId) {

        ApplicationRole applicationRole = null;
        try {
            applicationRole = getApplicationRoleManager().getApplicationRoleById(roleId);
            Role role = new Role();
            role.setId(roleId);
            role.setName(applicationRole.getRoleName());
            // TODO set permissions.
            return role;
        }
        catch (ApplicationRoleManagementException e) {
            String msg = "Error while retrieving application role with id: " + roleId;
            throw handleApplicationRoleManagementException(e, msg);
        }
    }

    /**
     * Get all roles of an application.
     *
     * @param applicationId Application ID.
     * @param before        Filter to get roles created before a given date. (optional)
     * @param after         Filter to get roles created after a given date. (optional)
     * @param limit         Maximum number of roles to return. (optional)
     * @param filter        Filter expression for filtering fields in the response. (optional)
     * @param sort          Sort expression for sorting the response. (optional)
     * @return List of roles.
     */
    public List<Role> getApplicationRoles(String applicationId, String before, String after, Integer limit,
                                          String filter, String sort) {

        List<ApplicationRole> applicationRoles = null;
        try {
            applicationRoles = getApplicationRoleManager().getApplicationRoles(applicationId);
            return applicationRoles.stream().map(applicationRole -> {
                Role role = new Role();
                role.setId(applicationRole.getRoleId());
                role.setName(applicationRole.getRoleName());
                // TODO set permissions.
                return role;
            }).collect(Collectors.toList());
        } catch (ApplicationRoleManagementException e) {
            String msg = "Error while retrieving application roles of application: " + applicationId;
            throw handleApplicationRoleManagementException(e, msg);
        }
    }

    /**
     * Delete a role by ID.
     *
     * @param applicationId Application ID.
     * @param roleId        Role ID.
     */
    public void deleteApplicationRole(String applicationId, String roleId) {

        try {
            getApplicationRoleManager().deleteApplicationRole(roleId);
        } catch (ApplicationRoleManagementException e) {
            String msg = "Error while deleting application role with id: " + roleId;
            throw handleApplicationRoleManagementException(e, msg);
        }
    }

    /**
     * Update a role by ID.
     *
     * @param applicationId Application ID.
     * @param roleId        Role ID.
     * @param roleUpdate    Role update.
     */
    public void updateApplicationRole(String applicationId, String roleId, RolePatchModel roleUpdate) {

        // TODO implement.
    }

    private ApplicationRoleManager getApplicationRoleManager() {

        return ApplicationManagementServiceHolder.getApplicationRoleManagerService();
    }

    private APIError handleApplicationRoleManagementException(ApplicationRoleManagementException e, String msg) {

        if (e instanceof ApplicationRoleManagementClientException) {
            throw buildClientError(e, msg);
        }
        throw buildServerError(e, msg);
    }

    private APIError buildServerError(ApplicationRoleManagementException e, String message) {

        String errorCode = getErrorCode(e, UNEXPECTED_SERVER_ERROR.getCode());
        return Utils.buildServerError(errorCode, message, e.getMessage(), e);
    }

    private APIError buildClientError(ApplicationRoleManagementException e, String message) {

        String errorCode = getErrorCode(e, INVALID_REQUEST.getCode());
        return Utils.buildClientError(errorCode, message, e.getMessage());
    }

    private String getErrorCode(ApplicationRoleManagementException e, String defaultErrorCode) {

        return e.getErrorCode() != null ? e.getErrorCode() : defaultErrorCode;
    }
}

