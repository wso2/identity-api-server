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

package org.wso2.carbon.identity.api.server.extension.management.common.utils;

/**
 * Constants for extension management.
 */
public class ExtensionMgtConstants {

    public static final String EXTENSION_MGT_PATH_COMPONENT = "/extensions";

    public static final String EXTENSION_TEMPLATE_PATH_COMPONENT = "/templates";

    public static final String EXTENSION_METADATA_PATH_COMPONENT = "/metadata";

    public static final String EXTENSION_MANAGEMENT_PREFIX = "EXT-";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        // Client errors starting from 600xx.
        ERROR_CODE_EXTENSION_NOT_FOUND("60001", "Resource not found.", "Unable to find a resource matching the " +
                "provided extension id: %s, extension type: %s."),
        ERROR_CODE_TEMPLATE_NOT_FOUND("60002", "Resource not found.", "Unable to find a resource matching the " +
                "provided extension id: %s, extension type: %s."),
        ERROR_CODE_METADATA_NOT_FOUND("60003", "Resource not found.", "Unable to find a resource matching the " +
                "provided extension id: %s, extension type: %s."),
        ERROR_CODE_INVALID_EXTENSION_TYPE("60003", "Invalid extension type.", "Provided extension type: %s is " +
                "invalid."),

        // Server Error starting from 650xx.
        ERROR_CODE_ERROR_GETTING_EXTENSION("65001", "Unable to get extension.",
                "Server encountered an error while getting the extension. Extension id: %s, " +
                        "Extension type: %s."),
        ERROR_CODE_ERROR_GETTING_EXTENSIONS("65002", "Unable to get extension.",
                "Server encountered an error while " +
                "getting the extension."),
        ERROR_CODE_ERROR_GETTING_EXTENSIONS_BY_TYPE("65003", "Unable to get extension.",
                "Server encountered an error while " +
                "getting the extension. Extension type: %s."),
        ERROR_CODE_ERROR_GETTING_TEMPLATE("65004", "Unable to get extension templates.",
                "Server encountered an error while getting the extension templates. Extension id: %s, " +
                        "Extension type: %s."),
        ERROR_CODE_ERROR_GETTING_METADATA("65005", "Unable to get extension metadata.",
                "Server encountered an error while getting the extension metadata. Extension id: %s, " +
                        "Extension type: %s.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return EXTENSION_MANAGEMENT_PREFIX + code;
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
