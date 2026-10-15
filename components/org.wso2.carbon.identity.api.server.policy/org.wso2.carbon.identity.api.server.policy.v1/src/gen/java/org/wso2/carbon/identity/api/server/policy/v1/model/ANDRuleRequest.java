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

package org.wso2.carbon.identity.api.server.policy.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.policy.v1.model.ExpressionRequest;
import javax.validation.constraints.*;

/**
 * A sub-rule that combines expressions with an AND condition; satisfied only when all of its expressions evaluate to true.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "A sub-rule that combines expressions with an AND condition; satisfied only when all of its expressions evaluate to true.")
public class ANDRuleRequest  {
  

@XmlType(name="ConditionEnum")
@XmlEnum(String.class)
public enum ConditionEnum {

    @XmlEnumValue("AND") AND(String.valueOf("AND"));


    private String value;

    ConditionEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ConditionEnum fromValue(String value) {
        for (ConditionEnum b : ConditionEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private ConditionEnum condition;
    private List<ExpressionRequest> expressions = new ArrayList<>();


    /**
    * The logical operator combining the expressions. Always \&quot;AND\&quot;.
    **/
    public ANDRuleRequest condition(ConditionEnum condition) {

        this.condition = condition;
        return this;
    }
    
    @ApiModelProperty(example = "AND", required = true, value = "The logical operator combining the expressions. Always \"AND\".")
    @JsonProperty("condition")
    @Valid
    @NotNull(message = "Property condition cannot be null.")

    public ConditionEnum getCondition() {
        return condition;
    }
    public void setCondition(ConditionEnum condition) {
        this.condition = condition;
    }

    /**
    * The list of expressions combined with AND.
    **/
    public ANDRuleRequest expressions(List<ExpressionRequest> expressions) {

        this.expressions = expressions;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "The list of expressions combined with AND.")
    @JsonProperty("expressions")
    @Valid
    @NotNull(message = "Property expressions cannot be null.")
 @Size(min=1)
    public List<ExpressionRequest> getExpressions() {
        return expressions;
    }
    public void setExpressions(List<ExpressionRequest> expressions) {
        this.expressions = expressions;
    }

    public ANDRuleRequest addExpressionsItem(ExpressionRequest expressionsItem) {
        this.expressions.add(expressionsItem);
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
        ANDRuleRequest anDRuleRequest = (ANDRuleRequest) o;
        return Objects.equals(this.condition, anDRuleRequest.condition) &&
            Objects.equals(this.expressions, anDRuleRequest.expressions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, expressions);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ANDRuleRequest {\n");
        
        sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
        sb.append("    expressions: ").append(toIndentedString(expressions)).append("\n");
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

