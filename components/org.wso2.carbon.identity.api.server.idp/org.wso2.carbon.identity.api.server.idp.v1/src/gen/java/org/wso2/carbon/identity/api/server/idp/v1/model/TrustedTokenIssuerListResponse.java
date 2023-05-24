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

package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.Link;
import org.wso2.carbon.identity.api.server.idp.v1.model.TrustedTokenIssuerListItem;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class TrustedTokenIssuerListResponse  {
  
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<Link> links = null;

    private List<TrustedTokenIssuerListItem> identityProviders = null;


    /**
    **/
    public TrustedTokenIssuerListResponse totalResults(Integer totalResults) {

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
    public TrustedTokenIssuerListResponse startIndex(Integer startIndex) {

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
    public TrustedTokenIssuerListResponse count(Integer count) {

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
    public TrustedTokenIssuerListResponse links(List<Link> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"href\":\"trusted-token-issuers?offset=50&limit=10\",\"rel\":\"next\"},{\"href\":\"trusted-token-issuers?offset=30&limit=10\",\"rel\":\"previous\"}]", value = "")
    @JsonProperty("links")
    @Valid
    public List<Link> getLinks() {
        return links;
    }
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public TrustedTokenIssuerListResponse addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public TrustedTokenIssuerListResponse identityProviders(List<TrustedTokenIssuerListItem> identityProviders) {

        this.identityProviders = identityProviders;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("identityProviders")
    @Valid
    public List<TrustedTokenIssuerListItem> getIdentityProviders() {
        return identityProviders;
    }
    public void setIdentityProviders(List<TrustedTokenIssuerListItem> identityProviders) {
        this.identityProviders = identityProviders;
    }

    public TrustedTokenIssuerListResponse addIdentityProvidersItem(TrustedTokenIssuerListItem identityProvidersItem) {
        if (this.identityProviders == null) {
            this.identityProviders = new ArrayList<>();
        }
        this.identityProviders.add(identityProvidersItem);
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
        TrustedTokenIssuerListResponse trustedTokenIssuerListResponse = (TrustedTokenIssuerListResponse) o;
        return Objects.equals(this.totalResults, trustedTokenIssuerListResponse.totalResults) &&
            Objects.equals(this.startIndex, trustedTokenIssuerListResponse.startIndex) &&
            Objects.equals(this.count, trustedTokenIssuerListResponse.count) &&
            Objects.equals(this.links, trustedTokenIssuerListResponse.links) &&
            Objects.equals(this.identityProviders, trustedTokenIssuerListResponse.identityProviders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, startIndex, count, links, identityProviders);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class TrustedTokenIssuerListResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    startIndex: ").append(toIndentedString(startIndex)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    identityProviders: ").append(toIndentedString(identityProviders)).append("\n");
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

