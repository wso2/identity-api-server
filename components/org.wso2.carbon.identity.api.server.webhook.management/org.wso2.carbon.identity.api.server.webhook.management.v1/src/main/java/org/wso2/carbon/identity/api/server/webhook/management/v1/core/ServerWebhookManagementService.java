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
import org.wso2.carbon.identity.webhook.management.api.model.WebhookDTO;
import org.wso2.carbon.identity.webhook.management.api.model.WebhookSummaryDTO;
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
            List<WebhookSummaryDTO> webhooks =
                    webhookManagementService.getWebhooks(CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            return new WebhookList().webhooks(webhooks.stream()
                    .map(this::getWebhookResponse)
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
            WebhookDTO webhookDTO = webhookManagementService.getWebhook(webhookId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            return getWebhookResponse(webhookDTO);
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
            WebhookDTO webhookDTO = webhookManagementService.createWebhook(webhook,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            return getWebhookResponse(webhookDTO);
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
            WebhookDTO webhookDTO = webhookManagementService.updateWebhook(webhookId, webhook,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            return getWebhookResponse(webhookDTO);
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

    /**
     * Build webhook model from webhook request.
     *
     * @param webhookRequest Webhook request.
     * @return Webhook model.
     */
    private Webhook buildWebhook(WebhookRequest webhookRequest) {

        Webhook webhook = new Webhook();
        webhook.setEndpoint(webhookRequest.getEndpoint());
        webhook.setDescription(webhookRequest.getDescription());
        webhook.setSecret(webhookRequest.getSecret());
        webhook.setEventSchemaName(webhookRequest.getEventSchema().getName());
        webhook.setEventSchemaUri(webhookRequest.getEventSchema().getUri());
        webhook.setEventsSubscribed(webhookRequest.getEventsSubscribed());

        return webhook;
    }

    /**
     * Get webhook response from webhook model.
     *
     * @param webhookSummaryDTO WebhookDTO model.
     * @return Webhook response.
     */
    private WebhookResponse getWebhookResponse(WebhookSummaryDTO webhookSummaryDTO) {

        WebhookResponse webhookResponse = new WebhookResponse();
        webhookResponse.setId(webhookSummaryDTO.getId());
        webhookResponse.setEndpoint(webhookSummaryDTO.getEndpoint());
        webhookResponse.setDescription(webhookSummaryDTO.getDescription());
        WebhookRequestEventSchema eventSchema = new WebhookRequestEventSchema();
        eventSchema.setName(webhookSummaryDTO.getEventSchemaName());
        eventSchema.setUri(webhookSummaryDTO.getEventSchemaUri());
        webhookResponse.setEventSchema(eventSchema);

        return webhookResponse;
    }

    /**
     * Get webhook response from webhook model.
     *
     * @param webhookDTO WebhookDTO model.
     * @return Webhook response.
     */
    private WebhookResponse getWebhookResponse(WebhookDTO webhookDTO) {

        WebhookResponse webhookResponse = new WebhookResponse();
        webhookResponse.setId(webhookDTO.getId());
        webhookResponse.setCreatedAt(webhookDTO.getCreatedAt());
        webhookResponse.setUpdatedAt(webhookDTO.getUpdatedAt());
        webhookResponse.setEndpoint(webhookDTO.getEndpoint());
        webhookResponse.setDescription(webhookDTO.getDescription());
        WebhookRequestEventSchema eventSchema = new WebhookRequestEventSchema();
        eventSchema.setName(webhookDTO.getEventSchemaName());
        eventSchema.setUri(webhookDTO.getEventSchemaUri());
        webhookResponse.setEventSchema(eventSchema);
        webhookResponse.setEventsSubscribed(webhookDTO.getEventsSubscribed());

        return webhookResponse;
    }
}
