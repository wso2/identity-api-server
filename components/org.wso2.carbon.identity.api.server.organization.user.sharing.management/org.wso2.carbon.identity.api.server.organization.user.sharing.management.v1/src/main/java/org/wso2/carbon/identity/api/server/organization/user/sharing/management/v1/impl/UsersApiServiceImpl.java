/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.impl;

import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.UsersApiService;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.core.UsersApiServiceCore;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.factories.UsersApiServiceCoreFactory;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserShareRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserShareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserSharedRolesResponse;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserUnshareRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserUnshareWithAllRequestBody;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.exception.UserSharingMgtClientException;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.exception.UserSharingMgtException;

import java.util.UUID;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.RESPONSE_DETAIL_USER_SHARE;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.RESPONSE_DETAIL_USER_UNSHARE;
import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.RESPONSE_STATUS_PROCESSING;

/**
 * Implementation of the user sharing management APIs.
 */
public class UsersApiServiceImpl implements UsersApiService {

    private final UsersApiServiceCore usersApiServiceCore;

    public UsersApiServiceImpl() {

        try {
            this.usersApiServiceCore = UsersApiServiceCoreFactory.getUsersApiServiceCore();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating UsersApiService.", e);
        }
    }

    @Override
    public Response processUserSharing(UserShareRequestBody userShareRequestBody) {

        try {
            usersApiServiceCore.shareUser(userShareRequestBody);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(usersApiServiceCore.getProcessSuccessResponse(RESPONSE_STATUS_PROCESSING,
                            RESPONSE_DETAIL_USER_SHARE)).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    @Override
    public Response processUserSharingAll(UserShareWithAllRequestBody userShareWithAllRequestBody) {

        try {
            usersApiServiceCore.shareUserWithAll(userShareWithAllRequestBody);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(usersApiServiceCore.getProcessSuccessResponse(RESPONSE_STATUS_PROCESSING,
                            RESPONSE_DETAIL_USER_SHARE)).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    @Override
    public Response processUserUnsharing(UserUnshareRequestBody userUnshareRequestBody) {

        try {
            usersApiServiceCore.unshareUser(userUnshareRequestBody);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(usersApiServiceCore.getProcessSuccessResponse(RESPONSE_STATUS_PROCESSING,
                            RESPONSE_DETAIL_USER_UNSHARE)).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    @Override
    public Response removeUserSharing(UserUnshareWithAllRequestBody userUnshareWithAllRequestBody) {

        try {
            usersApiServiceCore.unshareUserWithAll(userUnshareWithAllRequestBody);
            return Response.status(Response.Status.ACCEPTED)
                    .entity(usersApiServiceCore.getProcessSuccessResponse(RESPONSE_STATUS_PROCESSING,
                            RESPONSE_DETAIL_USER_UNSHARE)).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    @Override
    public Response usersUserIdSharedOrganizationsGet(String userId, String after, String before, Integer limit,
                                                      String filter, Boolean recursive) {

        try {
            UserSharedOrganizationsResponse response = usersApiServiceCore.getSharedOrganizations(
                    userId, after, before, limit, filter, recursive);
            return Response.ok().entity(response).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    @Override
    public Response usersUserIdSharedRolesGet(String userId, String orgId, String after, String before, Integer limit,
                                              String filter, Boolean recursive) {

        try {
            UserSharedRolesResponse response = usersApiServiceCore.getSharedRoles(
                    userId, orgId, after, before, limit, filter, recursive);
            return Response.ok().entity(response).build();
        } catch (UserSharingMgtClientException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(e)).build();
        } catch (UserSharingMgtException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildErrorResponse(e)).build();
        }
    }

    /**
     * Builds a structured error response.
     */
    private Error buildErrorResponse(UserSharingMgtException e) {

        return new Error().code(e.getErrorCode()).code(e.getErrorCode()).message(e.getMessage())
                .description(e.getDescription()).traceId(UUID.randomUUID());
    }
}
