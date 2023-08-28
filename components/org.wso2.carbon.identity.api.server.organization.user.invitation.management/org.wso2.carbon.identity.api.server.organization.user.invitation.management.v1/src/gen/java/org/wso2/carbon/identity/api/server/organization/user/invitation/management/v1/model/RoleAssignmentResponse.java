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

public class RoleAssignmentResponse  {
  
    private String applicationId;
    private String applicationName;
    private List<String> roles = new ArrayList<>();


    /**
    **/
    public RoleAssignmentResponse applicationId(String applicationId) {

        this.applicationId = applicationId;
        return this;
    }
    
    @ApiModelProperty(example = "f7594498-5b52-4201-abd5-d7cf72565c73", required = true, value = "")
    @JsonProperty("applicationId")
    @Valid
    @NotNull(message = "Property applicationId cannot be null.")

    public String getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
    **/
    public RoleAssignmentResponse applicationName(String applicationName) {

        this.applicationName = applicationName;
        return this;
    }
    
    @ApiModelProperty(example = "b2b_business_sample_application", value = "")
    @JsonProperty("applicationName")
    @Valid
    public String getApplicationName() {
        return applicationName;
    }
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
    **/
    public RoleAssignmentResponse roles(List<String> roles) {

        this.roles = roles;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("roles")
    @Valid
    @NotNull(message = "Property roles cannot be null.")

    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public RoleAssignmentResponse addRolesItem(String rolesItem) {
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
        RoleAssignmentResponse roleAssignmentResponse = (RoleAssignmentResponse) o;
        return Objects.equals(this.applicationId, roleAssignmentResponse.applicationId) &&
            Objects.equals(this.applicationName, roleAssignmentResponse.applicationName) &&
            Objects.equals(this.roles, roleAssignmentResponse.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, applicationName, roles);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RoleAssignmentResponse {\n");
        
        sb.append("    applicationId: ").append(toIndentedString(applicationId)).append("\n");
        sb.append("    applicationName: ").append(toIndentedString(applicationName)).append("\n");
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

