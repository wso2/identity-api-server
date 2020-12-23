/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.notification.sender.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Notification sender management related constant class.
 */
public class NotificationSenderManagementConstants {

    public static final String NOTIFICATION_SENDER_ERROR_PREFIX = "NSM-";
    public static final String NOTIFICATION_SENDER_CONTEXT_PATH = "/notification-senders";
    public static final String PUBLISHER_RESOURCE_TYPE = "Publisher";
    public static final String PUBLISHER_TYPE_PROPERTY = "type";
    public static final String DEFAULT_EMAIL_PUBLISHER = "EmailPublisher";
    public static final String DEFAULT_SMS_PUBLISHER = "SMSPublisher";
    public static final String PUBLISHER_FILE_EXTENSION = ".xml";
    public static final String RESOURCE_NOT_EXISTS_ERROR_CODE = "CONFIGM_00017";
    public static final String PLACEHOLDER_IDENTIFIER = "$";
    public static final String INLINE_BODY_PARAM_PREFIX = "body.";
    public static final String INLINE_BODY_PROPERTY = "body";
    public static final String PLUS = "+";
    public static final String URL_ENCODED_SPACE = "%20";
    public static final String CONFIG_MGT_ERROR_CODE_DELIMITER = "_";

    // Email Sender's main properties.
    public static final String SMTP_SERVER_HOST = "smtpServerHost";
    public static final String SMTP_PORT = "smtpPort";
    public static final String FROM_ADDRESS = "fromAddress";
    public static final String USERNAME = "userName";
    public static final String PASSWORD = "password";
    public static final String EMAIL_PUBLISHER_TYPE = "email";

    // SMS Sender's main properties.
    public static final String PROVIDER = "provider";
    public static final String PROVIDER_URL = "providerURL";
    public static final String KEY = "key";
    public static final String SECRET = "secret";
    public static final String SENDER = "sender";
    public static final String CLIENT_HTTP_METHOD_PROPERTY = "http.client.method";
    public static final String SMS_PUBLISHER_TYPE = "sms";

    // Constant for eventPublisher file generation.
    public static final String ROOT_ELEMENT = "eventPublisher";
    public static final String PUBLISHER_NAME = "name";
    public static final String PROCESSING_KEY = "processing";
    public static final String STATISTICS_KEY = "statistics";
    public static final String TRACE_KEY = "trace";
    public static final String XMLNS_KEY = "xmlns";
    public static final String XMLNS_VALUE = "http://wso2.org/carbon/eventpublisher";
    public static final String ENABLE = "enable";
    public static final String DISABLE = "disable";
    public static final String FROM = "from";
    public static final String STREAM_NAME = "streamName";
    public static final String STREAM_VERSION = "version";
    public static final String MAPPING = "mapping";
    public static final String CUSTOM_MAPPING_KEY = "customMapping";
    public static final String MAPPING_TYPE_KEY = "type";
    public static final String TEXT = "text";
    public static final String JSON = "json";
    public static final String INLINE = "inline";
    public static final String EMAIL_INLINE_BODY = "{{body}}{{footer}}";
    public static final String TO = "to";
    public static final String ADAPTER_TYPE_KEY = "eventAdapterType";
    public static final String ADAPTER_TYPE_EMAIL_VALUE = "email";
    public static final String ADAPTER_TYPE_HTTP_VALUE = "http";
    public static final String ADAPTER_PROPERTY = "property";
    public static final String ADAPTER_PROPERTY_NAME = "name";
    public static final String EMAIL_ADDRESS_PROPERTY = "email.address";
    public static final String EMAIL_ADDRESS_VALUE = "{{send-to}}";
    public static final String EMAIL_TYPE_PROPERTY = "email.type";
    public static final String EMAIL_TYPE_VALUE = "{{content-type}}";
    public static final String EMAIL_SUBJECT_PROPERTY = "email.subject";
    public static final String EMAIL_SUBJECT_VALUE = "{{subject}}";
    public static final String SMTP_PASSWORD_PROPERTY = "mail.smtp.password";
    public static final String SMTP_FROM_PROPERTY = "mail.smtp.from";
    public static final String SMTP_USER_PROPERTY = "mail.smtp.user";
    public static final String SMTP_HOST_PROPERTY = "mail.smtp.host";
    public static final String SMTP_PORT_PROPERTY = "mail.smtp.port";
    public static final String HTTP_URL_PROPERTY = "http.url";
    public static final String CONSTANT_HTTP_POST = "HttpPost";

    public static final List<String> INTERNAL_PROPERTIES =
            Collections.unmodifiableList(Arrays.asList(STREAM_NAME, STREAM_VERSION, PUBLISHER_TYPE_PROPERTY));
    public static final List<String> PROPERTIES_TO_SKIP_AT_ADAPTER_CONFIG =
            Collections.unmodifiableList(
                    Arrays.asList(STREAM_NAME, STREAM_VERSION, PUBLISHER_TYPE_PROPERTY, INLINE_BODY_PROPERTY));

    /**
     * Enums for error messages.
     */
    public enum ErrorMessage {

        // Client errors 600xx.
        ERROR_CODE_PUBLISHER_NOT_EXISTS_IN_SUPER_TENANT("60001",
                "No matching notification sender found in super tenant.",
                "Can not find a notification sender named: %s, in carbon.super."),
        ERROR_CODE_CONFLICT_PUBLISHER("60002", "Notification sender already exists.",
                "There exists a notification sender: %s in the tenant."),
        ERROR_CODE_SMS_PROVIDER_REQUIRED("60003", "Required attribute is missing",
                "SMS provider is not defined for notification sender."),
        ERROR_CODE_SMS_PAYLOAD_NOT_FOUND("60004", "SMS send API payload is not defined",
                "SMS send API payload for provider: %s, is not defined in file or POST body properties."),
        ERROR_CODE_SMS_PROVIDER_URL_REQUIRED("60005", "Required attribute is missing",
                "SMS provider url is not defined for notification sender."),

        // Server errors 650xx.
        ERROR_CODE_NO_ACTIVE_PUBLISHERS_FOUND("65001", "No active notification senders found.",
                "There exists no active notification senders in tenant: %s."),
        ERROR_CODE_SERVER_ERRORS_GETTING_EVENT_PUBLISHER("65002", "Error while getting event publisher configurations.",
                "Error occurred while retrieving event publisher configurations: %s."),
        ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDER("65003", "Error while getting notification sender.",
                "Error while retrieving notification sender resource: %s."),
        ERROR_CODE_ERROR_ADDING_NOTIFICATION_SENDER("65004", "Unable to add notification sender.",
                "Server encountered an error while adding the notification sender resource: %s"),
        ERROR_CODE_ERROR_DELETING_NOTIFICATION_SENDER("65005", "Unable to delete notification sender.",
                "Server encountered an error while deleting the notification sender resource: %s"),
        ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDERS_BY_TYPE("65006", "Error while getting notification senders.",
                "Error while retrieving %s notification sender resources."),
        ERROR_CODE_ERROR_UPDATING_NOTIFICATION_SENDER("65007", "Unable to update notification sender.",
                "Error while updating notification sender: %s."),
        ERROR_CODE_TRANSFORMER_EXCEPTION("65008", "Transformer Exception.", "Transformer Exception: %s ."),
        ERROR_CODE_PARSER_CONFIG_EXCEPTION("65009", "Parser Configuration Exception.",
                "Parser Configuration Exception: %s."),
        ERROR_CODE_NO_RESOURCE_EXISTS("65010", "No notification sender found.",
                "No notification sender found with name: %s.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return NOTIFICATION_SENDER_ERROR_PREFIX + code;
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
