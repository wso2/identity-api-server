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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v1.model.UserUnshareRequestBodyUserCriteria;
import javax.validation.constraints.*;

/**
 * The request body for unsharing users from all organizations. Includes a list of user IDs. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "The request body for unsharing users from all organizations. Includes a list of user IDs. ")
public class UserUnshareWithAllRequestBody  {
  
    private UserUnshareRequestBodyUserCriteria userCriteria;

    /**
    **/
    public UserUnshareWithAllRequestBody userCriteria(UserUnshareRequestBodyUserCriteria userCriteria) {

        this.userCriteria = userCriteria;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("userCriteria")
    @Valid
    public UserUnshareRequestBodyUserCriteria getUserCriteria() {
        return userCriteria;
    }
    public void setUserCriteria(UserUnshareRequestBodyUserCriteria userCriteria) {
        this.userCriteria = userCriteria;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserUnshareWithAllRequestBody userUnshareWithAllRequestBody = (UserUnshareWithAllRequestBody) o;
        return Objects.equals(this.userCriteria, userUnshareWithAllRequestBody.userCriteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userCriteria);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserUnshareWithAllRequestBody {\n");
        
        sb.append("    userCriteria: ").append(toIndentedString(userCriteria)).append("\n");
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

