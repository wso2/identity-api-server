/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.organization.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.Link;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class MetaAttributesResponse  {
  
    private List<Link> links = null;

    private List<String> attributes = null;


    /**
    **/
    public MetaAttributesResponse links(List<Link> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"href\":\"/api/server/v1/organizations/meta-attributes?limit=10&recursive=false&filter=attributes+co+C&after=MjAyMS0xMi0yMSAwNToxODozMS4wMDQzNDg=\",\"rel\":\"next\"},{\"href\":\"/api/server/v1/organizations/meta-attributes?limit=10&recursive=false&filter=attributes+co+C&before=MjAyMS0xMi0yMSAwNToxODozMS4wMDQzNDg=\",\"rel\":\"previous\"}]", value = "")
    @JsonProperty("links")
    @Valid
    public List<Link> getLinks() {
        return links;
    }
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public MetaAttributesResponse addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public MetaAttributesResponse attributes(List<String> attributes) {

        this.attributes = attributes;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("attributes")
    @Valid
    public List<String> getAttributes() {
        return attributes;
    }
    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public MetaAttributesResponse addAttributesItem(String attributesItem) {
        if (this.attributes == null) {
            this.attributes = new ArrayList<>();
        }
        this.attributes.add(attributesItem);
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
        MetaAttributesResponse metaAttributesResponse = (MetaAttributesResponse) o;
        return Objects.equals(this.links, metaAttributesResponse.links) &&
            Objects.equals(this.attributes, metaAttributesResponse.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(links, attributes);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MetaAttributesResponse {\n");
        
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
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

