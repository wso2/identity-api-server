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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.flow.management.v1.RuleFieldDefinition;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class FlowRuleMetaResponse  {
  
    private List<RuleFieldDefinition> ruleMetaData = null;


    /**
    * The fields a condition in this flow can be written against, with how each one is keyed, the operators it accepts and how its value is entered. Empty when the flow type publishes no condition fields. 
    **/
    public FlowRuleMetaResponse ruleMetaData(List<RuleFieldDefinition> ruleMetaData) {

        this.ruleMetaData = ruleMetaData;
        return this;
    }
    
    @ApiModelProperty(value = "The fields a condition in this flow can be written against, with how each one is keyed, the operators it accepts and how its value is entered. Empty when the flow type publishes no condition fields. ")
    @JsonProperty("ruleMetaData")
    @Valid
    public List<RuleFieldDefinition> getRuleMetaData() {
        return ruleMetaData;
    }
    public void setRuleMetaData(List<RuleFieldDefinition> ruleMetaData) {
        this.ruleMetaData = ruleMetaData;
    }

    public FlowRuleMetaResponse addRuleMetaDataItem(RuleFieldDefinition ruleMetaDataItem) {
        if (this.ruleMetaData == null) {
            this.ruleMetaData = new ArrayList<RuleFieldDefinition>();
        }
        this.ruleMetaData.add(ruleMetaDataItem);
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
        FlowRuleMetaResponse flowRuleMetaResponse = (FlowRuleMetaResponse) o;
        return Objects.equals(this.ruleMetaData, flowRuleMetaResponse.ruleMetaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleMetaData);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowRuleMetaResponse {\n");
        
        sb.append("    ruleMetaData: ").append(toIndentedString(ruleMetaData)).append("\n");
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

