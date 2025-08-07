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
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

public class FlowGenerateResultData {

    private List<Step> steps = null;

    private String error;

    /**
     *
     **/
    public FlowGenerateResultData steps(List<Step> steps) {

        this.steps = steps;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("steps")
    @Valid
    public List<Step> getSteps() {

        return steps;
    }

    public void setSteps(List<Step> steps) {

        this.steps = steps;
    }

    public FlowGenerateResultData addStepsItem(Step stepsItem) {

        if (this.steps == null) {
            this.steps = new ArrayList<Step>();
        }
        this.steps.add(stepsItem);
        return this;
    }

    /**
     *
     **/
    public FlowGenerateResultData error(String error) {

        this.error = error;
        return this;
    }

    @ApiModelProperty(example = "Error occurred while generating the flow", value = "")
    @JsonProperty("error")
    @Valid
    public String getError() {

        return error;
    }

    public void setError(String error) {

        this.error = error;
    }


    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowGenerateResultData flowGenerateResultData = (FlowGenerateResultData) o;
        return Objects.equals(this.steps, flowGenerateResultData.steps) &&
                Objects.equals(this.error, flowGenerateResultData.error);
    }

    @Override
    public int hashCode() {

        return Objects.hash(steps, error);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowGenerateResultData {\n");

        sb.append("    steps: ").append(toIndentedString(steps)).append("\n");
        sb.append("    error: ").append(toIndentedString(error)).append("\n");
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

