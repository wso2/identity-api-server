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
import javax.validation.constraints.*;

/**
 * A variable the flow keeps.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "A variable the flow keeps.")
public class FlowVariableInput  {
  
    private String name;

@XmlType(name="DataTypeEnum")
@XmlEnum(String.class)
public enum DataTypeEnum {

    @XmlEnumValue("STRING") STRING(String.valueOf("STRING")),
    @XmlEnumValue("INTEGER") INTEGER(String.valueOf("INTEGER")),
    @XmlEnumValue("DECIMAL") DECIMAL(String.valueOf("DECIMAL")),
    @XmlEnumValue("BOOLEAN") BOOLEAN(String.valueOf("BOOLEAN")),
    @XmlEnumValue("STRING_ARRAY") STRING_ARRAY(String.valueOf("STRING_ARRAY")),
    @XmlEnumValue("NUMBER_ARRAY") NUMBER_ARRAY(String.valueOf("NUMBER_ARRAY"));


    private String value;

    DataTypeEnum(String v) {
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
    public static DataTypeEnum fromValue(String value) {
        for (DataTypeEnum b : DataTypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private DataTypeEnum dataType;
    private Object value;
    private Boolean clientWritable = false;

    /**
    * Name of the variable, unique within the flow. Letters and digits only, up to 255 characters.
    **/
    public FlowVariableInput name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "plan", required = true, value = "Name of the variable, unique within the flow. Letters and digits only, up to 255 characters.")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Type of value the variable holds.
    **/
    public FlowVariableInput dataType(DataTypeEnum dataType) {

        this.dataType = dataType;
        return this;
    }
    
    @ApiModelProperty(example = "STRING", required = true, value = "Type of value the variable holds.")
    @JsonProperty("dataType")
    @Valid
    @NotNull(message = "Property dataType cannot be null.")

    public DataTypeEnum getDataType() {
        return dataType;
    }
    public void setDataType(DataTypeEnum dataType) {
        this.dataType = dataType;
    }

    /**
    * Value every run of the flow starts from, of the variable's type: a string, a number, a boolean, or an array of strings or numbers. Absent when the variable starts without one.
    **/
    public FlowVariableInput value(Object value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(example = "basic", value = "Value every run of the flow starts from, of the variable's type: a string, a number, a boolean, or an array of strings or numbers. Absent when the variable starts without one.")
    @JsonProperty("value")
    @Valid
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }

    /**
    * Whether the user may set the variable when the flow starts. Its value is then accepted from the variables object of the inputs that start the flow, and ignored on later requests.
    **/
    public FlowVariableInput clientWritable(Boolean clientWritable) {

        this.clientWritable = clientWritable;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Whether the user may set the variable when the flow starts. Its value is then accepted from the variables object of the inputs that start the flow, and ignored on later requests.")
    @JsonProperty("clientWritable")
    @Valid
    public Boolean getClientWritable() {
        return clientWritable;
    }
    public void setClientWritable(Boolean clientWritable) {
        this.clientWritable = clientWritable;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowVariableInput flowVariableInput = (FlowVariableInput) o;
        return Objects.equals(this.name, flowVariableInput.name) &&
            Objects.equals(this.dataType, flowVariableInput.dataType) &&
            Objects.equals(this.value, flowVariableInput.value) &&
            Objects.equals(this.clientWritable, flowVariableInput.clientWritable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dataType, value, clientWritable);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowVariableInput {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    clientWritable: ").append(toIndentedString(clientWritable)).append("\n");
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

