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

package org.wso2.carbon.identity.rest.api.server.workflow.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Represents a single logical condition or comparison within a rule. An expression defines a field, and operator, and a value that is evaluated.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Represents a single logical condition or comparison within a rule. An expression defines a field, and operator, and a value that is evaluated.")
public class Expression  {
  
    private String field;
    private String operator;
    private String value;

    /**
    * The field or attribute of the entity being evaluated (e.g.,application, role).
    **/
    public Expression field(String field) {

        this.field = field;
        return this;
    }
    
    @ApiModelProperty(example = "application", required = true, value = "The field or attribute of the entity being evaluated (e.g.,application, role).")
    @JsonProperty("field")
    @Valid
    @NotNull(message = "Property field cannot be null.")

    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }

    /**
    * The comparison operator used to evaluate the the field and value. Common operators include \&quot;equals\&quot;, \&quot;contains\&quot;, \&quot;startsWith\&quot;, etc.
    **/
    public Expression operator(String operator) {

        this.operator = operator;
        return this;
    }
    
    @ApiModelProperty(example = "equals", required = true, value = "The comparison operator used to evaluate the the field and value. Common operators include \"equals\", \"contains\", \"startsWith\", etc.")
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
    * The value that the field is compared against based on the operator.
    **/
    public Expression value(String value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(example = "FinanceApp", required = true, value = "The value that the field is compared against based on the operator.")
    @JsonProperty("value")
    @Valid
    @NotNull(message = "Property value cannot be null.")

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
        Expression expression = (Expression) o;
        return Objects.equals(this.field, expression.field) &&
            Objects.equals(this.operator, expression.operator) &&
            Objects.equals(this.value, expression.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, operator, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Expression {\n");
        
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

