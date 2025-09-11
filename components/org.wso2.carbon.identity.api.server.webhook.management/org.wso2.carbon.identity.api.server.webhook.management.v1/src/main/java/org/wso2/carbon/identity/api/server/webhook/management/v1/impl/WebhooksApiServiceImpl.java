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

package org.wso2.carbon.identity.api.server.webhook.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.webhook.management.v1.WebhooksApiService;
import org.wso2.carbon.identity.api.server.webhook.management.v1.core.ServerWebhookManagementService;
import org.wso2.carbon.identity.api.server.webhook.management.v1.factories.ServerWebhookManagementServiceFactory;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookList;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookRequest;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookResponse;

import java.net.URI;

import javax.ws.rs.core.Response;


/**
 * Implementation of the WebhooksApiService.
 */
public class WebhooksApiServiceImpl implements WebhooksApiService {

    private static final Log LOG = LogFactory.getLog(WebhooksApiServiceImpl.class);
    private final ServerWebhookManagementService serverWebhookManagementService;

    public WebhooksApiServiceImpl() {

        try {
            serverWebhookManagementService = ServerWebhookManagementServiceFactory.getServerWebhookManagementService();
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while retrieving WebhookManagementService.", e);
            throw new RuntimeException("Error occurred while retrieving WebhookManagementService.", e);
        }
    }

    @Override
    public Response getWebhooks() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting all webhooks.");
        }
        WebhookList webhooks = serverWebhookManagementService.getWebhooks();
        return Response.ok().entity(webhooks).build();
    }

    @Override
    public Response getWebhookByWebhookId(String webhookId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting webhook by ID: " + webhookId);
        }
        WebhookResponse webhook = serverWebhookManagementService.getWebhook(webhookId);
        return Response.ok().entity(webhook).build();
    }

    @Override
    public Response createWebhook(WebhookRequest webhookRequest) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating webhook with name: " + (webhookRequest != null ? webhookRequest.getName() : "null"));
        }
        WebhookResponse createdWebhook = serverWebhookManagementService.createWebhook(webhookRequest);
        URI location = ContextLoader.buildURIForBody(String.format(Constants.V1_API_PATH_COMPONENT +
                "/webhooks/%s", createdWebhook.getId()));
        return Response.created(location).entity(createdWebhook).build();
    }

    @Override
    public Response updateWebhook(String webhookId, WebhookRequest webhookRequest) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating webhook with ID: " + webhookId);
        }
        WebhookResponse updatedWebhook = serverWebhookManagementService.updateWebhook(webhookId, webhookRequest);
        return Response.ok().entity(updatedWebhook).build();
    }

    @Override
    public Response deleteWebhook(String webhookId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting webhook with ID: " + webhookId);
        }
        serverWebhookManagementService.deleteWebhook(webhookId);
        return Response.noContent().build();
    }

    @Override
    public Response activateWebhook(String webhookId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Activating webhook with ID: " + webhookId);
        }
        return Response.ok().entity(serverWebhookManagementService.activateWebhook(webhookId)).build();
    }

    @Override
    public Response deactivateWebhook(String webhookId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deactivating webhook with ID: " + webhookId);
        }
        return Response.ok().entity(serverWebhookManagementService.deactivateWebhook(webhookId)).build();
    }

    @Override
    public Response retryWebhook(String webhookId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrying webhook with ID: " + webhookId);
        }
        return Response.ok().entity(serverWebhookManagementService.retryWebhook(webhookId)).build();
    }
}
