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

package org.wso2.carbon.identity.api.server.consent.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PaginationLink;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeVersionSummaryDTO;
import javax.validation.constraints.*;

/**
 * Paginated list of purpose versions
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Paginated list of purpose versions")
public class PurposeVersionListResponse  {
  
    private Integer totalResults;
    private List<PaginationLink> links = null;

    private List<PurposeVersionSummaryDTO> versions = null;


    /**
    * Number of results returned in the current page
    **/
    public PurposeVersionListResponse totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "2", value = "Number of results returned in the current page")
    @JsonProperty("totalResults")
    @Valid
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
    * Pagination links for next/previous pages
    **/
    public PurposeVersionListResponse links(List<PaginationLink> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(value = "Pagination links for next/previous pages")
    @JsonProperty("links")
    @Valid
    public List<PaginationLink> getLinks() {
        return links;
    }
    public void setLinks(List<PaginationLink> links) {
        this.links = links;
    }

    public PurposeVersionListResponse addLinksItem(PaginationLink linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    * List of purpose versions
    **/
    public PurposeVersionListResponse versions(List<PurposeVersionSummaryDTO> versions) {

        this.versions = versions;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"id\":\"a1b2c3d4-1234-5678-abcd-ef1234567890\",\"version\":\"v1.0\",\"description\":\"Initial version\"},{\"id\":\"b2c3d4e5-2345-6789-bcde-f01234567892\",\"version\":\"v2.0\",\"description\":\"Added new consent elements for enhanced user authentication\"}]", value = "List of purpose versions")
    @JsonProperty("Versions")
    @Valid
    public List<PurposeVersionSummaryDTO> getVersions() {
        return versions;
    }
    public void setVersions(List<PurposeVersionSummaryDTO> versions) {
        this.versions = versions;
    }

    public PurposeVersionListResponse addVersionsItem(PurposeVersionSummaryDTO versionsItem) {
        if (this.versions == null) {
            this.versions = new ArrayList<>();
        }
        this.versions.add(versionsItem);
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
        PurposeVersionListResponse purposeVersionListResponse = (PurposeVersionListResponse) o;
        return Objects.equals(this.totalResults, purposeVersionListResponse.totalResults) &&
            Objects.equals(this.links, purposeVersionListResponse.links) &&
            Objects.equals(this.versions, purposeVersionListResponse.versions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, links, versions);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PurposeVersionListResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    versions: ").append(toIndentedString(versions)).append("\n");
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

