/*
 * Copyright (c) 2019-2025, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.idp.debug.common;

/**
 * Contains constants for IdP Debug Flow Data Provider API.
 */
public class Constants {

    public static final String DFDP_ERROR_CODE_DELIMITER = "-";
    public static final String DFDP_ERROR_PREFIX = "DFDP";

    /**
     * API path constants.
     */
    public static class V1 {
        public static final String API_PATH_COMPONENT = "/v1";
        public static final String DFDP_API_PATH_COMPONENT = "/debug";
    }

    /**
     * Error constants for IdP Debug Flow Data Provider.
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
                "Error occurred while processing the DFDP request."),
        ERROR_CODE_INVALID_IDP("10006", "Invalid identity provider.", 
                "The specified identity provider does not exist."),
        ERROR_CODE_INVALID_AUTHENTICATOR("10007", "Invalid authenticator.", 
                "The specified authenticator does not exist for the identity provider."),
        ERROR_CODE_AUTHENTICATION_FAILED("10008", "Authentication failed.", 
                "Authentication with the identity provider failed."),
        ERROR_CODE_CLAIMS_EXTRACTION_FAILED("10009", "Claims extraction failed.", 
                "Failed to extract claims from authentication response."),
        ERROR_CODE_UNSUPPORTED_FORMAT("10010", "Unsupported response format.", 
                "The requested response format is not supported.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return DFDP_ERROR_PREFIX + DFDP_ERROR_CODE_DELIMITER + code;
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

    /**
     * Response format constants.
     */
    public static class ResponseFormat {
        public static final String JSON = "json";
        public static final String HTML = "html";
        public static final String TEXT = "text";
        public static final String SUMMARY = "summary";
    }

    /**
     * Authentication status constants.
     */
    public static class AuthenticationStatus {
        public static final String SUCCESS = "SUCCESS";
        public static final String FAILED = "FAILED";
        public static final String ERROR = "ERROR";
    }

    /**
     * Default values.
     */
    public static class Defaults {
        public static final String DEFAULT_RESPONSE_FORMAT = ResponseFormat.JSON;
        public static final int DEFAULT_TIMEOUT_SECONDS = 30;
    }
}
