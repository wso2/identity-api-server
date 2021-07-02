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

import javax.ws.rs.core.Response;

/**
 * Contains all the user store related constants.
 */
public class UserStoreConstants {

    public static final String SECONDARY_USER_STORE_PREFIX = "SUS-";
    public static final String USER_STORE_PATH_COMPONENT = "/userstores";
    public static final String USER_STORE_DESCRIPTION = "/description";
    public static final String USER_STORE_CLASS_NAME = "/className";
    public static final String USER_STORE_DOMAIN_NAME = "/domainName";
    public static final String USER_STORE_PROPERTIES = "/properties/";
    public static final String USER_STORE_PROPERTY_MASK = "************";

    /**
     * Enum for user store related errors in the format of
     * Error Code - code to identify the error
     * Error Message - What went wrong
     * Error Description - Why it went wrong
     */
    public enum ErrorMessage {

        // Server Errors - 650xx
        ERROR_CODE_ERROR_ADDING_USER_STORE("65001",
                "Unable to add the secondary user store.",
                "Server Encountered an error while adding secondary user store.",
                Response.Status.INTERNAL_SERVER_ERROR),
        ERROR_CODE_ERROR_DELETING_USER_STORE("65002",
                "Unable to delete the secondary user store.",
                "Server Encountered an error while deleting the secondary user store.",
                Response.Status.INTERNAL_SERVER_ERROR),
        ERROR_CODE_ERROR_RETRIEVING_USER_STORE("65003",
                "Unable to get the configured user stores.",
                "Server Encountered an error while retrieving secondary user stores.",
                Response.Status.INTERNAL_SERVER_ERROR),
        ERROR_CODE_ERROR_UPDATING_USER_STORE("65004",
                "Unable to update the secondary user store configurations.",
                "Server Encountered an error while updating the secondary user store configurations.",
                Response.Status.INTERNAL_SERVER_ERROR),
        ERROR_CODE_PAGINATION_NOT_IMPLEMENTED("65005",
                "Pagination not supported.",
                "Pagination capabilities are not supported in this version of the API."),
        ERROR_CODE_FILTERING_NOT_IMPLEMENTED("65006",
                "Filtering not supported.",
                "Filtering capability is not supported in this version of the API."),
        ERROR_CODE_SORTING_NOT_IMPLEMENTED("65007",
                "Sorting not supported.",
                "Sorting capability is not supported in this version of the API."),
        ERROR_CODE_DATASOURCE_CONNECTION("65008",
                "Unable to check RDBMS connection Health",
                "Server Encountered an error while checking the data source connection.",
                Response.Status.INTERNAL_SERVER_ERROR),
        ERROR_CODE_RETRIEVING_USER_STORE_TYPE("65009",
                "Unable to retrieve the user store implementations",
                "Server Encountered an error while retrieving the user store types.",
                Response.Status.INTERNAL_SERVER_ERROR),
        ERROR_CODE_ERROR_RETRIEVING_USER_STORE_BY_DOMAIN_ID("65010",
                "Unable to get the user store by its domain id.",
                "Server Encountered an error while retrieving the user store by its domain id.",
                Response.Status.INTERNAL_SERVER_ERROR),
        ERROR_CODE_ERROR_RETRIEVING_PRIMARY_USERSTORE("65011",
                "Unable to get the primary user store.",
                "Server Encountered an error while retrieving the primary user store.",
                Response.Status.INTERNAL_SERVER_ERROR),
        ERROR_CODE_ERROR_RETRIEVING_REALM_CONFIG("65012",
                "Unable to get the realm configurations",
                "Server Encountered an error while retrieving realm configuration for tenant: %s",
                Response.Status.INTERNAL_SERVER_ERROR),

        // Client Errors - 600xx
        ERROR_CODE_DOMAIN_ID_NOT_FOUND("60001",
                "Resource not found.",
                "Unable to find any user store's domain id with the provided identifier",
                Response.Status.NOT_FOUND),
        ERROR_CODE_NOT_FOUND("60003", "Resource not found.",
                "Unable to find a required resource for this request", Response.Status.NOT_FOUND),
        ERROR_CODE_INVALID_INPUT("60004", "Invalid Input", "Provided Input is not valid.",
                Response.Status.BAD_REQUEST),
        ERROR_CODE_MANDATORY_PROPERTIES_NOT_FOUND("60006", "Mandatory property is missing ",
                " Required user store  property or its value is missing in the request "),
        ERROR_CODE_EMPTY_DOMAIN_ID("60007", "Userstore ID is not specified", "Userstore " +
                "ID is either NULL or empty."),
        ERROR_CODE_EMPTY_DOMAIN_NAME("60008", "Userstore domain name is not specified",
                "Userstore domain name is either NULL or empty."),
        ERROR_CODE_DOMAIN_ID_DOES_NOT_MATCH_WITH_NAME("60009", "Invalid userstore domain name or " +
                "domain ID", "Userstore domain name does not match with the userstore domain ID"),
        ERROR_CODE_REQUEST_BODY_NOT_FOUND("60010", "Invalid userstore update request",
                "Userstore update request is either NULL or empty"),
        ERROR_CODE_USER_STORE_LIMIT_REACHED("60011", "Unable to create a user store.",
                "Maximum number of allowed user stores have been reached.");

        private final String code;
        private final String message;
        private final String description;
        private Response.Status httpStatus;

        ErrorMessage(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        ErrorMessage(String code, String message, String description, Response.Status httpStatus) {
            this.code = code;
            this.message = message;
            this.description = description;
            this.httpStatus = httpStatus;
        }

        public String getCode() {
            return SECONDARY_USER_STORE_PREFIX + code;
        }

        public String getMessage() {
            return message;
        }

        public String getDescription() {
            return description;
        }

        public Response.Status getHttpStatus() {

            return httpStatus;
        }

        @Override
        public String toString() {
            return code + " | " + message;
        }
    }
}
