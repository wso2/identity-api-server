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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * The value a field is compared with. It is either a literal value given in the condition, or the value of another field, read when the flow runs. If the other field has no value, the condition does not match.
 **/
@ApiModel(description = "The value a field is compared with. It is either a literal value given in the condition, or the value of another field, read when the flow runs. If the other field has no value, the condition does not match.")
public class ConditionValue  {
  
@XmlType(name="TypeEnum")
@XmlEnum(String.class)
public enum TypeEnum {

    @XmlEnumValue("literal") LITERAL(String.valueOf("literal")),
    @XmlEnumValue("field") FIELD(String.valueOf("field"));


    private String value;

    TypeEnum(String v) {
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
    private Object literal;
    private FieldReference field;

    /**
    * Whether the value is a literal value or another field.
    **/
    public ConditionValue type(TypeEnum type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "literal", required = true, value = "Whether the value is a literal value or another field.")
    @JsonProperty("type")
    @Valid
    @NotNull(message = "Property type cannot be null.")

    public TypeEnum getType() {
        return type;
    }
    public void setType(TypeEnum type) {
        this.type = type;
    }

    /**
    * The literal value, when type is literal. An array of values for the in and notIn operators, and a single value for all other operators.
    **/
    public ConditionValue literal(Object literal) {

        this.literal = literal;
        return this;
    }
    
    @ApiModelProperty(value = "The literal value, when type is literal. An array of values for the in and notIn operators, and a single value for all other operators.")
    @JsonProperty("literal")
    @Valid
    public Object getLiteral() {
        return literal;
    }
    public void setLiteral(Object literal) {
        this.literal = literal;
    }

    /**
    * The field whose value is used, when type is field.
    **/
    public ConditionValue field(FieldReference field) {

        this.field = field;
        return this;
    }
    
    @ApiModelProperty(value = "The field whose value is used, when type is field.")
    @JsonProperty("field")
    @Valid
    public FieldReference getField() {
        return field;
    }
    public void setField(FieldReference field) {
        this.field = field;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConditionValue other = (ConditionValue) o;
        return Objects.equals(this.type, other.type) &&
            Objects.equals(this.literal, other.literal) &&
            Objects.equals(this.field, other.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, literal, field);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConditionValue {\n");
        
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    literal: ").append(toIndentedString(literal)).append("\n");
        sb.append("    field: ").append(toIndentedString(field)).append("\n");
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
