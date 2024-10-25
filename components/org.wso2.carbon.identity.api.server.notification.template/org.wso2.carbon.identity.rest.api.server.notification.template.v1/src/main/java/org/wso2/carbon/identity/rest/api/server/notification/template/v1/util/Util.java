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

package org.wso2.carbon.identity.rest.api.server.notification.template.v1.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.email.mgt.util.I18nEmailUtil;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.notification.template.common.Constants;
import org.wso2.carbon.identity.api.server.notification.template.common.TemplatesServiceHolder;
import org.wso2.carbon.identity.governance.exceptions.notiification.NotificationTemplateManagerClientException;
import org.wso2.carbon.identity.governance.exceptions.notiification.NotificationTemplateManagerException;
import org.wso2.carbon.identity.governance.exceptions.notiification.NotificationTemplateManagerServerException;
import org.wso2.carbon.identity.governance.model.NotificationTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.EmailTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SMSTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SimpleTemplate;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLDecode;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLEncode;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.APP_TEMPLATES_PATH;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.NOTIFICATION_CHANNEL_EMAIL;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.NOTIFICATION_TEMPLATES_API_BASE_PATH_EMAIL;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.NOTIFICATION_TEMPLATES_API_BASE_PATH_SMS;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.NOTIFICATION_TEMPLATES_API_PATH;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.ORG_TEMPLATES_PATH;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.SYSTEM_TEMPLATES_PATH;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.TEMPLATE_TYPES_PATH;

/**
 * Utility class for notification templates API
 */
public class Util {

    private static final Log log = LogFactory.getLog(Util.class);

    private static final String ERROR_CODE_DELIMITER = "-";

    /**
     * Builds a list of SimpleTemplate objects using the provided NotificationTemplate list.
     *
     * @param templates           NotificationTemplate list.
     * @param applicationUuid     Application UUID.
     * @param templateOwner       Template owner.
     * @param notificationChannel Notification channel.
     * @return List of SimpleTemplate objects.
     */
    public static List<SimpleTemplate> buildSimpleTemplateList(List<NotificationTemplate> templates,
                                           String applicationUuid, String templateOwner, String notificationChannel) {

        List<SimpleTemplate> simpleTemplates = new ArrayList<>();
        if (templates != null) {
            for (NotificationTemplate template : templates) {
                SimpleTemplate simpleTemplate = new SimpleTemplate();
                String templateTypeId = base64URLEncode(template.getDisplayName());
                String templateTypeLocation =
                        getTemplateTypeLocation(templateTypeId, notificationChannel);
                simpleTemplate.setSelf(getTemplateLocation(templateTypeLocation, applicationUuid,
                        template.getLocale(), templateOwner));
                simpleTemplate.setLocale(template.getLocale());
                simpleTemplates.add(simpleTemplate);
            }
        }
        return simpleTemplates;
    }

    /**
     * Resolves templateID using the templateTypeDisplayName.
     *
     * @param templateTypeDisplayName Display name of the template type.
     * @return templateID.
     */
    public static String resolveTemplateIdFromDisplayName(String templateTypeDisplayName) {

        return base64URLEncode(templateTypeDisplayName);
    }

    /**
     * Decodes the template type ID.
     *
     * @param encodedTemplateTypeId Encoded template type ID.
     * @return Decoded template type ID.
     */
    public static String decodeTemplateTypeId(String encodedTemplateTypeId) {

        try {
            return base64URLDecode(encodedTemplateTypeId);
        } catch (Throwable e) {
            throw handleError(Constants.ErrorMessage.ERROR_TEMPLATE_TYPE_NOT_FOUND);
        }
    }

    /**
     * Handles the error and returns an APIError object.
     *
     * @param error Error message.
     * @return APIError object.
     */
    public static APIError handleError(Constants.ErrorMessage error) {

        return new APIError(error.getHttpStatus(), getErrorBuilder(error).build());
    }

