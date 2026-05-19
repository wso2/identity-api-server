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
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentedElementDTO;
import javax.validation.constraints.*;

/**
 * Purpose information within a consent
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Purpose information within a consent")
public class ConsentedPurposeDTO  {
  
    private String name;
    private String id;
    private String type;
    private String versionId;
    private String version;
    private List<ConsentedElementDTO> elements = null;
    private Map<String, String> properties = null;


    /**
    **/
    public ConsentedPurposeDTO name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Privacy Policy", value = "")
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
    public ConsentedPurposeDTO id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "f83aa1a3-5d4d-4c0e-84db-c3a4f1e6c8b2", value = "")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {

        this.id = id;
    }

    /**
    * Purpose type classification
    **/
    public ConsentedPurposeDTO type(String type) {

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
    * UUID of the purpose version that was consented
    **/
    public ConsentedPurposeDTO versionId(String versionId) {

        this.versionId = versionId;
        return this;
    }
    
    @ApiModelProperty(example = "f83aa1a3-5d4d-4c0e-84db-c3a4f1e6c8b2", value = "UUID of the purpose version that was consented")
    @JsonProperty("versionId")
    @Valid
    public String getVersionId() {
        return versionId;
    }
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    /**
    * Human-readable version label (e.g. \&quot;2\&quot;). Null for pre-versioning consents.
    **/
    public ConsentedPurposeDTO version(String version) {

        this.version = version;
        return this;
    }
    
    @ApiModelProperty(example = "2", value = "Human-readable version label (e.g. \"2\"). Null for pre-versioning consents.")
    @JsonProperty("version")
    @Valid
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    /**
    * Consented elements for this purpose
    **/
    public ConsentedPurposeDTO elements(List<ConsentedElementDTO> elements) {

        this.elements = elements;
        return this;
    }
    
    @ApiModelProperty(value = "Consented elements for this purpose")
    @JsonProperty("elements")
    @Valid
    public List<ConsentedElementDTO> getElements() {
        return elements;
    }
    public void setElements(List<ConsentedElementDTO> elements) {
        this.elements = elements;
    }

    public ConsentedPurposeDTO addElementsItem(ConsentedElementDTO elementsItem) {
        if (this.elements == null) {
            this.elements = new ArrayList<>();
        }
        this.elements.add(elementsItem);
        return this;
    }

    /**
    * Key-value properties from the consented purpose version
    **/
    public ConsentedPurposeDTO properties(Map<String, String> properties) {

        this.properties = properties;
        return this;
    }

    @ApiModelProperty(value = "Key-value properties from the consented purpose version")
    @JsonProperty("properties")
    @Valid
    public Map<String, String> getProperties() {

        return properties;
    }
    public void setProperties(Map<String, String> properties) {

        this.properties = properties;
    }

    public ConsentedPurposeDTO putPropertiesItem(String key, String propertiesItem) {

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
        ConsentedPurposeDTO consentedPurposeDTO = (ConsentedPurposeDTO) o;
        return Objects.equals(this.name, consentedPurposeDTO.name) &&
            Objects.equals(this.id, consentedPurposeDTO.id) &&
            Objects.equals(this.type, consentedPurposeDTO.type) &&
            Objects.equals(this.versionId, consentedPurposeDTO.versionId) &&
            Objects.equals(this.version, consentedPurposeDTO.version) &&
            Objects.equals(this.elements, consentedPurposeDTO.elements) &&
            Objects.equals(this.properties, consentedPurposeDTO.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, type, versionId, version, elements, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentedPurposeDTO {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    versionId: ").append(toIndentedString(versionId)).append("\n");
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

