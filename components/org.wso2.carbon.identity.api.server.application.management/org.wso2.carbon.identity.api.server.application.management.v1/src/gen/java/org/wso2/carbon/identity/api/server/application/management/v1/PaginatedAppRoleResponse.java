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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.PaginationLink;
import org.wso2.carbon.identity.api.server.application.management.v1.Role;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PaginatedAppRoleResponse  {
  
    private List<PaginationLink> links = new ArrayList<>();

    private List<Role> roles = null;


    /**
    **/
    public PaginatedAppRoleResponse links(List<PaginationLink> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("links")
    @Valid
    @NotNull(message = "Property links cannot be null.")

    public List<PaginationLink> getLinks() {
        return links;
    }
    public void setLinks(List<PaginationLink> links) {
        this.links = links;
    }

    public PaginatedAppRoleResponse addLinksItem(PaginationLink linksItem) {
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public PaginatedAppRoleResponse roles(List<Role> roles) {

        this.roles = roles;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("roles")
    @Valid
    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public PaginatedAppRoleResponse addRolesItem(Role rolesItem) {
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
        PaginatedAppRoleResponse paginatedAppRoleResponse = (PaginatedAppRoleResponse) o;
        return Objects.equals(this.links, paginatedAppRoleResponse.links) &&
            Objects.equals(this.roles, paginatedAppRoleResponse.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(links, roles);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PaginatedAppRoleResponse {\n");
        
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

