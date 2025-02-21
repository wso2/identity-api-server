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

package org.wso2.carbon.identity.api.server.registration.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.registration.v1.Action;
import javax.validation.constraints.*;

/**
 * Represent individual UI components in the registration flow
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Represent individual UI components in the registration flow")
public class Element  {
  
    private String id;
    private String category;
    private String type;
    private String variant;
    private String version;
    private Boolean deprecated;
    private Action action;
    private Object config;

    /**
    * Unique identifier of the element
    **/
    public Element id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "dnd-element-210e95c0-c580-40b0-9646-7054bb340f64", required = true, value = "Unique identifier of the element")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Type of element
    **/
    public Element category(String category) {

        this.category = category;
        return this;
    }
    
    @ApiModelProperty(example = "FIELD", required = true, value = "Type of element")
    @JsonProperty("category")
    @Valid
    @NotNull(message = "Property category cannot be null.")

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    /**
    * Specific element type (e.g., INPUT, BUTTON)
    **/
    public Element type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "INPUT", required = true, value = "Specific element type (e.g., INPUT, BUTTON)")
    @JsonProperty("type")
    @Valid
    @NotNull(message = "Property type cannot be null.")

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    * Variant of the element (e.g., PRIMARY, TEXT)
    **/
    public Element variant(String variant) {

        this.variant = variant;
        return this;
    }
    
    @ApiModelProperty(example = "PASSWORD", required = true, value = "Variant of the element (e.g., PRIMARY, TEXT)")
    @JsonProperty("variant")
    @Valid
    @NotNull(message = "Property variant cannot be null.")

    public String getVariant() {
        return variant;
    }
    public void setVariant(String variant) {
        this.variant = variant;
    }

    /**
    * Version of the element
    **/
    public Element version(String version) {

        this.version = version;
        return this;
    }
    
    @ApiModelProperty(example = "1.0.0", value = "Version of the element")
    @JsonProperty("version")
    @Valid
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    /**
    * Indicate whether the element is deprecated
    **/
    public Element deprecated(Boolean deprecated) {

        this.deprecated = deprecated;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Indicate whether the element is deprecated")
    @JsonProperty("deprecated")
    @Valid
    public Boolean getDeprecated() {
        return deprecated;
    }
    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    /**
    **/
    public Element action(Action action) {

        this.action = action;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("action")
    @Valid
    public Action getAction() {
        return action;
    }
    public void setAction(Action action) {
        this.action = action;
    }

    /**
    * Configuration details of the element
    **/
    public Element config(Object config) {

        this.config = config;
        return this;
    }
    
    @ApiModelProperty(value = "Configuration details of the element")
    @JsonProperty("config")
    @Valid
    public Object getConfig() {
        return config;
    }
    public void setConfig(Object config) {
        this.config = config;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Element element = (Element) o;
        return Objects.equals(this.id, element.id) &&
            Objects.equals(this.category, element.category) &&
            Objects.equals(this.type, element.type) &&
            Objects.equals(this.variant, element.variant) &&
            Objects.equals(this.version, element.version) &&
            Objects.equals(this.deprecated, element.deprecated) &&
            Objects.equals(this.action, element.action) &&
            Objects.equals(this.config, element.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, type, variant, version, deprecated, action, config);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Element {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    category: ").append(toIndentedString(category)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    variant: ").append(toIndentedString(variant)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    deprecated: ").append(toIndentedString(deprecated)).append("\n");
        sb.append("    action: ").append(toIndentedString(action)).append("\n");
        sb.append("    config: ").append(toIndentedString(config)).append("\n");
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

