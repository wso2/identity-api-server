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

package org.wso2.carbon.identity.api.server.organization.role.management.v1.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.constant.RoleManagementEndpointConstants;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RoleGetResponse;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RoleGetResponseGroup;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RoleGetResponseUser;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RoleObj;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RoleObjMeta;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePatchOperation;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePatchRequest;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePatchResponse;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePostRequest;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePostRequestGroup;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePostRequestUser;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePostResponse;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePutRequest;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePutRequestGroup;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePutRequestUser;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePutResponse;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePutResponseMeta;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolesListResponse;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.util.RoleManagementEndpointUtils;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.organization.management.role.management.service.RoleManager;
import org.wso2.carbon.identity.organization.management.role.management.service.models.Group;
import org.wso2.carbon.identity.organization.management.role.management.service.models.PatchOperation;
import org.wso2.carbon.identity.organization.management.role.management.service.models.Role;
import org.wso2.carbon.identity.organization.management.role.management.service.models.RolesResponse;
import org.wso2.carbon.identity.organization.management.role.management.service.models.User;
import org.wso2.carbon.identity.organization.management.service.OrganizationUserResidentResolverService;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementClientException;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ERROR_BUILDING_GROUP_URI;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ERROR_BUILDING_ROLE_URI;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ERROR_BUILDING_USER_URI;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ERROR_WHILE_RESOLVING_USER_FROM_RESIDENT_ORG;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_PATCH_VALUE_EMPTY;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ROLE_DISPLAY_NAME_NULL;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_USER_ROOT_ORGANIZATION_NOT_FOUND;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.PATCH_OP_ADD;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.PATCH_OP_REMOVE;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.PATCH_OP_REPLACE;
import static org.wso2.carbon.identity.organization.management.service.util.Utils.handleClientException;

/**
 * The service class for Role Management in Organization Management.
 */
public class RoleManagementService {

    private final RoleManager roleManager;
    private final OrganizationUserResidentResolverService organizationUserResidentResolverService;

    private static final Log LOG = LogFactory.getLog(RoleManagementService.class);

    public RoleManagementService(RoleManager roleManager,
                                 OrganizationUserResidentResolverService organizationUserResidentResolverService) {

        this.roleManager = roleManager;
        this.organizationUserResidentResolverService = organizationUserResidentResolverService;
    }

