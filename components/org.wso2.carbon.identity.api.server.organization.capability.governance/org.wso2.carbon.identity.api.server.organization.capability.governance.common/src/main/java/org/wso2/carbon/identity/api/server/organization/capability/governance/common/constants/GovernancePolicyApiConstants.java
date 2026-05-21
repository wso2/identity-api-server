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

package org.wso2.carbon.identity.api.server.organization.capability.governance.common.constants;

/**
 * Constants for the Organization Capability Governance API component.
 */
public class GovernancePolicyApiConstants {

    public static final String ERROR_PREFIX = "OCGM-";

    /**
     * Error messages for the governance policy API.
     */
    public enum ErrorMessage {

        // Client errors.
        ERROR_MISSING_ORGANIZATION_CONTEXT("60001",
                "Unable to resolve organization context.",
                "The organization could not be resolved from the current request context."),
        ORGANIZATION_NOT_FOUND("60002",
                "Organization not found.",
                "No organization could be found for the given tenant domain."),

        // Server errors.
        ERROR_RESOLVING_ORGANIZATION_ID("65001",
                "Error resolving organization.",
                "An unexpected error occurred while resolving the organization for the tenant domain."),
        ERROR_EVALUATING_GOVERNANCE_POLICY("65002",
                "Error evaluating governance policy.",
                "An unexpected error occurred while evaluating the governance policy for the organization.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return ERROR_PREFIX + code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }
    }
}
