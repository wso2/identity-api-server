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
    public static final String ASYNC_OPERATION_STATUS_PATH = "async-operation-status";
    public static final String PAGINATION_AFTER = "after";
    public static final String PAGINATION_BEFORE = "before";
    public static final String ASYNC_STATUS_PREFIX = "ASYNC_STATUS-";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        // Client errors.
        ERROR_NO_ASYNC_STATUS_ON_GIVEN_ID("60100",
                "Unable to get async status.",
                "No async status is configured on the given Id."),
        ERROR_INVALID_OPERATION_ID("60101", "Operation with ID: %s doesn't exist.", "%s"),
        ERROR_INVALID_UNIT_OPERATION_ID("60102", "Invalid Unit Operation ID.",
                "Unit Operation with ID: %s doesn't exist."),
        ERROR_CODE_INVALID_PAGINATION_PARAMETER_NEGATIVE_LIMIT("60025", "Invalid pagination parameters.",
                "'limit' shouldn't be negative."),

        // Server errors.
        ERROR_NOT_IMPLEMENTED_ACTION_TYPE("650015",
                "Unable to perform the operation.",
                "The requested action type is not currently supported by the server."),
        ERROR_NOT_IMPLEMENTED_ACTION_RULE_FLOW_TYPE("650016",
                "Unable to perform the operation.",
                "Rules are not supported for the specified action type by the server."),
        ERROR_WHILE_INITIALIZING_RULE_BUILDER("650017",
                "Unable to perform the operation.",
                "Error while retrieving rule metadata for rule validations.");

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
