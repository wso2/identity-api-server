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

package org.wso2.carbon.identity.api.server.application.management.v1;

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

public class ApplicationSharingPatchOperation  {
  
    private String op;
    private String path;
    private List<Object> value = null;


    /**
    * Patch operation type (like SCIM): Currently only &#39;add&#39; and &#39;remove&#39; are supported.
    **/
    public ApplicationSharingPatchOperation op(String op) {

        this.op = op;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Patch operation type (like SCIM): Currently only 'add' and 'remove' are supported.")
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
    * Path in the format &#39;organizations[orgId eq \&quot;abc123\&quot;].roles&#39;
    **/
    public ApplicationSharingPatchOperation path(String path) {

        this.path = path;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Path in the format 'organizations[orgId eq \"abc123\"].roles'")
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
    * List of roles to add/remove
    **/
    public ApplicationSharingPatchOperation value(List<Object> value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(value = "List of roles to add/remove")
    @JsonProperty("value")
    @Valid
    public List<Object> getValue() {
        return value;
    }
    public void setValue(List<Object> value) {
        this.value = value;
    }

    public ApplicationSharingPatchOperation addValueItem(Object valueItem) {
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
        ApplicationSharingPatchOperation applicationSharingPatchOperation = (ApplicationSharingPatchOperation) o;
        return Objects.equals(this.op, applicationSharingPatchOperation.op) &&
            Objects.equals(this.path, applicationSharingPatchOperation.path) &&
            Objects.equals(this.value, applicationSharingPatchOperation.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(op, path, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ApplicationSharingPatchOperation {\n");
        
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

