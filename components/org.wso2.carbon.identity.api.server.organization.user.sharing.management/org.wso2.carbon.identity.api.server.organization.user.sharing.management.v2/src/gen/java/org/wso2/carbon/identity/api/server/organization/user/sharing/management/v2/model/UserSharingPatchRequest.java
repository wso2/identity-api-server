/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserCriteria;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.model.UserSharingPatchOperation;
import javax.validation.constraints.*;

/**
 * PATCH request for performing incremental role assignment operations on already shared users.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "PATCH request for performing incremental role assignment operations on already shared users.")
public class UserSharingPatchRequest  {
  
    private UserCriteria userCriteria;
    private List<UserSharingPatchOperation> operations = new ArrayList<>();


    /**
    **/
    public UserSharingPatchRequest userCriteria(UserCriteria userCriteria) {

        this.userCriteria = userCriteria;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("userCriteria")
    @Valid
    @NotNull(message = "Property userCriteria cannot be null.")

    public UserCriteria getUserCriteria() {
        return userCriteria;
    }
    public void setUserCriteria(UserCriteria userCriteria) {
        this.userCriteria = userCriteria;
    }

    /**
    **/
    public UserSharingPatchRequest operations(List<UserSharingPatchOperation> operations) {

        this.operations = operations;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("Operations")
    @Valid
    @NotNull(message = "Property operations cannot be null.")

    public List<UserSharingPatchOperation> getOperations() {
        return operations;
    }
    public void setOperations(List<UserSharingPatchOperation> operations) {
        this.operations = operations;
    }

    public UserSharingPatchRequest addOperationsItem(UserSharingPatchOperation operationsItem) {
        this.operations.add(operationsItem);
        return this;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserSharingPatchRequest userSharingPatchRequest = (UserSharingPatchRequest) o;
        return Objects.equals(this.userCriteria, userSharingPatchRequest.userCriteria) &&
            Objects.equals(this.operations, userSharingPatchRequest.operations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userCriteria, operations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserSharingPatchRequest {\n");
        
        sb.append("    userCriteria: ").append(toIndentedString(userCriteria)).append("\n");
        sb.append("    operations: ").append(toIndentedString(operations)).append("\n");
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

