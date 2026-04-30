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
 * Represents the sharing policy under which a connection has been shared. Only returned when &#x60;attributes&#x3D;sharingMode&#x60; is explicitly requested in the GET call.  - When present at the **top level** of the response, the policy is always   &#x60;ALL_EXISTING_AND_FUTURE_ORGS&#x60;. - When present **per organization**, the policy is always   &#x60;SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN&#x60;. - Connections shared via &#x60;SELECTED_ORG_ONLY&#x60; will not have a &#x60;sharingMode&#x60;   returned at either level.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Represents the sharing policy under which a connection has been shared. Only returned when `attributes=sharingMode` is explicitly requested in the GET call.  - When present at the **top level** of the response, the policy is always   `ALL_EXISTING_AND_FUTURE_ORGS`. - When present **per organization**, the policy is always   `SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN`. - Connections shared via `SELECTED_ORG_ONLY` will not have a `sharingMode`   returned at either level.")
public class ConnectionSharingMode  {
  

@XmlType(name="PolicyEnum")
@XmlEnum(String.class)
public enum PolicyEnum {

    @XmlEnumValue("ALL_EXISTING_AND_FUTURE_ORGS") ALL_EXISTING_AND_FUTURE_ORGS(String.valueOf("ALL_EXISTING_AND_FUTURE_ORGS")), @XmlEnumValue("SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN") SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN(String.valueOf("SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN"));


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
    * The sharing policy under which this connection was shared.  Possible values: - &#x60;ALL_EXISTING_AND_FUTURE_ORGS&#x60; - &#x60;SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN&#x60;
    **/
    public ConnectionSharingMode policy(PolicyEnum policy) {

        this.policy = policy;
        return this;
    }
    
    @ApiModelProperty(example = "ALL_EXISTING_AND_FUTURE_ORGS", required = true, value = "The sharing policy under which this connection was shared.  Possible values: - `ALL_EXISTING_AND_FUTURE_ORGS` - `SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN`")
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
        ConnectionSharingMode connectionSharingMode = (ConnectionSharingMode) o;
        return Objects.equals(this.policy, connectionSharingMode.policy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policy);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConnectionSharingMode {\n");
        
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

