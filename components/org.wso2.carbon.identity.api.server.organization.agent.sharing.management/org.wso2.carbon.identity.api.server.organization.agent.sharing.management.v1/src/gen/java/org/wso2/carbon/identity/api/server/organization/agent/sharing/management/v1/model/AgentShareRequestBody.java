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
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareRequestBodyAgentCriteria;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentShareRequestBodyOrganizations;
import javax.validation.constraints.*;

/**
 * The request body for sharing agents with multiple child organizations. Includes a list of agents, organizations, sharing scope as policy, and roles. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "The request body for sharing agents with multiple child organizations. Includes a list of agents, organizations, sharing scope as policy, and roles. ")
public class AgentShareRequestBody  {
  
    private AgentShareRequestBodyAgentCriteria agentCriteria;
    private List<AgentShareRequestBodyOrganizations> organizations = new ArrayList<>();


    /**
    **/
    public AgentShareRequestBody agentCriteria(AgentShareRequestBodyAgentCriteria agentCriteria) {

        this.agentCriteria = agentCriteria;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("agentCriteria")
    @Valid
    @NotNull(message = "Property agentCriteria cannot be null.")

    public AgentShareRequestBodyAgentCriteria getAgentCriteria() {
        return agentCriteria;
    }
    public void setAgentCriteria(AgentShareRequestBodyAgentCriteria agentCriteria) {
        this.agentCriteria = agentCriteria;
    }

    /**
    * List of organizations specifying sharing scope and roles.
    **/
    public AgentShareRequestBody organizations(List<AgentShareRequestBodyOrganizations> organizations) {

        this.organizations = organizations;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "List of organizations specifying sharing scope and roles.")
    @JsonProperty("organizations")
    @Valid
    @NotNull(message = "Property organizations cannot be null.")

    public List<AgentShareRequestBodyOrganizations> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(List<AgentShareRequestBodyOrganizations> organizations) {
        this.organizations = organizations;
    }

    public AgentShareRequestBody addOrganizationsItem(AgentShareRequestBodyOrganizations organizationsItem) {
        this.organizations.add(organizationsItem);
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
        AgentShareRequestBody agentShareRequestBody = (AgentShareRequestBody) o;
        return Objects.equals(this.agentCriteria, agentShareRequestBody.agentCriteria) &&
            Objects.equals(this.organizations, agentShareRequestBody.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentCriteria, organizations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentShareRequestBody {\n");
        
        sb.append("    agentCriteria: ").append(toIndentedString(agentCriteria)).append("\n");
        sb.append("    organizations: ").append(toIndentedString(organizations)).append("\n");
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

