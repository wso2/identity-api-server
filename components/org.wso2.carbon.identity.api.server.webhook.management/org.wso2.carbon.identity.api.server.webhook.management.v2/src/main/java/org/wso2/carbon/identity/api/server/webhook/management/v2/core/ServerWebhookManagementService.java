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

package org.wso2.carbon.identity.api.server.webhook.management.v2.core;

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.webhook.management.v2.constants.WebhookMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.ChannelResponse;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.ChildOrgSubscription;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.EventProfile;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.Link;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.Organization;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.SubscribedOrganizationList;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookCreateRequest;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookList;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookPatchRequest;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookResponse;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookSubscription;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookSummary;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookUpdateRequest;
import org.wso2.carbon.identity.api.server.webhook.management.v2.util.WebhookManagementAPIErrorBuilder;
import org.wso2.carbon.identity.subscription.management.api.model.Subscription;
import org.wso2.carbon.identity.webhook.management.api.exception.WebhookMgtException;
import org.wso2.carbon.identity.webhook.management.api.model.ChannelOrgSubscription;
import org.wso2.carbon.identity.webhook.management.api.model.ChannelSubscribedOrganizations;
import org.wso2.carbon.identity.webhook.management.api.model.OrganizationSubscription;
import org.wso2.carbon.identity.webhook.management.api.model.SubscriptionPolicy;
import org.wso2.carbon.identity.webhook.management.api.model.Webhook;
import org.wso2.carbon.identity.webhook.management.api.model.WebhookChannel;
import org.wso2.carbon.identity.webhook.management.api.model.WebhookStatus;
import org.wso2.carbon.identity.webhook.management.api.service.WebhookManagementService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V2_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.webhook.management.v2.constants.WebhookMgtEndpointConstants.CHANNEL_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.webhook.management.v2.constants.WebhookMgtEndpointConstants.DEFAULT_OFFSET;
import static org.wso2.carbon.identity.api.server.webhook.management.v2.constants.WebhookMgtEndpointConstants.DEFAULT_LIMIT;
import static org.wso2.carbon.identity.api.server.webhook.management.v2.constants.WebhookMgtEndpointConstants.ORGANIZATION_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.webhook.management.v2.constants.WebhookMgtEndpointConstants.PAGE_LINK_REL_NEXT;
import static org.wso2.carbon.identity.api.server.webhook.management.v2.constants.WebhookMgtEndpointConstants.PAGE_LINK_REL_PREVIOUS;
import static org.wso2.carbon.identity.api.server.webhook.management.v2.constants.WebhookMgtEndpointConstants.WEBHOOK_PATH_COMPONENT;

/**
 * Webhook management service for the v2 API.
 * <p>
 * Translates between the v2 API models and the {@link Webhook} domain model. Everything that speaks the domain
 * model is delegated to {@link WebhookManagementService}, which is shared with v1 unchanged.
 */
public class ServerWebhookManagementService {

    private final WebhookManagementService webhookManagementService;

    public ServerWebhookManagementService(WebhookManagementService webhookManagementService) {

        this.webhookManagementService = webhookManagementService;
    }

