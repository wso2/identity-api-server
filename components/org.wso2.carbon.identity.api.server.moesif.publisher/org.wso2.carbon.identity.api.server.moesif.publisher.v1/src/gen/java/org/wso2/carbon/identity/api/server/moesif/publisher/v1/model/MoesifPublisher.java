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

package org.wso2.carbon.identity.api.server.moesif.publisher.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class MoesifPublisher  {
  
    private String name;
    private Map<String, Boolean> eventPublisherEnablement = null;
    private Boolean enableAllPublishers;


    /**
    **/
    public MoesifPublisher name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "default", value = "")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Map of event publisher key to current enabled flag.
    **/
    public MoesifPublisher eventPublisherEnablement(Map<String, Boolean> eventPublisherEnablement) {

        this.eventPublisherEnablement = eventPublisherEnablement;
        return this;
    }
    
    @ApiModelProperty(example = "{\"authentication\":true,\"registration\":false,\"flow\":true,\"oauthToken\":true}", value = "Map of event publisher key to current enabled flag.")
    @JsonProperty("eventPublisherEnablement")
    @Valid
    public Map<String, Boolean> getEventPublisherEnablement() {
        return eventPublisherEnablement;
    }
    public void setEventPublisherEnablement(Map<String, Boolean> eventPublisherEnablement) {
        this.eventPublisherEnablement = eventPublisherEnablement;
    }


    public MoesifPublisher putEventPublisherEnablementItem(String key, Boolean eventPublisherEnablementItem) {
        if (this.eventPublisherEnablement == null) {
            this.eventPublisherEnablement = new HashMap<>();
        }
        this.eventPublisherEnablement.put(key, eventPublisherEnablementItem);
        return this;
    }

    

    /**
    * When true, all supported publishers are enabled regardless of eventPublisherEnablement.
    **/
    public MoesifPublisher enableAllPublishers(Boolean enableAllPublishers) {

        this.enableAllPublishers = enableAllPublishers;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "When true, all supported publishers are enabled regardless of eventPublisherEnablement.")
    @JsonProperty("enableAllPublishers")
    @Valid
    public Boolean getEnableAllPublishers() {
        return enableAllPublishers;
    }
    public void setEnableAllPublishers(Boolean enableAllPublishers) {
        this.enableAllPublishers = enableAllPublishers;
    }


    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoesifPublisher moesifPublisher = (MoesifPublisher) o;
        return Objects.equals(this.name, moesifPublisher.name) &&
            Objects.equals(this.eventPublisherEnablement, moesifPublisher.eventPublisherEnablement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, eventPublisherEnablement);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MoesifPublisher {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    eventPublisherEnablement: ").append(toIndentedString(eventPublisherEnablement)).append("\n");
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

