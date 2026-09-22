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

package org.wso2.carbon.identity.api.server.flow.management.v1;

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
/**
 * A single comparison against the flow execution context
 **/
@ApiModel(description = "A single comparison against the flow execution context")
public class Expression  {
  
    private String field;
    private String key;

@XmlType(name="OperatorEnum")
@XmlEnum(String.class)
public enum OperatorEnum {

    @XmlEnumValue("equals") EQUALS(String.valueOf("equals")), @XmlEnumValue("notEquals") NOTEQUALS(String.valueOf("notEquals")), @XmlEnumValue("contains") CONTAINS(String.valueOf("contains")), @XmlEnumValue("notContains") NOTCONTAINS(String.valueOf("notContains")), @XmlEnumValue("startsWith") STARTSWITH(String.valueOf("startsWith")), @XmlEnumValue("endsWith") ENDSWITH(String.valueOf("endsWith")), @XmlEnumValue("greaterThan") GREATERTHAN(String.valueOf("greaterThan")), @XmlEnumValue("lessThan") LESSTHAN(String.valueOf("lessThan")), @XmlEnumValue("in") IN(String.valueOf("in")), @XmlEnumValue("notIn") NOTIN(String.valueOf("notIn"));


    private String value;

    OperatorEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static OperatorEnum fromValue(String value) {
        for (OperatorEnum b : OperatorEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private OperatorEnum operator;
    private String value;
    private List<String> values = null;


    /**
    * One of the field names published in ruleMetaData
    **/
    public Expression field(String field) {

        this.field = field;
        return this;
    }
    
    @ApiModelProperty(example = "user.claims", required = true, value = "One of the field names published in ruleMetaData")
    @JsonProperty("field")
    @Valid
    @NotNull(message = "Property field cannot be null.")

    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }

    /**
    * Selects a value within the field, for fields that name a family of values rather than one
    **/
    public Expression key(String key) {

        this.key = key;
        return this;
    }
    
    @ApiModelProperty(example = "http://wso2.org/claims/country", value = "Selects a value within the field, for fields that name a family of values rather than one")
    @JsonProperty("key")
    @Valid
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public Expression operator(OperatorEnum operator) {

        this.operator = operator;
        return this;
    }
    
    @ApiModelProperty(example = "equals", required = true, value = "")
    @JsonProperty("operator")
    @Valid
    @NotNull(message = "Property operator cannot be null.")

    public OperatorEnum getOperator() {
        return operator;
    }
    public void setOperator(OperatorEnum operator) {
        this.operator = operator;
    }

    public Expression value(String value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(example = "LK", value = "The value the field is compared against. Its type is resolved from the field's own definition in ruleMetaData, as it is for action rules. Omitted for in and notIn, which carry values instead.")
    @JsonProperty("value")
    @Valid
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    /**
    * The set of values for in and notIn. Membership is tested against the whole set, so a multi valued field matches when the two sets intersect.
    **/
    public Expression values(List<String> values) {

        this.values = values;
        return this;
    }
    
    @ApiModelProperty(example = "[\"LK\",\"IN\"]", value = "The set of values for in and notIn. Membership is tested against the whole set, so a multi valued field matches when the two sets intersect.")
    @JsonProperty("values")
    @Valid
    public List<String> getValues() {
        return values;
    }
    public void setValues(List<String> values) {
        this.values = values;
    }

    public Expression addValuesItem(String valuesItem) {

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
        Expression expression = (Expression) o;
        return Objects.equals(this.field, expression.field) &&
            Objects.equals(this.key, expression.key) &&
            Objects.equals(this.operator, expression.operator) &&
            Objects.equals(this.value, expression.value) &&
            Objects.equals(this.values, expression.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, key, operator, value, values);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Expression {\n");
        
        sb.append("    field: ").append(toIndentedString(field)).append("\n");
        sb.append("    key: ").append(toIndentedString(key)).append("\n");
        sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
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
