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

package org.wso2.carbon.identity.api.server.vc.template.management.common;

/**
 * Constants related to VC template management.
 */
public final class VCTemplateManagementConstants {

    private VCTemplateManagementConstants() {

    }

    public static final String VC_TEMPLATE_PATH_COMPONENT = "vc-templates";
    public static final String PATH_SEPARATOR = "/";
    public static final Integer DEFAULT_LIMIT = 10;
    public static final String ASC_SORT_ORDER = "ASC";
    public static final String DESC_SORT_ORDER = "DESC";

    /**
     * Error message codes and default messages used in VC config management.
     */
    public enum ErrorMessages {
        // Client errors
        ERROR_CODE_TEMPLATE_ID_MISMATCH("VCM-60001", "Template id path and payload mismatch.",
                "Template id in the path does not match the id in the request payload."),
        ERROR_CODE_IDENTIFIER_ALREADY_EXISTS("VCM-60002", "Template with the same identifier already exists.",
                "A verifiable credential template with the provided identifier already exists in the system."),
        ERROR_CODE_EMPTY_FIELD("VCM-60003", "Invalid request.",
                "%s cannot be empty."),
        ERROR_CODE_INVALID_FIELD("VCM-60004", "Invalid request.",
                "%s is invalid."),
        ERROR_CODE_TEMPLATE_NOT_FOUND("VCM-60005", "Template not found.",
                "Verifiable credential template with the given identifier does not exist."),
        ERROR_CODE_UNSUPPORTED_VC_FORMAT("VCM-60006", "Unsupported verifiable credential format.",
                "The specified verifiable credential format is not supported."),
        ERROR_CODE_OFFER_NOT_FOUND("VCM-60007", "Offer not found.",
                "Credential offer for the specified template does not exist."),
        ERROR_CODE_INVALID_EXPIRY("VCM-60008", "Invalid expiry value.",
                "Expiry must be at least %d seconds."),
        ERROR_CODE_INVALID_CLAIM("VCM-60009", "Invalid claim.",
                "Invalid claim: %s"),
        ERROR_CODE_INVALID_QUERY_PARAM("VCM-60010", "Invalid query parameter.",
                "Invalid query parameter : %s"),

        // Server errors
        ERROR_CODE_PERSISTENCE_ERROR("VCM-65001", "Error while persisting template.",
                "An error occurred while storing the verifiable credential template in the database."),
        ERROR_CODE_RETRIEVAL_ERROR("VCM-65002", "Error while retrieving template.",
                "An error occurred while fetching the verifiable credential template from the database."),
        ERROR_CODE_DELETION_ERROR("VCM-65003", "Error while deleting template.",
                "An error occurred while removing the verifiable credential template from the database."),
        ERROR_CODE_TRANSACTION_ERROR("VCM-65004", "Error in database transaction.",
                "A database transaction error occurred while processing the verifiable credential template."),
        ERROR_CODE_CLAIM_VALIDATION_ERROR("VCM-65005", "Error while validating claims.",
                "Error while validating claims.");

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
    }
}

