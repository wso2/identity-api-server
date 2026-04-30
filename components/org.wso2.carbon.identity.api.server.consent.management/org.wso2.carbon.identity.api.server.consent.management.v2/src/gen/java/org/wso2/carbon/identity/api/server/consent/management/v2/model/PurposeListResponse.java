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
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PurposeSummaryDTO;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PurposeListResponse  {
  
    private Integer totalResults;
    private List<PaginationLink> links = null;

    private List<PurposeSummaryDTO> purposes = null;


    /**
    * Total number of results matching the query
    **/
    public PurposeListResponse totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "2", value = "Total number of results matching the query")
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
    public PurposeListResponse links(List<PaginationLink> links) {

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

    public PurposeListResponse addLinksItem(PaginationLink linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    * List of purposes
    **/
    public PurposeListResponse purposes(List<PurposeSummaryDTO> purposes) {

        this.purposes = purposes;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"id\":\"f83aa1a3-5d4d-4c0e-84db-c3a4f1e6c8b2\",\"name\":\"Privacy Policy\",\"description\":\"Collection of user data for privacy policy compliance and consent management\",\"type\":\"Policy\",\"latestVersion\":{\"id\":\"a1b2c3d4-1234-5678-abcd-ef1234567890\",\"version\":\"v1.0\"}},{\"id\":\"c2d3e4f5-2345-6789-bcde-f01234567891\",\"name\":\"DEFAULT\",\"description\":\"For core functionalities of the product\",\"type\":\"SP\",\"latestVersion\":{\"id\":\"b2c3d4e5-2345-6789-bcde-f01234567892\",\"version\":\"v1.0\"}}]", value = "List of purposes")
    @JsonProperty("Purposes")
    @Valid
    public List<PurposeSummaryDTO> getPurposes() {
        return purposes;
    }
    public void setPurposes(List<PurposeSummaryDTO> purposes) {
        this.purposes = purposes;
    }

    public PurposeListResponse addPurposesItem(PurposeSummaryDTO purposesItem) {
        if (this.purposes == null) {
            this.purposes = new ArrayList<>();
        }
        this.purposes.add(purposesItem);
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
        PurposeListResponse purposeListResponse = (PurposeListResponse) o;
        return Objects.equals(this.totalResults, purposeListResponse.totalResults) &&
            Objects.equals(this.links, purposeListResponse.links) &&
            Objects.equals(this.purposes, purposeListResponse.purposes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, links, purposes);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PurposeListResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    purposes: ").append(toIndentedString(purposes)).append("\n");
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

