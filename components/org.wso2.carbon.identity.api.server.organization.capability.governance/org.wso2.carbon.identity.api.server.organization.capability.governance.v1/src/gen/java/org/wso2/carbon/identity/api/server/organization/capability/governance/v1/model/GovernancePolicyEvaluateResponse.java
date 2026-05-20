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

package org.wso2.carbon.identity.api.server.organization.capability.governance.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class GovernancePolicyEvaluateResponse  {
  
    private String resourceType;
    private String capability;
    private Boolean allowed;

    /**
    **/
    public GovernancePolicyEvaluateResponse resourceType(String resourceType) {

        this.resourceType = resourceType;
        return this;
    }
    
    @ApiModelProperty(example = "application", required = true, value = "")
    @JsonProperty("resourceType")
    @Valid
    @NotNull(message = "Property resourceType cannot be null.")

    public String getResourceType() {
        return resourceType;
    }
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
    **/
    public GovernancePolicyEvaluateResponse capability(String capability) {

        this.capability = capability;
        return this;
    }
    
    @ApiModelProperty(example = "adaptive-auth", required = true, value = "")
    @JsonProperty("capability")
    @Valid
    @NotNull(message = "Property capability cannot be null.")

    public String getCapability() {
        return capability;
    }
    public void setCapability(String capability) {
        this.capability = capability;
    }

    /**
    * Whether the capability is permitted for the calling organization.
    **/
    public GovernancePolicyEvaluateResponse allowed(Boolean allowed) {

        this.allowed = allowed;
        return this;
    }
    
    @ApiModelProperty(example = "true", required = true, value = "Whether the capability is permitted for the calling organization.")
    @JsonProperty("allowed")
    @Valid
    @NotNull(message = "Property allowed cannot be null.")

    public Boolean getAllowed() {
        return allowed;
    }
    public void setAllowed(Boolean allowed) {
        this.allowed = allowed;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GovernancePolicyEvaluateResponse governancePolicyEvaluateResponse = (GovernancePolicyEvaluateResponse) o;
        return Objects.equals(this.resourceType, governancePolicyEvaluateResponse.resourceType) &&
            Objects.equals(this.capability, governancePolicyEvaluateResponse.capability) &&
            Objects.equals(this.allowed, governancePolicyEvaluateResponse.allowed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceType, capability, allowed);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class GovernancePolicyEvaluateResponse {\n");
        
        sb.append("    resourceType: ").append(toIndentedString(resourceType)).append("\n");
        sb.append("    capability: ").append(toIndentedString(capability)).append("\n");
        sb.append("    allowed: ").append(toIndentedString(allowed)).append("\n");
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

