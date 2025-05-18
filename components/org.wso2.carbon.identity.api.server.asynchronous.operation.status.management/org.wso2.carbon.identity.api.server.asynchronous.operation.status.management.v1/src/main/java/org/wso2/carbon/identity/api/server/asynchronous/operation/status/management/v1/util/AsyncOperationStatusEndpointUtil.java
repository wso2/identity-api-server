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

package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;
import org.wso2.carbon.identity.framework.async.operation.status.mgt.api.exception.AsyncOperationStatusMgtClientException;
import org.wso2.carbon.identity.framework.async.operation.status.mgt.api.exception.AsyncOperationStatusMgtException;
import org.wso2.carbon.identity.framework.async.operation.status.mgt.api.exception.AsyncOperationStatusMgtServerException;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.ASYNC_OPERATION_STATUS_PATH;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.ASYNC_STATUS_PREFIX;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.ErrorMessage.ERROR_NO_ASYNC_STATUS_ON_GIVEN_ID;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForBody;

/**
 * This class provides util functions to the asynchronous operation status endpoint.
 */
public class AsyncOperationStatusEndpointUtil {

    private static final Log LOG = LogFactory.getLog(AsyncOperationStatusEndpointUtil.class);

    public static String buildURIForPagination(String paginationURL, String resourcePath) {

        return buildURIForBody(PATH_SEPARATOR + V1_API_PATH_COMPONENT + PATH_SEPARATOR
                + ASYNC_OPERATION_STATUS_PATH + resourcePath + paginationURL).toString();
    }

    public static APIError handleException(Response.Status status,
                                           AsyncOperationStatusMgtEndpointConstants.ErrorMessage error) {

        return new APIError(status, getError(error.getCode(), error.getMessage(), error.getDescription()));
    }

    public static APIError handleException(Response.Status status,
                                           AsyncOperationStatusMgtEndpointConstants.ErrorMessage error, String data) {

        return new APIError(status, getError(error.getCode(), error.getMessage(),
                String.format(error.getDescription(), data)));
    }

    public static APIError handleException(Response.Status status, String errorCode,
                                           String message, String description) {

        return new APIError(status, getError(errorCode, message, description));
    }

    public static APIError handleAsyncOperationStatusMgtException(AsyncOperationStatusMgtException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof AsyncOperationStatusMgtClientException) {
            LOG.debug(e.getMessage(), e);
            if (ERROR_NO_ASYNC_STATUS_ON_GIVEN_ID.getCode().equals(e.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else {
            LOG.error(e.getMessage(), e);
        }
        String errorCode = e.getErrorCode();
        errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                ASYNC_STATUS_PREFIX + errorCode;
        return handleException(status, errorCode, e.getMessage(), e.getDescription());
    }

    public static AsyncOperationStatusMgtServerException buildAsyncStatusMgtServerException(
            AsyncOperationStatusMgtEndpointConstants.ErrorMessage error, Throwable e, String... data) {

        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }

        return new AsyncOperationStatusMgtServerException(error.getMessage(), description, error.getCode(), e);
    }

    public static AsyncOperationStatusMgtClientException buildAsyncStatusMgtClientException(
            AsyncOperationStatusMgtEndpointConstants.ErrorMessage error, String... data) {

        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }

        return new AsyncOperationStatusMgtClientException(error.getMessage(), description, error.getCode());
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
