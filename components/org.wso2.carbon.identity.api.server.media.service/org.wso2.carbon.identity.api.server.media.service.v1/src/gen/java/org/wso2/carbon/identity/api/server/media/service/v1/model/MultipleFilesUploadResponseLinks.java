/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.media.service.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import javax.validation.Valid;

public class MultipleFilesUploadResponseLinks  {
  
    private String rel;
    private String href;

    /**
    * Identifier for the sub resource.
    **/
    public MultipleFilesUploadResponseLinks rel(String rel) {

        this.rel = rel;
        return this;
    }
    
    @ApiModelProperty(example = "large", value = "Identifier for the sub resource.")
    @JsonProperty("rel")
    @Valid
    public String getRel() {
        return rel;
    }
    public void setRel(String rel) {
        this.rel = rel;
    }

    /**
    * Location of the uploaded sub resource.
    **/
    public MultipleFilesUploadResponseLinks href(String href) {

        this.href = href;
        return this;
    }
    
    @ApiModelProperty(example = "/t/carbon.super/api/server/v1/media/content/image/6e41cb95-c3b3-4e6c-928a-acb1b88e991d?identifier=large", value = "Location of the uploaded sub resource.")
    @JsonProperty("href")
    @Valid
    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultipleFilesUploadResponseLinks multipleFilesUploadResponseLinks = (MultipleFilesUploadResponseLinks) o;
        return Objects.equals(this.rel, multipleFilesUploadResponseLinks.rel) &&
            Objects.equals(this.href, multipleFilesUploadResponseLinks.href);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rel, href);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MultipleFilesUploadResponseLinks {\n");
        
        sb.append("    rel: ").append(toIndentedString(rel)).append("\n");
        sb.append("    href: ").append(toIndentedString(href)).append("\n");
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

