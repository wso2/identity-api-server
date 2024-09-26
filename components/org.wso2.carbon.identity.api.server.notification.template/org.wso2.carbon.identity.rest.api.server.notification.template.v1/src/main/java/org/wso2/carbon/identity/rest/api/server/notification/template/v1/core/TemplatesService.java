/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.server.notification.template.v1.core;

import org.wso2.carbon.identity.api.server.notification.template.common.Constants;
import org.wso2.carbon.identity.api.server.notification.template.common.TemplatesServiceHolder;
import org.wso2.carbon.identity.governance.exceptions.notiification.NotificationTemplateManagerException;
import org.wso2.carbon.identity.governance.model.NotificationTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.EmailTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.EmailTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SMSTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SMSTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SimpleTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.util.Util;

import java.util.List;

import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.APP_TEMPLATES_PATH;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.PATH_SEPARATOR;

/**
 * Service class for application email templates.
 */
public class TemplatesService {

    /**
     * Adds a new organization email template to the given template type. Template ID should not exist in the system.
     *
     * @param templateTypeId      Template type in which the template should be added.
     * @param emailTemplateWithID New email template.
     * @return Location of the newly created template if successful, 409 if template already exists, 500 otherwise.
     */
    public SimpleTemplate addEmailTemplate(String templateTypeId, EmailTemplateWithID emailTemplateWithID) {

        SimpleTemplate simpleTemplate = addEmailTemplate(templateTypeId, emailTemplateWithID, null);
        simpleTemplate.setSelf(getOrgTemplateLocation(templateTypeId, emailTemplateWithID.getLocale(),
                Constants.NOTIFICATION_CHANNEL_EMAIL));
        return simpleTemplate;
    }

