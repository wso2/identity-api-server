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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.RoleWithAudience;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class UserShareRequestBodyOrganizations  {
  
    private String orgId;

@XmlType(name="PolicyEnum")
@XmlEnum(String.class)
public enum PolicyEnum {

    @XmlEnumValue("THIS ORG ONLY") ONLY(String.valueOf("THIS ORG ONLY")), @XmlEnumValue("THIS ORG AND ALL EXISTING CHILDREN OF THIS ORG") AND_ALL_EXISTING_CHILDREN_OF_THIS_ORG(String.valueOf("THIS ORG AND ALL EXISTING CHILDREN OF THIS ORG")), @XmlEnumValue("THIS ORG AND ALL EXISTING AND FUTURE CHILDREN") AND_ALL_EXISTING_AND_FUTURE_CHILDREN(String.valueOf("THIS ORG AND ALL EXISTING AND FUTURE CHILDREN"));


    private String value;

    PolicyEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static PolicyEnum fromValue(String value) {
        for (PolicyEnum b : PolicyEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private PolicyEnum policy;
    private List<RoleWithAudience> roles = null;


    /**
    * The ID of the organization to share the users with.
    **/
    public UserShareRequestBodyOrganizations orgId(String orgId) {

        this.orgId = orgId;
        return this;
    }
    
    @ApiModelProperty(value = "The ID of the organization to share the users with.")
    @JsonProperty("orgId")
    @Valid
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
    * The scope of sharing for this organization.
    **/
    public UserShareRequestBodyOrganizations policy(PolicyEnum policy) {

        this.policy = policy;
        return this;
    }
    
    @ApiModelProperty(value = "The scope of sharing for this organization.")
    @JsonProperty("policy")
    @Valid
    public PolicyEnum getPolicy() {
        return policy;
    }
    public void setPolicy(PolicyEnum policy) {
        this.policy = policy;
    }

    /**
    * A list of roles to be shared with the organization.
    **/
    public UserShareRequestBodyOrganizations roles(List<RoleWithAudience> roles) {

        this.roles = roles;
        return this;
    }
    
    @ApiModelProperty(value = "A list of roles to be shared with the organization.")
    @JsonProperty("roles")
    @Valid
    public List<RoleWithAudience> getRoles() {
        return roles;
    }
    public void setRoles(List<RoleWithAudience> roles) {
        this.roles = roles;
    }

    public UserShareRequestBodyOrganizations addRolesItem(RoleWithAudience rolesItem) {
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
        UserShareRequestBodyOrganizations userShareRequestBodyOrganizations = (UserShareRequestBodyOrganizations) o;
        return Objects.equals(this.orgId, userShareRequestBodyOrganizations.orgId) &&
            Objects.equals(this.policy, userShareRequestBodyOrganizations.policy) &&
            Objects.equals(this.roles, userShareRequestBodyOrganizations.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, policy, roles);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserShareRequestBodyOrganizations {\n");
        
        sb.append("    orgId: ").append(toIndentedString(orgId)).append("\n");
        sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
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

