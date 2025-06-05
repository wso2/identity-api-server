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
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.Channel;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class EventProfile  {
  
    private String profile;
    private String uri;
    private List<Channel> channels = null;


    /**
    **/
    public EventProfile profile(String profile) {

        this.profile = profile;
        return this;
    }
    
    @ApiModelProperty(example = "WSO2", value = "")
    @JsonProperty("profile")
    @Valid
    public String getProfile() {
        return profile;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
    **/
    public EventProfile uri(String uri) {

        this.uri = uri;
        return this;
    }
    
    @ApiModelProperty(example = "https://schemas.identity.wso2.org", value = "")
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
    public EventProfile channels(List<Channel> channels) {

        this.channels = channels;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("channels")
    @Valid
    public List<Channel> getChannels() {
        return channels;
    }
    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public EventProfile addChannelsItem(Channel channelsItem) {
        if (this.channels == null) {
            this.channels = new ArrayList<Channel>();
        }
        this.channels.add(channelsItem);
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
        EventProfile eventProfile = (EventProfile) o;
        return Objects.equals(this.profile, eventProfile.profile) &&
            Objects.equals(this.uri, eventProfile.uri) &&
            Objects.equals(this.channels, eventProfile.channels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profile, uri, channels);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class EventProfile {\n");
        
        sb.append("    profile: ").append(toIndentedString(profile)).append("\n");
        sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
        sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
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

