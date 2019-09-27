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

package org.wso2.carbon.identity.rest.api.server.email.template.v1.dto;

import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.LocaleDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(description = "")
public class SimpleEmailTemplateResponseDTO {

    @Valid 
    @NotNull(message = "Property locale cannot be null.") 
    private LocaleDTO locale = null;

    @Valid 
    @NotNull(message = "Property contentType cannot be null.") 
    private String contentType = null;

    @Valid 
    private String location = null;

    /**
    **/
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("locale")
    public LocaleDTO getLocale() {
        return locale;
    }
    public void setLocale(LocaleDTO locale) {
        this.locale = locale;
    }

    /**
    * Content type of the emailt template.
    **/
    @ApiModelProperty(required = true, value = "Content type of the emailt template.")
    @JsonProperty("contentType")
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
    * Location of the created/updated resource.
    **/
    @ApiModelProperty(value = "Location of the created/updated resource.")
    @JsonProperty("location")
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SimpleEmailTemplateResponseDTO {\n");
        
        sb.append("    locale: ").append(locale).append("\n");
        sb.append("    contentType: ").append(contentType).append("\n");
        sb.append("    location: ").append(location).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
