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
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

/**
 * Contains a list of agent IDs to be unshared.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Contains a list of agent IDs to be unshared.")
public class AgentUnshareRequestBodyAgentCriteria  {
  
    private List<String> agentIds = null;


    /**
    * List of agent IDs.
    **/
    public AgentUnshareRequestBodyAgentCriteria agentIds(List<String> agentIds) {

        this.agentIds = agentIds;
        return this;
    }
    
    @ApiModelProperty(value = "List of agent IDs.")
    @JsonProperty("agentIds")
    @Valid
    public List<String> getAgentIds() {
        return agentIds;
    }
    public void setAgentIds(List<String> agentIds) {
        this.agentIds = agentIds;
    }

    public AgentUnshareRequestBodyAgentCriteria addAgentIdsItem(String agentIdsItem) {
        if (this.agentIds == null) {
            this.agentIds = new ArrayList<>();
        }
        this.agentIds.add(agentIdsItem);
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
        AgentUnshareRequestBodyAgentCriteria agentUnshareRequestBodyAgentCriteria = (AgentUnshareRequestBodyAgentCriteria) o;
        return Objects.equals(this.agentIds, agentUnshareRequestBodyAgentCriteria.agentIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentIds);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentUnshareRequestBodyAgentCriteria {\n");
        
        sb.append("    agentIds: ").append(toIndentedString(agentIds)).append("\n");
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

