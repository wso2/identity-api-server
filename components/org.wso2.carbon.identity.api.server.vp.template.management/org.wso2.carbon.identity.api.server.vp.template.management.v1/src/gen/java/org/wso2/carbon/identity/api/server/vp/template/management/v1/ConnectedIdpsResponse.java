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
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Response model for listing IDPs that reference a presentation definition.
 */
public class ConnectedIdpsResponse {

    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<ConnectedIdpItem> connectedIdps;

    @ApiModelProperty(value = "Total number of IDPs referencing this presentation definition.")
    @JsonProperty("totalResults")
    public Integer getTotalResults() {

        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {

        this.totalResults = totalResults;
    }

    @ApiModelProperty(value = "Index of the first result (1-based).")
    @JsonProperty("startIndex")
    public Integer getStartIndex() {

        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {

        this.startIndex = startIndex;
    }

    @ApiModelProperty(value = "Number of IDPs returned in this response.")
    @JsonProperty("count")
    public Integer getCount() {

        return count;
    }

    public void setCount(Integer count) {

        this.count = count;
    }

    @ApiModelProperty(value = "List of IDPs referencing this presentation definition.")
    @JsonProperty("connectedIdps")
    public List<ConnectedIdpItem> getConnectedIdps() {

        return connectedIdps;
    }

    public void setConnectedIdps(List<ConnectedIdpItem> connectedIdps) {

        this.connectedIdps = connectedIdps;
    }
}
