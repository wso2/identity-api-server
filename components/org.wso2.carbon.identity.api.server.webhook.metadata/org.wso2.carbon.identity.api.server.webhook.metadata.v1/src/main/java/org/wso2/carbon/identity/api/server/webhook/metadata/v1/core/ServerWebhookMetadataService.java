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

package org.wso2.carbon.identity.api.server.webhook.metadata.v1.core;

import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.webhook.metadata.common.WebhookMetadataServiceHolder;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.Channel;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.Event;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.EventProfile;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.EventProfileMetadata;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.WebhookMetadata;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.WebhookMetadataAdapter;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.WebhookMetadataOrganizationPolicy;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.WebhookMetadataProperties;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.util.WebhookMetadataAPIErrorBuilder;
import org.wso2.carbon.identity.organization.resource.sharing.policy.management.constant.PolicyEnum;
import org.wso2.carbon.identity.webhook.metadata.api.exception.WebhookMetadataException;
import org.wso2.carbon.identity.webhook.metadata.api.model.Adapter;
import org.wso2.carbon.identity.webhook.metadata.api.model.OrganizationPolicy;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.webhook.metadata.v1.constants.WebhookMetadataEndpointConstants.EVENT_PROFILE_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.webhook.metadata.v1.constants.WebhookMetadataEndpointConstants.ErrorMessage.ERROR_CODE_PROFILE_NOT_FOUND;
import static org.wso2.carbon.identity.api.server.webhook.metadata.v1.constants.WebhookMetadataEndpointConstants.WEBHOOK_METADATA_PATH_COMPONENT;

/**
 * Calls internal services to perform webhook metadata related operations.
 */
public class ServerWebhookMetadataService {

