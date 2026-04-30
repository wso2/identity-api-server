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

package org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Per-organization sharing configuration for selected organizations.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Per-organization sharing configuration for selected organizations.")
public class ConnectionOrgShareConfig  {
  
    private String orgId;

@XmlType(name="PolicyEnum")
@XmlEnum(String.class)
public enum PolicyEnum {

    @XmlEnumValue("SELECTED_ORG_ONLY") SELECTED_ORG_ONLY(String.valueOf("SELECTED_ORG_ONLY")), @XmlEnumValue("SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN") SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN(String.valueOf("SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN"));


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

    /**
    * The ID of the organization to share the connection with.
    **/
    public ConnectionOrgShareConfig orgId(String orgId) {

        this.orgId = orgId;
        return this;
    }
    
    @ApiModelProperty(example = "b028ca17-8f89-449c-ae27-fa955e66465d", required = true, value = "The ID of the organization to share the connection with.")
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
    * Sharing scope for this organization.  Possible values: - &#x60;SELECTED_ORG_ONLY&#x60; — Share with only this specific organization. Sub-organizations   created under it will not automatically receive the shared connection. - &#x60;SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN&#x60; — Share with this organization   and all of its child organizations, including those created in the future.
    **/
    public ConnectionOrgShareConfig policy(PolicyEnum policy) {

        this.policy = policy;
        return this;
    }
    
    @ApiModelProperty(example = "SELECTED_ORG_ONLY", required = true, value = "Sharing scope for this organization.  Possible values: - `SELECTED_ORG_ONLY` — Share with only this specific organization. Sub-organizations   created under it will not automatically receive the shared connection. - `SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN` — Share with this organization   and all of its child organizations, including those created in the future.")
    @JsonProperty("policy")
    @Valid
    @NotNull(message = "Property policy cannot be null.")

    public PolicyEnum getPolicy() {
        return policy;
    }
    public void setPolicy(PolicyEnum policy) {
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
        ConnectionOrgShareConfig connectionOrgShareConfig = (ConnectionOrgShareConfig) o;
        return Objects.equals(this.orgId, connectionOrgShareConfig.orgId) &&
            Objects.equals(this.policy, connectionOrgShareConfig.policy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, policy);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConnectionOrgShareConfig {\n");
        
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

