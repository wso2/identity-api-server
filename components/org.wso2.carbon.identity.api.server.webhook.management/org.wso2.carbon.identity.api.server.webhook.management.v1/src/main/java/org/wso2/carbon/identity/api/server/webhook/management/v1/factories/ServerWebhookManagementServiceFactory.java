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

package org.wso2.carbon.identity.api.server.webhook.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.webhook.management.common.WebhookManagementServiceHolder;
import org.wso2.carbon.identity.api.server.webhook.management.v1.core.ServerWebhookManagementService;
import org.wso2.carbon.identity.webhook.management.api.service.WebhookManagementService;

/**
 * Factory class for ServerWebhookManagementService.
 */
public class ServerWebhookManagementServiceFactory {

    private static final Log LOG = LogFactory.getLog(ServerWebhookManagementServiceFactory.class);
    private static final ServerWebhookManagementService SERVICE;

    static {
        WebhookManagementService webhookManagementService =
                WebhookManagementServiceHolder.getWebhookManagementService();
        if (webhookManagementService == null) {
            LOG.error("WebhookManagementService is not available from OSGi context.");
            throw new IllegalStateException("WebhookManagementService is not available from OSGi context.");
        }
        SERVICE = new ServerWebhookManagementService(webhookManagementService);
        LOG.info("ServerWebhookManagementService initialized successfully.");
    }

    /**
     * Get ServerWebhookManagementService service.
     *
     * @return ServerWebhookManagementService service.
     */
    public static ServerWebhookManagementService getServerWebhookManagementService() {

        return SERVICE;
    }
}
