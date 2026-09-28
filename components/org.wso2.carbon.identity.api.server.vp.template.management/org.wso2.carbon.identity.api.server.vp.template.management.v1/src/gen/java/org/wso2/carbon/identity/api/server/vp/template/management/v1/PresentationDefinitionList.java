/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.vp.template.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PaginationLink;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionListItem;
import javax.validation.constraints.*;

/**
 * Paginated list of presentation definitions.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Paginated list of presentation definitions.")
public class PresentationDefinitionList  {
  
    private Integer totalResults;
    private List<PaginationLink> links = null;

    private List<PresentationDefinitionListItem> presentationDefinitions = null;


    /**
    * Total number of presentation definitions matching the query.
    **/
    public PresentationDefinitionList totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "42", value = "Total number of presentation definitions matching the query.")
    @JsonProperty("totalResults")
    @Valid
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
    * Cursor-based pagination links (next/previous).
    **/
    public PresentationDefinitionList links(List<PaginationLink> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(value = "Cursor-based pagination links (next/previous).")
    @JsonProperty("links")
    @Valid
    public List<PaginationLink> getLinks() {
        return links;
    }
    public void setLinks(List<PaginationLink> links) {
        this.links = links;
    }

    public PresentationDefinitionList addLinksItem(PaginationLink linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    * List of presentation definition summaries for the current page.
    **/
    public PresentationDefinitionList presentationDefinitions(List<PresentationDefinitionListItem> presentationDefinitions) {

        this.presentationDefinitions = presentationDefinitions;
        return this;
    }
    
    @ApiModelProperty(value = "List of presentation definition summaries for the current page.")
    @JsonProperty("presentationDefinitions")
    @Valid
    public List<PresentationDefinitionListItem> getPresentationDefinitions() {
        return presentationDefinitions;
    }
    public void setPresentationDefinitions(List<PresentationDefinitionListItem> presentationDefinitions) {
        this.presentationDefinitions = presentationDefinitions;
    }

    public PresentationDefinitionList addPresentationDefinitionsItem(PresentationDefinitionListItem presentationDefinitionsItem) {
        if (this.presentationDefinitions == null) {
            this.presentationDefinitions = new ArrayList<>();
        }
        this.presentationDefinitions.add(presentationDefinitionsItem);
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
        PresentationDefinitionList presentationDefinitionList = (PresentationDefinitionList) o;
        return Objects.equals(this.totalResults, presentationDefinitionList.totalResults) &&
            Objects.equals(this.links, presentationDefinitionList.links) &&
            Objects.equals(this.presentationDefinitions, presentationDefinitionList.presentationDefinitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, links, presentationDefinitions);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PresentationDefinitionList {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    presentationDefinitions: ").append(toIndentedString(presentationDefinitions)).append("\n");
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

