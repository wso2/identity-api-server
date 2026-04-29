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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentCriteria;
import javax.validation.constraints.*;

/**
 * Request body for unsharing agents from selected organizations.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Request body for unsharing agents from selected organizations.")
public class AgentUnshareSelectedRequestBody  {
  
    private AgentCriteria agentCriteria;
    private List<String> orgIds = new ArrayList<>();


    /**
    **/
    public AgentUnshareSelectedRequestBody agentCriteria(AgentCriteria agentCriteria) {

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
    * List of organization IDs from which agents should be unshared.
    **/
    public AgentUnshareSelectedRequestBody orgIds(List<String> orgIds) {

        this.orgIds = orgIds;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "List of organization IDs from which agents should be unshared.")
    @JsonProperty("orgIds")
    @Valid
    @NotNull(message = "Property orgIds cannot be null.")

    public List<String> getOrgIds() {
        return orgIds;
    }
    public void setOrgIds(List<String> orgIds) {
        this.orgIds = orgIds;
    }

    public AgentUnshareSelectedRequestBody addOrgIdsItem(String orgIdsItem) {
        this.orgIds.add(orgIdsItem);
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
        AgentUnshareSelectedRequestBody agentUnshareSelectedRequestBody = (AgentUnshareSelectedRequestBody) o;
        return Objects.equals(this.agentCriteria, agentUnshareSelectedRequestBody.agentCriteria) &&
            Objects.equals(this.orgIds, agentUnshareSelectedRequestBody.orgIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentCriteria, orgIds);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentUnshareSelectedRequestBody {\n");
        
        sb.append("    agentCriteria: ").append(toIndentedString(agentCriteria)).append("\n");
        sb.append("    orgIds: ").append(toIndentedString(orgIds)).append("\n");
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

