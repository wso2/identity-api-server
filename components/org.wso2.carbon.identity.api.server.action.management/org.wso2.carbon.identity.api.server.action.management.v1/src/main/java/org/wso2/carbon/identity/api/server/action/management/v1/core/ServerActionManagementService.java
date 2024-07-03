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

package org.wso2.carbon.identity.api.server.action.management.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.action.mgt.exception.ActionMgtException;
import org.wso2.carbon.identity.action.mgt.model.Action;
import org.wso2.carbon.identity.action.mgt.model.AuthType;
import org.wso2.carbon.identity.action.mgt.model.EndpointConfig;
import org.wso2.carbon.identity.action.mgt.model.TypeEnums;
import org.wso2.carbon.identity.api.server.action.management.common.ActionManagementServiceHolder;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionBasicResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionTypesResponseItem;
import org.wso2.carbon.identity.api.server.action.management.v1.AuthenticationType;
import org.wso2.carbon.identity.api.server.action.management.v1.Endpoint;
import org.wso2.carbon.identity.api.server.action.management.v1.util.ActionMgtEndpointUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_CODE_ADD_ACTION;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_CODE_UPDATE_ACTION;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_NO_ACTION_CONFIGURED_ON_GIVEN_ID;

/**
 * Server Action Management Service.
 */
public class ServerActionManagementService {

    private static final Log LOG = LogFactory.getLog(ServerActionManagementService.class);

