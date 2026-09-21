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
 * The right hand side of an expression
 **/
@ApiModel(description = "The right hand side of an expression")
public class Value  {
  
@XmlType(name="TypeEnum")
@XmlEnum(String.class)
public enum TypeEnum {

    @XmlEnumValue("STRING") STRING(String.valueOf("STRING")), @XmlEnumValue("NUMBER") NUMBER(String.valueOf("NUMBER")), @XmlEnumValue("BOOLEAN") BOOLEAN(String.valueOf("BOOLEAN")), @XmlEnumValue("DATE_TIME") DATE_TIME(String.valueOf("DATE_TIME")), @XmlEnumValue("REFERENCE") REFERENCE(String.valueOf("REFERENCE")), @XmlEnumValue("RAW") RAW(String.valueOf("RAW")), @XmlEnumValue("LIST") LIST(String.valueOf("LIST"));


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
    private String value;
    private List<String> values = null;
    private String key;

    public Value type(TypeEnum type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "STRING", required = true, value = "")
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
    * The value, for every type except LIST
    **/
    public Value value(String value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(example = "LK", value = "The value, for every type except LIST")
    @JsonProperty("value")
    @Valid
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    /**
    * The values, for a type of LIST
    **/
    public Value values(List<String> values) {

        this.values = values;
        return this;
    }
    
    @ApiModelProperty(value = "The values, for a type of LIST")
    @JsonProperty("values")
    @Valid
    public List<String> getValues() {
        return values;
    }
    public void setValues(List<String> values) {
        this.values = values;
    }

    public Value addValuesItem(String valuesItem) {
        if (this.values == null) {
            this.values = new ArrayList<>();
        }
        this.values.add(valuesItem);
        return this;
    }

    /**
    * Key selecting a value within a REFERENCE field, where that field is keyed
    **/
    public Value key(String key) {

        this.key = key;
        return this;
    }
    
    @ApiModelProperty(example = "geoCountry", value = "Key selecting a value within a REFERENCE field, where that field is keyed")
    @JsonProperty("key")
    @Valid
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
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
        return Objects.equals(this.type, value.type) &&
            Objects.equals(this.value, value.value) &&
            Objects.equals(this.values, value.values) &&
            Objects.equals(this.key, value.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value, values, key);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Value {\n");
        
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    values: ").append(toIndentedString(values)).append("\n");
        sb.append("    key: ").append(toIndentedString(key)).append("\n");
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
