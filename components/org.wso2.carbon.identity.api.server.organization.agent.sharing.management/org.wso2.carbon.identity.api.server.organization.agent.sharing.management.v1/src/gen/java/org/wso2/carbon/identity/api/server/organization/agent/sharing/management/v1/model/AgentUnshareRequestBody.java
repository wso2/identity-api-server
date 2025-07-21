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
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentUnshareRequestBodyAgentCriteria;
import javax.validation.constraints.*;

/**
 * The request body for unsharing agents from multiple organizations. Includes a list of agent IDs and a list of organization IDs. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "The request body for unsharing agents from multiple organizations. Includes a list of agent IDs and a list of organization IDs. ")
public class AgentUnshareRequestBody  {
  
    private AgentUnshareRequestBodyAgentCriteria agentCriteria;
    private List<String> organizations = new ArrayList<>();


    /**
    **/
    public AgentUnshareRequestBody agentCriteria(AgentUnshareRequestBodyAgentCriteria agentCriteria) {

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

    /**
    * List of organization IDs from which the agents should be unshared.
    **/
    public AgentUnshareRequestBody organizations(List<String> organizations) {

        this.organizations = organizations;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "List of organization IDs from which the agents should be unshared.")
    @JsonProperty("organizations")
    @Valid
    @NotNull(message = "Property organizations cannot be null.")

    public List<String> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(List<String> organizations) {
        this.organizations = organizations;
    }

    public AgentUnshareRequestBody addOrganizationsItem(String organizationsItem) {
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
        AgentUnshareRequestBody agentUnshareRequestBody = (AgentUnshareRequestBody) o;
        return Objects.equals(this.agentCriteria, agentUnshareRequestBody.agentCriteria) &&
            Objects.equals(this.organizations, agentUnshareRequestBody.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentCriteria, organizations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentUnshareRequestBody {\n");
        
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

