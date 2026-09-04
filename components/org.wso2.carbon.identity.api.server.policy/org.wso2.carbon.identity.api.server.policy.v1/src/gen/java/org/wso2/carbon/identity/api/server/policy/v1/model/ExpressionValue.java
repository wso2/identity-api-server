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

package org.wso2.carbon.identity.api.server.policy.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ExpressionValue  {
  

@XmlType(name="TypeEnum")
@XmlEnum(String.class)
public enum TypeEnum {

    @XmlEnumValue("STRING") STRING(String.valueOf("STRING")), @XmlEnumValue("NUMBER") NUMBER(String.valueOf("NUMBER")), @XmlEnumValue("BOOLEAN") BOOLEAN(String.valueOf("BOOLEAN")), @XmlEnumValue("DATE_TIME") DATE_TIME(String.valueOf("DATE_TIME")), @XmlEnumValue("REFERENCE") REFERENCE(String.valueOf("REFERENCE")), @XmlEnumValue("RAW") RAW(String.valueOf("RAW")), @XmlEnumValue("LIST") LIST(String.valueOf("LIST")), @XmlEnumValue("SYMBOLIC") SYMBOLIC(String.valueOf("SYMBOLIC"));


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

    /**
    * The value type.
    **/
    public ExpressionValue type(TypeEnum type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "STRING", value = "The value type.")
    @JsonProperty("type")
    @Valid
    public TypeEnum getType() {
        return type;
    }
    public void setType(TypeEnum type) {
        this.type = type;
    }

    /**
    * The value to compare against.
    **/
    public ExpressionValue value(String value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(example = "ios", value = "The value to compare against.")
    @JsonProperty("value")
    @Valid
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExpressionValue expressionValue = (ExpressionValue) o;
        return Objects.equals(this.type, expressionValue.type) &&
            Objects.equals(this.value, expressionValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ExpressionValue {\n");
        
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

