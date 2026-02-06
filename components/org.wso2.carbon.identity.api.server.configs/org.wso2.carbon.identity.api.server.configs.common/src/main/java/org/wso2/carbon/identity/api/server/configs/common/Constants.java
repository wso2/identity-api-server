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

package org.wso2.carbon.identity.api.server.configs.common;

/**
 * Server configuration Management constant class.
 */
public class Constants {

    private Constants() {

    }

    public static final String CONFIG_ERROR_PREFIX = "CNF-";
    public static final String CONFIGS_AUTHENTICATOR_PATH_COMPONENT = "/configs/authenticators";
    public static final String CONFIGS_SCHEMAS_PATH_COMPONENT = "/configs/schemas";
    public static final String PATH_SEPERATOR = "/";

    // PATCH operation paths.
    public static final String IDLE_SESSION_PATH = "/idleSessionTimeoutPeriod";
    public static final String REMEMBER_ME_PATH = "/rememberMePeriod";
    public static final String ENABLE_MAXIMUM_SESSION_TIMEOUT_PATH = "/enableMaximumSessionTimeoutPeriod";
    public static final String MAXIMUM_SESSION_TIMEOUT_PATH = "/maximumSessionTimeoutPeriod";
    public static final String HOME_REALM_PATH_REGEX = "/homeRealmIdentifiers/[0-9]+";

    /**
     * PATCH operation path for CORS configuration.
     */
    public static final String CORS_CONFIG_ALLOW_GENERIC_HTTP_PATH_REGEX = "^/allowGenericHttpRequests$";
    public static final String CORS_CONFIG_ALLOW_ANY_ORIGIN_PATH_REGEX = "^/allowAnyOrigin";
    public static final String CORS_CONFIG_ALLOW_SUBDOMAINS_PATH_REGEX = "^/allowSubdomains$";
    public static final String CORS_CONFIG_SUPPORTED_METHODS_PATH_REGEX = "^/supportedMethods$";
    public static final String CORS_CONFIG_SUPPORT_ANY_HEADER_PATH_REGEX = "^/supportAnyHeader";
    public static final String CORS_CONFIG_SUPPORTED_HEADERS_PATH_REGEX = "^/supportedHeaders$";
    public static final String CORS_CONFIG_EXPOSED_HEADERS_PATH_REGEX = "^/exposedHeaders$";
    public static final String CORS_CONFIG_SUPPORTS_CREDENTIALS_PATH_REGEX = "^/supportsCredentials$";
    public static final String CORS_CONFIG_MAX_AGE_PATH_REGEX = "^/maxAge$";

    public static final String AUDIT = "audit";
    public static final String CARBON = "carbon";

    /**
     * PATCH operation path for Private Key JWT Validation configuration.
     */
    public static final String PRIVATE_KEY_JWT_VALIDATION_CONFIG_TOKEN_REUSE = "/enableTokenReuse";

    /**
     * PATCH operation path for Impersonation configuration.
     */
    public static final String IMPERSONATION_CONFIG_ENABLE_EMAIL_NOTIFICATION = "/enableEmailNotification";

    /**
     * PATCH operation paths for DCR configuration.
     */
    public static final String DCR_CONFIG_ENABLE_FAPI_ENFORCEMENT = "/enableFapiEnforcement";
    public static final String DCR_CONFIG_SSA_JWKS = "/ssaJwks";
    public static final String DCR_CONFIG_AUTHENTICATION_REQUIRED = "/authenticationRequired";
    public static final String DCR_CONFIG_MANDATE_SSA = "/mandateSSA";

