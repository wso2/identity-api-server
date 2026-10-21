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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.flow.management.v1.RuleLink;
import org.wso2.carbon.identity.api.server.flow.management.v1.RuleValueOption;
import javax.validation.constraints.*;

/**
 * How a value is entered: typed in, or picked from a list given here or fetched from the linked endpoints. Also describes how a field&#39;s qualifier is picked; absent on a field that names a single value. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "How a value is entered: typed in, or picked from a list given here or fetched from the linked endpoints. Also describes how a field's qualifier is picked; absent on a field that names a single value. ")
public class RuleValue  {
  

@XmlType(name="InputTypeEnum")
@XmlEnum(String.class)
public enum InputTypeEnum {

    @XmlEnumValue("input") INPUT(String.valueOf("input")), @XmlEnumValue("options") OPTIONS(String.valueOf("options"));


    private String value;

    InputTypeEnum(String v) {
        value = v;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
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

    @XmlEnumValue("string") STRING(String.valueOf("string")), @XmlEnumValue("number") NUMBER(String.valueOf("number")), @XmlEnumValue("boolean") BOOLEAN(String.valueOf("boolean")), @XmlEnumValue("date") DATE(String.valueOf("date")), @XmlEnumValue("reference") REFERENCE(String.valueOf("reference"));


    private String value;

    ValueTypeEnum(String v) {
        value = v;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
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
    private String valueReferenceAttribute;
    private String valueDisplayAttribute;
    private List<RuleLink> links = null;

    private List<RuleValueOption> values = null;


    /**
    * input for a value typed in, options for one picked from a list.
    **/
    public RuleValue inputType(InputTypeEnum inputType) {

        this.inputType = inputType;
        return this;
    }
    
    @ApiModelProperty(example = "options", value = "input for a value typed in, options for one picked from a list.")
    @JsonProperty("inputType")
    @Valid
    public InputTypeEnum getInputType() {
        return inputType;
    }
    public void setInputType(InputTypeEnum inputType) {
        this.inputType = inputType;
    }

    /**
    * The type of the value; reference for the id of a resource listed by the links.
    **/
    public RuleValue valueType(ValueTypeEnum valueType) {

        this.valueType = valueType;
        return this;
    }
    
    @ApiModelProperty(example = "reference", value = "The type of the value; reference for the id of a resource listed by the links.")
    @JsonProperty("valueType")
    @Valid
    public ValueTypeEnum getValueType() {
        return valueType;
    }
    public void setValueType(ValueTypeEnum valueType) {
        this.valueType = valueType;
    }

    /**
    * The attribute of a listed resource that is used as the value.
    **/
    public RuleValue valueReferenceAttribute(String valueReferenceAttribute) {

        this.valueReferenceAttribute = valueReferenceAttribute;
        return this;
    }
    
    @ApiModelProperty(example = "id", value = "The attribute of a listed resource that is used as the value.")
    @JsonProperty("valueReferenceAttribute")
    @Valid
    public String getValueReferenceAttribute() {
        return valueReferenceAttribute;
    }
    public void setValueReferenceAttribute(String valueReferenceAttribute) {
        this.valueReferenceAttribute = valueReferenceAttribute;
    }

    /**
    * The attribute of a listed resource that is shown for it.
    **/
    public RuleValue valueDisplayAttribute(String valueDisplayAttribute) {

        this.valueDisplayAttribute = valueDisplayAttribute;
        return this;
    }
    
    @ApiModelProperty(example = "name", value = "The attribute of a listed resource that is shown for it.")
    @JsonProperty("valueDisplayAttribute")
    @Valid
    public String getValueDisplayAttribute() {
        return valueDisplayAttribute;
    }
    public void setValueDisplayAttribute(String valueDisplayAttribute) {
        this.valueDisplayAttribute = valueDisplayAttribute;
    }

    /**
    * The endpoints that list or filter the values to pick from.
    **/
    public RuleValue links(List<RuleLink> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(value = "The endpoints that list or filter the values to pick from.")
    @JsonProperty("links")
    @Valid
    public List<RuleLink> getLinks() {
        return links;
    }
    public void setLinks(List<RuleLink> links) {
        this.links = links;
    }

    public RuleValue addLinksItem(RuleLink linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<RuleLink>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    * The values to pick from, when they are listed here.
    **/
    public RuleValue values(List<RuleValueOption> values) {

        this.values = values;
        return this;
    }
    
    @ApiModelProperty(value = "The values to pick from, when they are listed here.")
    @JsonProperty("values")
    @Valid
    public List<RuleValueOption> getValues() {
        return values;
    }
    public void setValues(List<RuleValueOption> values) {
        this.values = values;
    }

    public RuleValue addValuesItem(RuleValueOption valuesItem) {
        if (this.values == null) {
            this.values = new ArrayList<RuleValueOption>();
        }
        this.values.add(valuesItem);
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
        RuleValue ruleValue = (RuleValue) o;
        return Objects.equals(this.inputType, ruleValue.inputType) &&
            Objects.equals(this.valueType, ruleValue.valueType) &&
            Objects.equals(this.valueReferenceAttribute, ruleValue.valueReferenceAttribute) &&
            Objects.equals(this.valueDisplayAttribute, ruleValue.valueDisplayAttribute) &&
            Objects.equals(this.links, ruleValue.links) &&
            Objects.equals(this.values, ruleValue.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputType, valueType, valueReferenceAttribute, valueDisplayAttribute, links, values);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RuleValue {\n");
        
        sb.append("    inputType: ").append(toIndentedString(inputType)).append("\n");
        sb.append("    valueType: ").append(toIndentedString(valueType)).append("\n");
        sb.append("    valueReferenceAttribute: ").append(toIndentedString(valueReferenceAttribute)).append("\n");
        sb.append("    valueDisplayAttribute: ").append(toIndentedString(valueDisplayAttribute)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    values: ").append(toIndentedString(values)).append("\n");
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

