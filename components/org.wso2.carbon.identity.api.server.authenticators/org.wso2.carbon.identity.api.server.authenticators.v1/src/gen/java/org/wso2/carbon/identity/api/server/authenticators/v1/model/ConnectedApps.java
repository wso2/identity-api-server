/*
 * Copyright (c) 2023-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.authenticators.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.ConnectedApp;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.Link;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ConnectedApps  {
  
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<Link> links = null;
    private List<ConnectedApp> connectedApps = null;


    /**
    **/
    public ConnectedApps totalResults(Integer totalResults) {

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
    public ConnectedApps startIndex(Integer startIndex) {

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
    public ConnectedApps count(Integer count) {

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
    public ConnectedApps links(List<Link> links) {

        this.links = links;
        return this;
    }

    @ApiModelProperty(example = "[{\"href\":\"identity-provider/123e4567-e89b-12d3-a456-556642440000/connected-apps?offset=50&limit=10\",\"rel\":\"next\"},{\"href\":\"identity-provider/123e4567-e89b-12d3-a456-556642440000/connected-apps?offset=30&limit=10\",\"rel\":\"previous\"}]", value = "")
    @JsonProperty("links")
    @Valid
    public List<Link> getLinks() {
        return links;
    }
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public ConnectedApps addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public ConnectedApps connectedApps(List<ConnectedApp> connectedApps) {

        this.connectedApps = connectedApps;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("connectedApps")
    @Valid
    public List<ConnectedApp> getConnectedApps() {
        return connectedApps;
    }
    public void setConnectedApps(List<ConnectedApp> connectedApps) {
        this.connectedApps = connectedApps;
    }

    public ConnectedApps addConnectedAppsItem(ConnectedApp connectedAppsItem) {
        if (this.connectedApps == null) {
            this.connectedApps = new ArrayList<>();
        }
        this.connectedApps.add(connectedAppsItem);
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
        ConnectedApps connectedApps = (ConnectedApps) o;
        return Objects.equals(this.totalResults, connectedApps.totalResults) &&
            Objects.equals(this.startIndex, connectedApps.startIndex) &&
            Objects.equals(this.count, connectedApps.count) &&
            Objects.equals(this.links, connectedApps.links) &&
            Objects.equals(this.connectedApps, connectedApps.connectedApps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, startIndex, count, links, connectedApps);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConnectedApps {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    startIndex: ").append(toIndentedString(startIndex)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    connectedApps: ").append(toIndentedString(connectedApps)).append("\n");
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

