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
import org.wso2.carbon.identity.api.server.application.management.v1.Claim;
import javax.validation.constraints.*;

/**
 * User claims that need to be sent back to the application. If the claim mappings are local, use local claim URIs. If the custom claim mappings are configured, use the mapped application claim URI
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "User claims that need to be sent back to the application. If the claim mappings are local, use local claim URIs. If the custom claim mappings are configured, use the mapped application claim URI")
public class RequestedClaimConfiguration  {
  
    private Claim claim;
    private Boolean mandatory;

    /**
    **/
    public RequestedClaimConfiguration claim(Claim claim) {

        this.claim = claim;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("claim")
    @Valid
    @NotNull(message = "Property claim cannot be null.")

    public Claim getClaim() {
        return claim;
    }
    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    /**
    **/
    public RequestedClaimConfiguration mandatory(Boolean mandatory) {

        this.mandatory = mandatory;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "")
    @JsonProperty("mandatory")
    @Valid
    public Boolean getMandatory() {
        return mandatory;
    }
    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestedClaimConfiguration requestedClaimConfiguration = (RequestedClaimConfiguration) o;
        return Objects.equals(this.claim, requestedClaimConfiguration.claim) &&
            Objects.equals(this.mandatory, requestedClaimConfiguration.mandatory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(claim, mandatory);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RequestedClaimConfiguration {\n");
        
        sb.append("    claim: ").append(toIndentedString(claim)).append("\n");
        sb.append("    mandatory: ").append(toIndentedString(mandatory)).append("\n");
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

