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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * List response model for presentation definitions.
 */
@ApiModel(description = "List of presentation definitions")
public class PresentationDefinitionList {

    private Integer totalResults;
    private List<PresentationDefinitionListItem> presentationDefinitions = new ArrayList<>();

    @ApiModelProperty(value = "Total number of presentation definitions.")
    @JsonProperty("totalResults")
    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    @ApiModelProperty(value = "List of presentation definitions.")
    @JsonProperty("presentationDefinitions")
    public List<PresentationDefinitionListItem> getPresentationDefinitions() {
        return presentationDefinitions;
    }

    public void setPresentationDefinitions(List<PresentationDefinitionListItem> presentationDefinitions) {
        this.presentationDefinitions = presentationDefinitions;
    }
}
