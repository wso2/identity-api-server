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

package org.wso2.carbon.identity.api.server.policy.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResourceRequest;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PolicyRequest  {
  
    private String name;
    private List<PolicyResourceRequest> resources = new ArrayList<>();


    /**
    * The name of the policy.
    **/
    public PolicyRequest name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Corporate Policy", required = true, value = "The name of the policy.")
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
    * Resources attached to the policy. Each target may have at most one resource of each type.
    **/
    public PolicyRequest resources(List<PolicyResourceRequest> resources) {

        this.resources = resources;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Resources attached to the policy. Each target may have at most one resource of each type.")
    @JsonProperty("resources")
    @Valid
    @NotNull(message = "Property resources cannot be null.")

    public List<PolicyResourceRequest> getResources() {
        return resources;
    }
    public void setResources(List<PolicyResourceRequest> resources) {
        this.resources = resources;
    }

    public PolicyRequest addResourcesItem(PolicyResourceRequest resourcesItem) {
        this.resources.add(resourcesItem);
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
        PolicyRequest policyRequest = (PolicyRequest) o;
        return Objects.equals(this.name, policyRequest.name) &&
            Objects.equals(this.resources, policyRequest.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, resources);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PolicyRequest {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    resources: ").append(toIndentedString(resources)).append("\n");
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

