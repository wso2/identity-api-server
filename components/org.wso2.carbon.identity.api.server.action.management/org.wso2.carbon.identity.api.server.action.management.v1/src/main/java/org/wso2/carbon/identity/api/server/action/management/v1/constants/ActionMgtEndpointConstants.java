/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.action.management.v1.constants;

/**
 * Constants related to Action management.
 */
public class ActionMgtEndpointConstants {

    private ActionMgtEndpointConstants() {
    }

    public static final String ACTION_MANAGEMENT_PREFIX = "ACTION-";
    public static final String ACTION_PATH_COMPONENT = "/actions";
    public static final String PATH_CONSTANT = "/";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        // Client errors.
        ERROR_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES("60001",
                "Unable to perform the operation.",
                "Required authentication properties are not provided or invalid."),
        ERROR_INVALID_ACTION_ENDPOINT_AUTH_TYPE("60002",
                "Invalid Authentication Type for Action Endpoint.",
                "Invalid authentication type used for path parameter.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return ACTION_MANAGEMENT_PREFIX + code;
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
