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

package org.wso2.carbon.identity.api.server.device.policy.v1.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.device.policy.common.Constants;

import javax.ws.rs.core.Response;

/**
 * Builds {@link APIError} instances for the Device Policy API.
 */
public class DevicePolicyAPIErrorBuilder {

    private static final Log LOG = LogFactory.getLog(DevicePolicyAPIErrorBuilder.class);

    private DevicePolicyAPIErrorBuilder() {

    }

    /**
     * Build an APIError for an error detected at the API layer, with no backend cause to log
     * (e.g. request validation failures).
     *
     * @param status    HTTP status to return.
     * @param errorEnum API-layer error message.
     * @return APIError to be thrown back to the JAX-RS layer.
     */
    public static APIError handleException(Response.Status status, Constants.ErrorMessage errorEnum) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorEnum.code())
                .withMessage(errorEnum.message())
                .withDescription(errorEnum.description())
                .build();
        return new APIError(status, errorResponse);
    }

    /**
     * Build an APIError that wraps a backend cause (e.g. a failure while retrieving rule metadata).
     * The cause is logged.
     *
     * @param status    HTTP status to return.
     * @param errorEnum API-layer error message.
     * @param cause     The underlying exception to log.
     * @return APIError to be thrown back to the JAX-RS layer.
     */
    public static APIError handleException(Response.Status status, Constants.ErrorMessage errorEnum, Exception cause) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorEnum.code())
                .withMessage(errorEnum.message())
                .withDescription(errorEnum.description())
                .build(LOG, cause, errorEnum.description());
        return new APIError(status, errorResponse);
    }
}
