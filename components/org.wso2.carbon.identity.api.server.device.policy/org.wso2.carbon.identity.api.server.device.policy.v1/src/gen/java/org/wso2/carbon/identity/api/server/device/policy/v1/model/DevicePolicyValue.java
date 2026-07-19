/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.device.policy.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.device.policy.v1.model.DevicePolicyLink;
import org.wso2.carbon.identity.api.server.device.policy.v1.model.DevicePolicyValueObject;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class DevicePolicyValue  {
  

@XmlType(name="InputTypeEnum")
@XmlEnum(String.class)
public enum InputTypeEnum {

    @XmlEnumValue("input") INPUT(String.valueOf("input")), @XmlEnumValue("options") OPTIONS(String.valueOf("options"));


    private String value;

    InputTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static InputTypeEnum fromValue(String value) {
        for (InputTypeEnum b : InputTypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private InputTypeEnum inputType;

@XmlType(name="ValueTypeEnum")
@XmlEnum(String.class)
public enum ValueTypeEnum {

    @XmlEnumValue("string") STRING(String.valueOf("string")), @XmlEnumValue("number") NUMBER(String.valueOf("number")), @XmlEnumValue("boolean") BOOLEAN(String.valueOf("boolean")), @XmlEnumValue("date") DATE(String.valueOf("date")), @XmlEnumValue("reference") REFERENCE(String.valueOf("reference")), @XmlEnumValue("symbolic") SYMBOLIC(String.valueOf("symbolic"));


    private String value;

    ValueTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ValueTypeEnum fromValue(String value) {
        for (ValueTypeEnum b : ValueTypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private ValueTypeEnum valueType;
    private List<DevicePolicyValueObject> values = null;

    private List<DevicePolicyLink> links = null;


    /**
    **/
    public DevicePolicyValue inputType(InputTypeEnum inputType) {

        this.inputType = inputType;
        return this;
    }
    
    @ApiModelProperty(example = "options", value = "")
    @JsonProperty("inputType")
    @Valid
    public InputTypeEnum getInputType() {
        return inputType;
    }
    public void setInputType(InputTypeEnum inputType) {
        this.inputType = inputType;
    }

    /**
    **/
    public DevicePolicyValue valueType(ValueTypeEnum valueType) {

        this.valueType = valueType;
        return this;
    }
    
    @ApiModelProperty(example = "string", value = "")
    @JsonProperty("valueType")
    @Valid
    public ValueTypeEnum getValueType() {
        return valueType;
    }
    public void setValueType(ValueTypeEnum valueType) {
        this.valueType = valueType;
    }

    /**
    **/
    public DevicePolicyValue values(List<DevicePolicyValueObject> values) {

        this.values = values;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("values")
    @Valid
    public List<DevicePolicyValueObject> getValues() {
        return values;
    }
    public void setValues(List<DevicePolicyValueObject> values) {
        this.values = values;
    }

    public DevicePolicyValue addValuesItem(DevicePolicyValueObject valuesItem) {
        if (this.values == null) {
            this.values = new ArrayList<>();
        }
        this.values.add(valuesItem);
        return this;
    }

        /**
    **/
    public DevicePolicyValue links(List<DevicePolicyLink> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("links")
    @Valid
    public List<DevicePolicyLink> getLinks() {
        return links;
    }
    public void setLinks(List<DevicePolicyLink> links) {
        this.links = links;
    }

    public DevicePolicyValue addLinksItem(DevicePolicyLink linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
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
        DevicePolicyValue devicePolicyValue = (DevicePolicyValue) o;
        return Objects.equals(this.inputType, devicePolicyValue.inputType) &&
            Objects.equals(this.valueType, devicePolicyValue.valueType) &&
            Objects.equals(this.values, devicePolicyValue.values) &&
            Objects.equals(this.links, devicePolicyValue.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputType, valueType, values, links);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DevicePolicyValue {\n");
        
        sb.append("    inputType: ").append(toIndentedString(inputType)).append("\n");
        sb.append("    valueType: ").append(toIndentedString(valueType)).append("\n");
        sb.append("    values: ").append(toIndentedString(values)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

