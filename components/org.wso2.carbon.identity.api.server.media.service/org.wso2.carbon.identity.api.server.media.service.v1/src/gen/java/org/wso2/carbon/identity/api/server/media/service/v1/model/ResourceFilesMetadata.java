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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

public class ResourceFilesMetadata  {
  
    private String tag;
    private ResourceFilesMetadataSecurity security;
    private List<String> identifiers = null;


    /**
    * The file tag.
    **/
    public ResourceFilesMetadata tag(String tag) {

        this.tag = tag;
        return this;
    }
    
    @ApiModelProperty(example = "user", value = "The file tag.")
    @JsonProperty("tag")
    @Valid
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
    **/
    public ResourceFilesMetadata security(ResourceFilesMetadataSecurity security) {

        this.security = security;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("security")
    @Valid
    public ResourceFilesMetadataSecurity getSecurity() {
        return security;
    }
    public void setSecurity(ResourceFilesMetadataSecurity security) {
        this.security = security;
    }

    /**
    **/
    public ResourceFilesMetadata identifiers(List<String> identifiers) {

        this.identifiers = identifiers;
        return this;
    }
    
    @ApiModelProperty(example = "[\"large\",\"medium\",\"small\"]", value = "")
    @JsonProperty("identifiers")
    @Valid
    public List<String> getIdentifiers() {
        return identifiers;
    }
    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    public ResourceFilesMetadata addIdentifiersItem(String identifiersItem) {
        if (this.identifiers == null) {
            this.identifiers = new ArrayList<>();
        }
        this.identifiers.add(identifiersItem);
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
        ResourceFilesMetadata resourceFilesMetadata = (ResourceFilesMetadata) o;
        return Objects.equals(this.tag, resourceFilesMetadata.tag) &&
            Objects.equals(this.security, resourceFilesMetadata.security) &&
            Objects.equals(this.identifiers, resourceFilesMetadata.identifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, security, identifiers);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ResourceFilesMetadata {\n");
        
        sb.append("    tag: ").append(toIndentedString(tag)).append("\n");
        sb.append("    security: ").append(toIndentedString(security)).append("\n");
        sb.append("    identifiers: ").append(toIndentedString(identifiers)).append("\n");
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

