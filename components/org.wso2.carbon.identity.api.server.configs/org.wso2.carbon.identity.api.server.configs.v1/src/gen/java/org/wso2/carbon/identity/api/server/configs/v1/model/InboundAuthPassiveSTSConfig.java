/*
 * Copyright (c) 2023, WSO2 LLC. (https://www.wso2.com).
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
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class InboundAuthPassiveSTSConfig  {
  
    private Boolean enableRequestSigning;
    private String passiveSTSUrl;

    /**
    * Enable authentication requests signing
    **/
    public InboundAuthPassiveSTSConfig enableRequestSigning(Boolean enableRequestSigning) {

        this.enableRequestSigning = enableRequestSigning;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Enable authentication requests signing")
    @JsonProperty("enableRequestSigning")
    @Valid
    public Boolean getEnableRequestSigning() {
        return enableRequestSigning;
    }
    public void setEnableRequestSigning(Boolean enableRequestSigning) {
        this.enableRequestSigning = enableRequestSigning;
    }

    /**
    * Passive STS URL
    **/
    public InboundAuthPassiveSTSConfig passiveSTSUrl(String passiveSTSUrl) {

        this.passiveSTSUrl = passiveSTSUrl;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9443/passivests", value = "Passive STS URL")
    @JsonProperty("passiveSTSUrl")
    @Valid
    public String getPassiveSTSUrl() {
        return passiveSTSUrl;
    }
    public void setPassiveSTSUrl(String passiveSTSUrl) {
        this.passiveSTSUrl = passiveSTSUrl;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InboundAuthPassiveSTSConfig inboundAuthPassiveSTSConfig = (InboundAuthPassiveSTSConfig) o;
        return Objects.equals(this.enableRequestSigning, inboundAuthPassiveSTSConfig.enableRequestSigning) &&
            Objects.equals(this.passiveSTSUrl, inboundAuthPassiveSTSConfig.passiveSTSUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enableRequestSigning, passiveSTSUrl);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InboundAuthPassiveSTSConfig {\n");
        
        sb.append("    enableRequestSigning: ").append(toIndentedString(enableRequestSigning)).append("\n");
        sb.append("    passiveSTSUrl: ").append(toIndentedString(passiveSTSUrl)).append("\n");
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

