/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.role.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePostRequestGroup;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RolePostRequestUser;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RolePostRequest  {
  
    private String displayName;
    private List<RolePostRequestUser> users = null;

    private List<RolePostRequestGroup> groups = null;

    private List<String> permissions = null;


    /**
    **/
    public RolePostRequest displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "loginRole", required = true, value = "")
    @JsonProperty("displayName")
    @Valid
    @NotNull(message = "Property displayName cannot be null.")

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    **/
    public RolePostRequest users(List<RolePostRequestUser> users) {

        this.users = users;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("users")
    @Valid
    public List<RolePostRequestUser> getUsers() {
        return users;
    }
    public void setUsers(List<RolePostRequestUser> users) {
        this.users = users;
    }

    public RolePostRequest addUsersItem(RolePostRequestUser usersItem) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        this.users.add(usersItem);
        return this;
    }

        /**
    **/
    public RolePostRequest groups(List<RolePostRequestGroup> groups) {

        this.groups = groups;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("groups")
    @Valid
    public List<RolePostRequestGroup> getGroups() {
        return groups;
    }
    public void setGroups(List<RolePostRequestGroup> groups) {
        this.groups = groups;
    }

    public RolePostRequest addGroupsItem(RolePostRequestGroup groupsItem) {
        if (this.groups == null) {
            this.groups = new ArrayList<>();
        }
        this.groups.add(groupsItem);
        return this;
    }

        /**
    **/
    public RolePostRequest permissions(List<String> permissions) {

        this.permissions = permissions;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("permissions")
    @Valid
    public List<String> getPermissions() {
        return permissions;
    }
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public RolePostRequest addPermissionsItem(String permissionsItem) {
        if (this.permissions == null) {
            this.permissions = new ArrayList<>();
        }
        this.permissions.add(permissionsItem);
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
        RolePostRequest rolePostRequest = (RolePostRequest) o;
        return Objects.equals(this.displayName, rolePostRequest.displayName) &&
            Objects.equals(this.users, rolePostRequest.users) &&
            Objects.equals(this.groups, rolePostRequest.groups) &&
            Objects.equals(this.permissions, rolePostRequest.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, users, groups, permissions);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RolePostRequest {\n");
        
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    users: ").append(toIndentedString(users)).append("\n");
        sb.append("    groups: ").append(toIndentedString(groups)).append("\n");
        sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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

