/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.policy.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyListItem;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyListLink;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PolicyListResponse  {
  
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<PolicyListItem> policies = null;

    private List<PolicyListLink> links = null;


    /**
    * Number of results that match the listing operation.
    **/
    public PolicyListResponse totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "42", value = "Number of results that match the listing operation.")
    @JsonProperty("totalResults")
    @Valid
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
    * Index of the first element of the page, which will be equal to offset + 1.
    **/
    public PolicyListResponse startIndex(Integer startIndex) {

        this.startIndex = startIndex;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "Index of the first element of the page, which will be equal to offset + 1.")
    @JsonProperty("startIndex")
    @Valid
    public Integer getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    /**
    * Number of elements in the returned page.
    **/
    public PolicyListResponse count(Integer count) {

        this.count = count;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "Number of elements in the returned page.")
    @JsonProperty("count")
    @Valid
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
    * Page of policy summaries.
    **/
    public PolicyListResponse policies(List<PolicyListItem> policies) {

        this.policies = policies;
        return this;
    }
    
    @ApiModelProperty(value = "Page of policy summaries.")
    @JsonProperty("policies")
    @Valid
    public List<PolicyListItem> getPolicies() {
        return policies;
    }
    public void setPolicies(List<PolicyListItem> policies) {
        this.policies = policies;
    }

    public PolicyListResponse addPoliciesItem(PolicyListItem policiesItem) {
        if (this.policies == null) {
            this.policies = new ArrayList<>();
        }
        this.policies.add(policiesItem);
        return this;
    }

        /**
    * Pagination links (next/previous).
    **/
    public PolicyListResponse links(List<PolicyListLink> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(value = "Pagination links (next/previous).")
    @JsonProperty("links")
    @Valid
    public List<PolicyListLink> getLinks() {
        return links;
    }
    public void setLinks(List<PolicyListLink> links) {
        this.links = links;
    }

    public PolicyListResponse addLinksItem(PolicyListLink linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
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
        PolicyListResponse policyListResponse = (PolicyListResponse) o;
        return Objects.equals(this.totalResults, policyListResponse.totalResults) &&
            Objects.equals(this.startIndex, policyListResponse.startIndex) &&
            Objects.equals(this.count, policyListResponse.count) &&
            Objects.equals(this.policies, policyListResponse.policies) &&
            Objects.equals(this.links, policyListResponse.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, startIndex, count, policies, links);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PolicyListResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    startIndex: ").append(toIndentedString(startIndex)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    policies: ").append(toIndentedString(policies)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

