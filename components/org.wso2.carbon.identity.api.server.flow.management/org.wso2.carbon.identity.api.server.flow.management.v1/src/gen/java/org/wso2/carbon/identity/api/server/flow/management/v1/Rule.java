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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * One rule of a rule evaluation step
 **/
@ApiModel(description = "One rule of a rule evaluation step")
public class Rule  {
  
    private String id;
    private String name;
    private String next;
    private RuleExpression expression;

    /**
    * Identifier for this rule, stable across saves
    **/
    public Rule id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "rule_lk", required = true, value = "Identifier for this rule, stable across saves")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Label for the rule, shown on the canvas and cited in validation messages
    **/
    public Rule name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Sri Lanka", required = true, value = "Label for the rule, shown on the canvas and cited in validation messages")
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
    * ID of the step taken when this rule is selected
    **/
    public Rule next(String next) {

        this.next = next;
        return this;
    }
    
    @ApiModelProperty(example = "dnd-step-3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true, value = "ID of the step taken when this rule is selected")
    @JsonProperty("next")
    @Valid
    @NotNull(message = "Property next cannot be null.")

    public String getNext() {
        return next;
    }
    public void setNext(String next) {
        this.next = next;
    }

    /**
    * Expression this rule tests. Absent on the default rule, which always holds.
    **/
    public Rule expression(RuleExpression expression) {

        this.expression = expression;
        return this;
    }
    
    @ApiModelProperty(value = "Expression this rule tests. Absent on the default rule, which always holds.")
    @JsonProperty("expression")
    @Valid
    public RuleExpression getExpression() {
        return expression;
    }
    public void setExpression(RuleExpression expression) {
        this.expression = expression;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rule rule = (Rule) o;
        return Objects.equals(this.id, rule.id) &&
            Objects.equals(this.name, rule.name) &&
            Objects.equals(this.next, rule.next) &&
            Objects.equals(this.expression, rule.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, next, expression);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Rule {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    next: ").append(toIndentedString(next)).append("\n");
        sb.append("    expression: ").append(toIndentedString(expression)).append("\n");
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
