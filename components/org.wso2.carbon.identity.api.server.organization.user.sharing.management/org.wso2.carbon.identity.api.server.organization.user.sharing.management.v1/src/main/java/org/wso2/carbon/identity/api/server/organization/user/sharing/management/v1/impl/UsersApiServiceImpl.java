/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.*;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.*;
import java.util.List;

import javax.ws.rs.core.Response;

public class UsersApiServiceImpl implements UsersApiService {

    @Override
    public Response processUserSharing(UserShareRequestBody userShareRequestBody) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response processUserSharingAll(UserShareWithAllRequestBody userShareWithAllRequestBody) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response processUserUnsharing(UserUnshareRequestBody userUnshareRequestBody) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response removeUserSharing(UserUnshareWithAllRequestBody userUnshareWithAllRequestBody) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response usersUserIdSharedOrganizationsGet(String userId, String after, String before, Integer limit, String filter, Boolean recursive) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response usersUserIdSharedRolesGet(String userId, String orgId, String after, String before, Integer limit, String filter, Boolean recursive) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }
}
