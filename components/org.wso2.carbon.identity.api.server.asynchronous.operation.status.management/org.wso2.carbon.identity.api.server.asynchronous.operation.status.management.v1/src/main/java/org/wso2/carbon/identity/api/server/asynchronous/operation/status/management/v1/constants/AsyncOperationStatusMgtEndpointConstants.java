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

package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.constants;

/**
 * Constants for Async Operation Status Management Endpoints.
 */
public class AsyncOperationStatusMgtEndpointConstants {

    public static final String DESC_SORT_ORDER = "DESC";
    public static final String ASC_SORT_ORDER = "ASC";
    public static final String NEXT = "next";
    public static final String PREVIOUS = "previous";
    public static final String FILTER_PARAM = "filter";
    public static final String LIMIT_PARAM = "limit";

    public static final String PATH_SEPARATOR = "/";
    public static final String V1_API_PATH_COMPONENT = "v1";
    public static final String ASYNC_OPERATION_STATUS_PATH = "async-operations";
    public static final String PAGINATION_AFTER = "after";
    public static final String PAGINATION_BEFORE = "before";
    public static final String ASYNC_STATUS_PREFIX = "ASYNC-STATUS-";
    public static final String UNIT_OPERATIONS = "unit-operations";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        // Client errors.
        ERROR_NO_ASYNC_STATUS_ON_GIVEN_ID("60001",
                "Unable to get async status.",
                "No async status is configured on the given Id."),
        ERROR_INVALID_OPERATION_ID("60002", "Operation with ID: %s doesn't exist.", "%s"),
        ERROR_INVALID_UNIT_OPERATION_ID("60003", "Invalid Unit Operation ID.",
                "Unit Operation with ID: %s doesn't exist."),
        ERROR_CODE_INVALID_PAGINATION_PARAMETER_NEGATIVE_LIMIT("60004", "Invalid pagination parameters.",
                "'limit' shouldn't be negative.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return ASYNC_STATUS_PREFIX + code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }

        @Override
        public String toString() {

            return code + " | " + message;
        }
    }
}
