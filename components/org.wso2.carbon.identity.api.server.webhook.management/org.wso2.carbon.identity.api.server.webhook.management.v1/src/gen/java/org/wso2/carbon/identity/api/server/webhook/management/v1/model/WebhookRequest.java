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

package org.wso2.carbon.identity.api.server.webhook.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookRequestEventSchema;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;

public class WebhookRequest  {
  
    private String endpoint;
    private WebhookRequestEventSchema eventSchema;
    private String description;
    private String secret;
    private List<String> eventsSubscribed = new ArrayList<String>();


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
    public WebhookRequest endpoint(String endpoint) {

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
    public WebhookRequest eventSchema(WebhookRequestEventSchema eventSchema) {

        this.eventSchema = eventSchema;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("eventSchema")
    @Valid
    @NotNull(message = "Property eventSchema cannot be null.")

    public WebhookRequestEventSchema getEventSchema() {
        return eventSchema;
    }
    public void setEventSchema(WebhookRequestEventSchema eventSchema) {
        this.eventSchema = eventSchema;
    }

    /**
    * Webhook description.
    **/
    public WebhookRequest description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "A webhook for user events.", required = true, value = "Webhook description.")
    @JsonProperty("description")
    @Valid
    @NotNull(message = "Property description cannot be null.")

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Secret for validating webhook payloads.
    **/
    public WebhookRequest secret(String secret) {

        this.secret = secret;
        return this;
    }
    
    @ApiModelProperty(example = "my-secret", value = "Secret for validating webhook payloads.")
    @JsonProperty("secret")
    @Valid
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
    * List of events to subscribe to.
    **/
    public WebhookRequest eventsSubscribed(List<String> eventsSubscribed) {

        this.eventsSubscribed = eventsSubscribed;
        return this;
    }
    
    @ApiModelProperty(example = "[\"schemas.identity.wso2.org/events/logins/event-type/loginSuccess\",\"schemas.identity.wso2.org/events/logins/event-type/loginFailed\"]", required = true, value = "List of events to subscribe to.")
    @JsonProperty("eventsSubscribed")
    @Valid
    @NotNull(message = "Property eventsSubscribed cannot be null.")

    public List<String> getEventsSubscribed() {
        return eventsSubscribed;
    }
    public void setEventsSubscribed(List<String> eventsSubscribed) {
        this.eventsSubscribed = eventsSubscribed;
    }

    public WebhookRequest addEventsSubscribedItem(String eventsSubscribedItem) {
        this.eventsSubscribed.add(eventsSubscribedItem);
        return this;
    }

        /**
    * Webhook Status.
    **/
    public WebhookRequest status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "ACTIVE", value = "Webhook Status.")
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
        WebhookRequest webhookRequest = (WebhookRequest) o;
        return Objects.equals(this.endpoint, webhookRequest.endpoint) &&
            Objects.equals(this.eventSchema, webhookRequest.eventSchema) &&
            Objects.equals(this.description, webhookRequest.description) &&
            Objects.equals(this.secret, webhookRequest.secret) &&
            Objects.equals(this.eventsSubscribed, webhookRequest.eventsSubscribed) &&
            Objects.equals(this.status, webhookRequest.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpoint, eventSchema, description, secret, eventsSubscribed, status);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WebhookRequest {\n");
        
        sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
        sb.append("    eventSchema: ").append(toIndentedString(eventSchema)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
        sb.append("    eventsSubscribed: ").append(toIndentedString(eventsSubscribed)).append("\n");
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

