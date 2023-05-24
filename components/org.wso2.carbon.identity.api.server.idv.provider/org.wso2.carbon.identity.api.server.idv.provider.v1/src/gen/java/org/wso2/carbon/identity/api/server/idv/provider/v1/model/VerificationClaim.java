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

package org.wso2.carbon.identity.api.server.idv.provider.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class VerificationClaim  {
  
    private String localClaim;
    private String idvpClaim;

    /**
    **/
    public VerificationClaim localClaim(String localClaim) {

        this.localClaim = localClaim;
        return this;
    }
    
    @ApiModelProperty(example = "http://wso2.org/claims/dob", value = "")
    @JsonProperty("localClaim")
    @Valid
    public String getLocalClaim() {
        return localClaim;
    }
    public void setLocalClaim(String localClaim) {
        this.localClaim = localClaim;
    }

    /**
    **/
    public VerificationClaim idvpClaim(String idvpClaim) {

        this.idvpClaim = idvpClaim;
        return this;
    }
    
    @ApiModelProperty(example = "birthday", value = "")
    @JsonProperty("idvpClaim")
    @Valid
    public String getIdvpClaim() {
        return idvpClaim;
    }
    public void setIdvpClaim(String idvpClaim) {
        this.idvpClaim = idvpClaim;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VerificationClaim verificationClaim = (VerificationClaim) o;
        return Objects.equals(this.localClaim, verificationClaim.localClaim) &&
            Objects.equals(this.idvpClaim, verificationClaim.idvpClaim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localClaim, idvpClaim);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VerificationClaim {\n");
        
        sb.append("    localClaim: ").append(toIndentedString(localClaim)).append("\n");
        sb.append("    idvpClaim: ").append(toIndentedString(idvpClaim)).append("\n");
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

