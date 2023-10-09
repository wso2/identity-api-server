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

package org.wso2.carbon.identity.api.server.application.tag.management.v1.constants;

/**
 * Constants related to Application Tag management.
 */
public class ApplicationTagMgtEndpointConstants {

    private ApplicationTagMgtEndpointConstants() {
    }

    public static final String APPLICATION_TAG_MANAGEMENT_PREFIX = "APPLICATION-TAG-";
    public static final String APPLICATION_TAG_PATH_COMPONENT = "/applications-tags";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        // Client errors.
        ERROR_CODE_APPLICATION_TAG_NOT_FOUND("60001", "Unable to find the Application Tag.",
                "Unable to find the Application Tag with the id: %s in the tenant domain."),
        ERROR_CODE_INVALID_APPLICATION_TAG_NAME("60002",
                "Invalid Application Tag name provided.", "Application Tag name is required."),
        ERROR_CODE_INVALID_APPLICATION_TAG_COLOUR("60003",
                "Invalid Application Tag colour provided.", "Application Tag colour is required."),
        ERROR_CODE_INVALID_SEARCH_ATTRIBUTE("60007", "Invalid search attribute.",
                "Invalid search attribute: %s."),
        ERROR_CODE_INVALID_LIMIT("60004", "Invalid limit provided.",
                "Limit should be a positive integer."),

        // Server errors.
        ERROR_CODE_ADD_APPLICATION_TAG("65001", "Error while adding application tag.",
                "Server encountered an error while adding the application tag.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return APPLICATION_TAG_MANAGEMENT_PREFIX + code;
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