    /**
     * Get a page of webhooks.
     *
     * @param limit  Maximum number of records to return.
     * @param offset Number of records to skip.
     * @return Paginated webhook list.
     */
    public WebhookList getWebhooks(Integer limit, Integer offset) {

        int resolvedLimit = limit != null ? limit : DEFAULT_LIMIT;
        int resolvedOffset = offset != null ? offset : DEFAULT_OFFSET;
        try {
            List<Webhook> webhooks = webhookManagementService.getWebhooks(resolvedOffset, resolvedLimit,
                    ContextLoader.getTenantDomainFromContext());
            int totalResults = webhookManagementService.getWebhooksCount(
                    ContextLoader.getTenantDomainFromContext());
            return buildWebhookList(webhooks, totalResults, resolvedOffset, resolvedLimit);
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Get a webhook by its ID.
     *
     * @param webhookId Webhook ID.
     * @return Webhook representation.
     */
    public WebhookResponse getWebhook(String webhookId) {

        try {
            Webhook webhook = webhookManagementService.getWebhook(webhookId,
                    ContextLoader.getTenantDomainFromContext());
            if (webhook == null) {
                throw WebhookManagementAPIErrorBuilder.buildAPIError(Response.Status.NOT_FOUND,
                        WebhookMgtEndpointConstants.ErrorMessage.ERROR_NO_WEBHOOK_FOUND_ON_GIVEN_ID, webhookId);
            }
            return buildWebhookResponse(webhook);
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Create a webhook.
     *
     * @param webhookCreateRequest Webhook to create.
     * @return Created webhook representation.
     */
    public WebhookResponse createWebhook(WebhookCreateRequest webhookCreateRequest) {

        try {
            Webhook webhook = webhookManagementService.createWebhook(buildWebhookForCreate(webhookCreateRequest),
                    ContextLoader.getTenantDomainFromContext());
            return buildWebhookResponse(webhook);
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Replace a webhook.
     * <p>
     * {@code subscribedChannels} is the complete desired state: the channel set is replaced, and each channel's
     * organization fanout is set to what its {@code childOrgSubscription} says. A channel that omits it is set to
     * {@code NONE}.
     *
     * @param webhookId Webhook ID.
     * @param webhookUpdateRequest   Replacement representation.
     * @return Updated webhook representation.
     */
    public WebhookResponse updateWebhook(String webhookId, WebhookUpdateRequest webhookUpdateRequest) {

        try {
            Webhook existingWebhook = getExistingWebhook(webhookId);
            Webhook webhook = new Webhook.Builder()
                    .uuid(webhookId)
                    .endpoint(webhookUpdateRequest.getEndpoint())
                    .name(webhookUpdateRequest.getName())
                    .secret(webhookUpdateRequest.getSecret())
                    .eventProfileName(webhookUpdateRequest.getEventProfile() != null ?
                            webhookUpdateRequest.getEventProfile().getName() : null)
                    .eventProfileUri(webhookUpdateRequest.getEventProfile() != null ?
                            webhookUpdateRequest.getEventProfile().getUri() : null)
                    .eventProfileVersion(existingWebhook.getEventProfileVersion())
                    .status(existingWebhook.getStatus())
                    .eventsSubscribed(buildChannelSubscriptions(webhookUpdateRequest.getSubscribedChannels()))
                    .organizationSubscriptions(
                            buildOrganizationSubscriptions(webhookUpdateRequest.getSubscribedChannels()))
                    .build();
            Webhook updatedWebhook = webhookManagementService.updateWebhook(webhookId, webhook,
                    ContextLoader.getTenantDomainFromContext());
            return buildWebhookResponse(updatedWebhook);
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Partially update a webhook.
     * <p>
     * Applied as a read-modify-write over the replace operation: properties absent from the request are carried
     * forward from the stored webhook.
     *
     * @param webhookId Webhook ID.
     * @param webhookPatchRequest   Properties to modify.
     * @return Updated webhook representation.
     */
    public WebhookResponse patchWebhook(String webhookId, WebhookPatchRequest webhookPatchRequest) {

        if (webhookPatchRequest == null ||
                (webhookPatchRequest.getEndpoint() == null && webhookPatchRequest.getName() == null
                && webhookPatchRequest.getSecret() == null && webhookPatchRequest.getSubscribedChannels() == null)) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(Response.Status.BAD_REQUEST,
                    WebhookMgtEndpointConstants.ErrorMessage.ERROR_EMPTY_PATCH_REQUEST);
        }
        try {
            Webhook existingWebhook = getExistingWebhook(webhookId);

            // Absent properties are carried forward. Omitting secret preserves the stored one, which the domain
            // layer already handles, so a null secret is passed through rather than resolved here.
            Webhook.Builder builder = new Webhook.Builder()
                    .uuid(webhookId)
                    .endpoint(webhookPatchRequest.getEndpoint() != null ? webhookPatchRequest.getEndpoint() : existingWebhook.getEndpoint())
                    .name(webhookPatchRequest.getName() != null ? webhookPatchRequest.getName() : existingWebhook.getName())
                    .secret(webhookPatchRequest.getSecret())
                    .eventProfileName(existingWebhook.getEventProfileName())
                    .eventProfileUri(existingWebhook.getEventProfileUri())
                    .eventProfileVersion(existingWebhook.getEventProfileVersion())
                    .status(existingWebhook.getStatus());

            if (webhookPatchRequest.getSubscribedChannels() != null) {
                builder.eventsSubscribed(buildChannelSubscriptions(webhookPatchRequest.getSubscribedChannels()))
                        .organizationSubscriptions(buildOrganizationSubscriptions(webhookPatchRequest.getSubscribedChannels()));
            } else {
                builder.eventsSubscribed(existingWebhook.getEventsSubscribed())
                        .organizationSubscriptions(Collections.emptyList());
            }

            Webhook updatedWebhook = webhookManagementService.updateWebhook(webhookId, builder.build(),
                    ContextLoader.getTenantDomainFromContext());
            return buildWebhookResponse(updatedWebhook);
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Delete a webhook.
     *
     * @param webhookId Webhook ID.
     */
    public void deleteWebhook(String webhookId) {

        try {
            webhookManagementService.deleteWebhook(webhookId, ContextLoader.getTenantDomainFromContext());
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Activate a webhook.
     *
     * @param webhookId Webhook ID.
     * @return Updated webhook representation.
     */
    public WebhookResponse activateWebhook(String webhookId) {

        try {
            return buildWebhookResponse(webhookManagementService.activateWebhook(webhookId,
                    ContextLoader.getTenantDomainFromContext()));
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Deactivate a webhook.
     *
     * @param webhookId Webhook ID.
     * @return Updated webhook representation.
     */
    public WebhookResponse deactivateWebhook(String webhookId) {

        try {
            return buildWebhookResponse(webhookManagementService.deactivateWebhook(webhookId,
                    ContextLoader.getTenantDomainFromContext()));
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Retry the failed channel operations of a webhook.
     *
     * @param webhookId Webhook ID.
     * @return Updated webhook representation.
     */
    public WebhookResponse retryWebhook(String webhookId) {

        try {
            return buildWebhookResponse(webhookManagementService.retryWebhook(webhookId,
                    ContextLoader.getTenantDomainFromContext()));
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Get a single subscribed channel of a webhook.
     *
     * @param webhookId Webhook ID.
     * @param channelId Channel ID.
     * @return Channel representation, carrying the link to its subscribed organizations.
     */
    public ChannelResponse getWebhookChannel(String webhookId, String channelId) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            // One call: the channel, its subscription status and its organization policy describe the same
            // moment, and the channel's ownership is proved once rather than once per value.
            WebhookChannel webhookChannel = webhookManagementService.getWebhookChannel(
                    webhookId, channelId, tenantDomain);

            ChannelResponse channelResponse = new ChannelResponse();
            channelResponse.setChannelId(webhookChannel.getChannelId());
            channelResponse.setChannelUri(webhookChannel.getChannelUri());
            channelResponse.setStatus(webhookChannel.getStatus() != null
                    ? ChannelResponse.StatusEnum.fromValue(webhookChannel.getStatus().name())
                    : null);

            // organizationsRef is published here and not on the webhook representation, so each representation
            // links exactly one level down.
            ChildOrgSubscription childOrgSubscription = new ChildOrgSubscription();
            childOrgSubscription.setSubscriptionPolicy(toSubscriptionPolicyEnum(webhookChannel.getPolicy()));
            childOrgSubscription.setOrganizationsRef(buildOrganizationsRef(webhookId, channelId));
            channelResponse.setChildOrgSubscription(childOrgSubscription);
            return channelResponse;
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Get a page of the child organizations receiving events for a channel.
     *
     * @param webhookId Webhook ID.
     * @param channelId Channel ID.
     * @param limit     Maximum number of records to return.
     * @param offset    Number of records to skip.
     * @return Paginated organization list.
     */
    public SubscribedOrganizationList getChannelSubscribedOrganizations(String webhookId, String channelId,
                                                                        Integer limit, Integer offset) {

        int resolvedLimit = limit != null ? limit : DEFAULT_LIMIT;
        int resolvedOffset = offset != null ? offset : DEFAULT_OFFSET;
        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            // One call: the channel context and the page describe the same moment, and the channel's ownership
            // is proved once rather than once per value.
            ChannelSubscribedOrganizations channelSubscribedOrganizations =
                    webhookManagementService.getChannelOrganizationSubscriptions(
                            webhookId, channelId, tenantDomain, resolvedOffset, resolvedLimit);
            List<ChannelOrgSubscription> organizations = channelSubscribedOrganizations.getOrganizations();
            List<Organization> mappedOrganizations = buildSubscribedOrgList(organizations);

            SubscribedOrganizationList subscribedOrganizations = new SubscribedOrganizationList();
            subscribedOrganizations.setChannelUri(channelSubscribedOrganizations.getChannelUri());
            subscribedOrganizations.setSubscriptionPolicy(
                    toListSubscriptionPolicyEnum(channelSubscribedOrganizations.getPolicy()));
            subscribedOrganizations.setTotalResults(channelSubscribedOrganizations.getTotalCount());
            subscribedOrganizations.setStartIndex(resolvedOffset);
            // count describes the page actually returned, so it is taken from the mapped list rather than
            // from the domain result, which leaves buildSubscribedOrgList as the only null handling path.
            subscribedOrganizations.setCount(mappedOrganizations.size());
            subscribedOrganizations.setOrganizations(mappedOrganizations);
            return subscribedOrganizations;
        } catch (WebhookMgtException e) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(e);
        }
    }

    private Webhook getExistingWebhook(String webhookId) throws WebhookMgtException {

        Webhook existingWebhook = webhookManagementService.getWebhook(webhookId,
                ContextLoader.getTenantDomainFromContext());
        if (existingWebhook == null) {
            throw WebhookManagementAPIErrorBuilder.buildAPIError(Response.Status.NOT_FOUND,
                    WebhookMgtEndpointConstants.ErrorMessage.ERROR_NO_WEBHOOK_FOUND_ON_GIVEN_ID, webhookId);
        }
        return existingWebhook;
    }

    private Webhook buildWebhookForCreate(WebhookCreateRequest request) {

        return new Webhook.Builder()
                .endpoint(request.getEndpoint())
                .name(request.getName())
                .secret(request.getSecret())
                .eventProfileName(request.getEventProfile() != null ? request.getEventProfile().getName() : null)
                .eventProfileUri(request.getEventProfile() != null ? request.getEventProfile().getUri() : null)
                .status(request.getStatus() != null
                        ? WebhookStatus.valueOf(request.getStatus().value())
                        : null)
                .eventsSubscribed(buildChannelSubscriptions(request.getSubscribedChannels()))
                .organizationSubscriptions(buildOrganizationSubscriptions(request.getSubscribedChannels()))
                .build();
    }

    private List<Subscription> buildChannelSubscriptions(List<WebhookSubscription> channels) {

        if (channels == null) {
            return Collections.emptyList();
        }
        return channels.stream()
                .map(channel -> Subscription.builder().channelUri(channel.getChannelUri()).build())
                .collect(Collectors.toList());
    }

    /**
     * Map every supplied channel to an organization subscription.
     * <p>
     * A channel that omits {@code childOrgSubscription} is mapped to a null policy, which the service layer reads
     * as "leave this channel's organization subscriptions as they are". That is what the API documents: such a
     * channel keeps its current policy when it is already subscribed, and has none when it is new. A channel that
     * states {@code NONE} explicitly is a request to clear its fanout, which is how removal is expressed.
     */
    private List<OrganizationSubscription> buildOrganizationSubscriptions(List<WebhookSubscription> channels) {

        if (channels == null) {
            return Collections.emptyList();
        }
        List<OrganizationSubscription> subscriptions = new ArrayList<>();
        for (WebhookSubscription channel : channels) {
            ChildOrgSubscription childOrgSubscription = channel.getChildOrgSubscription();
            SubscriptionPolicy policy = childOrgSubscription != null
                    && childOrgSubscription.getSubscriptionPolicy() != null
                    ? SubscriptionPolicy.valueOf(childOrgSubscription.getSubscriptionPolicy().value())
                    : null;
            List<String> organizationIds = childOrgSubscription != null
                    && childOrgSubscription.getOrganizations() != null
                    ? childOrgSubscription.getOrganizations().stream()
                    .map(Organization::getOrgId)
                    .collect(Collectors.toList())
                    : null;
            subscriptions.add(OrganizationSubscription.builder()
                    .channelUri(channel.getChannelUri())
                    .policy(policy)
                    .organizationIds(organizationIds)
                    .build());
        }
        return subscriptions;
    }

    private WebhookList buildWebhookList(List<Webhook> webhooks, int totalResults, int offset, int limit) {

        WebhookList response = new WebhookList();
        response.setTotalResults(totalResults);
        response.setStartIndex(offset);
        response.setCount(webhooks != null ? webhooks.size() : 0);
        response.setWebhooks(webhooks != null
                ? webhooks.stream().map(this::buildWebhookSummary).collect(Collectors.toList())
                : Collections.emptyList());
        response.setLinks(buildPageLinks(totalResults, offset, limit));
        return response;
    }

    private List<Link> buildPageLinks(int totalResults, int offset, int limit) {

        List<Link> links = new ArrayList<>();
        if (offset + limit < totalResults) {
            links.add(buildPageLink(PAGE_LINK_REL_NEXT, offset + limit, limit));
        }
        if (offset > 0) {
            links.add(buildPageLink(PAGE_LINK_REL_PREVIOUS, Math.max(offset - limit, 0), limit));
        }
        return links;
    }

    private Link buildPageLink(String rel, int offset, int limit) {

        Link link = new Link();
        link.setRel(rel);
        link.setHref(ContextLoader.buildURIForBody(
                String.format("%s%s?limit=%d&offset=%d", V2_API_PATH_COMPONENT, WEBHOOK_PATH_COMPONENT,
                        limit, offset)).toString());
        return link;
    }

    private WebhookSummary buildWebhookSummary(Webhook webhook) {

        WebhookSummary webhookSummary = new WebhookSummary();
        webhookSummary.setId(webhook.getId());
        webhookSummary.setEndpoint(webhook.getEndpoint());
        webhookSummary.setName(webhook.getName());
        webhookSummary.setCreatedAt(toIsoString(webhook.getCreatedAt()));
        webhookSummary.setUpdatedAt(toIsoString(webhook.getUpdatedAt()));
        if (webhook.getStatus() != null) {
            webhookSummary.setStatus(WebhookSummary.StatusEnum.fromValue(webhook.getStatus().name()));
        }
        webhookSummary.setSelf(ContextLoader.buildURIForBody(
                String.format("%s%s/%s", V2_API_PATH_COMPONENT, WEBHOOK_PATH_COMPONENT, webhook.getId()))
                .toString());
        return webhookSummary;
    }

    private WebhookResponse buildWebhookResponse(Webhook webhook) throws WebhookMgtException {

        WebhookResponse response = new WebhookResponse();
        response.setId(webhook.getId());
        response.setCreatedAt(toIsoString(webhook.getCreatedAt()));
        response.setUpdatedAt(toIsoString(webhook.getUpdatedAt()));
        response.setEndpoint(webhook.getEndpoint());
        response.setName(webhook.getName());

        EventProfile eventProfile = new EventProfile();
        eventProfile.setName(webhook.getEventProfileName());
        eventProfile.setUri(webhook.getEventProfileUri());
        response.setEventProfile(eventProfile);

        if (webhook.getStatus() != null) {
            response.setStatus(WebhookResponse.StatusEnum.fromValue(webhook.getStatus().name()));
        }
        response.setSubscribedChannels(buildSubscribedChannels(webhook));
        return response;
    }

    /**
     * Build the channel list of a webhook. Each channel arrives complete -- URI, identity, subscription status
     * and organization policy -- so nothing is joined here.
     */
    private List<WebhookSubscription> buildSubscribedChannels(Webhook webhook) throws WebhookMgtException {

        List<WebhookSubscription> channels = new ArrayList<>();
        for (WebhookChannel webhookChannel : webhookManagementService.getWebhookChannels(
                webhook.getId(), ContextLoader.getTenantDomainFromContext())) {
            WebhookSubscription channel = new WebhookSubscription();
            channel.setChannelUri(webhookChannel.getChannelUri());
            if (webhookChannel.getStatus() != null) {
                channel.setStatus(WebhookSubscription.StatusEnum.fromValue(webhookChannel.getStatus().name()));
            }
            channel.setChannelId(webhookChannel.getChannelId());
            channel.setSelf(buildChannelSelf(webhook.getId(), webhookChannel.getChannelId()));

            // The webhook representation carries the policy but not organizationsRef: that link belongs to the
            // channel resource, one level down.
            ChildOrgSubscription childOrgSubscription = new ChildOrgSubscription();
            childOrgSubscription.setSubscriptionPolicy(toSubscriptionPolicyEnum(webhookChannel.getPolicy()));
            channel.setChildOrgSubscription(childOrgSubscription);

            channels.add(channel);
        }
        return channels;
    }

    private List<Organization> buildSubscribedOrgList(List<ChannelOrgSubscription> organizations) {

        if (organizations == null) {
            return Collections.emptyList();
        }
        return organizations.stream().map(organization -> {
            Organization mapped = new Organization();
            mapped.setOrgId(organization.getSubscribedOrgId());
            mapped.setOrgName(organization.getOrgName());
            mapped.setParentOrgId(organization.getParentOrgId());
            return mapped;
        }).collect(Collectors.toList());
    }

    private ChildOrgSubscription.SubscriptionPolicyEnum toSubscriptionPolicyEnum(SubscriptionPolicy policy) {

        return policy != null
                ? ChildOrgSubscription.SubscriptionPolicyEnum.fromValue(policy.name())
                : ChildOrgSubscription.SubscriptionPolicyEnum.fromValue(SubscriptionPolicy.NONE.name());
    }

    private SubscribedOrganizationList.SubscriptionPolicyEnum toListSubscriptionPolicyEnum(
            SubscriptionPolicy policy) {

        return policy != null
                ? SubscribedOrganizationList.SubscriptionPolicyEnum.fromValue(policy.name())
                : SubscribedOrganizationList.SubscriptionPolicyEnum.fromValue(SubscriptionPolicy.NONE.name());
    }


    private String buildChannelSelf(String webhookId, String channelId) {

        return ContextLoader.buildURIForBody(
                String.format("%s%s/%s%s/%s", V2_API_PATH_COMPONENT, WEBHOOK_PATH_COMPONENT, webhookId,
                        CHANNEL_PATH_COMPONENT, channelId)).toString();
    }

    private String buildOrganizationsRef(String webhookId, String channelId) {

        return ContextLoader.buildURIForBody(
                String.format("%s%s/%s%s/%s%s", V2_API_PATH_COMPONENT, WEBHOOK_PATH_COMPONENT, webhookId,
                        CHANNEL_PATH_COMPONENT, channelId, ORGANIZATION_PATH_COMPONENT)).toString();
    }

    private String toIsoString(Timestamp timestamp) {

        return timestamp != null ? timestamp.toInstant().toString() : null;
    }
}
