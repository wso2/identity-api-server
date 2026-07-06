/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.policy.v1.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.policy.common.Constants;
import org.wso2.carbon.identity.policy.management.api.constant.ErrorMessage;
import org.wso2.carbon.identity.policy.management.api.exception.PolicyManagementClientException;
import org.wso2.carbon.identity.policy.management.api.exception.PolicyManagementException;

import javax.ws.rs.core.Response;

/**
 * Handles exceptions raised by the Device Policy API and builds the corresponding {@link APIError}.
 */
public class PolicyManagementAPIErrorBuilder {

    private static final Log LOG = LogFactory.getLog(PolicyManagementAPIErrorBuilder.class);

    private PolicyManagementAPIErrorBuilder() {

    }

    /**
     * Build an APIError from a backend PolicyManagementException.
     * Client exceptions map to HTTP 400, server (and any other) exceptions map to HTTP 500.
     *
     * @param e         The backend exception.
     * @param errorEnum API-layer error message to surface to the client.
     * @return APIError to be thrown back to the JAX-RS layer.
     */
    public static APIError handleException(PolicyManagementException e, Constants.ErrorMessage errorEnum) {

        ErrorResponse errorResponse;
        Response.Status status;
        if (e instanceof PolicyManagementClientException) {
            errorResponse = new ErrorResponse.Builder()
                    .withCode(errorEnum.getCode())
                    .withMessage(errorEnum.getMessage())
                    .withDescription(e.getMessage())
                    .build(LOG, e.getMessage());
            if (ErrorMessage.ERROR_POLICY_ALREADY_EXISTS.getCode().equals(e.getErrorCode())) {
                status = Response.Status.CONFLICT;
            } else if (ErrorMessage.ERROR_POLICY_NOT_FOUND.getCode().equals(e.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else {
            errorResponse = new ErrorResponse.Builder()
                    .withCode(errorEnum.getCode())
                    .withMessage(errorEnum.getMessage())
                    .withDescription(errorEnum.getDescription())
                    .build(LOG, e, errorEnum.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Build an APIError for an error detected at the API layer (no backend exception),
     * optionally formatting the description with the supplied data.
     *
     * @param status    HTTP status to return.
     * @param errorEnum API-layer error message.
     * @param data      Optional values to format into the description.
     * @return APIError to be thrown back to the JAX-RS layer.
     */
    public static APIError handleException(Response.Status status, Constants.ErrorMessage errorEnum, String... data) {

        String description = errorEnum.getDescription();
        if (data != null && data.length > 0) {
            description = String.format(description, (Object[]) data);
        }
        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorEnum.getCode())
                .withMessage(errorEnum.getMessage())
                .withDescription(description)
                .build(LOG, errorEnum.getMessage());
        return new APIError(status, errorResponse);
    }

    /**
     * Build an APIError for an API-layer error that wraps a non-policy backend cause
     * (e.g. a failure while retrieving rule metadata). The cause is logged.
     *
     * @param status    HTTP status to return.
     * @param errorEnum API-layer error message.
     * @param cause     The underlying exception to log.
     * @return APIError to be thrown back to the JAX-RS layer.
     */
    public static APIError handleException(Response.Status status, Constants.ErrorMessage errorEnum, Exception cause) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorEnum.getCode())
                .withMessage(errorEnum.getMessage())
                .withDescription(cause.getMessage())
                .build(LOG, cause, errorEnum.getDescription());
        return new APIError(status, errorResponse);
    }
}
