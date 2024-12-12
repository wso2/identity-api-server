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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionType;
import org.wso2.carbon.identity.api.server.action.management.v1.Link;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ActionBasicResponse  {
  
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
    private List<Link> links = null;


    /**
    **/
    public ActionBasicResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "24f64d17-9824-4e28-8413-de45728d8e84", value = "")
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
    public ActionBasicResponse type(ActionType type) {

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
    **/
    public ActionBasicResponse name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Access Token Pre Issue", value = "")
    @JsonProperty("name")
    @Valid @Size(min=1,max=255)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public ActionBasicResponse description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "This is the configuration of pre-action for issuing access token.", value = "")
    @JsonProperty("description")
    @Valid @Size(max=255)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    **/
    public ActionBasicResponse status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(value = "")
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
    public ActionBasicResponse links(List<Link> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"href\":\"/t/wso2.com/api/server/v1/actions/preIssueAccessToken/24f64d17-9824-4e28-8413-de45728d8e84\",\"method\":\"GET\",\"rel\":\"self\"}]", value = "")
    @JsonProperty("links")
    @Valid
    public List<Link> getLinks() {
        return links;
    }
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public ActionBasicResponse addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<Link>();
        }
        this.links.add(linksItem);
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
        ActionBasicResponse actionBasicResponse = (ActionBasicResponse) o;
        return Objects.equals(this.id, actionBasicResponse.id) &&
            Objects.equals(this.type, actionBasicResponse.type) &&
            Objects.equals(this.name, actionBasicResponse.name) &&
            Objects.equals(this.description, actionBasicResponse.description) &&
            Objects.equals(this.status, actionBasicResponse.status) &&
            Objects.equals(this.links, actionBasicResponse.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, description, status, links);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ActionBasicResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

