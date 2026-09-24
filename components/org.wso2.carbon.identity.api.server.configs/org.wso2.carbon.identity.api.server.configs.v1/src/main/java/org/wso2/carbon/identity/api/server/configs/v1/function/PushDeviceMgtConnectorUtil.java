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

package org.wso2.carbon.identity.api.server.configs.v1.function;

import org.wso2.carbon.identity.api.server.configs.v1.model.DeviceRegistrationNotificationChannel;
import org.wso2.carbon.identity.api.server.configs.v1.model.PushDeviceMgtConfig;
import org.wso2.carbon.identity.notification.push.device.handler.model.DeviceRegistrationNotificationChannelEnum;
import org.wso2.carbon.identity.notification.push.device.handler.model.PushDeviceMgtConfigData;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for converting between the API-layer push device management model and the
 * push-device-handler model.
 */
public class PushDeviceMgtConnectorUtil {

    private PushDeviceMgtConnectorUtil() {
    }

    /**
     * Convert a push-device-handler {@link PushDeviceMgtConfigData} to the API-layer
     * {@link PushDeviceMgtConfig}.
     *
     * @param handlerConfig the push-device-handler model; may be null.
     * @return the API model; never null.
     */
    public static PushDeviceMgtConfig buildPushDeviceMgtConfig(PushDeviceMgtConfigData handlerConfig) {

        if (handlerConfig == null) {
            return null;
        }
        PushDeviceMgtConfig pushDeviceMgtConfig = new PushDeviceMgtConfig();
        pushDeviceMgtConfig.setEnableMultipleDeviceEnrollment(handlerConfig.getEnableMultipleDeviceEnrollment());
        pushDeviceMgtConfig.setMaximumDeviceLimit(handlerConfig.getMaximumDeviceLimit());
        pushDeviceMgtConfig.setEnableDeviceRegistrationNotifications(
                handlerConfig.getEnableDeviceRegistrationNotifications());
        pushDeviceMgtConfig.setDeviceRegistrationNotificationChannels(
                buildDeviceRegistrationNotificationChannel(handlerConfig.getDeviceRegistrationNotificationChannels()));
        return pushDeviceMgtConfig;
    }

    /**
     * Convert an API-layer {@link PushDeviceMgtConfig} to the push-device-handler
     * {@link PushDeviceMgtConfigData}.
     *
     * @param pushDeviceMgtConfig the API layer model; may be null.
     * @return the push-device-handler model; never null.
     */
    public static PushDeviceMgtConfigData buildPushDeviceMgtConfigData(PushDeviceMgtConfig pushDeviceMgtConfig) {

        PushDeviceMgtConfigData handlerConfig = new PushDeviceMgtConfigData();
        if (pushDeviceMgtConfig == null) {
            return handlerConfig;
        }
        handlerConfig.setEnableMultipleDeviceEnrollment(pushDeviceMgtConfig.getEnableMultipleDeviceEnrollment());
        handlerConfig.setMaximumDeviceLimit(pushDeviceMgtConfig.getMaximumDeviceLimit());
        handlerConfig.setEnableDeviceRegistrationNotifications(
                pushDeviceMgtConfig.getEnableDeviceRegistrationNotifications());
        handlerConfig.setDeviceRegistrationNotificationChannels(buildDeviceRegistrationNotificationChannel(
                pushDeviceMgtConfig.getDeviceRegistrationNotificationChannels()));
        return handlerConfig;
    }

    private static Set<DeviceRegistrationNotificationChannelEnum> buildDeviceRegistrationNotificationChannel(
            List<DeviceRegistrationNotificationChannel> channels) {

        if (channels == null) {
            return null;
        }
        Set<DeviceRegistrationNotificationChannelEnum> result =
                EnumSet.noneOf(DeviceRegistrationNotificationChannelEnum.class);
        for (DeviceRegistrationNotificationChannel channel : channels) {
            if (channel != null) {
                result.add(DeviceRegistrationNotificationChannelEnum.valueOf(channel.value()));
            }
        }
        return result;
    }

    private static List<DeviceRegistrationNotificationChannel> buildDeviceRegistrationNotificationChannel(
            Set<DeviceRegistrationNotificationChannelEnum> channels) {

        if (channels == null) {
            return null;
        }
        return channels.stream()
                .map(channel -> DeviceRegistrationNotificationChannel.fromValue(channel.name()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
