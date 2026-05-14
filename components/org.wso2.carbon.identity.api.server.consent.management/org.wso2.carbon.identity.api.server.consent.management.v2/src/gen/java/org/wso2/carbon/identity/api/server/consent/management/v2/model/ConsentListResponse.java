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
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ConsentSummaryDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.PaginationLink;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ConsentListResponse  {
  
    private Integer totalResults;
    private List<PaginationLink> links = null;

    private List<ConsentSummaryDTO> consents = null;


    /**
    * Number of results returned in the current page
    **/
    public ConsentListResponse totalResults(Integer totalResults) {

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
    public ConsentListResponse links(List<PaginationLink> links) {

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

    public ConsentListResponse addLinksItem(PaginationLink linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    * List of consent records
    **/
    public ConsentListResponse consents(List<ConsentSummaryDTO> consents) {

        this.consents = consents;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"id\":\"f83aa1a3-5d4d-4c0e-84db-c3a4f1e6c8b2\",\"subjectId\":\"alice@wso2.com\",\"serviceId\":\"admin-dashboard\",\"state\":\"ACTIVE\",\"timestamp\":1744000000000,\"expiryTime\":1766383796000},{\"id\":\"c2d3e4f5-2345-6789-bcde-f01234567891\",\"subjectId\":\"bob@wso2.com\",\"serviceId\":\"mobile-app\",\"state\":\"PENDING\",\"timestamp\":1744000100000,\"expiryTime\":null}]", value = "List of consent records")
    @JsonProperty("Consents")
    @Valid
    public List<ConsentSummaryDTO> getConsents() {
        return consents;
    }
    public void setConsents(List<ConsentSummaryDTO> consents) {
        this.consents = consents;
    }

    public ConsentListResponse addConsentsItem(ConsentSummaryDTO consentsItem) {
        if (this.consents == null) {
            this.consents = new ArrayList<>();
        }
        this.consents.add(consentsItem);
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
        ConsentListResponse consentListResponse = (ConsentListResponse) o;
        return Objects.equals(this.totalResults, consentListResponse.totalResults) &&
            Objects.equals(this.links, consentListResponse.links) &&
            Objects.equals(this.consents, consentListResponse.consents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, links, consents);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConsentListResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    consents: ").append(toIndentedString(consents)).append("\n");
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

