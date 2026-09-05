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

package org.wso2.carbon.identity.api.server.device.policy.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.device.policy.v1.model.DevicePolicyField;
import org.wso2.carbon.identity.api.server.device.policy.v1.model.DevicePolicyOperator;
import org.wso2.carbon.identity.api.server.device.policy.v1.model.DevicePolicyValue;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class DevicePolicyFieldDefinition  {
  
    private DevicePolicyField field;
    private List<DevicePolicyOperator> operators = null;

    private DevicePolicyValue value;

    /**
    **/
    public DevicePolicyFieldDefinition field(DevicePolicyField field) {

        this.field = field;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("field")
    @Valid
    public DevicePolicyField getField() {
        return field;
    }
    public void setField(DevicePolicyField field) {
        this.field = field;
    }

    /**
    **/
    public DevicePolicyFieldDefinition operators(List<DevicePolicyOperator> operators) {

        this.operators = operators;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("operators")
    @Valid
    public List<DevicePolicyOperator> getOperators() {
        return operators;
    }
    public void setOperators(List<DevicePolicyOperator> operators) {
        this.operators = operators;
    }

    public DevicePolicyFieldDefinition addOperatorsItem(DevicePolicyOperator operatorsItem) {
        if (this.operators == null) {
            this.operators = new ArrayList<>();
        }
        this.operators.add(operatorsItem);
        return this;
    }

        /**
    **/
    public DevicePolicyFieldDefinition value(DevicePolicyValue value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("value")
    @Valid
    public DevicePolicyValue getValue() {
        return value;
    }
    public void setValue(DevicePolicyValue value) {
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
        DevicePolicyFieldDefinition devicePolicyFieldDefinition = (DevicePolicyFieldDefinition) o;
        return Objects.equals(this.field, devicePolicyFieldDefinition.field) &&
            Objects.equals(this.operators, devicePolicyFieldDefinition.operators) &&
            Objects.equals(this.value, devicePolicyFieldDefinition.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, operators, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DevicePolicyFieldDefinition {\n");
        
        sb.append("    field: ").append(toIndentedString(field)).append("\n");
        sb.append("    operators: ").append(toIndentedString(operators)).append("\n");
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