    /**
     * Service for creating a role inside an organization.
     *
     * @param organizationId  The ID of the organization.
     * @param rolePostRequest The object created from request body.
     * @return The role creation response.
     */
    public Response createRole(String organizationId, RolePostRequest rolePostRequest) {

        try {
            Role role = roleManager.createRole(organizationId,
                    generateRoleFromPostRequest(rolePostRequest));
            URI roleURI = RoleManagementEndpointUtils.getUri(organizationId, role.getId(),
                    RoleManagementEndpointConstants.ROLE_PATH,
                    ERROR_CODE_ERROR_BUILDING_ROLE_URI);
            return Response.created(roleURI).entity(getRolePostResponse(role, roleURI)).build();
        } catch (OrganizationManagementClientException e) {
            return RoleManagementEndpointUtils.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return RoleManagementEndpointUtils.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Service for deleting a role inside an organization.
     *
     * @param organizationId The ID of the organization.
     * @param roleId         The ID of the role.
     * @return The role deletion response.
     */
    public Response deleteRole(String organizationId, String roleId) {

        try {
            roleManager.deleteRole(organizationId, roleId);
            return Response.noContent().build();
        } catch (OrganizationManagementClientException e) {
            return RoleManagementEndpointUtils.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return RoleManagementEndpointUtils.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Service for getting a role using role ID and organization ID.
     *
     * @param organizationId The ID of the organization.
     * @param roleId         The ID of the role.
     * @return The role corresponding to roleId and organizationId.
     */
    public Response getRoleUsingOrganizationIdAndRoleId(String organizationId, String roleId) {

        try {
            Role role = roleManager.getRoleById(organizationId, roleId);
            URI roleURI = RoleManagementEndpointUtils.getUri(organizationId, roleId,
                    RoleManagementEndpointConstants.ROLE_PATH,
                    ERROR_CODE_ERROR_BUILDING_ROLE_URI);
            return Response.ok().entity(getRoleGetResponse(organizationId, role, roleURI)).build();
        } catch (OrganizationManagementClientException e) {
            return RoleManagementEndpointUtils.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return RoleManagementEndpointUtils.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Get the roles inside an organization.
     *
     * @param organizationId The ID of the organization.
     * @param filter         Param for filtering the results.
     * @param count          Param for desired maximum number of query results per page.
     * @param cursor         Param for cursor to fetch the next page of results.
     * @return The roles inside an organization.
     */
    public Response getRolesOfOrganization(String organizationId, String filter, Integer count, String cursor) {

        try {
            int limitValue = validateCount(count);
            RolesResponse rolesResponse = roleManager.getOrganizationRoles(limitValue, filter, organizationId, cursor);
            return Response.ok().entity(getRoleListResponse(organizationId, rolesResponse)).build();
        } catch (OrganizationManagementClientException e) {
            return RoleManagementEndpointUtils.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return RoleManagementEndpointUtils.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Get the user roles inside an organization.
     *
     * @param organizationId The ID of the organization.
     * @param userId         The ID of the user.
     * @return The user roles inside an organization.
     */
    public Response getUserRolesOfOrganization(String organizationId, String userId) {

        try {
            String userResidentOrgId = String.valueOf(organizationUserResidentResolverService
                    .resolveResidentOrganization(userId, organizationId)
                    .orElseThrow(() -> handleClientException(ERROR_CODE_USER_ROOT_ORGANIZATION_NOT_FOUND, userId)));
            if (!StringUtils.equals(userResidentOrgId, organizationId)) {
                throw handleClientException
                        (ERROR_CODE_ERROR_WHILE_RESOLVING_USER_FROM_RESIDENT_ORG, userResidentOrgId, organizationId);
            }

            List<Role> userRolesResponse = roleManager.getUserOrganizationRoles(userId, organizationId);
            return Response.ok().entity(getUserRoleListResponse(organizationId, userRolesResponse)).build();
        } catch (OrganizationManagementClientException e) {
            return RoleManagementEndpointUtils.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return RoleManagementEndpointUtils.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Service for patching a role using role ID and organization ID.
     *
     * @param organizationId   The ID of the organization.
     * @param roleId           The ID of the role.
     * @param rolePatchRequest The request object created using request body.
     * @return The role patch response.
     */
    public Response patchRole(String organizationId, String roleId, RolePatchRequest rolePatchRequest) {

        try {
            List<RolePatchOperation> patchOperationList = rolePatchRequest.getOperations();
            List<PatchOperation> patchOperations = new ArrayList<>();

            for (RolePatchOperation rolePatchOperation : patchOperationList) {
                List<String> values = rolePatchOperation.getValue();
                String patchOp = rolePatchOperation.getOp().toString();
                if (StringUtils.equalsIgnoreCase(patchOp, PATCH_OP_REMOVE)) {
                    PatchOperation patchOperation = new PatchOperation(StringUtils.strip(rolePatchOperation.getOp()
                            .toString()), StringUtils.strip(rolePatchOperation.getPath()));
                    patchOperations.add(patchOperation);
                } else if ((CollectionUtils.isNotEmpty(values) && StringUtils.equalsIgnoreCase(patchOp, PATCH_OP_ADD))
                        || StringUtils.equalsIgnoreCase(patchOp, PATCH_OP_REPLACE)) {
                    PatchOperation patchOperation = new PatchOperation(StringUtils.strip(rolePatchOperation.getOp()
                            .toString()), StringUtils.strip(rolePatchOperation.getPath()), values);
                    patchOperations.add(patchOperation);
                } else {
                    // Invalid patch operations cannot be sent due to swagger validation.
                    // But, if values are not passed along with ADD operations, an error is thrown.
                    throw handleClientException(ERROR_CODE_PATCH_VALUE_EMPTY);
                }
            }
            Role role = roleManager.patchRole(organizationId, roleId, patchOperations);
            URI roleURI = RoleManagementEndpointUtils.getUri(organizationId, roleId,
                    RoleManagementEndpointConstants.ROLE_PATH,
                    ERROR_CODE_ERROR_BUILDING_ROLE_URI);
            return Response.ok().entity(getRolePatchResponse(role, roleURI)).build();
        } catch (OrganizationManagementClientException e) {
            return RoleManagementEndpointUtils.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return RoleManagementEndpointUtils.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Patching a role using PUT request.
     *
     * @param organizationId The organization ID.
     * @param roleId         The role ID.
     * @param rolePutRequest The request object created using request body.
     * @return Put role response.
     */
    public Response putRole(String organizationId, String roleId, RolePutRequest rolePutRequest) {

        try {
            if (StringUtils.isBlank(rolePutRequest.getDisplayName())) {
                throw handleClientException(ERROR_CODE_ROLE_DISPLAY_NAME_NULL);
            }
            String displayName = rolePutRequest.getDisplayName();
            List<RolePutRequestUser> users = rolePutRequest.getUsers();
            List<RolePutRequestGroup> groups = rolePutRequest.getGroups();
            List<String> permissions = rolePutRequest.getPermissions();

            Role role = roleManager.putRole(organizationId, roleId,
                    new Role(roleId, displayName,
                            (groups == null ? Collections.emptyList() : groups.stream().map(group ->
                                    new Group(group.getValue())).collect(Collectors.toList())),
                            (users == null ? Collections.emptyList() : users.stream().map(user ->
                                    new User(user.getValue())).collect(Collectors.toList())),
                            permissions));
            URI roleURI = RoleManagementEndpointUtils.getUri(organizationId, roleId,
                    RoleManagementEndpointConstants.ROLE_PATH,
                    ERROR_CODE_ERROR_BUILDING_ROLE_URI);

            return Response.ok().entity(getRolePutResponse(role, roleURI)).build();
        } catch (OrganizationManagementClientException e) {
            return RoleManagementEndpointUtils.handleClientErrorResponse(e, LOG);
        } catch (OrganizationManagementException e) {
            return RoleManagementEndpointUtils.handleServerErrorResponse(e, LOG);
        }
    }

    /**
     * Generating a Role object from the RoleRequest.
     *
     * @param rolePostRequest The request object coming from the API.
     * @return A role object.
     */
    private Role generateRoleFromPostRequest(RolePostRequest rolePostRequest) {

        Role role = new Role();
        role.setDisplayName(StringUtils.strip(rolePostRequest.getDisplayName()));
        if (CollectionUtils.isNotEmpty(rolePostRequest.getUsers())) {
            role.setUsers(rolePostRequest.getUsers().stream().map(RolePostRequestUser::getValue)
                    .map(User::new).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(rolePostRequest.getGroups())) {
            role.setGroups(rolePostRequest.getGroups().stream().map(RolePostRequestGroup::getValue)
                    .map(Group::new).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(rolePostRequest.getPermissions())) {
            role.setPermissions(rolePostRequest.getPermissions());
        }

        return role;
    }

    /**
     * Generating a RolePostResponse object for the response.
     *
     * @param role    A role object.
     * @param roleURI The URI of the role.
     * @return A RolePostResponse.
     */
    private RolePostResponse getRolePostResponse(Role role, URI roleURI) {

        RoleObjMeta roleObjMeta = new RoleObjMeta();
        roleObjMeta.location(roleURI.toString());

        RolePostResponse response = new RolePostResponse();
        response.setId(role.getId());
        response.setDisplayName(role.getDisplayName());
        response.setMeta(roleObjMeta);

        return response;
    }

    /**
     * Generating  RoleGetResponse for the response.
     *
     * @param organizationId The ID of the organization.
     * @param role           A role object.
     * @param roleURI        The URI of the role.
     * @return A RoleGetResponse.
     */
    private RoleGetResponse getRoleGetResponse(String organizationId, Role role, URI roleURI) {

        RoleObjMeta roleObjMeta = new RoleObjMeta();
        roleObjMeta.location(roleURI.toString());

        RoleGetResponse response = new RoleGetResponse();
        response.setId(role.getId());
        response.setDisplayName(role.getDisplayName());
        response.setMeta(roleObjMeta);
        response.setPermissions(role.getPermissions());

        if (CollectionUtils.isNotEmpty(role.getGroups())) {
            response.setGroups(getGroupsForResponseObject(role.getGroups(), organizationId));
        }

        if (CollectionUtils.isNotEmpty(role.getUsers())) {
            response.setUsers(getUsersForResponseObject(role.getUsers(), organizationId));
        }

        return response;
    }

    /**
     * Set the groups for the response if they exist.
     *
     * @param roleGroups     The groups assigned to a role.
     * @param organizationId The organizationId.
     * @return The RoleGetResponseGroup list.
     */
    private List<RoleGetResponseGroup> getGroupsForResponseObject(List<Group> roleGroups, String organizationId) {

        List<RoleGetResponseGroup> groups = new ArrayList<>();
        for (Group basicGroup : roleGroups) {
            RoleGetResponseGroup group = new RoleGetResponseGroup();
            group.value(basicGroup.getGroupId());
            group.display(basicGroup.getGroupName());
            group.$ref(RoleManagementEndpointUtils.getUri(organizationId, basicGroup.getGroupId(),
                    RoleManagementEndpointConstants.GROUP_PATH,
                    ERROR_CODE_ERROR_BUILDING_GROUP_URI).toString());
            groups.add(group);
        }
        return groups;
    }

    /**
     * Set the users for the response if they exist.
     *
     * @param roleUsers      The users assigned to a role.
     * @param organizationId The organizationId.
     * @return The RoleGetResponseUser list.
     */
    private List<RoleGetResponseUser> getUsersForResponseObject(List<User> roleUsers, String organizationId) {

        List<RoleGetResponseUser> users = new ArrayList<>();
        for (User basicUser : roleUsers) {
            String uri;
            if (StringUtils.isNotBlank(basicUser.getUserResidentOrgId())) {
                uri = RoleManagementEndpointUtils.buildSCIM2Uri(basicUser.getId(),
                        RoleManagementEndpointConstants.SCIM_USER_PATH,
                        ERROR_CODE_ERROR_BUILDING_USER_URI).toString();
                uri = uri.replace(organizationId, basicUser.getUserResidentOrgId());
            } else {
                uri = RoleManagementEndpointUtils.getUri(organizationId, basicUser.getId(),
                        RoleManagementEndpointConstants.USER_PATH,
                        ERROR_CODE_ERROR_BUILDING_USER_URI).toString();
            }

            RoleGetResponseUser user = new RoleGetResponseUser();
            user.value(basicUser.getId());
            user.display(basicUser.getUserName());
            user.$ref(uri);
            user.orgId(basicUser.getUserResidentOrgId());
            user.orgName(basicUser.getUserResidentOrgName());
            users.add(user);
        }
        return users;
    }

    /**
     * Generate a response object for patch operation.
     *
     * @param role    The role which needed to be included in the response.
     * @param roleURI The URI of the role.
     * @return A RolePatchResponse.
     */
    private RolePatchResponse getRolePatchResponse(Role role, URI roleURI) {

        RolePutResponseMeta roleObjMeta = new RolePutResponseMeta();
        roleObjMeta.setLocation(roleURI.toString());

        RolePatchResponse response = new RolePatchResponse();
        response.setDisplayName(role.getDisplayName());
        response.setMeta(roleObjMeta);
        response.setId(role.getId());

        return response;
    }

    /**
     * Generate a response object for put operation.
     *
     * @param role    The role which needed to be included in the response.
     * @param roleURI The URI of the role.
     * @return A RolePutResponse.
     */
    private RolePutResponse getRolePutResponse(Role role, URI roleURI) {

        RolePutResponseMeta roleObjMeta = new RolePutResponseMeta();
        roleObjMeta.setLocation(roleURI.toString());

        RolePutResponse responseObject = new RolePutResponse();
        responseObject.setDisplayName(role.getDisplayName());
        responseObject.setMeta(roleObjMeta);
        responseObject.setValue(role.getId());

        return responseObject;
    }

    /**
     * Generate a response object for get operation.
     *
     * @param organizationId The ID of the organization.
     * @param rolesResponse  Roles response including list of roles.
     * @return The RoleListResponse.
     */
    private RolesListResponse getRoleListResponse(String organizationId, RolesResponse rolesResponse) {

        RolesListResponse response = new RolesListResponse();
        response.setNextCursor(rolesResponse.getNextCursor());
        response.setPreviousCursor(rolesResponse.getPreviousCursor());
        response.setItemsPerPage(rolesResponse.getItemsPerPage());
        response.setTotalResults(rolesResponse.getTotalResults());

        if (rolesResponse.getRoles() != null) {
            List<RoleObj> roleDTOs = new ArrayList<>();
            for (Role role : rolesResponse.getRoles()) {
                RoleObj roleObj = new RoleObj();
                RoleObjMeta roleObjMeta = new RoleObjMeta();
                roleObjMeta.setLocation(RoleManagementEndpointUtils.getUri(organizationId, role.getId(),
                        RoleManagementEndpointConstants.ROLE_PATH,
                        ERROR_CODE_ERROR_BUILDING_ROLE_URI).toString());
                roleObj.setId(role.getId());
                roleObj.setDisplayName(role.getDisplayName());
                roleObj.setMeta(roleObjMeta);
                roleDTOs.add(roleObj);
            }
            response.setResources(roleDTOs);
        }
        return response;
    }

    /**
     * Generate a response object for get operation.
     *
     * @param organizationId    The ID of the organization.
     * @param userRolesResponse List of user roles.
     * @return The UserRolesListResponse.
     */
    private List<RoleObj> getUserRoleListResponse(String organizationId, List<Role> userRolesResponse) {

        List<RoleObj> roleDTOs = new ArrayList<>();
        for (Role role : userRolesResponse) {
            RoleObj roleObj = new RoleObj();
            RoleObjMeta roleObjMeta = new RoleObjMeta();
            roleObjMeta.setLocation(RoleManagementEndpointUtils.getUri(organizationId, role.getId(),
                    RoleManagementEndpointConstants.ROLE_PATH,
                    ERROR_CODE_ERROR_BUILDING_ROLE_URI).toString());
            roleObj.setId(role.getId());
            roleObj.setDisplayName(role.getDisplayName());
            roleObj.setMeta(roleObjMeta);
            roleDTOs.add(roleObj);
        }
        return roleDTOs;
    }

    /**
     * @param count The param for desired maximum number of query results per page.
     * @return The count.
     */
    private int validateCount(Integer count) {

        if (count == null) {
            int defaultItemsPerPage = IdentityUtil.getDefaultItemsPerPage();
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Given count is null. Therefore the default count is set to %s.",
                        defaultItemsPerPage));
            }
            return defaultItemsPerPage;
        }

        if (count < 0) {
            count = 0;
        }

        int maximumItemsPerPage = IdentityUtil.getMaximumItemPerPage();
        if (count > maximumItemsPerPage) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Given count exceeds the maximum count. Therefore the count is set to %s.",
                        maximumItemsPerPage));
            }
            return maximumItemsPerPage;
        }
        return count;
    }
}
