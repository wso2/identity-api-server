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

package org.wso2.carbon.identity.api.server.email.template.common;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;

import static org.wso2.carbon.email.mgt.constants.I18nMgtConstants.ErrorCodes.EMAIL_TEMPLATE_TYPE_ALREADY_EXISTS;
import static org.wso2.carbon.email.mgt.constants.I18nMgtConstants.ErrorCodes.EMAIL_TEMPLATE_TYPE_NODE_FOUND;

/**
 * Claim Management constant class.
 */
public class Constants {

    public static final String EMAIL_TEMPLATES_ERROR_CODE_PREFIX = "ETM-";
    public static final String EMAIL_TEMPLATES_API_BASE_PATH = "/email";
    public static final String EMAIL_TEMPLATE_TYPES_PATH = "/template-types";
    public static final String EMAIL_TEMPLATES_PATH = "/templates";
    public static final String PATH_SEPARATOR = "/";

    private static final Map<String, ErrorMessage> ERROR_CODE_MAP = new HashMap<>();

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        // TODO: 2019-10-07 Double check error codes
        ERROR_RETRIEVING_EMAIL_TEMPLATE_TYPES("50002", Status.INTERNAL_SERVER_ERROR,
                "Unable to retrieve email template types.",
                "Server encountered an error while retrieving email template types."),
        ERROR_RETRIEVING_EMAIL_TEMPLATE_TYPE("50003", Status.INTERNAL_SERVER_ERROR,
                "Unable to retrieve the email template type.",
                "Server encountered an error while retrieving the email template " +
                        "type identified by the given template-type-id."),
        ERROR_RETRIEVING_EMAIL_TEMPLATE("50004", Status.INTERNAL_SERVER_ERROR,
                "Unable to retrieve the email template.",
                "Server encountered an error while retrieving the email template " +
                        "identified by the given template-type-id and the template-id."),
        ERROR_ADDING_EMAIL_TEMPLATE_TYPE("50005", Status.INTERNAL_SERVER_ERROR,
                "Unable to add the email template type.",
                "Server encountered an error while adding the email template type."),
        ERROR_ADDING_EMAIL_TEMPLATE("50006", Status.INTERNAL_SERVER_ERROR,
                "Unable to add the email template.",
                "Server encountered an error while adding the email template to the system."),
        ERROR_DELETING_EMAIL_TEMPLATE_TYPE("50007", Status.INTERNAL_SERVER_ERROR,
                "Unable to delete the email template type.",
                "Server encountered an error while deleting the email template type."),
        ERROR_DELETING_EMAIL_TEMPLATE("50008", Status.INTERNAL_SERVER_ERROR,
                "Unable to delete the email template.",
                "Server encountered an error while deleting the email template."),
        ERROR_UPDATING_EMAIL_TEMPLATE_TYPE("50009", Status.INTERNAL_SERVER_ERROR,
                "Unable to update the email template type.",
                "Server encountered an error while updating the email template type."),
        ERROR_UPDATING_EMAIL_TEMPLATE("500010", Status.INTERNAL_SERVER_ERROR,
                "Unable to update the email template.",
                "Server encountered an error while updating the email template."),
        ERROR_INVALID_TEMPLATE_TYPE_ID("500011", Status.BAD_REQUEST,
                "Provided email template-type-id is invalid.",
                "Server encountered an error while processing the given template-type-id."),
        ERROR_EMAIL_TEMPLATE_TYPE_NOT_FOUND("500012", Status.NOT_FOUND,
                "Email Template Type does not exists.",
                "Specified email template type does not exist in the system."),
        ERROR_EMAIL_TEMPLATE_NOT_FOUND("500013", Status.NOT_FOUND,
                "Email Template does not exists.",
                "Specified email template does not exist in the system."),
        ERROR_EMAIL_TEMPLATE_ALREADY_EXISTS("500014", Status.CONFLICT,
                "Email Template already exists in the system.",
                "An email template for the provided template id already exists in the system."),
        ERROR_EMAIL_TEMPLATE_TYPE_ALREADY_EXISTS("500015", Status.CONFLICT,
                "Email Template Type already exists in the system.",
                "An email template type for the provided template display name already exists " +
                        "in the system."),
        ERROR_PAGINATION_NOT_SUPPORTED("500016", Status.NOT_IMPLEMENTED,
                "Pagination is not yet supported.",
                "Please remove 'limit' and 'offset' parameters from the request and try again."),
        ERROR_SORTING_NOT_SUPPORTED("500017", Status.NOT_IMPLEMENTED,
                "Sorting is not yet supported.",
                "Please remove 'sortOrder' and 'sortBy' parameters from the request and try again.");

        private final String message;
        private final Status httpStatus;
        private final String code;
        private final String description;

        ErrorMessage(String code, Status httpStatus, String message, String description) {

            this.code = code;
            this.httpStatus = httpStatus;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return EMAIL_TEMPLATES_ERROR_CODE_PREFIX + code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }

        public Status getHttpStatus() {

            return httpStatus;
        }

        @Override
        public String toString() {

            return code + " | " + message;
        }
    }

    static {
        ERROR_CODE_MAP.put(EMAIL_TEMPLATE_TYPE_NODE_FOUND, ErrorMessage.ERROR_EMAIL_TEMPLATE_TYPE_NOT_FOUND);
        ERROR_CODE_MAP.put(EMAIL_TEMPLATE_TYPE_ALREADY_EXISTS, ErrorMessage.ERROR_EMAIL_TEMPLATE_TYPE_ALREADY_EXISTS);
    }

    public static ErrorMessage getMappedErrorMessage(String errorCode) {

        return ERROR_CODE_MAP.get(errorCode);
    }
}
