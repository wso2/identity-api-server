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

package org.wso2.carbon.identity.api.server.policy.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleField;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleFieldValue;
import org.wso2.carbon.identity.api.server.policy.v1.model.RuleOperator;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RuleFieldMetadata  {
  
    private RuleField field;
    private List<RuleOperator> operators = null;

    private RuleFieldValue value;

    /**
    **/
    public RuleFieldMetadata field(RuleField field) {

        this.field = field;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("field")
    @Valid
    public RuleField getField() {
        return field;
    }
    public void setField(RuleField field) {
        this.field = field;
    }

    /**
    * Operators that can be applied to the field in rule expressions.
    **/
    public RuleFieldMetadata operators(List<RuleOperator> operators) {

        this.operators = operators;
        return this;
    }
    
    @ApiModelProperty(value = "Operators that can be applied to the field in rule expressions.")
    @JsonProperty("operators")
    @Valid
    public List<RuleOperator> getOperators() {
        return operators;
    }
    public void setOperators(List<RuleOperator> operators) {
        this.operators = operators;
    }

    public RuleFieldMetadata addOperatorsItem(RuleOperator operatorsItem) {
        if (this.operators == null) {
            this.operators = new ArrayList<>();
        }
        this.operators.add(operatorsItem);
        return this;
    }

        /**
    **/
    public RuleFieldMetadata value(RuleFieldValue value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("value")
    @Valid
    public RuleFieldValue getValue() {
        return value;
    }
    public void setValue(RuleFieldValue value) {
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
        RuleFieldMetadata ruleFieldMetadata = (RuleFieldMetadata) o;
        return Objects.equals(this.field, ruleFieldMetadata.field) &&
            Objects.equals(this.operators, ruleFieldMetadata.operators) &&
            Objects.equals(this.value, ruleFieldMetadata.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, operators, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RuleFieldMetadata {\n");
        
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

