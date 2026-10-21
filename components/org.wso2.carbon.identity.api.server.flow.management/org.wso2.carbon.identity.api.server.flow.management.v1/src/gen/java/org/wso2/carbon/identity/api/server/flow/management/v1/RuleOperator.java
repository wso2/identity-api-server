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
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RuleOperator  {
  
    private String name;
    private String displayName;

@XmlType(name="ValueInputEnum")
@XmlEnum(String.class)
public enum ValueInputEnum {

    @XmlEnumValue("none") NONE(String.valueOf("none")), @XmlEnumValue("single") SINGLE(String.valueOf("single")), @XmlEnumValue("list") LIST(String.valueOf("list"));


    private String value;

    ValueInputEnum(String v) {
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
    public static ValueInputEnum fromValue(String value) {
        for (ValueInputEnum b : ValueInputEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private ValueInputEnum valueInput;

    /**
    * Name of the operator.
    **/
    public RuleOperator name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "equals", value = "Name of the operator.")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Display name of the operator.
    **/
    public RuleOperator displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "equals", value = "Display name of the operator.")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    * The kind of value a condition with this operator takes: &#x60;none&#x60; for operators that check only the field, such as &#x60;isBlank&#x60; and &#x60;isEmpty&#x60;, &#x60;list&#x60; for &#x60;in&#x60; and &#x60;notIn&#x60;, and &#x60;single&#x60; for all other operators. 
    **/
    public RuleOperator valueInput(ValueInputEnum valueInput) {

        this.valueInput = valueInput;
        return this;
    }
    
    @ApiModelProperty(example = "single", value = "The kind of value a condition with this operator takes: `none` for operators that check only the field, such as `isBlank` and `isEmpty`, `list` for `in` and `notIn`, and `single` for all other operators. ")
    @JsonProperty("valueInput")
    @Valid
    public ValueInputEnum getValueInput() {
        return valueInput;
    }
    public void setValueInput(ValueInputEnum valueInput) {
        this.valueInput = valueInput;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RuleOperator ruleOperator = (RuleOperator) o;
        return Objects.equals(this.name, ruleOperator.name) &&
            Objects.equals(this.displayName, ruleOperator.displayName) &&
            Objects.equals(this.valueInput, ruleOperator.valueInput);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, displayName, valueInput);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RuleOperator {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    valueInput: ").append(toIndentedString(valueInput)).append("\n");
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

