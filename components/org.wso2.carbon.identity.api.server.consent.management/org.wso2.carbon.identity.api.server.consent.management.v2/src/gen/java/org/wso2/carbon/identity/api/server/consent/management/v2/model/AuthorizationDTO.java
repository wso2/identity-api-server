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

public class AuthorizationDTO  {
  
    private String userId;

@XmlType(name="StateEnum")
@XmlEnum(String.class)
public enum StateEnum {

    @XmlEnumValue("APPROVED") APPROVED(String.valueOf("APPROVED")), @XmlEnumValue("REJECTED") REJECTED(String.valueOf("REJECTED")), @XmlEnumValue("REVOKED") REVOKED(String.valueOf("REVOKED")), @XmlEnumValue("EXPIRED") EXPIRED(String.valueOf("EXPIRED"));


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
    private Long updatedTime;

    /**
    * Username of the user who performed this authorization
    **/
    public AuthorizationDTO userId(String userId) {

        this.userId = userId;
        return this;
    }
    
    @ApiModelProperty(example = "alice@wso2.com", value = "Username of the user who performed this authorization")
    @JsonProperty("userId")
    @Valid
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
    * Current state of this authorization
    **/
    public AuthorizationDTO state(StateEnum state) {

        this.state = state;
        return this;
    }
    
    @ApiModelProperty(example = "APPROVED", value = "Current state of this authorization")
    @JsonProperty("state")
    @Valid
    public StateEnum getState() {
        return state;
    }
    public void setState(StateEnum state) {
        this.state = state;
    }

    /**
    * Milliseconds since epoch when this authorization was last updated
    **/
    public AuthorizationDTO updatedTime(Long updatedTime) {

        this.updatedTime = updatedTime;
        return this;
    }
    
    @ApiModelProperty(example = "1702800000000", value = "Milliseconds since epoch when this authorization was last updated")
    @JsonProperty("updatedTime")
    @Valid
    public Long getUpdatedTime() {
        return updatedTime;
    }
    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorizationDTO authorizationDTO = (AuthorizationDTO) o;
        return Objects.equals(this.userId, authorizationDTO.userId) &&
            Objects.equals(this.state, authorizationDTO.state) &&
            Objects.equals(this.updatedTime, authorizationDTO.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, state, updatedTime);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthorizationDTO {\n");
        
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
        sb.append("    updatedTime: ").append(toIndentedString(updatedTime)).append("\n");
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

