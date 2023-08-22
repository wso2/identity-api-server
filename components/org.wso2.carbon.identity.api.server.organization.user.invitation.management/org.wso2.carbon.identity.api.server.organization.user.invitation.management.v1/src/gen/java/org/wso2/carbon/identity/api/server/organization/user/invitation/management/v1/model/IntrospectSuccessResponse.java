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
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class IntrospectSuccessResponse  {
  
    private String confirmationCode;
    private String username;
    private String userOrganization;
    private String initiatedOrganization;
    private String status;

    /**
    * Confirmation code of the invitation which needs to be introspected.
    **/
    public IntrospectSuccessResponse confirmationCode(String confirmationCode) {

        this.confirmationCode = confirmationCode;
        return this;
    }
    
    @ApiModelProperty(example = "2663329b-c8c5-4c71-9500-9ea8c4e77d94", required = true, value = "Confirmation code of the invitation which needs to be introspected.")
    @JsonProperty("confirmationCode")
    @Valid
    @NotNull(message = "Property confirmationCode cannot be null.")

    public String getConfirmationCode() {
        return confirmationCode;
    }
    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    /**
    * Username of the user who will be invited to the organization. This can be an email or an alphanumeric username.
    **/
    public IntrospectSuccessResponse username(String username) {

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
    * Organization which the user is residing.
    **/
    public IntrospectSuccessResponse userOrganization(String userOrganization) {

        this.userOrganization = userOrganization;
        return this;
    }
    
    @ApiModelProperty(example = "8763329b-c8c5-4c71-9500-9ea8c4e77345", required = true, value = "Organization which the user is residing.")
    @JsonProperty("userOrganization")
    @Valid
    @NotNull(message = "Property userOrganization cannot be null.")

    public String getUserOrganization() {
        return userOrganization;
    }
    public void setUserOrganization(String userOrganization) {
        this.userOrganization = userOrganization;
    }

    /**
    * Organization which the invitation is initiated.
    **/
    public IntrospectSuccessResponse initiatedOrganization(String initiatedOrganization) {

        this.initiatedOrganization = initiatedOrganization;
        return this;
    }
    
    @ApiModelProperty(example = "1239329b-c8c5-4c71-9500-9ea8c4e70987", required = true, value = "Organization which the invitation is initiated.")
    @JsonProperty("initiatedOrganization")
    @Valid
    @NotNull(message = "Property initiatedOrganization cannot be null.")

    public String getInitiatedOrganization() {
        return initiatedOrganization;
    }
    public void setInitiatedOrganization(String initiatedOrganization) {
        this.initiatedOrganization = initiatedOrganization;
    }

    /**
    * Status of the invitation.
    **/
    public IntrospectSuccessResponse status(String status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "PENDING/EXPIRED", required = true, value = "Status of the invitation.")
    @JsonProperty("status")
    @Valid
    @NotNull(message = "Property status cannot be null.")

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IntrospectSuccessResponse introspectSuccessResponse = (IntrospectSuccessResponse) o;
        return Objects.equals(this.confirmationCode, introspectSuccessResponse.confirmationCode) &&
            Objects.equals(this.username, introspectSuccessResponse.username) &&
            Objects.equals(this.userOrganization, introspectSuccessResponse.userOrganization) &&
            Objects.equals(this.initiatedOrganization, introspectSuccessResponse.initiatedOrganization) &&
            Objects.equals(this.status, introspectSuccessResponse.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmationCode, username, userOrganization, initiatedOrganization, status);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class IntrospectSuccessResponse {\n");
        
        sb.append("    confirmationCode: ").append(toIndentedString(confirmationCode)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    userOrganization: ").append(toIndentedString(userOrganization)).append("\n");
        sb.append("    initiatedOrganization: ").append(toIndentedString(initiatedOrganization)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

