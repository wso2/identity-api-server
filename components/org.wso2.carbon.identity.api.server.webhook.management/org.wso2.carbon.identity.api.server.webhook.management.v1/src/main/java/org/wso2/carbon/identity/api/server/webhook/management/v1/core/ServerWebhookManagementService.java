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

package org.wso2.carbon.identity.api.server.webhook.management.v1.core;

import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookList;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookRequest;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookRequestEventProfile;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookResponse;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookSummary;
import org.wso2.carbon.identity.api.server.webhook.management.v1.util.WebhookManagementAPIErrorBuilder;
import org.wso2.carbon.identity.webhook.management.api.exception.WebhookMgtException;
import org.wso2.carbon.identity.webhook.management.api.model.Webhook;
import org.wso2.carbon.identity.webhook.management.api.model.WebhookStatus;
import org.wso2.carbon.identity.webhook.management.api.service.WebhookManagementService;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.webhook.management.v1.constants.WebhookMgtEndpointConstants.ErrorMessage.ERROR_NO_WEBHOOK_FOUND_ON_GIVEN_ID;
import static org.wso2.carbon.identity.api.server.webhook.management.v1.constants.WebhookMgtEndpointConstants.WEBHOOK_PATH_COMPONENT;

/**
 * Call internal osgi services to perform webhook management operations.
 */
public class ServerWebhookManagementService {

    private final WebhookManagementService webhookManagementService;

    public ServerWebhookManagementService(WebhookManagementService webhookManagementService) {

        this.webhookManagementService = webhookManagementService;
    }

    /**
     * Get all webhooks.
     *
     * @return List of webhooks.
     */
    public WebhookList getWebhooks() {

        try {
            List<Webhook> webhooks =
                    webhookManagementService.getWebhooks(CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            return new WebhookList().webhooks(webhooks.stream()
                    .map(this::toWebhookSummary)
                    .collect(Collectors.toList()));
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Get webhook by ID.
     *
     * @param webhookId Webhook ID.
     * @return Webhook.
     */
    public WebhookResponse getWebhook(String webhookId) {

        try {
            Webhook webhook = webhookManagementService.getWebhook(webhookId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (webhook == null) {
                throw WebhookManagementAPIErrorBuilder.buildAPIError(Response.Status.NOT_FOUND,
                        ERROR_NO_WEBHOOK_FOUND_ON_GIVEN_ID, webhookId);
            }
            return getWebhookResponse(webhook);
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Create webhook.
     *
     * @param webhookRequest Webhook creation request.
     * @return Created webhook.
     */
    public WebhookResponse createWebhook(WebhookRequest webhookRequest) {

        try {
            Webhook webhook = buildWebhook(null, webhookRequest);
            return getWebhookResponse(webhookManagementService.createWebhook(webhook,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain()));
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Update webhook.
     *
     * @param webhookId      Webhook ID.
     * @param webhookRequest Webhook update request.
     * @return Updated webhook.
     */
    public WebhookResponse updateWebhook(String webhookId, WebhookRequest webhookRequest) {

        try {
            Webhook webhook = buildWebhook(webhookId, webhookRequest);
            return getWebhookResponse(webhookManagementService.updateWebhook(webhookId, webhook,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain()));
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Delete webhook.
     *
     * @param webhookId Webhook ID.
     */
    public void deleteWebhook(String webhookId) {

        try {
            webhookManagementService.deleteWebhook(webhookId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Activate webhook.
     *
     * @param webhookId Webhook ID.
     * @return Activated webhook.
     */
    public WebhookSummary activateWebhook(String webhookId) {

        try {
            return toWebhookSummary(webhookManagementService.activateWebhook(webhookId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain()));
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Deactivate webhook.
     *
     * @param webhookId Webhook ID.
     * @return Deactivated webhook.
     */
    public WebhookSummary deactivateWebhook(String webhookId) {

        try {
            return toWebhookSummary(webhookManagementService.deactivateWebhook(webhookId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain()));
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    private Webhook buildWebhook(String webhookId, WebhookRequest webhookRequest) {

        return new Webhook.Builder()
                .uuid(webhookId)
                .status(WebhookStatus.valueOf(webhookRequest.getStatus().name()))
                .endpoint(webhookRequest.getEndpoint())
                .name(webhookRequest.getName())
                .secret(webhookRequest.getSecret())
                .eventSchemaName(
                        webhookRequest.getEventProfile() != null ? webhookRequest.getEventProfile().getName() : null)
                .eventSchemaUri(
                        webhookRequest.getEventProfile() != null ? webhookRequest.getEventProfile().getUri() : null)
                .eventsSubscribed(webhookRequest.getChannelsSubscribed())
                .build();
    }

    private WebhookSummary toWebhookSummary(Webhook webhook) {

        WebhookSummary response = new WebhookSummary();
        response.setId(webhook.getUuid());
        response.setEndpoint(webhook.getEndpoint());
        response.setName(webhook.getName());

        // Convert timestamps to ISO 8601 string
        if (webhook.getCreatedAt() != null) {
            response.setCreatedAt(webhook.getCreatedAt().toInstant().toString());
        }
        if (webhook.getUpdatedAt() != null) {
            response.setUpdatedAt(webhook.getUpdatedAt().toInstant().toString());
        }

        // Map status
        if (webhook.getStatus() != null) {
            response.setStatus(WebhookSummary.StatusEnum.fromValue(webhook.getStatus().name()));
        }

        response.setSelf(
                ContextLoader.buildURIForBody(
                        String.format(V1_API_PATH_COMPONENT + WEBHOOK_PATH_COMPONENT + "/%s",
                                webhook.getUuid())).toString());

        return response;
    }

    private WebhookResponse getWebhookResponse(Webhook webhook) {

        WebhookResponse webhookResponse = new WebhookResponse();
        webhookResponse.setId(webhook.getUuid());
        webhookResponse.setCreatedAt(String.valueOf(webhook.getCreatedAt()));
        webhookResponse.setUpdatedAt(String.valueOf(webhook.getUpdatedAt()));
        webhookResponse.setEndpoint(webhook.getEndpoint());
        webhookResponse.setName(webhook.getName());
        WebhookRequestEventProfile eventProfile = new WebhookRequestEventProfile();
        eventProfile.setName(webhook.getEventSchemaName());
        eventProfile.setUri(webhook.getEventSchemaUri());
        webhookResponse.setEventProfile(eventProfile);
        webhookResponse.setStatus(WebhookResponse.StatusEnum.fromValue(webhook.getStatus().name()));
        try {
            webhookResponse.setChannelsSubscribed(webhook.getEventsSubscribed());
        } catch (WebhookMgtException e) {
            webhookResponse.setChannelsSubscribed(null);
        }
        return webhookResponse;
    }
}
