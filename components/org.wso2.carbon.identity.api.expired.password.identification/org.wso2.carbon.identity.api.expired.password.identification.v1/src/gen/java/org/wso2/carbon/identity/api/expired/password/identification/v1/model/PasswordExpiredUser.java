/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.expired.password.identification.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

import java.util.Objects;
import javax.validation.Valid;

public class PasswordExpiredUser  {
  
    private String userId;
    private String username;
    private String userStoreDomain;

    /**
    **/
    public PasswordExpiredUser userId(String userId) {

        this.userId = userId;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("userId")
    @Valid
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
    **/
    public PasswordExpiredUser username(String username) {

        this.username = username;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("username")
    @Valid
    @NotNull(message = "Property username cannot be null.")

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    /**
    **/
    public PasswordExpiredUser userStoreDomain(String userStoreDomain) {

        this.userStoreDomain = userStoreDomain;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("userStoreDomain")
    @Valid
    public String getUserStoreDomain() {
        return userStoreDomain;
    }
    public void setUserStoreDomain(String userStoreDomain) {
        this.userStoreDomain = userStoreDomain;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PasswordExpiredUser passwordExpiredUser = (PasswordExpiredUser) o;
        return Objects.equals(this.userId, passwordExpiredUser.userId) &&
            Objects.equals(this.username, passwordExpiredUser.username) &&
            Objects.equals(this.userStoreDomain, passwordExpiredUser.userStoreDomain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, userStoreDomain);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PasswordExpiredUser {\n");
        
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    userStoreDomain: ").append(toIndentedString(userStoreDomain)).append("\n");
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

