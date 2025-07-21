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

package org.wso2.carbon.identity.api.server.identity.governance.common;

/**
 * Contains all the identity governance related constants.
 */
public class GovernanceConstants {

    private static final String IDENTITY_GOVERNANCE = "IDG-";
    public static final String REL_CATEGORY = "category";
    public static final String REL_CONNECTOR = "connector";
    public static final String IDENTITY_GOVERNANCE_PATH_COMPONENT = "/identity-governance";

    /**
     * Enum for identity governance related errors in the format of
     * Error Code - code to identify the error
     * Error Message - What went wrong
     * Error Description - Why it went wrong
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_RETRIEVING_CATEGORIES("50001",
                "Unable to get the identity governance categories.",
                "Server Encountered an error while retrieving identity governance categories."),
        ERROR_CODE_ERROR_RETRIEVING_CATEGORY("50002",
                "Unable to get the identity governance category.",
                "Server Encountered an error while retrieving identity governance category."),
        ERROR_CODE_ERROR_RETRIEVING_CONNECTOR("50003",
                "Unable to get the identity governance connector.",
                "Server Encountered an error while retrieving identity governance connector."),
        ERROR_CODE_ERROR_UPDATING_CONNECTOR_PROPERTY("50004",
                "Unable to update the identity governance connector property.",
                "Server Encountered an error while updating identity governance connector property."),
        ERROR_CODE_PAGINATION_NOT_IMPLEMENTED("50005",
                "Pagination not supported.",
                "Pagination capabilities are not supported in this version of the API."),
        ERROR_CODE_FILTERING_NOT_IMPLEMENTED("50006",
                "Filtering not supported.",
                "Filtering capability is not supported in this version of the API."),
        ERROR_CODE_SORTING_NOT_IMPLEMENTED("50007",
                "Sorting not supported.",
                "Sorting capability is not supported in this version of the API."),
        ERROR_CODE_CATEGORY_NOT_FOUND("50008",
                "Resource not found.",
                "Unable to find any category with the provided identifier %s."),
        ERROR_CODE_CONNECTOR_NOT_FOUND("50009",
                "Resource not found.",
                "Unable to find any connector with the provided identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_CONNECTOR_PREFERENCES("50010",
                "Unable to get the identity governance preferences.",
                "Server Encountered an error while retrieving identity governance connector preferences."),
        ERROR_CODE_INCORRECT_CONNECTOR_NAME("50011", "Invalid connector name",
                "Unable to find a connector with the name %s."),
        ERROR_CODE_UNSUPPORTED_PROPERTY_NAME("50012", "Unsupported property is requested.",
                "The property %s is not supported by this API."),
        ERROR_CODE_INVALID_CONNECTOR_CONFIGURATION("50013", "Connector update failed.",
                "Unable to update the identity governance connector %s."),
        ERROR_CODE_INVALID_PASSWORD_EXPIRY_RULE("50014", "Connector update failed.",
                "Password expiry rule: %s is invalid."),
        ERROR_CODE_INVALID_PASSWORD_PATTERN_REGEX("50015", "Connector update failed.",
                "Password pattern regex is invalid."),
        ERROR_CODE_NO_PROPERTIES_TO_REVERT("50016", "No properties to revert.",
                "No properties were provided to revert."),
        ERROR_CODE_PROPERTY_NOT_FOUND("50017", "Property not found.",
                "The property: %s is not found in the connector: %s."),
        ERROR_CODE_ERROR_REVERTING_CONNECTOR_PROPERTY("50018",
                "Unable to revert the identity governance connector properties.",
                "Server Encountered an error while reverting identity governance connector properties.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return IDENTITY_GOVERNANCE + code;
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
