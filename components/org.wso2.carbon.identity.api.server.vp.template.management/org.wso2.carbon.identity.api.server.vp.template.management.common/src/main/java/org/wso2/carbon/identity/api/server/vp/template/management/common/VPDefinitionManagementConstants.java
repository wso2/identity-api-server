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

package org.wso2.carbon.identity.api.server.vp.template.management.common;

/**
 * Constants for Presentation Definition Management API.
 */
public class VPDefinitionManagementConstants {

    private VPDefinitionManagementConstants() {

    }

    public static final String VP_DEFINITION_MANAGEMENT_PATH_COMPONENT = "/openid4vp/presentation-definitions";
    public static final String IDENTITY_PROVIDER_PATH_COMPONENT = "/api/server/v1/identity-providers/";
    public static final String PATH_SEPARATOR = "/";
    public static final Integer DEFAULT_LIMIT = 10;
    public static final Integer MAX_LIMIT = 100;
    public static final String ASC_SORT_ORDER = "ASC";
    public static final String DESC_SORT_ORDER = "DESC";
    public static final String DEFAULT_CREDENTIAL_FORMAT = "dc+sd-jwt";
    public static final String DEFAULT_KEY_RESOLUTION_METHOD = "x5c";
    public static final String PARAM_LIMIT = "?limit=";
    public static final String PARAM_FILTER = "&filter=";
    public static final String PARAM_BEFORE = "&before=";
    public static final String PARAM_AFTER = "&after=";
    public static final String LINK_REL_PREVIOUS = "previous";
    public static final String LINK_REL_NEXT = "next";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        // Client errors (60xxx).
        ERROR_CODE_DEFINITION_NOT_FOUND("VPD-60001", "Presentation definition not found.",
                "Unable to find a presentation definition with the id: %s"),
        ERROR_CODE_INVALID_INPUT("VPD-60002", "Invalid input.",
                "Provided input is invalid. %s"),
        ERROR_CODE_DEFINITION_ALREADY_EXISTS("VPD-60003", "Presentation definition already exists.",
                "A presentation definition with the given identifier already exists."),
        ERROR_CODE_DEFINITION_IN_USE("VPD-60004", "Presentation definition is in use.",
                "The presentation definition '%s' is referenced by one or more connections and cannot be deleted."),
        ERROR_CODE_INVALID_PATCH_REQUEST("VPD-60005", "Invalid certificate patch request.", "%s"),
        ERROR_CODE_FEATURE_DISABLED("VPD-60006", "OpenID4VP feature is not enabled.",
                "The OpenID4VP feature is disabled. Enable it via [openid4vp] enabled=true in deployment.toml."),

        // Server errors (65xxx).
        ERROR_CODE_ERROR_RETRIEVING_CONNECTED_CONNECTIONS("VPD-65006",
                "Error retrieving connected connections.",
                "A system error occurred while retrieving connections for presentation definition with id: %s"),
        ERROR_CODE_ERROR_LISTING_DEFINITIONS("VPD-65001", "Error listing presentation definitions.",
                "A system error occurred while listing presentation definitions."),
        ERROR_CODE_ERROR_CREATING_DEFINITION("VPD-65002", "Error creating presentation definition.",
                "A system error occurred while creating the presentation definition."),
        ERROR_CODE_ERROR_RETRIEVING_DEFINITION("VPD-65003", "Error retrieving presentation definition.",
                "A system error occurred while retrieving the presentation definition with id: %s"),
        ERROR_CODE_ERROR_UPDATING_DEFINITION("VPD-65004", "Error updating presentation definition.",
                "A system error occurred while updating the presentation definition with id: %s"),
        ERROR_CODE_ERROR_DELETING_DEFINITION("VPD-65005", "Error deleting presentation definition.",
                "A system error occurred while deleting the presentation definition with id: %s");

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
