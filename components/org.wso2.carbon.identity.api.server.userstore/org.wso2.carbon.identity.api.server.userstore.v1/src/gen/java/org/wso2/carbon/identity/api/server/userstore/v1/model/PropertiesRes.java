/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.userstore.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.userstore.v1.model.Attribute;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PropertiesRes  {
  
    private String name;
    private String defaultValue;
    private String description;
    private List<Attribute> attributes = null;


    /**
    **/
    public PropertiesRes name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Some Mandatory Property Name", value = "")
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
    public PropertiesRes defaultValue(String defaultValue) {

        this.defaultValue = defaultValue;
        return this;
    }
    
    @ApiModelProperty(example = "Some Mandatory Property Value", value = "")
    @JsonProperty("defaultValue")
    @Valid
    public String getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
    **/
    public PropertiesRes description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Description of the property", value = "")
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
    public PropertiesRes attributes(List<Attribute> attributes) {

        this.attributes = attributes;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("attributes")
    @Valid
    public List<Attribute> getAttributes() {
        return attributes;
    }
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public PropertiesRes addAttributesItem(Attribute attributesItem) {
        if (this.attributes == null) {
            this.attributes = new ArrayList<>();
        }
        this.attributes.add(attributesItem);
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
        PropertiesRes propertiesRes = (PropertiesRes) o;
        return Objects.equals(this.name, propertiesRes.name) &&
            Objects.equals(this.defaultValue, propertiesRes.defaultValue) &&
            Objects.equals(this.description, propertiesRes.description) &&
            Objects.equals(this.attributes, propertiesRes.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, defaultValue, description, attributes);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PropertiesRes {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    defaultValue: ").append(toIndentedString(defaultValue)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
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

