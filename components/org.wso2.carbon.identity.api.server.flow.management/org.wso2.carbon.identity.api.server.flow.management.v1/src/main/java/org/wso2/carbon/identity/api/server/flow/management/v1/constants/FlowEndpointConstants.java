/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.flow.management.v1.constants;

/**
 * Constants related to the flow endpoint.
 */
public class FlowEndpointConstants {

    private FlowEndpointConstants() {

    }

    public static final String FLOW_PREFIX = "FM-";

    public static final String END_USER_ATTRIBUTE_PROFILE = "endUser";
    public static final String SELF_REGISTRATION_ATTRIBUTE_PROFILE = "selfRegistration";

    public static final String USERNAME_IDENTIFIER = "http://wso2.org/claims/username";
    public static final String PASSWORD_IDENTIFIER = "password";
    public static final String USER_IDENTIFIER_NAME = "User Identifier";
    public static final String USER_IDENTIFIER = "userIdentifier";

    public static final String IDP_TEMPLATE_ID = "templateId";
    public static final String MICROSOFT_IDP_TEMPLATE_ID = "microsoft-idp";

    /**
     * Error messages related to flow management.
     */
    public enum ErrorMessages {

        ERROR_CODE_INVALID_FLOW_TYPE("10001",
                "Invalid flow type.",
                "The provided flow type is not supported."),

        ERROR_CODE_GET_GOVERNANCE_CONFIG("10002",
                "Error occurred while retrieving the governance configuration.",
                "Server encountered an error while retrieving the governance configuration."),

        ERROR_CODE_GET_IDENTITY_PROVIDERS("10003",
                "Error occurred while retrieving identity providers.",
                "Server encountered an error while retrieving the identity providers."),

        ERROR_CODE_UNSUPPORTED_EXECUTOR("10004",
                "Unsupported executor used in the flow.",
                "The provided executor is not supported for the flow type. Please use a supported executor."),

        ERROR_CODE_MISSING_IDENTIFIER("10005",
                "Missing required identifier in flow.",
                "The flow must contain a FIELD component with the required identifier."),

        ERROR_CODE_DUPLICATE_COMPONENT_ID("10006",
                "Duplicate component ID found in the flow.",
                "All component IDs must be unique across the entire flow."),

        ERROR_CODE_INVALID_METADATA("10007",
                "Invalid metadata provided for the executor.",
                "The metadata provided for the executor is invalid."),

        ERROR_CODE_GET_SUPPORTED_CLAIMS("10008",
                "Failed to retrieve supported claims.",
                "An error occurred while reading supported claim metadata for the given attribute profile."),

        ERROR_CODE_INVALID_FLOW_COMPLETION_CONFIG("10009",
                "Invalid flow completion config value provided.",
                "The provided value for flow completion config %s is not valid."),

        ERROR_CODE_UNSUPPORTED_FLOW_COMPLETION_CONFIG("10010",
                "Unsupported flow completion config used in the flow.",
                "The provided flow completion config %s is not supported for the flow type %s."),

        ERROR_CODE_UNSUPPORTED_PROPERTY("10011",
                "Invalid property value.",
                "Property %s cannot be %s."),

        ERROR_CODE_EMPTY_STEPS("10012",
                "Empty steps in the flow.",
                "The steps in the flow cannot be empty.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessages(String code, String message, String description) {

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

    }

    /**
     * Constants related to flow schema.
     */
    public static class Schema {

        public static final String IDP_NAME = "idpName";
    }

    /**
     * Constants related to executors.
     */
    public static class Executors {

        public static final String OPENID_CONNECT_EXECUTOR = "OpenIDConnectExecutor";
        public static final String GOOGLE_EXECUTOR = "GoogleExecutor";
        public static final String FACEBOOK_EXECUTOR = "FacebookExecutor";
        public static final String OFFICE365_EXECUTOR = "Office365Executor";
        public static final String APPLE_EXECUTOR = "AppleExecutor";
        public static final String GITHUB_EXECUTOR = "GithubExecutor";
        public static final String FIDO2_EXECUTOR = "FIDO2Executor";
        public static final String USER_RESOLVE_EXECUTOR = "UserResolveExecutor";
        public static final String PASSWORD_PROVISIONING_EXECUTOR = "PasswordProvisioningExecutor";
        public static final String ABSTRACT_OTP_EXECUTOR = "AbstractOTPExecutor";
        public static final String EMAIL_OTP_EXECUTOR = "EmailOTPExecutor";
        public static final String SMS_OTP_EXECUTOR = "SMSOTPExecutor";
        public static final String MAGIC_LINK_EXECUTOR = "MagicLinkExecutor";
        public static final String PASSWORD_ONBOARD_EXECUTOR = "PasswordOnboardExecutor";
        public static final String CONFIRMATION_CODE_VALIDATION_EXECUTOR = "ConfirmationCodeValidationExecutor";
    }

    /**
     * Constants related to authenticators.
     */
    public static class Authenticators {

        public static final String OPENID_CONNECT_AUTHENTICATOR = "OpenIDConnectAuthenticator";
        public static final String GOOGLE_AUTHENTICATOR = "GoogleOIDCAuthenticator";
        public static final String GITHUB_AUTHENTICATOR = "GithubAuthenticator";
        public static final String FACEBOOK_AUTHENTICATOR = "FacebookAuthenticator";
        public static final String OFFICE365_AUTHENTICATOR = "Office365Authenticator";
        public static final String APPLE_AUTHENTICATOR = "AppleOIDCAuthenticator";
    }

    /**
     * Constants related to flow generation.
     */
    public static class FlowGeneration {

        public static final String STATUS_FAILED = "FAILED";
        public static final String STEPS = "steps";
        public static final String ERROR = "error";
        public static final String END = "END";
        public static final String NEXT = "next";
        public static final String EMPTY = "empty";
        public static final String NULL = "null";
        public static final String FORM = "FORM";
    }
}
