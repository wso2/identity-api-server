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
 * A group of conditions that must all match.
 **/
@ApiModel(description = "A group of conditions that must all match.")
public class Condition  {
  
@XmlType(name="OperatorEnum")
@XmlEnum(String.class)
public enum OperatorEnum {

    @XmlEnumValue("AND") AND(String.valueOf("AND"));


    private String value;

    OperatorEnum(String v) {
        value = v;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static OperatorEnum fromValue(String value) {
        for (OperatorEnum b : OperatorEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private OperatorEnum operator;
    private List<AtomicCondition> conditions = new ArrayList<>();

    /**
    * How the conditions are combined. Always AND.
    **/
    public Condition operator(OperatorEnum operator) {

        this.operator = operator;
        return this;
    }
    
    @ApiModelProperty(example = "AND", required = true, value = "How the conditions are combined. Always AND.")
    @JsonProperty("operator")
    @Valid
    @NotNull(message = "Property operator cannot be null.")

    public OperatorEnum getOperator() {
        return operator;
    }
    public void setOperator(OperatorEnum operator) {
        this.operator = operator;
    }

    /**
    * Conditions that must all match.
    **/
    public Condition conditions(List<AtomicCondition> conditions) {

        this.conditions = conditions;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Conditions that must all match.")
    @JsonProperty("conditions")
    @Valid
    @NotNull(message = "Property conditions cannot be null.")

    public List<AtomicCondition> getConditions() {
        return conditions;
    }
    public void setConditions(List<AtomicCondition> conditions) {
        this.conditions = conditions;
    }

    public Condition addConditionsItem(AtomicCondition conditionsItem) {
        if (this.conditions == null) {
            this.conditions = new ArrayList<>();
        }
        this.conditions.add(conditionsItem);
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
        Condition other = (Condition) o;
        return Objects.equals(this.operator, other.operator) &&
            Objects.equals(this.conditions, other.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, conditions);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Condition {\n");
        
        sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
        sb.append("    conditions: ").append(toIndentedString(conditions)).append("\n");
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
