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
        ERROR_CODE_INVALID_ACTION_TYPE("60001", "Unable to perform the operation.",
                "Provided action type is not supported."),
        ERROR_CODE_INVALID_ACTION_NAME("60002", "Unable to create an Action.",
                "Action name is required."),
        ERROR_CODE_INVALID_ACTION_ENDPOINT("60003", "Unable to create an Action.",
                "Action endpoint is required."),
        ERROR_CODE_INVALID_ACTION_ENDPOINT_URI("60004", "Unable to create an Action.",
                "Action endpoint URI is required."),
        ERROR_CODE_INVALID_ACTION_ENDPOINT_AUTHENTICATION("60005", "Unable to create an Action.",
                "Action endpoint authentication is required."),
        ERROR_CODE_INVALID_ACTION_ENDPOINT_AUTHENTICATION_TYPE("60006", "Unable to create an Action.",
                "Action endpoint authentication type is required."),
        ERROR_CODE_INVALID_ACTION_ENDPOINT_AUTHENTICATION_PROPERTIES("60007",
                "Unable to perform the operation.",
                "Required authentication properties are not provided or invalid."),
        ERROR_CODE_NOT_SUPPORTED_ACTION_ENDPOINT_AUTHENTICATION_TYPE("60008",
                "Unable to create an Action.",
                "Provided authentication type is not supported."),
        ERROR_CODE_MAXIMUM_ACTIONS_PER_ACTION_TYPE_REACHED("60009", "Unable to create an Action.",
                "Maximum number of actions per action type is reached."),
        ERROR_CODE_NO_ACTION_CONFIGURED_ON_GIVEN_ID("60010", "Unable to perform the operation.",
                "No Action is configured for the given action ID."),

        // Server errors.
        ERROR_CODE_ADD_ACTION("65001", "Error while adding action.",
                "Server encountered an error while adding the action."),
        ERROR_CODE_UPDATE_ACTION("65002", "Error while updating action.",
                                      "Server encountered an error while updating the action.");

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

    /**
     * Action Type.
     */
    public enum ActionTypes {

        PRE_ISSUE_ACCESS_TOKEN("preIssueAccessToken", "PRE_ISSUE_ACCESS_TOKEN",
                "Pre Issue Access Token", "Configure an extension point for modifying access " +
                "token via a custom service."),
        PRE_UPDATE_PASSWORD("preUpdatePassword", "PRE_UPDATE_PASSWORD",
                "Pre Update Password", "Configure an extension point for modifying user " +
                "password via a custom service.");

        private final String pathParam;
        private final String actionType;
        private final String displayName;
        private final String description;

        ActionTypes(String pathParam, String actionType, String displayName, String description) {

            this.pathParam = pathParam;
            this.actionType = actionType;
            this.displayName = displayName;
            this.description = description;
        }

        public String getPathParam() {

            return pathParam;
        }

        public String getActionType() {

            return actionType;
        }

        public String getDisplayName() {

            return displayName;
        }

        public String getDescription() {

            return description;
        }
    }

    /**
     * Enum for supported Authentication Type.
     */
    public enum AuthenticationType {

        NONE("NONE", new String[]{}),
        BEARER("BEARER", new String[]{"accessToken"}),
        BASIC("BASIC", new String[]{"username", "password"}),
        API_KEY("API_KEY", new String[]{"header", "value"});;

        private final String type;
        private final String[] properties;

        AuthenticationType(String type, String[]  properties) {

            this.type = type;
            this.properties = properties;
        }

        public String getType() {

            return type;
        }

        public String[] getProperties() {

            return properties;
        }
    }
}
