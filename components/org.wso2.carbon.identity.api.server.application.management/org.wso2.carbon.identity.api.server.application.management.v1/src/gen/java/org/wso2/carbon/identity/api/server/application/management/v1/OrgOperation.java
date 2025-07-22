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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class OrgOperation  {
  
    private String orgId;

@XmlType(name="OperationEnum")
@XmlEnum(String.class)
public enum OperationEnum {

    @XmlEnumValue("ADD_ROLE") ADD_ROLE(String.valueOf("ADD_ROLE")), @XmlEnumValue("REMOVE_ROLE") REMOVE_ROLE(String.valueOf("REMOVE_ROLE"));


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
    private Map<String, Object> properties = new HashMap<>();


    /**
    **/
    public OrgOperation orgId(String orgId) {

        this.orgId = orgId;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("orgId")
    @Valid
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
    **/
    public OrgOperation operation(OperationEnum operation) {

        this.operation = operation;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
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
    * Operation-specific configuration. - For ADD_ROLE/REMOVE_ROLE: requires &#x60;roles&#x60;. - For future operations, structure may vary. 
    **/
    public OrgOperation properties(Map<String, Object> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Operation-specific configuration. - For ADD_ROLE/REMOVE_ROLE: requires `roles`. - For future operations, structure may vary. ")
    @JsonProperty("properties")
    @Valid
    @NotNull(message = "Property properties cannot be null.")

    public Map<String, Object> getProperties() {
        return properties;
    }
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }


    public OrgOperation putPropertiesItem(String key, Object propertiesItem) {
        this.properties.put(key, propertiesItem);
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
        OrgOperation orgOperation = (OrgOperation) o;
        return Objects.equals(this.orgId, orgOperation.orgId) &&
            Objects.equals(this.operation, orgOperation.operation) &&
            Objects.equals(this.properties, orgOperation.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, operation, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class OrgOperation {\n");
        
        sb.append("    orgId: ").append(toIndentedString(orgId)).append("\n");
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

