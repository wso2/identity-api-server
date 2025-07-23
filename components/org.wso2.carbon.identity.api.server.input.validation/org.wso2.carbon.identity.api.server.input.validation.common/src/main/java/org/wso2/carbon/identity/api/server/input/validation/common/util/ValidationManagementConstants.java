/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.input.validation.common.util;

/**
 * Input validation management related constant class.
 */
public class ValidationManagementConstants {

    public static final String INPUT_VALIDATION_ERROR_PREFIX = "IVM-";
    public static final String INPUT_VALIDATION_MGT_ERROR_CODE_DELIMITER = "-";
    public static final String CORRELATION_ID = "Correlation-ID";

    /**
     * Enums for error messages.
     */
    public enum ErrorMessage {

        // Client errors 600xx.
        ERROR_CODE_INPUT_VALIDATION_NOT_EXISTS("60001",
                "Validation configurations are not configured.",
                "Validation configurations are not configured for organization: %s."),
        ERROR_CODE_FIELD_NOT_EXISTS("60002",
                "Field is not found.",
                "Invalid or unsupported field %s is provided."),
        // Server errors 650xx.
        ERROR_CODE_ERROR_GETTING_VALIDATION_CONFIG("65001",
                "Error while getting input validation configurations.",
                "Error while retrieving input validation configurations for organization: %s."),
        ERROR_CODE_ERROR_UPDATING_VALIDATION_CONFIG("65002",
                "Unable to update input validation configurations.",
                "Error while updating input validation configurations for organization: %s."),
        ERROR_CODE_ERROR_GETTING_VALIDATORS("65003",
                "Unable to retrieve existing validators.",
                "Error while retrieving validators for organization: %s."),
        ERROR_CODE_ERROR_REVERTING_VALIDATION_CONFIG("65004",
                "Unable to revert input validation configurations.",
                "Error while reverting input validation configurations for organization: %s.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return INPUT_VALIDATION_ERROR_PREFIX + code;
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
