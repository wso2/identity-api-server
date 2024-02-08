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

package org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class InvitationRequestBody  {
  
    private List<String> usernames = new ArrayList<>();

    private String userDomain;
    private List<String> roles = null;

    private List<String> groups = null;


    /**
    * List of usernames of the users who will be invited to the organization. This can be an email or an alphanumeric username.
    **/
    public InvitationRequestBody usernames(List<String> usernames) {

        this.usernames = usernames;
        return this;
    }
    
    @ApiModelProperty(example = "[\"xyz@gmail.com\",\"abc@gmail.com\"]", required = true, value = "List of usernames of the users who will be invited to the organization. This can be an email or an alphanumeric username.")
    @JsonProperty("usernames")
    @Valid
    @NotNull(message = "Property usernames cannot be null.")

    public List<String> getUsernames() {
        return usernames;
    }
    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public InvitationRequestBody addUsernamesItem(String usernamesItem) {
        this.usernames.add(usernamesItem);
        return this;
    }

        /**
    * User store domain of the user. If not provided, default userstore will be used.
    **/
    public InvitationRequestBody userDomain(String userDomain) {

        this.userDomain = userDomain;
        return this;
    }
    
    @ApiModelProperty(example = "PRIMARY", value = "User store domain of the user. If not provided, default userstore will be used.")
    @JsonProperty("userDomain")
    @Valid
    public String getUserDomain() {
        return userDomain;
    }
    public void setUserDomain(String userDomain) {
        this.userDomain = userDomain;
    }

    /**
    * Role assignments which the user will be assigned to.
    **/
    public InvitationRequestBody roles(List<String> roles) {

        this.roles = roles;
        return this;
    }
    
    @ApiModelProperty(example = "[\"f5b761ca-62f2-48ba-935b-f7b460f58e5c\",\"657fgq22-62f2-48ba-935b-f7bfgh6438fd\"]", value = "Role assignments which the user will be assigned to.")
    @JsonProperty("roles")
    @Valid
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public InvitationRequestBody addRolesItem(String rolesItem) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(rolesItem);
        return this;
    }

        /**
    * Group assignments which the user will be assigned to.
    **/
    public InvitationRequestBody groups(List<String> groups) {

        this.groups = groups;
        return this;
    }
    
    @ApiModelProperty(example = "[\"48badf-rty20-48ba-935b-f7b460f58e5c\",\"fd234100-c115-45dc-ad11-70846b783866\"]", value = "Group assignments which the user will be assigned to.")
    @JsonProperty("groups")
    @Valid
    public List<String> getGroups() {
        return groups;
    }
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public InvitationRequestBody addGroupsItem(String groupsItem) {
        if (this.groups == null) {
            this.groups = new ArrayList<>();
        }
        this.groups.add(groupsItem);
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
        InvitationRequestBody invitationRequestBody = (InvitationRequestBody) o;
        return Objects.equals(this.usernames, invitationRequestBody.usernames) &&
            Objects.equals(this.userDomain, invitationRequestBody.userDomain) &&
            Objects.equals(this.roles, invitationRequestBody.roles) &&
            Objects.equals(this.groups, invitationRequestBody.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usernames, userDomain, roles, groups);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InvitationRequestBody {\n");
        
        sb.append("    usernames: ").append(toIndentedString(usernames)).append("\n");
        sb.append("    userDomain: ").append(toIndentedString(userDomain)).append("\n");
        sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
        sb.append("    groups: ").append(toIndentedString(groups)).append("\n");
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

