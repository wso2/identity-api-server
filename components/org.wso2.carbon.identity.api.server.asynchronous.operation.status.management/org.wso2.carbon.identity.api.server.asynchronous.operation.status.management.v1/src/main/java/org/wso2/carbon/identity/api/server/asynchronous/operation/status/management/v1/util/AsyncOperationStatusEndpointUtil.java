package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants.AsyncOperationStatusMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;
import org.wso2.carbon.identity.framework.async.status.mgt.api.exception.AsyncStatusMgtClientException;
import org.wso2.carbon.identity.framework.async.status.mgt.api.exception.AsyncStatusMgtException;
import org.wso2.carbon.identity.framework.async.status.mgt.api.exception.AsyncStatusMgtServerException;

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

        return new APIError(status, getError(error.getCode(), error.getMessage(),
                error.getDescription()));
    }

    public static APIError handleException(Response.Status status,
                                           AsyncOperationStatusMgtEndpointConstants.ErrorMessage error,
                                           String data) {

        return new APIError(status, getError(error.getCode(), error.getMessage(),
                String.format(error.getDescription(), data)));
    }

    public static APIError handleException(Response.Status status, String errorCode,
                                           String message, String description) {

        return new APIError(status, getError(errorCode, message, description));
    }

    public static APIError handleAsyncStatusMgtException(AsyncStatusMgtException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof AsyncStatusMgtClientException) {
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

    public static AsyncStatusMgtServerException buildAsyncStatusMgtServerException(
            AsyncOperationStatusMgtEndpointConstants.ErrorMessage error,
            Throwable e, String... data) {

        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }

        return new AsyncStatusMgtServerException(error.getMessage(), description, error.getCode(), e);
    }

    public static AsyncStatusMgtClientException buildAsyncStatusMgtClientException(
            AsyncOperationStatusMgtEndpointConstants.ErrorMessage error, String... data) {

        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }

        return new AsyncStatusMgtClientException(error.getMessage(), description, error.getCode());
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
