/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.server.configs.common;

/**
 * Server configuration Management constant class.
 */
public class Constants {

    private Constants() {

    }

    public static final String CONFIG_PREFIX = "CNF-";
    public static final String CONFIGS_PATH_COMPONENT = "/configs";
    public static final String PATH_SEPERATOR = "/";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_LISTING_AUTHENTICATORS("60021",
                "Unable to list existing authenticators in the server.",
                "Server encountered an error while listing the authenticators."),
        ERROR_CODE_ERROR_RETRIEVING_AUTHENTICATOR("65003",
                "Unable to retrieve authenticator.",
                "Server encountered an error while retrieving the authenticator for the identifier %s."),
        ERROR_CODE_AUTHENTICATOR_NOT_FOUND("60002", "Resource not found.",
                "Unable to find a resource matching the provided authenticator identifier %s.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String code() {

            return CONFIG_PREFIX + code;
        }

        public String message() {

            return message;
        }

        public String description() {

            return description;
        }

        @Override
        public String toString() {

            return code + " | " + message;
        }
    }
}
