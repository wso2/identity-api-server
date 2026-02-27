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
                // Prevent instantiation
        }

        /**
         * Status constants for debug operations.
         */
        public static class Status {
                
                public static final String SUCCESS = "SUCCESS";
                public static final String FAILURE = "FAILURE";
                public static final String IN_PROGRESS = "IN_PROGRESS";
                public static final String DIRECT_RESULT = "DIRECT_RESULT";

                private Status() {
                        // Prevent instantiation
                }
        }

        /**
         * Resource type constants for debug operations.
         */
        public static class ResourceType {

                public static final String IDP = "IDP";
                public static final String FRAUD_DETECTION = "FRAUD_DETECTION";

                private ResourceType() {
                        // Prevent instantiation
                }
        }

        /**
         * Keys expected in responses emitted by the debug framework.
         */
        public static class ResponseKeys {

                public static final String SESSION_ID = "sessionId";
                public static final String STATE = "state";
                public static final String SUCCESS = "success";
                public static final String STATUS = "status";
                public static final String MESSAGE = "message";
                public static final String AUTHORIZATION_URL = "authorizationUrl";
                public static final String TIMESTAMP = "timestamp";

                private ResponseKeys() {
                        // Prevent instantiation
                }
        }

        /**
         * Error constants for debug flow.
         */
        public enum ErrorMessage {

                ERROR_CODE_ERROR_VALIDATING_REQUEST("10001", "Invalid request.",
                                "Request validation failed."),
                ERROR_CODE_ERROR_TESTING_IDP("10002", "IdP testing failed.",
                                "Error occurred while testing the identity provider."),
                ERROR_CODE_ERROR_RETRIEVING_IDPS("10003", "Error retrieving identity providers.",
                                "Error occurred while retrieving identity providers."),
                ERROR_CODE_ERROR_RETRIEVING_AUTHENTICATORS("10004", "Error retrieving authenticators.",
                                "Error occurred while retrieving authenticators for identity provider."),
                ERROR_CODE_ERROR_PROCESSING_REQUEST("10005", "Error processing request.",
                                "Error occurred while processing the Debug request."),
                ERROR_CODE_INVALID_IDP("10006", "Invalid identity provider.",
                                "The specified identity provider does not exist."),
                ERROR_CODE_INVALID_AUTHENTICATOR("10007", "Invalid authenticator.",
                                "The specified authenticator does not exist for the identity provider."),
                ERROR_CODE_AUTHENTICATION_FAILED("10008", "Authentication failed.",
                                "Authentication with the identity provider failed."),
                ERROR_CODE_CLAIMS_EXTRACTION_FAILED("10009", "Claims extraction failed.",
                                "Failed to extract claims from authentication response."),
                ERROR_CODE_UNSUPPORTED_FORMAT("10010", "Unsupported response format.",
                                "The requested response format is not supported."),
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
