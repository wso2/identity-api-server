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
import java.util.UUID;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeDTOLatestVersion;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeElementDTO;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PurposeDTO  {
  
    private UUID id;
    private String name;
    private String description;
    private String type;
    private PurposeDTOLatestVersion latestVersion;
    private List<PurposeElementDTO> elements = null;

    private Map<String, String> properties = null;


    /**
    * ID of the purpose
    **/
    public PurposeDTO id(UUID id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "f83aa1a3-5d4d-4c0e-84db-c3a4f1e6c8b2", value = "ID of the purpose")
    @JsonProperty("id")
    @Valid
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    /**
    * Name of the purpose
    **/
    public PurposeDTO name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Privacy Policy", value = "Name of the purpose")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Description of the purpose
    **/
    public PurposeDTO description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Collection of user data for privacy policy compliance and consent management", value = "Description of the purpose")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Purpose type classification
    **/
    public PurposeDTO type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "Policy", value = "Purpose type classification")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    **/
    public PurposeDTO latestVersion(PurposeDTOLatestVersion latestVersion) {

        this.latestVersion = latestVersion;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("latestVersion")
    @Valid
    public PurposeDTOLatestVersion getLatestVersion() {
        return latestVersion;
    }
    public void setLatestVersion(PurposeDTOLatestVersion latestVersion) {
        this.latestVersion = latestVersion;
    }

    /**
    * Consent elements associated with this purpose
    **/
    public PurposeDTO elements(List<PurposeElementDTO> elements) {

        this.elements = elements;
        return this;
    }
    
    @ApiModelProperty(value = "Consent elements associated with this purpose")
    @JsonProperty("elements")
    @Valid
    public List<PurposeElementDTO> getElements() {
        return elements;
    }
    public void setElements(List<PurposeElementDTO> elements) {
        this.elements = elements;
    }

    public PurposeDTO addElementsItem(PurposeElementDTO elementsItem) {
        if (this.elements == null) {
            this.elements = new ArrayList<>();
        }
        this.elements.add(elementsItem);
        return this;
    }

        /**
    * Properties from the latest version of this purpose.
    **/
    public PurposeDTO properties(Map<String, String> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(example = "{\"policy_url\":\"https://example.com/privacy-policy\",\"retentionPeriod\":\"365\"}", value = "Properties from the latest version of this purpose.")
    @JsonProperty("properties")
    @Valid
    public Map<String, String> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    public PurposeDTO putPropertiesItem(String key, String propertiesItem) {
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
        PurposeDTO purposeDTO = (PurposeDTO) o;
        return Objects.equals(this.id, purposeDTO.id) &&
            Objects.equals(this.name, purposeDTO.name) &&
            Objects.equals(this.description, purposeDTO.description) &&
            Objects.equals(this.type, purposeDTO.type) &&
            Objects.equals(this.latestVersion, purposeDTO.latestVersion) &&
            Objects.equals(this.elements, purposeDTO.elements) &&
            Objects.equals(this.properties, purposeDTO.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, latestVersion, elements, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PurposeDTO {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    latestVersion: ").append(toIndentedString(latestVersion)).append("\n");
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

