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
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

/**
 * Location of each file uploaded in a scenario where multiple representations of a single resource is uploaded.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Location of each file uploaded in a scenario where multiple representations of a single resource is uploaded.")
public class MultipleFilesUploadResponse  {
  
    private List<String> links = null;


    /**
    **/
    public MultipleFilesUploadResponse links(List<String> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(example = "[\"https://localhost:9443/t/carbon.super/api/server/v1/media/image/6e41cb95-c3b3-4e6c-928a-acb1b88e991d/data?identifier=large\",\"https://localhost:9443/t/carbon.super/api/server/v1/media/image/6e41cb95-c3b3-4e6c-928a-acb1b88e991d/data?identifier=medium\"]", value = "")
    @JsonProperty("links")
    @Valid
    public List<String> getLinks() {
        return links;
    }
    public void setLinks(List<String> links) {
        this.links = links;
    }

    public MultipleFilesUploadResponse addLinksItem(String linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultipleFilesUploadResponse multipleFilesUploadResponse = (MultipleFilesUploadResponse) o;
        return Objects.equals(this.links, multipleFilesUploadResponse.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(links);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MultipleFilesUploadResponse {\n");
        
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

