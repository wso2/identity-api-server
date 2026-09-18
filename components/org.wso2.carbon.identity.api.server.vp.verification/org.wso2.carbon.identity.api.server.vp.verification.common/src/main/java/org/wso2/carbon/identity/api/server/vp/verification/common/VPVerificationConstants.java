/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.vp.verification.common;

/**
 * Constants for the VP Verification REST API.
 */
public class VPVerificationConstants {

    public static final String CREDENTIAL_VERIFICATIONS_PATH = "/v1/openid4vp/vc-verifications";
    public static final String CLIENT_ERROR_CODE_PREFIX = "VPA-4";

    private VPVerificationConstants() {

    }

    /**
     * Error messages for VP Verification API.
     */
    public enum ErrorMessage {

        ERROR_CODE_INVALID_REQUEST("VPV-60001", "Invalid request."),
        ERROR_CODE_SESSION_NOT_FOUND("VPV-60401", "Verification session not found."),
        ERROR_CODE_DEFINITION_NOT_FOUND("VPV-60404", "Presentation definition not found."),
        ERROR_CODE_FEATURE_DISABLED("VPV-60501", "OpenID4VP feature is not enabled."),
        ERROR_CODE_INTERNAL_ERROR("VPV-65001", "Internal server error."),
        ERROR_CODE_SERVICE_UNAVAILABLE("VPV-65002",
                "VPVerificationService is not available.");

        private final String code;
        private final String message;

        ErrorMessage(String code, String message) {

            this.code = code;
            this.message = message;
        }

        public String getCode() {

            return code;
        }

        public String getMessage() {

            return message;
        }
    }
}
