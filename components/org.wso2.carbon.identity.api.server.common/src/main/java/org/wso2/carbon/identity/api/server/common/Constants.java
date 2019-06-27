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
        ERROR_CODE_ERROR_RETRIVING_CHALLENGES_FOR_USER("10002", "Unable to get user challenges.", "Server Encountered an " +
                "error while retrieving challenges for user."),
        ERROR_CODE_ERROR_RETRIVING_CHALLENGE_ANSWERS_OF_USER(CHALLENGE_QUESTION_PREFIX.getPrefix() + "10003",
                "Unable to get user challenge answers.",
                "Server Encountered an error while retrieving challenge answers of user."),
        ERROR_CODE_ERROR_RETRIVING_CHALLENGE_ANSWER_OF_USER(CHALLENGE_QUESTION_PREFIX.getPrefix() + "10004",
                "Unable to get user challenge answer.",
                "Server Encountered an error while retrieving challenge answer of user."),
        ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWERS_OF_USER(CHALLENGE_QUESTION_PREFIX.getPrefix() + "10005",
                "Unable to set user challenge answers.",
                "Server Encountered an error while setting answers to the user challenges."),
        ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWERS_OF_USER(CHALLENGE_QUESTION_PREFIX.getPrefix() + "10006",
                "Unable to update user challenge answer.",
                "Server Encountered an error while updating the answers to the user challenges."),
        ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWERS_OF_USER(CHALLENGE_QUESTION_PREFIX.getPrefix() + "10007",
                "Unable to remove user challenge answers.",
                "Server Encountered an error while removing answers of the user challenges."),
        ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWER_OF_USER(CHALLENGE_QUESTION_PREFIX.getPrefix() + "10008",
                "Unable to update user challenge answer.",
                "Server Encountered an error while updating the answer of the user challenge."),
        ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWER_OF_USER(CHALLENGE_QUESTION_PREFIX.getPrefix() + "10009",
                "Unable to update user challenge answer.",
                "Server Encountered an error while updating the answer of the user challenge."),
        ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWER_OF_USER(CHALLENGE_QUESTION_PREFIX.getPrefix() + "10010",
                "Unable to remove user challenge answer.",
                "Server Encountered an error while removing answer of the user challenge."),
        ERROR_CHALLENGE_ANSWER_MISSING(CHALLENGE_QUESTION_PREFIX.getPrefix() + "10011",
                "Invalid Request.", "Challenge question is missing in the user " +
                "challenge answer request."),
        //      ERROR_CODE_UNABLE_TO_GET_USER_CHALLLENGES("10012","Unable to get user challenges." , "Server Encountered an " +
//                "error while retrieving challenges for user."),
        ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGES(CHALLENGE_QUESTION_PREFIX.getPrefix() + "10013",
                "Challenge Answers Already set.",
                "User has already answered some challenges. Hence, Unable to add new Answers."),
        ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGES(CHALLENGE_QUESTION_PREFIX.getPrefix() + "10014",
                "Challenge Answers Not set.", "User has not" +
                " answered any challenges. Hence, Unable to process."),
        ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGE(CHALLENGE_QUESTION_PREFIX.getPrefix() + "10015",
                "Challenge Answer Already set.", "User has already " +
                "answered this challenge. Hence, Unable to as a new challenge answer."),
        ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGE(CHALLENGE_QUESTION_PREFIX.getPrefix() + "10016",
                "Challenge Answer Not set.", "User has not " +
                "answered this challenge. Hence, Unable to process."),
        ERROR_CODE_CHALLENGE_QUESTION_NOT_FOUND(CHALLENGE_QUESTION_PREFIX.getPrefix() + "18017",
                "No challenge question found.", "");;

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
