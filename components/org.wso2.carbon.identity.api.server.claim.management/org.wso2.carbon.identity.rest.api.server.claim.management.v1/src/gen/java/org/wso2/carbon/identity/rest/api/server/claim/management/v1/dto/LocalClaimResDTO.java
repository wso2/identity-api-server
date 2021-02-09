/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto;

import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.AttributeMappingDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.PropertyDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

    /**
    * Local claim response.
    **/
@ApiModel(description = "Local claim response.")
public class LocalClaimResDTO {

    @Valid 
    private String id = null;

    @Valid 
    private String claimURI = null;

    @Valid 
    private String dialectURI = null;

    @Valid 
    private String description = null;

    @Valid 
    private Integer displayOrder = null;

    @Valid 
    private String displayName = null;

    @Valid 
    private Boolean readOnly = null;

    @Valid 
    private String regEx = null;

    @Valid 
    private Boolean required = null;

    @Valid 
    private Boolean supportedByDefault = null;

    @Valid 
    private List<AttributeMappingDTO> attributeMapping = new ArrayList<AttributeMappingDTO>();

    @Valid 
    private List<PropertyDTO> properties = new ArrayList<PropertyDTO>();

    /**
    * claim ID.
    **/
    @ApiModelProperty(value = "claim ID.")
    @JsonProperty("id")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * A unique URI specific to the claim.
    **/
    @ApiModelProperty(value = "A unique URI specific to the claim.")
    @JsonProperty("claimURI")
    public String getClaimURI() {
        return claimURI;
    }
    public void setClaimURI(String claimURI) {
        this.claimURI = claimURI;
    }

    /**
    * URI of the claim dialect.
    **/
    @ApiModelProperty(value = "URI of the claim dialect.")
    @JsonProperty("dialectURI")
    public String getDialectURI() {
        return dialectURI;
    }
    public void setDialectURI(String dialectURI) {
        this.dialectURI = dialectURI;
    }

    /**
    * Description of the claim.
    **/
    @ApiModelProperty(value = "Description of the claim.")
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * The order in which the claim is displayed among other claims under the same dialect.
    **/
    @ApiModelProperty(value = "The order in which the claim is displayed among other claims under the same dialect.")
    @JsonProperty("displayOrder")
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
    * Name of the claim to be displayed in the UI.
    **/
    @ApiModelProperty(value = "Name of the claim to be displayed in the UI.")
    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    * Specifies if the claim is read-only.
    **/
    @ApiModelProperty(value = "Specifies if the claim is read-only.")
    @JsonProperty("readOnly")
    public Boolean getReadOnly() {
        return readOnly;
    }
    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
    * Regular expression used to validate inputs.
    **/
    @ApiModelProperty(value = "Regular expression used to validate inputs.")
    @JsonProperty("regEx")
    public String getRegEx() {
        return regEx;
    }
    public void setRegEx(String regEx) {
        this.regEx = regEx;
    }

    /**
    * Specifies if the claim is required for user registration.
    **/
    @ApiModelProperty(value = "Specifies if the claim is required for user registration.")
    @JsonProperty("required")
    public Boolean getRequired() {
        return required;
    }
    public void setRequired(Boolean required) {
        this.required = required;
    }

    /**
    * Specifies if the claim will be prompted during user registration and displayed on the user profile.
    **/
    @ApiModelProperty(value = "Specifies if the claim will be prompted during user registration and displayed on the user profile.")
    @JsonProperty("supportedByDefault")
    public Boolean getSupportedByDefault() {
        return supportedByDefault;
    }
    public void setSupportedByDefault(Boolean supportedByDefault) {
        this.supportedByDefault = supportedByDefault;
    }

    /**
    * Userstore attribute mappings.
    **/
    @ApiModelProperty(value = "Userstore attribute mappings.")
    @JsonProperty("attributeMapping")
    public List<AttributeMappingDTO> getAttributeMapping() {
        return attributeMapping;
    }
    public void setAttributeMapping(List<AttributeMappingDTO> attributeMapping) {
        this.attributeMapping = attributeMapping;
    }

    /**
    * Define any additional properties if required.
    **/
    @ApiModelProperty(value = "Define any additional properties if required.")
    @JsonProperty("properties")
    public List<PropertyDTO> getProperties() {
        return properties;
    }
    public void setProperties(List<PropertyDTO> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class LocalClaimResDTO {\n");
        
        sb.append("    id: ").append(id).append("\n");
        sb.append("    claimURI: ").append(claimURI).append("\n");
        sb.append("    dialectURI: ").append(dialectURI).append("\n");
        sb.append("    description: ").append(description).append("\n");
        sb.append("    displayOrder: ").append(displayOrder).append("\n");
        sb.append("    displayName: ").append(displayName).append("\n");
        sb.append("    readOnly: ").append(readOnly).append("\n");
        sb.append("    regEx: ").append(regEx).append("\n");
        sb.append("    required: ").append(required).append("\n");
        sb.append("    supportedByDefault: ").append(supportedByDefault).append("\n");
        sb.append("    attributeMapping: ").append(attributeMapping).append("\n");
        sb.append("    properties: ").append(properties).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
