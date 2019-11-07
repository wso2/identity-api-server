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
package org.wso2.carbon.identity.api.server.userstore.common;

/**
 * Contains all the user store related constants.
 */
public class UserStoreConstants {

    private static final String SECONDARY_USERSTORE_PREFIX = "SUS-";
    public static final String OPERATION_REPLACE = "REPLACE";
    public static final String OPERATION_ADD = "ADD";
    public static final String USER_STORE_PATH_COMPONENT = "/userstores";


    /**
     * Enum for user store related errors in the format of
     * Error Code - code to identify the error
     * Error Message - What went wrong
     * Error Description - Why it went wrong
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_ADDING_USER_STORE("50001",
                "Unable to add the secondary user store.",
                "Server Encountered an error while adding secondary user store."),
        ERROR_CODE_ERROR_DELETING_USER_STORE("50002",
                "Unable to delete the secondary user store.",
                "Server Encountered an error while deleting the secondary user store."),
        ERROR_CODE_ERROR_RETRIEVING_USER_STORE("50003",
                "Unable to get the configured user stores.",
                "Server Encountered an error while retrieving secondary user stores."),
        ERROR_CODE_ERROR_UPDATING_USER_STORE("50004",
                "Unable to update the secondary user store configurations.",
                "Server Encountered an error while updating the secondary user store configurations."),
        ERROR_CODE_PAGINATION_NOT_IMPLEMENTED("50005",
                "Pagination not supported.",
                "Pagination capabilities are not supported in this version of the API."),
        ERROR_CODE_FILTERING_NOT_IMPLEMENTED("50006",
                "Filtering not supported.",
                "Filtering capability is not supported in this version of the API."),
        ERROR_CODE_SORTING_NOT_IMPLEMENTED("50007",
                "Sorting not supported.",
                "Sorting capability is not supported in this version of the API."),
        ERROR_CODE_DOMAIN_ID_NOT_FOUND("50008",
                "Resource not found.",
                "Unable to find any user store's id with the provided identifier %s."),
        ERROR_CODE_DATASOURCE_CONNECTION("500011",
                "Unable to check RDBMS connection Health",
                "Server Encountered an error while checking the data source connection."),
        ERROR_CODE_RETRIEVING_USER_STORE_TYPE("500012",
                "Unable to retrieve the user store implementations",
                "Server Encountered an error while retrieving the implementations."),
        ;

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return SECONDARY_USERSTORE_PREFIX + code;
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
