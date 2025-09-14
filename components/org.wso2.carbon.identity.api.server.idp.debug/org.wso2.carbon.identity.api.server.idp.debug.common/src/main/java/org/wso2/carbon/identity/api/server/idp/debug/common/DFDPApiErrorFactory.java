/*
 * Copyright (c) 2019-2025, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.idp.debug.common;

import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.idp.debug.common.Constants.ErrorMessage;

/**
 * Factory class for creating API errors for IdP Debug Flow Data Provider.
 */
public class DFDPApiErrorFactory {

    /**
     * Build error response for the given error message.
     *
     * @param errorMessage Error message enum
     * @return Error response
     */
    public static ErrorResponse buildError(ErrorMessage errorMessage) {
        return buildError(errorMessage, null);
    }

    /**
     * Build error response for the given error message with custom description.
     *
     * @param errorMessage Error message enum
     * @param description Custom error description
     * @return Error response
     */
    public static ErrorResponse buildError(ErrorMessage errorMessage, String description) {
        return getErrorBuilder(errorMessage, description).build();
    }

    /**
     * Create an API error for request validation failure.
     *
     * @param description Error description
     * @return API error instance
     */
    public static APIError buildClientError(ErrorMessage errorMessage, String description) {
        return new APIError(Response.Status.BAD_REQUEST, getErrorBuilder(errorMessage, description).build());
    }

    /**
     * Create an API error for server-side errors.
     *
     * @param description Error description
     * @return API error instance
     */
    public static APIError buildServerError(ErrorMessage errorMessage, String description) {
        return new APIError(Response.Status.INTERNAL_SERVER_ERROR, getErrorBuilder(errorMessage, description).build());
    }

    /**
     * Create an API error for not found errors.
     *
     * @param description Error description
     * @return API error instance
     */
    public static APIError buildNotFoundError(ErrorMessage errorMessage, String description) {
        return new APIError(Response.Status.NOT_FOUND, getErrorBuilder(errorMessage, description).build());
    }

    /**
     * Create an API error for forbidden errors.
     *
     * @param description Error description
     * @return API error instance
     */
    public static APIError buildForbiddenError(ErrorMessage errorMessage, String description) {
        return new APIError(Response.Status.FORBIDDEN, getErrorBuilder(errorMessage, description).build());
    }

    /**
     * Build error response builder.
     *
     * @param errorMessage Error message enum
     * @param description  Detailed error description
     * @return ErrorResponse builder
     */
    private static ErrorResponse.Builder getErrorBuilder(ErrorMessage errorMessage, String description) {
        return new ErrorResponse.Builder()
                .withCode(errorMessage.getCode())
                .withMessage(errorMessage.getMessage())
                .withDescription(description != null ? description : errorMessage.getDescription());
    }
}
