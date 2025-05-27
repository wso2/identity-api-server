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

package org.wso2.carbon.identity.api.server.webhook.metadata.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.webhook.metadata.api.service.WebhookMetadataService;

/**
 * Service holder class for Webhook Metadata Service.
 */
public class WebhookMetadataServiceHolder {

    private WebhookMetadataServiceHolder() {

    }

    private static class WebhookMetadataServiceHolderInstance {

        static final WebhookMetadataService SERVICE = (WebhookMetadataService) PrivilegedCarbonContext.
                getThreadLocalCarbonContext().getOSGiService(WebhookMetadataService.class, null);
    }

    /**
     * Get Webhook Metadata Service osgi service.
     *
     * @return WebhookMetadataService.
     */
    public static WebhookMetadataService getWebhookMetadataService() {

        return WebhookMetadataServiceHolderInstance.SERVICE;
    }
}
