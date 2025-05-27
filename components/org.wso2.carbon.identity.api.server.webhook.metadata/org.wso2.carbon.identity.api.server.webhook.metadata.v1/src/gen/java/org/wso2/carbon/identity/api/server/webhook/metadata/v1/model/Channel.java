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

package org.wso2.carbon.identity.api.server.webhook.metadata.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.Event;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class Channel  {
  
    private String name;
    private String description;
    private String uri;
    private List<Event> events = null;


    /**
    **/
    public Channel name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "analytics", value = "")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public Channel description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Events related to user behavior analytics.", value = "")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    **/
    public Channel uri(String uri) {

        this.uri = uri;
        return this;
    }
    
    @ApiModelProperty(example = "https://schemas.identity.wso2.org/channels/analytics", value = "")
    @JsonProperty("uri")
    @Valid
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
    **/
    public Channel events(List<Event> events) {

        this.events = events;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("events")
    @Valid
    public List<Event> getEvents() {
        return events;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Channel addEventsItem(Event eventsItem) {
        if (this.events == null) {
            this.events = new ArrayList<Event>();
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
        Channel channel = (Channel) o;
        return Objects.equals(this.name, channel.name) &&
            Objects.equals(this.description, channel.description) &&
            Objects.equals(this.uri, channel.uri) &&
            Objects.equals(this.events, channel.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, uri, events);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Channel {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
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

