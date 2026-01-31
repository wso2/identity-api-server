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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserCriteria;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserOrgShareConfig;
import javax.validation.constraints.*;

/**
 * Request body for sharing users with a **selected set of organizations**.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Request body for sharing users with a **selected set of organizations**.")
public class UserShareSelectedRequestBody  {
  
    private UserCriteria userCriteria;
    private List<UserOrgShareConfig> organizations = new ArrayList<>();


    /**
    **/
    public UserShareSelectedRequestBody userCriteria(UserCriteria userCriteria) {

        this.userCriteria = userCriteria;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("userCriteria")
    @Valid
    @NotNull(message = "Property userCriteria cannot be null.")

    public UserCriteria getUserCriteria() {
        return userCriteria;
    }
    public void setUserCriteria(UserCriteria userCriteria) {
        this.userCriteria = userCriteria;
    }

    /**
    * List of organizations to share users with, along with policies and optional role assignments.
    **/
    public UserShareSelectedRequestBody organizations(List<UserOrgShareConfig> organizations) {

        this.organizations = organizations;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "List of organizations to share users with, along with policies and optional role assignments.")
    @JsonProperty("organizations")
    @Valid
    @NotNull(message = "Property organizations cannot be null.")

    public List<UserOrgShareConfig> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(List<UserOrgShareConfig> organizations) {
        this.organizations = organizations;
    }

    public UserShareSelectedRequestBody addOrganizationsItem(UserOrgShareConfig organizationsItem) {
        this.organizations.add(organizationsItem);
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
        UserShareSelectedRequestBody userShareSelectedRequestBody = (UserShareSelectedRequestBody) o;
        return Objects.equals(this.userCriteria, userShareSelectedRequestBody.userCriteria) &&
            Objects.equals(this.organizations, userShareSelectedRequestBody.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userCriteria, organizations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserShareSelectedRequestBody {\n");
        
        sb.append("    userCriteria: ").append(toIndentedString(userCriteria)).append("\n");
        sb.append("    organizations: ").append(toIndentedString(organizations)).append("\n");
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

