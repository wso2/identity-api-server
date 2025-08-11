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
    public static final String PATH_SEPARATOR = "/";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        // Client errors.
        ERROR_INVALID_ACTION_TYPE("60001", "Invalid action type.",
                "Invalid action type used for path parameter."),
        ERROR_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES("60010",
                "Unable to perform the operation.",
                "Required authentication properties are not provided or invalid."),
        ERROR_EMPTY_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES("60011",
                "Unable to perform the operation.",
                "Authentication property values cannot be empty."),
        ERROR_NO_ACTION_FOUND_ON_GIVEN_ACTION_TYPE_AND_ID("60012",
                "Action is not found.",
                "No action is found for given action id and action type"),
        ERROR_INVALID_RULE("60013", "Invalid rule.", "%s"),
        ERROR_INVALID_PAYLOAD("60014", "Invalid Request body.",
                "Provided request body content is not in the expected format."),
        ERROR_NOT_ALLOWED_ACTION_TYPE_IN_ORG_LEVEL("60015", "Action type is not allowed.",
                "The requested action type: %s is not allowed at the organization level."),
        ERROR_NOT_SUPPORTED_ALLOWED_HEADERS("60016", "Unable to perform the operation.",
                "Allowed headers are not supported for the action type: %s."),
        ERROR_NOT_SUPPORTED_ALLOWED_PARAMETERS("60017", "Unable to perform the operation.",
                "Allowed parameters are not supported for the action type: %s."),

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
