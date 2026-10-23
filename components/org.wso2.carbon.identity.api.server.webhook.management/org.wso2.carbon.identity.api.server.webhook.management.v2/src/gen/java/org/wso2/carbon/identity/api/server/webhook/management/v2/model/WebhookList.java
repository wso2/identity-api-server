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

package org.wso2.carbon.identity.api.server.webhook.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.Link;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookSummary;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;

public class WebhookList  {
  
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<Link> links = null;

    private List<WebhookSummary> webhooks = null;


    /**
    * Total number of webhooks available.
    **/
    public WebhookList totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "12", value = "Total number of webhooks available.")
    @JsonProperty("totalResults")
    @Valid
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
    * Index of the first record in this response.
    **/
    public WebhookList startIndex(Integer startIndex) {

        this.startIndex = startIndex;
        return this;
    }
    
    @ApiModelProperty(example = "0", value = "Index of the first record in this response.")
    @JsonProperty("startIndex")
    @Valid
    public Integer getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    /**
    * Number of records returned in this response.
    **/
    public WebhookList count(Integer count) {

        this.count = count;
        return this;
    }
    
    @ApiModelProperty(example = "12", value = "Number of records returned in this response.")
    @JsonProperty("count")
    @Valid
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
    * Navigation links. Contains &#x60;next&#x60; and &#x60;previous&#x60; where applicable.
    **/
    public WebhookList links(List<Link> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(value = "Navigation links. Contains `next` and `previous` where applicable.")
    @JsonProperty("links")
    @Valid
    public List<Link> getLinks() {
        return links;
    }
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public WebhookList addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<Link>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public WebhookList webhooks(List<WebhookSummary> webhooks) {

        this.webhooks = webhooks;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("webhooks")
    @Valid
    public List<WebhookSummary> getWebhooks() {
        return webhooks;
    }
    public void setWebhooks(List<WebhookSummary> webhooks) {
        this.webhooks = webhooks;
    }

    public WebhookList addWebhooksItem(WebhookSummary webhooksItem) {
        if (this.webhooks == null) {
            this.webhooks = new ArrayList<WebhookSummary>();
        }
        this.webhooks.add(webhooksItem);
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
        WebhookList webhookList = (WebhookList) o;
        return Objects.equals(this.totalResults, webhookList.totalResults) &&
            Objects.equals(this.startIndex, webhookList.startIndex) &&
            Objects.equals(this.count, webhookList.count) &&
            Objects.equals(this.links, webhookList.links) &&
            Objects.equals(this.webhooks, webhookList.webhooks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, startIndex, count, links, webhooks);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WebhookList {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    startIndex: ").append(toIndentedString(startIndex)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    webhooks: ").append(toIndentedString(webhooks)).append("\n");
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

