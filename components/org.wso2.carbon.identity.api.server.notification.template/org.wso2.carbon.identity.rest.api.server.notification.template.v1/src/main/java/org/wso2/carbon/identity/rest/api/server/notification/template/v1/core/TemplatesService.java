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

package org.wso2.carbon.identity.rest.api.server.notification.template.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.notification.template.common.Constants;
import org.wso2.carbon.identity.governance.exceptions.notiification.NotificationTemplateManagerException;
import org.wso2.carbon.identity.governance.model.NotificationTemplate;
import org.wso2.carbon.identity.governance.service.notification.NotificationTemplateManager;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.EmailTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.EmailTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SMSTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SMSTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SimpleTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.util.Util;

import java.util.List;

import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;

/**
 * Service class for application email templates.
 */
public class TemplatesService {

    private static final Log log = LogFactory.getLog(TemplatesService.class);
    private final NotificationTemplateManager notificationTemplateManager;

    public TemplatesService(NotificationTemplateManager notificationTemplateManager) {

        this.notificationTemplateManager = notificationTemplateManager;
        if (log.isDebugEnabled()) {
            log.debug("TemplatesService initialized successfully.");
        }
    }
    /**
     * Adds a new organization email template to the given template type. Template ID should not exist in the system.
     *
     * @param templateTypeId      Template type in which the template should be added.
     * @param emailTemplateWithID New email template.
     * @return Location of the newly created template if successful, 409 if template already exists, 500 otherwise.
     */
    public SimpleTemplate addEmailTemplate(String templateTypeId, EmailTemplateWithID emailTemplateWithID) {

        return addEmailTemplate(templateTypeId, emailTemplateWithID, null);
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

        if (log.isDebugEnabled()) {
            log.debug("Adding email template. TemplateTypeId: " + templateTypeId + ", Locale: " 
                    + emailTemplateWithID.getLocale());
        }
        try {
            NotificationTemplate notificationTemplate = Util.buildNotificationTemplateWithEmailTemplateWithID(
                    templateTypeId, emailTemplateWithID);
            notificationTemplateManager.addNotificationTemplate(notificationTemplate,
                    getTenantDomainFromContext(), applicationUuid);

            String templateOwner = StringUtils.isNotBlank(applicationUuid) ? Constants.NOTIFICATION_TEMPLATE_OWNER_APP :
                    Constants.NOTIFICATION_TEMPLATE_OWNER_ORG;
            String templateTypeLocation = Util.getTemplateTypeLocation(templateTypeId,
                    Constants.NOTIFICATION_CHANNEL_EMAIL);
            String templateLocation = Util.getTemplateLocation(templateTypeLocation, applicationUuid,
                    emailTemplateWithID.getLocale(), templateOwner);

            SimpleTemplate simpleEmailTemplate = new SimpleTemplate();
            simpleEmailTemplate.setSelf(templateLocation);
            simpleEmailTemplate.setLocale(notificationTemplate.getLocale());
            log.info("Successfully added email template. TemplateTypeId: " + templateTypeId + ", Locale: " 
                    + notificationTemplate.getLocale());
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

        return addSMSTemplate(templateTypeId, smsTemplateWithID, null);
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

        if (log.isDebugEnabled()) {
            log.debug("Adding SMS template. TemplateTypeId: " + templateTypeId + ", Locale: " 
                    + smsTemplateWithID.getLocale());
        }
        try {
            NotificationTemplate notificationTemplate = Util.buildNotificationTemplateWithSMSTemplateWithID(
                    templateTypeId, smsTemplateWithID);
            notificationTemplateManager.addNotificationTemplate(notificationTemplate, getTenantDomainFromContext(),
                    applicationUuid);

            String templateOwner = StringUtils.isNotBlank(applicationUuid) ? Constants.NOTIFICATION_TEMPLATE_OWNER_APP :
                    Constants.NOTIFICATION_TEMPLATE_OWNER_ORG;
            String templateTypeLocation = Util.getTemplateTypeLocation(templateTypeId,
                    Constants.NOTIFICATION_CHANNEL_SMS);
            String templateLocation = Util.getTemplateLocation(templateTypeLocation, applicationUuid,
                    smsTemplateWithID.getLocale(), templateOwner);

            SimpleTemplate simpleSMSTemplate = new SimpleTemplate();
            simpleSMSTemplate.setSelf(templateLocation);
            simpleSMSTemplate.setLocale(notificationTemplate.getLocale());
            log.info("Successfully added SMS template. TemplateTypeId: " + templateTypeId + ", Locale: " 
                    + notificationTemplate.getLocale());
            return simpleSMSTemplate;
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_ADDING_TEMPLATE);
        }
    }

