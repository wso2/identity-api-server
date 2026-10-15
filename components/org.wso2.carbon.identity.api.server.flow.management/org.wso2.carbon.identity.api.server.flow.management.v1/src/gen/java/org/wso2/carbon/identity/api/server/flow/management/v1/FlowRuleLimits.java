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
import javax.validation.constraints.*;

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

@ApiModel(description = "The limits the server validates decision steps and their conditions against.")
public class FlowRuleLimits  {
  

    private Integer maxDecisionNodes;
    private Integer maxRulesCombinedWithOr;
    private Integer maxExpressionsCombinedWithAnd;

    /**
    * Maximum number of decision steps in a flow.
    **/
    public FlowRuleLimits maxDecisionNodes(Integer maxDecisionNodes) {

        this.maxDecisionNodes = maxDecisionNodes;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "Maximum number of decision steps in a flow.")
    @JsonProperty("maxDecisionNodes")
    @Valid
    public Integer getMaxDecisionNodes() {
        return maxDecisionNodes;
    }
    public void setMaxDecisionNodes(Integer maxDecisionNodes) {
        this.maxDecisionNodes = maxDecisionNodes;
    }

    /**
    * Maximum number of rules a condition may combine with OR.
    **/
    public FlowRuleLimits maxRulesCombinedWithOr(Integer maxRulesCombinedWithOr) {

        this.maxRulesCombinedWithOr = maxRulesCombinedWithOr;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "Maximum number of rules a condition may combine with OR.")
    @JsonProperty("maxRulesCombinedWithOr")
    @Valid
    public Integer getMaxRulesCombinedWithOr() {
        return maxRulesCombinedWithOr;
    }
    public void setMaxRulesCombinedWithOr(Integer maxRulesCombinedWithOr) {
        this.maxRulesCombinedWithOr = maxRulesCombinedWithOr;
    }

    /**
    * Maximum number of expressions a rule may combine with AND.
    **/
    public FlowRuleLimits maxExpressionsCombinedWithAnd(Integer maxExpressionsCombinedWithAnd) {

        this.maxExpressionsCombinedWithAnd = maxExpressionsCombinedWithAnd;
        return this;
    }
    
    @ApiModelProperty(example = "5", value = "Maximum number of expressions a rule may combine with AND.")
    @JsonProperty("maxExpressionsCombinedWithAnd")
    @Valid
    public Integer getMaxExpressionsCombinedWithAnd() {
        return maxExpressionsCombinedWithAnd;
    }
    public void setMaxExpressionsCombinedWithAnd(Integer maxExpressionsCombinedWithAnd) {
        this.maxExpressionsCombinedWithAnd = maxExpressionsCombinedWithAnd;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowRuleLimits flowRuleLimits = (FlowRuleLimits) o;
        return Objects.equals(this.maxDecisionNodes, flowRuleLimits.maxDecisionNodes) &&
            Objects.equals(this.maxRulesCombinedWithOr, flowRuleLimits.maxRulesCombinedWithOr) &&
            Objects.equals(this.maxExpressionsCombinedWithAnd, flowRuleLimits.maxExpressionsCombinedWithAnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxDecisionNodes, maxRulesCombinedWithOr, maxExpressionsCombinedWithAnd);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowRuleLimits {\n");
        
        sb.append("    maxDecisionNodes: ").append(toIndentedString(maxDecisionNodes)).append("\n");
        sb.append("    maxRulesCombinedWithOr: ").append(toIndentedString(maxRulesCombinedWithOr)).append("\n");
        sb.append("    maxExpressionsCombinedWithAnd: ").append(toIndentedString(maxExpressionsCombinedWithAnd)).append("\n");
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

