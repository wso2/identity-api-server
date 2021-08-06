/*
* Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.userstore.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ClaimAttributeMapping  {
  
    private String claimURI;
    private String mappedAttribute;

    /**
    * A unique URI specific to the claim.
    **/
    public ClaimAttributeMapping claimURI(String claimURI) {

        this.claimURI = claimURI;
        return this;
    }
    
    @ApiModelProperty(example = "http://wso2.org/claims/username", required = true, value = "A unique URI specific to the claim.")
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
    * Userstore attribute to be mapped to.
    **/
    public ClaimAttributeMapping mappedAttribute(String mappedAttribute) {

        this.mappedAttribute = mappedAttribute;
        return this;
    }
    
    @ApiModelProperty(example = "username", required = true, value = "Userstore attribute to be mapped to.")
    @JsonProperty("mappedAttribute")
    @Valid
    @NotNull(message = "Property mappedAttribute cannot be null.")

    public String getMappedAttribute() {
        return mappedAttribute;
    }
    public void setMappedAttribute(String mappedAttribute) {
        this.mappedAttribute = mappedAttribute;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClaimAttributeMapping claimAttributeMapping = (ClaimAttributeMapping) o;
        return Objects.equals(this.claimURI, claimAttributeMapping.claimURI) &&
            Objects.equals(this.mappedAttribute, claimAttributeMapping.mappedAttribute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(claimURI, mappedAttribute);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ClaimAttributeMapping {\n");
        
        sb.append("    claimURI: ").append(toIndentedString(claimURI)).append("\n");
        sb.append("    mappedAttribute: ").append(toIndentedString(mappedAttribute)).append("\n");
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

