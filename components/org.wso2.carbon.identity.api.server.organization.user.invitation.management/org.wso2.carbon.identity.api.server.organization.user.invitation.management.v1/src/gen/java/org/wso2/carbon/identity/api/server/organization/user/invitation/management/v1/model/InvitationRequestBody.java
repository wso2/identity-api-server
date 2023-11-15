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
  
    private String username;
    private String userDomain;
    private List<String> roles = null;


    /**
    * Username of the user who will be invited to the organization. This can be an email or an alphanumeric username.
    **/
    public InvitationRequestBody username(String username) {

        this.username = username;
        return this;
    }
    
    @ApiModelProperty(example = "alex@gmail.com/alex", required = true, value = "Username of the user who will be invited to the organization. This can be an email or an alphanumeric username.")
    @JsonProperty("username")
    @Valid
    @NotNull(message = "Property username cannot be null.")

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    /**
    * User store domain of the user. If not provided, PRIMARY will be used.
    **/
    public InvitationRequestBody userDomain(String userDomain) {

        this.userDomain = userDomain;
        return this;
    }
    
    @ApiModelProperty(example = "PRIMARY", value = "User store domain of the user. If not provided, PRIMARY will be used.")
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
    
    @ApiModelProperty(value = "Role assignments which the user will be assigned to.")
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

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvitationRequestBody invitationRequestBody = (InvitationRequestBody) o;
        return Objects.equals(this.username, invitationRequestBody.username) &&
            Objects.equals(this.userDomain, invitationRequestBody.userDomain) &&
            Objects.equals(this.roles, invitationRequestBody.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, userDomain, roles);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InvitationRequestBody {\n");
        
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    userDomain: ").append(toIndentedString(userDomain)).append("\n");
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

