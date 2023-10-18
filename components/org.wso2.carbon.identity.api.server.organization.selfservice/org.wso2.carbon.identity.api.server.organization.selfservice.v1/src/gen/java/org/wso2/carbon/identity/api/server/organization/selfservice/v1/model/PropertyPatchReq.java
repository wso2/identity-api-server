/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.selfservice.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.*;

/**
 * Self-service property patch request.
 **/

import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Self-service property patch request.")
public class PropertyPatchReq  {
  

@XmlType(name="OperationEnum")
@XmlEnum(String.class)
public enum OperationEnum {

    @XmlEnumValue("UPDATE") UPDATE(String.valueOf("UPDATE"));


    private String value;

    OperationEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static OperationEnum fromValue(String value) {
        for (OperationEnum b : OperationEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private OperationEnum operation;
    private List<PropertyReq> properties = new ArrayList<>();


    /**
    * Self-service properties patch operation.
    **/
    public PropertyPatchReq operation(OperationEnum operation) {

        this.operation = operation;
        return this;
    }
    
    @ApiModelProperty(example = "UPDATE", required = true, value = "Self-service properties patch operation.")
    @JsonProperty("operation")
    @Valid
    @NotNull(message = "Property operation cannot be null.")

    public OperationEnum getOperation() {
        return operation;
    }
    public void setOperation(OperationEnum operation) {
        this.operation = operation;
    }

    /**
    * Self-service properties to patch.
    **/
    public PropertyPatchReq properties(List<PropertyReq> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Self-service properties to patch.")
    @JsonProperty("properties")
    @Valid
    @NotNull(message = "Property properties cannot be null.")

    public List<PropertyReq> getProperties() {
        return properties;
    }
    public void setProperties(List<PropertyReq> properties) {
        this.properties = properties;
    }

    public PropertyPatchReq addPropertiesItem(PropertyReq propertiesItem) {
        this.properties.add(propertiesItem);
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
        PropertyPatchReq propertyPatchReq = (PropertyPatchReq) o;
        return Objects.equals(this.operation, propertyPatchReq.operation) &&
            Objects.equals(this.properties, propertyPatchReq.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operation, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PropertyPatchReq {\n");
        
        sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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

