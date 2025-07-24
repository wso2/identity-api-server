/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.notification.sender.v2.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderServiceHolder;
import org.wso2.carbon.identity.api.server.notification.sender.v2.core.NotificationSenderManagementService;

/**
 * Factory class for NotificationSenderManagementService.
 */
public class NotificationSenderManagementServiceFactory {

    private static final Log log = LogFactory.getLog(NotificationSenderManagementServiceFactory.class);
    private static final NotificationSenderManagementService SERVICE;

    static {
        log.info("Initializing NotificationSenderManagementService");
        org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementService
                notificationSenderManagementService = NotificationSenderServiceHolder
                        .getNotificationSenderManagementService();

        if (notificationSenderManagementService == null) {
            String errorMsg = "NotificationSenderManagementService is not available from OSGi context.";
            log.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        SERVICE = new NotificationSenderManagementService(notificationSenderManagementService);
        log.info("NotificationSenderManagementService initialized successfully");
    }

    /**
     * Get NotificationSenderManagementService service.
     *
     * @return NotificationSenderManagementService service.
     */
    public static NotificationSenderManagementService getNotificationSenderManagementService() {

        return SERVICE;
    }
}
