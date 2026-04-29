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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentCriteria;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.RoleAssignment;
import javax.validation.constraints.*;

/**
 * Request body for sharing agents with **all organizations** controlled by the selected policy.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Request body for sharing agents with **all organizations** controlled by the selected policy.")
public class AgentShareAllRequestBody  {
  
    private AgentCriteria agentCriteria;
    private String policy;
    private RoleAssignment roleAssignment;

    /**
    **/
    public AgentShareAllRequestBody agentCriteria(AgentCriteria agentCriteria) {

        this.agentCriteria = agentCriteria;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("agentCriteria")
    @Valid
    @NotNull(message = "Property agentCriteria cannot be null.")

    public AgentCriteria getAgentCriteria() {
        return agentCriteria;
    }
    public void setAgentCriteria(AgentCriteria agentCriteria) {
        this.agentCriteria = agentCriteria;
    }

    /**
    * Global sharing policy.  Possible values: - &#x60;ALL_EXISTING_AND_FUTURE_ORGS&#x60;
    **/
    public AgentShareAllRequestBody policy(String policy) {

        this.policy = policy;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Global sharing policy.  Possible values: - `ALL_EXISTING_AND_FUTURE_ORGS`")
    @JsonProperty("policy")
    @Valid
    @NotNull(message = "Property policy cannot be null.")

    public String getPolicy() {
        return policy;
    }
    public void setPolicy(String policy) {
        this.policy = policy;
    }

    /**
    **/
    public AgentShareAllRequestBody roleAssignment(RoleAssignment roleAssignment) {

        this.roleAssignment = roleAssignment;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("roleAssignment")
    @Valid
    public RoleAssignment getRoleAssignment() {
        return roleAssignment;
    }
    public void setRoleAssignment(RoleAssignment roleAssignment) {
        this.roleAssignment = roleAssignment;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentShareAllRequestBody agentShareAllRequestBody = (AgentShareAllRequestBody) o;
        return Objects.equals(this.agentCriteria, agentShareAllRequestBody.agentCriteria) &&
            Objects.equals(this.policy, agentShareAllRequestBody.policy) &&
            Objects.equals(this.roleAssignment, agentShareAllRequestBody.roleAssignment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentCriteria, policy, roleAssignment);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentShareAllRequestBody {\n");
        
        sb.append("    agentCriteria: ").append(toIndentedString(agentCriteria)).append("\n");
        sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
        sb.append("    roleAssignment: ").append(toIndentedString(roleAssignment)).append("\n");
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

