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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.configs.v1.model.DeviceRegistrationNotificationChannel;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PushDeviceMgtConfig  {
  
    private Boolean enableMultipleDeviceEnrollment;
    private Integer maximumDeviceLimit;
    private Boolean enableDeviceRegistrationNotifications;
    private List<DeviceRegistrationNotificationChannel> deviceRegistrationNotificationChannels = null;


    /**
    * Whether to enable push device management related features.
    **/
    public PushDeviceMgtConfig enableMultipleDeviceEnrollment(Boolean enableMultipleDeviceEnrollment) {

        this.enableMultipleDeviceEnrollment = enableMultipleDeviceEnrollment;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Whether to enable push device management related features.")
    @JsonProperty("enableMultipleDeviceEnrollment")
    @Valid
    public Boolean getEnableMultipleDeviceEnrollment() {
        return enableMultipleDeviceEnrollment;
    }
    public void setEnableMultipleDeviceEnrollment(Boolean enableMultipleDeviceEnrollment) {
        this.enableMultipleDeviceEnrollment = enableMultipleDeviceEnrollment;
    }

    /**
    * Maximum number of devices a user can enroll. This property becomes only applicable if the enableMultipleDeviceEnrollment config is set to true.
    **/
    public PushDeviceMgtConfig maximumDeviceLimit(Integer maximumDeviceLimit) {

        this.maximumDeviceLimit = maximumDeviceLimit;
        return this;
    }
    
    @ApiModelProperty(example = "5", value = "Maximum number of devices a user can enroll. This property becomes only applicable if the enableMultipleDeviceEnrollment config is set to true.")
    @JsonProperty("maximumDeviceLimit")
    @Valid
    public Integer getMaximumDeviceLimit() {
        return maximumDeviceLimit;
    }
    public void setMaximumDeviceLimit(Integer maximumDeviceLimit) {
        this.maximumDeviceLimit = maximumDeviceLimit;
    }

    /**
    * Whether to notify the user when a new device is registered. The channels used to deliver the notification are configured via deviceRegistrationNotificationChannels.
    **/
    public PushDeviceMgtConfig enableDeviceRegistrationNotifications(Boolean enableDeviceRegistrationNotifications) {

        this.enableDeviceRegistrationNotifications = enableDeviceRegistrationNotifications;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Whether to notify the user when a new device is registered. The channels used to deliver the notification are configured via deviceRegistrationNotificationChannels.")
    @JsonProperty("enableDeviceRegistrationNotifications")
    @Valid
    public Boolean getEnableDeviceRegistrationNotifications() {
        return enableDeviceRegistrationNotifications;
    }
    public void setEnableDeviceRegistrationNotifications(Boolean enableDeviceRegistrationNotifications) {
        this.enableDeviceRegistrationNotifications = enableDeviceRegistrationNotifications;
    }

    /**
    * Channels used to deliver the device registration notification. Both channels can be selected to deliver notifications over email and to the user&#39;s existing devices simultaneously.
    **/
    public PushDeviceMgtConfig deviceRegistrationNotificationChannels(List<DeviceRegistrationNotificationChannel> deviceRegistrationNotificationChannels) {

        this.deviceRegistrationNotificationChannels = deviceRegistrationNotificationChannels;
        return this;
    }
    
    @ApiModelProperty(example = "[\"EMAIL\",\"PUSH_NOTIFICATION\"]", value = "Channels used to deliver the device registration notification. Both channels can be selected to deliver notifications over email and to the user's existing devices simultaneously.")
    @JsonProperty("deviceRegistrationNotificationChannels")
    @Valid
    public List<DeviceRegistrationNotificationChannel> getDeviceRegistrationNotificationChannels() {
        return deviceRegistrationNotificationChannels;
    }
    public void setDeviceRegistrationNotificationChannels(List<DeviceRegistrationNotificationChannel> deviceRegistrationNotificationChannels) {
        this.deviceRegistrationNotificationChannels = deviceRegistrationNotificationChannels;
    }

    public PushDeviceMgtConfig addDeviceRegistrationNotificationChannelsItem(DeviceRegistrationNotificationChannel deviceRegistrationNotificationChannelsItem) {
        if (this.deviceRegistrationNotificationChannels == null) {
            this.deviceRegistrationNotificationChannels = new ArrayList<>();
        }
        this.deviceRegistrationNotificationChannels.add(deviceRegistrationNotificationChannelsItem);
        return this;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PushDeviceMgtConfig pushDeviceMgtConfig = (PushDeviceMgtConfig) o;
        return Objects.equals(this.enableMultipleDeviceEnrollment, pushDeviceMgtConfig.enableMultipleDeviceEnrollment) &&
            Objects.equals(this.maximumDeviceLimit, pushDeviceMgtConfig.maximumDeviceLimit) &&
            Objects.equals(this.enableDeviceRegistrationNotifications, pushDeviceMgtConfig.enableDeviceRegistrationNotifications) &&
            Objects.equals(this.deviceRegistrationNotificationChannels, pushDeviceMgtConfig.deviceRegistrationNotificationChannels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enableMultipleDeviceEnrollment, maximumDeviceLimit, enableDeviceRegistrationNotifications, deviceRegistrationNotificationChannels);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PushDeviceMgtConfig {\n");
        
        sb.append("    enableMultipleDeviceEnrollment: ").append(toIndentedString(enableMultipleDeviceEnrollment)).append("\n");
        sb.append("    maximumDeviceLimit: ").append(toIndentedString(maximumDeviceLimit)).append("\n");
        sb.append("    enableDeviceRegistrationNotifications: ").append(toIndentedString(enableDeviceRegistrationNotifications)).append("\n");
        sb.append("    deviceRegistrationNotificationChannels: ").append(toIndentedString(deviceRegistrationNotificationChannels)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}

