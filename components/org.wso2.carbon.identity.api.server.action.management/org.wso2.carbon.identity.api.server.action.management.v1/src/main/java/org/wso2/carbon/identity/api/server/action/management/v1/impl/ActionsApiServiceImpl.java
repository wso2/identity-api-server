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

package org.wso2.carbon.identity.api.server.action.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionsApiService;
import org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.action.management.v1.core.ServerActionManagementService;
import org.wso2.carbon.identity.api.server.action.management.v1.factories.ActionManagementServiceFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;

import java.net.URI;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;

/**
 * Implementation of the Actions REST Api.
 */
public class ActionsApiServiceImpl implements ActionsApiService {

    private static final Log LOG = LogFactory.getLog(ActionsApiServiceImpl.class);
    private final ServerActionManagementService serverActionManagementService;

    public ActionsApiServiceImpl() {

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Initializing Actions API service implementation.");
            }
            this.serverActionManagementService = ActionManagementServiceFactory.getActionManagementService();
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while initiating server action management service.", e);
            throw new RuntimeException("Error occurred while initiating server action management service.", e);
        }
    }

    @Override
    public Response activateAction(String actionType, String actionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Activating action with type: " + actionType + " and id: " + actionId);
        }
        return Response.ok().entity(serverActionManagementService.activateAction(actionType, actionId)).build();
    }

    @Override
    public Response createAction(String actionType, String body) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating action with type: " + actionType);
        }
        ActionResponse actionResponse = serverActionManagementService.createAction(actionType, body);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT +
                ActionMgtEndpointConstants.ACTION_PATH_COMPONENT + "/" + actionResponse.getId());
        LOG.info("Action created successfully with id: " + actionResponse.getId());
        return Response.created(location).entity(actionResponse).build();
    }

    @Override
    public Response deactivateAction(String actionType, String actionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deactivating action with type: " + actionType + " and id: " + actionId);
        }
        return Response.ok().entity(serverActionManagementService.deactivateAction(actionType, actionId)).build();
    }

    @Override
    public Response deleteAction(String actionType, String actionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting action with type: " + actionType + " and id: " + actionId);
        }
        serverActionManagementService.deleteAction(actionType, actionId);
        LOG.info("Action deleted successfully with id: " + actionId);
        return Response.noContent().build();
    }

    @Override
    public Response getActionByActionId(String actionType, String actionId) {

        return Response.ok().entity(serverActionManagementService.getActionByActionId(actionType, 
                actionId)).build();
    }

    @Override
    public Response getActionTypes() {

        return Response.ok().entity(serverActionManagementService.getActionTypes()).build();
    }

    @Override
    public Response getActionsByActionType(String actionType) {

        return Response.ok().entity(serverActionManagementService.getActionsByActionType(actionType)).build();
    }

    @Override
    public Response updateAction(String actionType, String actionId, String body) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating action with type: " + actionType + " and id: " + actionId);
        }
        ActionResponse response = serverActionManagementService.updateAction(actionType, actionId, body);
        LOG.info("Action updated successfully with id: " + actionId);
        return Response.ok().entity(response).build();
    }
}
