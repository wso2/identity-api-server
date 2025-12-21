/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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
import org.wso2.carbon.identity.api.server.configs.v1.model.EventConfig;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class FraudDetectionConfig  {
  
    private Boolean publishUserInfo;
    private Boolean publishDeviceMetadata;
    private Boolean logRequestPayload;
    private List<EventConfig> events = null;


    /**
    * If true, publish user personal information to the fraud detector.
    **/
    public FraudDetectionConfig publishUserInfo(Boolean publishUserInfo) {

        this.publishUserInfo = publishUserInfo;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "If true, publish user personal information to the fraud detector.")
    @JsonProperty("publishUserInfo")
    @Valid
    public Boolean getPublishUserInfo() {
        return publishUserInfo;
    }
    public void setPublishUserInfo(Boolean publishUserInfo) {
        this.publishUserInfo = publishUserInfo;
    }

    /**
    * If true, publish device related metadata to the fraud detector.
    **/
    public FraudDetectionConfig publishDeviceMetadata(Boolean publishDeviceMetadata) {

        this.publishDeviceMetadata = publishDeviceMetadata;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "If true, publish device related metadata to the fraud detector.")
    @JsonProperty("publishDeviceMetadata")
    @Valid
    public Boolean getPublishDeviceMetadata() {
        return publishDeviceMetadata;
    }
    public void setPublishDeviceMetadata(Boolean publishDeviceMetadata) {
        this.publishDeviceMetadata = publishDeviceMetadata;
    }

    /**
    * If true, log the request payloads sent to the fraud detector.
    **/
    public FraudDetectionConfig logRequestPayload(Boolean logRequestPayload) {

        this.logRequestPayload = logRequestPayload;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "If true, log the request payloads sent to the fraud detector.")
    @JsonProperty("logRequestPayload")
    @Valid
    public Boolean getLogRequestPayload() {
        return logRequestPayload;
    }
    public void setLogRequestPayload(Boolean logRequestPayload) {
        this.logRequestPayload = logRequestPayload;
    }

    /**
    **/
    public FraudDetectionConfig events(List<EventConfig> events) {

        this.events = events;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("events")
    @Valid
    public List<EventConfig> getEvents() {
        return events;
    }
    public void setEvents(List<EventConfig> events) {
        this.events = events;
    }

    public FraudDetectionConfig addEventsItem(EventConfig eventsItem) {
        if (this.events == null) {
            this.events = new ArrayList<>();
        }
        this.events.add(eventsItem);
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
        FraudDetectionConfig fraudDetectionConfig = (FraudDetectionConfig) o;
        return Objects.equals(this.publishUserInfo, fraudDetectionConfig.publishUserInfo) &&
            Objects.equals(this.publishDeviceMetadata, fraudDetectionConfig.publishDeviceMetadata) &&
            Objects.equals(this.logRequestPayload, fraudDetectionConfig.logRequestPayload) &&
            Objects.equals(this.events, fraudDetectionConfig.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publishUserInfo, publishDeviceMetadata, logRequestPayload, events);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FraudDetectionConfig {\n");
        
        sb.append("    publishUserInfo: ").append(toIndentedString(publishUserInfo)).append("\n");
        sb.append("    publishDeviceMetadata: ").append(toIndentedString(publishDeviceMetadata)).append("\n");
        sb.append("    logRequestPayload: ").append(toIndentedString(logRequestPayload)).append("\n");
        sb.append("    events: ").append(toIndentedString(events)).append("\n");
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

