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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.RoleAssignment;
import javax.validation.constraints.*;

/**
 * Per-organization sharing configuration for selected organizations.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Per-organization sharing configuration for selected organizations.")
public class UserOrgShareConfig  {
  
    private String orgId;
    private String policy;
    private RoleAssignment roleAssignment;

    /**
    * Organization ID to share the users with.
    **/
    public UserOrgShareConfig orgId(String orgId) {

        this.orgId = orgId;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Organization ID to share the users with.")
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
    * Sharing scope for this organization.  Possible values: - &#x60;SELECTED_ORG_ONLY&#x60; - &#x60;SELECTED_ORG_WITH_ALL_EXISTING_CHILDREN_ONLY&#x60; - &#x60;SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN&#x60; - &#x60;SELECTED_ORG_WITH_EXISTING_IMMEDIATE_CHILDREN_ONLY&#x60; - &#x60;SELECTED_ORG_WITH_EXISTING_IMMEDIATE_AND_FUTURE_CHILDREN&#x60;
    **/
    public UserOrgShareConfig policy(String policy) {

        this.policy = policy;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Sharing scope for this organization.  Possible values: - `SELECTED_ORG_ONLY` - `SELECTED_ORG_WITH_ALL_EXISTING_CHILDREN_ONLY` - `SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN` - `SELECTED_ORG_WITH_EXISTING_IMMEDIATE_CHILDREN_ONLY` - `SELECTED_ORG_WITH_EXISTING_IMMEDIATE_AND_FUTURE_CHILDREN`")
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
    public UserOrgShareConfig roleAssignment(RoleAssignment roleAssignment) {

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
        UserOrgShareConfig userOrgShareConfig = (UserOrgShareConfig) o;
        return Objects.equals(this.orgId, userOrgShareConfig.orgId) &&
            Objects.equals(this.policy, userOrgShareConfig.policy) &&
            Objects.equals(this.roleAssignment, userOrgShareConfig.roleAssignment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, policy, roleAssignment);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserOrgShareConfig {\n");
        
        sb.append("    orgId: ").append(toIndentedString(orgId)).append("\n");
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

