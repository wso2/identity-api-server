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
import org.wso2.carbon.identity.api.server.idp.v1.model.Claim;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ProvisioningClaim  {
  
    private Claim claim;
    private String defaultValue;

    /**
    **/
    public ProvisioningClaim claim(Claim claim) {

        this.claim = claim;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("claim")
    @Valid
    public Claim getClaim() {
        return claim;
    }
    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    /**
    **/
    public ProvisioningClaim defaultValue(String defaultValue) {

        this.defaultValue = defaultValue;
        return this;
    }
    
    @ApiModelProperty(example = "sathya", value = "")
    @JsonProperty("defaultValue")
    @Valid
    public String getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProvisioningClaim provisioningClaim = (ProvisioningClaim) o;
        return Objects.equals(this.claim, provisioningClaim.claim) &&
            Objects.equals(this.defaultValue, provisioningClaim.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(claim, defaultValue);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ProvisioningClaim {\n");
        
        sb.append("    claim: ").append(toIndentedString(claim)).append("\n");
        sb.append("    defaultValue: ").append(toIndentedString(defaultValue)).append("\n");
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

