package org.wso2.carbon.identity.api.server.oidc.scope.management.common;

/**
 * Contains all the OIDC Scope Management Service related constants.
 */
public class OidcScopeConstants {

    public static final String OIDC_SCOPE_API_PATH_COMPONENT = "/oidc/scopes";
    public static final String PATH_SEPERATOR = "/";

    /**
     * Enum for OIDC Scope management service related errors.
     */
    public enum ErrorMessage {

        INVALID_REQUEST("60001", "Invalid Request."),
        ERROR_CONFLICT_REQUEST("41004", "Scope ID: %s already exists with OIDC claim " +
                "mapping in the system. Please use a different scope name or different OIDC claims to map.");

        private final String code;
        private final String message;

        ErrorMessage(String code, String message) {

            this.code = code;
            this.message = message;
        }

        public String getCode() {

            return code;
        }

        public String getMessage() {

            return message;
        }

        @Override
        public String toString() {

            return code + " : " + message;
        }
    }
}
