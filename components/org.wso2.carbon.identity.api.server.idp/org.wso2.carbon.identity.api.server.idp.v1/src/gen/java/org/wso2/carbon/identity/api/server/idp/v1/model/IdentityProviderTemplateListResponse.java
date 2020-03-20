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

package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderTemplateListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.Link;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class IdentityProviderTemplateListResponse  {
  
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<Link> links = null;

    private List<IdentityProviderTemplateListItem> templates = null;


    /**
    **/
    public IdentityProviderTemplateListResponse totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "")
    @JsonProperty("totalResults")
    @Valid
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
    **/
    public IdentityProviderTemplateListResponse startIndex(Integer startIndex) {

        this.startIndex = startIndex;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "")
    @JsonProperty("startIndex")
    @Valid
    public Integer getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    /**
    **/
    public IdentityProviderTemplateListResponse count(Integer count) {

        this.count = count;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "")
    @JsonProperty("count")
    @Valid
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
    **/
    public IdentityProviderTemplateListResponse links(List<Link> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"href\":\"identity-provider/templates?offset=50&limit=10\",\"rel\":\"next\"},{\"href\":\"identity-provider/templates?offset=30&limit=10\",\"rel\":\"previous\"}]", value = "")
    @JsonProperty("links")
    @Valid
    public List<Link> getLinks() {
        return links;
    }
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public IdentityProviderTemplateListResponse addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public IdentityProviderTemplateListResponse templates(List<IdentityProviderTemplateListItem> templates) {

        this.templates = templates;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("templates")
    @Valid
    public List<IdentityProviderTemplateListItem> getTemplates() {
        return templates;
    }
    public void setTemplates(List<IdentityProviderTemplateListItem> templates) {
        this.templates = templates;
    }

    public IdentityProviderTemplateListResponse addTemplatesItem(IdentityProviderTemplateListItem templatesItem) {
        if (this.templates == null) {
            this.templates = new ArrayList<>();
        }
        this.templates.add(templatesItem);
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
        IdentityProviderTemplateListResponse identityProviderTemplateListResponse = (IdentityProviderTemplateListResponse) o;
        return Objects.equals(this.totalResults, identityProviderTemplateListResponse.totalResults) &&
            Objects.equals(this.startIndex, identityProviderTemplateListResponse.startIndex) &&
            Objects.equals(this.count, identityProviderTemplateListResponse.count) &&
            Objects.equals(this.links, identityProviderTemplateListResponse.links) &&
            Objects.equals(this.templates, identityProviderTemplateListResponse.templates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, startIndex, count, links, templates);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityProviderTemplateListResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    startIndex: ").append(toIndentedString(startIndex)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    templates: ").append(toIndentedString(templates)).append("\n");
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

