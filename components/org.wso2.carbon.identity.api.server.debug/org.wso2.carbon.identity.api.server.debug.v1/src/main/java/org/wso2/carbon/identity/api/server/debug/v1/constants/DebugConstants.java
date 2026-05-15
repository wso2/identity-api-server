/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.debug.v1.constants;

/**
 * Contains constants for Debug API.
 */
public class DebugConstants {

    public static final String ERROR_CODE_PREFIX = "DSM-";
    public static final String CONNECTION_ID = "connectionId";

    private DebugConstants() {
        // Prevent instantiation.
    }

    /**
     * Resource type constants for debug operations.
     */
    public static final class ResourceType {

        public static final String IDP = "idp";

        private ResourceType() {
            // Prevent instantiation.
        }
    }


    /**
     * Error constants for debug flow.
     */
    public enum ErrorMessage {

        // Client error codes.
        ERROR_CODE_ERROR_VALIDATING_REQUEST("60001", "Invalid request.",
                "Debug request validation failed."),
        ERROR_CODE_RESULT_NOT_FOUND("60002", "Debug result not found.",
                "No debug result exists for the provided debug id."),

        // Server error codes.
        ERROR_CODE_ERROR_PROCESSING_REQUEST("65001", "Error processing request.",
                "Error occurred while processing the debug request.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return ERROR_CODE_PREFIX + code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }

        @Override
        public String toString() {

            return code + " | " + description;
        }
    }
}
