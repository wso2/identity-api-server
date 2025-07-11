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

package org.wso2.carbon.identity.api.server.organization.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class GetOrganizationResponseAncestorPath  {
  
    private String id;
    private String name;
    private Integer depth;

    /**
    **/
    public GetOrganizationResponseAncestorPath id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public GetOrganizationResponseAncestorPath name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public GetOrganizationResponseAncestorPath depth(Integer depth) {

        this.depth = depth;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("depth")
    @Valid
    public Integer getDepth() {
        return depth;
    }
    public void setDepth(Integer depth) {
        this.depth = depth;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetOrganizationResponseAncestorPath getOrganizationResponseAncestorPath = (GetOrganizationResponseAncestorPath) o;
        return Objects.equals(this.id, getOrganizationResponseAncestorPath.id) &&
            Objects.equals(this.name, getOrganizationResponseAncestorPath.name) &&
            Objects.equals(this.depth, getOrganizationResponseAncestorPath.depth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, depth);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class GetOrganizationResponseAncestorPath {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    depth: ").append(toIndentedString(depth)).append("\n");
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

