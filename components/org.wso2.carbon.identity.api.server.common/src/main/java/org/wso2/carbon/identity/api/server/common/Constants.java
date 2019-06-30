/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.common;

import static org.wso2.carbon.identity.api.server.common.Constants.ErrorPrefix.CHALLENGE_QUESTION_PREFIX;
import static org.wso2.carbon.identity.api.server.common.Constants.ErrorPrefix.USER_MANAGEMENT_PREFIX;

public class Constants {
    public static final String OPERATION_ADD = "ADD";
    public static final String TENANT_NAME_FROM_CONTEXT = "TenantNameFromContext";
    public static final String ERROR_CODE_DELIMITER = "-";
    public static final String CORRELATION_ID_MDC = "Correlation-ID";

    public enum ErrorPrefix {

        CHALLENGE_QUESTION_PREFIX("CQM-"),
        USER_MANAGEMENT_PREFIX("UMG-");

        private final String prefix;

        ErrorPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    public enum ErrorMessages {

        ERROR_CODE_INVALID_USERNAME(USER_MANAGEMENT_PREFIX.getPrefix() + "10001", "Invalid UserID provided", "The " +
                "provided userId is invalid."),
        ERROR_CODE_ERROR_RETRIVING_CHALLENGE(CHALLENGE_QUESTION_PREFIX.getPrefix() + "20002",
                "Unable to get the challenge.",
                "Server Encountered an error while retrieving the challenge."),
        ERROR_CODE_ERROR_RETRIVING_CHALLENGES(CHALLENGE_QUESTION_PREFIX.getPrefix() + "20003",
                "Unable to get the challenges.",
                "Server Encountered an error while retrieving challenges."),
        ERROR_CODE_ERROR_ADDING_CHALLENGES(CHALLENGE_QUESTION_PREFIX.getPrefix() + "20004",
                "Unable to add challenge set.",
                "Server Encountered an error while setting answers to the user challenges."),
        ERROR_CODE_ERROR_UPDATING_CHALLENGE_SET(CHALLENGE_QUESTION_PREFIX.getPrefix() + "20005",
                "Unable to update challenge set.",
                "Server Encountered an error while updating the challenge set."),
        ERROR_CODE_ERROR_ADDING_CHALLENGE_QUESTION_TO_A_SET(CHALLENGE_QUESTION_PREFIX.getPrefix() + "20006",
                "Unable to add a new challenge question.",
                "Server Encountered an error while adding a new question to the set."),
        ERROR_CODE_ERROR_DELETING_CHALLENGES(CHALLENGE_QUESTION_PREFIX.getPrefix() + "20007",
                "Unable to remove challenges.",
                "Server Encountered an error while removing the challenge set."),
        ERROR_CODE_ERROR_DELETING_CHALLENGE(CHALLENGE_QUESTION_PREFIX.getPrefix() + "20008",
                "Unable to remove challenge question.",
                "Server Encountered an error while removing the challenge question."),
        ERROR_CODE_ERROR_OPERATION_NOT_SUPPORTED(CHALLENGE_QUESTION_PREFIX.getPrefix() + "20009",
                "Patch operation not supported.",
                "Operation is not supported on the challenge set patch API."),
        ERROR_CHALLENGE_SET_NOT_EXISTS(CHALLENGE_QUESTION_PREFIX.getPrefix() + "20010",
                "Challenge set does not exists.", "Specified Challenge does not exist in the system, hence unable to " +
                "proceed.");

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

        @Override
        public String toString() {
            return code + " | " + message;
        }

    }

}
