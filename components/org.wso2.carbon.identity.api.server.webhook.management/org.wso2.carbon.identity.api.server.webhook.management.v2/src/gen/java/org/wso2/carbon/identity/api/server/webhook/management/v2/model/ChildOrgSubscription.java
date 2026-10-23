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
 * Fanout configuration controlling which &lt;b&gt;child&lt;/b&gt; organizations of the webhook&#39;s own organization receive events for a channel. Supplied inline per channel on the webhook create, replace and patch operations.  Named &#x60;childOrgSubscription&#x60; rather than &#x60;organizationSubscription&#x60; because the organization owning the webhook is itself an organization. Sub-organizations can own webhooks too, and at that level the owner subscribes to events from organizations below it — so an unqualified \&quot;organization\&quot; would not say whose events are meant. This names the direction explicitly: downward, into descendants.  &#x60;subscriptionPolicy&#x60; round-trips: it is returned by every operation that returns a channel, and accepted by every operation that writes one. &#x60;organizations&#x60; is write-only, because the subscribed organizations are a paginated collection — read them from the resource linked as &#x60;organizationsRef&#x60;. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;
@ApiModel(description = "Fanout configuration controlling which <b>child</b> organizations of the webhook's own organization receive events for a channel. Supplied inline per channel on the webhook create, replace and patch operations.  Named `childOrgSubscription` rather than `organizationSubscription` because the organization owning the webhook is itself an organization. Sub-organizations can own webhooks too, and at that level the owner subscribes to events from organizations below it — so an unqualified \"organization\" would not say whose events are meant. This names the direction explicitly: downward, into descendants.  `subscriptionPolicy` round-trips: it is returned by every operation that returns a channel, and accepted by every operation that writes one. `organizations` is write-only, because the subscribed organizations are a paginated collection — read them from the resource linked as `organizationsRef`. ")
public class ChildOrgSubscription  {
  

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

    private SubscriptionPolicyEnum subscriptionPolicy = SubscriptionPolicyEnum.NONE;
    private List<Organization> organizations = null;

    private String organizationsRef;

    /**
    * Organization-level fanout policy for this channel. &#x60;NONE&#x60; – no organization-level fanout. &#x60;ALL_EXISTING_AND_FUTURE_ORGS&#x60; – events fanned out to every organization, including ones created in the future. &#x60;SELECTED_ORGS_ONLY&#x60; – events delivered only to the organizations listed in the &#x60;organizations&#x60; array. Selected organizations do not include their children. 
    **/
    public ChildOrgSubscription subscriptionPolicy(SubscriptionPolicyEnum subscriptionPolicy) {

        this.subscriptionPolicy = subscriptionPolicy;
        return this;
    }
    
    @ApiModelProperty(example = "SELECTED_ORGS_ONLY", required = true, value = "Organization-level fanout policy for this channel. `NONE` – no organization-level fanout. `ALL_EXISTING_AND_FUTURE_ORGS` – events fanned out to every organization, including ones created in the future. `SELECTED_ORGS_ONLY` – events delivered only to the organizations listed in the `organizations` array. Selected organizations do not include their children. ")
    @JsonProperty("subscriptionPolicy")
    @Valid
    @NotNull(message = "Property subscriptionPolicy cannot be null.")

    public SubscriptionPolicyEnum getSubscriptionPolicy() {
        return subscriptionPolicy;
    }
    public void setSubscriptionPolicy(SubscriptionPolicyEnum subscriptionPolicy) {
        this.subscriptionPolicy = subscriptionPolicy;
    }

