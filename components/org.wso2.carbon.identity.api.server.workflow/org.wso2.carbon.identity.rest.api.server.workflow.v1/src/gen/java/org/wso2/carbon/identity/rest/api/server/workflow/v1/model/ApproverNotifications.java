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

package org.wso2.carbon.identity.rest.api.server.workflow.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ApproverNotifications  {
  
    private List<String> channels = null;

    private List<String> events = null;


    /**
    * Notification channels to be used for approvers (e.g., email, sms)
    **/
    public ApproverNotifications channels(List<String> channels) {

        this.channels = channels;
        return this;
    }
    
    @ApiModelProperty(example = "[\"email\",\"sms\"]", value = "Notification channels to be used for approvers (e.g., email, sms)")
    @JsonProperty("channels")
    @Valid
    public List<String> getChannels() {
        return channels;
    }
    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public ApproverNotifications addChannelsItem(String channelsItem) {
        if (this.channels == null) {
            this.channels = new ArrayList<>();
        }
        this.channels.add(channelsItem);
        return this;
    }

        /**
    * Events that trigger notifications for approvers (e.g., onAssignment, onRelease)
    **/
    public ApproverNotifications events(List<String> events) {

        this.events = events;
        return this;
    }
    
    @ApiModelProperty(example = "[\"onAssignment\",\"onRelease\"]", value = "Events that trigger notifications for approvers (e.g., onAssignment, onRelease)")
    @JsonProperty("events")
    @Valid
    public List<String> getEvents() {
        return events;
    }
    public void setEvents(List<String> events) {
        this.events = events;
    }

    public ApproverNotifications addEventsItem(String eventsItem) {
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
        ApproverNotifications approverNotifications = (ApproverNotifications) o;
        return Objects.equals(this.channels, approverNotifications.channels) &&
            Objects.equals(this.events, approverNotifications.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channels, events);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ApproverNotifications {\n");
        
        sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
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

