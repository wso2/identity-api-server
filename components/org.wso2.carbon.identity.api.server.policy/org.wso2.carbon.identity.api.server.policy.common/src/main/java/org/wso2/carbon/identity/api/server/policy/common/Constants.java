/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.policy.common;

/**
 * Constants for Device Policy API.
 */
public class Constants {

    private Constants() {}

    public static final String POLICY_ERROR_PREFIX = "PM-";

    public static final String V1_API_PATH_COMPONENT = "/v1";

    // Path component for building policy resource URIs.
    public static final String POLICY_PATH_COMPONENT = "/policies";

    /**
     * Error messages for Device Policy API.
     */
    public enum ErrorMessage {

        ERROR_CODE_POLICY_NOT_FOUND("60001",
                "Policy not found.",
                "No policy found for the given policy id: %s."),

        ERROR_CODE_INVALID_PAGINATION("60002",
                "Invalid pagination parameters.",
                "The 'limit' must be greater than or equal to 1 and 'offset' must be greater than or equal to 0."),

        ERROR_CODE_UNSUPPORTED_RESOURCE_TYPE("60003",
                "Unsupported policy resource type.",
                "Resource type '%s' is not supported for policy resources. Only RULE is currently supported."),

        ERROR_CODE_ERROR_ADDING_POLICY("65001",
                "Unable to add policy.",
                "Server encountered an error while adding the policy."),

        ERROR_CODE_ERROR_RETRIEVING_POLICY("65002",
                "Unable to retrieve policy.",
                "Server encountered an error while retrieving the policy."),

        ERROR_CODE_ERROR_UPDATING_POLICY("65003",
                "Unable to update policy.",
                "Server encountered an error while updating the policy."),

        ERROR_CODE_ERROR_DELETING_POLICY("65004",
                "Unable to delete policy.",
                "Server encountered an error while deleting the policy."),

        ERROR_CODE_ERROR_LISTING_POLICIES("65005",
                "Unable to list policies.",
                "Server encountered an error while listing policies."),

        ERROR_CODE_ERROR_RETRIEVING_METADATA("65006",
                "Unable to retrieve policy metadata.",
                "Server encountered an error while retrieving device policy field metadata.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return POLICY_ERROR_PREFIX + code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }

        @Override
        public String toString() {

            return getCode() + " | " + message;
        }
    }
}
