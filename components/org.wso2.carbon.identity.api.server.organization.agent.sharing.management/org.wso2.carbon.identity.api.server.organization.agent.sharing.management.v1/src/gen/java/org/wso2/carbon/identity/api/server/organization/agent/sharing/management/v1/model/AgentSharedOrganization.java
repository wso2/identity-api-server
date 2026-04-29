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
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.RoleShareConfig;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.SharingMode;
import javax.validation.constraints.*;

/**
 * Represents a single organization where the agent has shared access, including organization metadata and effective roles.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Represents a single organization where the agent has shared access, including organization metadata and effective roles.")
public class AgentSharedOrganization  {
  
    private String orgId;
    private String orgName;
    private String orgHandle;
    private String orgStatus;
    private String orgRef;
    private Boolean hasChildren;
    private Integer depthFromRoot;
    private String parentOrgId;
    private String parentAgentId;
    private String sharedAgentId;
    private String sharedType;
    private SharingMode sharingMode;
    private List<RoleShareConfig> roles = null;


    /**
    * ID of the child organization.
    **/
    public AgentSharedOrganization orgId(String orgId) {

        this.orgId = orgId;
        return this;
    }
    
    @ApiModelProperty(value = "ID of the child organization.")
    @JsonProperty("orgId")
    @Valid
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
    * Name of the child organization.
    **/
    public AgentSharedOrganization orgName(String orgName) {

        this.orgName = orgName;
        return this;
    }
    
    @ApiModelProperty(value = "Name of the child organization.")
    @JsonProperty("orgName")
    @Valid
    public String getOrgName() {
        return orgName;
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
    * Unique, human-readable handle of the organization.
    **/
    public AgentSharedOrganization orgHandle(String orgHandle) {

        this.orgHandle = orgHandle;
        return this;
    }
    
    @ApiModelProperty(example = "sampleOrg", value = "Unique, human-readable handle of the organization.")
    @JsonProperty("orgHandle")
    @Valid
    public String getOrgHandle() {
        return orgHandle;
    }
    public void setOrgHandle(String orgHandle) {
        this.orgHandle = orgHandle;
    }

    /**
    * Current status of the organization.
    **/
    public AgentSharedOrganization orgStatus(String orgStatus) {

        this.orgStatus = orgStatus;
        return this;
    }
    
    @ApiModelProperty(example = "ACTIVE", value = "Current status of the organization.")
    @JsonProperty("orgStatus")
    @Valid
    public String getOrgStatus() {
        return orgStatus;
    }
    public void setOrgStatus(String orgStatus) {
        this.orgStatus = orgStatus;
    }

    /**
    * API reference of the organization resource.
    **/
    public AgentSharedOrganization orgRef(String orgRef) {

        this.orgRef = orgRef;
        return this;
    }
    
    @ApiModelProperty(example = "/t/wso2.com/api/server/v1/organizations/b4526d91-a8bf-43d2-8b14-c548cf73065b", value = "API reference of the organization resource.")
    @JsonProperty("orgRef")
    @Valid
    public String getOrgRef() {
        return orgRef;
    }
    public void setOrgRef(String orgRef) {
        this.orgRef = orgRef;
    }

    /**
    * Indicates whether the organization has child organizations.
    **/
    public AgentSharedOrganization hasChildren(Boolean hasChildren) {

        this.hasChildren = hasChildren;
        return this;
    }
    
    @ApiModelProperty(value = "Indicates whether the organization has child organizations.")
    @JsonProperty("hasChildren")
    @Valid
    public Boolean getHasChildren() {
        return hasChildren;
    }
    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    /**
    * Depth of the organization in the hierarchy, where root is 0.
    **/
    public AgentSharedOrganization depthFromRoot(Integer depthFromRoot) {

        this.depthFromRoot = depthFromRoot;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "Depth of the organization in the hierarchy, where root is 0.")
    @JsonProperty("depthFromRoot")
    @Valid
    public Integer getDepthFromRoot() {
        return depthFromRoot;
    }
    public void setDepthFromRoot(Integer depthFromRoot) {
        this.depthFromRoot = depthFromRoot;
    }

    /**
    * ID of the parent organization (if applicable).
    **/
    public AgentSharedOrganization parentOrgId(String parentOrgId) {

        this.parentOrgId = parentOrgId;
        return this;
    }
    
    @ApiModelProperty(value = "ID of the parent organization (if applicable).")
    @JsonProperty("parentOrgId")
    @Valid
    public String getParentOrgId() {
        return parentOrgId;
    }
    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    /**
    * ID of the original agent in the home organization.
    **/
    public AgentSharedOrganization parentAgentId(String parentAgentId) {

        this.parentAgentId = parentAgentId;
        return this;
    }
    
    @ApiModelProperty(value = "ID of the original agent in the home organization.")
    @JsonProperty("parentAgentId")
    @Valid
    public String getParentAgentId() {
        return parentAgentId;
    }
    public void setParentAgentId(String parentAgentId) {
        this.parentAgentId = parentAgentId;
    }

    /**
    * ID of the **shared agent object** in the target organization (if applicable).
    **/
    public AgentSharedOrganization sharedAgentId(String sharedAgentId) {

        this.sharedAgentId = sharedAgentId;
        return this;
    }
    
    @ApiModelProperty(value = "ID of the **shared agent object** in the target organization (if applicable).")
    @JsonProperty("sharedAgentId")
    @Valid
    public String getSharedAgentId() {
        return sharedAgentId;
    }
    public void setSharedAgentId(String sharedAgentId) {
        this.sharedAgentId = sharedAgentId;
    }

    /**
    * Shared type of the agent. Example values: &#x60;SHARED&#x60;.
    **/
    public AgentSharedOrganization sharedType(String sharedType) {

        this.sharedType = sharedType;
        return this;
    }
    
    @ApiModelProperty(example = "SHARED", value = "Shared type of the agent. Example values: `SHARED`.")
    @JsonProperty("sharedType")
    @Valid
    public String getSharedType() {
        return sharedType;
    }
    public void setSharedType(String sharedType) {
        this.sharedType = sharedType;
    }

    /**
    **/
    public AgentSharedOrganization sharingMode(SharingMode sharingMode) {

        this.sharingMode = sharingMode;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("sharingMode")
    @Valid
    public SharingMode getSharingMode() {
        return sharingMode;
    }
    public void setSharingMode(SharingMode sharingMode) {
        this.sharingMode = sharingMode;
    }

    /**
    * Effective roles assigned to this agent in this organization via agent sharing.
    **/
    public AgentSharedOrganization roles(List<RoleShareConfig> roles) {

        this.roles = roles;
        return this;
    }
    
    @ApiModelProperty(value = "Effective roles assigned to this agent in this organization via agent sharing.")
    @JsonProperty("roles")
    @Valid
    public List<RoleShareConfig> getRoles() {
        return roles;
    }
    public void setRoles(List<RoleShareConfig> roles) {
        this.roles = roles;
    }

    public AgentSharedOrganization addRolesItem(RoleShareConfig rolesItem) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(rolesItem);
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
        AgentSharedOrganization agentSharedOrganization = (AgentSharedOrganization) o;
        return Objects.equals(this.orgId, agentSharedOrganization.orgId) &&
            Objects.equals(this.orgName, agentSharedOrganization.orgName) &&
            Objects.equals(this.orgHandle, agentSharedOrganization.orgHandle) &&
            Objects.equals(this.orgStatus, agentSharedOrganization.orgStatus) &&
            Objects.equals(this.orgRef, agentSharedOrganization.orgRef) &&
            Objects.equals(this.hasChildren, agentSharedOrganization.hasChildren) &&
            Objects.equals(this.depthFromRoot, agentSharedOrganization.depthFromRoot) &&
            Objects.equals(this.parentOrgId, agentSharedOrganization.parentOrgId) &&
            Objects.equals(this.parentAgentId, agentSharedOrganization.parentAgentId) &&
            Objects.equals(this.sharedAgentId, agentSharedOrganization.sharedAgentId) &&
            Objects.equals(this.sharedType, agentSharedOrganization.sharedType) &&
            Objects.equals(this.sharingMode, agentSharedOrganization.sharingMode) &&
            Objects.equals(this.roles, agentSharedOrganization.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, orgName, orgHandle, orgStatus, orgRef, hasChildren, depthFromRoot, parentOrgId, parentAgentId, sharedAgentId, sharedType, sharingMode, roles);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentSharedOrganization {\n");
        
        sb.append("    orgId: ").append(toIndentedString(orgId)).append("\n");
        sb.append("    orgName: ").append(toIndentedString(orgName)).append("\n");
        sb.append("    orgHandle: ").append(toIndentedString(orgHandle)).append("\n");
        sb.append("    orgStatus: ").append(toIndentedString(orgStatus)).append("\n");
        sb.append("    orgRef: ").append(toIndentedString(orgRef)).append("\n");
        sb.append("    hasChildren: ").append(toIndentedString(hasChildren)).append("\n");
        sb.append("    depthFromRoot: ").append(toIndentedString(depthFromRoot)).append("\n");
        sb.append("    parentOrgId: ").append(toIndentedString(parentOrgId)).append("\n");
        sb.append("    parentAgentId: ").append(toIndentedString(parentAgentId)).append("\n");
        sb.append("    sharedAgentId: ").append(toIndentedString(sharedAgentId)).append("\n");
        sb.append("    sharedType: ").append(toIndentedString(sharedType)).append("\n");
        sb.append("    sharingMode: ").append(toIndentedString(sharingMode)).append("\n");
        sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
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

