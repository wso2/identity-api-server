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

    public static final String CONFIG_ERROR_PREFIX = "CNF-";
    public static final String CONFIGS_AUTHENTICATOR_PATH_COMPONENT = "/configs/authenticators";
    public static final String PATH_SEPERATOR = "/";

    // PATCH operation paths.
    public static final String IDLE_SESSION_PATH = "/idleSessionTimeoutPeriod";
    public static final String REMEMBER_ME_PATH = "/rememberMePeriod";
    public static final String HOME_REALM_PATH_REGEX = "/homeRealmIdentifiers/[0-9]+";
    public static final String SIGNING_KEY_ALIAS = "/signingKeyAlias";

    /**
     * PATCH operation path for CORS configuration.
     */
    public static final String CORS_CONFIG_ALLOW_GENERIC_HTTP_PATH_REGEX = "^/allowGenericHttpRequests$";
    public static final String CORS_CONFIG_ALLOW_ANY_ORIGIN_PATH_REGEX = "^/allowAnyOrigin";
    public static final String CORS_CONFIG_ALLOW_SUBDOMAINS_PATH_REGEX = "^/allowSubdomains$";
    public static final String CORS_CONFIG_SUPPORTED_METHODS_PATH_REGEX = "^/supportedMethods$";
    public static final String CORS_CONFIG_SUPPORT_ANY_HEADER_PATH_REGEX = "^/supportAnyHeader";
    public static final String CORS_CONFIG_SUPPORTED_HEADERS_PATH_REGEX = "^/supportedHeaders$";
    public static final String CORS_CONFIG_EXPOSED_HEADERS_PATH_REGEX = "^/exposedHeaders$";
    public static final String CORS_CONFIG_SUPPORTS_CREDENTIALS_PATH_REGEX = "^/supportsCredentials$";
    public static final String CORS_CONFIG_MAX_AGE_PATH_REGEX = "^/maxAge$";

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
        ERROR_CODE_AUTHENTICATOR_NOT_FOUND("60002",
                "Resource not found.",
                "Unable to find a resource matching the provided authenticator identifier %s."),
        ERROR_CODE_INVALID_INPUT("60003",
                "Invalid input.",
                "One of the given inputs is invalid. %s."),
        ERROR_CODE_ERROR_UPDATING_CONFIGS("65004",
                "Unable to update server configs.",
                "Server encountered an " +
                "error while updating the server configs."),
        ERROR_CODE_ERROR_RETRIEVING_CONFIGS("65005",
                "Unable to retrieve server configs.",
                "Server encountered an " +
                "error while retrieving the server configs."),

        /**
         * CORS errors.
         */
        ERROR_CODE_CORS_CONFIG_RETRIEVE("65001",
                "Unable to retrieve CORS configuration.",
                "Server encountered an error while retrieving the CORS configuration."),
        ERROR_CODE_CORS_CONFIG_UPDATE("65002",
                "Unable to update CORS configuration.",
                "Server encountered an error while updating the CORS configuration.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String code() {

            return CONFIG_ERROR_PREFIX + code;
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
