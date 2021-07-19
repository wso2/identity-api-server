/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.endpoint.configuration.management.common;


/**
 * Endpoint configuration management related constant class.
 */
public class EndpointConfigurationManagementConstants {

    public static final String ERROR_PREFIX = "NSM-";

    public static final String CONFIGURATION_CONTEXT_PATH = "/endpoint-configurations";
    public static final String CONFIGURATION_RESOURCE_TYPE = "endpoint-configuration";
    public static final String PLUS = "+";
    public static final String URL_ENCODED_SPACE = "%20";
    public static final String RESOURCE_NOT_EXISTS_ERROR_CODE = "CONFIGM_00017";
    public static final String CONFIG_MGT_ERROR_CODE_DELIMITER = "_";
    public static final String CORRELATION_ID_MDC = "Correlation-ID";
    public static final String SERVER_API_PATH_COMPONENT = "/api/server";
    public static final String V1_API_PATH_COMPONENT = "/v1";

    public static final String TENANT_NAME_FROM_CONTEXT = "TenantNameFromContext";
    public static final String TENANT_CONTEXT_PATH_COMPONENT = "/t/%s";

    // endpoint configuration main properties.
    public static final String ENDPOINT_URL = "endPointURL";
    public static final String AUTH_TYPE = "authType";

    /**
     * Enums for error messages.
     */
    public enum ErrorMessage {

        ERROR_CODE_ENDPOINT_URI_NOT_SPECIFIED("CMT-60001", "Empty endpoint URI", "Endpoint URI is " +
                "not specified in the request"),
        ERROR_CODE_REFERENCE_NAME_NOT_SPECIFIED("CMT-60002", "Empty reference name",
                "Endpoint reference name is not specified in the request"),
        ERROR_CODE_AUTH_TYPE_URI_NOT_SPECIFIED("CMT-60003", "Empty auth type", "Auth type is " +
                "not specified in the request"),
        // Client errors 600xx.
        ERROR_CODE_CONFLICT_CONFIGURATION("60002", "Configuration already exists.",
                "There exists a endpoint Configuration: %s in the tenant."),

        // Server errors 650xx.
        ERROR_CODE_ERROR_GETTING_ENDPOINT_CONFIGURATION("65003", "Error while getting endpoint configurations.",
                "Error while retrieving endpoint configurations for resource: %s."),
        ERROR_CODE_ERROR_ADDING_ENDPOINT_CONFIGURATION("65004", "Unable to add endpoint configuration.",
                "Server encountered an error while adding the endpoint configuration: %s"),
        ERROR_CODE_ERROR_DELETING_ENDPOINT_CONFIGURATION("65005", "Unable to delete endpoint configuration.",
                "Server encountered an error while deleting the endpoint configuration: %s"),
        ERROR_CODE_ERROR_GETTING_ENDPOINT_CONFIGURATION_BY_TYPE("65006", "Error while getting endpoint configuration.",
                "Error while retrieving %s endpoint configuration."),
        ERROR_CODE_ERROR_UPDATING_ENDPOINT_CONFIGURATION("65007", "Unable to update endpoint configuration.",
                "Error while updating endpoint configuration: %s.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return ERROR_PREFIX + code;
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