    /**
     * Create a new action.
     *
     * @param actionType   Action type.
     * @param actionModel  Action model.
     * @return Action response.
     */
    public ActionResponse createAction(String actionType, ActionModel actionModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding Action for Action Type: " + actionType);
        }
        try {
            Action action = buildAction(actionModel);
            Action createdAction = ActionManagementServiceHolder.getActionManagementService().addAction(actionType,
                    action, CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (createdAction == null) {
                LOG.error(ERROR_CODE_ADD_ACTION.getDescription());
                throw ActionMgtEndpointUtil.handleException(Response.Status.INTERNAL_SERVER_ERROR,
                        ERROR_CODE_ADD_ACTION);
            }
            return buildActionResponse(createdAction);
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public List<ActionResponse> getActionsByActionType(String actionType) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving Actions for Action Type: " + actionType);
        }
        try {
            List<Action> actions = ActionManagementServiceHolder.getActionManagementService()
                    .getActionsByActionType(actionType,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (actions == null) {
                return Collections.emptyList();
            }

            List<ActionResponse> actionResponses = new ArrayList<>();
            for (Action action : actions) {
                actionResponses.add(buildActionResponse(action));
            }
            return actionResponses;
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionResponse updateAction(String actionType, String actionId, ActionModel actionModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating Action for Action Type: " + actionType + " and Action Id: " + actionId);
        }
        try {
            Action action = ActionManagementServiceHolder.getActionManagementService().getActionByActionId(actionId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (action == null) {
                throw ActionMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        ERROR_NO_ACTION_CONFIGURED_ON_GIVEN_ID);
            }
            action = buildAction(actionModel);
            Action updatedAction = ActionManagementServiceHolder.getActionManagementService().updateAction(actionType,
                    actionId, action, CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (updatedAction == null) {
                LOG.error(ERROR_CODE_UPDATE_ACTION.getDescription());
                throw ActionMgtEndpointUtil.handleException(Response.Status.INTERNAL_SERVER_ERROR,
                        ERROR_CODE_UPDATE_ACTION);
            }
            return buildActionResponse(updatedAction);
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public void deleteAction(String actionType, String actionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting Action for Action Type: " + actionType + " and Action Id: " + actionId);
        }
        try {
            Action action = ActionManagementServiceHolder.getActionManagementService().getActionByActionId(actionId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (action == null) {
                return;
            }
            ActionManagementServiceHolder.getActionManagementService().deleteAction(actionType, actionId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionBasicResponse activateAction(String actionType, String actionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Activating Action for Action Type: " + actionType + " and Action Id: " + actionId);
        }
        try {
            Action action = ActionManagementServiceHolder.getActionManagementService().getActionByActionId(actionId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (action == null) {
                throw ActionMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        ERROR_NO_ACTION_CONFIGURED_ON_GIVEN_ID);
            }
            Action activatedAction = ActionManagementServiceHolder.getActionManagementService()
                    .activateAction(actionType, actionId,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            return buildActionBasicResponse(activatedAction);
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionBasicResponse deactivateAction(String actionType, String actionId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deactivating Action for Action Type: " + actionType + " and Action Id: " + actionId);
        }
        try {
            Action action = ActionManagementServiceHolder.getActionManagementService().getActionByActionId(actionId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (action == null) {
                throw ActionMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        ERROR_NO_ACTION_CONFIGURED_ON_GIVEN_ID);
            }
            Action deactivatedAction = ActionManagementServiceHolder.getActionManagementService()
                    .deactivateAction(actionType, actionId,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            return buildActionBasicResponse(deactivatedAction);
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public List<ActionTypesResponseItem> getActions() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving Action Types.");
        }
        try {
            Map<String, Integer> actionsCountPerType = ActionManagementServiceHolder.getActionManagementService()
                    .getActionsCountPerType(CarbonContext.getThreadLocalCarbonContext().getTenantDomain());

            List<ActionTypesResponseItem> actionTypesResponseItems = new ArrayList<>();
            for (TypeEnums.ActionTypes actionType : TypeEnums.ActionTypes.values()) {

                actionTypesResponseItems.add(new ActionTypesResponseItem()
                        .type(ActionTypesResponseItem.TypeEnum.valueOf(actionType.getActionType()))
                        .displayName(actionType.getDisplayName())
                        .description(actionType.getDescription())
                        .count(actionsCountPerType.get(actionType.getActionType()) == null ? 0 :
                                actionsCountPerType.get(actionType.getActionType()))
                        .self(ActionMgtEndpointUtil.buildURIForActionType(actionType.getActionType())));
            }

            return actionTypesResponseItems;
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    /**
     * Build ActionResponse from Action.
     *
     * @param action Action object.
     * @return ActionResponse object.
     */
    private ActionResponse buildActionResponse(Action action) {

        return new ActionResponse()
                .id(action.getId())
                .type(ActionResponse.TypeEnum.valueOf(action.getType().toString()))
                .name(action.getName())
                .description(action.getDescription())
                .status(ActionResponse.StatusEnum.valueOf(action.getStatus().toString()))
                .endpoint(new Endpoint()
                        .uri(action.getEndpoint().getUri())
                        .authentication(new AuthenticationType()
                                .type(AuthenticationType.TypeEnum.valueOf(action.getEndpoint()
                                        .getAuthentication().getType().toString()))
                                .properties(action.getEndpoint().getAuthentication().getProperties())));
    }

    /**
     * Build ActionBasicResponse from Action.
     *
     * @param activatedAction Action Object
     * @return ActionBasicResponse object.
     */
    private ActionBasicResponse buildActionBasicResponse(Action activatedAction) {

        return new ActionBasicResponse()
                .id(activatedAction.getId())
                .type(ActionBasicResponse.TypeEnum.valueOf(activatedAction.getType().toString()))
                .name(activatedAction.getName())
                .description(activatedAction.getDescription())
                .status(ActionBasicResponse.StatusEnum.valueOf(activatedAction.getStatus().toString()));
    }

    /**
     * Create Action from the Action model.
     *
     * @param actionModel API model.
     * @return Action.
     */
    private Action buildAction(ActionModel actionModel) {

        ActionMgtEndpointUtil.validateActionEndpointAuthProperties(actionModel);
        Action.ActionRequestBuilder actionRequestBuilder = new Action.ActionRequestBuilder()
                .name(actionModel.getName())
                .description(actionModel.getDescription())
                .endpoint(new EndpointConfig.EndpointConfigBuilder()
                        .uri(actionModel.getEndpoint().getUri())
                        .authentication(new AuthType.AuthTypeBuilder()
                                .type(TypeEnums.AuthenticationType.valueOf(actionModel.getEndpoint().getAuthentication()
                                        .getType().toString()))
                                .properties(actionModel.getEndpoint().getAuthentication().getProperties())
                                .build())
                        .build());

        return actionRequestBuilder.build();
    }
}
