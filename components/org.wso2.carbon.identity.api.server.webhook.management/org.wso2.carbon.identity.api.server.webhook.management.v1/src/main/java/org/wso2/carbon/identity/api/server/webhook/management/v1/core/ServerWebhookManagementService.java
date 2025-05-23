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
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookList;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookRequest;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookRequestEventSchema;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookResponse;
import org.wso2.carbon.identity.api.server.webhook.management.v1.util.WebhookManagementAPIErrorBuilder;
import org.wso2.carbon.identity.webhook.management.api.exception.WebhookMgtException;
import org.wso2.carbon.identity.webhook.management.api.model.Webhook;
import org.wso2.carbon.identity.webhook.management.api.service.WebhookManagementService;

import java.util.List;
import java.util.stream.Collectors;

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
                    .map(this::toWebhookResponse)
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
            Webhook webhook = buildWebhook(webhookRequest);
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
            Webhook webhook = buildWebhook(webhookRequest);
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
     */
    public void activateWebhook(String webhookId) {

        try {
            webhookManagementService.activateWebhook(webhookId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Deactivate webhook.
     *
     * @param webhookId Webhook ID.
     */
    public void deactivateWebhook(String webhookId) {

        try {
            webhookManagementService.deactivateWebhook(webhookId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    private Webhook buildWebhook(WebhookRequest webhookRequest) {

        return new Webhook.Builder()
                .endpoint(webhookRequest.getEndpoint())
                .description(webhookRequest.getDescription())
                .secret(webhookRequest.getSecret())
                .eventSchemaName(
                        webhookRequest.getEventSchema() != null ? webhookRequest.getEventSchema().getName() : null)
                .eventSchemaUri(
                        webhookRequest.getEventSchema() != null ? webhookRequest.getEventSchema().getUri() : null)
                .eventsSubscribed(webhookRequest.getEventsSubscribed())
                .build();
    }

    private WebhookResponse toWebhookResponse(Webhook webhook) {

        WebhookResponse response = new WebhookResponse();
        response.setId(webhook.getUuid());
        response.setEndpoint(webhook.getEndpoint());
        response.setDescription(webhook.getDescription());

        // Convert event schema
        WebhookRequestEventSchema eventSchema = new WebhookRequestEventSchema();
        eventSchema.setName(webhook.getEventSchemaName());
        eventSchema.setUri(webhook.getEventSchemaUri());
        response.setEventSchema(eventSchema);

        // Convert timestamps to ISO 8601 string
        if (webhook.getCreatedAt() != null) {
            response.setCreatedAt(webhook.getCreatedAt().toInstant().toString());
        }
        if (webhook.getUpdatedAt() != null) {
            response.setUpdatedAt(webhook.getUpdatedAt().toInstant().toString());
        }

        // Map status
        if (webhook.getStatus() != null) {
            response.setStatus(WebhookResponse.StatusEnum.fromValue(webhook.getStatus().name()));
        }

        return response;
    }

    private WebhookResponse getWebhookResponse(Webhook webhook) {

        WebhookResponse webhookResponse = new WebhookResponse();
        webhookResponse.setId(webhook.getUuid());
        webhookResponse.setCreatedAt(String.valueOf(webhook.getCreatedAt()));
        webhookResponse.setUpdatedAt(String.valueOf(webhook.getUpdatedAt()));
        webhookResponse.setEndpoint(webhook.getEndpoint());
        webhookResponse.setDescription(webhook.getDescription());
        WebhookRequestEventSchema eventSchema = new WebhookRequestEventSchema();
        eventSchema.setName(webhook.getEventSchemaName());
        eventSchema.setUri(webhook.getEventSchemaUri());
        webhookResponse.setEventSchema(eventSchema);
        try {
            webhookResponse.setEventsSubscribed(webhook.getEventsSubscribed());
        } catch (WebhookMgtException e) {
            webhookResponse.setEventsSubscribed(null);
        }
        return webhookResponse;
    }
}
