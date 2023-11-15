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

public class InvitationResponse  {
  
    private String id;
    private String username;
    private String email;
    private List<RoleAssignmentResponse> roles = null;

    private String status;
    private String expiredAt;

    /**
    **/
    public InvitationResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "f7594498-5b52-4201-abd5-d7cf72565c73", required = true, value = "")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public InvitationResponse username(String username) {

        this.username = username;
        return this;
    }
    
    @ApiModelProperty(example = "alex@gmail.com/alex", required = true, value = "")
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
    **/
    public InvitationResponse email(String email) {

        this.email = email;
        return this;
    }
    
    @ApiModelProperty(example = "alex@gmail.com", required = true, value = "")
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
    **/
    public InvitationResponse roles(List<RoleAssignmentResponse> roles) {

        this.roles = roles;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("roles")
    @Valid
    public List<RoleAssignmentResponse> getRoles() {
        return roles;
    }
    public void setRoles(List<RoleAssignmentResponse> roles) {
        this.roles = roles;
    }

    public InvitationResponse addRolesItem(RoleAssignmentResponse rolesItem) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(rolesItem);
        return this;
    }

        /**
    **/
    public InvitationResponse status(String status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "PENDING/EXPIRED", required = true, value = "")
    @JsonProperty("status")
    @Valid
    @NotNull(message = "Property status cannot be null.")

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    /**
    **/
    public InvitationResponse expiredAt(String expiredAt) {

        this.expiredAt = expiredAt;
        return this;
    }
    
    @ApiModelProperty(example = "2021-08-10T10:15:30.00Z", value = "")
    @JsonProperty("expiredAt")
    @Valid
    public String getExpiredAt() {
        return expiredAt;
    }
    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvitationResponse invitationResponse = (InvitationResponse) o;
        return Objects.equals(this.id, invitationResponse.id) &&
            Objects.equals(this.username, invitationResponse.username) &&
            Objects.equals(this.email, invitationResponse.email) &&
            Objects.equals(this.roles, invitationResponse.roles) &&
            Objects.equals(this.status, invitationResponse.status) &&
            Objects.equals(this.expiredAt, invitationResponse.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, roles, status, expiredAt);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InvitationResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    expiredAt: ").append(toIndentedString(expiredAt)).append("\n");
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

