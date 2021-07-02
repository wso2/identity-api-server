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
package org.wso2.carbon.identity.api.server.tenant.management.common;

/**
 * Holds constants related to Tenant Management components.
 */
public class TenantManagementConstants {

    private TenantManagementConstants() {

    }

    public static final String TENANT_MANAGEMENT_PATH_COMPONENT = "/tenants";

    // Tenant pagination constants.
    public static final String PAGINATION_LINK_FORMAT = "?offset=%d&limit=%d";
    public static final String PAGINATION_WITH_FILTER_LINK_FORMAT = "?offset=%d&limit=%d&filter=%s";
    public static final String PAGE_LINK_REL_NEXT = "next";
    public static final String PAGE_LINK_REL_PREVIOUS = "previous";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        /* Adding the client error TM-60019 to align with
         the same client error defined in
         org.wso2.carbon.stratos.common.constants.TenantConstants */
        ERROR_CODE_TENANT_LIMIT_REACHED("TM-60019",
                "Unable to create a tenant.",
                "Maximum number of allowed tenants have been reached."),
        ERROR_CODE_ERROR_LISTING_TENANTS("TM-65001",
                "Unable to list existing tenants.",
                "Server encountered an error while listing the tenants."),
        ERROR_CODE_ERROR_ADDING_TENANT("TM-65002",
                "Unable to add tenant.", "Server encountered an error while adding the tenant."),
        ERROR_CODE_ERROR_RETRIEVING_TENANT("TM-65003",
                "Unable to retrieve tenant.", "Server encountered an error while retrieving the tenant for identifier" +
                " %s."),
        ERROR_CODE_UPDATE_LIFECYCLE_STATUS("TM-65004", "Error while updating the tenant.", "Server encountered an " +
                "error while the tenant life cycle status \"activated:\" %s ."),

        ERROR_CODE_BUILDING_LINKS("TM-65005", "Error building page links", "Error occurred during building page links. "
                + "%s"),
        ERROR_CODE_FILTER_NOT_IMPLEMENTED("TM-65006", "Filtering not supported.", "Filtering capability is not " +
                "supported in this version of the API."),
        ERROR_CODE_ERROR_VALIDATING_TENANT_CODE("TM-65007",
                                               "Unable to add tenant.", "Error occurred in validating the code."),
        ERROR_CODE_ERROR_CHECKING_TENANT_AVAILABILITY("TM-65008",
                                                        "Unable to check availability of domain.",
                "Server encountered an error while checking for tenant domain"),
        ERROR_CODE_DELETE_TENANT_METADATA("TM-65008", "Error while deleting the tenant metadata.",
                "Server encountered an error while deleting the tenant metadata identified by %s .");

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