    /**
     * SAML2 metadata endpoint uri path.
     */
    public static final String SAML2_METADATA_ENDPOINT_URI_PATH = "/identity/metadata/saml2";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_LISTING_AUTHENTICATORS("60021",
                "Unable to list existing authenticators in the server.",
                "Server encountered an error while listing the authenticators."),
        ERROR_CODE_ERROR_RETRIEVING_AUTHENTICATOR("65003",
                "Unable to retrieve authenticator.",
                "Server encountered an error while retrieving the authenticator for the identifier %s."),
        ERROR_CODE_AUTHENTICATOR_NOT_FOUND("60002",
                "Resource not found.",
                "Unable to find a resource matching the provided authenticator identifier %s."),
        ERROR_CODE_INVALID_INPUT("60003",
                "Invalid input.",
                "One of the given inputs is invalid. %s."),
        ERROR_CODE_ERROR_UPDATING_CONFIGS("65004",
                "Unable to update server configs.",
                "Server encountered an " +
                "error while updating the server configs."),
        ERROR_CODE_ERROR_RETRIEVING_CONFIGS("65005",
                "Unable to retrieve server configs.",
                "Server encountered an " +
                "error while retrieving the server configs."),
        ERROR_CODE_SCHEMA_NOT_FOUND("60004",
                "Resource not found.",
                "Unable to find a resource matching the provided schema identifier %s."),
        ERROR_CODE_CONFIG_UPDATE_NOT_ALLOWED("60005",
                "Configuration update not allowed.",
                "The requested update is not allowed for the organization."),

        /**
         * CORS errors.
         */
        ERROR_CODE_CORS_CONFIG_RETRIEVE("65001",
                "Unable to retrieve CORS configuration.",
                "Server encountered an error while retrieving the CORS configuration."),
        ERROR_CODE_CORS_CONFIG_UPDATE("65002",
                "Unable to update CORS configuration.",
                "Server encountered an error while updating the CORS configuration."),

        /**
         * Private Key JWT Validation errors.
         */
        ERROR_CODE_PRIVATE_KEY_JWT_VALIDATOR_CONFIG_RETRIEVE("65006",
                                                "Unable to retrieve Private Key JWT Validation configuration.",
                                                "Server encountered an error while retrieving the " +
                                                        "Private Key JWT Validation configuration."),
        ERROR_CODE_PRIVATE_KEY_JWT_VALIDATOR_CONFIG_UPDATE("65007",
                                              "Unable to update Private Key JWT Validation configuration.",
                                              "Server encountered an error while updating the " +
                                                      "Private Key JWT Validation configuration."),
        ERROR_JWT_AUTHENTICATOR_SERVICE_NOT_FOUND("60505",
                "Private Key JWT Authenticator is not supported.",
                "Private Key JWT Authenticator service is unavailable at the moment."),

        //Remote logging server configuration error.
        ERROR_CODE_ERROR_RESETTING_REMOTE_LOGGING_CONFIGS("65008",
                "Unable to reset remote logging  configs.",
                "Server encountered an " +
                        "error while resetting the remote logging configs."),

        ERROR_CODE_ERROR_UPDATING_REMOTE_LOGGING_CONFIGS("65009",
                "Unable to update remote logging  configs.",
                "Server encountered an " +
                        "error while updating the remote logging configs."),
        ERROR_CODE_ERROR_GETTING_REMOTE_LOGGING_CONFIGS("65017",
                "Unable to get remote logging  configs.",
                "Server encountered an " +
                        "error while getting the remote logging configs."),


        ERROR_CODE_INVALID_TENANT_DOMAIN_FOR_REMOTE_LOGGING_CONFIG("60506",
                "Invalid tenant domain for accessing remote logging config service",
                "Remote logging configuration service is only supported for super tenant."),

        ERROR_CODE_INVALID_LOG_TYPE_FOR_REMOTE_LOGGING_CONFIG("60507",
                "Invalid log type provided remote logging config service",
                "Remote logging configuration service only supports audit or carbon."),
        ERROR_CODE_REMOTE_LOGGING_CONFIG_NOT_FOUND("60508",
                "Resource not found.",
                "Unable to find a resource matching the provided log type %s."),

