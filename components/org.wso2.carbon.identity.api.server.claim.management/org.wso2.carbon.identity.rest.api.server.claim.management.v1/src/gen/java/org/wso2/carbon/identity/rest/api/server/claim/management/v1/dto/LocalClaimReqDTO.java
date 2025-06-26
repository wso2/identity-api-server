/*
 * Copyright (c) 2021-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto;

import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
    * Local claim request.
    **/
@ApiModel(description = "Local claim request.")
public class LocalClaimReqDTO {

    @Valid 
    @NotNull(message = "Property claimURI cannot be null.") 
    private String claimURI = null;

    @Valid
    private String description = null;

    @Valid 
    private Integer displayOrder = null;

    @Valid 
    @NotNull(message = "Property displayName cannot be null.") 
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
    private DataType dataType = null;

    @Valid
    private String[] subAttributes = null;

    @Valid
    private LabelValueDTO[] canonicalValues = null;

    @Valid
    private Boolean multiValued = null;

    public enum UniquenessScopeEnum {
         NONE,  WITHIN_USERSTORE,  ACROSS_USERSTORES, 
    };
    @Valid 
    private UniquenessScopeEnum uniquenessScope = null;

    public enum SharedProfileValueResolvingMethodEnum {
        FromOrigin, FromSharedProfile, FromFirstFoundInHierarchy,
    };

    @Valid
    private SharedProfileValueResolvingMethodEnum sharedProfileValueResolvingMethod = null;

    @Valid
    @NotNull(message = "Property attributeMapping cannot be null.") 
    private List<AttributeMappingDTO> attributeMapping = new ArrayList<AttributeMappingDTO>();

    @Valid 
    private List<PropertyDTO> properties = new ArrayList<PropertyDTO>();

    @Valid 
    private ProfilesDTO profiles = null;

    @Valid
    private InputFormatDTO inputFormat = null;

    /**
    * A unique URI specific to the claim.
    **/
    @ApiModelProperty(required = true, value = "A unique URI specific to the claim.")
    @JsonProperty("claimURI")
    public String getClaimURI() {
        return claimURI;
    }
    public void setClaimURI(String claimURI) {
        this.claimURI = claimURI;
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
    @ApiModelProperty(required = true, value = "Name of the claim to be displayed in the UI.")
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
     * Specifies the type of data which the claim holds.
     **/
    @ApiModelProperty(value = "Specifies the type of data stored in the corresponding claim value.")
    @JsonProperty("dataType")
    public DataType getDataType() {

        return dataType;
    }
    public void setDataType(DataType dataType) {

        this.dataType = dataType;
    }

    /**
     * The sub attributes of the complex attribute.
     **/
    @ApiModelProperty(value = "The sub attributes of the complex attribute.")
    @JsonProperty("subAttributes")
    public String[] getSubAttributes() {

        return subAttributes != null ? subAttributes.clone() : new String[0];
    }
    public void setSubAttributes(String[] subAttributes) {

        this.subAttributes = subAttributes != null ? subAttributes.clone() : null;
    }

    /**
     * The possible values for the attribute.
     **/
    @ApiModelProperty(value = "The possible values for the attribute.")
    @JsonProperty("canonicalValues")
    public LabelValueDTO[] getCanonicalValues() {

        return canonicalValues != null ? canonicalValues.clone() : new LabelValueDTO[0];
    }
    public void setCanonicalValues(LabelValueDTO[] canonicalValues) {

        this.canonicalValues = canonicalValues != null ? canonicalValues.clone() : null;
    }

    /**
     * Specifies if the claim can hold multiple values.
     **/
    @ApiModelProperty(value = "Specifies if the claim can hold multiple values.")
    @JsonProperty("multiValued")
    public Boolean getMultiValued() {
        return multiValued;
    }
    public void setMultiValued(Boolean multiValued) {
        this.multiValued = multiValued;
    }

    /**
    * Specifies the scope of uniqueness validation for the claim value.
    **/
    @ApiModelProperty(value = "Specifies the scope of uniqueness validation for the claim value.")
    @JsonProperty("uniquenessScope")
    public UniquenessScopeEnum getUniquenessScope() {
        return uniquenessScope;
    }
    public void setUniquenessScope(UniquenessScopeEnum uniquenessScope) {
        this.uniquenessScope = uniquenessScope;
    }

    /**
     * Specifies claim value resolving method for shared user profile.
     **/
    @ApiModelProperty(value = "Specifies claim value resolving method for shared user profile.")
    @JsonProperty("sharedProfileValueResolvingMethod")
    public SharedProfileValueResolvingMethodEnum getSharedProfileValueResolvingMethod() {
        return sharedProfileValueResolvingMethod;
    }

    public void setSharedProfileValueResolvingMethod(
            SharedProfileValueResolvingMethodEnum sharedProfileValueResolvingMethod) {
        this.sharedProfileValueResolvingMethod = sharedProfileValueResolvingMethod;
    }

    /**
    * Userstore attribute mappings.
    **/
    @ApiModelProperty(required = true, value = "Userstore attribute mappings.")
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

    /**
    **/
    @ApiModelProperty(value = "Define any attribute profiles.")
    @JsonProperty("profiles")
    public ProfilesDTO getProfiles() {
        return profiles;
    }
    public void setProfiles(ProfilesDTO profiles) {
        this.profiles = profiles;
    }

    @ApiModelProperty(value = "The input format of the attribute.")
    @JsonProperty("inputFormat")
    public InputFormatDTO getInputFormat() {
        return inputFormat;
    }
    public void setInputFormat(InputFormatDTO inputFormat) {
        this.inputFormat = inputFormat;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class LocalClaimReqDTO {\n");
        
        sb.append("    claimURI: ").append(claimURI).append("\n");
        sb.append("    description: ").append(description).append("\n");
        sb.append("    displayOrder: ").append(displayOrder).append("\n");
        sb.append("    displayName: ").append(displayName).append("\n");
        sb.append("    readOnly: ").append(readOnly).append("\n");
        sb.append("    regEx: ").append(regEx).append("\n");
        sb.append("    required: ").append(required).append("\n");
        sb.append("    supportedByDefault: ").append(supportedByDefault).append("\n");
        sb.append("    dataType: ").append(dataType).append("\n");
        sb.append("    subAttributes: ").append(Arrays.toString(subAttributes)).append("\n");
        sb.append("    canonicalValues: ").append(Arrays.toString(canonicalValues)).append("\n");
        sb.append("    multiValued: ").append(multiValued).append("\n");
        sb.append("    uniquenessScope: ").append(uniquenessScope).append("\n");
        sb.append("    sharedProfileValueResolvingMethod: ").append(sharedProfileValueResolvingMethod).append("\n");
        sb.append("    attributeMapping: ").append(attributeMapping).append("\n");
        sb.append("    properties: ").append(properties).append("\n");
        sb.append("    profiles: ").append(profiles).append("\n");
        sb.append("    inputFormat: ").append(inputFormat).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
