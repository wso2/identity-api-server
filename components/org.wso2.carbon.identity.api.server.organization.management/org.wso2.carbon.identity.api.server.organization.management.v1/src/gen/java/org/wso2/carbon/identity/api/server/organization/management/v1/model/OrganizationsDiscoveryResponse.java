/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationDiscoveryResponse;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class OrganizationsDiscoveryResponse  {
  
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<Link> links = null;

    private List<OrganizationDiscoveryResponse> organizations = null;


    /**
    **/
    public OrganizationsDiscoveryResponse totalResults(Integer totalResults) {

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
    public OrganizationsDiscoveryResponse startIndex(Integer startIndex) {

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
    public OrganizationsDiscoveryResponse count(Integer count) {

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
    public OrganizationsDiscoveryResponse links(List<Link> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"href\":\"/api/server/v1/organizations/discovery?filter=attributes.type+eq+emailDomain&limit=10&offset=50\",\"rel\":\"next\"},{\"href\":\"/api/server/v1/organizations/discovery?filter=attributes.type+eq+emailDomain&limit=10&offset=30\",\"rel\":\"previous\"}]", value = "")
    @JsonProperty("links")
    @Valid
    public List<Link> getLinks() {
        return links;
    }
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public OrganizationsDiscoveryResponse addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public OrganizationsDiscoveryResponse organizations(List<OrganizationDiscoveryResponse> organizations) {

        this.organizations = organizations;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("organizations")
    @Valid
    public List<OrganizationDiscoveryResponse> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(List<OrganizationDiscoveryResponse> organizations) {
        this.organizations = organizations;
    }

    public OrganizationsDiscoveryResponse addOrganizationsItem(OrganizationDiscoveryResponse organizationsItem) {
        if (this.organizations == null) {
            this.organizations = new ArrayList<>();
        }
        this.organizations.add(organizationsItem);
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
        OrganizationsDiscoveryResponse organizationsDiscoveryResponse = (OrganizationsDiscoveryResponse) o;
        return Objects.equals(this.totalResults, organizationsDiscoveryResponse.totalResults) &&
            Objects.equals(this.startIndex, organizationsDiscoveryResponse.startIndex) &&
            Objects.equals(this.count, organizationsDiscoveryResponse.count) &&
            Objects.equals(this.links, organizationsDiscoveryResponse.links) &&
            Objects.equals(this.organizations, organizationsDiscoveryResponse.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, startIndex, count, links, organizations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class OrganizationsDiscoveryResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    startIndex: ").append(toIndentedString(startIndex)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    organizations: ").append(toIndentedString(organizations)).append("\n");
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

