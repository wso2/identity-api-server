/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.debug.v1.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;
import org.wso2.carbon.identity.api.server.debug.v1.constants.DebugConstants;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkClientException;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkException;

import javax.ws.rs.core.Response;

/**
 * Utility class for debug API.
 */
public class DebugExceptionHandler {

    private static final Log LOG = LogFactory.getLog(DebugExceptionHandler.class);

    private DebugExceptionHandler() {

    }

    /**
     * Builds an APIError from a typed ErrorMessage enum and HTTP status.
     *
     * @param status HTTP status.
     * @param error  Typed error message enum.
     * @return APIError.
     */
    public static APIError handleException(Response.Status status, DebugConstants.ErrorMessage error) {

        return new APIError(status, getError(error.getCode(), error.getMessage(), error.getDescription()));
    }

    /**
     * Builds an ErrorDTO.
     *
     * @param errorCode Error code.
     * @param errorMessage Error message.
     * @param errorDescription Error description.
     * @return ErrorDTO.
     */
    public static ErrorDTO getError(String errorCode, String errorMessage, String errorDescription) {

        ErrorDTO error = new ErrorDTO();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        error.setDescription(errorDescription);
        return error;
    }

    /**
     * Handles a DebugFrameworkException by mapping it to an APIError.
     *
     * @param e Debug framework exception.
     * @return APIError with the appropriate HTTP status and error details.
     */
    public static APIError handleDebugException(DebugFrameworkException e) {

        Response.Status status;
        if (e instanceof DebugFrameworkClientException) {
            LOG.debug(e.getMessage(), e);
            status = Response.Status.BAD_REQUEST;
        } else {
            LOG.error(e.getMessage(), e);
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }

        String errorCode = e.getErrorCode();
        if (errorCode == null) {
            errorCode = DebugConstants.ErrorMessage.ERROR_CODE_ERROR_PROCESSING_REQUEST.getCode();
        } else if (!errorCode.startsWith(DebugConstants.ERROR_CODE_PREFIX)) {
            errorCode = DebugConstants.ERROR_CODE_PREFIX + errorCode;
        }

        return handleException(status, errorCode, e.getMessage(), e.getDescription());
    }

    private static APIError handleException(Response.Status status, String errorCode,
            String message, String description) {

        return new APIError(status, getError(errorCode, message, description));
    }

}
