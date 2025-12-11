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
 * Represents the *policy + role assignment* mode used for sharing.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Represents the *policy + role assignment* mode used for sharing.")
public class SharingMode  {
  
    private String policy;
    private RoleAssignment roleAssignment;

    /**
    * Effective sharing policy. Values depend on context: - For global sharing (share-with-all): &#x60;ALL_EXISTING_ORGS_ONLY&#x60;, &#x60;ALL_EXISTING_AND_FUTURE_ORGS&#x60;,   &#x60;IMMEDIATE_EXISTING_ORGS_ONLY&#x60;, &#x60;IMMEDIATE_EXISTING_AND_FUTURE_ORGS&#x60;. - For per-org configs: &#x60;SELECTED_ORG_*&#x60; variants.
    **/
    public SharingMode policy(String policy) {

        this.policy = policy;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Effective sharing policy. Values depend on context: - For global sharing (share-with-all): `ALL_EXISTING_ORGS_ONLY`, `ALL_EXISTING_AND_FUTURE_ORGS`,   `IMMEDIATE_EXISTING_ORGS_ONLY`, `IMMEDIATE_EXISTING_AND_FUTURE_ORGS`. - For per-org configs: `SELECTED_ORG_*` variants.")
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
    public SharingMode roleAssignment(RoleAssignment roleAssignment) {

        this.roleAssignment = roleAssignment;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("roleAssignment")
    @Valid
    @NotNull(message = "Property roleAssignment cannot be null.")

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
        SharingMode sharingMode = (SharingMode) o;
        return Objects.equals(this.policy, sharingMode.policy) &&
            Objects.equals(this.roleAssignment, sharingMode.roleAssignment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policy, roleAssignment);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SharingMode {\n");
        
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

