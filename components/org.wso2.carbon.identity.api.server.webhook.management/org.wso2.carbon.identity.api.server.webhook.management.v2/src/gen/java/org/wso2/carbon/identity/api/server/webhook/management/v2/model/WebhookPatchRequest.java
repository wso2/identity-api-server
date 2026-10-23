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
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookSubscription;
import javax.validation.constraints.*;

/**
 * JSON Merge Patch document (RFC 7386) describing the properties to change. Every property is optional and an omitted property is left unchanged. &#x60;status&#x60; is not patchable — use the activate and deactivate sub-resources.  No property is &#x60;nullable&#x60;: a &#x60;null&#x60; member is rejected with &#x60;400&#x60; rather than removing the property, because no webhook property has a valid removed state. &#x60;minProperties&#x60; likewise rejects an empty document, which RFC 7386 would otherwise treat as a no-op. Both deviations are deliberate and documented on the operation. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;
@ApiModel(description = "JSON Merge Patch document (RFC 7386) describing the properties to change. Every property is optional and an omitted property is left unchanged. `status` is not patchable — use the activate and deactivate sub-resources.  No property is `nullable`: a `null` member is rejected with `400` rather than removing the property, because no webhook property has a valid removed state. `minProperties` likewise rejects an empty document, which RFC 7386 would otherwise treat as a no-op. Both deviations are deliberate and documented on the operation. ")
public class WebhookPatchRequest  {
  
    private String endpoint;
    private String name;
    private String secret;
    private List<WebhookSubscription> subscribedChannels = null;


    /**
    * Replaces the webhook URL.
    **/
    public WebhookPatchRequest endpoint(String endpoint) {

        this.endpoint = endpoint;
        return this;
    }
    
    @ApiModelProperty(example = "https://example.com/webhook", value = "Replaces the webhook URL.")
    @JsonProperty("endpoint")
    @Valid
    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
    * Replaces the webhook name.
    **/
    public WebhookPatchRequest name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Login Webhook.", value = "Replaces the webhook name.")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Replaces the secret used to validate webhook payloads. Omit to keep the current secret.
    **/
    public WebhookPatchRequest secret(String secret) {

        this.secret = secret;
        return this;
    }
    
    @ApiModelProperty(example = "my-new-secret", value = "Replaces the secret used to validate webhook payloads. Omit to keep the current secret.")
    @JsonProperty("secret")
    @Valid
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
    * Replaces the entire channel set, with the same semantics as the replace operation. Omit the property to leave subscriptions untouched. 
    **/
    public WebhookPatchRequest subscribedChannels(List<WebhookSubscription> subscribedChannels) {

        this.subscribedChannels = subscribedChannels;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"channelUri\":\"https://schemas.identity.wso2.org/events/login\",\"childOrgSubscription\":{\"subscriptionPolicy\":\"ALL_EXISTING_AND_FUTURE_ORGS\"}},{\"channelUri\":\"https://schemas.identity.wso2.org/events/token\",\"childOrgSubscription\":{\"subscriptionPolicy\":\"SELECTED_ORGS_ONLY\",\"organizations\":[{\"orgId\":\"b028ca17-8f89-449c-ae27-fa955e66465d\"}]}}]", value = "Replaces the entire channel set, with the same semantics as the replace operation. Omit the property to leave subscriptions untouched. ")
    @JsonProperty("subscribedChannels")
    @Valid @Size(min=1)
    public List<WebhookSubscription> getSubscribedChannels() {
        return subscribedChannels;
    }
    public void setSubscribedChannels(List<WebhookSubscription> subscribedChannels) {
        this.subscribedChannels = subscribedChannels;
    }

    public WebhookPatchRequest addSubscribedChannelsItem(WebhookSubscription subscribedChannelsItem) {
        if (this.subscribedChannels == null) {
            this.subscribedChannels = new ArrayList<WebhookSubscription>();
        }
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
        WebhookPatchRequest webhookPatchRequest = (WebhookPatchRequest) o;
        return Objects.equals(this.endpoint, webhookPatchRequest.endpoint) &&
            Objects.equals(this.name, webhookPatchRequest.name) &&
            Objects.equals(this.secret, webhookPatchRequest.secret) &&
            Objects.equals(this.subscribedChannels, webhookPatchRequest.subscribedChannels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpoint, name, secret, subscribedChannels);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WebhookPatchRequest {\n");
        
        sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
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

