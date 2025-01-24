/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.rule.metadata.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Field;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Operator;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;

public class FieldDefinition  {
  
    private Field field;
    private List<Operator> operators = null;

    private Value value;

    /**
    **/
    public FieldDefinition field(Field field) {

        this.field = field;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("field")
    @Valid
    public Field getField() {
        return field;
    }
    public void setField(Field field) {
        this.field = field;
    }

    /**
    * Specifies the list of valid operators that can be applied to this field in rule expressions. Each operator defines a comparison or matching condition (e.g., \\\&quot;equals\\\&quot;, \\\&quot;contains\\\&quot;, \\\&quot;greaterThan\\\&quot;) that determines how the field&#39;s value will be evaluated within a rule. 
    **/
    public FieldDefinition operators(List<Operator> operators) {

        this.operators = operators;
        return this;
    }
    
    @ApiModelProperty(value = "Specifies the list of valid operators that can be applied to this field in rule expressions. Each operator defines a comparison or matching condition (e.g., \\\"equals\\\", \\\"contains\\\", \\\"greaterThan\\\") that determines how the field's value will be evaluated within a rule. ")
    @JsonProperty("operators")
    @Valid
    public List<Operator> getOperators() {
        return operators;
    }
    public void setOperators(List<Operator> operators) {
        this.operators = operators;
    }

    public FieldDefinition addOperatorsItem(Operator operatorsItem) {
        if (this.operators == null) {
            this.operators = new ArrayList<Operator>();
        }
        this.operators.add(operatorsItem);
        return this;
    }

        /**
    **/
    public FieldDefinition value(Value value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("value")
    @Valid
    public Value getValue() {
        return value;
    }
    public void setValue(Value value) {
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
        FieldDefinition fieldDefinition = (FieldDefinition) o;
        return Objects.equals(this.field, fieldDefinition.field) &&
            Objects.equals(this.operators, fieldDefinition.operators) &&
            Objects.equals(this.value, fieldDefinition.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, operators, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FieldDefinition {\n");
        
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

