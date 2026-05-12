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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeElementBinding;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PurposeCreateRequest  {
  
    private String name;
    private String description;
    private String type;
    private String version;
    private List<PurposeElementBinding> elements = null;

    private Map<String, String> properties = null;


    /**
    * Name of the purpose
    **/
    public PurposeCreateRequest name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Privacy Policy", required = true, value = "Name of the purpose")
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
    * Human-readable description of the purpose
    **/
    public PurposeCreateRequest description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Collection of user data for privacy policy compliance and consent management", value = "Human-readable description of the purpose")
    @JsonProperty("description")
    @Valid @Size(max=1024)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Purpose type classification
    **/
    public PurposeCreateRequest type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "Policy", required = true, value = "Purpose type classification")
    @JsonProperty("type")
    @Valid
    @NotNull(message = "Property type cannot be null.")
 @Size(max=255)
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    * Initial version label for the purpose (e.g., \&quot;v1.0\&quot;)
    **/
    public PurposeCreateRequest version(String version) {

        this.version = version;
        return this;
    }
    
    @ApiModelProperty(example = "v1", required = true, value = "Initial version label for the purpose (e.g., \"v1.0\")")
    @JsonProperty("version")
    @Valid
    @NotNull(message = "Property version cannot be null.")
 @Size(min=1,max=100)
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    /**
    * Consent elements associated with this purpose
    **/
    public PurposeCreateRequest elements(List<PurposeElementBinding> elements) {

        this.elements = elements;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"id\":\"2ee774d9-a277-4b7e-b7dd-8873d515fd24\",\"mandatory\":true},{\"id\":\"fefd9d59-2fd5-49d6-9459-48668318a97f\",\"mandatory\":false}]", value = "Consent elements associated with this purpose")
    @JsonProperty("elements")
    @Valid @Size(min=0)
    public List<PurposeElementBinding> getElements() {
        return elements;
    }
    public void setElements(List<PurposeElementBinding> elements) {
        this.elements = elements;
    }

    public PurposeCreateRequest addElementsItem(PurposeElementBinding elementsItem) {
        if (this.elements == null) {
            this.elements = new ArrayList<>();
        }
        this.elements.add(elementsItem);
        return this;
    }

        /**
    * Free-form key-value properties applied to the initial version snapshot; these properties are persisted with the initial version.
    **/
    public PurposeCreateRequest properties(Map<String, String> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(example = "{\"policy_url\":\"https://example.com/privacy-policy\",\"retentionPeriod\":\"365\"}", value = "Free-form key-value properties applied to the initial version snapshot; these properties are persisted with the initial version.")
    @JsonProperty("properties")
    @Valid
    public Map<String, String> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    public PurposeCreateRequest putPropertiesItem(String key, String propertiesItem) {
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
        PurposeCreateRequest purposeCreateRequest = (PurposeCreateRequest) o;
        return Objects.equals(this.name, purposeCreateRequest.name) &&
            Objects.equals(this.description, purposeCreateRequest.description) &&
            Objects.equals(this.type, purposeCreateRequest.type) &&
            Objects.equals(this.version, purposeCreateRequest.version) &&
            Objects.equals(this.elements, purposeCreateRequest.elements) &&
            Objects.equals(this.properties, purposeCreateRequest.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, type, version, elements, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PurposeCreateRequest {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    elements: ").append(toIndentedString(elements)).append("\n");
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

