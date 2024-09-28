/*
 * Copyright (c) 2021-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.branding.preference.management.common;

/**
 * Branding preference management related constant class.
 */
public class BrandingPreferenceManagementConstants {

    public static final String BRANDING_PREFERENCE_ERROR_PREFIX = "BPM-";
    public static final String BRANDING_PREFERENCE_CONTEXT_PATH = "/branding-preference";
    public static final String CUSTOM_TEXT_PREFERENCE_CONTEXT_PATH = "/branding-preference/text";
    public static final String QUERY_PARAM_INDICATOR = "?";
    public static final String GET_PREFERENCE_COMPONENT_WITH_QUERY_PARAM = "type=%s&name=%s&locale=%s";
    public static final String GET_CUSTOM_TEXT_COMPONENT_WITH_QUERY_PARAM = "type=%s&name=%s&screen=%s&locale=%s";
    public static final String ORGANIZATION_TYPE = "ORG";
    public static final String APPLICATION_TYPE = "APP";
    public static final String CUSTOM_TYPE = "CUSTOM";
    public static final String DEFAULT_LOCALE = "en-US";
    public static final String BRANDING_PREFERENCE_MGT_ERROR_CODE_DELIMITER = "_";

    public static final String BRANDING_PREFERENCE_NOT_EXISTS_ERROR_CODE = "BRANDINGM_00002";
    public static final String BRANDING_PREFERENCE_ALREADY_EXISTS_ERROR_CODE = "BRANDINGM_00003";
    public static final String BRANDING_PREFERENCE_NOT_ALLOWED_ERROR_CODE = "BRANDINGM_00011";
    public static final String CUSTOM_TEXT_PREFERENCE_NOT_EXISTS_ERROR_CODE = "BRANDINGM_00023";
    public static final String CUSTOM_TEXT_PREFERENCE_ALREADY_EXISTS_ERROR_CODE = "BRANDINGM_00024";

    /**
     * Enums for error messages.
     */
    public enum ErrorMessage {

        // Client errors 600xx.
        ERROR_CODE_INVALID_BRANDING_PREFERENCE("60001",
                "Invalid Branding Preference configurations.",
                "Invalid Branding Preference configurations in request"),
        ERROR_CODE_BRANDING_PREFERENCE_NOT_EXISTS("60002",
                "Branding preferences are not configured.",
                "Branding preferences are not configured for organization: %s."),
        ERROR_CODE_CONFLICT_BRANDING_PREFERENCE("60003", "Branding preference already exists.",
                "There exists a branding preference configurations in the organization: %s."),
        ERROR_CODE_NOT_ALLOWED_BRANDING_PREFERENCE_CONFIGURATIONS("60004",
                "Not allowed branding preference configurations.",
                "Requested branding preference configuration: %s is not allowed for the organization."),
        ERROR_CODE_INVALID_CUSTOM_TEXT_PREFERENCE("60005",
                "Invalid custom text preference configurations.",
                "Invalid custom text preference configurations in request"),
        ERROR_CODE_CUSTOM_TEXT_PREFERENCE_NOT_EXISTS("60006",
                "Custom text preferences are not configured.",
                "Custom text preferences are not configured for organization: %s."),
        ERROR_CODE_CONFLICT_CUSTOM_TEXT_PREFERENCE("60007", "Custom Text preference already exists.",
                "There exists a custom text preference configurations in the organization: %s."),

        // Server errors 650xx.
        ERROR_CODE_ERROR_GETTING_BRANDING_PREFERENCE("65001",
                "Error while getting branding preference configurations.",
                "Error while retrieving branding preference configurations for organization: %s."),
        ERROR_CODE_ERROR_ADDING_BRANDING_PREFERENCE("65002",
                "Unable to add branding preference configurations.",
                "Server encountered an error while adding the branding preference configurations for organization: %s"),
        ERROR_CODE_ERROR_DELETING_BRANDING_PREFERENCE("65003",
                "Unable to delete branding preference configurations.",
                "Server encountered an error while deleting branding preference configurations for organization: %s"),
        ERROR_CODE_ERROR_UPDATING_BRANDING_PREFERENCE("65004",
                "Unable to update branding preference configurations.",
                "Error while updating branding preference configurations for organization: %s."),
        ERROR_CODE_ERROR_GETTING_CUSTOM_TEXT_PREFERENCE("65005",
                "Error while getting custom text preference configurations.",
                "Error while retrieving custom text preference configurations for organization: %s."),
        ERROR_CODE_ERROR_ADDING_CUSTOM_TEXT_PREFERENCE("65006",
                "Unable to add custom text preference configurations.",
                "Server encountered an error while adding the custom text configurations for organization: %s"),
        ERROR_CODE_ERROR_DELETING_CUSTOM_TEXT_PREFERENCE("65007",
                "Unable to delete custom text preference configurations.",
                "Server encountered an error while deleting custom text configurations for organization: %s"),
        ERROR_CODE_ERROR_UPDATING_CUSTOM_TEXT_PREFERENCE("65008",
                "Unable to update custom text preference configurations.",
                "Error while updating custom text preference configurations for organization: %s."),
        ERROR_CODE_ERROR_GETTING_BRANDING_RESULT_STATUS("65009",
                "Error while getting branding preference generation result status.",
                "Error while retrieving branding preference generation result status for operation."),
        ERROR_CODE_ERROR_GETTING_BRANDING_RESULT("65010",
                "Error while getting branding preference generation result.",
                "Error while retrieving branding preference generation result for operation.");


        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return BRANDING_PREFERENCE_ERROR_PREFIX + code;
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
