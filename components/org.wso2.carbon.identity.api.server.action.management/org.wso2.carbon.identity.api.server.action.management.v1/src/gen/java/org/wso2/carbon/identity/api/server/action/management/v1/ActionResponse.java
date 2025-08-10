/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.action.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionType;
import org.wso2.carbon.identity.api.server.action.management.v1.EndpointResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ORRuleResponse;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ActionResponse  {
  
    private String id;
    private ActionType type;
    private String name;
    private String description;

@XmlType(name="StatusEnum")
@XmlEnum(String.class)
public enum StatusEnum {

    @XmlEnumValue("ACTIVE") ACTIVE(String.valueOf("ACTIVE")), @XmlEnumValue("INACTIVE") INACTIVE(String.valueOf("INACTIVE"));


    private String value;

    StatusEnum(String v) {
        value = v;
    }

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
    private String createdAt;
    private String updatedAt;
    private EndpointResponse endpoint;
    private ORRuleResponse rule;

    /**
    * Unique identifier of the action.
    **/
    public ActionResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "24f64d17-9824-4e28-8413-de45728d8e84", value = "Unique identifier of the action.")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public ActionResponse type(ActionType type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("type")
    @Valid
    public ActionType getType() {
        return type;
    }
    public void setType(ActionType type) {
        this.type = type;
    }

    /**
    * Name of the action.
    **/
    public ActionResponse name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Access Token Pre Issue", value = "Name of the action.")
    @JsonProperty("name")
    @Valid @Size(min=1,max=255)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Description of the action.
    **/
    public ActionResponse description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "This action invokes before issuing an access token.", value = "Description of the action.")
    @JsonProperty("description")
    @Valid @Size(max=255)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Status of the action.
    **/
    public ActionResponse status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "ACTIVE", value = "Status of the action.")
    @JsonProperty("status")
    @Valid
    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
    * Created time of the action.
    **/
    public ActionResponse createdAt(String createdAt) {

        this.createdAt = createdAt;
        return this;
    }
    
    @ApiModelProperty(example = "2025-08-01 12:00:00", value = "Created time of the action.")
    @JsonProperty("createdAt")
    @Valid
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
    * Last updated time of the action.
    **/
    public ActionResponse updatedAt(String updatedAt) {

        this.updatedAt = updatedAt;
        return this;
    }
    
    @ApiModelProperty(example = "2025-09-01 13:00:00", value = "Last updated time of the action.")
    @JsonProperty("updatedAt")
    @Valid
    public String getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
    **/
    public ActionResponse endpoint(EndpointResponse endpoint) {

        this.endpoint = endpoint;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("endpoint")
    @Valid
    public EndpointResponse getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(EndpointResponse endpoint) {
        this.endpoint = endpoint;
    }

    /**
    **/
    public ActionResponse rule(ORRuleResponse rule) {

        this.rule = rule;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("rule")
    @Valid
    public ORRuleResponse getRule() {
        return rule;
    }
    public void setRule(ORRuleResponse rule) {
        this.rule = rule;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActionResponse actionResponse = (ActionResponse) o;
        return Objects.equals(this.id, actionResponse.id) &&
            Objects.equals(this.type, actionResponse.type) &&
            Objects.equals(this.name, actionResponse.name) &&
            Objects.equals(this.description, actionResponse.description) &&
            Objects.equals(this.status, actionResponse.status) &&
            Objects.equals(this.createdAt, actionResponse.createdAt) &&
            Objects.equals(this.updatedAt, actionResponse.updatedAt) &&
            Objects.equals(this.endpoint, actionResponse.endpoint) &&
            Objects.equals(this.rule, actionResponse.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, description, status, createdAt, updatedAt, endpoint, rule);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ActionResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
        sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
        sb.append("    rule: ").append(toIndentedString(rule)).append("\n");
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

