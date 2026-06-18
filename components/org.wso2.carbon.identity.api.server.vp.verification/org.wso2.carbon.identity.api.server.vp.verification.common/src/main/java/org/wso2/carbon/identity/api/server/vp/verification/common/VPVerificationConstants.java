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

    public static final String VP_VERIFICATION_INITIATE_PATH = "/api/server/v1/vp/verification/initiate";
    public static final String VP_VERIFICATION_STATUS_PATH = "/api/server/v1/vp/verification/status";

    private VPVerificationConstants() {
    }

    /**
     * Error messages for VP Verification API.
     */
    public enum ErrorMessage {

        ERROR_CODE_INVALID_REQUEST("VPV-60001", "Invalid request."),
        ERROR_CODE_SESSION_NOT_FOUND("VPV-60401", "Verification session not found."),
        ERROR_CODE_INTERNAL_ERROR("VPV-65001", "Internal server error."),
        ERROR_CODE_SERVICE_UNAVAILABLE("VPV-65002",
                "StandaloneVerificationService is not available.");

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
