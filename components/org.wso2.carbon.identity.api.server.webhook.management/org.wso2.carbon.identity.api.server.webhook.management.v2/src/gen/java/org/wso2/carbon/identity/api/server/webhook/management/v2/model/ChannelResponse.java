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
 * A single subscribed channel of a webhook, with its subscription status and child organization fanout policy.  Carries &#x60;childOrgSubscription.organizationsRef&#x60; — the link to the paginated collection of child organizations receiving events. The webhook representation omits that link and offers &#x60;self&#x60; on each channel instead, so navigation proceeds one level at a time: webhook, then channel, then organizations. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;
@ApiModel(description = "A single subscribed channel of a webhook, with its subscription status and child organization fanout policy.  Carries `childOrgSubscription.organizationsRef` — the link to the paginated collection of child organizations receiving events. The webhook representation omits that link and offers `self` on each channel instead, so navigation proceeds one level at a time: webhook, then channel, then organizations. ")
public class ChannelResponse  {
  
    private String channelId;
    private String channelUri;

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
    private ChildOrgSubscription childOrgSubscription;

    /**
    * Unique identifier of this subscribed channel.
    **/
    public ChannelResponse channelId(String channelId) {

        this.channelId = channelId;
        return this;
    }
    
    @ApiModelProperty(example = "2b6e5a7d-4c88-4e01-9a73-0f5cb8e3d219", value = "Unique identifier of this subscribed channel.")
    @JsonProperty("channelId")
    @Valid
    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
    * Channel URI.
    **/
    public ChannelResponse channelUri(String channelUri) {

        this.channelUri = channelUri;
        return this;
    }
    
    @ApiModelProperty(example = "https://schemas.identity.wso2.org/events/token", value = "Channel URI.")
    @JsonProperty("channelUri")
    @Valid
    public String getChannelUri() {
        return channelUri;
    }
    public void setChannelUri(String channelUri) {
        this.channelUri = channelUri;
    }

    /**
    * Status of the subscription.
    **/
    public ChannelResponse status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "SUBSCRIPTION_ACCEPTED", value = "Status of the subscription.")
    @JsonProperty("status")
    @Valid
    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
    **/
    public ChannelResponse childOrgSubscription(ChildOrgSubscription childOrgSubscription) {

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



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChannelResponse channelResponse = (ChannelResponse) o;
        return Objects.equals(this.channelId, channelResponse.channelId) &&
            Objects.equals(this.channelUri, channelResponse.channelUri) &&
            Objects.equals(this.status, channelResponse.status) &&
            Objects.equals(this.childOrgSubscription, channelResponse.childOrgSubscription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelId, channelUri, status, childOrgSubscription);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ChannelResponse {\n");
        
        sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
        sb.append("    channelUri: ").append(toIndentedString(channelUri)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    childOrgSubscription: ").append(toIndentedString(childOrgSubscription)).append("\n");
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

