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

package org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Role assignments which the user will be assigned to.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Role assignments which the user will be assigned to.")
public class InvitationSuccessResponseResult  {
  
    private String status;
    private String errorCode;
    private String errorMessage;
    private String errorDescription;

    /**
    **/
    public InvitationSuccessResponseResult status(String status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "Successful/Failed", value = "")
    @JsonProperty("status")
    @Valid
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    /**
    **/
    public InvitationSuccessResponseResult errorCode(String errorCode) {

        this.errorCode = errorCode;
        return this;
    }
    
    @ApiModelProperty(example = "OUI-00000", value = "")
    @JsonProperty("errorCode")
    @Valid
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
    **/
    public InvitationSuccessResponseResult errorMessage(String errorMessage) {

        this.errorMessage = errorMessage;
        return this;
    }
    
    @ApiModelProperty(example = "Some Error Message", value = "")
    @JsonProperty("errorMessage")
    @Valid
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
    **/
    public InvitationSuccessResponseResult errorDescription(String errorDescription) {

        this.errorDescription = errorDescription;
        return this;
    }
    
    @ApiModelProperty(example = "Some Error Description", value = "")
    @JsonProperty("errorDescription")
    @Valid
    public String getErrorDescription() {
        return errorDescription;
    }
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvitationSuccessResponseResult invitationSuccessResponseResult = (InvitationSuccessResponseResult) o;
        return Objects.equals(this.status, invitationSuccessResponseResult.status) &&
            Objects.equals(this.errorCode, invitationSuccessResponseResult.errorCode) &&
            Objects.equals(this.errorMessage, invitationSuccessResponseResult.errorMessage) &&
            Objects.equals(this.errorDescription, invitationSuccessResponseResult.errorDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, errorCode, errorMessage, errorDescription);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InvitationSuccessResponseResult {\n");
        
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
        sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
        sb.append("    errorDescription: ").append(toIndentedString(errorDescription)).append("\n");
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

