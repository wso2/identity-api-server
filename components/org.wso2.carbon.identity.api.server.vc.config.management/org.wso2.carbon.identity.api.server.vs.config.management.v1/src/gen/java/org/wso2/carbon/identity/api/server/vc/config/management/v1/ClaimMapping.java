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

package org.wso2.carbon.identity.api.server.vc.config.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ClaimMapping  {
  
    private String claimURI;
    private String display;

    /**
    **/
    public ClaimMapping claimURI(String claimURI) {

        this.claimURI = claimURI;
        return this;
    }
    
    @ApiModelProperty(example = "givenname", required = true, value = "")
    @JsonProperty("claimURI")
    @Valid
    @NotNull(message = "Property claimURI cannot be null.")

    public String getClaimURI() {
        return claimURI;
    }
    public void setClaimURI(String claimURI) {
        this.claimURI = claimURI;
    }

    /**
    * claim display name.
    **/
    public ClaimMapping display(String display) {

        this.display = display;
        return this;
    }
    
    @ApiModelProperty(example = "given_name", required = true, value = "claim display name.")
    @JsonProperty("display")
    @Valid
    @NotNull(message = "Property display cannot be null.")

    public String getDisplay() {
        return display;
    }
    public void setDisplay(String display) {
        this.display = display;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClaimMapping claimMapping = (ClaimMapping) o;
        return Objects.equals(this.claimURI, claimMapping.claimURI) &&
            Objects.equals(this.display, claimMapping.display);
    }

    @Override
    public int hashCode() {
        return Objects.hash(claimURI, display);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ClaimMapping {\n");
        
        sb.append("    claimURI: ").append(toIndentedString(claimURI)).append("\n");
        sb.append("    display: ").append(toIndentedString(display)).append("\n");
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

