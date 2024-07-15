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
import org.wso2.carbon.identity.action.management.model.AuthProperty;
import org.wso2.carbon.identity.action.management.model.AuthType;
import org.wso2.carbon.identity.action.management.model.EndpointConfig;
import org.wso2.carbon.identity.api.server.action.management.common.ActionManagementServiceHolder;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionBasicResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionTypesResponseItem;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.AuthenticationTypeProperties;
import org.wso2.carbon.identity.api.server.action.management.v1.AuthenticationTypeResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.EndpointResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.util.ActionMgtEndpointUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_INVALID_ACTION_ENDPOINT_AUTH_TYPE;

/**
 * Server Action Management Service.
 */
public class ServerActionManagementService {

    private static final Log LOG = LogFactory.getLog(ServerActionManagementService.class);

    public ActionResponse createAction(String actionType, ActionModel actionModel) {

        try {
            return buildActionResponse(ActionManagementServiceHolder.getActionManagementService()
                    .addAction(actionType, buildAction(actionModel),
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()));
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public List<ActionResponse> getActionsByActionType(String actionType) {

        try {
            List<Action> actions = ActionManagementServiceHolder.getActionManagementService()
                    .getActionsByActionType(actionType,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain());

            List<ActionResponse> actionResponses = new ArrayList<>();
            for (Action action : actions) {
                actionResponses.add(buildActionResponse(action));
            }
            return actionResponses;
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionResponse updateAction(String actionType, String actionId, ActionUpdateModel actionUpdateModel) {

        try {
            Action updatingAction = new Action.ActionRequestBuilder()
                    .name(actionUpdateModel.getName())
                    .description(actionUpdateModel.getDescription())
                    .endpoint(new EndpointConfig.EndpointConfigBuilder()
                            .uri(actionUpdateModel.getEndpointUri())
                            .build())
                    .build();

            return buildActionResponse(ActionManagementServiceHolder.getActionManagementService()
                    .updateAction(actionType, actionId, updatingAction,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()));
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public void deleteAction(String actionType, String actionId) {

        try {
            ActionManagementServiceHolder.getActionManagementService().deleteAction(actionType, actionId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionBasicResponse activateAction(String actionType, String actionId) {

        try {
            return buildActionBasicResponse(ActionManagementServiceHolder.getActionManagementService()
                    .activateAction(actionType, actionId,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()));
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionBasicResponse deactivateAction(String actionType, String actionId) {

        try {
            return buildActionBasicResponse(ActionManagementServiceHolder.getActionManagementService()
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
            for (Action.ActionTypes actionType : Action.ActionTypes.values()) {

                actionTypesResponseItems.add(new ActionTypesResponseItem()
                        .type(ActionTypesResponseItem.TypeEnum.valueOf(actionType.getActionType()))
                        .displayName(actionType.getDisplayName())
                        .description(actionType.getDescription())
                        .count(actionsCountPerType.getOrDefault(actionType.getActionType(), 0))
                        .self(ActionMgtEndpointUtil.buildURIForActionType(actionType.getActionType())));
            }

            return actionTypesResponseItems;
        } catch (ActionMgtException e) {
            throw ActionMgtEndpointUtil.handleActionMgtException(e);
        }
    }

    public ActionResponse updateActionEndpointAuthentication(String actionType, String actionId, String authType,
                                                         AuthenticationTypeProperties authenticationTypeProperties) {

        try {
            String resolvedAuthType = getAuthTypeFromPath(authType);
            AuthType authentication = new AuthType.AuthTypeBuilder()
                    .type(AuthType.AuthenticationType.valueOf(resolvedAuthType))
                    .properties(getActionEndpointAuthProperties(resolvedAuthType,
                            authenticationTypeProperties.getProperties()))
                    .build();
            return buildActionResponse(ActionManagementServiceHolder.getActionManagementService()
                    .updateActionEndpointAuthentication(actionType, actionId, authentication,
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain()));
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
                .endpoint(new EndpointResponse()
                        .uri(action.getEndpoint().getUri())
                        .authentication(new AuthenticationTypeResponse()
                                .type(AuthenticationTypeResponse.TypeEnum.valueOf(action.getEndpoint()
                                        .getAuthentication().getType().toString()))));
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
     * @param actionModel Action model.
     * @return Action.
     */
    private Action buildAction(ActionModel actionModel) {

        Action.ActionRequestBuilder actionRequestBuilder = new Action.ActionRequestBuilder()
                .name(actionModel.getName())
                .description(actionModel.getDescription())
                .endpoint(new EndpointConfig.EndpointConfigBuilder()
                        .uri(actionModel.getEndpoint().getUri())
                        .authentication(new AuthType.AuthTypeBuilder()
                                .type(AuthType.AuthenticationType.valueOf(actionModel.getEndpoint().getAuthentication()
                                        .getType().toString()))
                                .properties(getActionEndpointAuthProperties(
                                        actionModel.getEndpoint().getAuthentication().getType().name(),
                                        actionModel.getEndpoint().getAuthentication().getProperties()))
                                .build())
                        .build());

        return actionRequestBuilder.build();
    }

    /**
     * Build Action Endpoint Authentication properties.
     *
     * @param authType          Authentication Type.
     * @param authPropertiesMap Authentication properties.
     * @return List of AuthProperty.
     */
    private List<AuthProperty> getActionEndpointAuthProperties(String authType, Map<String, Object> authPropertiesMap) {

        List<AuthProperty> authProperties = new ArrayList<>();
        for (AuthType.AuthenticationType type: AuthType.AuthenticationType.values()) {
            if (type.getType().equals(authType)) {
                for (AuthProperty property: type.getProperties()) {
                    if (authPropertiesMap == null || !authPropertiesMap.containsKey(property.getName())) {
                        throw ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                                ERROR_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES);
                    }
                    property.setValue(authPropertiesMap.get(property.getName()).toString());
                    authProperties.add(property);
                }
                if (authPropertiesMap.size() > type.getProperties().size()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Removing the given unnecessary properties from the Action Endpoint " +
                                "authentication properties of Authentication Type: " + authType);
                    }
                }
            }
        }
        return authProperties;
    }

    /**
     * Get AuthType from path.
     *
     * @param authType Authentication Type.
     * @return Auth Type resolved from the path param.
     */
    private String getAuthTypeFromPath(String authType) {

        return Arrays.stream(AuthType.AuthenticationType.values())
                .filter(type -> type.getPathParam().equals(authType))
                .map(AuthType.AuthenticationType::getType)
                .findFirst()
                .orElseThrow(() -> ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                        ERROR_INVALID_ACTION_ENDPOINT_AUTH_TYPE));
    }
}
