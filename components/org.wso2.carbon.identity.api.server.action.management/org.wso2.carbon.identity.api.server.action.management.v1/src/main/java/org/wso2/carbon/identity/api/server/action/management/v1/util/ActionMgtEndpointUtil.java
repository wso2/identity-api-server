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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.action.management.api.exception.ActionMgtClientException;
import org.wso2.carbon.identity.action.management.api.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.api.exception.ActionMgtServerException;
import org.wso2.carbon.identity.action.management.api.model.Action;
import org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.action.management.api.constant.ErrorMessage.ERROR_NO_ACTION_CONFIGURED_ON_GIVEN_ACTION_TYPE_AND_ID;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.ACTION_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.action.management.v1.constants.ActionMgtEndpointConstants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;

/**
 * Utility class for Action management endpoint.
 */
public class ActionMgtEndpointUtil {

    private static final Log LOG = LogFactory.getLog(ActionMgtEndpointUtil.class);
    private static final String ACTION_TYPE_LINK_FORMAT = Constants.V1_API_PATH_COMPONENT + ACTION_PATH_COMPONENT
            + PATH_SEPARATOR;

    public static String buildURIForActionType(String actionType) {

        return ContextLoader.buildURIForBody(ACTION_TYPE_LINK_FORMAT +
                Action.ActionTypes.valueOf(actionType).getPathParam()).toString();
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
            if (ERROR_NO_ACTION_CONFIGURED_ON_GIVEN_ACTION_TYPE_AND_ID.getCode().equals(e.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else {
            LOG.error(e.getMessage(), e);
        }
        String errorCode = e.getErrorCode();
        errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                ActionMgtEndpointConstants.ACTION_MANAGEMENT_PREFIX + errorCode;
        return handleException(status, errorCode, e.getMessage(), e.getDescription());
    }

    public static ActionMgtServerException buildActionMgtServerException(ActionMgtEndpointConstants.ErrorMessage error,
                                                                         Throwable e, String... data) {

        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }

        return new ActionMgtServerException(error.getMessage(), description, error.getCode(), e);
    }

    public static ActionMgtClientException buildActionMgtClientException(ActionMgtEndpointConstants.ErrorMessage error,
                                                                         Throwable e, String... data) {

        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }

        return new ActionMgtClientException(error.getMessage(), description, error.getCode());
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
