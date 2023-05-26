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

package org.wso2.carbon.identity.api.server.extension.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ExtensionResponseModel  {
  
    private String id;
    private String name;
    private String displayName;
    private String description;
    private String image;
    private Integer displayOrder;
    private List<String> tags = null;

    private String category;
    private String type;

    /**
    **/
    public ExtensionResponseModel id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "d7c8549f-32af-4f53-9013-f66f1a6c67bf", value = "")
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
    public ExtensionResponseModel name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "google-oidc", value = "")
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
    public ExtensionResponseModel displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "Google Auth", value = "")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    **/
    public ExtensionResponseModel description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Google Auth Connection", value = "")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    **/
    public ExtensionResponseModel image(String image) {

        this.image = image;
        return this;
    }
    
    @ApiModelProperty(example = "google-oidc.png", value = "")
    @JsonProperty("image")
    @Valid
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    /**
    **/
    public ExtensionResponseModel displayOrder(Integer displayOrder) {

        this.displayOrder = displayOrder;
        return this;
    }
    
    @ApiModelProperty(example = "3", value = "")
    @JsonProperty("displayOrder")
    @Valid
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
    **/
    public ExtensionResponseModel tags(List<String> tags) {

        this.tags = tags;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("tags")
    @Valid
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public ExtensionResponseModel addTagsItem(String tagsItem) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tagsItem);
        return this;
    }

        /**
    **/
    public ExtensionResponseModel category(String category) {

        this.category = category;
        return this;
    }
    
    @ApiModelProperty(example = "DEFAULT", value = "")
    @JsonProperty("category")
    @Valid
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    /**
    **/
    public ExtensionResponseModel type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "connection", value = "")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExtensionResponseModel extensionResponseModel = (ExtensionResponseModel) o;
        return Objects.equals(this.id, extensionResponseModel.id) &&
            Objects.equals(this.name, extensionResponseModel.name) &&
            Objects.equals(this.displayName, extensionResponseModel.displayName) &&
            Objects.equals(this.description, extensionResponseModel.description) &&
            Objects.equals(this.image, extensionResponseModel.image) &&
            Objects.equals(this.displayOrder, extensionResponseModel.displayOrder) &&
            Objects.equals(this.tags, extensionResponseModel.tags) &&
            Objects.equals(this.category, extensionResponseModel.category) &&
            Objects.equals(this.type, extensionResponseModel.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, displayName, description, image, displayOrder, tags, category, type);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ExtensionResponseModel {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
        sb.append("    category: ").append(toIndentedString(category)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

