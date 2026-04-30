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
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionCriteria;
import javax.validation.constraints.*;

/**
 * Request body for sharing connections with **all child organizations** controlled by the specified policy.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Request body for sharing connections with **all child organizations** controlled by the specified policy.")
public class ConnectionShareWithAllRequestBody  {
  
    private ConnectionCriteria connectionCriteria;

@XmlType(name="PolicyEnum")
@XmlEnum(String.class)
public enum PolicyEnum {

    @XmlEnumValue("ALL_EXISTING_AND_FUTURE_ORGS") ALL_EXISTING_AND_FUTURE_ORGS(String.valueOf("ALL_EXISTING_AND_FUTURE_ORGS"));


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
    **/
    public ConnectionShareWithAllRequestBody connectionCriteria(ConnectionCriteria connectionCriteria) {

        this.connectionCriteria = connectionCriteria;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("connectionCriteria")
    @Valid
    @NotNull(message = "Property connectionCriteria cannot be null.")

    public ConnectionCriteria getConnectionCriteria() {
        return connectionCriteria;
    }
    public void setConnectionCriteria(ConnectionCriteria connectionCriteria) {
        this.connectionCriteria = connectionCriteria;
    }

    /**
    * Global sharing policy.  Possible values: - &#x60;ALL_EXISTING_AND_FUTURE_ORGS&#x60; — Share with all sub-organizations that currently   exist **and** any sub-organizations created in the future. This is the recommended   policy for connections intended to be used across the entire organization hierarchy.
    **/
    public ConnectionShareWithAllRequestBody policy(PolicyEnum policy) {

        this.policy = policy;
        return this;
    }
    
    @ApiModelProperty(example = "ALL_EXISTING_AND_FUTURE_ORGS", required = true, value = "Global sharing policy.  Possible values: - `ALL_EXISTING_AND_FUTURE_ORGS` — Share with all sub-organizations that currently   exist **and** any sub-organizations created in the future. This is the recommended   policy for connections intended to be used across the entire organization hierarchy.")
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
        ConnectionShareWithAllRequestBody connectionShareWithAllRequestBody = (ConnectionShareWithAllRequestBody) o;
        return Objects.equals(this.connectionCriteria, connectionShareWithAllRequestBody.connectionCriteria) &&
            Objects.equals(this.policy, connectionShareWithAllRequestBody.policy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionCriteria, policy);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConnectionShareWithAllRequestBody {\n");
        
        sb.append("    connectionCriteria: ").append(toIndentedString(connectionCriteria)).append("\n");
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

