/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.tag.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ApplicationTagsListItem  {
  
    private String id;
    private String name;
    private String colour;
    private Integer associatedAppsCount;
    private String self;

    /**
    **/
    public ApplicationTagsListItem id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "85e3f4b8-0d22-4181-b1e3-1651f71b88bd", value = "")
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
    public ApplicationTagsListItem name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "HR", value = "")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public ApplicationTagsListItem colour(String colour) {

        this.colour = colour;
        return this;
    }
    
    @ApiModelProperty(example = "#677b66", value = "")
    @JsonProperty("colour")
    @Valid
    public String getColour() {
        return colour;
    }
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
    **/
    public ApplicationTagsListItem associatedAppsCount(Integer associatedAppsCount) {

        this.associatedAppsCount = associatedAppsCount;
        return this;
    }
    
    @ApiModelProperty(example = "2", value = "")
    @JsonProperty("associatedAppsCount")
    @Valid
    public Integer getAssociatedAppsCount() {
        return associatedAppsCount;
    }
    public void setAssociatedAppsCount(Integer associatedAppsCount) {
        this.associatedAppsCount = associatedAppsCount;
    }

    /**
    **/
    public ApplicationTagsListItem self(String self) {

        this.self = self;
        return this;
    }
    
    @ApiModelProperty(example = "/t/wso2.com/api/server/v1/applications-tags/85e3f4b8-0d22-4181-b1e3-1651f71b88bd", value = "")
    @JsonProperty("self")
    @Valid
    public String getSelf() {
        return self;
    }
    public void setSelf(String self) {
        this.self = self;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationTagsListItem applicationTagsListItem = (ApplicationTagsListItem) o;
        return Objects.equals(this.id, applicationTagsListItem.id) &&
            Objects.equals(this.name, applicationTagsListItem.name) &&
            Objects.equals(this.colour, applicationTagsListItem.colour) &&
            Objects.equals(this.associatedAppsCount, applicationTagsListItem.associatedAppsCount) &&
            Objects.equals(this.self, applicationTagsListItem.self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, colour, associatedAppsCount, self);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ApplicationTagsListItem {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    colour: ").append(toIndentedString(colour)).append("\n");
        sb.append("    associatedAppsCount: ").append(toIndentedString(associatedAppsCount)).append("\n");
        sb.append("    self: ").append(toIndentedString(self)).append("\n");
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

