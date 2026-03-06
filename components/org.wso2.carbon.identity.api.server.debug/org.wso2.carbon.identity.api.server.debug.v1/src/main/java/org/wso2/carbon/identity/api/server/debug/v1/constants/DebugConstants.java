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
 * Contains constants for Debug Flow Data Provider API.
 */
public final class DebugConstants {

    private DebugConstants() {
        // Prevent instantiation.
    }

    /**
     * Status constants for debug operations.
     */
    public static final class Status {

        public static final String SUCCESS = "SUCCESS";
        public static final String FAILURE = "FAILURE";
        public static final String IN_PROGRESS = "IN_PROGRESS";
        public static final String DIRECT_RESULT = "DIRECT_RESULT";

        private Status() {
            // Prevent instantiation.
        }
    }

    /**
     * Request property keys used by debug API.
     */
    public static final class RequestKeys {

        public static final String CONNECTION_ID = "connectionId";

        private RequestKeys() {
            // Prevent instantiation.
        }
    }

    /**
     * Resource type constants for debug operations.
     */
    public static final class ResourceType {

        public static final String IDP = "idp";
        public static final String FRAUD_DETECTION = "fraud_detection";

        private ResourceType() {
            // Prevent instantiation.
        }
    }

    /**
     * Keys expected in responses emitted by the debug framework.
     */
    public static final class ResponseKeys {

        public static final String DEBUG_ID = "debugId";
        public static final String STATE = "state";
        public static final String SUCCESS = "success";
        public static final String STATUS = "status";
        public static final String MESSAGE = "message";
        public static final String AUTHORIZATION_URL = "authorizationUrl";
        public static final String TIMESTAMP = "timestamp";

        private ResponseKeys() {
            // Prevent instantiation.
        }
    }

    /**
     * Error constants for debug flow.
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_VALIDATING_REQUEST("10001", "Invalid request.",
                "Request validation failed."),
        ERROR_CODE_ERROR_PROCESSING_REQUEST("10005", "Error processing request.",
                "Error occurred while processing the debug request."),
        ERROR_CODE_RESULT_NOT_FOUND("10011", "Debug result not found.",
                "No debug result exists for the provided session id.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return code;
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
