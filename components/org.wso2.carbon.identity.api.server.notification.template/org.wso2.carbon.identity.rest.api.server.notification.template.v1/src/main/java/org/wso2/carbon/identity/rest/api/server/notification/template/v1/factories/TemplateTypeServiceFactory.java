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

package org.wso2.carbon.identity.rest.api.server.notification.template.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.notification.template.common.TemplatesServiceHolder;
import org.wso2.carbon.identity.governance.service.notification.NotificationTemplateManager;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.core.TemplateTypeService;

/**
 * This class is the factory class for TemplateTypeService.
 */
public class TemplateTypeServiceFactory {

    private static final Log log = LogFactory.getLog(TemplateTypeServiceFactory.class);
    private static final TemplateTypeService SERVICE;

    static {
        NotificationTemplateManager notificationTemplateManager = TemplatesServiceHolder
                .getNotificationTemplateManager();
        if (notificationTemplateManager == null) {
            log.error("NotificationTemplateManager is not available from OSGi context");
            throw new IllegalStateException("NotificationTemplateManager is not available from OSGi context.");
        }
        SERVICE = new TemplateTypeService(notificationTemplateManager);
    }

    /**
     * Get TemplateTypeService service.
     *
     * @return TemplateTypeService service.
     */
    public static TemplateTypeService getTemplateTypeService() {

        return SERVICE;
    }
}
