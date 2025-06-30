/*
 * Copyright (c) 2024-2025, WSO2 LLC. (http://www.wso2.com).
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
import org.wso2.carbon.identity.api.server.application.management.v1.RoleShareConfig;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class BasicOrganizationResponse  {
  
    private String id;
    private String name;
    private String orgHandle;
    private String parentId;

@XmlType(name="StatusEnum")
@XmlEnum(String.class)
public enum StatusEnum {

    @XmlEnumValue("ACTIVE") ACTIVE(String.valueOf("ACTIVE")), @XmlEnumValue("DISABLED") DISABLED(String.valueOf("DISABLED"));


    private String value;

    StatusEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static StatusEnum fromValue(String value) {
        for (StatusEnum b : StatusEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private StatusEnum status;
    private String ref;
    private Boolean hasChildren;
    private Integer depthFromRoot;
    private List<RoleShareConfig> roles = new ArrayList<>();


    /**
    **/
    public BasicOrganizationResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "b4526d91-a8bf-43d2-8b14-c548cf73065b", required = true, value = "")
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
    public BasicOrganizationResponse name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "ABC Builders", required = true, value = "")
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
    public BasicOrganizationResponse orgHandle(String orgHandle) {

        this.orgHandle = orgHandle;
        return this;
    }
    
    @ApiModelProperty(example = "abcbuilders", required = true, value = "")
    @JsonProperty("orgHandle")
    @Valid
    @NotNull(message = "Property orgHandle cannot be null.")

    public String getOrgHandle() {
        return orgHandle;
    }
    public void setOrgHandle(String orgHandle) {
        this.orgHandle = orgHandle;
    }

    /**
    * The parent organization ID. 
    **/
    public BasicOrganizationResponse parentId(String parentId) {

        this.parentId = parentId;
        return this;
    }
    
    @ApiModelProperty(example = "08f8c1d2-4b3e-4c5a-9f6b-7d8e9f0a1b2c", required = true, value = "The parent organization ID. ")
    @JsonProperty("parentId")
    @Valid
    @NotNull(message = "Property parentId cannot be null.")

    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
    **/
    public BasicOrganizationResponse status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "ACTIVE", required = true, value = "")
    @JsonProperty("status")
    @Valid
    @NotNull(message = "Property status cannot be null.")

    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
    **/
    public BasicOrganizationResponse ref(String ref) {

        this.ref = ref;
        return this;
    }
    
    @ApiModelProperty(example = "/t/wso2.com/api/server/v1/organizations/b4526d91-a8bf-43d2-8b14-c548cf73065b", required = true, value = "")
    @JsonProperty("ref")
    @Valid
    @NotNull(message = "Property ref cannot be null.")

    public String getRef() {
        return ref;
    }
    public void setRef(String ref) {
        this.ref = ref;
    }

    /**
    **/
    public BasicOrganizationResponse hasChildren(Boolean hasChildren) {

        this.hasChildren = hasChildren;
        return this;
    }
    
    @ApiModelProperty(example = "true", required = true, value = "")
    @JsonProperty("hasChildren")
    @Valid
    @NotNull(message = "Property hasChildren cannot be null.")

    public Boolean getHasChildren() {
        return hasChildren;
    }
    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    /**
    **/
    public BasicOrganizationResponse depthFromRoot(Integer depthFromRoot) {

        this.depthFromRoot = depthFromRoot;
        return this;
    }
    
    @ApiModelProperty(example = "1", required = true, value = "")
    @JsonProperty("depthFromRoot")
    @Valid
    @NotNull(message = "Property depthFromRoot cannot be null.")

    public Integer getDepthFromRoot() {
        return depthFromRoot;
    }
    public void setDepthFromRoot(Integer depthFromRoot) {
        this.depthFromRoot = depthFromRoot;
    }

    /**
    * List of roles that are shared with the application in this organization.
    **/
    public BasicOrganizationResponse roles(List<RoleShareConfig> roles) {

        this.roles = roles;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "List of roles that are shared with the application in this organization.")
    @JsonProperty("roles")
    @Valid
    @NotNull(message = "Property roles cannot be null.")

    public List<RoleShareConfig> getRoles() {
        return roles;
    }
    public void setRoles(List<RoleShareConfig> roles) {
        this.roles = roles;
    }

    public BasicOrganizationResponse addRolesItem(RoleShareConfig rolesItem) {
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
        BasicOrganizationResponse basicOrganizationResponse = (BasicOrganizationResponse) o;
        return Objects.equals(this.id, basicOrganizationResponse.id) &&
            Objects.equals(this.name, basicOrganizationResponse.name) &&
            Objects.equals(this.orgHandle, basicOrganizationResponse.orgHandle) &&
            Objects.equals(this.parentId, basicOrganizationResponse.parentId) &&
            Objects.equals(this.status, basicOrganizationResponse.status) &&
            Objects.equals(this.ref, basicOrganizationResponse.ref) &&
            Objects.equals(this.hasChildren, basicOrganizationResponse.hasChildren) &&
            Objects.equals(this.depthFromRoot, basicOrganizationResponse.depthFromRoot) &&
            Objects.equals(this.roles, basicOrganizationResponse.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, orgHandle, parentId, status, ref, hasChildren, depthFromRoot, roles);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class BasicOrganizationResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    orgHandle: ").append(toIndentedString(orgHandle)).append("\n");
        sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    ref: ").append(toIndentedString(ref)).append("\n");
        sb.append("    hasChildren: ").append(toIndentedString(hasChildren)).append("\n");
        sb.append("    depthFromRoot: ").append(toIndentedString(depthFromRoot)).append("\n");
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

