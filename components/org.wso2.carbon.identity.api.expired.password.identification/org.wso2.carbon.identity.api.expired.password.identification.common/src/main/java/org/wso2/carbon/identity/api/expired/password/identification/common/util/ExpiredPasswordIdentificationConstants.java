/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.expired.password.identification.common.util;

/**
 * Password expired users identification related constant class.
 */
public class ExpiredPasswordIdentificationConstants {

    public static final String EXPIRED_PASSWORD_IDENTIFICATION_MANAGEMENT_SERVICE_ERROR_PREFIX = "PASS-EXP_ACC-";
    public static final String DATE_EXPIRED_AFTER = "expiredAfter";
    public static final String DATE_EXCLUDE_AFTER = "excludeAfter";
    public static final String DATE_FORMAT_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";

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
                  "The value provided for %s parameter is invalid. Date format should be yyyy-mm-dd."),

        ERROR_INVALID_DATE("60003",
                   "Invalid date provided.",
                   "The date provided for %s parameter is invalid."),
        PASSWORD_EXPIRY_FEATURE_NOT_ENABLED("60004",
                "The password expiry feature is not enabled.",
                "The password expiry feature needs to be enabled to retrieve the password expired users."),

        // Server errors 650xx.
        ERROR_RETRIEVING_PASSWORD_EXPIRED_USERS("65001",
                "Error while retrieving password expired users.",
                "Error while retrieving password expired users for organization: %s.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return EXPIRED_PASSWORD_IDENTIFICATION_MANAGEMENT_SERVICE_ERROR_PREFIX + code;
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
