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

package org.wso2.carbon.identity.api.server.action.management.v1.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.action.mgt.exception.ActionMgtClientException;
import org.wso2.carbon.identity.action.mgt.exception.ActionMgtException;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;

import java.util.Map;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ACTION_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_ACTION_ENDPOINT;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_ACTION_ENDPOINT_AUTHENTICATION;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_ACTION_ENDPOINT_AUTHENTICATION_TYPE;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_ACTION_ENDPOINT_URI;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_ACTION_NAME;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_ACTION_TYPE;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ErrorMessage.ERROR_CODE_NOT_SUPPORTED_ACTION_ENDPOINT_AUTHENTICATION_TYPE;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.PATH_CONSTANT;
import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;

/**
 * Utility class for Action management endpoint.
 */
public class ActionMgtEndpointUtil {

    private static final Log LOG = LogFactory.getLog(ActionMgtEndpointUtil.class);
    private static final String ACTION_TYPE_LINK_FORMAT = Constants.V1_API_PATH_COMPONENT + ACTION_PATH_COMPONENT
            + PATH_CONSTANT;

    public static String getValidatedActionType(String actionType) {

        for (ActionMgtEndpointConstants.ActionTypes type: ActionMgtEndpointConstants.ActionTypes.values()) {

            if (type.getPathParam().equals(actionType)) {
                return type.getActionType();
            }
        }
        throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_ACTION_TYPE);
    }

    public static void validateAction(ActionModel actionModel) {

        if (StringUtils.isBlank(actionModel.getName())) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_ACTION_NAME);
        }
        if (actionModel.getEndpoint() == null) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_ACTION_ENDPOINT);
        }
        if (StringUtils.isBlank(actionModel.getEndpoint().getUri())) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_ACTION_ENDPOINT_URI);
        }
        if (actionModel.getEndpoint().getAuthentication() == null) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_ACTION_ENDPOINT_AUTHENTICATION);
        }
        if (StringUtils.isBlank(actionModel.getEndpoint().getAuthentication().getType().toString())) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_ACTION_ENDPOINT_AUTHENTICATION_TYPE);
        }

        String authnType = actionModel.getEndpoint().getAuthentication().getType().toString();
        Map<String, Object> authnProperties = actionModel.getEndpoint().getAuthentication().getProperties();

        for (ActionMgtEndpointConstants.AuthenticationType type:
                ActionMgtEndpointConstants.AuthenticationType.values()) {

            if (type.getType().equals(authnType)) {
                for (String property: type.getProperties()) {
                    if (authnProperties == null || authnProperties.get(property) == null) {
                        throw handleException(Response.Status.BAD_REQUEST,
                                ERROR_CODE_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES);
                    }
                }
                return;
            }
        }
        throw handleException(Response.Status.BAD_REQUEST,
                ERROR_CODE_NOT_SUPPORTED_ACTION_ENDPOINT_AUTHENTICATION_TYPE);
    }


    public static String buildURIForActionType(String actionType) {

        return ContextLoader.buildURIForBody(ACTION_TYPE_LINK_FORMAT +
                ActionMgtEndpointConstants.ActionTypes.valueOf(actionType).getPathParam()).toString();
    }

    public static APIError handleException(Response.Status status,
                                           ActionMgtEndpointConstants.ErrorMessage error) {

        return new APIError(status, getError(error.getCode(), error.getMessage(),
                error.getDescription()));
    }

    public static APIError handleException(Response.Status status,
                                           ActionMgtEndpointConstants.ErrorMessage error,
                                           String data) {

        return new APIError(status, getError(error.getCode(), error.getMessage(),
                String.format(error.getDescription(), data)));
    }

    public static APIError handleException(Response.Status status, String errorCode,
                                           String message, String description) {

        return new APIError(status, getError(errorCode, message, description));
    }

    public static APIError handleActionMgtException(ActionMgtException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof ActionMgtClientException) {
            LOG.debug(e.getMessage(), e);
            status = Response.Status.BAD_REQUEST;
        } else {
            LOG.error(e.getMessage(), e);
        }
        String errorCode = e.getErrorCode();
        errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                ActionMgtEndpointConstants.ACTION_MANAGEMENT_PREFIX + errorCode;
        return handleException(status, errorCode, e.getMessage(), e.getDescription());
    }

    /**
     * Returns a generic error object.
     *
     * @param errorCode        Error code.
     * @param errorMessage     Error message.
     * @param errorDescription Error description.
     * @return A generic error with the specified details.
     */
    public static ErrorDTO getError(String errorCode, String errorMessage, String errorDescription) {

        ErrorDTO error = new ErrorDTO();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        error.setDescription(errorDescription);
        return error;
    }
}
