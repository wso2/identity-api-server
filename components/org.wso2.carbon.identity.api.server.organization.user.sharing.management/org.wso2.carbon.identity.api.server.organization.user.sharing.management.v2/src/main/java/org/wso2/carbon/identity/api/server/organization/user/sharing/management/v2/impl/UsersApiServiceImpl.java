/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.UsersApiService;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.core.UsersApiServiceCore;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.factories.UsersApiServiceCoreFactory;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserShareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserSharingPatchRequest;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserUnshareAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserUnshareSelectedRequestBody;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.constants.UserSharingMgtConstants.ErrorMessage.ERROR_INITIATING_USERS_API_SERVICE;

/**
 * Implementation of the user sharing management APIs V2.
 */
public class UsersApiServiceImpl implements UsersApiService {

    private static final Log LOG = LogFactory.getLog(UsersApiServiceImpl.class);
    private final UsersApiServiceCore usersApiServiceCore;

    public UsersApiServiceImpl() {

        try {
            this.usersApiServiceCore = UsersApiServiceCoreFactory.getUsersApiServiceCore();
            LOG.info("UsersApiServiceImpl V2 initialized successfully.");
        } catch (IllegalStateException e) {
            LOG.error("Failed to initialize UsersApiServiceImpl: " + e.getMessage());
            throw new RuntimeException(ERROR_INITIATING_USERS_API_SERVICE.getMessage(), e);
        }
    }

    @Override
    public Response getUserSharedOrganizations(String userId, String before, String after, String filter, Integer limit,
                                               Boolean recursive, String attributes) {

        return usersApiServiceCore.getUserSharedOrganizations(userId, before, after, filter, limit, recursive,
                attributes);
    }

    @Override
    public Response patchUserSharing(UserSharingPatchRequest userSharingPatchRequest) {

        return usersApiServiceCore.patchUserSharing(userSharingPatchRequest);
    }

    @Override
    public Response shareUsersWithAll(UserShareAllRequestBody userShareAllRequestBody) {

        return usersApiServiceCore.shareUsersWithAllOrgs(userShareAllRequestBody);
    }

    @Override
    public Response shareUsersWithSelected(UserShareSelectedRequestBody userShareSelectedRequestBody) {

        return usersApiServiceCore.shareUsersWithSelectedOrgs(userShareSelectedRequestBody);
    }

    @Override
    public Response unshareUsersFromAll(UserUnshareAllRequestBody userUnshareAllRequestBody) {

        return usersApiServiceCore.unshareUsersFromAllOrgs(userUnshareAllRequestBody);
    }

    @Override
    public Response unshareUsersFromSelected(UserUnshareSelectedRequestBody userUnshareSelectedRequestBody) {

        return usersApiServiceCore.unshareUsersFromSelectedOrgs(userUnshareSelectedRequestBody);
    }
}
