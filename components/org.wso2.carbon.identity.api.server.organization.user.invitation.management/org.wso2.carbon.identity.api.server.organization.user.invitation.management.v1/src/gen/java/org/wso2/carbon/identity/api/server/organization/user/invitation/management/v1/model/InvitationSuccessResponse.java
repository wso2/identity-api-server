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
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.RoleAssignmentResponse;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class InvitationSuccessResponse  {
  
    private String username;
    private String email;
    private List<RoleAssignmentResponse> roleAssignments = new ArrayList<>();


    /**
    * Username of the user who will be invited to the organization. This can be an email or an alphanumeric username.
    **/
    public InvitationSuccessResponse username(String username) {

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
    * Email of the user who will be invited to the organization.
    **/
    public InvitationSuccessResponse email(String email) {

        this.email = email;
        return this;
    }
    
    @ApiModelProperty(example = "alex@gmail.com", required = true, value = "Email of the user who will be invited to the organization.")
    @JsonProperty("email")
    @Valid
    @NotNull(message = "Property email cannot be null.")

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    /**
    * Role assignments which the user will be assigned to.
    **/
    public InvitationSuccessResponse roleAssignments(List<RoleAssignmentResponse> roleAssignments) {

        this.roleAssignments = roleAssignments;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Role assignments which the user will be assigned to.")
    @JsonProperty("roleAssignments")
    @Valid
    @NotNull(message = "Property roleAssignments cannot be null.")

    public List<RoleAssignmentResponse> getRoleAssignments() {
        return roleAssignments;
    }
    public void setRoleAssignments(List<RoleAssignmentResponse> roleAssignments) {
        this.roleAssignments = roleAssignments;
    }

    public InvitationSuccessResponse addRoleAssignmentsItem(RoleAssignmentResponse roleAssignmentsItem) {
        this.roleAssignments.add(roleAssignmentsItem);
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
        InvitationSuccessResponse invitationSuccessResponse = (InvitationSuccessResponse) o;
        return Objects.equals(this.username, invitationSuccessResponse.username) &&
            Objects.equals(this.email, invitationSuccessResponse.email) &&
            Objects.equals(this.roleAssignments, invitationSuccessResponse.roleAssignments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, roleAssignments);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InvitationSuccessResponse {\n");
        
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    roleAssignments: ").append(toIndentedString(roleAssignments)).append("\n");
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

