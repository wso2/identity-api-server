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

package org.wso2.carbon.identity.api.server.credential.management.common;

import java.text.Collator;
import java.util.Arrays;
import java.util.Locale;

/**
 * Credential Management related constants.
 */
public class CredentialManagementConstants {

    private CredentialManagementConstants() {

    }

    /**
     * Enum for supported credential types.
     */
    public enum CredentialTypes {

        PASSKEY("passkey"),
        PUSH_AUTH("push-auth");

        private final String apiValue;

        CredentialTypes(String apiValue) {

            this.apiValue = apiValue;
        }

        public String getApiValue() {

            return apiValue;
        }

        /**
         * Resolve the credential type for a given identifier. Accepts both API values and enum names.
         *
         * @param value Credential type provided by the caller.
         * @return Matching credential type if available.
         */
        public static CredentialTypes fromString(String value) {

            if (value == null) {

                return null;
            }

            String candidate = value.trim();
            if (candidate.isEmpty()) {

                return null;
            }

            Collator collator = Collator.getInstance(Locale.ROOT);
            collator.setStrength(Collator.PRIMARY);

            return Arrays.stream(values())
                    .filter(type -> collator.compare(type.name(), candidate) == 0
                            || collator.compare(type.getApiValue(), candidate) == 0)
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * Enum for error messages.
     */
    public enum ErrorMessages {

        // Server errors.
        ERROR_CODE_GET_PASSKEYS("65001", "Error retrieving registered passkeys.",
                "Unexpected server error while fetching registered passkeys for entity ID: %s."),
        ERROR_CODE_DELETE_PASSKEYS("65002", "Error deleting registered passkey.",
                "Unexpected server error while deleting registered passkey: %s for entity ID: %s."),
        ERROR_CODE_GET_PUSH_AUTH_DEVICE("65003", "Error retrieving registered push auth devices.",
                "Unexpected server error while fetching registered push auth device for entity ID: %s."),
        ERROR_CODE_DELETE_PUSH_AUTH_DEVICE("65004", "Error deleting registered push auth devices.",
                "Unexpected server error while deleting registered push auth device: %s for entity ID: %s."),

        // Client errors.
        ERROR_CODE_DELETE_PASSKEY_CREDENTIAL("60001", "Error deleting credential.",
                "The request to delete the passkey credential: %s  was invalid."),
        ERROR_CODE_DELETE_PUSH_AUTH_CREDENTIAL("60002", "Error deleting credential.",
                "The request to delete the push auth credential: %s  was invalid."),
        ERROR_CODE_GET_USERNAME_FROM_USERID("60003", "Error retrieving username from user ID.",
                "The request to retrieve the username from the user ID: %s was invalid."),
        ERROR_CODE_ENTITY_NOT_FOUND("60004", "Entity not found",
                "Entity with ID %s not found in the tenant domain."),
        ERROR_CODE_INVALID_CREDENTIAL_TYPE("60005",
                "Invalid credential type.",
                "The provided credential type is not supported."),
        ERROR_CODE_INVALID_CREDENTIAL_ID("60006",
                "Invalid credential ID.",
                "The provided credential ID is invalid."),
        ;

        private static final String ERROR_PREFIX = "CM";
        public static final String ERROR_CODE_PUSH_AUTH_DEVICE_NOT_FOUND = "PDH-15010";
        private final String code;
        private final String message;
        private final String description;

        ErrorMessages(String code, String message, String description) {

            this.code = ERROR_PREFIX + "-" + code;
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

            return code + ":" + message;
        }
    }
}
