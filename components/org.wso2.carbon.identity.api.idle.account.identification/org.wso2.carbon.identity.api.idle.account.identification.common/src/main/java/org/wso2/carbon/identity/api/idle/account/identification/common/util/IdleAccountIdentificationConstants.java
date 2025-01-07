/*
 * Copyright (c) 2023-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.idle.account.identification.common.util;

/**
 * Inactive users management related constant class.
 */
public class IdleAccountIdentificationConstants {

    public static final String INACTIVE_USER_MANAGEMENT_SERVICE_ERROR_PREFIX = "IDLE_ACC-";

    public static final String DATE_INACTIVE_AFTER = "inactiveAfter";
    public static final String DATE_EXCLUDE_BEFORE = "excludeBefore";
    public static final String DATE_FORMAT_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
    public static final String IS_DISABLED = "isDisabled";
    public static final String TRUE_VALUE = "true";
    public static final String FALSE_VALUE = "false";

    /**
     * Enums for error messages.
     */
    public enum ErrorMessage {

        // Client errors 600xx.
        ERROR_REQUIRED_PARAMETER_MISSING("60001",
                 "Required parameter is not provided.",
                 "%s parameter is required and cannot be empty."),

        ERROR_DATE_REGEX_MISMATCH("60002",
                  "Invalid date format provided.",
                  "The value provided for %s parameter is invalid. Date format should be yyyy-mm-dd"),

        ERROR_INVALID_DATE("60003",
                   "Invalid date provided.",
                   "The date provided for %s parameter is invalid"),

        ERROR_INVALID_DATE_COMBINATION("60004",
                "Invalid date combination is provided.",
                "The inactive after date must be before the exclude after date."),

        ERROR_INVALID_FILTER("60005",
                "Invalid filter value provided.",
                "The filter value provided is invalid"),

        // Server errors 650xx.
        ERROR_RETRIEVING_INACTIVE_USERS("65001",
                "Error while retrieving inactive users.",
                "Error while retrieving inactive users for organization: %s.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return INACTIVE_USER_MANAGEMENT_SERVICE_ERROR_PREFIX + code;
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