    /**
     * Handles the I18nEmailMgtException and returns an APIError object.
     *
     * @param exception I18nEmailMgtException.
     * @param errorEnum Error message.
     * @return APIError object.
     */
    public static APIError handleNotificationTemplateManagerException(NotificationTemplateManagerException exception,
                                                       Constants.ErrorMessage errorEnum) {

        ErrorResponse errorResponse;
        Response.Status status;
        String errorCode = extractErrorCode(exception.getErrorCode());
        if (exception instanceof NotificationTemplateManagerServerException
                && Constants.getNTMMappedErrorMessage(errorCode) != null) {
            // Specific error with code is found.
            Constants.ErrorMessage errorMessage = Constants.getNTMMappedErrorMessage(errorCode);
            errorResponse = getErrorBuilder(errorMessage).build(log, exception, errorEnum.getDescription());
            status = errorMessage.getHttpStatus();
        } else if (exception instanceof NotificationTemplateManagerClientException) {
            // Send client error with original exception message.
            errorResponse = getErrorBuilder(errorEnum).build(log, exception.getMessage());
            errorResponse.setDescription(exception.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else {
            // Server error
            errorResponse = getErrorBuilder(errorEnum).build(log, exception, errorEnum.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Builds EmailTemplateWithID using NotificationTemplate.
     *
     * @param internalTemplate NotificationTemplate object.
     * @return EmailTemplateWithID object.
     */
    public static EmailTemplateWithID buildEmailTemplateWithID(NotificationTemplate internalTemplate) {

        EmailTemplateWithID templateWithID = new EmailTemplateWithID();
        templateWithID.setLocale(internalTemplate.getLocale());
        templateWithID.setContentType(internalTemplate.getContentType());
        templateWithID.setSubject(internalTemplate.getSubject());
        templateWithID.setBody(internalTemplate.getBody());
        templateWithID.setFooter(internalTemplate.getFooter());
        return templateWithID;
    }

    /**
     * Builds SMSTemplateWithID using NotificationTemplate.
     *
     * @param internalTemplate NotificationTemplate object.
     * @return SMSTemplateWithID object.
     */
    public static SMSTemplateWithID buildSMSTemplateWithID(NotificationTemplate internalTemplate) {

        SMSTemplateWithID templateWithID = new SMSTemplateWithID();
        templateWithID.setLocale(internalTemplate.getLocale());
        templateWithID.setBody(internalTemplate.getBody());
        return templateWithID;
    }

    /**
     * Builds NotificationTemplate object using SMSTemplateWithID object.
     *
     * @param templateTypeId    Template type ID.
     * @param smsTemplateWithID SMSTemplateWithID Object.
     * @return NotificationTemplate object built using provided values.
     */
    public static NotificationTemplate buildNotificationTemplateWithSMSTemplateWithID(String templateTypeId,
                                                                               SMSTemplateWithID smsTemplateWithID) {

        String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
        NotificationTemplate notificationTemplate = new NotificationTemplate();
        notificationTemplate.setNotificationChannel(Constants.NOTIFICATION_CHANNEL_SMS);
        notificationTemplate.setLocale(smsTemplateWithID.getLocale());
        notificationTemplate.setBody(smsTemplateWithID.getBody());
        notificationTemplate.setDisplayName(templateTypeDisplayName);
        notificationTemplate.setLocale(smsTemplateWithID.getLocale());
        notificationTemplate.setType(I18nEmailUtil.getNormalizedName(templateTypeDisplayName));
        return notificationTemplate;
    }

    /**
     * Builds NotificationTemplate object using EmailTemplateWithID object.
     *
     * @param templateTypeId      Template type ID.
     * @param emailTemplateWithID EmailTemplateWithID Object.
     * @return NotificationTemplate object built using provided values.
     */
    public static NotificationTemplate buildNotificationTemplateWithEmailTemplateWithID(String templateTypeId,
                                                                           EmailTemplateWithID emailTemplateWithID) {

        String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
        NotificationTemplate notificationTemplate = new NotificationTemplate();
        notificationTemplate.setNotificationChannel(Constants.NOTIFICATION_CHANNEL_EMAIL);
        notificationTemplate.setLocale(emailTemplateWithID.getLocale());
        notificationTemplate.setBody(emailTemplateWithID.getBody());
        notificationTemplate.setDisplayName(templateTypeDisplayName);
        notificationTemplate.setType(I18nEmailUtil.getNormalizedName(templateTypeDisplayName));
        notificationTemplate.setSubject(emailTemplateWithID.getSubject());
        notificationTemplate.setFooter(emailTemplateWithID.getFooter());
        notificationTemplate.setContentType(emailTemplateWithID.getContentType());
        return notificationTemplate;
    }

    /**
     * Verify that the existence of the template type.
     *
     * @param notificationChannel     Notification channel.
     * @param templateTypeDisplayName Template type display name.
     */
    public static void verifyTemplateTypeExists(String notificationChannel, String templateTypeDisplayName) {

        try {
            boolean isTemplateTypeExists = TemplatesServiceHolder.getNotificationTemplateManager()
                    .isNotificationTemplateTypeExists(notificationChannel, templateTypeDisplayName,
                            getTenantDomainFromContext());
            if (!isTemplateTypeExists) {
                throw Util.handleError(Constants.ErrorMessage.ERROR_TEMPLATE_TYPE_NOT_FOUND);
            }
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_RETRIEVING_TEMPLATE_TYPE);
        }
    }

    /**
     * Builds the location of the template type.
     *
     * @param templateTypeId Template type ID.
     * @param notificationChannel   Notification channel type.
     * @return Location of the template.
     */
    public static String getTemplateTypeLocation(String templateTypeId, String notificationChannel) {

        String templateTypePath;
        // Only EMAIL and SMS are passed as the type. So, no need to check for other types.
        if (NOTIFICATION_CHANNEL_EMAIL.equals(notificationChannel)) {
            templateTypePath = NOTIFICATION_TEMPLATES_API_BASE_PATH_EMAIL;
        } else {
            templateTypePath = NOTIFICATION_TEMPLATES_API_BASE_PATH_SMS;
        }
        String location = V1_API_PATH_COMPONENT + NOTIFICATION_TEMPLATES_API_PATH + templateTypePath
                + TEMPLATE_TYPES_PATH + PATH_SEPARATOR + templateTypeId;
        return ContextLoader.buildURIForBody(location).toString();
    }

    /**
     * Generates EmailTemplateWithID using provided values.
     *
     * @param emailTemplate EmailTemplate object.
     * @param locale        Locale.
     * @return EmailTemplateWithID object.
     */
    public static EmailTemplateWithID buildEmailTemplateWithIdUsingEmailTemplate(
            org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.EmailTemplate emailTemplate,
            String locale) {
        EmailTemplateWithID emailTemplateWithID = new EmailTemplateWithID();
        emailTemplateWithID.setLocale(locale);
        emailTemplateWithID.setContentType(emailTemplate.getContentType());
        emailTemplateWithID.setSubject(emailTemplate.getSubject());
        emailTemplateWithID.setBody(emailTemplate.getBody());
        emailTemplateWithID.setFooter(emailTemplate.getFooter());
        return emailTemplateWithID;
    }

    /**
     * Generates SMSTemplateWithID using provided values.
     *
     * @param smsTemplate SMSTemplate object.
     * @param locale        Locale.
     * @return SMSTemplateWithID object.
     */
    public static SMSTemplateWithID buildSMSTemplateWithIdUsingSMSTemplate(
            org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SMSTemplate smsTemplate,
            String locale) {

        SMSTemplateWithID smsTemplateWithID = new SMSTemplateWithID();
        smsTemplateWithID.setLocale(locale);
        smsTemplateWithID.setBody(smsTemplate.getBody());
        return smsTemplateWithID;
    }


    /**
     * Generates self url for application templates.
     *
     * @param templateTypeLocation   Template type ID.
     * @param applicationUuid        Application UUID.
     * @param locale                 Locale.
     * @return                       SimpleTemplate object.
     */
    public static String getTemplateLocation(String templateTypeLocation, String applicationUuid, String locale,
                                             String templateOwner) {

        switch (templateOwner) {
            case Constants.NOTIFICATION_TEMPLATE_OWNER_APP:
                return templateTypeLocation + APP_TEMPLATES_PATH + PATH_SEPARATOR + applicationUuid
                        + PATH_SEPARATOR + locale;
            case Constants.NOTIFICATION_TEMPLATE_OWNER_ORG:
                return templateTypeLocation + ORG_TEMPLATES_PATH + PATH_SEPARATOR + locale;
            case Constants.NOTIFICATION_TEMPLATE_OWNER_SYSTEM:
                return templateTypeLocation + SYSTEM_TEMPLATES_PATH + PATH_SEPARATOR + locale;
            default:
                return null;
        }
    }

    private static ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).
                withMessage(errorMsg.getMessage()).withDescription(errorMsg.getDescription());
    }

    private static String extractErrorCode(String errorCodeWithScenario) {

        return errorCodeWithScenario.split(ERROR_CODE_DELIMITER)[1];
    }
}
