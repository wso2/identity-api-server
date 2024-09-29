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

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.notification.template.common.Constants;
import org.wso2.carbon.identity.api.server.notification.template.common.TemplatesServiceHolder;
import org.wso2.carbon.identity.governance.exceptions.notiification.NotificationTemplateManagerException;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.TemplateTypeOverview;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.TemplateTypeWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.util.Util;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.email.mgt.constants.TemplateMgtConstants.ErrorCodes
        .ERROR_SYSTEM_RESOURCE_DELETION_NOT_ALLOWED;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;

/**
 * Service class for the template types.
 */
public class TemplateTypeService {

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
        try {
            TemplatesServiceHolder.getNotificationTemplateManager().addNotificationTemplateType(notificationChannel,
                    templateTypeDisplayName, getTenantDomainFromContext());
            // Build a response object and send if everything is successful.
            TemplateTypeWithID response = new TemplateTypeWithID();
            response.setDisplayName(templateTypeDisplayName);
            String templateTypeId = Util.resolveTemplateIdFromDisplayName(templateTypeDisplayName);
            response.setId(templateTypeId);
            response.setSelf(Util.getTemplateTypeLocation(templateTypeId, notificationChannel));
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

        try {
            List<String> templateTypes = TemplatesServiceHolder.getNotificationTemplateManager()
                        .getAllNotificationTemplateTypes(notificationChannel, getTenantDomainFromContext());
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
     * @param templateId ID of the template type.
     */
    public void deleteNotificationTemplateType(String notificationChannel, String templateId)
            throws NotificationTemplateManagerException {

        String templateTypeDisplayName;
        templateTypeDisplayName = Util.decodeTemplateTypeId(templateId);
        try {
            boolean isTemplateTypeExists =
                    TemplatesServiceHolder.getNotificationTemplateManager().isNotificationTemplateTypeExists(
                            notificationChannel, templateTypeDisplayName, getTenantDomainFromContext());
            if (isTemplateTypeExists) {
                TemplatesServiceHolder.getNotificationTemplateManager().deleteNotificationTemplateType(
                        notificationChannel, templateTypeDisplayName, getTenantDomainFromContext());
            } else {
                throw Util.handleError(Constants.ErrorMessage.ERROR_TEMPLATE_TYPE_NOT_FOUND);
            }
        } catch (NotificationTemplateManagerException e) {
            String errorCode = StringUtils.EMPTY;
            if (StringUtils.isNotBlank(e.getErrorCode()) && e.getErrorCode().split("-").length > 1) {
                errorCode = e.getErrorCode().split("-")[1];
            }
            if (ERROR_SYSTEM_RESOURCE_DELETION_NOT_ALLOWED.equals(errorCode)) {
                throw e;
            }
            throw Util.handleNotificationTemplateManagerException(e,
                    Constants.ErrorMessage.ERROR_ERROR_DELETING_TEMPLATE_TYPE);
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
            boolean isTemplateTypeExists =
                    TemplatesServiceHolder.getNotificationTemplateManager().isNotificationTemplateTypeExists(
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
