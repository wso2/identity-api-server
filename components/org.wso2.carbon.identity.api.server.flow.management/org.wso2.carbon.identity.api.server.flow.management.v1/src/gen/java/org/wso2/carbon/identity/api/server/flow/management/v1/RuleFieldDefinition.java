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
 * A field a condition in this flow can be written against.
 **/
@ApiModel(description = "A field a condition in this flow can be written against.")
public class RuleFieldDefinition  {
  

    private RuleField field;
    private List<org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Operator> operators = new ArrayList<>();
    private RuleValueDefinition value;

    /**
    * The field, and how a value within it is picked.
    **/
    public RuleFieldDefinition field(RuleField field) {

        this.field = field;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "The field, and how a value within it is picked.")
    @JsonProperty("field")
    @Valid
    @NotNull(message = "Property field cannot be null.")

    public RuleField getField() {
        return field;
    }
    public void setField(RuleField field) {
        this.field = field;
    }

    /**
    * The operators the field takes.
    **/
    public RuleFieldDefinition operators(List<org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Operator> operators) {

        this.operators = operators;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "The operators the field takes.")
    @JsonProperty("operators")
    @Valid
    @NotNull(message = "Property operators cannot be null.")

    public List<org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Operator> getOperators() {
        return operators;
    }
    public void setOperators(List<org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Operator> operators) {
        this.operators = operators;
    }

    public RuleFieldDefinition addOperatorsItem(org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Operator operatorsItem) {
        if (this.operators == null) {
            this.operators = new ArrayList<>();
        }
        this.operators.add(operatorsItem);
        return this;
    }

    /**
    * What the field can be compared against.
    **/
    public RuleFieldDefinition value(RuleValueDefinition value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "What the field can be compared against.")
    @JsonProperty("value")
    @Valid
    @NotNull(message = "Property value cannot be null.")

    public RuleValueDefinition getValue() {
        return value;
    }
    public void setValue(RuleValueDefinition value) {
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
        RuleFieldDefinition other = (RuleFieldDefinition) o;
        return Objects.equals(this.field, other.field) &&
            Objects.equals(this.operators, other.operators) &&
            Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, operators, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RuleFieldDefinition {\n");
        
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
