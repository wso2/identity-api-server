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
public class CompleteEmailTemplateResponseDTO {

    @Valid 
    @NotNull(message = "Property locale cannot be null.") 
    private LocaleDTO locale = null;

    @Valid 
    @NotNull(message = "Property contentType cannot be null.") 
    private String contentType = null;

    @Valid 
    @NotNull(message = "Property subject cannot be null.") 
    private String subject = null;

    @Valid 
    @NotNull(message = "Property body cannot be null.") 
    private String body = null;

    @Valid 
    @NotNull(message = "Property footer cannot be null.") 
    private String footer = null;

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
    * The subject of the email.
    **/
    @ApiModelProperty(required = true, value = "The subject of the email.")
    @JsonProperty("subject")
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
    * The body of the email.
    **/
    @ApiModelProperty(required = true, value = "The body of the email.")
    @JsonProperty("body")
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    /**
    * The footer of the email.
    **/
    @ApiModelProperty(required = true, value = "The footer of the email.")
    @JsonProperty("footer")
    public String getFooter() {
        return footer;
    }
    public void setFooter(String footer) {
        this.footer = footer;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CompleteEmailTemplateResponseDTO {\n");
        
        sb.append("    locale: ").append(locale).append("\n");
        sb.append("    contentType: ").append(contentType).append("\n");
        sb.append("    subject: ").append(subject).append("\n");
        sb.append("    body: ").append(body).append("\n");
        sb.append("    footer: ").append(footer).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
