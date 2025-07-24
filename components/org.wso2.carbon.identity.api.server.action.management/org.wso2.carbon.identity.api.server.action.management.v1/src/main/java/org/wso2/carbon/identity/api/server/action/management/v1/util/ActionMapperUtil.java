/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.action.management.v1.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.action.management.api.exception.ActionMgtClientException;
import org.wso2.carbon.identity.action.management.api.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.api.model.Action;
import org.wso2.carbon.identity.action.management.api.model.ActionRule;
import org.wso2.carbon.identity.action.management.api.model.Authentication;
import org.wso2.carbon.identity.action.management.api.model.EndpointConfig;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionBasicResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionType;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionUpdateModel;
import org.wso2.carbon.identity.api.server.action.management.v1.AuthenticationTypeResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.EndpointResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.Link;
import org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_EMPTY_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES;

/**
 * Utility class for Action Mapper.
 */
public class ActionMapperUtil {

    private static final Log LOG = LogFactory.getLog(ActionMapperUtil.class);

    /**
     * Build Action object from ActionModel.
     *
     * @param actionType  Action type.
     * @param actionModel ActionModel object.
     * @return Action object.
     */
    public static Action buildActionRequest(Action.ActionTypes actionType, ActionModel actionModel)
            throws ActionMgtException {

        if (LOG.isDebugEnabled()) {
            String actionTypeName = actionType != null ? actionType.toString() : "null";
            String actionName = actionModel != null ? actionModel.getName() : "null";
            LOG.debug("Building action request for type: " + actionTypeName + " with name: " + actionName);
        }
        Authentication authentication = ActionMapperUtil.buildAuthentication(
                Authentication.Type.valueOfName(actionModel.getEndpoint().getAuthentication().getType().toString()),
                actionModel.getEndpoint().getAuthentication().getProperties());

        ActionRule actionRule = null;
        if (actionModel.getRule() != null) {
            actionRule = RuleMapper.toActionRule(actionModel.getRule(), actionType,
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }

        Action action = new Action.ActionRequestBuilder()
                .name(actionModel.getName())
                .description(actionModel.getDescription())
                .endpoint(new EndpointConfig.EndpointConfigBuilder()
                        .uri(actionModel.getEndpoint().getUri())
                        .authentication(authentication)
                        .build())
                .rule(actionRule)
                .build();
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Action request built successfully for type: " + actionType);
        }
        return action;
    }

    /**
     * Build Action from the ActionUpdateModel.
     *
     * @param actionType        Action Type.
     * @param actionUpdateModel Action Update Model.
     * @return Action.
     * @throws ActionMgtException If an error occurs while building the Action.
     */
    public static Action buildUpdatingActionRequest(Action.ActionTypes actionType, ActionUpdateModel actionUpdateModel)
            throws ActionMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Building updating action request for type: " + (actionType != null ? actionType : "null"));
        }
        EndpointConfig endpointConfig = null;
        if (actionUpdateModel.getEndpoint() != null) {

            Authentication authentication = null;
            if (actionUpdateModel.getEndpoint().getAuthentication() != null) {
                authentication = buildAuthentication(Authentication.Type.valueOfName(actionUpdateModel.getEndpoint()
                                .getAuthentication().getType().toString()),
                        actionUpdateModel.getEndpoint().getAuthentication().getProperties());
            }
            endpointConfig = new EndpointConfig.EndpointConfigBuilder()
                    .uri(actionUpdateModel.getEndpoint().getUri())
                    .authentication(authentication)
                    .build();
        }

        ActionRule actionRule = null;
        if (actionUpdateModel.getRule() != null) {
            actionRule = RuleMapper.toActionRule(actionUpdateModel.getRule(), actionType,
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        }

        Action action = new Action.ActionRequestBuilder()
                .name(actionUpdateModel.getName())
                .description(actionUpdateModel.getDescription())
                .endpoint(endpointConfig)
                .rule(actionRule)
                .build();
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating action request built successfully for type: " + actionType);
        }
        return action;
    }

    /**
     * Build ActionResponse from Action.
     *
     * @param action action object
     * @return ActionResponse object
     * @throws ActionMgtException If an error occurs while building the ActionResponse.
     */
    public static ActionResponse buildActionResponse(Action action) throws ActionMgtException {

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
                                        .getAuthentication().getType().toString()))))
                .rule((action.getActionRule() != null) ? RuleMapper.toORRuleResponse(action.getActionRule()) :
                        null);
    }

    /**
     * Build ActionBasicResponse from Action.
     *
     * @param action Action Object
     * @return ActionBasicResponse object.
     */
    public static ActionBasicResponse buildActionBasicResponse(Action action) {

        return new ActionBasicResponse()
                .id(action.getId())
                .type(ActionType.valueOf(action.getType().toString()))
                .name(action.getName())
                .description(action.getDescription())
                .status(ActionBasicResponse.StatusEnum.valueOf(action.getStatus().toString()))
                .links(buildLinks(action));
    }

    /**
     * Build Links for the Action.
     *
     * @param activatedAction Action object.
     * @return List of Links.
     */
    private static List<Link> buildLinks(Action activatedAction) {

        String baseUrl = ActionMgtEndpointUtil.buildURIForActionType(activatedAction.getType().getActionType());

        List<Link> links = new ArrayList<>();
        links.add(new Link()
                .href(baseUrl + ActionMgtEndpointConstants.PATH_SEPARATOR + activatedAction.getId())
                .rel("self")
                .method(Link.MethodEnum.GET));
        return links;
    }

    /**
     * Get Authentication object from the Authentication Type and Authentication properties.
     *
     * @param authType         Authentication Type.
     * @param authPropertiesMap Authentication properties.
     * @return Authentication object.
     */
    private static Authentication buildAuthentication(Authentication.Type authType,
                                                     Map<String, Object> authPropertiesMap)
            throws ActionMgtClientException {

        switch (authType) {
            case BASIC:
                if (authPropertiesMap == null
                        || !authPropertiesMap.containsKey(Authentication.Property.USERNAME.getName())
                        || !authPropertiesMap.containsKey(Authentication.Property.PASSWORD.getName())) {
                    throw new ActionMgtClientException(
                            ERROR_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES.getMessage(),
                            ERROR_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES.getDescription(),
                            ERROR_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES.getCode());
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
}
