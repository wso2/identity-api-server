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

public class PurposeVersionCreateRequest  {
  
    private String version;
    private Boolean setAsLatest = false;
    private String description;
    private List<PurposeElementBinding> elements = null;

    private Map<String, String> properties = null;


    /**
    * Version label (e.g., \&quot;v2.0\&quot;, \&quot;2024-Q1\&quot;)
    **/
    public PurposeVersionCreateRequest version(String version) {

        this.version = version;
        return this;
    }
    
    @ApiModelProperty(example = "v2", required = true, value = "Version label (e.g., \"v2.0\", \"2024-Q1\")")
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
    * Whether to set this version as the latest version upon creation
    **/
    public PurposeVersionCreateRequest setAsLatest(Boolean setAsLatest) {

        this.setAsLatest = setAsLatest;
        return this;
    }
    
    @ApiModelProperty(value = "Whether to set this version as the latest version upon creation")
    @JsonProperty("setAsLatest")
    @Valid
    public Boolean getSetAsLatest() {
        return setAsLatest;
    }
    public void setSetAsLatest(Boolean setAsLatest) {
        this.setAsLatest = setAsLatest;
    }

    /**
    * Description of changes in this version
    **/
    public PurposeVersionCreateRequest description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Added new consent elements for enhanced user authentication", value = "Description of changes in this version")
    @JsonProperty("description")
    @Valid @Size(max=1024)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Updated list of consent elements for this version
    **/
    public PurposeVersionCreateRequest elements(List<PurposeElementBinding> elements) {

        this.elements = elements;
        return this;
    }
    
    @ApiModelProperty(value = "Updated list of consent elements for this version")
    @JsonProperty("elements")
    @Valid
    public List<PurposeElementBinding> getElements() {
        return elements;
    }
    public void setElements(List<PurposeElementBinding> elements) {
        this.elements = elements;
    }

    public PurposeVersionCreateRequest addElementsItem(PurposeElementBinding elementsItem) {
        if (this.elements == null) {
            this.elements = new ArrayList<>();
        }
        this.elements.add(elementsItem);
        return this;
    }

        /**
    * Free-form key-value properties for this version. Immutable after creation.
    **/
    public PurposeVersionCreateRequest properties(Map<String, String> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(example = "{\"policy_url\":\"https://example.com/privacy-policy\",\"retentionPeriod\":\"365\"}", value = "Free-form key-value properties for this version. Immutable after creation.")
    @JsonProperty("properties")
    @Valid
    public Map<String, String> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    public PurposeVersionCreateRequest putPropertiesItem(String key, String propertiesItem) {
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
        PurposeVersionCreateRequest purposeVersionCreateRequest = (PurposeVersionCreateRequest) o;
        return Objects.equals(this.version, purposeVersionCreateRequest.version) &&
            Objects.equals(this.setAsLatest, purposeVersionCreateRequest.setAsLatest) &&
            Objects.equals(this.description, purposeVersionCreateRequest.description) &&
            Objects.equals(this.elements, purposeVersionCreateRequest.elements) &&
            Objects.equals(this.properties, purposeVersionCreateRequest.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, setAsLatest, description, elements, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PurposeVersionCreateRequest {\n");
        
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    setAsLatest: ").append(toIndentedString(setAsLatest)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

