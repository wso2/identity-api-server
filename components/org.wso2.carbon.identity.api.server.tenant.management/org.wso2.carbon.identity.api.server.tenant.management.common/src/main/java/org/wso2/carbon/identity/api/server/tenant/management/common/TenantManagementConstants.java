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

        ERROR_CODE_ERROR_LISTING_TENANTS("TM-65001",
                "Unable to list existing tenants.",
                "Server encountered an error while listing the tenants."),
        ERROR_CODE_ERROR_ADDING_TENANT("TM-65002",
                "Unable to add tenant.", "Server encountered an error while adding the tenant."),
        ERROR_CODE_ERROR_RETRIEVING_TENANT("TM-65003",
                "Unable to retrieve tenant.", "Server encountered an error while retrieving the tenant for identifier" +
                " %s."),
        ERROR_CODE_TENANT_NOT_FOUND("TM-65004", "Resource not found.", "Unable to find a resource matching the " +
                "provided tenant identifier %s."),
        ERROR_CODE_OWNER_NOT_FOUND("TM-65005", "Resource not found.", "Unable to find a resource matching the " +
                "provided tenant identifier %s."),
        ERROR_CODE_UPDATE_LIFECYCLE_STATUS("TM-65006", "Error while updating the tenant.", "Server encountered an " +
                "error while the tenant life cycle status \"activated:\" %s ."),

        ERROR_CODE_BUILDING_LINKS("TM-65007", "Error building page links", "Error occurred during building page links. "
                + "%s"),
        ERROR_CODE_FILTER_NOT_IMPLEMENTED("TM-65008", "Filtering not supported.", "Filtering capability is not " +
                "supported in this version of the API.");

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
