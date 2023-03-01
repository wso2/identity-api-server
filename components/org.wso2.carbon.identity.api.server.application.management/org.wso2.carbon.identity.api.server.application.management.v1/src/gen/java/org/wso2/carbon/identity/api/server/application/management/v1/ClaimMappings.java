/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.application.management.v1.Claim;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ClaimMappings  {
  
    private String applicationClaim;
    private Claim localClaim;

    /**
    * Claim URI recieved by the application
    **/
    public ClaimMappings applicationClaim(String applicationClaim) {

        this.applicationClaim = applicationClaim;
        return this;
    }
    
    @ApiModelProperty(example = "firstname", required = true, value = "Claim URI recieved by the application")
    @JsonProperty("applicationClaim")
    @Valid
    @NotNull(message = "Property applicationClaim cannot be null.")

    public String getApplicationClaim() {
        return applicationClaim;
    }
    public void setApplicationClaim(String applicationClaim) {
        this.applicationClaim = applicationClaim;
    }

    /**
    **/
    public ClaimMappings localClaim(Claim localClaim) {

        this.localClaim = localClaim;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("localClaim")
    @Valid
    @NotNull(message = "Property localClaim cannot be null.")

    public Claim getLocalClaim() {
        return localClaim;
    }
    public void setLocalClaim(Claim localClaim) {
        this.localClaim = localClaim;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClaimMappings claimMappings = (ClaimMappings) o;
        return Objects.equals(this.applicationClaim, claimMappings.applicationClaim) &&
            Objects.equals(this.localClaim, claimMappings.localClaim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationClaim, localClaim);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ClaimMappings {\n");
        
        sb.append("    applicationClaim: ").append(toIndentedString(applicationClaim)).append("\n");
        sb.append("    localClaim: ").append(toIndentedString(localClaim)).append("\n");
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

