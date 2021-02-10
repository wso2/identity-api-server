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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

    /**
    * Claim dialect request.
    **/
@ApiModel(description = "Claim dialect request.")
public class ClaimDialectReqDTO {

    @Valid 
    @NotNull(message = "Property dialectURI cannot be null.") 
    private String dialectURI = null;

    /**
    * URI of the claim dialect.
    **/
    @ApiModelProperty(required = true, value = "URI of the claim dialect.")
    @JsonProperty("dialectURI")
    public String getDialectURI() {
        return dialectURI;
    }
    public void setDialectURI(String dialectURI) {
        this.dialectURI = dialectURI;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ClaimDialectReqDTO {\n");
        
        sb.append("    dialectURI: ").append(dialectURI).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
