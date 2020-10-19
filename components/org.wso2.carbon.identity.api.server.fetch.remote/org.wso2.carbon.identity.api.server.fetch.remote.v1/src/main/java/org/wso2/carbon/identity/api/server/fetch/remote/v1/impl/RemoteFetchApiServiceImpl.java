/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.fetch.remote.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.RemoteFetchApiService;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.core.ServerRemoteFetchConfigManagementService;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.PushEventWebHookPOSTRequest;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationListResponse;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationPOSTRequest;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationPatchRequest;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.StatusListResponse;
import java.net.URI;
import javax.ws.rs.core.Response;

/**
 * Implementation of the Remote Fetch Rest API.
 */
public class RemoteFetchApiServiceImpl implements RemoteFetchApiService {

    @Autowired
    private ServerRemoteFetchConfigManagementService serverRemoteFetchConfigManagementService;

    @Override
    public Response addRemoteFetch(RemoteFetchConfigurationPOSTRequest remoteFetchConfigurationPOSTRequest) {

        String remoteFetchConfigurationId = serverRemoteFetchConfigManagementService
                .addRemoteFetchConfiguration(remoteFetchConfigurationPOSTRequest);
        return Response.created(getResourceLocation(remoteFetchConfigurationId)).build();
    }

    @Override
    public Response deleteRemoteFetch(String id) {

        serverRemoteFetchConfigManagementService.deleteRemoteFetchConfig(id);
        return Response.noContent().build();
    }

    @Override
    public Response getRemoteFetch(String id) {

        return Response.ok().entity(serverRemoteFetchConfigManagementService.getRemoteFetchConfig(id)).build();
    }

    @Override
    public Response getRemoteFetchConfigs() {

        RemoteFetchConfigurationListResponse remoteFetchConfigurationListResponse =
                serverRemoteFetchConfigManagementService.getRemoteFetchConfigs();
        return Response.ok().entity(remoteFetchConfigurationListResponse).build();
    }

    @Override
    public Response getStatus(String id) {

        StatusListResponse statusListResponse = serverRemoteFetchConfigManagementService.getStatus(id);
        return Response.ok().entity(statusListResponse).build();
    }

    @Override
    public Response handleWebHook(PushEventWebHookPOSTRequest pushEventWebHookPOSTRequest) {

        serverRemoteFetchConfigManagementService.handleWebHook(pushEventWebHookPOSTRequest);
        return Response.accepted().build();
    }

    @Override
    public Response triggerRemoteFetch(String id) {

        serverRemoteFetchConfigManagementService.triggerRemoteFetch(id);
        return Response.accepted().build();
    }

    @Override
    public Response updateRemoteFetch(String id,
                                      RemoteFetchConfigurationPatchRequest remoteFetchConfigurationPatchRequest) {

        serverRemoteFetchConfigManagementService.updateRemoteFetchConfig(id, remoteFetchConfigurationPatchRequest);
        return Response.ok().build();
    }

    private URI getResourceLocation(String resourceId) {

        return ContextLoader.buildURIForHeader(Constants.V1_API_PATH_COMPONENT +
                RemoteFetchConfigurationConstants.REMOTE_FETCH_CONFIGURATION_PATH_COMPONENT + "/" + resourceId);
    }
}
