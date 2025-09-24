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

package org.wso2.carbon.identity.api.server.credential.management.v1.constants;

public class CredentialMgtEndpointConstants {

    private CredentialMgtEndpointConstants() {

    }

    public static final String CREDENTIAL_MGT_PREFIX = "CM-";
    public static final String ERROR_CODE_DELIMITER = "-";

    /**
     * Error messages related to credential management.
     */
    public enum ErrorMessages {

        ERROR_CODE_INVALID_USER_ID("10001",
                "Invalid user ID.",
                "The provided user ID is invalid."),
        ERROR_CODE_INVALID_CREDENTIAL_TYPE("10002",
                "Invalid credential type.",
                "The provided credential type is not supported."),
        ERROR_CODE_INVALID_CREDENTIAL_ID("10003",
                "Invalid credential ID.",
                "The provided credential ID is invalid."),
        ERROR_CODE_GET_CREDENTIALS("10004",
                "Error occurred while retrieving credentials.",
                "Server encountered an error while retrieving credentials for the user.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessages(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return CREDENTIAL_MGT_PREFIX + code;
        }

        public String getMessage() {
            return message;
        }

        public String getDescription() {
            return description;
        }
    }
}