    /**
     * Retrieves the list of organization email templates of the given template type.
     *
     * @param templateTypeId      Template type ID.
     * @param notificationChannel Notification channel.
     * @param resolve             Whether to retrieve templates resolved through the ancestor organization hierarchy,
     *                            returning templates that are applicable across the tenant's organizational structure.
     * @return List of email templates.
     */
    public List<SimpleTemplate> getAllTemplatesOfTemplateType(String templateTypeId, String notificationChannel,
                                                              boolean resolve) {

        return getAllTemplatesOfTemplateType(templateTypeId, null, notificationChannel, resolve);
    }

    /**
     * Retrieves the list of application email templates of the given template type.
     *
     * @param templateTypeId      Template type ID.
     * @param applicationUuid     Application UUID.
     * @param notificationChannel Notification channel.
     * @param resolve             Whether to retrieve templates resolved through the ancestor organization hierarchy,
     *                            returning templates that are applicable across the tenant's organizational structure.
     * @return List of email templates.
     */
    public List<SimpleTemplate> getAllTemplatesOfTemplateType(String templateTypeId, String applicationUuid,
                                                              String notificationChannel, boolean resolve) {

        String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
        if (log.isDebugEnabled()) {
            log.debug("Retrieving all templates of type: " + templateTypeDisplayName + ", Channel: " 
                    + notificationChannel);
        }
        try {
            List<NotificationTemplate> templates = notificationTemplateManager.getNotificationTemplatesOfType(
                    notificationChannel, templateTypeDisplayName, getTenantDomainFromContext(), applicationUuid,
                    resolve);
            String templateOwner = StringUtils.isNotBlank(applicationUuid) ? Constants.NOTIFICATION_TEMPLATE_OWNER_APP :
                    Constants.NOTIFICATION_TEMPLATE_OWNER_ORG;
            return Util.buildSimpleTemplateList(templates, applicationUuid, templateOwner, notificationChannel);
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_RETRIEVING_TEMPLATES);
        }
    }

    /**
     * Retrieves the list of application email templates of the given template type.
     *
     * @param templateTypeId  Template type ID.
     * @param notificationChannel Notification channel.
     * @return List of email templates.
     */
    public List<SimpleTemplate> getAllSystemTemplatesOfTemplateType(String templateTypeId, String notificationChannel) {

        String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
        try {
            List<NotificationTemplate> templates = notificationTemplateManager.getAllSystemNotificationTemplatesOfType(
                    notificationChannel, templateTypeDisplayName);
            return Util.buildSimpleTemplateList(templates, null,
                    Constants.NOTIFICATION_TEMPLATE_OWNER_SYSTEM, notificationChannel);
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_RETRIEVING_TEMPLATES);
        }
    }

    /**
     * Retrieves the organization email template of the given template type and locale.
     *
     * @param templateTypeId Template type ID.
     * @param templateId     Template ID.
     * @param resolve         Whether to retrieve the template resolved through the ancestor organization hierarchy.
     * @return Email template.
     */
    public EmailTemplateWithID getEmailTemplate(String templateTypeId, String templateId, boolean resolve) {

        return getEmailTemplate(templateTypeId, templateId, null, resolve);
    }

    /**
     * Retrieves the application email template of the given template type and locale.
     *
     * @param templateTypeId  Template type ID.
     * @param templateId      Template ID.
     * @param applicationUuid Application UUID.
     * @param resolve         Whether to retrieve the template resolved through the ancestor organization hierarchy.
     * @return Email template.
     */
    public EmailTemplateWithID getEmailTemplate(String templateTypeId, String templateId, String applicationUuid,
                                                boolean resolve) {

        try {
            String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
            NotificationTemplate internalEmailTemplate = notificationTemplateManager.getNotificationTemplate(
                    Constants.NOTIFICATION_CHANNEL_EMAIL, templateTypeDisplayName, templateId,
                            getTenantDomainFromContext(), applicationUuid, resolve);
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
     * @param templateTypeId Template type ID.
     * @param templateId     Template ID.
     * @param resolve         Whether to retrieve the template resolved through the ancestor organization hierarchy.
     * @return SMS template.
     */
    public SMSTemplateWithID getSMSTemplate(String templateTypeId, String templateId, boolean resolve) {

        return getSMSTemplate(templateTypeId, templateId, null, resolve);
    }

    /**
     * Retrieves the application SMS template of the given template type and locale.
     *
     * @param templateTypeId  Template type ID.
     * @param templateId      Template ID.
     * @param applicationUuid Application UUID.
     * @param resolve         Whether to retrieve the template resolved through the ancestor organization hierarchy.
     * @return SMS template.
     */
    public SMSTemplateWithID getSMSTemplate(String templateTypeId, String templateId, String applicationUuid,
                                            boolean resolve) {

        try {
            String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
            NotificationTemplate internalEmailTemplate = notificationTemplateManager.getNotificationTemplate(
                    Constants.NOTIFICATION_CHANNEL_SMS, templateTypeDisplayName, templateId,
                    getTenantDomainFromContext(), applicationUuid, resolve);
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
            NotificationTemplate internalTemplate = notificationTemplateManager.getSystemNotificationTemplate(
                    Constants.NOTIFICATION_CHANNEL_EMAIL, templateTypeDisplayName, templateId);
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
            NotificationTemplate internalTemplate = notificationTemplateManager.getSystemNotificationTemplate(
                    Constants.NOTIFICATION_CHANNEL_SMS, templateTypeDisplayName, templateId);
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
        if (log.isDebugEnabled()) {
            log.debug("Updating email template. TemplateTypeId: " + templateTypeId + ", Locale: " + templateId);
        }
        try {
            NotificationTemplate notificationTemplate = Util.buildNotificationTemplateWithEmailTemplateWithID(
                    templateTypeId, emailTemplateWithID);
            notificationTemplateManager.updateNotificationTemplate(notificationTemplate, getTenantDomainFromContext(),
                    applicationUuid);
            log.info("Successfully updated email template. TemplateTypeId: " + templateTypeId + ", Locale: " 
                    + templateId);
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
        if (log.isDebugEnabled()) {
            log.debug("Updating SMS template. TemplateTypeId: " + templateTypeId + ", Locale: " + templateId);
        }
        try {
            NotificationTemplate notificationTemplate = Util.buildNotificationTemplateWithSMSTemplateWithID(
                    templateTypeId, smsTemplateWithID);
            notificationTemplateManager.updateNotificationTemplate(notificationTemplate, getTenantDomainFromContext(),
                    applicationUuid);
            log.info("Successfully updated SMS template. TemplateTypeId: " + templateTypeId + ", Locale: " 
                    + templateId);
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
        if (log.isDebugEnabled()) {
            log.debug("Deleting email template. TemplateTypeId: " + templateTypeId + ", Locale: " + locale);
        }
        try {
            Util.verifyTemplateTypeExists(Constants.NOTIFICATION_CHANNEL_EMAIL, templateTypeDisplayName);
            boolean notificationTemplateExists = notificationTemplateManager.isNotificationTemplateExists(
                    Constants.NOTIFICATION_CHANNEL_EMAIL, templateTypeDisplayName, locale,
                    getTenantDomainFromContext(), applicationUuid);
            if (notificationTemplateExists) {
                notificationTemplateManager.deleteNotificationTemplate(Constants.NOTIFICATION_CHANNEL_EMAIL,
                        templateTypeDisplayName, locale, getTenantDomainFromContext(), applicationUuid);
                log.info("Successfully deleted email template. TemplateTypeId: " + templateTypeId + ", Locale: " 
                        + locale);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Email template not found for deletion. TemplateTypeId: " + templateTypeId 
                            + ", Locale: " + locale);
                }
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
            Util.verifyTemplateTypeExists(Constants.NOTIFICATION_CHANNEL_SMS, templateTypeDisplayName);
            boolean notificationTemplateExists = notificationTemplateManager.isNotificationTemplateExists(
                    Constants.NOTIFICATION_CHANNEL_SMS, templateTypeDisplayName, locale, getTenantDomainFromContext(),
                    applicationUuid);
            if (notificationTemplateExists) {
                notificationTemplateManager.deleteNotificationTemplate(Constants.NOTIFICATION_CHANNEL_SMS,
                        templateTypeDisplayName, locale, getTenantDomainFromContext(), applicationUuid);
            } else {
                throw Util.handleError(Constants.ErrorMessage.ERROR_TEMPLATE_NOT_FOUND);
            }
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_DELETING_SMS_TEMPLATE);
        }
    }
}
