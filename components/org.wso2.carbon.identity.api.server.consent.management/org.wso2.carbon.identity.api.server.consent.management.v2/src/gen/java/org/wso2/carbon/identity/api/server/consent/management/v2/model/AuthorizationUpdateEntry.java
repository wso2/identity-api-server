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

package org.wso2.carbon.identity.api.server.consent.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AuthorizationUpdateEntry  {
  
    private String userId;
    private String type;

@XmlType(name="StateEnum")
@XmlEnum(String.class)
public enum StateEnum {

    @XmlEnumValue("APPROVED") APPROVED(String.valueOf("APPROVED")), @XmlEnumValue("REJECTED") REJECTED(String.valueOf("REJECTED")), @XmlEnumValue("REVOKED") REVOKED(String.valueOf("REVOKED"));


    private String value;

    StateEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static StateEnum fromValue(String value) {
        for (StateEnum b : StateEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private StateEnum state;

    /**
    * Username of the authorizer to add or update
    **/
    public AuthorizationUpdateEntry userId(String userId) {

        this.userId = userId;
        return this;
    }
    
    @ApiModelProperty(example = "alice@wso2.com", required = true, value = "Username of the authorizer to add or update")
    @JsonProperty("userId")
    @Valid
    @NotNull(message = "Property userId cannot be null.")

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
    * Type of the authorization principal. Used when adding a new authorizer.
    **/
    public AuthorizationUpdateEntry type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "USER", value = "Type of the authorization principal. Used when adding a new authorizer.")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    * Authorization state to set. Required when overriding an existing authorizer; for a newly added authorizer, omit to leave it pending. 
    **/
    public AuthorizationUpdateEntry state(StateEnum state) {

        this.state = state;
        return this;
    }
    
    @ApiModelProperty(example = "REVOKED", value = "Authorization state to set. Required when overriding an existing authorizer; for a newly added authorizer, omit to leave it pending. ")
    @JsonProperty("state")
    @Valid
    public StateEnum getState() {
        return state;
    }
    public void setState(StateEnum state) {
        this.state = state;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorizationUpdateEntry authorizationUpdateEntry = (AuthorizationUpdateEntry) o;
        return Objects.equals(this.userId, authorizationUpdateEntry.userId) &&
            Objects.equals(this.type, authorizationUpdateEntry.type) &&
            Objects.equals(this.state, authorizationUpdateEntry.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, type, state);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthorizationUpdateEntry {\n");
        
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
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

