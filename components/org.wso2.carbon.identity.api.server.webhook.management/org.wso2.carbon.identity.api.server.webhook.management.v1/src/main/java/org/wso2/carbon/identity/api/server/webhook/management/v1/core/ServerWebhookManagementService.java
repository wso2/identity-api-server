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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookList;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookRequest;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookRequestEventProfile;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookResponse;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookSubscription;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookSummary;
import org.wso2.carbon.identity.api.server.webhook.management.v1.util.WebhookManagementAPIErrorBuilder;
import org.wso2.carbon.identity.subscription.management.api.model.Subscription;
import org.wso2.carbon.identity.webhook.management.api.exception.WebhookMgtException;
import org.wso2.carbon.identity.webhook.management.api.model.Webhook;
import org.wso2.carbon.identity.webhook.management.api.model.WebhookStatus;
import org.wso2.carbon.identity.webhook.management.api.service.WebhookManagementService;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.webhook.management.v1.constants
        .WebhookMgtEndpointConstants.ErrorMessage.ERROR_NO_WEBHOOK_FOUND_ON_GIVEN_ID;
import static org.wso2.carbon.identity.api.server.webhook.management.v1.constants
        .WebhookMgtEndpointConstants.WEBHOOK_PATH_COMPONENT;

/**
 * Call internal osgi services to perform webhook management operations.
 */
public class ServerWebhookManagementService {

