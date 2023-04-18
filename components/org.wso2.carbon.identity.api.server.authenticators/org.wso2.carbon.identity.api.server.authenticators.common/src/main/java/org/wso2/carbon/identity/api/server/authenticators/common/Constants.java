/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.authenticators.common;

/**
 * Common constants for authenticators API.
 */
public class Constants {

    private Constants() {

    }

    public static final String AUTHENTICATOR_ERROR_PREFIX = "AUT-";
    public static final String FEDERATED_AUTHENTICATORS = "federatedAuthenticators";
    public static final String AUTHENTICATOR_PATH_COMPONENT = "/identity-providers";
    public static final String PATH_SEPERATOR = "/";
    public static final String PAGE_LINK_REL_NEXT = "next";
    public static final String PAGE_LINK_REL_PREVIOUS = "previous";
    public static final String PAGINATION_WITH_FILTER_LINK_FORMAT = "?offset=%d&limit=%d&filter=%s";
    public static final String PAGINATION_LINK_FORMAT = "?offset=%d&limit=%d";

    /**
     * Supported filter attributes.
     */
    public static class FilterAttributes {

        public static final String TAG = "tag";
        public static final String NAME = "name";
    }

    /**
     * Supported filter operations.
     */
    public static class FilterOperations {

        public static final String EQ = "eq";
        public static final String SW = "sw";
    }

    /**
     * Supported complex query operations.
     */
    public static class ComplexQueryOperations {

        public static final String AND = "and";
        public static final String OR = "or";
    }

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        ERROR_CODE_INVALID_FILTER_FORMAT("60001", "Invalid format used for filtering.",
                "Filter needs to be in the format <attribute>+<operation>+<value>. Eg: tag+eq+2FA"),
        ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE("60002", "Unsupported filter attribute.",
                "The filter attribute '%s' is not supported."),
        ERROR_CODE_ERROR_LISTING_AUTHENTICATORS("65001", "Unable to list the existing authenticators.",
                "Server encountered an error while listing the authenticators."),
        ERROR_CODE_ERROR_LISTING_IDPS("65002", "Unable to list the existing identity providers.",
                "Server encountered an error while listing the identity providers."),
        ERROR_CODE_UNSUPPORTED_COMPLEX_QUERY_IN_FILTER("65003", "Unsupported filter.",
                "The complex query used for filtering is not supported."),
        ERROR_CODE_UNSUPPORTED_FILTER_OPERATION_FOR_TAG("65004", "Unsupported filter operation.",
                "Unsupported filter operation '%s' for filter attribute 'tag'"),
        ERROR_CODE_UNSUPPORTED_FILTER_OPERATION_FOR_NAME("65005", "Unsupported filter operation.",
                "The filter operation '%s' is not supported for filter attribute 'name'."),
        ERROR_CODE_UNSUPPORTED_FILTER_FOR_MULTIPLE_NAMES("65006", "Unsupported filter.",
                "Filtering with multiple 'name' attributes is not supported."),
        ERROR_CODE_UNSUPPORTED_COMPLEX_QUERY_OPERATION_FOR_NAME("65007", "Unsupported complex query " +
                "operation in filter.", "Complex query with '%s' operation for filter attribute 'name' is not " +
                "supported."),
        ERROR_CODE_UNSUPPORTED_COMPLEX_QUERY_OPERATION_FOR_TAG("65008", "Unsupported complex query " +
                "operation in filter.", "Complex query with '%s' operation for filter attribute 'tag' is not " +
                "supported."),
        ERROR_CODE_PAGINATION_NOT_IMPLEMENTED("65009", "Pagination not supported.", "Pagination " +
                "capabilities are not supported in this version of the API."),
        ERROR_CODE_BUILDING_LINKS("65010", "Error building page links", "Error occurred during building page links. " +
                                          "%s"),
        ERROR_CODE_ERROR_RETRIEVING_IDP_CONNECTED_APPS("65011",
                "Unable to retrieve authenticator connected applications.",
                "Server encountered an error while retrieving the authenticator connected applications %s.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return AUTHENTICATOR_ERROR_PREFIX + code;
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
