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

package org.wso2.carbon.identity.api.server.authenticators.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Update the system authenticator AMR Value
 **/

import java.util.Objects;
import javax.validation.Valid;

@ApiModel(description = "Update the system authenticator AMR Value.")
public class SystemLocalAuthenticatorUpdate  {

    private String amrValue;

    /**
    **/
    public SystemLocalAuthenticatorUpdate amrValue(String amrValue) {

        this.amrValue = amrValue;
        return this;
    }
    
    @ApiModelProperty(example = "basic", required = true, value = "")
    @JsonProperty("amrValue")
    @Valid
    @NotNull(message = "Property amrValue cannot be null.")

    public String getAmrValue() {
        return amrValue;
    }
    public void setAmrValue(String amrValue) {
        this.amrValue = amrValue;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemLocalAuthenticatorUpdate systemLocalAuthenticatorUpdate = (SystemLocalAuthenticatorUpdate) o;
        return Objects.equals(this.amrValue, systemLocalAuthenticatorUpdate.amrValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amrValue);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SystemLocalAuthenticatorUpdate {\n");
        
        sb.append("    amrValue: ").append(toIndentedString(amrValue)).append("\n");
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

