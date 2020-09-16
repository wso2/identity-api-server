/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.server.cors.common;

/**
 * Server configuration Management constant class.
 */
public class Constants {

    private Constants() {

    }

    public static final String CORS_ERROR_PREFIX = "CRS-";
    public static final String PATH_SEPERATOR = "/";

    /**
     * PATCH operation path for CORS origins.
     */
    public static final String CORS_ORIGIN_PATH_REGEX = "/id/^[a-z0-9-]+$";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        ERROR_CODE_INVALID_CORS_ORIGIN_ID("60001",
                "Invalid CORS origin ID.",
                "%s is not a valid CORS origin ID."),

        /**
         * CORS errors.
         */
        ERROR_CODE_CORS_RETRIEVE("65001",
                "Unable to retrieve CORS origins.",
                "Server encountered an error while retrieving the CORS origins.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String code() {

            return CORS_ERROR_PREFIX + code;
        }

        public String message() {

            return message;
        }

        public String description() {

            return description;
        }

        @Override
        public String toString() {

            return code + " | " + message;
        }
    }
}
