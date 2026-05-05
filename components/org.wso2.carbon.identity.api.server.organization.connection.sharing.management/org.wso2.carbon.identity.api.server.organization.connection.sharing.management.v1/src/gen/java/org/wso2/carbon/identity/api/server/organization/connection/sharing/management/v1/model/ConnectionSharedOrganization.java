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

package org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionSharingMode;
import javax.validation.constraints.*;

/**
 * Represents a single organization that a connection has been shared with, including organization metadata and optionally the sharing mode.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Represents a single organization that a connection has been shared with, including organization metadata and optionally the sharing mode.")
public class ConnectionSharedOrganization  {
  
    private String orgId;
    private String orgName;
    private String orgHandle;
    private String orgStatus;
    private String orgRef;
    private Boolean hasChildren;
    private Integer depthFromRoot;
    private String parentOrgId;
    private String parentConnectionId;
    private String sharedConnectionId;
    private ConnectionSharingMode sharingMode;

    /**
    * ID of the child organization.
    **/
    public ConnectionSharedOrganization orgId(String orgId) {

        this.orgId = orgId;
        return this;
    }
    
    @ApiModelProperty(example = "b028ca17-8f89-449c-ae27-fa955e66465d", value = "ID of the child organization.")
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
    public ConnectionSharedOrganization orgName(String orgName) {

        this.orgName = orgName;
        return this;
    }
    
    @ApiModelProperty(example = "Child Org 1", value = "Name of the child organization.")
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
    public ConnectionSharedOrganization orgHandle(String orgHandle) {

        this.orgHandle = orgHandle;
        return this;
    }
    
    @ApiModelProperty(example = "child-org-1", value = "Unique, human-readable handle of the organization.")
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
    public ConnectionSharedOrganization orgStatus(String orgStatus) {

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
    * API reference URL of the organization resource.
    **/
    public ConnectionSharedOrganization orgRef(String orgRef) {

        this.orgRef = orgRef;
        return this;
    }
    
    @ApiModelProperty(example = "/t/wso2.com/api/server/v1/organizations/b028ca17-8f89-449c-ae27-fa955e66465d", value = "API reference URL of the organization resource.")
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
    public ConnectionSharedOrganization hasChildren(Boolean hasChildren) {

        this.hasChildren = hasChildren;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Indicates whether the organization has child organizations.")
    @JsonProperty("hasChildren")
    @Valid
    public Boolean getHasChildren() {
        return hasChildren;
    }
    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    /**
    * Depth of the organization in the hierarchy, where the root organization is 0.
    **/
    public ConnectionSharedOrganization depthFromRoot(Integer depthFromRoot) {

        this.depthFromRoot = depthFromRoot;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "Depth of the organization in the hierarchy, where the root organization is 0.")
    @JsonProperty("depthFromRoot")
    @Valid
    public Integer getDepthFromRoot() {
        return depthFromRoot;
    }
    public void setDepthFromRoot(Integer depthFromRoot) {
        this.depthFromRoot = depthFromRoot;
    }

    /**
    * ID of the parent (sharing-initiated) organization.
    **/
    public ConnectionSharedOrganization parentOrgId(String parentOrgId) {

        this.parentOrgId = parentOrgId;
        return this;
    }
    
    @ApiModelProperty(example = "08f8c1d2-4b3e-4c5a-9f6b-7d8e9f0a1b2c", value = "ID of the parent (sharing-initiated) organization.")
    @JsonProperty("parentOrgId")
    @Valid
    public String getParentOrgId() {
        return parentOrgId;
    }
    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    /**
    * UUID of the original connection in the parent (sharing-initiated) organization.
    **/
    public ConnectionSharedOrganization parentConnectionId(String parentConnectionId) {

        this.parentConnectionId = parentConnectionId;
        return this;
    }
    
    @ApiModelProperty(example = "7a1b7d63-8cfc-4dc9-9332-3f84641b72d8", value = "UUID of the original connection in the parent (sharing-initiated) organization.")
    @JsonProperty("parentConnectionId")
    @Valid
    public String getParentConnectionId() {
        return parentConnectionId;
    }
    public void setParentConnectionId(String parentConnectionId) {
        this.parentConnectionId = parentConnectionId;
    }

    /**
    * UUID of the shadow connection created in this child organization.
    **/
    public ConnectionSharedOrganization sharedConnectionId(String sharedConnectionId) {

        this.sharedConnectionId = sharedConnectionId;
        return this;
    }
    
    @ApiModelProperty(example = "550e8400-e29b-41d4-a716-446655440000", value = "UUID of the shadow connection created in this child organization.")
    @JsonProperty("sharedConnectionId")
    @Valid
    public String getSharedConnectionId() {
        return sharedConnectionId;
    }
    public void setSharedConnectionId(String sharedConnectionId) {
        this.sharedConnectionId = sharedConnectionId;
    }

    /**
    **/
    public ConnectionSharedOrganization sharingMode(ConnectionSharingMode sharingMode) {

        this.sharingMode = sharingMode;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("sharingMode")
    @Valid
    public ConnectionSharingMode getSharingMode() {
        return sharingMode;
    }
    public void setSharingMode(ConnectionSharingMode sharingMode) {
        this.sharingMode = sharingMode;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConnectionSharedOrganization connectionSharedOrganization = (ConnectionSharedOrganization) o;
        return Objects.equals(this.orgId, connectionSharedOrganization.orgId) &&
            Objects.equals(this.orgName, connectionSharedOrganization.orgName) &&
            Objects.equals(this.orgHandle, connectionSharedOrganization.orgHandle) &&
            Objects.equals(this.orgStatus, connectionSharedOrganization.orgStatus) &&
            Objects.equals(this.orgRef, connectionSharedOrganization.orgRef) &&
            Objects.equals(this.hasChildren, connectionSharedOrganization.hasChildren) &&
            Objects.equals(this.depthFromRoot, connectionSharedOrganization.depthFromRoot) &&
            Objects.equals(this.parentOrgId, connectionSharedOrganization.parentOrgId) &&
            Objects.equals(this.parentConnectionId, connectionSharedOrganization.parentConnectionId) &&
            Objects.equals(this.sharedConnectionId, connectionSharedOrganization.sharedConnectionId) &&
            Objects.equals(this.sharingMode, connectionSharedOrganization.sharingMode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, orgName, orgHandle, orgStatus, orgRef, hasChildren, depthFromRoot, parentOrgId, parentConnectionId, sharedConnectionId, sharingMode);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConnectionSharedOrganization {\n");
        
        sb.append("    orgId: ").append(toIndentedString(orgId)).append("\n");
        sb.append("    orgName: ").append(toIndentedString(orgName)).append("\n");
        sb.append("    orgHandle: ").append(toIndentedString(orgHandle)).append("\n");
        sb.append("    orgStatus: ").append(toIndentedString(orgStatus)).append("\n");
        sb.append("    orgRef: ").append(toIndentedString(orgRef)).append("\n");
        sb.append("    hasChildren: ").append(toIndentedString(hasChildren)).append("\n");
        sb.append("    depthFromRoot: ").append(toIndentedString(depthFromRoot)).append("\n");
        sb.append("    parentOrgId: ").append(toIndentedString(parentOrgId)).append("\n");
        sb.append("    parentConnectionId: ").append(toIndentedString(parentConnectionId)).append("\n");
        sb.append("    sharedConnectionId: ").append(toIndentedString(sharedConnectionId)).append("\n");
        sb.append("    sharingMode: ").append(toIndentedString(sharingMode)).append("\n");
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

