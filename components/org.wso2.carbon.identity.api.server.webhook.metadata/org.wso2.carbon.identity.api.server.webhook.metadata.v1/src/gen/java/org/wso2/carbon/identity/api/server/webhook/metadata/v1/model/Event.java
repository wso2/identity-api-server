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
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class Event  {
  
    private String eventName;
    private String eventDescription;
    private String eventUri;

    /**
    **/
    public Event eventName(String eventName) {

        this.eventName = eventName;
        return this;
    }
    
    @ApiModelProperty(example = "loginSuccess", value = "")
    @JsonProperty("eventName")
    @Valid
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
    **/
    public Event eventDescription(String eventDescription) {

        this.eventDescription = eventDescription;
        return this;
    }
    
    @ApiModelProperty(example = "Triggered when a user logs in successfully.", value = "")
    @JsonProperty("eventDescription")
    @Valid
    public String getEventDescription() {
        return eventDescription;
    }
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
    **/
    public Event eventUri(String eventUri) {

        this.eventUri = eventUri;
        return this;
    }
    
    @ApiModelProperty(example = "https://schemas.identity.wso2.org/events/logins/loginSuccess", value = "")
    @JsonProperty("eventUri")
    @Valid
    public String getEventUri() {
        return eventUri;
    }
    public void setEventUri(String eventUri) {
        this.eventUri = eventUri;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return Objects.equals(this.eventName, event.eventName) &&
            Objects.equals(this.eventDescription, event.eventDescription) &&
            Objects.equals(this.eventUri, event.eventUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventName, eventDescription, eventUri);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Event {\n");
        
        sb.append("    eventName: ").append(toIndentedString(eventName)).append("\n");
        sb.append("    eventDescription: ").append(toIndentedString(eventDescription)).append("\n");
        sb.append("    eventUri: ").append(toIndentedString(eventUri)).append("\n");
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

