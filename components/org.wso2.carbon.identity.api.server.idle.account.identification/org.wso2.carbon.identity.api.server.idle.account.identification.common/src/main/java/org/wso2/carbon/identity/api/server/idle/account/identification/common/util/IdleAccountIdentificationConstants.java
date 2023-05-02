package org.wso2.carbon.identity.api.server.idle.account.identification.common.util;

/**
 * Inactive users management related constant class.
 */
public class IdleAccountIdentificationConstants {

    public static final String INACTIVE_USER_MANAGEMENT_SERVICE_ERROR_PREFIX = "IDLE_ACC-";
    public static final String CORRELATION_ID = "Correlation-ID";

    /**
     * Enums for error messages.
     */
    public enum ErrorMessage {

        // Server errors 650xx.
        ERROR_RETRIEVING_INACTIVE_USERS("65001",
                "Error while retrieving inactive users.",
                "Error while retrieving inactive users for organization: %s."),

        // client errors 600xx.
        ERROR_REQUIRED_PARAMETER_MISSING("60001",
                 "Required parameter is not provided.",
                 "%s parameter is required and cannot be empty.");

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
