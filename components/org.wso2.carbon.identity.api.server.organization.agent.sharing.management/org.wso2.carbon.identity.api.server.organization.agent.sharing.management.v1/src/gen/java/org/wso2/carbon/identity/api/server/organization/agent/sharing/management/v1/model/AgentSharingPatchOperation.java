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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.RoleShareConfig;
import javax.validation.constraints.*;

/**
 * A single PATCH operation (SCIM-like) for agent sharing.  Currently supported: - &#x60;op: \&quot;add\&quot;&#x60;    → Add roles. - &#x60;op: \&quot;remove\&quot;&#x60; → Remove roles.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "A single PATCH operation (SCIM-like) for agent sharing.  Currently supported: - `op: \"add\"`    → Add roles. - `op: \"remove\"` → Remove roles.")
public class AgentSharingPatchOperation  {
  
    private String op;
    private String path;
    private List<RoleShareConfig> value = null;


    /**
    * Operation type. Supported values: &#x60;add&#x60;, &#x60;remove&#x60;.
    **/
    public AgentSharingPatchOperation op(String op) {

        this.op = op;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Operation type. Supported values: `add`, `remove`.")
    @JsonProperty("op")
    @Valid
    @NotNull(message = "Property op cannot be null.")

    public String getOp() {
        return op;
    }
    public void setOp(String op) {
        this.op = op;
    }

    /**
    * JSON-like path in the format: - &#x60;organizations[orgId eq \\\&quot;&lt;org-id&gt;\\\&quot;].roles&#x60;
    **/
    public AgentSharingPatchOperation path(String path) {

        this.path = path;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "JSON-like path in the format: - `organizations[orgId eq \\\"<org-id>\\\"].roles`")
    @JsonProperty("path")
    @Valid
    @NotNull(message = "Property path cannot be null.")

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    /**
    * List of roles to add/remove under the specified path.
    **/
    public AgentSharingPatchOperation value(List<RoleShareConfig> value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(value = "List of roles to add/remove under the specified path.")
    @JsonProperty("value")
    @Valid
    public List<RoleShareConfig> getValue() {
        return value;
    }
    public void setValue(List<RoleShareConfig> value) {
        this.value = value;
    }

    public AgentSharingPatchOperation addValueItem(RoleShareConfig valueItem) {
        if (this.value == null) {
            this.value = new ArrayList<>();
        }
        this.value.add(valueItem);
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
        AgentSharingPatchOperation agentSharingPatchOperation = (AgentSharingPatchOperation) o;
        return Objects.equals(this.op, agentSharingPatchOperation.op) &&
            Objects.equals(this.path, agentSharingPatchOperation.path) &&
            Objects.equals(this.value, agentSharingPatchOperation.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(op, path, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentSharingPatchOperation {\n");
        
        sb.append("    op: ").append(toIndentedString(op)).append("\n");
        sb.append("    path: ").append(toIndentedString(path)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