    /**
     * Adds a new application email template to the given template type. Template ID should not exist in the system.
     *
     * @param templateTypeId      Template type in which the template should be added.
     * @param emailTemplateWithID New email template.
     * @param applicationUuid     Application UUID.
     * @return Location of the newly created template if successful, 409 if template already exists, 500 otherwise.
     */
    public SimpleTemplate addEmailTemplate(String templateTypeId, EmailTemplateWithID emailTemplateWithID,
            String applicationUuid) {

        try {
            NotificationTemplate notificationTemplate = Util.buildNotificationTemplateWithEmailTemplateWithID(
                    templateTypeId, emailTemplateWithID);
            TemplatesServiceHolder.getNotificationTemplateManager().addNotificationTemplate(notificationTemplate,
                    getTenantDomainFromContext(), applicationUuid);

            // Create and send the location of the created object as the response.
            SimpleTemplate simpleEmailTemplate = new SimpleTemplate();
            simpleEmailTemplate.setSelf(getApplicationTemplateLocation(templateTypeId, applicationUuid,
                    notificationTemplate.getLocale(), Constants.NOTIFICATION_CHANNEL_EMAIL));
            simpleEmailTemplate.setLocale(notificationTemplate.getLocale());
            return simpleEmailTemplate;
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_ADDING_TEMPLATE);
        }
    }

    /**
     * Adds a new organization SMS template to the given template type. Template ID should not exist in the system.
     *
     * @param templateTypeId      Template type in which the template should be added.
     * @param smsTemplateWithID   New SMS template.
     * @return Location of the newly created template if successful, 409 if template already exists, 500 otherwise.
     */
    public SimpleTemplate addSMSTemplate(String templateTypeId, SMSTemplateWithID smsTemplateWithID) {

        SimpleTemplate simpleTemplate = addSMSTemplate(templateTypeId, smsTemplateWithID, null);
        simpleTemplate.setSelf(getOrgTemplateLocation(templateTypeId, smsTemplateWithID.getLocale(),
                Constants.NOTIFICATION_CHANNEL_SMS));
        return simpleTemplate;
    }

    /**
     * Adds a new application SMS template to the given template type. Template ID should not exist in the system.
     *
     * @param templateTypeId      Template type in which the template should be added.
     * @param smsTemplateWithID   New SMS template.
     * @param applicationUuid     Application UUID.
     * @return Location of the newly created template if successful, 409 if template already exists, 500 otherwise.
     */
    public SimpleTemplate addSMSTemplate(String templateTypeId, SMSTemplateWithID smsTemplateWithID,
            String applicationUuid) {

        try {
            NotificationTemplate notificationTemplate = Util.buildNotificationTemplateWithSMSTemplateWithID(
                    templateTypeId, smsTemplateWithID);
            TemplatesServiceHolder.getNotificationTemplateManager().addNotificationTemplate(notificationTemplate,
                    getTenantDomainFromContext(), applicationUuid);

            // Create and send the location of the created object as the response.
            SimpleTemplate simpleEmailTemplate = new SimpleTemplate();
            simpleEmailTemplate.setSelf(getApplicationTemplateLocation(templateTypeId, applicationUuid,
                    notificationTemplate.getLocale(), Constants.NOTIFICATION_CHANNEL_SMS));
            simpleEmailTemplate.setLocale(notificationTemplate.getLocale());
            return simpleEmailTemplate;
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_ADDING_TEMPLATE);
        }
    }

    /**
     * Retrieves the list of organization email templates of the given template type.
     *
     * @param templateTypeId Template type ID.
     * @return List of email templates.
     */
    public List<EmailTemplateWithID> getTemplatesListOfEmailTemplateType(String templateTypeId) {

        return getTemplatesListOfEmailTemplateType(templateTypeId, null);
    }

    /**
     * Retrieves the list of application email templates of the given template type.
     *
     * @param templateTypeId  Template type ID.
     * @param applicationUuid Application UUID.
     * @return List of email templates.
     */
    public List<EmailTemplateWithID> getTemplatesListOfEmailTemplateType(String templateTypeId,
                                                                         String applicationUuid) {

        String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
        try {
            List<NotificationTemplate> emailTemplates = TemplatesServiceHolder.getNotificationTemplateManager()
                    .getNotificationTemplatesOfType(Constants.NOTIFICATION_CHANNEL_EMAIL, templateTypeDisplayName,
                            getTenantDomainFromContext(), applicationUuid);
            return Util.buildEmailTemplateWithIDList(emailTemplates);
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_RETRIEVING_TEMPLATE_TYPE);
        }
    }

    /**
     * Retrieves the list of organization SMS templates of the given template type.
     *
     * @param templateTypeId  Template type ID.
     * @return List of SMS templates.
     */
    public List<SMSTemplateWithID> getTemplatesListOfSMSTemplateType(String templateTypeId) {

        return getTemplatesListOfSMSTemplateType(templateTypeId, null);
    }

    /**
     * Retrieves the list of application SMS templates of the given template type.
     *
     * @param templateTypeId  Template type ID.
     * @param applicationUuid Application UUID.
     * @return List of SMS templates.
     */
    public List<SMSTemplateWithID> getTemplatesListOfSMSTemplateType(String templateTypeId,
                                                                         String applicationUuid) {

        String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
        try {
            List<NotificationTemplate> smsTemplates = TemplatesServiceHolder.getNotificationTemplateManager()
                    .getNotificationTemplatesOfType(Constants.NOTIFICATION_CHANNEL_SMS, templateTypeDisplayName,
                            getTenantDomainFromContext(), applicationUuid);
            return Util.buildSMSTemplateWithIDList(smsTemplates);
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_RETRIEVING_TEMPLATE_TYPE);
        }
    }

    /**
     * Retrieves the organization email template of the given template type and locale.
     *
     * @param templateTypeId Template type ID.
     * @param templateId     Template ID.
     * @return Email template.
     */
    public EmailTemplateWithID getEmailTemplate(String templateTypeId, String templateId) {

        return getEmailTemplate(templateTypeId, templateId, null);
    }

    /**
     * Retrieves the application email template of the given template type and locale.
     *
     * @param templateTypeId  Template type ID.
     * @param templateId      Template ID.
     * @param applicationUuid Application UUID.
     * @return Email template.
     */
    public EmailTemplateWithID getEmailTemplate(String templateTypeId, String templateId, String applicationUuid) {

        try {
            String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
            NotificationTemplate internalEmailTemplate = TemplatesServiceHolder.getNotificationTemplateManager().
                    getNotificationTemplate(Constants.NOTIFICATION_CHANNEL_EMAIL, templateTypeDisplayName, templateId,
                            getTenantDomainFromContext(), applicationUuid);
            // NotificationTemplateManager sends the default template if no matching template found.
            // We need to check for the locale specifically.
            if (!internalEmailTemplate.getLocale().equals(templateId)) {
                throw Util.handleError(Constants.ErrorMessage.ERROR_TEMPLATE_NOT_FOUND);
            } else {
                return Util.buildEmailTemplateWithID(internalEmailTemplate);
            }
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_RETRIEVING_TEMPLATE);
        }
    }

    /**
     * Retrieves the organization SMS template of the given template type and locale.
     *
     * @param templateTypeId  Template type ID.
     * @param templateId      Template ID.
     * @return SMS template.
     */
    public SMSTemplateWithID getSMSTemplate(String templateTypeId, String templateId) {

        return getSMSTemplate(templateTypeId, templateId, null);
    }

    /**
     * Retrieves the application SMS template of the given template type and locale.
     *
     * @param templateTypeId  Template type ID.
     * @param templateId      Template ID.
     * @param applicationUuid Application UUID.
     * @return SMS template.
     */
    public SMSTemplateWithID getSMSTemplate(String templateTypeId, String templateId, String applicationUuid) {

        try {
            String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
            NotificationTemplate internalEmailTemplate = TemplatesServiceHolder.getNotificationTemplateManager().
                    getNotificationTemplate(Constants.NOTIFICATION_CHANNEL_SMS, templateTypeDisplayName, templateId,
                            getTenantDomainFromContext(), applicationUuid);
            // NotificationTemplateManager sends the default template if no matching template found.
            // We need to check for the locale specifically.
            if (!internalEmailTemplate.getLocale().equals(templateId)) {
                throw Util.handleError(Constants.ErrorMessage.ERROR_TEMPLATE_NOT_FOUND);
            } else {
                return Util.buildSMSTemplateWithID(internalEmailTemplate);
            }
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_RETRIEVING_TEMPLATE);
        }
    }

    /**
     * Retrieves the default email template of the given template type and locale.
     *
     * @param templateTypeId  Template type ID.
     * @param templateId      Template ID.
     * @return Default email template.
     */
    public EmailTemplateWithID getSystemEmailTemplate(String templateTypeId, String templateId) {

        try {
            String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
            NotificationTemplate internalTemplate = TemplatesServiceHolder.getNotificationTemplateManager().
                    getSystemNotificationTemplate(Constants.NOTIFICATION_CHANNEL_EMAIL,
                            templateTypeDisplayName, templateId);
            // NotificationTemplateManager sends the default template if no matching template found.
            // We need to check for the locale specifically.
            if (!internalTemplate.getLocale().equals(templateId)) {
                throw Util.handleError(Constants.ErrorMessage.ERROR_TEMPLATE_NOT_FOUND);
            } else {
                return Util.buildEmailTemplateWithID(internalTemplate);
            }
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_RETRIEVING_TEMPLATE);
        }
    }

    /**
     * Retrieves the default SMS template of the given template type and locale.
     *
     * @param templateTypeId  Template type ID.
     * @param templateId      Template ID.
     * @return Default SMS template.
     */
    public SMSTemplateWithID getSystemSmsTemplate(String templateTypeId, String templateId) {

        try {
            String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
            NotificationTemplate internalTemplate = TemplatesServiceHolder.getNotificationTemplateManager().
                    getSystemNotificationTemplate(Constants.NOTIFICATION_CHANNEL_SMS,
                            templateTypeDisplayName, templateId);
            // NotificationTemplateManager sends the default template if no matching template found.
            // We need to check for the locale specifically.
            if (!internalTemplate.getLocale().equals(templateId)) {
                throw Util.handleError(Constants.ErrorMessage.ERROR_TEMPLATE_NOT_FOUND);
            } else {
                return Util.buildSMSTemplateWithID(internalTemplate);
            }
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_RETRIEVING_TEMPLATE);
        }
    }

    /**
     * Updates the organization email template of the given template type and locale.
     *
     * @param templateTypeId Template type ID.
     * @param templateId     Template ID.
     * @param emailTemplate  Updated email template.
     */
    public void updateEmailTemplate(String templateTypeId, String templateId, EmailTemplate emailTemplate) {

        updateEmailTemplate(templateTypeId, templateId, emailTemplate, null);
    }

    /**
     * Updates the application email template of the given template type and locale.
     *
     * @param templateTypeId  Template type ID.
     * @param templateId      Template ID.
     * @param emailTemplate   Updated email template.
     * @param applicationUuid Application UUID.
     */
    public void updateEmailTemplate(String templateTypeId, String templateId, EmailTemplate emailTemplate,
            String applicationUuid) {

        EmailTemplateWithID emailTemplateWithID =
                Util.buildEmailTemplateWithIdUsingEmailTemplate(emailTemplate, templateId);
        try {
            NotificationTemplate notificationTemplate = Util.buildNotificationTemplateWithEmailTemplateWithID(
                    templateTypeId, emailTemplateWithID);
            TemplatesServiceHolder.getNotificationTemplateManager().updateNotificationTemplate(notificationTemplate,
                    getTenantDomainFromContext(), applicationUuid);
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_UPDATING_TEMPLATE);
        }
    }

    /**
     * Updates the organization SMS template of the given template type and locale.
     *
     * @param templateTypeId Template type ID.
     * @param templateId     Template ID.
     * @param smsTemplate    Updated SMS template.
     */
    public void updateSMSTemplate(String templateTypeId, String templateId, SMSTemplate smsTemplate) {

        updateSMSTemplate(templateTypeId, templateId, smsTemplate, null);
    }

    /**
     * Updates the application SMS template of the given template type and locale.
     *
     * @param templateTypeId      Template type ID.
     * @param templateId          Template ID.
     * @param smsTemplate         Updated SMS template.
     * @param applicationUuid Application UUID.
     */
    public void updateSMSTemplate(String templateTypeId, String templateId, SMSTemplate smsTemplate,
          String applicationUuid) {

        SMSTemplateWithID smsTemplateWithID =
                Util.buildSMSTemplateWithIdUsingSMSTemplate(smsTemplate, templateId);
        try {
            NotificationTemplate notificationTemplate = Util.buildNotificationTemplateWithSMSTemplateWithID(
                    templateTypeId, smsTemplateWithID);
            TemplatesServiceHolder.getNotificationTemplateManager().updateNotificationTemplate(notificationTemplate,
                    getTenantDomainFromContext(), applicationUuid);
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_UPDATING_TEMPLATE);
        }
    }

    /**
     * Deletes the organization email template of the given template type and locale.
     *
     * @param templateTypeId Template type ID.
     * @param locale         Template locale.
     */
    public void deleteEmailTemplate(String templateTypeId, String locale) {

        deleteEmailTemplate(templateTypeId, locale, null);
    }

    /**
     * Deletes the application email template of the given template type and locale.
     *
     * @param templateTypeId  Template type ID.
     * @param locale          Template locale.
     * @param applicationUuid Application UUID.
     */
    public void deleteEmailTemplate(String templateTypeId, String locale, String applicationUuid) {

        String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
        try {
            Util.assertTemplateTypeExistence(Constants.NOTIFICATION_CHANNEL_EMAIL, templateTypeDisplayName);
            boolean notificationTemplateExists = TemplatesServiceHolder.getNotificationTemplateManager()
                    .isNotificationTemplateExists(Constants.NOTIFICATION_CHANNEL_EMAIL, templateTypeDisplayName,
                            locale, getTenantDomainFromContext(), applicationUuid);
            if (notificationTemplateExists) {
                TemplatesServiceHolder.getNotificationTemplateManager().deleteNotificationTemplate(
                        Constants.NOTIFICATION_CHANNEL_EMAIL, templateTypeDisplayName, locale,
                        getTenantDomainFromContext(), applicationUuid);
            } else {
                throw Util.handleError(Constants.ErrorMessage.ERROR_TEMPLATE_NOT_FOUND);
            }
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_DELETING_EMAIL_TEMPLATE);
        }
    }

    /**
     * Deletes the organization SMS template of the given template type and locale.
     *
     * @param templateTypeId Template type ID.
     * @param locale         Template ID.
     */
    public void deleteSMSTemplate(String templateTypeId, String locale) {

        deleteSMSTemplate(templateTypeId, locale, null);
    }

    /**
     * Deletes the application SMS template of the given template type and locale.
     *
     * @param templateTypeId  Template type ID.
     * @param locale          Template ID.
     * @param applicationUuid Application UUID.
     */
    public void deleteSMSTemplate(String templateTypeId, String locale, String applicationUuid) {

        String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
        try {
            Util.assertTemplateTypeExistence(Constants.NOTIFICATION_CHANNEL_SMS, templateTypeDisplayName);
            boolean notificationTemplateExists = TemplatesServiceHolder.getNotificationTemplateManager()
                    .isNotificationTemplateExists(Constants.NOTIFICATION_CHANNEL_SMS, templateTypeDisplayName,
                            locale, getTenantDomainFromContext(), applicationUuid);
            if (notificationTemplateExists) {
                TemplatesServiceHolder.getNotificationTemplateManager().deleteNotificationTemplate(
                        Constants.NOTIFICATION_CHANNEL_SMS, templateTypeDisplayName, locale,
                        getTenantDomainFromContext(), applicationUuid);
            } else {
                throw Util.handleError(Constants.ErrorMessage.ERROR_TEMPLATE_NOT_FOUND);
            }
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_DELETING_SMS_TEMPLATE);
        }
    }

    private String getApplicationTemplateLocation(String templateTypeId, String applicationUuid, String locale,
                                                  String notificationType) {

        String templateLocation = Util.getTemplateTypeLocation(templateTypeId, notificationType);
        return templateLocation + APP_TEMPLATES_PATH + PATH_SEPARATOR + applicationUuid
                + PATH_SEPARATOR + locale;
    }

    private String getOrgTemplateLocation(String templateTypeId, String templateId, String type) {

        String templateLocation = Util.getTemplateTypeLocation(templateTypeId, type);
        return templateLocation + Constants.ORG_TEMPLATES_PATH + Constants.PATH_SEPARATOR + templateId;
    }
}
