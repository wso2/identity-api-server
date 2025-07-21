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
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AgentSharedOrganizationsResponseSharedOrganizations  {
  
    private String orgId;
    private String orgName;
    private String sharedAgentId;
    private String sharedType;
    private String rolesRef;

    /**
    * ID of the child organization
    **/
    public AgentSharedOrganizationsResponseSharedOrganizations orgId(String orgId) {

        this.orgId = orgId;
        return this;
    }
    
    @ApiModelProperty(example = "b028ca17-8f89-449c-ae27-fa955e66465d", value = "ID of the child organization")
    @JsonProperty("orgId")
    @Valid
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
    * Name of the child organization
    **/
    public AgentSharedOrganizationsResponseSharedOrganizations orgName(String orgName) {

        this.orgName = orgName;
        return this;
    }
    
    @ApiModelProperty(example = "Organization Name", value = "Name of the child organization")
    @JsonProperty("orgName")
    @Valid
    public String getOrgName() {
        return orgName;
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
    * ID of the shared agent
    **/
    public AgentSharedOrganizationsResponseSharedOrganizations sharedAgentId(String sharedAgentId) {

        this.sharedAgentId = sharedAgentId;
        return this;
    }
    
    @ApiModelProperty(example = "7a1b7d63-8cfc-4dc9-9332-3f84641b72d8", value = "ID of the shared agent")
    @JsonProperty("sharedAgentId")
    @Valid
    public String getSharedAgentId() {
        return sharedAgentId;
    }
    public void setSharedAgentId(String sharedAgentId) {
        this.sharedAgentId = sharedAgentId;
    }

    /**
    * Shared type of the agent (SHARED/INVITED)
    **/
    public AgentSharedOrganizationsResponseSharedOrganizations sharedType(String sharedType) {

        this.sharedType = sharedType;
        return this;
    }
    
    @ApiModelProperty(example = "SHARED", value = "Shared type of the agent (SHARED/INVITED)")
    @JsonProperty("sharedType")
    @Valid
    public String getSharedType() {
        return sharedType;
    }
    public void setSharedType(String sharedType) {
        this.sharedType = sharedType;
    }

    /**
    * URL reference to retrieve paginated roles for the shared agent in this organization
    **/
    public AgentSharedOrganizationsResponseSharedOrganizations rolesRef(String rolesRef) {

        this.rolesRef = rolesRef;
        return this;
    }
    
    @ApiModelProperty(example = "/api/server/v1/agents/{agentId}/shared-roles?orgId=b028ca17-8f89-449c-ae27-fa955e66465d&after=&before=&limit=2&filter=&recursive=false", value = "URL reference to retrieve paginated roles for the shared agent in this organization")
    @JsonProperty("rolesRef")
    @Valid
    public String getRolesRef() {
        return rolesRef;
    }
    public void setRolesRef(String rolesRef) {
        this.rolesRef = rolesRef;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentSharedOrganizationsResponseSharedOrganizations agentSharedOrganizationsResponseSharedOrganizations = (AgentSharedOrganizationsResponseSharedOrganizations) o;
        return Objects.equals(this.orgId, agentSharedOrganizationsResponseSharedOrganizations.orgId) &&
            Objects.equals(this.orgName, agentSharedOrganizationsResponseSharedOrganizations.orgName) &&
            Objects.equals(this.sharedAgentId, agentSharedOrganizationsResponseSharedOrganizations.sharedAgentId) &&
            Objects.equals(this.sharedType, agentSharedOrganizationsResponseSharedOrganizations.sharedType) &&
            Objects.equals(this.rolesRef, agentSharedOrganizationsResponseSharedOrganizations.rolesRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, orgName, sharedAgentId, sharedType, rolesRef);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentSharedOrganizationsResponseSharedOrganizations {\n");
        
        sb.append("    orgId: ").append(toIndentedString(orgId)).append("\n");
        sb.append("    orgName: ").append(toIndentedString(orgName)).append("\n");
        sb.append("    sharedAgentId: ").append(toIndentedString(sharedAgentId)).append("\n");
        sb.append("    sharedType: ").append(toIndentedString(sharedType)).append("\n");
        sb.append("    rolesRef: ").append(toIndentedString(rolesRef)).append("\n");
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

