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

package org.wso2.carbon.identity.api.server.admin.advisory.management.common;

/**
 * Admin advisory management constants.
 */
public class AdminAdvisoryConstants {

    private static final String ADMIN_ADVISORY = "ADV-";

    /**
     * Enum for admin advisory management related errors in the format of.
     * Error Code - code to identify the error
     * Message - What went wrong
     * Error Description - Why it went wrong
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_RETRIEVING_BANNER_CONFIG("50001",
                "Unable to get the admin advisory banner config.",
                "Server Encountered an error while retrieving the admin advisory banner config.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return ADMIN_ADVISORY + code;
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
