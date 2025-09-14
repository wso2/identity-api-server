/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.idp.debug.v1.constants;

/**
 * Constants for DFDP (Debug Flow Data Provider) Debug API.
 */
public class DFDPConstants {

    private DFDPConstants() {
        // Private constructor to prevent instantiation
    }

    // API Constants
    public static final String DEBUG_API_PATH_COMPONENT = "/api/server/v1/debug";
    public static final String DFDP_PREFIX = "DFDP-";

    // DFDP Debug Properties
    public static final String DFDP_ENABLED = "dfdp.enabled";
    public static final String DFDP_TARGET_IDP = "dfdp.target.idp";
    public static final String DFDP_TARGET_AUTHENTICATOR = "dfdp.target.authenticator";
    public static final String DFDP_SESSION_ID = "dfdp.session.id";
    public static final String DFDP_DEBUG_MODE = "dfdp.debug.mode";
    public static final String DFDP_EVENT_CAPTURE = "dfdp.event.capture";

    // Status Constants
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILURE = "FAILURE";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";

    // Event Types
    public static final String EVENT_AUTHENTICATION_STARTED = "DFDP_AUTHENTICATION_STARTED";
    public static final String EVENT_AUTHENTICATION_COMPLETED = "DFDP_AUTHENTICATION_COMPLETED";
    public static final String EVENT_CLAIM_MAPPING = "DFDP_CLAIM_MAPPING";
    public static final String EVENT_ERROR_OCCURRED = "DFDP_ERROR_OCCURRED";
    public static final String EVENT_STEP_SUCCESS = "DFDP_STEP_SUCCESS";
    public static final String EVENT_STEP_FAILURE = "DFDP_STEP_FAILURE";

    // Claim Processing Types
    public static final String CLAIMS_ORIGINAL = "ORIGINAL_CLAIMS";
    public static final String CLAIMS_MAPPED = "MAPPED_CLAIMS";
    public static final String CLAIMS_FILTERED = "FILTERED_CLAIMS";
    public static final String CLAIMS_SYSTEM = "SYSTEM_CLAIMS";

    // Error Codes
    public static final String ERROR_INVALID_REQUEST = "INVALID_REQUEST";
    public static final String ERROR_IDP_NOT_FOUND = "IDP_NOT_FOUND";
    public static final String ERROR_AUTHENTICATOR_NOT_FOUND = "AUTHENTICATOR_NOT_FOUND";
    public static final String ERROR_INVALID_PARAMETERS = "INVALID_PARAMETERS";
    public static final String ERROR_INVALID_SESSION = "INVALID_SESSION";
    public static final String ERROR_SESSION_NOT_FOUND = "SESSION_NOT_FOUND";
    public static final String ERROR_INTERNAL_ERROR = "INTERNAL_ERROR";
    public static final String ERROR_PROCESSING_ERROR = "PROCESSING_ERROR";

    // Session Management
    public static final String SESSION_PREFIX = "dfdp-session-";
    public static final String AUTH_TEST_PREFIX = "dfdp-auth-test-";
    public static final long SESSION_TIMEOUT_MINUTES = 30;

    // Framework Event Mapping
    public static final String FRAMEWORK_EVENT_PRE_AUTHENTICATION = "PRE_AUTHENTICATION";
    public static final String FRAMEWORK_EVENT_POST_AUTHENTICATION = "POST_AUTHENTICATION";
    public static final String FRAMEWORK_EVENT_STEP_SUCCESS = "AUTHENTICATION_STEP_SUCCESS";
    public static final String FRAMEWORK_EVENT_STEP_FAILURE = "AUTHENTICATION_STEP_FAILURE";

    // Claim Mapping Constants
    public static final String CLAIM_USERID = "http://wso2.org/claims/userid";
    public static final String CLAIM_EMAIL = "http://wso2.org/claims/emailaddress";
    public static final String CLAIM_FULLNAME = "http://wso2.org/claims/fullname";
    public static final String CLAIM_GIVENNAME = "http://wso2.org/claims/givenname";
    public static final String CLAIM_LASTNAME = "http://wso2.org/claims/lastname";

    // Remote Claim Mapping
    public static final String REMOTE_CLAIM_SUB = "sub";
    public static final String REMOTE_CLAIM_EMAIL = "email";
    public static final String REMOTE_CLAIM_NAME = "name";
    public static final String REMOTE_CLAIM_GIVEN_NAME = "given_name";
    public static final String REMOTE_CLAIM_FAMILY_NAME = "family_name";

    /**
     * Error message enum for DFDP Debug API.
     */
    public enum ErrorMessage {
        ERROR_CODE_INVALID_DEBUG_REQUEST("60001", "Invalid debug request", 
            "The debug request parameters are invalid: %s"),
        ERROR_CODE_IDP_NOT_FOUND("60002", "Identity Provider not found", 
            "Target Identity Provider not found: %s"),
        ERROR_CODE_AUTHENTICATOR_NOT_FOUND("60003", "Authenticator not found", 
            "Target authenticator not found: %s"),
        ERROR_CODE_SESSION_NOT_FOUND("60004", "Debug session not found", 
            "Debug session not found: %s"),
        ERROR_CODE_PROCESSING_ERROR("65001", "Debug processing error", 
            "Error occurred during debug processing: %s"),
        ERROR_CODE_EVENT_LISTENER_ERROR("65002", "Event listener error", 
            "Error in event listener processing: %s");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return DFDP_PREFIX + code;
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
