/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.common;

import static org.wso2.carbon.identity.api.server.common.Constants.ErrorPrefix.USER_MANAGEMENT_PREFIX;

/**
 * Common constants for server APIs.
 */
public class Constants {
    public static final String OPERATION_ADD = "ADD";
    public static final String TENANT_NAME_FROM_CONTEXT = "TenantNameFromContext";
    public static final String ERROR_CODE_DELIMITER = "-";
    public static final String CORRELATION_ID_MDC = "Correlation-ID";
    public static final String SERVER_API_PATH_COMPONENT = "/api/server";
    public static final String V1_API_PATH_COMPONENT = "/v1";
    public static final String TENANT_CONTEXT_PATH_COMPONENT = "/t/%s";
    public static final String ORGANIZATION_CONTEXT_PATH_COMPONENT = "/o/%s";
    public static final String ERROR_CODE_RESOURCE_LIMIT_REACHED = "RLS-10001";

    public static final String REGEX_COMMA = ",";

    // Export and Import related constants.
    public static final String YAML_FILE_EXTENSION = ".yml";
    public static final String JSON_FILE_EXTENSION = ".json";
    public static final String XML_FILE_EXTENSION = ".xml";
    public static final String MEDIA_TYPE_JSON = "json";
    public static final String MEDIA_TYPE_XML = "xml";
    public static final String MEDIA_TYPE_YAML = "yaml";
    public static final String MEDIA_TYPE_UNSUPPORTED = "unsupported";
    public static final String MASKING_VALUE = "********";
    static final String[] VALID_MEDIA_TYPES_XML = {"application/xml", "text/xml"};
    static final String[] VALID_MEDIA_TYPES_YAML = {"application/yaml", "text/yaml", "application/x-yaml"};
    static final String[] VALID_MEDIA_TYPES_JSON = {"application/json", "text/json"};

    /**
     * Enum for server error prefixes.
     */
    public enum ErrorPrefix {

        USER_MANAGEMENT_PREFIX("UMG-");

        private final String prefix;

        ErrorPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    /**
     * Enum for common server error messages.
     */
    public enum ErrorMessages {

        ERROR_CODE_INVALID_USERNAME(USER_MANAGEMENT_PREFIX.getPrefix() + "10001", "Invalid UserID provided", "The " +
                "provided userId is invalid.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessages(String code, String message, String description) {
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
            return code + " | " + message;
        }

    }

}
