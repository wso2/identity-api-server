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
            this.serverActionManagementService = ActionManagementServiceFactory.getActionManagementService();
            if (LOG.isDebugEnabled()) {
                LOG.debug("ActionsApiService initialized successfully");
            }
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while initiating server action management service", e);
            throw new RuntimeException("Error occurred while initiating server action management service.", e);
        }
    }

    @Override
    public Response activateAction(String actionType, String actionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("REST API call to activate action: " + actionId + " of type: " + actionType);
        }
        return Response.ok().entity(serverActionManagementService.activateAction(actionType, actionId)).build();
    }

    @Override
    public Response createAction(String actionType, String body) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("REST API call to create action of type: " + actionType);
        }
        ActionResponse actionResponse = serverActionManagementService.createAction(actionType, body);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT +
                ActionMgtEndpointConstants.ACTION_PATH_COMPONENT + "/" + actionResponse.getId());
        return Response.created(location).entity(actionResponse).build();
    }

    @Override
    public Response deactivateAction(String actionType, String actionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("REST API call to deactivate action: " + actionId + " of type: " + actionType);
        }
        return Response.ok().entity(serverActionManagementService.deactivateAction(actionType, actionId)).build();
    }

    @Override
    public Response deleteAction(String actionType, String actionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("REST API call to delete action: " + actionId + " of type: " + actionType);
        }
        serverActionManagementService.deleteAction(actionType, actionId);
        return Response.noContent().build();
    }

    @Override
    public Response getActionByActionId(String actionType, String actionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("REST API call to get action: " + actionId + " of type: " + actionType);
        }
        return Response.ok().entity(serverActionManagementService.getActionByActionId(actionType, actionId)).build();
    }

    @Override
    public Response getActionTypes() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("REST API call to get action types");
        }
        return Response.ok().entity(serverActionManagementService.getActionTypes()).build();
    }

    @Override
    public Response getActionsByActionType(String actionType) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("REST API call to get actions by type: " + actionType);
        }
        return Response.ok().entity(serverActionManagementService.getActionsByActionType(actionType)).build();
    }

    @Override
    public Response updateAction(String actionType, String actionId, String body) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("REST API call to update action: " + actionId + " of type: " + actionType);
        }
        return Response.ok().entity(serverActionManagementService.updateAction(actionType, actionId, body)).build();
    }
}
