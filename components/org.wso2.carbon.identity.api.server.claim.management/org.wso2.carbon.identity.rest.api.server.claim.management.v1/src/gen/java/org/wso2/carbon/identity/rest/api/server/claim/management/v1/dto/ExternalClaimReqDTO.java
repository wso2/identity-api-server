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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

    /**
    * External claim request.
    **/
@ApiModel(description = "External claim request.")
public class ExternalClaimReqDTO {

    @Valid 
    @NotNull(message = "Property claimURI cannot be null.") 
    private String claimURI = null;

    @Valid 
    @NotNull(message = "Property mappedLocalClaimURI cannot be null.") 
    private String mappedLocalClaimURI = null;

    /**
    * Claim URI of the external claim.
    **/
    @ApiModelProperty(required = true, value = "Claim URI of the external claim.")
    @JsonProperty("claimURI")
    public String getClaimURI() {
        return claimURI;
    }
    public void setClaimURI(String claimURI) {
        this.claimURI = claimURI;
    }

    /**
    * The local claim URI to map with the external claim.
    **/
    @ApiModelProperty(required = true, value = "The local claim URI to map with the external claim.")
    @JsonProperty("mappedLocalClaimURI")
    public String getMappedLocalClaimURI() {
        return mappedLocalClaimURI;
    }
    public void setMappedLocalClaimURI(String mappedLocalClaimURI) {
        this.mappedLocalClaimURI = mappedLocalClaimURI;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ExternalClaimReqDTO {\n");
        
        sb.append("    claimURI: ").append(claimURI).append("\n");
        sb.append("    mappedLocalClaimURI: ").append(mappedLocalClaimURI).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }

    public boolean equals(ExternalClaimResDTO externalClaimResDTO) {

        if (externalClaimResDTO == null) {
            return false;
        }

        return StringUtils.equals(this.getClaimURI(), externalClaimResDTO.getClaimURI()) &&
                StringUtils.equals(this.getMappedLocalClaimURI(), externalClaimResDTO.getMappedLocalClaimURI());
    }
}
