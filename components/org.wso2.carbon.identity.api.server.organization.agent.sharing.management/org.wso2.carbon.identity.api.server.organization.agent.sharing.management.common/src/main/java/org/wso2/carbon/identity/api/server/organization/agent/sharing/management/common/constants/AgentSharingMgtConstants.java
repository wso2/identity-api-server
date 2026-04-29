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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.common.constants;

/**
 * Holds the constants used by the agent sharing management API component.
 */
public class AgentSharingMgtConstants {

    public static final String ERROR_PREFIX = "ASM-";

    public static final String AGENT_IDS = "agentIds";

    public static final String RESPONSE_STATUS_PROCESSING = "Processing";
    public static final String RESPONSE_DETAIL_AGENT_SHARE = "Agent sharing process triggered successfully.";
    public static final String RESPONSE_DETAIL_AGENT_UNSHARE = "Agent unsharing process triggered successfully.";
    public static final String RESPONSE_DETAIL_AGENT_SHARE_PATCH =
            "Shared agent attributes patch process triggered successfully.";

    /**
     * Enum for agent sharing management related errors.
     * Error Code - code to identify the error.
     * Error Message - What went wrong.
     * Error Description - Why it went wrong.
     */
    public enum ErrorMessage {

        // Client errors.
        INVALID_SELECTIVE_AGENT_SHARE_REQUEST_BODY("60000",
                "Invalid selective agent share request body.",
                "The agent criteria provided for selective sharing is either null or empty. " +
                        "Please provide valid selective agent sharing criteria."),
        INVALID_GENERAL_AGENT_SHARE_REQUEST_BODY("60001",
                "Invalid general agent share request body.",
                "The agent criteria provided for general sharing is either null or empty. " +
                        "Please provide valid general agent sharing criteria."),
        INVALID_SELECTIVE_AGENT_UNSHARE_REQUEST_BODY("60002",
                "Invalid selective agent unshare request body.",
                "The agent criteria provided for selective unsharing is either null or empty. " +
                        "Please provide valid selective agent unsharing criteria."),
        INVALID_GENERAL_AGENT_UNSHARE_REQUEST_BODY("60003",
                "Invalid general agent unshare request body.",
                "The agent criteria provided for general unsharing is either null or empty. " +
                        "Please provide valid general agent unsharing criteria."),
        INVALID_UUID_FORMAT("60004",
                "Invalid UUID format.",
                "The UUID provided in the request is not in a valid format. " +
                        "Please provide a valid UUID."),
        INVALID_AGENT_SHARE_PATCH_REQUEST_BODY("60005",
                "Invalid agent share patch request body.",
                "The agent share patch request body is either null or empty. " +
                        "Please provide a valid agent share patch request body."),
        ERROR_MISSING_AGENT_CRITERIA("60006",
                "Missing agent criteria in the request body.",
                "The agent criteria is missing in the request body. Please provide the agent criteria to proceed."),
        ERROR_UNSUPPORTED_AGENT_SHARE_PATCH_PATH("60007",
                "Unsupported agent share patch path.",
                "The provided patch path to update attributes of shared agent is not supported. " +
                        "Please provide a valid patch path."),
        ERROR_EMPTY_AGENT_SHARE_PATCH_PATH("60008",
                "Empty agent share patch path.",
                "The provided patch path to update attributes of shared agent is empty. " +
                        "Please provide a valid patch path."),
        ERROR_UNSUPPORTED_AGENT_SHARE_POLICY("60009",
                "Unsupported agent share policy.",
                "The provided agent share policy is not supported. Please provide a valid agent share policy."),
        ERROR_MISSING_AGENT_IDS("60010",
                "Missing agent ID(s) in the request body.",
                "The agent ID(s) is missing in the request body. Please provide the agent ID to proceed."),
        ERROR_INVALID_LIMIT("60011",
                "Invalid limit value.",
                "The limit value provided in the request is invalid. Please provide a valid limit value."),
        ERROR_INVALID_CURSOR("60012",
                "Invalid cursor value.",
                "The cursor value provided in the request is invalid. Please provide a valid cursor value."),

        // Server errors.
        ERROR_INITIATING_AGENTS_API_SERVICE("65001",
                "Error initiating AgentsApiService.",
                "Error occurred while initiating AgentsApiService.");

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

        @Override
        public String toString() {

            return code + " | " + message;
        }
    }
}
