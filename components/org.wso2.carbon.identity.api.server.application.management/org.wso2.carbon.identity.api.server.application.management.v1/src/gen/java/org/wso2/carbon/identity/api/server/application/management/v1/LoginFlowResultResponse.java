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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.application.management.v1.StatusEnum;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class LoginFlowResultResponse  {
  
    private StatusEnum status;
    private Object data;

    /**
    **/
    public LoginFlowResultResponse status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("status")
    @Valid
    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
    * The payload of the response, which varies based on the operation status. - For IN_PROGRESS status, an empty JSON object is returned. - For COMPLETED status, the authentication sequence is returned. - For FAILED status, an error message is returned. 
    **/
    public LoginFlowResultResponse data(Object data) {

        this.data = data;
        return this;
    }
    
    @ApiModelProperty(value = "The payload of the response, which varies based on the operation status. - For IN_PROGRESS status, an empty JSON object is returned. - For COMPLETED status, the authentication sequence is returned. - For FAILED status, an error message is returned. ")
    @JsonProperty("data")
    @Valid
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginFlowResultResponse loginFlowResultResponse = (LoginFlowResultResponse) o;
        return Objects.equals(this.status, loginFlowResultResponse.status) &&
            Objects.equals(this.data, loginFlowResultResponse.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, data);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class LoginFlowResultResponse {\n");
        
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

