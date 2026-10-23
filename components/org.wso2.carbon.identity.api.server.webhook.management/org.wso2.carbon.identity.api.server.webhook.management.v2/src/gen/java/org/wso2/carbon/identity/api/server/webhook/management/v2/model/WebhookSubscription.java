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
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.ChildOrgSubscription;
import javax.validation.constraints.*;

/**
 * A subscribed channel as it appears on the webhook resource. Used by the create, replace, patch and retrieval operations.  &#x60;channelUri&#x60; and &#x60;childOrgSubscription.subscriptionPolicy&#x60; round-trip between request and response. &#x60;childOrgSubscription.organizations&#x60; is write-only — the organization list is a paginated collection read from the channel resource linked by &#x60;self&#x60;. The remaining properties are observed state, ignored if supplied in a request. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;
@ApiModel(description = "A subscribed channel as it appears on the webhook resource. Used by the create, replace, patch and retrieval operations.  `channelUri` and `childOrgSubscription.subscriptionPolicy` round-trip between request and response. `childOrgSubscription.organizations` is write-only — the organization list is a paginated collection read from the channel resource linked by `self`. The remaining properties are observed state, ignored if supplied in a request. ")
public class WebhookSubscription  {
  
    private String channelUri;
    private ChildOrgSubscription childOrgSubscription;
    private String channelId;
    private String self;

@XmlType(name="StatusEnum")
@XmlEnum(String.class)
public enum StatusEnum {

    @XmlEnumValue("SUBSCRIPTION_ACCEPTED") SUBSCRIPTION_ACCEPTED(String.valueOf("SUBSCRIPTION_ACCEPTED")), @XmlEnumValue("SUBSCRIPTION_PENDING") SUBSCRIPTION_PENDING(String.valueOf("SUBSCRIPTION_PENDING")), @XmlEnumValue("SUBSCRIPTION_ERROR") SUBSCRIPTION_ERROR(String.valueOf("SUBSCRIPTION_ERROR")), @XmlEnumValue("UNSUBSCRIPTION_ACCEPTED") UNSUBSCRIPTION_ACCEPTED(String.valueOf("UNSUBSCRIPTION_ACCEPTED")), @XmlEnumValue("UNSUBSCRIPTION_PENDING") UNSUBSCRIPTION_PENDING(String.valueOf("UNSUBSCRIPTION_PENDING")), @XmlEnumValue("UNSUBSCRIPTION_ERROR") UNSUBSCRIPTION_ERROR(String.valueOf("UNSUBSCRIPTION_ERROR"));


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
    * Channel URI to subscribe to.
    **/
    public WebhookSubscription channelUri(String channelUri) {

        this.channelUri = channelUri;
        return this;
    }
    
    @ApiModelProperty(example = "https://schemas.identity.wso2.org/events/login", required = true, value = "Channel URI to subscribe to.")
    @JsonProperty("channelUri")
    @Valid
    @NotNull(message = "Property channelUri cannot be null.")

    public String getChannelUri() {
        return channelUri;
    }
    public void setChannelUri(String channelUri) {
        this.channelUri = channelUri;
    }

    /**
    **/
    public WebhookSubscription childOrgSubscription(ChildOrgSubscription childOrgSubscription) {

        this.childOrgSubscription = childOrgSubscription;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("childOrgSubscription")
    @Valid
    public ChildOrgSubscription getChildOrgSubscription() {
        return childOrgSubscription;
    }
    public void setChildOrgSubscription(ChildOrgSubscription childOrgSubscription) {
        this.childOrgSubscription = childOrgSubscription;
    }

    /**
    * Unique identifier of this subscribed channel, used to address the channel resource. 
    **/
    public WebhookSubscription channelId(String channelId) {

        this.channelId = channelId;
        return this;
    }
    
    @ApiModelProperty(example = "9f2c7b31-0a4e-4d8f-9c11-2b6e5a7d4c88", value = "Unique identifier of this subscribed channel, used to address the channel resource. ")
    @JsonProperty("channelId")
    @Valid
    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
    * Link to this subscribed channel. Follow it to reach the channel resource, which carries &#x60;childOrgSubscription.organizationsRef&#x60; — the link to the child organizations receiving events. Each representation links one level down, so the webhook response stays free of nested organization links. 
    **/
    public WebhookSubscription self(String self) {

        this.self = self;
        return this;
    }
    
    @ApiModelProperty(example = "/t/carbon.super/api/server/v2/webhooks/eeb8c1a2-3f4d-4e5b-8c6f-7d8e9f0a1b2c/channels/9f2c7b31-0a4e-4d8f-9c11-2b6e5a7d4c88", value = "Link to this subscribed channel. Follow it to reach the channel resource, which carries `childOrgSubscription.organizationsRef` — the link to the child organizations receiving events. Each representation links one level down, so the webhook response stays free of nested organization links. ")
    @JsonProperty("self")
    @Valid
    public String getSelf() {
        return self;
    }
    public void setSelf(String self) {
        this.self = self;
    }

    /**
    * Status of the subscription.
    **/
    public WebhookSubscription status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "SUBSCRIPTION_PENDING", value = "Status of the subscription.")
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
        WebhookSubscription webhookSubscription = (WebhookSubscription) o;
        return Objects.equals(this.channelUri, webhookSubscription.channelUri) &&
            Objects.equals(this.childOrgSubscription, webhookSubscription.childOrgSubscription) &&
            Objects.equals(this.channelId, webhookSubscription.channelId) &&
            Objects.equals(this.self, webhookSubscription.self) &&
            Objects.equals(this.status, webhookSubscription.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelUri, childOrgSubscription, channelId, self, status);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WebhookSubscription {\n");
        
        sb.append("    channelUri: ").append(toIndentedString(channelUri)).append("\n");
        sb.append("    childOrgSubscription: ").append(toIndentedString(childOrgSubscription)).append("\n");
        sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
        sb.append("    self: ").append(toIndentedString(self)).append("\n");
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

