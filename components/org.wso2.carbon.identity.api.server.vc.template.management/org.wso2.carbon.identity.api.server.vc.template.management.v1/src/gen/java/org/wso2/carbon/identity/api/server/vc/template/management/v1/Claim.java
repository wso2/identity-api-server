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

package org.wso2.carbon.identity.api.server.vc.template.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class Claim  {
  
    private String name;
    private String type;
    private String claimUri;

    /**
    * The claim name as it appears in the issued VC.
    **/
    public Claim name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "given_name", required = true, value = "The claim name as it appears in the issued VC.")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public Claim type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "LOCAL", required = true, value = "")
    @JsonProperty("type")
    @Valid
    @NotNull(message = "Property type cannot be null.")

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    * The claim reference used for resolution. For LOCAL, this is the WSO2 IS claim URI. 
    **/
    public Claim claimUri(String claimUri) {

        this.claimUri = claimUri;
        return this;
    }
    
    @ApiModelProperty(example = "http://wso2.org/claims/givenname", required = true, value = "The claim reference used for resolution. For LOCAL, this is the WSO2 IS claim URI. ")
    @JsonProperty("claimUri")
    @Valid
    @NotNull(message = "Property claimUri cannot be null.")

    public String getClaimUri() {
        return claimUri;
    }
    public void setClaimUri(String claimUri) {
        this.claimUri = claimUri;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Claim claim = (Claim) o;
        return Objects.equals(this.name, claim.name) &&
            Objects.equals(this.type, claim.type) &&
            Objects.equals(this.claimUri, claim.claimUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, claimUri);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Claim {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    claimUri: ").append(toIndentedString(claimUri)).append("\n");
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

