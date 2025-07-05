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
import org.wso2.carbon.identity.action.management.api.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.api.model.Action;
import org.wso2.carbon.identity.action.management.api.service.ActionManagementService;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionBasicResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionType;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionTypesResponseItem;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.mapper.ActionMapper;
import org.wso2.carbon.identity.api.server.action.management.v1.mapper.ActionMapperFactory;
import org.wso2.carbon.identity.api.server.action.management.v1.util.ActionDeserializer;
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

    private final ActionManagementService actionManagementService;
    private static final Log LOG = LogFactory.getLog(ServerActionManagementService.class);
    private static final Set<String> NOT_IMPLEMENTED_ACTION_TYPES = new HashSet<>();

    public ServerActionManagementService(ActionManagementService actionManagementService) {

        this.actionManagementService = actionManagementService;
    }

    static {
        NOT_IMPLEMENTED_ACTION_TYPES.add(Action.ActionTypes.PRE_REGISTRATION.getPathParam());
        NOT_IMPLEMENTED_ACTION_TYPES.add(Action.ActionTypes.AUTHENTICATION.getPathParam());
    }

    public ActionResponse createAction(String actionType, String jsonBody) {

        try {
            Action.ActionTypes validatedActionType = validateActionType(actionType);
            Action action = buildAction(validatedActionType, jsonBody);
            String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            return buildActionResponse(actionManagementService.addAction(actionType, action, tenantDomain));
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public List<ActionBasicResponse> getActionsByActionType(String actionType) {

        try {
            validateActionType(actionType);
            List<Action> actions = actionManagementService.getActionsByActionType(actionType,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());

            List<ActionBasicResponse> actionBasicResponses = new ArrayList<>();
            for (Action action : actions) {
                actionBasicResponses.add(buildActionBasicResponse(action));
            }
            return actionBasicResponses;
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionResponse getActionByActionId(String actionType, String actionId) {

        try {
            validateActionType(actionType);
            Action action = actionManagementService.getActionByActionId(actionType, actionId,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (action == null) {
                throw ActionMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        ERROR_NO_ACTION_FOUND_ON_GIVEN_ACTION_TYPE_AND_ID);
            }
            return buildActionResponse(action);
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionResponse updateAction(String actionType, String actionId, String jsonBody) {

        try {
            Action.ActionTypes validatedActionType = validateActionType(actionType);
            Action updatingAction = buildUpdatingAction(validatedActionType, jsonBody);
            String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            return buildActionResponse(actionManagementService.updateAction(actionType, actionId, updatingAction, 
                    tenantDomain));
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public void deleteAction(String actionType, String actionId) {

        try {
            validateActionType(actionType);
            actionManagementService.deleteAction(actionType, actionId, CarbonContext.getThreadLocalCarbonContext()
                    .getTenantDomain());
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionBasicResponse activateAction(String actionType, String actionId) {

        try {
            validateActionType(actionType);
            return buildActionBasicResponse(actionManagementService.activateAction(actionType, actionId,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()));
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionBasicResponse deactivateAction(String actionType, String actionId) {

        try {
            validateActionType(actionType);
            return buildActionBasicResponse(actionManagementService.deactivateAction(actionType, actionId,
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
            Map<String, Integer> actionsCountPerType = actionManagementService.getActionsCountPerType(CarbonContext
                    .getThreadLocalCarbonContext().getTenantDomain());

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

        ActionMapper actionMapper = ActionMapperFactory.getActionMapper(action.getType());
        return actionMapper.toActionResponse(action);
    }

    /**
     * Build ActionBasicResponse from Action.
     *
     * @param action action object
     * @return ActionBasicResponse object
     * @throws ActionMgtException If an error occurs while building the ActionBasicResponse.
     */
    private ActionBasicResponse buildActionBasicResponse(Action action) throws ActionMgtException {

        ActionMapper actionMapper = ActionMapperFactory.getActionMapper(action.getType());
        return actionMapper.toActionBasicResponse(action);
    }

    /**
     * Build Action from the ActionModel.
     *
     * @param actionType Action Type.
     * @param jsonBody   JSON body.
     * @return Action.
     * @throws ActionMgtException If an error occurs while building the Action.
     */
    private Action buildAction(Action.ActionTypes actionType, String jsonBody)
            throws ActionMgtException {

        ActionModel actionModel = ActionDeserializer.deserializeActionModel(actionType, jsonBody);
        ActionMapper actionMapper = ActionMapperFactory.getActionMapper(actionType);
        return actionMapper.toAction(actionModel);
    }

    /**
     * Build Action from the ActionUpdateModel.
     *
     * @param actionType Action Type.
     * @param jsonBody   JSON body.
     * @return Action.
     * @throws ActionMgtException If an error occurs while building the Action.
     */
    private Action buildUpdatingAction(Action.ActionTypes actionType, String jsonBody)
            throws ActionMgtException {

        ActionUpdateModel actionUpdateModel = ActionDeserializer.deserializeActionUpdateModel(actionType, jsonBody);
        ActionMapper actionMapper = ActionMapperFactory.getActionMapper(actionType);
        return actionMapper.toAction(actionUpdateModel);
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
