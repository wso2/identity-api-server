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

package org.wso2.carbon.identity.api.server.device.mgt.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class DeviceResponse  {
  
    private String id;
    private String userId;
    private String deviceName;
    private String deviceModel;
    private String status;
    private OffsetDateTime registeredAt;
    private String metadata;

    /**
    * The device UUID.
    **/
    public DeviceResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "74070bae-df8c-42bf-8754-5173c237c936", value = "The device UUID.")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * The user identifier who owns the device.
    **/
    public DeviceResponse userId(String userId) {

        this.userId = userId;
        return this;
    }
    
    @ApiModelProperty(example = "kaviska", value = "The user identifier who owns the device.")
    @JsonProperty("userId")
    @Valid
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
    * The display name of the device.
    **/
    public DeviceResponse deviceName(String deviceName) {

        this.deviceName = deviceName;
        return this;
    }
    
    @ApiModelProperty(example = "My iPhone", value = "The display name of the device.")
    @JsonProperty("deviceName")
    @Valid
    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
    * The hardware model of the device.
    **/
    public DeviceResponse deviceModel(String deviceModel) {

        this.deviceModel = deviceModel;
        return this;
    }
    
    @ApiModelProperty(example = "iPhone 15 Pro", value = "The hardware model of the device.")
    @JsonProperty("deviceModel")
    @Valid
    public String getDeviceModel() {
        return deviceModel;
    }
    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    /**
    * The current device status.
    **/
    public DeviceResponse status(String status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "ACTIVE", value = "The current device status.")
    @JsonProperty("status")
    @Valid
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    /**
    * The timestamp when the device was registered.
    **/
    public DeviceResponse registeredAt(OffsetDateTime registeredAt) {

        this.registeredAt = registeredAt;
        return this;
    }
    
    @ApiModelProperty(example = "2026-04-27T10:00Z", value = "The timestamp when the device was registered.")
    @JsonProperty("registeredAt")
    @Valid
    public OffsetDateTime getRegisteredAt() {
        return registeredAt;
    }
    public void setRegisteredAt(OffsetDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    /**
    * Additional non-sensitive metadata associated with the device.
    **/
    public DeviceResponse metadata(String metadata) {

        this.metadata = metadata;
        return this;
    }
    
    @ApiModelProperty(example = "{\"osVersion\":\"17.0\",\"deviceType\":\"mobile\",\"manufacturer\":\"Apple\"}", value = "Additional non-sensitive metadata associated with the device.")
    @JsonProperty("metadata")
    @Valid
    public String getMetadata() {
        return metadata;
    }
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceResponse deviceResponse = (DeviceResponse) o;
        return Objects.equals(this.id, deviceResponse.id) &&
            Objects.equals(this.userId, deviceResponse.userId) &&
            Objects.equals(this.deviceName, deviceResponse.deviceName) &&
            Objects.equals(this.deviceModel, deviceResponse.deviceModel) &&
            Objects.equals(this.status, deviceResponse.status) &&
            Objects.equals(this.registeredAt, deviceResponse.registeredAt) &&
            Objects.equals(this.metadata, deviceResponse.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, deviceName, deviceModel, status, registeredAt, metadata);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DeviceResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    deviceName: ").append(toIndentedString(deviceName)).append("\n");
        sb.append("    deviceModel: ").append(toIndentedString(deviceModel)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    registeredAt: ").append(toIndentedString(registeredAt)).append("\n");
        sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

