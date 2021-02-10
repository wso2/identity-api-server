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

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(description = "")
public class LinkDTO {

    @Valid 
    private String href = null;

    @Valid 
    private String rel = null;

    /**
    * Relative path to the target resource.
    **/
    @ApiModelProperty(value = "Relative path to the target resource.")
    @JsonProperty("href")
    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }

    /**
    * Describes how the current context is related to the target resource.
    **/
    @ApiModelProperty(value = "Describes how the current context is related to the target resource.")
    @JsonProperty("rel")
    public String getRel() {
        return rel;
    }
    public void setRel(String rel) {
        this.rel = rel;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class LinkDTO {\n");
        
        sb.append("    href: ").append(href).append("\n");
        sb.append("    rel: ").append(rel).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
