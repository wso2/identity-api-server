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

import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtClientException;

/**
 * Constants related to the flow endpoint.
 */
public class FlowEndpointConstants {

    private FlowEndpointConstants() {

    }

    public static final String FLOW_PREFIX = "FM-";
    public static final String USER_RESOLVE_EXECUTOR = "UserResolveExecutor";
    public static final String PASSWORD_PROVISIONING_EXECUTOR = "PasswordProvisioningExecutor";
    public static final String EMAIL_OTP_EXECUTOR = "EmailOTPExecutor";
    public static final String SMS_OTP_EXECUTOR = "SmsOTPExecutor";
    public static final String PASSWORD_ONBOARD_EXECUTOR = "PasswordOnboardExecutor";

    public static final String END_USER_ATTRIBUTE_PROFILE = "End-User-Profile";
    public static final String SELF_REGISTRATION_ATTRIBUTE_PROFILE = "Self-Registration";

    public static final String USERNAME_IDENTIFIER = "http://wso2.org/claims/username";
    public static final String PASSWORD_IDENTIFIER = "password";

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

        ERROR_CODE_GET_LOCAL_AUTHENTICATORS("10003",
                "Error occurred while retrieving local authenticators.",
                "Server encountered an error while retrieving the local authenticators."),

        ERROR_CODE_UNSUPPORTED_EXECUTOR("10004",
                "Unsupported executor used in the flow.",
                "The provided executor is not supported for the flow type. Please use a supported executor."),

        ERROR_CODE_MISSING_IDENTIFIER("10005",
                "Missing required identifier in flow.",
                "The flow must contain a FIELD component with the required identifier."),

        ERROR_CODE_DUPLICATE_COMPONENT_ID("10006",
                "Duplicate component ID found in the flow.",
                "All component IDs must be unique across the entire flow.");

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
     * Supported flow types.
     */
    public enum FlowType {

        REGISTRATION,
        PASSWORD_RECOVERY,
        ASK_PASSWORD;

        /**
         * Check if a given string is a valid flow type.
         *
         * @param value Flow type as string
         */
        public static void validateFlowType(String value) {

            for (FlowType type : FlowType.values()) {
                if (type.name().equals(value)) {
                    return;
                }
            }
            throw Utils.handleFlowMgtException(new FlowMgtClientException(
                    ErrorMessages.ERROR_CODE_INVALID_FLOW_TYPE.getCode(),
                    ErrorMessages.ERROR_CODE_INVALID_FLOW_TYPE.getMessage(),
                    ErrorMessages.ERROR_CODE_INVALID_FLOW_TYPE.getDescription()));
        }
    }

    /**
     * Constants related to flow schema.
     */
    public static class Schema {

        public static final String IDP_NAME = "idpName";
    }

}
