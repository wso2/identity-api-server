/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.webhook.management.v2.impl;

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.webhook.management.v2.WebhooksApiService;
import org.wso2.carbon.identity.api.server.webhook.management.v2.core.ServerWebhookManagementService;
import org.wso2.carbon.identity.api.server.webhook.management.v2.factories.ServerWebhookManagementServiceFactory;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookCreateRequest;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookPatchRequest;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookResponse;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookUpdateRequest;

import java.net.URI;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V2_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.webhook.management.v2.constants.WebhookMgtEndpointConstants
        .WEBHOOK_PATH_COMPONENT;

/**
 * Implementation of the WebhooksApiService.
 */
public class WebhooksApiServiceImpl implements WebhooksApiService {

    private final ServerWebhookManagementService serverWebhookManagementService;

    public WebhooksApiServiceImpl() {

        try {
            serverWebhookManagementService =
                    ServerWebhookManagementServiceFactory.getServerWebhookManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating the webhook management service.", e);
        }
    }

    @Override
    public Response createWebhook(WebhookCreateRequest webhookCreateRequest) {

        WebhookResponse webhook = serverWebhookManagementService.createWebhook(webhookCreateRequest);
        URI location = ContextLoader.buildURIForHeader(
                String.format("%s%s/%s", V2_API_PATH_COMPONENT, WEBHOOK_PATH_COMPONENT, webhook.getId()));
        return Response.created(location).entity(webhook).build();
    }

    @Override
    public Response getWebhooks(Integer limit, Integer offset) {

        return Response.ok().entity(serverWebhookManagementService.getWebhooks(limit, offset)).build();
    }

    @Override
    public Response getWebhookByWebhookId(String webhookId) {

        return Response.ok().entity(serverWebhookManagementService.getWebhook(webhookId)).build();
    }

    @Override
    public Response updateWebhook(String webhookId, WebhookUpdateRequest webhookUpdateRequest) {

        return Response.ok()
                .entity(serverWebhookManagementService.updateWebhook(webhookId, webhookUpdateRequest)).build();
    }

    @Override
    public Response patchWebhook(String webhookId, WebhookPatchRequest webhookPatchRequest) {

        return Response.ok()
                .entity(serverWebhookManagementService.patchWebhook(webhookId, webhookPatchRequest)).build();
    }

    @Override
    public Response deleteWebhook(String webhookId) {

        serverWebhookManagementService.deleteWebhook(webhookId);
        return Response.noContent().build();
    }

    @Override
    public Response activateWebhook(String webhookId) {

        return Response.ok().entity(serverWebhookManagementService.activateWebhook(webhookId)).build();
    }

    @Override
    public Response deactivateWebhook(String webhookId) {

        return Response.ok().entity(serverWebhookManagementService.deactivateWebhook(webhookId)).build();
    }

    @Override
    public Response retryWebhook(String webhookId) {

        return Response.ok().entity(serverWebhookManagementService.retryWebhook(webhookId)).build();
    }

    @Override
    public Response getWebhookChannel(String webhookId, String channelId) {

        return Response.ok()
                .entity(serverWebhookManagementService.getWebhookChannel(webhookId, channelId)).build();
    }

    @Override
    public Response getChannelSubscribedOrganizations(String webhookId, String channelId, Integer limit,
                                                      Integer offset) {

        return Response.ok().entity(serverWebhookManagementService.getChannelSubscribedOrganizations(
                webhookId, channelId, limit, offset)).build();
    }
}
