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

import org.wso2.carbon.email.mgt.SMSProviderPayloadTemplateManager;
import org.wso2.carbon.event.publisher.core.EventPublisherService;
import org.wso2.carbon.identity.configuration.mgt.core.ConfigurationManager;

/**
 * Service holder class for notification senders configurations.
 */
public class NotificationSenderServiceHolder {

    private static ConfigurationManager notificationSenderConfigManager;
    private static EventPublisherService eventPublisherService;
    private static SMSProviderPayloadTemplateManager smsProviderPayloadTemplateManager;

    /**
     * Get ConfigurationManager OSGi service.
     *
     * @return NotificationSenderConfig Manager.
     */
    public static ConfigurationManager getNotificationSenderConfigManager() {

        return notificationSenderConfigManager;
    }

    /**
     * Set ConfigurationManager OSGi service.
     *
     * @param notificationSenderConfigManager Configuration Manager.
     */
    public static void setNotificationSenderConfigManager(ConfigurationManager notificationSenderConfigManager) {

        NotificationSenderServiceHolder.notificationSenderConfigManager = notificationSenderConfigManager;
    }

    /**
     * Get event publisher OSGi service.
     *
     * @return EventPublisher Service.
     */
    public static EventPublisherService getEventPublisherService() {

        return eventPublisherService;
    }

    /**
     * Set an event publisher OSGi service.
     *
     * @param eventPublisherService EventPublisher Service.
     */
    public static void setEventPublisherService(EventPublisherService eventPublisherService) {

        NotificationSenderServiceHolder.eventPublisherService = eventPublisherService;
    }

    /**
     * Get SMS providers' payload template manager OSGi service.
     *
     * @returns SMSProviderPayloadTemplateManager service.
     */
    public static SMSProviderPayloadTemplateManager getSmsProviderPayloadTemplateManager() {

        return smsProviderPayloadTemplateManager;
    }

    /**
     * Set SMS providers' payload template manager OSGi service.
     *
     * @param smsProviderPayloadTemplateManager SMSProviderPayloadTemplateManager service.
     */
    public static void setSmsProviderPayloadTemplateManager(
            SMSProviderPayloadTemplateManager smsProviderPayloadTemplateManager) {

        NotificationSenderServiceHolder.smsProviderPayloadTemplateManager = smsProviderPayloadTemplateManager;
    }
}
