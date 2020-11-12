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

package org.wso2.carbon.identity.api.server.script.library.common;

/**
 * Script Library Management constant class.
 */
public class Constants {

    public static final String SCRIPT_LIBRARY_MANAGEMENT_PREFIX = "SCL-";
    public static final String SCRIPT_LIBRARY_PATH_COMPONENT = "/script-libraries";
    public static final String SCRIPT_LIBRARY_EXTENSION = ".js";
    public static final String SCRIPT_LIBRARY_CONTENT_PATH = "/content";

    private Constants() {

    }

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {
        ERROR_CODE_ERROR_LISTING_SCRIPT_LIBRARIES("65001", "Unable to list existing script libraries.",
                "Server encountered an error while listing the script libraries."),
        ERROR_CODE_ERROR_ADDING_SCRIPT_LIBRARY("65002", "Unable to add Script library.",
                "Server encountered an error while adding the script library."),
        ERROR_CODE_ERROR_DELETING_SCRIPT_LIBRARY("65004", "Unable to delete script library.",
                "Server encountered an error while deleting the script library for the identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_SCRIPT_LIBRARY("65003", "Unable to retrieve script library.",
                "Server encountered an error while retrieving the script library for identifier %s."),
        ERROR_CODE_ERROR_UPDATING_SCRIPT_LIBRARY("65005", "Unable to update script library.",
                "Server encountered an error while updating the script library for identifier %s."),
        ERROR_SCRIPT_LIBRARY_NOT_FOUND("60006", "Script library not found.",
                "Script library cannot be found for the provided name: %s in the tenantDomain: %s."),
        ERROR_SCRIPT_LIBRARY_ALREADY_FOUND("60007", "Script library already exist.",
                "Script library already exist for the provided name: %s in the tenantDomain: %s."),
        ERROR_SCRIPT_LIBRARY_NAME_VALIDATION("60008", "Invalid script library name.",
                "Script library name should include the .js extension."),
        ERROR_SCRIPT_LIBRARY_OFFSET_VALIDATION("60009", "Invalid offset.",
                "Offset should be greater than or equal to 0"),
        ERROR_CODE_ERROR_ENCODING_URL("60010", "Error while encoding the script library name.",
                "Server encountered an error while encoding the script library name %s.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return SCRIPT_LIBRARY_MANAGEMENT_PREFIX + this.code;
        }

        public String getMessage() {

            return this.message;
        }

        public String getDescription() {

            return this.description;
        }

        public String toString() {

            return this.code + " | " + this.message;
        }
    }
}
