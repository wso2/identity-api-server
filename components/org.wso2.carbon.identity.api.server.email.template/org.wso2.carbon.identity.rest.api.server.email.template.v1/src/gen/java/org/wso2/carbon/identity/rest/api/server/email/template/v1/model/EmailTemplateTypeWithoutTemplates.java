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

package org.wso2.carbon.identity.rest.api.server.email.template.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class EmailTemplateTypeWithoutTemplates  {
  
    private String id;
    private String displayName;
    private String location;

    /**
    * Unique id of the email template type.
    **/
    public EmailTemplateTypeWithoutTemplates id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "YWNjb3VudGNvbmZpcm1hdGlvbg", required = true, value = "Unique id of the email template type.")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Display name of the email template type.
    **/
    public EmailTemplateTypeWithoutTemplates displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "Account Confirmation", required = true, value = "Display name of the email template type.")
    @JsonProperty("displayName")
    @Valid
    @NotNull(message = "Property displayName cannot be null.")

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    * Location of the created/updated resource.
    **/
    public EmailTemplateTypeWithoutTemplates location(String location) {

        this.location = location;
        return this;
    }
    
    @ApiModelProperty(example = "/t/{tenant-domain}/api/server/v1/email/template-types/YWNjb3VudGNvbmZpcm1hdGlvbg", required = true, value = "Location of the created/updated resource.")
    @JsonProperty("location")
    @Valid
    @NotNull(message = "Property location cannot be null.")

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmailTemplateTypeWithoutTemplates emailTemplateTypeWithoutTemplates = (EmailTemplateTypeWithoutTemplates) o;
        return Objects.equals(this.id, emailTemplateTypeWithoutTemplates.id) &&
            Objects.equals(this.displayName, emailTemplateTypeWithoutTemplates.displayName) &&
            Objects.equals(this.location, emailTemplateTypeWithoutTemplates.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName, location);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class EmailTemplateTypeWithoutTemplates {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    location: ").append(toIndentedString(location)).append("\n");
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

