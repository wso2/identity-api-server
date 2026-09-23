/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.webhook.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * A child organization receiving events for a channel. Requests supply &#x60;orgId&#x60; only; responses add the resolved name and parent. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;
@ApiModel(description = "A child organization receiving events for a channel. Requests supply `orgId` only; responses add the resolved name and parent. ")
public class Organization  {
  
    private String orgId;
    private String orgName;
    private String parentOrgId;

    /**
    * Organization ID. Only this organization is subscribed — its own child organizations are not included, and children created later are not added. 
    **/
    public Organization orgId(String orgId) {

        this.orgId = orgId;
        return this;
    }
    
    @ApiModelProperty(example = "b028ca17-8f89-449c-ae27-fa955e66465d", required = true, value = "Organization ID. Only this organization is subscribed — its own child organizations are not included, and children created later are not added. ")
    @JsonProperty("orgId")
    @Valid
    @NotNull(message = "Property orgId cannot be null.")

    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
    * Organization name.
    **/
    public Organization orgName(String orgName) {

        this.orgName = orgName;
        return this;
    }
    
    @ApiModelProperty(example = "sub003", value = "Organization name.")
    @JsonProperty("orgName")
    @Valid
    public String getOrgName() {
        return orgName;
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
    * Immediate parent organization, for reconstructing the hierarchy. **May reference an organization that is not present in this response** — because it is not itself subscribed, or because it falls on another page. Results are ordered parents-before-children, so a client building the tree in order can treat an unresolved parent as the root of a rendered subtree. 
    **/
    public Organization parentOrgId(String parentOrgId) {

        this.parentOrgId = parentOrgId;
        return this;
    }
    
    @ApiModelProperty(example = "7c1f9a83-5b2e-41d7-9f30-2ac4e8b16d55", value = "Immediate parent organization, for reconstructing the hierarchy. **May reference an organization that is not present in this response** — because it is not itself subscribed, or because it falls on another page. Results are ordered parents-before-children, so a client building the tree in order can treat an unresolved parent as the root of a rendered subtree. ")
    @JsonProperty("parentOrgId")
    @Valid
    public String getParentOrgId() {
        return parentOrgId;
    }
    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Organization organization = (Organization) o;
        return Objects.equals(this.orgId, organization.orgId) &&
            Objects.equals(this.orgName, organization.orgName) &&
            Objects.equals(this.parentOrgId, organization.parentOrgId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, orgName, parentOrgId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Organization {\n");
        
        sb.append("    orgId: ").append(toIndentedString(orgId)).append("\n");
        sb.append("    orgName: ").append(toIndentedString(orgName)).append("\n");
        sb.append("    parentOrgId: ").append(toIndentedString(parentOrgId)).append("\n");
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

