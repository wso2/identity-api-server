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
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.Organization;
import javax.validation.constraints.*;

/**
 * Paginated list of the child organizations currently receiving events for a channel, with the context needed to interpret it.  This is a paginated view of a sub-collection, not a value that can be sent back — &#x60;organizations&#x60; holds only the current page, so echoing it into a webhook write would unsubscribe everything outside that page. Writes take the plain array under &#x60;childOrgSubscription.organizations&#x60;. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;
@ApiModel(description = "Paginated list of the child organizations currently receiving events for a channel, with the context needed to interpret it.  This is a paginated view of a sub-collection, not a value that can be sent back — `organizations` holds only the current page, so echoing it into a webhook write would unsubscribe everything outside that page. Writes take the plain array under `childOrgSubscription.organizations`. ")
public class SubscribedOrganizationList  {
  
    private String channelUri;

@XmlType(name="SubscriptionPolicyEnum")
@XmlEnum(String.class)
public enum SubscriptionPolicyEnum {

    @XmlEnumValue("NONE") NONE(String.valueOf("NONE")), @XmlEnumValue("ALL_EXISTING_AND_FUTURE_ORGS") ALL_EXISTING_AND_FUTURE_ORGS(String.valueOf("ALL_EXISTING_AND_FUTURE_ORGS")), @XmlEnumValue("SELECTED_ORGS_ONLY") SELECTED_ORGS_ONLY(String.valueOf("SELECTED_ORGS_ONLY"));


    private String value;

    SubscriptionPolicyEnum(String v) {
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

    public static SubscriptionPolicyEnum fromValue(String value) {
        for (SubscriptionPolicyEnum b : SubscriptionPolicyEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private SubscriptionPolicyEnum subscriptionPolicy;
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<Organization> organizations = null;


    /**
    * Channel URI this list belongs to.
    **/
    public SubscribedOrganizationList channelUri(String channelUri) {

        this.channelUri = channelUri;
        return this;
    }
    
    @ApiModelProperty(example = "https://schemas.identity.wso2.org/events/registration", value = "Channel URI this list belongs to.")
    @JsonProperty("channelUri")
    @Valid
    public String getChannelUri() {
        return channelUri;
    }
    public void setChannelUri(String channelUri) {
        this.channelUri = channelUri;
    }

    /**
    * Child organization fanout policy configured for this channel, included so the list can be interpreted without a second call. &#x60;NONE&#x60; always yields an empty list. 
    **/
    public SubscribedOrganizationList subscriptionPolicy(SubscriptionPolicyEnum subscriptionPolicy) {

        this.subscriptionPolicy = subscriptionPolicy;
        return this;
    }
    
    @ApiModelProperty(example = "ALL_EXISTING_AND_FUTURE_ORGS", value = "Child organization fanout policy configured for this channel, included so the list can be interpreted without a second call. `NONE` always yields an empty list. ")
    @JsonProperty("subscriptionPolicy")
    @Valid
    public SubscriptionPolicyEnum getSubscriptionPolicy() {
        return subscriptionPolicy;
    }
    public void setSubscriptionPolicy(SubscriptionPolicyEnum subscriptionPolicy) {
        this.subscriptionPolicy = subscriptionPolicy;
    }

    /**
    * Total number of child organizations subscribed to this channel. Under &#x60;ALL_EXISTING_AND_FUTURE_ORGS&#x60; this can change between pages as organizations are created. 
    **/
    public SubscribedOrganizationList totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "142", value = "Total number of child organizations subscribed to this channel. Under `ALL_EXISTING_AND_FUTURE_ORGS` this can change between pages as organizations are created. ")
    @JsonProperty("totalResults")
    @Valid
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
    * Index of the first record in this response.
    **/
    public SubscribedOrganizationList startIndex(Integer startIndex) {

        this.startIndex = startIndex;
        return this;
    }
    
    @ApiModelProperty(example = "0", value = "Index of the first record in this response.")
    @JsonProperty("startIndex")
    @Valid
    public Integer getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    /**
    * Number of records returned in this response.
    **/
    public SubscribedOrganizationList count(Integer count) {

        this.count = count;
        return this;
    }
    
    @ApiModelProperty(example = "2", value = "Number of records returned in this response.")
    @JsonProperty("count")
    @Valid
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
    * The child organizations on this page, ordered parents before children. 
    **/
    public SubscribedOrganizationList organizations(List<Organization> organizations) {

        this.organizations = organizations;
        return this;
    }
    
    @ApiModelProperty(value = "The child organizations on this page, ordered parents before children. ")
    @JsonProperty("organizations")
    @Valid
    public List<Organization> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public SubscribedOrganizationList addOrganizationsItem(Organization organizationsItem) {
        if (this.organizations == null) {
            this.organizations = new ArrayList<Organization>();
        }
        this.organizations.add(organizationsItem);
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
        SubscribedOrganizationList subscribedOrganizationList = (SubscribedOrganizationList) o;
        return Objects.equals(this.channelUri, subscribedOrganizationList.channelUri) &&
            Objects.equals(this.subscriptionPolicy, subscribedOrganizationList.subscriptionPolicy) &&
            Objects.equals(this.totalResults, subscribedOrganizationList.totalResults) &&
            Objects.equals(this.startIndex, subscribedOrganizationList.startIndex) &&
            Objects.equals(this.count, subscribedOrganizationList.count) &&
            Objects.equals(this.organizations, subscribedOrganizationList.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelUri, subscriptionPolicy, totalResults, startIndex, count, organizations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SubscribedOrganizationList {\n");
        
        sb.append("    channelUri: ").append(toIndentedString(channelUri)).append("\n");
        sb.append("    subscriptionPolicy: ").append(toIndentedString(subscriptionPolicy)).append("\n");
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    startIndex: ").append(toIndentedString(startIndex)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    organizations: ").append(toIndentedString(organizations)).append("\n");
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