    private static final Log LOG = LogFactory.getLog(ServerWebhookManagementService.class);
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

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving webhooks for tenant: " + tenantDomain);
        }
        try {
            List<Webhook> webhooks = webhookManagementService.getWebhooks(tenantDomain);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Found " + webhooks.size() + " webhooks for tenant: " + tenantDomain);
            }
            return new WebhookList().webhooks(webhooks.stream()
                    .map(this::toWebhookSummary)
                    .collect(Collectors.toList()));
        } catch (WebhookMgtException e) {
            LOG.error("Error retrieving webhooks for tenant: " + tenantDomain, e);
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

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving webhook with ID: " + webhookId + " for tenant: " + tenantDomain);
        }
        try {
            Webhook webhook = webhookManagementService.getWebhook(webhookId, tenantDomain);
            if (webhook == null) {
                LOG.warn("Webhook not found with ID: " + webhookId + " for tenant: " + tenantDomain);
                throw WebhookManagementAPIErrorBuilder.buildAPIError(Response.Status.NOT_FOUND,
                        ERROR_NO_WEBHOOK_FOUND_ON_GIVEN_ID, webhookId);
            }
            return getWebhookResponse(webhook);
        } catch (WebhookMgtException e) {
            LOG.error("Error retrieving webhook with ID: " + webhookId + " for tenant: " + tenantDomain, e);
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

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        String webhookName = webhookRequest != null ? webhookRequest.getName() : "null";
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating webhook with name: " + webhookName + " for tenant: " + tenantDomain);
        }
        if (webhookRequest == null) {
            throw new IllegalArgumentException("Webhook request cannot be null");
        }
        try {
            Webhook webhook = buildWebhook(null, webhookRequest);
            WebhookResponse response = getWebhookResponse(
                    webhookManagementService.createWebhook(webhook, tenantDomain));
            LOG.info("Webhook created successfully with ID: " + response.getId() + " for tenant: " + tenantDomain);
            return response;
        } catch (WebhookMgtException e) {
            LOG.error("Error creating webhook with name: " + webhookName + " for tenant: " + tenantDomain, e);
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

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating webhook with ID: " + webhookId + " for tenant: " + tenantDomain);
        }
        try {
            Webhook webhook = buildWebhook(webhookId, webhookRequest);
            WebhookResponse response = getWebhookResponse(
                    webhookManagementService.updateWebhook(webhookId, webhook, tenantDomain));
            LOG.info("Webhook updated successfully with ID: " + webhookId + " for tenant: " + tenantDomain);
            return response;
        } catch (WebhookMgtException e) {
            LOG.error("Error updating webhook with ID: " + webhookId + " for tenant: " + tenantDomain, e);
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Delete webhook.
     *
     * @param webhookId Webhook ID.
     */
    public void deleteWebhook(String webhookId) {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting webhook with ID: " + webhookId + " for tenant: " + tenantDomain);
        }
        try {
            webhookManagementService.deleteWebhook(webhookId, tenantDomain);
            LOG.info("Webhook deleted successfully with ID: " + webhookId + " for tenant: " + tenantDomain);
        } catch (WebhookMgtException e) {
            LOG.error("Error deleting webhook with ID: " + webhookId + " for tenant: " + tenantDomain, e);
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Activate webhook.
     *
     * @param webhookId Webhook ID.
     * @return Activated webhook.
     */
    public WebhookResponse activateWebhook(String webhookId) {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Activating webhook with ID: " + webhookId + " for tenant: " + tenantDomain);
        }
        try {
            WebhookResponse response = getWebhookResponse(
                    webhookManagementService.activateWebhook(webhookId, tenantDomain));
            LOG.info("Webhook activated successfully with ID: " + webhookId + " for tenant: " + tenantDomain);
            return response;
        } catch (WebhookMgtException e) {
            LOG.error("Error activating webhook with ID: " + webhookId + " for tenant: " + tenantDomain, e);
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Deactivate webhook.
     *
     * @param webhookId Webhook ID.
     * @return Deactivated webhook.
     */
    public WebhookResponse deactivateWebhook(String webhookId) {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deactivating webhook with ID: " + webhookId + " for tenant: " + tenantDomain);
        }
        try {
            WebhookResponse response = getWebhookResponse(
                    webhookManagementService.deactivateWebhook(webhookId, tenantDomain));
            LOG.info("Webhook deactivated successfully with ID: " + webhookId + " for tenant: " + tenantDomain);
            return response;
        } catch (WebhookMgtException e) {
            LOG.error("Error deactivating webhook with ID: " + webhookId + " for tenant: " + tenantDomain, e);
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Retry webhook.
     *
     * @param webhookId Webhook ID.
     * @return Retried webhook.
     */
    public WebhookResponse retryWebhook(String webhookId) {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrying webhook with ID: " + webhookId + " for tenant: " + tenantDomain);
        }
        try {
            WebhookResponse response = getWebhookResponse(
                    webhookManagementService.retryWebhook(webhookId, tenantDomain));
            LOG.info("Webhook retry initiated successfully for ID: " + webhookId + " for tenant: " + tenantDomain);
            return response;
        } catch (WebhookMgtException e) {
            LOG.error("Error retrying webhook with ID: " + webhookId + " for tenant: " + tenantDomain, e);
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    private Webhook buildWebhook(String webhookId, WebhookRequest webhookRequest) {

        List<Subscription> subscriptions = null;
        if (webhookRequest.getChannelsSubscribed() != null) {
            subscriptions = webhookRequest.getChannelsSubscribed().stream()
                    .map(channelUri -> Subscription.builder()
                            .channelUri(channelUri)
                            .build())
                    .collect(Collectors.toList());
        }

        return new Webhook.Builder()
                .uuid(webhookId)
                .status(WebhookStatus.valueOf(webhookRequest.getStatus().name()))
                .endpoint(webhookRequest.getEndpoint())
                .name(webhookRequest.getName())
                .secret(webhookRequest.getSecret())
                .eventProfileName(
                        webhookRequest.getEventProfile() != null ? webhookRequest.getEventProfile().getName() : null)
                .eventProfileUri(
                        webhookRequest.getEventProfile() != null ? webhookRequest.getEventProfile().getUri() : null)
                .eventsSubscribed(subscriptions)
                .build();
    }

    private WebhookSummary toWebhookSummary(Webhook webhook) {

        WebhookSummary response = new WebhookSummary();
        response.setId(webhook.getId());
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
                                webhook.getId())).toString());

        return response;
    }

    private WebhookResponse getWebhookResponse(Webhook webhook) {

        WebhookResponse webhookResponse = new WebhookResponse();
        webhookResponse.setId(webhook.getId());
        webhookResponse.setCreatedAt(webhook.getCreatedAt().toInstant().toString());
        webhookResponse.setUpdatedAt(webhook.getUpdatedAt().toInstant().toString());
        webhookResponse.setEndpoint(webhook.getEndpoint());
        webhookResponse.setName(webhook.getName());
        WebhookRequestEventProfile eventProfile = new WebhookRequestEventProfile();
        eventProfile.setName(webhook.getEventProfileName());
        eventProfile.setUri(webhook.getEventProfileUri());
        webhookResponse.setEventProfile(eventProfile);
        webhookResponse.setStatus(WebhookResponse.StatusEnum.fromValue(webhook.getStatus().name()));
        try {
            if (webhook.getEventsSubscribed() != null) {
                webhookResponse.setChannelsSubscribed(
                        webhook.getEventsSubscribed().stream()
                                .map(subscription -> new WebhookSubscription()
                                        .channelUri(subscription.getChannelUri())
                                        .status(subscription.getStatus() != null
                                                ? WebhookSubscription.StatusEnum.fromValue(
                                                subscription.getStatus().name())
                                                : null))
                                .collect(Collectors.toList())
                );
            } else {
                webhookResponse.setChannelsSubscribed(null);
            }
        } catch (WebhookMgtException e) {
            LOG.warn("Error setting channels subscribed for webhook ID: " + webhook.getId(), e);
            webhookResponse.setChannelsSubscribed(null);
        }
        return webhookResponse;
    }
}
