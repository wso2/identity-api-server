package org.wso2.carbon.identity.api.server.idle.account.identification.common.util;

/**
 * Inactive users management related constant class.
 */
public class IdleAccountIdentificationConstants {

    public static final String INACTIVE_USER_MANAGEMENT_SERVICE_ERROR_PREFIX = "IDLE_ACC-";
    public static final String CORRELATION_ID = "Correlation-ID";

    public static final String DATE_INACTIVE_AFTER = "inactiveAfter";
    public static final String DATE_EXCLUDE_BEFORE = "excludeBefore";
    public static final String DATE_FORMAT_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";

    /**
     * Enums for error messages.
     */
    public enum ErrorMessage {

        // Client errors 600xx.
        ERROR_REQUIRED_PARAMETER_MISSING("60001",
                 "Required parameter is not provided.",
                 "%s parameter is required and cannot be empty."),

        ERROR_DATE_REGEX_MISMATCH("60002",
                  "Invalid date format provided.",
                  "The value provided for %s parameter is invalid. Date format should be yyyy-mm-dd"),

        ERROR_INVALID_DATE("60003",
                   "Invalid date provided.",
                   "The date provided for %s parameter is invalid"),

        // Server errors 650xx.
        ERROR_RETRIEVING_INACTIVE_USERS("65001",
                "Error while retrieving inactive users.",
                "Error while retrieving inactive users for organization: %s.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return INACTIVE_USER_MANAGEMENT_SERVICE_ERROR_PREFIX + code;
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
