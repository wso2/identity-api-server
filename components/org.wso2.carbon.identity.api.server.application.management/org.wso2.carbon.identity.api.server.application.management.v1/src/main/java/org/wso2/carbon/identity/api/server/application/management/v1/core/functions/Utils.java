/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * Utility functions.
 */
public class Utils {

    private static final Log log = LogFactory.getLog(Utils.class);

    static boolean getBooleanValue(Boolean aBoolean) {

        return aBoolean != null && aBoolean;
    }

    static APIError buildClientError(String message) {

        // TODO handle errors properly.
        ErrorResponse.Builder builder = new ErrorResponse.Builder();
        ErrorResponse errorResponse = builder
                .withMessage("Invalid Request.")
                .withDescription(message)
                .build(log, message);

        Response.Status status = Response.Status.BAD_REQUEST;
        return new APIError(status, errorResponse);
    }

    static APIError buildServerErrorResponse(Exception e, String message) {

        // TODO handle errors properly.
        ErrorResponse.Builder builder = new ErrorResponse.Builder();
        ErrorResponse errorResponse = builder
                .withMessage("Server error while trying the attempted operation.")
                .withDescription(message)
                .build(log, e, message);

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, errorResponse);
    }

    static APIError buildNotImplementedErrorResponse(String message) {

        // TODO handle errors properly.
        ErrorResponse.Builder builder = new ErrorResponse.Builder();
        ErrorResponse errorResponse = builder
                .withMessage("Server error while trying the attempted operation.")
                .withDescription(message)
                .build(log, message);

        Response.Status status = Response.Status.NOT_IMPLEMENTED;
        return new APIError(status, errorResponse);
    }

}
