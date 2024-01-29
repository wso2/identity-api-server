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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.Valid;

public class DCRConfig  {
  
    private Boolean clientAuthenticationRequired;
    private String ssaJwks;
    private Boolean enableFapiEnforcement;

    /**
    * If false, the client authentication is not required for the DCR request, otherwise, the configured authentication mechanism will be used.
    **/
    public DCRConfig clientAuthenticationRequired(Boolean clientAuthenticationRequired) {

        this.clientAuthenticationRequired = clientAuthenticationRequired;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "If false, the client authentication is not required for the DCR request, otherwise, the configured authentication mechanism will be used.")
    @JsonProperty("clientAuthenticationRequired")
    @Valid
    public Boolean getClientAuthenticationRequired() {

        return clientAuthenticationRequired;
    }
    public void setClientAuthenticationRequired(Boolean clientAuthenticationRequired) {

        this.clientAuthenticationRequired = clientAuthenticationRequired;
    }

    /**
    * The JWKS endpoint to validate the SSA.
    **/
    public DCRConfig ssaJwks(String ssaJwks) {

        this.ssaJwks = ssaJwks;
        return this;
    }
    
    @ApiModelProperty(example = "https://keystore.openbankingtest.org.uk/0015800001HQQrZAAX/oQ4KoaavpOuoE7rvQsZEOV.jwks", value = "The JWKS endpoint to validate the SSA.")
    @JsonProperty("ssaJwks")
    @Valid
    public String getSsaJwks() {

        return ssaJwks;
    }
    public void setSsaJwks(String ssaJwks) {

        this.ssaJwks = ssaJwks;
    }

    /**
    * If true, the FAPI security profile will be enforced for the DCR request.
    **/
    public DCRConfig enableFapiEnforcement(Boolean enableFapiEnforcement) {

        this.enableFapiEnforcement = enableFapiEnforcement;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "If true, the FAPI security profile will be enforced for the DCR request.")
    @JsonProperty("enableFapiEnforcement")
    @Valid
    public Boolean getEnableFapiEnforcement() {

        return enableFapiEnforcement;
    }
    public void setEnableFapiEnforcement(Boolean enableFapiEnforcement) {

        this.enableFapiEnforcement = enableFapiEnforcement;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DCRConfig dcRConfig = (DCRConfig) o;
        return Objects.equals(this.clientAuthenticationRequired, dcRConfig.clientAuthenticationRequired) &&
            Objects.equals(this.ssaJwks, dcRConfig.ssaJwks) &&
            Objects.equals(this.enableFapiEnforcement, dcRConfig.enableFapiEnforcement);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clientAuthenticationRequired, ssaJwks, enableFapiEnforcement);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DCRConfig {\n");
        
        sb.append("    clientAuthenticationRequired: ").append(toIndentedString(clientAuthenticationRequired)).append("\n");
        sb.append("    ssaJwks: ").append(toIndentedString(ssaJwks)).append("\n");
        sb.append("    enableFapiEnforcement: ").append(toIndentedString(enableFapiEnforcement)).append("\n");
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