        /**
         * DCR Configuration errors.
         */

        ERROR_CODE_DCR_CONFIG_RETRIEVE("60516",
                "Unable to retrieve DCR configuration.",
                "Server encountered an error while retrieving the " +
                        "DCR configuration."),

        ERROR_CODE_DCR_CONFIG_UPDATE("60517",
                "Unable to update DCR configuration.",
                "Server encountered an error while updating the DCR configuration."),

        /**
         * Inbound auth config error messages.
         */
        ERROR_CODE_RESIDENT_IDP_NOT_FOUND("65010", "Resident IDP not found.",
                "Unable to find the resident IDP for the tenant domain %s."),
        ERROR_CODE_FEDERATED_AUTHENTICATOR_CONFIG_NOT_FOUND("65011",
                "Federated authenticator config not found.",
                "Unable to find the federated authenticator config for %s in the resident IDP."),
        ERROR_CODE_FEDERATED_AUTHENTICATOR_PROPERTIES_NOT_FOUND("65012",
                "Federated authenticator properties not found.",
                "Unable to find the authenticator properties for the federated authenticator %s."),
        ERROR_CODE_ERROR_SAML_INBOUND_AUTH_CONFIG_RETRIEVE("65013",
                "Unable to retrieve SAML inbound auth configs.",
                "Server encountered an error while retrieving the SAML inbound auth configs."),
        ERROR_CODE_ERROR_SAML_INBOUND_AUTH_CONFIG_UPDATE("65014",
                "Unable to update SAML inbound auth configs.",
                "Server encountered an error while updating the SAML inbound auth configs."),
        ERROR_CODE_ERROR_PASSIVE_STS_INBOUND_AUTH_CONFIG_RETRIEVE("65015",
                "Unable to retrieve Passive STS inbound auth configs.",
                "Server encountered an error while retrieving the Passive STS inbound auth configs."),
        ERROR_CODE_ERROR_PASSIVE_STS_INBOUND_AUTH_CONFIG_UPDATE("65016",
                "Unable to update Passive STS inbound auth configs.",
                "Server encountered an error while updating the Passive STS inbound auth configs."),
        ERROR_CODE_IMP_CONFIG_RETRIEVE("65018",
                "Unable to retrieve Impersonation configuration.",
                "Server encountered an error while retrieving the Impersonation configuration of %s."),
        ERROR_CODE_IMP_CONFIG_UPDATE("65019",
                "Unable to update Impersonation configuration.",
                "Server encountered an error while updating the Impersonation configuration of %s."),
        ERROR_CODE_ERROR_SAML_INBOUND_AUTH_CONFIG_DELETE("65020",
                "Unable to delete SAML inbound auth configs.",
                "Server encountered an error while deleting the SAML inbound auth configs."),
        ERROR_CODE_ERROR_PASSIVE_STS_INBOUND_AUTH_CONFIG_DELETE("65021",
                "Unable to delete Passive STS inbound auth configs.",
                "Server encountered an error while deleting the Passive STS inbound auth configs."),
        ERROR_CODE_IMP_CONFIG_DELETE("65022",
                "Unable to delete Impersonation configuration.",
                "Server encountered an error while deleting the Impersonation configuration of %s."),

        ERROR_CODE_FRAUD_DETECTION_CONFIG_RETRIEVE("65023",
                "Unable to retrieve Fraud Detection configuration.",
                "Server encountered an error while retrieving the Fraud Detection configuration."),
        ERROR_CODE_FRAUD_DETECTION_CONFIG_UPDATE("65024",
                "Unable to update Fraud Detection configuration.",
                "Server encountered an error while updating the Fraud Detection configuration.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String code() {

            return CONFIG_ERROR_PREFIX + code;
        }

        public String message() {

            return message;
        }

        public String description() {

            return description;
        }

        @Override
        public String toString() {

            return code + " | " + message;
        }
    }
}
