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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.action.management.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.model.Action;
import org.wso2.carbon.identity.action.management.model.Authentication;
import org.wso2.carbon.identity.action.management.model.EndpointConfig;
import org.wso2.carbon.identity.api.server.action.management.common.ActionManagementServiceHolder;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionBasicResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionType;
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

import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_EMPTY_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_INVALID_ACTION_ENDPOINT_AUTH_TYPE;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_NO_ACTION_FOUND_ON_GIVEN_ACTION_TYPE_AND_ID;

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

    public ActionResponse getActionByActionId(String actionType, String actionId) {

        try {
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
            return buildActionResponse(ActionManagementServiceHolder.getActionManagementService()
                    .updateAction(actionType, actionId, buildUpdatingAction(actionUpdateModel),
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
                        .type(ActionType.valueOf(actionType.getActionType()))
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
            Authentication authentication = buildAuthentication(getAuthTypeFromPath(authType),
                    authenticationTypeProperties.getProperties());
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
                .type(ActionType.valueOf(action.getType().toString()))
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
                .type(ActionType.valueOf(activatedAction.getType().toString()))
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

        Authentication authentication = buildAuthentication(
                Authentication.Type.valueOf(actionModel.getEndpoint().getAuthentication().getType().toString()),
                actionModel.getEndpoint().getAuthentication().getProperties());

        return new Action.ActionRequestBuilder()
                .name(actionModel.getName())
                .description(actionModel.getDescription())
                .endpoint(new EndpointConfig.EndpointConfigBuilder()
                        .uri(actionModel.getEndpoint().getUri())
                        .authentication(authentication)
                        .build())
                .build();
    }

    /**
     * Build Action from the ActionUpdateModel.
     *
     * @param actionUpdateModel ActionUpdateModel.
     * @return Action.
     */
    private Action buildUpdatingAction(ActionUpdateModel actionUpdateModel) {

        EndpointConfig endpointConfig = null;
        if (actionUpdateModel.getEndpoint() != null) {

            Authentication authentication = null;
            if (actionUpdateModel.getEndpoint().getAuthentication() != null) {
                authentication = buildAuthentication(Authentication.Type.valueOf(actionUpdateModel.getEndpoint()
                                .getAuthentication().getType().toString()),
                        actionUpdateModel.getEndpoint().getAuthentication().getProperties());
            }
            endpointConfig = new EndpointConfig.EndpointConfigBuilder()
                    .uri(actionUpdateModel.getEndpoint().getUri())
                    .authentication(authentication)
                    .build();
        }

        return new Action.ActionRequestBuilder()
                .name(actionUpdateModel.getName())
                .description(actionUpdateModel.getDescription())
                .endpoint(endpointConfig)
                .build();
    }

    /**
     * Get Authentication object from the Authentication Type and Authentication properties.
     *
     * @param authType         Authentication Type.
     * @param authPropertiesMap Authentication properties.
     * @return Authentication object.
     */
    private Authentication buildAuthentication(Authentication.Type authType, Map<String, Object> authPropertiesMap) {

        switch (authType) {
            case BASIC:
                if (authPropertiesMap == null
                        || !authPropertiesMap.containsKey(Authentication.Property.USERNAME.getName())
                        || !authPropertiesMap.containsKey(Authentication.Property.PASSWORD.getName())) {
                    throw ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                            ERROR_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES);
                }
                String username = (String) authPropertiesMap.get(Authentication.Property.USERNAME.getName());
                String password = (String) authPropertiesMap.get(Authentication.Property.PASSWORD.getName());

                if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                    throw ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                            ERROR_EMPTY_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES);
                }

                return new Authentication.BasicAuthBuilder(username, password).build();
            case BEARER:
                if (authPropertiesMap == null
                        || !authPropertiesMap.containsKey(Authentication.Property.ACCESS_TOKEN.getName())) {
                    throw ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                            ERROR_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES);
                }
                String accessToken = (String) authPropertiesMap.get(Authentication.Property.ACCESS_TOKEN.getName());

                if (StringUtils.isEmpty(accessToken)) {
                    throw ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                            ERROR_EMPTY_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES);
                }

                return new Authentication.BearerAuthBuilder(accessToken).build();
            case API_KEY:
                if (authPropertiesMap == null
                        || !authPropertiesMap.containsKey(Authentication.Property.HEADER.getName())
                        || !authPropertiesMap.containsKey(Authentication.Property.VALUE.getName())) {
                    throw ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                            ERROR_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES);
                }
                String header = (String) authPropertiesMap.get(Authentication.Property.HEADER.getName());
                String value = (String) authPropertiesMap.get(Authentication.Property.VALUE.getName());

                if (StringUtils.isEmpty(header) || StringUtils.isEmpty(value)) {
                    throw ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                            ERROR_EMPTY_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES);
                }

                return new Authentication.APIKeyAuthBuilder(header, value).build();
            case NONE:
                return new Authentication.NoneAuthBuilder().build();
            default:
                return null;
        }
    }

    /**
     * Get Authentication Type from path.
     *
     * @param authType Authentication Type.
     * @return Auth Type resolved from the path param.
     */
    private Authentication.Type getAuthTypeFromPath(String authType) {

        return Arrays.stream(Authentication.Type.values())
                .filter(type -> type.getPathParam().equals(authType))
                .findFirst()
                .orElseThrow(() -> ActionMgtEndpointUtil.handleException(Response.Status.BAD_REQUEST,
                        ERROR_INVALID_ACTION_ENDPOINT_AUTH_TYPE));
    }
}
