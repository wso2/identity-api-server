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

package org.wso2.carbon.identity.api.server.notification.template.common;


import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.governance.service.notification.NotificationTemplateManager;

/**
 * This class is used to hold the TemplateManager service.
 */
public class TemplatesServiceHolder {

    private TemplatesServiceHolder() {}

    private static class NotificationTemplateMangerServiceHolder {

        static final NotificationTemplateManager SERVICE = (NotificationTemplateManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(NotificationTemplateManager.class, null);
    }

    /**
     * Get TemplateManager osgi service.
     * @return TemplateManager
     */
    public static NotificationTemplateManager getNotificationTemplateManager() {

        return NotificationTemplateMangerServiceHolder.SERVICE;
    }
}
