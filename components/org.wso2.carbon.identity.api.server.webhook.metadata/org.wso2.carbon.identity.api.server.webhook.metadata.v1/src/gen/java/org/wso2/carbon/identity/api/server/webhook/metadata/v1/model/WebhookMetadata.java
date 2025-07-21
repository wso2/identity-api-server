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
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.EventProfileMetadata;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.WebhookMetadataAdapter;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class WebhookMetadata  {
  
    private List<EventProfileMetadata> profiles = null;

    private WebhookMetadataAdapter adapter;

    /**
    **/
    public WebhookMetadata profiles(List<EventProfileMetadata> profiles) {

        this.profiles = profiles;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("profiles")
    @Valid
    public List<EventProfileMetadata> getProfiles() {
        return profiles;
    }
    public void setProfiles(List<EventProfileMetadata> profiles) {
        this.profiles = profiles;
    }

    public WebhookMetadata addProfilesItem(EventProfileMetadata profilesItem) {
        if (this.profiles == null) {
            this.profiles = new ArrayList<EventProfileMetadata>();
        }
        this.profiles.add(profilesItem);
        return this;
    }

        /**
    **/
    public WebhookMetadata adapter(WebhookMetadataAdapter adapter) {

        this.adapter = adapter;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("adapter")
    @Valid
    public WebhookMetadataAdapter getAdapter() {
        return adapter;
    }
    public void setAdapter(WebhookMetadataAdapter adapter) {
        this.adapter = adapter;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebhookMetadata webhookMetadata = (WebhookMetadata) o;
        return Objects.equals(this.profiles, webhookMetadata.profiles) &&
            Objects.equals(this.adapter, webhookMetadata.adapter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profiles, adapter);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WebhookMetadata {\n");
        
        sb.append("    profiles: ").append(toIndentedString(profiles)).append("\n");
        sb.append("    adapter: ").append(toIndentedString(adapter)).append("\n");
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

