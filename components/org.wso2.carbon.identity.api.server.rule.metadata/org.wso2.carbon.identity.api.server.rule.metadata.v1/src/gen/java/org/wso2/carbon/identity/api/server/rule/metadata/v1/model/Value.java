/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.rule.metadata.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Link;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.model.ValueObject;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;

public class Value  {
  

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
    private List<Link> links = null;

    private List<ValueObject> values = null;


    /**
    * Defines how the field should be presented and populated in the rule configuration UI. This property indicates whether the field allows direct user input or if the values are selected from predefined options. Possible types include:    - \&quot;input\&quot;: Allows for direct user entry, such as text or numeric input.   - \&quot;options\&quot;: Provides a list of selectable values, often fetched from an external data source, enabling users to pick from predefined choices. 
    **/
    public Value inputType(InputTypeEnum inputType) {

        this.inputType = inputType;
        return this;
    }
    
    @ApiModelProperty(value = "Defines how the field should be presented and populated in the rule configuration UI. This property indicates whether the field allows direct user input or if the values are selected from predefined options. Possible types include:    - \"input\": Allows for direct user entry, such as text or numeric input.   - \"options\": Provides a list of selectable values, often fetched from an external data source, enabling users to pick from predefined choices. ")
    @JsonProperty("inputType")
    @Valid
    public InputTypeEnum getInputType() {
        return inputType;
    }
    public void setInputType(InputTypeEnum inputType) {
        this.inputType = inputType;
    }

    /**
    * Specifies the expected data type for the field’s value within a rule expression. This property defines how the field&#39;s value should be interpreted when used in rule conditions. Possible types include:   - \&quot;string\&quot;: Text value.   - \&quot;number\&quot;: Numerical value.   - \&quot;boolean\&quot;: True or false.   - \&quot;date\&quot;: Date value.   - \&quot;reference\&quot;: A reference to an external identifier, often used with options-type fields to indicate that the value is an ID or a unique attribute from related data.\&quot; 
    **/
    public Value valueType(ValueTypeEnum valueType) {

        this.valueType = valueType;
        return this;
    }
    
    @ApiModelProperty(value = "Specifies the expected data type for the field’s value within a rule expression. This property defines how the field's value should be interpreted when used in rule conditions. Possible types include:   - \"string\": Text value.   - \"number\": Numerical value.   - \"boolean\": True or false.   - \"date\": Date value.   - \"reference\": A reference to an external identifier, often used with options-type fields to indicate that the value is an ID or a unique attribute from related data.\" ")
    @JsonProperty("valueType")
    @Valid
    public ValueTypeEnum getValueType() {
        return valueType;
    }
    public void setValueType(ValueTypeEnum valueType) {
        this.valueType = valueType;
    }

    /**
    * The key attribute in the options data (e.g., &#39;id&#39;) used to represent the option&#39;s selected value in rule expressions. Only available when &#39;valueType&#39; is &#39;reference&#39;.
    **/
    public Value valueReferenceAttribute(String valueReferenceAttribute) {

        this.valueReferenceAttribute = valueReferenceAttribute;
        return this;
    }
    
    @ApiModelProperty(example = "id", value = "The key attribute in the options data (e.g., 'id') used to represent the option's selected value in rule expressions. Only available when 'valueType' is 'reference'.")
    @JsonProperty("valueReferenceAttribute")
    @Valid
    public String getValueReferenceAttribute() {
        return valueReferenceAttribute;
    }
    public void setValueReferenceAttribute(String valueReferenceAttribute) {
        this.valueReferenceAttribute = valueReferenceAttribute;
    }

    /**
    * The attribute to show as the label for each option in the dropdown (e.g., &#39;name&#39;) when listing options. Only available when &#39;valueType&#39; is &#39;reference&#39;.
    **/
    public Value valueDisplayAttribute(String valueDisplayAttribute) {

        this.valueDisplayAttribute = valueDisplayAttribute;
        return this;
    }
    
    @ApiModelProperty(example = "name", value = "The attribute to show as the label for each option in the dropdown (e.g., 'name') when listing options. Only available when 'valueType' is 'reference'.")
    @JsonProperty("valueDisplayAttribute")
    @Valid
    public String getValueDisplayAttribute() {
        return valueDisplayAttribute;
    }
    public void setValueDisplayAttribute(String valueDisplayAttribute) {
        this.valueDisplayAttribute = valueDisplayAttribute;
    }

    /**
    * Endpoints to retrieve or search for options dynamically. Included only when &#39;valueType&#39; is &#39;reference&#39;.
    **/
    public Value links(List<Link> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"href\":\"/scim2/roles?offset=0&limit=10\",\"method\":\"GET\",\"rel\":\"values\"},{\"href\":\"/scim2/roles/.search\",\"method\":\"GET\",\"rel\":\"filter\"}]", value = "Endpoints to retrieve or search for options dynamically. Included only when 'valueType' is 'reference'.")
    @JsonProperty("links")
    @Valid
    public List<Link> getLinks() {
        return links;
    }
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public Value addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<Link>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    * List of selectable values for options fields when &#39;valueType&#39; is &#39;string&#39;, &#39;number&#39;, &#39;boolean&#39;, or &#39;date&#39;.
    **/
    public Value values(List<ValueObject> values) {

        this.values = values;
        return this;
    }
    
    @ApiModelProperty(value = "List of selectable values for options fields when 'valueType' is 'string', 'number', 'boolean', or 'date'.")
    @JsonProperty("values")
    @Valid
    public List<ValueObject> getValues() {
        return values;
    }
    public void setValues(List<ValueObject> values) {
        this.values = values;
    }

    public Value addValuesItem(ValueObject valuesItem) {
        if (this.values == null) {
            this.values = new ArrayList<ValueObject>();
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
        Value value = (Value) o;
        return Objects.equals(this.inputType, value.inputType) &&
            Objects.equals(this.valueType, value.valueType) &&
            Objects.equals(this.valueReferenceAttribute, value.valueReferenceAttribute) &&
            Objects.equals(this.valueDisplayAttribute, value.valueDisplayAttribute) &&
            Objects.equals(this.links, value.links) &&
            Objects.equals(this.values, value.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputType, valueType, valueReferenceAttribute, valueDisplayAttribute, links, values);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Value {\n");
        
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

