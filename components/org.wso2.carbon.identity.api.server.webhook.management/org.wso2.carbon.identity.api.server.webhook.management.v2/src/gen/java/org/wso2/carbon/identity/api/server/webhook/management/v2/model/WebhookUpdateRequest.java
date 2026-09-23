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

package org.wso2.carbon.identity.api.server.webhook.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.EventProfile;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookSubscription;
import javax.validation.constraints.*;

/**
 * Full replacement of a webhook, including per-channel organization fanout. This is the retrieval representation minus the read-only properties, so a retrieved webhook can be modified and sent back as-is.  The two write-only properties — &#x60;secret&#x60; and &#x60;subscribedChannels[].childOrgSubscription.organizations&#x60; — are never returned and are therefore preserved when omitted rather than cleared. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;
@ApiModel(description = "Full replacement of a webhook, including per-channel organization fanout. This is the retrieval representation minus the read-only properties, so a retrieved webhook can be modified and sent back as-is.  The two write-only properties — `secret` and `subscribedChannels[].childOrgSubscription.organizations` — are never returned and are therefore preserved when omitted rather than cleared. ")
public class WebhookUpdateRequest  {
  
    private String endpoint;
    private EventProfile eventProfile;
    private String name;
    private String secret;
    private List<WebhookSubscription> subscribedChannels = new ArrayList<WebhookSubscription>();


    /**
    * Webhook URL.
    **/
    public WebhookUpdateRequest endpoint(String endpoint) {

        this.endpoint = endpoint;
        return this;
    }
    
    @ApiModelProperty(example = "https://example.com/webhook", required = true, value = "Webhook URL.")
    @JsonProperty("endpoint")
    @Valid
    @NotNull(message = "Property endpoint cannot be null.")

    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
    **/
    public WebhookUpdateRequest eventProfile(EventProfile eventProfile) {

        this.eventProfile = eventProfile;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("eventProfile")
    @Valid
    @NotNull(message = "Property eventProfile cannot be null.")

    public EventProfile getEventProfile() {
        return eventProfile;
    }
    public void setEventProfile(EventProfile eventProfile) {
        this.eventProfile = eventProfile;
    }

    /**
    * Webhook name.
    **/
    public WebhookUpdateRequest name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Login Webhook.", required = true, value = "Webhook name.")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Secret for validating webhook payloads. Never returned by any operation, so omitting it preserves the stored secret rather than clearing it. 
    **/
    public WebhookUpdateRequest secret(String secret) {

        this.secret = secret;
        return this;
    }
    
    @ApiModelProperty(example = "my-secret", value = "Secret for validating webhook payloads. Never returned by any operation, so omitting it preserves the stored secret rather than clearing it. ")
    @JsonProperty("secret")
    @Valid
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
    * The channels this webhook should be subscribed to, with their organization fanout. Replaced wholesale: channels absent from the array are unsubscribed, and channels present but not stored are subscribed.  A channel that omits &#x60;childOrgSubscription&#x60; entirely keeps its current policy if it is already subscribed, or defaults to &#x60;NONE&#x60; if it is new. 
    **/
    public WebhookUpdateRequest subscribedChannels(List<WebhookSubscription> subscribedChannels) {

        this.subscribedChannels = subscribedChannels;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"channelUri\":\"https://schemas.identity.wso2.org/events/login\",\"childOrgSubscription\":{\"subscriptionPolicy\":\"NONE\"}},{\"channelUri\":\"https://schemas.identity.wso2.org/events/registration\",\"childOrgSubscription\":{\"subscriptionPolicy\":\"SELECTED_ORGS_ONLY\",\"organizations\":[{\"orgId\":\"b028ca17-8f89-449c-ae27-fa955e66465d\"}]}}]", required = true, value = "The channels this webhook should be subscribed to, with their organization fanout. Replaced wholesale: channels absent from the array are unsubscribed, and channels present but not stored are subscribed.  A channel that omits `childOrgSubscription` entirely keeps its current policy if it is already subscribed, or defaults to `NONE` if it is new. ")
    @JsonProperty("subscribedChannels")
    @Valid
    @NotNull(message = "Property subscribedChannels cannot be null.")
 @Size(min=1)
    public List<WebhookSubscription> getSubscribedChannels() {
        return subscribedChannels;
    }
    public void setSubscribedChannels(List<WebhookSubscription> subscribedChannels) {
        this.subscribedChannels = subscribedChannels;
    }

    public WebhookUpdateRequest addSubscribedChannelsItem(WebhookSubscription subscribedChannelsItem) {
        this.subscribedChannels.add(subscribedChannelsItem);
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
        WebhookUpdateRequest webhookUpdateRequest = (WebhookUpdateRequest) o;
        return Objects.equals(this.endpoint, webhookUpdateRequest.endpoint) &&
            Objects.equals(this.eventProfile, webhookUpdateRequest.eventProfile) &&
            Objects.equals(this.name, webhookUpdateRequest.name) &&
            Objects.equals(this.secret, webhookUpdateRequest.secret) &&
            Objects.equals(this.subscribedChannels, webhookUpdateRequest.subscribedChannels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpoint, eventProfile, name, secret, subscribedChannels);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WebhookUpdateRequest {\n");
        
        sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
        sb.append("    eventProfile: ").append(toIndentedString(eventProfile)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
        sb.append("    subscribedChannels: ").append(toIndentedString(subscribedChannels)).append("\n");
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

