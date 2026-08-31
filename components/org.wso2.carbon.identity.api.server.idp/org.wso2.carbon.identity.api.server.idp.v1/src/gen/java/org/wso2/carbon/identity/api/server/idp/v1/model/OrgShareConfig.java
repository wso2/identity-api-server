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

package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class OrgShareConfig  {
  
    private String orgId;
    private String policy;

    /**
    **/
    public OrgShareConfig orgId(String orgId) {

        this.orgId = orgId;
        return this;
    }
    
    @ApiModelProperty(example = "682edf68-4835-4bb8-961f-0a16bc6cc866", required = true, value = "")
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
    * Sharing scope for this organization.  Possible values: - &#x60;SELECTED_ORG_ONLY&#x60; - &#x60;SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN&#x60;
    **/
    public OrgShareConfig policy(String policy) {

        this.policy = policy;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Sharing scope for this organization.  Possible values: - `SELECTED_ORG_ONLY` - `SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN`")
    @JsonProperty("policy")
    @Valid
    @NotNull(message = "Property policy cannot be null.")

    public String getPolicy() {
        return policy;
    }
    public void setPolicy(String policy) {
        this.policy = policy;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrgShareConfig orgShareConfig = (OrgShareConfig) o;
        return Objects.equals(this.orgId, orgShareConfig.orgId) &&
            Objects.equals(this.policy, orgShareConfig.policy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, policy);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class OrgShareConfig {\n");
        
        sb.append("    orgId: ").append(toIndentedString(orgId)).append("\n");
        sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
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

