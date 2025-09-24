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

import java.util.Arrays;
import java.util.Optional;

public class CredentialManagementConstants {

    private CredentialManagementConstants() {

    }

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
        public static Optional<CredentialTypes> fromString(String value) {

            if (value == null) {
                return Optional.empty();
            }

            String candidate = value.trim();
            if (candidate.isEmpty()) {
                return Optional.empty();
            }
            return Arrays.stream(values())
                    .filter(type -> type.name().equalsIgnoreCase(candidate)
                            || type.getApiValue().equalsIgnoreCase(candidate))
                    .findFirst();
        }
    }

    public enum ErrorMessages {

        // Server errors.
        ERROR_CODE_GET_PASSKEYS("65001", "Error retrieving registered passkeys.",
                "Unexpected server error while fetching registered passkeys for user: %id."),
        ERROR_CODE_DELETE_PASSKEYS("65002", "Error deleting registered passkey.",
                "Unexpected server error while deleting registered passkey: %credentialId for user: %id."),
        ERROR_CODE_GET_PUSH_AUTH_DEVICE("65003", "Error retrieving registered push auth devices.",
                "Unexpected server error while fetching registered push auth device for user: %id."),
        ERROR_CODE_DELETE_PUSH_AUTH_DEVICE("65004", "Error deleting registered push auth devices.",
                "Unexpected server error while deleting registered push auth device: %credentialId for user: %id."),

        // Client errors.
        ERROR_CODE_DELETE_PASSKEY_CREDENTIAL("60001", "Error deleting credential.",
                "The request to delete the passkey credential: %credentialId  was invalid."),
        ERROR_CODE_DELETE_PUSH_AUTH_CREDENTIAL("60001", "Error deleting credential.",
                "The request to delete the push auth credential: %credentialId  was invalid."),;

        private static final String ERROR_PREFIX = "CM";
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
