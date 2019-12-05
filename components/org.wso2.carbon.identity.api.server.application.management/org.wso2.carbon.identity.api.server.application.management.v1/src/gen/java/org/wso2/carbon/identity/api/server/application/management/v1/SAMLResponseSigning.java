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

public class SAMLResponseSigning  {
  
    private Boolean enabled = true;
    private String signingAlgorithm;

    /**
    **/
    public SAMLResponseSigning enabled(Boolean enabled) {

        this.enabled = enabled;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("enabled")
    @Valid
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
    **/
    public SAMLResponseSigning signingAlgorithm(String signingAlgorithm) {

        this.signingAlgorithm = signingAlgorithm;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("signingAlgorithm")
    @Valid
    public String getSigningAlgorithm() {
        return signingAlgorithm;
    }
    public void setSigningAlgorithm(String signingAlgorithm) {
        this.signingAlgorithm = signingAlgorithm;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SAMLResponseSigning saMLResponseSigning = (SAMLResponseSigning) o;
        return Objects.equals(this.enabled, saMLResponseSigning.enabled) &&
            Objects.equals(this.signingAlgorithm, saMLResponseSigning.signingAlgorithm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, signingAlgorithm);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SAMLResponseSigning {\n");
        
        sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
        sb.append("    signingAlgorithm: ").append(toIndentedString(signingAlgorithm)).append("\n");
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

