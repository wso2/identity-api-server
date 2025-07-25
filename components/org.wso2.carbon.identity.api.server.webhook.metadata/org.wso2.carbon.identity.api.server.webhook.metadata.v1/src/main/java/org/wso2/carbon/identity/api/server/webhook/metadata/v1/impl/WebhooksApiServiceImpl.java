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

package org.wso2.carbon.identity.api.server.webhook.metadata.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.WebhooksApiService;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.core.ServerWebhookMetadataService;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.factories.ServerWebhookMetadataServiceFactory;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.EventProfile;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.WebhookMetadata;

import javax.ws.rs.core.Response;

/**
 * Implementation of the WebhooksApi Service.
 */
public class WebhooksApiServiceImpl implements WebhooksApiService {

    private static final Log LOG = LogFactory.getLog(WebhooksApiServiceImpl.class);

    @Override
    public Response getEventProfile(String profileName) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("API request to get event profile: " + profileName);
        }
        ServerWebhookMetadataService webhookMetadataService =
                ServerWebhookMetadataServiceFactory.getServerWebhookMetadataService();
        EventProfile eventProfile = webhookMetadataService.getEventProfile(profileName);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully processed API request for event profile: " + profileName);
        }
        return Response.ok().entity(eventProfile).build();
    }

    @Override
    public Response getEventProfiles() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("API request to get webhook metadata with event profiles");
        }
        ServerWebhookMetadataService webhookMetadataService =
                ServerWebhookMetadataServiceFactory.getServerWebhookMetadataService();
        WebhookMetadata webhookMetadata = webhookMetadataService.getWebhookMetadata();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully processed API request for webhook metadata");
        }
        return Response.ok().entity(webhookMetadata).build();
    }
}
