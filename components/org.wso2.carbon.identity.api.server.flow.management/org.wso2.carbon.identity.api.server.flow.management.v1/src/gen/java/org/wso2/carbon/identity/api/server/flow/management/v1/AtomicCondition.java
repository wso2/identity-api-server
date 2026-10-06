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
 * A single comparison of a field against a value.
 **/
@ApiModel(description = "A single comparison of a field against a value.")
public class AtomicCondition  {
  

    private FieldReference field;
    private String operator;
    private ConditionValue value;

    /**
    * The field being compared.
    **/
    public AtomicCondition field(FieldReference field) {

        this.field = field;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "The field being compared.")
    @JsonProperty("field")
    @Valid
    @NotNull(message = "Property field cannot be null.")

    public FieldReference getField() {
        return field;
    }
    public void setField(FieldReference field) {
        this.field = field;
    }

    /**
    * How the field and the value are compared, one of the operators published for the field in ruleMetaData.
    **/
    public AtomicCondition operator(String operator) {

        this.operator = operator;
        return this;
    }
    
    @ApiModelProperty(example = "equals", required = true, value = "How the field and the value are compared, one of the operators published for the field in ruleMetaData.")
    @JsonProperty("operator")
    @Valid
    @NotNull(message = "Property operator cannot be null.")

    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
    * What the field is compared against.
    **/
    public AtomicCondition value(ConditionValue value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "What the field is compared against.")
    @JsonProperty("value")
    @Valid
    @NotNull(message = "Property value cannot be null.")

    public ConditionValue getValue() {
        return value;
    }
    public void setValue(ConditionValue value) {
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
        AtomicCondition other = (AtomicCondition) o;
        return Objects.equals(this.field, other.field) &&
            Objects.equals(this.operator, other.operator) &&
            Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, operator, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AtomicCondition {\n");
        
        sb.append("    field: ").append(toIndentedString(field)).append("\n");
        sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
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
