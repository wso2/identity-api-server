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

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.webhook.metadata.common.WebhookMetadataServiceHolder;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.Channel;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.Event;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.EventProfile;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.EventProfileMetadata;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.util.WebhookMetadataAPIErrorBuilder;
import org.wso2.carbon.identity.webhook.metadata.api.exception.WebhookMetadataException;

import java.util.List;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.webhook.metadata.v1.constants.WebhookMetadataEndpointConstants.EVENT_PROFILE_PATH_COMPONENT;
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
            return mapEventProfile(eventProfile);
        } catch (WebhookMetadataException e) {
            throw WebhookMetadataAPIErrorBuilder.buildAPIError(e);
        }
    }

    /**
     * Get all event profile names available in the system.
     *
     * @return List of event profile names
     */
    public List<EventProfileMetadata> getEventProfileNames() {

        try {
            return WebhookMetadataServiceHolder.getWebhookMetadataService().getSupportedEventProfiles()
                    .stream()
                    .map(this::mapEventProfileMetadata)
                    .collect(Collectors.toList());
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
            org.wso2.carbon.identity.webhook.metadata.api.model.EventProfileMetadata metadata) {

        EventProfileMetadata mappedMetadata = new EventProfileMetadata();
        mappedMetadata.setName(metadata.getName());
        mappedMetadata.setUri(metadata.getUri());
        mappedMetadata.setSelf(
                ContextLoader.buildURIForBody(
                        String.format(V1_API_PATH_COMPONENT + WEBHOOK_METADATA_PATH_COMPONENT +
                                EVENT_PROFILE_PATH_COMPONENT + "/%s", metadata.getName())).toString());
        return mappedMetadata;
    }
}
