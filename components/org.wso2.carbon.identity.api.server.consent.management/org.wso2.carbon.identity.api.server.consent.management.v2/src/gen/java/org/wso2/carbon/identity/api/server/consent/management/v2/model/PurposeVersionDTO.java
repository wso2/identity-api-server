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
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeElementDTO;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PurposeVersionDTO  {
  
    private String id;
    private String version;
    private String description;
    private List<PurposeElementDTO> elements = null;

    private Map<String, String> properties = null;


    /**
    * ID of the purpose version
    **/
    public PurposeVersionDTO id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "f83aa1a3-5d4d-4c0e-84db-c3a4f1e6c8b2", value = "ID of the purpose version")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Version label
    **/
    public PurposeVersionDTO version(String version) {

        this.version = version;
        return this;
    }
    
    @ApiModelProperty(example = "v2", value = "Version label")
    @JsonProperty("version")
    @Valid
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    /**
    * Description of changes in this version
    **/
    public PurposeVersionDTO description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Added new consent elements for enhanced user authentication", value = "Description of changes in this version")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Consent elements in this version
    **/
    public PurposeVersionDTO elements(List<PurposeElementDTO> elements) {

        this.elements = elements;
        return this;
    }
    
    @ApiModelProperty(value = "Consent elements in this version")
    @JsonProperty("elements")
    @Valid
    public List<PurposeElementDTO> getElements() {
        return elements;
    }
    public void setElements(List<PurposeElementDTO> elements) {
        this.elements = elements;
    }

    public PurposeVersionDTO addElementsItem(PurposeElementDTO elementsItem) {
        if (this.elements == null) {
            this.elements = new ArrayList<>();
        }
        this.elements.add(elementsItem);
        return this;
    }

        /**
    * Free-form key-value properties stored with this version.
    **/
    public PurposeVersionDTO properties(Map<String, String> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(example = "{\"policy_url\":\"https://example.com/privacy-policy\",\"retentionPeriod\":\"365\"}", value = "Free-form key-value properties stored with this version.")
    @JsonProperty("properties")
    @Valid
    public Map<String, String> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    public PurposeVersionDTO putPropertiesItem(String key, String propertiesItem) {
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
        PurposeVersionDTO purposeVersionDTO = (PurposeVersionDTO) o;
        return Objects.equals(this.id, purposeVersionDTO.id) &&
            Objects.equals(this.version, purposeVersionDTO.version) &&
            Objects.equals(this.description, purposeVersionDTO.description) &&
            Objects.equals(this.elements, purposeVersionDTO.elements) &&
            Objects.equals(this.properties, purposeVersionDTO.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, description, elements, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PurposeVersionDTO {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
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

