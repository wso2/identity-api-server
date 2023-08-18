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
import org.wso2.carbon.identity.api.server.application.management.v1.Permission;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RolePatchModel  {
  
    private String name;
    private List<Permission> addedPermissions = new ArrayList<>();

    private List<Permission> removedPermissions = new ArrayList<>();


    /**
    **/
    public RolePatchModel name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "uuid", required = true, value = "")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public RolePatchModel addedPermissions(List<Permission> addedPermissions) {

        this.addedPermissions = addedPermissions;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("addedPermissions")
    @Valid
    @NotNull(message = "Property addedPermissions cannot be null.")

    public List<Permission> getAddedPermissions() {
        return addedPermissions;
    }
    public void setAddedPermissions(List<Permission> addedPermissions) {
        this.addedPermissions = addedPermissions;
    }

    public RolePatchModel addAddedPermissionsItem(Permission addedPermissionsItem) {
        this.addedPermissions.add(addedPermissionsItem);
        return this;
    }

        /**
    **/
    public RolePatchModel removedPermissions(List<Permission> removedPermissions) {

        this.removedPermissions = removedPermissions;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("removedPermissions")
    @Valid
    @NotNull(message = "Property removedPermissions cannot be null.")

    public List<Permission> getRemovedPermissions() {
        return removedPermissions;
    }
    public void setRemovedPermissions(List<Permission> removedPermissions) {
        this.removedPermissions = removedPermissions;
    }

    public RolePatchModel addRemovedPermissionsItem(Permission removedPermissionsItem) {
        this.removedPermissions.add(removedPermissionsItem);
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
        RolePatchModel rolePatchModel = (RolePatchModel) o;
        return Objects.equals(this.name, rolePatchModel.name) &&
            Objects.equals(this.addedPermissions, rolePatchModel.addedPermissions) &&
            Objects.equals(this.removedPermissions, rolePatchModel.removedPermissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, addedPermissions, removedPermissions);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RolePatchModel {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    addedPermissions: ").append(toIndentedString(addedPermissions)).append("\n");
        sb.append("    removedPermissions: ").append(toIndentedString(removedPermissions)).append("\n");
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

