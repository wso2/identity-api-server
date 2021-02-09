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
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LinkDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

    /**
    * Claim dialect response.
    **/
@ApiModel(description = "Claim dialect response.")
public class ClaimDialectResDTO {

    @Valid 
    private String id = null;

    @Valid 
    private String dialectURI = null;

    @Valid 
    private LinkDTO link = null;

    /**
    * Dialect id.
    **/
    @ApiModelProperty(value = "Dialect id.")
    @JsonProperty("id")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * URI of the claim dialect.
    **/
    @ApiModelProperty(value = "URI of the claim dialect.")
    @JsonProperty("dialectURI")
    public String getDialectURI() {
        return dialectURI;
    }
    public void setDialectURI(String dialectURI) {
        this.dialectURI = dialectURI;
    }

    /**
    **/
    @ApiModelProperty(value = "")
    @JsonProperty("link")
    public LinkDTO getLink() {
        return link;
    }
    public void setLink(LinkDTO link) {
        this.link = link;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ClaimDialectResDTO {\n");
        
        sb.append("    id: ").append(id).append("\n");
        sb.append("    dialectURI: ").append(dialectURI).append("\n");
        sb.append("    link: ").append(link).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
