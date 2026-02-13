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

package org.wso2.carbon.identity.api.server.workflow.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Constants for workflow notification channels and events.
 */
public class NotificationConstants {

    // Pre-computed sets auto-generated from enum values for efficient validation
    private static final Set<String> VALID_CHANNELS;
    private static final Set<String> VALID_INITIATOR_EVENTS;
    private static final Set<String> VALID_APPROVER_EVENTS;

    static {
        // Build channel set from enum
        Set<String> channels = new HashSet<>();
        for (Channel channel : Channel.values()) {
            channels.add(channel.getValue().toLowerCase(Locale.ROOT));
        }
        VALID_CHANNELS = Collections.unmodifiableSet(channels);

        // Build initiator event set from enum
        Set<String> initiatorEvents = new HashSet<>();
        for (InitiatorEvent event : InitiatorEvent.values()) {
            initiatorEvents.add(event.getValue());
        }
        VALID_INITIATOR_EVENTS = Collections.unmodifiableSet(initiatorEvents);

        // Build approver event set from enum
        Set<String> approverEvents = new HashSet<>();
        for (ApproverEvent event : ApproverEvent.values()) {
            approverEvents.add(event.getValue());
        }
        VALID_APPROVER_EVENTS = Collections.unmodifiableSet(approverEvents);
    }
    private NotificationConstants() {
        // Private constructor to prevent instantiation
    }

    /**
     * Validate if the given channel is supported.
     *
     * @param channel Channel to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidChannel(String channel) {
        return channel != null && VALID_CHANNELS.contains(channel.toLowerCase(Locale.ROOT));
    }

    /**
     * Validate if the given event is supported for initiator.
     *
     * @param event Event to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidInitiatorEvent(String event) {
        return event != null && VALID_INITIATOR_EVENTS.contains(event);
    }

    /**
     * Validate if the given event is supported for approver.
     *
     * @param event Event to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidApproverEvent(String event) {
        return event != null && VALID_APPROVER_EVENTS.contains(event);
    }

    /**
     * Get all supported channels.
     *
     * @return Set of valid channel names
     */
    public static Set<String> getSupportedChannels() {
        return VALID_CHANNELS;
    }

    /**
     * Get all supported initiator events.
     *
     * @return Set of valid initiator event names
     */
    public static Set<String> getSupportedInitiatorEvents() {
        return VALID_INITIATOR_EVENTS;
    }

    /**
     * Get all supported approver events.
     *
     * @return Set of valid approver event names
     */
    public static Set<String> getSupportedApproverEvents() {
        return VALID_APPROVER_EVENTS;
    }

    /**
     * Supported notification channels.
     */
    public enum Channel {
        EMAIL("email"),
        SMS("sms");

        private final String value;

        Channel(String value) {
            this.value = value;
        }

        public static Channel fromValue(String value) {
            if (value == null) {
                return null;
            }
            for (Channel channel : values()) {
                if (channel.value.equals(value)) {
                    return channel;
                }
            }
            return null;
        }

        public static boolean isValid(String value) {
            return fromValue(value) != null;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Supported notification events for initiator.
     */
    public enum InitiatorEvent {
        ON_APPROVAL("onApproval"),
        ON_REJECTION("onRejection");

        private final String value;

        InitiatorEvent(String value) {
            this.value = value;
        }

        public static InitiatorEvent fromValue(String value) {
            if (value == null) {
                return null;
            }
            for (InitiatorEvent event : values()) {
                if (event.value.equals(value)) {
                    return event;
                }
            }
            return null;
        }

        public static boolean isValid(String value) {
            return fromValue(value) != null;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Supported notification events for approver.
     */
    public enum ApproverEvent {
        ON_ASSIGNMENT("onAssignment"),
        ON_RELEASE("onRelease");

        private final String value;

        ApproverEvent(String value) {
            this.value = value;
        }

        public static ApproverEvent fromValue(String value) {
            if (value == null) {
                return null;
            }
            for (ApproverEvent event : values()) {
                if (event.value.equals(value)) {
                    return event;
                }
            }
            return null;
        }

        public static boolean isValid(String value) {

            return fromValue(value) != null;
        }

        public String getValue() {

            return value;
        }
    }
}
