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

package org.wso2.carbon.identity.api.server.consent.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ElementCreateRequest  {
  
    private String name;
    private String displayName;
    private String description;
    private Map<String, String> properties = null;


    /**
    * Identifier/name of the consent element
    **/
    public ElementCreateRequest name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "email_address", required = true, value = "Identifier/name of the consent element")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")
 @Size(min=1,max=255)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Human-readable display name
    **/
    public ElementCreateRequest displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "Email Address", value = "Human-readable display name")
    @JsonProperty("displayName")
    @Valid @Size(max=255)
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    * Detailed description of the element
    **/
    public ElementCreateRequest description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "User email address used for account notifications and communications", value = "Detailed description of the element")
    @JsonProperty("description")
    @Valid @Size(max=1024)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Free-form key-value properties for this element.
    **/
    public ElementCreateRequest properties(Map<String, String> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(example = "{\"dataCategory\":\"personal\",\"retentionPeriod\":\"365\"}", value = "Free-form key-value properties for this element.")
    @JsonProperty("properties")
    @Valid
    public Map<String, String> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    public ElementCreateRequest putPropertiesItem(String key, String propertiesItem) {
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(key, propertiesItem);
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
        ElementCreateRequest elementCreateRequest = (ElementCreateRequest) o;
        return Objects.equals(this.name, elementCreateRequest.name) &&
            Objects.equals(this.displayName, elementCreateRequest.displayName) &&
            Objects.equals(this.description, elementCreateRequest.description) &&
            Objects.equals(this.properties, elementCreateRequest.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, displayName, description, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ElementCreateRequest {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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

