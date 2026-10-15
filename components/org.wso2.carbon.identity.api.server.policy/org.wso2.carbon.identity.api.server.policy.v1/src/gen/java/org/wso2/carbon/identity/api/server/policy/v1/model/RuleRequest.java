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
import org.wso2.carbon.identity.api.server.policy.v1.model.ANDRuleRequest;
import javax.validation.constraints.*;

/**
 * A rule that combines one or more AND-grouped sub-rules with an OR condition; the rule is satisfied when any sub-rule is satisfied.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "A rule that combines one or more AND-grouped sub-rules with an OR condition; the rule is satisfied when any sub-rule is satisfied.")
public class RuleRequest  {
  

@XmlType(name="ConditionEnum")
@XmlEnum(String.class)
public enum ConditionEnum {

    @XmlEnumValue("OR") OR(String.valueOf("OR"));


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
    private List<ANDRuleRequest> rules = new ArrayList<>();


    /**
    * The logical operator combining the sub-rules. Always \&quot;OR\&quot;.
    **/
    public RuleRequest condition(ConditionEnum condition) {

        this.condition = condition;
        return this;
    }
    
    @ApiModelProperty(example = "OR", value = "The logical operator combining the sub-rules. Always \"OR\".")
    @JsonProperty("condition")
    @Valid
    public ConditionEnum getCondition() {
        return condition;
    }
    public void setCondition(ConditionEnum condition) {
        this.condition = condition;
    }

    /**
    * The AND-grouped sub-rules combined with OR.
    **/
    public RuleRequest rules(List<ANDRuleRequest> rules) {

        this.rules = rules;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "The AND-grouped sub-rules combined with OR.")
    @JsonProperty("rules")
    @Valid
    @NotNull(message = "Property rules cannot be null.")
 @Size(min=1)
    public List<ANDRuleRequest> getRules() {
        return rules;
    }
    public void setRules(List<ANDRuleRequest> rules) {
        this.rules = rules;
    }

    public RuleRequest addRulesItem(ANDRuleRequest rulesItem) {
        this.rules.add(rulesItem);
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
        RuleRequest ruleRequest = (RuleRequest) o;
        return Objects.equals(this.condition, ruleRequest.condition) &&
            Objects.equals(this.rules, ruleRequest.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, rules);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RuleRequest {\n");
        
        sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
        sb.append("    rules: ").append(toIndentedString(rules)).append("\n");
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

