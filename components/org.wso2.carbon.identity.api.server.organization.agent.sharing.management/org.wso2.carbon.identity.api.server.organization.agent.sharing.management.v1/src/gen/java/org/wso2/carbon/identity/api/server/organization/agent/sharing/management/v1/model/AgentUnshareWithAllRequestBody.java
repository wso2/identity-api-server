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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareRequestBodyAgentCriteria;
import javax.validation.constraints.*;

/**
 * The request body for unsharing agents from all organizations. Includes a list of agent IDs. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "The request body for unsharing agents from all organizations. Includes a list of agent IDs. ")
public class AgentUnshareWithAllRequestBody  {
  
    private AgentUnshareRequestBodyAgentCriteria agentCriteria;

    /**
    **/
    public AgentUnshareWithAllRequestBody agentCriteria(AgentUnshareRequestBodyAgentCriteria agentCriteria) {

        this.agentCriteria = agentCriteria;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("agentCriteria")
    @Valid
    @NotNull(message = "Property agentCriteria cannot be null.")

    public AgentUnshareRequestBodyAgentCriteria getAgentCriteria() {
        return agentCriteria;
    }
    public void setAgentCriteria(AgentUnshareRequestBodyAgentCriteria agentCriteria) {
        this.agentCriteria = agentCriteria;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentUnshareWithAllRequestBody agentUnshareWithAllRequestBody = (AgentUnshareWithAllRequestBody) o;
        return Objects.equals(this.agentCriteria, agentUnshareWithAllRequestBody.agentCriteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentCriteria);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentUnshareWithAllRequestBody {\n");
        
        sb.append("    agentCriteria: ").append(toIndentedString(agentCriteria)).append("\n");
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

