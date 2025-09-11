/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.fetch.remote.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.RemoteFetchApiService;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.core.ServerRemoteFetchConfigManagementService;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.factories.ServerRemoteFetchConfigManagementServiceFactory;
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

    private static final Log log = LogFactory.getLog(RemoteFetchApiServiceImpl.class);
    private final ServerRemoteFetchConfigManagementService serverRemoteFetchConfigManagementService;

    public RemoteFetchApiServiceImpl() {

        try {
            this.serverRemoteFetchConfigManagementService = ServerRemoteFetchConfigManagementServiceFactory
                    .getServerRemoteFetchConfigManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating ServerRemoteFetchConfigManagementService.", e);
        }
    }

    @Override
    public Response addRemoteFetch(RemoteFetchConfigurationPOSTRequest remoteFetchConfigurationPOSTRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Adding remote fetch configuration with name: " + 
                    (remoteFetchConfigurationPOSTRequest != null ? 
                    remoteFetchConfigurationPOSTRequest.getRemoteFetchName() : "null"));
        }
        String remoteFetchConfigurationId = serverRemoteFetchConfigManagementService
                .addRemoteFetchConfiguration(remoteFetchConfigurationPOSTRequest);
        log.info("Remote fetch configuration added successfully with ID: " + remoteFetchConfigurationId);
        return Response.created(getResourceLocation(remoteFetchConfigurationId)).build();
    }

    @Override
    public Response deleteRemoteFetch(String id) {

        if (log.isDebugEnabled()) {
            log.debug("Deleting remote fetch configuration with ID: " + id);
        }
        serverRemoteFetchConfigManagementService.deleteRemoteFetchConfig(id);
        log.info("Remote fetch configuration deleted successfully with ID: " + id);
        return Response.noContent().build();
    }

    @Override
    public Response getRemoteFetch(String id) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving remote fetch configuration with ID: " + id);
        }
        return Response.ok().entity(serverRemoteFetchConfigManagementService.getRemoteFetchConfig(id)).build();
    }

    @Override
    public Response getRemoteFetchConfigs() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving all remote fetch configurations.");
        }
        RemoteFetchConfigurationListResponse remoteFetchConfigurationListResponse =
                serverRemoteFetchConfigManagementService.getRemoteFetchConfigs();
        return Response.ok().entity(remoteFetchConfigurationListResponse).build();
    }

    @Override
    public Response getStatus(String id) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving status for remote fetch configuration with ID: " + id);
        }
        StatusListResponse statusListResponse = serverRemoteFetchConfigManagementService.getStatus(id);
        return Response.ok().entity(statusListResponse).build();
    }

    @Override
    public Response handleWebHook(PushEventWebHookPOSTRequest pushEventWebHookPOSTRequest) {

        log.info("Received webhook request for remote fetch configuration.");
        if (log.isDebugEnabled()) {
            log.debug("Processing webhook request from repository: " + 
                    (pushEventWebHookPOSTRequest != null && pushEventWebHookPOSTRequest.getRepository() != null ? 
                    pushEventWebHookPOSTRequest.getRepository().getCloneUrl() : "unknown"));
        }
        serverRemoteFetchConfigManagementService.handleWebHook(pushEventWebHookPOSTRequest);
        log.info("Webhook request processed successfully.");
        return Response.accepted().build();
    }

    @Override
    public Response triggerRemoteFetch(String id) {

        log.info("Triggering remote fetch for configuration with ID: " + id);
        serverRemoteFetchConfigManagementService.triggerRemoteFetch(id);
        log.info("Remote fetch triggered successfully for configuration with ID: " + id);
        return Response.accepted().build();
    }

    @Override
    public Response updateRemoteFetch(String id,
                                      RemoteFetchConfigurationPatchRequest remoteFetchConfigurationPatchRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Updating remote fetch configuration with ID: " + id);
        }
        serverRemoteFetchConfigManagementService.updateRemoteFetchConfig(id, remoteFetchConfigurationPatchRequest);
        log.info("Remote fetch configuration updated successfully with ID: " + id);
        return Response.ok().build();
    }

    private URI getResourceLocation(String resourceId) {

        return ContextLoader.buildURIForHeader(Constants.V1_API_PATH_COMPONENT +
                RemoteFetchConfigurationConstants.REMOTE_FETCH_CONFIGURATION_PATH_COMPONENT + "/" + resourceId);
    }
}
