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

package org.wso2.carbon.identity.api.server.device.policy.common;

/**
 * Constants for the Device Policy API.
 */
public class Constants {

    private Constants() {
    }

    public static final String DEVICE_POLICY_ERROR_PREFIX = "DPM-";

    public static final String V1_API_PATH_COMPONENT = "/v1";

    public static final String DEVICE_POLICY_PATH_COMPONENT = "/device-policies";

    /**
     * Error messages for the Device Policy API.
     */
    public enum ErrorMessage {

        ERROR_CODE_INVALID_PLATFORM("60001",
                "Invalid platform.",
                "Supported platforms: android, ios, macos, windows."),
        ERROR_CODE_ERROR_RETRIEVING_METADATA("65001",
                "Unable to retrieve device policy metadata.",
                "Server encountered an error while retrieving device policy field metadata.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        /**
         * Returns the error code with prefix.
         *
         * @return Error code.
         */
        public String code() {

            return DEVICE_POLICY_ERROR_PREFIX + code;
        }

        /**
         * Returns the error message.
         *
         * @return Error message.
         */
        public String message() {

            return message;
        }

        /**
         * Returns the error description.
         *
         * @return Error description.
         */
        public String description() {

            return description;
        }
    }
}
