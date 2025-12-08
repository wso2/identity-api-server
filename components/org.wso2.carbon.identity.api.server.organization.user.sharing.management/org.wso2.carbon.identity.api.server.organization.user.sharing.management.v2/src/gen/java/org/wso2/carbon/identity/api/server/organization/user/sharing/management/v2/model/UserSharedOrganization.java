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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.RoleShareConfig;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.SharingMode;
import javax.validation.constraints.*;

/**
 * Represents a single organization where the user has shared access, including effective roles.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Represents a single organization where the user has shared access, including effective roles.")
public class UserSharedOrganization  {
  
    private String orgId;
    private String orgName;
    private String sharedUserId;
    private String sharedType;
    private SharingMode sharingMode;
    private List<RoleShareConfig> roles = null;


    /**
    * ID of the child organization.
    **/
    public UserSharedOrganization orgId(String orgId) {

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
    public UserSharedOrganization orgName(String orgName) {

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
    * ID of the **shared user object** in the target organization (if applicable).
    **/
    public UserSharedOrganization sharedUserId(String sharedUserId) {

        this.sharedUserId = sharedUserId;
        return this;
    }
    
    @ApiModelProperty(value = "ID of the **shared user object** in the target organization (if applicable).")
    @JsonProperty("sharedUserId")
    @Valid
    public String getSharedUserId() {
        return sharedUserId;
    }
    public void setSharedUserId(String sharedUserId) {
        this.sharedUserId = sharedUserId;
    }

    /**
    * Shared type of the user. Example values: &#x60;SHARED&#x60;, &#x60;INVITED&#x60;.
    **/
    public UserSharedOrganization sharedType(String sharedType) {

        this.sharedType = sharedType;
        return this;
    }
    
    @ApiModelProperty(value = "Shared type of the user. Example values: `SHARED`, `INVITED`.")
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
    public UserSharedOrganization sharingMode(SharingMode sharingMode) {

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
    * Effective roles assigned to this user in this organization via user sharing.
    **/
    public UserSharedOrganization roles(List<RoleShareConfig> roles) {

        this.roles = roles;
        return this;
    }
    
    @ApiModelProperty(value = "Effective roles assigned to this user in this organization via user sharing.")
    @JsonProperty("roles")
    @Valid
    public List<RoleShareConfig> getRoles() {
        return roles;
    }
    public void setRoles(List<RoleShareConfig> roles) {
        this.roles = roles;
    }

    public UserSharedOrganization addRolesItem(RoleShareConfig rolesItem) {
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
        UserSharedOrganization userSharedOrganization = (UserSharedOrganization) o;
        return Objects.equals(this.orgId, userSharedOrganization.orgId) &&
            Objects.equals(this.orgName, userSharedOrganization.orgName) &&
            Objects.equals(this.sharedUserId, userSharedOrganization.sharedUserId) &&
            Objects.equals(this.sharedType, userSharedOrganization.sharedType) &&
            Objects.equals(this.sharingMode, userSharedOrganization.sharingMode) &&
            Objects.equals(this.roles, userSharedOrganization.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, orgName, sharedUserId, sharedType, sharingMode, roles);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserSharedOrganization {\n");
        
        sb.append("    orgId: ").append(toIndentedString(orgId)).append("\n");
        sb.append("    orgName: ").append(toIndentedString(orgName)).append("\n");
        sb.append("    sharedUserId: ").append(toIndentedString(sharedUserId)).append("\n");
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

