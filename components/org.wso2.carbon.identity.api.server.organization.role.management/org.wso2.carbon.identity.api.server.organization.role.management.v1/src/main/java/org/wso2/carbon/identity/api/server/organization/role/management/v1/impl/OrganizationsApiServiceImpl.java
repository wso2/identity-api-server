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

package org.wso2.carbon.identity.api.server.organization.role.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.OrganizationsApiService;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.factories.RoleManagementServiceFactory;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePatchRequest;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePostRequest;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePutRequest;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.service.RoleManagementService;

import javax.ws.rs.core.Response;

/**
 * Implementation of the RolesApi Service.
 */
public class OrganizationsApiServiceImpl implements OrganizationsApiService {

    private static final Log LOG = LogFactory.getLog(OrganizationsApiServiceImpl.class);
    private final RoleManagementService roleManagementService;

    public OrganizationsApiServiceImpl() {

        try {
            this.roleManagementService = RoleManagementServiceFactory.getRoleManagementService();
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while initiating role management service", e);
            throw new RuntimeException("Error occurred while initiating role management service.", e);
        }
    }

    @Override
    public Response createRole(String organizationId, RolePostRequest rolePostRequest) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating role for organization: " + organizationId);
        }
        return roleManagementService.createRole(organizationId, rolePostRequest);
    }

    @Override
    public Response organizationsOrganizationIdRolesGet(String organizationId, String filter, Integer count,
                                                        String cursor) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving roles for organization: " + organizationId);
        }
        return roleManagementService.getRolesOfOrganization(organizationId, filter, count, cursor);
    }

    @Override
    public Response organizationsOrganizationIdRolesRoleIdDelete(String roleId, String organizationId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting role: " + roleId + " from organization: " + organizationId);
        }
        return roleManagementService.deleteRole(organizationId, roleId);
    }

    @Override
    public Response organizationsOrganizationIdRolesRoleIdGet(String roleId, String organizationId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving role: " + roleId + " from organization: " + organizationId);
        }
        return roleManagementService.getRoleUsingOrganizationIdAndRoleId(organizationId, roleId);
    }

    @Override
    public Response organizationsOrganizationIdRolesRoleIdPatch(String roleId, String organizationId,
                                                                RolePatchRequest rolePatchRequest) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Patching role: " + roleId + " in organization: " + organizationId);
        }
        return roleManagementService.patchRole(organizationId, roleId, rolePatchRequest);
    }

    @Override
    public Response organizationsOrganizationIdRolesRoleIdPut(String roleId, String organizationId,
                                                              RolePutRequest rolePutRequest) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating role: " + roleId + " in organization: " + organizationId);
        }
        return roleManagementService.putRole(organizationId, roleId, rolePutRequest);
    }

    @Override
    public Response organizationsOrganizationIdUsersUserIdRolesGet(String userId, String organizationId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving roles for user: " + userId + " in organization: " + organizationId);
        }
        return roleManagementService.getUserRolesOfOrganization(organizationId, userId);
    }
}
