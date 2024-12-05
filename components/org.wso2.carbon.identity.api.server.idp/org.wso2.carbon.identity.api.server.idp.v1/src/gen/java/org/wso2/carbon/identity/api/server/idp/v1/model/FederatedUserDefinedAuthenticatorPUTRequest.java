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

package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.idp.v1.model.Endpoint;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class FederatedUserDefinedAuthenticatorPUTRequest  {
  
    private String authenticatorId;
    private Boolean isEnabled = false;
    private Boolean isDefault = false;
    private Endpoint endpoint;

    /**
    **/
    public FederatedUserDefinedAuthenticatorPUTRequest authenticatorId(String authenticatorId) {

        this.authenticatorId = authenticatorId;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("authenticatorId")
    @Valid
    public String getAuthenticatorId() {
        return authenticatorId;
    }
    public void setAuthenticatorId(String authenticatorId) {
        this.authenticatorId = authenticatorId;
    }

    /**
    **/
    public FederatedUserDefinedAuthenticatorPUTRequest isEnabled(Boolean isEnabled) {

        this.isEnabled = isEnabled;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("isEnabled")
    @Valid
    public Boolean getIsEnabled() {
        return isEnabled;
    }
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
    **/
    public FederatedUserDefinedAuthenticatorPUTRequest isDefault(Boolean isDefault) {

        this.isDefault = isDefault;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "")
    @JsonProperty("isDefault")
    @Valid
    public Boolean getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    /**
    **/
    public FederatedUserDefinedAuthenticatorPUTRequest endpoint(Endpoint endpoint) {

        this.endpoint = endpoint;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("endpoint")
    @Valid
    public Endpoint getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FederatedUserDefinedAuthenticatorPUTRequest federatedUserDefinedAuthenticatorPUTRequest = (FederatedUserDefinedAuthenticatorPUTRequest) o;
        return Objects.equals(this.authenticatorId, federatedUserDefinedAuthenticatorPUTRequest.authenticatorId) &&
            Objects.equals(this.isEnabled, federatedUserDefinedAuthenticatorPUTRequest.isEnabled) &&
            Objects.equals(this.isDefault, federatedUserDefinedAuthenticatorPUTRequest.isDefault) &&
            Objects.equals(this.endpoint, federatedUserDefinedAuthenticatorPUTRequest.endpoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticatorId, isEnabled, isDefault, endpoint);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FederatedUserDefinedAuthenticatorPUTRequest {\n");
        
        sb.append("    authenticatorId: ").append(toIndentedString(authenticatorId)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    isDefault: ").append(toIndentedString(isDefault)).append("\n");
        sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
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

