/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.user.invitation.management.common;

/**
 * Holds the constants which the shared user invitation management API component is using.
 */
public class UserInvitationMgtConstants {

    public static final String ERROR_PREFIX = "OUI-";

    /**
     * Enum for shared user invitation management related errors.
     * Error Code - code to identify the error.
     * Error Message - What went wrong.
     * Error Description - Why it went wrong.
     */
    public enum ErrorMessage {

        // Client errors.
        ERROR_CODE_USER_NOT_FOUND("60000",
                "Invalid user identifier is provided.",
                "Invalid user identifier %s is provided."),
        ERROR_CODE_INVALID_INVITATION("60001",
                "Invalid invitation.",
                "Provided invitation with the id %s is invalid."),
        ERROR_CODE_INVALID_CONFIRMATION_CODE("60002",
                "Invalid confirmation code.",
                "Could not validate the confirmation code %s."),
        ERROR_CODE_MULTIPLE_INVITATIONS_FOR_USER("60003",
                "Unable to create the invitation.",
                "Multiple invitations found for the user %s."),
        ERROR_CODE_UNSUPPORTED_LIMIT("60004",
                "Unsupported param.",
                "Limit param is not supported yet."),
        ERROR_CODE_UNSUPPORTED_OFFSET("60005",
                "Unsupported param.",
                "Offset param is not supported yet."),
        ERROR_CODE_UNSUPPORTED_SORT_ORDER("60006",
                "Unsupported param.",
                "Sort order param is not supported yet."),
        ERROR_CODE_UNSUPPORTED_SORT_BY("60007",
                "Unsupported param.",
                "Sort order param is not supported yet."),
        ERROR_CODE_ACTIVE_INVITATION_AVAILABLE("60008",
                "Invitation already exists.",
                "An active invitation already exists for the user %s."),
        ERROR_CODE_INVALID_FILTER("60009",
                "Invalid filter.",
                "Provided filter %s is not valid."),

        // Server errors.
        ERROR_CODE_CREATE_INVITATION("65001",
                "Unable to create the invitation.",
                "Could not create the invitation to the user %s."),
        ERROR_CODE_GET_INVITATIONS("65002",
                "Unable to retrieve the invitations.",
                "Could not retrieve the invitations for the organization."),
        ERROR_CODE_VALIDATE_INVITATION("65003",
                "Unable to validate the invitation.",
                "Could not validate the invitation with the confirmation code %s."),
        ERROR_CODE_DELETE_INVITATION("65004",
                "Unable to delete the invitation.",
                "Could not delete the invitation with the id %s."),
        ERROR_CODE_NOT_IMPLEMENTED("65100",
                "Not Implemented.",
                "Method is not implemented.");


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
