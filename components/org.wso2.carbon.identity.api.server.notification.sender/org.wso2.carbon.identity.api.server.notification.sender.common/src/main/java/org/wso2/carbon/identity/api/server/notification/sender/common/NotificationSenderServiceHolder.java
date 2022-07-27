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

import org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementService;

/**
 * Service holder class for notification senders configurations.
 */
public class NotificationSenderServiceHolder {

    private static NotificationSenderManagementService notificationSenderManagementService;

    /**
     * Get Notification Sender Manager OSGi service.
     *
     * @return NotificationSenderConfig Manager.
     */
    public static NotificationSenderManagementService getNotificationSenderManagementService() {

        return notificationSenderManagementService;
    }

    /**
     * Set Notification Sender Manager OSGi service.
     *
     * @param notificationSenderConfigManager Configuration Manager.
     */
    public static void setNotificationSenderManagementService(NotificationSenderManagementService
                                                                      notificationSenderConfigManager) {

        NotificationSenderServiceHolder.notificationSenderManagementService = notificationSenderConfigManager;
    }
}
