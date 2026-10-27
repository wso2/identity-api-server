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
 * Webhook to create. Organization fanout may be supplied inline per channel so that the webhook and its fanout are created atomically; afterwards it is managed through the channel resource. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;
@ApiModel(description = "Webhook to create. Organization fanout may be supplied inline per channel so that the webhook and its fanout are created atomically; afterwards it is managed through the channel resource. ")
public class WebhookCreateRequest  {
  
    private String endpoint;
    private EventProfile eventProfile;
    private String name;
    private String secret;
    private List<WebhookSubscription> subscribedChannels = new ArrayList<WebhookSubscription>();


@XmlType(name="StatusEnum")
@XmlEnum(String.class)
public enum StatusEnum {

    @XmlEnumValue("ACTIVE") ACTIVE(String.valueOf("ACTIVE")), @XmlEnumValue("INACTIVE") INACTIVE(String.valueOf("INACTIVE"));


    private String value;

    StatusEnum(String v) {
        value = v;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static StatusEnum fromValue(String value) {
        for (StatusEnum b : StatusEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private StatusEnum status;

    /**
    * Webhook URL.
    **/
    public WebhookCreateRequest endpoint(String endpoint) {

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
    public WebhookCreateRequest eventProfile(EventProfile eventProfile) {

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
    public WebhookCreateRequest name(String name) {

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
    * Secret used to sign webhook payloads, so the receiver can verify them. Never returned by any operation.  Whether it applies depends on the event profile, which determines how payloads are signed. Profiles that sign with a shared secret require it; profiles that sign with a key pair do not. 
    **/
    public WebhookCreateRequest secret(String secret) {

        this.secret = secret;
        return this;
    }
    
    @ApiModelProperty(example = "my-secret", value = "Secret used to sign webhook payloads, so the receiver can verify them. Never returned by any operation.  Whether it applies depends on the event profile, which determines how payloads are signed. Profiles that sign with a shared secret require it; profiles that sign with a key pair do not. ")
    @JsonProperty("secret")
    @Valid
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
    * Channels to subscribe to, each with optional organization fanout.
    **/
    public WebhookCreateRequest subscribedChannels(List<WebhookSubscription> subscribedChannels) {

        this.subscribedChannels = subscribedChannels;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"channelUri\":\"https://schemas.identity.wso2.org/events/login\",\"childOrgSubscription\":{\"subscriptionPolicy\":\"NONE\"}},{\"channelUri\":\"https://schemas.identity.wso2.org/events/registration\",\"childOrgSubscription\":{\"subscriptionPolicy\":\"ALL_EXISTING_AND_FUTURE_ORGS\"}},{\"channelUri\":\"https://schemas.identity.wso2.org/events/token\",\"childOrgSubscription\":{\"subscriptionPolicy\":\"SELECTED_ORGS_ONLY\",\"organizations\":[{\"orgId\":\"b028ca17-8f89-449c-ae27-fa955e66465d\"},{\"orgId\":\"a17b28ca-9f89-449c-ae27-fa955e66465f\"}]}},{\"channelUri\":\"https://schemas.identity.wso2.org/events/user\"}]", required = true, value = "Channels to subscribe to, each with optional organization fanout.")
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

    public WebhookCreateRequest addSubscribedChannelsItem(WebhookSubscription subscribedChannelsItem) {
        this.subscribedChannels.add(subscribedChannelsItem);
        return this;
    }

        /**
    * Desired webhook status on create. The retrieval response reports the derived aggregate status, which may also be &#x60;PARTIALLY_ACTIVE&#x60; or &#x60;PARTIALLY_INACTIVE&#x60; while channels settle. 
    **/
    public WebhookCreateRequest status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "ACTIVE", value = "Desired webhook status on create. The retrieval response reports the derived aggregate status, which may also be `PARTIALLY_ACTIVE` or `PARTIALLY_INACTIVE` while channels settle. ")
    @JsonProperty("status")
    @Valid
    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebhookCreateRequest webhookCreateRequest = (WebhookCreateRequest) o;
        return Objects.equals(this.endpoint, webhookCreateRequest.endpoint) &&
            Objects.equals(this.eventProfile, webhookCreateRequest.eventProfile) &&
            Objects.equals(this.name, webhookCreateRequest.name) &&
            Objects.equals(this.secret, webhookCreateRequest.secret) &&
            Objects.equals(this.subscribedChannels, webhookCreateRequest.subscribedChannels) &&
            Objects.equals(this.status, webhookCreateRequest.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpoint, eventProfile, name, secret, subscribedChannels, status);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WebhookCreateRequest {\n");
        
        sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
        sb.append("    eventProfile: ").append(toIndentedString(eventProfile)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
        sb.append("    subscribedChannels: ").append(toIndentedString(subscribedChannels)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

