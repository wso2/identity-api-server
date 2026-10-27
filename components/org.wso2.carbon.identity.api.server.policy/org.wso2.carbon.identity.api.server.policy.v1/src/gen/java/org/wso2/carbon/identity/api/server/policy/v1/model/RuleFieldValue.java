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

package org.wso2.carbon.identity.api.server.policy.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleFieldValueLink;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleFieldValueOption;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RuleFieldValue  {
  

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

    @XmlEnumValue("string") STRING(String.valueOf("string")), @XmlEnumValue("number") NUMBER(String.valueOf("number")), @XmlEnumValue("boolean") BOOLEAN(String.valueOf("boolean")), @XmlEnumValue("date") DATE(String.valueOf("date")), @XmlEnumValue("reference") REFERENCE(String.valueOf("reference"));


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
    private String valueReferenceAttribute;
    private String valueDisplayAttribute;
    private List<RuleFieldValueLink> links = null;

    private List<RuleFieldValueOption> values = null;


    /**
    * Whether the value is entered directly or selected from options.
    **/
    public RuleFieldValue inputType(InputTypeEnum inputType) {

        this.inputType = inputType;
        return this;
    }
    
    @ApiModelProperty(value = "Whether the value is entered directly or selected from options.")
    @JsonProperty("inputType")
    @Valid
    public InputTypeEnum getInputType() {
        return inputType;
    }
    public void setInputType(InputTypeEnum inputType) {
        this.inputType = inputType;
    }

    /**
    * Data type of the value.
    **/
    public RuleFieldValue valueType(ValueTypeEnum valueType) {

        this.valueType = valueType;
        return this;
    }
    
    @ApiModelProperty(value = "Data type of the value.")
    @JsonProperty("valueType")
    @Valid
    public ValueTypeEnum getValueType() {
        return valueType;
    }
    public void setValueType(ValueTypeEnum valueType) {
        this.valueType = valueType;
    }

    /**
    * Attribute of the options data used as the value. Only available when &#39;valueType&#39; is &#39;reference&#39;.
    **/
    public RuleFieldValue valueReferenceAttribute(String valueReferenceAttribute) {

        this.valueReferenceAttribute = valueReferenceAttribute;
        return this;
    }
    
    @ApiModelProperty(example = "id", value = "Attribute of the options data used as the value. Only available when 'valueType' is 'reference'.")
    @JsonProperty("valueReferenceAttribute")
    @Valid
    public String getValueReferenceAttribute() {
        return valueReferenceAttribute;
    }
    public void setValueReferenceAttribute(String valueReferenceAttribute) {
        this.valueReferenceAttribute = valueReferenceAttribute;
    }

    /**
    * Attribute of the options data shown as the label. Only available when &#39;valueType&#39; is &#39;reference&#39;.
    **/
    public RuleFieldValue valueDisplayAttribute(String valueDisplayAttribute) {

        this.valueDisplayAttribute = valueDisplayAttribute;
        return this;
    }
    
    @ApiModelProperty(example = "name", value = "Attribute of the options data shown as the label. Only available when 'valueType' is 'reference'.")
    @JsonProperty("valueDisplayAttribute")
    @Valid
    public String getValueDisplayAttribute() {
        return valueDisplayAttribute;
    }
    public void setValueDisplayAttribute(String valueDisplayAttribute) {
        this.valueDisplayAttribute = valueDisplayAttribute;
    }

    /**
    * Endpoints to retrieve or search for options. Only available when &#39;valueType&#39; is &#39;reference&#39;.
    **/
    public RuleFieldValue links(List<RuleFieldValueLink> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(value = "Endpoints to retrieve or search for options. Only available when 'valueType' is 'reference'.")
    @JsonProperty("links")
    @Valid
    public List<RuleFieldValueLink> getLinks() {
        return links;
    }
    public void setLinks(List<RuleFieldValueLink> links) {
        this.links = links;
    }

    public RuleFieldValue addLinksItem(RuleFieldValueLink linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    * Selectable values of an options field.
    **/
    public RuleFieldValue values(List<RuleFieldValueOption> values) {

        this.values = values;
        return this;
    }
    
    @ApiModelProperty(value = "Selectable values of an options field.")
    @JsonProperty("values")
    @Valid
    public List<RuleFieldValueOption> getValues() {
        return values;
    }
    public void setValues(List<RuleFieldValueOption> values) {
        this.values = values;
    }

    public RuleFieldValue addValuesItem(RuleFieldValueOption valuesItem) {
        if (this.values == null) {
            this.values = new ArrayList<>();
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
        RuleFieldValue ruleFieldValue = (RuleFieldValue) o;
        return Objects.equals(this.inputType, ruleFieldValue.inputType) &&
            Objects.equals(this.valueType, ruleFieldValue.valueType) &&
            Objects.equals(this.valueReferenceAttribute, ruleFieldValue.valueReferenceAttribute) &&
            Objects.equals(this.valueDisplayAttribute, ruleFieldValue.valueDisplayAttribute) &&
            Objects.equals(this.links, ruleFieldValue.links) &&
            Objects.equals(this.values, ruleFieldValue.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputType, valueType, valueReferenceAttribute, valueDisplayAttribute, links, values);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RuleFieldValue {\n");
        
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

