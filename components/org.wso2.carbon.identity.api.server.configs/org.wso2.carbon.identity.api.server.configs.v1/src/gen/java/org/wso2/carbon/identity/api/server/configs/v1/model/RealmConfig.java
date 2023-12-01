/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RealmConfig  {
  
    private String adminUser;
    private String adminRole;
    private String everyoneRole;

    /**
    * Username of the super admin user in the system.
    **/
    public RealmConfig adminUser(String adminUser) {

        this.adminUser = adminUser;
        return this;
    }
    
    @ApiModelProperty(example = "admin", value = "Username of the super admin user in the system.")
    @JsonProperty("adminUser")
    @Valid
    public String getAdminUser() {
        return adminUser;
    }
    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    /**
    * Name of the super admin role in the system.
    **/
    public RealmConfig adminRole(String adminRole) {

        this.adminRole = adminRole;
        return this;
    }
    
    @ApiModelProperty(example = "Internal/admin", value = "Name of the super admin role in the system.")
    @JsonProperty("adminRole")
    @Valid
    public String getAdminRole() {
        return adminRole;
    }
    public void setAdminRole(String adminRole) {
        this.adminRole = adminRole;
    }

    /**
    * Name of the everyone role in the system.
    **/
    public RealmConfig everyoneRole(String everyoneRole) {

        this.everyoneRole = everyoneRole;
        return this;
    }
    
    @ApiModelProperty(example = "Internal/everyone", value = "Name of the everyone role in the system.")
    @JsonProperty("everyoneRole")
    @Valid
    public String getEveryoneRole() {
        return everyoneRole;
    }
    public void setEveryoneRole(String everyoneRole) {
        this.everyoneRole = everyoneRole;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RealmConfig realmConfig = (RealmConfig) o;
        return Objects.equals(this.adminUser, realmConfig.adminUser) &&
            Objects.equals(this.adminRole, realmConfig.adminRole) &&
            Objects.equals(this.everyoneRole, realmConfig.everyoneRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminUser, adminRole, everyoneRole);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RealmConfig {\n");
        
        sb.append("    adminUser: ").append(toIndentedString(adminUser)).append("\n");
        sb.append("    adminRole: ").append(toIndentedString(adminRole)).append("\n");
        sb.append("    everyoneRole: ").append(toIndentedString(everyoneRole)).append("\n");
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

