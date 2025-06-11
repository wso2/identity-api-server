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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.RoleSharing;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class OrgHierarchyNode  {
  
    private String orgId;
    private String policy;
    private RoleSharing roleSharing;
    private List<OrgHierarchyNode> children = new ArrayList<>();


    /**
    **/
    public OrgHierarchyNode orgId(String orgId) {

        this.orgId = orgId;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("orgId")
    @Valid
    @NotNull(message = "Property orgId cannot be null.")

    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
    **/
    public OrgHierarchyNode policy(String policy) {

        this.policy = policy;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
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
    public OrgHierarchyNode roleSharing(RoleSharing roleSharing) {

        this.roleSharing = roleSharing;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("roleSharing")
    @Valid
    @NotNull(message = "Property roleSharing cannot be null.")

    public RoleSharing getRoleSharing() {
        return roleSharing;
    }
    public void setRoleSharing(RoleSharing roleSharing) {
        this.roleSharing = roleSharing;
    }

    /**
    **/
    public OrgHierarchyNode children(List<OrgHierarchyNode> children) {

        this.children = children;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("children")
    @Valid
    @NotNull(message = "Property children cannot be null.")

    public List<OrgHierarchyNode> getChildren() {
        return children;
    }
    public void setChildren(List<OrgHierarchyNode> children) {
        this.children = children;
    }

    public OrgHierarchyNode addChildrenItem(OrgHierarchyNode childrenItem) {
        this.children.add(childrenItem);
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
        OrgHierarchyNode orgHierarchyNode = (OrgHierarchyNode) o;
        return Objects.equals(this.orgId, orgHierarchyNode.orgId) &&
            Objects.equals(this.policy, orgHierarchyNode.policy) &&
            Objects.equals(this.roleSharing, orgHierarchyNode.roleSharing) &&
            Objects.equals(this.children, orgHierarchyNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, policy, roleSharing, children);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class OrgHierarchyNode {\n");
        
        sb.append("    orgId: ").append(toIndentedString(orgId)).append("\n");
        sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
        sb.append("    roleSharing: ").append(toIndentedString(roleSharing)).append("\n");
        sb.append("    children: ").append(toIndentedString(children)).append("\n");
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

