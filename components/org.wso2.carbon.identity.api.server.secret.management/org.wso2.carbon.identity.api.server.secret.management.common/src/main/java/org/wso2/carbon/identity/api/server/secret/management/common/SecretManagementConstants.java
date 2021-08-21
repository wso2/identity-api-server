/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org).
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

package org.wso2.carbon.identity.api.server.secret.management.common;

/**
 * Secret management related constant class.
 */
public class SecretManagementConstants {

    public static final String SECRET_CONTEXT_PATH = "/secrets";
    public static final String CONFIG_MGT_ERROR_CODE_DELIMITER = "_";
    public static final String CORRELATION_ID_MDC = "Correlation-ID";
    public static final String V1_API_PATH_COMPONENT = "/v1";

    /**
     * Enums for error messages.
     */
    public enum ErrorMessage {

        // Client errors 600xx.
        ERROR_CODE_CONFLICT_SECRET("SECRETM_00006", "Secret already exists.", "There exists a " +
                "secret: %s in the tenant."),
        ERROR_CODE_SECRET_NOT_FOUND("SECRETM_00009", "Secret not found.", "Secret with the " +
                "name: %s does not exists."),
        ERROR_CODE_CONFLICT_SECRET_TYPE("SECRETM_00019", "Secret type already exists.",
                "Secret type with the name: %s already exists."),
        ERROR_CODE_SECRET_TYPE_NOT_FOUND("SECRETM_00021", "Secret type not found.",
                "Secret type with the name: %s does not exists."),

        ERROR_CODE_REFERENCE_NAME_NOT_SPECIFIED("60001", "Empty reference name",
                "Secret reference name is not specified in the request"),
        ERROR_CODE_SECRET_KEY_NOT_SPECIFIED("60002", "Empty key",
                "Secret key is not specified in the request"),
        ERROR_CODE_SECRET_VALUE_NOT_SPECIFIED("60003", "Empty value",
                "Secret value is not specified in the request"),

        // Server errors 650xx.
        ERROR_CODE_ERROR_GETTING_SECRET("65003", "Error while getting secret.",
                "Error while retrieving secret for for the name: %s."),
        ERROR_CODE_ERROR_ADDING_SECRET("65004", "Unable to add the secret.",
                "Server encountered an error while adding the secret: %s"),
        ERROR_CODE_ERROR_DELETING_SECRET("65005", "Unable to delete the secret.",
                "Server encountered an error while deleting the secret: %s"),
        ERROR_CODE_ERROR_GETTING_SECRET_BY_NAME("65006", "Error while getting secret.",
                "Error while retrieving %s secret."),
        ERROR_CODE_ERROR_GETTING_SECRETS("65007", "Error while getting secrets.",
                "Error while retrieving secrets."),
        ERROR_CODE_ERROR_UPDATING_SECRET("65008", "Unable to update the secret.",
                "Error while updating secret: %s.");

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

            return code + " | " + message;
        }
    }
}
