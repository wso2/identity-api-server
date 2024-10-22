/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.notification.template.common;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response.Status;

import static org.wso2.carbon.email.mgt.constants.TemplateMgtConstants.ErrorCodes.ERROR_ADDING_TEMPLATE;
import static org.wso2.carbon.email.mgt.constants.TemplateMgtConstants.ErrorCodes.ERROR_RESOLVING_MAIN_APPLICATION;
import static org.wso2.carbon.email.mgt.constants.TemplateMgtConstants.ErrorCodes
        .ERROR_SYSTEM_RESOURCE_DELETION_NOT_ALLOWED;
import static org.wso2.carbon.email.mgt.constants.TemplateMgtConstants.ErrorCodes.TEMPLATE_ALREADY_EXISTS;
import static org.wso2.carbon.email.mgt.constants.TemplateMgtConstants.ErrorCodes.TEMPLATE_NOT_FOUND;
import static org.wso2.carbon.email.mgt.constants.TemplateMgtConstants.ErrorCodes.TEMPLATE_TYPE_ALREADY_EXISTS;
import static org.wso2.carbon.email.mgt.constants.TemplateMgtConstants.ErrorCodes.TEMPLATE_TYPE_NOT_FOUND;

/**
 * Constants related to notification templates.
 */
public class Constants {

    public static final String NOTIFICATION_TEMPLATES_ERROR_CODE_PREFIX = "NTM-";
    public static final String NOTIFICATION_TEMPLATES_API_BASE_PATH_EMAIL = "/email";
    public static final String NOTIFICATION_TEMPLATES_API_BASE_PATH_SMS = "/sms";
    public static final String NOTIFICATION_TEMPLATES_API_PATH = "/notification";
    public static final String TEMPLATE_TYPES_PATH = "/template-types";
    public static final String APP_TEMPLATES_PATH =  "/app-templates";
    public static final String ORG_TEMPLATES_PATH = "/org-templates";
    public static final String SYSTEM_TEMPLATES_PATH = "/system-templates";
    public static final String PATH_SEPARATOR = "/";
    public static final String NOTIFICATION_CHANNEL_EMAIL = "EMAIL";
    public static final String NOTIFICATION_CHANNEL_SMS = "SMS";
    public static final String NOTIFICATION_TEMPLATE_OWNER_APP = "APP";
    public static final String NOTIFICATION_TEMPLATE_OWNER_ORG = "ORG";
    public static final String NOTIFICATION_TEMPLATE_OWNER_SYSTEM = "SYSTEM";

