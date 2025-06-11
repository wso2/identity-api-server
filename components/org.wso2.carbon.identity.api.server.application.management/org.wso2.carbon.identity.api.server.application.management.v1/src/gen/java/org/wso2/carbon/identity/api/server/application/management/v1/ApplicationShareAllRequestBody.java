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
import org.wso2.carbon.identity.api.server.application.management.v1.RoleSharing;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ApplicationShareAllRequestBody  {
  
    private String applicationId;

@XmlType(name="PolicyEnum")
@XmlEnum(String.class)
public enum PolicyEnum {

    @XmlEnumValue("ALL_EXISTING_ORGS_ONLY") ALL_EXISTING_ORGS_ONLY(String.valueOf("ALL_EXISTING_ORGS_ONLY")), @XmlEnumValue("ALL_EXISTING_AND_FUTURE_ORGS") ALL_EXISTING_AND_FUTURE_ORGS(String.valueOf("ALL_EXISTING_AND_FUTURE_ORGS")), @XmlEnumValue("NO_ORGS") NO_ORGS(String.valueOf("NO_ORGS"));


    private String value;

    PolicyEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static PolicyEnum fromValue(String value) {
        for (PolicyEnum b : PolicyEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private PolicyEnum policy;
    private RoleSharing roleSharing;

    /**
    **/
    public ApplicationShareAllRequestBody applicationId(String applicationId) {

        this.applicationId = applicationId;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("applicationId")
    @Valid
    @NotNull(message = "Property applicationId cannot be null.")

    public String getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
    * - ALL_EXISTING_ORGS_ONLY: Share with all organizations currently present. - ALL_EXISTING_AND_FUTURE_ORGS: Share with all current and any newly created organizations in future. 
    **/
    public ApplicationShareAllRequestBody policy(PolicyEnum policy) {

        this.policy = policy;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "- ALL_EXISTING_ORGS_ONLY: Share with all organizations currently present. - ALL_EXISTING_AND_FUTURE_ORGS: Share with all current and any newly created organizations in future. ")
    @JsonProperty("policy")
    @Valid
    @NotNull(message = "Property policy cannot be null.")

    public PolicyEnum getPolicy() {
        return policy;
    }
    public void setPolicy(PolicyEnum policy) {
        this.policy = policy;
    }

    /**
    **/
    public ApplicationShareAllRequestBody roleSharing(RoleSharing roleSharing) {

        this.roleSharing = roleSharing;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("roleSharing")
    @Valid
    @NotNull(message = "Property roleSharing cannot be null.")

    public RoleSharing getRoleSharing() {
        return roleSharing;
    }
    public void setRoleSharing(RoleSharing roleSharing) {
        this.roleSharing = roleSharing;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationShareAllRequestBody applicationShareAllRequestBody = (ApplicationShareAllRequestBody) o;
        return Objects.equals(this.applicationId, applicationShareAllRequestBody.applicationId) &&
            Objects.equals(this.policy, applicationShareAllRequestBody.policy) &&
            Objects.equals(this.roleSharing, applicationShareAllRequestBody.roleSharing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, policy, roleSharing);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ApplicationShareAllRequestBody {\n");
        
        sb.append("    applicationId: ").append(toIndentedString(applicationId)).append("\n");
        sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
        sb.append("    roleSharing: ").append(toIndentedString(roleSharing)).append("\n");
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

