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
import org.wso2.carbon.identity.api.server.vp.template.management.v1.ConnectedIdpItem;
import javax.validation.constraints.*;

/**
 * List of identity provider connections that reference a given presentation definition.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "List of identity provider connections that reference a given presentation definition. ")
public class ConnectedIdpsResponse  {
  
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<ConnectedIdpItem> connectedIdps = null;


    /**
    * Total number of connections referencing this presentation definition.
    **/
    public ConnectedIdpsResponse totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "3", value = "Total number of connections referencing this presentation definition.")
    @JsonProperty("totalResults")
    @Valid
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
    * Index of the first result (1-based).
    **/
    public ConnectedIdpsResponse startIndex(Integer startIndex) {

        this.startIndex = startIndex;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "Index of the first result (1-based).")
    @JsonProperty("startIndex")
    @Valid
    public Integer getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    /**
    * Number of connections returned in this response.
    **/
    public ConnectedIdpsResponse count(Integer count) {

        this.count = count;
        return this;
    }
    
    @ApiModelProperty(example = "3", value = "Number of connections returned in this response.")
    @JsonProperty("count")
    @Valid
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
    * The connections that reference this presentation definition.
    **/
    public ConnectedIdpsResponse connectedIdps(List<ConnectedIdpItem> connectedIdps) {

        this.connectedIdps = connectedIdps;
        return this;
    }
    
    @ApiModelProperty(value = "The connections that reference this presentation definition.")
    @JsonProperty("connectedIdps")
    @Valid
    public List<ConnectedIdpItem> getConnectedIdps() {
        return connectedIdps;
    }
    public void setConnectedIdps(List<ConnectedIdpItem> connectedIdps) {
        this.connectedIdps = connectedIdps;
    }

    public ConnectedIdpsResponse addConnectedIdpsItem(ConnectedIdpItem connectedIdpsItem) {
        if (this.connectedIdps == null) {
            this.connectedIdps = new ArrayList<>();
        }
        this.connectedIdps.add(connectedIdpsItem);
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
        ConnectedIdpsResponse connectedIdpsResponse = (ConnectedIdpsResponse) o;
        return Objects.equals(this.totalResults, connectedIdpsResponse.totalResults) &&
            Objects.equals(this.startIndex, connectedIdpsResponse.startIndex) &&
            Objects.equals(this.count, connectedIdpsResponse.count) &&
            Objects.equals(this.connectedIdps, connectedIdpsResponse.connectedIdps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, startIndex, count, connectedIdps);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConnectedIdpsResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    startIndex: ").append(toIndentedString(startIndex)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    connectedIdps: ").append(toIndentedString(connectedIdps)).append("\n");
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

