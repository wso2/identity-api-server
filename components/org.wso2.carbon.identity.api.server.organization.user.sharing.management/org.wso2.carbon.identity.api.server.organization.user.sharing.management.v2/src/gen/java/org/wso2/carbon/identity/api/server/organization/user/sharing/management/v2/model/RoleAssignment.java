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
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.RoleShareConfig;
import javax.validation.constraints.*;

/**
 * Role assignment configuration for shared users.  This controls which roles are assigned to the user in the target organization.  - &#x60;NONE&#x60;     → Do not assign any roles via user sharing. - &#x60;SELECTED&#x60; → Assign only the roles listed under &#x60;roles&#x60;.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Role assignment configuration for shared users.  This controls which roles are assigned to the user in the target organization.  - `NONE`     → Do not assign any roles via user sharing. - `SELECTED` → Assign only the roles listed under `roles`.")
public class RoleAssignment  {
  

@XmlType(name="ModeEnum")
@XmlEnum(String.class)
public enum ModeEnum {

    @XmlEnumValue("NONE") NONE(String.valueOf("NONE")), @XmlEnumValue("SELECTED") SELECTED(String.valueOf("SELECTED"));


    private String value;

    ModeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ModeEnum fromValue(String value) {
        for (ModeEnum b : ModeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private ModeEnum mode = ModeEnum.SELECTED;
    private List<RoleShareConfig> roles = null;


    /**
    * Mode of role assignment.
    **/
    public RoleAssignment mode(ModeEnum mode) {

        this.mode = mode;
        return this;
    }
    
    @ApiModelProperty(value = "Mode of role assignment.")
    @JsonProperty("mode")
    @Valid
    public ModeEnum getMode() {
        return mode;
    }
    public void setMode(ModeEnum mode) {
        this.mode = mode;
    }

    /**
    * List of roles to assign when &#x60;mode &#x3D; SELECTED&#x60;. Required when &#x60;mode &#x3D; SELECTED&#x60;. Ignored when &#x60;mode &#x3D; NONE&#x60;.
    **/
    public RoleAssignment roles(List<RoleShareConfig> roles) {

        this.roles = roles;
        return this;
    }
    
    @ApiModelProperty(value = "List of roles to assign when `mode = SELECTED`. Required when `mode = SELECTED`. Ignored when `mode = NONE`.")
    @JsonProperty("roles")
    @Valid
    public List<RoleShareConfig> getRoles() {
        return roles;
    }
    public void setRoles(List<RoleShareConfig> roles) {
        this.roles = roles;
    }

    public RoleAssignment addRolesItem(RoleShareConfig rolesItem) {
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
        RoleAssignment roleAssignment = (RoleAssignment) o;
        return Objects.equals(this.mode, roleAssignment.mode) &&
            Objects.equals(this.roles, roleAssignment.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, roles);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RoleAssignment {\n");
        
        sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
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

