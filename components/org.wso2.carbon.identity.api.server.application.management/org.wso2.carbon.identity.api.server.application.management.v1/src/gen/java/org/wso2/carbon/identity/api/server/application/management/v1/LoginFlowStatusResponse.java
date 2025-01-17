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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class LoginFlowStatusResponse  {
  
    private String operationId;
    private Object status;

    /**
    **/
    public LoginFlowStatusResponse operationId(String operationId) {

        this.operationId = operationId;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("operationId")
    @Valid
    public String getOperationId() {
        return operationId;
    }
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
    * A generic object representing the status
    **/
    public LoginFlowStatusResponse status(Object status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(value = "A generic object representing the status")
    @JsonProperty("status")
    @Valid
    public Object getStatus() {
        return status;
    }
    public void setStatus(Object status) {
        this.status = status;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginFlowStatusResponse loginFlowStatusResponse = (LoginFlowStatusResponse) o;
        return Objects.equals(this.operationId, loginFlowStatusResponse.operationId) &&
            Objects.equals(this.status, loginFlowStatusResponse.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId, status);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class LoginFlowStatusResponse {\n");
        
        sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

