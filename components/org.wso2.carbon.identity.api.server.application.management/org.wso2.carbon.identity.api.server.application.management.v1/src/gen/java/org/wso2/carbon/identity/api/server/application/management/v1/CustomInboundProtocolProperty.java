/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class CustomInboundProtocolProperty  {
  
    private String name;

@XmlType(name="TypeEnum")
@XmlEnum(String.class)
public enum TypeEnum {

    @XmlEnumValue("STRING") STRING(String.valueOf("STRING")), @XmlEnumValue("BOOLEAN") BOOLEAN(String.valueOf("BOOLEAN")), @XmlEnumValue("INTEGER") INTEGER(String.valueOf("INTEGER"));


    private String value;

    TypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static TypeEnum fromValue(String value) {
        for (TypeEnum b : TypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private TypeEnum type;
    private Boolean required;
    private List<String> availableValues = null;

    private String defaultValue;
    private String validationRegex;
    private Integer displayOrder;
    private Boolean isConfidential = false;

    /**
    **/
    public CustomInboundProtocolProperty name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Encryption Algorithm", value = "")
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
    public CustomInboundProtocolProperty type(TypeEnum type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "STRING", value = "")
    @JsonProperty("type")
    @Valid
    public TypeEnum getType() {
        return type;
    }
    public void setType(TypeEnum type) {
        this.type = type;
    }

    /**
    **/
    public CustomInboundProtocolProperty required(Boolean required) {

        this.required = required;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("required")
    @Valid
    public Boolean getRequired() {
        return required;
    }
    public void setRequired(Boolean required) {
        this.required = required;
    }

    /**
    **/
    public CustomInboundProtocolProperty availableValues(List<String> availableValues) {

        this.availableValues = availableValues;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("availableValues")
    @Valid
    public List<String> getAvailableValues() {
        return availableValues;
    }
    public void setAvailableValues(List<String> availableValues) {
        this.availableValues = availableValues;
    }

    public CustomInboundProtocolProperty addAvailableValuesItem(String availableValuesItem) {
        if (this.availableValues == null) {
            this.availableValues = new ArrayList<>();
        }
        this.availableValues.add(availableValuesItem);
        return this;
    }

        /**
    **/
    public CustomInboundProtocolProperty defaultValue(String defaultValue) {

        this.defaultValue = defaultValue;
        return this;
    }
    
    @ApiModelProperty(example = "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p", value = "")
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
    public CustomInboundProtocolProperty validationRegex(String validationRegex) {

        this.validationRegex = validationRegex;
        return this;
    }
    
    @ApiModelProperty(example = "^[a-b][A-B]*", value = "")
    @JsonProperty("validationRegex")
    @Valid
    public String getValidationRegex() {
        return validationRegex;
    }
    public void setValidationRegex(String validationRegex) {
        this.validationRegex = validationRegex;
    }

    /**
    **/
    public CustomInboundProtocolProperty displayOrder(Integer displayOrder) {

        this.displayOrder = displayOrder;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "")
    @JsonProperty("displayOrder")
    @Valid
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
    **/
    public CustomInboundProtocolProperty isConfidential(Boolean isConfidential) {

        this.isConfidential = isConfidential;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("isConfidential")
    @Valid
    public Boolean getIsConfidential() {
        return isConfidential;
    }
    public void setIsConfidential(Boolean isConfidential) {
        this.isConfidential = isConfidential;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomInboundProtocolProperty customInboundProtocolProperty = (CustomInboundProtocolProperty) o;
        return Objects.equals(this.name, customInboundProtocolProperty.name) &&
            Objects.equals(this.type, customInboundProtocolProperty.type) &&
            Objects.equals(this.required, customInboundProtocolProperty.required) &&
            Objects.equals(this.availableValues, customInboundProtocolProperty.availableValues) &&
            Objects.equals(this.defaultValue, customInboundProtocolProperty.defaultValue) &&
            Objects.equals(this.validationRegex, customInboundProtocolProperty.validationRegex) &&
            Objects.equals(this.displayOrder, customInboundProtocolProperty.displayOrder) &&
            Objects.equals(this.isConfidential, customInboundProtocolProperty.isConfidential);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, required, availableValues, defaultValue, validationRegex, displayOrder, isConfidential);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CustomInboundProtocolProperty {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    required: ").append(toIndentedString(required)).append("\n");
        sb.append("    availableValues: ").append(toIndentedString(availableValues)).append("\n");
        sb.append("    defaultValue: ").append(toIndentedString(defaultValue)).append("\n");
        sb.append("    validationRegex: ").append(toIndentedString(validationRegex)).append("\n");
        sb.append("    displayOrder: ").append(toIndentedString(displayOrder)).append("\n");
        sb.append("    isConfidential: ").append(toIndentedString(isConfidential)).append("\n");
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

