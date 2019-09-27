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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

    /**
    * The locale of the email template.
    **/
@ApiModel(description = "The locale of the email template.")
public class LocaleResponseDTO {

    @Valid 
    private String localeCode = null;

    @Valid 
    @NotNull(message = "Property displayName cannot be null.") 
    private String displayName = null;

    @Valid 
    private String location = null;

    /**
    * Unique code of the locale.
    **/
    @ApiModelProperty(value = "Unique code of the locale.")
    @JsonProperty("localeCode")
    public String getLocaleCode() {
        return localeCode;
    }
    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    /**
    * Unique code of the locale.
    **/
    @ApiModelProperty(required = true, value = "Unique code of the locale.")
    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
        sb.append("class LocaleResponseDTO {\n");
        
        sb.append("    localeCode: ").append(localeCode).append("\n");
        sb.append("    displayName: ").append(displayName).append("\n");
        sb.append("    location: ").append(location).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
