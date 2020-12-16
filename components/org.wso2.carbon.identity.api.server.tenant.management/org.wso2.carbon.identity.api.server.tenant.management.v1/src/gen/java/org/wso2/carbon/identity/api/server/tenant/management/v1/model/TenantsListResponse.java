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

package org.wso2.carbon.identity.api.server.tenant.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.Link;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantListItem;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class TenantsListResponse  {
  
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<Link> links = null;

    private List<TenantListItem> tenants = null;


    /**
    **/
    public TenantsListResponse totalResults(Integer totalResults) {

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
    public TenantsListResponse startIndex(Integer startIndex) {

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
    public TenantsListResponse count(Integer count) {

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
    public TenantsListResponse links(List<Link> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"href\":\"/api/server/v1/tenants?offset=50&limit=10\",\"rel\":\"next\"},{\"href\":\"/api/server/v1/tenants?offset=30&limit=10\",\"rel\":\"previous\"}]", value = "")
    @JsonProperty("links")
    @Valid
    public List<Link> getLinks() {
        return links;
    }
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public TenantsListResponse addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public TenantsListResponse tenants(List<TenantListItem> tenants) {

        this.tenants = tenants;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("tenants")
    @Valid
    public List<TenantListItem> getTenants() {
        return tenants;
    }
    public void setTenants(List<TenantListItem> tenants) {
        this.tenants = tenants;
    }

    public TenantsListResponse addTenantsItem(TenantListItem tenantsItem) {
        if (this.tenants == null) {
            this.tenants = new ArrayList<>();
        }
        this.tenants.add(tenantsItem);
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
        TenantsListResponse tenantsListResponse = (TenantsListResponse) o;
        return Objects.equals(this.totalResults, tenantsListResponse.totalResults) &&
            Objects.equals(this.startIndex, tenantsListResponse.startIndex) &&
            Objects.equals(this.count, tenantsListResponse.count) &&
            Objects.equals(this.links, tenantsListResponse.links) &&
            Objects.equals(this.tenants, tenantsListResponse.tenants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, startIndex, count, links, tenants);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class TenantsListResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    startIndex: ").append(toIndentedString(startIndex)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    tenants: ").append(toIndentedString(tenants)).append("\n");
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

