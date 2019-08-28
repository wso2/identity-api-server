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

package org.wso2.carbon.identity.api.server.challenge.common;

/**
 * Contains all the server challenge management related constants.
 */
public class ChallengeConstant {

    public static final String CHALLENGE_QUESTION_PREFIX = "CQM-";
    public static final String CHALLENGES_PATH_COMPONENT = "/challenges";
    public static final String CHALLENGE_QUESTION_SET_PATH_COMPONENT = CHALLENGES_PATH_COMPONENT +
            "/%s";

    /**
     * Enum for server challenge management related errors in the format of
     * Error Code - code to identify the error
     * Error Message - What went wrong
     * Error Description - Why it went wrong
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_RETRIVING_CHALLENGE("50002",
                "Unable to get the challenge.",
                "Server encountered an error while retrieving the challenge."),
        ERROR_CODE_ERROR_RETRIVING_CHALLENGES("50003",
                "Unable to get the challenges.",
                "Server encountered an error while retrieving challenges."),
        ERROR_CODE_ERROR_ADDING_CHALLENGES("50004",
                "Unable to add challenge set.",
                "Server encountered an error while setting answers to the user challenges."),
        ERROR_CODE_ERROR_UPDATING_CHALLENGE_SET("50005",
                "Unable to update challenge set.",
                "Server encountered an error while updating the challenge set."),
        ERROR_CODE_ERROR_ADDING_CHALLENGE_QUESTION_TO_A_SET("50006",
                "Unable to add a new challenge question.",
                "Server encountered an error while adding a new question to the set."),
        ERROR_CODE_ERROR_DELETING_CHALLENGES("50007",
                "Unable to remove challenges.",
                "Server encountered an error while removing the challenge set."),
        ERROR_CODE_ERROR_DELETING_CHALLENGE("50008",
                "Unable to remove challenge question.",
                "Server encountered an error while removing the challenge question."),
        ERROR_CODE_ERROR_OPERATION_NOT_SUPPORTED("50009",
                "Patch operation not supported.",
                "Operation is not supported on the challenge set patch API."),
        ERROR_CHALLENGE_SET_NOT_EXISTS("500010",
                "Challenge set does not exists.",
                "Specified challenge does not exist in the system, hence unable to proceed.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return ChallengeConstant.CHALLENGE_QUESTION_PREFIX + code;
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
