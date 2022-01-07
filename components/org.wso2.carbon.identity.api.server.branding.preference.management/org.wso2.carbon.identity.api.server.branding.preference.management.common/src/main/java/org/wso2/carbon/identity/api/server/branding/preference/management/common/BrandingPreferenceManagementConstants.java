/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.branding.preference.management.common;

/**
 * Branding preference management related constant class.
 */
public class BrandingPreferenceManagementConstants {

    public static final String BRANDING_PREFERENCE_ERROR_PREFIX = "BPM-";
    public static final String BRANDING_PREFERENCE_CONTEXT_PATH = "/branding-preference";
    public static final String QUERY_PARAM_INDICATOR = "?";
    public static final String GET_PREFERENCE_COMPONENT_WITH_QUERY_PARAM = "type=%s&name=%s&locale=%s";
    public static final String ORGANIZATION_TYPE = "ORG";
    public static final String APPLICATION_TYPE = "APP";
    public static final String CUSTOM_TYPE = "CUSTOM";
    public static final String DEFAULT_LOCALE = "en-US";
    public static final String BRANDING_PREFERENCE_MGT_ERROR_CODE_DELIMITER = "_";

    public static final String BRANDING_PREFERENCE_NOT_EXISTS_ERROR_CODE = "BRANDINGM_00002";
    public static final String BRANDING_PREFERENCE_ALREADY_EXISTS_ERROR_CODE = "BRANDINGM_00003";

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
                "Error while updating branding preference configurations for organization: %s.");

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
