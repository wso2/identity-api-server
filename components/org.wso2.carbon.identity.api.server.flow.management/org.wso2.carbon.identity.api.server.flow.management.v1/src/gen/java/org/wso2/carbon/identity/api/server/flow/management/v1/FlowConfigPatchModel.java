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
import javax.validation.constraints.NotNull;

@ApiModel(description = "Patch model for configurations of a flow type")
public class FlowConfigPatchModel {

    private String flowType;
    private Boolean isEnabled;
    private Boolean isAutoLoginEnabled;

    /**
     * Flow type
     **/
    public FlowConfigPatchModel flowType(String flowType) {

        this.flowType = flowType;
        return this;
    }

    @ApiModelProperty(example = "REGISTRATION", required = true, value = "Flow type")
    @JsonProperty("flowType")
    @Valid
    @NotNull(message = "Property flowType cannot be null.")

    public String getFlowType() {

        return flowType;
    }

    public void setFlowType(String flowType) {

        this.flowType = flowType;
    }

    /**
     * Indicate whether the orchestration is enabled for the flow
     **/
    public FlowConfigPatchModel isEnabled(Boolean isEnabled) {

        this.isEnabled = isEnabled;
        return this;
    }

    @ApiModelProperty(example = "true", value = "Indicate whether the orchestration is enabled for the flow")
    @JsonProperty("isEnabled")
    @Valid
    public Boolean getIsEnabled() {

        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {

        this.isEnabled = isEnabled;
    }

    /**
     * Indicate whether the auto login is enabled for the flow
     **/
    public FlowConfigPatchModel isAutoLoginEnabled(Boolean isAutoLoginEnabled) {

        this.isAutoLoginEnabled = isAutoLoginEnabled;
        return this;
    }

    @ApiModelProperty(example = "true", value = "Indicate whether the auto login is enabled for the flow")
    @JsonProperty("isAutoLoginEnabled")
    @Valid
    public Boolean getIsAutoLoginEnabled() {

        return isAutoLoginEnabled;
    }

    public void setIsAutoLoginEnabled(Boolean isAutoLoginEnabled) {

        this.isAutoLoginEnabled = isAutoLoginEnabled;
    }


    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowConfigPatchModel flowConfigPatchModel = (FlowConfigPatchModel) o;
        return Objects.equals(this.flowType, flowConfigPatchModel.flowType) &&
                Objects.equals(this.isEnabled, flowConfigPatchModel.isEnabled) &&
                Objects.equals(this.isAutoLoginEnabled, flowConfigPatchModel.isAutoLoginEnabled);
    }

    @Override
    public int hashCode() {

        return Objects.hash(flowType, isEnabled, isAutoLoginEnabled);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowConfigPatchModel {\n");

        sb.append("    flowType: ").append(toIndentedString(flowType)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    isAutoLoginEnabled: ").append(toIndentedString(isAutoLoginEnabled)).append("\n");
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