    /**
    * Explicit list of organizations to receive events. Accepted only when &#x60;subscriptionPolicy&#x60; is &#x60;SELECTED_ORGS_ONLY&#x60;; rejected under any other policy.  Must be non-empty. &#x60;SELECTED_ORGS_ONLY&#x60; with nothing selected is the same state as &#x60;NONE&#x60;, so it is not a second way to express it — to stop organization fanout, set &#x60;subscriptionPolicy&#x60; to &#x60;NONE&#x60;.  &lt;b&gt;Write-only, and preserved when omitted.&lt;/b&gt; This is the only preserve-on-omit rule besides &#x60;secret&#x60;, and it exists for the same reason — the list is never returned, so a client performing a read-modify-write cycle cannot resend it. Concretely:  - **omitted** — the channel&#39;s current organization subscriptions are left   exactly as they are, whatever else the request changes.  - **supplied** — replaced wholesale against the organizations currently   subscribed: those present are subscribed, those absent are unsubscribed.   Required when a channel is *first* set to &#x60;SELECTED_ORGS_ONLY&#x60;, since there is nothing to preserve.  Only &#x60;orgId&#x60; is supplied; the organizations resource returns the same objects with &#x60;orgName&#x60; and &#x60;parentOrgId&#x60; resolved. 
    **/
    public ChildOrgSubscription organizations(List<Organization> organizations) {

        this.organizations = organizations;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"orgId\":\"b028ca17-8f89-449c-ae27-fa955e66465d\"},{\"orgId\":\"a17b28ca-9f89-449c-ae27-fa955e66465f\"}]", value = "Explicit list of organizations to receive events. Accepted only when `subscriptionPolicy` is `SELECTED_ORGS_ONLY`; rejected under any other policy.  Must be non-empty. `SELECTED_ORGS_ONLY` with nothing selected is the same state as `NONE`, so it is not a second way to express it — to stop organization fanout, set `subscriptionPolicy` to `NONE`.  <b>Write-only, and preserved when omitted.</b> This is the only preserve-on-omit rule besides `secret`, and it exists for the same reason — the list is never returned, so a client performing a read-modify-write cycle cannot resend it. Concretely:  - **omitted** — the channel's current organization subscriptions are left   exactly as they are, whatever else the request changes.  - **supplied** — replaced wholesale against the organizations currently   subscribed: those present are subscribed, those absent are unsubscribed.   Required when a channel is *first* set to `SELECTED_ORGS_ONLY`, since there is nothing to preserve.  Only `orgId` is supplied; the organizations resource returns the same objects with `orgName` and `parentOrgId` resolved. ")
    @JsonProperty("organizations")
    @Valid @Size(min=1,max=100)
    public List<Organization> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public ChildOrgSubscription addOrganizationsItem(Organization organizationsItem) {
        if (this.organizations == null) {
            this.organizations = new ArrayList<Organization>();
        }
        this.organizations.add(organizationsItem);
        return this;
    }

        /**
    * Link to the child organizations currently receiving events for this channel, resolved with names and parent references and paginated.  &lt;b&gt;Returned by the channel resource only.&lt;/b&gt; It is omitted from the webhook representation, which offers &#x60;self&#x60; on each channel instead — so each representation links one level down and the webhook response does not carry two URLs per channel. Where it is returned it is always present, including under &#x60;NONE&#x60;, where the referenced resource yields an empty list. 
    **/
    public ChildOrgSubscription organizationsRef(String organizationsRef) {

        this.organizationsRef = organizationsRef;
        return this;
    }
    
    @ApiModelProperty(example = "/t/carbon.super/api/server/v2/webhooks/eeb8c1a2-3f4d-4e5b-8c6f-7d8e9f0a1b2c/channels/2b6e5a7d-4c88-4e01-9a73-0f5cb8e3d219/organizations", value = "Link to the child organizations currently receiving events for this channel, resolved with names and parent references and paginated.  <b>Returned by the channel resource only.</b> It is omitted from the webhook representation, which offers `self` on each channel instead — so each representation links one level down and the webhook response does not carry two URLs per channel. Where it is returned it is always present, including under `NONE`, where the referenced resource yields an empty list. ")
    @JsonProperty("organizationsRef")
    @Valid
    public String getOrganizationsRef() {
        return organizationsRef;
    }
    public void setOrganizationsRef(String organizationsRef) {
        this.organizationsRef = organizationsRef;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChildOrgSubscription childOrgSubscription = (ChildOrgSubscription) o;
        return Objects.equals(this.subscriptionPolicy, childOrgSubscription.subscriptionPolicy) &&
            Objects.equals(this.organizations, childOrgSubscription.organizations) &&
            Objects.equals(this.organizationsRef, childOrgSubscription.organizationsRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriptionPolicy, organizations, organizationsRef);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ChildOrgSubscription {\n");
        
        sb.append("    subscriptionPolicy: ").append(toIndentedString(subscriptionPolicy)).append("\n");
        sb.append("    organizations: ").append(toIndentedString(organizations)).append("\n");
        sb.append("    organizationsRef: ").append(toIndentedString(organizationsRef)).append("\n");
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

