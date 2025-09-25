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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.notification.template.common.Constants;
import org.wso2.carbon.identity.governance.exceptions.notiification.NotificationTemplateManagerException;
import org.wso2.carbon.identity.governance.service.notification.NotificationTemplateManager;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.TemplateTypeOverview;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.TemplateTypeWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.util.Util;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;
import static org.wso2.carbon.identity.api.server.notification.template.common.Constants.NOTIFICATION_CHANNEL_EMAIL;

/**
 * Service class for the template types.
 */
public class TemplateTypeService {

    private static final Log log = LogFactory.getLog(TemplateTypeService.class);
    private final NotificationTemplateManager notificationTemplateManager;

    public TemplateTypeService(NotificationTemplateManager notificationTemplateManager) {

        this.notificationTemplateManager = notificationTemplateManager;
        if (log.isDebugEnabled()) {
            log.debug("TemplateTypeService initialized successfully.");
        }
    }
    /**
     * Add a new template type for a given channel.
     *
     * @param notificationChannel  Notification channel (Eg: sms, email).
     * @param templateTypeOverview Template type overview.
     * @return TemplateTypeWithID object.
     */
    public TemplateTypeWithID addNotificationTemplateType(String notificationChannel,
                                                          TemplateTypeOverview templateTypeOverview) {

        String templateTypeDisplayName = templateTypeOverview.getDisplayName();
        if (log.isDebugEnabled()) {
            log.debug("Adding template type: " + templateTypeDisplayName + ", Channel: " + notificationChannel);
        }
        try {
            notificationTemplateManager.addNotificationTemplateType(notificationChannel, templateTypeDisplayName,
                    getTenantDomainFromContext());
            // Build a response object and send if everything is successful.
            TemplateTypeWithID response = new TemplateTypeWithID();
            response.setDisplayName(templateTypeDisplayName);
            String templateTypeId = Util.resolveTemplateIdFromDisplayName(templateTypeDisplayName);
            response.setId(templateTypeId);
            response.setSelf(Util.getTemplateTypeLocation(templateTypeId, notificationChannel));
            log.info("Successfully added template type: " + templateTypeDisplayName + ", Channel: " 
                    + notificationChannel);
            return response;
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_ADDING_TEMPLATE_TYPE);
        }
    }

    /**
     * Get all available notification template types for a notification chanel in the tenant.
     *
     * @param notificationChannel Notification channel (Eg: sms, email).
     * @return List of TemplateTypeWithID objects.
     */
    public List<TemplateTypeWithID> getAllNotificationTemplateTypes(String notificationChannel) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving all template types for channel: " + notificationChannel);
        }
        try {
            List<String> templateTypes = notificationTemplateManager.getAllNotificationTemplateTypes(
                    notificationChannel, getTenantDomainFromContext());
            List<TemplateTypeWithID> templateTypeWithIDs = new ArrayList<>();
            if (templateTypes != null) {
                for (String emailTemplateType : templateTypes) {
                    TemplateTypeWithID templateTypeWithID = new TemplateTypeWithID();
                    templateTypeWithID.setDisplayName(emailTemplateType);
                    String templateTypeId = Util.resolveTemplateIdFromDisplayName(emailTemplateType);
                    templateTypeWithID.setId(templateTypeId);
                    templateTypeWithID.setSelf(
                            Util.getTemplateTypeLocation(templateTypeId, notificationChannel));
                    templateTypeWithIDs.add(templateTypeWithID);
                }
            }
            return templateTypeWithIDs;
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_RETRIEVING_TEMPLATE_TYPES);
        }
    }

    /**
     * Delete a notification template type from the tenant.
     *
     * @param notificationChannel Notification channel (Eg: sms, email).
     * @param templateTypeId      ID of the template type.
     */
    public void deleteNotificationTemplateType(String notificationChannel, String templateTypeId) {

        String templateTypeDisplayName;
        templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
        if (log.isDebugEnabled()) {
            log.debug("Deleting template type: " + templateTypeDisplayName + ", Channel: " + notificationChannel);
        }
        try {
            boolean isTemplateTypeExists = notificationTemplateManager.isNotificationTemplateTypeExists(
                            notificationChannel, templateTypeDisplayName, getTenantDomainFromContext());
            if (isTemplateTypeExists) {
                notificationTemplateManager.deleteNotificationTemplateType(notificationChannel, templateTypeDisplayName,
                        getTenantDomainFromContext());
                log.info("Successfully deleted template type: " + templateTypeDisplayName + ", Channel: " 
                        + notificationChannel);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Template type not found for deletion: " + templateTypeDisplayName);
                }
                throw Util.handleError(Constants.ErrorMessage.ERROR_TEMPLATE_TYPE_NOT_FOUND);
            }
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_DELETING_TEMPLATE_TYPE);
        }
    }

    /**
     * Reset a specific template type. Deletes all org and app templates under the type.
     *
     * @param notificationChannel Notification channel (Eg: sms, email).
     * @param templateTypeId      ID of the template type.
     */
    public void resetTemplateType(String notificationChannel, String templateTypeId) {

        if (NOTIFICATION_CHANNEL_EMAIL.equalsIgnoreCase(notificationChannel)) {
            notificationChannel = NOTIFICATION_CHANNEL_EMAIL;
        } else if (Constants.NOTIFICATION_CHANNEL_SMS.equalsIgnoreCase(notificationChannel)) {
            notificationChannel = Constants.NOTIFICATION_CHANNEL_SMS;
        } else {
            throw Util.handleError(Constants.ErrorMessage.ERROR_ERROR_INVALID_NOTIFICATION_CHANNEL);
        }
        String templateTypeDisplayName;
        templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
        try {
            boolean isTemplateTypeExists = notificationTemplateManager.isNotificationTemplateTypeExists(
                            notificationChannel, templateTypeDisplayName, getTenantDomainFromContext());
            if (isTemplateTypeExists) {
                notificationTemplateManager.resetNotificationTemplateType(notificationChannel, templateTypeDisplayName,
                        getTenantDomainFromContext()
                );
            } else {
                throw Util.handleError(Constants.ErrorMessage.ERROR_TEMPLATE_TYPE_NOT_FOUND);
            }
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_RESETTING_TEMPLATE_TYPE);
        }
    }

    /**
     * Get a specific notification template type.
     *
     * @param notificationChannel Notification channel (Eg: sms, email).
     * @param templateTypeId      ID of the template type.
     * @return TemplateTypeWithID object.
     */
    public TemplateTypeWithID getNotificationTemplateType(String notificationChannel,
                                                                  String templateTypeId) {

        String templateTypeDisplayName = Util.decodeTemplateTypeId(templateTypeId);
        try {
            boolean isTemplateTypeExists = notificationTemplateManager.isNotificationTemplateTypeExists(
                            notificationChannel, templateTypeDisplayName, getTenantDomainFromContext());
            if (isTemplateTypeExists) {
                TemplateTypeWithID templateTypeWithID = new TemplateTypeWithID();
                templateTypeWithID.setDisplayName(templateTypeDisplayName);
                templateTypeWithID.setId(templateTypeId);
                templateTypeWithID.setSelf(Util.getTemplateTypeLocation(templateTypeId, notificationChannel));
                return templateTypeWithID;
            } else {
                throw Util.handleError(Constants.ErrorMessage.ERROR_TEMPLATE_TYPE_NOT_FOUND);
            }
        } catch (NotificationTemplateManagerException e) {
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_RETRIEVING_TEMPLATE);
        }
    }

}
