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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

/**
 * Location(s) of media and metadata.
 **/
@ApiModel(description = "Location(s) of media and metadata.")
public class MediaInformationResponse  {
  
    private List<String> links = null;

    private MediaInformationResponseMetadata metadata;

    /**
    **/
    public MediaInformationResponse links(List<String> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(example = "[\"/t/carbon.super/api/server/v1/media/content/image/6e41cb95-c3b3-4e6c-928a-acb1b88e991d\"]", value = "")
    @JsonProperty("links")
    @Valid
    public List<String> getLinks() {
        return links;
    }
    public void setLinks(List<String> links) {
        this.links = links;
    }

    public MediaInformationResponse addLinksItem(String linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public MediaInformationResponse metadata(MediaInformationResponseMetadata metadata) {

        this.metadata = metadata;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("metadata")
    @Valid
    public MediaInformationResponseMetadata getMetadata() {
        return metadata;
    }
    public void setMetadata(MediaInformationResponseMetadata metadata) {
        this.metadata = metadata;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MediaInformationResponse mediaInformationResponse = (MediaInformationResponse) o;
        return Objects.equals(this.links, mediaInformationResponse.links) &&
            Objects.equals(this.metadata, mediaInformationResponse.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(links, metadata);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MediaInformationResponse {\n");
        
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

