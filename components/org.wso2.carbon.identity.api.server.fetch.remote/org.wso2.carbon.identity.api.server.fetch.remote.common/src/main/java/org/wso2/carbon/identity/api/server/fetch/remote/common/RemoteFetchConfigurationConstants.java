/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.fetch.remote.common;

/**
 * Remote Fetch Configuration constant class.
 */
public class RemoteFetchConfigurationConstants {

    public static final String REMOTE_FETCH_CONFIGURATION_MANAGEMENT_PREFIX = "RFE-";
    public static final String ERROR_CODE_DELIMITER = "-";
    public static final String FREQUENCY = "frequency";
    public static final String URI = "uri";
    public static final String BRANCH = "branch";
    public static final String DIRECTORY = "directory";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String USER_NAME = "userName";
    public static final String IS_ENABLED = "isEnabled";
    public static final String REMOTE_FETCH_NAME = "remoteFetchName";
    public static final String ACTION_LISTENER = "actionListener";
    public static final String ACTION_LISTENER_ATTRIBUTES = "actionListenerAttributes";
    public static final String REPOSITORY_MANAGER = "repositoryManger";
    public static final String REPOSITORY_MANAGER_ATTRIBUTES = "repositoryMangerAttributes";
    public static final String CONFIGURATION_DEPLOYER = "configDeployer";
    public static final String CONFIGURATION_DEPLOYER_ATTRIBUTES = "configDeployerAttributes";
    public static final String WEBHOOK_REQUEST = "webHookRequest";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    public static final String REMOTE_FETCH_CONFIGURATION_PATH_COMPONENT = "/remote-fetch";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_LISTING_RF_CONFIGS("65001",
                "Unable to list existing remote fetch configurations.",
                "Server encountered an error while listing remote fetch configurations."),
        ERROR_CODE_ERROR_ADDING_RF_CONFIG("65002",
                "Unable to add remote fetch configuration.",
                "Server encountered an error while adding the remote fetch configuration."),
        ERROR_CODE_ERROR_RETRIEVING_RF_CONFIG("65003",
                "Unable to retrieve remote fetch configuration.",
                "Server encountered an error while retrieving the remote fetch configuration for identifier %s."),
        ERROR_CODE_ERROR_DELETING_RF_CONFIGS("65004",
                "Unable to delete remote fetch configuration.",
                "Server encountered an error while deleting the emote fetch configuration for the identifier %s."),
        ERROR_CODE_ERROR_UPDATING_RF_CONFIG("65005",
                "Unable to update remote fetch configuration.",
                "Server encountered an error while updating the remote fetch configuration for identifier %s."),
        ERROR_CODE_ERROR_TRIGGER_REMOTE_FETCH("65006",
                "Unable to trigger remote fetch .",
                "Server encountered an error while triggering the remote fetch for identifier %s."),
        ERROR_CODE_ERROR_STATUS_REMOTE_FETCH("65007",
                "Unable to get status for remote fetch .",
                "Server encountered an error while getting status for the remote fetch for identifier %s."),
        ERROR_CODE_ERROR_WEB_HOOK_REMOTE_FETCH("65008",
                "Unable to handle web hook for remote fetch .",
                "Server encountered an error while handling web hook."),
        ERROR_CODE_INVALID_RE_CONFIG_INPUT("60001", "Invalid input.",
                "Unable to create a remote fetch configuration. " +
                        "Input value for %s is Invalid or Missing."),
        ERROR_CODE_RE_CONFIG_NOT_FOUND("60002", "Resource not found.",
                "Unable to find a resource matching the provided " +
                        "remote fetch configuration identifier %s."),
        ERROR_CODE_COMMIT_NOT_FOUND("60003", "Commit not found.",
                "Unable to find commit params.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return REMOTE_FETCH_CONFIGURATION_MANAGEMENT_PREFIX + code;
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
