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
import org.wso2.carbon.identity.action.management.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.model.Action;
import org.wso2.carbon.identity.api.server.action.management.common.ActionManagementServiceHolder;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionBasicResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.builder.ActionBuilder;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionType;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionTypesResponseItem;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.util.ActionBuilderUtil;
import org.wso2.carbon.identity.api.server.action.management.v1.util.ActionMgtEndpointUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_INVALID_ACTION_TYPE;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_NOT_IMPLEMENTED_ACTION_TYPE;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_NO_ACTION_FOUND_ON_GIVEN_ACTION_TYPE_AND_ID;

/**
 * Server Action Management Service.
 */
public class ServerActionManagementService {

    private static final Log LOG = LogFactory.getLog(ServerActionManagementService.class);
    private static final Set<String> NOT_IMPLEMENTED_ACTION_TYPES = new HashSet<>();

    static {
        NOT_IMPLEMENTED_ACTION_TYPES.add(Action.ActionTypes.PRE_UPDATE_PROFILE.getPathParam());
        NOT_IMPLEMENTED_ACTION_TYPES.add(Action.ActionTypes.PRE_REGISTRATION.getPathParam());
        NOT_IMPLEMENTED_ACTION_TYPES.add(Action.ActionTypes.AUTHENTICATION.getPathParam());
    }

    public ActionResponse createAction(String actionType, ActionModel actionModel) {

        try {
            Action.ActionTypes validatedActionType = validateActionType(actionType);
            String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            return buildActionResponse(ActionManagementServiceHolder.getActionManagementService()
                    .addAction(actionType, buildAction(validatedActionType, actionModel), tenantDomain));
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public List<ActionBasicResponse> getActionsByActionType(String actionType) {

        try {
            validateActionType(actionType);
            List<Action> actions = ActionManagementServiceHolder.getActionManagementService()
                    .getActionsByActionType(actionType,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain());

            List<ActionBasicResponse> actionBasicResponses = new ArrayList<>();
            for (Action action : actions) {
                actionBasicResponses.add(ActionBuilderUtil.buildActionBasicResponse(action));
            }
            return actionBasicResponses;
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionResponse getActionByActionId(String actionType, String actionId) {

        try {
            validateActionType(actionType);
            Action action = ActionManagementServiceHolder.getActionManagementService()
                    .getActionByActionId(actionType, actionId,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (action == null) {
                throw ActionMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        ERROR_NO_ACTION_FOUND_ON_GIVEN_ACTION_TYPE_AND_ID);
            }
            return buildActionResponse(ActionManagementServiceHolder.getActionManagementService()
                    .getActionByActionId(actionType, actionId,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()));
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionResponse updateAction(String actionType, String actionId, ActionUpdateModel actionUpdateModel) {

        try {
            Action.ActionTypes validatedActionType = validateActionType(actionType);
            String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            return buildActionResponse(ActionManagementServiceHolder.getActionManagementService()
                    .updateAction(actionType, actionId,
                            buildUpdatingAction(validatedActionType, actionUpdateModel), tenantDomain));
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public void deleteAction(String actionType, String actionId) {

        try {
            validateActionType(actionType);
            ActionManagementServiceHolder.getActionManagementService().deleteAction(actionType, actionId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionBasicResponse activateAction(String actionType, String actionId) {

        try {
            validateActionType(actionType);
            return ActionBuilderUtil.buildActionBasicResponse(ActionManagementServiceHolder.getActionManagementService()
                    .activateAction(actionType, actionId,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()));
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionBasicResponse deactivateAction(String actionType, String actionId) {

        try {
            validateActionType(actionType);
            return ActionBuilderUtil.buildActionBasicResponse(ActionManagementServiceHolder.getActionManagementService()
                    .deactivateAction(actionType, actionId,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()));
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public List<ActionTypesResponseItem> getActionTypes() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving Action Types.");
        }
        try {
            Map<String, Integer> actionsCountPerType = ActionManagementServiceHolder.getActionManagementService()
                    .getActionsCountPerType(CarbonContext.getThreadLocalCarbonContext().getTenantDomain());

            List<ActionTypesResponseItem> actionTypesResponseItems = new ArrayList<>();
            for (Action.ActionTypes actionType : Action.ActionTypes.filterByCategory(
                    Action.ActionTypes.Category.PRE_POST)) {

                if (!NOT_IMPLEMENTED_ACTION_TYPES.contains(actionType.getPathParam())) {
                    actionTypesResponseItems.add(new ActionTypesResponseItem()
                            .type(ActionType.valueOf(actionType.getActionType()))
                            .displayName(actionType.getDisplayName())
                            .description(actionType.getDescription())
                            .count(actionsCountPerType.getOrDefault(actionType.getActionType(), 0))
                            .self(ActionMgtEndpointUtil.buildURIForActionType(actionType.getActionType())));
                }
            }

            return actionTypesResponseItems;
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    /**
     * Build ActionResponse from Action.
     *
     * @param action action object
     * @return ActionResponse object
     * @throws ActionMgtException If an error occurs while building the ActionResponse.
     */
    private ActionResponse buildActionResponse(Action action) throws ActionMgtException {

        ActionBuilder actionBuilder = ActionBuilderUtil.getActionBuilder(action.getType());
        return actionBuilder.buildActionResponse(action);
    }

    /**
     * Build Action from the ActionModel.
     *
     * @param actionType  Action Type.
     * @param actionModel Action Model.
     * @return Action.
     * @throws ActionMgtException If an error occurs while building the Action.
     */
    private Action buildAction(Action.ActionTypes actionType, ActionModel actionModel)
            throws ActionMgtException {

        ActionBuilder actionBuilder = ActionBuilderUtil.getActionBuilder(actionType);
        return actionBuilder.buildAction(actionModel);
    }

    /**
     * Build Action from the ActionUpdateModel.
     *
     * @param actionType        Action Type.
     * @param actionUpdateModel Action Update Model.
     * @return Action.
     * @throws ActionMgtException If an error occurs while building the Action.
     */
    private Action buildUpdatingAction(Action.ActionTypes actionType, ActionUpdateModel actionUpdateModel)
            throws ActionMgtException {

        ActionBuilder actionBuilder = ActionBuilderUtil.getActionBuilder(actionType);
        return actionBuilder.buildAction(actionUpdateModel);
    }

    private Action.ActionTypes validateActionType(String actionType) {

        Action.ActionTypes actionTypeEnum = getActionTypeFromPath(actionType);

        if (NOT_IMPLEMENTED_ACTION_TYPES.contains(actionTypeEnum.getPathParam())) {
            throw ActionMgtEndpointUtil.handleException(Response.Status.NOT_IMPLEMENTED,
                    ERROR_NOT_IMPLEMENTED_ACTION_TYPE);
        }

        return actionTypeEnum;
    }

    private Action.ActionTypes getActionTypeFromPath(String actionType) {

        return Arrays.stream(Action.ActionTypes.filterByCategory(Action.ActionTypes.Category.PRE_POST))
                .filter(actionTypeObj -> actionTypeObj.getPathParam().equals(actionType))
                .findFirst()
                .orElseThrow(() -> ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                        ERROR_INVALID_ACTION_TYPE));
    }
}