    /**
     * Get an event profile by name.
     *
     * @param profileName Name of the event profile
     * @return Event profile
     */
    public EventProfile getEventProfile(String profileName) {

        try {
            org.wso2.carbon.identity.webhook.metadata.api.model.EventProfile eventProfile =
                    WebhookMetadataServiceHolder.getWebhookMetadataService().getEventProfile(profileName);
            if (eventProfile == null) {
                throw WebhookMetadataAPIErrorBuilder.buildAPIError(Response.Status.NOT_FOUND,
                        ERROR_CODE_PROFILE_NOT_FOUND, profileName);
            }
            return mapEventProfile(eventProfile);
        } catch (WebhookMetadataException e) {
            throw WebhookMetadataAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Get Webhook metadata which includes all the event profiles and the active adapter.
     *
     * @return Webhook metadata containing event profiles and active adapter
     */
    public WebhookMetadata getWebhookMetadata() {

        try {
            List<org.wso2.carbon.identity.webhook.metadata.api.model.EventProfile> eventProfiles =
                    WebhookMetadataServiceHolder.getWebhookMetadataService().getSupportedEventProfiles();
            List<EventProfileMetadata> eventProfileMetadataList = eventProfiles.stream()
                    .map(this::mapEventProfileMetadata)
                    .collect(Collectors.toList());

            org.wso2.carbon.identity.webhook.metadata.api.model.WebhookMetadataProperties webhookMetadataProperties =
                    WebhookMetadataServiceHolder.getWebhookMetadataService().getWebhookMetadataProperties(
                            CarbonContext.getThreadLocalCarbonContext().getTenantDomain());

            Adapter adapter =
                    WebhookMetadataServiceHolder.getEventAdapterMetadataService().getCurrentActiveAdapter();
            WebhookMetadataAdapter webhookMetadataAdapter = mapWebhookMetadataAdapter(adapter);

            WebhookMetadata webhookMetadata = new WebhookMetadata();
            webhookMetadata.setProfiles(eventProfileMetadataList);
            webhookMetadata.setAdapter(webhookMetadataAdapter);
            webhookMetadata.setOrganizationPolicy(mapOrganizationPolicy(webhookMetadataProperties));
            return webhookMetadata;
        } catch (WebhookMetadataException e) {
            throw WebhookMetadataAPIErrorBuilder.buildAPIError(e);
        }
    }

    public WebhookMetadata updateWebhookMetadataProperties(
            WebhookMetadataProperties webhookMetadataProperties) {

        try {
            WebhookMetadataServiceHolder.getWebhookMetadataService()
                    .updateWebhookMetadataProperties(mapWebhookMetadataProperties(webhookMetadataProperties),
                            CarbonContext.getThreadLocalCarbonContext()
                                    .getTenantDomain());
            return getWebhookMetadata();
        } catch (WebhookMetadataException e) {
            throw WebhookMetadataAPIErrorBuilder.buildAPIError(e);
        }
    }

    private EventProfile mapEventProfile(org.wso2.carbon.identity.webhook.metadata.api.model.EventProfile profile) {

        EventProfile eventProfile = new EventProfile();
        eventProfile.setProfile(profile.getProfile());
        eventProfile.setUri(profile.getUri());

        List<Channel> channels = profile.getChannels().stream()
                .map(this::mapChannel)
                .collect(Collectors.toList());
        eventProfile.setChannels(channels);

        return eventProfile;
    }

    private Channel mapChannel(org.wso2.carbon.identity.webhook.metadata.api.model.Channel channel) {

        Channel mappedChannel = new Channel();
        mappedChannel.setName(channel.getName());
        mappedChannel.setDescription(channel.getDescription());
        mappedChannel.setUri(channel.getUri());

        List<Event> events = channel.getEvents().stream()
                .map(this::mapEvent)
                .collect(Collectors.toList());
        mappedChannel.setEvents(events);

        return mappedChannel;
    }

    private Event mapEvent(org.wso2.carbon.identity.webhook.metadata.api.model.Event event) {

        Event mappedEvent = new Event();
        mappedEvent.setEventName(event.getEventName());
        mappedEvent.setEventDescription(event.getEventDescription());
        mappedEvent.setEventUri(event.getEventUri());
        return mappedEvent;
    }

    private EventProfileMetadata mapEventProfileMetadata(
            org.wso2.carbon.identity.webhook.metadata.api.model.EventProfile eventProfile) {

        EventProfileMetadata mappedMetadata = new EventProfileMetadata();
        mappedMetadata.setName(eventProfile.getProfile());
        mappedMetadata.setUri(eventProfile.getUri());
        mappedMetadata.setSelf(
                ContextLoader.buildURIForBody(
                        String.format(V1_API_PATH_COMPONENT + WEBHOOK_METADATA_PATH_COMPONENT +
                                EVENT_PROFILE_PATH_COMPONENT + "/%s", eventProfile.getProfile())).toString());
        return mappedMetadata;
    }

    private WebhookMetadataAdapter mapWebhookMetadataAdapter(Adapter adapter) {

        WebhookMetadataAdapter webhookMetadataAdapter = new WebhookMetadataAdapter();
        webhookMetadataAdapter.setName(adapter.getName());
        webhookMetadataAdapter.setType(adapter.getType().toString());
        return webhookMetadataAdapter;
    }

    private WebhookMetadataOrganizationPolicy mapOrganizationPolicy(
            org.wso2.carbon.identity.webhook.metadata.api.model.WebhookMetadataProperties properties) {

        WebhookMetadataOrganizationPolicy organizationPolicy = new WebhookMetadataOrganizationPolicy();
        organizationPolicy.setPolicyName(
                WebhookMetadataOrganizationPolicy.PolicyNameEnum.fromValue(
                        properties.getOrganizationPolicy().getPolicyValue()
                )
        );
        return organizationPolicy;
    }

    private org.wso2.carbon.identity.webhook.metadata.api.model.WebhookMetadataProperties mapWebhookMetadataProperties(
            WebhookMetadataProperties apiProperties) {

        OrganizationPolicy organizationPolicy = new OrganizationPolicy(
                PolicyEnum.getPolicyByValue(apiProperties.getOrganizationPolicy().getPolicyName().value()));

        return new org.wso2.carbon.identity.webhook.metadata.api.model.WebhookMetadataProperties.Builder()
                .organizationPolicy(organizationPolicy)
                .build();
    }
}
