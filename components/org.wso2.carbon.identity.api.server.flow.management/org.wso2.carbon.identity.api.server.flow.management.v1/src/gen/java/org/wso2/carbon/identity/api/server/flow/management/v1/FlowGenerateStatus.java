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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

import javax.validation.Valid;

@ApiModel(description = "Status response for generating a flow")
public class FlowGenerateStatus {

    private Boolean optimizingQuery;
    private Boolean fetchingSamples;
    private Boolean generatingFlow;
    private Boolean completed;

    /**
     *
     **/
    public FlowGenerateStatus optimizingQuery(Boolean optimizingQuery) {

        this.optimizingQuery = optimizingQuery;
        return this;
    }

    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("optimizingQuery")
    @Valid
    public Boolean getOptimizingQuery() {

        return optimizingQuery;
    }

    public void setOptimizingQuery(Boolean optimizingQuery) {

        this.optimizingQuery = optimizingQuery;
    }

    /**
     *
     **/
    public FlowGenerateStatus fetchingSamples(Boolean fetchingSamples) {

        this.fetchingSamples = fetchingSamples;
        return this;
    }

    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("fetchingSamples")
    @Valid
    public Boolean getFetchingSamples() {

        return fetchingSamples;
    }

    public void setFetchingSamples(Boolean fetchingSamples) {

        this.fetchingSamples = fetchingSamples;
    }

    /**
     *
     **/
    public FlowGenerateStatus generatingFlow(Boolean generatingFlow) {

        this.generatingFlow = generatingFlow;
        return this;
    }

    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("generatingFlow")
    @Valid
    public Boolean getGeneratingFlow() {

        return generatingFlow;
    }

    public void setGeneratingFlow(Boolean generatingFlow) {

        this.generatingFlow = generatingFlow;
    }

    /**
     *
     **/
    public FlowGenerateStatus completed(Boolean completed) {

        this.completed = completed;
        return this;
    }

    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("completed")
    @Valid
    public Boolean getCompleted() {

        return completed;
    }

    public void setCompleted(Boolean completed) {

        this.completed = completed;
    }


    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowGenerateStatus flowGenerateStatus = (FlowGenerateStatus) o;
        return Objects.equals(this.optimizingQuery, flowGenerateStatus.optimizingQuery) &&
                Objects.equals(this.fetchingSamples, flowGenerateStatus.fetchingSamples) &&
                Objects.equals(this.generatingFlow, flowGenerateStatus.generatingFlow) &&
                Objects.equals(this.completed, flowGenerateStatus.completed);
    }

    @Override
    public int hashCode() {

        return Objects.hash(optimizingQuery, fetchingSamples, generatingFlow, completed);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowGenerateStatus {\n");

        sb.append("    optimizingQuery: ").append(toIndentedString(optimizingQuery)).append("\n");
        sb.append("    fetchingSamples: ").append(toIndentedString(fetchingSamples)).append("\n");
        sb.append("    generatingFlow: ").append(toIndentedString(generatingFlow)).append("\n");
        sb.append("    completed: ").append(toIndentedString(completed)).append("\n");
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

