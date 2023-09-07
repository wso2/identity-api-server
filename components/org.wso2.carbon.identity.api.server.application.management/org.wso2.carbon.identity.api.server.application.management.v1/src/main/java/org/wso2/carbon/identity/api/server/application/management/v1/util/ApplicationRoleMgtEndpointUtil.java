/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.v1.Error;
import org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationRoleMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.application.management.v1.exception.ApplicationRoleMgtEndpointException;
import org.wso2.carbon.identity.application.role.mgt.exceptions.ApplicationRoleManagementClientException;
import org.wso2.carbon.identity.application.role.mgt.exceptions.ApplicationRoleManagementException;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationRoleMgtEndpointConstants.ErrorMessage.ERROR_CODE_GROUP_ALREADY_ASSIGNED;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationRoleMgtEndpointConstants.ErrorMessage.ERROR_CODE_GROUP_NOT_FOUND;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationRoleMgtEndpointConstants.ErrorMessage.ERROR_CODE_IDP_NOT_FOUND;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationRoleMgtEndpointConstants.ErrorMessage.ERROR_CODE_SCOPE_ALREADY_ASSIGNED;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationRoleMgtEndpointConstants.ErrorMessage.ERROR_CODE_USER_ALREADY_ASSIGNED;
import static org.wso2.carbon.identity.api.server.application.management.v1.constants.ApplicationRoleMgtEndpointConstants.ErrorMessage.ERROR_CODE_USER_NOT_FOUND;
import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;

/**
 * Application role management endpoint util.
 */
public class ApplicationRoleMgtEndpointUtil {

    private static final Log log = LogFactory.getLog(ApplicationRoleMgtEndpointUtil.class);

    public static ApplicationRoleMgtEndpointException handleException(Response.Status status,
                                                                      ApplicationRoleMgtEndpointConstants.ErrorMessage
                                                                              error) {

        return new ApplicationRoleMgtEndpointException(status, getError(error.getCode(), error.getMessage(),
                error.getDescription()));
    }

    public static ApplicationRoleMgtEndpointException handleException(Response.Status status,
                                                                      ApplicationRoleMgtEndpointConstants.ErrorMessage
                                                                              error, String data) {

        return new ApplicationRoleMgtEndpointException(status, getError(error.getCode(), error.getMessage(),
                String.format(error.getDescription(), data)));
    }
    public static ApplicationRoleMgtEndpointException handleException(Response.Status status, String errorCode,
                                                                      String message, String description) {

        return new ApplicationRoleMgtEndpointException(status, getError(errorCode, message, description));
    }
    public static ApplicationRoleMgtEndpointException handleApplicationRoleMgtException(
            ApplicationRoleManagementException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof ApplicationRoleManagementClientException) {

            if (e.getErrorCode().equals(ERROR_CODE_USER_NOT_FOUND.getCode()) ||
                    e.getErrorCode().equals(ERROR_CODE_GROUP_NOT_FOUND.getCode()) ||
                    e.getErrorCode().equals(ERROR_CODE_IDP_NOT_FOUND.getCode())
            ) {
                status = Response.Status.NOT_FOUND;
            } else if (e.getErrorCode().equals(ERROR_CODE_USER_ALREADY_ASSIGNED.getCode()) ||
                    e.getErrorCode().equals(ERROR_CODE_GROUP_ALREADY_ASSIGNED.getCode()) ||
                    e.getErrorCode().equals(ERROR_CODE_SCOPE_ALREADY_ASSIGNED.getCode())
            ) {
                status = Response.Status.CONFLICT;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else {
            log.error(e.getMessage(), e);
        }
        String errorCode = e.getErrorCode();
        errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                ApplicationRoleMgtEndpointConstants.APP_ROLE_MGT_ERROR_CODE_PREFIX + errorCode;
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
    public static Error getError(String errorCode, String errorMessage, String errorDescription) {

        Error error = new Error();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        error.setDescription(errorDescription);
        return error;
    }
}
