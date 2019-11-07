/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticator;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class FederatedAuthenticatorRequest  {
  
    private String defaultAuthenticatorId;
    private List<FederatedAuthenticator> authenticators = null;


    /**
    * Id of the federated authenticator to be used as the default authenticator for the respective identity provider
    **/
    public FederatedAuthenticatorRequest defaultAuthenticatorId(String defaultAuthenticatorId) {

        this.defaultAuthenticatorId = defaultAuthenticatorId;
        return this;
    }
    
    @ApiModelProperty(example = "U0FNTDJBdXRoZW50aWNhdG9y", required = true, value = "Id of the federated authenticator to be used as the default authenticator for the respective identity provider")
    @JsonProperty("defaultAuthenticatorId")
    @Valid
    @NotNull(message = "Property defaultAuthenticatorId cannot be null.")

    public String getDefaultAuthenticatorId() {
        return defaultAuthenticatorId;
    }
    public void setDefaultAuthenticatorId(String defaultAuthenticatorId) {
        this.defaultAuthenticatorId = defaultAuthenticatorId;
    }

    /**
    * Includes the list of federated authenticators supported by the respective identity provider. This should include the authenticator specified as the defaultAuthenticator
    **/
    public FederatedAuthenticatorRequest authenticators(List<FederatedAuthenticator> authenticators) {

        this.authenticators = authenticators;
        return this;
    }
    
    @ApiModelProperty(value = "Includes the list of federated authenticators supported by the respective identity provider. This should include the authenticator specified as the defaultAuthenticator")
    @JsonProperty("authenticators")
    @Valid
    public List<FederatedAuthenticator> getAuthenticators() {
        return authenticators;
    }
    public void setAuthenticators(List<FederatedAuthenticator> authenticators) {
        this.authenticators = authenticators;
    }

    public FederatedAuthenticatorRequest addAuthenticatorsItem(FederatedAuthenticator authenticatorsItem) {
        if (this.authenticators == null) {
            this.authenticators = new ArrayList<>();
        }
        this.authenticators.add(authenticatorsItem);
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
        FederatedAuthenticatorRequest federatedAuthenticatorRequest = (FederatedAuthenticatorRequest) o;
        return Objects.equals(this.defaultAuthenticatorId, federatedAuthenticatorRequest.defaultAuthenticatorId) &&
            Objects.equals(this.authenticators, federatedAuthenticatorRequest.authenticators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultAuthenticatorId, authenticators);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FederatedAuthenticatorRequest {\n");
        
        sb.append("    defaultAuthenticatorId: ").append(toIndentedString(defaultAuthenticatorId)).append("\n");
        sb.append("    authenticators: ").append(toIndentedString(authenticators)).append("\n");
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

