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
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResourceResponse;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PolicyResponse  {
  
    private String id;
    private String name;
    private List<PolicyResourceResponse> resources = null;


    /**
    * The policy resource ID.
    **/
    public PolicyResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "74070bae-df8c-42bf-8754-5173c237c936", required = true, value = "The policy resource ID.")
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
    * The name of the policy.
    **/
    public PolicyResponse name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Corporate Device Policy", required = true, value = "The name of the policy.")
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
    * Resources associated with the policy.
    **/
    public PolicyResponse resources(List<PolicyResourceResponse> resources) {

        this.resources = resources;
        return this;
    }
    
    @ApiModelProperty(value = "Resources associated with the policy.")
    @JsonProperty("resources")
    @Valid
    public List<PolicyResourceResponse> getResources() {
        return resources;
    }
    public void setResources(List<PolicyResourceResponse> resources) {
        this.resources = resources;
    }

    public PolicyResponse addResourcesItem(PolicyResourceResponse resourcesItem) {
        if (this.resources == null) {
            this.resources = new ArrayList<>();
        }
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
        PolicyResponse policyResponse = (PolicyResponse) o;
        return Objects.equals(this.id, policyResponse.id) &&
            Objects.equals(this.name, policyResponse.name) &&
            Objects.equals(this.resources, policyResponse.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, resources);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PolicyResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

