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

package org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants;

/**
 * Holds the constants which the connection sharing management API component is using.
 */
public class ConnectionSharingMgtConstants {

    public static final String ERROR_PREFIX = "CSM-";

    public static final String CONNECTION_IDS = "CONNECTION_IDS";
    public static final String CONNECTION_NAMES = "CONNECTION_NAMES";

    public static final String RESPONSE_STATUS_PROCESSING = "Processing";
    public static final String RESPONSE_DETAIL_CONNECTION_SHARE = "Connection sharing process triggered successfully.";
    public static final String RESPONSE_DETAIL_CONNECTION_UNSHARE =
            "Connection unsharing process triggered successfully.";

    /**
     * Enum for connection sharing management related errors.
     * Error Code - code to identify the error.
     * Error Message - What went wrong.
     * Error Description - Why it went wrong.
     */
    public enum ErrorMessage {

        // Client errors.
        INVALID_SELECTIVE_CONNECTION_SHARE_REQUEST_BODY("60000",
                "Invalid selective connection share request body.",
                "The connection criteria provided for selective sharing is either null or empty. " +
                        "Please provide valid selective connection sharing criteria."),
        INVALID_GENERAL_CONNECTION_SHARE_REQUEST_BODY("60001",
                "Invalid general connection share request body.",
                "The connection criteria provided for general sharing is either null or empty. " +
                        "Please provide valid general connection sharing criteria."),
        INVALID_SELECTIVE_CONNECTION_UNSHARE_REQUEST_BODY("60002",
                "Invalid selective connection unshare request body.",
                "The connection criteria provided for selective unsharing is either null or empty. " +
                        "Please provide valid selective connection unsharing criteria."),
        INVALID_GENERAL_CONNECTION_UNSHARE_REQUEST_BODY("60003",
                "Invalid general connection unshare request body.",
                "The connection criteria provided for general unsharing is either null or empty. " +
                        "Please provide valid general connection unsharing criteria."),
        ERROR_MISSING_CONNECTION_CRITERIA("60004",
                "Missing connection criteria in the request body.",
                "The connection criteria is missing in the request body. " +
                        "Please provide the connection criteria to proceed."),
        ERROR_UNSUPPORTED_CONNECTION_SHARE_POLICY("60005",
                "Unsupported connection share policy.",
                "The provided connection share policy is not supported. " +
                        "Please provide a valid connection share policy."),
        INVALID_UUID_FORMAT("60006",
                "Invalid UUID format.",
                "The UUID provided in the request is not in a valid format. " +
                        "Please provide a valid UUID."),
        ERROR_INVALID_LIMIT("60007",
                "Invalid limit value.",
                "The limit value provided in the request is invalid. " +
                        "Please provide a valid limit value."),
        ERROR_INVALID_CURSOR("60008",
                "Invalid cursor value.",
                "The cursor value provided in the request is invalid. " +
                        "Please provide a valid cursor value."),

        // Server errors.
        ERROR_INITIATING_CONNECTIONS_API_SERVICE("65001",
                "Error initiating ConnectionsApiService.",
                "Error occurred while initiating ConnectionsApiService.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return ERROR_PREFIX + code;
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
