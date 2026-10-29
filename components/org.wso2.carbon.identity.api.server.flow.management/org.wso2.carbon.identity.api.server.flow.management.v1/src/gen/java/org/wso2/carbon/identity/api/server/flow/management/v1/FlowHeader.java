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
 * A header the flow reads from each request. A request may repeat it, so it holds an array, and a request without it keeps the value an earlier request gave.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "A header the flow reads from each request. A request may repeat it, so it holds an array, and a request without it keeps the value an earlier request gave.")
public class FlowHeader  {
  
    private String name;

@XmlType(name="DataTypeEnum")
@XmlEnum(String.class)
public enum DataTypeEnum {

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

    /**
    * Name of the header.
    **/
    public FlowHeader name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "x-ab-variant", required = true, value = "Name of the header.")
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
    * Type of each value the request gives.
    **/
    public FlowHeader dataType(DataTypeEnum dataType) {

        this.dataType = dataType;
        return this;
    }
    
    @ApiModelProperty(example = "STRING_ARRAY", required = true, value = "Type of each value the request gives.")
    @JsonProperty("dataType")
    @Valid
    @NotNull(message = "Property dataType cannot be null.")

    public DataTypeEnum getDataType() {
        return dataType;
    }
    public void setDataType(DataTypeEnum dataType) {
        this.dataType = dataType;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowHeader flowHeader = (FlowHeader) o;
        return Objects.equals(this.name, flowHeader.name) &&
            Objects.equals(this.dataType, flowHeader.dataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dataType);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowHeader {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
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