    // ERROR MESSAGES
    private static final Map<String, ErrorMessage> NTM_ERROR_CODE_MAP = new HashMap<>();

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        ERROR_TEMPLATE_TYPE_ALREADY_EXISTS("65001", Status.CONFLICT,
                "Template Type already exists in the system.",
                "A template type for the provided template display name already exists " +
                        "in the system."),
        ERROR_TEMPLATE_TYPE_NOT_FOUND("65002", Status.NOT_FOUND,
                "Template Type does not exist.",
                "Specified template type does not exist in the system."),
        ERROR_TEMPLATE_ALREADY_EXISTS("65003", Status.CONFLICT,
                "Template already exists in the system.",
                "A template for the provided template id already exists in the system."),
        ERROR_TEMPLATE_NOT_FOUND("65004", Status.NOT_FOUND,
                "Template does not exist.",
                "Specified template does not exist in the system."),
        ERROR_ERROR_RETRIEVING_TEMPLATE_TYPES("60002", Status.INTERNAL_SERVER_ERROR,
                "Unable to retrieve template types.",
                "Server encountered an error while retrieving template types."),
        ERROR_ERROR_RETRIEVING_TEMPLATE_TYPE("60003", Status.INTERNAL_SERVER_ERROR,
                "Unable to retrieve the template type.",
                "Server encountered an error while retrieving the template " +
                        "type identified by the given template-type-id."),
        ERROR_ERROR_ADDING_TEMPLATE_TYPE("60004", Status.INTERNAL_SERVER_ERROR,
                "Unable to add the template type.",
                "Server encountered an error while adding template type."),
        ERROR_ERROR_DELETING_TEMPLATE_TYPE("60005", Status.INTERNAL_SERVER_ERROR,
                "Unable to delete the template type.",
                "Server encountered an error while deleting the template type."),
        ERROR_ERROR_ADDING_TEMPLATE("60006", Status.INTERNAL_SERVER_ERROR,
                "Unable to add the template.",
                "Server encountered an error while adding the template to the system."),
        ERROR_ERROR_UPDATING_TEMPLATE("60007", Status.INTERNAL_SERVER_ERROR,
                "Unable to update the template.",
                "Server encountered an error while updating the template."),
        ERROR_ERROR_RETRIEVING_TEMPLATE("60008", Status.INTERNAL_SERVER_ERROR,
                "Unable to retrieve the template.",
                "Server encountered an error while retrieving the template " +
                        "identified by the given template-type-id and the template-id."),
        ERROR_ERROR_DELETING_EMAIL_TEMPLATE("60009", Status.INTERNAL_SERVER_ERROR,
                "Unable to delete the email template.",
                "Server encountered an error while deleting the email template."),
        ERROR_ERROR_DELETING_SMS_TEMPLATE("60010", Status.INTERNAL_SERVER_ERROR,
                "Unable to delete the SMS template.",
                "Server encountered an error while deleting the SMS template."),
        ERROR_ERROR_RETRIEVING_TEMPLATES("60012", Status.INTERNAL_SERVER_ERROR,
                "Unable to retrieve templates.",
                "Server encountered an error while retrieving templates."),
        ERROR_ERROR_RESETTING_TEMPLATE_TYPE("60013", Status.INTERNAL_SERVER_ERROR,
                "Unable to reset the template type.",
                "Server encountered an error while deleting the templates of the template type."),
        ERROR_ERROR_RESOLVING_MAIN_APPLICATION("60014", Status.INTERNAL_SERVER_ERROR,
                "Unable to resolve the main application.",
                "Server encountered an error while resolving the main application."),
        ERROR_ERROR_INVALID_NOTIFICATION_CHANNEL("60015", Status.BAD_REQUEST,
                "Invalid notification channel.",
                "Notification channel can only be either 'EMAIL' or 'SMS'."),
        ERROR_ERROR_SYSTEM_RESOURCE_DELETION_NOT_ALLOWED("60016", Status.FORBIDDEN,
                "System resource deletion not allowed.",
                "System resources cannot be deleted.");

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

            return NOTIFICATION_TEMPLATES_ERROR_CODE_PREFIX + code;
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
        NTM_ERROR_CODE_MAP.put(TEMPLATE_TYPE_ALREADY_EXISTS, ErrorMessage.ERROR_TEMPLATE_TYPE_ALREADY_EXISTS);
        NTM_ERROR_CODE_MAP.put(TEMPLATE_TYPE_NOT_FOUND, ErrorMessage.ERROR_TEMPLATE_TYPE_NOT_FOUND);
        NTM_ERROR_CODE_MAP.put(TEMPLATE_ALREADY_EXISTS, ErrorMessage.ERROR_TEMPLATE_ALREADY_EXISTS);
        NTM_ERROR_CODE_MAP.put(TEMPLATE_NOT_FOUND, ErrorMessage.ERROR_TEMPLATE_NOT_FOUND);
        NTM_ERROR_CODE_MAP.put(ERROR_ADDING_TEMPLATE, ErrorMessage.ERROR_ERROR_ADDING_TEMPLATE);
        NTM_ERROR_CODE_MAP.put(ERROR_RESOLVING_MAIN_APPLICATION, ErrorMessage.ERROR_ERROR_RESOLVING_MAIN_APPLICATION);
        NTM_ERROR_CODE_MAP.put(ERROR_SYSTEM_RESOURCE_DELETION_NOT_ALLOWED,
                ErrorMessage.ERROR_ERROR_SYSTEM_RESOURCE_DELETION_NOT_ALLOWED);
    }

    public static ErrorMessage getNTMMappedErrorMessage(String errorCode) {

        return NTM_ERROR_CODE_MAP.get(errorCode);
    }
}
