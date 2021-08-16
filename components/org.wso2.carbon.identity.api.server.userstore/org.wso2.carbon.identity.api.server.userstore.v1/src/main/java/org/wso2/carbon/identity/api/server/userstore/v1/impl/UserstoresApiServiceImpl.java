/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.userstore.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.userstore.v1.UserstoresApiService;
import org.wso2.carbon.identity.api.server.userstore.v1.core.ServerUserStoreService;
import org.wso2.carbon.identity.api.server.userstore.v1.model.ClaimAttributeMapping;
import org.wso2.carbon.identity.api.server.userstore.v1.model.PatchDocument;
import org.wso2.carbon.identity.api.server.userstore.v1.model.RDBMSConnectionReq;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreReq;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreResponse;

import java.net.URI;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForHeader;
import static org.wso2.carbon.identity.api.server.userstore.common.UserStoreConstants.USER_STORE_PATH_COMPONENT;

/**
 * User Store API Service Implementation
 */
public class UserstoresApiServiceImpl implements UserstoresApiService {

    @Autowired
    private ServerUserStoreService serverUserStoreService;

    @Override
    public Response addUserStore(UserStoreReq userStoreReq) {

        UserStoreResponse response = serverUserStoreService.addUserStore(userStoreReq);
        return Response.created(getResourceLocation(response.getId())).entity(response).build();
    }

    @Override
    public Response deleteUserStore(String userstoreDomainId) {

        serverUserStoreService.deleteUserStore(userstoreDomainId);
        return Response.noContent().build();
    }

    @Override
    public Response getAvailableUserStoreTypes() {

        return Response.ok().entity(serverUserStoreService.getAvailableUserStoreTypes()).build();
    }

    @Override
    public Response getPrimaryUserStore() {

        return Response.ok().entity(serverUserStoreService.getPrimaryUserStore()).build();
    }

    @Override
    public Response getSecondaryUserStores(Integer limit, Integer offset, String filter, String sort,
                                           String requiredAttributes) {

        return Response.ok()
                .entity(serverUserStoreService.getUserStoreList(limit, offset, filter, sort, requiredAttributes))
                .build();
    }

    @Override
    public Response getUserStoreByDomainId(String userstoreDomainId) {

        return Response.ok().entity(serverUserStoreService.getUserStoreByDomainId(userstoreDomainId)).build();
    }

    @Override
    public Response getUserStoreManagerProperties(String typeId) {

        return Response.ok().entity(serverUserStoreService.getUserStoreManagerProperties(typeId)).build();
    }

    @Override
    public Response updateAttributeMappings(String userstoreDomainId,
                                           List<ClaimAttributeMapping> claimAttributeMappings) {
        serverUserStoreService.updateClaimAttributeMappings(userstoreDomainId,
                claimAttributeMappings);
        return Response.ok().build();
    }

    @Override
    public Response patchUserStore(String userstoreDomainId, List<PatchDocument> patchDocument) {

        return Response.ok().entity(serverUserStoreService.patchUserStore(userstoreDomainId, patchDocument)).build();
    }

    @Override
    public Response testRDBMSConnection(RDBMSConnectionReq rdBMSConnectionReq) {

        return Response.ok().entity(serverUserStoreService.testRDBMSConnection(rdBMSConnectionReq)).build();
    }

    @Override
    public Response updateUserStore(String userstoreDomainId, UserStoreReq userStoreReq) {

        return Response.ok().entity(serverUserStoreService.editUserStore(userstoreDomainId, userStoreReq)).build();
    }

    private URI getResourceLocation(String id) {

        return buildURIForHeader(String.format(V1_API_PATH_COMPONENT + USER_STORE_PATH_COMPONENT + "/%s", id));
    }
}
