/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.notification.sender.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementService;

/**
 * Service holder class for notification senders configurations.
 */
public class NotificationSenderServiceHolder {

    private static final Log LOG = LogFactory.getLog(NotificationSenderServiceHolder.class);

    private NotificationSenderServiceHolder() {}

    private static class NotificationSenderManagementServiceHolder {

        static final NotificationSenderManagementService SERVICE =
                (NotificationSenderManagementService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(NotificationSenderManagementService.class, null);
    }

    /**
     * Get Notification Sender Manager OSGi service.
     *
     * @return NotificationSenderConfig Manager.
     */
    public static NotificationSenderManagementService getNotificationSenderManagementService() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving NotificationSenderManagementService from OSGi service registry.");
        }
        NotificationSenderManagementService service = NotificationSenderManagementServiceHolder.SERVICE;
        if (service == null) {
            LOG.warn("NotificationSenderManagementService is not available in the OSGi service registry.");
        } else if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully retrieved NotificationSenderManagementService from OSGi service registry.");
        }
        return service;
    }
}
