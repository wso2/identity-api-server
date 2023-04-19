/*
* Copyright (c) 2021, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.authenticators.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class Error  {
  
    private String code;
    private String message;
    private String description;
    private String traceId;

    /**
    * An error code.
    **/
    public Error code(String code) {

        this.code = code;
        return this;
    }
    
    @ApiModelProperty(example = "AUT-00000", required = true, value = "An error code.")
    @JsonProperty("code")
    @Valid
    @NotNull(message = "Property code cannot be null.")

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    /**
    * An error message.
    **/
    public Error message(String message) {

        this.message = message;
        return this;
    }
    
    @ApiModelProperty(example = "Some Error Message", required = true, value = "An error message.")
    @JsonProperty("message")
    @Valid
    @NotNull(message = "Property message cannot be null.")

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    /**
    * An error description.
    **/
    public Error description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Some Error Description", value = "An error description.")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * An error trace identifier.
    **/
    public Error traceId(String traceId) {

        this.traceId = traceId;
        return this;
    }
    
    @ApiModelProperty(example = "e0fbcfeb-3617-43c4-8dd0-7b7d38e13047", value = "An error trace identifier.")
    @JsonProperty("traceId")
    @Valid
    public String getTraceId() {
        return traceId;
    }
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Error error = (Error) o;
        return Objects.equals(this.code, error.code) &&
            Objects.equals(this.message, error.message) &&
            Objects.equals(this.description, error.description) &&
            Objects.equals(this.traceId, error.traceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, description, traceId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Error {\n");
        
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    traceId: ").append(toIndentedString(traceId)).append("\n");
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

