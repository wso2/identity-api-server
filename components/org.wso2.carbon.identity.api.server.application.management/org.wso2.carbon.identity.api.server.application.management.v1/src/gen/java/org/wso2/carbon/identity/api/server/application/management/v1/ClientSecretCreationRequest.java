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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Request payload for creating a new client secret.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Request payload for creating a new client secret.")
public class ClientSecretCreationRequest  {
  
    private Long expiresAt;

    /**
    * Expiry time as Unix epoch seconds; must be a future time. 0 or omitted for a non-expiring secret.
    **/
    public ClientSecretCreationRequest expiresAt(Long expiresAt) {

        this.expiresAt = expiresAt;
        return this;
    }
    
    @ApiModelProperty(example = "1761568483", value = "Expiry time as Unix epoch seconds; must be a future time. 0 or omitted for a non-expiring secret.")
    @JsonProperty("expiresAt")
    @Valid
    public Long getExpiresAt() {
        return expiresAt;
    }
    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientSecretCreationRequest clientSecretCreationRequest = (ClientSecretCreationRequest) o;
        return Objects.equals(this.expiresAt, clientSecretCreationRequest.expiresAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expiresAt);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ClientSecretCreationRequest {\n");
        
        sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

