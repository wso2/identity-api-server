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


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.email.mgt.constants.I18nMgtConstants;
import org.wso2.carbon.identity.governance.service.notification.NotificationTemplateManager;

import java.util.Hashtable;

/**
 * This class is used to hold the TemplateManager service.
 */
public class TemplatesServiceHolder {

    private static final Log LOG = LogFactory.getLog(TemplatesServiceHolder.class);

    private TemplatesServiceHolder () {

    }

    private static class NotificationTemplateManagerHolder {

        static final NotificationTemplateManager SERVICE = resolveNotificationTemplateManager();
    }

    private static NotificationTemplateManager resolveNotificationTemplateManager() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Resolving NotificationTemplateManager service.");
        }
        Hashtable<String, String> serviceProperties = new Hashtable<>();
        serviceProperties.put(I18nMgtConstants.SERVICE_PROPERTY_KEY_SERVICE_NAME,
                I18nMgtConstants.SERVICE_PROPERTY_VAL_NOTIFICATION_TEMPLATE_MANAGER);
        NotificationTemplateManager taskOperationService = (NotificationTemplateManager) PrivilegedCarbonContext.
                getThreadLocalCarbonContext().getOSGiService(NotificationTemplateManager.class, serviceProperties);
        if (taskOperationService == null) {
            LOG.warn("Unable to retrieve NotificationTemplateManager service. Service may not be available.");
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully resolved NotificationTemplateManager service.");
            }
        }
        return taskOperationService;
    }

    /**
     * Get NotificationTemplateManager osgi service.
     *
     * @return NotificationTemplateManager.
     */
    public static NotificationTemplateManager getNotificationTemplateManager() {

        NotificationTemplateManager service = NotificationTemplateManagerHolder.SERVICE;
        if (service == null && LOG.isDebugEnabled()) {
            LOG.debug("NotificationTemplateManager service is not available.");
        }
        return service;
    }
}
