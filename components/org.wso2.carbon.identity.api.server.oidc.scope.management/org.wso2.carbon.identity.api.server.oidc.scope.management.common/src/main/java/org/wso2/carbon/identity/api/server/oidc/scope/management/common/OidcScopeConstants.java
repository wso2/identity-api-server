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
package org.wso2.carbon.identity.api.server.oidc.scope.management.common;

/**
 * Contains all the OIDC Scope Management Service related constants.
 */
public class OidcScopeConstants {

    public static final String OIDC_SCOPE_API_PATH_COMPONENT = "/oidc/scopes";
    public static final String PATH_SEPERATOR = "/";

    /**
     * Enum for OIDC scope management service related errors.
     */
    public enum ErrorMessage {

        INVALID_REQUEST("OAUTH-60001", "Invalid Request."),
        ERROR_CONFLICT_REQUEST("41004", "Scope already exists."),
        SCOPE_NOT_FOUND("41003", "Scope not found.");

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

        @Override
        public String toString() {

            return code + " : " + message;
        }
    }
}
